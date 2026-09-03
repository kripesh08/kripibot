# KripiBot - Full-Stack Gemini AI Chatbot

A production-ready conversational AI chatbot built with **Java Spring Boot 3**, **React (Vite)**, **PostgreSQL**, and **Google Gemini API** (Free Tier).

---

## 🌟 Key Features

- ⚡ **Google Gemini Integration**: Supports `gemini-1.5-flash`, `gemini-2.0-flash`, and `gemini-1.5-pro` (free tier compatible).
- 💬 **Multi-Turn Context Memory**: Retains conversation thread history and sends it seamlessly to Gemini.
- 🗄️ **Automatic PostgreSQL Persistence**: Stores conversations, messages, and token usage via Hibernate JPA (`ddl-auto: update` automatically creates tables).
- 🎨 **Modern Dark UI**: Glassmorphic dark aesthetic, fluid responsive layout, Markdown rendering, and code syntax highlighting with one-click copy.
- ⚙️ **Configurable Settings**: Provide your Gemini API key via UI Settings, `application.yml`, or environment variable (`GEMINI_API_KEY`).

---

## 🚀 Quick Start Guide

### 1. Backend Setup (Spring Boot)

1. Open `backend/src/main/resources/application.yml` and check your PostgreSQL credentials:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/postgres
       username: postgres
       password: your_postgres_password
   ```
2. Set your Google Gemini API Key (get a free key from [Google AI Studio](https://aistudio.google.com/app/apikey)):
   - Either in `backend/src/main/resources/application.yml` under `gemini.api.key`
   - Or export it in your terminal:
     ```powershell
     $env:GEMINI_API_KEY="your-gemini-api-key"
     ```
   - Or simply enter it in the frontend UI Settings modal!
3. Run the backend:
   ```powershell
   cd backend
   mvn spring-boot:run
   ```
   Backend runs on `http://localhost:8080`.

---

### 2. Frontend Setup (React + Vite)

1. Install dependencies & start the dev server:
   ```powershell
   cd frontend
   npm install
   npm run dev
   ```
2. Open `http://localhost:5173` in your browser.

---

## 🐳 Running with Docker

The easiest way to run KripiBot on any machine — no need to install Java, Maven, or Node.js.

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running

### 1. Clone the repo

```bash
git clone https://github.com/kripesh08/kripibot.git
cd kripibot
```

### 2. Create your `.env` file

```bash
cp .env.example .env
```

Edit `.env` with your real values:

```env
DB_USERNAME=postgres
DB_PASSWORD=your_db_password
GEMINI_API_KEY=your_gemini_api_key
GEMINI_MODEL=gemini-2.5-flash
```

> Get a free Gemini API key from [Google AI Studio](https://aistudio.google.com/app/apikey)

### 3. Start everything

```bash
docker compose up -d
```

This will automatically:
- Pull PostgreSQL from Docker Hub
- Build the Spring Boot backend image
- Build the React/Nginx frontend image
- Start all 3 containers wired together

### 4. Open the app

| Service | URL |
|---------|-----|
| 🌐 Frontend | **http://localhost:3000** |
| 🔌 Backend API | http://localhost:8081 |

### Useful commands

```bash
# View running containers
docker compose ps

# View live logs
docker compose logs -f

# Stop everything
docker compose down

# Stop and delete the database volume (fresh start)
docker compose down -v

# Rebuild images after code changes
docker compose build
docker compose up -d
```

### Want to skip building? Use pre-built images from Docker Hub

```bash
# Pull pre-built images (faster — no compilation needed)
docker compose pull
docker compose up -d
```

Pre-built images: [`kripi08/kripibot-backend`](https://hub.docker.com/r/kripi08/kripibot-backend) · [`kripi08/kripibot-frontend`](https://hub.docker.com/r/kripi08/kripibot-frontend)

---

## 📁 Project Architecture

```
d:\KripiBot\
├── backend\
│   ├── pom.xml
│   └── src\main\
│       ├── java\com\kripibot\
│       │   ├── KripiBotApplication.java
│       │   ├── config\            (CORS, Gemini Properties)
│       │   ├── controller\        (ChatController, ConversationController)
│       │   ├── dto\               (ChatRequest, ChatResponse, DTOs)
│       │   ├── exception\         (GlobalExceptionHandler)
│       │   ├── model\             (Conversation, Message, Role)
│       │   ├── repository\        (ConversationRepository, MessageRepository)
│       │   └── service\           (GeminiService, ChatService)
│       └── resources\
│           └── application.yml
└── frontend\
    ├── package.json
    ├── vite.config.js
    ├── index.html
    └── src\
        ├── App.jsx
        ├── index.css              (Glassmorphic Dark Theme)
        ├── components\
        │   ├── Sidebar.jsx        (Chat History & Management)
        │   ├── ChatArea.jsx       (Conversation Stream & Hero State)
        │   ├── MessageItem.jsx    (Markdown & Code Highlights)
        │   ├── ChatInput.jsx      (Expanding Input & Shortcuts)
        │   └── SettingsModal.jsx  (API Key & Model Selector)
        └── services\
            └── api.js             (REST API Client)
```
