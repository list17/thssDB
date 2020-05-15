package cn.edu.thssdb.exception;

/**
 * 数据处理时出现的错误, 包括获取主键失败等.
 */
public class ExpressionHandleException extends RuntimeException {
    private String message;
    public enum ErrorCode {
        GENERAL("Exception: general"),
        INDEX_OVERFLOW("Exception: index overflow."),
        VALUE_OF_VARIABLE_NOT_FOUND("Exception: some variables of the expression have not been assigned."),
        AMBIGUOUS_VARIABLE("Exception: the variables in the expression are ambiguous.");

        private String message;
        ErrorCode(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
    private ErrorCode errorCode;

    public ExpressionHandleException() {
        this.errorCode = ErrorCode.GENERAL;
    }
    public ExpressionHandleException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}