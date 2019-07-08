package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.gameworkshop.progs.foundation.foundationUtils;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXMLNode;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iClone;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.tols.xml.xmlRight;
import org.w3c.dom.Element;

/**
 *
 * @author 刘祎鹏
 */
public class BeanFuncControl implements iClone, iAdtXMLNode
{

    //功能名称
    private String funcName = "";
    //功能标签
    private String funcTag = "";
    //持续周期
    private int period = 0;
    //开关冷却
    private int cooldown = 0;
    //自动重复
    private int autoLoop = 0;
    //效果目标范围
    private int targetRange = iConst.FUNC_ACT_TARGET_SELF;
    private int actionSide = iConst.FUNC_ACT_SIDE_OUR ;
    //描述文本
    private String description = "";
    //启动功能
    private String enableImplements = "";
    //循环功能
    private String loopImplenments = "";
    //关闭功能
    private String disableImplements = "";
    //初始化功能环境
    private String initFuncEnv = "";

    @Override
    public Object cloneMe()
    {
        BeanFuncControl bean = new BeanFuncControl();
        bean.setFuncTag(getFuncTag());
        bean.setFuncName(getFuncName());
        bean.setPeriod(getPeriod());
        bean.setCooldown(getCooldown());
        bean.setAutoLoop(getAutoLoop());
        bean.setTargetRange(getTargetRange());
        bean.setDescription(getDescription());
        bean.setEnableImplements(getEnableImplements());
        bean.setLoopImplenments(getLoopImplenments());
        bean.setDisableImplements(getDisableImplements());
        bean.setInitFuncEnv(getInitFuncEnv());
        bean.setActionSide(getActionSide());
        return bean;
    }

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("funcName", getFuncName());
        e.setAttribute("funcTag", getFuncTag());
        e.setAttribute("period", getPeriod() + "");
        e.setAttribute("cooldown", getCooldown() + "");
        e.setAttribute("autoLoop", getAutoLoop() + "");
        e.setAttribute("targetRange", getTargetRange()+ "");
        e.setAttribute("description", getDescription());
        e.setAttribute("enableImplements", getEnableImplements());
        e.setAttribute("loopImplenments", getLoopImplenments());
        e.setAttribute("disableImplements", getDisableImplements());
        e.setAttribute("initFuncEnv", getInitFuncEnv());
        e.setAttribute("actionSide", getActionSide()+"");
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try 
        {
            setFuncTag(e.getAttribute("funcTag"));
            setFuncName(e.getAttribute("funcName"));
            setPeriod(foundationUtils.readElementValueAsInt(e, "period"));
            setCooldown(foundationUtils.readElementValueAsInt(e, "cooldown"));
            setAutoLoop(foundationUtils.readElementValueAsInt(e, "autoLoop"));
            setTargetRange(foundationUtils.readElementValueAsInt(e, "targetRange"));
            setDescription(e.getAttribute("description"));
            setEnableImplements(e.getAttribute("enableImplements"));
            setLoopImplenments(e.getAttribute("loopImplenments"));
            setDisableImplements(e.getAttribute("disableImplements"));
            setInitFuncEnv(e.getAttribute("initFuncEnv")); 
            setActionSide(foundationUtils.readElementValueAsInt(e, "actionSide"));
            return true ;
        }
        catch(Exception excp )
        {
            excp.printStackTrace();
            return false;
        }
    }

    @Override
    public String _getXmlNodeName()
    {
        return "FUNC_CTRL";
    }

    public String getFuncName()
    {
        return funcName;
    }

    public void setFuncName(String funcName)
    {
        this.funcName = funcName;
    }

    public String getFuncTag()
    {
        return funcTag;
    }

    public void setFuncTag(String funcTag)
    {
        this.funcTag = funcTag;
    }

    public int getPeriod()
    {
        return period;
    }

    public void setPeriod(int period)
    {
        this.period = period;
    }

    public int getCooldown()
    {
        return cooldown;
    }

    public void setCooldown(int cooldown)
    {
        this.cooldown = cooldown;
    }

    public int getAutoLoop()
    {
        return autoLoop;
    }

    public void setAutoLoop(int autoLoop)
    {
        this.autoLoop = autoLoop;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getEnableImplements()
    {
        return enableImplements;
    }

    public void setEnableImplements(String enableImplements)
    {
        this.enableImplements = enableImplements;
    }

    public String getLoopImplenments()
    {
        return loopImplenments;
    }

    public void setLoopImplenments(String loopImplenments)
    {
        this.loopImplenments = loopImplenments;
    }

    public String getDisableImplements()
    {
        return disableImplements;
    }

    public void setDisableImplements(String disableImplements)
    {
        this.disableImplements = disableImplements;
    }

    public String getInitFuncEnv()
    {
        return initFuncEnv;
    }

    public void setInitFuncEnv(String initFuncEnv)
    {
        this.initFuncEnv = initFuncEnv;
    }

    public int getActionSide()
    {
        return actionSide;
    }

    public void setActionSide(int actionSide)
    {
        this.actionSide = actionSide;
    }

    public int getTargetRange()
    {
        return targetRange;
    }

    public void setTargetRange(int targetRange)
    {
        this.targetRange = targetRange;
    }

}

//
//LOG
//TIME:
//REC:
//
