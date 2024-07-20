package com.airport.ape.user.controller;

import com.airport.ape.user.entity.po.AuthReportDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {
    @PostMapping
    public Object fileUpload(AuthReportDto authReportDto, MultipartFile reportFile,MultipartFile msdsFile,MultipartFile imgFile){

        return authReportDto.toString() + "\n"+
                reportFile.getOriginalFilename()+"\n" +
                msdsFile.getOriginalFilename()+ "\n" +
                imgFile.getOriginalFilename() +"\n";
    }
}
