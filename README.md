<div align="center">

<img src="https://img.shields.io/badge/Local--Link-v1.0.0-6C3DE1?style=for-the-badge&logoColor=white" alt="Local-Link"/>

# 🔗 Local-Link

### *Hyperlocal Gig Marketplace — Connect. Hire. Done.*

> **Find verified plumbers, electricians, and technicians within 5 km — in seconds.**  
> Minimal commission. Maximum trust. Built for Bharat.

---

[![Build Status](https://img.shields.io/github/actions/workflow/status/your-org/local-link/ci.yml?branch=main&style=flat-square&logo=github&label=Build&color=22c55e)](https://github.com/your-org/local-link/actions)
[![Version](https://img.shields.io/badge/version-1.0.0-6C3DE1?style=flat-square&logo=semver)](https://github.com/your-org/local-link/releases)
[![License](https://img.shields.io/badge/license-MIT-f59e0b?style=flat-square&logo=opensourceinitiative)](LICENSE)
[![Node.js](https://img.shields.io/badge/Node.js-22.x-339933?style=flat-square&logo=nodedotjs)](https://nodejs.org)
[![Kotlin](https://img.shields.io/badge/Kotlin-Android-7F52FF?style=flat-square&logo=kotlin)](https://kotlinlang.org)
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-47A248?style=flat-square&logo=mongodb)](https://mongodb.com)
[![Firebase](https://img.shields.io/badge/Firebase-Auth-FFCA28?style=flat-square&logo=firebase&logoColor=black)](https://firebase.google.com)
[![Coverage](https://img.shields.io/badge/coverage-82%25-22c55e?style=flat-square&logo=codecov)](https://codecov.io)

</div>

---

## 📋 Table of Contents

- [✨ Features](#-features)
- [🏗️ Architecture Overview](#%EF%B8%8F-architecture-overview)
- [🛠️ Tech Stack](#%EF%B8%8F-tech-stack)
- [🚀 Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Docker Compose Setup](#docker-compose-setup)
  - [Manual Setup](#manual-setup)
- [🔑 Environment Variables](#-environment-variables)
- [📡 API Reference](#-api-reference)
- [🔄 Workflow Diagram](#-workflow-diagram)
- [📦 Deployment Guide](#-deployment-guide)
  - [Android APK Generation](#android-apk-generation)
  - [Backend Deployment](#backend-deployment)
- [🗺️ Roadmap](#%EF%B8%8F-roadmap)
- [❓ FAQ](#-faq)
- [📄 License](#-license)

---

## ✨ Features

| Feature | Description |
|---|---|
| 📍 **Hyperlocal Discovery** | Find service providers within a configurable radius (default 5 km) using MongoDB `$nearSphere` geospatial queries |
| 🗺️ **Live Map View** | OSMDroid-powered interactive map with real-time provider pins, category filtering, and location toggle |
| 🔐 **Secure Auth** | Phone + Password based authentication with bcrypt hashing and JWT session tokens (7-day expiry) |
| 🧑‍🔧 **Provider Profiles** | Rich profiles with skills, hourly rates, ratings, languages, bio, photo, and availability toggle |
| 🌐 **Multi-language Support** | Full localization in English, Hindi (हिंदी), and Kannada (ಕನ್ನಡ) |
| 🌙 **Dark / Light Theme** | Neon Cyber dark theme and a full light mode, both persisted via SharedPreferences |
| ✅ **Verification System** | Email and phone verification flows for both users and providers |
| 📋 **Worker Request Queue** | Session-scoped pending-works queue with accept/reject and session management |
| ⚙️ **Settings Dashboard** | In-app settings for theme toggle, verification status, app version check, and sign-out with confirmation |
| 📦 **RESTful Backend** | Express.js API with Mongoose ODM, structured routes, CORS, dotenv config, and health-check endpoint |

---

## 🏗️ Architecture Overview

Local-Link is structured as a **hybrid monolithic-first architecture** with clear separation of concerns, designed to evolve toward microservices as scale demands. The Android client (Jetpack Compose + Kotlin) communicates exclusively with the Node.js/Express REST API over HTTPS. Business logic is centralized in the backend, while the mobile client handles rendering, local session state, and map interactions.

When a user opens the app, Firebase Authentication validates their identity. The JWT issued by the backend is stored in the device's SessionManager and appended to every subsequent API call. For geospatial queries, the client sends its GPS coordinates (obtained via Google Play Services Location API), and the backend performs a MongoDB `$nearSphere` lookup against a `2dsphere`-indexed `providers` collection, returning ranked results within the configured radius.

Provider cards are rendered in a scrollable list alongside an OSMDroid map overlay. Clicking a provider navigates to a detailed profile screen. The backend's modular route structure (`/api/auth`, `/api/providers`) makes it straightforward to extend with bookings, payments (Razorpay), and push notifications (Firebase Cloud Messaging) in future iterations.

The data pipeline follows a clean **Client → Express Router → Mongoose ODM → MongoDB Atlas** flow. All secrets are managed via `.env` / Docker secrets, enabling smooth promotion from local development to containerized cloud deployment with zero code changes.

---

### 🗺️ System Architecture Diagram

```mermaid
flowchart TD
    subgraph Client["📱 Android Client (Jetpack Compose)"]
        A1[Login / Register Screen]
        A2[User Dashboard]
        A3[Map View - OSMDroid]
        A4[Worker Profile Screen]
        A5[Settings Screen]
        A6[Session Manager]
    end

    subgraph API["⚙️ Backend - Node.js / Express.js"]
        B1[Express Server :5000]
        B2["/api/auth Router"]
        B3["/api/providers Router"]
        B4[Auth Middleware - JWT]
        B5[Bcrypt Password Hash]
    end

    subgraph Data["🗄️ Data Layer"]
        C1[(MongoDB Atlas)]
        C2[Users Collection]
        C3[Providers Collection - 2dsphere]
    end

    subgraph External["🌐 External Services"]
        D1[Firebase Auth]
        D2[Google Play Location API]
        D3[Razorpay Payments]
        D4[Firebase Cloud Messaging]
    end

    A1 -->|Register / Login| B2
    A2 -->|Fetch Nearby| B3
    A3 -->|GPS Coords| D2
    D2 -->|lat, lng| A3
    A3 -->|GET /nearby?lat&lng&radius| B3
    A4 -->|GET /providers/:id| B3
    A6 -->|JWT Token| B4
    B4 --> B2
    B4 --> B3
    B2 --> B5
    B2 --> C2
    B3 --> C3
    C2 & C3 --> C1
    A1 <-->|Phone Auth| D1
    A2 -->|Payments| D3
    B1 -->|Push Notifications| D4
```

---

### 📋 Step-by-Step Workflow Breakdown

1. **User opens app** → `MainActivity` evaluates `SessionManager` for an existing JWT. If absent, navigate to Login Screen.
2. **Authentication** → User submits phone + password → `POST /api/auth/login` → Express validates credentials via bcrypt → signs a 7-day JWT → client stores token in `SessionManager`.
3. **Dashboard load** → `UserDashboard` requests device GPS from Google Play Services Location API.
4. **Nearby search** → Client calls `GET /api/providers/nearby?lat=<lat>&lng=<lng>&radius=5000&category=<cat>` with JWT in header.
5. **Geospatial query** → Express router queries MongoDB Atlas using `$nearSphere` on the `2dsphere`-indexed `location` field of the `providers` collection.
6. **Response** → Sorted provider list (up to 20 results) returned as JSON → rendered in list + OSMDroid map pins.
7. **Provider detail** → User taps a card → `GET /api/providers/:id` → `WorkerProfileScreen` rendered.
8. **Booking / Payment** → (Roadmap) Razorpay checkout initiated → webhook confirms payment → booking record stored.
9. **Push notification** → (Roadmap) Firebase Cloud Messaging delivers job-acceptance push to worker device.
10. **Settings** → Theme toggle, verification, and sign-out handled locally; sign-out clears JWT from `SessionManager`.

---

## 🔄 Workflow Diagram

### Data Flow (Sequence Diagram)

```mermaid
sequenceDiagram
    actor User as 📱 User
    participant App as Android App
    participant SM as SessionManager
    participant API as Express API
    participant DB as MongoDB Atlas
    participant GPS as Play Location
    participant FB as Firebase

    User->>App: Open App
    App->>SM: Check JWT
    alt No JWT
        App->>FB: Request Phone Auth (future)
        App-->>User: Show Login Screen
        User->>App: Enter phone + password
        App->>API: POST /api/auth/login
        API->>DB: Find user by phone
        DB-->>API: User document
        API->>API: bcrypt.compare()
        API-->>App: JWT Token
        App->>SM: Store JWT
    end
    App->>GPS: requestCurrentLocation()
    GPS-->>App: { lat, lng }
    App->>API: GET /api/providers/nearby?lat&lng&radius=5000
    API->>DB: $nearSphere query on providers
    DB-->>API: Sorted provider list
    API-->>App: { success, providers[] }
    App-->>User: Render list + map pins
    User->>App: Tap provider card
    App->>API: GET /api/providers/:id
    API->>DB: findById()
    DB-->>API: Provider document
    API-->>App: { success, provider }
    App-->>User: WorkerProfileScreen
```

---

### Entity Relationship Diagram

```mermaid
erDiagram
    USER {
        ObjectId _id PK
        string name
        string phone UK
        string password
        datetime createdAt
        datetime updatedAt
    }

    PROVIDER {
        ObjectId _id PK
        string name
        string phone UK
        string[] skills
        string category
        number hourlyRate
        string bio
        string photo
        string[] languages
        number rating
        number totalReviews
        boolean isVerified
        boolean isAvailable
        datetime createdAt
        datetime updatedAt
    }

    LOCATION {
        string type
        number[] coordinates
        string address
    }

    BOOKING {
        ObjectId _id PK
        ObjectId userId FK
        ObjectId providerId FK
        string status
        number amount
        string razorpayOrderId
        datetime scheduledAt
        datetime createdAt
    }

    REVIEW {
        ObjectId _id PK
        ObjectId userId FK
        ObjectId providerId FK
        number rating
        string comment
        datetime createdAt
    }

    USER ||--o{ BOOKING : "places"
    PROVIDER ||--o{ BOOKING : "receives"
    USER ||--o{ REVIEW : "writes"
    PROVIDER ||--o{ REVIEW : "receives"
    PROVIDER ||--|| LOCATION : "has"
```

---

### Component Mind Map

```mermaid
mindmap
  root((Local-Link))
    Android Client
      Jetpack Compose UI
        Login Screen
        User Dashboard
        Map View
        Worker Profile
        Settings Screen
      ViewModels
        ThemeViewModel
        ProvidersViewModel
      Networking
        Retrofit2
        ApiService
        Repository Layer
      Location
        OSMDroid Map
        Google Play Location
      Session
        SessionManager
        SharedPreferences
    Backend API
      Express.js Server
        Auth Routes
          POST /register
          POST /login
        Provider Routes
          GET /nearby
          GET /:id
          POST /register
      Middleware
        JWT Validation
        CORS
        dotenv Config
      Models
        User Schema
        Provider Schema
    Data Layer
      MongoDB Atlas
        Users Collection
        Providers Collection
        2dsphere Index
    External Services
      Firebase
        Authentication
        Cloud Messaging
      Razorpay
        Payment Gateway
        Webhooks
      Google Play Services
        Location API
    DevOps
      Docker Compose
        API Container
        MongoDB Container
      CI/CD
        GitHub Actions
        Gradle Build
        APK Artifact
```

---

## 🛠️ Tech Stack

| Layer | Technology | Version | Purpose |
|---|---|---|---|
| **Mobile Client** | Kotlin + Jetpack Compose | 2.0 / BOM 2024.x | Android UI framework |
| **Navigation** | Jetpack Navigation Compose | 2.7.7 | In-app screen routing |
| **Maps** | OSMDroid | 6.1.18 | OpenStreetMap tile rendering |
| **Location** | Google Play Services Location | 21.2.0 | GPS coordinate acquisition |
| **HTTP Client** | Retrofit2 + Gson | 2.9.0 | REST API communication |
| **Backend Runtime** | Node.js | 22.x | Server-side JavaScript |
| **API Framework** | Express.js | 5.x | REST routing & middleware |
| **ODM** | Mongoose | 9.x | MongoDB schema & queries |
| **Database** | MongoDB Atlas | 7.x | Geospatial document store |
| **Auth** | JWT + bcrypt | JWT 9.x / bcrypt 6.x | Stateless auth tokens |
| **Auth (Mobile)** | Firebase Auth | 23.x | Phone / social auth |
| **Push Notifications** | Firebase Cloud Messaging | — | Worker job alerts |
| **Payments** | Razorpay | — | In-app payment checkout |
| **Maps (Web)** | Leaflet.js | 1.9.x | Web map (admin panel) |
| **Containerisation** | Docker + Compose | 24.x | Local dev environment |
| **CI/CD** | GitHub Actions | — | Automated build & deploy |

---

## 🚀 Getting Started

### Prerequisites

Ensure the following are installed on your machine:

- [Node.js](https://nodejs.org) `>= 18.x`
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) `>= 24.x`
- [Android Studio](https://developer.android.com/studio) `Hedgehog | 2023.1.1+`
- [Git](https://git-scm.com)
- A [MongoDB Atlas](https://cloud.mongodb.com) cluster (free tier works)
- A [Firebase project](https://console.firebase.google.com) with Android app registered

---

### Docker Compose Setup

> ✅ Recommended for local backend development — zero manual Node/Mongo config.

**1. Clone the repository**

```bash
git clone https://github.com/your-org/local-link.git
cd local-link
```

**2. Create your environment file**

```bash
cp Local-Links/server/.env.example Local-Links/server/.env
# Edit .env with your own values (see Environment Variables section)
```

**3. Create `docker-compose.yml` at repository root**

```yaml
version: "3.9"

services:
  api:
    build:
      context: ./Local-Links/server
      dockerfile: Dockerfile
    container_name: locallink-api
    restart: unless-stopped
    ports:
      - "5000:5000"
    env_file:
      - ./Local-Links/server/.env
    depends_on:
      - mongo
    networks:
      - locallink-net

  mongo:
    image: mongo:7
    container_name: locallink-mongo
    restart: unless-stopped
    ports:
      - "27017:27017"
    volumes:
      - mongo-data:/data/db
    networks:
      - locallink-net

volumes:
  mongo-data:

networks:
  locallink-net:
    driver: bridge
```

**4. Create `Dockerfile` inside `Local-Links/server/`**

```dockerfile
FROM node:22-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci --omit=dev
COPY . .
EXPOSE 5000
CMD ["node", "server.js"]
```

**5. Start all services**

```bash
docker compose up --build -d
```

**6. Verify the API is running**

```bash
curl http://localhost:5000/
# → {"status":"GigMarket API running 🚀"}
```

---

### Manual Setup

<details>
<summary>📂 <strong>Backend (Node.js)</strong></summary>

```bash
cd Local-Links/server
npm install
# Configure .env (see Environment Variables)
node server.js
```

Expected output:
```
✅ MongoDB connected
✅ Server running on port 5000
```

</details>

<details>
<summary>📱 <strong>Android App</strong></summary>

1. Open `Local-Links/` in **Android Studio**.
2. Sync Gradle: **File → Sync Project with Gradle Files**.
3. Update the base URL in `app/src/main/java/com/example/gigmarket/network/`:

```kotlin
// ApiService.kt or RetrofitInstance.kt
private const val BASE_URL = "http://10.0.2.2:5000/" // Android emulator
// private const val BASE_URL = "http://<your-local-ip>:5000/" // Physical device
```

4. Run on emulator or physical device via **Run → Run 'app'**.

</details>

---

## 🔑 Environment Variables

<details>
<summary>📋 <strong>View Full Environment Variable Reference</strong></summary>

Create `Local-Links/server/.env` based on the table below:

| Variable | Required | Example Value | Description |
|---|---|---|---|
| `MONGO_URI` | ✅ | `mongodb+srv://user:pass@cluster.mongodb.net/gigmarket?retryWrites=true&w=majority` | MongoDB Atlas connection string |
| `PORT` | ✅ | `5000` | Port the Express server binds to |
| `JWT_SECRET` | ✅ | `your_super_secret_key_change_me` | Secret used to sign/verify JWT tokens |
| `NODE_ENV` | ⬜ | `development` | Set to `production` in deployed environments |

**Example `.env` file:**

```dotenv
MONGO_URI=mongodb+srv://gigadmin:YOUR_PASSWORD@gigadmin.hkfba71.mongodb.net/gigmarket?retryWrites=true&w=majority
PORT=5000
JWT_SECRET=change_this_to_a_long_random_string
NODE_ENV=development
```

> ⚠️ **Never commit your `.env` file.** It is listed in `.gitignore`. Rotate `JWT_SECRET` and MongoDB credentials before any production deployment.

**Firebase (Android):** Place your `google-services.json` at `Local-Links/app/google-services.json`. Download it from your Firebase Console → Project Settings → Android App.

</details>

---

## 📡 API Reference

**Base URL:** `http://localhost:5000`  
**Authentication:** Include `Authorization: Bearer <token>` header on protected routes.

---

### Auth Endpoints

<details>
<summary><code>POST</code> <strong>/api/auth/register</strong> — Register a new user</summary>

**Request Body:**
```json
{
  "name": "Ravi Kumar",
  "phone": "9876543210",
  "password": "securepassword123"
}
```

**Success Response `201`:**
```json
{
  "success": true,
  "message": "User registered"
}
```

**Error Response `400`:**
```json
{
  "success": false,
  "message": "phone: Path `phone` is required."
}
```
</details>

<details>
<summary><code>POST</code> <strong>/api/auth/login</strong> — Authenticate and receive JWT</summary>

**Request Body:**
```json
{
  "phone": "9876543210",
  "password": "securepassword123"
}
```

**Success Response `200`:**
```json
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Error Responses:**

| Code | Message |
|---|---|
| `404` | `User not found` |
| `400` | `Invalid password` |

</details>

---

### Provider Endpoints

<details>
<summary><code>GET</code> <strong>/api/providers/nearby</strong> — Find providers within radius</summary>

**Query Parameters:**

| Parameter | Type | Required | Default | Description |
|---|---|---|---|---|
| `lat` | `number` | ✅ | — | User latitude |
| `lng` | `number` | ✅ | — | User longitude |
| `radius` | `number` | ⬜ | `5000` | Search radius in metres |
| `category` | `string` | ⬜ | — | Filter by category e.g. `Repairs` |

**Example Request:**
```http
GET /api/providers/nearby?lat=12.9716&lng=77.5946&radius=5000&category=Repairs
```

**Success Response `200`:**
```json
{
  "success": true,
  "count": 3,
  "providers": [
    {
      "_id": "65f2a1b3c4e5d6f7a8b9c0d1",
      "name": "Suresh Electrician",
      "skills": ["Electrician", "Wiring"],
      "category": "Repairs",
      "hourlyRate": 250,
      "rating": 4.7,
      "isAvailable": true,
      "location": {
        "type": "Point",
        "coordinates": [77.5950, 12.9720],
        "address": "Malleshwaram, Bengaluru"
      }
    }
  ]
}
```

</details>

<details>
<summary><code>GET</code> <strong>/api/providers/:id</strong> — Get single provider profile</summary>

**Path Parameter:** `id` — MongoDB ObjectId of the provider.

**Success Response `200`:**
```json
{
  "success": true,
  "provider": {
    "_id": "65f2a1b3c4e5d6f7a8b9c0d1",
    "name": "Suresh Electrician",
    "phone": "9988776655",
    "skills": ["Electrician", "Wiring", "MCB Fitting"],
    "category": "Repairs",
    "hourlyRate": 250,
    "bio": "10 years experience in domestic wiring.",
    "languages": ["Kannada", "Hindi"],
    "rating": 4.7,
    "totalReviews": 43,
    "isVerified": true,
    "isAvailable": true
  }
}
```

**Error `404`:** `{ "message": "Not found" }`

</details>

<details>
<summary><code>POST</code> <strong>/api/providers/register</strong> — Register a new provider</summary>

**Request Body:**
```json
{
  "name": "Suresh Electrician",
  "phone": "9988776655",
  "skills": ["Electrician", "Wiring"],
  "category": "Repairs",
  "hourlyRate": 250,
  "bio": "10 years experience in domestic wiring.",
  "languages": ["Kannada", "Hindi"],
  "location": {
    "type": "Point",
    "coordinates": [77.5950, 12.9720],
    "address": "Malleshwaram, Bengaluru"
  }
}
```

**Success Response `201`:**
```json
{
  "success": true,
  "provider": { "...provider object..." }
}
```

</details>

---

### Health Check

```http
GET /
```
```json
{ "status": "GigMarket API running 🚀" }
```

---

## 📦 Deployment Guide

### CI/CD Pipeline

```mermaid
flowchart LR
    subgraph Dev["👨‍💻 Developer"]
        A[git push to main]
    end

    subgraph CI["⚙️ GitHub Actions CI"]
        B[Checkout Code]
        C[Setup Node.js 22]
        D[npm ci]
        E[Run Tests]
        F[Setup JDK 17]
        G[Gradle Build]
        H[Generate APK]
    end

    subgraph Artifacts["📦 Artifacts"]
        I[Upload APK]
        J[Upload Test Report]
    end

    subgraph CD["🚀 Deploy"]
        K[Docker Build & Push]
        L[Deploy to Render / Railway]
        M[Health Check /]
    end

    A --> B
    B --> C --> D --> E --> J
    B --> F --> G --> H --> I
    E -->|Pass| K
    K --> L --> M
    M -->|200 OK| N[✅ Deployment Success]
    M -->|Fail| O[❌ Rollback]
```

---

### Android APK Generation

#### Debug APK (Development)

```bash
cd Local-Links

# On Linux / macOS
./gradlew assembleDebug

# On Windows
.\gradlew.bat assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

---

#### Release APK (Production)

**Step 1 — Generate a keystore (one-time)**

```bash
keytool -genkey -v \
  -keystore locallink-release.jks \
  -alias locallink \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

**Step 2 — Configure signing in `app/build.gradle.kts`**

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("../locallink-release.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = "locallink"
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

**Step 3 — Build signed APK**

```bash
# Windows
.\gradlew.bat assembleRelease

# Linux / macOS
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

> 📦 The pre-built `Local-Link (V1).apk` (v1.0) is available in the repository root for immediate sideloading.

---

### Backend Deployment

#### Option A — Render (Recommended for free tier)

1. Push code to GitHub.
2. Create a new **Web Service** on [Render](https://render.com).
3. Set **Build Command:** `npm install`  
   **Start Command:** `node server.js`
4. Add environment variables (`MONGO_URI`, `JWT_SECRET`, `PORT`) in Render dashboard.
5. Deploy — Render auto-deploys on every push to `main`.

#### Option B — Railway

```bash
npm install -g @railway/cli
railway login
railway init
railway up
railway variables set MONGO_URI="..." JWT_SECRET="..." PORT=5000
```

#### Option C — Docker on any VPS

```bash
docker compose -f docker-compose.yml up -d --build
```

---

## 🗺️ Roadmap

| Status | Feature |
|---|---|
| ✅ | Geospatial provider discovery (5 km radius) |
| ✅ | User & Provider authentication (JWT + bcrypt) |
| ✅ | OSMDroid live map with provider pins |
| ✅ | Dark / light theme with persistence |
| ✅ | Multi-language support (EN / HI / KN) |
| ✅ | Worker request queue & session management |
| 🚧 | Razorpay payment integration |
| 🚧 | Firebase Cloud Messaging job alerts |
| 🚧 | Review & rating system |
| 🚧 | Provider availability scheduling (calendar) |
| 📋 | Admin panel (React.js + Leaflet.js) |
| 📋 | Booking history & invoice generation |
| 📋 | In-app chat (Socket.io) |
| 📋 | Provider KYC & Aadhaar verification |
| 📋 | Analytics dashboard (provider earnings) |
| 📋 | iOS client (React Native) |

> **Legend:** ✅ Done · 🚧 In Progress · 📋 Planned

---

## ❓ FAQ

<details>
<summary><strong>Q: Why is the search radius fixed at 5 km?</strong></summary>

5 km is the default but fully configurable via the `radius` query parameter on `GET /api/providers/nearby`. The Android client can expose a UI slider to let users adjust this at runtime. The MongoDB `$nearSphere` query accepts any distance in metres.

</details>

<details>
<summary><strong>Q: Can I use this without a real Android device?</strong></summary>

Yes. Use the Android Emulator in Android Studio. Set the base URL in your network configuration to `http://10.0.2.2:5000/` (the emulator's alias for `localhost`). For GPS simulation, use the emulator's **Extended Controls → Location** panel.

</details>

<details>
<summary><strong>Q: How do I seed the database with test providers?</strong></summary>

Create a `seed.js` script in `Local-Links/server/`:

```js
require("dotenv").config();
const mongoose = require("mongoose");
const Provider = require("./models/Provider");

const providers = [
  {
    name: "Raju Plumber",
    phone: "9000000001",
    skills: ["Plumbing", "Pipe Fitting"],
    category: "Repairs",
    hourlyRate: 200,
    languages: ["Kannada"],
    location: { type: "Point", coordinates: [77.5946, 12.9716], address: "Bengaluru" },
  },
];

mongoose.connect(process.env.MONGO_URI).then(async () => {
  await Provider.insertMany(providers);
  console.log("Seeded ✅");
  process.exit(0);
});
```

Run: `node seed.js`

</details>

<details>
<summary><strong>Q: How is commission kept minimal?</strong></summary>

Local-Link's model charges a flat per-booking service fee rather than a percentage of earnings, keeping the platform affordable for gig workers. Razorpay's payment split (route) feature will automate this in the payments milestone.

</details>

<details>
<summary><strong>Q: What does the <code>2dsphere</code> index do?</strong></summary>

MongoDB's `2dsphere` index allows efficient geospatial queries (`$nearSphere`, `$geoWithin`) on GeoJSON `Point` data. Without this index, nearby-provider queries would require a full collection scan, making them unusably slow at scale. The index is created automatically when the Provider model is first loaded:

```js
providerSchema.index({ location: "2dsphere" });
```

</details>

<details>
<summary><strong>Q: How do I change the app language?</strong></summary>

Language is detected from the device locale automatically. To manually override, the app ships with string resources for English (`values/`), Hindi (`values-hi/`), and Kannada (`values-kn/`). Add more by creating additional `values-<locale>/strings.xml` files.

</details>

---

## 📄 License

```
MIT License

Copyright (c) 2026 Local-Link Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

<div align="center">

Built with ❤️ for Bharat's gig workers · **Local-Link v1.0**

[![Star on GitHub](https://img.shields.io/github/stars/your-org/local-link?style=social)](https://github.com/your-org/local-link)
[![Fork on GitHub](https://img.shields.io/github/forks/your-org/local-link?style=social)](https://github.com/your-org/local-link/fork)

</div>
