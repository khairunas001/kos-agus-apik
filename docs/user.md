## 🔹 Endpoints

### 1. Create User ✅

**`POST /kos-agus/users/register`**

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
  "status": "201 CREATED",
  "data": [
    {
      "username": "jane_doe",
      "name": "Jane Doe",
      "nik": "9876543210",
      "phone": "081298765432",
      "email": "jane@example.com",
      "roles": "customers"
    },
    {
      "username": "john_doe",
      "name": "John Doe",
      "nik": "1234567890",
      "phone": "081234567890",
      "email": "john@example.com",
      "roles": "customers"
    }
  ]
}
```

### 2. Gel All User ✅

**`POST /kos-agus/users`**

📄 Response (200 OK):
```json
{
  "status": "200 OK",
  "data": {
    "id": "uuid-2",
    "username": "jane_doe",
    "name": "Jane Doe",
    "phone": "081298765432",
    "email": "jane@example.com",
    "roles": "customers"
  }
}
```
