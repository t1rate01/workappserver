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
  
### Poista työntekijä companystä ja preapproved listalta  - /workers/{userId}
  
Method: Delete  
Odottaa auth tokenia ja pathvariablena userId, joka pitää poistaa.  
Autentikoi, tarkistaa roolin ja poistaa userin ja siihen sidotun emailin preapproved listalta.  
200 OK vastaus.  
  
### Päivitä työntekijän rooli - /workers/{userID}  
  
Method: Put  
Odottaa auth tokenia, pathvariablena userId ja bodyna rooli,  
autentikoi, tarkistaa oikeudet ja päivittää roolin sekä useriin että preapproved listalle.  
200 OK vastaus.  
