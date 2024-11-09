# Auth API Spec

## Login User

Endpoint : POST /api/auth/login

Request Body :

```json
{
  "idUser": "id petugas",
  "password": "password"
}
```

Response Body (Success) :

- expiredAt: millisecond

```json
{
  "data": {
    "token": "TOKEN",
    "expiredAt": 123123343
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "username atau password salah"
}
```

## Logout User

Endpoint : DELETE /api/auth/logout

Request Header :

- X-API-TOKEN : Token (Wajib)

Response Body (Success) :

```json
{
  "data": "berhasil keluar"
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized"
}
```