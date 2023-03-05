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
import com.zhongan.life.modules.gateway.repository.EnvironmentRepository;
import com.zhongan.life.modules.gateway.service.mapstruct.EnvironmentMapper;
import com.zhongan.life.utils.FileUtil;
import com.zhongan.life.utils.PageUtil;
import com.zhongan.life.utils.QueryHelp;
import com.zhongan.life.utils.ValidationUtil;
import com.zhongan.life.modules.gateway.domain.Environment;
import lombok.RequiredArgsConstructor;
import com.zhongan.life.modules.gateway.service.EnvironmentService;
import com.zhongan.life.modules.gateway.service.dto.EnvironmentDto;
import com.zhongan.life.modules.gateway.service.dto.EnvironmentQueryCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* 
* @description 服务实现
* @author wuxins
* @date 2023-02-24
**/
@Service
@RequiredArgsConstructor
public class EnvironmentServiceImpl implements EnvironmentService {

    private final EnvironmentRepository environmentRepository;
    private final EnvironmentMapper environmentMapper;

    @Override
    public Map<String,Object> queryAll(EnvironmentQueryCriteria criteria, Pageable pageable){
        Page<Environment> page = environmentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(environmentMapper::toDto));
    }

    @Override
    public List<EnvironmentDto> queryAll(EnvironmentQueryCriteria criteria){
        return environmentMapper.toDto(environmentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public EnvironmentDto findById(Long id) {
        Environment environment = environmentRepository.findById(id).orElseGet(Environment::new);
        ValidationUtil.isNull(environment.getId(),"Environment","id",id);
        return environmentMapper.toDto(environment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EnvironmentDto create(Environment resources) {
        if(environmentRepository.findByEnv(resources.getEnv()) != null){
            throw new EntityExistException(Environment.class,"env",resources.getEnv());
        }
        return environmentMapper.toDto(environmentRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Environment resources) {
        Environment environment = environmentRepository.findById(resources.getId()).orElseGet(Environment::new);
        ValidationUtil.isNull( environment.getId(),"Environment","id",resources.getId());
        Environment environment1 = environmentRepository.findByEnv(resources.getEnv());
        if(environment1 != null && !environment1.getId().equals(environment.getId())){
            throw new EntityExistException(Environment.class,"env",resources.getEnv());
        }
        environment.copy(resources);
        environmentRepository.save(environment);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            environmentRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<EnvironmentDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EnvironmentDto environment : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("environment name", environment.getName());
            map.put("environment code", environment.getEnv());
            map.put("record created time", environment.getGmtCreated());
            map.put("record updated time", environment.getGmtModified());
            map.put("who created the record", environment.getCreator());
            map.put("who updated the record", environment.getModifier());
            map.put("logical delete identifier(Y-effective,N-ineffective)", environment.getIsDeleted());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
