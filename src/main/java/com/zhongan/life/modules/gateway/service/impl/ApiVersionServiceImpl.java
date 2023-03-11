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

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.zhongan.life.modules.gateway.domain.*;
import com.zhongan.life.modules.gateway.repository.*;
import com.zhongan.life.modules.gateway.service.ApiVersionService;
import com.zhongan.life.modules.gateway.service.dto.ApiDto;
import com.zhongan.life.modules.gateway.service.dto.ApiSyncDto;
import com.zhongan.life.modules.gateway.service.dto.ApiVersionDto;
import com.zhongan.life.modules.gateway.service.dto.ApiVersionQueryCriteria;
import com.zhongan.life.modules.gateway.service.mapstruct.ApiMapper;
import com.zhongan.life.modules.gateway.service.mapstruct.ApiVersionMapper;
import com.zhongan.life.utils.FileUtil;
import com.zhongan.life.utils.PageUtil;
import com.zhongan.life.utils.QueryHelp;
import com.zhongan.life.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yicai.liu
 * @description 服务实现
 * @date 2023-02-25
 **/
@Service
@RequiredArgsConstructor
public class ApiVersionServiceImpl implements ApiVersionService {

    private final ApiVersionRepository apiVersionRepository;
    private final ApiVersionMapper apiVersionMapper;
    private final ApiRepository apiRepository;
    private final ApiMapper apiMapper;
    private final EnvironmentRepository environmentRepository;
    private final UpstreamServiceRepository upstreamServiceRepository;
    private final UpstreamServiceVersionRepository upstreamServiceVersionRepository;

    @Override
    public Map<String, Object> queryAll(ApiVersionQueryCriteria criteria, Pageable pageable) {
        criteria.setEnv("dev");
        Page<ApiVersion> page = apiVersionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        List<ApiVersion> apiVersionList = page.getContent();
        for (ApiVersion apiVersion : apiVersionList) {
            Api api = new Api();
            api.setApiCode(apiVersion.getApiCode());
            Optional<? extends Api> optionalApi = apiRepository.findOne(Example.of(api));
            if (optionalApi.isPresent()) {
                api = optionalApi.get();
                apiVersion.setName(api.getName());
                apiVersion.setDescription(api.getDescription());
                apiVersion.setMaintainer(api.getMaintainer());
            }
        }
        return PageUtil.toPage(page.map(apiVersionMapper::toDto));
    }

    @Override
    public List<ApiVersionDto> queryAll(ApiVersionQueryCriteria criteria) {
        return apiVersionMapper.toDto(apiVersionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public ApiVersionDto findById(Long id) {
        ApiVersion apiVersion = apiVersionRepository.findById(id).orElseGet(ApiVersion::new);
        ValidationUtil.isNull(apiVersion.getId(), "ApiVersion", "id", id);
        return apiVersionMapper.toDto(apiVersion);
    }

    @Override
    public ApiDto findByApiCode(String apiCode) {

        List<Environment> envs = environmentRepository.findAll();
        Api api = apiRepository.findByApiCode(apiCode);
        List<ApiVersion> apiVersions = apiVersionRepository.findByApiCode(apiCode);
        Map<String, ApiVersion> envWithApiVersion = apiVersions.stream().collect(Collectors.toMap(ApiVersion::getEnv, v -> v));
        List<ApiVersion> itemList = new ArrayList<>();
        Map<String, String> serviceCodeWithName = new HashMap<>();
        envs.forEach(i -> {
            ApiVersion item = new ApiVersion();
            if (envWithApiVersion.containsKey(i.getEnv())) {
                item = envWithApiVersion.get(i.getEnv());
                item.setEnvName(i.getName());
                if (!serviceCodeWithName.containsKey(item.getServiceCode())) {
                    UpstreamService upstreamService = upstreamServiceRepository.findByServiceCode(item.getServiceCode());
                    serviceCodeWithName.put(upstreamService.getServiceCode(), upstreamService.getName());
                }
                UpstreamServiceVersion upstreamServiceVersion = upstreamServiceVersionRepository.findByServiceCodeAndEnv(item.getServiceCode(), item.getEnv());
                if (upstreamServiceVersion != null) {
                    item.setServiceName(serviceCodeWithName.get(item.getServiceCode()));
                    item.setServiceAddress(upstreamServiceVersion.getAddress());
                }
            } else {
                item.setEnv(i.getEnv());
                item.setEnvName(i.getName());
            }
            itemList.add(item);
        });
        api.setApiVersions(itemList);
        return apiMapper.toDto(api);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiVersionDto create(ApiVersion resources) {
        resources.setApiCode(new SnowflakeGenerator().next().toString());
        resources.setEnv("DEV");
        Api api = new Api();
        api.setApiCode(resources.getApiCode());
        api.setName(resources.getName());
        api.setMaintainer(resources.getMaintainer());
        api.setDescription(resources.getDescription());
        apiRepository.save(api);
        return apiVersionMapper.toDto(apiVersionRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ApiVersion resources) {
        resources.setEnv("DEV");
        if (!StringUtils.isAllBlank(resources.getName(), resources.getMaintainer(), resources.getDescription())) {
            Api api = new Api();
            api.setApiCode(resources.getApiCode());
            Optional<? extends Api> optionalApi = apiRepository.findOne(Example.of(api));
            if (optionalApi.isPresent()) {
                api = optionalApi.get();
                api.setApiCode(resources.getApiCode());
                api.setName(resources.getName());
                api.setMaintainer(resources.getMaintainer());
                api.setDescription(resources.getDescription());
                apiRepository.save(api);
            }
        }
        ApiVersion apiVersion = apiVersionRepository.findById(resources.getId()).orElseGet(ApiVersion::new);
        ValidationUtil.isNull(apiVersion.getId(), "ApiVersion", "id", resources.getId());
        apiVersion.copy(resources);
        apiVersionRepository.save(apiVersion);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            apiVersionRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ApiVersionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ApiVersionDto apiVersion : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("api code", apiVersion.getApiCode());
            map.put("api request method", apiVersion.getMethod());
            map.put("api request origin url", apiVersion.getSrcUrl());
            map.put("api proxy forwarding url", apiVersion.getDesUrl());
            map.put("api service code", apiVersion.getServiceCode());
            map.put("api env", apiVersion.getEnv());
            map.put("rate limit switch", apiVersion.getNeedRateLimit());
            map.put("rate limit size", apiVersion.getRateLimit());
            map.put("fallback switch", apiVersion.getNeedFallback());
            map.put("fallback information", apiVersion.getFallback());
            map.put("monitor switch", apiVersion.getNeedMonitor());
            map.put("api request timeout", apiVersion.getReadTimeout());
            map.put("ignore header params", apiVersion.getIgnoreHeaderParams());
            map.put("ignore query params", apiVersion.getIgnoreQueryParams());
            map.put("record created time", apiVersion.getGmtCreated());
            map.put("record updated time", apiVersion.getGmtModified());
            map.put("who created the record", apiVersion.getCreator());
            map.put("who updated the record", apiVersion.getModifier());
            map.put("logical delete identifier(Y-effective,N-ineffective)", apiVersion.getIsDeleted());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional
    public void sync(ApiSyncDto resources) {
        List<String> apiCodes = resources.getApiCodes();
        String fromEnv = resources.getFromEnv();
        String toEnv = resources.getToEnv();
        if (CollectionUtils.isEmpty(apiCodes) || StringUtils.isBlank(fromEnv) || StringUtils.isBlank(toEnv)) {
            throw new RuntimeException("Parameter can not be null");
        }
        if (fromEnv.equals(toEnv)) {
            throw new RuntimeException("env must differ");
        }

        apiCodes.forEach(i -> {
            ApiVersion fromApi = apiVersionRepository.findByApiCodeAndEnv(i, fromEnv);
            if (fromApi == null) {
                throw new RuntimeException("from api not exists");
            }
            ApiVersion toApi = new ApiVersion();
            toApi.setApiCode(i);
            toApi.setEnv(toEnv);
            apiVersionRepository.delete(toApi);
            toApi.copy(fromApi);
            toApi.setId(null);
            toApi.setEnv(toEnv);
            apiVersionRepository.save(toApi);
        });
    }
}
