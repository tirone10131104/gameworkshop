package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
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

public class dlgSelectObjectClass extends javax.swing.JDialog
{

    private wakeup up = null;
    private beanObjectClass bean = null;
    private List locs = null;
    private boolean bOK = false;
    private boolean bInitTable = false;

    public dlgSelectObjectClass(java.awt.Frame parent, boolean modal, wakeup _up)
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
        makeTree();
        makeTable();
    }
    
    private beanObjectClass getSelOc ()
    {
        int idx = tbOcls.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        beanObjectClass boc = (beanObjectClass) locs.get(idx);
        return boc;
    }

    private void doOK()
    {
        bean = getSelOc();
        if (bean == null)
        {
            return ;
        }
        bOK = true;
        setVisible(false);
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    private void makeTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("名称");
            mtm.addColumn("标签");
            mtm.addColumn("描述");
            tbOcls.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbOcls);
            mtm = (myTableModel) tbOcls.getModel();
        }
        myTreeNode mtp = getSelectType();
        if (mtp == null)
        {
            tbOcls.setModel(mtm);
            return;
        }
        int tpid = mtp.getNodeOID();
        objectClassDefine ocd = new objectClassDefine(up);
        locs = ocd.getObjectClassByType(tpid, false);
        if (locs != null)
        {
            for (int i = 0; i < locs.size(); i++)
            {
                beanObjectClass boc = (beanObjectClass) locs.get(i);
                Vector v = new Vector();
                v.add(boc.getClassName());
                v.add(boc.getClassTag());
                v.add(boc.getClassDesp());
                mtm.addRow(v);
            }
        }
        tbOcls.setModel(mtm);
    }

    private myTreeNode getSelectType()
    {
        TreePath tph = trTypes.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() == 0)
        {
            return null;
        }
        return mtn;
    }

    public boolean getOK()
    {
        return bOK;
    }

    public beanObjectClass getSelectedObjectClass()
    {
        return bean;
    }

    private void makeTree()
    {
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_OBJ_CLS, false);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trTypes.setModel(dtm);
        guiCommon.expandTree(trTypes);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        trTypes = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbOcls = new javax.swing.JTable();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        tbOcls.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbOcls.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tbOcls);

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
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancle)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void trTypesValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trTypesValueChanged
    {//GEN-HEADEREND:event_trTypesValueChanged
        makeTable();
    }//GEN-LAST:event_trTypesValueChanged

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        doCancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        doOK();
    }//GEN-LAST:event_btnOKActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable tbOcls;
    private javax.swing.JTree trTypes;
    // End of variables declaration//GEN-END:variables
}
