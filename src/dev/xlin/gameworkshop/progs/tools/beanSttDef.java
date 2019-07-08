
package dev.xlin.gameworkshop.progs.tools;

import dev.xlin.tols.data.annotations.JDBId;
import dev.xlin.tols.data.annotations.JDBTable;

/**
 *
 * @author 刘祎鹏
 */
@SuppressWarnings("static-access")
@JDBTable(tableName = "ic_stt_def")
public class beanSttDef
{
    @JDBId
    private int OID = 0;
    private String sttName = "";
    private int state = 0;
    private String sttDesp = "";
    private String sttTag = "";
    private int sttSystem = 0;
    private int sttChild = 1;

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getSttName()
    {
        return sttName;
    }

    public void setSttName(String sttName)
    {
        this.sttName = sttName;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public String getSttDesp()
    {
        return sttDesp;
    }

    public void setSttDesp(String sttDesp)
    {
        this.sttDesp = sttDesp;
    }

    public String getSttTag()
    {
        return sttTag;
    }

    public void setSttTag(String sttTag)
    {
        this.sttTag = sttTag;
    }

    public int getSttSystem()
    {
        return sttSystem;
    }

    public void setSttSystem(int sttSystem)
    {
        this.sttSystem = sttSystem;
    }

    public int getSttChild()
    {
        return sttChild;
    }

    public void setSttChild(int sttChild)
    {
        this.sttChild = sttChild;
    }
    
}
