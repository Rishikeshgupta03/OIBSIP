# Online Reservation System (Java + Swing + JDBC + SQLite)

**Oasis Infobyte — Java Development Internship — Task 1**

A complete, desktop GUI train-ticket reservation system built with
**Core Java, Swing, and JDBC/SQLite**. No Spring, no Hibernate, no
Maven/Gradle — a clean, layered, dependency-light application.

---

## 1. Project Description

This application simulates an online train reservation counter. A user
logs in, then from a dashboard can book a new ticket (auto-generating a
unique 10-digit PNR), look up an existing booking by PNR, or cancel a
booking — all backed by a local SQLite database accessed exclusively
through `PreparedStatement`-based DAO classes.

The codebase follows a classic layered architecture:

```
ui/        →  Swing screens (presentation layer, no SQL here)
dao/       →  Data Access Objects (all SQL lives here)
model/     →  Plain data classes (User, Train, Reservation)
database/  →  Connection management + schema/seed-data setup
util/      →  Cross-cutting helpers (validation, PNR generation, dates)
```

---

## 2. Features

-  **Login** — username/password authentication with a clear invalid-login message
-  **Dashboard** — Reserve Ticket / View Reservation / Cancel Reservation / Logout / Exit
-  **Reserve Ticket**
  - Passenger Name, Age, Gender, Train Number, auto-filled Train Name,
    Journey Date, Class, Source, Destination
  - Book / Reset / Back buttons
  - Full input validation before booking
  - Auto-generates a unique 10-digit PNR
  - Success dialog showing the new PNR
-  **View Reservation** — search by PNR, full details or "Reservation Not Found"
-  **Cancel Reservation** — fetch by PNR, confirmation dialog, delete, success message
-  **SQLite database** — auto-created tables (`Users`, `Trains`, `Reservations`),
  auto-seeded with a login user and 5 sample trains
-  **Train auto-fill** — typing a valid train number fills in the train name automatically
-  **Robust validation** — no empty fields, alphabets-only names, age 1–120,
  valid train numbers, journey date can't be in the past, source ≠ destination,
  duplicate PNRs are prevented
-  **Crash-proof** — every DB call and every input parse is wrapped in
  try/catch with friendly dialogs; the app never terminates unexpectedly

---

## 3. Technologies Used

| Technology            | Purpose                                      |
|------------------------|-----------------------------------------------|
| Java 17 (Java 11 compatible) | Core language                          |
| Swing                  | Desktop GUI                                   |
| JDBC                   | Database connectivity                         |
| SQLite (via sqlite-jdbc) | Embedded, file-based relational database   |
| DAO Pattern             | Clean separation between UI and SQL          |
| `java.time`             | Date parsing/validation/formatting          |
| Exception Handling      | `try/catch` around every DB call and every parse |

No Maven, no Gradle, no Spring, no Hibernate — pure Core Java plus the
single JDBC driver jar required to talk to SQLite (see Section 5).

---

## 4. Folder Structure

```
Java-Task1-OnlineReservationSystem/
│
├── src/
│   ├── Main.java                       # Entry point
│   ├── database/
│   │   ├── DBConnection.java           # Opens JDBC connections to reservation.db
│   │   └── DatabaseInitializer.java    # Creates tables + seeds sample data
│   ├── dao/
│   │   ├── UserDAO.java                # Login authentication queries
│   │   ├── TrainDAO.java               # Train lookup queries
│   │   └── ReservationDAO.java         # Booking / search / cancel queries
│   ├── model/
│   │   ├── User.java
│   │   ├── Train.java
│   │   └── Reservation.java
│   ├── ui/
│   │   ├── LoginFrame.java
│   │   ├── DashboardFrame.java
│   │   ├── ReservationFrame.java
│   │   ├── ViewReservationFrame.java
│   │   └── CancelReservationFrame.java
│   └── util/
│       ├── Validation.java             # Name/age/date/train-number rules
│       ├── PNRGenerator.java           # Random 10-digit PNR generator
│       └── DateUtil.java               # ISO <-> display date formatting
│
├── database/
│   ├── reservation.db                  # Pre-built SQLite DB (tables + seed data)
│   └── schema.sql                      # Reference DDL (for sqlite3 CLI users)
│
├── screenshots/                        # Add your own screenshots before submission
├── output/
│   └── sample_run_output.txt           # Captured sample run of the DAO layer
└── README.md
```

---

## 5. How to Run

### Step 1 — Get the SQLite JDBC driver

This project needs the `sqlite-jdbc` driver jar on the classpath (it is
the JDBC driver, not a framework — the task's tech stack explicitly
calls for JDBC + SQLite). Download it once:

```bash
curl -L -o sqlite-jdbc.jar \
  https://github.com/xerial/sqlite-jdbc/releases/download/3.53.2.0/sqlite-jdbc-3.53.2.0.jar
```

(Or grab any recent release from
`https://github.com/xerial/sqlite-jdbc/releases` and rename it to `sqlite-jdbc.jar`.)

### Step 2 — Compile

From the project root:

```bash
cd Java-Task1-OnlineReservationSystem
javac -cp sqlite-jdbc.jar -d out $(find src -name "*.java")
```

### Step 3 — Run

Run from the project root (so the relative `database/` folder is found):

```bash
java -cp sqlite-jdbc.jar:out Main
```

On Windows (PowerShell/cmd), use a semicolon instead of a colon in the classpath:

```powershell
java -cp "sqlite-jdbc.jar;out" Main
```

### Sample Login Credentials

| Username | Password |
|----------|----------|
| admin    | admin123 |

### Sample Trains (auto-seeded)

| Train Number | Train Name           |
|---------------|------------------------|
| 12301         | Rajdhani Express       |
| 12295         | Sanghamitra Express    |
| 12555         | Gorakhdham Express     |
| 22406         | Anand Vihar Express    |
| 12951         | Mumbai Rajdhani        |

A pre-built `database/reservation.db` is already included, so the app
works immediately — `DatabaseInitializer` also re-runs safely on every
startup (it only creates tables / seeds data that don't already exist).

---

## 6. Class Diagram

```
┌────────────┐        ┌─────────────────┐        ┌────────────────────┐
│   Main      │───────▶│   ui.LoginFrame  │───────▶│  ui.DashboardFrame  │
└────────────┘        └────────┬─────────┘        └──────────┬─────────┘
                                │ uses                        │ opens
                                ▼                              ▼
                       ┌────────────────┐      ┌──────────────────────────┐
                       │  dao.UserDAO    │      │ ui.ReservationFrame        │
                       └────────┬────────┘      │ ui.ViewReservationFrame    │
                                │                │ ui.CancelReservationFrame  │
                                ▼                └──────────┬────────────────┘
                       ┌────────────────┐                   │ uses
                       │ database.       │                   ▼
                       │ DBConnection    │◀─────┬───────────────────────────┐
                       └────────┬────────┘      │  dao.TrainDAO               │
                                │                │  dao.ReservationDAO         │
                                ▼                └──────────┬───────────────┘
                       ┌────────────────┐                   │
                       │ SQLite database │                   ▼
                       │ (reservation.db)│      ┌──────────────────────────┐
                       └────────────────┘      │ model.User                  │
                                                 │ model.Train                 │
                                                 │ model.Reservation           │
                                                 └──────────────────────────┘

               ┌───────────────────────────────────┐
               │ util.Validation / PNRGenerator /    │  (used by ui + dao layers)
               │ util.DateUtil                        │
               └───────────────────────────────────┘
```

---

## 7. Sample Output

See `output/sample_run_output.txt` for a captured run exercising every
DAO operation end-to-end (login success/failure, train auto-fill
lookup, PNR generation, booking, view, cancel) against the real SQLite
database — confirming the full data layer works correctly.

---

## 8. Project Explanation

- **`Main.java`** initializes the database (creating tables and seeding
  sample data if needed) before showing any UI, and wraps startup in a
  try/catch so a database problem never crashes with a raw stack trace.
- **`database.DBConnection`** is the single place that knows the JDBC
  URL and how to open a connection; every DAO reuses it.
- **`database.DatabaseInitializer`** is idempotent — safe to call on
  every launch. It creates `Users`, `Trains`, `Reservations` with
  `CREATE TABLE IF NOT EXISTS`, and seeds an admin user + 5 trains only
  if they don't already exist.
- **DAO classes** (`UserDAO`, `TrainDAO`, `ReservationDAO`) are the
  *only* classes that contain SQL, and every statement uses
  `PreparedStatement` — no string concatenation anywhere, which
  prevents SQL injection by construction.
- **`ui` classes** never touch SQL directly; they call DAO methods and
  translate results/exceptions into `JOptionPane` dialogs. This keeps
  the presentation layer swappable (e.g. you could add a web UI later
  without touching the DAO or database layers at all).
- **`util.Validation`** centralizes every input rule (name pattern, age
  range, train-number format, journey-date-not-in-past, source ≠
  destination) so both the Reservation form and any future screen can
  reuse the exact same rules.
- **`util.PNRGenerator`** + **`ReservationDAO.generateUniquePnr()`**
  together guarantee a truly unique PNR: the DAO keeps generating
  random 10-digit codes and checking the database until it finds one
  that isn't already in use (collisions are astronomically rare, so
  this loop runs once in practice).

---

## 9. Possible Future Improvements

- Hash passwords (e.g. BCrypt) instead of storing them as plain text
- Add seat/berth availability tracking per train and journey date
- Add an "Update Reservation" screen (currently only book/view/cancel)
- Add pagination and a searchable list view for admins to browse all
  reservations
- Add unit tests (JUnit) for the `dao` and `util` packages
- Package as a runnable JAR with the SQLite driver bundled via
  `jar cfe` + `Class-Path` manifest entry, so `java -jar app.jar` works
  without a separate `-cp` flag

---

## 11. Author

Built for the **Oasis Infobyte Java Development Internship — Task 1: Online Reservation System**.
