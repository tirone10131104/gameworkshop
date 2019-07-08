package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.beanCtxBaseResource;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldCfgResItem;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author 刘祎鹏
 */
public class worldTypeConfigResource implements iDAO, iBeanCheckable
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_world_cfg_res_item";

    public worldTypeConfigResource(wakeup _up)
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
        //检查RES
        beanCtxWorldCfgResItem bean = (beanCtxWorldCfgResItem) o;
        baseResourceDefine brdef = new baseResourceDefine(up);
        beanCtxBaseResource bcbr = (beanCtxBaseResource) brdef.getRecordByID(bean.getResOID());
        if (brdef.checkBean(bcbr) == false)
        {
            return ctxReturn.WLD_RES_NOT_EXIST;
        }
        int rlg = checkLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        bean.setCriTag(tagCreator.createDataTag(up, table, "criTag", 4, 4));
        bean.setOID(OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH));
        return jcommon.eInsert(sn, bean, table, bln);
    }

    private int checkLogic(beanCtxWorldCfgResItem bean)
    {
        if (bean.getCapMax() <= 0)
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getCapMin() < 0)
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getCapMax() < bean.getCapMin())
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getDiffcMax() < 0 || bean.getDiffcMax() > 1 || bean.getDiffcMax() < bean.getDiffcMin())
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getDiffcMin() < 0 || bean.getDiffcMin() > 1 || bean.getDiffcMax() < bean.getDiffcMin())
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getEffiMax() < 0 || bean.getEffiMax() > 1 || bean.getEffiMax() < bean.getEffiMin())
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getEffiMin() < 0 || bean.getEffiMin() > 1 || bean.getEffiMax() < bean.getEffiMin())
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getCountMax() < 0 || bean.getCountMax() < bean.getCountMin())
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getCountMin() < 0 || bean.getCountMax() < bean.getCountMin())
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getDefaultVisb() < 0 || bean.getDefaultVisb() > 1)
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getSurveyMin() < 0 || bean.getSurveyMin() > 1)
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getSurveyMax() < 0 || bean.getSurveyMax() > 1)
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getSurveyMin() > bean.getSurveyMax())
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getDangerMin() < 0 || bean.getDangerMax() > 1)
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        if (bean.getDangerMin() > bean.getDangerMax())
        {
            return iReturn.BEAN_DATA_RANGE_ERROR;
        }
        return 0;
    }

    public List getResItems(int cfgid, int resid)
    {
        String sql = "select * from " + table + " where cfgOID = " + cfgid;
        if (resid != 0)
        {
            sql = sql + " and resOID = " + resid;
        }
        return sn.querySQL(sql, beanCtxWorldCfgResItem.class);
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != beanCtxWorldCfgResItem.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    public List getResourceListByConfig(int cid)
    {
        String sql = "select * from " + table + " where cfgOID = " + cid;
        return sn.querySQL(sql, beanCtxWorldCfgResItem.class);
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        beanCtxWorldCfgResItem bean = (beanCtxWorldCfgResItem) o;
        beanCtxWorldCfgResItem obean = (beanCtxWorldCfgResItem) getRecordByID(bean.getOID());
        if (obean == null)
        {
            return ctxReturn.WLDTPS_RES_CONFIG_NOTEXIST;
        }
        int rlg = checkLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        //数据迁移
        obean.setProbability(bean.getProbability());
        obean.setCapMax(bean.getCapMax());
        obean.setCapMin(bean.getCapMin());
        obean.setDiffcMax(bean.getDiffcMax());
        obean.setDiffcMin(bean.getDiffcMin());
        obean.setEffiMax(bean.getEffiMax());
        obean.setEffiMin(bean.getEffiMin());
        obean.setCountMax(bean.getCountMax());
        obean.setCountMin(bean.getCountMin());
        obean.setCfgDescp(bean.getCfgDescp());
        obean.setSurveyMax(bean.getSurveyMax());
        obean.setSurveyMin(bean.getSurveyMin());
        obean.setDefaultVisb(bean.getDefaultVisb());
        obean.setDangerMax(bean.getDangerMax());
        obean.setDangerMin(bean.getDangerMin());
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
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
        List ls = sn.querySQL(sql, beanCtxWorldCfgResItem.class);
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
        if (o.getClass() != beanCtxWorldCfgResItem.class)
        {
            return false;
        }
        return true;
    }

    public static beanCtxWorldCfgResItem generateResItemData()
    {
        beanCtxWorldCfgResItem bean = new beanCtxWorldCfgResItem();
        Random r = new Random();
        long lmax = randomCap(r);
        long lmin = 0;
        while (true)
        {
            lmin = randomCap(r);
            if (lmin < lmax)
            {
                break;
            }
        }
        bean.setCapMax(lmax);
        bean.setCapMin(lmin);
        //PROB
        bean.setProbability(r.nextDouble());
        //diff
        double[] dsDiff = makeDoubles(r);
        bean.setDiffcMin(dsDiff[0]);
        bean.setDiffcMax(dsDiff[1]);
        //eff
        double[] dsEff = makeDoubles(r);
        bean.setEffiMin(dsEff[0]);
        bean.setEffiMax(dsEff[1]);
        //surv
        double[] dsSurv = makeDoubles(r);
        bean.setSurveyMin(dsSurv[0]);
        bean.setSurveyMax(dsSurv[1]);
        //dang
        double[] dsDang = makeDoubles(r);
        bean.setDangerMin(dsDang[0]);
        bean.setDangerMax(dsDang[1]);
        //visb
        bean.setDefaultVisb(r.nextDouble());
        //count
        int[] isCnt = makeIntegers(r, 100);
        bean.setCountMin(isCnt[0]);
        bean.setCountMax(isCnt[1]);
        return bean;
    }

    private static int[] makeIntegers(Random r, int range)
    {
        int imin = randomInt(r, range);
        int imax = 0;
        while (true)
        {
            imax = randomInt(r, range);
            if (imax >= imin)
            {
                break;
            }
        }
        int[] is = new int[2];
        is[0] = imin;
        is[1] = imax;
        return is;
    }

    private static double[] makeDoubles(Random r)
    {
        double[] ds = new double[2];
        double dmin = r.nextDouble();
        double dmax = 0;
        while (true)
        {
            dmax = r.nextDouble();
            if (dmax >= dmin)
            {
                break;
            }
        }
        ds[0] = dmin;
        ds[1] = dmax;
        return ds;
    }

    private static int randomInt(Random r, int range)
    {
        return r.nextInt(range);
    }

    private static long randomCap(Random r)
    {
        while (true)
        {
            long l = r.nextInt();
            if (l > 0)
            {
                return l;
            }
        }
    }
}
