package cn.mengcankeji.dd.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 已创建第三方企业应用配置,后期对接多企业多应用需要考虑配置到DB存储@TODO
 */
@Configuration
@Data
public class CorpConfig {

    /**
     * 已创建的第三方企业应用的corpId
     */
    @Value("dd.corp.corpId")
    private String corpId;

    /**
     * 已创建的第三方企业应用的SuiteKey
     */
    @Value("dd.corp.suiteKey")
    private String suiteKey;

    /**
     * 已创建的第三方企业应用的suiteSecret
     */
    @Value("dd.corp.suiteSecret")
    private String suiteSecret;

    /**
     * 已创建的第三方企业应用的token
     */
    @Value("dd.corp.token")
    private String token;

    /**
     * 已创建的第三方企业应用的aesKey
     */
    @Value("dd.corp.aesKey")
    private String aesKey;


}
