package cn.edu.thssdb.exception;

public class ColumnMismatchException extends SQLHandleException {
    public ColumnMismatchException() {
        super("Exception: Column mismatch.");
    }
}
