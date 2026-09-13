package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.AiChatRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI聊天记录Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface AiChatRecordMapper extends BaseMapper<AiChatRecord> {
}
