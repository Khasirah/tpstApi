# Posisi Surat API Spec

## Create Posisi Surat

Endpoint : POST /api/posisiSurat

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

- idPosisi : POS1

```json
{
  "idPosisi": "id posisi",
  "keteranganPosisi": "keterangan posisi",
  "createdDate": "12-09-2024 14:00:00",
  "updatedDate": "12-09-2024 14:00:00"
}
```

Response Body (Success) :

```json
{
  "status": true,
  "data": "$keteranganPosisi berhasil ditambahkan"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```

## Get List Posisi Surat

Endpoint : GET /api/posisiSurat

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": [
    {
      "idPosisi": "id posisi",
      "keteranganPosisi": "keterangan posisi",
      "createdDate": "12-09-2024 14:00:00",
      "updatedDate": "12-09-2024 14:00:00"
    },
    {
      "idPosisi": "id posisi",
      "keteranganPosisi": "keterangan posisi",
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

## Update Posisi Surat

Endpoint : GET /api/posisiSurat/{idPosisi}

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

```json
{
  "keteranganPosisi": "keterangan posisi",
  "updatedDate": "12-09-2024 14:00:00"
}
```

Response Body (Success) :

```json
{
  "status": true,
  "data": "${keteranganPosisi} berhasil diubah"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```

## Remove Posisi Surat

Endpoint : GET /api/posisiSurat/{idPosisi}

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "status": true,
  "data": "$keteranganPosisi berhasil dihapus"
}
```

Response Body (Failed) :

```json
{
  "status": false,
  "errors": "Unauthorized"
}
```