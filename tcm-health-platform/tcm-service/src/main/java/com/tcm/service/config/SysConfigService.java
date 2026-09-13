package com.tcm.service.config;

import java.util.List;
import java.util.Map;

/**
 * 系统配置服务接口
 *
 * @author Ti
 * @since 2026-02-06
 */
public interface SysConfigService {

    /**
     * 获取所有配置
     */
    List<Map<String, Object>> getAllConfigs();

    /**
     * 根据key获取配置值
     */
    String getConfigValue(String configKey);

    /**
     * 更新配置
     */
    void updateConfig(String configKey, String configValue);

    /**
     * 批量更新配置
     */
    void batchUpdateConfigs(List<Map<String, String>> configs);
}
