package io.pivotal;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "test-props")
@Component
public class TestProperties {
    private String environment;
    private String dataCenter;
}
