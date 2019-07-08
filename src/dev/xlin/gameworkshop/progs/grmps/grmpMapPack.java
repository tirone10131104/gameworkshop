package dev.xlin.gameworkshop.progs.grmps;

import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class grmpMapPack implements iDAO
{

    private wakeup up = null;
    private session sn = null;
    private String table = "";

    public grmpMapPack(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    /**
     *
     * @param o
     * @param bln
     * @return
     */
    @Override
    public int createRecord(Object o, boolean bln)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param o
     * @param bln
     * @return 
     */
    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param i
     * @return 
     */
    @Override
    public int deleteRecord(int i)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return 
     */
    @Override
    public List getAllRecord()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param i
     * @return 
     */
    @Override
    public Object getRecordByID(int i)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

//
//LOG
//TIME:
//REC:
//
