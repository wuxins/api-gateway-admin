package com.zhongan.life.modules.gateway.service.mapstruct;

import com.zhongan.life.base.BaseMapper;
import com.zhongan.life.modules.gateway.domain.Api;
import com.zhongan.life.modules.gateway.service.dto.ApiDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yicai.liu
 * @date 2023-02-25
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiMapper extends BaseMapper<ApiDto, Api> {

}
