package airport.cargos.com.spring.starter.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "user.property.default")
public class UserProperty {
    private String packaging;
    private String groupId;
    private String artifactId;
    private String version;
    private String application;
}
