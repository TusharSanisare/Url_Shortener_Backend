# URL Shortener with Microservices and AI-Powered Personalization
## A high-performance URL shortener built using Spring Boot, React, and PostgreSQL, with AI-powered personalized short URLs for enhanced user engagement. The application is designed to handle high traffic efficiently, leveraging Redis for caching, Docker for containerization, and Chart.js for real-time analytics.

## Features
AI-Powered Personalization: Uses Deepseek API to generate personalized and relevant short URLs based on the content of the original URL.
Redis Caching: Reduces database load and improves response times.
Real-Time Analytics: Tracks URL clicks in real-time using Chart.js.
High Performance: Handles 1000+ concurrent requests with an average response time of < 50ms.
Containerization: Uses Docker and Docker Compose for seamless deployment and scaling.

## Technologies Used
Backend: Spring Boot (Java)
Frontend: React
Database: PostgreSQL
Caching: Redis
AI Integration: Google Gemini API
Containerization: Docker, Docker Compose
Analytics: Chart.js

## Setup and Installation
Prerequisites:
Java 17+
Node.js (for React frontend)
Docker and Docker Compose
PostgreSQL
Redis
Steps to Run the Project
Clone the Repository:

```bash
git clone https://github.com/TusharSanisare/Url_Shortener_Backend.git
Url_Shortener_Backend
```
Navigate to the backend directory:
Update the application.properties file with your PostgreSQL and Redis credentials.

Build and run the Spring Boot application:

```bash
./mvnw clean install
./mvnw spring-boot:run
```
Backend API: http://localhost:8080
