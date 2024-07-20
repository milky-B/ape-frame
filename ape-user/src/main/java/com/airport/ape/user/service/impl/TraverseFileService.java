package com.airport.ape.user.service.impl;

import com.airport.ape.user.entity.po.FileLog;
import com.airport.ape.user.mapper.FileLogMapper;
import io.swagger.annotations.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class TraverseFileService {
    @Autowired
    private FileLogMapper fileLogMapper;
    public void traverseFile(String path,Long waybill) throws IOException {
        //waybillId,去pallet找出托号
        File file = new File(path+"\\"+waybill);
        File[] fileArray = file.listFiles();
        if (fileArray == null) {
            throw new IllegalArgumentException("This isn't a directory");
        }
        String pallet=null;
        for (File f : fileArray) {
            if (f.isDirectory()&&"Pallet".equals(f.getName())) {
                Optional<File> first = Arrays.stream(f.listFiles()).findFirst();
                if(first.isPresent()){
                    String[] split = first.get().getName().split("-");
                    pallet = split[0];
                }
                break;
            }
        }
        if(!StringUtils.hasLength(pallet)){
            throw new IllegalArgumentException("文件夹缺失Pallet");
        }
        traverseFile(file, waybill,pallet);
    }
    public void traverseFile(File file,Long waybill,String pallet) throws IOException {
        if (file.exists()) {
            File[] fileArray = file.listFiles();
            for (File f : fileArray) {
                if (f.isDirectory()) {
                    traverseFile(f,waybill,pallet);
                } else {
                    //插入数据
                    FileLog fileLog = new FileLog();
                    fileLog.setPath(f.getCanonicalPath());
                    fileLog.setWaybill(waybill);
                    fileLog.setPallet(pallet);
                    fileLog.setCreateTime(new Date());
                    fileLogMapper.insert(fileLog);
                }
            }
        } else {
            throw new FileNotFoundException("找不到指定文件");
        }
    }

    public FileLog fileLog(Long waybillId){
        //path = path.replace("\\", "\\\\");
        return fileLogMapper.selectByWaybillId(waybillId);
    }
}
