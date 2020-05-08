package cn.edu.thssdb.exception;

public class TypeMismatchException extends SQLHandleException {
    public TypeMismatchException(String violatedType) {
        super("Exception: the type: " + violatedType + ", of the entry could not match the type of the column.");
    }
}
