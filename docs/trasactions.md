## Transaction API Documentation

### 1. Create Room ✅❌

**`POST /kos-agus/transactions/create`**

### Request Header
- `X-KOS-AGUS-API-TOKEN`: Token aktif (Mandatory)
- Role = `costumers` (Mandatory)

### create trasaksi, dan auto bikin room jadi on booked

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



# delete room, dan auto bikin room jadi avbailable lagi

# update room, dan auto bikin room jadi avbailable lagi jika batal dan booked jika berhasil

# get room, tampilkan semua data