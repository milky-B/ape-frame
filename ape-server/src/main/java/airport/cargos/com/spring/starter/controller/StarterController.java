package airport.cargos.com.spring.starter.controller;

import airport.cargos.com.spring.starter.bean.UserOptions;
import airport.cargos.com.spring.starter.conf.UserProperty;
import airport.cargos.com.spring.starter.constant.Result;
import airport.cargos.com.spring.starter.constant.StateEnum;
import airport.cargos.com.spring.starter.service.GeneratorService;
import com.google.protobuf.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Controller
@Slf4j
@RequestMapping("/spring/starter")
public class StarterController {
    @Autowired
    private UserProperty userProperty;
    @Autowired
    private GeneratorService generatorService;

    @PostMapping("/download")
    public void download(@RequestBody UserOptions userOptions) {
        checkUserOptions(userOptions);
        generatorService.startDownload(userOptions);
    }
    private void checkUserOptions(UserOptions userOptions){
        if (StateEnum.IS.getCode().equals(userOptions.getIsDefault())) {
            userOptions.setGroupId(userProperty.getGroupId());
            userOptions.setApplication(userProperty.getApplication());
            userOptions.setPackaging(userProperty.getPackaging());
            userOptions.setArtifactId(userProperty.getArtifactId());
            userOptions.setVersion(userProperty.getVersion());
        }
    }
    @PostMapping("getCode")
    @ResponseBody
    public Result getCode(@RequestBody UserOptions userOptions) {
        Result result = null;
        try {
            checkUserOptions(userOptions);
            result = Result.success(generatorService.getCode(userOptions));
        } catch (IOException e) {
            log.error("ioException {}",e.getMessage(),e);
            result = Result.fail();
        } catch (ServiceException e) {
            log.error("serviceException: {}",e.getMessage(),e);
            result = Result.fail();
        }
        return result;
    }
    @GetMapping("/download/{uuid}")
    public void downloadByUuid(@PathVariable String uuid) {
        try {
            generatorService.downloadByUuid(uuid);
        } catch (IOException e) {
            log.error("ioException {}",e.getMessage(),e);
        }
    }
}
