package com.airport.ape.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileCompressionChecker {
    public static boolean isCompressedFile(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            System.out.println("This is a directory.");
            return false;
        }
        try (InputStream is = new FileInputStream(filePath)) {
            byte[] magicNumbers = new byte[4];
            int bytesRead = is.read(magicNumbers);

            // 判断文件类型的魔数
            if (bytesRead >= 2 && magicNumbers[0] == (byte) 0x1F && magicNumbers[1] == (byte) 0x8B) {
                // GZIP 文件
                System.out.println("gzip");
                return true;
            } else if (bytesRead >= 2 && magicNumbers[0] == (byte) 0x50 && magicNumbers[1] == (byte) 0x4B) {
                // ZIP 文件
                System.out.println("zip");
                return true;
            } else if (bytesRead >= 3 && magicNumbers[0] == (byte) 0x1F && magicNumbers[1] == (byte) 0x9D && magicNumbers[2] == (byte) 0x90) {
                // TAR.Z 文件
                System.out.println("tar");
                return true;
            }

            // 添加其他压缩文件的魔数判断

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        String filePath = "D:\\89898";
        if (isCompressedFile(filePath)) {
            System.out.println("This is a compressed file.");
        } else {
            System.out.println("This is not a compressed file.");
        }
    }
}
