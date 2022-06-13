package com.olegator555.rasp.DB.model;

public class DBArgs {
    private String name;
    private String sqlType;
    private Boolean primaryKey;

    public DBArgs(String name, String sqlType, Boolean primaryKey) {
        this.name = name;
        this.sqlType = sqlType;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
