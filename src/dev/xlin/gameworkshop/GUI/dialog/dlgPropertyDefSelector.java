package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanPropertyDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import java.awt.Toolkit;
import java.util.List;
import java.util.Vector;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class dlgPropertyDefSelector extends javax.swing.JDialog
{

    private boolean bOK = false;
    private wakeup up = null;
    private beanPropertyDefine bean = null;
    private List lpros = null;
    private boolean bInitTable = false;

    public dlgPropertyDefSelector(java.awt.Frame parent, boolean modal, wakeup _up)
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

    private void makeTree()
    {
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_SYS_PROP, false);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trTypes.setModel(dtm);
    }

    private myTreeNode getSelectedType()
    {
        TreePath tph = trTypes.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        return (myTreeNode) tph.getLastPathComponent();
    }

    private beanPropertyDefine getSelectedProperty()
    {
        int idx = tbProps.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (beanPropertyDefine) lpros.get(idx);
    }

    private void makeTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("属性名");
            mtm.addColumn("标签");
            mtm.addColumn("描述");
            mtm.addColumn("类型");
            bInitTable = true;
        }
        else
        {
            fast.clearTableModel(tbProps);
            mtm = (myTableModel) tbProps.getModel();
        }
        myTreeNode mtp = getSelectedType();
        if (mtp == null)
        {
            tbProps.setModel(mtm);
            return ;
        }
        propertyDefine pdef = new propertyDefine(up);
        lpros = pdef.getPropsByType(mtp.getNodeOID(), false);
        if (lpros != null)
        {
            for (int i = 0; i < lpros.size(); i++)
            {
                beanPropertyDefine bpd = (beanPropertyDefine) lpros.get(i);
                Vector v = new Vector();
                v.add(bpd.getPropName());
                v.add(bpd.getPropTag());
                v.add(bpd.getPropDesp());
                v.add(iConst.translate(bpd.getDataType()));
                mtm.addRow(v);
            }
        }
        tbProps.setModel(mtm);
    }

    private void doOK()
    {
        bean = getSelectedProperty();
        if (bean == null)
        {
            return;
        }
        bOK = true;
        setVisible(false);
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    public boolean getOK ()
    {
        return bOK;
    }
    
    public beanPropertyDefine getSelectedProp ()
    {
        return bean;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        trTypes = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProps = new javax.swing.JTable();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("选择一个属性");

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

        trTypes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                trTypesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(trTypes);

        jSplitPane1.setLeftComponent(jScrollPane1);

        tbProps.setModel(new javax.swing.table.DefaultTableModel(
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
        tbProps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tbProps);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancle)
                .addContainerGap())
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
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

    private void trTypesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_trTypesMouseClicked
    {//GEN-HEADEREND:event_trTypesMouseClicked
        makeTable();
    }//GEN-LAST:event_trTypesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable tbProps;
    private javax.swing.JTree trTypes;
    // End of variables declaration//GEN-END:variables
}
