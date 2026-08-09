# 🤖 AI-Persona

### Autonomous AI Persona • Continuous Research • Intelligent Content Generation

<p align="center">

**AI-Persona is an autonomous AI system that creates a domain-aware persona, discovers emerging information, evaluates what matters, generates original transmissions, and continuously publishes them through a live intelligence feed.**

</p>

<p align="center">

[![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge\&logo=openjdk)](#)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=for-the-badge\&logo=springboot)](#)
[![JavaScript](https://img.shields.io/badge/JavaScript-ES6%2B-yellow?style=for-the-badge\&logo=javascript)](#)
[![Appwrite](https://img.shields.io/badge/Deployed%20with-Appwrite-f02e65?style=for-the-badge\&logo=appwrite)](#)
[![GitHub](https://img.shields.io/badge/Source-GitHub-black?style=for-the-badge\&logo=github)](#)

</p>

---

## 🌌 What is AI-Persona?

Most AI applications work like this:

```text
User
  ↓
Prompt
  ↓
AI
  ↓
Response
```

**AI-Persona takes a different approach.**

The user creates an AI persona once, defines its domain, and lets the system continuously operate around that identity.

```text
                ┌──────────────────────┐
                │      AI-PERSONA      │
                │                      │
                │  Identity + Domain   │
                └──────────┬───────────┘
                           │
                           ▼
                 ┌──────────────────┐
                 │  DISCOVER        │
                 │  Emerging Topics │
                 └────────┬─────────┘
                          │
                          ▼
                 ┌──────────────────┐
                 │  EVALUATE        │
                 │  What Matters?   │
                 └────────┬─────────┘
                          │
                          ▼
                 ┌──────────────────┐
                 │  GENERATE        │
                 │  AI Transmission │
                 └────────┬─────────┘
                          │
                          ▼
                 ┌──────────────────┐
                 │  VALIDATE        │
                 └────────┬─────────┘
                          │
                          ▼
                 ┌──────────────────┐
                 │  PUBLISH         │
                 │  Live Feed       │
                 └──────────────────┘
```

The objective is simple:

> **Don't wait for the user to ask what is happening. Let the persona discover what is worth knowing.**

---

# ✨ Core Features

| Feature                  | Description                                               |
| ------------------------ | --------------------------------------------------------- |
| 🧠 AI Persona            | Create a domain-specific autonomous persona               |
| 🎯 Domain Awareness      | Persona behavior is centered around its configured domain |
| 🔎 Topic Discovery       | Discover emerging technical information                   |
| 📰 Multi-Source Research | Research through Hacker News and arXiv                    |
| ⚖️ Editorial Evaluation  | Decide whether a discovered topic is worth publishing     |
| ✍️ AI Generation         | Generate original domain-oriented transmissions           |
| 🔍 Validation            | Process generated content before publication              |
| 📡 Autonomous Publishing | Publish transmissions without repeated manual prompting   |
| 🧾 Rationale             | Show why a topic was selected                             |
| 🔗 Sources               | Preserve relevant source references                       |
| 📊 Live Status           | Monitor persona state and publishing activity             |
| 🆔 Agent Identity        | Track the active persona through its agent ID             |
| 🌌 Immersive UI          | Aurora + universe + orbital AI-console interface          |

---

# 🧬 Persona System

The first step is creating the persona.

```text
┌─────────────────────────────────────┐
│           INITIALIZE PERSONA        │
│                                     │
│  Persona name                       │
│  ┌───────────────────────────────┐  │
│  │ Ada                           │  │
│  └───────────────────────────────┘  │
│                                     │
│  Domain                              │
│  ┌───────────────────────────────┐  │
│  │ AI Security Researcher      ▼ │  │
│  └───────────────────────────────┘  │
│                                     │
│       [ Initialize agent → ]        │
└─────────────────────────────────────┘
```

### Supported domains

* AI Security Researcher
* Machine Learning Engineer
* AI Product Analyst
* Open Source Contributor
* Robotics Engineer
* Developer Advocate
* AI Ethics Researcher
* Custom Domain

A custom domain allows the user to create a more specialized persona.

Example:

```text
Persona:
Nova

Domain:
Quantum Computing
```

---

# 🔬 Autonomous Research Pipeline

AI-Persona is designed as a continuous research loop.

```text
┌───────────────┐
│ External Web  │
│ Information   │
└───────┬───────┘
        │
        ▼
┌───────────────┐
│   Discovery   │
└───────┬───────┘
        │
        ▼
┌───────────────┐
│   Relevance   │
│   Evaluation  │
└───────┬───────┘
        │
        ▼
┌───────────────┐
│   Editorial   │
│    Decision   │
└───────┬───────┘
        │
        ▼
┌───────────────┐
│ AI Generation │
└───────┬───────┘
        │
        ▼
┌───────────────┐
│   Validation  │
└───────┬───────┘
        │
        ▼
┌───────────────┐
│  Persistence   │
└───────┬───────┘
        │
        ▼
┌───────────────┐
│ Transmission  │
│     Feed      │
└───────────────┘
```

---

# 📰 Information Sources

AI-Persona currently uses external technical information sources including:

### Hacker News

Provides current technology discussions, engineering developments, and emerging technical topics.

### arXiv

Provides research papers and emerging developments across technical and scientific fields.

The system uses these sources as **research signals**, rather than blindly publishing everything it discovers.

---

# ⚖️ Discovery ≠ Publication

One of the core ideas behind AI-Persona is that finding information is not enough.

The system follows:

```text
                DISCOVERED
                    │
                    ▼
              Is it relevant?
                    │
          ┌─────────┴─────────┐
          │                   │
         NO                  YES
          │                   │
          ▼                   ▼
       Ignore            Is it worth
                         publishing?
                              │
                       ┌──────┴──────┐
                       │             │
                      NO            YES
                       │             │
                       ▼             ▼
                    Ignore        Generate
                                     │
                                     ▼
                                  Publish
```

This editorial layer is what makes the system more than a simple information scraper.

---

# 🖥️ Product Interface

AI-Persona is designed as an **AI intelligence console**.

The interface provides:

### Persona Identity

Displays the currently active persona and domain.

### Agent Status

Shows whether the autonomous agent is active, connecting, or encountering an issue.

### Next Transmission

Displays the upcoming autonomous publishing cycle.

### Published Count

Tracks how many transmissions have been published.

### Agent ID

Provides the unique identifier associated with the active persona.

### Transmission Feed

Displays generated AI content.

### Rationale

Provides context about why the topic was selected.

### Sources

Shows the external information associated with the transmission.

---

# 🎨 Design System

The UI intentionally avoids the appearance of a traditional admin dashboard.

### Visual language

```text
Obsidian Background
        +
Universe / Stars
        +
Purple Aurora
        +
Orbital Rings
        +
Glass Panels
        +
Purple AI Controls
        +
Cyan System Signals
```

The goal is to create the feeling of interacting with an **autonomous intelligence system** rather than a normal CRUD application.

---

# 🏗️ Architecture

```text
                           AI-PERSONA
                               │
              ┌────────────────┴────────────────┐
              │                                 │
              ▼                                 ▼
        ┌─────────────┐                  ┌─────────────┐
        │  FRONTEND   │                  │   BACKEND   │
        │             │                  │             │
        │ HTML        │                  │ Spring Boot │
        │ CSS         │                  │ Java        │
        │ JavaScript  │                  │ REST APIs   │
        └──────┬──────┘                  └──────┬──────┘
               │                                │
               │                         ┌──────▼──────┐
               │                         │ AI PERSONA  │
               │                         │   ENGINE    │
               │                         └──────┬──────┘
               │                                │
               │                 ┌──────────────┼──────────────┐
               │                 │              │              │
               │                 ▼              ▼              ▼
               │            Discovery       Evaluation     Generation
               │                 │              │              │
               │                 └──────────────┼──────────────┘
               │                                │
               │                          ┌─────▼─────┐
               │                          │ Validation│
               │                          └─────┬─────┘
               │                                │
               │                          ┌─────▼─────┐
               │                          │Persistence│
               │                          └─────┬─────┘
               │                                │
               └────────────── REST ────────────┘
```

---

# 📁 Repository Structure

```text
AI-Persona/
│
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── app.js
│
├── backend/
│   ├── pom.xml
│   │
│   └── src/
│       └── main/
│           ├── java/
│           │   └── ...
│           │
│           └── resources/
│               └── ...
│
└── README.md
```

---

# 🔌 API Architecture

The frontend communicates with the Spring Boot backend through REST endpoints.

### Initialize Persona

```http
POST /api/agent/init
```

Creates or initializes the active AI persona.

---

### Agent Status

```http
GET /api/agent/status
```

Returns the current state of the autonomous persona.

---

### Transmission Feed

```http
GET /api/agent/feed
```

Returns the published transmissions associated with the active persona.

---

# 🔄 Complete User Journey

```text
                    USER
                     │
                     ▼
            Create AI Persona
                     │
                     ▼
             Select Domain
                     │
                     ▼
             Initialize Agent
                     │
                     ▼
              ┌────────────┐
              │ AI PERSONA │
              └─────┬──────┘
                    │
          ┌─────────▼─────────┐
          │ Autonomous Cycle  │
          └─────────┬─────────┘
                    │
             Discover Topics
                    │
                    ▼
             Evaluate Topics
                    │
                    ▼
            Generate Content
                    │
                    ▼
               Validate
                    │
                    ▼
                Publish
                    │
                    ▼
              Live Feed
                    │
                    └──────────► Repeat
```

---

# ☁️ Deployment

AI-Persona is designed to be deployed with **Appwrite Sites**.

The frontend can be connected directly to the GitHub repository through Appwrite's Git deployment workflow. Appwrite Sites supports GitHub-connected deployments where pushing to the configured production branch can automatically trigger a new build and deployment.

### Deployment architecture

```text
                 GitHub
                    │
                    │ push
                    ▼
              ┌───────────┐
              │  Appwrite │
              │   Sites   │
              └─────┬─────┘
                    │
                 Build
                    │
                    ▼
              ┌───────────┐
              │ Frontend  │
              │   Build   │
              └─────┬─────┘
                    │
                    ▼
              Public Website
```

For a plain HTML/CSS/JavaScript frontend, Appwrite Sites supports static hosting, where the generated site files are served through Appwrite's infrastructure/CDN.

### Recommended repository setup

```text
AI-Persona/
│
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── app.js
│
├── backend/
│   └── Spring Boot project
│
└── README.md
```

For Appwrite Sites, configure the site's root/build settings so the deployment output contains the frontend files that should be publicly served. Appwrite's documentation notes that only files included in the configured output directory are available on the public site.

---

# 🚀 Local Development

## Requirements

* Java JDK
* Maven
* Git
* Modern web browser
* Eclipse / IntelliJ IDEA / VS Code

---

## Clone

```bash
git clone <YOUR_REPOSITORY_URL>

cd AI-Persona
```

---

## Backend

```bash
cd backend

mvn spring-boot:run
```

---

## Frontend

Open the `frontend` directory using your preferred local development server.

```text
frontend/
├── index.html
├── style.css
└── app.js
```

The frontend communicates with the running Spring Boot backend through the configured REST endpoints.

---

# 🧪 Example Persona

```text
┌──────────────────────────────────────┐
│             AI-PERSONA               │
├──────────────────────────────────────┤
│                                      │
│ Persona                               │
│ Ada                                  │
│                                      │
│ Domain                               │
│ AI Security Researcher              │
│                                      │
│ Status                               │
│ ● Active                             │
│                                      │
│ Next Transmission                   │
│ 04:32                                │
│                                      │
│ Published                            │
│ 12                                   │
│                                      │
└──────────────────────────────────────┘
```

The persona then continuously operates around its configured domain.

---

# 🧠 Design Philosophy

AI-Persona is built around five core ideas:

### 01 — Identity

An AI should have a defined context and professional identity.

### 02 — Awareness

The persona should be able to discover information from its surrounding information environment.

### 03 — Judgment

Not every piece of information deserves attention.

### 04 — Creation

The persona should transform relevant information into useful original content.

### 05 — Continuity

The system should continue operating instead of waiting for another prompt.

---

# 🔮 Future Scope

AI-Persona can evolve into a larger autonomous research platform.

Potential extensions include:

* Multiple simultaneous personas
* Persona memory
* Long-term knowledge graphs
* Semantic topic clustering
* Duplicate detection
* More external research sources
* Advanced relevance scoring
* Personalized feeds
* Research trend visualization
* Notification system
* Source credibility scoring
* Multi-agent collaboration
* Persona-to-persona communication
* Historical research timelines
* Advanced analytics

---

# 🧩 Technology Stack

| Layer            | Technology                         |
| ---------------- | ---------------------------------- |
| Frontend         | HTML5, CSS3, JavaScript            |
| Backend          | Java, Spring Boot                  |
| APIs             | REST                               |
| Research Sources | Hacker News, arXiv                 |
| Build            | Maven                              |
| Version Control  | Git + GitHub                       |
| Deployment       | Appwrite                           |
| UI               | Custom Aurora / Universe interface |

---

# 🏆 Project Highlights

> **AI-Persona is not designed as a simple chatbot.**

It combines:

```text
Autonomous Agents
       +
Information Discovery
       +
Editorial Decision Making
       +
Generative AI
       +
Source-Based Research
       +
Full-Stack Engineering
       +
Modern Product UI
```

The central idea is to create an AI that doesn't simply answer questions.

It **develops a perspective, watches its domain, evaluates information, and continuously produces useful intelligence.**

---

# 📌 Project Status

🚧 **Active Development**

The project is currently being refined across:

* frontend experience
* autonomous agent behavior
* research pipeline
* content generation
* deployment
* production readiness

---

# 🤝 Contributing

Contributions, suggestions, and ideas are welcome.

If you'd like to contribute:

```bash
git clone <repository>

git checkout -b feature/your-feature

git add .

git commit -m "Add your feature"

git push origin feature/your-feature
```

Then open a Pull Request.

---

# 📄 License

This project is currently intended for educational, experimental, and research purposes.

Add an open-source license if you plan to distribute the project publicly.

---

# ⭐ AI-Persona

### Give an AI an identity.

### Give it a domain.

### Let it discover what matters.

**Build the persona.
Watch the signal.**
