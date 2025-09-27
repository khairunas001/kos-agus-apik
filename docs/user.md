## 🔹 Endpoints

### 1. Get All Users
**`GET /users`**

📄 Response:
```json
[
  {
    "id": "uuid-1",
    "username": "john_doe",
    "name": "John Doe",
    "nik": "1234567890",
    "phone": "08123456789",
    "email": "john@example.com",
    "roles": "customers"
  }
]
```

### 2. Get User by ID

**`GET /users/{id}`**

📄 Response:
```json
{
"id": "uuid-1",
"username": "john_doe",
"name": "John Doe",
"nik": "1234567890",
"phone": "08123456789",
"email": "john@example.com",
"roles": "customers"
}
```

### 3. Create User

**`POST /users`**

📥 Request Body:
```json
{
"username": "jane_doe",
"password": "hashed_password",
"name": "Jane Doe",
"nik": "9876543210",
"phone": "081298765432",
"email": "jane@example.com",
"roles": "customers"
}
```
📄 Response (201 Created):
```json
{
"message": "User created successfully",
"data": {
    "id": "uuid-2",
    "username": "jane_doe",
    "name": "Jane Doe",
    "nik": "9876543210",
    "phone": "081298765432",
    "email": "jane@example.com",
    "roles": "customers"
  }
}
```

### 4. Update User

**`PUT /users/{id}`**

📥 Request Body:

```json
{
"name": "Jane A. Doe",
"phone": "081211223344",
"email": "jane_new@example.com",
"roles": "admin"
}
```

📄 Response:
```json
{
"message": "User updated successfully",
"data": {
    "id": "uuid-2",
    "username": "jane_doe",
    "name": "Jane A. Doe",
    "nik": "9876543210",
    "phone": "081211223344",
    "email": "jane_new@example.com",
    "roles": "admin"
  }
}

```

### 5. Delete User

**`DELETE /users/{id}`**

📄 Response:

```json
{
  "message": "User deleted successfully"
}
```
🔹 Error Response Format
```json
{
  "status": "error",
  "message": "Username already exists"
}
```
