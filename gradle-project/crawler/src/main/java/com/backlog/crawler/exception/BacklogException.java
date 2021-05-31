package com.backlog.crawler.exception;

/**
 * BacklogAPI実行時に異常を検知した場合に発行する例外.
 *
 * @author honobono
 */
public class BacklogException extends RuntimeException {

    /**
     * コンストラクタ.
     *
     * @param message 詳細メッセージ
     * @param cause 発生原因
     */
    public BacklogException(String message, Throwable cause) {
        super(message, cause);
    }
}
