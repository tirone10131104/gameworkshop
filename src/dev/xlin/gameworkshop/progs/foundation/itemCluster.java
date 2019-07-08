package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemCluster;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanObjectClass;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.codeTools;
import java.util.List;

public class itemCluster implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_item_cluster";

    public itemCluster(wakeup _up)
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
        BeanItemCluster bean = (BeanItemCluster) o;
        if (getItemClusterByTag(bean.getClusterTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        int r1 = doCheckLogic(bean);
        if (r1 != 0)
        {
            return r1;
        }
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        return jcommon.eInsert(sn, bean, table, false);
    }

    public int moveToType(int icid, int tpid)
    {
        String sql = "update " + table + " set typeID = " + tpid + " where OID = " + icid;
        return jcommon.eExcute(up, sql);
    }

    public BeanItemCluster getItemClusterByTag(String tag)
    {
        String sql = "select * from " + table + " where clusterTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanItemCluster.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanItemCluster) ls.get(0);
    }

    private int doCheckLogic(BeanItemCluster bean)
    {
        //数据目标类型
        //根据类型检查IDS
        int[] ids = codeTools.convertStrToArr(bean.getOCLS());
        if (bean.getTargetType() == iConst.ICLUS_TARTYPE_ITEMS)
        {
            itemDefine idef = new itemDefine(up);
            for (int i = 0; i < ids.length; i++)
            {
                int id = ids[i];
                BeanItem bit = (BeanItem) idef.getRecordByID(id);
                if (idef.checkBean(bit) == false)
                {
                    return iReturn.DATA_TARGET_ERROR;
                }
            }
        }
        else if (bean.getTargetType() == iConst.ICLUS_TARTYPE_OCLS)
        {
            objectClassDefine ocd = new objectClassDefine(up);
            for (int i = 0; i < ids.length; i++)
            {
                int id = ids[i];
                BeanObjectClass boc = (BeanObjectClass) ocd.getRecordByID(id);
                if (ocd.checkBean(boc) == false)
                {
                    return iReturn.DATA_TARGET_ERROR;
                }
            }
        }
        else if (bean.getTargetType() == iConst.ICLUS_TARTYPE_IT_TPS || bean.getTargetType() == iConst.ICLUS_TARTYPE_OC_TPS)
        {
            sttType stp = new sttType(up);
            for (int i = 0; i < ids.length; i++)
            {
                int id = ids[i];
                beanSttType bst = (beanSttType) stp.getRecordByID(id);
                if (stp.checkBean(bst) == false)
                {
                    return iReturn.DATA_TARGET_ERROR;
                }
            }
        }
        else
        {
            return iReturn.DATA_TARGET_TYPE_ERROR;
        }
        return 0;
    }

    private int doCheckParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanItemCluster.class)
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
        BeanItemCluster bean = (BeanItemCluster) o;
        BeanItemCluster obean = (BeanItemCluster) getRecordByID(bean.getOID());
        int r2 = doCheckLogic(bean);
        if (r2 != 0)
        {
            return r2;
        }
        obean.setClusterName(bean.getClusterName());
        obean.setDescript(bean.getDescript());
        obean.setOCLS(bean.getOCLS());
        obean.setTargetType(bean.getTargetType());
        return jcommon.eUpdate(sn, obean, table, "OID", false);
    }

    @Override
    public int deleteRecord(int i)
    {
        String sql = "update " + table + " set status = " + iDAO.OBJECT_STATE_DELETE + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    public List getItemClustersByType(int tp, boolean showAll)
    {
        String sql = "select * from " + table + " where typeID = " + tp;
        if (showAll == false)
        {
            sql = sql + " and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, BeanItemCluster.class);
    }

    public List findItemClusters(int tartp, int tpid, boolean showAll)
    {
        String sql = "select * from " + table + " where OID > 0 ";
        if (tartp != 0)
        {
            sql = sql + " and targetType = " + tartp;
        }
        if (tpid != 0)
        {
            sql = sql + " and typeID = " + tpid;
        }
        if (showAll == false)
        {
            sql = sql + " and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, BeanItemCluster.class);
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
        List ls = sn.querySQL(sql, BeanItemCluster.class);
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
        if (o.getClass() != BeanItemCluster.class)
        {
            return false;
        }
        BeanItemCluster bic = (BeanItemCluster) o;
        if (bic.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int revertBean(int i)
    {
        String sql = "update tb_item_cluster set status = " + iDAO.OBJECT_STATE_ACTIVE
                + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public int destroyBean(int i)
    {
        BeanItemCluster bic = (BeanItemCluster) getRecordByID(i);
        if (bic == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (bic.getStatus() == iDAO.OBJECT_STATE_ACTIVE)
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
