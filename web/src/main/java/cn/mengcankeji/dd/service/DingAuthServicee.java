package cn.mengcankeji.dd.service;

public interface DingAuthServicee {
    String getUser(String authCode);

    String getAdmin(String authCode);
}
