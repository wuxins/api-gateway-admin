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
package com.zhongan.life.modules.gateway.rest;


import com.zhongan.life.modules.gateway.domain.ApiVersion;
import com.zhongan.life.modules.gateway.service.ApiVersionService;
import com.zhongan.life.modules.gateway.service.dto.ApiSyncDto;
import com.zhongan.life.modules.gateway.service.dto.ApiVersionQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这是一个测试Controller
 *
 * @author yicai.liu
 * @date 2023-02-25
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 创建资源
     *
     * @param response 响应
     * @param criteria 创建条件
     * @return 资源信息返回
     * @throws IOException IO一场
     */
    @GetMapping(value = "/create")
    @ResponseBody
    public ApiVersion create(HttpServletResponse response, @RequestBody ApiVersionQueryCriteria criteria) throws IOException {
        return new ApiVersion();
    }

    /**
     * 查询资源
     *
     * @param pageable 查询响应
     * @param criteria 查询条件
     * @return 资源信息返回
     */
    @GetMapping(value = "/query/{id}")
    @ResponseBody
    public ResponseEntity<ApiVersion> query(@PathVariable String id, @RequestBody ApiVersionQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(new ApiVersion(), HttpStatus.OK);
    }
}
