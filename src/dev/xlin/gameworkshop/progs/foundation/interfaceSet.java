package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfSet;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import dev.xlin.tools.codeTools;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口集管理程序
 *
 * @author 刘祎鹏
 */
public class interfaceSet implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_prog_interface_set";

    public interfaceSet(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    public int createInterfaceSet(BeanProgIntfSet bean)
    {
        return createRecord(bean, false);
    }

    public int updateInterfaceSet(BeanProgIntfSet bean)
    {
        return modifyRecord(bean, false);
    }

    //检查参数O 
    private int checkParamObject(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanProgIntfSet.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    @Override
    public int createRecord(Object o, boolean bln)
    {
        int r0 = checkParamObject(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanProgIntfSet bean = (BeanProgIntfSet) o;
        //检查INTF列表
        int rcl = checkLogics(bean);
        if (rcl != 0)
        {
            return rcl;
        }
        //检查和准备
        int rpb = prepareBean(bean);
        if (rpb != 0)
        {
            return rpb;
        }
        //数据库执行
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        return jcommon.eInsert(sn, bean, table, bln);
    }

    private int checkLogics(BeanProgIntfSet bean)
    {
        int[] ifs = codeTools.convertStrToArr(bean.getSetInterfaces());
        interfaceDefine idef = new interfaceDefine(up);
        for (int i = 0; i < ifs.length; i++)
        {
            int fid = ifs[i];
            BeanProgIntfDefine bpid = (BeanProgIntfDefine) idef.getRecordByID(fid);
            if (idef.checkBean(bpid) == false)
            {
                return iReturn.INTF_DEFINE_NOTEXIST;
            }
        }
        return 0;
    }

    //检查并准备BEAN的必要标签数据
    private int prepareBean(BeanProgIntfSet bean)
    {
        if (bean.getOID() == 0)
        {
            int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
            bean.setOID(oid);
        }
        else
        {
            if (getRecordByID(bean.getOID()) != null)
            {
                return iReturn.BEAN_OID_REPEAT;
            }
        }
        if (bean.getSetTag().trim().equals(""))
        {
            String tag = tagCreator.createDataTag(up, table, "setTag", 2, 4);
            bean.setSetTag(tag);
        }
        else
        {
            if (getProgIntfSetByTag(bean.getSetTag()) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        return 0;
    }

    public BeanProgIntfSet getProgIntfSetByTag(String tag)
    {
        String sql = "select * from " + table + " where setTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanProgIntfSet.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanProgIntfSet) ls.get(0);
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = checkParamObject(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanProgIntfSet bean = (BeanProgIntfSet) o;
        BeanProgIntfSet obean = (BeanProgIntfSet) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //检查标签
        String stg = bean.getSetTag().trim();
        if (stg.equals(""))
        {
            return iReturn.BEAN_TAG_EMPTY;
        }
        if (stg.equals(obean.getSetTag()) == false)
        {
            //发现新的STG和原先的STG不同，则检查
            if (getProgIntfSetByTag(stg) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        //数据迁移
        int rcl = checkLogics(bean);
        if (rcl != 0)
        {
            return rcl;
        }
        obean.setSetDesp(bean.getSetDesp());
        obean.setSetInterfaces(bean.getSetInterfaces());
        obean.setSetName(bean.getSetName());
        obean.setSetTag(bean.getSetTag());
        obean.setStpID(bean.getStpID());
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    @Override
    public int deleteRecord(int i)
    {
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_DELETE
                + " where OID = " + i;
        System.err.println("sql = " + sql);
        return jcommon.eExcute(up, sql);
    }

    @Override
    public List getAllRecord()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param stpid
     * @param showAll
     * @return
     */
    public List getIntfSetsByType(int stpid, boolean showAll)
    {
        String sql = "select * from " + table + " where stpID = " + stpid;
        if (showAll == false)
        {
            sql = sql + " and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, BeanProgIntfSet.class);
    }

    @Override
    public Object getRecordByID(int i)
    {
        String sql = "select * from " + table + " where OID = " + i;
        List ls = sn.querySQL(sql, BeanProgIntfSet.class);
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
        if (o.getClass() != BeanProgIntfSet.class)
        {
            return false;
        }
        BeanProgIntfSet bean = (BeanProgIntfSet) o;
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
        BeanProgIntfSet bean = (BeanProgIntfSet) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (bean.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_NOT_READY_DESTROY;
        }
        String sql = "delete from " + table
                + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    public List loadInterfaceDefineBySet(int stid)
    {
        BeanProgIntfSet bean = (BeanProgIntfSet) getRecordByID(stid);
        if (bean == null)
        {
            return null;
        }
        return loadInterfaceDefineByString(bean.getSetInterfaces());
    }

    public List loadInterfaceDefineByString(String str)
    {
        int[] ids = codeTools.convertStrToArr(str);
        if (ids.length == 0)
        {
            return null;
        }
        String sql = "select * from tb_prog_interface_define where OID in (";
        for (int i = 0; i < ids.length; i++)
        {
            int id = ids[i];
            sql = sql + " " +id +" ";
            if (i < ids.length - 1)
            {
                sql = sql + ",";
            }
        }
        sql = sql +")";
        return sn.querySQL(sql, BeanProgIntfDefine.class);
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
