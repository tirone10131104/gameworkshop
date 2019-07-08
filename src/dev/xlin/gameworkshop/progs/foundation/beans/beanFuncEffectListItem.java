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
public class beanFuncEffectListItem implements iClone, iAdtXMLNode, iDateElementCleanValue, iDataElement
{

    private int OID = 0;
    private int targetType = iConst.FUNC_EFT_TAR_PROP;
    private int targetOID = 0;
    private int valueMethod = 0;
    private double effectValue = 0;
    //同类型效果方法（叠加，取大，取小，取均，替代，回避）
    private int sameEffectMethod = 0;
    private int hide = 0;
    private String description = "";
    private int status = 0;

    @Override
    public Object cloneMe()
    {
        beanFuncEffectListItem bean = new beanFuncEffectListItem();
        bean.setOID(getOID());
        bean.setTargetOID(getTargetOID());
        bean.setTargetType(getTargetType());
        bean.setValueMethod(getValueMethod());
        bean.setEffectValue(getEffectValue());
        bean.setHide(getHide());
        bean.setDescription(getDescription());
        bean.setStatus(getStatus());
        bean.setSameEffectMethod(sameEffectMethod);
        return bean ; 
    }

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("description", description);
        e.setAttribute("OID", OID +"");
        e.setAttribute("targetOID", targetOID+"");
        e.setAttribute("targetType", targetType+"");
        e.setAttribute("effectValue", effectValue+"");
        e.setAttribute("hide", hide+"");
        e.setAttribute("status", status+"");
        e.setAttribute("sameEffectMethod", sameEffectMethod+"");
        e.setAttribute("valueMethod", valueMethod+"");
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try 
        {
            setOID(foundationUtils.readElementValueAsInt(e, "OID"));
            setTargetOID(foundationUtils.readElementValueAsInt(e, "targetOID"));
            setTargetType(foundationUtils.readElementValueAsInt(e, "targetType"));
            setValueMethod(foundationUtils.readElementValueAsInt(e, "valueMethod"));
            setEffectValue(foundationUtils.readElementValueAsDouble(e, "effectValue"));
            setHide(foundationUtils.readElementValueAsInt(e, "hide"));
            setDescription(e.getAttribute("description"));
            setStatus(foundationUtils.readElementValueAsInt(e, "status"));
            setSameEffectMethod(foundationUtils.readElementValueAsInt(e, "sameEffectMethod"));
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
        return "FUNC_E_L_I";
    }

    @Override
    public void _setValueClear()
    {
        setEffectValue(0) ; 
    }

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

    public int getTargetOID()
    {
        return targetOID;
    }

    public void setTargetOID(int targetOID)
    {
        this.targetOID = targetOID;
    }

    public int getValueMethod()
    {
        return valueMethod;
    }

    public void setValueMethod(int valueMethod)
    {
        this.valueMethod = valueMethod;
    }

    public double getEffectValue()
    {
        return effectValue;
    }

    public void setEffectValue(double effectValue)
    {
        this.effectValue = effectValue;
    }

    public int getHide()
    {
        return hide;
    }

    public void setHide(int hide)
    {
        this.hide = hide;
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

    public int getSameEffectMethod()
    {
        return sameEffectMethod;
    }

    public void setSameEffectMethod(int sameEffectMethod)
    {
        this.sameEffectMethod = sameEffectMethod;
    }

}

//
//LOG
//TIME:
//REC:
//
