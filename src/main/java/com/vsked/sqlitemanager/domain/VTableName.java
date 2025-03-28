package com.vsked.sqlitemanager.domain;

import java.util.regex.Pattern;

public class VTableName {
    private String tableName;

    public VTableName(String tableName) {
        if(tableName==null){
            throw new IllegalArgumentException("table name not be null！");
        }
        //TODO check min and max table name rule
        if(tableName.length()>63){
            throw new IllegalArgumentException("table name length not more than 63！");
        }

        String[] sqliteLimitTableName={"ABORT", "ACTION", "ADD", "AFTER", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC",
                "ATTACH", "AUTOINCREMENT", "BEFORE", "BEGIN", "BETWEEN",   "BY", "CASCADE", "CASE", "CAST", "CHECK",
                "COLLATE", "COMMIT", "CONFLICT", "CONSTRAINT", "CREATE",   "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "DATABASE",
                "DEFAULT", "DEFERRABLE", "DEFERRED", "DELETE", "DESC",     "DETACH", "DISTINCT", "DROP", "EACH", "ELSE",
                "END", "ESCAPE", "EXCEPT", "EXCLUSIVE", "EXISTS",           "EXPLAIN", "FAIL", "FOR", "FOREIGN", "FROM",
                "FULL", "GLOB", "GROUP", "HAVING", "IF",                "IGNORE", "IMMEDIATE", "IN", "INDEX", "INDEXED",
                "INITIALLY", "INNER", "INSERT", "INSTEAD", "INTERSECT",  "INTO", "IS", "ISNULL", "JOIN", "KEY",
                "LEFT", "LIKE", "LIMIT", "MATCH", "NATURAL",        "NO", "NOT", "NOTNULL", "NULL", "OF",
                "OFFSET", "ON", "OR", "ORDER", "OUTER",             "PLAN", "PRAGMA", "PRIMARY", "QUERY", "RAISE",
                "RECURSIVE", "REFERENCES", "REGEXP", "REINDEX", "RELEASE",   "RENAME", "REPLACE", "RESTRICT", "RIGHT", "ROLLBACK",
                "ROW", "SAVEPOINT", "SELECT", "SET", "TABLE",     "TEMP", "TEMPORARY", "THEN", "TO", "TRANSACTION",
                "TRIGGER", "UNION", "UNIQUE", "UPDATE", "USING",   "VACUUM", "VALUES", "VIEW", "VIRTUAL", "WHEN"   };
//
//        Pattern pattern = Pattern.compile("[a-zA-Z][0-9a-zA-Z_]{62}");
//        boolean validTableName=pattern.matcher(tableName).matches();
//
//        if(!validTableName){
//            throw new IllegalArgumentException("table name valid fail");
//        }

        //TODO sqlite key word valid

        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
