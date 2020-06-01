package cn.edu.thssdb.utils;

public class ValueInstance {
    private static ValueInstance instance = null;
    private boolean isInit; // 初始化读取scripts标志

    private ValueInstance() {
        this.isInit = true;
    }

    public static ValueInstance getInstance() {
        if (instance == null) {
            synchronized (ValueInstance.class) {
                if (instance == null) {
                    instance = new ValueInstance();
                }
            }
        }
        return instance;
    }

    public boolean getIsInit() {
        return this.isInit;
    }

    public void changeIsInit() {
        this.isInit = !this.isInit;
    }
}
