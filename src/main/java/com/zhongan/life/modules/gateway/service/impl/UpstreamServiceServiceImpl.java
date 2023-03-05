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
import com.zhongan.life.modules.gateway.domain.UpstreamService;
import com.zhongan.life.modules.gateway.domain.UpstreamServiceVersion;
import com.zhongan.life.modules.gateway.repository.UpstreamServiceRepository;
import com.zhongan.life.modules.gateway.repository.UpstreamServiceVersionRepository;
import com.zhongan.life.modules.gateway.service.UpstreamServiceService;
import com.zhongan.life.modules.gateway.service.dto.UpstreamServiceDto;
import com.zhongan.life.modules.gateway.service.dto.UpstreamServiceQueryCriteria;
import com.zhongan.life.modules.gateway.service.mapstruct.UpstreamServiceMapper;
import com.zhongan.life.utils.FileUtil;
import com.zhongan.life.utils.PageUtil;
import com.zhongan.life.utils.QueryHelp;
import com.zhongan.life.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yicai.liu
 * @description 服务实现
 * @date 2023-02-25
 **/
@Service
@RequiredArgsConstructor
public class UpstreamServiceServiceImpl implements UpstreamServiceService {

    private final UpstreamServiceRepository upstreamServiceRepository;
    private final UpstreamServiceMapper upstreamServiceMapper;

    private final UpstreamServiceVersionRepository upstreamServiceVersionRepository;

    @Override
    public Map<String, Object> queryAll(UpstreamServiceQueryCriteria criteria, Pageable pageable) {
        Page<UpstreamService> page = upstreamServiceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        List<UpstreamService> upstreamServiceList = page.getContent();
        upstreamServiceList.forEach(i -> i.setUpstreamServiceVersions(upstreamServiceVersionRepository.findByServiceCode(i.getServiceCode())));
        return PageUtil.toPage(page.map(upstreamServiceMapper::toDto));
    }

    @Override
    public List<UpstreamServiceDto> queryAll(UpstreamServiceQueryCriteria criteria) {
        List<UpstreamServiceDto> upstreamServiceDtos = upstreamServiceMapper.toDto(upstreamServiceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
        upstreamServiceDtos.forEach(i -> i.setUpstreamServiceVersions(upstreamServiceVersionRepository.findByServiceCode(i.getServiceCode())));
        return upstreamServiceDtos;
    }

    @Override
    @Transactional
    public UpstreamServiceDto findById(Long id) {
        UpstreamService upstreamService = upstreamServiceRepository.findById(id).orElseGet(UpstreamService::new);
        ValidationUtil.isNull(upstreamService.getId(), "UpstreamService", "id", id);
        upstreamService.setUpstreamServiceVersions(upstreamServiceVersionRepository.findByServiceCode(upstreamService.getServiceCode()));
        return upstreamServiceMapper.toDto(upstreamService);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UpstreamServiceDto create(UpstreamService resources) {
        if (upstreamServiceRepository.findByServiceCode(resources.getServiceCode()) != null) {
            throw new EntityExistException(UpstreamService.class, "service_code", resources.getServiceCode());
        }
        List<UpstreamServiceVersion> upstreamServiceVersions = resources.getUpstreamServiceVersions();
        upstreamServiceVersions.forEach(i -> i.setServiceCode(resources.getServiceCode()));
        upstreamServiceVersionRepository.saveAll(upstreamServiceVersions);
        return upstreamServiceMapper.toDto(upstreamServiceRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UpstreamService resources) {
        UpstreamService upstreamService = upstreamServiceRepository.findById(resources.getId()).orElseGet(UpstreamService::new);
        ValidationUtil.isNull(upstreamService.getId(), "UpstreamService", "id", resources.getId());
        UpstreamService upstreamService1 = upstreamServiceRepository.findByServiceCode(resources.getServiceCode());
        if (upstreamService1 != null && !upstreamService1.getId().equals(upstreamService.getId())) {
            throw new EntityExistException(UpstreamService.class, "service_code", resources.getServiceCode());
        }
        upstreamService.copy(resources);
        upstreamServiceRepository.save(upstreamService);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            upstreamServiceRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<UpstreamServiceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UpstreamServiceDto upstreamService : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("service name", upstreamService.getName());
            map.put("service code", upstreamService.getServiceCode());
            map.put("record created time", upstreamService.getGmtCreated());
            map.put("record updated time", upstreamService.getGmtModified());
            map.put("who created the record", upstreamService.getCreator());
            map.put("who updated the record", upstreamService.getModifier());
            map.put("logical delete identifier(Y-effective,N-ineffective)", upstreamService.getIsDeleted());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
