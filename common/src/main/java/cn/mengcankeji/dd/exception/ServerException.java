package cn.mengcankeji.dd.exception;

import lombok.Data;

@Data
public class ServerException extends RuntimeException{

    private String code;

    public ServerException(String code, String message){
        super(message);
        this.code = code;
    }

    public ServerException(String message){
        super(message);
    }
}
