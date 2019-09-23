package com.nokia.web.config;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;



/**
 * @author zhg
 * @version $ Id: CasProperties.java,V 0.1 2019/4/7 22:41 zhg Exp $
 */
@ConfigurationProperties(prefix="cas")
@Data
public class CasProperties {

    private String serverUrl;

    private String serverLoginUrl;

    private String serverLogoutUrl;

}
