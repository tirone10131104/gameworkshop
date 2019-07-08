package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfSet;
import dev.xlin.gameworkshop.progs.foundation.interfaceSet;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import java.awt.Toolkit;
import java.util.List;
import java.util.Vector;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class DlgSelectIntfSet extends javax.swing.JDialog
{

    private wakeup up = null;
    private boolean bOK = false;
    private boolean bInitTable = false;
    private List lsets = null;
    private int selectedIDX = 0;

    public DlgSelectIntfSet(java.awt.Frame parent, boolean modal, wakeup _up)
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
        makeSetType();
        makeSetTable();
    }

    private void makeSetType()
    {
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_PROG_INTF_SET, false);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trTypes.setModel(dtm);
        guiCommon.expandTree(trTypes);
    }

    private void makeSetTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("模板名称");
            mtm.addColumn("标签");
            mtm.addColumn("描述");
        }
        else
        {
            fast.clearTableModel(tbSet);
            mtm = (myTableModel) tbSet.getModel();
        }
        int tpid = getSelectedTypeID();
        if (tpid != 0)
        {
            interfaceSet iset = new interfaceSet(up);
            lsets = iset.getIntfSetsByType(tpid, false);
            if (lsets != null)
            {
                for (int i = 0; i < lsets.size(); i++)
                {
                    BeanProgIntfSet bpis = (BeanProgIntfSet) lsets.get(i);
                    Vector v = new Vector();
                    v.add(bpis.getSetName());
                    v.add(bpis.getSetTag());
                    v.add(bpis.getSetDesp());
                    mtm.addRow(v);
                }
            }
        }
        tbSet.setModel(mtm);
    }

    private int getSelectedTypeID()
    {
        TreePath tph = trTypes.getSelectionPath();
        if (tph == null)
        {
            return 0;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        return mtn.getNodeOID();
    }

    public boolean getOK ()
    {
        return bOK;
    }
    
    private void OK()
    {
        int idx = tbSet.getSelectedRow();
        if (idx < 0)
        {
            return;
        }
        selectedIDX = idx;
        bOK = true;
        setVisible(false);
    }

    public BeanProgIntfSet getSelectedIntfSet()
    {
        return (BeanProgIntfSet) lsets.get(selectedIDX);
    }

    private void cancle()
    {
        bOK = false;
        selectedIDX = 0;
        setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        trTypes = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbSet = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

        trTypes.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trTypesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(trTypes);

        jSplitPane1.setLeftComponent(jScrollPane1);

        tbSet.setModel(new javax.swing.table.DefaultTableModel(
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
        tbSet.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tbSet);

        jSplitPane1.setRightComponent(jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancle)
                .addContainerGap())
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        OK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void trTypesValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trTypesValueChanged
    {//GEN-HEADEREND:event_trTypesValueChanged
        makeSetTable();
    }//GEN-LAST:event_trTypesValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable tbSet;
    private javax.swing.JTree trTypes;
    // End of variables declaration//GEN-END:variables
}
