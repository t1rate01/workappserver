
# Servers planned structure

## Packages and Classes

### Package: com.backend.server.users
- User
- UserRepository


### Package: com.backend.server.companies
- Company
- CompanyRepository
- CompanyService
- CompanyRestController !!
- CompanyApprovedEmails
- CompAppEmailsRepository

### Package: com.backend.server.security
- SecurityService
- SecurityRestController !!  aloitettu
- Encoder
- RefreshToken
- RefreshTokenRepository

### Package: com.backend.server.utility
- Role
- Auditable 'aikaleimaa varten, ei sisällä last_modified_by'  
- AuditableReports 'aikaleimaa varten, sisältää last_modified_by'  
- LoginResponse
- JsonConverter * POISTETTU, jätetty kommentteina


### Package: com.backend.server.shifts !!
- 
- 

### Package: com.backend.server.reportedhours 
- WorkDay
- WorkDayRepository
- WorkDayService
- WorkDayRestController !!!
  
  
### Package: com.backend.server.devices !! (tarpeellisuus?)
- Mietinnässä, koska refreshtoken toteutus pitäisi mahdollistaa useilla eri laitteilla kirjautumisen halutessa ns "Stay logged in" tilassa
- 

