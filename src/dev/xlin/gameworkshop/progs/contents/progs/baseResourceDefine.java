package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.beanCtxBaseResource;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItem;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
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
 * 基础资源定义工具
 *
 * @author 刘祎鹏
 */
public class baseResourceDefine implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_base_resource";
    public static int STT_DEF_CTX_RES = 101;

    public baseResourceDefine(wakeup _up)
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
        beanCtxBaseResource bean = (beanCtxBaseResource) o;
        //检查标签
        if (getResourceByTag(bean.getResTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        //检查物体是否被资源占用
        if (getResourceByItem(bean.getItemOID()) != null)
        {
            return iReturn.C_BARS_ITEM_REPEAT;
        }
        //检查物体是否存在
        itemDefine idef = new itemDefine(up);
        beanItem bit = (beanItem) idef.getRecordByID(bean.getItemOID());
        if (idef.checkBean(bit) == false)
        {
            return iReturn.C_BARS_ITEM_NOTEXIST;
        }
        if (bean.getOID() != 0)
        {
            if (getRecordByID(bean.getOID()) != null)
            {
                return iReturn.BEAN_OID_REPEAT;
            }
        }
        else
        {
            int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
            bean.setOID(oid);
        }
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        return jcommon.eInsert(sn, bean, table, false);
    }

    public int moveResrouceToType(int OID, int ntpid)
    {
        beanCtxBaseResource bean = (beanCtxBaseResource) getRecordByID(OID);
        if (checkBean(bean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        String sql = "update " + table + " set typeOID = " + ntpid + " where OID = " + OID;
        return jcommon.eExcute(up, sql);
    }

    public beanCtxBaseResource getResourceByTag(String tag)
    {
        String sql = "select * from " + table + " where resTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, beanCtxBaseResource.class);
        if (ls == null)
        {
            return null;
        }
        return (beanCtxBaseResource) ls.get(0);
    }

    public beanCtxBaseResource getResourceByItem(int iOID)
    {
        String sql = "select * from " + table + " where itemOID = " + iOID;
        List ls = sn.querySQL(sql, beanCtxBaseResource.class);
        if (ls == null)
        {
            return null;
        }
        return (beanCtxBaseResource) ls.get(0);
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != beanCtxBaseResource.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    public List getResListByType(int tpid, boolean showAll)
    {
        String sql = "select * from " + table + " where typeOID = " + tpid;
        if (showAll == false)
        {
            sql = sql + " and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, beanCtxBaseResource.class);
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        beanCtxBaseResource bean = (beanCtxBaseResource) o;
        beanCtxBaseResource obean = (beanCtxBaseResource) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setResName(bean.getResName());
        return jcommon.eUpdate(sn, obean, table, "OID", false);
    }

    @Override
    public int deleteRecord(int i)
    {
        beanCtxBaseResource bean = (beanCtxBaseResource) getRecordByID(i);
        if (checkBean(bean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
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
        List ls = sn.querySQL(sql, beanCtxBaseResource.class);
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
        if (o.getClass() != beanCtxBaseResource.class)
        {
            return false;
        }
        beanCtxBaseResource bean = (beanCtxBaseResource) o;
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int revertBean(int i)
    {
        beanCtxBaseResource bean = (beanCtxBaseResource) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (bean.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_CANT_REVERT;
        }
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_ACTIVE
                + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public int destroyBean(int i)
    {
        beanCtxBaseResource bean = (beanCtxBaseResource) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (bean.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_NOT_READY_DESTROY;
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

//
//LOG
//TIME:
//REC:
//
