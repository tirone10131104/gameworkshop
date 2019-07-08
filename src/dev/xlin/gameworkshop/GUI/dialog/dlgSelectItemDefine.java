package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItem;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class dlgSelectItemDefine extends javax.swing.JDialog
{

    private wakeup up = null;
    private boolean bOK = false;
    private List les = null;
    private boolean bInitTable = false;
    private HashMap hocls = new HashMap();
    private beanItem bean = null;
    private int OCLS = 0;

    public dlgSelectItemDefine(java.awt.Frame parent, boolean modal, wakeup _up)
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
        myTreeNode mrt = guiCodes.makeFullTypeTree(up, systemType.CODE_STT_ITEMS, false ,0 );
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trTypes.setModel(dtm);
        guiCommon.expandTree(trTypes);
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

    private void makeTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("名称");
            mtm.addColumn("标签");
            mtm.addColumn("描述");
            mtm.addColumn("物类");
            tbEquips.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbEquips);
            mtm = (myTableModel) tbEquips.getModel();
        }
        myTreeNode mtn = getSelectType();
        int tpid = 0;
        if (mtn != null)
        {
            tpid = mtn.getNodeOID();
        }
        itemDefine sed = new itemDefine(up);
        les = sed.queryItems(tpid, OCLS, txtNames.getText().trim());
        objectClassDefine ocd = new objectClassDefine(up);
        if (les != null)
        {
            for (int i = 0; i < les.size(); i++)
            {
                beanItem bse = (beanItem) les.get(i);
                Vector v = new Vector();
                v.add(bse.getItemName());
                v.add(bse.getItemTag());
                v.add(bse.getItemDesp());
                beanObjectClass boc = getOCLSinCache(ocd, bse.getOclsID());
                if (boc != null)
                {
                    v.add(boc.getClassName());
                }
                else
                {
                    v.add("?");
                }
                mtm.addRow(v);
            }
        }
        tbEquips.setModel(mtm);
    }

    private beanObjectClass getOCLSinCache(objectClassDefine ocd, int ocid)
    {
        if (hocls.containsKey(ocid))
        {
            beanObjectClass boc = (beanObjectClass) hocls.get(ocid);
            return boc;
        }
        else
        {
            beanObjectClass boc = (beanObjectClass) ocd.getRecordByID(ocid);
            if (boc == null)
            {
                return null;
            }
            else
            {
                hocls.put(boc.getOID(), boc);
                return boc;
            }
        }
    }

    private beanItem getSelectedEquip()
    {
        int idx = tbEquips.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (beanItem) les.get(idx);
    }

    public boolean getOK()
    {
        return bOK;
    }

    private void doOK()
    {
        beanItem bse = getSelectedEquip();
        if (bse == null)
        {
            fast.warn("选择需要的装备");
            return;
        }
        bOK = true;
        bean = bse;
        setVisible(false);
    }

    public beanItem getSelectedItemDefine()
    {
        return bean;
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    private void doSelOCLS()
    {
        dlgSelectObjectClass dlg = new dlgSelectObjectClass(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            beanObjectClass boc = dlg.getSelectedObjectClass();
            txtOCLS.setText(boc.getClassName() + "<" + boc.getClassTag() + ">");
            OCLS = boc.getOID();
            makeTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doClearOCLS()
    {
        OCLS = 0;
        txtOCLS.setText("");
        makeTable();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        trTypes = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbEquips = new javax.swing.JTable();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtOCLS = new javax.swing.JTextField();
        btnSelOCLS = new javax.swing.JButton();
        txtNames = new javax.swing.JTextField();
        btnQuery = new javax.swing.JButton();
        btnClearOCLS = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("物体选择器");

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

        tbEquips.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbEquips.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tbEquips);

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

        jLabel1.setText("物类");

        txtOCLS.setEditable(false);

        btnSelOCLS.setText("选择");
        btnSelOCLS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelOCLSActionPerformed(evt);
            }
        });

        btnQuery.setText("查询");
        btnQuery.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnQueryActionPerformed(evt);
            }
        });

        btnClearOCLS.setText("清除");
        btnClearOCLS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnClearOCLSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancle)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtOCLS, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelOCLS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClearOCLS)
                .addGap(17, 17, 17)
                .addComponent(txtNames, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnQuery)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtOCLS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelOCLS)
                    .addComponent(txtNames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnQuery)
                    .addComponent(btnClearOCLS))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
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

    private void trTypesValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trTypesValueChanged
    {//GEN-HEADEREND:event_trTypesValueChanged
        makeTable();
    }//GEN-LAST:event_trTypesValueChanged

    private void btnSelOCLSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelOCLSActionPerformed
    {//GEN-HEADEREND:event_btnSelOCLSActionPerformed
        doSelOCLS();
    }//GEN-LAST:event_btnSelOCLSActionPerformed

    private void btnClearOCLSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnClearOCLSActionPerformed
    {//GEN-HEADEREND:event_btnClearOCLSActionPerformed
        doClearOCLS();
    }//GEN-LAST:event_btnClearOCLSActionPerformed

    private void btnQueryActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnQueryActionPerformed
    {//GEN-HEADEREND:event_btnQueryActionPerformed
        makeTable();
    }//GEN-LAST:event_btnQueryActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnClearOCLS;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnQuery;
    private javax.swing.JButton btnSelOCLS;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable tbEquips;
    private javax.swing.JTree trTypes;
    private javax.swing.JTextField txtNames;
    private javax.swing.JTextField txtOCLS;
    // End of variables declaration//GEN-END:variables
}
