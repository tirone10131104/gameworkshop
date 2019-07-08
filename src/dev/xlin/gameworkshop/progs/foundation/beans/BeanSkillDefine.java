package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataBean;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockHeader;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.tols.interfaces.iBean;

/**
 *
 * @author 刘祎鹏
 */
public class BeanSkillDefine implements iDataBean, iDatablockHeader
{

    //技能主ID
    private int OID = 0;
    //名称
    private String skillName = "";
    //标签
    private String skillTag = "";
    //基本描述
    private String skillDesp = "";
    //分类号
    private int typeOID = 0;
    //状态
    private int status = 0;
    //分级调用类型，（调用分级，级联调用 ，仅调主技能）
    private int levelInvokeType = iConst.SKL_INVK_TYPE_LEVEL;
    //获取判定接口实现
    private String getJudgeImpl = "";
    //获取后处理接口实现
    private String getSkillImpl = "";
    //级别变更后处理接口实现
    private String levelChangeImpl = "";
    //失去技能后处理接口实现
    private String lossSkillImpl = "";
    //技能主记录对应的区块引导
    private String dataHeader = "";
    //技能升级判定接口实现
    private String upgradeJudgeImpl = "";
    //技能升级操作接口实现
    private String upgradeSkillImpl = "";
    //隐藏
    private int hide = 0;
    //分级数据使用方法（主分合并，仅用分级数据）
    private int levelDataMethod = iConst.SKL_LV_DATA_LEVEL;
    //主分同种数据使用方法。（取大，取小，仅用主，仅用分，叠加）
    private int sameDataMethod = iConst.SKL_LV_DATA_LEVEL;
    //直接父技能，用于建立基本的层级结构
    private int parentSkillOID = 0;
    //分级类型（顺序分级，自动分级，不分级)
    private int levelType = iConst.SKL_LEVEL_TYPE_NO;

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getSkillName()
    {
        return skillName;
    }

    public void setSkillName(String skillName)
    {
        this.skillName = skillName;
    }

    public String getSkillTag()
    {
        return skillTag;
    }

    public void setSkillTag(String skillTag)
    {
        this.skillTag = skillTag;
    }

    public String getSkillDesp()
    {
        return skillDesp;
    }

    public void setSkillDesp(String skillDesp)
    {
        this.skillDesp = skillDesp;
    }

    public int getTypeOID()
    {
        return typeOID;
    }

    public void setTypeOID(int typeOID)
    {
        this.typeOID = typeOID;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getLevelInvokeType()
    {
        return levelInvokeType;
    }

    public void setLevelInvokeType(int levelInvokeType)
    {
        this.levelInvokeType = levelInvokeType;
    }

    public String getGetJudgeImpl()
    {
        return getJudgeImpl;
    }

    public void setGetJudgeImpl(String getJudgeImpl)
    {
        this.getJudgeImpl = getJudgeImpl;
    }

    public String getGetSkillImpl()
    {
        return getSkillImpl;
    }

    public void setGetSkillImpl(String getSkillImpl)
    {
        this.getSkillImpl = getSkillImpl;
    }

    public String getLevelChangeImpl()
    {
        return levelChangeImpl;
    }

    public void setLevelChangeImpl(String levelChangeImpl)
    {
        this.levelChangeImpl = levelChangeImpl;
    }

    public String getLossSkillImpl()
    {
        return lossSkillImpl;
    }

    public void setLossSkillImpl(String lossSkillImpl)
    {
        this.lossSkillImpl = lossSkillImpl;
    }

    public String getDataHeader()
    {
        return dataHeader;
    }

    public void setDataHeader(String dataHeader)
    {
        this.dataHeader = dataHeader;
    }

    public int getHide()
    {
        return hide;
    }

    public void setHide(int hide)
    {
        this.hide = hide;
    }

    public int getLevelDataMethod()
    {
        return levelDataMethod;
    }

    public void setLevelDataMethod(int levelDataMethod)
    {
        this.levelDataMethod = levelDataMethod;
    }

    public int getSameDataMethod()
    {
        return sameDataMethod;
    }

    public void setSameDataMethod(int sameDataMethod)
    {
        this.sameDataMethod = sameDataMethod;
    }

    public String getUpgradeJudgeImpl()
    {
        return upgradeJudgeImpl;
    }

    public void setUpgradeJudgeImpl(String upgradeJudgeImpl)
    {
        this.upgradeJudgeImpl = upgradeJudgeImpl;
    }

    @Override
    public int _getPKIDX()
    {
        return getOID();
    }

    @Override
    public String _getDataName()
    {
        return getSkillName();
    }

    @Override
    public String _getDataTag()
    {
        return getSkillTag();
    }

    @Override
    public int _getTypeOID()
    {
        return getTypeOID();
    }

    @Override
    public int _getDataStatus()
    {
        return getStatus();
    }

    @Override
    public String _getDatablockHeader()
    {
        return getDataHeader();
    }

    public String getUpgradeSkillImpl()
    {
        return upgradeSkillImpl;
    }

    public void setUpgradeSkillImpl(String upgradeSkillImpl)
    {
        this.upgradeSkillImpl = upgradeSkillImpl;
    }

    public int getParentSkillOID()
    {
        return parentSkillOID;
    }

    public void setParentSkillOID(int parentSkillOID)
    {
        this.parentSkillOID = parentSkillOID;
    }

    public int getLevelType()
    {
        return levelType;
    }

    public void setLevelType(int levelType)
    {
        this.levelType = levelType;
    }

}

//
//LOG
//TIME:
//REC:
//
