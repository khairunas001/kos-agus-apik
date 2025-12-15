## Transaction API Documentation

### 1. Create Room ✅❌

**`POST /kos-agus/transactions/create`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
- Role = `costumers` (Mandatory)
- create trasaksi, dan auto bikin room jadi on booked

📥 Request Body:

```json
{
  "user_id": "user 1",
  "room_id": "kamar 1",
  "amount": "kamar mandi dalam",
  "period": 1000000,
  "payment_date": "2025-12-09 16:32:55",
  "payment_status": "pending",
  "payment_method" : "cash"
}
```

📄 Response (201 Created):

```json
{
  "status": "201 CREATED",
  "data": {
    "user_id": "user 1",
    "room_id": "kamar 1",
    "amount": "kamar mandi dalam",
    "period": 1000000,
    "payment_date": "2025-12-09 16:32:55",
    "payment_status": "pending",
    "payment_method": "cash"
  }
}

```

### 2. Update Room ✅❌

**`PATCH /kos-agus/transactions/update/{transactions_id}`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
- Role = `admin` (Mandatory)
- update "payment_status": menjadi "success" dan update room_availability menjadi "booked"
- update "payment_status": menjadi "canceled" dan room_availability menjadi "available"

📥 Request Body:

```json
{
  "payment_status": "success"
}
```

📄 Response (201 Created):

```json
{
  "status": "200 OK",
  "data": {
    "user_id": "user 1",
    "room_id": "kamar 1",
    "amount": "kamar mandi dalam",
    "period": 1000000,
    "payment_date": "2025-12-09 16:32:55",
    "payment_status": "success",
    "payment_method": "cash"
  }
}

```

### 2. Update Room ✅❌

**`PATCH /kos-agus/transactions`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
-

📄 Response (201 Created):

```json
{
  "status": "200 OK",
  "data": [
    {
      "user_id": "user 1",
      "room_id": "kamar 1",
      "amount": "kamar mandi dalam",
      "period": 1000000,
      "payment_date": "2025-12-09 16:32:55",
      "payment_status": "success",
      "payment_method": "cash"
    },
    {
      "user_id": "user 1",
      "room_id": "kamar 1",
      "amount": "kamar mandi dalam",
      "period": 1000000,
      "payment_date": "2025-12-09 16:32:55",
      "payment_status": "success",
      "payment_method": "cash"
    }
  ]
}

```