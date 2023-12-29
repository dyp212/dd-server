package cn.mengcankeji.dd.web;

/**
 *
 */
public interface ErrorCode {
    /**
     * 获取错误编码
     *
     * @return 错误码
     */
    int getCode();

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getMessage();
}
