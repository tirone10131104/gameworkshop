package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfRegister;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.List;

public class interfaceRegister implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_prog_interface_register";

    public interfaceRegister(wakeup _up)
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
        beanProgIntfRegister bean = (beanProgIntfRegister) o;
        //检查标签
        if (getRegisterByTag(bean.getRegTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        //检查源
        interfaceDefine itdef = new interfaceDefine(up);
        beanProgIntfDefine bdef = (beanProgIntfDefine) itdef.getRecordByID(bean.getDefOID());
        if (itdef.checkBean(bdef) == false)
        {
            return iReturn.INTF_DEFINE_NOTEXIST;
        }
        //准备数据
        int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_LONG_MIN, iDAO.OID_LONG_LENTH);
        bean.setOID(oid);
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        //存储
        return jcommon.eInsert(sn, bean, table, bln);
    }
    
    public List getRegsByDef(int def , boolean showAll )
    {
        String sql = "select * from " + table +" where defOID = " + def;
        if (showAll == false )
        {
            sql = sql +" and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, beanProgIntfRegister.class);
    }

    public beanProgIntfRegister getRegisterByTag(String tag)
    {
        String sql = "select * from " + table + " where regTag = '" + tag + "'";
        List ls = sn.querySQL(sql, beanProgIntfRegister.class);
        if (ls == null)
        {
            return null;
        }
        return (beanProgIntfRegister) ls.get(0);
    }

    private int doCheckParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != beanProgIntfRegister.class)
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
        beanProgIntfRegister bean = (beanProgIntfRegister) o;
        beanProgIntfRegister obean = (beanProgIntfRegister) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //data transfer
        obean.setRegDescription(bean.getRegDescription());
        obean.setImplAddress(bean.getImplAddress());
        obean.setPreLoad(bean.getPreLoad());
        obean.setRegName(bean.getRegName());
        //update
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    @Override
    public int deleteRecord(int i)
    {
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
        List ls = sn.querySQL(sql, beanProgIntfRegister.class);
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
        if (o.getClass() != beanProgIntfRegister.class)
        {
            return false;
        }
        beanProgIntfRegister bean = (beanProgIntfRegister) o;
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int revertBean(int i)
    {
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_ACTIVE
                + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public int destroyBean(int i)
    {
        beanProgIntfRegister bean = (beanProgIntfRegister) getRecordByID(i);
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
