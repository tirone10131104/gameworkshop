package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iClone;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataColumnOper;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElementOper;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataPageOper;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockClearValue;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockCopy;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockInitInstance;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDateElementCleanValue;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.interfaces.iDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author 刘祎鹏
 */
public class dataStructOperator implements iDataPageOper, iDataColumnOper, iDataElementOper, iAdtDocumentSave, iDatablockClearValue, iDatablockCopy
{

    private ArrayList datapages = new ArrayList();
    private HashMap columnOIDMapping = new HashMap();
    private HashMap elementOIDMapping = new HashMap();
    private boolean bNeedSave = false;

    @Override
    public Object datablockCopy( )
    {
        ArrayList anps = new ArrayList();
        for (int i = 0; i < datapages.size(); i++)
        {
            beanDataPage bdp = (beanDataPage) datapages.get(i);
            anps.add(bdp.cloneMe());
        }
        return anps;
    }

    @Override
    public void setAllValueClear()
    {
        Set ks = elementOIDMapping.keySet();
        Iterator itor = ks.iterator();
        while (itor.hasNext())
        {
            int eid = (int) itor.next();
            iDateElementCleanValue idecv = (iDateElementCleanValue) elementOIDMapping.get(eid);
            idecv._setValueClear();
        }
    }

    public dataStructOperator(ArrayList apage)
    {
        datapages = apage;
        columnOIDMapping = new HashMap();
        elementOIDMapping = new HashMap();
        rebuildAllMapping();
    }

    public void rebuildAllMapping()
    {
        columnOIDMapping.clear();
        elementOIDMapping.clear();
        for (int i = 0; i < datapages.size(); i++)
        {
            beanDataPage bpage = (beanDataPage) datapages.get(i);
            List lcos = bpage.getColumns();
            for (int j = 0; j < lcos.size(); j++)
            {
                beanDataColumn bdc = (beanDataColumn) lcos.get(j);
                columnOIDMapping.put(bdc.getOID(), bdc);
                List les = bdc.getDatas();
                for (int k = 0; k < les.size(); k++)
                {
                    iDataElement ide = (iDataElement) les.get(k);
                    elementOIDMapping.put(ide._getDataOID(), ide);
                }
            }
        }
    }

    @Override
    public List getPageList()
    {
        ArrayList arl = new ArrayList();
        for (int i = 0; i < datapages.size(); i++)
        {
            beanDataPage bdp = (beanDataPage) datapages.get(i);
            arl.add(bdp.cloneMe());
        }
        return arl;
    }

    @Override
    public beanDataPage getDataPageByIndex(int idx)
    {
        beanDataPage bean = getDataPageByIndexNotClone(idx);
        if (bean != null)
        {
            return (beanDataPage) bean.cloneMe();
        }
        else
        {
            return null;
        }
    }

    @Override
    public beanDataPage getDataPageByTag(String tag)
    {
        beanDataPage bean = getDataPageByTagNotClone(tag);
        if (bean == null)
        {
            return null;
        }
        else
        {
            return (beanDataPage) bean.cloneMe();
        }
    }

    private beanDataPage getDataPageByTagNotClone(String tag)
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            beanDataPage bdp = (beanDataPage) datapages.get(i);
            if (bdp.getPageTag().equals(tag.trim()))
            {
                return (beanDataPage) bdp.cloneMe();
            }
        }
        return null;
    }

    private beanDataPage getDataPageByIndexNotClone(int idx)
    {
        try
        {
            beanDataPage bean = (beanDataPage) datapages.get(idx);
            return bean;
        }
        catch (Exception excp)
        {
            return null;
        }
    }

    @Override
    public int updatePage(String name, int idx)
    {
        beanDataPage bean = getDataPageByIndexNotClone(idx);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        bean.setPageName(name);
        bNeedSave = true;
        return iDAO.OPERATE_SUCCESS;
    }

    @Override
    public int getPageIndex(String tag)
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            beanDataPage bean = (beanDataPage) datapages.get(i);
            if (bean.getPageTag().equals(tag.trim()))
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getPageTag(int index)
    {
        beanDataPage bean = getDataPageByIndexNotClone(index);
        if (bean == null)
        {
            return null;
        }
        else
        {
            return bean.getPageTag();
        }
    }

    @Override
    public boolean removePageByIndex(int index)
    {
        beanDataPage bean = getDataPageByIndexNotClone(index);
        if (bean == null)
        {
            return false;
        }
        //防误删数据验证，等
        List lcos = getColumnListByPageIndex(index);
        if (lcos.size() != 0)
        {
            return false;
        }
        datapages.remove(index);
        bNeedSave = true;
        return true;
    }

    @Override
    public boolean removePageByTag(String stg)
    {
        int idx = getPageIndex(stg);
        if (idx < 0)
        {
            return false;
        }
        return removePageByIndex(idx);
    }

    @Override
    public List getColumnListByPageIndex(int pgidx)
    {
        beanDataPage bean = getDataPageByIndexNotClone(pgidx);
        if (bean == null)
        {
            return null;
        }
        ArrayList acs = new ArrayList();
        for (int i = 0; i < bean.getColumns().size(); i++)
        {
            beanDataColumn bdc = (beanDataColumn) bean.getColumns().get(i);
            acs.add(bdc.cloneMe());
        }
        return acs;
    }

    @Override
    public List getColumnListByPageTag(String tag)
    {
        int idx = getPageIndex(tag);
        if (idx < 0)
        {
            return null;
        }
        return getColumnListByPageIndex(idx);
    }

    @Override
    public int appendColumn(beanDataColumn bean, int pageIdx)
    {
        if (bean == null)
        {
            return -iDAO.PARAM_OBJECT_NULL;
        }
        beanDataPage bpage = getDataPageByIndexNotClone(pageIdx);
        if (bpage == null)
        {
            return -iReturn.DATA_BLOCK_PAGE_NOTEXIST;
        }
        //检查标签
        String stg = bean.getColumnTag();
        if (getDataColumnByTagNotClone(stg) != null)
        {
            return -iReturn.BEAN_TAG_REPEAT;
        }
        //开始制作一个OID
        int oid = createColumnOID();
        bean.setOID(oid);
        ArrayList acos = bpage.getColumns();
        acos.add(bean);
        bpage.setColumns(acos);
        columnOIDMapping.put(oid, bean);
        bNeedSave = true;
        return oid;
    }

    private int createColumnOID()
    {
        Random r = new Random();
        while (true)
        {
            int oid = r.nextInt();
            if (oid <= 0)
            {
                continue;
            }
            if (columnOIDMapping.containsKey(oid) == false)
            {
                return oid;
            }
        }
    }

    private beanDataColumn getDataColumnByOIDNotClone(int oid)
    {
        if (columnOIDMapping.containsKey(oid))
        {
            return (beanDataColumn) columnOIDMapping.get(oid);
        }
        else
        {
            return null;
        }
    }

    //通过标签获取列数据bean
    private beanDataColumn getDataColumnByTagNotClone(String tag)
    {
        Set ks = columnOIDMapping.keySet();
        Iterator itor = ks.iterator();
        while (itor.hasNext())
        {
            int oid = (int) itor.next();
            beanDataColumn bean = (beanDataColumn) columnOIDMapping.get(oid);
            if (bean.getColumnTag().equals(tag.trim()))
            {
                return bean;
            }
        }
        return null;
    }

    @Override
    public int updateColumn(beanDataColumn bean)
    {
        beanDataColumn obean = getDataColumnByOIDNotClone(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setColumnName(bean.getColumnName());
        bNeedSave = true;
        return iDAO.OPERATE_SUCCESS;
    }

    @Override
    public int removeColumn(int oid)
    {
        beanDataColumn obean = getDataColumnByOIDNotClone(oid);
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //获取COLUMN的所在的PAGE
        int pidx = getColumnPageIndex(oid);
        beanDataPage bpage = getDataPageByIndexNotClone(pidx);
        ArrayList acs = bpage.getColumns();
        for (int i = 0; i < acs.size(); i++)
        {
            beanDataColumn bdc = (beanDataColumn) acs.get(i);
            if (bdc.getOID() == oid)
            {
                acs.remove(i);
                bNeedSave = true;
                return iDAO.OPERATE_SUCCESS;
            }
        }
        return iDAO.OBJECT_RECORD_NOTEXIST;
    }

    @Override
    public int getColumnPageIndex(int oid)
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            beanDataPage bpage = (beanDataPage) datapages.get(i);
            List lcos = bpage.getColumns();
            for (int j = 0; j < lcos.size(); j++)
            {
                beanDataColumn bcol = (beanDataColumn) lcos.get(j);
                if (bcol.getOID() == oid)
                {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public beanDataColumn getColumnByOID(int oid)
    {
        beanDataColumn bcol = getDataColumnByOIDNotClone(oid);
        if (bcol == null)
        {
            return null;
        }
        else
        {
            return (beanDataColumn) bcol.cloneMe();
        }
    }

    @Override
    public beanDataColumn getColumnByTag(String tag)
    {
        beanDataColumn bcol = getDataColumnByTagNotClone(tag);
        if (bcol == null)
        {
            return null;
        }
        else
        {
            return (beanDataColumn) bcol.cloneMe();
        }
    }

    @Override
    public int appendDataElement(iDataElement ide, int colOID)
    {
        beanDataColumn bcol = getDataColumnByOIDNotClone(colOID);
        if (bcol == null)
        {
            return iReturn.DATA_BLOCK_COLUMN_NOTEXIST;
        }
        bcol.getDatas().add(ide);
        //缓存
        elementOIDMapping.put(ide._getDataOID(), ide);
        bNeedSave = true;
        return iDAO.OPERATE_SUCCESS;
    }

    @Override
    public int updateDataElement(iDataElement ide)
    {
        int cid = getDataElementColumnOID(ide._getDataOID());
        if (cid < 0)
        {
            return iReturn.DATA_BLOCK_COLUMN_NOTEXIST;
        }
        beanDataColumn bcol = getDataColumnByOIDNotClone(cid);
        List lds = bcol.getDatas();
        for (int i = 0; i < lds.size(); i++)
        {
            iDataElement oide = (iDataElement) lds.get(i);
            if (oide._getDataOID() == ide._getDataOID())
            {
                //执行替换
                lds.remove(i);
                lds.add(i, ide);
                //缓存替换
                elementOIDMapping.put(ide._getDataOID(), ide);
                bNeedSave = true;
                return iDAO.OPERATE_SUCCESS;
            }
        }
        return iReturn.DATA_BLOCK_DATA_NOTEXIST;
    }

    @Override
    public int disableDataElement(iDataElement ide)
    {
        return updateDataElement(ide);
    }

    @Override
    public int revertDataElement(iDataElement ide)
    {
        return updateDataElement(ide);
    }

    @Override
    public int destroyDataElement(iDataElement ide)
    {
        int cid = getDataElementColumnOID(ide._getDataOID());
        if (cid < 0)
        {
            return iReturn.DATA_BLOCK_COLUMN_NOTEXIST;
        }
        beanDataColumn bcol = getDataColumnByOIDNotClone(cid);
        List lds = bcol.getDatas();
        for (int i = 0; i < lds.size(); i++)
        {
            iDataElement oide = (iDataElement) lds.get(i);
            if (oide._getDataOID() == ide._getDataOID())
            {
                //执行删除
                elementOIDMapping.remove(ide._getDataOID());
                lds.remove(i);
                bNeedSave = true;
                return iDAO.OPERATE_SUCCESS;
            }
        }
        return iReturn.DATA_BLOCK_DATA_NOTEXIST;
    }

    @Override
    public List getDataElementList(int colID, boolean showAll)
    {
        beanDataColumn bean = getDataColumnByOIDNotClone(colID);
        if (bean == null)
        {
            return null;
        }
        List lds = bean.getDatas();
        ArrayList arl = new ArrayList();
        for (int i = 0; i < lds.size(); i++)
        {
            if (showAll == false)
            {
                iDataElement ide = (iDataElement) lds.get(i);
                if (ide._getStatus() != iDAO.OBJECT_STATE_ACTIVE)
                {
                    continue;
                }
            }
            iClone iclo = (iClone) lds.get(i);
            arl.add(iclo.cloneMe());
        }
        return arl;
    }

    @Override
    public int createElementOID()
    {
        Random r = new Random();
        while (true)
        {
            int oid = r.nextInt();
            if (oid <= 0)
            {
                continue;
            }
            if (elementOIDMapping.containsKey(oid) == false)
            {
                return oid;
            }
        }
    }

    @Override
    public int getDataElementColumnOID(int oid)
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            beanDataPage bdp = (beanDataPage) datapages.get(i);
            List lcs = bdp.getColumns();
            for (int j = 0; j < lcs.size(); j++)
            {
                beanDataColumn bdc = (beanDataColumn) lcs.get(j);
                List les = bdc.getDatas();
                for (int k = 0; k < les.size(); k++)
                {
                    iDataElement ide = (iDataElement) les.get(k);
                    if (ide._getDataOID() == oid)
                    {
                        return bdc.getOID();
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public iDataElement getDataElementByOID(int oid)
    {
        iDataElement ide = (iDataElement) elementOIDMapping.get(oid);
        iClone iclo = (iClone) ide;
        return (iDataElement) iclo.cloneMe();
    }

    private iDataElement getDataElementByOIDNotClone(int oid)
    {
        iDataElement ide = (iDataElement) elementOIDMapping.get(oid);
        return ide;
    }

    @Override
    public int appendDataPage(beanDataPage bean)
    {
        if (getDataPageByTagNotClone(bean.getPageTag()) != null)
        {
            return -iReturn.BEAN_TAG_REPEAT;
        }
        datapages.add(bean);
        bNeedSave = true;
        return datapages.size() - 1;
    }

    @Override
    public List getAllDataElements(boolean showAll)
    {
        ArrayList aes = new ArrayList();
        for (int i = 0; i < datapages.size(); i++)
        {
            beanDataPage bdp = (beanDataPage) datapages.get(i);
            List lcs = bdp.getColumns();
            for (int j = 0; j < lcs.size(); j++)
            {
                beanDataColumn bdc = (beanDataColumn) lcs.get(j);
                List lds = bdc.getDatas();
                for (int k = 0; k < lds.size(); k++)
                {
                    iDataElement ide = (iDataElement) lds.get(k);
                    if (showAll == false)
                    {
                        if (ide._getStatus() == iDAO.OBJECT_STATE_ACTIVE)
                        {
                            iClone ico = (iClone) ide;
                            aes.add(ico.cloneMe());
                        }
                    }
                    else
                    {
                        iClone ico = (iClone) ide;
                        aes.add(ico.cloneMe());
                    }
                }
            }
        }
        return aes;
    }

    @Override
    public boolean isNeedSave()
    {
        return bNeedSave;
    }

    @Override
    public void resetSave()
    {
        bNeedSave = false;
    }

    @Override
    public void save()
    {

    }

    @Override
    public boolean moveColumnUp(int oid)
    {
        int cidx = getColumnIndex(oid);
        if (cidx < 0)
        {
            return false;
        }
        if (cidx == 0)
        {
            return false;
        }
        int pgidx = getColumnPageIndex(oid);
        beanDataPage bpage = getDataPageByIndexNotClone(pgidx);
        List lcs = bpage.getColumns();
        beanDataColumn bpre = (beanDataColumn) lcs.get(cidx - 1);
        lcs.remove(cidx - 1);
        lcs.add(cidx, bpre);
        bNeedSave = true;
        return true;
    }

    private int getColumnIndex(int oid)
    {
        int pgidx = getColumnPageIndex(oid);
        beanDataPage bpage = getDataPageByIndexNotClone(pgidx);
        List lcs = bpage.getColumns();
        for (int i = 0; i < lcs.size(); i++)
        {
            beanDataColumn bdc = (beanDataColumn) lcs.get(i);
            if (bdc.getOID() == oid)
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean moveColumnDown(int oid)
    {
        int cidx = getColumnIndex(oid);
        if (cidx < 0)
        {
            return false;
        }
        int pgidx = getColumnPageIndex(oid);
        beanDataPage bpage = getDataPageByIndexNotClone(pgidx);
        List lcs = bpage.getColumns();
        if (cidx == lcs.size() - 1)
        {
            return false;
        }
        beanDataColumn bcol = (beanDataColumn) lcs.get(cidx);
        lcs.remove(cidx);
        lcs.add(cidx + 1, bcol);
        bNeedSave = true;
        return true;
    }

    private int getDataElementIndex(int oid)
    {
        int colid = getDataElementColumnOID(oid);
        if (colid < 0)
        {
            return -1;
        }
        beanDataColumn bcol = getDataColumnByOIDNotClone(colid);
        List lds = bcol.getDatas();
        for (int i = 0; i < lds.size(); i++)
        {
            iDataElement ide = (iDataElement) lds.get(i);
            if (ide._getDataOID() == oid)
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean moveDataUp(int oid)
    {
        //取得DEIDX 
        int deidx = getDataElementIndex(oid);
        if (deidx < 0)
        {
            return false;
        }
        if (deidx == 0)
        {
            return false;
        }
        int coid = getDataElementColumnOID(oid);
        beanDataColumn bcol = getDataColumnByOIDNotClone(coid);
        List ldes = bcol.getDatas();
        Object opre = ldes.get(deidx - 1);
        ldes.remove(deidx - 1);
        ldes.add(deidx, opre);
        bNeedSave = true;
        return true;
    }

    @Override
    public boolean moveDataDown(int oid)
    {
        int deidx = getDataElementIndex(oid);
        if (deidx < 0)
        {
            return false;
        }
        int coid = getDataElementColumnOID(oid);
        beanDataColumn bcol = getDataColumnByOIDNotClone(coid);
        List ldes = bcol.getDatas();
        if (deidx == ldes.size() - 1)
        {
            return false;
        }
        Object ode = ldes.get(deidx);
        ldes.remove(deidx);
        ldes.add(deidx + 1, ode);
        bNeedSave = true;
        return true;
    }

    @Override
    public boolean moveDataToColumn(int oid, int colOID)
    {
        //获取到IDE接口
        iDataElement ide = getDataElementByOIDNotClone(oid);
        //获取原COL
        int oriCoid = getDataElementColumnOID(oid);
        if (oriCoid < 0)
        {
            return false;
        }
        //取得目标COLOID对应的COL
        beanDataColumn btarCol = getDataColumnByOIDNotClone(colOID);
        if (btarCol == null)
        {
            return false;
        }
        int ididx = getDataElementIndex(oid);
        beanDataColumn oriCol = getDataColumnByOIDNotClone(oriCoid);
        List lods = oriCol.getDatas();
        lods.remove(ididx);
        //放置IDE到新的COL
        btarCol.getDatas().add(ide);
        bNeedSave = true;
        return true;
    }

    @Override
    public boolean moveColumnToPage(int oid, int pgidx)
    {
        //获取原COL
        beanDataColumn oriCol = getDataColumnByOIDNotClone(oid);
        if (oriCol == null)
        {
            return false;
        }
        beanDataPage bpage = getDataPageByIndexNotClone(pgidx);
        if (bpage == null)
        {
            return false;
        }
        int opidx = getColumnPageIndex(oid);
        int ocidx = getColumnIndex(oid);
        beanDataPage oriPage = getDataPageByIndexNotClone(opidx);
        oriPage.getColumns().remove(ocidx);
        //放置到新的页
        bpage.getColumns().add(oriCol);
        bNeedSave = true;
        return true;
    }

    @Override
    public boolean movePageUp(int idx)
    {
        if (idx <= 0)
        {
            return false;
        }
        beanDataPage dpre = (beanDataPage) datapages.get(idx - 1);
        datapages.remove(idx - 1);
        datapages.add(idx, dpre);
        bNeedSave = true;
        return true;
    }

    @Override
    public boolean movePageDown(int idx)
    {
        if (idx < 0)
        {
            return false;
        }
        if (idx >= datapages.size() - 1)
        {
            return false;
        }
        beanDataPage bpage = (beanDataPage) datapages.get(idx);
        datapages.remove(idx);
        datapages.add(idx + 1, bpage);
        bNeedSave = true;
        return true;
    }

}

//
//LOG
//TIME:
//REC:
//
