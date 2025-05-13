# 🎵 Album & Artist Manager – Java + MySQL

Sistema console in Java per la gestione di un database musicale contenente informazioni su album e artisti.  
Il progetto implementa operazioni CRUD complete tramite JDBC, seguendo l’architettura DAO.

---

## 📁 Struttura del progetto

```
src/
├── app/                    → Avvio applicazione e menu interattivo (Main.java)
├── config/                 → Connessione al database (MySQLConnection.java)
├── dao/                    → Interfacce DAO e relative implementazioni
├── model/                  → Classi Java che rappresentano le entità Album e Artista
└── resources/              → (eventuali script SQL)
```

---

## 🗄️ Database

Il database si chiama `music` e contiene due tabelle: `album` e `artisti`.

- `album`: contiene informazioni su titolo, data di uscita, genere, artista collegato.
- `artisti`: contiene nome, nazione e anno di inizio attività.

### Esempio di schema (semplificato):

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

## ⚙️ Funzionalità

- Visualizzazione di tutti gli artisti o album
- Inserimento, modifica e cancellazione di artisti e album
- Ricerca per ID
- Collegamento tra album e artista (relazione molti-a-uno)

---

## 🧰 Tecnologie utilizzate

- Java
- JDBC
- MySQL
- DAO Pattern
- IDE: Visual Studio Code

---

## 🚀 Avvio del progetto

1. Clona il repository:
```bash
git clone https://github.com/tuo-username/music-crud-java.git
```

2. Crea il database `music` e importa le tabelle (vedi sezione SQL sopra).

3. Aggiorna i dati di connessione nel file `MySQLConnection.java`.

4. Compila ed esegui `Main.java`.

---

## 👤 Autore

Rosario Mirabella  
[LinkedIn](https://www.linkedin.com/in/rosario-mirabella-9a70b6194) • mirabellarosario@hotmail.com

---

## 📄 Licenza

Questo progetto è open source e distribuito sotto licenza MIT.
