package com.laigeoffer.pmhub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laigeoffer.pmhub.system.domain.pmhubOAuth2Client;
import org.apache.ibatis.annotations.Mapper;

/**
 * OAuth2客户端 数据层
 *
 * @author zw
 */
@Mapper
public interface pmhubOAuth2ClientMapper extends BaseMapper<pmhubOAuth2Client> {

}
