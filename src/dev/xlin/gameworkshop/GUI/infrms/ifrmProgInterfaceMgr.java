package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.dlgProgIntfDef;
import dev.xlin.gameworkshop.GUI.dialog.dlgProgIntfReg;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.GUI.guiFullTreeGuiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfRegister;
import dev.xlin.gameworkshop.progs.foundation.interfaceDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaceRegister;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.dlgTools.dlgSelectTreeNode;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import java.util.Vector;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;

public class ifrmProgInterfaceMgr extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private boolean bInitDefTb = false;
    private boolean bInitRegTb = false;
    private List ldefs = null;
    private List lregs = null;

    public ifrmProgInterfaceMgr(wakeup _up, JDesktopPane _desk)
    {
        initComponents();
        up = _up;
        desk = _desk;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        makeTypeTree();
        makeDefTable();
        makeRegTable();
    }

    private void makeTypeTree()
    {
        myTreeNode mrt = guiCodes.makeFullTypeTree(up, systemType.CODE_STT_PROG_INTERFACE, ckShowAllTps.isSelected(), 0);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeTps.setModel(dtm);
        guiCommon.expandTree(treeTps);
    }

    private void makeDefTable()
    {
        myTableModel mtm = null;
        if (bInitDefTb == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("名称");
            mtm.addColumn("标签");
            mtm.addColumn("描述");
            mtm.addColumn("地址");
            mtm.addColumn("系统");
            mtm.addColumn("状态");
            tbDefs.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbDefs);
            mtm = (myTableModel) tbDefs.getModel();
        }
        interfaceDefine idef = new interfaceDefine(up);
        myTreeNode mtp = guiFullTreeGuiCodes.getSelectedType(treeTps);
        int tpid = 0;
        if (mtp != null)
        {
            tpid = mtp.getNodeOID();
        }
        ldefs = idef.getInterfacesByType(tpid, ckShowAllDefs.isSelected());
        if (ldefs != null)
        {
            for (int i = 0; i < ldefs.size(); i++)
            {
                beanProgIntfDefine bdef = (beanProgIntfDefine) ldefs.get(i);
                Vector v = new Vector();
                v.add(bdef.getIntfName());
                v.add(bdef.getIntfTag());
                v.add(bdef.getIntfDesp());
                v.add(bdef.getIntfAddress());
                v.add(iConst.translateIBOL(bdef.getSystemIntf()));
                v.add(iConst.transDAOState(bdef.getStatus()));
                mtm.addRow(v);
            }
        }
        tbDefs.setModel(mtm);
    }

    private void makeRegTable()
    {
        myTableModel mtm = null;
        if (bInitRegTb == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("名称");
            mtm.addColumn("标签");
            mtm.addColumn("地址"); 
            mtm.addColumn("状态");
            tbRegs.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbRegs);
            mtm = (myTableModel) tbRegs.getModel();
        }
        int doid = 0;
        beanProgIntfDefine bdef = getSelectedDef();
        if (bdef != null)
        {
            doid = bdef.getOID();
        }
        interfaceRegister ireg = new interfaceRegister(up);
        lregs = ireg.getRegsByDef(doid, ckShowAllRegs.isSelected());
        if (lregs != null)
        {
            for (int i = 0; i < lregs.size(); i++)
            {
                beanProgIntfRegister breg = (beanProgIntfRegister) lregs.get(i);
                Vector v = new Vector();
                v.add(breg.getRegName());
                v.add(breg.getRegTag());
                v.add(breg.getImplAddress());
                v.add(iConst.transDAOState(breg.getStatus()));
                mtm.addRow(v);
            }
        }
        tbRegs.setModel(mtm);
    }

    private void doNewType()
    {
        int r = guiFullTreeGuiCodes.doNewType(treeTps, up, systemType.CODE_STT_PROG_INTERFACE);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("创建分类操作失败", r);
        }
    }

    private void doEditType()
    {
        int r = guiFullTreeGuiCodes.doEditType(treeTps, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("修改分类操作失败", r);
        }
    }

    private void doDisType()
    {
        int r = guiFullTreeGuiCodes.doDisType(treeTps, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("失效分类操作失败", r);
        }
    }

    private void doRevType()
    {
        int r = guiFullTreeGuiCodes.doRevType(treeTps, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("恢复分类操作失败", r);
        }
    }

    private void doDesType()
    {
        int r = guiFullTreeGuiCodes.doDesType(treeTps, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("销毁分类操作失败", r);
        }
    }

    private void doMoveType()
    {
        int r = guiFullTreeGuiCodes.doMoveType(treeTps, up, systemType.CODE_STT_PROG_INTERFACE);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("移动分类操作失败", r);
        }
    }

    private void doNewIntfDefine()
    {
        myTreeNode msel = guiFullTreeGuiCodes.getSelectedType(treeTps);
        if (msel == null)
        {
            return;
        }
        dlgProgIntfDef dlg = new dlgProgIntfDef(null, true, up, null, msel.getNodeOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeDefTable();
            fast.msg("添加接口定义操作完成");
        }
        dlg.dispose();
        dlg = null;
    }

    private beanProgIntfDefine getSelectedDef()
    {
        int idx = tbDefs.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (beanProgIntfDefine) ldefs.get(idx);
    }

    private void doEditDef()
    {
        beanProgIntfDefine bean = getSelectedDef();
        if (bean == null)
        {
            return;
        }
        dlgProgIntfDef dlg = new dlgProgIntfDef(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeDefTable();
            fast.msg("修改接口定义操作完成");
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDisDef()
    {
        beanProgIntfDefine bean = getSelectedDef();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将选择的接口数据设置为失效？");
        if (sel != fast.YES)
        {
            return;
        }
        interfaceDefine idef = new interfaceDefine(up);
        int r = idef.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeDefTable();
            fast.msg("失效操作完成");
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void doRevDef()
    {
        beanProgIntfDefine bean = getSelectedDef();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将选择的接口数据恢复为有效？");
        if (sel != fast.YES)
        {
            return;
        }
        interfaceDefine idef = new interfaceDefine(up);
        int r = idef.revertBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeDefTable();
            fast.msg("恢复操作完成");
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void doDesDef()
    {
        beanProgIntfDefine bean = getSelectedDef();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将选择的接口数据销毁？\n这个操作将不可恢复\n这个操作会因为还有实现该接口的实例而失败！");
        if (sel != fast.YES)
        {
            return;
        }
        interfaceDefine idef = new interfaceDefine(up);
        int r = idef.destroyBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeDefTable();
            fast.msg("销毁操作完成");
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void doMoveDef()
    {
        beanProgIntfDefine bean = getSelectedDef();
        if (bean == null)
        {
            return;
        }
        myTreeNode mrt = guiCodes.makeFullTypeTree(up, systemType.CODE_STT_PROG_INTERFACE, false, bean.getTypeID());
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mrt);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeDefTable();
            fast.msg("移动操作完成");
        }
        dlg.dispose();
        dlg = null;
    }

    private void doNewImpl()
    {
        beanProgIntfDefine bdef = getSelectedDef();
        if (bdef == null)
        {
            return;
        }
        dlgProgIntfReg dlg = new dlgProgIntfReg(null, true, up, null, bdef.getOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeRegTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private beanProgIntfRegister getSelectedReg()
    {
        int idx = tbRegs.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (beanProgIntfRegister) lregs.get(idx);
    }

    private void doEditImpl()
    {
        beanProgIntfRegister bean = getSelectedReg();
        if (bean == null)
        {
            return;
        }
        dlgProgIntfReg dlg = new dlgProgIntfReg(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeRegTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDisImpl()
    {
        beanProgIntfRegister bean = getSelectedReg();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将选择的程序接口设置为失效？");
        if (sel != fast.YES)
        {
            return;
        }
        interfaceRegister ireg = new interfaceRegister(up);
        int r = ireg.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeRegTable();
            fast.msg("操作完成");
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void doRevImpl()
    {
        beanProgIntfRegister bean = getSelectedReg();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将选择的程序接口恢复为有效？");
        if (sel != fast.YES)
        {
            return;
        }
        interfaceRegister ireg = new interfaceRegister(up);
        int r = ireg.revertBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeRegTable();
            fast.msg("操作完成");
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void doDesImpl()
    {
        beanProgIntfRegister bean = getSelectedReg();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将选择的程序接口进行销毁？\n这个操作不可被恢复");
        if (sel != fast.YES)
        {
            return;
        }
        interfaceRegister ireg = new interfaceRegister(up);
        int r = ireg.destroyBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeRegTable();
            fast.msg("操作完成");
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popTree = new javax.swing.JPopupMenu();
        miNewType = new javax.swing.JMenuItem();
        miEditType = new javax.swing.JMenuItem();
        miDisType = new javax.swing.JMenuItem();
        miRevType = new javax.swing.JMenuItem();
        miDesType = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miMoveType = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miNewDef = new javax.swing.JMenuItem();
        popDef = new javax.swing.JPopupMenu();
        miNewIDef = new javax.swing.JMenuItem();
        miEditDef = new javax.swing.JMenuItem();
        miDisDef = new javax.swing.JMenuItem();
        miRevDef = new javax.swing.JMenuItem();
        miDesDef = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        miMoveDef = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        miNewImpl = new javax.swing.JMenuItem();
        popReg = new javax.swing.JPopupMenu();
        miNewReg = new javax.swing.JMenuItem();
        miEditReg = new javax.swing.JMenuItem();
        miDisReg = new javax.swing.JMenuItem();
        miRevReg = new javax.swing.JMenuItem();
        miDesReg = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        ckShowAllDefs = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDefs = new javax.swing.JTable();
        btnNewDef = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        ckShowAllRegs = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbRegs = new javax.swing.JTable();
        btnNewImpl = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        ckShowAllTps = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeTps = new javax.swing.JTree();

        miNewType.setText("新建分类");
        miNewType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewTypeActionPerformed(evt);
            }
        });
        popTree.add(miNewType);

        miEditType.setText("修改分类");
        miEditType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditTypeActionPerformed(evt);
            }
        });
        popTree.add(miEditType);

        miDisType.setText("失效分类");
        miDisType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisTypeActionPerformed(evt);
            }
        });
        popTree.add(miDisType);

        miRevType.setText("恢复分类");
        miRevType.setToolTipText("");
        miRevType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevTypeActionPerformed(evt);
            }
        });
        popTree.add(miRevType);

        miDesType.setText("销毁分类");
        miDesType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesTypeActionPerformed(evt);
            }
        });
        popTree.add(miDesType);
        popTree.add(jSeparator1);

        miMoveType.setText("移动分类");
        miMoveType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveTypeActionPerformed(evt);
            }
        });
        popTree.add(miMoveType);
        popTree.add(jSeparator2);

        miNewDef.setText("新建接口定义");
        miNewDef.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewDefActionPerformed(evt);
            }
        });
        popTree.add(miNewDef);

        miNewIDef.setText("新建定义");
        miNewIDef.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewIDefActionPerformed(evt);
            }
        });
        popDef.add(miNewIDef);

        miEditDef.setText("修改定义");
        miEditDef.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditDefActionPerformed(evt);
            }
        });
        popDef.add(miEditDef);

        miDisDef.setText("失效定义");
        miDisDef.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisDefActionPerformed(evt);
            }
        });
        popDef.add(miDisDef);

        miRevDef.setText("恢复定义");
        miRevDef.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevDefActionPerformed(evt);
            }
        });
        popDef.add(miRevDef);

        miDesDef.setText("销毁定义");
        miDesDef.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesDefActionPerformed(evt);
            }
        });
        popDef.add(miDesDef);
        popDef.add(jSeparator3);

        miMoveDef.setText("移动定义");
        miMoveDef.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveDefActionPerformed(evt);
            }
        });
        popDef.add(miMoveDef);
        popDef.add(jSeparator4);

        miNewImpl.setText("新建实现");
        popDef.add(miNewImpl);

        miNewReg.setText("新建注册");
        miNewReg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewRegActionPerformed(evt);
            }
        });
        popReg.add(miNewReg);

        miEditReg.setText("修改注册");
        miEditReg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditRegActionPerformed(evt);
            }
        });
        popReg.add(miEditReg);

        miDisReg.setText("失效注册");
        miDisReg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisRegActionPerformed(evt);
            }
        });
        popReg.add(miDisReg);

        miRevReg.setText("恢复注册");
        miRevReg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevRegActionPerformed(evt);
            }
        });
        popReg.add(miRevReg);

        miDesReg.setText("销毁注册");
        miDesReg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesRegActionPerformed(evt);
            }
        });
        popReg.add(miDesReg);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("应用接口程序管理器");
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

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

        jSplitPane2.setDividerLocation(400);
        jSplitPane2.setDividerSize(3);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        ckShowAllDefs.setText("显示全部接口");
        ckShowAllDefs.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllDefsActionPerformed(evt);
            }
        });

        tbDefs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbDefs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbDefs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbDefsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbDefs);

        btnNewDef.setText("添加定义");
        btnNewDef.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewDefActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllDefs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNewDef)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckShowAllDefs)
                    .addComponent(btnNewDef))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
        );

        jSplitPane2.setTopComponent(jPanel2);

        ckShowAllRegs.setText("显示全部注册");
        ckShowAllRegs.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllRegsActionPerformed(evt);
            }
        });

        tbRegs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbRegs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbRegs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbRegsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbRegs);

        btnNewImpl.setText("实现接口");
        btnNewImpl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewImplActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllRegs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNewImpl)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckShowAllRegs)
                    .addComponent(btnNewImpl))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );

        jSplitPane1.setRightComponent(jPanel1);

        ckShowAllTps.setText("显示全部分类");
        ckShowAllTps.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllTpsActionPerformed(evt);
            }
        });

        treeTps.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                treeTpsMouseClicked(evt);
            }
        });
        treeTps.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeTpsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treeTps);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllTps)
                .addContainerGap(184, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(ckShowAllTps)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ckShowAllTpsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllTpsActionPerformed
    {//GEN-HEADEREND:event_ckShowAllTpsActionPerformed
        makeTypeTree();
    }//GEN-LAST:event_ckShowAllTpsActionPerformed

    private void miNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewTypeActionPerformed
    {//GEN-HEADEREND:event_miNewTypeActionPerformed
        doNewType();
    }//GEN-LAST:event_miNewTypeActionPerformed

    private void miEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditTypeActionPerformed
    {//GEN-HEADEREND:event_miEditTypeActionPerformed
        doEditType();
    }//GEN-LAST:event_miEditTypeActionPerformed

    private void miDisTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisTypeActionPerformed
    {//GEN-HEADEREND:event_miDisTypeActionPerformed
        doDisType();
    }//GEN-LAST:event_miDisTypeActionPerformed

    private void miRevTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevTypeActionPerformed
    {//GEN-HEADEREND:event_miRevTypeActionPerformed
        doRevType();
    }//GEN-LAST:event_miRevTypeActionPerformed

    private void miDesTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesTypeActionPerformed
    {//GEN-HEADEREND:event_miDesTypeActionPerformed
        doDesType();
    }//GEN-LAST:event_miDesTypeActionPerformed

    private void miMoveTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveTypeActionPerformed
    {//GEN-HEADEREND:event_miMoveTypeActionPerformed
        doMoveType();
    }//GEN-LAST:event_miMoveTypeActionPerformed

    private void miNewDefActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewDefActionPerformed
    {//GEN-HEADEREND:event_miNewDefActionPerformed
        doNewIntfDefine();
    }//GEN-LAST:event_miNewDefActionPerformed

    private void treeTpsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeTpsMouseClicked
    {//GEN-HEADEREND:event_treeTpsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popTree.show(treeTps, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_treeTpsMouseClicked

    private void btnNewImplActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewImplActionPerformed
    {//GEN-HEADEREND:event_btnNewImplActionPerformed
        doNewImpl();
    }//GEN-LAST:event_btnNewImplActionPerformed

    private void tbDefsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbDefsMouseClicked
    {//GEN-HEADEREND:event_tbDefsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popDef.show(tbDefs, evt.getX(), evt.getY());
        }
        else
        {
            makeRegTable();
        }
    }//GEN-LAST:event_tbDefsMouseClicked

    private void miEditDefActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditDefActionPerformed
    {//GEN-HEADEREND:event_miEditDefActionPerformed
        doEditDef();
    }//GEN-LAST:event_miEditDefActionPerformed

    private void miDisDefActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisDefActionPerformed
    {//GEN-HEADEREND:event_miDisDefActionPerformed
        doDisDef();
    }//GEN-LAST:event_miDisDefActionPerformed

    private void miRevDefActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevDefActionPerformed
    {//GEN-HEADEREND:event_miRevDefActionPerformed
        doRevDef();
    }//GEN-LAST:event_miRevDefActionPerformed

    private void miDesDefActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesDefActionPerformed
    {//GEN-HEADEREND:event_miDesDefActionPerformed
        doDesDef();
    }//GEN-LAST:event_miDesDefActionPerformed

    private void miMoveDefActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveDefActionPerformed
    {//GEN-HEADEREND:event_miMoveDefActionPerformed
        doMoveDef();
    }//GEN-LAST:event_miMoveDefActionPerformed

    private void btnNewDefActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewDefActionPerformed
    {//GEN-HEADEREND:event_btnNewDefActionPerformed
        doNewIntfDefine();
    }//GEN-LAST:event_btnNewDefActionPerformed

    private void miNewIDefActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewIDefActionPerformed
    {//GEN-HEADEREND:event_miNewIDefActionPerformed
        doNewIntfDefine();
    }//GEN-LAST:event_miNewIDefActionPerformed

    private void treeTpsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeTpsValueChanged
    {//GEN-HEADEREND:event_treeTpsValueChanged
        makeDefTable();
        makeRegTable();
    }//GEN-LAST:event_treeTpsValueChanged

    private void ckShowAllDefsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllDefsActionPerformed
    {//GEN-HEADEREND:event_ckShowAllDefsActionPerformed
        makeDefTable();
    }//GEN-LAST:event_ckShowAllDefsActionPerformed

    private void ckShowAllRegsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllRegsActionPerformed
    {//GEN-HEADEREND:event_ckShowAllRegsActionPerformed
        makeRegTable();
    }//GEN-LAST:event_ckShowAllRegsActionPerformed

    private void miEditRegActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditRegActionPerformed
    {//GEN-HEADEREND:event_miEditRegActionPerformed
        doEditImpl();
    }//GEN-LAST:event_miEditRegActionPerformed

    private void miNewRegActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewRegActionPerformed
    {//GEN-HEADEREND:event_miNewRegActionPerformed
        doNewImpl();
    }//GEN-LAST:event_miNewRegActionPerformed

    private void miDesRegActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesRegActionPerformed
    {//GEN-HEADEREND:event_miDesRegActionPerformed
        doDesImpl();
    }//GEN-LAST:event_miDesRegActionPerformed

    private void miDisRegActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisRegActionPerformed
    {//GEN-HEADEREND:event_miDisRegActionPerformed
        doDisImpl();
    }//GEN-LAST:event_miDisRegActionPerformed

    private void miRevRegActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevRegActionPerformed
    {//GEN-HEADEREND:event_miRevRegActionPerformed
        doRevImpl();
    }//GEN-LAST:event_miRevRegActionPerformed

    private void tbRegsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbRegsMouseClicked
    {//GEN-HEADEREND:event_tbRegsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popReg.show(tbRegs, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbRegsMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsProgInterfaceMgr(false);
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewDef;
    private javax.swing.JButton btnNewImpl;
    private javax.swing.JCheckBox ckShowAllDefs;
    private javax.swing.JCheckBox ckShowAllRegs;
    private javax.swing.JCheckBox ckShowAllTps;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JMenuItem miDesDef;
    private javax.swing.JMenuItem miDesReg;
    private javax.swing.JMenuItem miDesType;
    private javax.swing.JMenuItem miDisDef;
    private javax.swing.JMenuItem miDisReg;
    private javax.swing.JMenuItem miDisType;
    private javax.swing.JMenuItem miEditDef;
    private javax.swing.JMenuItem miEditReg;
    private javax.swing.JMenuItem miEditType;
    private javax.swing.JMenuItem miMoveDef;
    private javax.swing.JMenuItem miMoveType;
    private javax.swing.JMenuItem miNewDef;
    private javax.swing.JMenuItem miNewIDef;
    private javax.swing.JMenuItem miNewImpl;
    private javax.swing.JMenuItem miNewReg;
    private javax.swing.JMenuItem miNewType;
    private javax.swing.JMenuItem miRevDef;
    private javax.swing.JMenuItem miRevReg;
    private javax.swing.JMenuItem miRevType;
    private javax.swing.JPopupMenu popDef;
    private javax.swing.JPopupMenu popReg;
    private javax.swing.JPopupMenu popTree;
    private javax.swing.JTable tbDefs;
    private javax.swing.JTable tbRegs;
    private javax.swing.JTree treeTps;
    // End of variables declaration//GEN-END:variables
}
