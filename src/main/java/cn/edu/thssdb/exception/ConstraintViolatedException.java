package cn.edu.thssdb.exception;

public class ConstraintViolatedException extends RuntimeException {
    public ConstraintViolatedException(String violatedType) {
        super("Exception: the constraint: " + violatedType + ", is violated.");
    }
}
