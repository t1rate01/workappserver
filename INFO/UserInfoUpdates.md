# Käyttäjän tietojen päivitys endpoint ohjeet  
  
### Käyttäjän omien tietojen päivitys (ei salasanan) -  /api/user/update  
  
Method: Put  
Odottaa auth tokenia, ja JSONIA jossa muokkaukseen menevät kentät.  
Muokattavissa olevat kentät on: 
```jsonesimerkki
{
  "email":"uusisähköposti@test.com",
  "firstName":"Mestari",
  "lastName":"Mahtavin",
  "phoneNumber":"0700123123"
}
```
  
Vastaus 200 OK Updated successfully.  

### Toisen käyttäjän tietojen muokkaus (vain master) - /api/user/update/{userId}  
  
Method: Put  
Odottaa auth tokenia, ja JSONIA jossa samat vaihtoehdot kuten edellä, mutta lisäksi voi muuttaa  
"role":"WORKER" tai SUPERVISOR tai MASTER. Oman roolin vaihtamista ei hyväksytä.  
Vältytään siltä että omistaja alentaa itse itsensä vahingossa.  
Vastaus 200 OK Updated Succesfully.  

