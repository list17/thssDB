package cn.edu.thssdb.exception;

public class KeyNotExistException extends SQLHandleException {
    public KeyNotExistException() {
        super("Exception: key doesn't exist!");
    }
}