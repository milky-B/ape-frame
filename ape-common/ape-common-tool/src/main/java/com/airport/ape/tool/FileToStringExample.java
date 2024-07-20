package com.airport.ape.tool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileToStringExample {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\lee\\Desktop\\99950632886\\Pallet\\GZ99950632886T0001-12-20231205205043.json"; // 替换为实际文件路径

        try {
            String fileContent = readAll(filePath);
            System.out.println("File Content:\n" + fileContent);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static String readAll(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(path);
        return new String(fileBytes, StandardCharsets.UTF_8);
    }

    private static String lineFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        }

        return fileContent.toString();
    }
    private static String readByte(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        StringBuilder fileContent = new StringBuilder();

        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(path))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                // 将字节数组转换为字符串
                String chunk = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                fileContent.append(chunk);
            }
        }

        return fileContent.toString();
    }
}