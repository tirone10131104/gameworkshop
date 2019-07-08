package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillLevel;
import dev.xlin.gameworkshop.progs.foundation.skillDefine;
import dev.xlin.gameworkshop.progs.foundation.skillLevel;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author 刘祎鹏
 */
public class dlgSelectSkill extends javax.swing.JDialog
{

    private wakeup up = null;
    private List llvs = null;
    private HashMap hsks = null;
    private boolean bOK = false;

    private beanSkillDefine bskd = null;
    private beanSkillLevel bskl = null;
    private skillDefine skdef = null;
    private skillLevel sklvl = null;
    private boolean bInitTable = false;

    public dlgSelectSkill(java.awt.Frame parent, boolean modal, wakeup _up)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        skdef = new skillDefine(up);
        sklvl = new skillLevel(up);
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        makeTypeTree();
        makeSkillTree();
        makeLevelTable();
    }

    private void makeTypeTree()
    {
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_SKILL_DEF, false);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trTps.setModel(dtm);
        guiCommon.expandTree(trTps);
    }

    private myTreeNode getSelectedType()
    {
        TreePath tph = trTps.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode msel = (myTreeNode) tph.getLastPathComponent();
        if (msel.getNodeOID() == 0)
        {
            return null;
        }
        return msel;
    }

    private beanSkillDefine getSelectedSkillTreeNode()
    {
        TreePath tph = trSkills.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode msel = (myTreeNode) tph.getLastPathComponent();
        if (msel.getNodeOID() == 0)
        {
            return null;
        }
        return (beanSkillDefine) hsks.get(msel.getNodeOID());
    }

    private void makeSkillTree()
    {
        int tpid = 0;
        myTreeNode mtp = getSelectedType();
        if (mtp != null)
        {
            tpid = mtp.getNodeOID();
        }
        List lsk = skdef.getSkillByType(tpid, false);
        hsks = new HashMap();
        myTreeNode mrt = new myTreeNode("[技能数据库]", 0, 0);
        if (lsk != null)
        {
            for (int i = 0; i < lsk.size(); i++)
            {
                beanSkillDefine bsd = (beanSkillDefine) lsk.get(i);
                hsks.put(bsd.getOID(), bsd);
            }
            //开始递归
            dMakeSkillTree(mrt, 0);
        }
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trSkills.setModel(dtm);
        guiCommon.expandTree(trSkills);
    }

    private void dMakeSkillTree(myTreeNode mpar, int parid)
    {
        List lcs = findChildSkills(parid);
        for (int i = 0; i < lcs.size(); i++)
        {
            beanSkillDefine bsd = (beanSkillDefine) lcs.get(i);
            myTreeNode mtn = new myTreeNode(bsd.getSkillName(), bsd.getOID(), 1);
            dMakeSkillTree(mtn, bsd.getOID());
            mpar.add(mtn);
        }
    }

    private List findChildSkills(int parid)
    {
        List lr = new ArrayList();
        Set ks = hsks.keySet();
        Iterator itor = ks.iterator();
        while (itor.hasNext())
        {
            int kid = (int) itor.next();
            beanSkillDefine bsd = (beanSkillDefine) hsks.get(kid);
            if (bsd.getParentSkillOID() == parid)
            {
                lr.add(bsd);
            }
        }
        return lr;
    }

    private void makeLevelTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("分级序号");
            mtm.addColumn("名称");
            mtm.addColumn("描述");
            mtm.addColumn("主分级");
            tbLevels.setModel(mtm);
            mtm = (myTableModel) tbLevels.getModel();
            bInitTable = true;
        }
        else
        {
            fast.clearTableModel(tbLevels);
            mtm = (myTableModel) tbLevels.getModel();
        }
        beanSkillDefine bsel = getSelectedSkillTreeNode();
        if (bsel != null)
        {
            llvs = sklvl.getLevelBySkill(bsel.getOID());
            if (llvs != null)
            {
                for (int i = 0; i < llvs.size(); i++)
                {
                    beanSkillLevel blv = (beanSkillLevel) llvs.get(i);
                    Object[] os = fast.makeObjectArray(4);
                    os[0] = blv.getLevelIdx() + "";
                    os[1] = blv.getLevelName();
                    os[2] = blv.getLevelDesp();
                    os[3] = iConst.translateIBOL(blv.getMasterLevel());
                    mtm.addRow(os);
                }
            }
        }
        tbLevels.setModel(mtm);
    }

    private void OK()
    {
        if (bskd == null)
        {
            return;
        }
        if (bskl == null)
        {
            return;
        }
        bOK = true;
        setVisible(false);
    }

    private void cancle()
    {
        bOK = false;
        setVisible(false);
    }

    public boolean getOK()
    {
        return bOK;
    }

    public beanSkillDefine getSelectedDefine()
    {
        return bskd;
    }

    public beanSkillLevel getSelectedLevel()
    {
        return bskl;
    }

    private void skillTreeSelected()
    {
        beanSkillDefine b = getSelectedSkillTreeNode();
        bskd = b;
        bskl = null;
        makeLevelTable();
    }

    private void levelTableSelected()
    {
        int idx = tbLevels.getSelectedRow();
        if (idx < 0)
        {
            bskl = null;
            return;
        }
        else
        {
            bskl = (beanSkillLevel) llvs.get(idx);
        }
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
        trTps = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        trSkills = new javax.swing.JTree();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbLevels = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("技能选择工具");

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

        jSplitPane1.setDividerLocation(400);

        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        trTps.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trTpsValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(trTps);

        jSplitPane2.setTopComponent(jScrollPane2);

        trSkills.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trSkillsValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(trSkills);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
        );

        jSplitPane2.setRightComponent(jPanel1);

        jSplitPane1.setLeftComponent(jSplitPane2);

        tbLevels.setModel(new javax.swing.table.DefaultTableModel(
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
        tbLevels.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbLevels.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                tbLevelsMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbLevels);

        jSplitPane1.setRightComponent(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(670, Short.MAX_VALUE)
                .addComponent(btnOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancle)
                .addContainerGap())
            .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
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

    private void trTpsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trTpsValueChanged
    {//GEN-HEADEREND:event_trTpsValueChanged
        makeSkillTree();
        makeLevelTable();
        bskd = null;
        bskl = null;
    }//GEN-LAST:event_trTpsValueChanged

    private void trSkillsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trSkillsValueChanged
    {//GEN-HEADEREND:event_trSkillsValueChanged
        skillTreeSelected();
    }//GEN-LAST:event_trSkillsValueChanged

    private void tbLevelsMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbLevelsMouseReleased
    {//GEN-HEADEREND:event_tbLevelsMouseReleased
        levelTableSelected();
    }//GEN-LAST:event_tbLevelsMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable tbLevels;
    private javax.swing.JTree trSkills;
    private javax.swing.JTree trTps;
    // End of variables declaration//GEN-END:variables
}
