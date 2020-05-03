package cn.edu.thssdb.exception;

public class FileNotExistException extends RuntimeException {
    private String path;

    public FileNotExistException(String path) {
        this.path = path;
    }

    @Override
    public String getMessage() {
        return "Exception: the data file: " + this.path + ", does not exist.";
    }
}
