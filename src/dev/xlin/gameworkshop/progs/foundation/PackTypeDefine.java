package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanPackageTypeDefine;
import dev.xlin.tols.data.JDBSession;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.JDAO;
import java.util.List;

/**
 * 背包类型设置
 *
 * @author lyp
 */
public class PackTypeDefine implements JDAO
{

    private wakeup up = null;
    private JDBSession jdsn = null;

    public PackTypeDefine(wakeup _up)
    {
        up = _up ;
        jdsn = new JDBSession(up);
    }

    @Override
    public String getBaseTable()
    {
        return "tb_pack_type_define";
    }

    @Override
    public String getBaseId()
    {
        return "OID";
    }

    @Override
    public long save(Object o)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(Object o)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int disable(Object o)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int enable(Object o)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(Object o)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object get(Object o)
    {
        if (o== null )
        {
            return null;
        }
        String sql = "select * from " + getBaseTable() +" where " + getBaseId() +" = " + o;
        List ls = jdsn.query(sql, getEntityClass());
        if (ls == null )
        {
            return null;
        }
        return ls.get(0);
    }

    @Override
    public int check(Object o)
    {
        if (o == null )
        {
            return OBJECT_NOTEXIST;
        }
        if (o.getClass()!= getEntityClass())
        {
            return PARAM_OBJECT_CLASS_INCORRECT;
        }
        BeanPackageTypeDefine bean = (BeanPackageTypeDefine) o ;
        if (bean.getStatus()!= OBJECT_STATUS_ACTIVE)
        {
            return OBJECT_DISABLED;
        }
        return 0 ; 
    }

    @Override
    public Class getEntityClass()
    {
        return BeanPackageTypeDefine.class;
    }

}
