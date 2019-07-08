package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.gameworkshop.progs.foundation.foundationUtils;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXMLNode;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iClone;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDateElementCleanValue;
import dev.xlin.tols.xml.xmlRight;
import org.w3c.dom.Element;

public class BeanRecycleListItemData  implements iDataElement, iClone, iAdtXMLNode,iDateElementCleanValue
{

    private int OID = 0 ;
    private int itemOID = 0;
    private double quantity = 0;
    private String description = "";
    private int status = 0;
    private int hide = 0;
    
    
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
        BeanRecycleListItemData bean = new BeanRecycleListItemData();
        bean.setOID(getOID());
        bean.setItemOID(getItemOID());
        bean.setQuantity(getQuantity());
        bean.setDescription(getDescription());
        bean.setStatus(status);
        bean.setHide(getHide());
        return bean;
    }

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("OID", getOID()+"");
        e.setAttribute("itemOID", getItemOID()+"");
        e.setAttribute("quantity", getQuantity()+"");
        e.setAttribute("description", getDescription());
        e.setAttribute("status", getStatus()+"");
        e.setAttribute("hide", getHide()+"");
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try 
        {
            OID = foundationUtils.readElementValueAsInt(e, "OID");
            itemOID = foundationUtils.readElementValueAsInt(e, "itemOID");
            quantity = foundationUtils.readElementValueAsDouble(e, "quantity");
            description = e.getAttribute("description");
            status = foundationUtils.readElementValueAsInt(e, "status" );
            hide = foundationUtils.readElementValueAsInt(e, "hide");
            return true;
        }
        catch(Exception excp )
        {
            return false;
        }
    }

    @Override
    public String _getXmlNodeName()
    {
        return "SYS_B_R_D";
    }

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public int getItemOID()
    {
        return itemOID;
    }

    public void setItemOID(int itemOID)
    {
        this.itemOID = itemOID;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
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
    public void _setValueClear()
    {
        quantity = 0;
    }
    
}
