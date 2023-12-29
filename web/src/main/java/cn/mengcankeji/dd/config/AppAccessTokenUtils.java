package cn.mengcankeji.dd.config;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AppAccessTokenUtils {

    @Resource
    private AppConfig appConfig;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 获取企业内部应用的accessToken
     */
    public String getAccessToken() throws UnsupportedEncodingException {
        String accessTokenKey = Md5Crypt.md5Crypt(("app_"+appConfig.getAppKey()).getBytes("UTF-8"));
        Object accessTokenObj = redisTemplate.opsForValue().get(accessTokenKey);
        if(accessTokenObj != null){
            return String.valueOf(accessTokenObj);
        }
        GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                                                            .setAppKey(appConfig.getAppKey())
                                                            .setAppSecret(appConfig.getAppSecret());
        try{
            GetAccessTokenResponse getAccessTokenResponse = getClient().getAccessToken(getAccessTokenRequest);
            Integer statusCode = getAccessTokenResponse.getStatusCode();
            //判断statusCode返回@TODO
            String accessToken = getAccessTokenResponse.getBody().getAccessToken();
            Long expireIn = getAccessTokenResponse.getBody().getExpireIn();
            if(!StringUtils.isEmpty(accessToken) && expireIn != null){
                redisTemplate.opsForValue().set(accessTokenKey, accessToken, expireIn, TimeUnit.SECONDS);
                return accessToken;
            }
        } catch(Exception e){
            TeaException err = new TeaException(e.getMessage(), e);
            log.error("获取企业内部应该AccessToken时异常，err_code->{}, err_msg->{}", err.getCode(), err.getMessage());
        }
        return null;
    }

    /**
     * 获取微应用后台免登的accessToken
     */
    public String getSsoAccessToken() throws UnsupportedEncodingException {
        String accessTokenKey = Md5Crypt.md5Crypt(("app_sso_"+appConfig.getAppKey()).getBytes("UTF-8"));
        Object accessTokenObj = redisTemplate.opsForValue().get(accessTokenKey);
        if(accessTokenObj != null){
            return String.valueOf(accessTokenObj);
        }
        GetSsoAccessTokenRequest getSsoAccessTokenRequest = new GetSsoAccessTokenRequest()
                .setCorpid(appConfig.getCorpid())
                .setSsoSecret(appConfig.getSsoSecret());
        try{
            GetSsoAccessTokenResponse getSsoAccessTokenResponse = getClient().getSsoAccessToken(getSsoAccessTokenRequest);
            Integer statusCode = getSsoAccessTokenResponse.getStatusCode();
            //判断statusCode返回@TODO
            String accessToken = getSsoAccessTokenResponse.getBody().getAccessToken();
            Long expireIn = getSsoAccessTokenResponse.getBody().getExpireIn();
            if(!StringUtils.isEmpty(accessToken) && expireIn != null){
                redisTemplate.opsForValue().set(accessTokenKey, accessToken, expireIn, TimeUnit.SECONDS);
                return accessToken;
            }
        } catch(Exception e){
            TeaException err = new TeaException(e.getMessage(), e);
            log.error("获取企业内部应该AccessToken时异常，err_code->{}, err_msg->{}", err.getCode(), err.getMessage());
        }
        return null;
    }

    /***
     * 获取定制应用的accessToken
     * 不同企业的不同应用使用各自的配置信息，@TODO 可以考虑数据表里存储，有管理页面来管理
     *  存储可以参考 https://open.dingtalk.com/document/isvapp/develop-mini-programs-using-rds-push
     *
     * @param suiteKey 定制应用的CustomKey
     * @param suiteSecret 定制应用的CustomSecret
     * @param authCorpId  授权企业的CorpId
     * @param suiteTicket 钉钉推送的suiteTicket，定制应用该参数自定义，比如Test,  @TODO 这个值考虑DB读取
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getCorpAccessToken(String suiteKey, String suiteSecret, String authCorpId, String suiteTicket) throws UnsupportedEncodingException {
        String accessTokenKey = Md5Crypt.md5Crypt(("app_corp_"+suiteKey).getBytes("UTF-8"));
        Object accessTokenObj = redisTemplate.opsForValue().get(accessTokenKey);
        if(accessTokenObj != null){
            return String.valueOf(accessTokenObj);
        }
        GetCorpAccessTokenRequest getCorpAccessTokenRequest = new GetCorpAccessTokenRequest()
                .setSuiteKey(suiteKey)
                .setSuiteSecret(suiteSecret)
                .setAuthCorpId(authCorpId)
                .setSuiteTicket(suiteTicket);
        try{
            GetCorpAccessTokenResponse getCorpAccessTokenResponse = getClient().getCorpAccessToken(getCorpAccessTokenRequest);
            Integer statusCode = getCorpAccessTokenResponse.getStatusCode();
            //判断statusCode返回@TODO
            String accessToken = getCorpAccessTokenResponse.getBody().getAccessToken();
            Long expireIn = getCorpAccessTokenResponse.getBody().getExpireIn();
            if(!StringUtils.isEmpty(accessToken) && expireIn != null){
                redisTemplate.opsForValue().set(accessTokenKey, accessToken, expireIn, TimeUnit.SECONDS);
                return accessToken;
            }
        } catch(Exception e){
            TeaException err = new TeaException(e.getMessage(), e);
            log.error("获取企业内部应该AccessToken时异常，err_code->{}, err_msg->{}", err.getCode(), err.getMessage());
        }
        return null;
    }

    private Client getClient() throws Exception {
        Config config = new Config();
        config.setProtocol("https");
        config.setRegionId("central");
        return new Client(config);
    }

}
