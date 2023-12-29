package cn.mengcankeji.dd.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 已创建的企业内部应用的配置
 */
@Configuration
@Data
public class AppConfig {

    /**
     * 已创建的企业内部应用的AppKey
     */
    @Value("dd.app.appKey")
    private String appKey;

    /**
     * 已创建的企业内部应用的appSecret
     */
    @Value("dd.app.appSecret")
    private String appSecret;

    /**
     * 企业的corpId
     */
    @Value("ddd.app.corpid")
    private String corpid;

    /**
     * sso密钥，可以在开发者后台基本信息—开发信息
     */
    @Value("dd.aap.ssoSecret")
    private String ssoSecret;
}
