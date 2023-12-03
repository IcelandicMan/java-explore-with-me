package ru.practicum.util.exception;

import org.hibernate.JDBCException;

import java.sql.SQLException;

public class ConstraintViolationException extends JDBCException {
    private String constraintName;

    public ConstraintViolationException(String message, SQLException root, String constraintName) {
        super(message, root);
        this.constraintName = constraintName;
    }

    public ConstraintViolationException(String message, SQLException root, String sql, String constraintName) {
        super(message, root, sql);
        this.constraintName = constraintName;
    }

    public String getConstraintName() {
        return this.constraintName;
    }
}
