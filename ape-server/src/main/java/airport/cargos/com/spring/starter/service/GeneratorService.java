package airport.cargos.com.spring.starter.service;

import airport.cargos.com.spring.starter.bean.Dependency;
import airport.cargos.com.spring.starter.bean.ProjectZipPath;
import airport.cargos.com.spring.starter.bean.UserOptions;
import airport.cargos.com.spring.starter.constant.Result;
import airport.cargos.com.spring.starter.constant.StateEnum;
import airport.cargos.com.spring.starter.handler.AbstractGeneratorHandler;
import airport.cargos.com.spring.starter.mapper.DependencyMapper;
import airport.cargos.com.spring.starter.mapper.ProjectZipPathMapper;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GeneratorService extends AbstractGeneratorHandler<Object>  {

    @Autowired
    private DependencyMapper dependencyMapper;
    @Autowired
    private ProjectZipPathMapper projectZipPathMapper;

    public Object startDownload(UserOptions userOptions) {
        try {
             super.start(userOptions);
             log.info("response success");
        } catch (ServiceException e) {
            log.error("serviceException: {}",e.getMessage(),e);
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException {}", e.getMessage(),e);
        }catch (IOException e) {
            log.error("IoException: {}",e.getMessage(),e);
        } catch (Exception e){
            log.error("Exception: {}",e.getMessage(),e);
        }
        return null;
    }

    @Override
    protected Result returnZipToUser(String filePath) throws IOException {
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);

        HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getResponse();
        //指定内容为文件类型,浏览器提供下载操作
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        //以附件的方式展示
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);

        try(InputStream inputStream = new FileInputStream(filePath);
            ServletOutputStream outputStream = response.getOutputStream()){
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return null;
    }

    @Override
    public List<JSONObject> dependencies(UserOptions userOptions) {
        List<Long> parentIds = new ArrayList<>();
        List<JSONObject> jsonObjects = new ArrayList<>();
        if(CollectionUtils.isEmpty(userOptions.getIds())){
            return jsonObjects;
        }
        List<Dependency> dependencies = dependencyMapper.selectBatchIds(userOptions.getIds()).stream().filter(dependency -> !StateEnum.IS.getCode().equals(dependency.getIsParent())).collect(Collectors.toList());

        Long parentId = userOptions.getParentId();
        while(parentId !=null && parentId>0){
            Dependency dependency = dependencyMapper.selectById(parentId);
            if(Objects.isNull(dependency)){
                parentId = dependency.getParentId();
                if(parentId!=0){
                    parentIds.add(parentId);
                }
            }
        }
        JSONObject jsonObject = null;
        for(Dependency dependency: dependencies){
            jsonObject=new JSONObject();
            jsonObject.put("groupId",dependency.getGroupId());
            jsonObject.put("artifactId",dependency.getArtifactId());
            if(!parentIds.contains(dependency.getParentId())){
                jsonObject.put("version",dependency.getVersion());
            }
            jsonObjects.add(jsonObject);
        }
        return jsonObjects;
    }

    @Override
    public JSONObject parent(UserOptions userOptions) {
        Long parentId = userOptions.getParentId();
        Dependency dependency = null;
        JSONObject jsonObject = null;
        if(parentId!=null&&parentId!=0){
            jsonObject = new JSONObject();
            dependency = dependencyMapper.selectById(parentId);
            jsonObject.put("groupId",dependency.getGroupId());
            jsonObject.put("artifactId",dependency.getArtifactId());
            jsonObject.put("version",dependency.getVersion());
        }
        return jsonObject;
    }

    public String getCode(UserOptions userOptions) throws IOException, ServiceException {
        // 生成项目文件
        String filePath = generateProjectFiles(userOptions);
        // 创建压缩文件
        String zipFilePath = zipCompress(filePath);
        //将路径存储到数据库
        String uuid = UUID.randomUUID().toString().replace("-", "");
        ProjectZipPath projectZipPath = new ProjectZipPath();
        projectZipPath.setUuid(uuid);
        projectZipPath.setPath(zipFilePath);
        projectZipPath.setCreateTime(new Date());
        projectZipPath.setModifyTime(new Date());
        projectZipPathMapper.insert(projectZipPath);
        return uuid;
    }

    public void downloadByUuid(String uuid) throws IOException {
        //根据uuid找到路径
        ProjectZipPath projectZipPath = projectZipPathMapper.selectById(uuid);
        if(projectZipPath==null){
            throw new FileNotFoundException("not found file by the uuid");
        }
        returnZipToUser(projectZipPath.getPath());
    }
}
