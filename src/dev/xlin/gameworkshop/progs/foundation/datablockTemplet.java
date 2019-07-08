package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.beanDatablockTemplet;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXML;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.dbTask;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import dev.xlin.tools.OIDCreator;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class datablockTemplet
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_datablock_templet";

    public datablockTemplet(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    public int saveAsTemplet(beanDatablockTemplet bean, iAdtXML iax )
    {
        //检查系统中是否已经有了dbtype和oriid的数据
        beanDatablockTemplet btmp = getTempletByOriID(bean.getDbType(), bean.getOriID());
        if (btmp != null)
        {
            return iReturn.DATA_BLOCK_TEMP_ORI_REPEAT;
        }
        dbTask.openManualTask(up);
        //数据插入
        int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
        bean.setOID(oid);
        datablockService dbsrv = new datablockService(up);
        xmlRight xr = iax.transToXML();
        String s = xr.transformToString();
        long roid = dbsrv.saveData(s );
        if (roid < 0)
        {
            return dbTask.returnFail(up, -(int)roid);
        }
        bean.setDatablock(roid);
        int r0 = jcommon.eInsert(sn, bean, table, false);
        if (r0 != iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnFail(up, r0);
        }
        else
        {
            return dbTask.returnSuccess(up);
        }
    }

    public beanDatablockTemplet getTempletByOID(int oid)
    {
        String sql = "select * from " + table + " where OID = " + oid;
        List ls = sn.querySQL(sql, beanDatablockTemplet.class);
        if (ls == null)
        {
            return null;
        }
        return (beanDatablockTemplet) ls.get(0);
    }

    public List getDatablockTemplets(int dbType, int ocls, String stxt)
    {
        if (stxt == null)
        {
            stxt = "";
        }
        stxt = stxt.trim();
        String sql = "select * from " + table + " where 1>0 ";
        if (dbType != 0)
        {
            sql = sql + " and dbType = " + dbType;
        }
        if (ocls != 0)
        {
            sql = sql + " and oclsID = " + ocls;
        }
        if (stxt.equals("") == false)
        {
            sql = sql + " and tempName like '%" + stxt + "%'";
        }
        return sn.querySQL(sql, beanDatablockTemplet.class);
    }

    public int updateTempletInfo(beanDatablockTemplet bean)
    {
        beanDatablockTemplet obean = getTempletByOID(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setTempDesp(bean.getTempDesp());
        obean.setTempName(bean.getTempName());
        return jcommon.eUpdate(sn, obean, table, "OID", false);
    }

    public int updateTempletData(beanDatablockTemplet bean)
    {
        beanDatablockTemplet obean = getTempletByOID(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setDatablock(bean.getDatablock());
        return jcommon.eUpdate(sn, obean, table, "OID", false);
    }

    public int removeTemplet(int oid)
    {
        String sql = "delete from " + table + " where OID = " + oid;
        return jcommon.eExcute(up, sql);
    }

    //通过ORIID读取模板，主要用于防重复
    private beanDatablockTemplet getTempletByOriID(int dbtype, long oriid)
    {
        String sql = "select * from " + table + " where dbType = " + dbtype + " and oriID = " + oriid;
        List ls = sn.querySQL(sql, beanDatablockTemplet.class);
        if (ls == null)
        {
            return null;
        }
        return (beanDatablockTemplet) ls.get(0);
    }

}

//
//LOG
//TIME:
//REC:
//
