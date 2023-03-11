package com.zhongan.life.modules.gateway.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiSyncDto {

    private List<String> apiCodes;

    private String fromEnv;

    private String toEnv;
}
