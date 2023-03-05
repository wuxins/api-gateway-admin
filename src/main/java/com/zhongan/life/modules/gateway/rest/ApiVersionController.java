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
import com.zhongan.life.modules.gateway.service.dto.ApiVersionQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yicai.liu
 * @date 2023-02-25
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "API管理")
@RequestMapping("/api/apiVersion")
public class ApiVersionController {

    private final ApiVersionService apiVersionService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('apiVersion:list')")
    public void exportApiVersion(HttpServletResponse response, ApiVersionQueryCriteria criteria) throws IOException {
        apiVersionService.download(apiVersionService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询API")
    @PreAuthorize("@el.check('apiVersion:list')")
    public ResponseEntity<Object> queryApiVersion(ApiVersionQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(apiVersionService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("查询API")
    @PreAuthorize("@el.check('apiVersion:list')")
    @GetMapping(value = "/{apiCode}")
    public ResponseEntity<Object> queryApi(@PathVariable String apiCode) {
        return new ResponseEntity<>(apiVersionService.findByApiCode(apiCode), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增API")
    @PreAuthorize("@el.check('apiVersion:add')")
    public ResponseEntity<Object> createApiVersion(@Validated @RequestBody ApiVersion resources) {
        return new ResponseEntity<>(apiVersionService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改API")
    @PreAuthorize("@el.check('apiVersion:edit')")
    public ResponseEntity<Object> updateApiVersion(@Validated @RequestBody ApiVersion resources) {
        apiVersionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @ApiOperation("删除API")
    @PreAuthorize("@el.check('apiVersion:del')")
    public ResponseEntity<Object> deleteApiVersion(@RequestBody Long[] ids) {
        apiVersionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
