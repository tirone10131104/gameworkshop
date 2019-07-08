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
public class BeanFuncPropListItem implements iClone, iAdtXMLNode, iDateElementCleanValue, iDataElement
{

    private int OID = 0;
    private int propOID = 0;
    private double value = 0;
    private String description = "";
    private int hide = 0;
    private int status = 0;

    @Override
    public Object cloneMe()
    {
        BeanFuncPropListItem bean = new BeanFuncPropListItem();
        bean.setOID(OID);
        bean.setPropOID(propOID);
        bean.setValue(value);
        bean.setDescription(description);
        bean.setHide(hide);
        bean.setStatus(status);
        return bean;
    }

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("OID", OID+"");
        e.setAttribute("propOID", propOID+"");
        e.setAttribute("value",value+"");
        e.setAttribute("description", description);
        e.setAttribute("hide", hide+"");
        e.setAttribute("status", status+"");
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try
        {
            setOID(foundationUtils.readElementValueAsInt(e, "OID"));
            setPropOID(foundationUtils.readElementValueAsInt(e, "propOID"));
            setValue(foundationUtils.readElementValueAsDouble(e, "value"));
            setDescription(e.getAttribute("description"));
            setHide(foundationUtils.readElementValueAsInt(e, "hide"));
            setStatus(foundationUtils.readElementValueAsInt(e, "status"));
            return true ; 
        }
        catch(Exception excp )
        {
            excp.printStackTrace();
            return false;
        }
    }

    @Override
    public String _getXmlNodeName()
    {
        return "FUNC_P_L_I";
    }

    @Override
    public void _setValueClear()
    {
        value = 0 ;
    }

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public int getPropOID()
    {
        return propOID;
    }

    public void setPropOID(int propOID)
    {
        this.propOID = propOID;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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
