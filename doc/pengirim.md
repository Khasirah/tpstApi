# Pengirim API Spec

## Create Pengirim

Endpoint : POST /api/pengirim

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

- idPengirim : PNG1

```json
{
  "idPengirim": "id pengirim",
  "namaPengirim": "nama pengirim",
  "createdDate": "12-09-2024 14:00:00",
  "updatedDate": "12-09-2024 14:00:00"
}
```

Response Body (Success) :

```json
{
  "status": true,
  "data": "$namaPengirim berhasil ditambahkan"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```

## Get List Pengirim 

Endpoint : GET /api/pengirim

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": [
    {
      "idPengirim": "id pengirim",
      "namaPengirim": "nama pengirim",
      "createdDate": "12-09-2024 14:00:00"
    },
    {
      "idPengirim": "id pengirim",
      "namaPengirim": "nama pengirim",
      "createdDate": "12-09-2024 14:00:00"
    }
  ],
  "paging": {
    "currentPage": 0,
    "totalPage": 10,
    "size": 10
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

## Search Pengirim

Endpoint : GET /api/pengirim/search

Query Param :

- namaPengirim : String, pengirim namaPengirim, menggunakan like query

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": [
    {
      "idPengirim": "id Pengirim",
      "namaPengirim": "nama Pengirim"
    },
    {
      "idPengirim": "id Pengirim",
      "namaPengirim": "nama Pengirim"
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

## Remove Pengirim

Endpoint : GET /api/pengirim/{idPengirim}

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": "$namaPengirim berhasil dihapus"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```