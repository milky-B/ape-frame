import com.airport.ape.tool.FileUtils;
import com.airport.ape.tool.Md5Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UtilTest {
    public static void traverseFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] fileArray = file.listFiles();
            if(fileArray==null){
                System.out.println("This isn't a directory");
                return;
            }
            for (File f : fileArray) {
                if (f.isDirectory()) {
                    System.out.println("【文件夹】-----" + f.getAbsolutePath());
                    // -----递归的方法体
                    traverseFile(f.getAbsolutePath());
                } else {
                    String name = f.getName();
                    System.out.println(name);
                    try {
                        String canonicalPath = f.getCanonicalPath();
                        System.out.println(canonicalPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("【文件】-----" + f.getAbsolutePath());
                }
            }
        } else {
            System.out.println("文件不存在！");
        }
    }
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

    public static void main(String[] args) {
        //参数"path"为：要遍历的文件根目录
        traverseFile("C:\\Users\\lee\\Desktop\\gdtransport");
        /*String s = zipDecompress("C:\\Users\\lee\\Desktop\\gdtransport.zip");
        System.out.println(s);*/
    }

}
