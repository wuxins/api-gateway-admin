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


import com.zhongan.life.modules.gateway.domain.Group;
import com.zhongan.life.modules.gateway.service.GroupService;
import com.zhongan.life.modules.gateway.service.dto.GroupQueryCriteria;
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
*
* @author yicai.liu
* @date 2023-02-25
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "分组管理")
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('group:list')")
    public void exportGroup(HttpServletResponse response, GroupQueryCriteria criteria) throws IOException {
        groupService.download(groupService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询分组")
    @PreAuthorize("@el.check('group:list')")
    public ResponseEntity<Object> queryGroup(GroupQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(groupService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增分组")
    @PreAuthorize("@el.check('group:add')")
    public ResponseEntity<Object> createGroup(@Validated @RequestBody Group resources){
        return new ResponseEntity<>(groupService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改分组")
    @PreAuthorize("@el.check('group:edit')")
    public ResponseEntity<Object> updateGroup(@Validated @RequestBody Group resources){
        groupService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @ApiOperation("删除分组")
    @PreAuthorize("@el.check('group:del')")
    public ResponseEntity<Object> deleteGroup(@RequestBody Long[] ids) {
        groupService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
