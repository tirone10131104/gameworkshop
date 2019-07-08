package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.MDIPaneControl;
import dev.xlin.gameworkshop.GUI.dialog.DlgDBTempletInfo;
import dev.xlin.gameworkshop.GUI.dialog.DlgItemTempletInfo;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablockTemplet;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemTemplet;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.datablockTemplet;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.itemTempletService;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import java.util.Vector;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class IFrmItemTempletMgr extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private List ltmps = null;
    private boolean bInitTable = false;

    public IFrmItemTempletMgr(wakeup _up, JDesktopPane _desk)
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
        makeTable();
    }

    private void makeTree()
    {
        myTreeNode mrt = guiCodes.makeObjectClassTree(up, false);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeOcls.setModel(dtm);
        guiCommon.expandTree(treeOcls);
    }

    private BeanObjectClass getSelectedOCLS()
    {
        TreePath tph = treeOcls.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() != 2)
        {
            return null;
        }
        objectClassDefine ocd = new objectClassDefine(up);
        BeanObjectClass BOC = (BeanObjectClass) ocd.getRecordByID(mtn.getNodeOID());
        return BOC;
    }

    private void makeTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("名称");
            mtm.addColumn("描述");
            tbTemps.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbTemps);
            mtm = (myTableModel) tbTemps.getModel();
        }
        BeanObjectClass boc = getSelectedOCLS();
        int ocid = 0;
        if (boc != null)
        {
            ocid = boc.getOID();
        }
        itemTempletService its = new itemTempletService(up);
        ltmps = its.getItemTemplets(ocid, "");
        itemDefine idef = new itemDefine(up);
        if (ltmps != null)
        {
            for (int i = 0; i < ltmps.size(); i++)
            {
                BeanItemTemplet bit = (BeanItemTemplet) ltmps.get(i);
                Vector v = new Vector();
                v.add(bit.getTempName());
                v.add(bit.getTempDesp());
                if (bit.getSrcItem() != 0)
                {
                    BeanItem bitem = (BeanItem) idef.getRecordByID(bit.getSrcItem());
                    if (bitem!= null )
                    {
                        v.add(bitem.getItemName());
                    }
                    else
                    {
                        v.add("[来源已被删除]");
                    }
                }
                else
                {
                    v.add("-");
                }
                mtm.addRow(v);
            }
        }
    }

    private BeanItemTemplet getSelectedTemplet()
    {
        int idx = tbTemps.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanItemTemplet) ltmps.get(idx);
    }

    private void doEditInfo()
    {
        BeanItemTemplet bean = getSelectedTemplet();
        if (bean == null)
        {
            return;
        }
        DlgItemTempletInfo dlg = new DlgItemTempletInfo(null, true, up, bean);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanItemTemplet nbt = dlg.getItemTemplet();
            itemTempletService its = new itemTempletService(up);
            int r = its.updateItemTempInfo(nbt);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTable();
            }
            else
            {
                fast.err("修改信息操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void doRemoveTemp()
    {
        BeanItemTemplet bean = getSelectedTemplet();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认要删除选中的模板数据？\n这个操作不可被恢复。\n已使用该模板的数据不会受到影响");
        if (sel != fast.YES)
        {
            return;
        }
        itemTempletService its = new itemTempletService(up);
        int r = its.removeItemTemplet(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("删除模板操作失败", r);
        }
    }

    private void doOpenEditor()
    {
        BeanItemTemplet bean = getSelectedTemplet();
        if (bean == null)
        {
            return;
        }
        if (MDIPaneControl.isMDIFrameOpened(MDIPaneControl.IFRM_ITEM_TEMPLET_DATA, bean.getOID()+""))
        {
            fast.warn("已经打开了这个数据模板编辑器");
            return ; 
        }
        IFrmItemTempletEditor ifrm = new IFrmItemTempletEditor(up, desk, bean);
        desk.add(ifrm);
        ifrm.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popTemp = new javax.swing.JPopupMenu();
        miEditInfo = new javax.swing.JMenuItem();
        miRemove = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miEditor = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTemps = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        treeOcls = new javax.swing.JTree();

        miEditInfo.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditInfo.setText("修改信息");
        miEditInfo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditInfoActionPerformed(evt);
            }
        });
        popTemp.add(miEditInfo);

        miRemove.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRemove.setText("删除模板");
        miRemove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRemoveActionPerformed(evt);
            }
        });
        popTemp.add(miRemove);
        popTemp.add(jSeparator1);

        miEditor.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditor.setForeground(new java.awt.Color(255, 0, 0));
        miEditor.setText("物体模板数据编辑器");
        miEditor.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditorActionPerformed(evt);
            }
        });
        popTemp.add(miEditor);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("物体模板数据管理器");
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

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(3);

        tbTemps.setModel(new javax.swing.table.DefaultTableModel(
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
        tbTemps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbTemps.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbTempsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbTemps);

        jSplitPane1.setRightComponent(jScrollPane1);

        treeOcls.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                treeOclsMouseClicked(evt);
            }
        });
        treeOcls.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeOclsValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(treeOcls);

        jSplitPane1.setLeftComponent(jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void miEditInfoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditInfoActionPerformed
    {//GEN-HEADEREND:event_miEditInfoActionPerformed
        doEditInfo();
    }//GEN-LAST:event_miEditInfoActionPerformed

    private void miRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveActionPerformed
    {//GEN-HEADEREND:event_miRemoveActionPerformed
        doRemoveTemp();
    }//GEN-LAST:event_miRemoveActionPerformed

    private void treeOclsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeOclsMouseClicked
    {//GEN-HEADEREND:event_treeOclsMouseClicked

    }//GEN-LAST:event_treeOclsMouseClicked

    private void treeOclsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeOclsValueChanged
    {//GEN-HEADEREND:event_treeOclsValueChanged
        makeTable();
    }//GEN-LAST:event_treeOclsValueChanged

    private void tbTempsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbTempsMouseClicked
    {//GEN-HEADEREND:event_tbTempsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popTemp.show(tbTemps, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbTempsMouseClicked

    private void miEditorActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditorActionPerformed
    {//GEN-HEADEREND:event_miEditorActionPerformed
        doOpenEditor();
    }//GEN-LAST:event_miEditorActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsItemTempMgr(false);
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem miEditInfo;
    private javax.swing.JMenuItem miEditor;
    private javax.swing.JMenuItem miRemove;
    private javax.swing.JPopupMenu popTemp;
    private javax.swing.JTable tbTemps;
    private javax.swing.JTree treeOcls;
    // End of variables declaration//GEN-END:variables
}
