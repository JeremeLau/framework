package com.guoguang.utils.filelogger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;


/**
 * Created by Jereme on 2018/8/21.
 */

public class FileLogger {
    private static final Logger log = Logger.getRootLogger();
    
    public static void initConfig(String logPath, String logFileName) {
        FileLogConfiguration logConfiger = new FileLogConfiguration();
        // 设置Log4j的输出路径文件夹
        
        File dirFile = new File(logPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        } // 文件夹准备

        // 其实这下面可以做成一个生成器的设计模式
        logConfiger.setFileName(logPath + logFileName);  // 设置文件名字
        logConfiger.setRootLevel(Level.DEBUG); // 设置调试等级

        logConfiger.setUseFileAppender(true); // 设置用户文件输出器
        logConfiger.setFilePattern("%d - [%p::%t] - %m%n"); // 设置文件输出模式
        logConfiger.setImmediateFlush(true); // 设置是否立即刷新
        logConfiger.setInternalDebugging(false);
        logConfiger.setMaxBackupSize(20); // 设置最大备份数量
        logConfiger.setMaxFileSize(1024 * 1024); // 设置最大文件数量

        logConfiger.setUseLogCatAppender(true);
        logConfiger.setLogCatPattern("%m%n");
        logConfiger.configure();
    }

    public static void v(String msg) {
        log.debug(buildMessage(msg));
    }

    public static void v(String msg, Throwable thr) {
        log.debug(buildMessage(msg), thr);
    }

    public static void d(String msg) {
        log.debug(buildMessage(msg));
    }

    public static void d(String msg, Throwable thr) {
        log.debug(buildMessage(msg), thr);
    }

    public static void i(String msg) {
        log.info(buildMessage(msg));
    }

    public static void i(String msg, Throwable thr) {
        log.info(buildMessage(msg), thr);
    }

    public static void w(String msg) {
        log.warn(buildMessage(msg));
    }

    public static void w(String msg, Throwable thr) {
        log.warn(buildMessage(msg), thr);
    }

    public static void e(String msg) {
        log.error(buildMessage(msg));
    }

    public static void e(String msg, Throwable thr) {
        log.error(buildMessage(msg), thr);
    }

    /**
     * 生成消息
     * @param msg
     * @return
     */
    private static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace()
                .getStackTrace()[2];

        return caller.getClassName() + "." +
                caller.getMethodName() + "(): " + msg;
    }
}
