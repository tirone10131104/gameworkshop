package dev.xlin.gameworkshop.progs.contents.beans;

/**
 *
 * @author 刘祎鹏
 */
public class beanCtxCelestialWorld
{
    //OID
    private int OID = 0 ;
    //天体名称
    private String worldName = "";
    //父节点OID 
    private int parentOID = 0 ;
    //主星体数据ID
    private int stellarOID = 0;
    private int worldType = 0 ;
    private String descp = "";
    private String worldCode = "";
    private int orbitIdx =  0 ;
    private double angel = 0;
    private int worldTypeSetOID = 0 ;
    private int worldTypeMainOID = 0 ;
    private int worldTypeConfigOID = 0 ;
    private int systemSerialIdx = 0;

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getWorldName()
    {
        return worldName;
    }

    public void setWorldName(String worldName)
    {
        this.worldName = worldName;
    }

    public int getParentOID()
    {
        return parentOID;
    }

    public void setParentOID(int parentOID)
    {
        this.parentOID = parentOID;
    }

    public int getStellarOID()
    {
        return stellarOID;
    }

    public void setStellarOID(int stellarOID)
    {
        this.stellarOID = stellarOID;
    }

    public int getWorldType()
    {
        return worldType;
    }

    public void setWorldType(int worldType)
    {
        this.worldType = worldType;
    }

    public String getDescp()
    {
        return descp;
    }

    public void setDescp(String descp)
    {
        this.descp = descp;
    }

    public String getWorldCode()
    {
        return worldCode;
    }

    public void setWorldCode(String worldCode)
    {
        this.worldCode = worldCode;
    }

    public int getOrbitIdx()
    {
        return orbitIdx;
    }

    public void setOrbitIdx(int orbitIdx)
    {
        this.orbitIdx = orbitIdx;
    }

    public double getAngel()
    {
        return angel;
    }

    public void setAngel(double angel)
    {
        this.angel = angel;
    }

    public int getWorldTypeSetOID()
    {
        return worldTypeSetOID;
    }

    public void setWorldTypeSetOID(int worldTypeSetOID)
    {
        this.worldTypeSetOID = worldTypeSetOID;
    }

    public int getWorldTypeMainOID()
    {
        return worldTypeMainOID;
    }

    public void setWorldTypeMainOID(int worldTypeMainOID)
    {
        this.worldTypeMainOID = worldTypeMainOID;
    }

    public int getSystemSerialIdx()
    {
        return systemSerialIdx;
    }

    public void setSystemSerialIdx(int systemSerialIdx)
    {
        this.systemSerialIdx = systemSerialIdx;
    }

    public int getWorldTypeConfigOID()
    {
        return worldTypeConfigOID;
    }

    public void setWorldTypeConfigOID(int worldTypeConfigOID)
    {
        this.worldTypeConfigOID = worldTypeConfigOID;
    }
    
}

//
//LOG
//TIME:
//REC:
//
