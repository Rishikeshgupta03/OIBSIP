# ATM Interface (Java Console Application)

**Oasis Infobyte — Java Development Internship — Task 3**

A complete, console-based ATM simulation built with **Core Java** and clean
Object-Oriented Programming principles. No GUI, no database, no external
libraries — just plain Java demonstrating solid software design.

---

## 1. Project Description

This project simulates a real-world ATM machine in the console. A user logs
in with a **User ID** and **PIN**, and can then perform standard banking
operations — deposits, withdrawals, transfers, balance inquiries, and
viewing transaction history — all backed by an in-memory "bank" of
predefined accounts.

The project is intentionally split into focused classes (`Account`, `Bank`,
`Transaction`, `ATM`, `Utils`, `Main`) so that data, business rules, and
console UI logic are cleanly separated — making the codebase easy to read,
extend, and reuse.

---

## 2. Features

-  **Secure login** with User ID + PIN, locked out after 3 failed attempts
-  **Balance Inquiry** — view current balance, formatted to 2 decimals
-  **Deposit Money** — with validation (amount must be > 0)
-  **Withdraw Money** — with validation (amount > 0 and ≤ balance)
-  **Transfer Money** — to any other predefined account, with full validation
-  **Transaction History** — every transaction logged with ID, timestamp,
  type, amount, resulting balance, and description, shown in a formatted table
-  **Account Details** — view customer name, user ID, account number, type, balance
-  **Logout** — return to the login screen without closing the app
-  **Exit** — gracefully terminate the application
-  **Robust input validation** everywhere (never crashes on bad input)
-  5 predefined sample accounts, pre-loaded with sample transactions

---

## 3. Technologies Used

| Technology                | Purpose                                  |
|----------------------------|-------------------------------------------|
| Java 17 (Java 11 compatible) | Core language                          |
| OOP (Encapsulation, Composition) | Application design                 |
| Collections (`ArrayList`, `HashMap`) | Storing accounts & transactions |
| `java.time` (`LocalDateTime`) | Transaction timestamps                |
| `Scanner`                  | Console input                            |
| Exception Handling (`try/catch`) | Crash-proof input & operations     |

No Maven, no Gradle, no external dependencies — pure Core Java.

---

## 4. Folder Structure

```
ATMInterface/
│
├── src/
│   ├── Main.java          # Entry point — seeds data & starts the app
│   ├── ATM.java            # Console UI: login flow & main menu operations
│   ├── Bank.java           # Manages accounts, authentication, transfers
│   ├── Account.java        # Represents a single customer account
│   ├── Transaction.java    # Represents a single transaction record
│   └── Utils.java          # Formatting & safe-input helper methods
│
├── screenshots/            # Add your own screenshots here before submission
├── output/                 # Sample console output (sample_run_output.txt)
└── README.md
```

---

## 5. Class Diagram

```
┌───────────────────┐        ┌───────────────────────┐
│       Main         │        │          ATM            │
│---------------------│        │---------------------------│
│ + main(String[])    │───────▶│ - bank: Bank              │
└───────────────────┘        │ - scanner: Scanner        │
                              │---------------------------│
                              │ + start()                 │
                              │ - login(): Account         │
                              │ - showMainMenu(Account)    │
                              │ - depositMoney(Account)    │
                              │ - withdrawMoney(Account)   │
                              │ - transferMoney(Account)   │
                              │ - transactionHistory(...)  │
                              │ - accountDetails(Account)  │
                              └────────────┬──────────────┘
                                           │ uses
                                           ▼
                              ┌───────────────────────┐
                              │          Bank            │
                              │---------------------------│
                              │ - accountsByUserId: Map    │
                              │ - accountsByAccountNumber  │
                              │---------------------------│
                              │ + addAccount(Account)      │
                              │ + authenticate(id, pin)    │
                              │ + findByUserId(id)         │
                              │ + findByAccountNumber(no)  │
                              │ + transfer(sender, no, amt)│
                              └────────────┬──────────────┘
                                           │ 1        *
                                           ▼
                              ┌───────────────────────┐
                              │        Account            │
                              │---------------------------│
                              │ - userId, pin              │
                              │ - accountNumber            │
                              │ - customerName             │
                              │ - balance                  │
                              │ - accountType               │
                              │ - transactionHistory: List  │
                              │---------------------------│
                              │ + deposit(amount, desc)    │
                              │ + withdraw(amount, desc)   │
                              │ + applyCredit(...)         │
                              │ + applyDebit(...)          │
                              │ + isPinCorrect(pin)        │
                              └────────────┬──────────────┘
                                           │ 1        *
                                           ▼
                              ┌───────────────────────┐
                              │      Transaction           │
                              │---------------------------│
                              │ - transactionId             │
                              │ - dateTime                  │
                              │ - type, amount               │
                              │ - availableBalance           │
                              │ - description                │
                              └───────────────────────┘

                              ┌───────────────────────┐
                              │         Utils              │
                              │  (static helper methods)   │
                              │  formatCurrency, formatDate,│
                              │  printHeader, printLine,    │
                              │  readSafeInt, readSafeDouble│
                              └───────────────────────┘
```

---

## 6. How to Run

### Compile
```bash
cd ATMInterface/src
javac *.java
```

### Run
```bash
java Main
```

### Sample Login Credentials

| User ID | PIN  | Account Number | Name           | Balance      |
|---------|------|-----------------|----------------|--------------|
| john01  | 1111 | 1000000001      | John Carter    | Rs. 15,000.00 |
| sara22  | 2222 | 1000000002      | Sara Williams  | Rs. 32,500.75 |
| amit99  | 3333 | 1000000003      | Amit Sharma    | Rs. 8,250.00  |
| lisa07  | 4444 | 1000000004      | Lisa Brown     | Rs. 120,000.00|
| raj45   | 5555 | 1000000005      | Raj Patel      | Rs. 500.50    |

A sample run's console output is saved in `output/sample_run_output.txt`.

---

## 7. Project Explanation

- **`Main.java`** wires everything together: it creates a `Bank`, seeds it
  with 5 sample accounts and a few demo transactions, then starts the `ATM`.
  Everything is wrapped in a top-level `try/catch` so the app can never
  crash with an unhandled stack trace.
- **`ATM.java`** owns the console UI only: printing menus, reading input,
  and calling into `Account`/`Bank` to perform the actual work. It never
  mutates balances directly.
- **`Bank.java`** owns the collection of accounts (two `HashMap`s for O(1)
  lookup by User ID and by Account Number) and any operation that spans
  more than one account, like `transfer()`.
- **`Account.java`** owns a single customer's data and enforces that the
  balance can only change through `deposit()`, `withdraw()`,
  `applyCredit()`, and `applyDebit()` — each of which automatically logs a
  `Transaction`, so the balance and the transaction history can never
  drift out of sync.
- **`Transaction.java`** is an immutable record of one transaction (all
  fields `final`), matching how a real bank never edits a past transaction.
- **`Utils.java`** centralizes formatting and "safe" input reading so that
  bad input (letters instead of numbers, empty strings, etc.) is handled
  in exactly one place instead of being duplicated across every menu option.

---

## 8. Viva / Interview Questions

1. **Why did you separate `Bank` and `Account` into two classes instead of one?**
   Single Responsibility Principle — `Account` manages one customer's data,
   while `Bank` manages the collection of all accounts and cross-account
   operations like transfers.

2. **Why does `Account` not expose a public `setBalance()` method?**
   To guarantee every balance change is logged as a `Transaction`. Allowing
   a direct setter would let the balance and history drift out of sync.

3. **Why use two `HashMap`s in `Bank` instead of one?**
   To get O(1) lookups both by `userId` (used during login) and by
   `accountNumber` (used during transfers), instead of scanning a list.

4. **How does the application prevent crashes on invalid input?**
   `Utils.readSafeInt()` / `readSafeDouble()` loop with `try/catch` around
   `Integer.parseInt` / `Double.parseDouble`, re-prompting on
   `NumberFormatException` instead of propagating it.

5. **How would you persist accounts across runs (add a database)?**
   Replace the in-memory `HashMap`s inside `Bank` with a repository
   interface backed by JDBC/a file, without changing `ATM.java` at all —
   this is the benefit of separating business logic from the UI layer.

6. **What OOP principles are demonstrated here?**
   Encapsulation (private fields + getters/setters), composition
   (`Account` has a `List<Transaction>`, `Bank` has `Account`s),
   abstraction (UI code depends only on public methods, not internals),
   and method overloading (`Transaction`'s two constructors).

7. **Why is `Transaction`'s `dateTime` field `final`?**
   A transaction record should be immutable once created — a real bank
   never edits a past transaction's timestamp.

8. **How is the 3-failed-login-attempt lockout implemented?**
   `ATM.login()` uses a countdown loop (`attemptsRemaining`), decrementing
   on every failed `bank.authenticate()` call, and returns `null` to signal
   the caller to terminate the application once attempts reach zero.

---

## 9. Possible Future Improvements

- Persist accounts and transactions to a file or real database (JDBC)
- Add PIN-change and new-account-registration features
- Add interest calculation for Savings accounts
- Encrypt PINs instead of storing them as plain text
- Add unit tests (JUnit) for `Bank` and `Account` business logic
- Build a JavaFX or Spring Boot + web front-end on top of the same `Bank`/`Account` model

---

## 10. Author

Built for the **Oasis Infobyte Java Development Internship — Task 3: ATM Interface**.
