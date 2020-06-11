package cn.edu.thssdb.client;

import cn.edu.thssdb.rpc.thrift.*;
import cn.edu.thssdb.utils.Global;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.Scanner;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    static final String HOST_ARGS = "h";
    static final String HOST_NAME = "host";

    static final String HELP_ARGS = "help";
    static final String HELP_NAME = "help";

    static final String PORT_ARGS = "p";
    static final String PORT_NAME = "port";

    static final String USER_NAME = "u";
    static final String USER_PASS = "w";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static final PrintStream SCREEN_PRINTER = new PrintStream(System.out);
    private static final Scanner SCANNER = new Scanner(System.in);

    private static TTransport transport;
    private static TProtocol protocol;
    private static IService.Client client;
    private static CommandLine commandLine;
    private static long sessionId;

    public static void main(String[] args) {
        commandLine = parseCmd(args);
        if (commandLine.hasOption(HELP_ARGS)) {
            showHelp();
            return;
        }
        try {
            echoStarting();
            String host = commandLine.getOptionValue(HOST_ARGS, Global.DEFAULT_SERVER_HOST);
            String username = commandLine.getOptionValue(USER_NAME, Global.DEFAULT_USER);
            String password = commandLine.getOptionValue(USER_PASS, Global.DEFAULT_USER_PASSWORD);
            int port = Integer.parseInt(commandLine.getOptionValue(PORT_ARGS, String.valueOf(Global.DEFAULT_SERVER_PORT)));
            transport = new TSocket(host, port);
            transport.open();
            protocol = new TBinaryProtocol(transport);
            client = new IService.Client(protocol);
            boolean open = false;
//            ConnectResp resp = client.connect(new ConnectReq(args[0], args[1]));
            ConnectResp resp = client.connect(new ConnectReq("asdf", "123456"));
            if (resp.status.code == Global.SUCCESS_CODE){
                sessionId = resp.sessionId;
                open = true;
            }
            if (resp.status.code == Global.FAILURE_CODE) {
                println(ANSI_RED + resp.status.msg + ANSI_RESET);
                return;
            }
            while (true) {
                print(Global.CLI_PREFIX);
                String msg = SCANNER.nextLine();
                long startTime = System.currentTimeMillis();
                switch (msg.trim()) {
                    case Global.SHOW_TIME:
                        getTime();
                        break;
                    case Global.QUIT:
                        DisconnectReq req = new DisconnectReq(resp.sessionId);
                        DisconnectResp resp1 = client.disconnect(req);
                        println(ANSI_RED + resp1.status.msg + ANSI_RESET);
                        open = false;
                        break;
                    default:
                        ExecuteStatementReq executeStatementReq = new ExecuteStatementReq();
                        executeStatementReq.statement = msg;
                        executeStatementReq.sessionId = resp.sessionId;
                        ExecuteStatementResp executeStatementResp = client.executeStatement(executeStatementReq);
                        if(executeStatementResp.status.msg.equals("success")){
                            println(ANSI_GREEN + executeStatementResp.status.msg + ANSI_RESET);
                        } else {
                            println(ANSI_RED + executeStatementResp.status.msg + ANSI_RESET);
                        }
                        println(executeStatementResp.status.result);
                        break;
                }
                long endTime = System.currentTimeMillis();
                println(ANSI_BLUE + "It costs " + (endTime - startTime) + " ms." + ANSI_RESET);
                if (!open) {
                    break;
                }
            }
            transport.close();
        } catch (TTransportException e) {
            logger.error(e.getMessage());
        } catch (TException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void getTime() {
        GetTimeReq req = new GetTimeReq();
        try {
            println(client.getTime(req).getTime());
        } catch (TException e) {
            logger.error(e.getMessage());
        }
    }

    static Options createOptions() {
        Options options = new Options();
        options.addOption(Option.builder(HELP_ARGS)
                .argName(HELP_NAME)
                .desc("Display help information(optional)")
                .hasArg(false)
                .required(false)
                .build()
        );
        options.addOption(Option.builder(HOST_ARGS)
                .argName(HOST_NAME)
                .desc("Host (optional, default 127.0.0.1)")
                .hasArg(false)
                .required(false)
                .build()
        );
        options.addOption(Option.builder(PORT_ARGS)
                .argName(PORT_NAME)
                .desc("Port (optional, default 6667)")
                .hasArg(false)
                .required(false)
                .build()
        );
        return options;
    }

    static CommandLine parseCmd(String[] args) {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            println("Invalid command line argument!");
            System.exit(-1);
        }
        return cmd;
    }

    static void showHelp() {
        // TODO
        println("DO IT YOURSELF");
    }

    static void echoStarting() {
        println("----------------------");
        println("Starting ThssDB Client");
        println("----------------------");
    }

    static void print(String msg) {
        SCREEN_PRINTER.print(msg);
    }

    static void println() {
        SCREEN_PRINTER.println();
    }

    static void println(String msg) {
        SCREEN_PRINTER.println(msg);
    }
}
