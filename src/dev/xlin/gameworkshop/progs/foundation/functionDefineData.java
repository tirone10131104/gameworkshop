package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.beanFuncControl;
import dev.xlin.gameworkshop.progs.foundation.beans.beanFuncEffectListItem;
import dev.xlin.gameworkshop.progs.foundation.beans.beanFuncEnableListItem;
import dev.xlin.gameworkshop.progs.foundation.beans.beanFuncPropListItem;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXML;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iClone;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockClearValue;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockCopy;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockInitInstance;
import dev.xlin.gameworkshop.progs.iReturn;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 功能定义数据结构
 *
 * @author Administrator
 */
public class functionDefineData implements iAdtXML, iAdtDocumentSave, iDatablockFace, iDatablockInitInstance, iDatablockClearValue, iDatablockCopy
{

    private wakeup up = null;
    private session sn = null;
    private HashMap datapages = new HashMap();
    private boolean saveFlag = false;
    public static final String keyCtrl = "FUNC_CTRL";
    public static final String keyEnableList = "FUNC_ENB_LIST";
    public static final String keyPropertyList = "FUNC_PROP_LIST";
    public static final String keyEffecttList = "FUNC_EFT_LIST";

    /**
     * 添加属性节点
     *
     * @param bean
     * @return
     */
    public int appendFuncProperty(beanFuncPropListItem bean)
    {
        if (bean == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        List ls = getListByKey(keyPropertyList);
        bean.setOID(createNodeOID(ls));
        ls.add(bean);
        return iDAO.OPERATE_SUCCESS;
    }

    /**
     * 修改属性节点
     *
     * @param bean
     * @return
     */
    public int updateFuncProperty(beanFuncPropListItem bean)
    {
        if (bean == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        beanFuncPropListItem obean = getFuncPropListItemNotClone(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (obean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setDescription(bean.getDescription());
        obean.setValue(bean.getValue());
        obean.setHide(bean.getHide());
        return iDAO.OPERATE_SUCCESS;
    }

    /**
     * 添加启用节点
     *
     * @param bean
     * @return
     */
    public int appendFuncEnableItem(beanFuncEnableListItem bean)
    {
        if (bean == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        List ls = getListByKey(keyEnableList);
        bean.setOID(createNodeOID(ls));
        ls.add(bean);
        return iDAO.OPERATE_SUCCESS;
    }

    /**
     * 更新启用节点
     *
     * @param bean
     * @return
     */
    public int updateFuncEnableItem(beanFuncEnableListItem bean)
    {
        if (bean == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        beanFuncEnableListItem obean = getFuncEnableListItemNotClone(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (obean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setDescription(bean.getDescription());
        obean.setHide(bean.getHide());
        obean.setMethod(bean.getMethod());
        obean.setValue(bean.getValue());
        return iDAO.OPERATE_SUCCESS;
    }

    /**
     * 添加效果节点
     *
     * @param bean
     * @return
     */
    public int appendFuncEffect(beanFuncEffectListItem bean)
    {
        if (bean == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        bean.setStatus(iDAO.OBJECT_STATE_ACTIVE);
        List ls = getListByKey(keyEffecttList);
        bean.setOID(createNodeOID(ls));
        ls.add(bean);
        return iDAO.OPERATE_SUCCESS;
    }

    /**
     * 更新效果节点
     *
     * @param bean
     * @return
     */
    public int updateFuncEffect(beanFuncEffectListItem bean)
    {
        if (bean == null)
        {
            return iDAO.PARAM_OBJECT_NULL;
        }
        beanFuncEffectListItem obean = getFuncEffectListItemNotClone(bean.getOID());
        if (obean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (obean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        //数据迁移
        obean.setDescription(bean.getDescription());
        obean.setEffectValue(bean.getEffectValue());
        obean.setValueMethod(bean.getValueMethod());
        obean.setSameEffectMethod(bean.getSameEffectMethod());
        obean.setHide(bean.getHide());
        return iDAO.OPERATE_SUCCESS;
    }

    /**
     * 更新功能定义
     *
     * @param _bean
     * @return
     */
    public int updateFunctionControl(beanFuncControl bean)
    {
        beanFuncControl obean = getControlNotClone();
        obean.setAutoLoop(bean.getAutoLoop());
        obean.setCooldown(bean.getCooldown());
        obean.setDescription(bean.getDescription());
        obean.setDisableImplements(bean.getDisableImplements());
        obean.setEnableImplements(bean.getEnableImplements());
        obean.setFuncName(bean.getFuncName());
        obean.setFuncTag(bean.getFuncTag());
        obean.setInitFuncEnv(bean.getInitFuncEnv());
        obean.setLoopImplenments(bean.getLoopImplenments());
        obean.setPeriod(bean.getPeriod());
        obean.setTargetRange(bean.getTargetRange());
        return iDAO.OPERATE_SUCCESS;
    }

    public beanFuncControl getFunctionControl()
    {
        return (beanFuncControl) getControlNotClone().cloneMe();
    }

    private beanFuncControl getControlNotClone()
    {
        return (beanFuncControl) datapages.get(keyCtrl);
    }

    /**
     * 获取效果列表的拷贝
     *
     * @return
     */
    public ArrayList getEffectList(boolean showAll)
    {
        return returnCopyedList(keyEffecttList, showAll);
    }

    /**
     * 获取属性列表的拷贝
     *
     * @return
     */
    public ArrayList getPropList(boolean showAll)
    {
        return returnCopyedList(keyPropertyList, showAll);
    }

    /**
     * 获取启用列表的拷贝
     *
     * @return
     */
    public ArrayList getEnableList(boolean showAll)
    {
        return returnCopyedList(keyEnableList, showAll);
    }

    private ArrayList returnCopyedList(String key, boolean showAll)
    {
        ArrayList arl = new ArrayList();
        List les = getListByKey(key);
        for (int i = 0; i < les.size(); i++)
        {
            iDataElement bean = (iDataElement) les.get(i);
            if (showAll == false && bean._getStatus() != iDAO.OBJECT_STATE_ACTIVE)
            {
                continue;
            }
            iClone ico = (iClone) bean;
            arl.add(ico.cloneMe());
        }
        return arl;
    }

    public int disableFuncNode(String nodeKey, int OID)
    {
        return switchFuncNodeStatus(nodeKey, OID, iDAO.OBJECT_STATE_DELETE);
    }

    private int switchFuncNodeStatus(String nodeKey, int OID, int status)
    {
        if (nodeKey == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        nodeKey = nodeKey.trim();
        iDataElement ide = findDataElement(nodeKey, OID);
        if (ide == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        if (nodeKey.equals(keyEffecttList))
        {
            beanFuncEffectListItem bean = (beanFuncEffectListItem) ide;
            bean.setStatus(status);
        }
        else if (nodeKey.equals(keyPropertyList))
        {
            beanFuncPropListItem bean = (beanFuncPropListItem) ide;
            bean.setStatus(status);
        }
        else if (nodeKey.equals(keyEnableList))
        {
            beanFuncEnableListItem bean = (beanFuncEnableListItem) ide;
            bean.setStatus(status);
        }
        else
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        return iDAO.OPERATE_SUCCESS;
    }

    private iDataElement findDataElement(String nodeKey, int OID)
    {
        List ls = getListByKey(nodeKey);
        if (ls == null)
        {
            return null;
        }
        for (int i = 0; i < ls.size(); i++)
        {
            iDataElement ide = (iDataElement) ls.get(i);
            if (ide._getDataOID() == OID)
            {
                return ide;
            }
        }
        return null;
    }

    public int revertFuncNode(String nodeKey, int OID)
    {
        return switchFuncNodeStatus(nodeKey, OID, iDAO.OBJECT_STATE_ACTIVE);
    }

    private int getIndexOfFuncNode(String nodeKey, int OID)
    {
        List lns = getListByKey(nodeKey);
        if (lns == null)
        {
            return -1;
        }
        for (int i = 0; i < lns.size(); i++)
        {
            iDataElement ide = (iDataElement) lns.get(i);
            if (ide._getDataOID() == OID)
            {
                return i;
            }
        }
        return -1;
    }

    public int destroyFuncNode(String nodeKey, int OID)
    {
        int idx = getIndexOfFuncNode(nodeKey, OID);
        if (idx < 0)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        List ls = getListByKey(nodeKey);
        iDataElement ide = (iDataElement) ls.get(idx);
        if (ide._getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            return iReturn.BEAN_NOT_READY_DESTROY;
        }
        ls.remove(idx);
        return iDAO.OPERATE_SUCCESS;
    }

    public int moveFuncNodeUp(String nodeKey, int OID)
    {
        int idx = getIndexOfFuncNode(nodeKey, OID);
        if (idx == 0)
        {
            return iReturn.BEAN_CANT_MOVE_UP;
        }
        List ls = getListByKey(nodeKey);
        iDataElement ide = (iDataElement) ls.get(idx - 1);
        ls.remove(idx - 1);
        ls.add(idx, ide);
        return iReturn.SUCCESS;
    }

    public int moveFuncNodeDown(String nodeKey, int OID)
    {
        List ls = getListByKey(nodeKey);
        int idx = getIndexOfFuncNode(nodeKey, OID);
        if (idx == ls.size() - 1)
        {
            return iReturn.BEAN_CANT_MOVE_DOWN;
        }
        iDataElement ide = (iDataElement) ls.get(idx);
        ls.remove(idx);
        ls.add(idx + 1, ide);
        return iReturn.SUCCESS;
    }

    private List getListByKey(String key)
    {
        return (List) datapages.get(key);
    }

    //获取启用列表的引用
    private ArrayList getEnableListNotClone()
    {
        return (ArrayList) getListByKey(keyEnableList);
    }

    //获取属性列表的引用
    private ArrayList getPropListNotClone()
    {
        return (ArrayList) getListByKey(keyPropertyList);
    }

    //获取效果列表的引用
    private ArrayList getEffectListNotClone()
    {
        return (ArrayList) getListByKey(keyEffecttList);
    }

    /**
     * 按OID获取效果的拷贝
     *
     * @param oid
     * @return
     */
    public beanFuncEffectListItem getFuncEffectItem(int oid)
    {
        iDataElement ide = getFuncNodeNotClone(keyEffecttList, oid);
        if (ide == null)
        {
            return null;
        }
        iClone ico = (iClone) ide;
        return (beanFuncEffectListItem) ico.cloneMe();
    }

    /**
     * 按OID获取属性的拷贝
     *
     * @param oid
     * @return
     */
    public beanFuncPropListItem getFuncPropertyItem(int oid)
    {
        iDataElement ide = getFuncNodeNotClone(keyPropertyList, oid);
        if (ide == null)
        {
            return null;
        }
        iClone ico = (iClone) ide;
        return (beanFuncPropListItem) ico.cloneMe();
    }

    /**
     * 按OID获取启用项的拷贝
     *
     * @param oid
     * @return
     */
    public beanFuncEnableListItem getFuncEnableItem(int oid)
    {
        iDataElement ide = getFuncNodeNotClone(keyEnableList, oid);
        if (ide == null)
        {
            return null;
        }
        iClone ico = (iClone) ide;
        return (beanFuncEnableListItem) ico.cloneMe();
    }

    //获取效果节点的引用
    private beanFuncEffectListItem getFuncEffectListItemNotClone(int oid)
    {
        return (beanFuncEffectListItem) getFuncNodeNotClone(keyEffecttList, oid);
    }

    //获取启用节点的引用
    private beanFuncEnableListItem getFuncEnableListItemNotClone(int oid)
    {
        return (beanFuncEnableListItem) getFuncNodeNotClone(keyEnableList, oid);
    }

    //获取属性节点的引用
    private beanFuncPropListItem getFuncPropListItemNotClone(int oid)
    {
        return (beanFuncPropListItem) getFuncNodeNotClone(keyPropertyList, oid);
    }

    //通过节点名和节点ID，取得节点的引用
    private iDataElement getFuncNodeNotClone(String nodeKey, int oid)
    {
        List ls = getListByKey(nodeKey);
        if (ls == null)
        {
            return null;
        }
        for (int i = 0; i < ls.size(); i++)
        {
            iDataElement ide = (iDataElement) ls.get(i);
            if (ide._getDataOID() == oid)
            {
                return ide;
            }
        }
        return null;
    }

    //通用的OID创建工具
    private int createNodeOID(List ls)
    {
        Random r = new Random();
        while (true)
        {
            int i = r.nextInt();
            if (i <= 0)
            {
                continue;
            }
            if (findElementInList(ls, i) == null)
            {
                return i;
            }
        }
    }

    private iDataElement findElementInList(List ls, int id)
    {
        for (int j = 0; j < ls.size(); j++)
        {
            iDataElement ide = (iDataElement) ls.get(j);
            if (ide._getDataOID() == id)
            {
                return ide;
            }
        }
        return null;
    }

    public functionDefineData()
    {

    }

    @Override
    public xmlRight transToXML()
    {
        try
        {
            xmlRight xr = new xmlRight();
            xr.createXML();
            Element erot = xr.createElement("ROOT");
            //转换控制节点
            beanFuncControl bctrl = (beanFuncControl) datapages.get(keyCtrl);
            Element ctrl = bctrl.transToXmlElement(xr);
            erot.appendChild(ctrl);
            //转换启用数据列表
            List lenb = (List) datapages.get(keyEnableList);
            for (int i = 0; i < lenb.size(); i++)
            {
                beanFuncEnableListItem bfeli = (beanFuncEnableListItem) lenb.get(i);
                Element eenb = bfeli.transToXmlElement(xr);
                erot.appendChild(eenb);
            }
            //转换属性数据列表
            List lprop = (List) datapages.get(keyPropertyList);
            for (int i = 0; i < lprop.size(); i++)
            {
                beanFuncPropListItem bfpli = (beanFuncPropListItem) lprop.get(i);
                Element epp = bfpli.transToXmlElement(xr);
                erot.appendChild(epp);
            }
            //转换效果数据列表
            List lefs = (List) datapages.get(keyEffecttList);
            for (int i = 0; i < lefs.size(); i++)
            {
                beanFuncEffectListItem beli = (beanFuncEffectListItem) lefs.get(i);
                Element eeft = beli.transToXmlElement(xr);
                erot.appendChild(eeft);
            }
            xr.appendElementToDoc(erot);
            return xr;
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean revertFromXML(xmlRight xr)
    {
        try
        {
            createEmptyDataStruct();
            Element erot = xr.getDocument().getDocumentElement();
            //恢复控制数据
            NodeList nlCtrl = erot.getElementsByTagName(new beanFuncControl()._getXmlNodeName());
            Element ectrl = (Element) nlCtrl.item(0);
            beanFuncControl bfc = new beanFuncControl();
            boolean bct = bfc.revertFromXmlElement(ectrl);
            if (bct == false)
            {
                return false;
            }
            datapages.put(keyCtrl, bfc);
            //恢复启用数据列表
            NodeList nlEnbs = erot.getElementsByTagName(new beanFuncEnableListItem()._getXmlNodeName());
            ArrayList aenbs = (ArrayList) datapages.get(keyEnableList);
            for (int i = 0; i < nlEnbs.getLength(); i++)
            {
                Element eenb = (Element) nlEnbs.item(i);
                beanFuncEnableListItem benb = new beanFuncEnableListItem();
                boolean b = benb.revertFromXmlElement(eenb);
                if (b == false)
                {
                    return false;
                }
                aenbs.add(benb);
            }
            datapages.put(keyEnableList, aenbs);
            //恢复属性数据列表
            NodeList nlProps = erot.getElementsByTagName(new beanFuncPropListItem()._getXmlNodeName());
            ArrayList aprops = (ArrayList) datapages.get(keyPropertyList);
            for (int i = 0; i < nlProps.getLength(); i++)
            {
                Element epp = (Element) nlProps.item(i);
                beanFuncPropListItem bean = new beanFuncPropListItem();
                boolean b = bean.revertFromXmlElement(epp);
                if (b == false)
                {
                    return false;
                }
                aprops.add(bean);
            }
            datapages.put(keyPropertyList, aprops);
            //恢复效果数据列表
            NodeList nlEffs = erot.getElementsByTagName(new beanFuncEffectListItem()._getXmlNodeName());
            ArrayList aefs = (ArrayList) datapages.get(keyEffecttList);
            for (int i = 0; i < nlEffs.getLength(); i++)
            {
                Element eeft = (Element) nlEffs.item(i);
                beanFuncEffectListItem bean = new beanFuncEffectListItem();
                boolean b = bean.revertFromXmlElement(eeft);
                if (b == false)
                {
                    return false;
                }
                aefs.add(bean);
            }
            datapages.put(keyEffecttList, aefs);
            return true;
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isNeedSave()
    {
        return saveFlag;
    }

    @Override
    public void resetSave()
    {
        saveFlag = false;
    }

    @Override
    public void save()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDatablockServiceTag()
    {
        return "SYS_DB_FUNCTION_DEFINE_DATA";
    }

    @Override
    public String getDatablockName()
    {
        return "功能定义数据";
    }

    @Override
    public void initDatablcok(wakeup _up)
    {
        up = _up;
        createEmptyDataStruct();
    }

    private void createEmptyDataStruct()
    {
        datapages.clear();
        datapages.put(keyCtrl, new beanFuncControl());
        datapages.put(keyEffecttList, new ArrayList());
        datapages.put(keyEnableList, new ArrayList());
        datapages.put(keyPropertyList, new ArrayList());
    }

    @Override
    public void setAllValueClear()
    {
        ArrayList aenbs = (ArrayList) datapages.get(keyEnableList);
        for (int i = 0; i < aenbs.size(); i++)
        {
            beanFuncEnableListItem bean = (beanFuncEnableListItem) aenbs.get(i);
            bean._setValueClear();
        }
        ArrayList aprop = (ArrayList) datapages.get(keyPropertyList);
        for (int i = 0; i < aprop.size(); i++)
        {
            beanFuncPropListItem bean = (beanFuncPropListItem) aprop.get(i);
            bean._setValueClear();
        }
        ArrayList aefs = (ArrayList) datapages.get(keyEffecttList);
        for (int i = 0; i < aefs.size(); i++)
        {
            beanFuncEffectListItem bean = (beanFuncEffectListItem) aefs.get(i);
            bean._setValueClear();
        }
    }

    @Override
    public Object datablockCopy()
    {
        xmlRight xr = transToXML();
        iAdtXML iax = new functionDefineData();
        iDatablockInitInstance idi = (iDatablockInitInstance) iax;
        idi.initDatablcok(up);
        boolean b = iax.revertFromXML(xr);
        if (b)
        {
            return iax;
        }
        else
        {
            return null;
        }
    }

}
