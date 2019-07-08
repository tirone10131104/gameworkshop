package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.MDIPaneControl;
import dev.xlin.gameworkshop.GUI.dialog.DlgDBTempletInfo;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablockDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablockTemplet;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.datablockDefine;
import dev.xlin.gameworkshop.progs.foundation.datablockTemplet;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class IFrmDatablockTempMgr extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private boolean bInitTable = false;
    private List ltmp = null;

    public IFrmDatablockTempMgr(wakeup _up, JDesktopPane _desk)
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
        makeCombo();
        makeTree();
        makeTable();
    }

    private void makeCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        datablockDefine dbd = new datablockDefine(up);
        listItem lall = new listItem("-全部数据类型-", 0);
        mod.addElement(lall);
        List lds = dbd.getAllRecord();
        if (lds != null)
        {
            for (int i = 0; i < lds.size(); i++)
            {
                BeanDatablockDefine bdd = (BeanDatablockDefine) lds.get(i);
                listItem li = new listItem(bdd.getDbName(), bdd.getOID());
                mod.addElement(li);
            }
        }
        cmbDbType.setModel(mod);
    }

    private listItem getSelectDatalockType()
    {
        listItem li = (listItem) cmbDbType.getSelectedItem();
        return li;
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
            mtm.addColumn("数据类型");
            tbTemps.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbTemps);
            mtm = (myTableModel) tbTemps.getModel();
        }
        int dtpid = 0;
        listItem ldtp = getSelectDatalockType();
        if (ldtp != null)
        {
            dtpid = ldtp.getNodeOID();
        }
        BeanObjectClass boc = getSelectedOCLS();
        int ocid = 0;
        if (boc != null)
        {
            ocid = boc.getOID();
        }
        datablockTemplet dtmp = new datablockTemplet(up);
        ltmp = dtmp.getDatablockTemplets(dtpid, ocid, "");
        if (ltmp != null)
        {
            for (int i = 0; i < ltmp.size(); i++)
            {
                BeanDatablockTemplet bdt = (BeanDatablockTemplet) ltmp.get(i);
                Vector v = new Vector();
                v.add(bdt.getTempName());
                v.add(bdt.getTempDesp());
                v.add(iConst.translate(bdt.getDbType()));
                mtm.addRow(v);
            }
        }
        tbTemps.setModel(mtm);
    }

    private BeanDatablockTemplet getSelectedTemplet()
    {
        int idx = tbTemps.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanDatablockTemplet) ltmp.get(idx);
    }

    private void doEditInfo()
    {
        BeanDatablockTemplet bean = getSelectedTemplet();
        if (bean == null)
        {
            return;
        }
        DlgDBTempletInfo dlg = new DlgDBTempletInfo(null, true, up, bean);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanDatablockTemplet nb = dlg.getTempletBean();
            datablockTemplet dbt = new datablockTemplet(up);
            int r = dbt.updateTempletInfo(nb);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTable();
            }
            else
            {
                fast.err("更新模板信息失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void doRemoveTemp()
    {
        BeanDatablockTemplet bean = getSelectedTemplet();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认要删除选中的模板数据？\n这个操作不可被恢复。\n已使用该模板的数据不会受到影响");
        if (sel != fast.YES)
        {
            return;
        }
        datablockTemplet dbt = new datablockTemplet(up);
        int r = dbt.removeTemplet(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("删除操作失败", r);
        }
    }

    private void doEditData()
    {
        BeanDatablockTemplet bean = getSelectedTemplet();
        if (bean == null)
        {
            return;
        }
        if (MDIPaneControl.isMDIFrameOpened(MDIPaneControl.IFRM_DATABLOCK_TEMP, bean.getOID() +""))
        {
            fast .warn("数据区块编辑器已经打开");
            return ; 
        }
        IFrmDatablockTempEditor ifrm = new IFrmDatablockTempEditor(desk, up, bean);
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
        miEditData = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTemps = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        cmbDbType = new javax.swing.JComboBox<>();
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

        miEditData.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditData.setForeground(new java.awt.Color(255, 0, 0));
        miEditData.setText("编辑模板数据");
        miEditData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditDataActionPerformed(evt);
            }
        });
        popTemp.add(miEditData);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("数据区块模板管理");

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

        cmbDbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDbType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbDbTypeActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cmbDbType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(cmbDbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 871, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbDbTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbDbTypeActionPerformed
    {//GEN-HEADEREND:event_cmbDbTypeActionPerformed
        makeTable();
    }//GEN-LAST:event_cmbDbTypeActionPerformed

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

    private void miEditInfoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditInfoActionPerformed
    {//GEN-HEADEREND:event_miEditInfoActionPerformed
        doEditInfo();
    }//GEN-LAST:event_miEditInfoActionPerformed

    private void miRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveActionPerformed
    {//GEN-HEADEREND:event_miRemoveActionPerformed
        doRemoveTemp();
    }//GEN-LAST:event_miRemoveActionPerformed

    private void miEditDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditDataActionPerformed
    {//GEN-HEADEREND:event_miEditDataActionPerformed
        doEditData();
    }//GEN-LAST:event_miEditDataActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbDbType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem miEditData;
    private javax.swing.JMenuItem miEditInfo;
    private javax.swing.JMenuItem miRemove;
    private javax.swing.JPopupMenu popTemp;
    private javax.swing.JTable tbTemps;
    private javax.swing.JTree treeOcls;
    // End of variables declaration//GEN-END:variables
}
