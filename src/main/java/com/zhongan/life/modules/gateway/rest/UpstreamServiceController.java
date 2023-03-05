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


import com.zhongan.life.modules.gateway.domain.UpstreamService;
import com.zhongan.life.modules.gateway.service.UpstreamServiceService;
import com.zhongan.life.modules.gateway.service.dto.UpstreamServiceQueryCriteria;
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
 *
 * @date 2023-02-25
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "应用管理")
@RequestMapping("/api/upstreamService")
public class UpstreamServiceController {

    private final UpstreamServiceService upstreamServiceService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('upstreamService:list')")
    public void exportUpstreamService(HttpServletResponse response, UpstreamServiceQueryCriteria criteria) throws IOException {
        upstreamServiceService.download(upstreamServiceService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询应用")
    @PreAuthorize("@el.check('upstreamService:list')")
    public ResponseEntity<Object> queryUpstreamService(UpstreamServiceQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(upstreamServiceService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("查询应用列表")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('upstreamService:list')")
    public ResponseEntity<Object> queryUpstreamServiceAll(HttpServletResponse response, UpstreamServiceQueryCriteria criteria) throws IOException {
        return new ResponseEntity<>(upstreamServiceService.queryAll(criteria), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增应用")
    @PreAuthorize("@el.check('upstreamService:add')")
    public ResponseEntity<Object> createUpstreamService(@Validated @RequestBody UpstreamService resources) {
        return new ResponseEntity<>(upstreamServiceService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改应用")
    @PreAuthorize("@el.check('upstreamService:edit')")
    public ResponseEntity<Object> updateUpstreamService(@Validated @RequestBody UpstreamService resources) {
        upstreamServiceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @ApiOperation("删除应用")
    @PreAuthorize("@el.check('upstreamService:del')")
    public ResponseEntity<Object> deleteUpstreamService(@RequestBody Long[] ids) {
        upstreamServiceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
