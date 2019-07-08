package dev.xlin.gameworkshop.GUI.CONTENT.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxConstellation;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxStellarRegion;
import dev.xlin.gameworkshop.progs.contents.progs.constellationData;
import dev.xlin.gameworkshop.progs.contents.progs.stellarRegion;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.awt.Toolkit;

public class dlgCtxConstellation extends javax.swing.JDialog
{

    private wakeup up = null;
    private int regionID = 0;
    private boolean bOK = false;
    private boolean bEdit = false;
    private beanCtxConstellation bean = null;

    public dlgCtxConstellation(java.awt.Frame parent, boolean modal, wakeup _up, beanCtxConstellation _bean, int _regionID)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        regionID = _regionID;
        bean = _bean;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        if (bean != null)
        {
            bEdit = true;
            txtName.setText(bean.getConName());
            txtTag.setText(bean.getConTag());
            txtDesp.setText(bean.getConDesp());
            txtTag.setEnabled(false);
            btnGeneTag.setEnabled(false);
            btnCheckTag.setEnabled(false);
        }
        else
        {
            bean = new beanCtxConstellation();
            bEdit = false;
        }
    }

    private void geneTag()
    {
        System.err.println("geneTag CONS ");
        String stg = tagCreator.createDataTag(up, "tbc_constellation", "conTag", 2, 5);
        txtTag.setText(stg);
        System.err.println("tag = " + stg);
    }

    public boolean getOK()
    {
        return bOK;
    }

    private boolean checkTag(boolean rpsc)
    {
        String stg = txtTag.getText().trim();
        if (stg.equals(""))
        {
            fast.warn("标签不可为空");
            return false;
        }
        stellarRegion srg = new stellarRegion(up);
        if (srg.getRegionByTag(stg) != null)
        {
            fast.warn("标签被占用");
            return false;
        }
        if (rpsc)
        {
            fast.msg("标签可以使用");
        }
        return true;
    }

    private void OK()
    {
        int r = 0;
        constellationData csd = new constellationData(up);
        bean.setConDesp(txtDesp.getText().trim());
        bean.setConName(txtName.getText().trim());
        bean.setConTag(txtTag.getText().trim());
        if (bEdit)
        {
            r = csd.modifyRecord(bean, false);
        }
        else
        {
            if (checkTag(false) == false)
            {
                return;
            }
            bean.setRegionOID(regionID);
            stellarRegion strg = new stellarRegion(up);
            beanCtxStellarRegion bcsr = (beanCtxStellarRegion) strg.getRecordByID(regionID);
            bean.setGlxOID(bcsr.getGlaxyOID());
            r = csd.createRecord(bean, false);
        }
        if (r == iDAO.OPERATE_SUCCESS)
        {
            bOK = true;
            setVisible(false);
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void cancle()
    {
        bOK = false;
        setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtTag = new javax.swing.JTextField();
        txtDesp = new javax.swing.JTextField();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        tbrTagTools = new javax.swing.JToolBar();
        btnGeneTag = new javax.swing.JButton();
        btnCheckTag = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("星座设置");
        setResizable(false);

        jLabel1.setText("星座名称");

        jLabel2.setText("星座标签");

        jLabel3.setText("星座描述");

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

        tbrTagTools.setBorder(null);
        tbrTagTools.setFloatable(false);
        tbrTagTools.setRollover(true);

        btnGeneTag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dev/xlin/gameworkshop/GUI/res/gener.png"))); // NOI18N
        btnGeneTag.setToolTipText("自动创建标签");
        btnGeneTag.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnGeneTagActionPerformed(evt);
            }
        });
        tbrTagTools.add(btnGeneTag);

        btnCheckTag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dev/xlin/gameworkshop/GUI/res/prot.png"))); // NOI18N
        btnCheckTag.setToolTipText("检查标签合法性");
        btnCheckTag.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCheckTagActionPerformed(evt);
            }
        });
        tbrTagTools.add(btnCheckTag);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 260, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTag)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDesp)))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, tbrTagTools, txtTag});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        OK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnGeneTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnGeneTagActionPerformed
    {//GEN-HEADEREND:event_btnGeneTagActionPerformed
        geneTag();
    }//GEN-LAST:event_btnGeneTagActionPerformed

    private void btnCheckTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCheckTagActionPerformed
    {//GEN-HEADEREND:event_btnCheckTagActionPerformed
        checkTag(true);
    }//GEN-LAST:event_btnCheckTagActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnCheckTag;
    private javax.swing.JButton btnGeneTag;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar tbrTagTools;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTag;
    // End of variables declaration//GEN-END:variables
}
