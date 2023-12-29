package cn.mengcankeji.dd.web;

import cn.mengcankeji.dd.common.DingEventConstant;
import cn.mengcankeji.dd.config.CorpConfig;
import cn.mengcankeji.dd.util.DingCallbackCrypto;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/v1/callback")
@RequiredArgsConstructor
public class DingNotifyController {

    private final CorpConfig corpConfig;

    @PostMapping(value = "ding")
    public Object dingCallback(
            @RequestParam(value = "signature") String signature,
            @RequestParam(value = "timestamp") Long timestamp,
            @RequestParam(value = "nonce") String nonce,
            @RequestBody(required = false) JSONObject body) {
        String params = "signature:" + signature + " timestamp:" + timestamp + " nonce:" + nonce + " body:" + body;
        try {
            log.info("begin ding callback:" + params);
            //参数分别填写Token、Aes_key和第三方企业应用的suiteKey
            DingCallbackCrypto dingTalkEncryptor = new DingCallbackCrypto(corpConfig.getToken(), corpConfig.getAesKey(), corpConfig.getCorpId());
            // 从post请求的body中获取回调信息的加密数据进行解密处理
            String encrypt = body.getString("encrypt");
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp.toString(), nonce, encrypt);
            JSONObject callBackContent = JSON.parseObject(plainText);
            // 根据回调事件类型做不同的业务处理
            //建议开发者在回调接口中只对接收到的事件进行落库处理，不进行业务逻辑处理，保证接口不会超时
            String eventType = callBackContent.getString("EventType");
            if (DingEventConstant.EVENT_CHECK_CREATE_SUITE_URL.equals(eventType)) {
                log.info("验证新创建的回调URL有效性: " + plainText);
                System.out.println("验证新创建的回调URL有效性：" + callBackContent);
            } else if (DingEventConstant.EVENT_CHECK_UPADTE_SUITE_URL.equals(eventType)) {
                log.info("验证更新回调URL有效性: " + plainText);
                System.out.println("验证更新回调URL有效性：" + callBackContent);
            } else if (DingEventConstant.SYNC_HTTP_PUSH_HIGH.equals(eventType)) {
                // suite_ticket用于用签名形式生成accessToken(访问钉钉服务端的凭证)，需要保存到应用的db。
                // 钉钉会定期向本callback url推送suite_ticket新值用以提升安全性。
                // 应用在获取到新的时值时，保存db成功后，返回给钉钉success加密串（如本demo的return）
                log.info("应用suite_ticket数据推送: " + plainText);
                JSONObject bizDataObj = callBackContent.getJSONArray("bizData").getJSONObject(0);
                Integer bizType = bizDataObj.getInteger("biz_type");
                //不同的授权事件参考 https://open.dingtalk.com/document/isvapp/authorization-event-1?spm=ding_open_doc.document.0.0.351c351c5Z8HYV
                if(bizType.equals(2)){
                    String suiteTicket = bizDataObj.getJSONObject("biz_data").getString("suiteTicket");
                    //@TODO 这里保存相应信息（每5小时推送更新一次），然后在AppAccessTokenUtils->getCorpAccessToken 调用时使用

                }
            } else if (DingEventConstant.EVENT_TMP_AUTH_CODE.equals(eventType)) {
                // 本事件应用应该异步进行授权开通企业的初始化，目的是尽最大努力快速返回给钉钉服务端。用以提升企业管理员开通应用体验
                // 即使本接口没有收到数据或者收到事件后处理初始化失败都可以后续再用户试用应用时从前端获取到corpId并拉取授权企业信息，进而初始化开通及企业。
                log.info("企业授权开通应用事件: " + plainText);
            } else {
                System.out.println("其他事件:" + callBackContent);
            }
            // 返回success的加密信息表示回调处理成功
            System.out.println(dingTalkEncryptor.getEncryptedMap("success", timestamp, nonce));
            return dingTalkEncryptor.getEncryptedMap("success", timestamp, nonce);
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            log.error("process callback fail." + params, e);
            return "fail";
        }
    }
}
