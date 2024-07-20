package com.airport.ape.tool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: leen
 * @Description: 动态配置读取
 * @Date: 2023/12/29 16:22
 */
@Slf4j
public class PropertiesUtils {
    private Map<String, Properties> propertiesMap = new HashMap<>();
    private Map<String, Long> modifyTimeMap = new HashMap<>();

    private String basePath = "";

    public void setBasePath(String path) {
        this.basePath = path;
    }

    private PropertiesUtils() {
    }

    private static class PropertiesUtilsSingleton {
        private static PropertiesUtils instance = new PropertiesUtils();
    }

    public static PropertiesUtils getInstance() {
        return PropertiesUtilsSingleton.instance;
    }

    public String getPropertyValue(String fileName, String key) {
        fileName = convertFileName(fileName);
        Properties properties = propertiesMap.get(fileName);
        try {
            if (properties == null) {
                loadProperties(fileName);
            } else {
                checkProperties(fileName);
            }
        } catch (IOException e) {
            log.error("IOException{}",e.getMessage(),e);
            return "";
        } catch (URISyntaxException e) {
            log.error("URISyntaxException{}",e.getMessage(),e);
            return "";
        }
        return propertiesMap.get(fileName).getProperty(key);
    }

    private void checkProperties(String fileName) throws URISyntaxException, IOException {
        File propertiesFile = getPropertiesFile(fileName);
        if (propertiesFile ==null||!propertiesFile.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        if (propertiesFile.lastModified() > modifyTimeMap.get(fileName)) {
            loadProperties(fileName);
        }
    }

    private String convertFileName(String fileName) {
        if (fileName.endsWith(".properties")) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName;
    }

    private void loadProperties(String fileName) throws URISyntaxException, FileNotFoundException {
        File file = getPropertiesFile(fileName);
        if(file==null||!file.exists()){
            throw new FileNotFoundException("文件不存在");
        }
        Long lastModifyTime = file.lastModified();

        if (propertiesMap.get(fileName) != null) {
            propertiesMap.remove(fileName);
        }

        try(FileInputStream fileInputStream = new FileInputStream(file)){
            Properties properties = new Properties();
            properties.load(fileInputStream);
            propertiesMap.put(fileName, properties);
            modifyTimeMap.put(fileName, lastModifyTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getPropertiesFile(String fileName) throws URISyntaxException {
        if (StringUtils.isNotBlank(basePath)) {
            return new File(String.valueOf(Paths.get(basePath, fileName + ".properties")));
        }
        String filePath = System.getProperty("user.dir") + File.separator + fileName + ".properties";
        File file = new File(filePath);
        if (!file.exists()) {
            file = null;
            URL resource = PropertiesUtils.class.getResource("/" + fileName + ".properties");
            if (resource != null) {
                file = new File(resource.toURI());
            }
        }
        return file;
    }
}
