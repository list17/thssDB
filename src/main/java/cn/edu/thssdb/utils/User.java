package cn.edu.thssdb.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String username;
    private String password;
    private ArrayList<String> create;
    private ArrayList<String> readAuth;
    private ArrayList<String> writeAuth;

    public User(String username, String password) {
        this.username = username;
        this.password = password;

        this.create = new ArrayList<String>();
        this.readAuth = new ArrayList<String>();
        this.writeAuth = new ArrayList<String>();
    }

    public void addCreate(String db_name) {
        if (!this.create.contains(db_name)) {
            this.create.add(db_name);
        }
    }

    public void addReadAuth(String db_name) {
        if (!this.readAuth.contains(db_name)) {
            this.readAuth.add(db_name);
        }
    }

    public void addWriteAuth(String db_name) {
        if (!this.writeAuth.contains(db_name)) {
            this.writeAuth.add(db_name);
        }
    }

    public boolean checkReadAuth(String db_name) {
        if (this.create.contains(db_name)) {
            return true;
        } else if (this.writeAuth.contains(db_name)) {
            return true;
        } else return this.readAuth.contains(db_name);
    }

    public boolean checkWriteAuth(String db_name) {
        if (this.create.contains(db_name)) {
            return true;
        } else return this.writeAuth.contains(db_name);
    }

    public boolean checkCreate(String db_name) {
        return this.create.contains(db_name);
    }

    public void deleteRead(String db_name) {
        for (int i = 0; i < this.readAuth.size(); i++) {
            if (this.readAuth.get(i).equals(db_name)) {
                this.readAuth.remove(i);
                i--;
            }
        }
    }

    public void deleteWrite(String db_name) {
        for (int i = 0; i < this.writeAuth.size(); i++) {
            if (this.writeAuth.get(i).equals(db_name)) {
                this.writeAuth.remove(i);
                i--;
            }
        }
    }

    public void deleteCreate(String db_name) {
        for (int i = 0; i < this.create.size(); i++) {
            if (this.create.get(i).equals(db_name)) {
                this.create.remove(i);
                i--;
            }
        }
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
