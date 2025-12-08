## ROOM API Documentation

### 1. Create Room ✅

**`POST /kos-agus/rooms/create`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
- Role = `admin` (Mandatory)

📥 Request Body:

```json
{
  "title": "kamar 1",
  "availability": "available",
  "details": "kamar mandi dalam",
  "price": 1000000
}
```

📄 Response (201 Created):

```json
{
  "status": "201 CREATED",
  "data": {
    "id": "uuid-2",
    "title": "kamar 1",
    "availability": "available",
    "details": "kamar mandi dalam",
    "price": 1000000
  }
}

```
### 2. Delete room ✅


**`DELETE /kos-agus/rooms/delete/{room_id}`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
- Role = `admin` (Mandatory)

📄 Response (200 OK):

```json
{
  "status": "200 OK",
  "data": "room deleted successfully"
}

```

### 3. Update Room ❌

**`PATCH /kos-agus/rooms/update/{room_id}`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
- Role = `admin` (Mandatory)

📥 Request Body:

```json
{
  "title": "kamar 3",
  "availability": "booked",
  "details": "kamar mandi luar",
  "price": 1500000
}
```

📄 Response (201 Created):

```json
{
  "status": "201 CREATED",
  "data": {
    "id": "uuid-2",
    "title": "kamar 3",
    "availability": "booked",
    "details": "kamar mandi luar",
    "price": 1500000
  }
}

```

### 4. Get All Room -kurang test❌

**`GET /kos-agus/rooms`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)

📄 Response (200 OK):

```json
{
  "status": "200 OK",
  "data": [
    {
      "id": "uuid-2",
      "title": "kamar 3",
      "availability": "booked",
      "details": "kamar mandi luar",
      "price": 1500000
    },
    {
      "id": "uuid-2",
      "title": "kamar 1",
      "availability": "available",
      "details": "kamar mandi dalam",
      "price": 1000000
    }
  ]
}

```

### 5. Get Room ❌

**`GET /kos-agus/rooms/{room_id}`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
- Role = `admin` (Mandatory)

📄 Response (200 OK):

```json
{
  "status": "200 OK",
  "data": {
    "id": "uuid-2",
    "title": "kamar 3",
    "availability": "booked",
    "details": "kamar mandi luar",
    "price": 1500000
  }
}

```