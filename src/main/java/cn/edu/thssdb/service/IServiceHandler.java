package cn.edu.thssdb.service;

import cn.edu.thssdb.rpc.thrift.ConnectReq;
import cn.edu.thssdb.rpc.thrift.ConnectResp;
import cn.edu.thssdb.rpc.thrift.DisconnetResp;
import cn.edu.thssdb.rpc.thrift.ExecuteStatementReq;
import cn.edu.thssdb.rpc.thrift.ExecuteStatementResp;
import cn.edu.thssdb.rpc.thrift.GetTimeReq;
import cn.edu.thssdb.rpc.thrift.GetTimeResp;
import cn.edu.thssdb.rpc.thrift.IService;
import cn.edu.thssdb.rpc.thrift.Status;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.Global;
import org.apache.thrift.TException;

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
        resp.sessionId = sessionId;
        resp.status = status;
        this.manager.addConnection(sessionId, resp);
        System.out.println(sessionId);
        return resp;
    }

    @Override
    public DisconnetResp disconnect(DisconnetResp req) throws TException {
        // TODO
        return null;
    }

    @Override
    public ExecuteStatementResp executeStatement(ExecuteStatementReq req) throws TException{
        // TODO
        return null;
    }
}
