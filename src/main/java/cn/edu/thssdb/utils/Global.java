package cn.edu.thssdb.utils;

public class Global {
    public static int fanout = 129;

    public static int SUCCESS_CODE = 0;
    public static int FAILURE_CODE = -1;

    public static String DEFAULT_SERVER_ROOT = "data";
    public static String DEFAULT_SERVER_HOST = "127.0.0.1";
    public static int DEFAULT_SERVER_PORT = 6667;

    public static String DEFAULT_USER = "root";
    public static String DEFAULT_USER_PASSWORD = "123456";

    public static String CLI_PREFIX = "ThssDB>";
    public static final String SHOW_TIME = "show time;";
    public static final String QUIT = "quit;";

    public static int NO_LOCK = 0;
    public static int LOCK_S = 1;
    public static int LOCK_X = 2;

    public static int DEAD_LOCK_DELAY = 60000; // ms

    public static final String S_URL_INTERNAL = "jdbc:default:connection";
}
