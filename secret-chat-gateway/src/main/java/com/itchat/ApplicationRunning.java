package com.itchat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 王青玄
 * @Contact 1121586359@qq.com
 * @ClassName ApplicationRunning.java
 * @create 2024年09月04日 下午12:55
 * @Description 网关服务启动类
 * @Version V1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient  // 开启服务的注册和发现功能
@Slf4j
public class ApplicationRunning {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(ApplicationRunning.class, args);
        Environment env = application.getEnvironment();
        var ip = InetAddress.getLocalHost().getHostAddress();
        var port = env.getProperty("server.port");
        var path = env.getProperty("server.servlet.context-path");
        log.info("(♥◠‿◠)ﾉﾞ  网关服务启动类 启动成功   ლ(´ڡ`ლ)ﾞ");
        log.info("http://" + ip + ":" + port + path + "/index");

    }
}
