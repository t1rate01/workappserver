# Muiden vuororaporttien ja työvuorolistojen päivitys info:  
  
## /api/report/company  
Method: GET  
Odottaa tokenia, rooli vähintään supervisor, palauttaa yrityksen kaikki raportoidut  
vuorot kaikilta työntekijöiltä.  

## /api/report/others  
Method: GET  
Odottaa tokenia, palauttaa kuin ylhäällä mutta jättää pois käyttäjän omat raportoidut vuorot.  

## /api/shifts/others  
Method: GET  
Odottaa tokenia, palauttaa kaikki TULEVAT määrätyt työvuorot(työvuorolista), paitsi käyttäjän omat.  
  
## /api/shifts/others/all  
Method: GET  
Odottaa tokenia, palauttaa kaikki menneet ja tulevat työvuorolistat, paitsi käyttäjän omat.  