package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.beanPropertyDefine;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class propertyDefine implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String table = "tb_prop_define";
    public propertyDefine(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    public List getPropsByType(int tpid, boolean showAll)
    {
        String sql = "select * from tb_prop_define where stpID = " + tpid;
        if (showAll == false)
        {
            sql = sql + " and state = " + iDAO.OBJECT_STATE_ACTIVE;
        } 
        return sn.querySQL(sql, beanPropertyDefine.class);
    }

    @Override
    public int createRecord(Object o, boolean bln)
    {
        int r0 = doCheckParam(o);
        if (r0!= 0)
        {
            return r0;
        }
        beanPropertyDefine bean = (beanPropertyDefine) o ; 
        beanPropertyDefine obean = getPropertyByTag(bean.getPropTag());
        if (obean != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        int oid = OIDCreator.createOID(up, table, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH);
        bean.setOID(oid);
        bean.setState(iDAO.OBJECT_STATE_ACTIVE);
        return jcommon.eInsert(sn, bean, table, bln);
    }
    
    public beanPropertyDefine getPropertyByTag (String tag )
    {
        String sql = "select * from tb_prop_define where propTag = '" + tag.trim() +"'";
        List ls = sn.querySQL(sql, beanPropertyDefine.class);
        if (ls == null)
        {
            return null;
        }
        return (beanPropertyDefine) ls.get(0);
    }

    private int doCheckParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != beanPropertyDefine.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = doCheckParam(o);
        if (r0 != 0)
        {
            return r0 ;
        }
        beanPropertyDefine bean = (beanPropertyDefine) o ; 
        beanPropertyDefine obean = (beanPropertyDefine) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setPropDesp(bean.getPropDesp());
        obean.setPropName(bean.getPropName());
        obean.setDataType(bean.getDataType());
        return jcommon.eUpdate(sn, obean, table, "OID", bln);
    }

    public int movePropertyToType (int oid , int tpid )
    {
        sttType stp =  new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(tpid);
        if (bst == null)
        {
            return iReturn.STT_TYPE_NOT_EXIST;
        }
        if (bst.getSttID() != systemType.CODE_STT_SYS_PROP)
        {
            return iReturn.STT_CANT_MOVE_TO_DEFF_STTP;
        }
        String sql = "update " + table +" set stpID = " + tpid+" where OID = " + oid;
        return jcommon.eExcute(up, sql);
    }
    
    @Override
    public int deleteRecord(int i)
    {
        String sql = "update " + table +" set state = " + iDAO.OBJECT_STATE_DELETE
                +" where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public List getAllRecord()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRecordByID(int i)
    {
        String sql = "select * from " + table +" where OID = " + i ;
        List ls = sn.querySQL(sql, beanPropertyDefine.class);
        if (ls == null)
        {
            return null;
        }
        return ls.get(0);
    }

    @Override
    public boolean checkBean(Object o)
    {
        if (o == null)
        {
            return false;
        }
        if (o.getClass().equals(beanPropertyDefine.class) == false)
        {
            return false;
        }
        beanPropertyDefine bean = (beanPropertyDefine) o;
        if (bean.getState()!= iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true ; 
    }

    @Override
    public int revertBean(int i)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int destroyBean(int i)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List getDeleted()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
