package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.beanPropertyData;
import dev.xlin.gameworkshop.progs.foundation.beans.beanPropertyDefine;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author 刘祎鹏
 */
public class propertyListData implements iDataPageOper, iDataColumnOper, iDataElementOper, iAdtXML, iAdtDocumentSave, iDatablockFace, iDatablockInitInstance, iDatablockClearValue, iDatablockCopy
{

    private wakeup up = null;
    private session sn = null;
    private ArrayList datapages = new ArrayList();
    private dataStructOperator DSO = null;

    @Override
    public void initDatablcok(wakeup _up)
    {
        up = _up;
        sn = new session(up);
        createData();
    }

    public propertyListData()
    {

    }

    public void createData()
    {
        datapages.clear();
        DSO = new dataStructOperator(datapages);
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
    public int appendDataElement(iDataElement ide, int colOID)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        beanPropertyData bpd = (beanPropertyData) ide;
        //检测基本逻辑
        //检查属性是否存在
        propertyDefine pdef = new propertyDefine(up);
        beanPropertyDefine bpdef = (beanPropertyDefine) pdef.getRecordByID(bpd.getPropID());
        if (pdef.checkBean(bpdef) == false)
        {
            return iReturn.PROPERTY_DEF_NOT_EXIST;
        }
        //检查属性是否已经使用
        beanPropertyData obean = getPropertyDataByPropID(bpd.getPropID());
        if (obean != null)
        {
            return iReturn.PROPERTY_DATA_REPEAT;
        }
        bpd.setDatatype(bpdef.getDataType());
        if (bpd.getPropName().trim().equals(""))
        {
            bpd.setPropName(bpdef.getPropName());
        }
        bpd.setPropTag(bpdef.getPropTag());
        bpd.setState(iDAO.OBJECT_STATE_ACTIVE);
        int oid = DSO.createElementOID();
        bpd.setOID(oid);
        return DSO.appendDataElement(ide, colOID);
    }

    public beanPropertyData getPropertyDataByPropID(int pid)
    {
        List ls = getAllDataElements(true);
        for (int i = 0; i < ls.size(); i++)
        {
            beanPropertyData bpd = (beanPropertyData) ls.get(i);
            if (bpd.getPropID() == pid)
            {
                return bpd;
            }
        }
        return null;
    }

    @Override
    public int updateDataElement(iDataElement ide)
    {
        beanPropertyData bpd = (beanPropertyData) ide;
        beanPropertyData obean = (beanPropertyData) getDataElementByOID(bpd.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setPropDescription(bpd.getPropDescription());
        obean.setPropValue(bpd.getPropValue());
        obean.setPropName(bpd.getPropName());
        obean.setHide(bpd.getHide());
        return DSO.updateDataElement(obean);
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
    public int appendDataPage(beanDataPage bean)
    {
        return DSO.appendDataPage(bean);
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
    public List getAllDataElements(boolean showAll)
    {
        return DSO.getAllDataElements(showAll);
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
        return "SYS_DB_PROPERTY_DATA_LIST";
    }

    @Override
    public String getDatablockName()
    {
        return "属性值数据";
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
    public boolean moveColumnToPage(int oid, int pgidx)
    {
        return DSO.moveColumnToPage(oid, pgidx);
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
    public int getColumnPageIndex(int oid)
    {
        return DSO.getColumnPageIndex(oid);
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
        iAdtXML iax = new propertyListData();
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

//
//LOG
//TIME:
//REC:
//
