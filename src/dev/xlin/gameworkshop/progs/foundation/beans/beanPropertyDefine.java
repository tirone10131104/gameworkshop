package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataBean;

public class beanPropertyDefine  implements iDataBean
{

    private int OID = 0;
    private String propName = "";
    private String propTag = "";
    private String propDesp = "";
    private int state = 0;
    private int dataType = 0;
    private int stpID = 0;

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getPropName()
    {
        return propName;
    }

    public void setPropName(String propName)
    {
        this.propName = propName;
    }

    public String getPropTag()
    {
        return propTag;
    }

    public void setPropTag(String propTag)
    {
        this.propTag = propTag;
    }

    public String getPropDesp()
    {
        return propDesp;
    }

    public void setPropDesp(String propDesp)
    {
        this.propDesp = propDesp;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public int getDataType()
    {
        return dataType;
    }

    public void setDataType(int dataType)
    {
        this.dataType = dataType;
    }

    public int getStpID()
    {
        return stpID;
    }

    public void setStpID(int stpID)
    {
        this.stpID = stpID;
    }

    @Override
    public int _getPKIDX()
    {
         return OID;
    }

    @Override
    public String _getDataName()
    {
        return propName;
    }

    @Override
    public String _getDataTag()
    {
        return propTag;
    }

    @Override
    public int _getTypeOID()
    {
        return stpID;
    }

    @Override
    public int _getDataStatus()
    {
        return  state;
    }
}
