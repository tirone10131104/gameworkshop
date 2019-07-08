package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanRecycleListItemData;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanRecycleRequestListData;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXML;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataColumnOper;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElementOper;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataPageOper;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockClearValue;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockCopy;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockInitInstance;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class recycleListData implements iDataPageOper, iDataColumnOper, iDataElementOper, iAdtXML, iAdtDocumentSave, iDatablockFace, iDatablockInitInstance, iDatablockClearValue, iDatablockCopy
{

    private wakeup up = null;
    private session sn = null;
    private ArrayList recyReqs = new ArrayList();
    private ArrayList datapages = new ArrayList();
    private dataStructOperator DSO = null;

    public int appendRecycleRequestItem(BeanRecycleRequestListData brld)
    {
        if (brld == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        int rch = checkRecycleRequestBean(brld);
        if (rch != 0)
        {
            return rch;
        }
        int oid = createRROID();
        brld.setOID(oid);
        brld.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        recyReqs.add(brld);
        return iDAO.OPERATE_SUCCESS;
    }

    public int updateRecycleRequestItem(BeanRecycleRequestListData bean)
    {
        if (bean == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        BeanRecycleRequestListData obean = getRecycleRequestNotClone(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (obean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setDescription(bean.getDescription());
        obean.setHide(bean.getHide());
        obean.setRequestMethod(bean.getRequestMethod());
        obean.setQuantity(bean.getQuantity());
        return iDAO.OPERATE_SUCCESS;
    }

    public int disableRecycleRequestItem(int oid)
    {
        BeanRecycleRequestListData obean = getRecycleRequestNotClone(oid);
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setStatus(iDAO.OBJECT_STATE_DELETE);
        return iDAO.OPERATE_SUCCESS;
    }

    public int revertRecycleRequestItem(int oid)
    {
        BeanRecycleRequestListData obean = getRecycleRequestNotClone(oid);
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        return iDAO.OPERATE_SUCCESS;
    }

    public int destroyRecycleRequestItem(int oid)
    {
        BeanRecycleRequestListData obean = getRecycleRequestNotClone(oid);
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (obean.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_NOT_READY_DESTROY;
        }
        int idx = getIndexOfRequestData(oid);
        recyReqs.remove(idx);
        return iDAO.OPERATE_SUCCESS;
    }

    public int moveRecycleRequestUp(int oid)
    {
        int idx = getIndexOfRequestData(oid);
        if (idx == 0)
        {
            return iReturn.BEAN_CANT_MOVE_UP;
        }
        BeanRecycleRequestListData bpre = (BeanRecycleRequestListData) recyReqs.get(idx - 1);
        recyReqs.remove(idx - 1);
        recyReqs.add(idx, bpre);
        return iDAO.OPERATE_SUCCESS;
    }

    public int moveRecycleRequestDown(int oid)
    {
        int idx = getIndexOfRequestData(oid);
        if (idx == recyReqs.size() - 1)
        {
            return iReturn.BEAN_CANT_MOVE_DOWN;
        }
        BeanRecycleRequestListData blast = (BeanRecycleRequestListData) recyReqs.get(idx + 1);
        recyReqs.remove(idx + 1);
        recyReqs.add(idx, blast);
        return iDAO.OPERATE_SUCCESS;
    }

    public List getRecycleRequestList(boolean showAll, int tartp)
    {
        ArrayList arl = new ArrayList();
        for (int i = 0; i < recyReqs.size(); i++)
        {
            BeanRecycleRequestListData bean = (BeanRecycleRequestListData) recyReqs.get(i);
            if (showAll == false && bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
            {
                continue;
            }
            if (tartp != 0 && bean.getTargetType() != tartp)
            {
                continue;
            }
            arl.add(bean.cloneMe());
        }
        return arl;
    }

    private int getIndexOfRequestData(int oid)
    {
        for (int i = 0; i < recyReqs.size(); i++)
        {
            BeanRecycleRequestListData brr = (BeanRecycleRequestListData) recyReqs.get(i);
            if (brr.getOID() == oid)
            {
                return i;
            }
        }
        return -1;
    }

    private int checkRecycleRequestBean(BeanRecycleRequestListData bean)
    {
        if (bean.getQuantity() < 0)
        {
            return 1;
        }
        return 0;
    }

    private BeanRecycleRequestListData getRecycleRequestNotClone(int oid)
    {
        for (int i = 0; i < recyReqs.size(); i++)
        {
            BeanRecycleRequestListData bri = (BeanRecycleRequestListData) recyReqs.get(i);
            if (bri.getOID() == oid)
            {
                return bri;
            }
        }
        return null;
    }

    public BeanRecycleRequestListData getRecycleRequestListItemByOID(int oid)
    {
        BeanRecycleRequestListData brld = getRecycleRequestNotClone(oid);
        if (brld == null)
        {
            return null;
        }
        return (BeanRecycleRequestListData) brld.cloneMe();
    }

    private int createRROID()
    {
        Random r = new Random();
        while (true)
        {
            int i = r.nextInt();
            if (i <= 0)
            {
                continue;
            }
            if (getRecycleRequestNotClone(i) == null)
            {
                return i;
            }
        }
    }

    @Override
    public int appendDataElement(iDataElement ide, int colOID)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        BeanRecycleListItemData bean = (BeanRecycleListItemData) ide;
        //逻辑检查
        int r0 = doCheckBeanLogic(bean);
        if (r0 != 0)
        {
            return r0;
        }
        int oid = DSO.createElementOID();
        bean.setOID(oid);
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        return DSO.appendDataElement(ide, colOID);
    }

    private int doCheckBeanLogic(BeanRecycleListItemData bean)
    {
        if (bean.getQuantity() < 0)
        {
            return iReturn.RECY_QUANTITY_ERROR;
        }
        itemDefine idef = new itemDefine(up);
        BeanItem bit = (BeanItem) idef.getRecordByID(bean.getItemOID());
        if (idef.checkBean(bit) == false)
        {
            return iReturn.RECY_ITEM_NOTEXIST;
        }
        return 0;
    }

    @Override
    public int updateDataElement(iDataElement ide)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }

        BeanRecycleListItemData bean = (BeanRecycleListItemData) ide;
        BeanRecycleListItemData obean = (BeanRecycleListItemData) getDataElementByOID(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        int r0 = doCheckBeanLogic(bean);
        if (r0 != 0)
        {
            return r0;
        }
        obean.setDescription(bean.getDescription());
        obean.setHide(bean.getHide());
        obean.setItemOID(bean.getItemOID());
        obean.setQuantity(bean.getQuantity());
        return DSO.updateDataElement(obean);
    }

    @Override
    public List getPageList()
    {
        return DSO.getPageList();
    }

    @Override
    public beanDataPage getDataPageByIndex(int idx)
    {
        return DSO.getDataPageByIndex(idx);
    }

    @Override
    public beanDataPage getDataPageByTag(String tag)
    {
        return DSO.getDataPageByTag(tag);
    }

    @Override
    public int appendDataPage(beanDataPage bean)
    {
        return DSO.appendDataPage(bean);
    }

    @Override
    public int updatePage(String name, int idx)
    {
        return DSO.updatePage(name, idx);
    }

    @Override
    public int getPageIndex(String tag)
    {
        return DSO.getPageIndex(tag);
    }

    @Override
    public String getPageTag(int index)
    {
        return DSO.getPageTag(index);
    }

    @Override
    public boolean removePageByIndex(int index)
    {
        return DSO.removePageByIndex(index);
    }

    @Override
    public boolean removePageByTag(String stg)
    {
        return DSO.removePageByTag(stg);
    }

    @Override
    public boolean movePageUp(int idx)
    {
        return DSO.movePageUp(idx);
    }

    @Override
    public boolean movePageDown(int idx)
    {
        return DSO.movePageDown(idx);
    }

    @Override
    public int appendColumn(beanDataColumn bean, int pageIdx)
    {
        return DSO.appendColumn(bean, pageIdx);
    }

    @Override
    public int updateColumn(beanDataColumn bean)
    {
        return DSO.updateColumn(bean);
    }

    @Override
    public int removeColumn(int oid)
    {
        return DSO.removeColumn(oid);
    }

    @Override
    public List getColumnListByPageIndex(int pgidx)
    {
        return DSO.getColumnListByPageIndex(pgidx);
    }

    @Override
    public List getColumnListByPageTag(String tag)
    {
        return DSO.getColumnListByPageTag(tag);
    }

    @Override
    public beanDataColumn getColumnByOID(int oid)
    {
        return DSO.getColumnByOID(oid);
    }

    @Override
    public beanDataColumn getColumnByTag(String tag)
    {
        return DSO.getColumnByTag(tag);
    }

    @Override
    public boolean moveColumnUp(int oid)
    {
        return DSO.moveColumnUp(oid);
    }

    @Override
    public boolean moveColumnDown(int oid)
    {
        return DSO.moveColumnDown(oid);
    }

    @Override
    public boolean moveColumnToPage(int oid, int pgidx)
    {
        return DSO.moveColumnToPage(oid, pgidx);
    }

    @Override
    public int getColumnPageIndex(int oid)
    {
        return DSO.getColumnPageIndex(oid);
    }

    @Override
    public int disableDataElement(iDataElement ide)
    {
        return DSO.disableDataElement(ide);
    }

    @Override
    public int revertDataElement(iDataElement ide)
    {
        return DSO.revertDataElement(ide);
    }

    @Override
    public int destroyDataElement(iDataElement ide)
    {
        return DSO.destroyDataElement(ide);
    }

    @Override
    public List getDataElementList(int colID, boolean showAll)
    {
        return DSO.getDataElementList(colID, showAll);
    }

    @Override
    public int createElementOID()
    {
        return DSO.createElementOID();
    }

    @Override
    public int getDataElementColumnOID(int oid)
    {
        return DSO.getDataElementColumnOID(oid);
    }

    @Override
    public iDataElement getDataElementByOID(int oid)
    {
        return DSO.getDataElementByOID(oid);
    }

    @Override
    public List getAllDataElements(boolean showAll)
    {
        return DSO.getAllDataElements(showAll);
    }

    @Override
    public boolean moveDataUp(int oid)
    {
        return DSO.moveDataUp(oid);
    }

    @Override
    public boolean moveDataDown(int oid)
    {
        return DSO.moveDataDown(oid);
    }

    @Override
    public boolean moveDataToColumn(int oid, int colOID)
    {
        return DSO.moveDataToColumn(oid, colOID);
    }

    @Override
    public xmlRight transToXML()
    {
        xmlRight xr = new xmlRight();
        xr.createXML();
        Document doc = xr.getDocument();
        Element erot = xr.createElement("ROOT");
        //拆解主数据
        for (int i = 0; i < datapages.size(); i++)
        {
            beanDataPage bdp = (beanDataPage) datapages.get(i);
            Element epage = bdp.transToXmlElement(xr);
            erot.appendChild(epage);
        }
        //拆解需求数据
        for (int i = 0; i < recyReqs.size(); i++)
        {
            BeanRecycleRequestListData bean = (BeanRecycleRequestListData) recyReqs.get(i);
            Element erq = bean.transToXmlElement(xr);
            erot.appendChild(erq);
        }
        doc.appendChild(erot);
        return xr;
    }

    @Override
    public boolean revertFromXML(xmlRight xr)
    {
        try
        {
            datapages.clear();
            recyReqs.clear();
            Document doc = xr.getDocument();
            NodeList rls = doc.getChildNodes();
            Element erot = (Element) rls.item(0);
            NodeList nls = erot.getChildNodes();
            for (int i = 0; i < nls.getLength(); i++)
            {
                Element e = (Element) nls.item(i);
                if (e.getNodeName().equals("B_PAGE"))
                {
                    beanDataPage bpage = new beanDataPage();
                    boolean b = bpage.revertFromXmlElement(e);
                    if (b)
                    {
                        datapages.add(bpage);
                    }
                }
                else if (e.getNodeName().equals("RECY_REQ_LIDT"))
                {
                    BeanRecycleRequestListData brrld = new BeanRecycleRequestListData();
                    boolean b = brrld.revertFromXmlElement(e);
                    if (b)
                    {
                        recyReqs.add(brrld);
                    }
                }
            }
            DSO = new dataStructOperator(datapages);
            DSO.rebuildAllMapping();
            return true;
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isNeedSave()
    {
        return DSO.isNeedSave();
    }

    @Override
    public void resetSave()
    {
        DSO.resetSave();
    }

    @Override
    public void save()
    {

    }

    @Override
    public String getDatablockServiceTag()
    {
        return "SYS_DB_RECYCLE_DATA_LIST";
    }

    @Override
    public String getDatablockName()
    {
        return "回收数据列表";
    }

    @Override
    public void initDatablcok(wakeup _up)
    {
        up = _up;
        sn = new session(up);
        createData();
    }

    public void createData()
    {
        datapages.clear();
        DSO = new dataStructOperator(datapages);
    }

    @Override
    public void setAllValueClear()
    {
        DSO.setAllValueClear();
    }

    @Override
    public Object datablockCopy()
    {
        xmlRight xr = transToXML();
        iAdtXML iax = new recycleListData();
        iDatablockInitInstance idi = (iDatablockInitInstance) iax;
        idi.initDatablcok(up);
        boolean b = iax.revertFromXML(xr);
        if (b)
        {
            return iax;
        }
        else
        {
            return null;
        }
    }
}
