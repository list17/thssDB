package cn.edu.thssdb.exception;

public class TypeMismatchException extends RuntimeException {
    private String violatedType;

    public TypeMismatchException(String violatedType) {
        this.violatedType = violatedType;
    }

    @Override
    public String getMessage() {
        return "Exception: the type: " + this.violatedType + ", of the entry could not match the type of the column.";
    }
}
