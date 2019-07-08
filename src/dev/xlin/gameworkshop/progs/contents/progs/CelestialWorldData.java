package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxCelestialWorld;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxStellarData;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldTypeMain;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldTypeSet;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import dev.xlin.tools.constChk;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class CelestialWorldData implements iDAO, iBeanCheckable
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_celestial_world_data";

    public CelestialWorldData(wakeup _up)
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
        BeanCtxCelestialWorld bean = (BeanCtxCelestialWorld) o;
        int rlg = checkLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        //逻辑性数据准备
        if (bean.getWorldType() == ctxConst.WORLD_TYPE_PLANET)
        {
            bean.setParentOID(0);
        }
        bean.setOrbitIdx(makeOrbitIdx(bean.getStellarOID(), bean.getParentOID(), bean.getWorldType()));
        bean.setSystemSerialIdx(makeSerialIdx(bean.getStellarOID()));
        int rpp = prepareBean(bean);
        if (rpp != 0)
        {
            return rpp;
        }
        return jcommon.eInsert(sn, bean, table, bln);
    }

    private int checkLogic(BeanCtxCelestialWorld bean)
    {
        //检查WORLD TYPE 常量
        if (constChk.isConst(ctxConst.class, "WORLD_TYPE_", bean.getWorldType()) == false)
        {
            return ctxReturn.WLD_TYPE_ERROR;
        }
        //检查STELLAR 
        StellarData sdt = new StellarData(up);
        BeanCtxStellarData bcsd = (BeanCtxStellarData) sdt.getRecordByID(bean.getStellarOID());
        if (sdt.checkBean(bcsd) == false)
        {
            return ctxReturn.WLD_STELLAR_NOTEXIST;
        }
        //检查MAIN 
        WorldTypeMain wtm = new WorldTypeMain(up);
        BeanCtxWorldTypeMain bwmain = (BeanCtxWorldTypeMain) wtm.getRecordByID(bean.getWorldTypeMainOID());
        if (wtm.checkBean(bwmain) == false)
        {
            return ctxReturn.WLD_WORLD_MAIN_ERROR;
        }
        if (bwmain.getWorldTypeCst() != bean.getWorldType())
        {
            return ctxReturn.WLD_WORLD_MAIN_ERROR;
        }
        //检查SET 
        WorldTypeSet wts = new WorldTypeSet(up);
        BeanCtxWorldTypeSet bmset = (BeanCtxWorldTypeSet) wts.getRecordByID(bean.getWorldTypeSetOID());
        if (wts.checkBean(bmset) == false)
        {
            return ctxReturn.WLD_WORLD_SET_ERROR;
        }
        if (bmset.getWorldMainOID() != bean.getWorldTypeMainOID())
        {
            return ctxReturn.WLD_WORLD_SET_ERROR;
        }
        if (bean.getWorldType() == ctxConst.WORLD_TYPE_MOON || (bean.getWorldType() == ctxConst.WORLD_TYPE_ASTEROID && bean.getParentOID() != 0))
        {
            //如果MOON ,检查PARENT 
            BeanCtxCelestialWorld bpar = (BeanCtxCelestialWorld) getRecordByID(bean.getParentOID());
            if (checkBean(bpar) == false)
            {
                return ctxReturn.WLD_WORLD_PARENT_ERROR;
            }
            if (bpar.getWorldType() != ctxConst.WORLD_TYPE_PLANET)
            {
                return ctxReturn.WLD_WORLD_PARENT_ERROR;
            }
        }
        return 0;
    }

    public List getChildWroldConsts(int pcst)
    {
        System.err.println("PCST =  " + pcst);
        ArrayList arl = new ArrayList();
        if (pcst == 0)
        {
            arl.add(ctxConst.WORLD_TYPE_PLANET);
            arl.add(ctxConst.WORLD_TYPE_ASTEROID);
        }
        else if (pcst == ctxConst.WORLD_TYPE_PLANET)
        {
            arl.add(ctxConst.WORLD_TYPE_MOON);
            arl.add(ctxConst.WORLD_TYPE_ASTEROID);
        }
        else if (pcst == ctxConst.WORLD_TYPE_MOON)
        {
            arl.add(ctxConst.WORLD_TYPE_ASTEROID);
        }
        else if (pcst == ctxConst.WORLD_TYPE_ASTEROID)
        {
            //无类型数据
        }
        return arl;
    }

    private int makeOrbitIdx(int stid, int parent, int wtp)
    {
        String sql = "select max(orbitIdx) as smax from " + table + " where stellarOID = " + stid + " and parentOID = " + parent + " and worldType = " + wtp;
        try
        {
            Hashtable[] hts = up.querySQL(sql);
            if (hts == null)
            {
                return 1;
            }
            Hashtable ht = hts[0];
            int imax = (int) ht.get("smax");
            return imax + 1;
        }
        catch (Exception excp)
        {
            return -1;
        }
    }

    private int makeSerialIdx(int stelid)
    {
        String sql = "select max(systemSerialIdx) as smax from " + table + " where stellarOID = " + stelid;
        try
        {
            Hashtable[] hts = up.querySQL(sql);
            if (hts == null)
            {
                return 1;
            }
            Hashtable ht = hts[0];
            int imax = (int) ht.get("smax");
            return imax + 1;
        }
        catch (Exception excp)
        {
            return -1;
        }
    }

    private int prepareBean(BeanCtxCelestialWorld bean)
    {
        if (bean.getOID() == 0)
        {
            bean.setOID(OIDCreator.createOID(up, table, "OID", iDAO.OID_LONG_MIN, iDAO.OID_LONG_LENTH));
        }
        else
        {
            if (getRecordByID(bean.getOID()) != null)
            {
                return iReturn.BEAN_OID_REPEAT;
            }
        }
        if (bean.getWorldCode().trim().equals(""))
        {
            //建CODE
            bean.setWorldCode(tagCreator.createDataTag(up, table, "worldCode", "C", "", 4, 4));
        }
        else
        {
            if (getCelestialWorldByCode(bean.getWorldCode()) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        return 0;
    }

    public BeanCtxCelestialWorld getCelestialWorldByCode(String scd)
    {
        String sql = "select * from " + table + " where worldCode = '" + scd.trim() + "'";
        List ls = sn.querySQL(sql, BeanCtxCelestialWorld.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanCtxCelestialWorld) ls.get(0);
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanCtxCelestialWorld.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    public List getCelestialsByParent(int stid, int parid, int wtp)
    {
        String sql = "select * from " + table + " where stellarOID = " + stid
                + " and parentOID = " + parid;
        if (wtp != 0)
        {
            sql = sql + " and worldType = " + wtp;
        }
        sql = sql + " order by orbitIdx ";
        return sn.querySQL(sql, BeanCtxCelestialWorld.class);
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanCtxCelestialWorld bean = (BeanCtxCelestialWorld) o;
        BeanCtxCelestialWorld obean = (BeanCtxCelestialWorld) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //检查逻辑
        int rlg = checkLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        //数据迁移
        obean.setAngel(bean.getAngel());
        obean.setWorldTypeMainOID(bean.getWorldTypeMainOID());
        obean.setWorldTypeSetOID(bean.getWorldTypeSetOID());
        obean.setWorldTypeConfigOID(bean.getWorldTypeConfigOID());
        obean.setWorldName(bean.getWorldName());
        obean.setDescp(bean.getDescp());
        bean.setWorldCode(bean.getWorldCode().trim());
        if (bean.getWorldCode().equals(""))
        {
            return iReturn.BEAN_TAG_EMPTY;
        }
        //检查一下标签是否被修改
        if (obean.getWorldCode().equals(bean.getWorldCode().trim()) == false)
        {
            if (getCelestialWorldByCode(bean.getWorldCode()) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        obean.setWorldCode(bean.getWorldCode());
        //数据库操作
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    @Override
    public int deleteRecord(int i)
    {
        BeanCtxCelestialWorld bean = (BeanCtxCelestialWorld) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (getCelestialsByParent(bean.getStellarOID(), bean.getOID(), 0) != null)
        {
            return iReturn.BEAN_FOREIGN_KEY_ERROR;
        }
        try
        {
            String sql = "delete from " + table + " where OID = " + i;
            return jcommon.eExcute(up, sql);
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return iDAO.OPERATE_FAIL;
        }
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
        List ls = sn.querySQL(sql, BeanCtxCelestialWorld.class);
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
        if (o.getClass() != BeanCtxCelestialWorld.class)
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
