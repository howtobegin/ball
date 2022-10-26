package com.ball.common.test.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JimChery
 */
public class CreateAllSql {

    private static final String TARGET = "application-all.sql";

    public static void main(String[] args) {
        String parentPath = System.getProperty("user.dir");
        List<File> sqlFile = getAllSqlFile(parentPath);
        sqlFile.sort(((o1, o2) -> {
            if (o1.getName().startsWith("init")) {
                if (o2.getName().startsWith("init")) {
                    return 0;
                } else {
                    return 1;
                }
            } else if (o2.getName().startsWith("init")){
                return -1;
            }
            return 0;
        }));
        String targetFile = parentPath + "/doc/" + TARGET;
        try (FileOutputStream os = new FileOutputStream(targetFile)) {
            sqlFile.forEach(file -> {
                System.out.println(file.getAbsolutePath());
                // 首先注释sql来自哪个文件
                try {
                    os.write("-- ".getBytes());
                    os.write(file.getName().getBytes());
                    os.write("文件内语句\n\n".getBytes());
                    os.write(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                    os.write("\n\n\n\n".getBytes());
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<File> getAllSqlFile(String parentPath) {
        List<File> result = new ArrayList<>();
        File parent = new File(parentPath);
        findSql(parent, result);
        return result;
    }

    private static void findSql(File file, List<File> result) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return;
            for (File f : files) {
                findSql(f, result);
            }
        } else if (file.getName().endsWith(".sql")) {
            if (!file.getName().equals(TARGET) && !file.getName().equals("delete.sql")) {
                result.add(file);
            }
        }
    }

}
