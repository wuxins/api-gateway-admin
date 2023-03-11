package com.zhongan.life;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

/**
 * TODO
 *
 * @className: T
 * @author: liuyicai
 * @date: 2023-03-11 16:59
 */
public class T {
    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        config.setProjectPath("/Users/liuyicai/work/gateway/api-gateway-admin"); // root project path
        config.setProjectName("api gateway admin"); // project name
        config.setApiVersion("V1.0");       // api version
        config.setDocsPath("/Users/liuyicai/work/gateway/api-gateway-admin/doc"); // api docs target path
        config.setAutoGenerate(Boolean.TRUE);  // auto generate
        Docs.buildHtmlDocs(config); // execute to generate
    }
}
