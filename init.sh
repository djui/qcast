#!/bin/sh -e

sqlite3 sqlite.db "CREATE TABLE IF NOT EXISTS presentations" \
    "(id TEXT PRIMARY KEY NOT NULL, " \
    "cdate DATETIME DEFAULT(CURRENT_TIMESTAMP), " \
    "date DATETIME NOT NULL, " \
    "data BLOB NOT NULL)"
    
