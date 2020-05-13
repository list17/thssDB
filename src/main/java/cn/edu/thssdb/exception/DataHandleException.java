package cn.edu.thssdb.exception;

/**
 * 数据处理时出现的错误, 包括获取主键失败等.
 */
public class DataHandleException extends RuntimeException {
    private String message;
    public enum ErrorCode {
        GENERAL("Exception: general"),
        INDEX_OVERFLOW("Exception: index overflow."),
        MULTIENTRY_LENGTH_MISMATCH(" Exception: the lengths of two multientry are not the same.");

        private String message;
        private ErrorCode(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
    private ErrorCode errorCode;

    public DataHandleException() {
        this.errorCode = ErrorCode.GENERAL;
    }
    public DataHandleException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}