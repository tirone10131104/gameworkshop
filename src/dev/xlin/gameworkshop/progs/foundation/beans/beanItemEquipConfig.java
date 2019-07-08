package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.gameworkshop.progs.foundation.foundationUtils;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXMLNode;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iClone;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDateElementCleanValue;
import dev.xlin.tols.xml.xmlRight;
import org.w3c.dom.Element;

public class beanItemEquipConfig implements iDataElement, iClone, iAdtXMLNode, iDateElementCleanValue
{

    private int OID = 0;
    private int equipType = 0;
    private int slotType = 0;
    private int slotIndex = 0;
    private int slotQuantity = 0;
    private String slotName = "";
    private String slotDesp = "";
    private int limitTarget = 0;
    private String limitTargetOIDS = "";
    private int status = 0;

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("OID", OID + "");
        e.setAttribute("equipType", equipType + "");
        e.setAttribute("slotType", slotType + "");
        e.setAttribute("slotIndex", slotIndex + "");
        e.setAttribute("slotQuantity", slotQuantity + "");
        e.setAttribute("slotName", slotName);
        e.setAttribute("slotDesp", slotDesp);
        e.setAttribute("limitTarget", limitTarget + "");
        e.setAttribute("limitTargetOIDS", limitTargetOIDS);
        e.setAttribute("status", status + "");
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try
        {
            OID = foundationUtils.readElementValueAsInt(e, "OID");
            equipType = foundationUtils.readElementValueAsInt(e, "equipType");
            slotType = foundationUtils.readElementValueAsInt(e, "slotType");
            slotIndex = foundationUtils.readElementValueAsInt(e, "slotIndex");
            slotQuantity = foundationUtils.readElementValueAsInt(e, "slotQuantity");
            slotName = e.getAttribute("slotName");
            slotDesp = e.getAttribute("slotDesp");
            limitTarget = foundationUtils.readElementValueAsInt(e, "limitTarget");
            limitTargetOIDS = e.getAttribute("limitTargetOIDS");
            status = foundationUtils.readElementValueAsInt(e, "status");
            return true;
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return false;
        }
    }

    @Override
    public String _getDataTitle()
    {
        return "物体装配数据";
    }

    @Override
    public int _getDataOID()
    {
        return getOID();
    }

    @Override
    public int _getStatus()
    {
        return getStatus();
    }

    @Override
    public Object cloneMe()
    {
        beanItemEquipConfig bean = new beanItemEquipConfig();
        bean.setEquipType(equipType);
        bean.setLimitTarget(limitTarget);
        bean.setLimitTargetOIDS(limitTargetOIDS);
        bean.setOID(OID);
        bean.setSlotDesp(slotDesp);
        bean.setSlotIndex(slotIndex);
        bean.setSlotName(slotName);
        bean.setSlotQuantity(slotQuantity);
        bean.setSlotType(slotType);
        bean.setStatus(status);
        return bean;
    }

    @Override
    public String _getXmlNodeName()
    {
        return "SYS_I_E_C";
    }

    @Override
    public void _setValueClear()
    {
        return;
    }

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public int getSlotType()
    {
        return slotType;
    }

    public void setSlotType(int slotType)
    {
        this.slotType = slotType;
    }

    public int getSlotIndex()
    {
        return slotIndex;
    }

    public void setSlotIndex(int slotIndex)
    {
        this.slotIndex = slotIndex;
    }

    public int getSlotQuantity()
    {
        return slotQuantity;
    }

    public void setSlotQuantity(int slotQuantity)
    {
        this.slotQuantity = slotQuantity;
    }

    public String getSlotName()
    {
        return slotName;
    }

    public void setSlotName(String slotName)
    {
        this.slotName = slotName;
    }

    public String getSlotDesp()
    {
        return slotDesp;
    }

    public void setSlotDesp(String slotDesp)
    {
        this.slotDesp = slotDesp;
    }

    public int getLimitTarget()
    {
        return limitTarget;
    }

    public void setLimitTarget(int limitTarget)
    {
        this.limitTarget = limitTarget;
    }

    public String getLimitTargetOIDS()
    {
        return limitTargetOIDS;
    }

    public void setLimitTargetOIDS(String limitTargetOIDS)
    {
        this.limitTargetOIDS = limitTargetOIDS;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getEquipType()
    {
        return equipType;
    }

    public void setEquipType(int equipType)
    {
        this.equipType = equipType;
    }

}
