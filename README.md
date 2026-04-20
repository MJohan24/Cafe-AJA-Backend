# CafeAJA - Backend Service (Spring MVC)

Backend service untuk aplikasi kasir CafeAJA. Service ini menangani katalog menu, checkout Midtrans Snap, webhook status pembayaran, riwayat transaksi, dan inventaris.

## Gambaran Sistem

Sistem dipisah menjadi dua aplikasi:

- Frontend Flutter: dijalankan di emulator, desktop, atau perangkat mobile.
- Backend Spring MVC: dijalankan terpisah via Docker Compose.

Komponen utama backend:

- Spring Boot Web + Validation
- Spring Data JPA
- MySQL 8
- Midtrans Sandbox (Snap + webhook)

## API Design

Base path utama API:

- `/api/v1`

Untuk kompatibilitas transisi, endpoint lama berbasis `/api` masih tersedia.

## Daftar Endpoint

### Health

- `GET /health`
- `GET /api/v1/health`

### POS & Checkout

- `GET /api/v1/menu`
- `POST /api/v1/payments/initiate`
- `POST /api/v1/payments/webhook`
- `GET /api/v1/transactions/today`
- `GET /api/v1/inventory`

## Contoh Kontrak Initiate Payment

Request:

```json
{
  "customer_name": "Walk-in",
  "items": [
    {
      "menu_id": 1,
      "name": "Espresso",
      "quantity": 2,
      "price": 18000
    }
  ]
}
```

Respons:

```json
{
  "order_id": "CAFEAJA-20260421103010-901",
  "snap_token": "xxxx",
  "redirect_url": "https://app.sandbox.midtrans.com/snap/v2/vtweb/xxxx",
  "status": "PENDING"
}
```

## Struktur Repository

```text
backend_cafe_aja/
├── Dockerfile
├── docker-compose.yml
├── .env.example
├── pom.xml
└── src/main/java/com/cafeflow/
    ├── config/
    ├── controllers/
    ├── dto/
    ├── entities/
    ├── repositories/
    └── services/
```

## Menjalankan Service

### 1) Siapkan Environment

```bash
copy .env.example .env
```

Set minimal variabel berikut di file `.env`:

- `MIDTRANS_SERVER_KEY=SB-Mid-server-xxxxxxxxxxxxxxxx`

### 2) Jalankan dengan Docker

```bash
docker compose --env-file .env up --build
```

### 3) Verifikasi Health Check

```bash
curl http://localhost:8080/health
curl http://localhost:8080/api/v1/health
```

## Catatan Midtrans Webhook

Untuk testing lokal, Midtrans tidak bisa mengakses localhost langsung. Gunakan tunnel (contoh: ngrok) lalu arahkan notifikasi Midtrans ke endpoint:

- `https://<tunnel-domain>/api/v1/payments/webhook`

## Integrasi dengan Frontend Flutter

Frontend berada di folder lain:

- `D:/buat project sendiri/cafe_aja`

Dokumen kontrak API untuk frontend:

- `D:/buat project sendiri/cafe_aja/docs/api_spec.md`
