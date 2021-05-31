package com.backlog.crawler.constant;

import lombok.Getter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * スケジューラ種別を表す定数.
 *
 * @author honobono
 */
public enum SchedulerType {

    /** 課題. */
    ISSUE("issue");

    private static final Map<String, SchedulerType> TYPE_MAP = Collections.unmodifiableMap(createMap());

    @Getter
    private final String value;

    private SchedulerType(String value) {
        this.value = value;
    }

    private static Map<String, SchedulerType> createMap() {
        SchedulerType[] types = SchedulerType.values();
        Map<String, SchedulerType> map = new HashMap<String, SchedulerType>(types.length);
        for (SchedulerType type : types) {
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
    public static SchedulerType of(String value) {
        if (value == null) {
            return null;
        }
        SchedulerType SchedulerType = TYPE_MAP.get(value);
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
}
