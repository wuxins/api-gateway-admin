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
package com.zhongan.life.modules.gateway.service.impl;

import com.zhongan.life.exception.EntityExistException;
import com.zhongan.life.modules.gateway.repository.ApiGroupRepository;
import com.zhongan.life.modules.gateway.repository.ApiRepository;
import com.zhongan.life.modules.gateway.repository.ApiVersionRepository;
import com.zhongan.life.modules.gateway.repository.GroupRepository;
import com.zhongan.life.modules.gateway.service.mapstruct.GroupMapper;
import com.zhongan.life.utils.FileUtil;
import com.zhongan.life.utils.PageUtil;
import com.zhongan.life.utils.QueryHelp;
import com.zhongan.life.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import com.zhongan.life.modules.gateway.domain.Api;
import com.zhongan.life.modules.gateway.domain.ApiGroup;
import com.zhongan.life.modules.gateway.domain.ApiVersion;
import com.zhongan.life.modules.gateway.domain.Group;
import com.zhongan.life.modules.gateway.service.GroupService;
import com.zhongan.life.modules.gateway.service.dto.GroupDto;
import com.zhongan.life.modules.gateway.service.dto.GroupQueryCriteria;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author yicai.liu
 * 
 * @description 服务实现
 * @date 2023-02-25
 **/
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    private final ApiVersionRepository apiVersionRepository;

    private final ApiRepository apiRepository;

    private final ApiGroupRepository apiGroupRepository;

    @Override
    public Map<String, Object> queryAll(GroupQueryCriteria criteria, Pageable pageable) {
        Page<Group> page = groupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        List<Group> groupList = page.getContent();
        if (!CollectionUtils.isEmpty(groupList)) {
            for (Group group : groupList) {
                ApiGroup apiGroup = new ApiGroup();
                apiGroup.setGroupCode(group.getGroupCode());
                List<ApiGroup> apiGroups = apiGroupRepository.findAll(Example.of(apiGroup));
                List<ApiVersion> apiVersions = Lists.newArrayList();
                apiGroups.forEach(i -> {
                    Api api = new Api();
                    api.setApiCode(i.getApiCode());
                    Optional<Api> one = apiRepository.findOne(Example.of(api));
                    if (one.isPresent()) {
                        api = one.get();
                        ApiVersion apiVersionCondition = new ApiVersion();
                        apiVersionCondition.setEnv("dev");
                        apiVersionCondition.setApiCode(i.getApiCode());
                        Optional<ApiVersion> apiVersionOptional = apiVersionRepository.findOne(Example.of(apiVersionCondition));
                        if (apiVersionOptional.isPresent()) {
                            ApiVersion apiVersion = apiVersionOptional.get();
                            apiVersion.setName(api.getName());
                            apiVersion.setDescription(api.getDescription());
                            apiVersion.setMaintainer(api.getMaintainer());
                            apiVersions.add(apiVersion);
                        }
                    }
                });
                group.setApiVersions(apiVersions);
            }
        }
        return PageUtil.toPage(page.map(groupMapper::toDto));
    }

    @Override
    public List<GroupDto> queryAll(GroupQueryCriteria criteria) {
        return groupMapper.toDto(groupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public GroupDto findById(Long id) {
        Group group = groupRepository.findById(id).orElseGet(Group::new);
        ValidationUtil.isNull(group.getId(), "Group", "id", id);
        return groupMapper.toDto(group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GroupDto create(Group resources) {
        if (groupRepository.findByGroupCode(resources.getGroupCode()) != null) {
            throw new EntityExistException(Group.class, "group_code", resources.getGroupCode());
        }
        return groupMapper.toDto(groupRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Group resources) {
        Group group = groupRepository.findById(resources.getId()).orElseGet(Group::new);
        ValidationUtil.isNull(group.getId(), "Group", "id", resources.getId());
        Group group1 = groupRepository.findByGroupCode(resources.getGroupCode());
        if (group1 != null && !group1.getId().equals(group.getId())) {
            throw new EntityExistException(Group.class, "group_code", resources.getGroupCode());
        }
        group.copy(resources);
        groupRepository.save(group);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            groupRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<GroupDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (GroupDto group : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("api group name", group.getName());
            map.put("api group code", group.getGroupCode());
            map.put("record created time", group.getGmtCreated());
            map.put("record updated time", group.getGmtModified());
            map.put("who created the record", group.getCreator());
            map.put("who updated the record", group.getModifier());
            map.put("logical delete identifier(Y-effective,N-ineffective)", group.getIsDeleted());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
