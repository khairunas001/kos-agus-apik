# 🔐 AUTH API Documentation

## 1. Login User ✅

**POST /kos-agus/api/auth/login**

### Request Body
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

### Response (200 OK)
```json
{
  "status": "200 OK",
  "data": {
    "token": "generated-jwt-token",
    "expiredAt": "2025-01-01T10:00:00Z"
  }
}
```

### Response (400 Bad Request – wrong credentials)
```json
{
  "status": "400 BAD_REQUEST",
  "errors": "Invalid username or password"
}
```

---

## 2. Logout (Single Device) ✅

**DELETE /kos-agus/api/auth/logout**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)

### Response (200 OK)
```json
{
  "status": "200 OK",
  "data": null
}
```

### Response (401 Unauthorized)
```json
{
  "status": "401 UNAUTHORIZED",
  "errors": "Token is invalid or expired"
}
```

---

## 3. Logout All Devices ✅

**DELETE /kos-agus/api/auth/logout-all-device**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)

### Response (200 OK)
```json
{
  "status": "200 OK",
  "data": "Deleted data success"
}
```

### Response (401 Unauthorized)
```json
{
  "status": "401 UNAUTHORIZED",
  "errors": "Token invalid"
}
```