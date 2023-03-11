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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.sql.Timestamp;
import java.io.Serializable;

/**
 * @author yicai.liu
 *
 * @description /
 * @date 2023-02-25
 **/
@Data
public class ApiVersionDto implements Serializable {

    /**
     * primary key
     */
    private Long id;

    /**
     * api code
     */
    private String apiCode;

    /**
     * api request method
     */
    private String method;

    /**
     * api request origin url
     */
    private String srcUrl;

    /**
     * api proxy forwarding url
     */
    private String desUrl;

    /**
     * api service code
     */
    private String serviceCode;

    /**
     * api env
     */
    private String env;

    /**
     * rate limit switch
     */
    private String needRateLimit;

    /**
     * rate limit size
     */
    private Integer rateLimit;

    /**
     * fallback switch
     */
    private String needFallback;

    /**
     * fallback information
     */
    private String fallback;

    /**
     * monitor switch
     */
    private String needMonitor;

    /**
     * api request timeout
     */
    private Integer readTimeout;

    /**
     * ignore header params
     */
    private String ignoreHeaderParams;

    /**
     * ignore query params
     */
    private String ignoreQueryParams;

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

    private String name;

    private String maintainer;

    private String description;
    private String envName;
    private String serviceName;
    private String serviceAddress;
}
