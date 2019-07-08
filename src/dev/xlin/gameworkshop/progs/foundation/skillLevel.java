package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillLevel;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.dbTask;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.json.JSONObject;

/**
 *
 * @author 刘祎鹏
 */
public class skillLevel implements iDAO
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_skill_level";

    public skillLevel(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    @Override
    public int createRecord(Object o, boolean inSrv)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        beanSkillLevel bean = (beanSkillLevel) o;
        //检查，并丰富
        int rck = doCheckLevelAndComplete(bean);
        if (rck != 0)
        {
            return rck;
        }
        //数据填充
        bean.setOID(OIDCreator.createOID(up, table, "OID", iDAO.OID_LONG_MIN, iDAO.OID_LONG_LENTH));
        //创建DATAHEADER - JSON 
        dbTask.openManualTask(up);
        datablockService dbs = new datablockService(up);
        try
        {
            ArrayList ablk = new ArrayList();
            ablk.add(iConst.SYS_DB_SKILL_UPGRADE_CONDITION);
            ablk.add(iConst.SYS_DB_FUNCTION_DEFINE_DATA);
            String s = dbs.initDatablockStruct(ablk, true);
            bean.setDataHeader(s);
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return dbTask.returnFail(up, iReturn.DATA_BLOCK_TYPE_UNKNOWN);
        }
        int r = jcommon.eInsert(sn, bean, table, inSrv);
        if (r != iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnFail(up, r);
        }
        else
        {
            return dbTask.returnSuccess(up);
        }
    }

    private int doCheckLevelAndComplete(beanSkillLevel bean)
    {
        //检查SKILL是否存在
        skillDefine skd = new skillDefine(up);
        beanSkillDefine bskd = (beanSkillDefine) skd.getRecordByID(bean.getSkillOID());
        if (skd.checkBean(bskd) == false)
        {
            return iReturn.SKL_NOT_FOUND;
        }
        //自动在创建时进行过滤，避免被污染
        //创建只做追加，不做更多的可控项
        bean.setLevelIdx(0);
        bean.setMasterLevel(0);
        //获取技能节点的所有列表。
        List lls = getLevelBySkill(bean.getSkillOID());
        if (lls != null)
        {
            //HEAD设置为列表末尾的节点，IDX= 节点末尾的IDX+1，
            beanSkillLevel bsklv = (beanSkillLevel) lls.get(lls.size() - 1);
            bean.setLevelIdx(bsklv.getLevelIdx() + 1);
        }
        else
        {
            //如果列表为空，则自动master=true
            bean.setLevelIdx(1);
            bean.setMasterLevel(iConst.BOL_TRUE);
        }
        return 0;
    }

    public List getLevelBySkill(int skid)
    {
        String sql = "select * from " + table + " where skillOID = " + skid
                + " order by levelIdx ";
        return sn.querySQL(sql, beanSkillLevel.class);
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != beanSkillLevel.class)
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
        beanSkillLevel bean = (beanSkillLevel) o;
        beanSkillLevel obean = (beanSkillLevel) getRecordByID(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setLevelDesp(bean.getLevelDesp());
        obean.setLevelName(bean.getLevelName());
        //数据操作
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    @Override
    public int deleteRecord(int i)
    {
        beanSkillLevel bean = (beanSkillLevel) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        dbTask.openManualTask(up);
        //数据库事务操作
        //区块删除
        datablockService dbs = new datablockService(up);
        JSONObject jso = JSONObject.fromObject(bean.getDataHeader());
        Set ks = jso.keySet();
        Iterator itor = ks.iterator();
        while (itor.hasNext())
        {
            String dbid = (String) itor.next();
            int blkid = jso.getInt(dbid);
            int rdb = dbs.removeData(blkid);
            if (rdb != iDAO.OPERATE_SUCCESS)
            {
                return dbTask.returnFail(up, rdb);
            }
        }
        //记录删除
        String sql = "delete from " + table + " where OID = " + i;
        int r = jcommon.eExcute(up, sql);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnSuccess(up);
        }
        else
        {
            return dbTask.returnFail(up, i);
        }
    }

    public beanSkillLevel getLevel(int skid, int lvidx)
    {
        String sql = "select * from " + table + " where skillOID = " + skid
                + " and levelIdx = " + lvidx;
        List ls = sn.querySQL(sql, beanSkillLevel.class);
        if (ls == null)
        {
            return null;
        }
        return (beanSkillLevel) ls.get(0);
    }

    public int moveLevelUp(int id)
    {
        beanSkillLevel obean = (beanSkillLevel) getRecordByID(id);
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (obean.getLevelIdx() == 0)
        {
            return iReturn.BEAN_CANT_MOVE_UP;
        }
        beanSkillLevel bhead = getLevel(obean.getSkillOID(), obean.getLevelIdx() - 1);
        //执行操作
        //对调
        bhead.setLevelIdx(obean.getLevelIdx());
        obean.setLevelIdx(obean.getLevelIdx() - 1);
        //数据操作
        ArrayList aup = new ArrayList();
        aup.add(obean);
        aup.add(bhead);
        boolean b = sn.updateRows(aup, table, "OID", false);
        if (b)
        {
            return iDAO.OPERATE_SUCCESS;
        }
        else
        {
            return iDAO.OPERATE_FAIL;
        }
    }

    public int moveLevelDown(int id)
    {
        beanSkillLevel obean = (beanSkillLevel) getRecordByID(id);
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //获取其尾随的Level
        beanSkillLevel blask = getLevel(obean.getSkillOID(), obean.getLevelIdx() + 1);
        if (blask == null)
        {
            return iReturn.BEAN_CANT_MOVE_DOWN;
        }
        //执行调换
        obean.setLevelIdx(obean.getLevelIdx() + 1);
        blask.setLevelIdx(blask.getLevelIdx() - 1);
        //数据操作
        ArrayList aup = new ArrayList();
        aup.add(obean);
        aup.add(blask);
        boolean b = sn.updateRows(aup, table, "OID", true);
        if (b)
        {
            return iDAO.OPERATE_SUCCESS;
        }
        else
        {
            return iDAO.OPERATE_FAIL;
        }
    }

    public int setLevelAsMaster(int id)
    {
        beanSkillLevel obean = (beanSkillLevel) getRecordByID(id);
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (obean.getMasterLevel() == iConst.BOL_TRUE)
        {
            return iDAO.OPERATE_SUCCESS;
        }
        //获取原MASTER记录
        //数据库事务启用
        dbTask.openManualTask(up);
        beanSkillLevel boms = getMasterLevel(obean.getSkillOID());
        if (boms != null)
        {
            //取消原MAST，设置新的为MAST
            String sup = "update " + table + " set masterLevel = " + iConst.BOL_FALSE
                    + " where OID = " + boms.getOID();
            String smas = "update " + table + " set masterLevel = " + iConst.BOL_TRUE
                    + " where OID = " + obean.getOID();
            int r1 = jcommon.eExcute(up, sup);
            if (r1 != iDAO.OPERATE_SUCCESS)
            {
                return dbTask.returnFail(up, r1);
            }
            int r2 = jcommon.eExcute(up, smas);
            if (r2 != iDAO.OPERATE_SUCCESS)
            {
                return dbTask.returnFail(up, r2);
            }
        }
        else
        {
            //直接设置本数据为MAST
            String smas = "update " + table + " set masterLevel = " + iConst.BOL_TRUE
                    + " where OID = " + obean.getOID();
            int r2 = jcommon.eExcute(up, smas);
            if (r2 != iDAO.OPERATE_SUCCESS)
            {
                return dbTask.returnFail(up, r2);
            }
        }
        return dbTask.returnSuccess(up);
    }

    public beanSkillLevel getMasterLevel(int skid)
    {
        String sql = "select * from " + table + " where masterLevel = " + iConst.BOL_TRUE
                + " and skillOID = " + skid;
        List ls = sn.querySQL(sql, beanSkillLevel.class);
        if (ls == null)
        {
            return null;
        }
        return (beanSkillLevel) ls.get(0);
    }

    public int rebuildSkillLevelIndex(int skid, boolean useTask)
    {
        List ls = getLevelBySkill(skid);
        if (ls == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        ArrayList aup = new ArrayList();
        for (int i = 0; i < ls.size(); i++)
        {
            beanSkillLevel bsl = (beanSkillLevel) ls.get(i);
            String sup = "update " + table + " set levelIdx = " + (i + 1)
                    + " where OID = " + bsl.getOID();
            System.err.println("sup = " + sup);
            aup.add(sup);
        }
        if (useTask)
        {
            up.setAutoCommit(false);
        }
        for (int i = 0; i < aup.size(); i++)
        {
            String sql = (String) aup.get(i);
            int rex = jcommon.eExcute(up, sql);
            if (rex != iDAO.OPERATE_SUCCESS)
            {
                if (useTask)
                {
                    up.roolBack();
                    up.setAutoCommit(true);
                }
                return rex;
            }
        }
        if (useTask)
        {
            up.commit();
            up.setAutoCommit(true);
        }
        return iDAO.OPERATE_SUCCESS;
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
        List ls = sn.querySQL(sql, beanSkillLevel.class);
        if (ls == null)
        {
            return null;
        }
        return ls.get(0);
    }

}
