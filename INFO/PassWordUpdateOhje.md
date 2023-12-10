# Ohjeet salasanan päivityksille.   
  
### Oman salasanan päivitys - /api/user/update/password  
  
Method: Put  
Odottaa auth tokenia, json muodossa newPassword. Onnistuessaan nollaa käyttäjän tokenit, vaadi sisäänkirjaus uusiksi.  
Vastaus 200 OK "Password updated successfully".  
  
### Toisen salasanan päivitys, vain master - /api/user/update/password/{userId}  
  
Method: Put  
Odottaa auth tokenia, urliparametrinä kohteen userId, json muodossa newPassword.  
Vain master roolin käytettävissä. Nollaa käyttäjän tokenit onnistuessaan.  
Vastaus 200 OK "Targets password updated successfully".  
  
