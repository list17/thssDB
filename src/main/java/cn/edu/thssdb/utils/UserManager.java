package cn.edu.thssdb.utils;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {
    // 用户管理器单例对象
    private static UserManager instance = null;

    // 用户的session表
    private HashMap<Long, String> userMap;

    private UserManager() {
        this.userMap = new HashMap<Long, String>();
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


}
