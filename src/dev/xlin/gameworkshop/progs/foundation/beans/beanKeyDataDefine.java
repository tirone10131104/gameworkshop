package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataBean;

/**
 *
 * @author 刘祎鹏
 */
public class beanKeyDataDefine implements iDataBean
{

    private int OID = 0;
    private String keyName = "";
    private String keyTag = "";
    private String keyDesp = "";
    private int typeOID = 0;
    private int dataType = 0;
    private int cacheLoad = 0;
    private int status = 0;
    private int dataChunkID = 0;

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getKeyName()
    {
        return keyName;
    }

    public void setKeyName(String keyName)
    {
        this.keyName = keyName;
    }

    public String getKeyTag()
    {
        return keyTag;
    }

    public void setKeyTag(String keyTag)
    {
        this.keyTag = keyTag;
    }

    public String getKeyDesp()
    {
        return keyDesp;
    }

    public void setKeyDesp(String keyDesp)
    {
        this.keyDesp = keyDesp;
    }

    public int getTypeOID()
    {
        return typeOID;
    }

    public void setTypeOID(int typeOID)
    {
        this.typeOID = typeOID;
    }

    public int getDataType()
    {
        return dataType;
    }

    public void setDataType(int dataType)
    {
        this.dataType = dataType;
    }

    public int getCacheLoad()
    {
        return cacheLoad;
    }

    public void setCacheLoad(int cacheLoad)
    {
        this.cacheLoad = cacheLoad;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getDataChunkID()
    {
        return dataChunkID;
    }

    public void setDataChunkID(int dataChunkID)
    {
        this.dataChunkID = dataChunkID;
    }

    @Override
    public int _getPKIDX()
    {
        return OID;
    }

    @Override
    public String _getDataName()
    {
        return keyName;
    }

    @Override
    public String _getDataTag()
    {
        return keyTag;
    }

    @Override
    public int _getTypeOID()
    {
        return typeOID;
    }

    @Override
    public int _getDataStatus()
    {
        return status;
    }

}

//
//LOG
//TIME:
//REC:
//
