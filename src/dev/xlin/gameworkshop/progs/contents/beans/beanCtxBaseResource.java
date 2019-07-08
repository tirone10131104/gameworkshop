package dev.xlin.gameworkshop.progs.contents.beans;

import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataBean;

/**
 *
 * @author 刘祎鹏
 */
public class beanCtxBaseResource implements iDataBean
{

    private int OID = 0;
    private String resName = "";
    private String resTag = "";
    private int itemOID = 0;
    private int status = 0;
    private int typeOID = 0;

    @Override
    public int _getPKIDX()
    {
        return getOID();
    }

    @Override
    public String _getDataName()
    {
        return getResName();
    }

    @Override
    public String _getDataTag()
    {
        return getResTag();
    }

    @Override
    public int _getTypeOID()
    {
        return getTypeOID();
    }

    @Override
    public int _getDataStatus()
    {
        return getStatus();
    }

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getResName()
    {
        return resName;
    }

    public void setResName(String resName)
    {
        this.resName = resName;
    }

    public String getResTag()
    {
        return resTag;
    }

    public void setResTag(String resTag)
    {
        this.resTag = resTag;
    }

    public int getItemOID()
    {
        return itemOID;
    }

    public void setItemOID(int itemOID)
    {
        this.itemOID = itemOID;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getTypeOID()
    {
        return typeOID;
    }

    public void setTypeOID(int typeOID)
    {
        this.typeOID = typeOID;
    }

}

//
//LOG
//TIME:
//REC:
//
