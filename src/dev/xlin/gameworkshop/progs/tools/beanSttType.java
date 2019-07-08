package dev.xlin.gameworkshop.progs.tools;

import dev.xlin.tols.data.annotations.JDBId;
import dev.xlin.tols.data.annotations.JDBTable;

/**
 *
 * @author 刘祎鹏
 */
@SuppressWarnings("static-access")
@JDBTable(tableName = "ic_stt_type")
public class beanSttType
{

    @JDBId
    private int OID = 0;
    private int sttID = 0;
    private String typeName = "";
    private int state = 0;
    private int typeParent = 0;

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public int getSttID()
    {
        return sttID;
    }

    public void setSttID(int sttID)
    {
        this.sttID = sttID;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public int getTypeParent()
    {
        return typeParent;
    }

    public void setTypeParent(int typeParent)
    {
        this.typeParent = typeParent;
    }

}
