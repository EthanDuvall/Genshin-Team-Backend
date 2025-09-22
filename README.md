# Genshin Team Builder API

A Spring Boot REST API for generating optimized Genshin Impact teams based on owned characters and a chosen core
character.  
This project uses a JSON-based database for character storage and team generation logic.

---

## 🚀 Features

- **List Characters** – Fetch all available characters from the database.
- **Generate Teams** – Provide a core character and your owned characters to generate recommended teams.
- **JSON Database** – Reads character and team data from JSON files instead of a traditional SQL database.

---

## ⚙️ Endpoints

### 1. Get All Characters

**Request:**

```http
GET /genshinBuilder/characters
```

**Response:**

```json
{
  "48": {
    "id": 48,
    "name": "Nahida",
    "element": "Dendro",
    "rarity": "five",
    "icon": "/image/nahida.webp",
    "roles": [
      "support",
      "mainDps",
      "offDps"
    ]
  },
  "49": {
    "id": 49,
    "name": "Navia",
    "element": "Geo",
    "rarity": "five",
    "icon": "/image/navia.webp",
    "roles": [
      "mainDps"
    ]
  }
}
```

---

### 2. Generate Teams

**Request:**

```http
POST /genshinBuilder/generate
Content-Type: application/json

{
  "coreId": 1,
  "ownedCharacters": [1, 2, 3, 4, 5]
}
```

**Response:**

```json
[
  {
    "reaction": "Melt",
    "members": {
      "Pyro": {
        "id": 5,
        "name": "Hu Tao"
      },
      "Hydro": {
        "id": 6,
        "name": "Xingqiu"
      },
      "Flex": {
        "id": 1,
        "name": "Albedo"
      }
    }
  },
  {
    "reaction": "Vape",
    "members": {
      "Pyro": {
        "id": 5,
        "name": "Hu Tao"
      },
      "Hydro": {
        "id": 6,
        "name": "Xingqiu"
      },
      "Flex": {
        "id": 2,
        "name": "Alhaitham"
      }
    }
  }
]
```

---

## 🛠️ Project Structure

```
genshinteambuillder/
├── genshinTeamBuilderApi/
│   ├── GenshinApiController.java   # REST endpoints
│   ├── JsonDatabaseService.java    # JSON database logic
│   ├── Character.java              # Character model
│   ├── Team.java                   # Team model
│   └── ...
├── resources/
│   ├── characters.json             # Character data
│   ├── reactions.json              # Reaction/team rules
│   └── ...
```

---

## ▶️ Running the Project

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/genshin-team-builder.git
   cd genshin-team-builder
   ```

2. Build & run with Gradle:
   ```bash
   ./gradlew bootRun
   ```

3. API will be available at:
   ```
   http://localhost:8080/genshinBuilder
   ```

---

## 📌 Example Usage

- Get all characters:
  ```bash
  curl http://localhost:8080/genshinBuilder/characters
  ```

- Generate teams:
  ```bash
  curl -X POST http://localhost:8080/genshinBuilder/generate        -H "Content-Type: application/json"        -d '{"coreId": 1, "ownedCharacters": [1,2,3,4]}'
  ```

---

## 📂 Sample Data

### `characters.json`

```json
{
  "1": {
    "id": 1,
    "name": "Albedo",
    "element": "Geo",
    "rarity": "five",
    "roles": [
      "shielder",
      "offDps"
    ]
  },
  "2": {
    "id": 2,
    "name": "Alhaitham",
    "element": "Dendro",
    "rarity": "five",
    "roles": [
      "onFieldDps"
    ]
  },
  "5": {
    "id": 5,
    "name": "Hu Tao",
    "element": "Pyro",
    "rarity": "five",
    "roles": [
      "onFieldDps"
    ]
  },
  "6": {
    "id": 6,
    "name": "Xingqiu",
    "element": "Hydro",
    "rarity": "four",
    "roles": [
      "subDps",
      "support"
    ]
  }
}
```

### `reactions.json`

```json
{
  "Melt": {
    "Pyro": [
      5
    ],
    "Hydro": [
      6
    ],
    "Flex": [
      1
    ]
  },
  "Vape": {
    "Pyro": [
      5
    ],
    "Hydro": [
      6
    ],
    "Flex": [
      2
    ]
  }
}
```

---

## 📖 Notes

- Ensure `characters.json` and `reactions.json` are placed in `src/main/resources/`.
- This project is intended for educational and fan-use only (non-commercial).  
