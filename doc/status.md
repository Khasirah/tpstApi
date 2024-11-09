# Status API Spec

## Create Status

Endpoint : POST /api/status

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

- idStatus : STAT1

```json
{
  "idStatus": "id status",
  "status": "status",
  "createdDate": "12-09-2024 14:00:00",
  "updatedDate": "12-09-2024 14:00:00"
}
```

Response Body (Success) :

```json
{
  "status": true,
  "data": "$status berhasil ditambahkan"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```

## Get List Status

Endpoint : GET /api/status

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": [
    {
      "idStatus": "id status",
      "status": "status",
      "createdDate": "12-09-2024 14:00:00",
      "updatedDate": "12-09-2024 14:00:00"
    },
    {
      "idStatus": "id status",
      "status": "status",
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

## Update Status

Endpoint : GET /api/status/{idStatus}

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

```json
{
  "status": "status",
  "updatedDate": "12-09-2024 14:00:00"
}
```

Response Body (Success) :

```json
{
  "status": true,
  "data": "${status} berhasil diubah"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```

## Remove Status

Endpoint : GET /api/status/{idStatus}

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": "$status berhasil dihapus"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```