package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldTypeSet;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class WorldTypeSet implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_world_type_set";

    public WorldTypeSet(wakeup _up)
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
        BeanCtxWorldTypeSet bean = (BeanCtxWorldTypeSet) o;
        //检查TAG和OID
        if (bean.getOID() != 0)
        {
            if (getRecordByID(bean.getOID()) != null)
            {
                return iReturn.BEAN_OID_REPEAT;
            }
        }
        if (getTypeSetByTag(bean.getSetTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        //检查逻辑，
        WorldTypeMain wtm = new WorldTypeMain(up);
        if (wtm.checkBean(wtm.getRecordByID(bean.getWorldMainOID())) == false)
        {
            return ctxReturn.WLDTPS_WMAIN_ERROR;
        }
        //数据准备
        if (bean.getOID() == 0)
        {
            bean.setOID(OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH));
        }
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        //数据库操作
        return jcommon.eInsert(sn, bean, table, bln);
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanCtxWorldTypeSet.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanCtxWorldTypeSet bean = (BeanCtxWorldTypeSet) o;
        BeanCtxWorldTypeSet obean = (BeanCtxWorldTypeSet) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setSetName(bean.getSetName());
        //修改
        return jcommon.eUpdate(sn, obean, table, "OID", false);
    }

    @Override
    public int deleteRecord(int i)
    {
        BeanCtxWorldTypeSet bean = (BeanCtxWorldTypeSet) getRecordByID(i);
        if (checkBean(bean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        WorldTypeConfig wtc = new WorldTypeConfig(up);
        List lcs = wtc.getConfigsByWorldSet(bean.getOID());
        if (lcs != null)
        {
            return iReturn.BEAN_FOREIGN_KEY_ERROR;
        }
        //修改状态
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_DELETE
                + " where OID = " + i;
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
        List ls = sn.querySQL(sql, BeanCtxWorldTypeSet.class);
        if (ls == null)
        {
            return null;
        }
        return ls.get(0);
    }

    public BeanCtxWorldTypeSet getTypeSetByTag(String tag)
    {
        String sql = "select * from " + table + " where setTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanCtxWorldTypeSet.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanCtxWorldTypeSet) ls.get(0);
    }

    public List getWorldSetsByMainType(int tpid, boolean showAll)
    {
        String sql = "select * from " + table + " where worldMainOID = " + tpid;
        if (showAll == false)
        {
            sql = sql + " and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, BeanCtxWorldTypeSet.class);
    }

    @Override
    public boolean checkBean(Object o)
    {
        if (o == null)
        {
            return false;
        }
        if (o.getClass() != BeanCtxWorldTypeSet.class)
        {
            return false;
        }
        BeanCtxWorldTypeSet bean = (BeanCtxWorldTypeSet) o;
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int revertBean(int i)
    {
        BeanCtxWorldTypeSet bean = (BeanCtxWorldTypeSet) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (bean.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_CANT_REVERT;
        }
        //修改状态
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_ACTIVE
                + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public int destroyBean(int i)
    {
        BeanCtxWorldTypeSet bean = (BeanCtxWorldTypeSet) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (bean.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_NOT_READY_DESTROY;
        }
        //修改状态
        String sql = "delete from  " + table + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public List getDeleted()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

//
//LOG
//TIME:
//REC:
//
