package dev.xlin.gameworkshop.progs.foundation.interfaces;

import dev.xlin.gameworkshop.progs.foundation.beanDataColumn;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public interface iDataColumnOper
{

    public int appendColumn(beanDataColumn bean, int pageIdx );

    public int updateColumn(beanDataColumn bean);

    public int removeColumn(int oid);

    public List getColumnListByPageIndex(int pgidx);

    public List getColumnListByPageTag(String tag);

    public beanDataColumn getColumnByOID(int oid);

    public beanDataColumn getColumnByTag(String tag);
    
    public boolean moveColumnUp (int oid );
    
    public boolean moveColumnDown (int oid );
    
    public boolean moveColumnToPage (int oid , int pgidx );
    
    public int getColumnPageIndex (int oid );
    
}

//
//LOG
//TIME:
//REC:
//
