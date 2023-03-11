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

import com.zhongan.life.modules.gateway.domain.UpstreamServiceVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yicai.liu
 * @date 2023-02-25
 **/
public interface UpstreamServiceVersionRepository extends JpaRepository<UpstreamServiceVersion, Long>, JpaSpecificationExecutor<UpstreamServiceVersion> {
    /**
     * 根据 ServiceCode 查询
     *
     * @param serviceCode /
     * @return /
     */
    List<UpstreamServiceVersion> findByServiceCode(String serviceCode);

    UpstreamServiceVersion findByServiceCodeAndEnv(String serviceCode, String env);
}
