package cn.iocoder.yudao.server.util;


import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 常用文件操作
 */

public class FileUtils {

    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static List<String> messageList = new ArrayList<>();

    private static List<String> conetntList = new ArrayList<>();

    private static Integer maxCount = 2000;

    private static Boolean isExisitDirectory = false;

    public static void writeLog(String directoryPath, String content) {
        if (StringUtil.isNotEmpty(directoryPath) && StringUtil.isNotEmpty(content)) {
            //判断目录是否存在
            if (!isExisitDirectory){
                File directory = new File(directoryPath);
                if (!directory.exists()) { // 如果目录不存在则进行创建
                    boolean created = directory.mkdirs();
                    if (!created) {
                        logger.error("无法创建目录:"+directoryPath);
                        return;
                    }
                }
                isExisitDirectory = true;
            }



            //写日志文件
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String fileName = directoryPath+"/"+System.currentTimeMillis()+".txt";
                    try{
                        FileWriter fileWriter = new FileWriter(fileName,true);
                        BufferedWriter writer = new BufferedWriter(fileWriter);
                        for(String ss:conetntList){
                            writer.write(ss);
                            writer.newLine();
                        }
                        writer.flush();
                        writer.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
