package airport.cargos.com.spring.starter.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
@Data
@TableName("dependency")
public class Dependency {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;
    private String groupId;
    private String artifactId;
    private String version;
    private String type;
    private String scope;
    private Long parentId;
    private Date createTime;
    private Date modifyTime;
    private Integer isParent;
}
