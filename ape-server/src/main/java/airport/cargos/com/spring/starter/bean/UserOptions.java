package airport.cargos.com.spring.starter.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOptions {
    //是否使用系统默认值
    private Integer isDefault;
    //maven parent
    private Long parentId;
    //packageName
    private String packaging;
    //maven groupId
    private String groupId;
    private String name;
    private String description;
    //maven artifactId
    private String artifactId;
    //maven version
    private String version;
    //dependencies id
    private List<Long> ids;
    //application name
    private String application;
}
