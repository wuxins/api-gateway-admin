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
package com.zhongan.life.modules.gateway.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
 * @author yicai.liu
 *
 * @description /
 * @date 2023-02-25
 **/
@Entity
@Data
@Table(name = "t_api_version")
@DynamicInsert
@DynamicUpdate
public class ApiVersion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "primary key")
    private Long id;

    @Column(name = "`api_code`")
    @ApiModelProperty(value = "api code")
    private String apiCode;

    @Column(name = "`method`")
    @ApiModelProperty(value = "api request method")
    private String method;

    @Column(name = "`src_url`")
    @ApiModelProperty(value = "api request origin url")
    private String srcUrl;

    @Column(name = "`des_url`")
    @ApiModelProperty(value = "api proxy forwarding url")
    private String desUrl;

    @Column(name = "`service_code`")
    @ApiModelProperty(value = "api service code")
    private String serviceCode;

    @Column(name = "`env`")
    @ApiModelProperty(value = "api env")
    private String env;

    @Column(name = "`need_rate_limit`")
    @ApiModelProperty(value = "rate limit switch")
    private String needRateLimit;

    @Column(name = "`rate_limit`")
    @ApiModelProperty(value = "rate limit size")
    private Integer rateLimit;

    @Column(name = "`need_fallback`")
    @ApiModelProperty(value = "fallback switch")
    private String needFallback;

    @Column(name = "`fallback`")
    @ApiModelProperty(value = "fallback information")
    private String fallback;

    @Column(name = "`need_monitor`")
    @ApiModelProperty(value = "monitor switch")
    private String needMonitor;

    @Column(name = "`read_timeout`")
    @ApiModelProperty(value = "api request timeout")
    private Integer readTimeout;

    @Column(name = "`ignore_header_params`")
    @ApiModelProperty(value = "ignore header params")
    private String ignoreHeaderParams;

    @Column(name = "`ignore_query_params`")
    @ApiModelProperty(value = "ignore query params")
    private String ignoreQueryParams;

    @Column(name = "`gmt_created`")
    @ApiModelProperty(value = "record created time")
    private Timestamp gmtCreated;

    @Column(name = "`gmt_modified`")
    @ApiModelProperty(value = "record updated time")
    private Timestamp gmtModified;

    @Column(name = "`creator`")
    @ApiModelProperty(value = "who created the record")
    private String creator;

    @Column(name = "`modifier`")
    @ApiModelProperty(value = "who updated the record")
    private String modifier;

    @Column(name = "`is_deleted`")
    @ApiModelProperty(value = "logical delete identifier(Y-effective,N-ineffective)")
    private String isDeleted;

    @Transient
    @ApiModelProperty(value = "api group name")
    private String name;

    @Transient
    @ApiModelProperty(value = "api maintainer")
    private String maintainer;

    @Transient
    @ApiModelProperty(value = "api description")
    private String description;

    @Transient
    private String envName;
    @Transient
    private String serviceName;
    @Transient
    private String serviceAddress;

    public void copy(ApiVersion source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
