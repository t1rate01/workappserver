# Tekoälyn antama endpoint dokumentaatio tähänastisista

# User Registration Endpoint
POST /api/register  
  
Register a new user with the required details.  
  
Request Body: RegisterDTO  
  
email (string): The email address of the new user.  
password (string): The password for the new user.  
firstName (string): The first name of the new user.  
lastName (string): The last name of the new user.  
phoneNumber (string): The phone number of the new user.  
Response:  
  
200 OK: User created successfully. Response body: "User created successfully"  
400 Bad Request: User creation failed. Response body: Error message string.  
500 Internal Server Error: An unexpected error occurred. Response body: "An unexpected error occurred"  
Example Request:  
´´´{
  "email": "jane.doe@example.com",
  "password": "SecureP@ssw0rd",
  "firstName": "Jane",
  "lastName": "Doe",
  "phoneNumber": "1234567890"
}
´´´
# User Login Endpoint  
POST /api/login  
  
Allows a user to login with email and password using Basic Auth.  
  
Headers:  
  
Authorization: The Basic Auth string prefixed with Basic and followed by base64 encoded email and password.  
Response:  
  
200 OK: Login successful. Response body: LoginResponse object.  
400 Bad Request: Error in Basic Auth format. Response body: Error message string.  
401 Unauthorized: Missing Basic Auth format or Unauthorized access. Response body: Error message string.  
Example Header:  
´´´Authorization: Basic amFuZS5kb2VAZXhhbXBsZS5jb206U2VjdXJlUEBzc3cwcmQ=´´´
  
# Access Token Refresh Endpoint  
POST /api/refresh  
  
Refreshes the access token using a refresh token.  
  
Headers:  
  
Authorization: The Bearer token string prefixed with Bearer and followed by the refresh token.  
Response:  
  
200 OK: Token refreshed successfully. Response body: New access token string.  
401 Unauthorized: Invalid refresh token. Response body: Error message string.  
  
# Add Work Shift Endpoint  
POST /api/report/add  
  
Allows the user to add a work shift.  
  
Request Body: WorkDayDTO  
  
date (string): The date of the work shift.  
startTime (string): The start time of the shift.  
endTime (string): The end time of the shift.  
breaksTotal (integer): The total break time in minutes.  
description (string): A description of the work shift.  
Headers:  
  
Authorization: The Bearer token string prefixed with Bearer followed by the user's access token.  
Response:  

200 OK: Shift added successfully. Response body: WorkDay object with shift details.  
400 Bad Request: Shift addition failed. Response body: Error message string.  
401 Unauthorized: Unauthorized access. Response body: Error message string.  
Example Request:  
´´´{
  "date": "2023-11-08",
  "startTime": "09:00",
  "endTime": "17:00",
  "breaksTotal": 30,
  "description": "Regular shift"
}
´´´
  
# Get User Work Shifts Endpoint  
GET /api/report/personal  
  
Retrieves the list of work shifts for the authenticated user.  
  
Headers:  
  
Authorization: The Bearer token string prefixed with Bearer followed by the user's access token.  
Response:  
  
200 OK: Successfully retrieved the list of work shifts. Response body: List of WorkDay objects.  
400 Bad Request: Error occurred in retrieving shifts. Response body: Error message string.  
401 Unauthorized: Unauthorized access. Response body: Error message string.  