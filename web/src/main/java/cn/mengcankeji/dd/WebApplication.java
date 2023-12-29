package cn.mengcankeji.dd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

@RestController
@SpringBootApplication(scanBasePackages = "cn.mengcankeji.dd")
@MapperScan("cn.mengcankeji.dd.mapper")
public class WebApplication {

    @GetMapping("/")
    public String welcome(){
        return "welcome";
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class);
    }
}
