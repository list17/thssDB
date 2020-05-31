package cn.edu.thssdb.exception;

public class DuplicateKeyException extends SQLHandleException {
    /**
     * @param message 错误信息
     */
    public DuplicateKeyException() {
        super("Exception: insertion caused duplicated keys!");
    }
}
