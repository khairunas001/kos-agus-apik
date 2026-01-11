## Transaction API Documentation

### 1. Create Transaction ✅

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
    "id": "3e8f515a-00e6-4055-8eb5-ef3cadd46385",
    "user_id": "9adf92ae-b9b8-4574-bafa-4ed4881bbf56",
    "room": {
      "id": "63f63277-5854-49ed-8c07-9bba15b9c304",
      "title": "kamar 9999",
      "availability": "booked",
      "details": "kamar mandi dalam",
      "price": 1000000
    },
    "amount": 4000000,
    "period": "2026-04-22",
    "payment_date": "2025-12-22T10:45:27.5986642",
    "payment_status": "pending",
    "payment_method": "cash"
  }
}

```

### 2. Update Transaction (Transaction confirmation) ✅ 

**`PATCH /kos-agus/transactions/update/{transactions_id}`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
- Role = `admin` (Mandatory)
- update "payment_status": menjadi "paid" dan update room_availability menjadi "booked"
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
    "room": {
      "id": "63f63277-5854-49ed-8c07-9bba15b9c304",
      "title": "kamar 9999",
      "availability": "booked",
      "details": "kamar mandi dalam",
      "price": 1000000
    },
    "amount": 1000000,
    "period": "2025-12-10 16:32:55",
    "payment_date": "2025-12-09 16:32:55",
    "payment_status": "paid",
    "payment_method": "cash"
  }
}

```

### 3. get All Transaction ✅ 

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
      "room": {
        "id": "63f63277-5854-49ed-8c07-9bba15b9c304",
        "title": "kamar 9999",
        "availability": "booked",
        "details": "kamar mandi dalam",
        "price": 1000000
      },
      "amount": 1000000,
      "period": "2025-12-10 16:32:55",
      "payment_date": "2025-12-09 16:32:55",
      "payment_status": "paid",
      "payment_method": "cash"
    },
    {
      "transactions_id" : "transactions 2",
      "user_id": "user 2",
      "room": {
        "id": "asdasdasd",
        "title": "kamar 9999",
        "availability": "booked",
        "details": "kamar mandi luar",
        "price": 1000000
      },
      "amount": 1000000,
      "period": "2025-12-10 16:32:55",
      "payment_date": "2025-12-09 16:32:55",
      "payment_status": "paid",
      "payment_method": "cash"
    }
  ]
}

```


### 4. get All Transaction Base user who have it ✅ -kurang testing❌ finish

**`GET /kos-agus/transactions/histories`**

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
      "user_id": "user 77",
      "room": {
        "id": "63f63277-5854-49ed-8c07-9bba15b9c304",
        "title": "kamar 9999",
        "availability": "booked",
        "details": "kamar mandi dalam",
        "price": 1000000
      },
      "amount": 1000000,
      "period": "2025-12-10 16:32:55",
      "payment_date": "2025-12-09 16:32:55",
      "payment_status": "paid",
      "payment_method": "cash"
    },
    {
      "transactions_id" : "transactions 2",
      "user_id": "user 77",
      "room": {
        "id": "asdasdasd",
        "title": "kamar 9999",
        "availability": "booked",
        "details": "kamar mandi luar",
        "price": 1000000
      },
      "amount": 1000000,
      "period": "2025-12-10 16:32:55",
      "payment_date": "2025-12-09 16:32:55",
      "payment_status": "paid",
      "payment_method": "cash"
    }
  ]
}

```