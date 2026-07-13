-- =====================================================================
-- Online Reservation System - Database Schema
-- =====================================================================
-- This file documents the exact schema that database.DatabaseInitializer
-- creates automatically at application startup. You do not need to run
-- this file manually - it exists for reference and for anyone who wants
-- to inspect/recreate the database structure independently (e.g. with
-- the sqlite3 CLI: `sqlite3 reservation.db < schema.sql`).
-- =====================================================================

PRAGMA foreign_keys = ON;

-- ---------------------------------------------------------------------
-- Users: login credentials for accessing the application.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

-- ---------------------------------------------------------------------
-- Trains: master list of trains reservations can be booked against.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Trains (
    train_number TEXT PRIMARY KEY,
    train_name TEXT NOT NULL
);

-- ---------------------------------------------------------------------
-- Reservations: one row per booked ticket.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Reservations (
    pnr TEXT PRIMARY KEY,               -- unique 10-digit PNR
    passenger_name TEXT NOT NULL,
    age INTEGER NOT NULL,
    gender TEXT NOT NULL,
    train_number TEXT NOT NULL,
    train_name TEXT NOT NULL,
    journey_date TEXT NOT NULL,         -- ISO format: yyyy-MM-dd
    travel_class TEXT NOT NULL,         -- SL, 3A, 2A, 1A, CC, 2S
    source TEXT NOT NULL,
    destination TEXT NOT NULL,
    booking_date TEXT NOT NULL,         -- ISO format: yyyy-MM-dd
    FOREIGN KEY (train_number) REFERENCES Trains(train_number)
);

-- ---------------------------------------------------------------------
-- Seed data: default login user (username: admin / password: admin123)
-- ---------------------------------------------------------------------
INSERT OR IGNORE INTO Users (username, password) VALUES ('admin', 'admin123');

-- ---------------------------------------------------------------------
-- Seed data: 5 sample trains
-- ---------------------------------------------------------------------
INSERT OR IGNORE INTO Trains (train_number, train_name) VALUES ('12301', 'Rajdhani Express');
INSERT OR IGNORE INTO Trains (train_number, train_name) VALUES ('12295', 'Sanghamitra Express');
INSERT OR IGNORE INTO Trains (train_number, train_name) VALUES ('12555', 'Gorakhdham Express');
INSERT OR IGNORE INTO Trains (train_number, train_name) VALUES ('22406', 'Anand Vihar Express');
INSERT OR IGNORE INTO Trains (train_number, train_name) VALUES ('12951', 'Mumbai Rajdhani');
