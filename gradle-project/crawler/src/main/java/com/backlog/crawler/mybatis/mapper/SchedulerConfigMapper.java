package com.backlog.crawler.mybatis.mapper;

import com.backlog.crawler.mybatis.entity.SchedulerConfig;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SchedulerConfigMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Delete({
        "delete from scheduler_config",
        "where scheduler_id = #{schedulerId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long schedulerId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Insert({
        "insert into scheduler_config (scheduler_id, scheduler_type, ",
        "comparison_type, `comment`, ",
        "exceeded_days, activated, ",
        "created_at, updated_at)",
        "values (#{schedulerId,jdbcType=BIGINT}, #{schedulerType,jdbcType=VARCHAR}, ",
        "#{comparisonType,jdbcType=VARCHAR}, #{comment,jdbcType=VARCHAR}, ",
        "#{exceededDays,jdbcType=INTEGER}, #{activated,jdbcType=BIT}, ",
        "#{createdAt,jdbcType=TIMESTAMP}, #{updatedAt,jdbcType=TIMESTAMP})"
    })
    int insert(SchedulerConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(SchedulerConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Select({
        "select",
        "scheduler_id, scheduler_type, comparison_type, `comment`, exceeded_days, activated, ",
        "created_at, updated_at",
        "from scheduler_config",
        "where scheduler_id = #{schedulerId,jdbcType=BIGINT}"
    })
    @ResultMap("com.backlog.crawler.mybatis.mapper.SchedulerConfigMapper.BaseResultMap")
    SchedulerConfig selectByPrimaryKeyWithLock(Long schedulerId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Select({
        "select",
        "scheduler_id, scheduler_type, comparison_type, `comment`, exceeded_days, activated, ",
        "created_at, updated_at",
        "from scheduler_config",
        "where scheduler_id = #{schedulerId,jdbcType=BIGINT}"
    })
    @ResultMap("com.backlog.crawler.mybatis.mapper.SchedulerConfigMapper.BaseResultMap")
    SchedulerConfig selectByPrimaryKey(Long schedulerId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Select({
            "select",
            "scheduler_id, scheduler_type, comparison_type, `comment`, exceeded_days, activated, ",
            "created_at, updated_at",
            "from scheduler_config",
            "where scheduler_type = #{schedulerType,jdbcType=VARCHAR}",
            "and activated = #{activated,jdbcType=BIT}"
    })
    @ResultMap("com.backlog.crawler.mybatis.mapper.SchedulerConfigMapper.BaseResultMap")
    List<SchedulerConfig> selectBySchedulerTypeAndActivated(String schedulerType, Boolean activated);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(SchedulerConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Update({
        "update scheduler_config",
        "set scheduler_type = #{schedulerType,jdbcType=VARCHAR},",
          "comparison_type = #{comparisonType,jdbcType=VARCHAR},",
          "`comment` = #{comment,jdbcType=VARCHAR},",
          "exceeded_days = #{exceededDays,jdbcType=INTEGER},",
          "activated = #{activated,jdbcType=BIT},",
          "created_at = #{createdAt,jdbcType=TIMESTAMP},",
          "updated_at = #{updatedAt,jdbcType=TIMESTAMP}",
        "where scheduler_id = #{schedulerId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(SchedulerConfig record);
}