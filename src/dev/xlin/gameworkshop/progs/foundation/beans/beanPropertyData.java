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
public class beanPropertyData implements iDataElement, iClone, iAdtXMLNode, iDateElementCleanValue
{

    private int OID = 0;
    private int propID = 0;
    private String propTag = "";
    private int datatype = 0;
    private String propValue = "";
    private int state = 0;
    private String propName = "";
    private String propDescription = "";
    private int hide = 0;

    @Override
    public String _getXmlNodeName()
    {
        return "SYS_B_P_D";
    }

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("OID", getOID() + "");
        e.setAttribute("propID", getPropID() + "");
        e.setAttribute("propTag", getPropTag());
        e.setAttribute("datatype", getDatatype() + "");
        e.setAttribute("propValue", getPropValue());
        e.setAttribute("state", getState() + "");
        e.setAttribute("propName", getPropName());
        e.setAttribute("propDescription", getPropDescription());
        e.setAttribute("hide", getHide() +"");
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try
        {
            setOID(foundationUtils.readElementValueAsInt(e, "OID"));
            setPropID(foundationUtils.readElementValueAsInt(e, "propID"));
            setPropTag(e.getAttribute("propTag"));
            setDatatype(foundationUtils.readElementValueAsInt(e, "datatype"));
            setPropValue(e.getAttribute("propValue"));
            setState(foundationUtils.readElementValueAsInt(e, "state"));
            setPropName(e.getAttribute("propName"));
            setPropDescription(e.getAttribute("propDescription"));
            setHide(foundationUtils.readElementValueAsInt(e, "hide"));
            return true;
        }
        catch (Exception excep)
        {
            return false;
        }
    }

    public int getPropID()
    {
        return propID;
    }

    public void setPropID(int propID)
    {
        this.propID = propID;
    }

    public String getPropTag()
    {
        return propTag;
    }

    public void setPropTag(String propTag)
    {
        this.propTag = propTag;
    }

    public int getDatatype()
    {
        return datatype;
    }

    public void setDatatype(int datatype)
    {
        this.datatype = datatype;
    }

    public String getPropValue()
    {
        return propValue;
    }

    public void setPropValue(String propValue)
    {
        this.propValue = propValue;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    @Override
    public String _getDataTitle()
    {
        return getPropDescription();
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
        beanPropertyData bpd = new beanPropertyData();
        bpd.setDatatype(getDatatype());
        bpd.setOID(getOID());
        bpd.setPropDescription(getPropDescription());
        bpd.setPropID(getPropID());
        bpd.setPropName(getPropName());
        bpd.setPropTag(getPropTag());
        bpd.setPropValue(getPropValue());
        bpd.setState(getState());
        bpd.setHide(hide);
        return bpd;
    }

    public String getPropName()
    {
        return propName;
    }

    public void setPropName(String propName)
    {
        this.propName = propName;
    }

    public String getPropDescription()
    {
        return propDescription;
    }

    public void setPropDescription(String propDescription)
    {
        this.propDescription = propDescription;
    }

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
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
        propValue = "";
    }

}

//
//LOG
//TIME:
//REC:
//
