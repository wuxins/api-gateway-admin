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
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;

/**
* 
* @description /
* @author yicai.liu
* @date 2023-02-25
**/
@Entity
@Data
@Table(name="t_upstream_service")
@DynamicInsert
@DynamicUpdate
public class UpstreamService implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "primary key")
    private Long id;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "service name")
    private String name;

    @Column(name = "`service_code`",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "service code")
    private String serviceCode;

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
    private List<UpstreamServiceVersion> upstreamServiceVersions;

    public void copy(UpstreamService source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
