package dev.xlin.gameworkshop.GUI.CONTENT;

import dev.xlin.gameworkshop.GUI.CONTENT.dialog.dlgCtxWorldConfig;
import dev.xlin.gameworkshop.GUI.CONTENT.dialog.dlgCtxWorldMain;
import dev.xlin.gameworkshop.GUI.CONTENT.dialog.dlgCtxWorldSet;
import dev.xlin.gameworkshop.GUI.CONTENT.dialog.dlgCtxWorldTypeResConfig;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.systemIconLib;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxBaseResource;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldCfgResItem;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldTypeConfig;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldTypeMain;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldTypeSet;
import dev.xlin.gameworkshop.progs.contents.progs.baseResourceDefine;
import dev.xlin.gameworkshop.progs.contents.progs.ctxConst;
import dev.xlin.gameworkshop.progs.contents.progs.ctxTranslate;
import dev.xlin.gameworkshop.progs.contents.progs.worldTypeConfig;
import dev.xlin.gameworkshop.progs.contents.progs.worldTypeConfigResource;
import dev.xlin.gameworkshop.progs.contents.progs.worldTypeMain;
import dev.xlin.gameworkshop.progs.contents.progs.worldTypeSet;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myCellRenderer;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.swingTools2.myTreeRenderer;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.constChk;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author 刘祎鹏
 */
public class ifrmCtxWorldConfig extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private worldTypeMain wtm = null;
    private worldTypeSet wts = null;
    private worldTypeConfig wtc = null;
    public static final int NODE_ROOT = 0;
    public static final int NODE_MAIN = 1;
    public static final int NODE_SET = 2;
    public static final int NODE_CONFIG = 3;
    private boolean bInitTable = false;
    private List LRS = null;

    public ifrmCtxWorldConfig(wakeup _up, JDesktopPane _desk)
    {
        initComponents();
        up = _up;
        desk = _desk;
        wtm = new worldTypeMain(up);
        wts = new worldTypeSet(up);
        wtc = new worldTypeConfig(up);
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        makeWorldTypeCombo();
        makeTree();
        makeResCfgTable();
    }

    private void makeResCfgTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("资源");
            mtm.addColumn("描述");
            mtm.addColumn("概率");
            mtm.addColumn("数量");
            mtm.addColumn("容量");
            mtm.addColumn("难度");
            mtm.addColumn("效率");
            mtm.addColumn("危险");
            tbRes.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbRes);
            mtm = (myTableModel) tbRes.getModel();
        }
        myTreeNode msel = getSelectedNode();
        int ncid = 0;
        if (msel != null)
        {
            if (msel.getNodeType() == NODE_CONFIG)
            {
                ncid = msel.getNodeOID();
            }
        }
        worldTypeConfigResource wtcr = new worldTypeConfigResource(up);
        LRS = wtcr.getResourceListByConfig(ncid);
        baseResourceDefine brd = new baseResourceDefine(up);
        if (LRS != null)
        {
            for (int i = 0; i < LRS.size(); i++)
            {
                beanCtxWorldCfgResItem bean = (beanCtxWorldCfgResItem) LRS.get(i);
                Object[] ros = fast.makeObjectArray(8);
                beanCtxBaseResource bcbr = (beanCtxBaseResource) brd.getRecordByID(bean.getResOID());
                if (bcbr != null)
                {
                    ros[0] = bcbr.getResName();
                }
                else
                {
                    ros[0] = "-";
                }
                ros[1] = bean.getCfgDescp();
                ros[2] = fast.makeRound2(bean.getProbability() * 100.0) + "%";
                ros[3] = bean.getCountMin() + "~" + bean.getCountMax();
                ros[4] = bean.getCapMin() + "~" + bean.getCapMax();
                ros[5] = fast.makeRound2(bean.getDiffcMin() * 100.0) + "%" + "~" + fast.makeRound2(bean.getDiffcMax() * 100.0) + "%";
                ros[6] = fast.makeRound2(bean.getEffiMin() * 100.0) + "%" + "~" + fast.makeRound2(bean.getEffiMax() * 100.0) + "%";
                ros[7] = fast.makeRound2(bean.getDangerMin() * 100) +"%" +"~" + fast.makeRound2(bean.getDangerMax() *100.0) + "%";
                mtm.addRow(ros);
            }
        }
        tbRes.setModel(mtm);
    }

    private String makeMainTitle(beanCtxWorldTypeMain bean)
    {
        String s = bean.getWmName() + "<" + bean.getWmTag() + ">";
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            s = s + " [失效]";
        }
        return s;
    }

    private String makeSetTitle(beanCtxWorldTypeSet bean)
    {
        String s = bean.getSetName() + "<" + bean.getSetTag() + ">";
        if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
        {
            s = s + " [失效]";
        }
        return s;
    }

    private void makeTree()
    {
        myTreeNode mrt = new myTreeNode("[世界配置数据库]", 0, 0);
        mrt.setIco(systemIconLib.icoData);
        int wtid = swsys.getComboBoxSelected(cmbWorldType);
        List lms = wtm.getWorldMainlistByType(wtid, ckShowAllTps.isSelected());
        if (lms != null)
        {
            for (int i = 0; i < lms.size(); i++)
            {
                beanCtxWorldTypeMain bwtm = (beanCtxWorldTypeMain) lms.get(i);
                myTreeNode mtm = new myTreeNode(makeMainTitle(bwtm), bwtm.getOID(), NODE_MAIN);
                mtm.setIco(systemIconLib.icoFolder);
                List lss = wts.getWorldSetsByMainType(bwtm.getOID(), ckShowAllTps.isSelected());
                if (lss != null)
                {
                    for (int j = 0; j < lss.size(); j++)
                    {
                        beanCtxWorldTypeSet bwts = (beanCtxWorldTypeSet) lss.get(j);
                        myTreeNode mts = new myTreeNode(makeSetTitle(bwts), bwts.getOID(), NODE_SET);
                        mts.setIco(systemIconLib.icoFolder2);
                        List lcs = wtc.getConfigsByWorldSet(bwts.getOID());
                        if (lcs != null)
                        {
                            for (int k = 0; k < lcs.size(); k++)
                            {
                                beanCtxWorldTypeConfig bwtc = (beanCtxWorldTypeConfig) lcs.get(k);
                                myTreeNode mtc = new myTreeNode(bwtc.getCfgName() + "<" + bwtc.getCfgTag() + ">", bwtc.getOID(), NODE_CONFIG);
                                mtc.setIco(systemIconLib.icoNode2);
                                mts.add(mtc);
                            }
                        }
                        mtm.add(mts);
                    }
                }
                mrt.add(mtm);
            }
        }
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trTps.setModel(dtm);
        myTreeRenderer mtr = new myTreeRenderer();
        trTps.setCellRenderer(mtr);
        guiCommon.expandTree(trTps);
    }

    private void makeWorldTypeCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        int[] ids = constChk.getFinalInts(ctxConst.class, "WORLD_TYPE_");
        for (int i = 0; i < ids.length; i++)
        {
            int id = ids[i];
            listItem li = new listItem(ctxTranslate.translateConst(id), id);
            li.setIco(systemIconLib.icoNode4);
            mod.addElement(li);
        }
        cmbWorldType.setModel(mod);
        cmbWorldType.setRenderer(new myCellRenderer());
    }

    private myTreeNode getSelectedNode()
    {
        TreePath tph = trTps.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        return (myTreeNode) tph.getLastPathComponent();
    }

    private int getSelectedNodeType()
    {
        myTreeNode mtn = getSelectedNode();
        if (mtn == null)
        {
            return -1;
        }
        return mtn.getNodeType();
    }

    private int getSelectedNodeOID()
    {
        myTreeNode mtn = getSelectedNode();
        if (mtn == null)
        {
            return -1;
        }
        return mtn.getNodeOID();
    }

    private void makeMiTpss()
    {
        disMiTpss();
        int itp = getSelectedNodeType();
        if (itp == NODE_MAIN)
        {
            miEditMain.setVisible(true);
            miDesMain.setVisible(true);
            miDisMain.setVisible(true);
            miRevMain.setVisible(true);
            miNewSet.setVisible(true);
        }
        else if (itp == NODE_SET)
        {
            spSet.setVisible(true);
            miEditSet.setVisible(true);
            miDisSet.setVisible(true);
            miRevSet.setVisible(true);
            miDesSet.setVisible(true);
            miNewCfg.setVisible(true);
        }
        else if (itp == NODE_CONFIG)
        {
            spConfig.setVisible(true);
            miEditCfg.setVisible(true);
            miDelCfg.setVisible(true);
            miNewResFromCfg.setVisible(true);
        }
    }

    private void disMiTpss()
    {
        miDelCfg.setVisible(false);
        miDesMain.setVisible(false);
        miDesSet.setVisible(false);
        miDisMain.setVisible(false);
        miDisSet.setVisible(false);
        miEditCfg.setVisible(false);
        miEditMain.setVisible(false);
        miEditSet.setVisible(false);
        miNewCfg.setVisible(false);
        miNewResFromCfg.setVisible(false);
        miNewSet.setVisible(false);
        miRevMain.setVisible(false);
        miRevSet.setVisible(false);
        spConfig.setVisible(false);
        spSet.setVisible(false);
    }

    private void newMain()
    {
        int tpcst = swsys.getComboBoxSelected(cmbWorldType);
        dlgCtxWorldMain dlg = new dlgCtxWorldMain(null, true, up, null, tpcst);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void editMain()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_MAIN)
        {
            return;
        }
        int moid = msel.getNodeOID();
        worldTypeMain wtm = new worldTypeMain(up);
        beanCtxWorldTypeMain bean = (beanCtxWorldTypeMain) wtm.getRecordByID(moid);
        dlgCtxWorldMain dlg = new dlgCtxWorldMain(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void disMain()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_MAIN)
        {
            return;
        }
        int sel = fast.ask("是否将这个类型设置为失效？");
        if (sel != fast.YES)
        {
            return;
        }
        worldTypeMain wtm = new worldTypeMain(up);
        int r = wtm.deleteRecord(msel.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void revMain()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_MAIN)
        {
            return;
        }
        int sel = fast.ask("是否将这个类型恢复为有效？");
        if (sel != fast.YES)
        {
            return;
        }
        worldTypeMain wtm = new worldTypeMain(up);
        System.err.println(".sel .id = " + msel.getNodeOID());
        int r = wtm.revertBean(msel.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void desMain()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_MAIN)
        {
            return;
        }
        int sel = fast.ask("是否将这个类型销毁？");
        if (sel != fast.YES)
        {
            return;
        }
        worldTypeMain wtm = new worldTypeMain(up);
        int r = wtm.destroyBean(msel.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void newSet()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_MAIN)
        {
            return;
        }
        dlgCtxWorldSet dlg = new dlgCtxWorldSet(null, true, up, null, msel.getNodeOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void editSet()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_SET)
        {
            return;
        }
        worldTypeSet wts = new worldTypeSet(up);
        beanCtxWorldTypeSet bean = (beanCtxWorldTypeSet) wts.getRecordByID(msel.getNodeOID());
        dlgCtxWorldSet dlg = new dlgCtxWorldSet(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void disSet()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_SET)
        {
            return;
        }
        int sel = fast.ask("是否将这个分类设置为失效？");
        if (sel != fast.YES)
        {
            return;
        }
        worldTypeSet wts = new worldTypeSet(up);
        int r = wts.deleteRecord(msel.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void revSet()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_SET)
        {
            return;
        }
        int sel = fast.ask("是否将这个分类恢复为有效？");
        if (sel != fast.YES)
        {
            return;
        }
        worldTypeSet wts = new worldTypeSet(up);
        int r = wts.revertBean(msel.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void desSet()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_SET)
        {
            return;
        }
        int sel = fast.ask("是否将这个分类销毁？");
        if (sel != fast.YES)
        {
            return;
        }
        worldTypeSet wts = new worldTypeSet(up);
        int r = wts.destroyBean(msel.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void newConfig()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_SET)
        {
            return;
        }
        dlgCtxWorldConfig dlg = new dlgCtxWorldConfig(null, true, up, null, msel.getNodeOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void editConfig()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_CONFIG)
        {
            return;
        }
        worldTypeConfig wtc = new worldTypeConfig(up);
        beanCtxWorldTypeConfig bean = (beanCtxWorldTypeConfig) wtc.getRecordByID(msel.getNodeOID());
        dlgCtxWorldConfig dlg = new dlgCtxWorldConfig(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void deleteConfig()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_CONFIG)
        {
            return;
        }
        int sel = fast.ask("是否要删除这个配置项");
        if (sel != fast.YES)
        {
            return;
        }
        worldTypeConfig wtc = new worldTypeConfig(up);
        int r = wtc.deleteRecord(msel.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("删除操作失败", r);
        }
    }

    private void newResConfig()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return;
        }
        if (msel.getNodeType() != NODE_CONFIG)
        {
            return;
        }
        dlgCtxWorldTypeResConfig dlg = new dlgCtxWorldTypeResConfig(null, true, up, null, msel.getNodeOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeResCfgTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private beanCtxWorldCfgResItem getSelectedResCfg()
    {
        int idx = tbRes.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (beanCtxWorldCfgResItem) LRS.get(idx);
    }

    private void editResConfig()
    {
        beanCtxWorldCfgResItem bean = getSelectedResCfg();
        if (bean == null)
        {
            return;
        }
        dlgCtxWorldTypeResConfig dlg = new dlgCtxWorldTypeResConfig(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeResCfgTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void deleteResConfig()
    {
        beanCtxWorldCfgResItem bean = getSelectedResCfg();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要删除这个资源配置数据节点");
        if (sel != fast.YES)
        {
            return;
        }
        worldTypeConfigResource wtcr = new worldTypeConfigResource(up);
        int r = wtcr.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeResCfgTable();
        }
        else
        {
            fast.err("删除配置数据失败", r);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popTps = new javax.swing.JPopupMenu();
        miNewMain = new javax.swing.JMenuItem();
        miEditMain = new javax.swing.JMenuItem();
        miDisMain = new javax.swing.JMenuItem();
        miRevMain = new javax.swing.JMenuItem();
        miDesMain = new javax.swing.JMenuItem();
        spSet = new javax.swing.JPopupMenu.Separator();
        miNewSet = new javax.swing.JMenuItem();
        miEditSet = new javax.swing.JMenuItem();
        miDisSet = new javax.swing.JMenuItem();
        miRevSet = new javax.swing.JMenuItem();
        miDesSet = new javax.swing.JMenuItem();
        spConfig = new javax.swing.JPopupMenu.Separator();
        miNewCfg = new javax.swing.JMenuItem();
        miEditCfg = new javax.swing.JMenuItem();
        miDelCfg = new javax.swing.JMenuItem();
        miNewResFromCfg = new javax.swing.JMenuItem();
        popRes = new javax.swing.JPopupMenu();
        miNewResCfgTable = new javax.swing.JMenuItem();
        miEditResCfg = new javax.swing.JMenuItem();
        miDeleteResCfg = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        ckShowAllTps = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        trTps = new javax.swing.JTree();
        cmbWorldType = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        scpRes = new javax.swing.JScrollPane();
        tbRes = new javax.swing.JTable();

        miNewMain.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewMain.setText("新建主类型");
        miNewMain.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewMainActionPerformed(evt);
            }
        });
        popTps.add(miNewMain);

        miEditMain.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditMain.setText("修改主类型");
        miEditMain.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditMainActionPerformed(evt);
            }
        });
        popTps.add(miEditMain);

        miDisMain.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisMain.setText("失效");
        miDisMain.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisMainActionPerformed(evt);
            }
        });
        popTps.add(miDisMain);

        miRevMain.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevMain.setText("恢复");
        miRevMain.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevMainActionPerformed(evt);
            }
        });
        popTps.add(miRevMain);

        miDesMain.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesMain.setText("销毁");
        miDesMain.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesMainActionPerformed(evt);
            }
        });
        popTps.add(miDesMain);
        popTps.add(spSet);

        miNewSet.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewSet.setText("新建分类");
        miNewSet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewSetActionPerformed(evt);
            }
        });
        popTps.add(miNewSet);

        miEditSet.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditSet.setText("修改分类");
        miEditSet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditSetActionPerformed(evt);
            }
        });
        popTps.add(miEditSet);

        miDisSet.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisSet.setText("失效");
        miDisSet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisSetActionPerformed(evt);
            }
        });
        popTps.add(miDisSet);

        miRevSet.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevSet.setText("恢复");
        miRevSet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevSetActionPerformed(evt);
            }
        });
        popTps.add(miRevSet);

        miDesSet.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesSet.setText("销毁");
        miDesSet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesSetActionPerformed(evt);
            }
        });
        popTps.add(miDesSet);
        popTps.add(spConfig);

        miNewCfg.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewCfg.setText("新建配置库");
        miNewCfg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewCfgActionPerformed(evt);
            }
        });
        popTps.add(miNewCfg);

        miEditCfg.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditCfg.setText("修改配置");
        miEditCfg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditCfgActionPerformed(evt);
            }
        });
        popTps.add(miEditCfg);

        miDelCfg.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDelCfg.setText("删除配置");
        miDelCfg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDelCfgActionPerformed(evt);
            }
        });
        popTps.add(miDelCfg);

        miNewResFromCfg.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewResFromCfg.setText("新建资源配制节点");
        miNewResFromCfg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewResFromCfgActionPerformed(evt);
            }
        });
        popTps.add(miNewResFromCfg);

        miNewResCfgTable.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewResCfgTable.setText("添加资源配置");
        miNewResCfgTable.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewResCfgTableActionPerformed(evt);
            }
        });
        popRes.add(miNewResCfgTable);

        miEditResCfg.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditResCfg.setText("修改");
        miEditResCfg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditResCfgActionPerformed(evt);
            }
        });
        popRes.add(miEditResCfg);

        miDeleteResCfg.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDeleteResCfg.setText("删除");
        miDeleteResCfg.setToolTipText("");
        miDeleteResCfg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDeleteResCfgActionPerformed(evt);
            }
        });
        popRes.add(miDeleteResCfg);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("WTPS配置管理");

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

        ckShowAllTps.setText("显示全部");
        ckShowAllTps.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllTpsActionPerformed(evt);
            }
        });

        trTps.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                trTpsMouseClicked(evt);
            }
        });
        trTps.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trTpsValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(trTps);

        cmbWorldType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbWorldType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbWorldTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllTps)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbWorldType, 0, 206, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckShowAllTps)
                    .addComponent(cmbWorldType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ckShowAllTps, cmbWorldType});

        jSplitPane1.setLeftComponent(jPanel1);

        scpRes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scpResMouseClicked(evt);
            }
        });

        tbRes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbRes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbRes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbResMouseClicked(evt);
            }
        });
        scpRes.setViewportView(tbRes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpRes, javax.swing.GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpRes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ckShowAllTpsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllTpsActionPerformed
    {//GEN-HEADEREND:event_ckShowAllTpsActionPerformed
        makeTree();
    }//GEN-LAST:event_ckShowAllTpsActionPerformed

    private void cmbWorldTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbWorldTypeActionPerformed
    {//GEN-HEADEREND:event_cmbWorldTypeActionPerformed
        makeTree();
    }//GEN-LAST:event_cmbWorldTypeActionPerformed

    private void trTpsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_trTpsMouseClicked
    {//GEN-HEADEREND:event_trTpsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popTps.show(trTps, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() > 1)
        {
            int stp = getSelectedNodeType();
            if (stp == NODE_SET)
            {
                editSet();
            }
            else if (stp == NODE_MAIN)
            {
                editMain();
            }
            else if (stp == NODE_CONFIG)
            {
                editConfig();
            }
            else if (stp == NODE_ROOT)
            {
                newMain();
            }
        }
    }//GEN-LAST:event_trTpsMouseClicked

    private void trTpsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trTpsValueChanged
    {//GEN-HEADEREND:event_trTpsValueChanged
        makeMiTpss();
        makeResCfgTable();
    }//GEN-LAST:event_trTpsValueChanged

    private void miNewMainActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewMainActionPerformed
    {//GEN-HEADEREND:event_miNewMainActionPerformed
        newMain();
    }//GEN-LAST:event_miNewMainActionPerformed

    private void miEditMainActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditMainActionPerformed
    {//GEN-HEADEREND:event_miEditMainActionPerformed
        editMain();
    }//GEN-LAST:event_miEditMainActionPerformed

    private void miNewSetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewSetActionPerformed
    {//GEN-HEADEREND:event_miNewSetActionPerformed
        newSet();
    }//GEN-LAST:event_miNewSetActionPerformed

    private void miEditSetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditSetActionPerformed
    {//GEN-HEADEREND:event_miEditSetActionPerformed
        editSet();
    }//GEN-LAST:event_miEditSetActionPerformed

    private void miNewCfgActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewCfgActionPerformed
    {//GEN-HEADEREND:event_miNewCfgActionPerformed
        newConfig();
    }//GEN-LAST:event_miNewCfgActionPerformed

    private void miEditCfgActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditCfgActionPerformed
    {//GEN-HEADEREND:event_miEditCfgActionPerformed
        editConfig();
    }//GEN-LAST:event_miEditCfgActionPerformed

    private void miNewResFromCfgActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewResFromCfgActionPerformed
    {//GEN-HEADEREND:event_miNewResFromCfgActionPerformed
        newResConfig();
    }//GEN-LAST:event_miNewResFromCfgActionPerformed

    private void tbResMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbResMouseClicked
    {//GEN-HEADEREND:event_tbResMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popRes.show(tbRes, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() > 1)
        {
            editResConfig();
        }
    }//GEN-LAST:event_tbResMouseClicked

    private void scpResMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scpResMouseClicked
    {//GEN-HEADEREND:event_scpResMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popRes.show(scpRes, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_scpResMouseClicked

    private void miEditResCfgActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditResCfgActionPerformed
    {//GEN-HEADEREND:event_miEditResCfgActionPerformed
        editResConfig();
    }//GEN-LAST:event_miEditResCfgActionPerformed

    private void miDeleteResCfgActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDeleteResCfgActionPerformed
    {//GEN-HEADEREND:event_miDeleteResCfgActionPerformed
        deleteResConfig();
    }//GEN-LAST:event_miDeleteResCfgActionPerformed

    private void miNewResCfgTableActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewResCfgTableActionPerformed
    {//GEN-HEADEREND:event_miNewResCfgTableActionPerformed
        newResConfig();
    }//GEN-LAST:event_miNewResCfgTableActionPerformed

    private void miDelCfgActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDelCfgActionPerformed
    {//GEN-HEADEREND:event_miDelCfgActionPerformed
        deleteConfig();
    }//GEN-LAST:event_miDelCfgActionPerformed

    private void miDisSetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisSetActionPerformed
    {//GEN-HEADEREND:event_miDisSetActionPerformed
        disSet();
    }//GEN-LAST:event_miDisSetActionPerformed

    private void miRevSetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevSetActionPerformed
    {//GEN-HEADEREND:event_miRevSetActionPerformed
        revSet();
    }//GEN-LAST:event_miRevSetActionPerformed

    private void miDesSetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesSetActionPerformed
    {//GEN-HEADEREND:event_miDesSetActionPerformed
        desSet();
    }//GEN-LAST:event_miDesSetActionPerformed

    private void miDisMainActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisMainActionPerformed
    {//GEN-HEADEREND:event_miDisMainActionPerformed
        disMain();
    }//GEN-LAST:event_miDisMainActionPerformed

    private void miRevMainActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevMainActionPerformed
    {//GEN-HEADEREND:event_miRevMainActionPerformed
        revMain();
    }//GEN-LAST:event_miRevMainActionPerformed

    private void miDesMainActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesMainActionPerformed
    {//GEN-HEADEREND:event_miDesMainActionPerformed
        desMain();
    }//GEN-LAST:event_miDesMainActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ckShowAllTps;
    private javax.swing.JComboBox<String> cmbWorldType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem miDelCfg;
    private javax.swing.JMenuItem miDeleteResCfg;
    private javax.swing.JMenuItem miDesMain;
    private javax.swing.JMenuItem miDesSet;
    private javax.swing.JMenuItem miDisMain;
    private javax.swing.JMenuItem miDisSet;
    private javax.swing.JMenuItem miEditCfg;
    private javax.swing.JMenuItem miEditMain;
    private javax.swing.JMenuItem miEditResCfg;
    private javax.swing.JMenuItem miEditSet;
    private javax.swing.JMenuItem miNewCfg;
    private javax.swing.JMenuItem miNewMain;
    private javax.swing.JMenuItem miNewResCfgTable;
    private javax.swing.JMenuItem miNewResFromCfg;
    private javax.swing.JMenuItem miNewSet;
    private javax.swing.JMenuItem miRevMain;
    private javax.swing.JMenuItem miRevSet;
    private javax.swing.JPopupMenu popRes;
    private javax.swing.JPopupMenu popTps;
    private javax.swing.JScrollPane scpRes;
    private javax.swing.JPopupMenu.Separator spConfig;
    private javax.swing.JPopupMenu.Separator spSet;
    private javax.swing.JTable tbRes;
    private javax.swing.JTree trTps;
    // End of variables declaration//GEN-END:variables
}
