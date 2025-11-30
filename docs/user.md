## USER API Documentation

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

### 2. Gel All User ✅

**`GET /kos-agus/users`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)

📄 Response (200 OK):

```json
{
  "status": "200 OK",
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

### 3. Gel Current User ✅

**`GET /kos-agus/users/current`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)

📄 Response (200 OK):

```json
{
  "status": "200 OK",
  "data": {
    "username": "jane_doe",
    "name": "Jane Doe",
    "nik": "9876543210",
    "phone": "081298765432",
    "email": "jane@example.com",
    "roles": "customers"
  }
}
```

### 4. Update User ✅

**`PUT /kos-agus/users/{id}`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)

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
