package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.beanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItemEquipStruct;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.dbTask;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.json.JSONObject;

/**
 *
 * @author 刘祎鹏
 */
public class itemDefine implements iDAO, iBeanCheckable, iBeanRevert
{

    private String tableItems = "tb_items";

    private wakeup up = null;
    private session sn = null;

    public itemDefine(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    @Override
    public int createRecord(Object o, boolean bln)
    {
        int r0 = doCheckParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        beanItem bean = (beanItem) o;
        //检查标签重叠
        if (getItemDefineByTag(bean.getItemTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        int rlg = doCheckItemLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        //OID
        int oid = OIDCreator.createOID(up, tableItems, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
        bean.setOID(oid);
        //创建其数据结构引导JSON
        dbTask.openManualTask(up);
        datablockService dbs = new datablockService(up);
        try
        {
            String s = dbs.initDatablockStruct(bean.getOclsID(), true); 
            datablockService dbsrv = new datablockService(up);
            long ldtid = dbsrv.saveData(s);
            if (ldtid < 0)
            {
                return dbTask.returnFail(up, -(int) ldtid);
            }
            else
            {
                bean.setEquipData(ldtid);
            }
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return dbTask.returnFail(up, iReturn.DATA_BLOCK_TYPE_UNKNOWN);
        }
        bean.setState(iDAO.OBJECT_STATE_ACTIVE);
        bean.setEquipStruct(makeItemEquipStructString(bean));
        int r = jcommon.eInsert(sn, bean, tableItems, false);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnSuccess(up);
        }
        else
        {
            return dbTask.returnFail(up, r);
        }
    }

    private String makeItemEquipStructString(beanItem bean)
    {
        String sr = "";
        itemEquipStruct ies = new itemEquipStruct(up);
        if (bean.getSlotRoot() != 0)
        {
            beanItemEquipStruct bies = (beanItemEquipStruct) ies.getRecordByID(bean.getSlotRoot());
            sr = sr + bies.getEquipTag();
        }
        if (bean.getSlotType() != 0)
        {
            beanItemEquipStruct bies = (beanItemEquipStruct) ies.getRecordByID(bean.getSlotType());
            sr = sr + ";" + bies.getEquipTag();
        }
        if (bean.getSlotIndex() != 0)
        {
            beanItemEquipStruct bies = (beanItemEquipStruct) ies.getRecordByID(bean.getSlotIndex());
            sr = sr + ";" + bies.getEquipTag();
        }
        return sr;
    }

    private int doCheckItemLogic(beanItem bean)
    {
//        if (bean.getRealItem() != iConst.BOL_TRUE && bean.getEquipment() == iConst.BOL_TRUE)
//        {
//            //非实物体，不可被设置为装备
//            return iReturn.ITEM_ERR_5002;
//        }
//        if (bean.getStackLimit() < 0)
//        {
//            return iReturn.ITEM_ERR_5006;
//        }
//        if (bean.getEquipLimit() < 0)
//        {
//            return iReturn.ITEM_ERR_5007;
//        }
        return 0;
    }

    public int moveToType(int icid, int tpid)
    {
        String sql = "update " + tableItems + " set tpid  = " + tpid + " where OID = " + icid;
        return jcommon.eExcute(up, sql);
    }

    public beanItem getItemDefineByTag(String tag)
    {
        String sql = "select * from " + tableItems + " where itemTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, beanItem.class);
        if (ls == null)
        {
            return null;
        }
        return (beanItem) ls.get(0);
    }

    private int doCheckParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != beanItem.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    public List queryItems(int tpid, int ocls, String text)
    {
        if (tpid == 0 && ocls == 0 && text.trim().equals(""))
        {
            return null;
        }
        String sql = "select * from " + tableItems + " where 1>0 ";
        if (tpid != 0)
        {
            sql = sql + " and tpid = " + tpid;
        }
        if (ocls != 0)
        {
            sql = sql + " and oclsID = " + ocls;
        }
        if (text.trim().equals("") == false)
        {
            text = text.trim();
            sql = sql + " and (itemName like '%" + text + "%' or itemTag like '%" + text + "%')";
        }
        sql = sql + " order by oclsID";
        return sn.querySQL(sql, beanItem.class);
    }

    public List getItemsByType(int tpid, boolean showAll)
    {
        String sql = "select * from " + tableItems + " where tpid = " + tpid;
        if (showAll == false)
        {
            sql = sql + " and state = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, beanItem.class);
    }

    public List getItemsByOcls(int ocls, boolean showAll)
    {
        String sql = "select * from " + tableItems + " where oclsID = " + ocls;
        if (showAll == false)
        {
            sql = sql + " and state = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, beanItem.class);
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = doCheckParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        beanItem bean = (beanItem) o;
        beanItem obean = (beanItem) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据更新
        obean.setItemName(bean.getItemName());
        obean.setItemDesp(bean.getItemDesp());
        obean.setHide(bean.getHide());
        obean.setLocked(bean.getLocked());
        obean.setStack(bean.getStack());
        obean.setStackLimit(bean.getStackLimit());
        obean.setSlotIndex(bean.getSlotIndex());
        obean.setSlotType(bean.getSlotType());
        obean.setEquipLimit(bean.getEquipLimit());
        obean.setSlotRoot(bean.getSlotRoot());
        obean.setEquipment(bean.getEquipment());
        obean.setEquipStruct(makeItemEquipStructString(bean));
        obean.setAbstractItem(bean.getAbstractItem());
        obean.setContainerItem(bean.getContainerItem());
        obean.setEquipRoot(bean.getEquipRoot());
        obean.setCapUse(bean.getCapUse());
        return jcommon.eUpdate(sn, obean, tableItems, "OID", false);
    }

    @Override
    public int deleteRecord(int i)
    {
        String slq = "update " + tableItems + " set state = " + iDAO.OBJECT_STATE_DELETE
                + " where OID = " + i;
        return jcommon.eExcute(up, slq);
    }

    @Override
    public List getAllRecord()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRecordByID(int i)
    {
        String sql = "select * from " + tableItems + " where OID = " + i;
        List ls = sn.querySQL(sql, beanItem.class);
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
        if (o.getClass() != beanItem.class)
        {
            return false;
        }
        beanItem bean = (beanItem) o;
        if (bean.getState() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int revertBean(int i)
    {
        String slq = "update " + tableItems + " set state = " + iDAO.OBJECT_STATE_ACTIVE
                + " where OID = " + i;
        return jcommon.eExcute(up, slq);
    }

    @Override
    public int destroyBean(int i)
    {
        //删除数据之前要特别的小心呐。。。。
        //在整体上，要删除所有联动的数据，特别是DATABLOCK相关对应的记录s
        beanItem bse = (beanItem) getRecordByID(i);
        if (bse.getState() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_NOT_READY_DESTROY;
        }
        //启用数据库事务
        dbTask.openManualTask(up);
        //读取DATABLOCK 引导数据
        JSONObject jso = JSONObject.fromObject(bse.getEquipData());
        datablockService dbs = new datablockService(up);
        //使用DBS挨个进行移除
        Set ks = jso.keySet();
        Iterator itor = ks.iterator();
        while (itor.hasNext())
        {
            String skey = (String) itor.next();
            int dbid = jso.getInt(skey);
            int r = dbs.removeData(dbid);
            if (r != iDAO.OPERATE_SUCCESS)
            {
                return dbTask.returnFail(up, r);
            }
        }
        //进行SHIP EQUIP 本体数据清除
        String sql = "delete from " + tableItems + " where OID = " + i;
        int r01 = jcommon.eExcute(up, sql);
        if (r01 != iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnFail(up, r01);
        }
        //数据存储
        return dbTask.returnSuccess(up);
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
