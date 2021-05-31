package com.backlog.crawler.logic;

import com.backlog.crawler.constant.SchedulerType;
import com.backlog.crawler.dto.SchedulerConfigKeyDto;
import com.backlog.crawler.mybatis.entity.SchedulerConfig;
import com.backlog.crawler.mybatis.mapper.SchedulerConfigMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.*;

/**
 * スケジューラ関連の共通ロジック.
 *
 * @author honobono
 */
@Component
public class SchedulerLogic {

    /** SchedulerConfigMapper. */
    private final SchedulerConfigMapper schedulerConfigMapper;

    @Autowired
    public SchedulerLogic(SchedulerConfigMapper schedulerConfigMapper) {
        this.schedulerConfigMapper = schedulerConfigMapper;
    }

    /**
     * スケジューラ設定を取得し、HashMapを生成する.
     *
     * @return スケジューラ設定のHashMap
     */
    public Map<SchedulerConfigKeyDto, SchedulerConfig> createMapOfSchedulerConfig(){
        List<SchedulerConfig> schedulerConfigList = schedulerConfigMapper.selectBySchedulerTypeAndActivated(SchedulerType.ISSUE.getValue(), Boolean.TRUE);
        if (CollectionUtils.isEmpty(schedulerConfigList)) {
            return null;
        }

        Map<SchedulerConfigKeyDto, SchedulerConfig> map = new HashMap<SchedulerConfigKeyDto, SchedulerConfig>(schedulerConfigList.size());
        for (SchedulerConfig schedulerConfig : schedulerConfigList) {
            map.put(new SchedulerConfigKeyDto(schedulerConfig.getSchedulerType(), schedulerConfig.getComparisonType()), schedulerConfig);
        }

        return map;
    }

    /**
     * スケジューラ接敵キー情報からスケジューラ情報を取得する.
     *
     * @param schedulerMap スケジューラ設定のHashMap
     * @param schedulerConfigKeyDto スケジューラ接敵キー情報
     * @return スケジューラ情報
     */
    public SchedulerConfig getSchedulerConfig(Map<SchedulerConfigKeyDto, SchedulerConfig> schedulerMap, SchedulerConfigKeyDto schedulerConfigKeyDto) {
        Assert.notNull(schedulerMap, "schedulerMap is null.");
        Assert.notNull(schedulerConfigKeyDto, "schedulerConfigKeyDto is null.");

        return schedulerMap.get(new SchedulerConfigKeyDto(schedulerConfigKeyDto.getSchedulerType(), schedulerConfigKeyDto.getComparisonType()));
    }
}
