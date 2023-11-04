# SecurityService Functions

## Public Methods

### register(String email, String password, String firstname, String lastname)
- Parameters: email (String), password (String), firstname (String), lastname (String)
- Returns: User

### register(String email, String password, String firstname, String lastname, String phoneNumber)
- Parameters: email (String), password (String), firstname (String), lastname (String), phoneNumber (String)
- Returns: User

### login(String email, String password)
- Parameters: email (String), password (String)
- Returns: LoginResponse

### isEmailApproved(String email)
- Parameters: email (String)
- Returns: Boolean

### createAccessToken(String email, Role role)
- Parameters: email (String), role (Role)
- Returns: String

### createRefreshToken(String email, Role role)
- Parameters: email (String), role (Role)
- Returns: String

### saveRefreshToken(User user, String refreshToken)
- Parameters: user (User), refreshToken (String)
- Returns: void

### refreshAccessToken(String refreshToken)
- Parameters: refreshToken (String)
- Returns: String

### expireAllTokens(String email)
- Parameters: email (String)
- Returns: String

### checkRoleFromToken(String token)
- Parameters: token (String)
- Returns: Role

### verifyToken(String token)
- Parameters: token (String)
- Returns: String

### verifyTokenAndRole(String token)
- Parameters: token (String)
- Returns: Role

### getUserFromToken(String token)
- Parameters: token (String)
- Returns: User

### getCompanyFromToken(String token)
- Parameters: token (String)
- Returns: Company

### pairAddedEmailToCompanyWithToken(String token, String newEmail)
- Parameters: token (String), newEmail (String)
- Returns: String

### isSuperVisor(Role role)
- Parameters: role (Role)
- Returns: Boolean

### isMaster(Role role)
- Parameters: role (Role)
- Returns: Boolean
