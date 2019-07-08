package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.gameworkshop.progs.foundation.foundationUtils;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXMLNode;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iClone;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDateElementCleanValue;
import dev.xlin.tols.xml.xmlRight;
import org.w3c.dom.Element;

public class beanBuildListItemData implements iDataElement, iClone, iAdtXMLNode , iDateElementCleanValue
{

    private int OID = 0;
    private int requestType = 0;
    private int targetType = 0;
    private int targetOID = 0;
    private double quantity = 0;
    private int hide = 0;
    private int status = 0;
    private String description = "";
    
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

    @Override
    public Object cloneMe()
    {
        beanBuildListItemData bean = new beanBuildListItemData();
        bean.setDescription(description);
        bean.setHide(hide);
        bean.setOID(OID);
        bean.setQuantity(quantity);
        bean.setRequestType(requestType);
        bean.setStatus(status);
        bean.setTargetOID(targetOID);
        bean.setTargetType(targetType);
        return bean ; 
    }

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("description", description);
        e.setAttribute("hide", hide+"");
        e.setAttribute("OID", OID+"");
        e.setAttribute("quantity", quantity+"");
        e.setAttribute("requestType", requestType+"");
        e.setAttribute("status", status+"");
        e.setAttribute("targetOID", targetOID+"");
        e.setAttribute("targetType", targetType+"");
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
            setQuantity(foundationUtils.readElementValueAsDouble(e, "quantity"));
            setRequestType(foundationUtils.readElementValueAsInt(e, "requestType"));
            setStatus(foundationUtils.readElementValueAsInt(e, "status"));
            setTargetOID(foundationUtils.readElementValueAsInt(e, "targetOID"));
            setTargetType(foundationUtils.readElementValueAsInt(e, "targetType"));
            return true;
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
        return "SYS_B_B_D";
    }

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

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

    public double getQuantity()
    {
        return quantity;
    }

    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
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
    public void _setValueClear()
    {
        quantity= 0 ;
    }

}
