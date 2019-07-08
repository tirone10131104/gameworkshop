/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.xlin.gameworkshop.progs.foundation.beans;

/**
 *
 * @author Administrator
 */
public class BeanProgIntfDefine
{

    private int OID = 0;
    private String intfName = "";
    private String intfTag = "";
    private String intfDesp = "";
    private int status = 0;
    private int typeID = 0;
    private String intfAddress = "";
    private int systemIntf = 0;
    private String paramList = "";
    private String actionFunction = "";
    private String paramFunction = "";

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getIntfName()
    {
        return intfName;
    }

    public void setIntfName(String intfName)
    {
        this.intfName = intfName;
    }

    public String getIntfTag()
    {
        return intfTag;
    }

    public void setIntfTag(String intfTag)
    {
        this.intfTag = intfTag;
    }

    public String getIntfDesp()
    {
        return intfDesp;
    }

    public void setIntfDesp(String intfDesp)
    {
        this.intfDesp = intfDesp;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getTypeID()
    {
        return typeID;
    }

    public void setTypeID(int typeID)
    {
        this.typeID = typeID;
    }

    public String getIntfAddress()
    {
        return intfAddress;
    }

    public void setIntfAddress(String intfAddress)
    {
        this.intfAddress = intfAddress;
    }

    public int getSystemIntf()
    {
        return systemIntf;
    }

    public void setSystemIntf(int systemIntf)
    {
        this.systemIntf = systemIntf;
    }

    public String getParamList()
    {
        return paramList;
    }

    public void setParamList(String paramList)
    {
        this.paramList = paramList;
    }

    public String getActionFunction()
    {
        return actionFunction;
    }

    public void setActionFunction(String actionFunction)
    {
        this.actionFunction = actionFunction;
    }

    public String getParamFunction()
    {
        return paramFunction;
    }

    public void setParamFunction(String paramFunction)
    {
        this.paramFunction = paramFunction;
    }

}
