package com.zhongan.life.modules.gateway.service.dto;

import com.zhongan.life.modules.gateway.domain.ApiVersion;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author yicai.liu
 * @description /
 * @date 2023-02-25
 **/
@Data
public class ApiDto implements Serializable {

    private Long id;

    private String name;

    private String apiCode;

    private String maintainer;

    private String description;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    private String creator;

    private String modifier;

    private String isDeleted;

    private List<ApiVersion> apiVersions;
}
