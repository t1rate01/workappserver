# workappserver
 Backend for workhours app



Toteutuksia:  

Loginissa saadaan 2 tokenia, 1 lyhyt ja 1 pitkäkestonen, lyhytkestosella pääsee toimintoihin.  
Pitkäkestonen tallennetaan laitteelle ja tietokantaan, ja jos frontilla epäonnistuu login lyhyellä  
tokenilla (on vanhentunut), frontti saa 401 vastauksen, jolloin frontin täytyy yrittää refreshata  
token lähettämällä pitkäkestonen token, jonka servu autentikoi ja onnistuessa lähettää uuden tokenin.  
Ei tarvitse laittaa kirjautumaan uudestaan eikä tarvitse pitää pitkäikäistä accesstokenia.  
Tähän jäi miettimättä isommin että mitä jos "Stay logged in" oliskin vaihtoehto...
  
Kolme roolitasoa users: WORKER, SUPERVISOR, MASTER.  
Tarkoitus että Worker näkee ja editoi vain omat tietonsa.  
Supervisor voi antaa vuoroja ja nähdä muiden tietoja.  
Master em. lisäksi lisätä approved emails.  
  
ApprovedEmails listaan verrataan kun yritetään rekisteröityä (tapahtuu email ja salasana).  
Listalta löytyessä kytketään työntekijä sitten sillä tiedolla suoraan oikeaan companyyn.  
Emailit aina uniikkeja.  

Alla suunnitelmaa, huutomerkillä merkatut on tekemättä tai isosti kesken.
Käytetty Lombok-kirjastoa kontstruktoreiden, getterien ja setterien generointiin  
jotta koodi pysyy luettavampana.  
  

Database MySQL, logiikka:  
Company, jossa onetomany suhde Users ja CompanyApprovedEmails 
Users, jolla manytoone suhde Company, ja TODO merkattu shifts ja työaikaraportit  
Shifts, joilla manytoone suhde Users. TODO  
ReportedHours, joilla manytoone suhde Users. TODO  



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
- Encoder
- RefreshToken
- RefreshTokenRepository

### Package: com.backend.server.utility
- Role
- Auditable
- AuditableReports
- LoginResponse
- JsonConverter


### Package: com.backend.server.shifts !!
- 
- 

### Package: com.backend.server.reportedhours !!  
- 
-  
  
  
### Package: com.backend.server.devices !! (tarpeellisuus?)
- Mietinnässä, koska refreshtoken toteutus pitäisi mahdollistaa useilla eri laitteilla kirjautumisen halutessa ns "Stay logged in" tilassa
- 



