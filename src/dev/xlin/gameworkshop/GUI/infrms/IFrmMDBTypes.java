package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.DlgMDBTypeDefine;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiFullTreeGuiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.JDAO;
import dev.xlin.tols.interfaces.iDAO;
import dev.xling.jmdbs.BeanMdbTypeDefine;
import dev.xling.jmdbs.MDBDataTypeDefine;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * MDB类型数据设置管理
 *
 * @author 刘祎鹏
 */
public class IFrmMDBTypes extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private boolean bInitTable = false;
    private JDesktopPane desk = null;

    private List ltps = null;

    public IFrmMDBTypes(wakeup _up, JDesktopPane _desk)
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
        makeTree();
        makeMTPTable();
    }

    private void makeTree()
    {
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_MDB_TYPE_DEFINE, ckShowAllTypes.isSelected());
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeMTPS.setModel(dtm);
        guiCommon.expandTree(treeMTPS);
    }

    private int getSelectedType()
    {
        TreePath tph = treeMTPS.getSelectionPath();
        if (tph == null)
        {
            return 0;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        return mtn.getNodeOID();
    }

    private void typeSelected()
    {
        makeMTPTable();
    }

    private BeanMdbTypeDefine getSelectedMTP()
    {
        int sid = tbMDBTP.getSelectedRow();
        if (sid < 0)
        {
            return null;
        }
        else
        {
            return (BeanMdbTypeDefine) ltps.get(sid);
        }
    }

    private void makeMTPTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("名称");
            mtm.addColumn("标签");
            mtm.addColumn("实体化");
            mtm.addColumn("垃圾回收");
            mtm.addColumn("状态");
            tbMDBTP.setModel(mtm);
            bInitTable = true;
        }
        else
        {
            fast.clearTableModel(tbMDBTP);
            mtm = (myTableModel) tbMDBTP.getModel();
        }
        MDBDataTypeDefine mtd = new MDBDataTypeDefine(up);
        int tpid = getSelectedType();
        ltps = mtd.getDefineByType(tpid, ckShowAll.isSelected());
        if (ltps != null)
        {
            for (int i = 0; i < ltps.size(); i++)
            {
                BeanMdbTypeDefine bean = (BeanMdbTypeDefine) ltps.get(i);
                Object[] or = new Object[5];
                or[0] = bean.getTypeName();
                or[1] = bean.getTypeTag();
                or[2] = iConst.translateIBOL(bean.getAllowEntity());
                or[3] = iConst.translateIBOL(bean.getAllowGC());
                or[4] = iConst.transDAOState(bean.getStatus());
                mtm.addRow(or);
            }
        }
        tbMDBTP.setModel(mtm);
    }

    private void newType()
    {
        int r = guiFullTreeGuiCodes.doNewType(treeMTPS, up, systemType.CODE_STT_MDB_TYPE_DEFINE);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("新建分类失败");
        }
    }

    private void editType()
    {
        int r = guiFullTreeGuiCodes.doEditType(treeMTPS, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("修改分类操作失败", r);
        }
    }

    private void disableType()
    {
        int r = guiFullTreeGuiCodes.doDisType(treeMTPS, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("失效分类操作失败", r);
        }
    }

    private void revertType()
    {
        int r = guiFullTreeGuiCodes.doRevType(treeMTPS, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("失效分类操作失败", r);
        }
    }

    private void deleteType()
    {
        int r = guiFullTreeGuiCodes.doDesType(treeMTPS, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("删除操作失败", r);
        }
    }

    private void newMTP()
    {
        int tpid = getSelectedType();
        if (tpid == 0)
        {
            return;
        }
        DlgMDBTypeDefine dlg = new DlgMDBTypeDefine(null, true, up, null, tpid);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeMTPTable();
            fast.msg("新建数据类型操作成功");
        }
        dlg.dispose();
        dlg = null;
    }

    private void editMTP()
    {
        BeanMdbTypeDefine bean = getSelectedMTP();
        if (bean == null)
        {
            return;
        }
        DlgMDBTypeDefine dlg = new DlgMDBTypeDefine(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeMTPTable();
            fast.msg("新建数据类型操作成功");
        }
        dlg.dispose();
        dlg = null;
    }

    private void disableMTP()
    {
        BeanMdbTypeDefine bean = getSelectedMTP();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否失效数据类型?");
        if (sel != fast.YES)
        {
            return;
        }
        MDBDataTypeDefine mtd = new MDBDataTypeDefine(up);
        int r = mtd.disable(bean.getOID());
        if (r == JDAO.OPERATE_SUCCESS)
        {
            makeMTPTable();
        }
        else
        {
            fast.err("失效数据类型操作失败", r);
        }
    }

    private void enableMTP()
    {
        BeanMdbTypeDefine bean = getSelectedMTP();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否恢复数据类型?");
        if (sel != fast.YES)
        {
            return;
        }
        MDBDataTypeDefine mtd = new MDBDataTypeDefine(up);
        int r = mtd.enable(bean.getOID());
        if (r == JDAO.OPERATE_SUCCESS)
        {
            makeMTPTable();
        }
        else
        {
            fast.err("恢复数据类型操作失败", r);
        }
    }

    private void deleteMTP()
    {
        BeanMdbTypeDefine bean = getSelectedMTP();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否删除数据类型?");
        if (sel != fast.YES)
        {
            return;
        }
        MDBDataTypeDefine mtd = new MDBDataTypeDefine(up);
        int r = mtd.delete(bean.getOID());
        if (r == JDAO.OPERATE_SUCCESS)
        {
            makeMTPTable();
        }
        else
        {
            fast.err("删除数据类型操作失败", r);
        }
    }

    private void moveMTP()
    {
        BeanMdbTypeDefine bean = getSelectedMTP();
        if (bean == null)
        {
            return;
        }
        guiCodes.makeFullTypeTree(up, systemType.CODE_STT_MDB_TYPE_DEFINE, false, bean.getTypeOID());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popTps = new javax.swing.JPopupMenu();
        miNewType = new javax.swing.JMenuItem();
        miEditType = new javax.swing.JMenuItem();
        miDisType = new javax.swing.JMenuItem();
        miRevType = new javax.swing.JMenuItem();
        miDeleteType = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miNewMTP = new javax.swing.JMenuItem();
        popMTP = new javax.swing.JPopupMenu();
        miEditMTP = new javax.swing.JMenuItem();
        miDisableMTP = new javax.swing.JMenuItem();
        miEnableMTP = new javax.swing.JMenuItem();
        miDeleteMTP = new javax.swing.JMenuItem();
        miMoveMTP = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        ckShowAll = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbMDBTP = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeMTPS = new javax.swing.JTree();
        ckShowAllTypes = new javax.swing.JCheckBox();

        miNewType.setText("新建分类");
        miNewType.setToolTipText("");
        miNewType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewTypeActionPerformed(evt);
            }
        });
        popTps.add(miNewType);

        miEditType.setText("修改分类");
        miEditType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditTypeActionPerformed(evt);
            }
        });
        popTps.add(miEditType);

        miDisType.setText("失效分类");
        miDisType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisTypeActionPerformed(evt);
            }
        });
        popTps.add(miDisType);

        miRevType.setText("恢复分类");
        miRevType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevTypeActionPerformed(evt);
            }
        });
        popTps.add(miRevType);

        miDeleteType.setText("删除分类");
        miDeleteType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDeleteTypeActionPerformed(evt);
            }
        });
        popTps.add(miDeleteType);
        popTps.add(jSeparator1);

        miNewMTP.setText("新建数据类型");
        miNewMTP.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewMTPActionPerformed(evt);
            }
        });
        popTps.add(miNewMTP);

        miEditMTP.setText("修改数据类型");
        miEditMTP.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditMTPActionPerformed(evt);
            }
        });
        popMTP.add(miEditMTP);

        miDisableMTP.setText("失效");
        miDisableMTP.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisableMTPActionPerformed(evt);
            }
        });
        popMTP.add(miDisableMTP);

        miEnableMTP.setText("恢复");
        miEnableMTP.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEnableMTPActionPerformed(evt);
            }
        });
        popMTP.add(miEnableMTP);

        miDeleteMTP.setText("删除数据类型");
        miDeleteMTP.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDeleteMTPActionPerformed(evt);
            }
        });
        popMTP.add(miDeleteMTP);

        miMoveMTP.setText("移动数据类型");
        miMoveMTP.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveMTPActionPerformed(evt);
            }
        });
        popMTP.add(miMoveMTP);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("内存数据库类型设置");
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

        jSplitPane1.setDividerLocation(299);
        jSplitPane1.setDividerSize(3);

        ckShowAll.setText("显示全部");
        ckShowAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllActionPerformed(evt);
            }
        });

        tbMDBTP.setModel(new javax.swing.table.DefaultTableModel(
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
        tbMDBTP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbMDBTP.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbMDBTPMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbMDBTP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAll)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ckShowAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel1);

        treeMTPS.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                treeMTPSMouseClicked(evt);
            }
        });
        treeMTPS.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeMTPSValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treeMTPS);

        ckShowAllTypes.setText("显示全部");
        ckShowAllTypes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllTypesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllTypes)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(ckShowAllTypes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel2);

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

    private void treeMTPSMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeMTPSMouseClicked
    {//GEN-HEADEREND:event_treeMTPSMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popTps.show(treeMTPS, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_treeMTPSMouseClicked

    private void treeMTPSValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeMTPSValueChanged
    {//GEN-HEADEREND:event_treeMTPSValueChanged
        typeSelected();
    }//GEN-LAST:event_treeMTPSValueChanged

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
        deleteType();
    }//GEN-LAST:event_miDeleteTypeActionPerformed

    private void miNewMTPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewMTPActionPerformed
    {//GEN-HEADEREND:event_miNewMTPActionPerformed
        newMTP();
    }//GEN-LAST:event_miNewMTPActionPerformed

    private void miEditMTPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditMTPActionPerformed
    {//GEN-HEADEREND:event_miEditMTPActionPerformed
        editMTP();
    }//GEN-LAST:event_miEditMTPActionPerformed

    private void miDisableMTPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisableMTPActionPerformed
    {//GEN-HEADEREND:event_miDisableMTPActionPerformed
        disableMTP();
    }//GEN-LAST:event_miDisableMTPActionPerformed

    private void miEnableMTPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEnableMTPActionPerformed
    {//GEN-HEADEREND:event_miEnableMTPActionPerformed
        enableMTP();
    }//GEN-LAST:event_miEnableMTPActionPerformed

    private void miDeleteMTPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDeleteMTPActionPerformed
    {//GEN-HEADEREND:event_miDeleteMTPActionPerformed
        deleteMTP();
    }//GEN-LAST:event_miDeleteMTPActionPerformed

    private void miMoveMTPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveMTPActionPerformed
    {//GEN-HEADEREND:event_miMoveMTPActionPerformed
        moveMTP();
    }//GEN-LAST:event_miMoveMTPActionPerformed

    private void miDisTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisTypeActionPerformed
    {//GEN-HEADEREND:event_miDisTypeActionPerformed
        disableType();
    }//GEN-LAST:event_miDisTypeActionPerformed

    private void miRevTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevTypeActionPerformed
    {//GEN-HEADEREND:event_miRevTypeActionPerformed
        revertType();
    }//GEN-LAST:event_miRevTypeActionPerformed

    private void ckShowAllTypesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllTypesActionPerformed
    {//GEN-HEADEREND:event_ckShowAllTypesActionPerformed
        makeTree();
    }//GEN-LAST:event_ckShowAllTypesActionPerformed

    private void ckShowAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllActionPerformed
    {//GEN-HEADEREND:event_ckShowAllActionPerformed
        makeMTPTable();
    }//GEN-LAST:event_ckShowAllActionPerformed

    private void tbMDBTPMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbMDBTPMouseClicked
    {//GEN-HEADEREND:event_tbMDBTPMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popMTP.show(tbMDBTP, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbMDBTPMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsMDBTPMgr(false);
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ckShowAll;
    private javax.swing.JCheckBox ckShowAllTypes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem miDeleteMTP;
    private javax.swing.JMenuItem miDeleteType;
    private javax.swing.JMenuItem miDisType;
    private javax.swing.JMenuItem miDisableMTP;
    private javax.swing.JMenuItem miEditMTP;
    private javax.swing.JMenuItem miEditType;
    private javax.swing.JMenuItem miEnableMTP;
    private javax.swing.JMenuItem miMoveMTP;
    private javax.swing.JMenuItem miNewMTP;
    private javax.swing.JMenuItem miNewType;
    private javax.swing.JMenuItem miRevType;
    private javax.swing.JPopupMenu popMTP;
    private javax.swing.JPopupMenu popTps;
    private javax.swing.JTable tbMDBTP;
    private javax.swing.JTree treeMTPS;
    // End of variables declaration//GEN-END:variables
}
