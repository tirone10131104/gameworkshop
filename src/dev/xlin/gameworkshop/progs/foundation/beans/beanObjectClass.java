package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.tols.interfaces.iBean;
import dev.xlin.tols.interfaces.iBeanDesp;

/**
 *
 * @author 刘祎鹏
 */
public class beanObjectClass implements iBean
{
    private int OID = 0 ;
    private String className = "";
    private String classTag = "";
    private String classDesp = "";
    private String classFuncs = "";
    private int state = 0 ;
    private int stpID=0; 
    private int stack = 0 ;
    private int stackLimit = 0 ;
    private int equipment = 0 ;
    private int slotType = 0 ;
    private int slotIndex = 0 ;
    private int equipRoot = 0 ;
    private int containerItem = 0 ;
    private int slotRoot = 0 ;
    private int abstractItem = 0 ;
    private int strictConfig = 0 ;
    
    @Override
    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getClassTag()
    {
        return classTag;
    }

    public void setClassTag(String classTag)
    {
        this.classTag = classTag;
    }

    public String getClassDesp()
    {
        return classDesp;
    }

    public void setClassDesp(String classDesp)
    {
        this.classDesp = classDesp;
    }

    public String getClassFuncs()
    {
        return classFuncs;
    }

    public void setClassFuncs(String classFuncs)
    {
        this.classFuncs = classFuncs;
    }

    @Override
    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public int getStpID()
    {
        return stpID;
    }

    public void setStpID(int stpID)
    {
        this.stpID = stpID;
    }

    @Override
    public String getName()
    {
        return getClassName();
    }

    public int getStack()
    {
        return stack;
    }

    public void setStack(int stack)
    {
        this.stack = stack;
    }

    public int getStackLimit()
    {
        return stackLimit;
    }

    public void setStackLimit(int stackLimit)
    {
        this.stackLimit = stackLimit;
    }

    public int getEquipment()
    {
        return equipment;
    }

    public void setEquipment(int equipment)
    {
        this.equipment = equipment;
    }

    public int getSlotType()
    {
        return slotType;
    }

    public void setSlotType(int slotType)
    {
        this.slotType = slotType;
    }

    public int getSlotIndex()
    {
        return slotIndex;
    }

    public void setSlotIndex(int slotIndex)
    {
        this.slotIndex = slotIndex;
    }

    public int getEquipRoot()
    {
        return equipRoot;
    }

    public void setEquipRoot(int equipRoot)
    {
        this.equipRoot = equipRoot;
    }

    public int getContainerItem()
    {
        return containerItem;
    }

    public void setContainerItem(int containerItem)
    {
        this.containerItem = containerItem;
    }
 
    public int getAbstractItem()
    {
        return abstractItem;
    }

    public void setAbstractItem(int abstractItem)
    {
        this.abstractItem = abstractItem;
    }

    public int getStrictConfig()
    {
        return strictConfig;
    }

    public void setStrictConfig(int strictConfig)
    {
        this.strictConfig = strictConfig;
    }

    public int getSlotRoot()
    {
        return slotRoot;
    }

    public void setSlotRoot(int slotRoot)
    {
        this.slotRoot = slotRoot;
    }

    
}

//
//LOG
//TIME:
//REC:
//
