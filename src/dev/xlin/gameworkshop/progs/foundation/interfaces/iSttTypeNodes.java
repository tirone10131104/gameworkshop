package dev.xlin.gameworkshop.progs.foundation.interfaces;

import java.util.List;

/**
 *  分类树下节点操作程序
 * @author 刘祎鹏
 */
public interface iSttTypeNodes
{
    
    /**
     * 根据TPID 获取分类节点下的所有数据项
     * @param tpid
     * @param showAll
     * @return 
     */
    public List getObjectsByType (int tpid , boolean showAll);
    
}
