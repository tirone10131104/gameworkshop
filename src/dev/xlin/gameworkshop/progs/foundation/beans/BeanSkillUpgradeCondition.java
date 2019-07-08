package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.gameworkshop.progs.foundation.foundationUtils;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXMLNode;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iClone;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDateElementCleanValue;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.tols.xml.xmlRight;
import org.w3c.dom.Element;

/**
 *
 * @author 刘祎鹏
 */
public class BeanSkillUpgradeCondition implements iClone, iAdtXMLNode, iDateElementCleanValue, iDataElement
{

    private int OID = 0;
    //升级方法(属性，键，技能，物品)
    private int targetType = iConst.DT_REQ_TARTYPE_SKILL;
    private int targetOID = 0;
    private int method = 0;
    private double value = 0;
    private int hide = 0;
    private int status = 0;
    private String description = "";

    public int getTargetType()
    {
        return targetType;
    }

    public void setTargetType(int targetType)
    {
        this.targetType = targetType;
    }

    public int getTargetOID()
    {
        return targetOID;
    }

    public void setTargetOID(int targetOID)
    {
        this.targetOID = targetOID;
    }

    public int getMethod()
    {
        return method;
    }

    public void setMethod(int method)
    {
        this.method = method;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public int getHide()
    {
        return hide;
    }

    public void setHide(int hide)
    {
        this.hide = hide;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public Object cloneMe()
    {
        BeanSkillUpgradeCondition bean = new BeanSkillUpgradeCondition();
        bean.setOID(OID);
        bean.setHide(hide);
        bean.setMethod(method);
        bean.setStatus(status);
        bean.setTargetOID(targetOID);
        bean.setTargetType(targetType);
        bean.setValue(value);
        bean.setDescription(description);
        return bean;
    }

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("OID", OID + "");
        e.setAttribute("hide", hide + "");
        e.setAttribute("method", method + "");
        e.setAttribute("status", status + "");
        e.setAttribute("targetOID", targetOID + "");
        e.setAttribute("targetType", targetType + "");
        e.setAttribute("value", value + "");
        e.setAttribute("description", description);
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try
        {
            setOID(foundationUtils.readElementValueAsInt(e, "OID"));
            setHide(foundationUtils.readElementValueAsInt(e, "hide"));
            setMethod(foundationUtils.readElementValueAsInt(e, "method"));
            setStatus(foundationUtils.readElementValueAsInt(e, "status"));
            setTargetOID(foundationUtils.readElementValueAsInt(e, "targetOID"));
            setTargetType(foundationUtils.readElementValueAsInt(e, "targetType"));
            setValue(foundationUtils.readElementValueAsDouble(e, "value"));
            setDescription(e.getAttribute("description"));
            return true;
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return false;
        }
    }

    @Override
    public String _getXmlNodeName()
    {
        return "SK_LV_UP_COND";
    }

    @Override
    public void _setValueClear()
    {
        value = 0;
    }

    @Override
    public String _getDataTitle()
    {
        return "技能升级判定";
    }

    @Override
    public int _getDataOID()
    {
        return OID;
    }

    @Override
    public int _getStatus()
    {
        return getStatus();
    }

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

}

//
//LOG
//TIME:
//REC:
//
