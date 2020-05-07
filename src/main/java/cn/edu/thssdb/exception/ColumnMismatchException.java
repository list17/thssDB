package cn.edu.thssdb.exception;

public class ColumnMismatchException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Exception: Column mismatch.";
    }
}
