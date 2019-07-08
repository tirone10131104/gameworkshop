package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxConstellation;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxStellarRegion;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class ConstellationData implements iDAO, iBeanCheckable
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_constellation";

    public ConstellationData(wakeup _up)
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
        BeanCtxConstellation bean = (BeanCtxConstellation) o;
        int ipr = prepareBean(bean);
        if (ipr != 0)
        {
            return ipr;
        }
        return jcommon.eInsert(sn, bean, table, bln);
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanCtxConstellation.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    private int prepareBean(BeanCtxConstellation bean)
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
        if (bean.getConTag().trim().equals("") == false)
        {
            if (getConstellationByTag(bean.getConTag()) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        else
        {
            bean.setConTag(tagCreator.createDataTag(up, table, "conTag", 2, 4));
        }
        return 0;
    }

    public BeanCtxConstellation getConstellationByTag(String stg)
    {
        String sql = "select * from " + table + " where conTag = '" + stg.trim() + "'";
        List ls = sn.querySQL(sql, BeanCtxConstellation.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanCtxConstellation) ls.get(0);
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanCtxConstellation bean = (BeanCtxConstellation) o;
        BeanCtxConstellation obean = (BeanCtxConstellation) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setConDesp(bean.getConDesp());
        obean.setConName(bean.getConName());
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    /**
     *
     * @param cid
     * @param rid
     * @return
     */
    public int moveToRegion(int cid, int rid)
    {
        //获取CID
        BeanCtxConstellation bean = (BeanCtxConstellation) getRecordByID(cid);
        if (checkBean(bean) == false)
        {
            return ctxReturn.WLD_CONSTELATION_NOTEXIST;
        }
        //获取RID
        StellarRegion srg = new StellarRegion(up);
        BeanCtxStellarRegion bcsr = (BeanCtxStellarRegion) srg.getRecordByID(rid);
        if (srg.checkBean(bcsr) == false)
        {
            return ctxReturn.WLD_STELLAR_NOTEXIST;
        }
        //检查
        if (bean.getRegionOID() == bcsr.getOID())
        {
            return ctxReturn.WLD_CONS_CANT_MOVE;
        }
        if (bean.getGlxOID() != bcsr.getGlaxyOID())
        {
            return ctxReturn.WLD_NOT_SAME_GLAXY;
        }
        //SQL生成
        String sql = "update " + table + " set regionOID = " + rid + " where OID = " + cid;
        //执行操作
        return jcommon.eExcute(up, sql);
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
        List ls = sn.querySQL(sql, BeanCtxConstellation.class);
        if (ls == null)
        {
            return null;
        }
        return ls.get(0);
    }

    public List queryConstellationsByRegion(int oid)
    {
        String sql = "select * from " + table + " where regionOID = " + oid;
        return sn.querySQL(sql, BeanCtxConstellation.class);
    }

    @Override
    public boolean checkBean(Object o)
    {
        if (o == null)
        {
            return false;
        }
        if (o.getClass() != BeanCtxConstellation.class)
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
