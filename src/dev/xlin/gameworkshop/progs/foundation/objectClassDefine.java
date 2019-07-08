package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.beanObjectClass;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import dev.xlin.tools.codeTools;
import dev.xlin.tools.constChk;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class objectClassDefine implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_object_class_define";

    public objectClassDefine(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    public List getObjectClassByType(int typeID, boolean showAll)
    {
        String sql = "select * from " + table + " where stpID = " + typeID;
        if (showAll == false)
        {
            sql = sql + " and state = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, beanObjectClass.class);
    }

    public beanObjectClass getObjectClassByOID(int oid)
    {
        return (beanObjectClass) getRecordByID(oid);
    }

    public beanObjectClass getoObjectClassByTag(String stg)
    {
        String sql = "select * from " + table + " where classTag = '" + stg.trim() + "'";
        List ls = sn.querySQL(sql, beanObjectClass.class);
        if (ls == null)
        {
            return null;
        }
        return (beanObjectClass) ls.get(0);
    }

    @Override
    public int createRecord(Object o, boolean bln)
    {
        int r0 = doCheckObject(o);
        if (r0 != 0)
        {
            return r0;
        }
        beanObjectClass bean = (beanObjectClass) o;
        if (getoObjectClassByTag(bean.getClassTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        int rlg = doCheckLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
        bean.setOID(oid);
        bean.setState(iDAO.OBJECT_STATE_ACTIVE);
        return jcommon.eInsert(sn, bean, table, bln);
    }

    private int doCheckLogic(beanObjectClass bean)
    {
        int[] ids = codeTools.convertStrToArr(bean.getClassFuncs());
        for (int i = 0; i < ids.length; i++)
        {
            int id = ids[i];
            if (constChk.isConst(iConst.class, "OCLS_FUNC_", id) == false && constChk.isConst(iConst.class, "SYS_DB_", id) == false )
            {
                return iReturn.OCLS_FUNCS_ERROR;
            }
        }
        return 0;
    }

    private int doCheckObject(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != beanObjectClass.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = doCheckObject(o);
        if (r0 != 0)
        {
            return r0;
        }
        beanObjectClass bean = (beanObjectClass) o;
        beanObjectClass obean = getObjectClassByOID(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        int rlg = doCheckLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        //数据迁移
        obean.setClassDesp(bean.getClassDesp());
        obean.setClassName(bean.getClassName());
        obean.setClassFuncs(bean.getClassFuncs());
        //新配置数据迁移
        obean.setStack(bean.getStack());
        obean.setStackLimit(bean.getStackLimit());
        obean.setEquipment(bean.getEquipment());
        obean.setSlotType(bean.getSlotType());
        obean.setSlotIndex(bean.getSlotIndex());
        obean.setEquipRoot(bean.getEquipRoot());
        obean.setContainerItem(bean.getContainerItem());
        obean.setSlotRoot(bean.getSlotRoot());
        obean.setAbstractItem(bean.getAbstractItem());
        obean.setStrictConfig(bean.getStrictConfig());
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    @Override
    public int deleteRecord(int i)
    {
        String sql = "update " + table + " set state = " + iDAO.OBJECT_STATE_DELETE + " where OID = " + i;
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
        List ls = sn.querySQL(sql, beanObjectClass.class);
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
        if (o.getClass() != beanObjectClass.class)
        {
            return false;
        }
        beanObjectClass bean = (beanObjectClass) o;
        if (bean.getState() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int revertBean(int i)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int destroyBean(int i)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
