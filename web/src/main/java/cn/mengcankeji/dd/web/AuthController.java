package cn.mengcankeji.dd.web;

import cn.mengcankeji.dd.service.DingAuthServicee;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {


    private final DingAuthServicee dingAuthServicee;

    /**
     * 用户授权后读取用户ID，暂时返回原始ID，后可以结合业务增加逻辑使用
     * @param authCode 用户授权码
     * @return
     */
    @GetMapping("user")
    public CommonResult<?> user(@RequestParam("authCode") String authCode){
        return CommonResult.success(dingAuthServicee.getUser(authCode));
    }

    /**
     * 后台用户授权后读取用户ID，暂时返回原始ID，后可以结合业务增加逻辑使用
     * @param authCode 用户授权码
     * @return
     */
    @GetMapping("admin")
    public CommonResult<?> admin(@RequestParam("authCode") String authCode){
        return CommonResult.success(dingAuthServicee.getAdmin(authCode));
    }

}
