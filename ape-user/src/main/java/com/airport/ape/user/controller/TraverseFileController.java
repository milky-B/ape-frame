package com.airport.ape.user.controller;

import com.airport.ape.user.entity.dto.FileLogDto;
import com.airport.ape.user.entity.po.FileLog;
import com.airport.ape.user.service.impl.TraverseFileService;
import com.airport.ape.web.entity.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("logFile")
public class TraverseFileController {
    @Autowired
    private TraverseFileService traverseFileService;

    //private static final String FILE_PATH="D:\\gdtransport";
    /*
    * {
        "path":"C:\\Users\\lee\\Desktop",
        "waybillId":99950632886
    }
    *
    * */
    /*@PostMapping
    public Object log(@RequestBody @ApiParam FileLogDto dto) {
        try {
            if (dto.getWaybillId() == null) {
                throw new IllegalArgumentException("id不能为空");
            }
            if (!StringUtils.hasLength(dto.getPath().trim())) {
                throw new IllegalArgumentException("检查路径");
            }
            //找到id对应的文件夹
            String filePath = findFile(dto.getPath(), String.valueOf(dto.getWaybillId()));
            if (filePath == null) {
                throw new IllegalArgumentException("文件不存在");
            }
            //查看是否已记录
            List<FileLog> fileLogs = traverseFileService.fileLog(filePath);
            if (!CollectionUtils.isEmpty(fileLogs)) {
                return Result.success();
            }
            //遍历filePath 记录所有文件名
            traverseFileService.traverseFile(filePath);
        } catch (IllegalArgumentException e) {
            Result.fail(e.getMessage());
        } catch (IOException e) {
            Result.fail("检查文件路径");
        }
        return Result.success();
    }*/
    //http://localhost:8080/logFile?path=C:\Users\lee\Desktop&waybillId=99950632886
    @GetMapping
    public Object log(@RequestParam("path") String path,@RequestParam("waybill") Long waybill) {
        try {
            if (path == null) {
                throw new IllegalArgumentException("id不能为空");
            }
            if (!StringUtils.hasLength(path.trim())) {
                throw new IllegalArgumentException("检查路径");
            }
            //查看是否已记录
            FileLog fileLogs = traverseFileService.fileLog(waybill);
            if (fileLogs!=null) {
                return Result.success();
            }
            //找到id对应的文件夹
            if (!findFile(path, String.valueOf(waybill))) {
                throw new IllegalArgumentException("指定文件不存在");
            }
            //遍历filePath 记录所有文件名
            traverseFileService.traverseFile(path,waybill);
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (IOException e) {
            return Result.fail("检查文件路径");
        }
        return Result.success();
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
                        filePath = zipDecompress(filePath);
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
}
