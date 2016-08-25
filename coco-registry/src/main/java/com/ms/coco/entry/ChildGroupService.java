package com.ms.coco.entry;

import java.util.List;

import com.ms.coco.model.ServerNode;

/**
 * @author wanglin/netboy
 * @version 创建时间：2016年8月11日 下午6:30:54
 * @func
 */
public interface ChildGroupService {
    List<? extends ServerNode> getAllReaders();

    List<? extends ServerNode> getAvailableReaders();

    boolean groupStatus();
}