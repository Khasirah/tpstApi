# Surat API Spec

## Create Surat

Endpoint : POST /api/surat

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

```json
{
  "data": {
    "nomorSurat": "",
    "namaPengirim": "nama Pengirim",
    "perihal": "",
    "idEkspedisi": "id ekspedisi",
    "nomorSeriEkspedisi": "",
    "kontakPengirim": "",
    "idPetugas": "id petugas",
    "idTujuanBagian": "id bagian"
  },
  "pdfFile": {
    "contentType": "application/pdf",
    "file": "file"
  }
}
```

Response Body (Success) :

```json
{
  "data": "$nomorSurat berhasil ditambahkan"
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized need login first"
}
```

## Get List Surat

Endpoint : GET /api/surat

Request Param :

- tahun : Integer, Surat created_date, using year query, default this year
- page : Integer, start from 0, default 0
- size : Integer, default 20

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "data": [
    {
      "idSurat": "",
      "nomorSurat": "",
      "namaPengirim": "",
      "perihal": "",
      "tanggalTerima": "",
      "idPetugasTpst": "petugas tpst",
      "namaPetugas": ""
    },
    {
      "idSurat": "",
      "nomorSurat": "",
      "namaPengirim": "",
      "perihal": "",
      "tanggalTerima": "",
      "idPetugasTpst": "petugas tpst",
      "namaPetugas": ""
    }
  ],
  "paging": {
    "currentPage": 0,
    "totalPage": 0,
    "size": 20
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized"
}
```

## Get Specific Surat

Endpoint : GET /api/surat/{idSurat}

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "data": {
    "id": "",
    "nomorSurat": "",
    "namaPengirim": "",
    "perihal": "",
    "namaBagian": "",
    "posisiSurat": "",
    "namaEkspedisi": "",
    "nomorSeriEkspedisi": "",
    "kontak": "",
    "namaPetugasTpst": "",
    "tanggalTerimaBagian": "",
    "penerimaBagian": "",
    "namaBerkas": "",
    "status": "",
    "tanggalTerimaSurat": ""
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized"
}
```

## Update Surat

Endpoint : PATCH /api/surat/{idSurat}

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

- idPosisi : optional
- nomorSeriEkspedisi : optional
- kontakPengirim : optional
- tanggalTerimaBidang : optional
- idPenerimaBidang : optional
- namaBerkas : optional

```json
{
  "data": {
    "idPosisi": "id posisi",
    "nomorSeriEkspedisi": "",
    "kontakPengirim": "",
    "tanggalTerimaBidang": "",
    "penerimaBidang": "id petugas",
    "namaBerkas": ""
  },
  "pdfFile": "pdfFile"
}
```

Response Body (Success) :

```json
{
  "data": "$nomorSurat berhasil diubah"
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized"
}
```

## Delete Surat (Admin)

Endpoint : DELETE /api/surat/{idSurat}

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "data": "$nomorSurat berhasil dihapus"
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized"
}
```