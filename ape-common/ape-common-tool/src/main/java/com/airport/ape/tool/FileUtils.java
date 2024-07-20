package com.airport.ape.tool;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

    public static String getCanonicalPath(String pathName) throws Exception {
        File directory = new File(pathName);
        return directory.getCanonicalPath();
    }
    public static boolean isCompressedFile(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            return false;
        }
        try (InputStream is = new FileInputStream(filePath)) {
            byte[] magicNumbers = new byte[4];
            int bytesRead = is.read(magicNumbers);
            if (bytesRead >= 2 && magicNumbers[0] == (byte) 0x50 && magicNumbers[1] == (byte) 0x4B) {
                // ZIP 文件
                return true;
            } else {
                throw new IllegalArgumentException("该文件非zip压缩文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean findFile(String parent, String id) throws IOException, IllegalArgumentException {
        File file = new File(parent);
        if (file.exists()) {
            File[] fileArray = file.listFiles();
            if (fileArray == null) {
                throw new IllegalArgumentException("This isn't a directory");
            }
            for (File f : fileArray) {
                String[] split = f.getName().split("\\.");
                if (split[0].equals(id)) {
                    //是压缩文件先解压
                    String filePath = f.getCanonicalPath();
                    if (isCompressedFile(filePath)) {
                        zipDecompress(filePath);
                        //修改文件名避免下次请求再次解压
                        /*String newName = f.getParent() + "\\zip_" + f.getName();
                        reName(f.getAbsolutePath(), newName);*/
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 解压缩ZIP文件
     *
     * @param zipFilePath ZIP文件路径
     */
    public static String zipDecompress(String zipFilePath) {
        File zipFile = new File(zipFilePath);
        String[] split = zipFilePath.split("\\.");
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                File file = new File(split[0], entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return split[0];
    }

    public static boolean reName(String absolutePathName, String newAbsoluteName) {
        File file = new File(absolutePathName);
        if (file == null)
            return false;
        if (!file.exists()) {
            System.out.println("file " + absolutePathName + " doesn't exits!");
            return false;
        } else {
            file.renameTo(new File(newAbsoluteName));
            return true;
        }
    }

    public static JSONObject jsonFileToObject(String filepath) throws IOException {
        String fileContent = readAll(filepath);
        return JSONObject.parseObject(fileContent);
    }

    private static String readAll(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(path);
        return new String(fileBytes, StandardCharsets.UTF_8);
    }
}