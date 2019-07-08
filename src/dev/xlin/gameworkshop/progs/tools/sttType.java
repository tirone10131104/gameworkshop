package dev.xlin.gameworkshop.progs.tools;

import dev.xlin.gameworkshop.progs.iConst;
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
 *
 * @author 刘祎鹏
 */
@SuppressWarnings("static-access")
public class sttType implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;

    public sttType(wakeup up)
    {
        this.up = up;
        this.sn = new session(this.up);
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
        beanSttType bean = (beanSttType) o;
        int r = this.checkBeanLogic(bean);
        if (r != 0)
        {
            return r;
        }
        //数据库
        if (bean.getOID() == 0)
        {
            OIDCreator oc = new OIDCreator(this.up);
            int oid = oc.create0Id("ic_stt_type", "OID", this.OID_MIN, this.OID_LENGTH);
            bean.setOID(oid);
        }
        bean.setState(this.OBJECT_STATE_ACTIVE);
        return jcommon.eInsert(sn, bean, "ic_stt_type", bln);
    }

    private int checkBeanLogic(beanSttType bean)
    {
        //根据STTID找到STTDEF 
        sttDef sdef = new sttDef(this.up);
        beanSttDef bsd = (beanSttDef) sdef.getRecordByID(bean.getSttID());
        if (bsd.getSttChild() != iConst.BOL_TRUE)
        {
            if (bean.getTypeParent() != 0)
            {
                return iReturn.STT_CANT_HAVE_CHILD;
            }
        }
        if (bean.getTypeParent() != 0)
        {
            beanSttType bst = (beanSttType) this.getRecordByID(bean.getTypeParent());
            if (this.checkBean(bst) == false)
            {
                return iReturn.STT_PARENT_TYPE_ERROR;
            }
        }
        return 0;
    }

    /**
     *
     * @param o
     * @param bln
     * @return
     */
    public int modifyRecord(Object o, boolean bln)
    {
        beanSttType bean = (beanSttType) o;
        beanSttType obean = (beanSttType) this.getRecordByID(bean.getOID());
        if (this.checkBean(obean) == false)
        {
            return this.OBJECT_RECORD_NOTEXIST;
        }
        int r0 = this.checkBeanLogic(bean);
        if (r0 != 0)
        {
            return r0;
        }
        //当PARENT 发生改变的时候，需要执行递归检测，防止错误。
        if (bean.getTypeParent() != obean.getTypeParent())
        {
            boolean b = this.dCheckIsChild(obean.getOID(), bean.getTypeParent());
            if (b)
            {
                return iReturn.STT_CANT_MOVE_TO_CHILD;
            }
        }
        //数据
        obean.setTypeName(bean.getTypeName());
        obean.setTypeParent(bean.getTypeParent());
        //数据库
        return jcommon.eUpdate(sn, obean, "ic_stt_type", "OID", bln);
    }

    private boolean dCheckIsChild(int oriID, int tagOID)
    {
        List lc = this.getChildren(oriID);
        boolean b = this.dCheck(lc, tagOID);
        if (b == true)
        {
            return true;
        }
        return false;
    }

    private boolean dCheck(List lc, int tagOID)
    {
        if (lc == null)
        {
            return false;
        }
        for (int i = 0; i < lc.size(); i++)
        {
            beanSttType bst = (beanSttType) lc.get(i);
            if (bst.getOID() == tagOID)
            {
                return true;
            }
            List ls = this.getChildren(bst.getOID());
            boolean b = this.dCheck(ls, tagOID);
            if (b)
            {
                return b;
            }
        }
        return false;
    }

    /**
     *
     * @param i
     * @return
     */
    public int deleteRecord(int i)
    {
        beanSttType bean = (beanSttType) this.getRecordByID(i);
        if (this.checkBean(bean) == false)
        {
            return iReturn.BEAN_CANT_DELETE;
        }
        if (this.getChildren(bean.getOID()) != null)
        {
            return iReturn.BEAN_CANT_DELETE;
        }
        //数据库
        bean.setState(this.OBJECT_STATE_DELETE);
        return jcommon.eUpdate(sn, bean, "ic_stt_type", "OID", false);
    }

    public List getChildren(int i)
    {
        String sql = "select * from ic_stt_type where typeParent = " + i
                + " and state = " + this.OBJECT_STATE_ACTIVE;;
        return this.sn.querySQL(sql, beanSttType.class);
    }

    /**
     *
     * @return
     */
    public List getAllRecord()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param def
     * @return
     */
    public List getAllRecordByDef(int def)
    {
        String sql = "select * from ic_stt_type where sttID = " + def
                + " and state = " + this.OBJECT_STATE_ACTIVE;
        return sn.querySQL(sql, beanSttType.class);
    }

    public List getTypesByDef(int def, boolean showAll)
    {
        String sql = "select * from ic_stt_type where sttID = " + def;
        if (showAll == false)
        {
            sql = sql + " and state = " + this.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, beanSttType.class);
    }

    /**
     *
     * @param def
     * @return
     */
    public List getAllByTag(String tag)
    {
        sttDef sdef = new sttDef(this.up);
        beanSttDef bsd = sdef.getDefByTag(tag);
        if (bsd == null)
        {
            return null;
        }
        return getAllRecordByDef(bsd.getOID());
    }

    /**
     *
     * @param i
     * @return
     */
    public Object getRecordByID(int i)
    {
        String sql = "select * from ic_stt_type where OID = " + i;
        List ls = this.sn.querySQL(sql, beanSttType.class);
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
        beanSttType bean = (beanSttType) o;
        if (bean.getState() != this.OBJECT_STATE_ACTIVE)
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
        beanSttType bean = (beanSttType) this.getRecordByID(i);
        if (bean == null)
        {
            return this.CANT_REVERT;
        }
        bean.setState(this.OBJECT_STATE_ACTIVE);
        return jcommon.eUpdate(sn, bean, "ic_stt_type", "OID", false);
    }

    /**
     *
     * @param i
     * @return
     */
    public int destroyBean(int i)
    {
        beanSttType bean = (beanSttType) this.getRecordByID(i);
        if (bean == null)
        {
            return this.CANT_DESTROY;
        }
        if (bean.getState() != this.OBJECT_STATE_DELETE)
        {
            return this.CANT_DESTROY;
        }
        String sql = "delete from ic_stt_type where OID = " + bean.getOID();
        return jcommon.eExcute(up, sql);
    }

    /**
     *
     * @return
     */
    public List getDeleted()
    {
        String sql = "select * from ic_stt_type where state = " + this.OBJECT_STATE_DELETE;
        return this.sn.querySQL(sql, beanSttType.class);
    }

    /**
     *
     * @param def
     * @return
     */
    public List getRootsByDef(int def, boolean showAll)
    {
        String sql = "select * from ic_stt_type where sttID = " + def
                + " and typeParent = 0";
        if (showAll == false)
        {
            sql = sql + "  and state = " + this.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, beanSttType.class);
    }

    /**
     *
     * @param stag
     * @return
     */
    public List getRootsByTag(String stag, boolean showAll)
    {
        sttDef sdef = new sttDef(this.up);
        beanSttDef bsd = sdef.getDefByTag(stag);
        if (bsd == null)
        {
            return null;
        }
        return this.getRootsByDef(bsd.getOID(), showAll);
    }

    /**
     *
     * @param def
     * @return
     */
    protected boolean isHasTypeByDef(int def)
    {
        String sql = "select * from ic_stt_type where sttID = " + def
                + " and state = " + this.OBJECT_STATE_ACTIVE;;
        List ls = this.sn.querySQL(sql, beanSttType.class);
        if (ls == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     *
     * @param sttID
     * @param stdTag
     * @param up
     * @return
     */
    public static int checkSttPointer(int sttID, String stdTag, wakeup up)
    {
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(sttID);
        if (stp.checkBean(bst) == false)
        {
            //分类树节点定义错误。
            return iReturn.STT_TYPE_POINTER_CHECK_FAIL;
        }
        sttDef sdef = new sttDef(up);
        beanSttDef bsd = (beanSttDef) sdef.getRecordByID(bst.getSttID());
        if (sdef.checkBean(bsd) == false)
        {
            return iReturn.STT_TYPE_POINTER_CHECK_FAIL;
        }
        if (bsd.getSttTag().equals(stdTag) == false)
        {
            return iReturn.STT_TYPE_POINTER_CHECK_FAIL;
        }
        return 0;
    }

    public static int checkSttPointer(int sttID, int defID, wakeup up)
    {
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(sttID);
        if (stp.checkBean(bst) == false)
        {
            //分类树节点定义错误。
            return iReturn.STT_TYPE_POINTER_CHECK_FAIL;
        }
        if (bst.getSttID() == defID)
        {
            return 0;
        }
        else
        {
            return iReturn.STT_TYPE_POINTER_CHECK_FAIL;
        }
    }
}
