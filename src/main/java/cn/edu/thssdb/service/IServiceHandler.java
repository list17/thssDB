package cn.edu.thssdb.service;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.rpc.thrift.*;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.Global;
import cn.edu.thssdb.parser.SQLParser;
import cn.edu.thssdb.parser.SQLLexer;
import cn.edu.thssdb.parser.Visitor;
import cn.edu.thssdb.statement.Statement;
import cn.edu.thssdb.exception.SQLThrowErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

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
        long sessionId = random.nextLong();
        ConnectResp resp = new ConnectResp();
        Status status = new Status();
        status.code = Global.SUCCESS_CODE;
        status.currentDatabase = "";
        resp.sessionId = sessionId;
        resp.status = status;
        this.manager.addConnection(sessionId, resp);
        System.out.println(sessionId);
        return resp;
    }

    @Override
    public DisconnetResp disconnect(DisconnetReq req) throws TException {
//        long sessionId = req.se
        return null;
    }

    @Override
    public ExecuteStatementResp executeStatement(ExecuteStatementReq req) throws TException{
        String command = req.statement;
        long sessionId = req.sessionId;
        // 解析命令
        String errorMessage = "success";
        QueryTable result = null;
        try{
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
            for (Statement statement: statements){
               result = statement.execute(this.manager, sessionId);
            }
        } catch (SQLHandleException e) {
            errorMessage = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Internal error";
        }
        ExecuteStatementResp executeStatementResp = new ExecuteStatementResp();
        executeStatementResp.status = this.manager.getConnection(sessionId).status;
        executeStatementResp.status.msg = errorMessage;
        if(result!=null && req.toString()!=null){
            executeStatementResp.status.result = result.toString();
            result = null;
        } else {
            executeStatementResp.status.result = "";
        }
//        executeStatementResp.rowList = result.rows;
        return executeStatementResp;
    }
}
