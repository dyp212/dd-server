package cn.mengcankeji.dd.common;

public class DingUrlConstant {

    //读取用户信息 -- 新版
    public final static String URL_USER_GET = "https://oapi.dingtalk.com/user/getuserinfo";

    //读取管理后台用户
    public final static String URL_ADMIN_USER_GET = "https://oapi.dingtalk.com/sso/getuserinfo";

    //     * 创建应用，验证回调URL创建有效事件（第一次保存回调URL之前）


    private static final String EVENT_CHECK_CREATE_SUITE_URL = "check_create_suite_url";

//*
//     * 创建应用，验证回调URL变更有效事件（第一次保存回调URL之后）


    private static final String EVENT_CHECK_UPADTE_SUITE_URL = "check_update_suite_url";

//*
//     * suite_ticket推送事件


    private static final String SYNC_HTTP_PUSH_HIGH = "SYNC_HTTP_PUSH_HIGH";

//*
//     * 企业授权开通应用事件


    private static final String EVENT_TMP_AUTH_CODE = "tmp_auth_code";
}
