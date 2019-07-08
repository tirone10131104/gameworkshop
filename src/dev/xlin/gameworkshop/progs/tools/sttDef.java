
package dev.xlin.gameworkshop.progs.tools;

import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import java.util.List;

/**
 *  ic_stt_def的DAO对象。<br>
 * 
 * @author 刘祎鹏
 */
@SuppressWarnings("static-access")
public class sttDef implements iDAO , iBeanCheckable,iBeanRevert 
{
    private wakeup up = null;
    private session sn = null;

    public sttDef (wakeup up)
    {
        this.up = up;
        this.sn = new session (this.up);
    }

    /**
     * 
     * @param o
     * @param bln
     * @return
     */
    public int createRecord(Object o, boolean bln)
    {
        if (o == null)
        {
            return this.PARAM_OBJECT_NULL;
        }
        beanSttDef bean = (beanSttDef) o;
        OIDCreator cc = new OIDCreator (this.up);
        int oid = cc.create0Id("ic_stt_def", "OID", OID_MIN, this.OID_LENGTH);
        bean.setOID(oid);
        bean.setState(this.OBJECT_STATE_ACTIVE);
        return jcommon.eInsert(sn, bean, "ic_stt_def", bln);
    }

    /**
     * 
     * @param o
     * @param bln
     * @return
     */
    public int modifyRecord(Object o, boolean bln)
    {
        if (o == null)
        {
            return this.PARAM_OBJECT_NULL;
        }
        beanSttDef bean = (beanSttDef)o;
        beanSttDef obean = (beanSttDef) this.getRecordByID(bean.getOID());
        if (this.checkBean(obean) == false)
        {
            return this.OBJECT_RECORD_NOTEXIST;
        }
        //数据转移
        obean.setSttDesp(bean.getSttDesp());
        obean.setSttName(bean.getSttName());
        return jcommon.eUpdate(sn, obean, "ic_stt_def", "OID", bln);
    }

    /**
     * 
     * @param i
     * @return
     */
    public int deleteRecord(int i)
    {
        sttType stp = new sttType (this.up);
        boolean b = stp.isHasTypeByDef(i);
        if (b)
        {
            return iReturn.BEAN_CANT_DELETE;
        }
        beanSttDef bean = (beanSttDef) this.getRecordByID(i);
        if (bean == null)
        {
            return this.OBJECT_RECORD_NOTEXIST;
        }
        if (bean.getSttSystem() == 1)
        {
            //系统确定的不能被删除。
            return iReturn.BEAN_CANT_DELETE;
        }
        bean.setState(this.OBJECT_STATE_DELETE);
        return jcommon.eUpdate(sn, bean, "ic_stt_def", "OID", false);
    }

    /**
     * 
     * @return
     */
    public List getAllRecord()
    {
        String sql = "select * from ic_stt_def where state = " + this.OBJECT_STATE_ACTIVE;
        return this.sn.querySQL(sql, beanSttDef.class);
    }

    /**
     * 
     * @param i
     * @return
     */
    public Object getRecordByID(int i)
    {
        String sql = "select * from ic_stt_def where OID = " + i;
        List ls = this.sn.querySQL(sql, beanSttDef.class);
        if (ls == null)
        {
            return null;
        }
        return ls.get(0);
    }

    /**
     *
     * @param o
     * @return
     */
    public boolean checkBean(Object o)
    {
        if (o == null)
        {
            return false;
        }
        beanSttDef bean = (beanSttDef) o;
        if (bean.getState()!= this.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    /**
     * 
     * @param i
     * @return
     */
    public int revertBean(int i)
    {
        beanSttDef bean = (beanSttDef) this.getRecordByID(i);
        if (bean == null)
        {
            return this.CANT_REVERT;
        }
        bean.setState(this.OBJECT_STATE_ACTIVE);
        return jcommon.eUpdate(sn, bean, "ic_stt_def", "OID", false);
    }

    /**
     * 
     * @param i
     * @return
     */
    public int destroyBean(int i)
    {
        beanSttDef bean = (beanSttDef) this.getRecordByID(i);
        if (bean == null)
        {
            return this.CANT_DESTROY;
        }
        if (bean.getState()!= this.OBJECT_STATE_DELETE)
        {
            return this.CANT_DESTROY;
        }
        String sql = "delete from ic_stt_def where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    /**
     * 
     * @return
     */
    public List getDeleted()
    {
        String sql = "select * from ic_stt_def where state = " + this.OBJECT_STATE_DELETE;
        return sn.querySQL(sql, beanSttDef.class);
    }

    /**
     * 
     * @param stag
     * @return
     */
    public beanSttDef getDefByTag(String stag)
    {
        String sql = "select * from ic_stt_def where sttTag = '" + stag + "'";
        List ls = this.sn.querySQL(sql, beanSttDef.class);
        if (ls == null)
        {
            return null;
        }
        return (beanSttDef) ls.get(0);
    }
    
    /**
     * 检查某个分类是否隶属于某个分类定义标签下
     * @param typeOID
     * @param stg
     * @param up
     * @return
     */
    public static boolean checkTypeDefByTag (int typeOID , String stg ,wakeup up)
    {
        sttDef sdef = new sttDef(up);
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(typeOID);
        if (stp .checkBean(bst) == false)
        {
            //分类树错误
            return false;
        }
        beanSttDef bsd = (beanSttDef) sdef.getRecordByID(bst.getSttID());
        if (sdef.checkBean(bsd) == false)
        {
            return false;
        }
        if (bsd.getSttTag().equals(stg.trim()) == false)
        {
            return false;
        }
        return true;
    }

}
