import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class FileToStringExample {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\lee\\Desktop\\99950632886\\Pallet\\GZ99950632886T0001-12-20231205205043.json"; // 替换为实际文件路径

        try {
            String fileContent = readFileToString(filePath);
            System.out.println("File Content:\n" + fileContent);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static String readFileToString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(path);
        return new String(fileBytes, StandardCharsets.UTF_8);
    }
}