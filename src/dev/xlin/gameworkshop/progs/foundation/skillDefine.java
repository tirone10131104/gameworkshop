package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanSkillDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.gameworkshop.progs.tools.dbTask;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBeanCheckable;
import dev.xlin.tols.interfaces.iBeanRevert;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.OIDCreator;
import dev.xlin.tools.constChk;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class skillDefine implements iDAO, iBeanCheckable, iBeanRevert
{

    private wakeup up = null;
    private session sn = null;
    private String tableSkill = "tb_skill_define";

    public skillDefine(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    @Override
    public int createRecord(Object o, boolean bln)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }

        BeanSkillDefine bean = (BeanSkillDefine) o;
        if (getSkillDefineByTag(bean.getSkillTag()) != null)
        {
            return iReturn.BEAN_TAG_REPEAT;
        }
        int rck = doCheckSkillData(bean);
        if (rck != 0)
        {
            return rck;
        }
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        bean.setOID(OIDCreator.createOID(up, tableSkill, "OID", iDAO.OID_MIN, iDAO.OID_LENGTH));
        //datablock init 
        dbTask.openManualTask(up);
        int r = jcommon.eInsert(sn, bean, tableSkill, false);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            return dbTask.returnSuccess(up);
        }
        else
        {
            return dbTask.returnFail(up, r);
        }
    }

    public int moveSkillAsChild(int skid, int parid)
    {
        BeanSkillDefine oribean = (BeanSkillDefine) getRecordByID(skid);
        BeanSkillDefine parbean = (BeanSkillDefine) getRecordByID(parid);
        if (checkBean(oribean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (checkBean(parbean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (oribean.getTypeOID() != parbean.getTypeOID())
        {
            return iReturn.SKL_CANT_MOVE_TO_DIF_TYPE;
        }
        //操作
        String sql = "update " + tableSkill + " set parentSkillOID = " + parid + " where OID = " + skid;
        return jcommon.eExcute(up, sql);
    }

    public int moveSkillAsRoot(int skid)
    {
        BeanSkillDefine oribean = (BeanSkillDefine) getRecordByID(skid);
        if (checkBean(oribean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //操作
        String sql = "update " + tableSkill + " set parentSkillOID = " + 0 + " where OID = " + skid;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public int modifyRecord(Object o, boolean bln)
    {
        int r0 = checkParam(o);
        if (r0 != 0)
        {
            return r0;
        }
        BeanSkillDefine bean = (BeanSkillDefine) o;
        BeanSkillDefine obean = (BeanSkillDefine) getRecordByID(bean.getOID());
        if (checkBean(obean) == false)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //检查
        int rlg = doCheckSkillData(bean);
        if (rlg != 0)
        {
            return rlg;
        }
        //数据迁移
        obean.setSkillName(bean.getSkillName());
        obean.setSkillDesp(bean.getSkillDesp());
        obean.setTypeOID(bean.getTypeOID());
        obean.setLevelInvokeType(bean.getLevelInvokeType());
        obean.setGetJudgeImpl(bean.getGetJudgeImpl());
        obean.setGetSkillImpl(bean.getGetSkillImpl());
        obean.setLevelChangeImpl(bean.getLevelChangeImpl());
        obean.setLossSkillImpl(bean.getLossSkillImpl());
        obean.setHide(bean.getHide());
        obean.setLevelDataMethod(bean.getLevelDataMethod());
        obean.setUpgradeJudgeImpl(bean.getUpgradeJudgeImpl());
        obean.setUpgradeSkillImpl(bean.getUpgradeSkillImpl());
        obean.setLevelType(bean.getLevelType());
        return jcommon.eUpdate(sn, obean, tableSkill, "OID", bln);
    }

    private int doCheckSkillData(BeanSkillDefine bean)
    {
        if (constChk.isConst(iConst.class, "SKL_INVK_TYPE_", bean.getLevelInvokeType()) == false)
        {
            return iReturn.SKL_INVOKE_TYPE_ERROR;
        }
        if (constChk.isConst(iConst.class, "SKL_LV_DATA_", bean.getLevelDataMethod()) == false)
        {
            return iReturn.SKL_DATA_METHOD_ERROR;
        }
        if (constChk.isConst(iConst.class, "SKL_SAME_DATA_", bean.getSameDataMethod()) == false)
        {
            return iReturn.SKL_SAME_DATA_METHOD_ERROR;
        }
        if (constChk.isConst(iConst.class, "SKL_LEVEL_TYPE_", bean.getLevelType()) == false)
        {
            return iReturn.SKL_LEVEL_TYPE_ERROR;
        }
        return 0;
    }

    private int checkParam(Object o)
    {
        if (o == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        if (o.getClass() != BeanSkillDefine.class)
        {
            return iDAO.PARAM_OBJECT_CLASS_INCORRECT;
        }
        return 0;
    }

    public BeanSkillDefine getSkillDefineByTag(String tag)
    {
        if (tag == null)
        {
            return null;
        }
        String sql = "select * from " + tableSkill + " where skillTag = '" + tag.trim() + "'";
        List ls = sn.querySQL(sql, BeanSkillDefine.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanSkillDefine) ls.get(0);
    }

    public List getSkillByType(int tp, boolean showAll)
    {
        String sql = "select  * from  " + tableSkill + " where typeOID = " + tp;
        if (showAll == false)
        {
            sql = sql + " and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, BeanSkillDefine.class);
    }

    public List getSkillsByParent(int poid, boolean showAll)
    {
        if (poid == 0)
        {
            return null;
        }
        String sql = "select * from " + tableSkill + " where parentSkillOID = " + poid;
        if (showAll == false)
        {
            sql = sql + " and status = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        return sn.querySQL(sql, BeanSkillDefine.class);
    }

    @Override
    public int deleteRecord(int i)
    {
        BeanSkillDefine bean = (BeanSkillDefine) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //获取直接下级
        if (getSkillsByParent(i, true) != null)
        {
            return iReturn.BEAN_CANT_DELETE;
        }
        //获取分级
        skillLevel sklv = new skillLevel(up);
        if (sklv.getLevelBySkill(i) != null)
        {
            return iReturn.BEAN_CANT_DELETE;
        }
        String sql = "update " + tableSkill + " set status = " + iDAO.OBJECT_STATE_DELETE
                + " where OID = " + i;
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
        String sql = "select * from " + tableSkill + " where OID = " + i;
        List ls = sn.querySQL(sql, BeanSkillDefine.class);
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
        if (o.getClass() != BeanSkillDefine.class)
        {
            return false;
        }
        BeanSkillDefine bean = (BeanSkillDefine) o;
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int revertBean(int i)
    {
        String sql = "update " + tableSkill + " set status = " + iDAO.OBJECT_STATE_ACTIVE
                + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public int destroyBean(int i)
    {
        BeanSkillDefine bean = (BeanSkillDefine) getRecordByID(i);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (bean.getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_NOT_READY_DESTROY;
        }
        String sql = "delete from " + tableSkill + " where OID = " + i;
        return jcommon.eExcute(up, sql);
    }

    @Override
    public List getDeleted()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

//
//LOG
//TIME:
//REC:
//
