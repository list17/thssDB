package cn.edu.thssdb.exception;

public class ConstraintViolatedException extends RuntimeException {
    private String violatedType;

    public ConstraintViolatedException(String violatedType) {
        this.violatedType = violatedType;
    }

    @Override
    public String getMessage() {
        return "Exception: the constraint: " + violatedType + ", is violated.";
    }
}
