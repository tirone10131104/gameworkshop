package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.DlgItemEquipStructNode;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemEquipStruct;
import dev.xlin.gameworkshop.progs.foundation.itemEquipStruct;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class IFrmItemEquipStruct extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;

    public IFrmItemEquipStruct(wakeup _up, JDesktopPane _desk)
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
    }

    private void makeTree()
    {
        itemEquipStruct ies = new itemEquipStruct(up);
        myTreeNode mrt = new myTreeNode("[装配逻辑结构]", 0, 0);
        dMakeTree(mrt, ies, 0);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeSt.setModel(dtm);
        guiCommon.expandTree(treeSt);
    }

    private void dMakeTree(myTreeNode mrt, itemEquipStruct ies, int parid)
    {
        List lcs = ies.getItemListByParent(parid, ckShowAll.isSelected());
        if (lcs != null)
        {
            for (int i = 0; i < lcs.size(); i++)
            {
                BeanItemEquipStruct bean = (BeanItemEquipStruct) lcs.get(i);
                String s= "[" + bean.getLevelID() +"]" + bean.getEquipName() + "<" + bean.getEquipTag() + ">";
                if (bean.getStatus()!= iDAO.OBJECT_STATE_ACTIVE)
                {
                    s = s +"  [失效]";
                }
                myTreeNode mtn = new myTreeNode(s, bean.getOID(), 1);
                dMakeTree(mtn, ies, bean.getOID());
                mrt.add(mtn);
            }
        }
    }

    private BeanItemEquipStruct getSelectedNode()
    {
        TreePath tph = treeSt.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() == 0)
        {
            return null;
        }
        itemEquipStruct ies = new itemEquipStruct(up);
        BeanItemEquipStruct bean = (BeanItemEquipStruct) ies.getRecordByID(mtn.getNodeOID());
        return bean;
    }

    private void doNewStNode()
    {
        BeanItemEquipStruct bpar = getSelectedNode();
        int parid = 0;
        if (bpar != null)
        {
            parid = bpar.getOID();
        }
        DlgItemEquipStructNode dlg = new DlgItemEquipStructNode(null, true, up, null, parid);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doEdit()
    {
        BeanItemEquipStruct bpar = getSelectedNode();
        if (bpar == null)
        {
            return;
        }
        DlgItemEquipStructNode dlg = new DlgItemEquipStructNode(null, true, up, bpar, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDis()
    {
        BeanItemEquipStruct bpar = getSelectedNode();
        if (bpar == null)
        {
            return;
        }
        int sel = fast.ask("是否确认将选中的节点设置为失效状态？");
        if (sel != fast.YES)
        {
            return;
        }
        itemEquipStruct ies = new itemEquipStruct(up);
        int r = ies.deleteRecord(bpar.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doRev()
    {
        BeanItemEquipStruct bpar = getSelectedNode();
        if (bpar == null)
        {
            return;
        }
        int sel = fast.ask("是否确认将选中的节点恢复为有效状态？");
        if (sel != fast.YES)
        {
            return;
        }
        itemEquipStruct ies = new itemEquipStruct(up);
        int r = ies.revertBean(bpar.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void doDes()
    {
        BeanItemEquipStruct bpar = getSelectedNode();
        if (bpar == null)
        {
            return;
        }
        int sel = fast.ask("是否确认将选中的节点销毁？\n这个操作不可被恢复。");
        if (sel != fast.YES)
        {
            return;
        }
        itemEquipStruct ies = new itemEquipStruct(up);
        int r = ies.destroyBean(bpar.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void doMoveUp()
    {
        BeanItemEquipStruct bpar = getSelectedNode();
        if (bpar == null)
        {
            return;
        }
        itemEquipStruct ies = new itemEquipStruct(up);
        int r = ies.moveUp(bpar.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
    }

    private void doMoveDown()
    {
        BeanItemEquipStruct bpar = getSelectedNode();
        if (bpar == null)
        {
            return;
        }
        itemEquipStruct ies = new itemEquipStruct(up);
        int r = ies.moveDown(bpar.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popSt = new javax.swing.JPopupMenu();
        miNewSt = new javax.swing.JMenuItem();
        miEditSt = new javax.swing.JMenuItem();
        miDisSt = new javax.swing.JMenuItem();
        miRevSt = new javax.swing.JMenuItem();
        miDesSt = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miMoveUp = new javax.swing.JMenuItem();
        miMoveDown = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeSt = new javax.swing.JTree();
        ckShowAll = new javax.swing.JCheckBox();

        miNewSt.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewSt.setText("新建节点");
        miNewSt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewStActionPerformed(evt);
            }
        });
        popSt.add(miNewSt);

        miEditSt.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditSt.setText("修改节点");
        miEditSt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditStActionPerformed(evt);
            }
        });
        popSt.add(miEditSt);

        miDisSt.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisSt.setText("失效节点");
        miDisSt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisStActionPerformed(evt);
            }
        });
        popSt.add(miDisSt);

        miRevSt.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevSt.setText("恢复节点");
        miRevSt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevStActionPerformed(evt);
            }
        });
        popSt.add(miRevSt);

        miDesSt.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesSt.setText("销毁节点");
        miDesSt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesStActionPerformed(evt);
            }
        });
        popSt.add(miDesSt);
        popSt.add(jSeparator1);

        miMoveUp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveUp.setText("上移");
        miMoveUp.setToolTipText("");
        miMoveUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveUpActionPerformed(evt);
            }
        });
        popSt.add(miMoveUp);

        miMoveDown.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveDown.setText("下移");
        miMoveDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveDownActionPerformed(evt);
            }
        });
        popSt.add(miMoveDown);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("物品装备体系管理");
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

        treeSt.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                treeStMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(treeSt);

        ckShowAll.setText("显示全部");
        ckShowAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAll)
                .addContainerGap(504, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(ckShowAll)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void treeStMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeStMouseClicked
    {//GEN-HEADEREND:event_treeStMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popSt.show(treeSt, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_treeStMouseClicked

    private void miNewStActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewStActionPerformed
    {//GEN-HEADEREND:event_miNewStActionPerformed
        doNewStNode();
    }//GEN-LAST:event_miNewStActionPerformed

    private void miEditStActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditStActionPerformed
    {//GEN-HEADEREND:event_miEditStActionPerformed
        doEdit();
    }//GEN-LAST:event_miEditStActionPerformed

    private void miDisStActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisStActionPerformed
    {//GEN-HEADEREND:event_miDisStActionPerformed
        doDis();
    }//GEN-LAST:event_miDisStActionPerformed

    private void miRevStActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevStActionPerformed
    {//GEN-HEADEREND:event_miRevStActionPerformed
        doRev();
    }//GEN-LAST:event_miRevStActionPerformed

    private void miDesStActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesStActionPerformed
    {//GEN-HEADEREND:event_miDesStActionPerformed
        doDes();
    }//GEN-LAST:event_miDesStActionPerformed

    private void miMoveUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveUpActionPerformed
    {//GEN-HEADEREND:event_miMoveUpActionPerformed
        doMoveUp();
    }//GEN-LAST:event_miMoveUpActionPerformed

    private void miMoveDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveDownActionPerformed
    {//GEN-HEADEREND:event_miMoveDownActionPerformed
        doMoveDown();
    }//GEN-LAST:event_miMoveDownActionPerformed

    private void ckShowAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllActionPerformed
    {//GEN-HEADEREND:event_ckShowAllActionPerformed
        makeTree();
    }//GEN-LAST:event_ckShowAllActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsItemEquipStruct(false);
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ckShowAll;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuItem miDesSt;
    private javax.swing.JMenuItem miDisSt;
    private javax.swing.JMenuItem miEditSt;
    private javax.swing.JMenuItem miMoveDown;
    private javax.swing.JMenuItem miMoveUp;
    private javax.swing.JMenuItem miNewSt;
    private javax.swing.JMenuItem miRevSt;
    private javax.swing.JPopupMenu popSt;
    private javax.swing.JTree treeSt;
    // End of variables declaration//GEN-END:variables
}
