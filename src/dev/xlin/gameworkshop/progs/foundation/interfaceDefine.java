package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfDefine;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.List;

public class interfaceDefine implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_prog_interface_define";

    public interfaceDefine(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    @Override
    public int createRecord(Object o, boolean bln)
    {
        int r0 = doCheckParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanProgIntfDefine bean = (BeanProgIntfDefine) o;
        //check tag repeat 
        if (getInterfaceByTag(bean.getIntfTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
        bean.setOID(oid);
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        return jcommon.eInsert(sn, bean, table, bln);
    }

    public BeanProgIntfDefine getInterfaceByTag(String tag)
    {
        String sql = "select * from " + table + " where intfTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanProgIntfDefine.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanProgIntfDefine) ls.get(0);
    }

    private int doCheckParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanProgIntfDefine.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = doCheckParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanProgIntfDefine bean = (BeanProgIntfDefine) o;
        BeanProgIntfDefine obean = (BeanProgIntfDefine) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //data transfer
        obean.setActionFunction(bean.getActionFunction());
        obean.setIntfAddress(bean.getIntfAddress());
        obean.setIntfDesp(bean.getIntfDesp());
        obean.setIntfName(bean.getIntfName());
        obean.setParamFunction(bean.getParamFunction());
        obean.setParamList(bean.getParamList());
        obean.setTypeID(bean.getTypeID());
        //update data
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    @Override
    public int deleteRecord(int i)
    {
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_DELETE + " where OID = " + i;
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
        List ls = sn.querySQL(sql, BeanProgIntfDefine.class);
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
        if (o.getClass() != BeanProgIntfDefine.class)
        {
            return false;
        }
        BeanProgIntfDefine bean = (BeanProgIntfDefine) o;
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }
    
    public List getInterfacesByType (int tpid , boolean showAll )
    {
        String sql = "select * from " + table+" where typeID = " + tpid;
        if (showAll == false )
        {
             sql = sql  +" and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, BeanProgIntfDefine.class);
    }

    @Override
    public int revertBean(int i)
    {
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_ACTIVE + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public int destroyBean(int i)
    {
        BeanProgIntfDefine bean = (BeanProgIntfDefine) getRecordByID(i);
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
