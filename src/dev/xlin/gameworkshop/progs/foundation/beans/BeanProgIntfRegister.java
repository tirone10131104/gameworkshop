package dev.xlin.gameworkshop.progs.foundation.beans;

public class BeanProgIntfRegister
{

    private int OID = 0;
    private String regDescription = "";
    private String regTag = "";
    private int preLoad = 0;
    private int defOID = 0;
    private String implAddress = "";
    private int status = 0;
    //新加入的，接口实现简要描述
    private String regName = "";

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getRegDescription()
    {
        return regDescription;
    }

    public void setRegDescription(String regDescription)
    {
        this.regDescription = regDescription;
    }

    public String getRegTag()
    {
        return regTag;
    }

    public void setRegTag(String regTag)
    {
        this.regTag = regTag;
    }

    public int getPreLoad()
    {
        return preLoad;
    }

    public void setPreLoad(int preLoad)
    {
        this.preLoad = preLoad;
    }

    public int getDefOID()
    {
        return defOID;
    }

    public void setDefOID(int defOID)
    {
        this.defOID = defOID;
    }

    public String getImplAddress()
    {
        return implAddress;
    }

    public void setImplAddress(String implAddress)
    {
        this.implAddress = implAddress;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getRegName()
    {
        return regName;
    }

    public void setRegName(String regName)
    {
        this.regName = regName;
    }

}
