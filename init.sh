#!/bin/sh -e

sqlite3 sqlite.db "CREATE TABLE IF NOT EXISTS presentation (id TEXT PRIMARY KEY NOT NULL, cdate DATETIME, data BLOB)"
