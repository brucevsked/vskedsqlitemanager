package com.vsked.sqlitemanager.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileTool {

    private static final Logger log = LoggerFactory.getLogger(FileTool.class);

    public static void main(String[] args) {
        String projectRoot = System.getProperty("user.dir"); // 获取项目根目录
        String sourceRoot = projectRoot + "/src/main/java";   // Java 源码目录
        sourceRoot=sourceRoot.replace("\\","/");

        log.info(sourceRoot);

        Path rootDir = Paths.get(sourceRoot);
        Path outputFile = Paths.get("C:/logs/output-all-content.txt");

        try (Stream<Path> paths = Files.walk(rootDir)) {
            // 打开一个 BufferedWriter 来写入文件
            try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
                paths
                        .filter(path -> !Files.isDirectory(path)) // 只保留文件
                        .forEach(path -> {
                            try {
                                // 读取文件内容并写入输出文件
                                String content = new String(Files.readAllBytes(path));
                                //writer.write("===== File: " + path.toAbsolutePath() + " =====\n");
                                writer.write(content);
                                writer.write("\n\n"); // 空行分隔
                            } catch (IOException e) {
                                log.error("无法读取文件: " + path,e);
                            }
                        });
            }
            log.info("所有文件已成功写入: {}" , outputFile.toAbsolutePath());
        } catch (IOException e) {
            log.error("发生 IO 错误",e);
        }
    }

}
