package cn.edu.thssdb.exception;

/**
 * 用户创建、删除、权限错误
 */

public class UserManageException extends RuntimeException {
    /**
     * @param message 错误信息
     */
    public UserManageException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}