package dev.xlin.gameworkshop.progs.runtime.microEngine;

import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.tols.data.JDBSession;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.OIDCreator;
import dev.xlin.tools.constChk;
import java.util.List;

/**
 * 微引擎配置数据管理程序
 *
 * @author 刘祎鹏
 */
public class MCEConfigDAO implements dev.xlin.tols.interfaces.JDAO
{

    private wakeup up = null;
    private JDBSession sn = null;

    public MCEConfigDAO(wakeup _up)
    {
        up = _up;
        sn = new JDBSession(up);
    }

    @Override
    public String getBaseTable()
    {
        return "ic_mce_config_main";
    }

    @Override
    public String getBaseId()
    {
        return "OID";
    }

    @Override
    public long save(Object o)
    {
        int r0 = _checkParamBean(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanMCEngineConfigMain bean = (BeanMCEngineConfigMain) o;
        int r1 = _checkLogic(bean);
        if (r1 != 0)
        {
            return r1;
        }
        if (bean.getMceTag().equals("") == false)
        {
            if (getMCEConfigByTag(bean.getMceTag()) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        //数据补全
        if (bean.getOID() == 0)
        {
            bean.setOID(OIDCreator.createOID_simple(up, getBaseTable(), getBaseId()));
        }
        bean.setStatus(OBJECT_STATUS_ACTIVE);
        if (bean.getMceTag().equals(""))
        {
            bean.setMceTag(tagCreator.createDataTag(up, getBaseTable(), "mceTag", 4, 4));
        }
        return sn.save(bean);
    }

    private int _checkParamBean(Object o)
    {
        if (o == null)
        {
            return PARAM_OBJECT_NULL;
        }
        if (o.getClass() != getEntityClass())
        {
            return PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    //bean受信
    //检查MCES配置项目的逻辑
    public int _checkLogic(BeanMCEngineConfigMain bean)
    {
        //检查TPS ，TPS从1-30
        if (bean.getMaxTps() < 1 || bean.getMaxTps() > 30)
        {
            return iReturn.MCES_MCE_TPS_ERROR;
        }
        //检查启动模式
        if (constChk.isConst(iConst.class, "MCES_MCST_", bean.getStartMethod()) == false)
        {
            return iReturn.MCES_MCE_START_MTD_ERROR;
        }
        //检查空闲模式
        if (constChk.isConst(iConst.class, "MCES_EMPTY_MTD_", bean.getEmptyMethod()) == false)
        {
            return iReturn.MCES_MCE_EMPTY_MTD_ERROR;
        }
        //检查等候时长
        if (bean.getEmptyWaitLong() < 0)
        {
            return iReturn.MCES_MCE_WAIT_ERROR;
        }
        //检查线程实例数量
        if (bean.getInstanceLimit() < 0 || bean.getInstanceLimit() > 10)
        {
            return iReturn.MCES_MCE_INST_LMT_ERROR;
        }
        return 0;
    }

    @Override
    public int update(Object o)
    {
        int r0 = _checkParamBean(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanMCEngineConfigMain bean = (BeanMCEngineConfigMain) o;
        BeanMCEngineConfigMain obean = (BeanMCEngineConfigMain) get(bean.getOID());
        int r2 = check(obean);
        if (r2 != 0)
        {
            return r2;
        }
        int r1 = _checkLogic(bean);
        if (r1 != 0)
        {
            return r1;
        }
        ////数据修改
        obean.setEmptyMethod(bean.getEmptyMethod());
        obean.setEmptyWaitLong(bean.getEmptyWaitLong());
        obean.setInstanceLimit(bean.getInstanceLimit());
        obean.setMaxTps(bean.getMaxTps());
        obean.setMceDescp(bean.getMceDescp());
        obean.setMceName(bean.getMceName());
        obean.setStartMethod(bean.getStartMethod());
        if (bean.getMceTag().equals(obean.getMceTag()) == false)
        {
            //检查到标签更换需求
            if (getMCEConfigByTag(bean.getMceTag()) == null)
            {
                obean.setMceTag(bean.getMceTag());
            }
        }
        return sn.update(obean);
    }

    @Override
    public int disable(Object o)
    {
        if (o == null)
        {
            return PARAM_OBJECT_NULL;
        }
        BeanMCEngineConfigMain bean = (BeanMCEngineConfigMain) get(o);
        if (bean == null)
        {
            return OBJECT_NOTEXIST;
        }
        if (bean.getStatus() != OBJECT_STATUS_ACTIVE)
        {
            return NOT_READY_TO_DISABLE;
        }
        String sql = "update " + getBaseTable() + " set status = " + OBJECT_STATUS_DISABLE + " where " + getBaseId() + " = " + o;
        return sn.execute(sql);
    }

    @Override
    public int enable(Object o)
    {
        if (o == null)
        {
            return PARAM_OBJECT_NULL;
        }
        BeanMCEngineConfigMain bean = (BeanMCEngineConfigMain) get(o);
        if (bean == null)
        {
            return OBJECT_NOTEXIST;
        }
        if (bean.getStatus() != OBJECT_STATUS_DISABLE)
        {
            return NO_NEED_TO_ENABLE;
        }
        String sql = "update " + getBaseTable() + " set status = " + OBJECT_STATUS_ACTIVE + " where " + getBaseId() + " = " + o;
        return sn.execute(sql);
    }

    @Override
    public int delete(Object o)
    {
        if (o == null)
        {
            return PARAM_OBJECT_NULL;
        }
        BeanMCEngineConfigMain bean = (BeanMCEngineConfigMain) get(o);
        if (bean == null)
        {
            return OBJECT_NOTEXIST;
        }
        if (bean.getStatus() != OBJECT_STATUS_DISABLE)
        {
            return NOT_READY_TO_DELETE;
        }
        String sql = "delete from " + getBaseTable() +" where " + getBaseId() +" = " + o ; 
        return sn.execute(sql);
    }

    @Override
    public Object get(Object o)
    { 
        String sql = "select * from " + getBaseTable() +" where " + getBaseId() +" = " + o ;
        List ls = sn.query(sql, getEntityClass());
        if (ls == null )
        {
            return null;
        }
        return ls.get(0);
    }

    public List getMCEList (boolean showAll )
    {
        String sql = "select * from " + getBaseTable() ;
        if (showAll == false )
        {
            sql = sql  +" where status = " + OBJECT_STATUS_ACTIVE;
        }
        return sn.query(sql, getEntityClass());
    }
    
    @Override
    public int check(Object o)
    {
        if (o == null)
        {
            return PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanMCEngineConfigMain.class)
        {
            return OBJECT_NOTEXIST;
        }
        BeanMCEngineConfigMain bean = (BeanMCEngineConfigMain) o;
        if (bean.getStatus() != OBJECT_STATUS_ACTIVE)
        {
            return OBJECT_DISABLED;
        }
        return 0;
    }

    public BeanMCEngineConfigMain getMCEConfigByTag(String tag)
    {
        String sql = "select * from " + getBaseTable() + " where mceTag = '" + tag + "'";
        List ls = sn.query(sql, getEntityClass());
        if (ls == null)
        {
            return null;
        }
        return (BeanMCEngineConfigMain) ls.get(0);
    }

    @Override
    public Class getEntityClass()
    {
        return BeanMCEngineConfigMain.class;
    }

}
