package com.zhongan.life.reporter;

import com.alibaba.fastjson.JSON;
import com.zhongan.life.modules.gateway.domain.UpstreamService;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * TODO
 *
 * @className: ApiReporter
 * @author: liuyicai
 * @date: 2023-03-04 22:47
 */
@Component
public class ApiReporter implements ApplicationContextAware {
    @SneakyThrows
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (Map.Entry mappingInfoHadlerMethod : map.entrySet()) {
            ApiMessage dto = new ApiMessage();
            // 获取url 及 请求类型
            RequestMappingInfo info = (RequestMappingInfo) mappingInfoHadlerMethod.getKey();
            Set<String> urlSet = info.getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            dto.setUrl(urlSet.toString());
            dto.setMethod(methods.toString());
            // 获取接口信息
            HandlerMethod method = (HandlerMethod) mappingInfoHadlerMethod.getValue();
            // 字段实体对象
            Class<?>[] parameterTypes = method.getMethod().getParameterTypes();
            for (Class<?> type : parameterTypes) {
                if ("[POST]".equals(methods.toString())) {
                    dto.setFieldNames(parseFieldNamesFromBean(type));
                }
                //将数据添加进redis 中 MAP_SYS_INTERFACE 以路径为key
                System.out.println(JSON.toJSONString(dto, true));
            }
        }
    }

    @SneakyThrows
    private static List<String> parseFieldNamesFromBean(Class<?> type) {
        List<String> results = new ArrayList<>();
        if (getBaseTypeSet().contains(type.getName())) {
            return results;
        }
        Class clazz = Class.forName(type.getName());
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            String name = field.getName();
            if (!"serialVersionUID".equals(name)) {
                results.add(name);
            }
            results.addAll(parseFieldNamesFromBean(field.getType()));
            //parseFieldNamesFromBean(field.getType());
        }
        return results;
    }

    public static void main(String[] args) {
        System.out.println(parseFieldNamesFromBean(UpstreamService.class));
    }

    private static Set<String> getBaseTypeSet() {
        String[] types = {Integer.class.getName(), Double.class.getName(), Long.class.getName(), Short.class.getName(),
                Byte.class.getName(), Boolean.class.getName(), Character.class.getName(), Float.class.getName(),
                String.class.getName(), Object.class.getName(), BigDecimal.class.getName(), Timestamp.class.getName(),
                Date.class.getName(), int.class.getName(), double.class.getName(), long.class.getName(),
                short.class.getName(), byte.class.getName(), boolean.class.getName(), char.class.getName(),
                float.class.getName()};
        return new HashSet<>(Arrays.asList(types));
    }
}
