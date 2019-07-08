package dev.xlin.gameworkshop.GUI.panes;

import dev.xlin.gameworkshop.GUI.dialog.DlgFuncControlNode;
import dev.xlin.gameworkshop.GUI.dialog.DlgFuncEffectItem;
import dev.xlin.gameworkshop.GUI.dialog.DlgFuncEnableItem;
import dev.xlin.gameworkshop.GUI.dialog.DlgFuncPropItem;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiDBLKPalCode;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanFuncControl;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanFuncEffectListItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanFuncEnableListItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanFuncPropListItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanKeyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanPropertyDefine;
import dev.xlin.gameworkshop.progs.foundation.datablockService;
import dev.xlin.gameworkshop.progs.foundation.functionDefineData;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockGUIPal;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.keyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class PalFunctionDataDefine extends javax.swing.JPanel implements iAdtDocumentSave, iDatablockFace, iDatablockGUIPal
{

    private functionDefineData fdd = null;
    private wakeup up = null;
    private long dbIndex = 0;
    private boolean needSave = false;
    private int oclsID = 0;

    private boolean bInitProp = false;
    private List lprops = null;

    private boolean bInitEffect = false;
    private List leffts = null;
    private boolean bInitEnable = false;
    private List lenbs = null;

    propertyDefine pdef = null;
    keyDataDefine kdef = null;
    itemDefine idef = null;

    public PalFunctionDataDefine()
    {
        initComponents();
    }

    private void initData()
    {
        pdef = new propertyDefine(up);
        kdef = new keyDataDefine(up);
        idef = new itemDefine(up);
        datablockService dbs = new datablockService(up);
        String sblock = dbs.loadData(dbIndex);
        xmlRight xr = new xmlRight();
        boolean b = xr.parseXMLfromString(sblock);
        if (b)
        {
            boolean b2 = fdd.revertFromXML(xr);
        }
        else
        {
        }
        showFuncControl();
        makePropTable();
        makeEffectTable();
        makeEnableTable();
    }

    private void makeEffectTable()
    {
        myTableModel mtm = null;
        if (bInitEffect == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("描述文本");
            mtm.addColumn("目标类型");
            mtm.addColumn("目标");
            mtm.addColumn("值方法");
            mtm.addColumn("值");
            mtm.addColumn("效果冲突");
            mtm.addColumn("隐藏");
            mtm.addColumn("状态");
            bInitEffect = true;
        }
        else
        {
            fast.clearTableModel(tbEfts);
            mtm = (myTableModel) tbEfts.getModel();
        }
        leffts = fdd.getEffectList(ckShowAll.isSelected());

        if (leffts != null)
        {
            for (int i = 0; i < leffts.size(); i++)
            {
                BeanFuncEffectListItem beft = (BeanFuncEffectListItem) leffts.get(i);
                Vector v = new Vector();
                v.add(beft.getDescription());
                v.add(iConst.translate(beft.getTargetType()));
                //v.add(beft.getTargetOID());
                if (beft.getTargetType() == iConst.FUNC_EFT_TAR_KEY)
                {
                    BeanKeyDataDefine bkey = (BeanKeyDataDefine) kdef.getRecordByID(beft.getTargetOID());
                    if (bkey != null)
                    {
                        v.add(bkey.getKeyName() + "<" + bkey.getKeyTag() + ">");
                    }
                    else
                    {
                        v.add("[null]");
                    }
                }
                else if (beft.getTargetType() == iConst.FUNC_EFT_TAR_PROP)
                {
                    BeanPropertyDefine bprop = (BeanPropertyDefine) pdef.getRecordByID(beft.getTargetOID());
                    if (bprop != null)
                    {
                        v.add(bprop.getPropName() + "<" + bprop.getPropTag() + ">");
                    }
                    else
                    {
                        v.add("[null]");
                    }
                }
                else
                {
                    v.add("-");
                }
                v.add(iConst.translate(beft.getValueMethod()));
                v.add(beft.getEffectValue());
                v.add(iConst.translate(beft.getSameEffectMethod()));
                v.add(iConst.translateIBOL(beft.getHide()));
                v.add(iConst.transDAOState(beft.getStatus()));
                mtm.addRow(v);
            }
        }
        tbEfts.setModel(mtm);
    }

    private void makeEnableTable()
    {
        myTableModel mtm = null;
        if (bInitEnable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("目标类型");
            mtm.addColumn("目标");
            mtm.addColumn("方法");
            mtm.addColumn("数量");
            mtm.addColumn("隐藏");
            mtm.addColumn("状态");
            tbEnbs.setModel(mtm);
            bInitEnable = true;
        }
        else
        {
            fast.clearTableModel(tbEnbs);
            mtm = (myTableModel) tbEnbs.getModel();
        }
        lenbs = fdd.getEnableList(ckShowAll.isSelected());
        for (int i = 0; i < lenbs.size(); i++)
        {
            BeanFuncEnableListItem bean = (BeanFuncEnableListItem) lenbs.get(i);
            Vector v = new Vector();
            v.add(iConst.translate(bean.getTargetType()));
            String stxt = guiCodes.makeTargetBeanDesp(up, bean.getTargetType(), bean.getTargetOID());
            v.add(stxt);
            v.add(iConst.translate(bean.getMethod()));
            v.add(bean.getValue());
            v.add(iConst.translateIBOL(bean.getHide()));
            v.add(iConst.transDAOState(bean.getStatus()));
            mtm.addRow(v);
        }
        tbEnbs.setModel(mtm);
    }

    private void makePropTable()
    {
        myTableModel mtm = null;
        if (bInitProp == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("属性");
            mtm.addColumn("值");
            mtm.addColumn("描述");
            mtm.addColumn("隐藏");
            mtm.addColumn("状态");
            tbProps.setModel(mtm);
            bInitProp = true;
        }
        else
        {
            fast.clearTableModel(tbProps);
            mtm = (myTableModel) tbProps.getModel();
        }
        lprops = fdd.getPropList(ckShowAll.isSelected());
        if (lprops != null)
        {
            for (int i = 0; i < lprops.size(); i++)
            {
                BeanFuncPropListItem bpp = (BeanFuncPropListItem) lprops.get(i);
                Vector v = new Vector();
                BeanPropertyDefine bpd = (BeanPropertyDefine) pdef.getRecordByID(bpp.getPropOID());
                v.add(bpd.getPropName() + "<" + bpd.getPropTag() + ">");
                v.add(bpp.getValue());
                v.add(bpp.getDescription());
                v.add(iConst.translateIBOL(bpp.getHide()));
                v.add(iConst.transDAOState(bpp.getStatus()));
                mtm.addRow(v);
            }
        }
        tbProps.setModel(mtm);
    }

    private void doNewProp()
    {
        DlgFuncPropItem dlg = new DlgFuncPropItem(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanFuncPropListItem bean = dlg.getFuncPropListItem();
            int r = fdd.appendFuncProperty(bean);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makePropTable();
            }
            else
            {
                fast.err("添加属性操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void showFuncControl()
    {
        BeanFuncControl bfc = fdd.getFunctionControl();
        txtActSide.setText(iConst.translate(bfc.getActionSide()));
        txtAutoloop.setText(iConst.translateIBOL(bfc.getAutoLoop()));
        txtCooldown.setText(bfc.getCooldown() + "");
        txtDescription.setText(bfc.getDescription());
        txtDisableImpl.setText(bfc.getDisableImplements());
        txtEnableImpl.setText(bfc.getEnableImplements());
        txtInitEnv.setText(bfc.getInitFuncEnv());
        txtLoopImpl.setText(bfc.getLoopImplenments());
        txtName.setText(bfc.getFuncName());
        txtPeriod.setText(bfc.getPeriod() + "");
        txtTag.setText(bfc.getFuncTag());
        txtTargetRegion.setText(iConst.translate(bfc.getTargetRange()));
    }

    private void doSave(boolean showMsg)
    {
        datablockService dbs = new datablockService(up);
        xmlRight xr = fdd.transToXML();
        String sxml = xr.transformToString();
        int r = dbs.updateData(dbIndex, sxml);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            fdd.resetSave();
            if (showMsg)
            {
                fast.msg("保存完成");
            }
        }
        else
        {
            if (showMsg)
            {
                fast.err("保存失败", r);
            }
        }
    }

    private void doUpdateFuncCtrl()
    {
        BeanFuncControl bfc = fdd.getFunctionControl();
        DlgFuncControlNode dlg = new DlgFuncControlNode(null, true, up, bfc);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            bfc = dlg.getFuncControl();
            int r = fdd.updateFunctionControl(bfc);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                showFuncControl();
            }
            else
            {
                fast.err("数据操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanFuncPropListItem getSelectedProp()
    {
        int idx = tbProps.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanFuncPropListItem) lprops.get(idx);
    }

    private void doEditProp()
    {
        BeanFuncPropListItem bean = getSelectedProp();
        if (bean == null)
        {
            return;
        }
        DlgFuncPropItem dlg = new DlgFuncPropItem(null, true, up, bean);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanFuncPropListItem bedit = dlg.getFuncPropListItem();
            int r = fdd.updateFuncProperty(bedit);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makePropTable();
            }
            else
            {
                fast.err("修改属性数据操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDisProp()
    {
        BeanFuncPropListItem bean = getSelectedProp();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认失效这个属性节点？");
        if (sel != fast.YES)
        {
            return;
        }
        int r = fdd.disableFuncNode(functionDefineData.keyPropertyList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makePropTable();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doRevProp()
    {
        BeanFuncPropListItem bean = getSelectedProp();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认恢复这个属性节点？");
        if (sel != fast.YES)
        {
            return;
        }
        int r = fdd.revertFuncNode(functionDefineData.keyPropertyList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makePropTable();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void doDesProp()
    {
        BeanFuncPropListItem bean = getSelectedProp();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认销毁这个属性节点？");
        if (sel != fast.YES)
        {
            return;
        }
        int r = fdd.destroyFuncNode(functionDefineData.keyPropertyList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makePropTable();
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void doMoveDownProp()
    {
        BeanFuncPropListItem bean = getSelectedProp();
        if (bean == null)
        {
            return;
        }
        int r = fdd.moveFuncNodeDown(functionDefineData.keyPropertyList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makePropTable();
        }
    }

    private void doMoveUpProp()
    {
        BeanFuncPropListItem bean = getSelectedProp();
        if (bean == null)
        {
            return;
        }
        int r = fdd.moveFuncNodeUp(functionDefineData.keyPropertyList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makePropTable();
        }
    }

    private void doNewEffect()
    {
        DlgFuncEffectItem dlg = new DlgFuncEffectItem(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanFuncEffectListItem bean = dlg.getEffectListItem();
            int r = fdd.appendFuncEffect(bean);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeEffectTable();
                fast.msg("添加效果数据操作完成");
            }
            else
            {
                fast.err("添加效果数据操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanFuncEffectListItem getSelectedEffectListItem()
    {
        int idx = tbEfts.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanFuncEffectListItem) leffts.get(idx);
    }

    private void doEditEffect()
    {
        BeanFuncEffectListItem bean = getSelectedEffectListItem();
        if (bean == null)
        {
            return;
        }
        DlgFuncEffectItem dlg = new DlgFuncEffectItem(null, true, up, bean);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanFuncEffectListItem b = dlg.getEffectListItem();
            int r = fdd.updateFuncEffect(b);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeEffectTable();
                fast.msg("添加效果数据操作完成");
            }
            else
            {
                fast.err("添加效果数据操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDisEffect()
    {
        BeanFuncEffectListItem bean = getSelectedEffectListItem();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否失效这个数据项");
        if (sel != fast.YES)
        {
            return;
        }
        int r = fdd.disableFuncNode(functionDefineData.keyEffecttList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeEffectTable();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doRevEffect()
    {
        BeanFuncEffectListItem bean = getSelectedEffectListItem();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否恢复这个数据项");
        if (sel != fast.YES)
        {
            return;
        }
        int r = fdd.revertFuncNode(functionDefineData.keyEffecttList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeEffectTable();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void doDesEffect()
    {
        BeanFuncEffectListItem bean = getSelectedEffectListItem();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否销毁这个数据项");
        if (sel != fast.YES)
        {
            return;
        }
        int r = fdd.destroyFuncNode(functionDefineData.keyEffecttList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeEffectTable();
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void doMoveUpEffect()
    {
        BeanFuncEffectListItem bean = getSelectedEffectListItem();
        if (bean == null)
        {
            return;
        }
        int r = fdd.moveFuncNodeUp(functionDefineData.keyEffecttList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeEffectTable();
        }
    }

    private void doMoveDownEffect()
    {
        BeanFuncEffectListItem bean = getSelectedEffectListItem();
        if (bean == null)
        {
            return;
        }
        int r = fdd.moveFuncNodeDown(functionDefineData.keyEffecttList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeEffectTable();
        }
    }

    private void doNewEnable()
    {
        DlgFuncEnableItem dlg = new DlgFuncEnableItem(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            int r = fdd.appendFuncEnableItem(dlg.getFuncEnableListItem());
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeEnableTable();
                fast.msg("添加启动参数数据项操作完成");
            }
            else
            {
                fast.err("添加启动参数数据项操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void doEditEnable()
    {
        BeanFuncEnableListItem bean = getSelectedEnableListItem();
        if (bean == null)
        {
            return;
        }
        DlgFuncEnableItem dlg = new DlgFuncEnableItem(null, true, up, bean);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            int r = fdd.updateFuncEnableItem(bean);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeEnableTable();
                fast.msg("修改启动参数数据项操作完成");
            }
            else
            {
                fast.err("修改启动参数数据项操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanFuncEnableListItem getSelectedEnableListItem()
    {
        int idx = tbEnbs.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanFuncEnableListItem) lenbs.get(idx);
    }

    private void doDisEnable()
    {
        BeanFuncEnableListItem bean = getSelectedEnableListItem();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否失效这个数据项");
        if (sel != fast.YES)
        {
            return;
        }
        int r = fdd.disableFuncNode(functionDefineData.keyEnableList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeEnableTable();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doRevEnable()
    {
        BeanFuncEnableListItem bean = getSelectedEnableListItem();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否恢复这个数据项");
        if (sel != fast.YES)
        {
            return;
        }
        int r = fdd.revertFuncNode(functionDefineData.keyEnableList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeEnableTable();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void doDesEnable()
    {
        BeanFuncEnableListItem bean = getSelectedEnableListItem();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否销毁这个数据项");
        if (sel != fast.YES)
        {
            return;
        }
        int r = fdd.destroyFuncNode(functionDefineData.keyEnableList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeEnableTable();
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void doMoveUpEnable()
    {
        BeanFuncEnableListItem bean = getSelectedEnableListItem();
        if (bean == null)
        {
            return;
        }
        int r = fdd.moveFuncNodeUp(functionDefineData.keyEnableList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeEnableTable();
        }
    }

    private void doMoveDownEnable()
    {
        BeanFuncEnableListItem bean = getSelectedEnableListItem();
        if (bean == null)
        {
            return;
        }
        int r = fdd.moveFuncNodeDown(functionDefineData.keyEnableList, bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeEnableTable();
        }
    }

    private void doSaveAsTemplet()
    {
        int r = guiDBLKPalCode.guiSaveAsTemplet(up, oclsID, fdd, dbIndex);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            fast.msg("已经将这个数据配置保存为模板");
        }
        else
        {
            fast.err("保存模板过程操作失败或者未完成", r);
        }
    }

    private void doImportTemplet()
    {
        boolean b = guiDBLKPalCode.guiImportTemplet(up, fdd);
        if (b)
        {
            showFuncControl();
            makeEffectTable();
            makeEnableTable();
            makePropTable();
        }
        else
        {
            fast.err("导入数据操作失败或者未完成");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popProp = new javax.swing.JPopupMenu();
        miNewProp = new javax.swing.JMenuItem();
        miEditProp = new javax.swing.JMenuItem();
        miDisProp = new javax.swing.JMenuItem();
        miRevProp = new javax.swing.JMenuItem();
        miDesProp = new javax.swing.JMenuItem();
        miUpProp = new javax.swing.JMenuItem();
        miDownProp = new javax.swing.JMenuItem();
        popEffs = new javax.swing.JPopupMenu();
        miNewEff = new javax.swing.JMenuItem();
        miEditEff = new javax.swing.JMenuItem();
        miDisEff = new javax.swing.JMenuItem();
        miRevEff = new javax.swing.JMenuItem();
        miDesEff = new javax.swing.JMenuItem();
        miUpEff = new javax.swing.JMenuItem();
        miDownEff = new javax.swing.JMenuItem();
        popEnbs = new javax.swing.JPopupMenu();
        miNewEnb = new javax.swing.JMenuItem();
        miEditEnb = new javax.swing.JMenuItem();
        miDisEnb = new javax.swing.JMenuItem();
        miRevEnb = new javax.swing.JMenuItem();
        miDesEnb = new javax.swing.JMenuItem();
        miUpEnb = new javax.swing.JMenuItem();
        miDownEnb = new javax.swing.JMenuItem();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtTag = new javax.swing.JTextField();
        txtPeriod = new javax.swing.JTextField();
        txtCooldown = new javax.swing.JTextField();
        txtAutoloop = new javax.swing.JTextField();
        txtTargetRegion = new javax.swing.JTextField();
        txtDescription = new javax.swing.JTextField();
        txtEnableImpl = new javax.swing.JTextField();
        txtLoopImpl = new javax.swing.JTextField();
        txtDisableImpl = new javax.swing.JTextField();
        txtInitEnv = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        btnUpdateControl = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtActSide = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProps = new javax.swing.JTable();
        tbarProps = new javax.swing.JToolBar();
        btnAddProp = new javax.swing.JButton();
        btnEditProp = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnMovePropUp = new javax.swing.JButton();
        btnMovePropDown = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        btnPopProp = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbEfts = new javax.swing.JTable();
        tbarEffs = new javax.swing.JToolBar();
        btnNewEffect = new javax.swing.JButton();
        tbrPopEff = new javax.swing.JToolBar();
        btnPopEffs = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbEnbs = new javax.swing.JTable();
        tbarEnb = new javax.swing.JToolBar();
        btnNewEnable = new javax.swing.JButton();
        tbrPopEnb = new javax.swing.JToolBar();
        btnPopEnbs = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        ckShowAll = new javax.swing.JCheckBox();
        btnFastSave = new javax.swing.JButton();
        btnSaveAsTemplet = new javax.swing.JButton();
        btnImportTemplet = new javax.swing.JButton();

        miNewProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewProp.setText("添加属性");
        miNewProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewPropActionPerformed(evt);
            }
        });
        popProp.add(miNewProp);

        miEditProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditProp.setText("修改属性");
        miEditProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditPropActionPerformed(evt);
            }
        });
        popProp.add(miEditProp);

        miDisProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisProp.setText("失效属性");
        miDisProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisPropActionPerformed(evt);
            }
        });
        popProp.add(miDisProp);

        miRevProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevProp.setText("恢复属性");
        miRevProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevPropActionPerformed(evt);
            }
        });
        popProp.add(miRevProp);

        miDesProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesProp.setText("销毁属性");
        miDesProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesPropActionPerformed(evt);
            }
        });
        popProp.add(miDesProp);

        miUpProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miUpProp.setText("上移");
        miUpProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miUpPropActionPerformed(evt);
            }
        });
        popProp.add(miUpProp);

        miDownProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDownProp.setText("下移");
        miDownProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDownPropActionPerformed(evt);
            }
        });
        popProp.add(miDownProp);

        miNewEff.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewEff.setText("添加效果");
        miNewEff.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewEffActionPerformed(evt);
            }
        });
        popEffs.add(miNewEff);

        miEditEff.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditEff.setText("修改效果");
        miEditEff.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditEffActionPerformed(evt);
            }
        });
        popEffs.add(miEditEff);

        miDisEff.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisEff.setText("失效效果");
        miDisEff.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisEffActionPerformed(evt);
            }
        });
        popEffs.add(miDisEff);

        miRevEff.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevEff.setText("恢复效果");
        miRevEff.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevEffActionPerformed(evt);
            }
        });
        popEffs.add(miRevEff);

        miDesEff.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesEff.setText("销毁效果");
        miDesEff.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesEffActionPerformed(evt);
            }
        });
        popEffs.add(miDesEff);

        miUpEff.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miUpEff.setText("上移");
        miUpEff.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miUpEffActionPerformed(evt);
            }
        });
        popEffs.add(miUpEff);

        miDownEff.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDownEff.setText("下移");
        miDownEff.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDownEffActionPerformed(evt);
            }
        });
        popEffs.add(miDownEff);

        miNewEnb.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewEnb.setText("添加参数");
        miNewEnb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewEnbActionPerformed(evt);
            }
        });
        popEnbs.add(miNewEnb);

        miEditEnb.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditEnb.setText("修改参数");
        miEditEnb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditEnbActionPerformed(evt);
            }
        });
        popEnbs.add(miEditEnb);

        miDisEnb.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisEnb.setText("失效参数");
        miDisEnb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisEnbActionPerformed(evt);
            }
        });
        popEnbs.add(miDisEnb);

        miRevEnb.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevEnb.setText("恢复参数");
        miRevEnb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevEnbActionPerformed(evt);
            }
        });
        popEnbs.add(miRevEnb);

        miDesEnb.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesEnb.setText("销毁参数");
        miDesEnb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesEnbActionPerformed(evt);
            }
        });
        popEnbs.add(miDesEnb);

        miUpEnb.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miUpEnb.setText("上移");
        miUpEnb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miUpEnbActionPerformed(evt);
            }
        });
        popEnbs.add(miUpEnb);

        miDownEnb.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDownEnb.setText("下移");
        miDownEnb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDownEnbActionPerformed(evt);
            }
        });
        popEnbs.add(miDownEnb);

        jLabel1.setText("功能名称");

        jLabel2.setText("功能标签");

        jLabel3.setText("持续周期");

        jLabel4.setText("冷却周期");

        jLabel5.setText("自动重复");

        jLabel6.setText("描述文本");

        jLabel7.setText("作用范围");

        jLabel8.setText("启动接口");

        jLabel9.setText("循环接口");

        jLabel10.setText("关闭接口");

        jLabel11.setText("环境接口");

        txtName.setEditable(false);

        txtTag.setEditable(false);

        txtPeriod.setEditable(false);
        txtPeriod.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPeriodActionPerformed(evt);
            }
        });

        txtCooldown.setEditable(false);

        txtAutoloop.setEditable(false);

        txtTargetRegion.setEditable(false);

        txtDescription.setEditable(false);

        txtEnableImpl.setEditable(false);
        txtEnableImpl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtEnableImplActionPerformed(evt);
            }
        });

        txtLoopImpl.setEditable(false);

        txtDisableImpl.setEditable(false);

        txtInitEnv.setEditable(false);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnUpdateControl.setText("设置控制数据");
        btnUpdateControl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnUpdateControlActionPerformed(evt);
            }
        });

        jLabel12.setText("作用方面");

        txtActSide.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtTag))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtPeriod))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtCooldown))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtAutoloop))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtDescription))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtTargetRegion))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtEnableImpl))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtLoopImpl))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtDisableImpl))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtInitEnv))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtActSide)))
                    .addComponent(btnUpdateControl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnUpdateControl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCooldown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtAutoloop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTargetRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtActSide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtEnableImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtLoopImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtDisableImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtInitEnv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator1)
        );

        tabs.addTab("功能控制数据", jPanel1);

        tbProps.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbProps.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbPropsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbProps);

        tbarProps.setFloatable(false);
        tbarProps.setRollover(true);

        btnAddProp.setText("添加属性数据");
        btnAddProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddPropActionPerformed(evt);
            }
        });
        tbarProps.add(btnAddProp);

        btnEditProp.setText("修改属性");
        btnEditProp.setToolTipText("");
        btnEditProp.setFocusable(false);
        btnEditProp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditProp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditPropActionPerformed(evt);
            }
        });
        tbarProps.add(btnEditProp);
        tbarProps.add(jSeparator2);

        btnMovePropUp.setText("上移");
        btnMovePropUp.setFocusable(false);
        btnMovePropUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMovePropUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMovePropUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMovePropUpActionPerformed(evt);
            }
        });
        tbarProps.add(btnMovePropUp);

        btnMovePropDown.setText("下移");
        btnMovePropDown.setFocusable(false);
        btnMovePropDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMovePropDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMovePropDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMovePropDownActionPerformed(evt);
            }
        });
        tbarProps.add(btnMovePropDown);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnPopProp.setForeground(new java.awt.Color(0, 0, 204));
        btnPopProp.setText("↓");
        btnPopProp.setFocusable(false);
        btnPopProp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopProp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPopProp.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPopPropMouseClicked(evt);
            }
        });
        btnPopProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopPropActionPerformed(evt);
            }
        });
        jToolBar2.add(btnPopProp);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(tbarProps, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbarProps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jToolBar2, tbarProps});

        tabs.addTab("控制属性列表", jPanel2);

        tbEfts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbEfts.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbEftsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbEfts);

        tbarEffs.setFloatable(false);
        tbarEffs.setRollover(true);

        btnNewEffect.setText("添加效果数据");
        btnNewEffect.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewEffectActionPerformed(evt);
            }
        });
        tbarEffs.add(btnNewEffect);

        tbrPopEff.setFloatable(false);
        tbrPopEff.setRollover(true);

        btnPopEffs.setForeground(new java.awt.Color(0, 0, 204));
        btnPopEffs.setText("↓");
        btnPopEffs.setFocusable(false);
        btnPopEffs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopEffs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPopEffs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPopEffsMouseClicked(evt);
            }
        });
        btnPopEffs.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopEffsActionPerformed(evt);
            }
        });
        tbrPopEff.add(btnPopEffs);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(tbarEffs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbrPopEff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbrPopEff, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbarEffs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tbarEffs, tbrPopEff});

        tabs.addTab("效果数据列表", jPanel3);

        tbEnbs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbEnbs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbEnbsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbEnbs);

        tbarEnb.setFloatable(false);
        tbarEnb.setRollover(true);

        btnNewEnable.setText("添加启用参数");
        btnNewEnable.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewEnableActionPerformed(evt);
            }
        });
        tbarEnb.add(btnNewEnable);

        tbrPopEnb.setFloatable(false);
        tbrPopEnb.setRollover(true);

        btnPopEnbs.setForeground(new java.awt.Color(0, 0, 204));
        btnPopEnbs.setText("↓");
        btnPopEnbs.setFocusable(false);
        btnPopEnbs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopEnbs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPopEnbs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPopEnbsMouseClicked(evt);
            }
        });
        btnPopEnbs.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopEnbsActionPerformed(evt);
            }
        });
        tbrPopEnb.add(btnPopEnbs);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(tbarEnb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbrPopEnb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbrPopEnb, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbarEnb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tbarEnb, tbrPopEnb});

        tabs.addTab("启用参数列表", jPanel4);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        ckShowAll.setText("显示全部");
        ckShowAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllActionPerformed(evt);
            }
        });
        jToolBar1.add(ckShowAll);

        btnFastSave.setText("快速保存");
        btnFastSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFastSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFastSave);

        btnSaveAsTemplet.setText("存为模板");
        btnSaveAsTemplet.setFocusable(false);
        btnSaveAsTemplet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveAsTemplet.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveAsTemplet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveAsTempletActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSaveAsTemplet);

        btnImportTemplet.setText("导入模板");
        btnImportTemplet.setFocusable(false);
        btnImportTemplet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImportTemplet.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImportTemplet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnImportTempletActionPerformed(evt);
            }
        });
        jToolBar1.add(btnImportTemplet);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tabs))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ckShowAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllActionPerformed
    {//GEN-HEADEREND:event_ckShowAllActionPerformed
        makePropTable();
        makeEffectTable();
        makeEnableTable();
    }//GEN-LAST:event_ckShowAllActionPerformed

    private void btnFastSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFastSaveActionPerformed
    {//GEN-HEADEREND:event_btnFastSaveActionPerformed
        doSave(true);
    }//GEN-LAST:event_btnFastSaveActionPerformed

    private void txtPeriodActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtPeriodActionPerformed
    {//GEN-HEADEREND:event_txtPeriodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPeriodActionPerformed

    private void txtEnableImplActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtEnableImplActionPerformed
    {//GEN-HEADEREND:event_txtEnableImplActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEnableImplActionPerformed

    private void btnUpdateControlActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnUpdateControlActionPerformed
    {//GEN-HEADEREND:event_btnUpdateControlActionPerformed
        doUpdateFuncCtrl();
    }//GEN-LAST:event_btnUpdateControlActionPerformed

    private void btnAddPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddPropActionPerformed
    {//GEN-HEADEREND:event_btnAddPropActionPerformed
        doNewProp();
    }//GEN-LAST:event_btnAddPropActionPerformed

    private void tbPropsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbPropsMouseClicked
    {//GEN-HEADEREND:event_tbPropsMouseClicked
        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            doEditProp();
        }
        else if (evt.getButton() == evt.BUTTON3)
        {
            popProp.show(tbProps, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbPropsMouseClicked

    private void miNewPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewPropActionPerformed
    {//GEN-HEADEREND:event_miNewPropActionPerformed
        doNewProp();
    }//GEN-LAST:event_miNewPropActionPerformed

    private void miEditPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditPropActionPerformed
    {//GEN-HEADEREND:event_miEditPropActionPerformed
        doEditProp();
    }//GEN-LAST:event_miEditPropActionPerformed

    private void miDisPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisPropActionPerformed
    {//GEN-HEADEREND:event_miDisPropActionPerformed
        doDisProp();
    }//GEN-LAST:event_miDisPropActionPerformed

    private void miRevPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevPropActionPerformed
    {//GEN-HEADEREND:event_miRevPropActionPerformed
        doRevProp();
    }//GEN-LAST:event_miRevPropActionPerformed

    private void miDesPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesPropActionPerformed
    {//GEN-HEADEREND:event_miDesPropActionPerformed
        doDesProp();
    }//GEN-LAST:event_miDesPropActionPerformed

    private void miUpPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miUpPropActionPerformed
    {//GEN-HEADEREND:event_miUpPropActionPerformed
        doMoveUpProp();
    }//GEN-LAST:event_miUpPropActionPerformed

    private void miDownPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDownPropActionPerformed
    {//GEN-HEADEREND:event_miDownPropActionPerformed
        doMoveDownProp();
    }//GEN-LAST:event_miDownPropActionPerformed

    private void btnNewEffectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewEffectActionPerformed
    {//GEN-HEADEREND:event_btnNewEffectActionPerformed
        doNewEffect();
    }//GEN-LAST:event_btnNewEffectActionPerformed

    private void miNewEffActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewEffActionPerformed
    {//GEN-HEADEREND:event_miNewEffActionPerformed
        doNewEffect();
    }//GEN-LAST:event_miNewEffActionPerformed

    private void miEditEffActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditEffActionPerformed
    {//GEN-HEADEREND:event_miEditEffActionPerformed
        doEditEffect();
    }//GEN-LAST:event_miEditEffActionPerformed

    private void miDisEffActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisEffActionPerformed
    {//GEN-HEADEREND:event_miDisEffActionPerformed
        doDisEffect();
    }//GEN-LAST:event_miDisEffActionPerformed

    private void miRevEffActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevEffActionPerformed
    {//GEN-HEADEREND:event_miRevEffActionPerformed
        doRevEffect();
    }//GEN-LAST:event_miRevEffActionPerformed

    private void miDesEffActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesEffActionPerformed
    {//GEN-HEADEREND:event_miDesEffActionPerformed
        doDesEffect();
    }//GEN-LAST:event_miDesEffActionPerformed

    private void miUpEffActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miUpEffActionPerformed
    {//GEN-HEADEREND:event_miUpEffActionPerformed
        doMoveUpEffect();
    }//GEN-LAST:event_miUpEffActionPerformed

    private void miDownEffActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDownEffActionPerformed
    {//GEN-HEADEREND:event_miDownEffActionPerformed
        doMoveDownEffect();
    }//GEN-LAST:event_miDownEffActionPerformed

    private void tbEftsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbEftsMouseClicked
    {//GEN-HEADEREND:event_tbEftsMouseClicked
        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            doEditEffect();
        }
        else if (evt.getButton() == evt.BUTTON3)
        {
            popEffs.show(tbEfts, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbEftsMouseClicked

    private void btnNewEnableActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewEnableActionPerformed
    {//GEN-HEADEREND:event_btnNewEnableActionPerformed
        doNewEnable();
    }//GEN-LAST:event_btnNewEnableActionPerformed

    private void miNewEnbActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewEnbActionPerformed
    {//GEN-HEADEREND:event_miNewEnbActionPerformed
        doNewEnable();
    }//GEN-LAST:event_miNewEnbActionPerformed

    private void miEditEnbActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditEnbActionPerformed
    {//GEN-HEADEREND:event_miEditEnbActionPerformed
        doEditEnable();
    }//GEN-LAST:event_miEditEnbActionPerformed

    private void miDisEnbActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisEnbActionPerformed
    {//GEN-HEADEREND:event_miDisEnbActionPerformed
        doDisEnable();
    }//GEN-LAST:event_miDisEnbActionPerformed

    private void miRevEnbActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevEnbActionPerformed
    {//GEN-HEADEREND:event_miRevEnbActionPerformed
        doRevEnable();
    }//GEN-LAST:event_miRevEnbActionPerformed

    private void miDesEnbActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesEnbActionPerformed
    {//GEN-HEADEREND:event_miDesEnbActionPerformed
        doDesEnable();
    }//GEN-LAST:event_miDesEnbActionPerformed

    private void miUpEnbActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miUpEnbActionPerformed
    {//GEN-HEADEREND:event_miUpEnbActionPerformed
        doMoveUpEnable();
    }//GEN-LAST:event_miUpEnbActionPerformed

    private void miDownEnbActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDownEnbActionPerformed
    {//GEN-HEADEREND:event_miDownEnbActionPerformed
        doMoveDownEnable();
    }//GEN-LAST:event_miDownEnbActionPerformed

    private void tbEnbsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbEnbsMouseClicked
    {//GEN-HEADEREND:event_tbEnbsMouseClicked
        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            doEditEnable();
        }
        else if (evt.getButton() == evt.BUTTON3)
        {
            popEnbs.show(tbEnbs, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbEnbsMouseClicked

    private void btnImportTempletActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnImportTempletActionPerformed
    {//GEN-HEADEREND:event_btnImportTempletActionPerformed
        doImportTemplet();
    }//GEN-LAST:event_btnImportTempletActionPerformed

    private void btnSaveAsTempletActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveAsTempletActionPerformed
    {//GEN-HEADEREND:event_btnSaveAsTempletActionPerformed
        doSaveAsTemplet();
    }//GEN-LAST:event_btnSaveAsTempletActionPerformed

    private void btnPopPropMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopPropMouseClicked
    {//GEN-HEADEREND:event_btnPopPropMouseClicked
        if (evt.getButton() == evt.BUTTON1)
        {
            popProp.show(btnPopProp, evt.getX(), popProp.getY() + btnPopProp.getHeight());
        }
    }//GEN-LAST:event_btnPopPropMouseClicked

    private void btnPopPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPopPropActionPerformed
    {//GEN-HEADEREND:event_btnPopPropActionPerformed

    }//GEN-LAST:event_btnPopPropActionPerformed

    private void btnEditPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditPropActionPerformed
    {//GEN-HEADEREND:event_btnEditPropActionPerformed
        doEditProp();
    }//GEN-LAST:event_btnEditPropActionPerformed

    private void btnMovePropUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMovePropUpActionPerformed
    {//GEN-HEADEREND:event_btnMovePropUpActionPerformed
        doMoveUpProp();
    }//GEN-LAST:event_btnMovePropUpActionPerformed

    private void btnMovePropDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMovePropDownActionPerformed
    {//GEN-HEADEREND:event_btnMovePropDownActionPerformed
        doMoveDownProp();
    }//GEN-LAST:event_btnMovePropDownActionPerformed

    private void btnPopEffsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopEffsMouseClicked
    {//GEN-HEADEREND:event_btnPopEffsMouseClicked
        if (evt.getButton() == evt.BUTTON1)
        {
            popEffs.show(btnPopEffs, evt.getX(), popEffs.getY() + btnPopEffs.getHeight());
        }
    }//GEN-LAST:event_btnPopEffsMouseClicked

    private void btnPopEffsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPopEffsActionPerformed
    {//GEN-HEADEREND:event_btnPopEffsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPopEffsActionPerformed

    private void btnPopEnbsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopEnbsMouseClicked
    {//GEN-HEADEREND:event_btnPopEnbsMouseClicked
        if (evt.getButton() == evt.BUTTON1)
        {
            popEnbs.show(btnPopEnbs, evt.getX(), popEnbs.getY() + btnPopEnbs.getHeight());
        }
    }//GEN-LAST:event_btnPopEnbsMouseClicked

    private void btnPopEnbsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPopEnbsActionPerformed
    {//GEN-HEADEREND:event_btnPopEnbsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPopEnbsActionPerformed

    @Override
    public boolean isNeedSave()
    {
        return fdd.isNeedSave();
    }

    @Override
    public void resetSave()
    {
        fdd.resetSave();
    }

    @Override
    public void save()
    {
        doSave(false);
    }

    @Override
    public String getDatablockServiceTag()
    {
        return fdd.getDatablockServiceTag();
    }

    @Override
    public String getDatablockName()
    {
        return fdd.getDatablockName();
    }

    @Override
    public void initData(wakeup _up, long _dbOID, int _oclsID)
    {
        up = _up;
        dbIndex = _dbOID;
        oclsID = _oclsID;
        fdd = new functionDefineData();
        fdd.initDatablcok(up);
        initData();
    }

    @Override
    public String getPalTitle()
    {
        return fdd.getDatablockName();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddProp;
    private javax.swing.JButton btnEditProp;
    private javax.swing.JButton btnFastSave;
    private javax.swing.JButton btnImportTemplet;
    private javax.swing.JButton btnMovePropDown;
    private javax.swing.JButton btnMovePropUp;
    private javax.swing.JButton btnNewEffect;
    private javax.swing.JButton btnNewEnable;
    private javax.swing.JButton btnPopEffs;
    private javax.swing.JButton btnPopEnbs;
    private javax.swing.JButton btnPopProp;
    private javax.swing.JButton btnSaveAsTemplet;
    private javax.swing.JButton btnUpdateControl;
    private javax.swing.JCheckBox ckShowAll;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JMenuItem miDesEff;
    private javax.swing.JMenuItem miDesEnb;
    private javax.swing.JMenuItem miDesProp;
    private javax.swing.JMenuItem miDisEff;
    private javax.swing.JMenuItem miDisEnb;
    private javax.swing.JMenuItem miDisProp;
    private javax.swing.JMenuItem miDownEff;
    private javax.swing.JMenuItem miDownEnb;
    private javax.swing.JMenuItem miDownProp;
    private javax.swing.JMenuItem miEditEff;
    private javax.swing.JMenuItem miEditEnb;
    private javax.swing.JMenuItem miEditProp;
    private javax.swing.JMenuItem miNewEff;
    private javax.swing.JMenuItem miNewEnb;
    private javax.swing.JMenuItem miNewProp;
    private javax.swing.JMenuItem miRevEff;
    private javax.swing.JMenuItem miRevEnb;
    private javax.swing.JMenuItem miRevProp;
    private javax.swing.JMenuItem miUpEff;
    private javax.swing.JMenuItem miUpEnb;
    private javax.swing.JMenuItem miUpProp;
    private javax.swing.JPopupMenu popEffs;
    private javax.swing.JPopupMenu popEnbs;
    private javax.swing.JPopupMenu popProp;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tbEfts;
    private javax.swing.JTable tbEnbs;
    private javax.swing.JTable tbProps;
    private javax.swing.JToolBar tbarEffs;
    private javax.swing.JToolBar tbarEnb;
    private javax.swing.JToolBar tbarProps;
    private javax.swing.JToolBar tbrPopEff;
    private javax.swing.JToolBar tbrPopEnb;
    private javax.swing.JTextField txtActSide;
    private javax.swing.JTextField txtAutoloop;
    private javax.swing.JTextField txtCooldown;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtDisableImpl;
    private javax.swing.JTextField txtEnableImpl;
    private javax.swing.JTextField txtInitEnv;
    private javax.swing.JTextField txtLoopImpl;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPeriod;
    private javax.swing.JTextField txtTag;
    private javax.swing.JTextField txtTargetRegion;
    // End of variables declaration//GEN-END:variables
}
