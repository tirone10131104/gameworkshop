package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.dlgMCEConfig;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.progs.runtime.microEngine.BeanMCEngineConfigMain;
import dev.xlin.gameworkshop.progs.runtime.microEngine.MCEConfigDAO;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.JDAO;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author 22972
 */
public class ifrmMCESMgr extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private List lsMces = null;

    private final int NODE_ROOT = 0;
    private final int NODE_MCE = 1;

    public ifrmMCESMgr(wakeup _up, JDesktopPane _desk)
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
        queryMCE();
        makeMCETree();
    }

    private void queryMCE()
    {
        MCEConfigDAO mcd = new MCEConfigDAO(up);
        lsMces = mcd.getMCEList(ckShowAll.isSelected());
    }

    private void makeMCETree()
    {
        myTreeNode mrt = new myTreeNode("[微引擎配置数据库]", 0, NODE_ROOT);
        if (lsMces != null)
        {
            for (int i = 0; i < lsMces.size(); i++)
            {
                BeanMCEngineConfigMain bean = (BeanMCEngineConfigMain) lsMces.get(i);
                String s = bean.getMceName();
                if (ckShowTag.isSelected())
                {
                    s = s + "<" + bean.getMceTag() + ">";
                }
                if (bean.getStatus() == JDAO.OBJECT_STATUS_DISABLE)
                {
                    s = s + " [失效]";
                }
                myTreeNode mce = new myTreeNode(s, bean.getOID(), NODE_MCE);
                mrt.add(mce);
            }
        }
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeMCS.setModel(dtm);
    }

    private void newMCE()
    {
        dlgMCEConfig dlg = new dlgMCEConfig(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            queryMCE();
            makeMCETree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void updateMCE()
    {
        BeanMCEngineConfigMain bean = getSelectedMCE();
        if (bean == null)
        {
            return;
        }
        if (bean.getStatus() != JDAO.OBJECT_STATUS_ACTIVE)
        {
            return;
        }
        dlgMCEConfig dlg = new dlgMCEConfig(null, true, up, bean);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            queryMCE();
            makeMCETree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void disableMCE()
    {
        BeanMCEngineConfigMain bean = getSelectedMCE();
        if (bean == null)
        {
            return;
        }
        if (bean.getStatus() != JDAO.OBJECT_STATUS_ACTIVE)
        {
            fast.warn("没有必要进行失效操作");
            return;
        }
        int sel = fast.ask("是否将选中的目标MCE进行失效处理?");
        if (sel != fast.YES)
        {
            return;
        }
        MCEConfigDAO mcd = new MCEConfigDAO(up);
        int r = mcd.disable(bean.getOID());
        if (r == JDAO.OPERATE_SUCCESS)
        {
            queryMCE();
            makeMCETree();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void enableMCE()
    {
        BeanMCEngineConfigMain bean = getSelectedMCE();
        if (bean == null)
        {
            return;
        }
        if (bean.getStatus() != JDAO.OBJECT_STATUS_DISABLE)
        {
            fast.warn("没有必要进行恢复操作");
            return;
        }
        int sel = fast.ask("是否将选中的目标MCE进行恢复处理?");
        if (sel != fast.YES)
        {
            return;
        }
        MCEConfigDAO mcd = new MCEConfigDAO(up);
        int r = mcd.enable(bean.getOID());
        if (r == JDAO.OPERATE_SUCCESS)
        {
            queryMCE();
            makeMCETree();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void deleteMCE()
    {
        BeanMCEngineConfigMain bean = getSelectedMCE();
        if (bean == null)
        {
            return;
        }
        if (bean.getStatus() != JDAO.OBJECT_STATUS_DISABLE)
        {
            fast.warn("不能删除有效的MCE配置");
            return;
        }
        int sel = fast.ask("是否将选中的目标MCE删除?");
        if (sel != fast.YES)
        {
            return;
        }
        MCEConfigDAO mcd = new MCEConfigDAO(up);
        int r = mcd.delete(bean.getOID());
        if (r == JDAO.OPERATE_SUCCESS)
        {
            queryMCE();
            makeMCETree();
        }
        else
        {
            fast.err("删除操作失败", r);
        }
    }

    //获取当前选择的树节点上的MCE
    private BeanMCEngineConfigMain getSelectedMCE()
    {
        TreePath tph = treeMCS.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        int id = mtn.getNodeOID();
        if (id == 0)
        {
            return null;
        }
        for (int i = 0; i < lsMces.size(); i++)
        {
            BeanMCEngineConfigMain bean = (BeanMCEngineConfigMain) lsMces.get(i);
            if (bean.getOID() == id)
            {
                return bean;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popTree = new javax.swing.JPopupMenu();
        miNewMCE = new javax.swing.JMenuItem();
        miUpdateMCE = new javax.swing.JMenuItem();
        miDisableMCE = new javax.swing.JMenuItem();
        miEnableMCE = new javax.swing.JMenuItem();
        miDeleteMCE = new javax.swing.JMenuItem();
        tbMCES = new javax.swing.JToolBar();
        btnNewMCE = new javax.swing.JButton();
        btnUpdateMCE = new javax.swing.JButton();
        btnDisableMCE = new javax.swing.JButton();
        btnEnableMCE = new javax.swing.JButton();
        btnDeleteMCE = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        ckShowAll = new javax.swing.JCheckBox();
        ckShowTag = new javax.swing.JCheckBox();
        spMain2 = new javax.swing.JSplitPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        treeMCS = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();

        miNewMCE.setText("新建MCE");
        miNewMCE.setToolTipText("");
        miNewMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewMCEActionPerformed(evt);
            }
        });
        popTree.add(miNewMCE);

        miUpdateMCE.setText("修改");
        miUpdateMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miUpdateMCEActionPerformed(evt);
            }
        });
        popTree.add(miUpdateMCE);

        miDisableMCE.setText("失效");
        miDisableMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisableMCEActionPerformed(evt);
            }
        });
        popTree.add(miDisableMCE);

        miEnableMCE.setText("恢复");
        miEnableMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEnableMCEActionPerformed(evt);
            }
        });
        popTree.add(miEnableMCE);

        miDeleteMCE.setText("删除");
        miDeleteMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDeleteMCEActionPerformed(evt);
            }
        });
        popTree.add(miDeleteMCE);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("MCE管理器");
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

        tbMCES.setFloatable(false);
        tbMCES.setRollover(true);

        btnNewMCE.setText("新建MCE");
        btnNewMCE.setFocusable(false);
        btnNewMCE.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewMCE.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewMCEActionPerformed(evt);
            }
        });
        tbMCES.add(btnNewMCE);

        btnUpdateMCE.setText("修改配置");
        btnUpdateMCE.setFocusable(false);
        btnUpdateMCE.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUpdateMCE.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUpdateMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnUpdateMCEActionPerformed(evt);
            }
        });
        tbMCES.add(btnUpdateMCE);

        btnDisableMCE.setText("失效配置");
        btnDisableMCE.setFocusable(false);
        btnDisableMCE.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDisableMCE.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDisableMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDisableMCEActionPerformed(evt);
            }
        });
        tbMCES.add(btnDisableMCE);

        btnEnableMCE.setText("恢复配置");
        btnEnableMCE.setFocusable(false);
        btnEnableMCE.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEnableMCE.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEnableMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEnableMCEActionPerformed(evt);
            }
        });
        tbMCES.add(btnEnableMCE);

        btnDeleteMCE.setText("删除配置");
        btnDeleteMCE.setFocusable(false);
        btnDeleteMCE.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDeleteMCE.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDeleteMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeleteMCEActionPerformed(evt);
            }
        });
        tbMCES.add(btnDeleteMCE);
        tbMCES.add(jSeparator1);

        ckShowAll.setText("显示全部");
        ckShowAll.setFocusable(false);
        ckShowAll.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ckShowAll.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ckShowAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ckShowAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllActionPerformed(evt);
            }
        });
        tbMCES.add(ckShowAll);

        ckShowTag.setText("显示标签");
        ckShowTag.setFocusable(false);
        ckShowTag.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ckShowTag.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ckShowTag.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowTagActionPerformed(evt);
            }
        });
        tbMCES.add(ckShowTag);

        spMain2.setDividerLocation(288);
        spMain2.setDividerSize(3);

        treeMCS.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                treeMCSMouseClicked(evt);
            }
        });
        treeMCS.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeMCSValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(treeMCS);

        spMain2.setLeftComponent(jScrollPane4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 955, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 664, Short.MAX_VALUE)
        );

        spMain2.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbMCES, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(spMain2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tbMCES, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(spMain2, javax.swing.GroupLayout.PREFERRED_SIZE, 666, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDisableMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDisableMCEActionPerformed
    {//GEN-HEADEREND:event_btnDisableMCEActionPerformed
        disableMCE();
    }//GEN-LAST:event_btnDisableMCEActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsMCEManager(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void ckShowAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllActionPerformed
    {//GEN-HEADEREND:event_ckShowAllActionPerformed
        queryMCE();
        makeMCETree();
    }//GEN-LAST:event_ckShowAllActionPerformed

    private void ckShowTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowTagActionPerformed
    {//GEN-HEADEREND:event_ckShowTagActionPerformed
        makeMCETree();
    }//GEN-LAST:event_ckShowTagActionPerformed

    private void btnNewMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewMCEActionPerformed
    {//GEN-HEADEREND:event_btnNewMCEActionPerformed
        newMCE();
    }//GEN-LAST:event_btnNewMCEActionPerformed

    private void btnUpdateMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnUpdateMCEActionPerformed
    {//GEN-HEADEREND:event_btnUpdateMCEActionPerformed
        updateMCE();
    }//GEN-LAST:event_btnUpdateMCEActionPerformed

    private void btnEnableMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEnableMCEActionPerformed
    {//GEN-HEADEREND:event_btnEnableMCEActionPerformed
        enableMCE();
    }//GEN-LAST:event_btnEnableMCEActionPerformed

    private void btnDeleteMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteMCEActionPerformed
    {//GEN-HEADEREND:event_btnDeleteMCEActionPerformed
        deleteMCE();
    }//GEN-LAST:event_btnDeleteMCEActionPerformed

    private void treeMCSMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeMCSMouseClicked
    {//GEN-HEADEREND:event_treeMCSMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3)
        {
            popTree.show(treeMCS, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_treeMCSMouseClicked

    private void treeMCSValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeMCSValueChanged
    {//GEN-HEADEREND:event_treeMCSValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_treeMCSValueChanged

    private void miNewMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewMCEActionPerformed
    {//GEN-HEADEREND:event_miNewMCEActionPerformed
        newMCE();
    }//GEN-LAST:event_miNewMCEActionPerformed

    private void miDisableMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisableMCEActionPerformed
    {//GEN-HEADEREND:event_miDisableMCEActionPerformed
        disableMCE();
    }//GEN-LAST:event_miDisableMCEActionPerformed

    private void miEnableMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEnableMCEActionPerformed
    {//GEN-HEADEREND:event_miEnableMCEActionPerformed
        enableMCE();
    }//GEN-LAST:event_miEnableMCEActionPerformed

    private void miDeleteMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDeleteMCEActionPerformed
    {//GEN-HEADEREND:event_miDeleteMCEActionPerformed
        deleteMCE();
    }//GEN-LAST:event_miDeleteMCEActionPerformed

    private void miUpdateMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miUpdateMCEActionPerformed
    {//GEN-HEADEREND:event_miUpdateMCEActionPerformed
        updateMCE();
    }//GEN-LAST:event_miUpdateMCEActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteMCE;
    private javax.swing.JButton btnDisableMCE;
    private javax.swing.JButton btnEnableMCE;
    private javax.swing.JButton btnNewMCE;
    private javax.swing.JButton btnUpdateMCE;
    private javax.swing.JCheckBox ckShowAll;
    private javax.swing.JCheckBox ckShowTag;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JMenuItem miDeleteMCE;
    private javax.swing.JMenuItem miDisableMCE;
    private javax.swing.JMenuItem miEnableMCE;
    private javax.swing.JMenuItem miNewMCE;
    private javax.swing.JMenuItem miUpdateMCE;
    private javax.swing.JPopupMenu popTree;
    private javax.swing.JSplitPane spMain2;
    private javax.swing.JToolBar tbMCES;
    private javax.swing.JTree treeMCS;
    // End of variables declaration//GEN-END:variables
}
