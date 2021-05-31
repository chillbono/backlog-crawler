package com.backlog.crawler.constant;

import lombok.Getter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * BacklogAPIのエラーコードを表す定数.
 *
 * @author honobono
 */
public enum BacklogErrorCode {

    /** バグ起因のエラー. */
    INTERNAL_ERROR("1", "InternalError", "接続先のバグ起因による例外です。"),

    /** ライセンスの機能制限によるエラー. */
    LICENCE_ERROR("2", "LicenceError", "ライセンスもしくはプランの機能が制限されています。"),

    /** ライセンスの期限切れによるエラー. */
    LICENCE_EXPIRED_ERROR("3", "LicenceExpiredError", "ライセンスの期限が切れています。"),

    /** アクセス制限によるエラー. */
    ACCESS_DENIED_ERROR("4", "AccessDeniedError", "アクセスが拒否されています。"),

    /** ユーザ権限外の操作実行時のエラー. */
    UNAUTHORIZED_OPERATION_ERROR("5", "UnauthorizedOperationError", "指定したユーザに権限がありません。"),

    /** リクエスト対象のリソースが存在しない場合のエラー. */
    NO_RESOURCE_ERROR("6", "NoResourceError", "リクエスト対象のリソースが存在しません。"),

    /** 不正なパラメータのリクエストを表すエラー. */
    INVALID_REQUEST_ERROR("7", "InvalidRequestError", "不正なパラメータです。"),

    /** スペースの容量制限を超える場合のエラー. */
    SPACE_OVER_CAPACITY_ERROR("8", "SpaceOverCapacityError", "スペースの容量制限を超えています。"),

    /** リソース追加時、リソースに設けられた制限を超える場合のエラー. */
    RESOURCE_OVERFLOW_ERROR("9", "ResourceOverflowError", "リソースの制限を超えています。"),

    /** 制限サイズを超えるファイルがアップロードされた場合のエラー. */
    TOO_LARGE_FILE_ERROR("10", "TooLargeFileError", "ファイルサイズが制限を超えています。"),

    /** 認証に失敗した場合のエラー. */
    AUTHENTICATION_ERROR("11", "AuthenticationError", "認証に失敗しました。"),

    /** 2段階認証を有効にしていないユーザが、2段階認証が必須なスペースへのアクセスを拒否された場合のエラー. */
    REQUIRED_MFA_ERROR("12", "RequiredMFAError", "アクセスが拒否されました。指定したユーザの2段階認証が無効になっています。"),

    /** API実行ユーザが、レート制限によってアクセスを拒否された場合のエラー. */
    TOO_MANY_REQUESTS_ERROR("13", "TooManyRequestsError", "アクセスが拒否されました。指定したユーザはレート制限されています。");

    private static final Map<String, BacklogErrorCode> TYPE_MAP = Collections.unmodifiableMap(createMap());

    @Getter
    private final String value;

    @Getter
    private final String label;

    @Getter
    private final String message;

    private BacklogErrorCode(String value, String label, String message) {
        this.value = value;
        this.label = label;
        this.message = message;
    }

    private static Map<String, BacklogErrorCode> createMap() {
        BacklogErrorCode[] types = BacklogErrorCode.values();
        Map<String, BacklogErrorCode> map = new HashMap<String, BacklogErrorCode>(types.length);
        for (BacklogErrorCode type : types) {
            map.put(type.getValue(), type);
        }
        return map;
    }

    /**
     * valueに該当するSchedulerTypeオブジェクトを取得する.
     *
     * @param value 状態値
     * @return SchedulerType
     */
    public static BacklogErrorCode of(String value) {
        if (value == null) {
            return null;
        }
        BacklogErrorCode SchedulerType = TYPE_MAP.get(value);
        return SchedulerType;
    }

    /**
     * 値を指定して定義が存在するかどうかを調べる.
     *
     * @param val カテゴリ値
     * @return 定義済み:true
     */
    public static boolean contains(String val) {
        return TYPE_MAP.containsKey(val);
    }

    /**
     * 値を指定してラベル名を取得する.
     * @param val カテゴリ値
     * @return valに該当するラベル
     */
    public static String getLabel(String val) {
        return of(val).getLabel();
    }

    /**
     * 値を指定してエラーメッセージを取得する.
     * @param val カテゴリ値
     * @return valに該当するエラーメッセージ
     */
    public static String getMessage(String val) {
        return of(val).getMessage();
    }
}
