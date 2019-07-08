package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfRegister;
import dev.xlin.gameworkshop.progs.foundation.interfaceRegister;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import java.awt.Toolkit;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class dlgSelectIntfImplsByISET extends javax.swing.JDialog
{

    private wakeup up = null;
    private String iSetTag = "";
    private boolean bOK = false;
    private beanProgIntfRegister bselReg = null;
    public dlgSelectIntfImplsByISET(java.awt.Frame parent, boolean modal, wakeup _up, String _iSetTag)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        iSetTag = _iSetTag;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        //(frmMain.getImageIcon());
        makeISetTree();
        makeImplsTree();
    }

    //创建接口模板树图
    private void makeISetTree()
    {
        myTreeNode miset = guiCodes.makeProgInterfaceSetTree(up, iSetTag, false);
        DefaultTreeModel dtm = new DefaultTreeModel(miset);
        treeISet.setModel(dtm);
        guiCommon.expandTree(treeISet);
    }

    private int getSelectedISET()
    {
        TreePath tph = treeISet.getSelectionPath();
        if (tph == null)
        {
            return 0 ;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        return mtn.getNodeOID();
    }
    
    //创建实现接口节点树图
    private void makeImplsTree()
    {
        int isid = getSelectedISET() ;
        myTreeNode mrt = guiCodes.makeProgImplTreeByISET(up , isid,false, false );
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeImpls.setModel(dtm);
        guiCommon.expandTree(treeImpls);
    }

    private void OK()
    {
        TreePath tph = treeImpls.getSelectionPath();
        if (tph == null )
        {
            return ;
        }
        myTreeNode msel = (myTreeNode) tph.getLastPathComponent();
        if (msel.getNodeOID() == 0 )
        {
            return ; 
        }
        interfaceRegister irg = new interfaceRegister(up);
        bselReg = (beanProgIntfRegister) irg.getRecordByID(msel.getNodeOID());
        bOK = true ; 
        setVisible(false);
    }
    
    private void cancle ()
    {
        bOK = false;
        bselReg = null;
        setVisible(false);
    }
    
    public boolean getOK ()
    {
        return bOK;
    }
    
    public beanProgIntfRegister getSelectedReg()
    {
        return bselReg;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        spDlg = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeISet = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        treeImpls = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("接口集程序实现节点选择");

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

        spDlg.setDividerLocation(288);

        jLabel1.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jLabel1.setText("接口集模板");

        treeISet.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeISetValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treeISet);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
        );

        spDlg.setLeftComponent(jPanel1);

        jLabel2.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jLabel2.setText("接口实现节点");

        jScrollPane2.setViewportView(treeImpls);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
        );

        spDlg.setRightComponent(jPanel2);

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
            .addComponent(spDlg)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(spDlg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void treeISetValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeISetValueChanged
    {//GEN-HEADEREND:event_treeISetValueChanged
        makeImplsTree();
    }//GEN-LAST:event_treeISetValueChanged

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
         OK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane spDlg;
    private javax.swing.JTree treeISet;
    private javax.swing.JTree treeImpls;
    // End of variables declaration//GEN-END:variables
}
