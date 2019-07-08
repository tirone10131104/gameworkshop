package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxGlaxyInfo;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
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
public class GlaxyInfo implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_glaxy_info";

    public GlaxyInfo(wakeup _up)
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
        BeanCtxGlaxyInfo bean = (BeanCtxGlaxyInfo) o;
        int rpp = prepareBean(bean);
        if (rpp != 0)
        {
            return rpp;
        }
        return jcommon.eInsert(sn, bean, table, false);
    }

    //准备数据
    private int prepareBean(BeanCtxGlaxyInfo bean)
    {
        if (bean.getOID() != 0)
        {
            if (getRecordByID(bean.getOID()) != null)
            {
                return iReturn.BEAN_OID_REPEAT;
            }
        }
        else
        {
            bean.setOID(OIDCreator.createOID(up, table, "OID", OID_MIN, OID_LENGTH));
        }
        if (bean.getGlxTag().trim().equals(""))
        {
            bean.setGlxTag(tagCreator.createDataTag(up, table, "glxTag", "GLX", "", 2, 4));
        }
        else
        {
            if (getCtxGlaxyInfoByTag(bean.getGlxTag()) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        return 0;
    }

    public BeanCtxGlaxyInfo getCtxGlaxyInfoByTag(String tag)
    {
        String sql = "select * from " + table + " where glxTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanCtxGlaxyInfo.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanCtxGlaxyInfo) ls.get(0);
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanCtxGlaxyInfo.class)
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
        BeanCtxGlaxyInfo bean = (BeanCtxGlaxyInfo) o;
        BeanCtxGlaxyInfo obean = (BeanCtxGlaxyInfo) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //迁移
        obean.setGlxConfigs(bean.getGlxConfigs());
        obean.setGlxDesp(bean.getGlxDesp());
        obean.setGlxName(bean.getGlxName());
        return jcommon.eUpdate(sn, obean, table, "OID", false);
    }

    @Override
    public int deleteRecord(int i)
    {
        BeanCtxGlaxyInfo bean = (BeanCtxGlaxyInfo) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //检查其下STELLA
        StellarData std = new StellarData(up);
        if (std.getStellarCount(bean.getOID()) != 0)
        {
            return iReturn.BEAN_FOREIGN_KEY_ERROR;
        }
        //SQL
        String sup = "update " + table + " set status = " + iDAO.OBJECT_STATE_DELETE
                + " where OID = " + i;
        return jcommon.eExcute(up, sup);
    }

    @Override
    public List getAllRecord()
    {
        return null;
    }

    @Override
    public Object getRecordByID(int i)
    {
        String sql = "select * from " + table + " where OID = " + i;
        List ls = sn.querySQL(sql, BeanCtxGlaxyInfo.class);
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
        if (o.getClass() != BeanCtxGlaxyInfo.class)
        {
            return false;
        }
        BeanCtxGlaxyInfo bean = (BeanCtxGlaxyInfo) o;
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int revertBean(int i)
    {
        BeanCtxGlaxyInfo bean = (BeanCtxGlaxyInfo) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_ACTIVE
                + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public int destroyBean(int i)
    {
        BeanCtxGlaxyInfo bean = (BeanCtxGlaxyInfo) getRecordByID(i);
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
