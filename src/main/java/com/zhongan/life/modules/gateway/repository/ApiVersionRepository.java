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
package com.zhongan.life.modules.gateway.repository;

import com.zhongan.life.modules.gateway.domain.ApiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
*
* @author yicai.liu
* @date 2023-02-25
**/
public interface ApiVersionRepository extends JpaRepository<ApiVersion, Long>, JpaSpecificationExecutor<ApiVersion> {

    List<ApiVersion> findByApiCode(String apiCode);

    ApiVersion findByApiCodeAndEnv(String apiCode,String dev);
    @Override
    void delete(ApiVersion entity);
}
