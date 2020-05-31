package cn.edu.thssdb.exception;

import java.io.IOException;

/**
 * 写文件报错
 */

public class FileWriteException extends SQLHandleException {
    /**
     * @param message 错误信息
     */
    public FileWriteException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}