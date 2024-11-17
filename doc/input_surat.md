# Input Surat API Spec

## Input Surat

Endpoint : GET /api/inputSurat/bagianAndEkspedisi

Request Header : 

- X-API-TOKEN : Token (Required)

Response Body (Success) :

```json
{
  "data": {
    "bagianList": [
      {
        "id": "integer",
        "namaBagian": "string",
        "singkatanBagian": "string",
        "createdDate": "string",
        "updatedDate": "string"
      }
    ],
    "ekspedisiList": [
      {
        "id": "integer",
        "namaEkspedisi": "string",
        "createdDate": "string",
        "updatedDate": "string"
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