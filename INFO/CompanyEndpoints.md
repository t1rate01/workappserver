# Companyn endpoint ohjeet  

### Kaikki työntekijät - /api/company/workers  
  
Method: Get  
Odottaa auth tokenia, ja sen perusteella jos käyttäjän luvat riittää, antaa  
json muotoisen palautuksen työntekijöistä ja tietoja.  

### Lisää työntekijä preapproved listalle - /api/workers/add  
  
Method: Post  
Odottaa auth tokenia, ja json muodossa: 
```json
{
    "email":"test@test.com",
    "role":" ROOLI "
}
```
  
Vastaa 200 OK jos onnistuu.  
  
### Poista työntekijä companystä ja preapproved listalta  - api/company/workers/{userId}
  
Method: Delete  
Odottaa auth tokenia ja pathvariablena userId, joka pitää poistaa.  
Autentikoi, tarkistaa roolin ja poistaa userin ja siihen sidotun emailin preapproved listalta.  
200 OK vastaus.  
  
### Päivitä työntekijän rooli - api/company/workers/{userID}  
  
Method: Put  
Odottaa auth tokenia, pathvariablena userId ja json bodyna rooli,  
autentikoi, tarkistaa oikeudet ja päivittää roolin sekä useriin että preapproved listalle.  
200 OK vastaus.  
  
### Listaa yrityksen hyväksytyt sähköpostit - /api/company/workers/email  
  
Method: Get  
Odottaa auth tokenia, rooli vähintään supervisor. Palauttaa listan esihyväksytyistä sähköposteista  
ja muiden tietojen mukana myös Boolean onko sähköpostilla rekisteröity käyttäjä.  
  
### Lisää esihyväksytty sähköposti - /api/company/workers/add  
  
Method: Post  
Odottaa auth tokenia ja JSONia jossa email ja role. Vähintään supervisor, hakee tokenista  
lisääjän companyn ja liittää lisätyn emailin siihen. Vastaus 200 OK "Email added".  
  
### Poista työntekijä (ja esihyväksytty sähköposti) - /api/company/workers/{userId}  
  
Method: Delete  
Odottaa auth tokenia ja urliparametriä, jossa poistettavan userin id (saat sen /api/company/workers vastauksesta)  
Vain Master roolille. Jos sähköpostilla on rekisteröity, käyttäjäkin poistetaan. Vastaus 200 OK "User deleted".  
  
### Poista esihyväksytty sähköposti (ja työntekijä, jos maililla on rekisteröity) - /api/company/workers/email/{email}  
  
Method: Delete  
Odottaa auth tokenia ja urliparametrina poistettavaa sähköpostia. Vain master roolille. Jos maililla on rekisteröity,  
poistetaan myös käyttäjä. Vastaus 200 OK "Email deleted".  
  
### Päivitä työntekijän rooli - /api/company/workers/{userID}  
  
Method: Put  
Odottaa auth tokenia, urliparametrinä kohteen userID ja JSON muodossa uusi role.  
Vain supervisor ja master, supervisor ei voi antaa master oikeuksia.  
Vastaus 200 OK "Role updated".  
  
### Päivitä/muokkaa esihyväksyttyä sähköpostia - /api/company/workers/email/{oldEmail}  
  
Method: Put  
Odottaa auth tokenia, urliparametrinä kohteen VANHA sähköposti, JSON muodossa uusi sähköposti  
ja halutessa uusi rooli. Hox, älä laita roolia jos sitä ei muuteta.  
Roolia päivittäessä supervisor ei voi antaa master roolia.  
Vastaus 200 OK "Email updated".  
  
### Muokkaa companyn settingsejä - /api/company/settings  
  
Method: Put  
Odottaa auth tokenia, JSON muodossa uusi/uudet asetukset "settings":"{"asetus1":"asetus", jne}  
Vain master, vastaus 200 OK "Settings updated".  
Voi laittaa halutessaan myös companynamen vaihtumaan.
  
```jsonesimerkkejä
{
  "settings": {
    "setting1": "muutettusetting",
    "uusisetting": "uusiasetus"
  }
}
  
{
  "companyname":"muutettuname",
  "settings": {
    "setting2": "muutettukakkossetting",
    "uusisetting": "uusiasetusmuutettu",
    "vieläuudempisetting":"sikauusasetus"
  }
}

```
  
### Hae companyn settings - /api/company/settings  
  
Method: Get  
Odottaa auth tokenia, hakee tokeninomistajan perusteella yrityksen asetukset.  
Vastaus 200 OK ja settings JSON.  
