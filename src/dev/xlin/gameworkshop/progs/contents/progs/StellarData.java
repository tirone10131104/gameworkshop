package dev.xlin.gameworkshop.progs.contents.progs;

import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxConstellation;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxStellarData;
import dev.xlin.gameworkshop.progs.contents.beans.poObjectPhysic;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.dbTask;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iDAO;
import static dev.xlin.tols.interfaces.iDAO.OID_LENGTH;
import static dev.xlin.tols.interfaces.iDAO.OID_MIN;
import dev.xlin.tools.OIDCreator;
import static java.lang.Math.PI;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class StellarData implements iDAO, iBeanCheckable
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tbc_stellar_data";

    public StellarData(wakeup _up)
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
        BeanCtxStellarData bean = (BeanCtxStellarData) o;
        int rpp = prepareBean(bean);
        if (rpp != 0)
        {
            return rpp;
        }
        return jcommon.eInsert(sn, bean, table, bln);
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanCtxStellarData.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    /**
     * *
     * 分配一个星座里的最大恒星序号
     *
     * @param consid
     * @return
     */
    public int allocateConstellationMaxIndex(int consid)
    {
        String sql = "select  conIndex  from " + table + " where conOID = " + consid + " order by conIndex desc ";
        try
        {
            System.err.println("sql = " + sql);
            Hashtable[] hts = up.querySQL(sql);
            if (hts != null)
            {
                Hashtable ht = hts[0];
                int ix = (int) ht.get("conIndex");
                return ix + 1;
            }
        }
        catch (Exception excp)
        {
            return -1;
        }
        return 0;
    }

    //准备数据
    private int prepareBean(BeanCtxStellarData bean)
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
        if (bean.getStTag().trim().equals(""))
        {
            bean.setStTag(tagCreator.createDataTag(up, table, "stTag", "STLA", "", 3, 5));
        }
        else
        {
            if (getStellarDataByTag(bean.getStTag()) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        return 0;
    }

    public List getStellarsByRegion(int rid, int cid)
    {
        String sql = "select * from " + table + " where regionOID = " + rid;
        if (cid != 0)
        {
            sql = sql + " and conOID = " + cid;
        }
        sql = sql + " order by conOID , conIndex "; 
        System.err.println(".sql = " + sql );
        return sn.querySQL(sql, BeanCtxStellarData.class);
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanCtxStellarData bean = (BeanCtxStellarData) o;
        BeanCtxStellarData obean = (BeanCtxStellarData) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //检查标签更改合理性
        if (bean.getStTag().trim().equals(obean.getStTag().trim()) == false)
        {
            //标签已经被修改，
            if (getStellarDataByTag(bean.getStTag().trim()) != null)
            {
                return iReturn.BEAN_TAG_REPEAT;
            }
        }
        //数据迁移 
        obean.setStName(bean.getStName());
        obean.setStTag(bean.getStTag().trim());
        obean.setX(bean.getX());
        obean.setY(bean.getY());
        obean.setZ(bean.getZ());
        obean.setAbsMang(bean.getAbsMang());
        obean.setCosmic1(bean.getCosmic1());
        obean.setCosmic2(bean.getCosmic2());
        obean.setDensity(bean.getDensity());
        obean.setGeneSeed(bean.getGeneSeed());
        obean.setGravity(bean.getGravity());
        obean.setLuminosity(bean.getLuminosity());
        obean.setMass(bean.getMass());
        obean.setSpectrumID(bean.getSpectrumID());
        obean.setSpectrumLevel(bean.getSpectrumLevel());
        obean.setTempreture(bean.getTempreture());
        obean.setStDesp(bean.getStDesp());
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    public BeanCtxStellarData getStellarDataByTag(String tag)
    {
        String sql = "select * from " + table + " where stTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanCtxStellarData.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanCtxStellarData) ls.get(0);
    }

    public List queryStellarDataInDLS(List dls, double lx, double ly, double width, double height)
    {
        if (dls == null)
        {
            return null;
        }
        ArrayList arl = new ArrayList();
        for (int i = 0; i < dls.size(); i++)
        {
            BeanCtxStellarData bcsd = (BeanCtxStellarData) dls.get(i);
            if (bcsd.getX() < lx || bcsd.getX() > (lx + width))
            {
                continue;
            }
            if (bcsd.getY() < ly || bcsd.getY() > (ly + height))
            {
                continue;
            }
            arl.add(bcsd);
        }
        return arl;
    }

    public List queryStellarData(int uniID, double lx, double ly, double width, double height)
    {
        String sql = "select * from " + table + " where universeOID = " + uniID
                + " and x > " + lx + " and x < " + (lx + width)
                + " and y > " + ly + " and y < " + (ly + height);
        return sn.querySQL(sql, BeanCtxStellarData.class);
    }

    public int getStellarCount(int uniID)
    {
        String sql = "select count(OID) as ict from " + table + " where universeOID = " + uniID;
        try
        {
            Hashtable[] hts = up.querySQL(sql);
            if (hts == null)
            {
                return 0;
            }
            Hashtable ht = hts[0];
            int ict = (int) ht.get("ict");
            return ict;
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteRecord(int i)
    {
        BeanCtxStellarData obean = (BeanCtxStellarData) getRecordByID(i);
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        try
        {
            dbTask.openManualTask(up);
            String sql = "delete from " + table + " where OID = " + i;
            int rdel = jcommon.eExcute(up, sql);
            if (rdel != iDAO.OPERATE_SUCCESS)
            {
                return dbTask.returnFail(up, rdel);
            }
            int r = rebuildConstellationIndexer(obean.getConOID(), true);
            if (r != iDAO.OPERATE_SUCCESS)
            {
                return dbTask.returnFail(up, r);
            }
            return dbTask.returnSuccess(up);
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return iDAO.OPERATE_FAIL;
        }
    }

    public int rebuildConstellationIndexer(int conid, boolean inSrv)
    {
        if (inSrv == false)
        {
            dbTask.openManualTask(up);
        }
        String sql = "select * from " + table + " where conOID = " + conid + " order by conIndex";
        List ls = sn.querySQL(sql, BeanCtxStellarData.class);
        if (ls == null)
        {
            return iDAO.OPERATE_SUCCESS;
        }
        for (int i = 0; i < ls.size(); i++)
        {
            BeanCtxStellarData bean = (BeanCtxStellarData) ls.get(i);
            String sup = "update " + table + " set conIndex = " + i + " where OID = " + bean.getOID();
            int rup = jcommon.eExcute(up, sup);
            if (rup != iDAO.OPERATE_SUCCESS)
            {
                if (inSrv == false)
                {
                    return dbTask.returnFail(up, rup);
                }
                else
                {
                    return rup;
                }
            }
        }
        if (inSrv == false)
        {
            return dbTask.returnSuccess(up);
        }
        else
        {
            return iDAO.OPERATE_SUCCESS;
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
        List ls = sn.querySQL(sql, BeanCtxStellarData.class);
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
        if (o.getClass() != BeanCtxStellarData.class)
        {
            return false;
        }
        return true;
    }


    public poObjectPhysic calculateStarPhysicData(double dmass, double dtemp)
    {
        poObjectPhysic pop = new poObjectPhysic();
        //目标恒星光度计算
        double dlum = Math.pow(dmass / 1.0, 3.5) * 1.0;
        pop.setLuminosity(dlum);
        //总辐射量= 太阳总辐射量* 光度
        double dtotF = Math.pow(10.0, 26) * 3.8 * dlum;
        //表面积计算
        double dts = Math.pow(10, -8) * 5.67 * Math.pow(dtemp, 4.0);
        double ds = dtotF / dts;
        //计算直径
        double dr = Math.sqrt(ds / (4.0 * PI)) / 1000.0;
        double dD = dr * 2.0;
        BigDecimal bD = new BigDecimal(dD);
        pop.setDiameter(dD);
        //计算体积
        //半径单位改为米
        double drm = dr * 1000.0;
        double dV = 4 / 3 * PI * Math.pow(drm, 3.0);
        //太阳标准质量(KG)
        double dSunMass = 1.989 * Math.pow(10.0, 30.0);
        //计算目标恒星标准质量（KG）
        double dstarMass = dmass * dSunMass;
        //计算目标恒星密度(KG/M3)
        double dP = dstarMass / dV;
        pop.setDensity(dP);
        BigDecimal bigP = new BigDecimal(dP);
        //计算表面重力加速度
        double dg = 6.67 * Math.pow(10.0, -11) * dstarMass / Math.pow(drm, 2.0);
        pop.setGravity(dg);
        //计算第一宇宙速度
        double v1 = Math.sqrt(dg * drm) / 1000.0;
        pop.setCosmic1(v1);
        double v2 = Math.sqrt(2.0) * v1;
        pop.setCosmic2(v2);
        //计算绝对星等 d = 10秒差距
        //--1计算亮度 E=L/（4πd^2）
        double de = dlum / (4.0 * PI * Math.pow(10.0, 2.0));
        //--2计算常量a a=m1+2.5lgE1
        //--（太阳m1绝对星等为4.83 ， 亮度为E1为
        //--计算太阳标准亮度E1。
        double dsunE1 = 1.0 / (4.0 * PI * Math.pow(10.0, 2.0));
        //--开始计算a
        double dca = 4.83 + 2.5 * Math.log10(dsunE1);
        //计算绝对星等 -> -2.5lgE+a
        double dm = -2.5 * Math.log10(de) + dca;
        pop.setAbsMang(dm);
        return pop;
    }

    /**
     * 在星座序列里，将恒星上移一位
     *
     * @param oid
     * @return
     */
    public int moveStellarUp(int oid)
    {
        //获取恒星
        BeanCtxStellarData bean = (BeanCtxStellarData) getRecordByID(oid);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //获取上一个
        String sql = "select * from " + table + " where conIndex < " + bean.getConIndex()
                + " and conOID = " + bean.getConOID() + " order by conIndex desc  limit 1 ";
        List ls = sn.querySQL(sql, BeanCtxStellarData.class);
        if (ls == null)
        {
            return iReturn.BEAN_CANT_MOVE_UP;
        }
        System.err.println("ls.siz = " + ls.size());
        BeanCtxStellarData bpre = (BeanCtxStellarData) ls.get(0);
        //准备两个SQL 
        String s1 = "update " + table + " set conIndex = " + bpre.getConIndex() + " where OID = " + bean.getOID();
        String s2 = "update " + table + " set conIndex = " + bean.getConIndex() + " where OID = " + bpre.getOID();
        dbTask.openManualTask(up);
        int r1 = jcommon.eExcute(up, s1);
        if (r1 != iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnFail(up, r1);
        }
        int r2 = jcommon.eExcute(up, s2);
        if (r2 != iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnFail(up, r2);
        }
        return dbTask.returnSuccess(up);
    }

    /**
     * *
     * 在星座序列里，将恒星下移一位
     *
     * @param oid
     * @return
     */
    public int moveStellarDown(int oid)
    {
        //获取恒星
        BeanCtxStellarData bean = (BeanCtxStellarData) getRecordByID(oid);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //获取上一个
        String sql = "select * from " + table + " where conIndex > " + bean.getConIndex()
                + " and conOID = " + bean.getConOID() + " order by conIndex limit 1 ";
        List ls = sn.querySQL(sql, BeanCtxStellarData.class);
        if (ls == null)
        {
            return iReturn.BEAN_CANT_MOVE_UP;
        }
        BeanCtxStellarData blast = (BeanCtxStellarData) ls.get(0);
        //准备两个SQL 
        String s1 = "update " + table + " set conIndex = " + blast.getConIndex() + " where OID = " + bean.getOID();
        String s2 = "update " + table + " set conIndex = " + bean.getConIndex() + " where OID = " + blast.getOID();
        dbTask.openManualTask(up);
        int r1 = jcommon.eExcute(up, s1);
        if (r1 != iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnFail(up, r1);
        }
        int r2 = jcommon.eExcute(up, s2);
        if (r2 != iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnFail(up, r2);
        }
        return dbTask.returnSuccess(up);
    }

    /**
     * 将恒星移动到其他的星座里
     *
     * @param sid
     * @param cid
     * @return
     */
    public int moveStellarToConstellation(int sid, int cid)
    {
        BeanCtxStellarData obean = (BeanCtxStellarData) getRecordByID(sid);
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (obean.getConOID() == cid)
        {
            return iReturn.BEAN_DO_NOT_NEED_MOVE;
        }
        //检查目标星座是否操作
        ConstellationData cdt = new ConstellationData(up);
        BeanCtxConstellation bccd = (BeanCtxConstellation) cdt.getRecordByID(cid);
        if (bccd == null)
        {
            return ctxReturn.WLD_CONSTELATION_NOTEXIST;
        }
        //获取目标星座的一个最大INDX  
        int cmx = allocateConstellationMaxIndex(cid);
        //手工事务，额么么么么
        dbTask.openManualTask(up);
        //更新恒星的星座点
        String sup = "update " + table + " set conOID = " + cid + ",conIndex = " + cmx +",regionOID= " + bccd.getRegionOID()  + " where OID = " + sid;
        int rup = jcommon.eExcute(up, sup);
        if (rup != iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnFail(up, rup);
        }
        //重置原星座的序列
        int rrb = rebuildConstellationIndexer(obean.getConOID(), true);
        if (rrb != iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnFail(up, rrb);
        }
        return dbTask.returnSuccess(up);
    }

}

//
//LOG
//TIME:
//REC:
//
