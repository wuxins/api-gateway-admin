/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package com.zhongan.life.modules.gateway.service;

import com.zhongan.life.modules.gateway.service.dto.ApiDto;
import com.zhongan.life.modules.gateway.service.dto.ApiSyncDto;
import com.zhongan.life.modules.gateway.service.dto.ApiVersionDto;
import com.zhongan.life.modules.gateway.service.dto.ApiVersionQueryCriteria;
import com.zhongan.life.modules.gateway.domain.ApiVersion;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
*
* @description 服务接口
* @author yicai.liu
* @date 2023-02-25
**/
public interface ApiVersionService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ApiVersionQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ApiVersionDto>
    */
    List<ApiVersionDto> queryAll(ApiVersionQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ApiVersionDto
     */
    ApiVersionDto findById(Long id);

    ApiDto findByApiCode(String apiCode);

    /**
    * 创建
    * @param resources /
    * @return ApiVersionDto
    */
    ApiVersionDto create(ApiVersion resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(ApiVersion resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<ApiVersionDto> all, HttpServletResponse response) throws IOException;

    void sync(ApiSyncDto resources);
}
