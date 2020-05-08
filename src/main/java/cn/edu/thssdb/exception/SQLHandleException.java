package cn.edu.thssdb.exception;

/**
 * 代表一个能从服务端传递给客户端的错误。
 * 该错误包含了大多数无须再次细分的异常，如创建表失败、删除失败等等。
 * 该错误如果出现需要告知客户端展示给用户
 */

public class SQLHandleException extends RuntimeException {
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