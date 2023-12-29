package cn.mengcankeji.dd.service.impl;

import cn.mengcankeji.dd.common.DingUrlConstant;
import cn.mengcankeji.dd.config.AppAccessTokenUtils;
import cn.mengcankeji.dd.exception.ServerException;
import cn.mengcankeji.dd.service.DingAuthServicee;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiSsoGetuserinfoRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiSsoGetuserinfoResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DingAuthServiceImpl implements DingAuthServicee {

    private final AppAccessTokenUtils appAccessTokenUtils;

    @Override
    public String getUser(String authCode){
        try {
            String accessToken = appAccessTokenUtils.getAccessToken();
            if(null == accessToken){
                throw new ServerException("accessToken读取失败");
            }
            DingTalkClient client = new DefaultDingTalkClient(DingUrlConstant.URL_USER_GET);
            OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
            request.setCode(authCode);
            request.setHttpMethod("GET");
            OapiUserGetuserinfoResponse response = client.execute(request, accessToken);
            // 查询得到当前用户的userId
            if(response.isSuccess()){
                String userId = response.getUserid();
                return userId;
            }
        } catch (Exception e) {
            log.error("读取用户信息失败，", e);
        }
        return null;
    }
    @Override
    public String getAdmin(String authCode){
        try {
            String accessToken = appAccessTokenUtils.getSsoAccessToken();
            if(null == accessToken){
                throw new ServerException("accessToken读取失败");
            }
            // 获取应用管理员的身份信息
            DingTalkClient client = new DefaultDingTalkClient(DingUrlConstant.URL_ADMIN_USER_GET);
            OapiSsoGetuserinfoRequest request = new OapiSsoGetuserinfoRequest();
            request.setCode(authCode);
            request.setHttpMethod("GET");
            OapiSsoGetuserinfoResponse response = client.execute(request, accessToken);
            if(response.isSuccess()){
                OapiSsoGetuserinfoResponse.UserInfo adminUser = response.getUserInfo();
                return adminUser.getUserid();
            }
        } catch (Exception e) {
            log.error("读取用户信息失败，", e);
        }
        return null;
    }
}
