package airport.cargos.com.spring.starter.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("project_zip_path")
public class ProjectZipPath {
    @TableId(value = "uuid")
    private String uuid;
    private String path;
    private Date createTime;
    private Date modifyTime;
}
