package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemEquipConfig;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXML;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElementOper;
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

/**
 *
 * @author Administrator
 */
public class itemEquipConfigList implements iDataElementOper, iAdtXML, iAdtDocumentSave, iDatablockFace, iDatablockInitInstance, iDatablockClearValue, iDatablockCopy
{

    private wakeup up = null;
    private session sn = null;
    private ArrayList datapages = new ArrayList();
    private boolean saveFlag = false;

    public itemEquipConfigList()
    {

    }

    @Override
    public void initDatablcok(wakeup _up)
    {
        up = _up;
        sn = new session(up);
        datapages = new ArrayList();
    }

    @Override
    public int appendDataElement(iDataElement ide, int colOID)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        BeanItemEquipConfig bean = (BeanItemEquipConfig) ide;
        if (findConfigByKeys(bean.getEquipType(), bean.getSlotType(), bean.getSlotIndex()) != null)
        {
            return iReturn.IEQCFG_SLOT_CONFIG_REPEAT;
        }
        BeanItemEquipConfig biec = (BeanItemEquipConfig) ide;
        int oid = createElementOID();
        biec.setOID(oid);
        biec.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        datapages.add(biec);
        saveFlag = true;
        return iDAO.OPERATE_SUCCESS;
    }

    public BeanItemEquipConfig findConfigByKeys(int ieqp, int isltp, int slix)
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            BeanItemEquipConfig biec = (BeanItemEquipConfig) datapages.get(i);
            if (biec.getEquipType() == ieqp && biec.getSlotType() == isltp && biec.getSlotIndex() == slix)
            {
                return biec;
            }
        }
        return null;
    }

    @Override
    public int updateDataElement(iDataElement ide)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        BeanItemEquipConfig bean = (BeanItemEquipConfig) ide;
        BeanItemEquipConfig obean = (BeanItemEquipConfig) getItemEquipConfigNotClone(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setLimitTarget(bean.getLimitTarget());
        obean.setLimitTargetOIDS(bean.getLimitTargetOIDS());
        obean.setSlotDesp(bean.getSlotDesp());
        obean.setSlotName(bean.getSlotName());
        obean.setSlotQuantity(bean.getSlotQuantity());
        saveFlag = true;
        return iDAO.OPERATE_SUCCESS;
    }

    @Override
    public int disableDataElement(iDataElement ide)
    {
        BeanItemEquipConfig obean = getItemEquipConfigNotClone(ide._getDataOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setStatus(iDAO.OBJECT_STATE_DELETE);
        saveFlag = true;
        return iDAO.OPERATE_SUCCESS;
    }

    @Override
    public int revertDataElement(iDataElement ide)
    {
        BeanItemEquipConfig obean = getItemEquipConfigNotClone(ide._getDataOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        saveFlag = true;
        return iDAO.OPERATE_SUCCESS;
    }

    @Override
    public int destroyDataElement(iDataElement ide)
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            BeanItemEquipConfig bies = (BeanItemEquipConfig) datapages.get(i);
            if (bies.getOID() == ide._getDataOID())
            {
                if (bies.getStatus() != iDAO.OBJECT_STATE_DELETE)
                {
                    return iReturn.BEAN_NOT_READY_DESTROY;
                }
                datapages.remove(i);
                saveFlag = true;
                return iDAO.OPERATE_SUCCESS;
            }
        }
        return iDAO.OBJECT_RECORD_NOTEXIST;
    }

    @Override
    public List getDataElementList(int colID, boolean showAll)
    {
        return getAllDataElements(showAll);
    }

    @Override
    public int createElementOID()
    {
        Random r = new Random();
        while (1 > 0)
        {
            int ix = r.nextInt();
            if (ix <= 0)
            {
                continue;
            }
            if (getDataElementByOID(ix) == null)
            {
                return ix;
            }
        }
    }

    @Override
    public int getDataElementColumnOID(int oid)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private BeanItemEquipConfig getItemEquipConfigNotClone(int oid)
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            BeanItemEquipConfig biec = (BeanItemEquipConfig) datapages.get(i);
            if (biec.getOID() == oid)
            {
                return biec;
            }
        }
        return null;
    }

    @Override
    public iDataElement getDataElementByOID(int oid)
    {
        BeanItemEquipConfig bean = getItemEquipConfigNotClone(oid);
        if (bean == null)
        {
            return null;
        }
        return (iDataElement) bean.cloneMe();
    }

    @Override
    public List getAllDataElements(boolean showAll)
    {
        ArrayList arl = new ArrayList();
        for (int i = 0; i < datapages.size(); i++)
        {
            BeanItemEquipConfig biec = (BeanItemEquipConfig) datapages.get(i);
            if (showAll == false)
            {
                if (biec.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
                {
                    continue;
                }
            }
            BeanItemEquipConfig bcopy = (BeanItemEquipConfig) biec.cloneMe();
            arl.add(bcopy);
        }
        return arl;
    }

    public int getIndexOfEquipConfig(int oid)
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            BeanItemEquipConfig biec = (BeanItemEquipConfig) datapages.get(i);
            if (biec.getOID() == oid)
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean moveDataUp(int oid)
    {
        int idx = getIndexOfEquipConfig(oid);
        if (idx <= 0)
        {
            return false;
        }
        BeanItemEquipConfig bpre = (BeanItemEquipConfig) datapages.get(idx - 1);
        datapages.remove(idx - 1);
        datapages.add(idx, bpre);
        saveFlag = true;
        return true;
    }

    @Override
    public boolean moveDataDown(int oid)
    {
        int idx = getIndexOfEquipConfig(oid);
        if (idx < 0)
        {
            return false;
        }
        if (idx == datapages.size() - 1)
        {
            return false;
        }
        BeanItemEquipConfig bean = (BeanItemEquipConfig) datapages.get(idx);
        datapages.remove(idx);
        datapages.add(idx + 1, bean);
        saveFlag = true;
        return true;
    }

    @Override
    public boolean moveDataToColumn(int oid, int colOID)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            BeanItemEquipConfig bean = (BeanItemEquipConfig) datapages.get(i);
            Element ebean = bean.transToXmlElement(xr);
            erot.appendChild(ebean);
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
                BeanItemEquipConfig bean = new BeanItemEquipConfig();
                boolean b = bean.revertFromXmlElement(e);
                if (b)
                {
                    datapages.add(bean);
                }
            }
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
        return saveFlag;
    }

    @Override
    public void resetSave()
    {
        saveFlag = false;
    }

    @Override
    public void save()
    {

    }

    @Override
    public String getDatablockServiceTag()
    {
        return "SYS_DB_EQUIP_CONFIG_LIST";
    }

    @Override
    public String getDatablockName()
    {
        return "装配设置数据";
    }
 
    @Override
    public void setAllValueClear()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object datablockCopy()
    {
        xmlRight xr = transToXML();
        iAdtXML iax = new itemEquipConfigList();
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
