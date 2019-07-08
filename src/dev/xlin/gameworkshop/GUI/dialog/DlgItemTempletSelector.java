package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemTemplet;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.itemTempletService;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
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

public class DlgItemTempletSelector extends javax.swing.JDialog
{

    private wakeup up = null;
    private int NODE_ROOT = 0;
    private int NODE_TYPE = 0;
    private int NODE_OCLS = 2;
    private HashMap hoclsCache = new HashMap();
    private boolean bInitTable = false;
    private List ltemps = null;
    private BeanItemTemplet bean = null;
    private boolean bOK = false;
    private int OCLSID = 0;

    public DlgItemTempletSelector(java.awt.Frame parent, boolean modal, wakeup _up, int _oclsID)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        OCLSID = _oclsID;
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
                        BeanObjectClass boc = (BeanObjectClass) locs.get(j);
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
        int ioc = 0;
        if (OCLSID != 0)
        {
            ioc = OCLSID;
        }
        else
        {
            myTreeNode mocls = getSelectedOcls();
            if (mocls != null)
            {
                ioc = mocls.getNodeOID();
            }
        }
        itemTempletService its = new itemTempletService(up);
        ltemps = its.getItemTemplets(ioc, txtNames.getText().trim());
        if (ltemps != null)
        {
            for (int i = 0; i < ltemps.size(); i++)
            {
                BeanItemTemplet btmp = (BeanItemTemplet) ltemps.get(i);
                Vector v = new Vector();
                v.add(btmp.getTempName());
                v.add(btmp.getTempDesp());
                if (hoclsCache.containsKey(btmp.getOclsID()))
                {
                    BeanObjectClass boc = (BeanObjectClass) hoclsCache.get(btmp.getOclsID());
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

    private BeanItemTemplet getSelectedTemplet()
    {
        int idx = tbTemplet.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanItemTemplet) ltemps.get(idx);
    }

    public boolean getOK()
    {
        return bOK;
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

    public BeanItemTemplet getTemplet()
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

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeOCLS = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbTemplet = new javax.swing.JTable();
        txtNames = new javax.swing.JTextField();
        btnQuery = new javax.swing.JButton();

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
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
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
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

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        doCancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        doOK();
    }//GEN-LAST:event_btnOKActionPerformed

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
