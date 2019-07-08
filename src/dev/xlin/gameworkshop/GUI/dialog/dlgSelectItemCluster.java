package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItemCluster;
import dev.xlin.gameworkshop.progs.foundation.itemCluster;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class dlgSelectItemCluster extends javax.swing.JDialog
{

    private wakeup up = null;
    private List lcs = null;
    private boolean bOK = false;
    private boolean bInitTable = false;
    private beanItemCluster bean = null;

    public dlgSelectItemCluster(java.awt.Frame parent, boolean modal, wakeup _up)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        makeCombo();
        makeTree();
        makeTable();
    }

    public boolean getOK ()
    {
        return bOK;
    }
    
    private void makeCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        listItem lall = new listItem("[所有类型]", 0);
        mod.addElement(lall);
        int[] ids = constChk.getFinalInts(iConst.class, "ICLUS_TARTYPE_");
        for (int i = 0; i < ids.length; i++)
        {
            int id = ids[i];
            listItem li = new listItem(iConst.translate(id), id);
            mod.addElement(li);
        }
        cmbTarType.setModel(mod);
    }

    private myTreeNode getSelectedType()
    {
        TreePath tph = treeTps.getSelectionPath();
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

    private void makeTable()
    {
        myTableModel mtm = new myTableModel();
        if (bInitTable == false)
        {
            mtm.addColumn("名称");
            mtm.addColumn("标签");
            mtm.addColumn("目标类型");
            tbClus.setModel(mtm);
            bInitTable = true;
        }
        else
        {
            fast.clearTableModel(tbClus);
            mtm = (myTableModel) tbClus.getModel();
        }
        int tpid = 0;
        myTreeNode mtp = getSelectedType();
        if (mtp != null)
        {
            tpid = mtp.getNodeOID();
        }
        int dtp = swsys.getComboBoxSelected(cmbTarType);
        itemCluster iclu = new itemCluster(up);
        lcs = iclu.findItemClusters(dtp, tpid, false);
        if (lcs != null)
        {
            for (int i = 0; i < lcs.size(); i++)
            {
                beanItemCluster bic = (beanItemCluster) lcs.get(i);
                Vector v = new Vector();
                v.add(bic.getClusterName());
                v.add(bic.getClusterTag());
                v.add(iConst.translate(bic.getTargetType()));
                mtm.addRow(v);
            }
        }
        tbClus.setModel(mtm);
    }

    private void makeTree()
    {
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_ITEM_CLUSTER, false);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeTps.setModel(dtm);
    }

    private void doOK()
    {
        int idx = tbClus.getSelectedRow();
        if (idx < 0)
        {
            return;
        }
        bean = (beanItemCluster) lcs.get(idx);
        bOK = true;
        setVisible(false);
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    public beanItemCluster getSelectedBean()
    {
        if (bOK)
        {
            return bean;
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        cmbTarType = new javax.swing.JComboBox<>();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeTps = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbClus = new javax.swing.JTable();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("数据类型");

        cmbTarType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbTarType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTarTypeActionPerformed(evt);
            }
        });

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

        treeTps.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeTpsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treeTps);

        jSplitPane1.setLeftComponent(jScrollPane1);

        tbClus.setModel(new javax.swing.table.DefaultTableModel(
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
        tbClus.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tbClus);

        jSplitPane1.setRightComponent(jScrollPane2);

        btnCancle.setText("取消");
        btnCancle.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancleActionPerformed(evt);
            }
        });

        btnOK.setText("确定");
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTarType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbTarType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbTarTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTarTypeActionPerformed
    {//GEN-HEADEREND:event_cmbTarTypeActionPerformed
        makeTable();
    }//GEN-LAST:event_cmbTarTypeActionPerformed

    private void treeTpsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeTpsValueChanged
    {//GEN-HEADEREND:event_treeTpsValueChanged
        makeTable();
    }//GEN-LAST:event_treeTpsValueChanged

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        doOK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        doCancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox<String> cmbTarType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable tbClus;
    private javax.swing.JTree treeTps;
    // End of variables declaration//GEN-END:variables
}
