package cn.edu.thssdb.exception;

/**
 * 代表客户端与服务器之间的连接断开时发生异常
 */

public class DisconnectionException extends SQLHandleException {
    public DisconnectionException(long sessionId) {
        super("Exception: client:" + String.valueOf(sessionId) + " closed failed");
    }
}
