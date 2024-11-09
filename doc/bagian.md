# Bagian API Spec

## Create Bagian

Endpoint : POST /api/bagian

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

- idBagian : BAG1

```json
{
  "idBagian": "id bagian",
  "namaBagian": "nama bagian",
  "singkatanBagian": "singkatan bagian",
  "createdDate": "12-09-2024 14:00:00",
  "updatedDate": "12-09-2024 14:00:00"
}
```

Response Body (Success) :

```json
{
  "status": true,
  "data": "$namaBagian berhasil ditambahkan"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```

## Get Daftar Bagian

Endpoint : GET /api/bagian

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": [
    {
      "namaBagian": "nama bagian",
      "singkatanBagian": "singkatan bagian",
      "createdDate": "12-09-2024 14:00:00",
      "updatedDate": "12-09-2024 14:00:00"
    },
    {
      "namaBagian": "nama bagian",
      "singkatanBagian": "singkatan bagian",
      "createdDate": "12-09-2024 14:00:00",
      "updatedDate": "12-09-2024 14:00:00"
    }
  ]
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```

## Get Bagian

Endpoint : GET /api/bagian/{idBagian}

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": {
      "namaBagian": "nama bagian",
      "singkatanBagian": "singkatan bagian",
      "createdDate": "12-09-2024 14:00:00",
      "updatedDate": "12-09-2024 14:00:00"
    }
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```

## Update Bagian

Endpoint : PATCH /api/bagian/{idBagian}

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

- namaBagian: Optional
- singkatanBagian: Optional

```json
{
  "namaBagian": "nama bagian",
  "singkatanBagian": "singkatan bagian",
  "updatedDate": "12-09-2024 14:00:00"
}
```

Response Body (Success) :

```json
{
  "status": true,
  "data": "$namaBagian berhasil diupdate"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```

## Remove Bagian

Endpoint : DELETE /api/bagian/{idBagian}

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": "$namaBagian berhasil dihapus"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```