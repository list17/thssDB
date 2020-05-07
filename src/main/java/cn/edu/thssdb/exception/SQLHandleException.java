package cn.edu.thssdb.exception;

/**
 * 代表一个能从服务端传递给客户端的错误。
 * 改错误如果出现需要告知客户端展示给用户
 */

public class SQLHandleException extends Exception {
    /**
     * @param message 错误信息
     */
    public SQLHandleException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}