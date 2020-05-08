package cn.edu.thssdb.exception;

/**
 * 代表无法在本地找到数据库文件
 */
public class FileNotExistException extends SQLHandleException {
    public FileNotExistException(String path) {
        super("Exception: the data file: " + path + ", does not exist.");
    }
}
