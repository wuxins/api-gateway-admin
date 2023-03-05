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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author yicai.liu
 * @description /
 * @date 2023-02-25
 **/
@Entity
@Data
@Table(name = "t_api")
@DynamicInsert
@DynamicUpdate
public class Api implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "primary key")
    private Long id;

    @Column(name = "`name`")
    @ApiModelProperty(value = "api group name")
    private String name;

    @Column(name = "`api_code`")
    @ApiModelProperty(value = "api code")
    private String apiCode;

    @Column(name = "`maintainer`")
    @ApiModelProperty(value = "api maintainer")
    private String maintainer;

    @Column(name = "`description`")
    @ApiModelProperty(value = "api description")
    private String description;

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
    private List<ApiVersion> apiVersions;

    public void copy(Api source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
