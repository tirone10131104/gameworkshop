package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.beanDatablockDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanDatablockTemplet;
import dev.xlin.gameworkshop.progs.foundation.beans.beanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.datablockDefine;
import dev.xlin.gameworkshop.progs.foundation.datablockTemplet;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class dlgDatablockTempletSelector extends javax.swing.JDialog
{

    private wakeup up = null;
    private beanDatablockTemplet bean = null;
    private boolean bOK = false;
    private int NODE_ROOT = 0;
    private int NODE_TYPE = 0;
    private int NODE_OCLS = 2;
    private boolean bInitTable = false;
    private List ltemps = null;
    private HashMap hoclsCache = new HashMap();
    private String blockTag = "";

    public dlgDatablockTempletSelector(java.awt.Frame parent, boolean modal, wakeup _up, String _blcokTag)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        blockTag = _blcokTag;
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

    private void makeTree()
    {
        hoclsCache.clear();
        sttType stp = new sttType(up);
        List lrt = stp.getRootsByDef(systemType.CODE_STT_OBJ_CLS, false);
        myTreeNode mrt = new myTreeNode("[物类数据库]", 0, 0);
        objectClassDefine ocd = new objectClassDefine(up);
        if (lrt != null)
        {
            for (int i = 0; i < lrt.size(); i++)
            {
                beanSttType bst = (beanSttType) lrt.get(i);
                myTreeNode mst = new myTreeNode("[分类]" + bst.getTypeName(), 0, NODE_TYPE);
                List locs = ocd.getObjectClassByType(bst.getOID(), false);
                if (locs != null)
                {
                    for (int j = 0; j < locs.size(); j++)
                    {
                        beanObjectClass boc = (beanObjectClass) locs.get(j);
                        hoclsCache.put(boc.getOID(), boc);
                        myTreeNode moc = new myTreeNode(boc.getClassName() + "<" + boc.getClassTag() + ">", boc.getOID(), NODE_OCLS);
                        mst.add(moc);
                    }
                }
                mrt.add(mst);
            }
        }
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeOCLS.setModel(dtm);
        guiCommon.expandTree(treeOCLS);
    }

    private void makeCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        datablockDefine dbdef = new datablockDefine(up);
        if (blockTag.equals(""))
        {
            List lrs = dbdef.getAllRecord();
            if (lrs != null)
            {
                for (int i = 0; i < lrs.size(); i++)
                {
                    beanDatablockDefine bdef = (beanDatablockDefine) lrs.get(i);
                    listItem li = new listItem(bdef.getDbName(), bdef.getOID());
                    mod.addElement(li);
                }
            }
        }
        else
        {
            beanDatablockDefine bdef = dbdef.getDataDefineByTag(blockTag);
            if (bdef != null)
            {
                listItem li = new listItem(bdef.getDbName(), bdef.getOID());
                mod.addElement(li);
            }
        }
        cmbDatatype.setModel(mod);
    }

    private listItem getSelectedDatatype()
    {
        listItem li = (listItem) cmbDatatype.getSelectedItem();
        return li;
    }

    private myTreeNode getSelectedOcls()
    {
        TreePath tph = treeOCLS.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() != NODE_OCLS)
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
            mtm.addColumn("模板名称");
            mtm.addColumn("模板描述");
            mtm.addColumn("物类");
        }
        else
        {
            fast.clearTableModel(tbTemplet);
            mtm = (myTableModel) tbTemplet.getModel();
        }
        listItem ldt = getSelectedDatatype();
        int idt = ldt.getNodeOID();
        myTreeNode mocls = getSelectedOcls();
        int ioc = 0;
        if (mocls != null)
        {
            ioc = mocls.getNodeOID();
        }
        datablockTemplet dbt = new datablockTemplet(up);
        ltemps = dbt.getDatablockTemplets(idt, ioc ,txtNames.getText().trim());
        if (ltemps != null)
        {
            for (int i = 0; i < ltemps.size(); i++)
            {
                beanDatablockTemplet btmp = (beanDatablockTemplet) ltemps.get(i);
                Vector v = new Vector();
                v.add(btmp.getTempName());
                v.add(btmp.getTempDesp());
                if (hoclsCache.containsKey(btmp.getOclsID()))
                {
                    beanObjectClass boc = (beanObjectClass) hoclsCache.get(btmp.getOclsID());
                    v.add(boc.getClassName());
                }
                else
                {
                    v.add("[" + btmp.getOclsID() + "]");
                }
                mtm.addRow(v);
            }
        }
        tbTemplet.setModel(mtm);
    }

    public boolean getOK()
    {
        return bOK;
    }

    private  beanDatablockTemplet getSelectedTemplet()
    {
        int idx = tbTemplet.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (beanDatablockTemplet) ltemps.get(idx);
    }

    private void doOK()
    {
        bean = getSelectedTemplet();
        if (bean == null)
        {
            fast.warn("请选择一个模板");
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

    public beanDatablockTemplet getTemplet()
    {
        return bean;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        cmbDatatype = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeOCLS = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbTemplet = new javax.swing.JTable();
        txtNames = new javax.swing.JTextField();
        btnQuery = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("选择数据模板");

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

        cmbDatatype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDatatype.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbDatatypeActionPerformed(evt);
            }
        });

        treeOCLS.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                treeOCLSMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(treeOCLS);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbDatatype, 0, 267, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbDatatype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel1);

        tbTemplet.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tbTemplet);

        btnQuery.setText("查询");
        btnQuery.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnQueryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNames)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnQuery)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnQuery))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

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
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane1)
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

    private void cmbDatatypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbDatatypeActionPerformed
    {//GEN-HEADEREND:event_cmbDatatypeActionPerformed
        makeTable();
    }//GEN-LAST:event_cmbDatatypeActionPerformed

    private void treeOCLSMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeOCLSMouseClicked
    {//GEN-HEADEREND:event_treeOCLSMouseClicked
        makeTable();
    }//GEN-LAST:event_treeOCLSMouseClicked

    private void btnQueryActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnQueryActionPerformed
    {//GEN-HEADEREND:event_btnQueryActionPerformed
        makeTable();
    }//GEN-LAST:event_btnQueryActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnQuery;
    private javax.swing.JComboBox<String> cmbDatatype;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable tbTemplet;
    private javax.swing.JTree treeOCLS;
    private javax.swing.JTextField txtNames;
    // End of variables declaration//GEN-END:variables
}
