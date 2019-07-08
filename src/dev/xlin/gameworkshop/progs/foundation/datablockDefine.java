package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablockDefine;
import dev.xlin.gameworkshop.progs.iConst;
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
public class datablockDefine implements iDAO, iBeanCheckable
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_datablock_define";

    public datablockDefine(wakeup _up)
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
        BeanDatablockDefine bean = (BeanDatablockDefine) o;
        //检查标签
        if (getDataDefineByTag(bean.getDbTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        //数据准备
        int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
        bean.setOID(oid);
        return jcommon.eInsert(sn, bean, table, false);
    }

    public BeanDatablockDefine getDataDefineByXmlNodeTag(String tag)
    {
        String sql = "select * from " + table + " where xmlNodeTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanDatablockDefine.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanDatablockDefine) ls.get(0);
    }

    public BeanDatablockDefine getDataDefineByTag(String stg)
    {
        String sql = "select * from " + table + " where dbTag = '" + stg.trim() + "'";
        List ls = sn.querySQL(sql, BeanDatablockDefine.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanDatablockDefine) ls.get(0);
    }

    private int doCheckParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanDatablockDefine.class)
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
        BeanDatablockDefine bean = (BeanDatablockDefine) o;
        BeanDatablockDefine obean = (BeanDatablockDefine) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据更新
        obean.setDbAdtClass(bean.getDbAdtClass());
        obean.setDbDesp(bean.getDbDesp());
        obean.setDbName(bean.getDbName());
        obean.setDbPalClass(bean.getDbPalClass());
        obean.setDataBeanClass(bean.getDataBeanClass());
        obean.setXmlNodeTag(bean.getXmlNodeTag());
        obean.setInstanceConfig(bean.getInstanceConfig());
        return jcommon.eUpdate(sn, obean, table, "OID", false);
    }

    @Override
    public int deleteRecord(int i)
    {
        BeanDatablockDefine bean = (BeanDatablockDefine) getRecordByID(i);
        {
            if (bean == null)
            {
                return iDAO.OBJECT_RECORD_NOTEXIST;
            }
            if (bean.getSystemDb() == iConst.BOL_TRUE)
            {
                return iReturn.BEAN_CANT_DELETE;
            }
        }
        String sql = "delete from " + table + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public List getAllRecord()
    {
        String sql = "select * from " + table +" order by OID ";
        return sn.querySQL(sql, BeanDatablockDefine.class);
    }

    @Override
    public Object getRecordByID(int i)
    {
        String sql = "select * from " + table + " where OID = " + i;
        List ls = sn.querySQL(sql, BeanDatablockDefine.class);
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
        if (o.getClass() != BeanDatablockDefine.class)
        {
            return false;
        }
        return true;
    }

}

//
//LOG
//TIME:
//REC:
//
