package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.tols.data.annotations.JDBId;
import dev.xlin.tols.data.annotations.JDBTable;

/**
 *
 * @author 刘祎鹏
 */
@JDBTable(tableName = "tb_datablock")
public class BeanDatablock
{
    @JDBId
    private long OID = 0; 
    private String datablock = "";
 
    public String getDatablock()
    {
        return datablock;
    }

    public void setDatablock(String datablock)
    {
        this.datablock = datablock;
    }

    public long getOID()
    {
        return OID;
    }

    public void setOID(long OID)
    {
        this.OID = OID;
    }
    
}

//
//LOG
//TIME:
//REC:
//
