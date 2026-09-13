package com.tcm.service.config.impl;

import com.tcm.service.config.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 系统配置服务实现
 *
 * @author Ti
 * @since 2026-02-06
 */
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl implements SysConfigService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getAllConfigs() {
        return jdbcTemplate.queryForList(
                "SELECT id, config_key, config_value, config_name, config_type, remark FROM sys_config ORDER BY config_type, id");
    }

    @Override
    public String getConfigValue(String configKey) {
        List<String> values = jdbcTemplate.queryForList(
                "SELECT config_value FROM sys_config WHERE config_key = ?", String.class, configKey);
        return values.isEmpty() ? null : values.get(0);
    }

    @Override
    public void updateConfig(String configKey, String configValue) {
        jdbcTemplate.update(
                "UPDATE sys_config SET config_value = ?, update_time = NOW() WHERE config_key = ?",
                configValue, configKey);
    }

    @Override
    @Transactional
    public void batchUpdateConfigs(List<Map<String, String>> configs) {
        for (Map<String, String> config : configs) {
            updateConfig(config.get("configKey"), config.get("configValue"));
        }
    }
}
