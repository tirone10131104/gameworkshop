package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.DlgDatablockDefine;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablockDefine;
import dev.xlin.gameworkshop.progs.foundation.datablockDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import java.util.Vector;
import javax.swing.JDesktopPane;

public class IFrmDatablockDefine extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private boolean bInitTable = false;
    private List ldbs = null;

    public IFrmDatablockDefine(wakeup _up, JDesktopPane _desk)
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
        makeTable();
    }

    private void makeTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("数据名称");
            mtm.addColumn("数据标签");
            mtm.addColumn("数据描述");
            mtm.addColumn("数据结构");
            mtm.addColumn("操作面板");
            mtm.addColumn("数据节点");
            mtm.addColumn("实体类名");
            mtm.addColumn("系统");
            mtm.addColumn("实例设置");
            bInitTable = true;
        }
        else
        {
            fast.clearTableModel(tbDefs);
            mtm = (myTableModel) tbDefs.getModel();
        }
        datablockDefine def = new datablockDefine(up);
        ldbs = def.getAllRecord();
        if (ldbs != null)
        {
            for (int i = 0; i < ldbs.size(); i++)
            {
                BeanDatablockDefine bdd = (BeanDatablockDefine) ldbs.get(i);
                Vector v = new Vector();
                v.add(bdd.getDbName());
                v.add(bdd.getDbTag());
                v.add(bdd.getDbDesp());
                v.add(bdd.getDbAdtClass());
                v.add(bdd.getDbPalClass());
                v.add(bdd.getXmlNodeTag());
                v.add(bdd.getDataBeanClass());
                v.add(iConst.translateIBOL(bdd.getSystemDb()));
                v.add(iConst.translate(bdd.getInstanceConfig()));
                mtm.addRow(v);
            }
        }
        tbDefs.setModel(mtm);
    }

    private BeanDatablockDefine getSelectedDefine()
    {
        int idx = tbDefs.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanDatablockDefine) ldbs.get(idx);
    }

    private void doNewDB()
    {
        DlgDatablockDefine dlg = new DlgDatablockDefine(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doEditDB()
    {
        BeanDatablockDefine bean = getSelectedDefine();
        if (bean == null)
        {
            return;
        }
        DlgDatablockDefine dlg = new DlgDatablockDefine(null, true, up, bean);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDeleteDB()
    {
        BeanDatablockDefine bean = getSelectedDefine();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认删除这个数据区块定义？");
        if (sel != fast.YES)
        {
            return;
        }
        datablockDefine dbd = new datablockDefine(up);
        int r = dbd.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("删除操作成功", r);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbDefs = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("数据区块管理");
        setToolTipText("");
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

        tbDefs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbDefs.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbDefs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbDefs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbDefsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbDefs);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnNew.setText("新建数据");
        btnNew.setFocusable(false);
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNew.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNew);

        btnEdit.setText("修改数据");
        btnEdit.setFocusable(false);
        btnEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEdit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEdit);

        btnDelete.setText("删除数据");
        btnDelete.setFocusable(false);
        btnDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDelete);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsDatablockDefine(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewActionPerformed
    {//GEN-HEADEREND:event_btnNewActionPerformed
        doNewDB();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditActionPerformed
    {//GEN-HEADEREND:event_btnEditActionPerformed
        doEditDB();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteActionPerformed
    {//GEN-HEADEREND:event_btnDeleteActionPerformed
        doDeleteDB();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tbDefsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbDefsMouseClicked
    {//GEN-HEADEREND:event_tbDefsMouseClicked
        if (evt.getClickCount() >= 2)
        {
            doEditDB();
        }
    }//GEN-LAST:event_tbDefsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNew;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tbDefs;
    // End of variables declaration//GEN-END:variables
}
