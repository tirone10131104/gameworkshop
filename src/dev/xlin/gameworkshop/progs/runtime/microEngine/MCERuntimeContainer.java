package dev.xlin.gameworkshop.progs.runtime.microEngine;

import dev.xlin.gameworkshop.progs.iReturn;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCE微引擎运行时容器
 *
 * @author 刘祎鹏
 */
public class MCERuntimeContainer
{

    private static ConcurrentHashMap MCES = new ConcurrentHashMap();

    public int registMCEngine(MCEngine mce)
    {
        if (mce == null)
        {
            return -iReturn.PARAM_NULL;
        }
        if (MCES.containsValue(mce))
        {
            return -iReturn.MCES_MCE_ALREADY_IN;
        }
        if (mce.getMCEID() == 0)
        {
            return -iReturn.MCES_MCS_NOT_INITIALIZED;
        }
        //检查配置数量
        int ilmt = mce.getInstanceLimit();
        if (ilmt != 0)
        {
            //有数量配置关系
            //获取当前MCES中已存在的MCEID实例个数
            int count = getCountOfMCE(mce.getMCEID());
            if (count >= ilmt)
            {
                return -iReturn.MCES_MCS_INST_OUT_LIMIT;
            }
        }
        int id = _createMCEID();
        //通过反射，直接为MCE赋予进程ID 
        Class clsMCE = mce.getClass();
        try
        {
            Field[] flds = clsMCE.getDeclaredFields();
            for (int i = 0; i < flds.length; i++)
            {
                Field fld = flds[i];
                if (fld.getName().equals("instanceID"))
                {
                    fld.setAccessible(true);
                    fld.setInt(mce, id);
                }
            }  
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return iReturn.MCES_MCS_MCE_INIT_REG_FAIL;
        }
        MCES.put(id, mce);
        return id;
    }

    //获取当前MCES中MCE对应的实例个数
    private int getCountOfMCE(int mid)
    {
        Iterator itor = MCES.values().iterator();
        int count = 0;
        while (itor.hasNext())
        {

            MCEngine mce = (MCEngine) itor.next();
            if (mce.getMCEID() == mid)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * 根据ID 获取MCE示例
     *
     * @param instid
     * @return
     */
    public MCEngine getMCEngineByID(long instid)
    {
        if (MCES.containsKey(instid) == false)
        {
            return null;
        }
        return (MCEngine) MCES.get(instid);
    }

    /**
     *
     * @param itask
     * @param instid
     * @return
     */
    public int putTask(iMCETask itask, int instid)
    {
        MCEngine mce = getMCEngineByID(instid);
        if (mce == null)
        {
            return -iReturn.MCES_MCE_NOT_EXIST;
        }
        return mce.putTask(itask);
    }

    private int _createMCEID()
    {
        Random r = new Random();
        while (true)
        {
            int id = r.nextInt();
            if (id < 0)
            {
                continue;
            }
            if (MCES.contains(id) == false)
            {
                return id;
            }
        }
    }

    public boolean unregistMCE(MCEngine mce)
    {
        return true;
    }

}
