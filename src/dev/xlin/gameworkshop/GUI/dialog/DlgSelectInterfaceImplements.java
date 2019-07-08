package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiFullTreeGuiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfRegister;
import dev.xlin.gameworkshop.progs.foundation.interfaceDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaceRegister;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import java.awt.Toolkit;
import java.util.List;
import java.util.Vector;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Tirone
 */
public class DlgSelectInterfaceImplements extends javax.swing.JDialog
{

    private wakeup up = null;
    private List ldefs = null;
    private List lregs = null;
    private boolean bTbDef = false;
    private boolean bTbReg = false;
    private boolean bOK = false;
    private BeanProgIntfRegister bselReg = null;
    
    public DlgSelectInterfaceImplements(java.awt.Frame parent, boolean modal, wakeup _up)
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
        makeDefTable();
        makeRegTable();
    }

    private void makeTree()
    {
        myTreeNode mrt = guiCodes.makeFullTypeTree(up, systemType.CODE_STT_PROG_INTERFACE, false, 0);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trTps.setModel(dtm);
        guiCommon.expandTree(trTps);
    }

    private void makeDefTable()
    {
        myTreeNode msel = guiFullTreeGuiCodes.getSelectedType(trTps);
        int tpid = 0;
        if (msel != null)
        {
            tpid = msel.getNodeOID();
        }
        interfaceDefine idef = new interfaceDefine(up);
        ldefs = idef.getInterfacesByType(tpid, false);
        myTableModel mtm = null;
        if (bTbDef == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("名字");
            mtm.addColumn("标签");
            mtm.addColumn("接口定义");
            tbDefs.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbDefs);
            mtm = (myTableModel) tbDefs.getModel();
        }
        if (ldefs != null)
        {
            for (int i = 0; i < ldefs.size(); i++)
            {
                BeanProgIntfDefine bdef = (BeanProgIntfDefine) ldefs.get(i);
                Vector v = new Vector();
                v.add(bdef.getIntfName());
                v.add(bdef.getIntfTag());
                v.add(bdef.getIntfAddress());
                mtm.addRow(v);
            }
        }
        tbDefs.setModel(mtm);
    }

    private BeanProgIntfDefine getSelectedDefine()
    {
        int idx = tbDefs.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanProgIntfDefine) ldefs.get(idx);
    }

    private void makeRegTable()
    {
        BeanProgIntfDefine bdef = getSelectedDefine();
        int defid = 0 ;
        if (bdef != null)
        {
            defid = bdef.getOID();
        }
        myTableModel mtm = null;
        if (bTbReg == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("标签");
            mtm.addColumn("描述");
            mtm.addColumn("地址");
            tbImpls.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbImpls);
            mtm = (myTableModel) tbImpls.getModel();
        }
        interfaceRegister ireg = new interfaceRegister(up);
        lregs = ireg.getRegsByDef(defid, false);
        if (lregs!= null)
        {
            for (int i = 0; i < lregs.size(); i++)
            {
                BeanProgIntfRegister bean = (BeanProgIntfRegister) lregs.get(i);
                Vector v = new Vector();
                v.add(bean.getRegTag());
                v.add(bean.getRegDescription());
                v.add(bean.getImplAddress());
                mtm.addRow(v);
            }
        }
        tbImpls.setModel(mtm);
    }

    private BeanProgIntfRegister getRegSel ()
    {
        int idx = tbImpls.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        BeanProgIntfRegister bean = (BeanProgIntfRegister) lregs.get(idx);
        return bean ;
    }
    
    private void doOK()
    {
        BeanProgIntfRegister bean = getRegSel();
        if (bean == null)
        {
            return;
        }
        bselReg = bean ; 
        bOK = true;
        setVisible(false);
    }
    
    public BeanProgIntfRegister getSelectedRegister ()
    {
        return bselReg;
    }

    private void doCancle()
    {
        bselReg = null;
        bOK = false;
        setVisible(false);
    }

    public boolean getOK ()
    {
        return bOK;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDefs = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbImpls = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        trTps = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("选择接口实现注册");

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

        jSplitPane2.setDividerLocation(300);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        tbDefs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbDefs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbDefsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbDefs);

        jSplitPane2.setTopComponent(jScrollPane2);

        tbImpls.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbImpls.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbImplsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbImpls);

        jSplitPane2.setRightComponent(jScrollPane3);

        jSplitPane1.setRightComponent(jSplitPane2);

        trTps.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trTpsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(trTps);

        jSplitPane1.setLeftComponent(jScrollPane1);

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
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1157, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
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
        doOK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        doCancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void trTpsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trTpsValueChanged
    {//GEN-HEADEREND:event_trTpsValueChanged
        makeDefTable();
        makeRegTable();
    }//GEN-LAST:event_trTpsValueChanged

    private void tbDefsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbDefsMouseClicked
    {//GEN-HEADEREND:event_tbDefsMouseClicked
        makeRegTable();
    }//GEN-LAST:event_tbDefsMouseClicked

    private void tbImplsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbImplsMouseClicked
    {//GEN-HEADEREND:event_tbImplsMouseClicked
        if(evt.getClickCount() >=2)
        {
            doOK();
        }
    }//GEN-LAST:event_tbImplsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable tbDefs;
    private javax.swing.JTable tbImpls;
    private javax.swing.JTree trTps;
    // End of variables declaration//GEN-END:variables
}
