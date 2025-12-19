## Transaction API Documentation

### 1. Create Room ✅❌

**`POST /kos-agus/transactions/create`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
- Role = `costumers` (Mandatory)
- create trasaksi, dan auto bikin room jadi on booked
- period = waktu saat ini ditambah 1 bulan? atau user memasukkan jumlah bulan, kumudian waktu saat ini ditambah jumlah bulan yang sudah diikirim user, dan mengalikan amount sesuai dengan bulan

📥 Request Body:

```json
{
  "room_id": "room-1",
  "duration_month": 1,
  "payment_method": "cash"
}
```

📄 Response (201 Created):

```json
{
  "status": "201 CREATED",
  "data": {
    "transactions_id" : "transactions 1",
    "user_id": "user 1",
    "room_id": "kamar 1",
    "amount": 1000000,
    "period": "2025-12-10 16:32:55",
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
    "transactions_id" : "transactions 1",
    "user_id": "user 1",
    "room_id": "kamar 1",
    "amount": 1000000,
    "period": "2025-12-10 16:32:55",
    "payment_date": "2025-12-09 16:32:55",
    "payment_status": "success",
    "payment_method": "cash"
  }
}

```

### 2. get Room ✅❌

**`GET /kos-agus/transactions`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
-

📄 Response (201 Created):

```json
{
  "status": "200 OK",
  "data": [
    {
      "transactions_id" : "transactions 1",
      "user_id": "user 1",
      "room_id": "kamar 1",
      "amount": 1000000,
      "period": "2025-12-10 16:32:55",
      "payment_date": "2025-12-09 16:32:55",
      "payment_status": "success",
      "payment_method": "cash"
    },
    {
      "transactions_id" : "transactions 2",
      "user_id": "user 1",
      "room_id": "kamar 1",
      "amount": 1000000,
      "period": "2025-12-10 16:32:55",
      "payment_date": "2025-12-09 16:32:55",
      "payment_status": "success",
      "payment_method": "cash"
    }
  ]
}

```