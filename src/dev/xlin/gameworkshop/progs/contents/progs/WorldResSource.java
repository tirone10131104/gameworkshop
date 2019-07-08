package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxBaseResource;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxCelestialWorld;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldCfgResItem;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldResSource;
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
import java.util.Random;

/**
 *
 * @author 刘祎鹏
 */
public class WorldResSource implements iDAO, iBeanCheckable
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_res_mine_source";

    public WorldResSource(wakeup _up)
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
        BeanCtxWorldResSource bean = (BeanCtxWorldResSource) o;
        //检测RES
        BaseResourceDefine brd = new BaseResourceDefine(up);
        BeanCtxBaseResource bcbr = (BeanCtxBaseResource) brd.getRecordByID(bean.getResOID());
        if (brd.checkBean(bcbr) == false)
        {
            return ctxReturn.WLD_RES_NOT_EXIST;
        }
        //检测WORLD
        CelestialWorldData cwd = new CelestialWorldData(up);
        BeanCtxCelestialWorld bccw = (BeanCtxCelestialWorld) cwd.getRecordByID(bean.getWorldOID());
        if (cwd.checkBean(bccw) == false)
        {
            return ctxReturn.WLD_WORLD_NOT_EXIST;
        }
        //检查这个配置已经存在的数据
        List ls = getResSourceByWorld(bean.getWorldOID(), bean.getResOID(), bean.getCfgID());
        if (ls != null)
        {
            WorldTypeConfigResource wtcr = new WorldTypeConfigResource(up);
            BeanCtxWorldCfgResItem bri = (BeanCtxWorldCfgResItem) wtcr.getRecordByID(bean.getCfgID());
            if (ls.size() - 1 >= bri.getCountMax())
            {
                return ctxReturn.WLD_MAX_RES_ITEM_REACHED;
            }
        }
        //准备数据
        int rpp = prepareBean(bean);
        if (rpp != 0)
        {
            return rpp;
        }
        //数据库操作
        return jcommon.eInsert(sn, bean, table, bln);
    }

    public BeanCtxWorldResSource generateResSource(int cfgid, int resid)
    {
        WorldTypeConfigResource wtcr = new WorldTypeConfigResource(up);
        List lris = wtcr.getResItems(cfgid, resid);
        if (lris == null)
        {
            return null;
        }
        int idx = 0;
        if (lris.size() > 1)
        {
            idx = makeIntCap(0, lris.size() - 1);
        }
        BeanCtxWorldCfgResItem bri = (BeanCtxWorldCfgResItem) lris.get(idx);
        return generateResSource(bri);
    }

    public BeanCtxWorldResSource generateResSource(BeanCtxWorldCfgResItem bri)
    {
        BeanCtxWorldResSource nbrs = new BeanCtxWorldResSource();
        int caps = makeIntCap((int) bri.getCapMin(), (int) bri.getCapMax());
        double eff = makeDoubleVal(bri.getEffiMin(), bri.getEffiMax());
        double diff = makeDoubleVal(bri.getDiffcMin(), bri.getDiffcMax());
        double surv = makeDoubleVal(bri.getSurveyMin(), bri.getSurveyMax());
        double effDec = makeDoubleVal(0, 1);
        double diffInc = makeDoubleVal(0, 1);
        System.err.println("bri.mmin = " + bri.getDangerMin() +" mmax "+ bri.getDangerMax());
        double danger = makeDoubleVal(bri.getDangerMin(), bri.getDangerMax());
        System.err.println("danger = " + danger);
        double dangerIncrs = makeDoubleVal(0, 1);
        System.err.println("dangerIncrs = " + dangerIncrs);
        nbrs.setCapability(caps);
        nbrs.setEfficiency(eff);
        nbrs.setDifficulty(diff);
        nbrs.setEffDecln(effDec);
        nbrs.setDiffIncrs(diffInc);
        nbrs.setSurveyLevel(surv);
        nbrs.setDanger(danger);
        nbrs.setDangerIncrs(dangerIncrs);
        double dvisb = makeDoubleVal(0, 1.0);
        if (dvisb > bri.getDefaultVisb())
        {
            nbrs.setDefaultVisb(0);
        }
        else
        {
            nbrs.setDefaultVisb(1);
        }
        return nbrs;
    }

    private int makeIntCap(int min, int max)
    {
        Random r = new Random();
        while (true)
        {
            int i = r.nextInt(max);
            if (i >= min && i <= max)
            {
                return i;
            }
        }
    }

    private double makeDoubleVal(double min, double max)
    {
        Random r = new Random();
        while (true)
        {
            if (min == max)
            {
                return min;
            }
            double d = r.nextDouble();
            if (d >= min && d <= max)
            {
                return d;
            }
        }
    }

    private int prepareBean(BeanCtxWorldResSource bean)
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
        if (bean.getTagCode().trim().equals(""))
        {
            bean.setTagCode(tagCreator.createDataTag(up, table, "tagCode", "WRS", "", 5, 5));
        }
        else
        {
            if (getResSourceByCode(bean.getTagCode()) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        return 0;
    }

    public BeanCtxWorldResSource getResSourceByCode(String tag)
    {
        String sql = "select * from " + table + " where tagCode = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanCtxWorldResSource.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanCtxWorldResSource) ls.get(0);
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanCtxWorldResSource.class)
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
        BeanCtxWorldResSource bean = (BeanCtxWorldResSource) o;
        BeanCtxWorldResSource obean = (BeanCtxWorldResSource) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setCapability(bean.getCapability());
        obean.setDifficulty(bean.getDifficulty());
        obean.setEfficiency(bean.getEfficiency());
        obean.setX(bean.getX());
        obean.setY(bean.getY());
        obean.setDiffIncrs(bean.getDiffIncrs());
        obean.setEffDecln(bean.getEffDecln());
        obean.setSurveyLevel(bean.getSurveyLevel());
        obean.setDefaultVisb(bean.getDefaultVisb());
        obean.setDanger(bean.getDanger());
        obean.setDangerIncrs(bean.getDangerIncrs());
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

    public List getResSourceByWorld(int wldid, int resid, int cfgid)
    {
        String sql = "select * from " + table + " where worldOID = " + wldid;
        if (resid != 0)
        {
            sql = sql + " and resOID = " + resid;
        }
        if (cfgid != 0)
        {
            sql = sql + " and cfgID = " + cfgid;
        }
        sql = sql + " order by capability";
        System.err.println("sql = " + sql);
        return sn.querySQL(sql, BeanCtxWorldResSource.class);
    }

    @Override
    public Object getRecordByID(int i)
    {
        String sql = "select * from " + table + " where OID = " + i;
        List ls = sn.querySQL(sql, BeanCtxWorldResSource.class);
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
        if (o.getClass() != BeanCtxWorldResSource.class)
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
