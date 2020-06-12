package cn.edu.thssdb.utils;

import cn.edu.thssdb.exception.KeyNotExistException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.index.BPlusTree;
import cn.edu.thssdb.schema.*;
import cn.edu.thssdb.type.ColumnType;
import com.sun.scenario.effect.impl.state.AccessHelper;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class UserManager {
    // 用户管理器单例对象
    private static UserManager instance = null;

    // 用户的session表
    private HashMap<Long, String> userSessionMap;

    // 用户Table
    private HashMap<String, User> userMap;

    private UserManager() {
        this.userMap = new HashMap<String, User>();
        this.userSessionMap = new HashMap<Long, String>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    // 验证密码
    public boolean login(String username, String password) {
        if (this.userMap.get(username) == null) {
            return false;
        }
        else {
            return this.userMap.get(username).checkPassword(password);
        }
    }

    // Connect后用户登录成功添加入userMap
    public void addSessionUser(Long sessionId, String username) {
        this.userSessionMap.put(sessionId, username);
    }

    // 获取当前sessionId对应的用户名
    public String getCurUsername(Long sessionId) {
        return this.userSessionMap.get(sessionId);
    }

    // 查找用户是否存在
    public boolean checkUserExist(Long sessionId) {
        return this.userMap.get(this.userSessionMap.get(sessionId)) != null;
    }

    public boolean checkUserExist(String username) {
        return this.userMap.get(username) != null;
    }

    public void createUser(String username, String password) {
        if (password.charAt(0) == '\'' && password.charAt(password.length() - 1) == '\'') {
            password = password.substring(1, password.length() -1);
        }
        User newUser = new User(username, password);
        this.userMap.put(username, newUser);
        this.serialize();
    }

    public void dropUser(String username) {
        this.userMap.remove(username);
        this.serialize();
    }

    // 初始化user并加入root用户(123456)
    public void initUserTable() {
        createUser(Global.DEFAULT_USER, Global.DEFAULT_USER_PASSWORD);
    }

    public boolean checkReadable(String db_name, Long sessionId) {
        User user = this.userMap.get(this.userSessionMap.get(sessionId));
        return user.checkReadAuth(db_name);
    }

    public boolean checkWritable(String db_name, Long sessionId) {
        User user = this.userMap.get(this.userSessionMap.get(sessionId));
        return user.checkWriteAuth(db_name);
    }

    public boolean checkAuthable(String db_name, Long sessionId) {
        User user = this.userMap.get(this.userSessionMap.get(sessionId));
        return user.checkCreate(db_name);
    }

    public void authRead(String db_name, String username) {
        User user = this.userMap.get(username);
        user.addReadAuth(db_name);
        this.serialize();
    }

    public void authWrite(String db_name, String username) {
        User user = this.userMap.get(username);
        user.addWriteAuth(db_name);
        this.serialize();
    }

    public void authCreate(String db_name, String username) {
        User user = this.userMap.get(username);
        user.addCreate(db_name);
        this.serialize();
    }

    public void deAuthRead(String db_name, String username) {
        User user = this.userMap.get(username);
        user.deleteRead(db_name);
        this.serialize();
    }

    public void deAuthWrite(String db_name, String username) {
        User user = this.userMap.get(username);
        user.deleteWrite(db_name);
        this.serialize();
    }

    // 删除数据库时调用，会遍历userMap删除所有用户的db_name
    public void deAuthCreate(String db_name) {
        for (HashMap.Entry<String, User> entry : this.userMap.entrySet()) {
            User tmp_user = entry.getValue();
            tmp_user.deleteRead(db_name);
            tmp_user.deleteWrite(db_name);
            tmp_user.deleteCreate(db_name);
        }
    }

    public void serialize() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("./user"));

            objectOutputStream.writeObject(new ArrayList<User>(this.userMap.values()));

            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLHandleException("Exception: Save user info failed.");
        }
    }

    public synchronized void deserialize() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("./user"));

            ArrayList<User> tmpUserList = (ArrayList<User>) objectInputStream.readObject();;

            for (User tmpUser : tmpUserList) {
//                System.out.println(tmpUser.getUsername() + " output " + tmpUser.getPassword());
                this.userMap.put(tmpUser.getUsername(), tmpUser);
            }

            objectInputStream.close();
        } catch (Exception e) {
//            e.printStackTrace();
//            throw new SQLHandleException("Exception: Load user info failed.");
        }
    }
}
