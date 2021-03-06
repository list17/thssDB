package cn.edu.thssdb.service;

import cn.edu.thssdb.exception.DisconnectionException;
import cn.edu.thssdb.exception.ExpressionHandleException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.exception.SQLThrowErrorListener;
import cn.edu.thssdb.parser.SQLLexer;
import cn.edu.thssdb.parser.SQLParser;
import cn.edu.thssdb.parser.Visitor;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.rpc.thrift.*;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.statement.Statement;
import cn.edu.thssdb.utils.Global;
import cn.edu.thssdb.utils.TransactionManager;
import cn.edu.thssdb.utils.UserManager;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class IServiceHandler implements IService.Iface {

    private final Manager manager = new Manager();
    private final Random random = new Random();

    @Override
    public GetTimeResp getTime(GetTimeReq req) throws TException {
        GetTimeResp resp = new GetTimeResp();
        resp.setTime(new Date().toString());
        resp.setStatus(new Status(Global.SUCCESS_CODE, ""));
        return resp;
    }

    @Override
    public ConnectResp connect(ConnectReq req) throws TException {
        // TODO
        String username = req.username;
        String password = req.password;

        UserManager um = UserManager.getInstance();

        if (!um.login(username, password)) {
            ConnectResp resp = new ConnectResp();
            Status status = new Status();
            status.msg = "Wrong username or password.";
            status.code = Global.FAILURE_CODE;
            status.currentDatabase = "";
            resp.status = status;
            return resp;
        }

        long sessionId = random.nextLong();
        TransactionManager tm = TransactionManager.getInstance();
        tm.initFlag(sessionId);
        ConnectResp resp = new ConnectResp();
        Status status = new Status();
        status.msg = "Current user: " + username;
        status.code = Global.SUCCESS_CODE;
        status.currentDatabase = "";
        resp.sessionId = sessionId;
        resp.status = status;
        this.manager.addConnection(sessionId, resp);
        um.addSessionUser(sessionId, username);
        System.out.println(sessionId + " 已连接, user: " + username);
        return resp;
    }

    @Override
    public DisconnectResp disconnect(DisconnectReq req) {
        long sessionId = req.sessionId;
        DisconnectResp resp = new DisconnectResp();
        resp.status = this.manager.getConnection(sessionId).status;
        String errorMessage = "Exit successfully";
        try {
            manager.removeConnection(sessionId);
        } catch (DisconnectionException e) {
            errorMessage = e.getMessage();
        }
        resp.status.msg = errorMessage;
        System.out.println(sessionId + " 已断开");
        return resp;
    }

    @Override
    public ExecuteStatementResp executeStatement(ExecuteStatementReq req) throws TException {
        String command = req.statement;
        long sessionId = req.sessionId;

        String[] commands = command.split(";");
        // 解析命令
        String errorMessage = "success";
        QueryTable result = null;
        try {
            SQLThrowErrorListener listener = new SQLThrowErrorListener();
            SQLLexer lexer = new SQLLexer(CharStreams.fromString(command));
            lexer.removeErrorListeners();
            lexer.addErrorListener(listener);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SQLParser parser = new SQLParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(listener);
            ParseTree tree = parser.parse();
            // 遍历语法树
            Visitor visitor = new Visitor();
            ArrayList<Statement> statements = (ArrayList<Statement>) visitor.visit(tree);
            // 执行语句
            int i = 0;
            for (Statement statement : statements) {
                result = statement.execute(this.manager, sessionId, commands[i]);
                i++;
            }
        } catch (SQLHandleException e) {
            errorMessage = e.getMessage();
        } catch (ExpressionHandleException e) {
            errorMessage = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Internal error";
        }
        ExecuteStatementResp executeStatementResp = new ExecuteStatementResp();
        executeStatementResp.status = this.manager.getConnection(sessionId).status;
        executeStatementResp.status.msg = errorMessage;
        if (result != null && req.toString() != null) {
            executeStatementResp.columnsList = result.getColumns();
            executeStatementResp.rowList = result.getRows();
        } else {
            executeStatementResp.columnsList = null;
            executeStatementResp.rowList = null;
        }
        return executeStatementResp;
    }
}
