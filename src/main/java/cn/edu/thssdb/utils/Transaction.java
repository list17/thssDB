package cn.edu.thssdb.utils;

import cn.edu.thssdb.schema.Manager;

import java.util.ArrayList;

public class Transaction {
    private ArrayList<String> scrtipts;
    private Long sessionId;

    public Transaction(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void addScript(String script) {
        this.scrtipts.add(script);
    }

    public void output(Manager manager, Long sessionId) {

    }
}
