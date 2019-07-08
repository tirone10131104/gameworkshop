package dev.xlin.gameworkshop.progs.foundation.beans;

import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataBean;
import dev.xlin.tols.data.annotations.JDBId;
import dev.xlin.tols.data.annotations.JDBTable;

/**
 *
 * @author 刘祎鹏
 */
@JDBTable(tableName = "tb_items")
public class BeanItem implements iDataBean
{

    @JDBId
    private int OID = 0;
    private String itemName = "";
    private String itemTag = "";
    private String itemDesp = "";
    private int tpid = 0;
    private int oclsID = 0;
    private int state = 0;
    private long equipData = 0;
    private int hide = 0;
    private int systemItem = 0;
    private int locked = 0;
    private int stack = 0;
    private int stackLimit = 0;
    private int equipment = 0;
    private int slotType = 0;
    private int slotIndex = 0;
    private int equipLimit = 0;
    private int slotRoot = 0;
    private String equipStruct = "";
    private int abstractItem = 0;
    private int containerItem = 0;
    private int equipRoot = 0;
    private double capUse = 0.0;

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public int getTpid()
    {
        return tpid;
    }

    public void setTpid(int tpid)
    {
        this.tpid = tpid;
    }

    public int getOclsID()
    {
        return oclsID;
    }

    public void setOclsID(int oclsID)
    {
        this.oclsID = oclsID;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getItemTag()
    {
        return itemTag;
    }

    public void setItemTag(String itemTag)
    {
        this.itemTag = itemTag;
    }

    public String getItemDesp()
    {
        return itemDesp;
    }

    public void setItemDesp(String itemDesp)
    {
        this.itemDesp = itemDesp;
    }

    public int getHide()
    {
        return hide;
    }

    public void setHide(int hide)
    {
        this.hide = hide;
    }

    public int getSystemItem()
    {
        return systemItem;
    }

    public void setSystemItem(int systemItem)
    {
        this.systemItem = systemItem;
    }

    public int getLocked()
    {
        return locked;
    }

    public void setLocked(int locked)
    {
        this.locked = locked;
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

    public int getEquipLimit()
    {
        return equipLimit;
    }

    public void setEquipLimit(int equipLimit)
    {
        this.equipLimit = equipLimit;
    }

    public String getEquipStruct()
    {
        return equipStruct;
    }

    public void setEquipStruct(String equipStruct)
    {
        this.equipStruct = equipStruct;
    }

    public int getSlotIndex()
    {
        return slotIndex;
    }

    public void setSlotIndex(int slotIndex)
    {
        this.slotIndex = slotIndex;
    }

    public String getName()
    {
        return getItemName();
    }

    public int getAbstractItem()
    {
        return abstractItem;
    }

    public void setAbstractItem(int abstractItem)
    {
        this.abstractItem = abstractItem;
    }

    public int getContainerItem()
    {
        return containerItem;
    }

    public void setContainerItem(int containerItem)
    {
        this.containerItem = containerItem;
    }

    public int getEquipRoot()
    {
        return equipRoot;
    }

    public void setEquipRoot(int equipRoot)
    {
        this.equipRoot = equipRoot;
    }

    public double getCapUse()
    {
        return capUse;
    }

    public void setCapUse(double capUse)
    {
        this.capUse = capUse;
    }

    @Override
    public int _getPKIDX()
    {
        return getOID();
    }

    @Override
    public String _getDataName()
    {
        return getItemName();
    }

    @Override
    public String _getDataTag()
    {
        return getItemTag();
    }

    @Override
    public int _getTypeOID()
    {
        return getTpid();
    }

    @Override
    public int _getDataStatus()
    {
        return getState();
    }

    public int getSlotRoot()
    {
        return slotRoot;
    }

    public void setSlotRoot(int slotRoot)
    {
        this.slotRoot = slotRoot;
    }

    public long getEquipData()
    {
        return equipData;
    }

    public void setEquipData(long equipData)
    {
        this.equipData = equipData;
    }

}

//
//LOG
//TIME:
//REC:
//
