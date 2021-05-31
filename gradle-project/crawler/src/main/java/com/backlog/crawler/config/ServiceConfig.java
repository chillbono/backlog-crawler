package com.backlog.crawler.config;

import com.backlog.crawler.backlog.api.BacklogApiSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

/**
 * ServiceレイヤのDIを設定するクラス。
 *
 * @author honobono
 */
@Configuration
public class ServiceConfig {

    /** Environment. */
    private final Environment env;

    @Autowired
    public ServiceConfig(Environment env) {
        this.env = env;
    }

    /**
     * RestTemplate.
     *
     * @param builder RestTemplateBuilder
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // 接続タイムアウト値（秒）
        Integer connectTimeout  = env.getProperty("resttemplate.connect.timeout.secounds", Integer.class, 30);
        // 読込タイムアウト値（秒）
        Integer readTimeout  = env.getProperty("resttemplate.connect.timeout.secounds", Integer.class, 60);
        return builder
                .setConnectTimeout(Duration.ofSeconds(connectTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }

    /**
     * BacklogApiSetting.
     *
     * @return RestTemplate
     */
    @Bean
    public BacklogApiSetting backlogApiSetting() {
        return new BacklogApiSetting(env);
    }
}
