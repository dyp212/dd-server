package cn.mengcankeji.dd.common;

public class DingEventConstant {

    // 创建应用，验证回调URL创建有效事件（第一次保存回调URL之前）
    public static final String EVENT_CHECK_CREATE_SUITE_URL = "check_create_suite_url";

    // 创建应用，验证回调URL变更有效事件（第一次保存回调URL之后）
    public static final String EVENT_CHECK_UPADTE_SUITE_URL = "check_update_suite_url";

    //  suite_ticket推送事件
    public static final String SYNC_HTTP_PUSH_HIGH = "SYNC_HTTP_PUSH_HIGH";

    //   企业授权开通应用事件
    public static final String EVENT_TMP_AUTH_CODE = "tmp_auth_code";


}
