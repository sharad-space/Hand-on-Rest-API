Employee Source Service
=======================

Overview
--------
This service reads legacy employee records from an existing "employee" table, transforms them into a canonical format, persists them into the "source_employee" control table, and optionally forwards the payload to an external receiver service.

Primary goals:
- Expose legacy data for inspection and migration
- Persist transformed records in a control table
- Push employee data to a receiver endpoint

Endpoints
---------
- GET /api/source/legacy-employees
  - Returns all rows from the legacy "employee" table (Entity: LegacyEmployee).
  - Uses LegacyEmployeeService.getAllEmployees(), which calls LegacyEmployeeRepository.findAll().

- GET /api/source/employees
  - Returns saved rows from the control table "source_employee" (Entity: SourceEmployee).
  - Uses SourceEmployeeService.getAllEmployees().

- POST /api/source/sync
  - Triggers a sync: reads legacy rows, converts to EmployeeDto, saves to source_employee, and sends payload to receiver.
  - Previously ran within a single @Transactional boundary. Updated behavior (see Changes below).

- POST /api/source/push
  - Accepts a list of EmployeeDto and forwards to the receiver service via EmployeeSyncService.sendToReceiver().

Key Classes and Entities
------------------------
- LegacyEmployee (entity) -> maps to table `employee` (columns: id, name, email, department, salary, phoneNumber, createdAt)
- SourceEmployee (entity) -> maps to table `source_employee`
- LegacyEmployeeService -> reads legacy table, throws ResourceNotFoundException when empty
- SourceEmployeeService -> persists transformed EmployeeDto into source_employee
- EmployeeSyncService -> coordinates legacy->dto->persist->send flow; uses RestTemplate to POST to external receiver

Configuration
-------------
- employee.receiver.url (application.properties / env) — URL the service posts employee payloads to.

Recent Change
-------------
To avoid rolling back persisted data when the network call to the receiver fails, syncAllEmployees() was modified:
- Removed class-level @Transactional on syncAllEmployees() so repository.saveAll() commits before the network call.
- sendToReceiver(...) is invoked after saveAll returns. If the send fails, records remain persisted in source_employee and an ApiException is thrown to notify the caller.

Implications
------------
- Pros: The control table always reflects what was saved locally even if the external push fails.
- Cons: Possible inconsistency between local and receiver data if the external push fails. No automatic retry is currently implemented.

Recommendations / Next Steps
---------------------------
- Implement an outbox pattern: write the payload to an outbox table in the same transaction as saving the source_employee, and have a separate reliable worker read/send/outbox with retries and mark delivered. This guarantees at-least-once delivery without keeping network calls inside DB transactions.
- Add retry logic with backoff when calling the receiver (e.g., Spring Retry or Resilience4j) if immediate reliability is acceptable.
- Add monitoring/metrics and an admin endpoint to re-send failed batches.
- Consider idempotency keys on receiver side to make retries safe.

How to run (dev)
-----------------
1. Build and run with Maven/Gradle as usual (Spring Boot):
   - mvn spring-boot:run
2. Example curl calls:
   - Get legacy rows: curl -s http://localhost:8080/api/source/legacy-employees
   - Trigger sync: curl -X POST INSERT INTO employee (
    name,
    email,
    department,
    salary,
    phone_number,
    created_at
)
VALUES (
    'Sharad Prajapati',
    'sharad.prajapati@example.com',
    'IT',
    75000.00,
    '9876543210',
    NOW()
);
   - Push custom payload: curl -X POST -H "Content-Type: application/json" -d '[{"id":1,"name":"Alice",...}]' http://localhost:8080/api/source/push

Testing / Observability
-----------------------
- Unit tests: add tests around EmployeeSyncService to mock RestTemplate and verify behavior when sendToReceiver succeeds/fails.
- Integration: run with a test receiver (mock server) to validate end-to-end flow and failure modes.

Contact / Ownership
-------------------
- Service maintained in this repository: employee-source-service.

If you want, next steps can be implemented now: add retry with Resilience4j, or implement an outbox table + worker. Which should be implemented first?