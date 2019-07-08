 
package dev.xlin.gameworkshop.progs.contents.beans;
 
public class beanCtxStellarRegion
{
    private int OID = 0 ;
    private int glaxyOID = 0 ;
    private String regionName = "";
    private String regionTag = "";
    private String regionConfigs = "";
    private String regionDesp = "";
    
    private int membCount = 0;

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }
 
    public String getRegionConfigs()
    {
        return regionConfigs;
    }

    public void setRegionConfigs(String regionConfigs)
    {
        this.regionConfigs = regionConfigs;
    }

    public int getMembCount()
    {
        return membCount;
    }

    public void setMembCount(int membCount)
    {
        this.membCount = membCount;
    }

    public String getRegionName()
    {
        return regionName;
    }

    public void setRegionName(String regionName)
    {
        this.regionName = regionName;
    }

    public String getRegionTag()
    {
        return regionTag;
    }

    public void setRegionTag(String regionTag)
    {
        this.regionTag = regionTag;
    }

    public String getRegionDesp()
    {
        return regionDesp;
    }

    public void setRegionDesp(String regionDesp)
    {
        this.regionDesp = regionDesp;
    }

    public int getGlaxyOID()
    {
        return glaxyOID;
    }

    public void setGlaxyOID(int glaxyOID)
    {
        this.glaxyOID = glaxyOID;
    }
     
}
