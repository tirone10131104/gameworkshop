package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemEquipStruct;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class itemEquipStruct implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_item_equip_struct";

    public static final int STRUCT_MAX_LEVEL = 3;

    public itemEquipStruct(wakeup _up)
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
        BeanItemEquipStruct bean = (BeanItemEquipStruct) o;
        if (getItemEquipStructByTag(bean.getEquipTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        int rlg = doCheckLogic(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        int mxid = findMaxIndex(bean);
        bean.setIndexID(mxid + 1);
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
        bean.setOID(oid);
        if (bean.getParentID() != 0)
        {
            BeanItemEquipStruct bpar = (BeanItemEquipStruct) getRecordByID(bean.getParentID());
            bean.setLevelID(bpar.getLevelID() + 1);
        }
        else
        {
            bean.setLevelID(1);
        }
        return jcommon.eInsert(sn, bean, table, bln);
    }

    private int findMaxIndex(BeanItemEquipStruct bean)
    {
        String sql = "select max(indexID ) as maxid from " + table + " where parentID = " + bean.getParentID();
        try
        {
            Hashtable[] hts = up.querySQL(sql);
            if (hts == null)
            {
                return 0;
            }
            else
            {
                Hashtable ht = hts[0];
                return (int) ht.get("maxid");
            }
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return 0;
        }
    }

    private int doCheckLogic(BeanItemEquipStruct bean)
    {
        if (bean.getParentID() != 0)
        {
            BeanItemEquipStruct bpar = (BeanItemEquipStruct) getRecordByID(bean.getParentID());
            if (bpar == null)
            {
                return iReturn.IEST_PARENT_NOTEXIST;
            }
            if (bpar.getLevelID() == STRUCT_MAX_LEVEL)
            {
                return iReturn.IEST_OVER_MAX_LEVEL;
            }
        }
        return 0;
    }

    public BeanItemEquipStruct getItemEquipStructByTag(String stg)
    {
        String sql = "select * from " + table + " where equipTag = '" + stg.trim() + "'";
        List ls = sn.querySQL(sql, BeanItemEquipStruct.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanItemEquipStruct) ls.get(0);
    }

    public List getItemListByParent(int id, boolean showAll)
    {
        String sql = "select * from " + table + " where parentID = " + id;
        if (showAll == false)
        {
            sql = sql + " and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        sql = sql + " order by indexID";
        return sn.querySQL(sql, BeanItemEquipStruct.class);
    }

    public List getItemListByParentTag(String stg, boolean showAll)
    {
        BeanItemEquipStruct bpar = getItemEquipStructByTag(stg);
        if (bpar == null)
        {
            return null;
        }
        return getItemListByParent(bpar.getOID(), showAll);
    }

    public int moveUp(int oid)
    {
        BeanItemEquipStruct bean = (BeanItemEquipStruct) getRecordByID(oid);
        if (checkBean(bean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        String sql = "select * from " + table + " where parentID = " + bean.getParentID()
                + " and indexID < " + bean.getIndexID() + " order by indexID desc ";
        List ls = sn.querySQL(sql, BeanItemEquipStruct.class);
        if (ls == null)
        {
            return iDAO.OPERATE_FAIL;
        }
        else
        {
            BeanItemEquipStruct bpre = (BeanItemEquipStruct) ls.get(0);
            int tidx = bpre.getIndexID();
            //数据交换
            bpre.setIndexID(bean.getIndexID());
            bean.setIndexID(tidx);
            //数据库操作
            ArrayList aup = new ArrayList();
            aup.add(bpre);
            aup.add(bean);
            boolean b = sn.updateRows(aup, table, "OID", false);
            if (b == false)
            {
                return iDAO.OPERATE_FAIL;
            }
            else
            {
                return iDAO.OPERATE_SUCCESS;
            }
        }
    }

    public int moveDown(int oid)
    {
        BeanItemEquipStruct bean = (BeanItemEquipStruct) getRecordByID(oid);
        if (checkBean(bean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        String sql = "select * from " + table + " where parentID = " + bean.getParentID()
                + " and indexID > " + bean.getIndexID() + " order by indexID ";
        List ls = sn.querySQL(sql, BeanItemEquipStruct.class);
        if (ls == null)
        {
            return iDAO.OPERATE_FAIL;
        }
        else
        {
            BeanItemEquipStruct blast = (BeanItemEquipStruct) ls.get(0);
            int tidx = blast.getIndexID();
            //数据交换
            blast.setIndexID(bean.getIndexID());
            bean.setIndexID(tidx);
            //数据库操作
            ArrayList aup = new ArrayList();
            aup.add(blast);
            aup.add(bean);
            boolean b = sn.updateRows(aup, table, "OID", false);
            if (b == false)
            {
                return iDAO.OPERATE_FAIL;
            }
            else
            {
                return iDAO.OPERATE_SUCCESS;
            }
        }
    }

    private int doCheckParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanItemEquipStruct.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = doCheckParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanItemEquipStruct bean = (BeanItemEquipStruct) o;
        BeanItemEquipStruct obean = (BeanItemEquipStruct) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据转移
        obean.setEquipDesp(bean.getEquipDesp());
        obean.setEquipName(bean.getEquipName());
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    @Override
    public int deleteRecord(int i)
    {
        List ls = getItemListByParent(i, true);
        if (ls != null)
        {
            return iReturn.BEAN_CANT_DELETE;
        }
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_DELETE
                +" where OID = " + i ;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public List getAllRecord()
    {
        String sql = "select * from " + table;
        return sn.querySQL(sql, BeanItemEquipStruct.class);
    }

    @Override
    public Object getRecordByID(int i)
    {
        String sql = "select * from " + table + " where OID = " + i;
        List ls = sn.querySQL(sql, BeanItemEquipStruct.class);
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
        if (o.getClass() != BeanItemEquipStruct.class)
        {
            return false;
        }
        BeanItemEquipStruct bean = (BeanItemEquipStruct) o;
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int revertBean(int i)
    {
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_ACTIVE
                +" where OID = " + i ;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public int destroyBean(int i)
    {
        BeanItemEquipStruct bean = (BeanItemEquipStruct) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (bean.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_NOT_READY_DESTROY;
        }
        String sql = "delete from " + table + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public List getDeleted()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
