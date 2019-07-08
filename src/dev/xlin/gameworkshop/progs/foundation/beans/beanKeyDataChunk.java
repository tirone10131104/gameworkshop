package dev.xlin.gameworkshop.progs.foundation.beans;

public class beanKeyDataChunk
{
    private int OID = 0;
    private String chunkName = "";
    private String chunkDesp = "";
    private int preLoadChunk = 0;
    private int status = 0 ;
    
    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getChunkName()
    {
        return chunkName;
    }

    public void setChunkName(String chunkName)
    {
        this.chunkName = chunkName;
    }

    public String getChunkDesp()
    {
        return chunkDesp;
    }

    public void setChunkDesp(String chunkDesp)
    {
        this.chunkDesp = chunkDesp;
    }

    public int getPreLoadChunk()
    {
        return preLoadChunk;
    }

    public void setPreLoadChunk(int preLoadChunk)
    {
        this.preLoadChunk = preLoadChunk;
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
