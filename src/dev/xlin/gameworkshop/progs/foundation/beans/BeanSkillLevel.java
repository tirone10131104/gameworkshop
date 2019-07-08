package dev.xlin.gameworkshop.progs.foundation.beans;

/**
 *
 * @author 刘祎鹏
 */
public class BeanSkillLevel
{

    private int OID = 0;
    private int skillOID = 0;
    private int levelIdx = 0;
    private String dataHeader = "";
    private String levelName = "";
    private String levelDesp = "";
    private int masterLevel = 0;

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public int getSkillOID()
    {
        return skillOID;
    }

    public void setSkillOID(int skillOID)
    {
        this.skillOID = skillOID;
    }

    public String getDataHeader()
    {
        return dataHeader;
    }

    public void setDataHeader(String dataHeader)
    {
        this.dataHeader = dataHeader;
    }

    public String getLevelName()
    {
        return levelName;
    }

    public void setLevelName(String levelName)
    {
        this.levelName = levelName;
    }

    public String getLevelDesp()
    {
        return levelDesp;
    }

    public void setLevelDesp(String levelDesp)
    {
        this.levelDesp = levelDesp;
    }

    public int getLevelIdx()
    {
        return levelIdx;
    }

    public void setLevelIdx(int levelIdx)
    {
        this.levelIdx = levelIdx;
    }

    public int getMasterLevel()
    {
        return masterLevel;
    }

    public void setMasterLevel(int masterLevel)
    {
        this.masterLevel = masterLevel;
    }
 

}

//
//LOG
//TIME:
//REC:
//
