package dev.xlin.gameworkshop.progs.runtime.microEngine;

import dev.xlin.tols.data.annotations.JDBId;
import dev.xlin.tols.data.annotations.JDBTable;

/**
 * 微引擎主配置数据
 *
 * @author 刘祎鹏
 */
@JDBTable(tableName = "ic_mce_config_main")
public class BeanMCEngineConfigMain
{

    @JDBId
    private int OID = 0;
    private String mceName = "";
    private String mceTag = "";
    private String mceDescp = "";
    private int maxTps = 0;
    private int startMethod = 0;
    private int emptyMethod = 0;
    private int emptyWaitLong = 0;
    private int status = 0 ;
    private int instanceLimit = 0 ;
    
    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getMceName()
    {
        return mceName;
    }

    public void setMceName(String mceName)
    {
        this.mceName = mceName;
    }

    public String getMceTag()
    {
        return mceTag;
    }

    public void setMceTag(String mceTag)
    {
        this.mceTag = mceTag;
    }

    public String getMceDescp()
    {
        return mceDescp;
    }

    public void setMceDescp(String mceDescp)
    {
        this.mceDescp = mceDescp;
    }

    public int getMaxTps()
    {
        return maxTps;
    }

    public void setMaxTps(int maxTps)
    {
        this.maxTps = maxTps;
    }

    public int getStartMethod()
    {
        return startMethod;
    }

    public void setStartMethod(int startMethod)
    {
        this.startMethod = startMethod;
    }

    public int getEmptyMethod()
    {
        return emptyMethod;
    }

    public void setEmptyMethod(int emptyMethod)
    {
        this.emptyMethod = emptyMethod;
    }

    public int getEmptyWaitLong()
    {
        return emptyWaitLong;
    }

    public void setEmptyWaitLong(int emptyWaitLong)
    {
        this.emptyWaitLong = emptyWaitLong;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getInstanceLimit()
    {
        return instanceLimit;
    }

    public void setInstanceLimit(int instanceLimit)
    {
        this.instanceLimit = instanceLimit;
    }

}
