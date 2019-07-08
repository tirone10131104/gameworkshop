package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.DlgProgIntfSet;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiFullTreeGuiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfSet;
import dev.xlin.gameworkshop.progs.foundation.interfaceSet;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.dlgTools.dlgTreeToList;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.codeTools;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class IFrmProgIntfSets extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private boolean bInitSetTable = false;
    private boolean bInitIntfTable = false;
    private List lsets = null;
    private List lintfs = null;
    private interfaceSet iset = null;

    public IFrmProgIntfSets(wakeup _up, JDesktopPane _desk)
    {
        initComponents();
        up = _up;
        desk = _desk;
        iset = new interfaceSet(up);
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        makeTypeTree();
        makeSetTable();
        makeIntfTable();
    }

    private void makeTypeTree()
    {
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_PROG_INTF_SET, ckShowAllTypes.isSelected());
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trTypes.setModel(dtm);
        guiCommon.expandTree(trTypes);
    }

    private void makeSetTable()
    {
        myTableModel mtm = null;
        if (bInitSetTable)
        {
            fast.clearTableModel(tbSets);
            mtm = (myTableModel) tbSets.getModel();
        }
        else
        {
            mtm = new myTableModel();
            mtm.addColumn("名称");
            mtm.addColumn("标签");
            mtm.addColumn("描述");
            mtm.addColumn("状态");
            bInitSetTable = true;
        }
        beanSttType bst = getSelectedType();
        int tpid = 0;
        if (bst != null)
        {
            tpid = bst.getOID();
        }
        lsets = iset.getIntfSetsByType(tpid, ckShowAllSets.isSelected());
        if (lsets != null)
        {
            for (int i = 0; i < lsets.size(); i++)
            {
                BeanProgIntfSet bpis = (BeanProgIntfSet) lsets.get(i);
                Object[] orow = new Object[4];
                orow[0] = bpis.getSetName();
                orow[1] = bpis.getSetTag();
                orow[2] = bpis.getSetDesp();
                orow[3] = iConst.transDAOState(bpis.getStatus());
                mtm.addRow(orow);
            }
        }
        tbSets.setModel(mtm);
    }

    private void makeIntfTable()
    {
        myTableModel mtm = null;
        if (bInitIntfTable)
        {
            fast.clearTableModel(tbIntfDefs);
            mtm = (myTableModel) tbIntfDefs.getModel();
        }
        else
        {
            mtm = new myTableModel();
            mtm.addColumn("名称");
            mtm.addColumn("接口");
            bInitIntfTable = true;
        }
        BeanProgIntfSet bset = getSelectedIntfSet();
        String s = "";
        if (bset != null)
        {
            s = bset.getSetInterfaces();
        }
        lintfs = iset.loadInterfaceDefineByString(s);
        if (lintfs == null)
        {
            lintfs = new ArrayList();
        }
        for (int i = 0; i < lintfs.size(); i++)
        {
            BeanProgIntfDefine bpid = (BeanProgIntfDefine) lintfs.get(i);
            Object[] orow = new Object[2];
            orow[0] = bpid.getIntfName() + "<" + bpid.getIntfTag() + ">";
            orow[1] = bpid.getIntfAddress();
            mtm.addRow(orow);
        }
        tbIntfDefs.setModel(mtm);
    }

    private void newType()
    {
        int r = guiFullTreeGuiCodes.doNewType(trTypes, up, systemType.CODE_STT_PROG_INTF_SET);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("创建分类操作失败", r);
        }
    }

    private void editType()
    {
        int r = guiFullTreeGuiCodes.doEditType(trTypes, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("修改分类操作失败", r);
        }
    }

    private void disableType()
    {
        beanSttType bst = getSelectedType();
        if (bst == null)
        {
            return;
        }
        interfaceSet ifs = new interfaceSet(up);
        List lss = ifs.getIntfSetsByType(bst.getOID(), true);
        if (lss != null)
        {
            fast.warn("这个分类下还有接口集模板的定义\n不可以被失效");
            return;
        }
        int r = guiFullTreeGuiCodes.doDisType(trTypes, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("失效分类操作失败", r);
        }
    }

    private void revertType()
    {
        int r = guiFullTreeGuiCodes.doRevType(trTypes, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("恢复分类操作失败", r);
        }
    }

    private void destroyType()
    {
        int r = guiFullTreeGuiCodes.doDesType(trTypes, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("销毁分类操作失败", r);
        }
    }

    private beanSttType getSelectedType()
    {
        TreePath tph = trTypes.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        int tpid = mtn.getNodeOID();
        if (tpid == 0)
        {
            return null;
        }
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(tpid);
        return bst;
    }

    private void newIFS()
    {
        beanSttType bst = getSelectedType();
        if (bst == null)
        {
            return;
        }
        DlgProgIntfSet dlg = new DlgProgIntfSet(null, true, up, null, bst.getOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeSetTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanProgIntfSet getSelectedIntfSet()
    {
        int idx = tbSets.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanProgIntfSet) lsets.get(idx);
    }

    private void editIFS()
    {
        BeanProgIntfSet bean = getSelectedIntfSet();
        if (bean == null)
        {
            return;
        }
        DlgProgIntfSet dlg = new DlgProgIntfSet(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeSetTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void disableIFS()
    {
        BeanProgIntfSet bean = getSelectedIntfSet();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将选择的接口集模板设置为失效状态？");
        if (sel != fast.YES)
        {
            return;
        }
        int r = iset.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeSetTable();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void revertIFS()
    {
        BeanProgIntfSet bean = getSelectedIntfSet();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将选择的接口集模板恢复为有效状态？");
        if (sel != fast.YES)
        {
            return;
        }
        int r = iset.revertBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeSetTable();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void destroyIFS()
    {
        BeanProgIntfSet bean = getSelectedIntfSet();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将选择的接口集模板销毁？\n这个操作将不可被恢复。");
        if (sel != fast.YES)
        {
            return;
        }
        int r = iset.destroyBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeSetTable();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void configSetIntfs()
    {
        BeanProgIntfSet bean = getSelectedIntfSet();
        if (bean == null)
        {
            return;
        }
        myTreeNode mrt = guiCodes.makeProgInterfaceDefineTree(up);
        List lds = guiCodes.makeProgInterfaceListItems(up, bean);
        dlgTreeToList dlg = new dlgTreeToList(null, true, mrt, lds);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            List lsel = dlg.getSelectList();
            String srs = "";
            for (int i = 0; i < lsel.size(); i++)
            {
                listItem li = (listItem) lsel.get(i);
                srs = codeTools.addIntIntoString(srs, li.getNodeOID());
            }
            if (srs.trim().equals(""))
            {
                fast.warn("模板必须包含至少一个接口");
                return;
            }
            bean.setSetInterfaces(srs);
            int r = iset.updateInterfaceSet(bean);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeIntfTable();
            }
            else
            {
                fast.err("接口调整操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popTypes = new javax.swing.JPopupMenu();
        miNewType = new javax.swing.JMenuItem();
        miEditType = new javax.swing.JMenuItem();
        miDisableType = new javax.swing.JMenuItem();
        miRevertType = new javax.swing.JMenuItem();
        miDestroyType = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miNewIntfSetFromTp = new javax.swing.JMenuItem();
        popIFS = new javax.swing.JPopupMenu();
        miNewIFS = new javax.swing.JMenuItem();
        miEditIFS = new javax.swing.JMenuItem();
        miDisableIFS = new javax.swing.JMenuItem();
        miRevertIFS = new javax.swing.JMenuItem();
        miDestroyIFS = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miConfigIntfs = new javax.swing.JMenuItem();
        popIntf = new javax.swing.JPopupMenu();
        miCfgIntf = new javax.swing.JMenuItem();
        spMain = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        ckShowAllTypes = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        trTypes = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        ckShowAllSets = new javax.swing.JCheckBox();
        spSets = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        scpSets = new javax.swing.JScrollPane();
        tbSets = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        scpIntfs = new javax.swing.JScrollPane();
        tbIntfDefs = new javax.swing.JTable();

        miNewType.setText("新建分类");
        miNewType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewTypeActionPerformed(evt);
            }
        });
        popTypes.add(miNewType);

        miEditType.setText("修改分类");
        miEditType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditTypeActionPerformed(evt);
            }
        });
        popTypes.add(miEditType);

        miDisableType.setText("失效分类");
        miDisableType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisableTypeActionPerformed(evt);
            }
        });
        popTypes.add(miDisableType);

        miRevertType.setText("恢复分类");
        miRevertType.setToolTipText("");
        miRevertType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevertTypeActionPerformed(evt);
            }
        });
        popTypes.add(miRevertType);

        miDestroyType.setText("销毁分类");
        miDestroyType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyTypeActionPerformed(evt);
            }
        });
        popTypes.add(miDestroyType);
        popTypes.add(jSeparator1);

        miNewIntfSetFromTp.setText("新建接口集模板");
        miNewIntfSetFromTp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewIntfSetFromTpActionPerformed(evt);
            }
        });
        popTypes.add(miNewIntfSetFromTp);

        miNewIFS.setText("新建接口集");
        miNewIFS.setToolTipText("");
        miNewIFS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewIFSActionPerformed(evt);
            }
        });
        popIFS.add(miNewIFS);

        miEditIFS.setText("修改接口集");
        miEditIFS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditIFSActionPerformed(evt);
            }
        });
        popIFS.add(miEditIFS);

        miDisableIFS.setText("失效接口集");
        miDisableIFS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisableIFSActionPerformed(evt);
            }
        });
        popIFS.add(miDisableIFS);

        miRevertIFS.setText("恢复接口集");
        miRevertIFS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevertIFSActionPerformed(evt);
            }
        });
        popIFS.add(miRevertIFS);

        miDestroyIFS.setText("销毁接口集");
        miDestroyIFS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyIFSActionPerformed(evt);
            }
        });
        popIFS.add(miDestroyIFS);
        popIFS.add(jSeparator2);

        miConfigIntfs.setText("配置接口");
        miConfigIntfs.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miConfigIntfsActionPerformed(evt);
            }
        });
        popIFS.add(miConfigIntfs);

        miCfgIntf.setText("配置接口列表");
        miCfgIntf.setToolTipText("");
        miCfgIntf.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miCfgIntfActionPerformed(evt);
            }
        });
        popIntf.add(miCfgIntf);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("接口集合模板管理");
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

        spMain.setDividerLocation(288);
        spMain.setDividerSize(3);

        ckShowAllTypes.setText("显示全部");
        ckShowAllTypes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllTypesActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllTypes)
                .addContainerGap(208, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ckShowAllTypes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))
        );

        spMain.setLeftComponent(jPanel1);

        ckShowAllSets.setText("显示全部");
        ckShowAllSets.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllSetsActionPerformed(evt);
            }
        });

        spSets.setDividerLocation(500);
        spSets.setDividerSize(3);
        spSets.setResizeWeight(0.6);

        scpSets.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scpSetsMouseClicked(evt);
            }
        });

        tbSets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbSets.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbSets.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbSetsMouseClicked(evt);
            }
        });
        scpSets.setViewportView(tbSets);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpSets, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpSets, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );

        spSets.setLeftComponent(jPanel3);

        scpIntfs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scpIntfsMouseClicked(evt);
            }
        });

        tbIntfDefs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbIntfDefs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbIntfDefs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbIntfDefsMouseClicked(evt);
            }
        });
        scpIntfs.setViewportView(tbIntfDefs);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpIntfs, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpIntfs, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );

        spSets.setRightComponent(jPanel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllSets)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(spSets)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(ckShowAllSets)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spSets))
        );

        spMain.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spMain, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsIntfSetMgr(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void miNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewTypeActionPerformed
    {//GEN-HEADEREND:event_miNewTypeActionPerformed
        newType();
    }//GEN-LAST:event_miNewTypeActionPerformed

    private void miEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditTypeActionPerformed
    {//GEN-HEADEREND:event_miEditTypeActionPerformed
        editType();
    }//GEN-LAST:event_miEditTypeActionPerformed

    private void miDisableTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisableTypeActionPerformed
    {//GEN-HEADEREND:event_miDisableTypeActionPerformed
        disableType();
    }//GEN-LAST:event_miDisableTypeActionPerformed

    private void miRevertTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertTypeActionPerformed
    {//GEN-HEADEREND:event_miRevertTypeActionPerformed
        revertType();
    }//GEN-LAST:event_miRevertTypeActionPerformed

    private void miDestroyTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyTypeActionPerformed
    {//GEN-HEADEREND:event_miDestroyTypeActionPerformed
        destroyType();
    }//GEN-LAST:event_miDestroyTypeActionPerformed

    private void miNewIntfSetFromTpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewIntfSetFromTpActionPerformed
    {//GEN-HEADEREND:event_miNewIntfSetFromTpActionPerformed
        newIFS();
    }//GEN-LAST:event_miNewIntfSetFromTpActionPerformed

    private void trTypesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_trTypesMouseClicked
    {//GEN-HEADEREND:event_trTypesMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3)
        {
            popTypes.show(trTypes, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() >= 2)
        {
            editType();
        }
    }//GEN-LAST:event_trTypesMouseClicked

    private void ckShowAllTypesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllTypesActionPerformed
    {//GEN-HEADEREND:event_ckShowAllTypesActionPerformed
        makeTypeTree();
    }//GEN-LAST:event_ckShowAllTypesActionPerformed

    private void tbSetsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbSetsMouseClicked
    {//GEN-HEADEREND:event_tbSetsMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() >= 2)
        {
            editIFS();
        }
        else if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 1)
        {
            makeIntfTable();
        }
        else if (evt.getButton() == MouseEvent.BUTTON3)
        {
            popIFS.show(tbSets, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbSetsMouseClicked

    private void scpSetsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scpSetsMouseClicked
    {//GEN-HEADEREND:event_scpSetsMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() >= 2)
        {
            newIFS();
        }
        else if (evt.getButton() == MouseEvent.BUTTON3)
        {
            popIFS.show(scpSets, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_scpSetsMouseClicked

    private void trTypesValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trTypesValueChanged
    {//GEN-HEADEREND:event_trTypesValueChanged
        makeSetTable();
    }//GEN-LAST:event_trTypesValueChanged

    private void miEditIFSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditIFSActionPerformed
    {//GEN-HEADEREND:event_miEditIFSActionPerformed
        editIFS();
    }//GEN-LAST:event_miEditIFSActionPerformed

    private void miDisableIFSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisableIFSActionPerformed
    {//GEN-HEADEREND:event_miDisableIFSActionPerformed
        disableIFS();
    }//GEN-LAST:event_miDisableIFSActionPerformed

    private void miRevertIFSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertIFSActionPerformed
    {//GEN-HEADEREND:event_miRevertIFSActionPerformed
        revertIFS();
    }//GEN-LAST:event_miRevertIFSActionPerformed

    private void miDestroyIFSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyIFSActionPerformed
    {//GEN-HEADEREND:event_miDestroyIFSActionPerformed
        destroyIFS();
    }//GEN-LAST:event_miDestroyIFSActionPerformed

    private void miConfigIntfsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miConfigIntfsActionPerformed
    {//GEN-HEADEREND:event_miConfigIntfsActionPerformed
        configSetIntfs();
    }//GEN-LAST:event_miConfigIntfsActionPerformed

    private void ckShowAllSetsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllSetsActionPerformed
    {//GEN-HEADEREND:event_ckShowAllSetsActionPerformed
        makeSetTable();
    }//GEN-LAST:event_ckShowAllSetsActionPerformed

    private void tbIntfDefsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbIntfDefsMouseClicked
    {//GEN-HEADEREND:event_tbIntfDefsMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() >= 2)
        {
            configSetIntfs();
        }
        else if (evt.getButton() == MouseEvent.BUTTON3)
        {
            popIntf.show(tbIntfDefs, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbIntfDefsMouseClicked

    private void miCfgIntfActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miCfgIntfActionPerformed
    {//GEN-HEADEREND:event_miCfgIntfActionPerformed
        configSetIntfs();
    }//GEN-LAST:event_miCfgIntfActionPerformed

    private void scpIntfsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scpIntfsMouseClicked
    {//GEN-HEADEREND:event_scpIntfsMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3)
        {
            popIntf.show(scpIntfs, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_scpIntfsMouseClicked

    private void miNewIFSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewIFSActionPerformed
    {//GEN-HEADEREND:event_miNewIFSActionPerformed
        newIFS();
    }//GEN-LAST:event_miNewIFSActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ckShowAllSets;
    private javax.swing.JCheckBox ckShowAllTypes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuItem miCfgIntf;
    private javax.swing.JMenuItem miConfigIntfs;
    private javax.swing.JMenuItem miDestroyIFS;
    private javax.swing.JMenuItem miDestroyType;
    private javax.swing.JMenuItem miDisableIFS;
    private javax.swing.JMenuItem miDisableType;
    private javax.swing.JMenuItem miEditIFS;
    private javax.swing.JMenuItem miEditType;
    private javax.swing.JMenuItem miNewIFS;
    private javax.swing.JMenuItem miNewIntfSetFromTp;
    private javax.swing.JMenuItem miNewType;
    private javax.swing.JMenuItem miRevertIFS;
    private javax.swing.JMenuItem miRevertType;
    private javax.swing.JPopupMenu popIFS;
    private javax.swing.JPopupMenu popIntf;
    private javax.swing.JPopupMenu popTypes;
    private javax.swing.JScrollPane scpIntfs;
    private javax.swing.JScrollPane scpSets;
    private javax.swing.JSplitPane spMain;
    private javax.swing.JSplitPane spSets;
    private javax.swing.JTable tbIntfDefs;
    private javax.swing.JTable tbSets;
    private javax.swing.JTree trTypes;
    // End of variables declaration//GEN-END:variables
}
