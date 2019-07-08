package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablock;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablockDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemTemplet;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXML;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockClearValue;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.dbTask;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import dev.xlin.tools.codeTools;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.json.JSONObject;

public class itemTempletService
{

    private wakeup up = null;
    private session sn = null;

    public itemTempletService(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    public List getItemTemplets(int ocls, String s)
    {
        String sql = "select * from tb_item_templet where oclsID = " + ocls;
        if (s.trim().equals("") == false)
        {
            sql = sql + " and tempName like '%" + s.trim() + "%'";
        }
        return sn.querySQL(sql, BeanItemTemplet.class);
    }

    public int updateItemTempInfo(BeanItemTemplet bean)
    {
        BeanItemTemplet obean = getItemTempletByOID(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setTempDesp(bean.getTempDesp());
        obean.setTempName(bean.getTempName());
        return jcommon.eUpdate(sn, obean, "tb_item_templet", "OID", false);
    }

    public int removeItemTemplet(int oid)
    {
        BeanItemTemplet bit = getItemTempletByOID(oid);
        if (bit == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        dbTask.openManualTask(up);
        String shd = bit.getDatablockHeader();
        JSONObject jso = JSONObject.fromObject(shd);
        Set ks = jso.keySet();
        Iterator itor = ks.iterator();
        datablockService dbs = new datablockService(up);
        while (itor.hasNext())
        {
            String okey = (String) itor.next();
            int dbid = jso.getInt(okey);
            int rdb = dbs.removeData(dbid);
            if (rdb != iDAO.OPERATE_SUCCESS)
            {
                return dbTask.returnFail(up, rdb);
            }
        }
        //删除ITEMP 
        String sql = "delete from tb_item_templet where OID = " + oid;
        int r1 = jcommon.eExcute(up, sql);
        if (r1 != iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnFail(up, r1);
        }
        else
        {
            return dbTask.returnSuccess(up);
        }
    }

    public int setItemFromTemplet(int itid, int tpid)
    {
        itemDefine idef = new itemDefine(up);
        BeanItem bit = (BeanItem) idef.getRecordByID(itid);
        if (idef.checkBean(bit) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        BeanItemTemplet btmp = getItemTempletByOID(tpid);
        if (btmp == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        objectClassDefine ocd = new objectClassDefine(up);
        datablockService dbs = new datablockService(up);
        BeanObjectClass boc = (BeanObjectClass) ocd.getRecordByID(bit.getOclsID());
        JSONObject jso = JSONObject.fromObject(btmp.getDatablockHeader());
        JSONObject njo = new JSONObject();
        int[] ifcs = codeTools.convertStrToArr(boc.getClassFuncs());
        dbTask.openManualTask(up);
        for (int i = 0; i < ifcs.length; i++)
        {
            int fid = ifcs[i];
            int bid = jso.getInt(fid + "");
            String sdb = dbs.loadData(bid);
            long ndbx = dbs.saveData(sdb);
            if (ndbx < 0)
            {
                return dbTask.returnFail(up, -(int) ndbx);
            }
            njo.put(fid, ndbx);
            int r0 = dbs.removeData(bid);
            if (r0 != iDAO.OPERATE_SUCCESS)
            {
                return dbTask.returnFail(up, r0);
            }
        }
        return dbs.updateData(bit.getEquipData(), njo.toString());
    }

    public BeanItemTemplet getItemTempletByOID(int oid)
    {
        String sql = "select * from tb_item_templet where OID = " + oid;
        List ls = sn.querySQL(sql, BeanItemTemplet.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanItemTemplet) ls.get(0);
    }

    public BeanItemTemplet getItemTempletBySourceItem(int id)
    {
        String sql = "select * from tb_item_templet where srcItem = " + id;
        List ls = sn.querySQL(sql, BeanItemTemplet.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanItemTemplet) ls.get(0);
    }

    public int saveAsTemplet(int itid, String name, String desp, boolean clear)
    {
        itemDefine idef = new itemDefine(up);
        BeanItem bit = (BeanItem) idef.getRecordByID(itid);
        if (idef.checkBean(bit) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (getItemTempletBySourceItem(itid) != null)
        {
            return iReturn.ITEM_TEMPLET_REPEAT;
        }
        //开始
        //获取ITEM自身的datablock引导
        //拆解为各数据块的引导
        //透过数据块定义，反射操作器，读取数据，恢复结构，清零，转存，重新聚合引导
        //一系列灼热接口操作，完成模板创建 
        datablockService dbsrv = new datablockService(up);
        BeanDatablock bdt = dbsrv.getDatabean(bit.getEquipData());
        if (bdt == null)
        {
            return iReturn.ITEM_HEADER_DATA_NOTEXIST;
        }
        String sHeader = bdt.getDatablock();
        JSONObject jso = JSONObject.fromObject(sHeader);
        JSONObject njo = new JSONObject();
        objectClassDefine ocd = new objectClassDefine(up);
        BeanObjectClass boc = (BeanObjectClass) ocd.getRecordByID(bit.getOclsID());
        int[] ifcs = codeTools.convertStrToArr(boc.getClassFuncs());
        datablockDefine dbdef = new datablockDefine(up);
        datablockService dsrv = new datablockService(up);
        dbTask.openManualTask(up);
        for (int i = 0; i < ifcs.length; i++)
        {
            try
            {
                int fid = ifcs[i];
                int dbidx = jso.getInt(fid + "");
                String sctx = dsrv.loadData(dbidx);
                BeanDatablockDefine block = (BeanDatablockDefine) dbdef.getRecordByID(fid);
                Object odb = Class.forName(block.getDbAdtClass()).newInstance();
                iAdtXML iax = (iAdtXML) odb;
                xmlRight xr = new xmlRight();
                boolean bxr = xr.parseXMLfromString(sctx);
                if (!bxr)
                {
                    continue;
                }
                boolean brv = iax.revertFromXML(xr);
                if (!brv)
                {
                    continue;
                }
                if (clear)
                {
                    iDatablockClearValue idcv = (iDatablockClearValue) odb;
                    idcv.setAllValueClear();
                }
                String snx = iax.transToXML().transformToString();
                long nblkIdx = dsrv.saveData(snx);
                if (nblkIdx >= 0)
                {
                    njo.put(fid, nblkIdx);
                }
                else
                {
                    return dbTask.returnFail(up, -(int) nblkIdx);
                }
            }
            catch (Exception excp)
            {
                excp.printStackTrace();
            }
        }
        BeanItemTemplet btmp = new BeanItemTemplet();
        btmp.setDatablockHeader(njo.toString());
        btmp.setSrcItem(itid);
        btmp.setTempDesp(desp);
        btmp.setTempName(name);
        btmp.setOclsID(bit.getOclsID());
        btmp.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        int r = jcommon.eInsert(sn, btmp, "tb_item_templet", false);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnSuccess(up);
        }
        else
        {
            return dbTask.returnFail(up, r);
        }
    }

}
