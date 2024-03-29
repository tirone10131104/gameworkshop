package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanBuildListItemData;
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
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import dev.xlin.tools.constChk;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class buildListData implements iDataPageOper, iDataColumnOper, iDataElementOper, iAdtXML, iAdtDocumentSave, iDatablockFace, iDatablockInitInstance, iDatablockClearValue, iDatablockCopy
{

    private wakeup up = null;
    private session sn = null;
    private ArrayList datapages = new ArrayList();
    private dataStructOperator DSO = null;

    @Override
    public int appendDataElement(iDataElement ide, int colOID)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        BeanBuildListItemData bean = (BeanBuildListItemData) ide;
        int r0 = doCheckBeanLogic(bean);
        if (r0 != 0)
        {
            return r0;
        }
        int oid = DSO.createElementOID();
        bean.setOID(oid);
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        return DSO.appendDataElement(bean, colOID);
    }

    @Override
    public int updateDataElement(iDataElement ide)
    {
        BeanBuildListItemData bean = (BeanBuildListItemData) ide;
        BeanBuildListItemData obean = (BeanBuildListItemData) getDataElementByOID(bean.getOID());
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
        obean.setQuantity(bean.getQuantity());
        obean.setRequestType(bean.getRequestType());
        obean.setTargetOID(bean.getTargetOID());
        obean.setTargetType(bean.getTargetType());
        return DSO.updateDataElement(obean);
    }

    private int doCheckBeanLogic(BeanBuildListItemData bean)
    {
        boolean b00 = constChk.isConst(iConst.class, "TARGET_REQ_METD_", bean.getRequestType());
        if (b00 == false)
        {
            return 1;
        }
        if (bean.getQuantity() < 0)
        {
            return 2;
        }
        return 0;
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
        for (int i = 0; i < datapages.size(); i++)
        {
            beanDataPage bdp = (beanDataPage) datapages.get(i);
            Element epage = bdp.transToXmlElement(xr);
            erot.appendChild(epage);
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
            Document doc = xr.getDocument();
            NodeList rls = doc.getChildNodes();
            Element erot = (Element) rls.item(0);
            NodeList nls = erot.getChildNodes();
            for (int i = 0; i < nls.getLength(); i++)
            {
                Element e = (Element) nls.item(i);
                beanDataPage bpage = new beanDataPage();
                boolean b = bpage.revertFromXmlElement(e);
                if (b)
                {
                    datapages.add(bpage);
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
        return "SYS_DB_BUILD_DATA_LIST";
    }

    @Override
    public String getDatablockName()
    {
        return "制造数据列表";
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
        iAdtXML iax = new buildListData();
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
