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
public class BeanItemEquipStruct
{

    private int OID = 0;
    private String equipName = "";
    private String equipDesp = "";
    private String equipTag = "";
    private int parentID = 0;
    private int levelID = 0;
    private int systemID = 0;
    private int status = 0;
    private int indexID = 0 ;
    
    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getEquipName()
    {
        return equipName;
    }

    public void setEquipName(String equipName)
    {
        this.equipName = equipName;
    }

    public String getEquipDesp()
    {
        return equipDesp;
    }

    public void setEquipDesp(String equipDesp)
    {
        this.equipDesp = equipDesp;
    }

    public String getEquipTag()
    {
        return equipTag;
    }

    public void setEquipTag(String equipTag)
    {
        this.equipTag = equipTag;
    }

    public int getParentID()
    {
        return parentID;
    }

    public void setParentID(int parentID)
    {
        this.parentID = parentID;
    }

    public int getLevelID()
    {
        return levelID;
    }

    public void setLevelID(int levelID)
    {
        this.levelID = levelID;
    }

    public int getSystemID()
    {
        return systemID;
    }

    public void setSystemID(int systemID)
    {
        this.systemID = systemID;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getIndexID()
    {
        return indexID;
    }

    public void setIndexID(int indexID)
    {
        this.indexID = indexID;
    }

}
