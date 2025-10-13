# 🚀 SmartHire – AI-Powered Job Application Tracking & Screening System

## Overview

SmartHire is an intelligent recruitment automation platform that integrates **AI resume screening**, **test generation**, **code evaluation**, and **real-time notifications** via WebSockets to streamline the entire hiring process.

## 🧠 Features

### 🤖 AI-Powered Screening
- **Automated Resume Parsing & Scoring** - AI-driven analysis and ranking of candidate resumes
- **Personalized Test Generation** - Dynamic test creation using OpenAI GPT models
- **Intelligent Candidate Matching** - Smart job-candidate pairing based on skills and experience

### 💻 Technical Evaluation
- **Code Evaluation Engine** - Real-time code execution and testing via Judge0 API
- **Multi-language Support** - Support for 40+ programming languages
- **Automated Scoring System** - Objective assessment of technical skills and problem-solving

### 🔔 Real-Time Communication
- **WebSocket Notifications** - Instant updates for HR teams and candidates
- **Email Integration** - Automated email notifications for application status
- **Live Dashboard Updates** - Real-time status changes and analytics

### ☁️ Cloud Integration
- **Resume Storage** - Secure cloud storage via AWS S3/Google Drive API
- **Scalable Infrastructure** - Docker-based containerized deployment
- **Secure File Handling** - Encrypted file management and access control

### 📊 Analytics & Reporting
- **Candidate Pipeline Tracking** - Visualize candidate journey from application to hire
- **Performance Analytics** - Measure screening effectiveness and time-to-hire
- **Custom Reports** - Generate detailed recruitment metrics and insights

## 🏗️ Tech Stack

### Frontend
- **React.js 18** - Modern UI framework with hooks and functional components
- **Tailwind CSS** - Utility-first CSS framework for responsive design
- **Axios** - HTTP client for API communication
- **STOMP + SockJS** - WebSocket client implementation for real-time features
- **React Router** - Client-side routing and navigation
- **Chart.js** - Data visualization and analytics dashboards

### Backend
- **Spring Boot 3** - Java application framework with auto-configuration
- **Spring Data JPA** - Database abstraction layer with Hibernate
- **Spring Security** - Authentication and authorization framework
- **JWT** - JSON Web Tokens for stateless authentication
- **Spring WebSocket** - Real-time bidirectional communication
- **Spring Mail** - Email service integration
- **Spring Validation** - Request validation and error handling

### Database
- **PostgreSQL 14** - Primary relational database
- **Redis** - Caching and session management
- **Database Migrations** - Flyway for version-controlled database changes

### AI & External Services
- **OpenAI API** - GPT models for test generation and resume analysis
- **spaCy** - NLP processing for resume parsing
- **Judge0 API** - Code evaluation and execution engine
- **Amazon S3** - Cloud file storage for resumes and documents
- **Google Drive API** - Alternative cloud storage solution

### Security
- **JWT Authentication** - Secure token-based authentication
- **CORS Configuration** - Cross-origin resource sharing setup
- **Data Encryption** - Field-level encryption for sensitive data
- **Input Validation** - Comprehensive request validation

### Deployment & DevOps
- **Docker** - Containerization for consistent environments
- **Docker Compose** - Multi-container application management
- **Maven** - Java dependency management and build automation
- **npm** - JavaScript package management
- **Git** - Version control system

## 🏛️ Architecture

### System Architecture Diagram
```text
                         ┌────────────────────────┐
                         │      Frontend (React)  │
                         │────────────────────────│
                         │ • Tailwind CSS         │
                         │ • STOMP + SockJS       │
                         │ • Axios, React Router  │
                         └────────────┬───────────┘
                                      │
                        WebSocket + REST API Calls
                                      │
                         ┌────────────┴────────────┐
                         │   Backend (Spring Boot) │
                         │──────────────────────── │
                         │ • Spring Security (JWT) │
                         │ • WebSocket (STOMP)     │
                         │ • Spring Data JPA       │
                         │ • REST Controllers      │
                         └────────────┬────────────┘
                                      │
                     ┌────────────────┴────────────────┐
                     │                                 │
          ┌──────────▼─────────┐             ┌──────────▼──────────┐
          │  PostgreSQL DB     │             │   External APIs     │
          │────────────────────│             │─────────────────────│
          │ • Candidates       │             │ • OpenAI (AI Tests) │
          │ • Jobs & Tests     │             │ • Judge0 (Code Eval)│
          │ • Applications     │             │ • AWS / Google Drive│
          └────────────────────┘             └─────────────────────┘

                         ┌─────────────────────────┐
                         │  Real-Time Notifications│
                         │─────────────────────────│
                         │ • WebSocket Messages    │
                         │ • Email Notifications   │
                         └─────────────────────────┘

```


### Data Flow
1. **Candidate Application** → Resume upload → AI parsing → Database storage
2. **HR Dashboard** → Real-time updates → Candidate management → Interview scheduling
3. **Test Generation** → AI-powered creation → Candidate testing → Automated evaluation
4. **Code Evaluation** → Judge0 integration → Result processing → Score calculation

## 🚀 Quick Start

### Prerequisites
- **Java 17** or higher
- **Node.js 18** or higher
- **PostgreSQL 14** or higher
- **Maven 3.8** or higher
- **Docker** (optional, for containerized deployment)

### 1️⃣ Clone Repository
```bash
git clone https://github.com/karthikdokiparthi/Smart_Hire
cd Smart_Hire
```
### 2️⃣ Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 3️⃣ Frontend Setup
```bash
cd frontend
npm install
npm start
```

### 4️⃣ Database Initialization
```bash
-- Create database
CREATE DATABASE smarthire;

-- Create tables (example)
CREATE TABLE candidates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    resume_url TEXT,
    skills TEXT,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE jobs (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100),
    description TEXT,
    posted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE applications (
    id SERIAL PRIMARY KEY,
    candidate_id INT REFERENCES candidates(id),
    job_id INT REFERENCES jobs(id),
    status VARCHAR(50) DEFAULT 'Applied',
    score DECIMAL,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tests (
    id SERIAL PRIMARY KEY,
    application_id INT REFERENCES applications(id),
    test_url TEXT,
    score DECIMAL,
    completed_at TIMESTAMP
);
```
## ⚙️ Configuration
### Create a .env file in backend directory:

#### PostgreSQL
```base
DB_URL=jdbc:postgresql://localhost:5432/smarthire
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
```
#### JWT
```base
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION_MS=86400000
```
#### OpenAI API
```base
OPENAI_API_KEY=your_openai_api_key
```

#### Judge0 API
```base
JUDGE0_API_KEY=your_judge0_api_key
```
#### Email Service
```base
EMAIL_HOST=smtp.example.com
EMAIL_PORT=587
EMAIL_USERNAME=your_email
EMAIL_PASSWORD=your_email_password
```

## 📦 Deployment
### Using Docker Compose
#### Build and start containers
```base
docker-compose up --build
```
#### Stop containers
```base
docker-compose down
```

### Manual Deployment

1. Start PostgreSQL and configure the database.

2. Run backend with Maven: mvn spring-boot:run

3. Run frontend: npm start

## 🧪 Testing

### Backend Unit Tests:
```base
cd backend
mvn test
```

### Frontend Testing:
```base
cd frontend
npm test
```

### End-to-End Tests: Use Postman or REST client for API testing.

# 🛠️ Troubleshooting

* Database connection issues: Ensure .env credentials match PostgreSQL setup.

* Port conflicts: Default backend 8080, frontend 3000.

* WebSocket not connecting: Verify STOMP endpoint URL and CORS settings.

# 👤 Author

Karthik Dokiparthi – Full Stack Java Developer

GitHub: https://github.com/karthikdokiparthi
