package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldTypeMain;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import dev.xlin.tools.constChk;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class worldTypeMain implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_world_type_main";

    public worldTypeMain(wakeup _up)
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
        beanCtxWorldTypeMain bean = (beanCtxWorldTypeMain) o;
        if (getWorldTypeMainByTag(bean.getWmTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        int rlg = checkDataLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        if (bean.getOID() != 0)
        {
            beanCtxWorldTypeMain obean = (beanCtxWorldTypeMain) getRecordByID(bean.getOID());
            if (obean != null)
            {
                return iReturn.BEAN_OID_REPEAT;
            }
        }
        //准备
        if (bean.getOID() == 0)
        {
            int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
            bean.setOID(oid);
        }
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        //数据库
        return jcommon.eInsert(sn, bean, table, bln);
    }

    public beanCtxWorldTypeMain getWorldTypeMainByTag(String tag)
    {
        String sql = "select * from " + table + " where wmTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, beanCtxWorldTypeMain.class);
        if (ls == null)
        {
            return null;
        }
        return (beanCtxWorldTypeMain) ls.get(0);
    }

    private int checkDataLogic(beanCtxWorldTypeMain bean)
    {
        if (constChk.isConst(ctxConst.class, "WORLD_TYPE_", bean.getWorldTypeCst()) == false)
        {
            return ctxReturn.WLDTPS_CONST_ERROR;
        }
        return 0;
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != beanCtxWorldTypeMain.class)
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
        beanCtxWorldTypeMain bean = (beanCtxWorldTypeMain) o;
        beanCtxWorldTypeMain obean = (beanCtxWorldTypeMain) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setDescp(bean.getDescp());
        obean.setWmName(bean.getWmName());
        obean.setHide(bean.getHide());
        //数据库修改
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    public List getWorldMainlistByType(int tpid, boolean showAll)
    {
        String sql = "select * from " + table + " where worldTypeCst = " + tpid;
        if (showAll == false)
        {
            sql = sql + " and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, beanCtxWorldTypeMain.class);
    }

    @Override
    public int deleteRecord(int i)
    {
        //检查Sets是否存在
        beanCtxWorldTypeMain bmain = (beanCtxWorldTypeMain) getRecordByID(i);
        if (checkBean(bmain) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        worldTypeSet wts = new worldTypeSet(up);
        List lset = wts.getWorldSetsByMainType(bmain.getOID(), true);
        if (lset != null)
        {
            return iReturn.BEAN_FOREIGN_KEY_ERROR;
        }
        //数据操作status
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_DELETE
                + " where OID = " + bmain.getOID();
        System.err.println("sql = " + sql );
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
        List ls = sn.querySQL(sql, beanCtxWorldTypeMain.class);
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
        if (o.getClass() != beanCtxWorldTypeMain.class)
        {
            return false;
        }
        beanCtxWorldTypeMain bean = (beanCtxWorldTypeMain) o;
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int revertBean(int i)
    {
        beanCtxWorldTypeMain bmain = (beanCtxWorldTypeMain) getRecordByID(i);
        if (bmain == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (bmain.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_CANT_REVERT;
        }
        //数据操作status
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_ACTIVE
                + " where OID = " + bmain.getOID();
        return jcommon.eExcute(up, sql);
    }

    @Override
    public int destroyBean(int i)
    {
        beanCtxWorldTypeMain bmain = (beanCtxWorldTypeMain) getRecordByID(i);
        if (bmain == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (bmain.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_CANT_REVERT;
        }
        String sql = "delete from " + table + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public List getDeleted()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
