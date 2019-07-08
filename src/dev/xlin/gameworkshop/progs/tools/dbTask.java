/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.xlin.gameworkshop.progs.tools;

import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;

/**
 *
 * @author Administrator
 */
public class dbTask
{

    public static void openManualTask(wakeup up)
    {
        up.setAutoCommit(false);
    }

    /**
     * 结束数据库手工事务，返回用户指定的错误常量值
     *
     * @param up
     * @param fail
     * @return
     */
    public static int returnFail(wakeup up, int fail)
    {
        up.roolBack();
        up.setAutoCommit(true);
        return fail;
    }

    /**
     * 结束数据库手工事务，返回成功标志
     *
     * @param up
     * @return
     */
    public static int returnSuccess(wakeup up)
    {
        up.commit();
        up.setAutoCommit(true);
        return iDAO.OPERATE_SUCCESS;
    }
}
