package com.backlog.crawler.constant;

import lombok.Getter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 課題状態を表す定数.
 *
 * @author honobono
 */
public enum IssueStatus {

    /** 未対応. */
    WAITING("未対応"),

    /** 処理中. */
    DOING("処理中"),

    /** 処理済み. */
    DONE("処理済み"),

    /** 完了. */
    COMPLETED("完了");

    private static final Map<String, IssueStatus> TYPE_MAP = Collections.unmodifiableMap(createMap());

    @Getter
    private final String value;

    private IssueStatus(String value) {
        this.value = value;
    }

    private static Map<String, IssueStatus> createMap() {
        IssueStatus[] types = IssueStatus.values();
        Map<String, IssueStatus> map = new HashMap<String, IssueStatus>(types.length);
        for (IssueStatus type : types) {
            map.put(type.getValue(), type);
        }
        return map;
    }

    /**
     * valueに該当するIssueStatusオブジェクトを取得する.
     *
     * @param value 状態値
     * @return IssueStatus
     */
    public static IssueStatus of(String value) {
        if (value == null) {
            return null;
        }
        IssueStatus issueStatus = TYPE_MAP.get(value);
        return issueStatus;
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
}
