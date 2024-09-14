package com.laigeoffer.pmhub.system.service;

import com.laigeoffer.pmhub.system.domain.pmhubAsync;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 异步任务管理 服务层
 *
 * @author zw
 */
public interface IpmhubAsyncService {

    /**
     * 创建异步任务记录
     *
     * @param asyncName 异步任务名
     * @param asyncType 异步任务类型
     * @param createBy  创建者
     * @return id
     */
    pmhubAsync addAsyncJob(String asyncName, String asyncType, String createBy);

    /**
     * 更新任务状态
     * @param pmhubAsync 任务信息
     */
    void updateAsyncJob(pmhubAsync pmhubAsync);


    /**
     * 查询异步任务信息
     * @param pmhubAsync
     * @return {@link List}<{@link pmhubAsync}>
     */
    List<pmhubAsync> list(pmhubAsync pmhubAsync);

    /**
     * 查询单条异步任务信息
     * @param id id
     * @return {@link List}<{@link pmhubAsync}>
     */
    pmhubAsync load(String id);

    /**
     * 删除
     * @param ids ids
     */
    void delete(String[] ids);



    void downloadFile(String id, String user, HttpServletResponse response);
}
