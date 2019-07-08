package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.gameworkshop.progs.foundation.foundationUtils;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXMLNode;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iClone;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDateElementCleanValue;
import dev.xlin.tols.xml.xmlRight;
import org.w3c.dom.Element;

/**
 *
 * @author 刘祎鹏
 */
public class BeanEffectData implements iDataElement, iClone, iAdtXMLNode, iDateElementCleanValue
{

    private int OID = 0;
    //本机，装备，或者物类，船
    private int targetType = 0;
    private int targetOID = 0;
    private int propID = 0;
    private String description = "";
    private int valueType = 0;
    private double effectValue = 0;
    private int state = 0;
    private int hide =  0;
    
    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public int getTargetType()
    {
        return targetType;
    }

    public void setTargetType(int targetType)
    {
        this.targetType = targetType;
    }

    public int getPropID()
    {
        return propID;
    }

    public void setPropID(int propID)
    {
        this.propID = propID;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getValueType()
    {
        return valueType;
    }

    public void setValueType(int valueType)
    {
        this.valueType = valueType;
    }

    public double getEffectValue()
    {
        return effectValue;
    }

    public void setEffectValue(double effectValue)
    {
        this.effectValue = effectValue;
    }

    public int getTargetOID()
    {
        return targetOID;
    }

    public void setTargetOID(int targetOID)
    {
        this.targetOID = targetOID;
    }

    @Override
    public String _getDataTitle()
    {
        return getDescription();
    }

    @Override
    public int _getDataOID()
    {
        return getOID();
    }

    @Override
    public int _getStatus()
    {
        return getState();
    }

    @Override
    public Object cloneMe()
    {
        BeanEffectData bean = new BeanEffectData();
        bean.setDescription(getDescription());
        bean.setEffectValue(getEffectValue());
        bean.setOID(getOID());
        bean.setPropID(getPropID());
        bean.setState(getState());
        bean.setTargetOID(getTargetOID());
        bean.setTargetType(getTargetType());
        bean.setValueType(getValueType());
        bean.setHide(hide);
        return bean;
    }

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("description", getDescription());
        e.setAttribute("effectValue", getEffectValue() + "");
        e.setAttribute("OID", getOID() + "");
        e.setAttribute("propID", getPropID() + "");
        e.setAttribute("state", getState() + "");
        e.setAttribute("targetOID", getTargetOID() + "");
        e.setAttribute("targetType", getTargetType() + "");
        e.setAttribute("valueType", getValueType() + "");
        e.setAttribute("hide", hide+"");
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try
        {
            setDescription(e.getAttribute("description"));
            setEffectValue(foundationUtils.readElementValueAsDouble(e, "effectValue"));
            setOID(foundationUtils.readElementValueAsInt(e, "OID"));
            setPropID(foundationUtils.readElementValueAsInt(e, "propID"));
            setState(foundationUtils.readElementValueAsInt(e, "state"));
            setTargetOID(foundationUtils.readElementValueAsInt(e, "targetOID"));
            setTargetType(foundationUtils.readElementValueAsInt(e, "targetType"));
            setValueType(foundationUtils.readElementValueAsInt(e, "valueType"));
            setHide(foundationUtils.readElementValueAsInt(e, "hide")); 
            return true;
        }
        catch (Exception excp)
        {
            return false;
        }
    }

    @Override
    public String _getXmlNodeName()
    {
        return "SYS_B_E_D";
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public int getHide()
    {
        return hide;
    }

    public void setHide(int hide)
    {
        this.hide = hide;
    }

    @Override
    public void _setValueClear()
    {
        effectValue = 0;
    }

}

//
//LOG
//TIME:
//REC:
//
