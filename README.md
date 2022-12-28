# AccountService

Description:

API structure with role model, business logic, security, and persistance.



Endpoints:

Authentication:


POST api/auth/signup allows the user to register on the service;

POST api/auth/changepass changes a user password.

Business functionality:


GET api/empl/payment gives access to the employee's payrolls;

POST api/acct/payments uploads payrolls;

PUT api/acct/payments updates payment information.

Service functionality:


PUT api/admin/user/role changes user roles;

DELETE api/admin/user deletes a user;

GET api/admin/user displays information about all users.

Auditor:


GET api/security/events returns all security events(login attempts, role change etc..)
