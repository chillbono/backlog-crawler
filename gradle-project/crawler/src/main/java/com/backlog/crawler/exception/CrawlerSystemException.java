package com.backlog.crawler.exception;

/**
 * システム上の想定外の異常が発生した場合に発行する例外.
 *
 * @author honobono
 */
public class CrawlerSystemException extends RuntimeException {

    /**
     * コンストラクタ.
     *
     * @param message 詳細メッセージ
     */
    public CrawlerSystemException(String message) {
        super(message);
    }

    /**
     * コンストラクタ.
     *
     * @param message 詳細メッセージ
     * @param cause 発生原因
     */
    public CrawlerSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
