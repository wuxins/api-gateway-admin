package com.zhongan.life.modules.gateway.service;

import com.zhongan.life.modules.gateway.domain.UpstreamService;
import com.zhongan.life.modules.gateway.service.dto.UpstreamServiceDto;
import com.zhongan.life.modules.gateway.service.dto.UpstreamServiceQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yicai.liu
 * @description 服务接口
 * @date 2023-02-25
 **/
public interface UpstreamServiceService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(UpstreamServiceQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<UpstreamServiceDto>
     */
    List<UpstreamServiceDto> queryAll(UpstreamServiceQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return UpstreamServiceDto
     */
    UpstreamServiceDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return UpstreamServiceDto
     */
    UpstreamServiceDto create(UpstreamService resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(UpstreamService resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<UpstreamServiceDto> all, HttpServletResponse response) throws IOException;
}
