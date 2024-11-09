# Ekspedisi API Spec

## Create Ekspedisi

Endpoint : POST /api/ekspedisi

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

- idEkspedisi : EKS1

```json
{
  "idEkspedisi": "id ekspedisi",
  "namaEkspedisi": "nama ekspedisi",
  "createdDate": "12-09-2024 14:00:00",
  "updatedDate": "12-09-2024 14:00:00"
}
```

Response Body (Success) :

```json
{
  "status": true,
  "data": "$namaEkspidisi berhasil ditambahkan"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```

## Get List Ekspeidisi

Endpoint : GET /api/ekspedisi

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": [
    {
      "idEkspedisi": "id ekspedisi",
      "namaEkspedisi": "nama ekspedisi",
      "createdDate": "12-09-2024 14:00:00"
    },
    {
      "idEkspedisi": "id ekspedisi",
      "namaEkspedisi": "nama ekspedisi",
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

## Search Ekspedisi

Endpoint : GET /api/ekspedisi/search

Query Param :

- namaEkspedisi : String, ekspedisi namaEkspedisi, menggunakan like query

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": [
    {
      "idEkspedisi": "id ekspedisi",
      "namaEkspedisi": "nama ekspedisi"
    },
    {
      "idEkspedisi": "id ekspedisi",
      "namaEkspedisi": "nama ekspedisi"
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

## Remove Ekspedisi

Endpoint : GET /api/ekspedisi/{idEkspedisi}

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": "$namaEkspedisi berhasil dihapus"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```