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
package com.zhongan.life.modules.gateway.service.dto;

import lombok.Data;
import com.zhongan.life.modules.gateway.domain.UpstreamServiceVersion;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author yicai.liu
 * 
 * @description /
 * @date 2023-02-25
 **/
@Data
public class UpstreamServiceDto implements Serializable {

    /**
     * primary key
     */
    private Long id;

    /**
     * service name
     */
    private String name;

    /**
     * service code
     */
    private String serviceCode;

    /**
     * record created time
     */
    private Timestamp gmtCreated;

    /**
     * record updated time
     */
    private Timestamp gmtModified;

    /**
     * who created the record
     */
    private String creator;

    /**
     * who updated the record
     */
    private String modifier;

    /**
     * logical delete identifier(Y-effective,N-ineffective)
     */
    private String isDeleted;

    private List<UpstreamServiceVersion> upstreamServiceVersions;
}
