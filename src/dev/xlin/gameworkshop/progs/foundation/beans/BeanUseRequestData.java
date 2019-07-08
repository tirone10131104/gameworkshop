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
public class BeanUseRequestData implements iDataElement, iClone, iAdtXMLNode, iDateElementCleanValue
{
    
    private int OID = 0;
    private int requestType = 0;
    private int targetType = 0;
    private int targetOID = 0;
    private String description = "";
    private int status = 0;
    private int hide = 0;
    private double quantity = 0;
    
    public int getRequestType()
    {
        return requestType;
    }
    
    public void setRequestType(int requestType)
    {
        this.requestType = requestType;
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
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
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
        return getStatus();
    }
    
    @Override
    public Object cloneMe()
    {
        BeanUseRequestData bean = new BeanUseRequestData();
        bean.setDescription(getDescription());
        bean.setHide(getHide());
        bean.setOID(getOID());
        bean.setRequestType(getRequestType());
        bean.setStatus(getStatus());
        bean.setTargetOID(getTargetOID());
        bean.setTargetType(getTargetType());
        bean.setQuantity(quantity);
        return bean;
    }
    
    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("description", getDescription());
        e.setAttribute("hide", getHide() + "");
        e.setAttribute("OID", getOID() + "");
        e.setAttribute("requestType", getRequestType() + "");
        e.setAttribute("status", getStatus() + "");
        e.setAttribute("targetOID", getTargetOID() + "");
        e.setAttribute("targetType", getTargetType() + "");
        e.setAttribute("quantity", quantity + "");
        return e;
    }
    
    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try
        {
            setDescription(e.getAttribute("description"));
            setHide(foundationUtils.readElementValueAsInt(e, "hide"));
            setOID(foundationUtils.readElementValueAsInt(e, "OID"));
            setRequestType(foundationUtils.readElementValueAsInt(e, "requestType"));
            setStatus(foundationUtils.readElementValueAsInt(e, "status"));
            setTargetOID(foundationUtils.readElementValueAsInt(e, "targetOID"));
            setTargetType(foundationUtils.readElementValueAsInt(e, "targetType"));
            setQuantity(foundationUtils.readElementValueAsDouble(e, "quantity"));
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
        return "SYS_B_U_D";
    }
    
    public int getOID()
    {
        return OID;
    }
    
    public void setOID(int OID)
    {
        this.OID = OID;
    }
    
    public double getQuantity()
    {
        return quantity;
    }
    
    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }

    @Override
    public void _setValueClear()
    {
        quantity = 0 ;
    }
    
}
