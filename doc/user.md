# User API Spec

## Create User (Admin)

Endpoint : POST /api/users

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

```json
{
  "idUser": "id petugas",
  "namaUser": "nama petugas",
  "password": "password",
  "idBagian": "idBagian",
  "idKelompok": "idKelompok"
}
```

Response Body (Success) :

```json
{
  
  "data": "$namaPetugas berhasil didaftarkan"
}
```

Response Body (Failed) :

```json
{
  
  "errors": "Unauthorized"
}
```

## Create User using csv (Admin)

Endpoint : POST api/users/upload

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

```json
{
  "csvFile": "file.csv"
}
```

Response Body (Success) :

```json
{
  
  "data": "$total users berhasil didaftarkan"
}
```

Response Body (Failed) :

```json
{
  
  "errors": "Unauthorized"
}
```

## Get User

Endpoint : GET /api/users/current

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "data": {
    "idUser": "idUser",
    "namaUser": "namaUser",
    "idBagian": "idBagian",
    "idKelompok": "idKelompok"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized"
}
```

## Get User (Admin)

Endpoint : GET /api/users/{idUser}

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "data": {
    "idUser": "id petugas",
    "namaUser": "nama petugas",
    "idBagian": "idBagian",
    "idKelompok": "idKelompok"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized"
}
```
## Get List Users (Admin)

Endpoint : GET /api/users/

Query Param :

- idUser : String, User idUser, using like query, optional
- namaUser : String, User namaUser, using like query, optional
- page : Integer, start from 0, default 0
- size : Integer, default 20

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  
  "data": [
    {
      "idUser": "id petugas",
      "namaUser": "nama petugas",
      "idBagian": "idBagian",
      "status": "online",
      "createdDate": "12-09-2024 14:00:00",
      "updatedDate": "12-09-2024 14:00:00"
    },
    {
      "idUser": "id petugas",
      "namaUser": "nama petugas",
      "idBagian": "idBagian",
      "status": "online",
      "createdDate": "12-09-2024 14:00:00",
      "updatedDate": "12-09-2024 14:00:00"
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

## Update User (Admin)

Endpoint : PATCH /api/users/{idUser}

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

- nama User: optional
- password: optional
- id Bagian: optional
- id kelompok: optional

```json
{
  "namaUser": "nama petugas",
  "password": "password",
  "idBagian": "idBagian"
}
```

Response Body (Success) :

```json
{
  
  "data": "berhasil update data"
}
```

Response Body (Failed) :

```json
{
  
  "errors": "Unauthorized"
}
```

## Update User

Endpoint : PATCH /api/users/current

Request Header :

- X-API-TOKEN : Token (Wajib)

Request Body :

- nama User: optional
- password: optional
- id Bagian: optional
- id kelompok: optional

```json
{
  "namaUser": "nama petugas",
  "password": "password",
  "idBagian": "idBagian",
  "updatedDate": "12-09-2024 14:00:00"
}
```

Response Body (Success) :

```json
{
  
  "data": "berhasil update data"
}
```

Response Body (Failed) :

```json
{
  
  "errors": "Unauthorized"
}
```

## Hapus User

Endpoint : DELETE /api/users/{idUser}

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  
  "data": "$namaUser berhasil dihapus"
}
```

Response Body (Failed) :

```json
{
  
  "errors": "Unauthorized"
}
```