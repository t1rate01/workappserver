
# Toiminnot jotka testattu  

- Register toimii oikein, täytyy olla Company ja Company ID sidottu Approved email lisättynä  
sitoo tahdotulla tavalla rekisteröitäessä oikeaan companyyn työntekijän.  
- Login toimii ja vastaus DTO lähettää token, refreshtoken, role, companyname, companysettings{}
- Testattu virheellisiä tilanteita, esim sähköposti ei approved listalla rekisteröintiyritys (servun vastaus tosi siisti)  
testattu väärällä käyttäjätunnuksella ja/tai salasanalla login, vastaus aina 400 unauthrorized ja NULL sisältöä.  

## SERVER LIVE URL  
https://workhoursapp-9a5bdf993d73.herokuapp.com/

# Ohjeita 
- Tietokannassa on *Etunimesi*Company, ja approved email niihin sidottuna *etunimesi*@test.com, 
joilla voi rekisteröityä ja nähdä sen linkittyvän oikeaan companyyn. 

### Rekisteröinti
Komento jolla helppo lisätä consolista oma testifirma ja sille approved email  
```sql
INSERT INTO companies (created_at, updated_at, company_name, settings_json) 
VALUES (current_timestamp, current_timestamp, 'YourCompanyName', '{"setting1":"value1","setting2":"value2"}');
```
Jonka jälkeen hae Companylle tullut ID:  
```sql
SELECT id FROM companies WHERE company_name = 'YourCompanyName';
```
Ja lisää tällä ID:llä uusi sähköposti hyväksytylle listalle  
```sql
INSERT INTO approved_emails (company_id, email, role) 
VALUES (YourCompanyId, 'test@test.com', 'WORKER');
```
Roolit: "WORKER","SUPERVISOR","MASTER"
Nyt voit käyttää /api/register rekisteröidäksesi uuden käyttäjän. Endpoint odottaa JSON bodyä tässä muodossa :
```json
{
    "email": "test@test.com",
    "password": "yourpassword",
    "firstName": "Test",
    "lastName": "User",
    "phoneNumber": "1234567890"
}
```
Puhelinnumero ei ole pakollinen, funktio on overloaded.  
  
### LOGIN  
Login käyttää Base64 muotoa, ei ole turvallinen mutta eipä ole tunnukset sellasenaan ilmassa.  
ThunderClient / Postmanista Auth -> Basic ja usernameen tulee sähköposti.  
Kirjautuminen palauttaa Jsonin muotoa 
```json
{
    "token":"tokensotku",
    "refreshToken":"refreshtokensotku",
    "role": "WORKER",
    "companyname":"TestCompany",
    "companySettings": {
        "testsetting1":"testiasetus1",
        "testsettting2":"testiasetus2"
    }
}
```
companySettings on JSON muodossa tietokannassa, jätin tarkoituksella vapaamuotoiseksi että voi fronttia tehdessä  
kohtuu vapaasti päättää mitä asetuksia sinne tahtoo laittaa. Itse mietin esim logourl ja vastaavia.  

### Muita alueita on aloitettu/tehty muttei testattu
