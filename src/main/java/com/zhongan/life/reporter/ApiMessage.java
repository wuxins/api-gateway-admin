package com.zhongan.life.reporter;

/**
 * TODO
 *
 * @className: SysInterfaceDTO
 * @author: liuyicai
 * @date: 2023-03-04 22:52
 */

import lombok.Data;

import java.util.List;

@Data
public class ApiMessage {
    private String url;
    private String method;
    private List<String> fieldNames;
}
