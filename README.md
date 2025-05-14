# 🎵 Music Manager – Java + MySQL

A console-based Java application for managing a music database containing information about albums and artists.  
The project implements full CRUD operations via JDBC, following a DAO architecture.

---

## 📁 Project Structure

```
src/
├── app/                    → Application entry point and menu interface (Main.java)
├── config/                 → Database connection setup (MySQLConnection.java)
├── dao/                    → DAO interfaces and implementations
├── model/                  → Java classes representing Album and Artist entities
└── resources/              → (optional config files or SQL scripts)
```

---

## 🗄️ Database

The database is named `music` and includes two related tables: `album` and `artisti`.

- `album`: stores information about title, release date, genre, and associated artist.
- `artisti`: stores artist name, country, and start year.

### Example schema (simplified):

```sql
CREATE TABLE artisti (
  artista_id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100),
  nazione VARCHAR(100),
  anno_inizio INT
);

CREATE TABLE album (
  album_id INT AUTO_INCREMENT PRIMARY KEY,
  titolo VARCHAR(100),
  data_uscita DATE,
  genere VARCHAR(20),
  artista_id INT,
  FOREIGN KEY (artista_id) REFERENCES artisti(artista_id)
);
```

---

## ⚙️ Features

- View all artists or albums
- Create, read, update, and delete artist and album entries
- Search by ID
- Relational link between albums and artists (many-to-one)

---

## 🧰 Technologies Used

- Java
- JDBC
- MySQL
- DAO Pattern
- IDE: Visual Studio Code

---

## 🚀 Getting Started

1. Clone the repository:
```bash
git clone https://github.com/Rosariomirabella/music.git
```

2. Create the `music` database and run the SQL schema (see above).

3. Update the DB credentials in `MySQLConnection.java`.

4. Compile and run `Main.java`.

---

## 📄 License

This project is open source and distributed under GPL-3.0 license.
