package dev.xlin.gameworkshop.progs.foundation.interfaces;

import java.util.List;

public interface iDataElementOper
{

    public int appendDataElement(iDataElement ide, int colOID);

    public int updateDataElement(iDataElement ide);

    public int disableDataElement(iDataElement ide);

    public int revertDataElement(iDataElement ide);

    public int destroyDataElement(iDataElement ide);

    public List getDataElementList(int colID, boolean showAll);

    public int createElementOID();

    public int getDataElementColumnOID(int oid);
    
    public iDataElement getDataElementByOID (int oid );
    
    public List getAllDataElements (boolean showAll );
    
    public boolean moveDataUp (int oid );
    public boolean moveDataDown (int oid );
    public boolean moveDataToColumn (int oid , int colOID );
    
    
}
