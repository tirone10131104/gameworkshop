package dev.xlin.gameworkshop.GUI;

import dev.xlin.gameworkshop.GUI.dialog.dlgKeyDataSelector;
import dev.xlin.gameworkshop.GUI.dialog.dlgPropertyDefSelector;
import dev.xlin.gameworkshop.GUI.dialog.dlgSelectIntfImplsByISET;
import dev.xlin.gameworkshop.GUI.dialog.dlgSelectIntfSet;
import dev.xlin.gameworkshop.GUI.dialog.dlgSelectItemDefine;
import dev.xlin.gameworkshop.GUI.dialog.dlgSelectSkill;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItemEquipStruct;
import dev.xlin.gameworkshop.progs.foundation.beans.beanKeyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfRegister;
import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfSet;
import dev.xlin.gameworkshop.progs.foundation.beans.beanPropertyDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillLevel;
import dev.xlin.gameworkshop.progs.foundation.interfaceDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaceRegister;
import dev.xlin.gameworkshop.progs.foundation.interfaceSet;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataBean;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.itemEquipStruct;
import dev.xlin.gameworkshop.progs.foundation.keyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.gameworkshop.progs.foundation.skillDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.codeTools;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;

/**
 *
 * @author 刘祎鹏
 */
public class guiCodes
{

    public static DefaultComboBoxModel makeItemEquipTypeModel(wakeup up)
    {
        DefaultComboBoxModel modEQTP = new DefaultComboBoxModel();
        listItem lall = new listItem("[通用]", 0);
        modEQTP.addElement(lall);
        itemEquipStruct ies = new itemEquipStruct(up);
        List ls = ies.getItemListByParent(0, false);
        if (ls != null)
        {
            for (int i = 0; i < ls.size(); i++)
            {
                beanItemEquipStruct bies = (beanItemEquipStruct) ls.get(i);
                listItem li = new listItem(bies.getEquipName(), bies.getOID());
                modEQTP.addElement(li);
            }
        }
        return modEQTP;
    }

    public static DefaultComboBoxModel makeItemEquipChildModel(wakeup up, int eqid)
    {
        DefaultComboBoxModel modSLTP = new DefaultComboBoxModel();
        listItem lall = new listItem("[通用]", 0);
        modSLTP.addElement(lall);
        if (eqid != 0)
        {
            itemEquipStruct ies = new itemEquipStruct(up);
            List ls = ies.getItemListByParent(eqid, false);
            if (ls != null)
            {
                for (int i = 0; i < ls.size(); i++)
                {
                    beanItemEquipStruct bies = (beanItemEquipStruct) ls.get(i);
                    listItem li = new listItem(bies.getEquipName(), bies.getOID());
                    modSLTP.addElement(li);
                }
            }
        }
        return modSLTP;
    }

    public static myTreeNode makeObjectClassTree(wakeup up, boolean showAll)
    {
        myTreeNode mrt = new myTreeNode("[物类数据库]", 0, 0);
        session sn = new session(up);
        String sqlTps = "select * from ic_stt_type where sttID = " + systemType.CODE_STT_OBJ_CLS;
        if (showAll == false)
        {
            sqlTps = sqlTps + " and state = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        List lsTps = sn.querySQL(sqlTps, beanSttType.class);
        String sqlOcs = "select OID , className, stpID from tb_object_class_define ";
        if (showAll == false)
        {
            sqlOcs = sqlOcs + " where state = " + iDAO.OBJECT_STATE_ACTIVE;
        }
        List lsOcs = sn.querySQL(sqlOcs, beanObjectClass.class);
        if (lsTps != null)
        {
            for (int i = 0; i < lsTps.size(); i++)
            {
                beanSttType bst = (beanSttType) lsTps.get(i);
                myTreeNode mst = new myTreeNode("[分类]" + bst.getTypeName(), bst.getOID(), 1);
                makeOclsNode(lsOcs, mst, bst.getOID());
                mrt.add(mst);
            }
        }
        return mrt;
    }

    private static void makeOclsNode(List lsOcs, myTreeNode mpar, int tpid)
    {
        if (lsOcs != null)
        {
            for (int i = 0; i < lsOcs.size(); i++)
            {
                beanObjectClass boc = (beanObjectClass) lsOcs.get(i);
                if (boc.getStpID() == tpid)
                {
                    myTreeNode moc = new myTreeNode(boc.getClassName(), boc.getOID(), 2);
                    mpar.add(moc);
                }
            }
        }
    }

    public static myTreeNode makeFlatTypeTree(wakeup up, int itp, boolean showAll)
    {
        myTreeNode mrt = new myTreeNode("[分类]", 0, 0);
        sttType stp = new sttType(up);
        List lts = stp.getRootsByDef(itp, showAll);
        if (lts != null)
        {
            for (int i = 0; i < lts.size(); i++)
            {
                beanSttType bst = (beanSttType) lts.get(i);
                String s = bst.getTypeName();
                if (bst.getState() != iDAO.OBJECT_STATE_ACTIVE)
                {
                    s = s + " [失效]";
                }
                myTreeNode mst = new myTreeNode(s, bst.getOID(), 1);
                mrt.add(mst);
            }
        }
        return mrt;
    }

    public static myTreeNode makeFullTypeTree(wakeup up, int itp, boolean showAll, int exid)
    {
        myTreeNode mrt = new myTreeNode("[分类]", 0, 0);
        sttType stp = new sttType(up);
        List ltps = stp.getTypesByDef(itp, showAll);
        List lts = findTypesByParent(ltps, 0);
        if (lts != null)
        {
            for (int i = 0; i < lts.size(); i++)
            {
                beanSttType bst = (beanSttType) lts.get(i);
                dMakeTypeTree(ltps, mrt, bst, exid);
            }
        }
        return mrt;
    }

    private static List findTypesByParent(List ltps, int parid)
    {
        if (ltps == null)
        {
            return null;
        }
        List lr = new ArrayList();
        for (int i = 0; i < ltps.size(); i++)
        {
            beanSttType bst = (beanSttType) ltps.get(i);
            if (bst.getTypeParent() == parid)
            {
                lr.add(bst);
            }
        }
        return lr;
    }

    private static void dMakeTypeTree(List ltps, myTreeNode mrot, beanSttType btp, int exid)
    {
        if (btp.getOID() == exid)
        {
            return;
        }
        String s = btp.getTypeName();
        if (btp.getState() != iDAO.OBJECT_STATE_ACTIVE)
        {
            s = s + "   [失效]";
        }
        myTreeNode mnd = new myTreeNode(s, btp.getOID(), 1);
        List lcd = findTypesByParent(ltps, btp.getOID());
        if (lcd != null)
        {
            for (int i = 0; i < lcd.size(); i++)
            {
                beanSttType bean = (beanSttType) lcd.get(i);
                dMakeTypeTree(ltps, mnd, bean, exid);
            }
        }
        mrot.add(mnd);
    }

    public static void setCheckBoxSelection(int ick, JCheckBox jck)
    {
        if (ick == iConst.BOL_TRUE)
        {
            jck.setSelected(true);
        }
        else
        {
            jck.setSelected(false);
        }
    }

    public static int getCheckBoxSelection(JCheckBox jck)
    {
        if (jck.isSelected())
        {
            return iConst.BOL_TRUE;
        }
        else
        {
            return iConst.BOL_FALSE;
        }
    }

    public static String makeTargetBeanDesp(wakeup up, int targetType, int targetOID)
    {

        System.err.println("makeTargetText");
        iDAO ido = null;
        switch (targetType)
        {
            case iConst.DT_REQ_TARTYPE_PROP:
                ido = new propertyDefine(up);
                break;
            case iConst.DT_REQ_TARTYPE_ITEM:
                ido = new itemDefine(up);
                break;
            case iConst.DT_REQ_TARTYPE_KEY:
                ido = new keyDataDefine(up);
                break;
            case iConst.DT_REQ_TARTYPE_SKILL:
                ido = new skillDefine(up);
                break;
            default:
                return "?";
        }
        if (ido == null)
        {
            return "?";
        }
        iDataBean idbean = (iDataBean) ido.getRecordByID(targetOID);
        if (idbean == null)
        {
            return "[" + iConst.translate(targetType) + "]" + "null";
        }
        String sr = "[" + iConst.translate(targetType) + "]" + idbean._getDataName() + "<" + idbean._getDataTag() + ">";
        return sr;
    }

    public static iDataBean selectTargetDataBean(int tartp, wakeup up)
    {
        if (tartp == iConst.DT_REQ_TARTYPE_ITEM)
        {
            dlgSelectItemDefine dlg = new dlgSelectItemDefine(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanItem bit = dlg.getSelectedItemDefine();
                return bit;
            }
            dlg.dispose();
            dlg = null;
        }
        else if (tartp == iConst.DT_REQ_TARTYPE_PROP)
        {
            dlgPropertyDefSelector dlg = new dlgPropertyDefSelector(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanPropertyDefine bpd = dlg.getSelectedProp();
                return bpd;
            }
            dlg.dispose();
            dlg = null;
        }
        else if (tartp == iConst.DT_REQ_TARTYPE_KEY)
        {
            dlgKeyDataSelector dlg = new dlgKeyDataSelector(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanKeyDataDefine bkd = dlg.getSelectedKey();
                return bkd;
            }
            dlg.dispose();
            dlg = null;
        }
        else if (tartp == iConst.DT_REQ_TARTYPE_SKILL)
        {
            dlgSelectSkill dlg = new dlgSelectSkill(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanSkillDefine bskd = dlg.getSelectedDefine();
                beanSkillLevel bslv = dlg.getSelectedLevel();
                bskd.setStatus(bslv.getLevelIdx());
                return bskd;
            }
            dlg.dispose();
            dlg = null;
        }
        return null;
    }

    //使用递归方法创建程序接口定义列表树图
    public static myTreeNode makeProgInterfaceDefineTree(wakeup up)
    {
        myTreeNode mrt = new myTreeNode("[程序接口定义数据库]", 0, 0);
        interfaceDefine idef = new interfaceDefine(up);
        sttType stp = new sttType(up);
        List ltps = stp.getRootsByDef(systemType.CODE_STT_PROG_INTERFACE, false);
        dMakeProgIntfDefTypes(up, mrt, ltps, stp, idef);
        return mrt;
    }

    //递归创建程序接口定义列表树图
    private static void dMakeProgIntfDefTypes(wakeup up, myTreeNode mpar, List ltps, sttType stp, interfaceDefine idef)
    {
        if (ltps != null)
        {
            for (int i = 0; i < ltps.size(); i++)
            {
                beanSttType bst = (beanSttType) ltps.get(i);
                myTreeNode mst = new myTreeNode("[分类]" + bst.getTypeName(), 0, 0);
                List lis = idef.getInterfacesByType(bst.getOID(), false);
                if (lis != null)
                {
                    for (int j = 0; j < lis.size(); j++)
                    {
                        beanProgIntfDefine bpid = (beanProgIntfDefine) lis.get(j);
                        myTreeNode mtn = new myTreeNode(bpid.getIntfName() + "<" + bpid.getIntfTag() + ">", bpid.getOID(), 1);
                        mst.add(mtn);
                    }
                }
                List lctps = stp.getChildren(bst.getOID());
                dMakeProgIntfDefTypes(up, mst, lctps, stp, idef);
                mpar.add(mst);
            }
        }
    }

    //预先将接口集模板中的接口定义列表转换为listItem 的LIST
    public static List makeProgInterfaceListItems(wakeup up, beanProgIntfSet bean)
    {
        int[] ids = codeTools.convertStrToArr(bean.getSetInterfaces());
        interfaceDefine idef = new interfaceDefine(up);
        ArrayList lids = new ArrayList();
        for (int i = 0; i < ids.length; i++)
        {
            int id = ids[i];
            beanProgIntfDefine bpid = (beanProgIntfDefine) idef.getRecordByID(id);
            if (bpid != null)
            {
                listItem li = new listItem(bpid.getIntfName() + "<" + bpid.getIntfTag() + ">", bpid.getOID());
                lids.add(li);
            }
        }
        return lids;
    }

    //根据指定的/或不指定接口集模板创建一个接口模板树图
    public static myTreeNode makeProgInterfaceSetTree(wakeup up, String isetTag, boolean showAll)
    {
        myTreeNode mrt = new myTreeNode("[接口集模板]", 0, 0);
        sttType stp = new sttType(up);
        interfaceSet iset = new interfaceSet(up);
        List ltps = stp.getRootsByDef(systemType.CODE_STT_PROG_INTF_SET, false);
        if (ltps != null)
        {
            for (int i = 0; i < ltps.size(); i++)
            {
                beanSttType bst = (beanSttType) ltps.get(i);
                myTreeNode mst = new myTreeNode("[分类]" + bst.getTypeName(), 0, 0);
                List lset = iset.getIntfSetsByType(bst.getOID(), showAll);
                if (lset != null)
                {
                    for (int j = 0; j < lset.size(); j++)
                    {
                        beanProgIntfSet bpis = (beanProgIntfSet) lset.get(j);
                        if (isetTag.trim().equals("") == false)
                        {
                            if (bpis.getSetTag().trim().equals(isetTag) == false)
                            {
                                continue;
                            }
                        }
                        myTreeNode mset = new myTreeNode(bpis.getSetName() + "<" + bpis.getSetTag() + ">", bpis.getOID(), 1);
                        mst.add(mset);
                    }
                }
                if (mst.getChildCount() != 0)
                {
                    mrt.add(mst);
                }
            }
        }
        return mrt;
    }

    //通过一个指定的程序集模板，创建一个程序实现树图
    public static myTreeNode makeProgImplTreeByISET(wakeup up, int isid, boolean showAll, boolean showFullAddr)
    {
        interfaceSet iset = new interfaceSet(up);
        interfaceDefine idef = new interfaceDefine(up);
        interfaceRegister ireg = new interfaceRegister(up);
        myTreeNode mrt = new myTreeNode("[程序接口目录]", 0, 0);
        beanProgIntfSet bset = (beanProgIntfSet) iset.getRecordByID(isid);
        if (bset != null)
        {
            int[] ids = codeTools.convertStrToArr(bset.getSetInterfaces());
            for (int i = 0; i < ids.length; i++)
            {
                int idid = ids[i];
                beanProgIntfDefine bpid = (beanProgIntfDefine) idef.getRecordByID(idid);
                if (bpid != null)
                {
                    myTreeNode mdef = new myTreeNode("[接口]" + bpid.getIntfName(), 0, 0);
                    List lrgs = ireg.getRegsByDef(bpid.getOID(), showAll);
                    if (lrgs != null)
                    {
                        for (int j = 0; j < lrgs.size(); j++)
                        {
                            beanProgIntfRegister bpir = (beanProgIntfRegister) lrgs.get(j);
                            String s = bpir.getRegDescription() + "<" + bpir.getRegTag() + ">";
                            if (showFullAddr)
                            {
                                s = s + " [" + bpir.getImplAddress() + "]";
                            }
                            myTreeNode mrg = new myTreeNode(s, bpir.getOID(), 1);
                            mdef.add(mrg);
                        }
                    }
                    mrt.add(mdef);
                }
            }
        }
        return mrt;
    }

    /**
     * 公开：通过一个接口集模板，选择一个
     *
     * @param up
     * @param isetCode
     * @return
     */
    public static beanProgIntfRegister selectIntfImpl(wakeup up, String isetCode)
    {
        dlgSelectIntfImplsByISET dlg = new dlgSelectIntfImplsByISET(null, true, up, isetCode);
        System.err.println(".isc = " + isetCode);
        dlg.setVisible(true);
        beanProgIntfRegister bpir = null;
        if (dlg.getOK())
        {
            bpir = dlg.getSelectedReg();
        }
        dlg.dispose();
        dlg = null;
        return bpir;
    }

    /**
     * 选择任务格式模板
     *
     * @param up
     * @return
     */
    public static beanProgIntfSet selectProgIntfSet(wakeup up)
    {
        dlgSelectIntfSet dlg = new dlgSelectIntfSet(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            beanProgIntfSet bpis = dlg.getSelectedIntfSet();
            return bpis;
        }
        dlg.dispose();
        dlg = null;
        return null;
    }

    /**
     * 尝试读取一个接口实现的数据行，并制作其展示字符串
     *
     * @param up
     * @param rfid
     * @return
     */
    public static String tryLoadIntfReg(wakeup up, int rfid)
    {
        interfaceRegister ireg = new interfaceRegister(up);
        if (rfid == 0)
        {
            return "[空调用]";
        }
        else
        {

            beanProgIntfRegister bpir = (beanProgIntfRegister) ireg.getRecordByID(rfid);
            if (bpir != null)
            {
                String s = "";
                s = bpir.getRegName() + "<" + bpir.getRegTag() + ">";
                if (bpir.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
                {
                    s = s + " [已失效]";
                }
                return s;
            }
            else
            {
                return "[空调用]";
            }
        }
    }

    /**
     * 尝试读取接口集合的数据记录，并制作展示文本
     *
     * @param up
     * @param isid
     * @return
     */
    public static String tryLoadIntfSet(wakeup up, int isid)
    {
        if (isid != 0)
        {
            interfaceSet iset = new interfaceSet(up);
            beanProgIntfSet bpis = (beanProgIntfSet) iset.getRecordByID(isid);
            if (bpis != null)
            {
                return bpis.getSetName() + "<" + bpis.getSetTag() + ">";
            }
            else
            {
                return "[通用格式]";
            }
        }
        else
        {
            return "[通用格式]";
        }
    }

}

//
//LOG
//TIME:
//REC:
//
