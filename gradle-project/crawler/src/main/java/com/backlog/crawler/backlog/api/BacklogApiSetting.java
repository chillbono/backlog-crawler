package com.backlog.crawler.backlog.api;

import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * BacklogAPIの接続設定クラス.
 *
 * @author honobono
 */
public class BacklogApiSetting {

    /** 接続先URL: 課題一覧の取得. */
    public static final String PATH_GET_ISSUES = "/api/v2/issues";

    /** 接続先URL: 課題参加者一覧の取得. */
    public static final String PATH_GET_ISSUES_PARTICIPANTS = "/api/v2/issues/{issueIdOrKey}/participants";

    /** 接続先URL: 課題コメントの追加. */
    public static final String PATH_POST_ISSUES_COMMENTS = "/api/v2/issues/{issueIdOrKey}/comments";

    /** ホスト名. */
    @Getter
    private final String hostname;

    /** APIキー. */
    @Getter
    private final String apiKey;

    /**
     * コンストラクタ.
     *
     * @param env App環境設定
     */
    public BacklogApiSetting(Environment env) {
        this.hostname = this.buildUri(env, "backlog.api.hostname");
        this.apiKey = env.getProperty("backlog.api.key");
    }

    private String buildUri(Environment env, String uri) {
        String baseUri = env.getProperty(uri).trim();
        if (baseUri != null && baseUri.endsWith("/")) {
            baseUri = baseUri.substring(0, baseUri.length() - 1);
        }
        return baseUri;
    }

    /**
     * リクエストURIの生成.
     *
     * @param endPoint エンドポイント
     * @return UriComponentsBuilder
     */
    public UriComponentsBuilder createUri(String endPoint) {
        Assert.notNull(endPoint, "endPoint is null.");
        return UriComponentsBuilder.fromHttpUrl(this.getHostname() + endPoint);
    }
}
