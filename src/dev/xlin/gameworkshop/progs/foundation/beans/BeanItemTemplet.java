package dev.xlin.gameworkshop.progs.foundation.beans;

/**
 *
 * @author Administrator
 */
public class BeanItemTemplet
{

    private int OID = 0;
    private String tempName = "";
    private int srcItem = 0;
    private int oclsID = 0;
    private String tempDesp = "";
    private int status = 0;
    private String datablockHeader = "";

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getTempName()
    {
        return tempName;
    }

    public void setTempName(String tempName)
    {
        this.tempName = tempName;
    }

    public int getSrcItem()
    {
        return srcItem;
    }

    public void setSrcItem(int srcItem)
    {
        this.srcItem = srcItem;
    }

    public int getOclsID()
    {
        return oclsID;
    }

    public void setOclsID(int oclsID)
    {
        this.oclsID = oclsID;
    }

    public String getTempDesp()
    {
        return tempDesp;
    }

    public void setTempDesp(String tempDesp)
    {
        this.tempDesp = tempDesp;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getDatablockHeader()
    {
        return datablockHeader;
    }

    public void setDatablockHeader(String datablockHeader)
    {
        this.datablockHeader = datablockHeader;
    }

}
