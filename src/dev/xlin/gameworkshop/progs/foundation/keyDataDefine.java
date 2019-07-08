package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanKeyDataDefine;
import dev.xlin.gameworkshop.progs.iConst;
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
public class keyDataDefine implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_key_data_define";

    public keyDataDefine(wakeup _up)
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
            return 0;
        }
        BeanKeyDataDefine bean = (BeanKeyDataDefine) o;
        //检查逻辑关系
        int rlg = doCheckLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        if (getKeyDefineByTag(bean.getKeyTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        //数据准备
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
        bean.setOID(oid);
        return jcommon.eInsert(sn, bean, table, false);
    }

    public int moveToType(int kid, int tpid)
    {
        String sql = "update " + table + " set typeOID  = " + tpid + " where OID = " + kid;
        return jcommon.eExcute(up, sql);
    }

    public BeanKeyDataDefine getKeyDefineByTag(String tag)
    {
        String sql = "select * from " + table + " where keyTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanKeyDataDefine.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanKeyDataDefine) ls.get(0);
    }

    private int doCheckLogic(BeanKeyDataDefine bean)
    {
        //检查数据类型
        if (constChk.isConst(iConst.class, "KDT_DTP_", bean.getDataType()) == false)
        {
            return iReturn.KDT_DATA_TYPE_ERROR;
        }
        return 0;
    }

    private int doCheckParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanKeyDataDefine.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    public List getKeyListByType(int tpid, boolean showAll)
    {
        String sql = "select * from " + table + " where typeOID = " + tpid;
        if (showAll == false)
        {
            sql = sql + " and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, BeanKeyDataDefine.class);
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = doCheckParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanKeyDataDefine bean = (BeanKeyDataDefine) o;
        BeanKeyDataDefine obean = (BeanKeyDataDefine) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }

        int rlg = doCheckLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        obean.setCacheLoad(bean.getCacheLoad());
        obean.setDataType(bean.getDataType());
        obean.setKeyDesp(bean.getKeyDesp());
        obean.setKeyName(bean.getKeyName());
        return jcommon.eUpdate(sn, obean, table, "OID", false);
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
        List ls = sn.querySQL(sql, BeanKeyDataDefine.class);
        if (ls == null)
        {
            return ls;
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
        if (o.getClass() != BeanKeyDataDefine.class)
        {
            return false;
        }
        BeanKeyDataDefine bean = (BeanKeyDataDefine) o;
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
        BeanKeyDataDefine bean = (BeanKeyDataDefine) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OPERATE_SUCCESS;
        }
        if (bean.getStatus() == iDAO.OBJECT_STATE_ACTIVE)
        {
            return iReturn.BEAN_NOT_READY_DESTROY;
        }
        String sql = "delete from " + table + " where OID = " + bean.getOID();
        return jcommon.eExcute(up, sql);
    }

    @Override
    public List getDeleted()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List queryKeyDatas (int tpid , String text )
    {
        if (tpid == 0 && text.trim().equals(""))
        {
            return null;
        }
        String sql = "select * from " + table +" where 1>0 ";
        if (tpid != 0)
        {
            sql = sql +" and typeOID = "+ tpid;
        }
        if (text.trim().equals("") == false)
        {
            sql = sql  +" and keyName like '%"+text+"%'";
        }
        return sn.querySQL(sql, BeanKeyDataDefine.class);
    }
    
}

//
//LOG
//TIME:
//REC:
//
