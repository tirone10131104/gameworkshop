package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.tols.data.annotations.JDBId;
import dev.xlin.tols.data.annotations.JDBTable;

/**
 *
 * @author 刘祎鹏
 */
@JDBTable(tableName = "tb_pack_type_define")
public class BeanPackageTypeDefine
{

    @JDBId
    private int OID = 0;

    private String packTypeName = "";
    private String packTypeTag = "";
    private int limitPropID = 0;
    private int restrictType = 0;
    private int restrictOID = 0;
    private int itemType = 0;
    private int status = 0;

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getPackTypeName()
    {
        return packTypeName;
    }

    public void setPackTypeName(String packTypeName)
    {
        this.packTypeName = packTypeName;
    }

    public String getPackTypeTag()
    {
        return packTypeTag;
    }

    public void setPackTypeTag(String packTypeTag)
    {
        this.packTypeTag = packTypeTag;
    }

    public int getLimitPropID()
    {
        return limitPropID;
    }

    public void setLimitPropID(int limitPropID)
    {
        this.limitPropID = limitPropID;
    }

    public int getRestrictType()
    {
        return restrictType;
    }

    public void setRestrictType(int restrictType)
    {
        this.restrictType = restrictType;
    }

    public int getRestrictOID()
    {
        return restrictOID;
    }

    public void setRestrictOID(int restrictOID)
    {
        this.restrictOID = restrictOID;
    }

    public int getItemType()
    {
        return itemType;
    }

    public void setItemType(int itemType)
    {
        this.itemType = itemType;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

}
