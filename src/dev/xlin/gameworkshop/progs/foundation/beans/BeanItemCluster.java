package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.tols.interfaces.iBean;

public class BeanItemCluster implements iBean
{
    private int OID = 0 ;
    private String clusterName = "";
    private String clusterTag = "";
    private String descript = "";
    private int status = 0;
    private String OCLS = "";
    private int targetType = 0;
    private int typeID = 0 ;
    
    @Override
    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getClusterName()
    {
        return clusterName;
    }

    public void setClusterName(String clusterName)
    {
        this.clusterName = clusterName;
    }

    public String getClusterTag()
    {
        return clusterTag;
    }

    public void setClusterTag(String clusterTag)
    {
        this.clusterTag = clusterTag;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getOCLS()
    {
        return OCLS;
    }

    public void setOCLS(String OCLS)
    {
        this.OCLS = OCLS;
    }

    public int getTargetType()
    {
        return targetType;
    }

    public void setTargetType(int targetType)
    {
        this.targetType = targetType;
    }

    public int getTypeID()
    {
        return typeID;
    }

    public void setTypeID(int typeID)
    {
        this.typeID = typeID;
    }

    @Override
    public String getName()
    {
        return clusterName;
    }

    @Override
    public int getState()
    {
        return status;
    }
    
}
