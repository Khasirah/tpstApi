# Dashboard API Spec

## Dashboard

Endpoint : GET /api/dashboard

Request Header : 

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "data": {
    "totalSuratMasuk": "integer",
    "totalSuratAtTPST": "integer",
    "totalPetugas": "integer",
    "totalSuratByPetugasTPST": "integer",
    "newestSuratInbound": [
      {
        "idUser": "string",
        "nomorSurat": "string",
        "perihal": "string",
        "tanggalTerima": "string",
        "idPetugasTPST": "string",
        "namaPetugasTPST": "string"
      },
      {
        "idUser": "string",
        "nomorSurat": "string",
        "perihal": "string",
        "tanggalTerima": "string",
        "idPetugasTPST": "string",
        "namaPetugasTPST": "string"
      }
    ]
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "string"
}
```