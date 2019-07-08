package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldTypeConfig;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldTypeSet;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class WorldTypeConfig implements iDAO, iBeanCheckable
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_world_type_config";

    public WorldTypeConfig(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    @Override
    public int createRecord(Object o, boolean bln)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        //检查CONFIG来源
        BeanCtxWorldTypeConfig bean = (BeanCtxWorldTypeConfig) o;
        WorldTypeSet wts = new WorldTypeSet(up);
        BeanCtxWorldTypeSet bset = (BeanCtxWorldTypeSet) wts.getRecordByID(bean.getSetOID());
        if (wts.checkBean(bset) == false)
        {
            return ctxReturn.WLDTPS_WSET_ERROR;
        }
        //检查TAG
        if (getConfigByTag(bean.getCfgTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        //检查OID
        if (bean.getOID() != 0)
        {
            if (getRecordByID(bean.getOID()) != null)
            {
                return iReturn.BEAN_OID_REPEAT;
            }
        }
        //数据填充
        if (bean.getOID() == 0)
        {
            bean.setOID(OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH));
        }
        //数据库操作
        return jcommon.eInsert(sn, bean, table, bln);
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanCtxWorldTypeConfig.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    public BeanCtxWorldTypeConfig getConfigByTag(String tag)
    {
        String sql = "select * from " + table + " where cfgTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanCtxWorldTypeConfig.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanCtxWorldTypeConfig) ls.get(0);
    }

    public List getConfigsByWorldSet(int setid)
    {
        String sql = "select * from " + table + " where setOID = " + setid;
        return sn.querySQL(sql, BeanCtxWorldTypeConfig.class);
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanCtxWorldTypeConfig bean = (BeanCtxWorldTypeConfig) o;
        BeanCtxWorldTypeConfig obean = (BeanCtxWorldTypeConfig) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setCfgName(bean.getCfgName());
        obean.setDescp(bean.getDescp());
        obean.setProbability(bean.getProbability());
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    @Override
    public int deleteRecord(int i)
    {
        //检查RES CONFIG DATA是否存在
        WorldTypeConfigResource wtcr = new WorldTypeConfigResource(up);
        List lcs = wtcr.getResourceListByConfig(i);
        if (lcs != null)
        {
            return iReturn.BEAN_FOREIGN_KEY_ERROR;
        }
        String sql = "delete from " + table + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public List getAllRecord()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRecordByID(int i)
    {
        String sql = "select * from " + table + " where OID = " + i;
        List ls = sn.querySQL(sql, BeanCtxWorldTypeConfig.class);
        if (ls == null)
        {
            return null;
        }
        return ls.get(0);
    }

    @Override
    public boolean checkBean(Object o)
    {
        if (o == null)
        {
            return false;
        }
        if (o.getClass() != BeanCtxWorldTypeConfig.class)
        {
            return false;
        }
        return true;
    }
}
