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
public class beanFuncEnableListItem implements iClone, iAdtXMLNode, iDateElementCleanValue , iDataElement
{

    private int OID = 0;
    private int method = iConst.TARGET_REQ_METD_CHECK;
    private int targetType = iConst.DT_REQ_TARTYPE_PROP;
    private int targetOID = 0;
    private double value = 0;
    private int hide = 0;
    private int status = 0;
    private String description = "";

    @Override
    public Object cloneMe()
    {
        beanFuncEnableListItem bean = new beanFuncEnableListItem();
        bean.setOID(getOID());
        bean.setMethod(getMethod());
        bean.setTargetOID(getTargetOID());
        bean.setTargetType(getTargetType());
        bean.setValue(getValue());
        bean.setHide(getHide());
        bean.setStatus(getStatus());
        bean.setDescription(description);
        return bean;
    }

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("OID", getOID() + "");
        e.setAttribute("method", getMethod() + "");
        e.setAttribute("targetOID", getTargetOID() + "");
        e.setAttribute("targetType", getTargetType() + "");
        e.setAttribute("value", getValue() + "");
        e.setAttribute("hide", getHide() + "");
        e.setAttribute("status", getStatus() + "");
        e.setAttribute("description", description);
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try
        {
            setOID(foundationUtils.readElementValueAsInt(e, "OID"));
            setMethod(foundationUtils.readElementValueAsInt(e, "method"));
            setTargetOID(foundationUtils.readElementValueAsInt(e, "targetOID"));
            setTargetType(foundationUtils.readElementValueAsInt(e, "targetType"));
            setValue(foundationUtils.readElementValueAsDouble(e, "value"));
            setHide(foundationUtils.readElementValueAsInt(e, "hide"));
            setStatus(foundationUtils.readElementValueAsInt(e, "status"));
            setDescription(e.getAttribute(description));
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
        return "FUNC_ENB_L_I";
    }

    @Override
    public void _setValueClear()
    {
        setValue(0);
    }

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public int getMethod()
    {
        return method;
    }

    public void setMethod(int method)
    {
        this.method = method;
    }

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
    public String _getDataTitle()
    {
        return description;
    }

    @Override
    public int _getDataOID()
    {
        return OID;
    }

    @Override
    public int _getStatus()
    {
        return status;
    }

}

//
//LOG
//TIME:
//REC:
//
