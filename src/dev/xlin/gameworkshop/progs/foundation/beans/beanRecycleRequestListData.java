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
public class beanRecycleRequestListData implements iDataElement, iClone, iAdtXMLNode, iDateElementCleanValue
{

    private int OID = 0;
    private int requestMethod = iConst.TARGET_REQ_METD_CHECK;
    private int targetType = iConst.DT_REQ_TARTYPE_SKILL;
    private int targetOID = 0;
    private String description = "";
    private double quantity = 0;
    private int hide = 0;
    private int status = 0;

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
        beanRecycleRequestListData bean = new beanRecycleRequestListData();
        bean.setOID(OID);
        bean.setRequestMethod(requestMethod);
        bean.setTargetOID(targetOID);
        bean.setTargetType(targetType);
        bean.setDescription(description);
        bean.setQuantity(quantity);
        bean.setHide(hide);
        bean.setStatus(status);
        return bean;
    }

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("OID", OID + "");
        e.setAttribute("requestMethod", requestMethod + "");
        e.setAttribute("targetOID", targetOID + "");
        e.setAttribute("targetType", targetType + "");
        e.setAttribute("description", description);
        e.setAttribute("quantity", quantity + "");
        e.setAttribute("hide", hide + "");
        e.setAttribute("status", status + "");
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try
        {
            setOID(foundationUtils.readElementValueAsInt(e, "OID"));
            setRequestMethod(foundationUtils.readElementValueAsInt(e, "requestMethod"));
            setTargetOID(foundationUtils.readElementValueAsInt(e, "targetOID"));
            setTargetType(foundationUtils.readElementValueAsInt(e, "targetType"));
            setDescription(e.getAttribute("description"));
            setQuantity(foundationUtils.readElementValueAsDouble(e, "quantity"));
            setHide(foundationUtils.readElementValueAsInt(e, "hide"));
            setStatus(foundationUtils.readElementValueAsInt(e, "status"));
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
        return "RECY_REQ_LIDT";
    }

    @Override
    public void _setValueClear()
    {
        quantity = 0;
    }

    public int getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(int requestMethod)
    {
        this.requestMethod = requestMethod;
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
