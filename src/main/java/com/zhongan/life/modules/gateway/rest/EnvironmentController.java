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


import com.zhongan.life.modules.gateway.domain.Environment;
import com.zhongan.life.modules.gateway.service.EnvironmentService;
import com.zhongan.life.modules.gateway.service.dto.EnvironmentQueryCriteria;
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
 * @author wuxins
 * @date 2023-02-24
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "环境管理")
@RequestMapping("/api/environment")
public class EnvironmentController {

    private final EnvironmentService environmentService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('environment:list')")
    public void exportEnvironment(HttpServletResponse response, EnvironmentQueryCriteria criteria) throws IOException {
        environmentService.download(environmentService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询环境")
    @PreAuthorize("@el.check('environment:list')")
    public ResponseEntity<Object> queryEnvironment(EnvironmentQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(environmentService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("查询环境")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('environment:list')")
    public ResponseEntity<Object> queryEnvironment(EnvironmentQueryCriteria criteria) throws IOException {
        return new ResponseEntity<>(environmentService.queryAll(criteria), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增环境")
    @PreAuthorize("@el.check('environment:add')")
    public ResponseEntity<Object> createEnvironment(@Validated @RequestBody Environment resources) {
        return new ResponseEntity<>(environmentService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改环境")
    @PreAuthorize("@el.check('environment:edit')")
    public ResponseEntity<Object> updateEnvironment(@Validated @RequestBody Environment resources) {
        environmentService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @ApiOperation("删除环境")
    @PreAuthorize("@el.check('environment:del')")
    public ResponseEntity<Object> deleteEnvironment(@RequestBody Long[] ids) {
        environmentService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
