package com.backlog.crawler.backlog.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * エラーレスポンスを格納するDTO.
 *
 * @author honobono
 */
@Getter
@Setter
public class ErrorResponseDto {

    /** エラー内容. */
    private List<Errors> errors;

    /**
     * エラー内容.
     *
     * @author honobono.
     */
    @Getter
    @Setter
    public static class Errors {

        /** エラーメッセージ. */
        private String message;

        /** エラーコード. */
        private Integer code;

        /** 詳細情報. */
        private String moreInfo;
    }
}
