package cn.edu.thssdb.exception;

/**
 * 代表客户端与服务器之间的连接异常断开
 */

public class SessionLostException extends RuntimeException{
    public SessionLostException(long sessionId) {
        super("Session of client:" + String.valueOf(sessionId) + " lost.");
    }
}