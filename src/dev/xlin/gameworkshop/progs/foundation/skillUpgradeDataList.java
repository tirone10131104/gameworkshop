package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanSkillUpgradeCondition;
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
 * @author 刘祎鹏
 */
public class skillUpgradeDataList implements iAdtXML, iAdtDocumentSave, iDatablockFace, iDatablockInitInstance, iDatablockClearValue, iDatablockCopy, iDataElementOper
{

    private wakeup up = null;
    private session sn = null;
    private ArrayList datapages = new ArrayList();
    private boolean saveFlag = false;

    @Override
    public int appendDataElement(iDataElement ide, int colOID)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        BeanSkillUpgradeCondition bean = (BeanSkillUpgradeCondition) ide;
        bean.setOID(createElementOID());
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        datapages.add(bean);
        return iDAO.OPERATE_SUCCESS;
    }

    @Override
    public int updateDataElement(iDataElement ide)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        BeanSkillUpgradeCondition bean = (BeanSkillUpgradeCondition) ide;
        BeanSkillUpgradeCondition obean = getBeanNotClone(bean.getOID());
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
        obean.setValue(bean.getValue());
        obean.setHide(bean.getHide());
        obean.setMethod(bean.getMethod());
        return iDAO.OPERATE_SUCCESS;
    }

    @Override
    public int disableDataElement(iDataElement ide)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        BeanSkillUpgradeCondition bean = (BeanSkillUpgradeCondition) ide;
        BeanSkillUpgradeCondition obean = getBeanNotClone(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        obean.setStatus(iDAO.OBJECT_STATE_DELETE);
        return iDAO.OPERATE_SUCCESS;
    }

    @Override
    public int revertDataElement(iDataElement ide)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        BeanSkillUpgradeCondition bean = (BeanSkillUpgradeCondition) ide;
        BeanSkillUpgradeCondition obean = getBeanNotClone(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (obean.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_CANT_REVERT;
        }
        obean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        return iDAO.OPERATE_SUCCESS;
    }

    @Override
    public int destroyDataElement(iDataElement ide)
    {
        if (ide == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        BeanSkillUpgradeCondition bean = (BeanSkillUpgradeCondition) ide;
        BeanSkillUpgradeCondition obean = getBeanNotClone(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (obean.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_NOT_READY_DESTROY;
        }
        for (int i = 0; i < datapages.size(); i++)
        {
            BeanSkillUpgradeCondition bori = (BeanSkillUpgradeCondition) datapages.get(i);
            if (bori.getOID() == obean.getOID())
            {
                datapages.remove(i);
                return iDAO.OPERATE_SUCCESS;
            }
        }
        return iDAO.OPERATE_FAIL;
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
        while (true)
        {
            int i = r.nextInt();
            if (i <= 0)
            {
                continue;
            }
            if (getBeanNotClone(i) == null)
            {
                return i;
            }
        }
    }

    private BeanSkillUpgradeCondition getBeanNotClone(int oid)
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            BeanSkillUpgradeCondition bean = (BeanSkillUpgradeCondition) datapages.get(i);
            if (bean.getOID() == oid)
            {
                return bean;
            }
        }
        return null;
    }

    @Override
    public int getDataElementColumnOID(int oid)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public iDataElement getDataElementByOID(int oid)
    {
        BeanSkillUpgradeCondition bean = getBeanNotClone(oid);
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
            BeanSkillUpgradeCondition bean = (BeanSkillUpgradeCondition) datapages.get(i);
            if (showAll == false && bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
            {
                continue;
            }
            arl.add(bean.cloneMe());
        }
        return arl;
    }

    private int getIndexOf(int oid)
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            BeanSkillUpgradeCondition bean = (BeanSkillUpgradeCondition) datapages.get(i);
            if (bean.getOID() == oid)
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean moveDataUp(int oid)
    {
        int idx = getIndexOf(oid);
        if (idx <= 0)
        {
            return false;
        }
        Object opre = datapages.get(idx - 1);
        datapages.remove(idx - 1);
        datapages.add(idx, opre);
        return true;
    }

    @Override
    public boolean moveDataDown(int oid)
    {
        int idx = getIndexOf(oid);
        if (idx < 0)
        {
            return false;
        }
        if (idx == datapages.size() - 1)
        {
            return false;
        }
        Object o = datapages.get(idx);
        datapages.remove(idx);
        datapages.add(idx + 1, o);
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
        Element erot = xr.createElement("ROOT");
        for (int i = 0; i < datapages.size(); i++)
        {
            BeanSkillUpgradeCondition bean = (BeanSkillUpgradeCondition) datapages.get(i);
            Element e = bean.transToXmlElement(xr);
            erot.appendChild(e);
        }
        xr.appendElementToDoc(erot);
        return xr;
    }

    @Override
    public boolean revertFromXML(xmlRight xr)
    {
        datapages.clear();
        Element erot = xr.getDocument().getDocumentElement();
        BeanSkillUpgradeCondition b = new BeanSkillUpgradeCondition();
        NodeList nl = erot.getElementsByTagName(b._getXmlNodeName());
        for (int i = 0; i < nl.getLength(); i++)
        {
            Element e = (Element) nl.item(i);
            BeanSkillUpgradeCondition bean = new BeanSkillUpgradeCondition();
            boolean brv = bean.revertFromXmlElement(e);
            if (brv == false)
            {
                return false;
            }
            datapages.add(bean);
        }
        saveFlag = false;
        return true;
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
        return "SYS_DB_SKILL_UPGRADE_CONDITION";
    }

    @Override
    public String getDatablockName()
    {
        return "技能升级数据列表";
    }

    @Override
    public void initDatablcok(wakeup _up)
    {
        up = _up;
    }

    @Override
    public void setAllValueClear()
    {
        for (int i = 0; i < datapages.size(); i++)
        {
            BeanSkillUpgradeCondition bean = (BeanSkillUpgradeCondition) datapages.get(i);
            bean.setValue(0);
        }
    }

    @Override
    public Object datablockCopy()
    {
        xmlRight xr = transToXML();
        iAdtXML iax = new skillUpgradeDataList();
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
