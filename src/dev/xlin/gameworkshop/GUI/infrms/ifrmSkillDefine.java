package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.MDIPaneControl;
import dev.xlin.gameworkshop.GUI.dialog.dlgSkillDefine;
import dev.xlin.gameworkshop.GUI.dialog.dlgSkillLevelInfo;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.GUI.guiFullTreeGuiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillLevel;
import dev.xlin.gameworkshop.progs.foundation.skillDefine;
import dev.xlin.gameworkshop.progs.foundation.skillLevel;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.dlgTools.dlgSelectTreeNode;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author 刘祎鹏
 */
public class ifrmSkillDefine extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private skillDefine sklDef = null;
    private JDesktopPane desk = null;
    private List lsks = null;
    private boolean bLvTbInit = false;
    private List llvs = null;

    public ifrmSkillDefine(wakeup _up, JDesktopPane _desk)
    {
        initComponents();
        up = _up;
        desk = _desk;
        sklDef = new skillDefine(up);
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        makeTypeTree();
        makeSkillTree();
        showSkillInfo();
        makeLevelTable();
    }

    private void makeLevelTable()
    {
        myTableModel mtm = null;
        if (bLvTbInit == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("分级序号");
            mtm.addColumn("名称");
            mtm.addColumn("描述");
            mtm.addColumn("主分级");
            tbLevels.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbLevels);
            mtm = (myTableModel) tbLevels.getModel();
        }
        int tpid = 0;
        beanSkillDefine bskl = getSelectedSkill();
        if (bskl != null)
        {
            tpid = bskl.getOID();
        }
        skillLevel sklv = new skillLevel(up);
        llvs = sklv.getLevelBySkill(tpid);
        if (llvs != null)
        {
            for (int i = 0; i < llvs.size(); i++)
            {
                beanSkillLevel bsl = (beanSkillLevel) llvs.get(i);
                Object[] orow = fast.makeObjectArray(4);
                orow[0] = bsl.getLevelIdx();
                orow[1] = bsl.getLevelName();
                orow[2] = bsl.getLevelDesp();
                orow[3] = iConst.translateIBOL(bsl.getMasterLevel());
                mtm.addRow(orow);
            }
        }
        tbLevels.setModel(mtm);
    }

    private myTreeNode getSelectedTypeNode()
    {
        TreePath tph = trTypes.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode msel = (myTreeNode) tph.getLastPathComponent();
        if (msel.getNodeOID() != 0)
        {
            return msel;
        }
        else
        {
            return null;
        }
    }

    private beanSkillDefine getSelectedSkill()
    {
        TreePath tph = trSkls.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode msel = (myTreeNode) tph.getLastPathComponent();
        if (msel.getNodeOID() == 0)
        {
            System.err.println(".msel = null");
            return null;
        }
        else
        {
            System.err.println(". find skill in list .id = " + msel.getNodeOID());
            return findSkillInList(msel.getNodeOID());
        }
    }

    private beanSkillDefine findSkillInList(int id)
    {
        if (lsks == null)
        {
            return null;
        }
        for (int i = 0; i < lsks.size(); i++)
        {
            beanSkillDefine bean = (beanSkillDefine) lsks.get(i);
            if (bean.getOID() == id)
            {
                return bean;
            }
        }
        return null;
    }

    private void showSkillInfo()
    {
        System.err.println(".show .skl .info ");
        beanSkillDefine bean = getSelectedSkill();
        System.err.println(".bean . = " + bean);
        if (bean == null)
        {
            txtGeSkillImpl.setText("");
            txtGetJudgeImpl.setText("");
            txtHide.setText("");
            txtLevelChangeImpl.setText("");
            txtLevelData.setText("");
            txtLevelInvoke.setText("");
            txtLevelType.setText("");
            txtLossSkillImpl.setText("");
            txtSameData.setText("");
            txtSkillDesp.setText("");
            txtSkillName.setText("");
            txtSkillTag.setText("");
            txtSkillUpgrade.setText("");
            txtUpgradeJudge.setText("");
        }
        else
        {
            txtGeSkillImpl.setText(bean.getGetSkillImpl());
            txtGetJudgeImpl.setText(bean.getGetJudgeImpl());
            txtHide.setText(iConst.translateIBOL(bean.getHide()));
            txtLevelChangeImpl.setText(bean.getLevelChangeImpl());
            txtLevelData.setText(iConst.translate(bean.getLevelDataMethod()));
            txtLevelInvoke.setText(iConst.translate(bean.getLevelInvokeType()));
            txtLevelType.setText(iConst.translate(bean.getLevelType()));
            txtLossSkillImpl.setText(bean.getLossSkillImpl());
            txtSameData.setText(iConst.translate(bean.getSameDataMethod()));
            txtSkillDesp.setText(bean.getSkillDesp());
            txtSkillName.setText(bean.getSkillName());
            txtSkillTag.setText(bean.getSkillTag());
            txtSkillUpgrade.setText(bean.getUpgradeSkillImpl());
            txtUpgradeJudge.setText(bean.getUpgradeJudgeImpl());
        }
    }

    private void makeTypeTree()
    {
        myTreeNode mtn = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_SKILL_DEF, ckShowAllType.isSelected());
        DefaultTreeModel dtm = new DefaultTreeModel(mtn);
        trTypes.setModel(dtm);
        guiCommon.expandTree(trTypes);
    }

    private void makeSkillTree()
    {
        System.err.println(".m skl tree ");
        myTreeNode mtp = getSelectedTypeNode();
        myTreeNode mskr = new myTreeNode("[技能数据库]", 0, 0);
        if (mtp != null)
        {
            lsks = sklDef.getSkillByType(mtp.getNodeOID(), ckShowAllSkl.isSelected());
            if (ckShowSklFlat.isSelected())
            {
                if (lsks != null)
                {
                    for (int i = 0; i < lsks.size(); i++)
                    {
                        beanSkillDefine bean = (beanSkillDefine) lsks.get(i);
                        myTreeNode mskl = new myTreeNode(makeSkillNodeName(bean), bean.getOID(), 1);
                        mskr.add(mskl);
                    }
                }
            }
            else if (ckShowSklLvs.isSelected())
            {
                dMakeSklTree(mskr, lsks, 0, 0);
            }
        }
        DefaultTreeModel dtm = new DefaultTreeModel(mskr);
        trSkls.setModel(dtm);
        guiCommon.expandTree(trSkls);
    }

    private String makeSkillNodeName(beanSkillDefine bean)
    {
        String s = bean.getSkillName() + "<" + bean.getSkillTag() + ">";
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            s = s + " [失效]";
        }
        return s;
    }

    private void dMakeSklTree(myTreeNode mpar, List ls, int pid, int exid)
    {
        if (ls != null)
        {
            for (int i = 0; i < ls.size(); i++)
            {
                beanSkillDefine bean = (beanSkillDefine) ls.get(i);
                if (bean.getOID() == exid)
                {
                    continue;
                }
                if (bean.getParentSkillOID() == pid)
                {
                    myTreeNode mskl = new myTreeNode(makeSkillNodeName(bean), bean.getOID(), 1);
                    dMakeSklTree(mskl, ls, bean.getOID(), exid);
                    mpar.add(mskl);
                }
            }
        }
    }

    private void newType()
    {
        String s = fast.input("请输入分类名称", "");
        if (s == null)
        {
            return;
        }
        if (s.trim().equals(""))
        {
            return;
        }
        beanSttType bst = new beanSttType();
        bst.setSttID(systemType.CODE_STT_SKILL_DEF);
        bst.setTypeName(s.trim());
        sttType stp = new sttType(up);
        int r = stp.createRecord(bst, false);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("创建分类失败", r);
        }
    }

    private void editType()
    {
        int r = guiFullTreeGuiCodes.doEditType(trTypes, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
    }

    private void disType()
    {
        myTreeNode msel = getSelectedTypeNode();
        if (msel == null)
        {
            return;
        }
        List l = sklDef.getSkillByType(msel.getNodeOID(), true);
        if (l != null)
        {
            fast.warn("这个分类下还有存在技能，不可失效处理");
            return;
        }
        int sel = fast.ask("是否需要失效选择的分类？");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        int r = stp.deleteRecord(msel.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void revType()
    {
        myTreeNode msel = getSelectedTypeNode();
        if (msel == null)
        {
            return;
        }
        int sel = fast.ask("是否需要恢复选择的分类？");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        int r = stp.revertBean(msel.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("恢复数据操作失败", r);
        }
    }

    private void desType()
    {
        myTreeNode msel = getSelectedTypeNode();
        if (msel == null)
        {
            return;
        }
        int sel = fast.ask("是否需要销毁选择的分类？");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        int r = stp.destroyBean(msel.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("销毁数据操作失败", r);
        }
    }

    private void newSkillFromType()
    {
        myTreeNode msel = getSelectedTypeNode();
        if (msel == null)
        {
            return;
        }
        dlgSkillDefine dlg = new dlgSkillDefine(null, true, up, null, msel.getNodeOID(), 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeSkillTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void newSkillFromParent()
    {
        beanSkillDefine bspar = getSelectedSkill();
        if (bspar == null)
        {
            return;
        }
        dlgSkillDefine dlg = new dlgSkillDefine(null, true, up, null, 0, bspar.getOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeSkillTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void editSkill()
    {
        beanSkillDefine bean = getSelectedSkill();
        if (bean == null)
        {
            return;
        }
        dlgSkillDefine dlg = new dlgSkillDefine(null, true, up, bean, 0, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeSkillTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void disableSkill()
    {
        beanSkillDefine bean = getSelectedSkill();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将这个选择的技能设置为失效？\n当其还存在分级或者子技能的情况下，操作会被阻止。");
        if (sel != fast.YES)
        {
            return;
        }
        int r = sklDef.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeSkillTree();
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void revertSkill()
    {
        beanSkillDefine bean = getSelectedSkill();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将这个选择的技能恢复为有效？ ");
        if (sel != fast.YES)
        {
            return;
        }
        int r = sklDef.revertBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeSkillTree();
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void destroySkill()
    {
        beanSkillDefine bean = getSelectedSkill();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将这个选择的技能进行销毁？\n销毁操作不可被恢复 ");
        if (sel != fast.YES)
        {
            return;
        }
        int r = sklDef.destroyBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeSkillTree();
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void moveSkillToNode()
    {
        beanSkillDefine bean = getSelectedSkill();
        if (bean == null)
        {
            return;
        }
        myTreeNode mtn = new myTreeNode("[技能数据库]", 0, 0);
        dMakeSklTree(mtn, lsks, 0, bean.getOID());
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mtn);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            myTreeNode msel = dlg.getSelect();
            if (msel != null)
            {
                int tpid = msel.getNodeOID();
                int r = sklDef.moveSkillAsChild(bean.getOID(), tpid);
                if (r == iDAO.OPERATE_SUCCESS)
                {
                    makeSkillTree();
                }
                else
                {
                    fast.err("移动失败", r);
                }
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void setSkillAsRoot()
    {
        beanSkillDefine bean = getSelectedSkill();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将技能移动至同类根节点？");
        if (sel != fast.YES)
        {
            return;
        }
        int r = sklDef.moveSkillAsRoot(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeSkillTree();
        }
        else
        {
            fast.err("移动失败", r);
        }
    }

    private void newSkillLevel()
    {
        beanSkillDefine bean = getSelectedSkill();
        if (bean == null)
        {
            return;
        }
        dlgSkillLevelInfo dlg = new dlgSkillLevelInfo(null, true, up, null, bean.getOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeLevelTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private beanSkillLevel getSelectedLevel()
    {
        int idx = tbLevels.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (beanSkillLevel) llvs.get(idx);
    }

    private void editSkillLevelInfo()
    {
        beanSkillLevel bean = getSelectedLevel();
        if (bean == null)
        {
            return;
        }
        dlgSkillLevelInfo dlg = new dlgSkillLevelInfo(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeLevelTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void deleteLevel()
    {
        beanSkillLevel bean = getSelectedLevel();
        if (bean == null)
        {
            return;
        }
        System.err.println("lv id = " + bean.getOID());
        int sel = fast.ask("是否要删除这个指定分级？\n删除以后数据不可恢复\n建议在删除完成后，执行重建序列操作。");
        if (sel != fast.YES)
        {
            return;
        }
        skillLevel sklv = new skillLevel(up);
        int r = sklv.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeLevelTable();
        }
        else
        {
            fast.err("删除分级操作失败", r);
        }
    }

    private void moveLvUp()
    {
        beanSkillLevel bean = getSelectedLevel();
        if (bean == null)
        {
            return;
        }
        skillLevel sklv = new skillLevel(up);
        int r = sklv.moveLevelUp(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeLevelTable();
        }
    }

    private void moveLvDown()
    {
        beanSkillLevel bean = getSelectedLevel();
        if (bean == null)
        {
            return;
        }
        skillLevel sklv = new skillLevel(up);
        int r = sklv.moveLevelDown(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeLevelTable();
        }
    }

    private void setLvAsMaster()
    {
        beanSkillLevel bean = getSelectedLevel();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("确认将选中的分级设置为主分级？");
        if (sel != fast.YES)
        {
            return;
        }
        skillLevel sklv = new skillLevel(up);
        int r = sklv.setLevelAsMaster(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeLevelTable();
        }
        else
        {
            fast.err("设置主分级操作失败", r);
        }
    }

    private void rebuldLvIndex()
    {
        beanSkillDefine bskl = getSelectedSkill();
        if (bskl == null)
        {
            return;
        }
        skillLevel sklv = new skillLevel(up);
        int r = sklv.rebuildSkillLevelIndex(bskl.getOID(), true);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeLevelTable();
        }
        else
        {
            fast.err("重建分级序列操作失败", r);
        }
    }

    private void openLevelDataEditor()
    {
        beanSkillLevel bsklv = getSelectedLevel();
        if (bsklv == null)
        {
            return;
        }
        boolean bop = MDIPaneControl.isMDIFrameOpened(MDIPaneControl.IFRM_SKILL_LEVEL_DATA, bsklv.getOID() + "");
        if (bop)
        {
            fast.warn("不能重复打开数据编辑器");
            return;
        }
        ifrmSkillLevelEditor ifrm = new ifrmSkillLevelEditor(up, bsklv, desk);
        desk.add(ifrm);
        ifrm.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        bgpViewSkill = new javax.swing.ButtonGroup();
        popType = new javax.swing.JPopupMenu();
        miNewType = new javax.swing.JMenuItem();
        miEditType = new javax.swing.JMenuItem();
        miDeleteType = new javax.swing.JMenuItem();
        miRevertType = new javax.swing.JMenuItem();
        miDestroyType = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        miNewSklFromType = new javax.swing.JMenuItem();
        popSkl = new javax.swing.JPopupMenu();
        miNewSklRoot = new javax.swing.JMenuItem();
        miNewSklChild = new javax.swing.JMenuItem();
        miEditSkl = new javax.swing.JMenuItem();
        miDisSkl = new javax.swing.JMenuItem();
        miRevSkl = new javax.swing.JMenuItem();
        miDesSkl = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        miMoveSkl = new javax.swing.JMenuItem();
        miSetSklRoot = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        miNewLevelFromSkill = new javax.swing.JMenuItem();
        popLvs = new javax.swing.JPopupMenu();
        miNewLv = new javax.swing.JMenuItem();
        miEditLv = new javax.swing.JMenuItem();
        miRemoveLv = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        miEditLevelData = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        miMoveLvUp = new javax.swing.JMenuItem();
        miMoveLvDown = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        miSetLvMaster = new javax.swing.JMenuItem();
        miRebuldLvIndex = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        ckShowAllType = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        trTypes = new javax.swing.JTree();
        jToolBar2 = new javax.swing.JToolBar();
        btnPopType = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        ckShowAllSkl = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        trSkls = new javax.swing.JTree();
        ckShowSklLvs = new javax.swing.JCheckBox();
        ckShowSklFlat = new javax.swing.JCheckBox();
        jToolBar3 = new javax.swing.JToolBar();
        btnPopSkill = new javax.swing.JButton();
        palInfo = new javax.swing.JPanel();
        tabSkill = new javax.swing.JTabbedPane();
        palSklInfo = new javax.swing.JPanel();
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
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtSkillName = new javax.swing.JTextField();
        txtSkillTag = new javax.swing.JTextField();
        txtSkillDesp = new javax.swing.JTextField();
        txtLevelType = new javax.swing.JTextField();
        txtLevelInvoke = new javax.swing.JTextField();
        txtLevelData = new javax.swing.JTextField();
        txtSameData = new javax.swing.JTextField();
        txtHide = new javax.swing.JTextField();
        txtGetJudgeImpl = new javax.swing.JTextField();
        txtGeSkillImpl = new javax.swing.JTextField();
        txtLevelChangeImpl = new javax.swing.JTextField();
        txtLossSkillImpl = new javax.swing.JTextField();
        txtUpgradeJudge = new javax.swing.JTextField();
        txtSkillUpgrade = new javax.swing.JTextField();
        btnEditSkill = new javax.swing.JButton();
        palSklLevel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbLevels = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnNewLevel = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnMoveUp = new javax.swing.JButton();
        btnMoveDown = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnSetMaster = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnEditFunc = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        btnPopLevel = new javax.swing.JButton();

        miNewType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewType.setText("新建分类");
        miNewType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewTypeActionPerformed(evt);
            }
        });
        popType.add(miNewType);

        miEditType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditType.setText("修改分类");
        miEditType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditTypeActionPerformed(evt);
            }
        });
        popType.add(miEditType);

        miDeleteType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDeleteType.setText("失效分类");
        miDeleteType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDeleteTypeActionPerformed(evt);
            }
        });
        popType.add(miDeleteType);

        miRevertType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevertType.setText("恢复分类");
        miRevertType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevertTypeActionPerformed(evt);
            }
        });
        popType.add(miRevertType);

        miDestroyType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDestroyType.setText("销毁分类");
        miDestroyType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyTypeActionPerformed(evt);
            }
        });
        popType.add(miDestroyType);
        popType.add(jSeparator4);

        miNewSklFromType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewSklFromType.setText("创建技能");
        miNewSklFromType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewSklFromTypeActionPerformed(evt);
            }
        });
        popType.add(miNewSklFromType);

        miNewSklRoot.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewSklRoot.setText("新建根技能");
        miNewSklRoot.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewSklRootActionPerformed(evt);
            }
        });
        popSkl.add(miNewSklRoot);

        miNewSklChild.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewSklChild.setText("新建子技能");
        miNewSklChild.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewSklChildActionPerformed(evt);
            }
        });
        popSkl.add(miNewSklChild);

        miEditSkl.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditSkl.setText("修改技能");
        miEditSkl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditSklActionPerformed(evt);
            }
        });
        popSkl.add(miEditSkl);

        miDisSkl.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisSkl.setText("失效技能");
        miDisSkl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisSklActionPerformed(evt);
            }
        });
        popSkl.add(miDisSkl);

        miRevSkl.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevSkl.setText("恢复技能");
        miRevSkl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevSklActionPerformed(evt);
            }
        });
        popSkl.add(miRevSkl);

        miDesSkl.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesSkl.setText("销毁技能");
        miDesSkl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesSklActionPerformed(evt);
            }
        });
        popSkl.add(miDesSkl);
        popSkl.add(jSeparator5);

        miMoveSkl.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveSkl.setText("移动技能到其他节点");
        miMoveSkl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveSklActionPerformed(evt);
            }
        });
        popSkl.add(miMoveSkl);

        miSetSklRoot.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miSetSklRoot.setText("设置根节点技能");
        miSetSklRoot.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miSetSklRootActionPerformed(evt);
            }
        });
        popSkl.add(miSetSklRoot);
        popSkl.add(jSeparator6);

        miNewLevelFromSkill.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewLevelFromSkill.setText("添加分级");
        miNewLevelFromSkill.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewLevelFromSkillActionPerformed(evt);
            }
        });
        popSkl.add(miNewLevelFromSkill);

        miNewLv.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewLv.setText("新建分级");
        popLvs.add(miNewLv);

        miEditLv.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditLv.setText("修改分级信息");
        miEditLv.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditLvActionPerformed(evt);
            }
        });
        popLvs.add(miEditLv);

        miRemoveLv.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRemoveLv.setText("移除分级");
        miRemoveLv.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRemoveLvActionPerformed(evt);
            }
        });
        popLvs.add(miRemoveLv);
        popLvs.add(jSeparator7);

        miEditLevelData.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditLevelData.setForeground(new java.awt.Color(255, 0, 0));
        miEditLevelData.setText("编辑分级数据");
        miEditLevelData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditLevelDataActionPerformed(evt);
            }
        });
        popLvs.add(miEditLevelData);
        popLvs.add(jSeparator9);

        miMoveLvUp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveLvUp.setText("上移");
        miMoveLvUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveLvUpActionPerformed(evt);
            }
        });
        popLvs.add(miMoveLvUp);

        miMoveLvDown.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveLvDown.setText("下移");
        miMoveLvDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveLvDownActionPerformed(evt);
            }
        });
        popLvs.add(miMoveLvDown);
        popLvs.add(jSeparator8);

        miSetLvMaster.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miSetLvMaster.setText("设为主分级");
        miSetLvMaster.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miSetLvMasterActionPerformed(evt);
            }
        });
        popLvs.add(miSetLvMaster);

        miRebuldLvIndex.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRebuldLvIndex.setText("重建序号");
        miRebuldLvIndex.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRebuldLvIndexActionPerformed(evt);
            }
        });
        popLvs.add(miRebuldLvIndex);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("技能数据库");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });

        jSplitPane1.setDividerLocation(400);

        jSplitPane2.setDividerLocation(260);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        ckShowAllType.setText("显示所有分类");
        ckShowAllType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllTypeActionPerformed(evt);
            }
        });

        trTypes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                trTypesMouseClicked(evt);
            }
        });
        trTypes.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trTypesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(trTypes);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnPopType.setForeground(new java.awt.Color(0, 0, 204));
        btnPopType.setText("↓");
        btnPopType.setBorder(null);
        btnPopType.setFocusable(false);
        btnPopType.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPopTypeMouseClicked(evt);
            }
        });
        btnPopType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopTypeActionPerformed(evt);
            }
        });
        jToolBar2.add(btnPopType);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 279, Short.MAX_VALUE)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckShowAllType))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ckShowAllType, jToolBar2});

        jSplitPane2.setTopComponent(jPanel2);

        ckShowAllSkl.setText("显示所有技能");
        ckShowAllSkl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllSklActionPerformed(evt);
            }
        });

        trSkls.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                trSklsMouseClicked(evt);
            }
        });
        trSkls.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trSklsValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(trSkls);

        bgpViewSkill.add(ckShowSklLvs);
        ckShowSklLvs.setForeground(new java.awt.Color(173, 0, 252));
        ckShowSklLvs.setSelected(true);
        ckShowSklLvs.setText("级联显示");
        ckShowSklLvs.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowSklLvsActionPerformed(evt);
            }
        });

        bgpViewSkill.add(ckShowSklFlat);
        ckShowSklFlat.setForeground(new java.awt.Color(173, 0, 252));
        ckShowSklFlat.setText("平铺显示");
        ckShowSklFlat.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowSklFlatActionPerformed(evt);
            }
        });

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnPopSkill.setForeground(new java.awt.Color(0, 0, 204));
        btnPopSkill.setText("↓");
        btnPopSkill.setBorder(null);
        btnPopSkill.setFocusable(false);
        btnPopSkill.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPopSkillMouseClicked(evt);
            }
        });
        jToolBar3.add(btnPopSkill);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllSkl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckShowSklLvs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckShowSklFlat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane2)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ckShowAllSkl)
                        .addComponent(ckShowSklLvs)
                        .addComponent(ckShowSklFlat))
                    .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ckShowAllSkl, ckShowSklFlat, ckShowSklLvs, jToolBar3});

        jSplitPane2.setRightComponent(jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jLabel1.setText("技能名称");

        jLabel2.setText("技能标签");

        jLabel3.setText("技能描述");

        jLabel4.setText("分级调用");

        jLabel5.setText("分级数据");

        jLabel6.setText("同类数据");

        jLabel7.setText("分级类型");

        jLabel8.setText("是否隐藏");

        jLabel9.setText("获取判断");

        jLabel10.setText("获取技能");

        jLabel11.setText("分级变动");

        jLabel12.setText("失去技能");

        jLabel13.setText("升级判断");

        jLabel14.setText("升级技能");

        txtSkillName.setEditable(false);

        txtSkillTag.setEditable(false);

        txtSkillDesp.setEditable(false);

        txtLevelType.setEditable(false);

        txtLevelInvoke.setEditable(false);

        txtLevelData.setEditable(false);

        txtSameData.setEditable(false);

        txtHide.setEditable(false);

        txtGetJudgeImpl.setEditable(false);

        txtGeSkillImpl.setEditable(false);

        txtLevelChangeImpl.setEditable(false);

        txtLossSkillImpl.setEditable(false);

        txtUpgradeJudge.setEditable(false);

        txtSkillUpgrade.setEditable(false);

        btnEditSkill.setText("修改配置");
        btnEditSkill.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditSkillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout palSklInfoLayout = new javax.swing.GroupLayout(palSklInfo);
        palSklInfo.setLayout(palSklInfoLayout);
        palSklInfoLayout.setHorizontalGroup(
            palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(palSklInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSkillName))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSkillUpgrade))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSkillTag))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSkillDesp))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLevelType))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLevelInvoke))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLevelData))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSameData))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHide))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGetJudgeImpl))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGeSkillImpl))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLevelChangeImpl))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLossSkillImpl))
                    .addGroup(palSklInfoLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUpgradeJudge))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, palSklInfoLayout.createSequentialGroup()
                        .addGap(0, 583, Short.MAX_VALUE)
                        .addComponent(btnEditSkill)))
                .addContainerGap())
        );
        palSklInfoLayout.setVerticalGroup(
            palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(palSklInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSkillName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSkillTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtSkillDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtLevelType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtLevelInvoke, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtLevelData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtSameData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtHide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtGetJudgeImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtGeSkillImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtLevelChangeImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtLossSkillImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtUpgradeJudge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(palSklInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtSkillUpgrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditSkill)
                .addContainerGap(187, Short.MAX_VALUE))
        );

        tabSkill.addTab("基本信息", palSklInfo);

        tbLevels.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbLevels.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbLevels.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbLevelsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbLevels);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnNewLevel.setText("新建分级");
        btnNewLevel.setFocusable(false);
        btnNewLevel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewLevel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewLevel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewLevelActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNewLevel);
        jToolBar1.add(jSeparator1);

        btnMoveUp.setText("上移");
        btnMoveUp.setFocusable(false);
        btnMoveUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMoveUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMoveUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMoveUpActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMoveUp);

        btnMoveDown.setText("下移");
        btnMoveDown.setFocusable(false);
        btnMoveDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMoveDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMoveDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMoveDownActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMoveDown);
        jToolBar1.add(jSeparator2);

        btnSetMaster.setText("设为主级");
        btnSetMaster.setFocusable(false);
        btnSetMaster.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSetMaster.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSetMaster.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSetMasterActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSetMaster);
        jToolBar1.add(jSeparator3);

        btnEditFunc.setText("编辑功能数据");
        btnEditFunc.setFocusable(false);
        btnEditFunc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditFunc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditFunc.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditFuncActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditFunc);

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        btnPopLevel.setForeground(new java.awt.Color(0, 0, 204));
        btnPopLevel.setText("↓");
        btnPopLevel.setBorder(null);
        btnPopLevel.setFocusable(false);
        btnPopLevel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPopLevelMouseClicked(evt);
            }
        });
        jToolBar4.add(btnPopLevel);

        javax.swing.GroupLayout palSklLevelLayout = new javax.swing.GroupLayout(palSklLevel);
        palSklLevel.setLayout(palSklLevelLayout);
        palSklLevelLayout.setHorizontalGroup(
            palSklLevelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
            .addGroup(palSklLevelLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        palSklLevelLayout.setVerticalGroup(
            palSklLevelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, palSklLevelLayout.createSequentialGroup()
                .addGroup(palSklLevelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE))
        );

        palSklLevelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jToolBar1, jToolBar4});

        tabSkill.addTab("分级定义", palSklLevel);

        javax.swing.GroupLayout palInfoLayout = new javax.swing.GroupLayout(palInfo);
        palInfo.setLayout(palInfoLayout);
        palInfoLayout.setHorizontalGroup(
            palInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabSkill)
        );
        palInfoLayout.setVerticalGroup(
            palInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabSkill)
        );

        jSplitPane1.setRightComponent(palInfo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1082, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ckShowAllSklActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllSklActionPerformed
    {//GEN-HEADEREND:event_ckShowAllSklActionPerformed
        makeSkillTree();
    }//GEN-LAST:event_ckShowAllSklActionPerformed

    private void ckShowSklFlatActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowSklFlatActionPerformed
    {//GEN-HEADEREND:event_ckShowSklFlatActionPerformed
        makeSkillTree();
    }//GEN-LAST:event_ckShowSklFlatActionPerformed

    private void ckShowSklLvsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowSklLvsActionPerformed
    {//GEN-HEADEREND:event_ckShowSklLvsActionPerformed
        makeSkillTree();
    }//GEN-LAST:event_ckShowSklLvsActionPerformed

    private void trTypesValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trTypesValueChanged
    {//GEN-HEADEREND:event_trTypesValueChanged
        makeSkillTree();
        showSkillInfo();
    }//GEN-LAST:event_trTypesValueChanged

    private void trTypesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_trTypesMouseClicked
    {//GEN-HEADEREND:event_trTypesMouseClicked
        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            editType();
        }
        else if (evt.getButton() != evt.BUTTON1)
        {
            popType.show(trTypes, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_trTypesMouseClicked

    private void miNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewTypeActionPerformed
    {//GEN-HEADEREND:event_miNewTypeActionPerformed
        newType();
    }//GEN-LAST:event_miNewTypeActionPerformed

    private void miEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditTypeActionPerformed
    {//GEN-HEADEREND:event_miEditTypeActionPerformed
        editType();
    }//GEN-LAST:event_miEditTypeActionPerformed

    private void miDeleteTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDeleteTypeActionPerformed
    {//GEN-HEADEREND:event_miDeleteTypeActionPerformed
        disType();
    }//GEN-LAST:event_miDeleteTypeActionPerformed

    private void miRevertTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertTypeActionPerformed
    {//GEN-HEADEREND:event_miRevertTypeActionPerformed
        revType();
    }//GEN-LAST:event_miRevertTypeActionPerformed

    private void miDestroyTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyTypeActionPerformed
    {//GEN-HEADEREND:event_miDestroyTypeActionPerformed
        desType();
    }//GEN-LAST:event_miDestroyTypeActionPerformed

    private void ckShowAllTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllTypeActionPerformed
    {//GEN-HEADEREND:event_ckShowAllTypeActionPerformed
        makeTypeTree();
    }//GEN-LAST:event_ckShowAllTypeActionPerformed

    private void miNewSklFromTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewSklFromTypeActionPerformed
    {//GEN-HEADEREND:event_miNewSklFromTypeActionPerformed
        newSkillFromType();
    }//GEN-LAST:event_miNewSklFromTypeActionPerformed

    private void trSklsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trSklsValueChanged
    {//GEN-HEADEREND:event_trSklsValueChanged
        showSkillInfo();
        makeLevelTable();
    }//GEN-LAST:event_trSklsValueChanged

    private void miNewSklRootActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewSklRootActionPerformed
    {//GEN-HEADEREND:event_miNewSklRootActionPerformed
        newSkillFromType();
    }//GEN-LAST:event_miNewSklRootActionPerformed

    private void miNewSklChildActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewSklChildActionPerformed
    {//GEN-HEADEREND:event_miNewSklChildActionPerformed
        newSkillFromParent();
    }//GEN-LAST:event_miNewSklChildActionPerformed

    private void miEditSklActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditSklActionPerformed
    {//GEN-HEADEREND:event_miEditSklActionPerformed
        editSkill();
    }//GEN-LAST:event_miEditSklActionPerformed

    private void trSklsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_trSklsMouseClicked
    {//GEN-HEADEREND:event_trSklsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popSkl.show(trSkls, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            editSkill();
        }
    }//GEN-LAST:event_trSklsMouseClicked

    private void miDisSklActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisSklActionPerformed
    {//GEN-HEADEREND:event_miDisSklActionPerformed
        disableSkill();
    }//GEN-LAST:event_miDisSklActionPerformed

    private void miRevSklActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevSklActionPerformed
    {//GEN-HEADEREND:event_miRevSklActionPerformed
        revertSkill();
    }//GEN-LAST:event_miRevSklActionPerformed

    private void miDesSklActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesSklActionPerformed
    {//GEN-HEADEREND:event_miDesSklActionPerformed
        destroySkill();
    }//GEN-LAST:event_miDesSklActionPerformed

    private void miMoveSklActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveSklActionPerformed
    {//GEN-HEADEREND:event_miMoveSklActionPerformed
        moveSkillToNode();
    }//GEN-LAST:event_miMoveSklActionPerformed

    private void miSetSklRootActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miSetSklRootActionPerformed
    {//GEN-HEADEREND:event_miSetSklRootActionPerformed
        setSkillAsRoot();
    }//GEN-LAST:event_miSetSklRootActionPerformed

    private void btnEditSkillActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditSkillActionPerformed
    {//GEN-HEADEREND:event_btnEditSkillActionPerformed
        editSkill();
    }//GEN-LAST:event_btnEditSkillActionPerformed

    private void miNewLevelFromSkillActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewLevelFromSkillActionPerformed
    {//GEN-HEADEREND:event_miNewLevelFromSkillActionPerformed
        newSkillLevel();
    }//GEN-LAST:event_miNewLevelFromSkillActionPerformed

    private void btnNewLevelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewLevelActionPerformed
    {//GEN-HEADEREND:event_btnNewLevelActionPerformed
        newSkillLevel();
    }//GEN-LAST:event_btnNewLevelActionPerformed

    private void tbLevelsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbLevelsMouseClicked
    {//GEN-HEADEREND:event_tbLevelsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popLvs.show(tbLevels, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            editSkillLevelInfo();
        }
    }//GEN-LAST:event_tbLevelsMouseClicked

    private void miEditLvActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditLvActionPerformed
    {//GEN-HEADEREND:event_miEditLvActionPerformed
        editSkillLevelInfo();
    }//GEN-LAST:event_miEditLvActionPerformed

    private void miRemoveLvActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveLvActionPerformed
    {//GEN-HEADEREND:event_miRemoveLvActionPerformed
        deleteLevel();
    }//GEN-LAST:event_miRemoveLvActionPerformed

    private void miMoveLvUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveLvUpActionPerformed
    {//GEN-HEADEREND:event_miMoveLvUpActionPerformed
        moveLvUp();
    }//GEN-LAST:event_miMoveLvUpActionPerformed

    private void miMoveLvDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveLvDownActionPerformed
    {//GEN-HEADEREND:event_miMoveLvDownActionPerformed
        moveLvDown();
    }//GEN-LAST:event_miMoveLvDownActionPerformed

    private void miSetLvMasterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miSetLvMasterActionPerformed
    {//GEN-HEADEREND:event_miSetLvMasterActionPerformed
        setLvAsMaster();
    }//GEN-LAST:event_miSetLvMasterActionPerformed

    private void miRebuldLvIndexActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRebuldLvIndexActionPerformed
    {//GEN-HEADEREND:event_miRebuldLvIndexActionPerformed
        rebuldLvIndex();
    }//GEN-LAST:event_miRebuldLvIndexActionPerformed

    private void btnMoveUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMoveUpActionPerformed
    {//GEN-HEADEREND:event_btnMoveUpActionPerformed
        moveLvUp();
    }//GEN-LAST:event_btnMoveUpActionPerformed

    private void btnMoveDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMoveDownActionPerformed
    {//GEN-HEADEREND:event_btnMoveDownActionPerformed
        moveLvDown();
    }//GEN-LAST:event_btnMoveDownActionPerformed

    private void btnSetMasterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSetMasterActionPerformed
    {//GEN-HEADEREND:event_btnSetMasterActionPerformed
        setLvAsMaster();
    }//GEN-LAST:event_btnSetMasterActionPerformed

    private void btnPopTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPopTypeActionPerformed
    {//GEN-HEADEREND:event_btnPopTypeActionPerformed

    }//GEN-LAST:event_btnPopTypeActionPerformed

    private void btnPopTypeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopTypeMouseClicked
    {//GEN-HEADEREND:event_btnPopTypeMouseClicked
        popType.show(btnPopType, evt.getX(), btnPopType.getY() + btnPopType.getHeight());
    }//GEN-LAST:event_btnPopTypeMouseClicked

    private void btnPopSkillMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopSkillMouseClicked
    {//GEN-HEADEREND:event_btnPopSkillMouseClicked
        popSkl.show(btnPopSkill, evt.getX(), btnPopSkill.getY() + btnPopSkill.getHeight());
    }//GEN-LAST:event_btnPopSkillMouseClicked

    private void btnPopLevelMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopLevelMouseClicked
    {//GEN-HEADEREND:event_btnPopLevelMouseClicked
        popLvs.show(btnPopLevel, evt.getX(), btnPopLevel.getY() + btnPopLevel.getHeight());
    }//GEN-LAST:event_btnPopLevelMouseClicked

    private void miEditLevelDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditLevelDataActionPerformed
    {//GEN-HEADEREND:event_miEditLevelDataActionPerformed
        openLevelDataEditor();
    }//GEN-LAST:event_miEditLevelDataActionPerformed

    private void btnEditFuncActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditFuncActionPerformed
    {//GEN-HEADEREND:event_btnEditFuncActionPerformed
        openLevelDataEditor();
    }//GEN-LAST:event_btnEditFuncActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsSkillDefine(false);
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgpViewSkill;
    private javax.swing.JButton btnEditFunc;
    private javax.swing.JButton btnEditSkill;
    private javax.swing.JButton btnMoveDown;
    private javax.swing.JButton btnMoveUp;
    private javax.swing.JButton btnNewLevel;
    private javax.swing.JButton btnPopLevel;
    private javax.swing.JButton btnPopSkill;
    private javax.swing.JButton btnPopType;
    private javax.swing.JButton btnSetMaster;
    private javax.swing.JCheckBox ckShowAllSkl;
    private javax.swing.JCheckBox ckShowAllType;
    private javax.swing.JCheckBox ckShowSklFlat;
    private javax.swing.JCheckBox ckShowSklLvs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JMenuItem miDeleteType;
    private javax.swing.JMenuItem miDesSkl;
    private javax.swing.JMenuItem miDestroyType;
    private javax.swing.JMenuItem miDisSkl;
    private javax.swing.JMenuItem miEditLevelData;
    private javax.swing.JMenuItem miEditLv;
    private javax.swing.JMenuItem miEditSkl;
    private javax.swing.JMenuItem miEditType;
    private javax.swing.JMenuItem miMoveLvDown;
    private javax.swing.JMenuItem miMoveLvUp;
    private javax.swing.JMenuItem miMoveSkl;
    private javax.swing.JMenuItem miNewLevelFromSkill;
    private javax.swing.JMenuItem miNewLv;
    private javax.swing.JMenuItem miNewSklChild;
    private javax.swing.JMenuItem miNewSklFromType;
    private javax.swing.JMenuItem miNewSklRoot;
    private javax.swing.JMenuItem miNewType;
    private javax.swing.JMenuItem miRebuldLvIndex;
    private javax.swing.JMenuItem miRemoveLv;
    private javax.swing.JMenuItem miRevSkl;
    private javax.swing.JMenuItem miRevertType;
    private javax.swing.JMenuItem miSetLvMaster;
    private javax.swing.JMenuItem miSetSklRoot;
    private javax.swing.JPanel palInfo;
    private javax.swing.JPanel palSklInfo;
    private javax.swing.JPanel palSklLevel;
    private javax.swing.JPopupMenu popLvs;
    private javax.swing.JPopupMenu popSkl;
    private javax.swing.JPopupMenu popType;
    private javax.swing.JTabbedPane tabSkill;
    private javax.swing.JTable tbLevels;
    private javax.swing.JTree trSkls;
    private javax.swing.JTree trTypes;
    private javax.swing.JTextField txtGeSkillImpl;
    private javax.swing.JTextField txtGetJudgeImpl;
    private javax.swing.JTextField txtHide;
    private javax.swing.JTextField txtLevelChangeImpl;
    private javax.swing.JTextField txtLevelData;
    private javax.swing.JTextField txtLevelInvoke;
    private javax.swing.JTextField txtLevelType;
    private javax.swing.JTextField txtLossSkillImpl;
    private javax.swing.JTextField txtSameData;
    private javax.swing.JTextField txtSkillDesp;
    private javax.swing.JTextField txtSkillName;
    private javax.swing.JTextField txtSkillTag;
    private javax.swing.JTextField txtSkillUpgrade;
    private javax.swing.JTextField txtUpgradeJudge;
    // End of variables declaration//GEN-END:variables
}
