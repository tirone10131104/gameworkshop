package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.beanCtxStellarRegion;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iDAO;
import static dev.xlin.tols.interfaces.iDAO.OID_LENGTH;
import static dev.xlin.tols.interfaces.iDAO.OID_MIN;
import dev.xlin.tools.OIDCreator;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class stellarRegion implements iDAO, iBeanCheckable
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_stellar_region";

    public stellarRegion(wakeup _up)
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
        beanCtxStellarRegion bean = (beanCtxStellarRegion) o;
        prepareBean(bean);
        return jcommon.eInsert(sn, bean, table, bln);
    }

    //准备数据
    private int prepareBean(beanCtxStellarRegion bean)
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
        if (bean.getRegionTag().trim().equals(""))
        {
            bean.setRegionTag(tagCreator.createDataTag(up, table, "stTag", "STRG", "", 2, 5));
        }
        else
        {
            if (getRegionByTag(bean.getRegionTag()) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        return 0;
    }

    public beanCtxStellarRegion getRegionByTag(String tag)
    {
        String sql = "select * from " + table + " where regionTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, beanCtxStellarRegion.class);
        if (ls == null)
        {
            return null;
        }
        return null;
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != beanCtxStellarRegion.class)
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
        beanCtxStellarRegion bean = (beanCtxStellarRegion) o;
        beanCtxStellarRegion obean = (beanCtxStellarRegion) getRecordByID(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setRegionName(bean.getRegionName());
        obean.setRegionConfigs(bean.getRegionConfigs());
        obean.setRegionDesp(bean.getRegionDesp());
        return jcommon.eUpdate(sn, obean, table, "OID", false);
    }

    public List getRegionsByGlaxy(int uid)
    {
        String sql = "select * from " + table + " where glaxyOID = " + uid;
        return sn.querySQL(sql, beanCtxStellarRegion.class);
    }

    @Override
    public int deleteRecord(int i)
    {
        String sql = "delete from " + table + " where OID = " + i;
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
        System.err.println("sql = " + sql);
        List ls = sn.querySQL(sql, beanCtxStellarRegion.class);
        System.err.println("ls = " + ls);
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
        if (o.getClass() != beanCtxStellarRegion.class)
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
