package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.runtime.microEngine.BeanMCEngineConfigMain;
import dev.xlin.gameworkshop.progs.runtime.microEngine.MCEConfigDAO;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.JDAO;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;

public class DlgMCEConfig extends javax.swing.JDialog
{

    private BeanMCEngineConfigMain bean = null;
    private wakeup up = null;
    private boolean bOK = false;
    private boolean bEdit = false;

    public DlgMCEConfig(java.awt.Frame parent, boolean modal, wakeup _up, BeanMCEngineConfigMain _bean)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        makeMaxTPSCombo();
        makeStartMtdCombo();
        makeEmptyMtdCombo();
        makeEmptyWaitCombo();
        makeInstanceLmtCombo();
        if (bean == null)
        {
            bean = new BeanMCEngineConfigMain();
            bEdit = false;
            geneTag();
        }
        else
        {
            txtDesp.setText(bean.getMceDescp());
            txtName.setText(bean.getMceName());
            txtTag.setText(bean.getMceTag());
            swsys.doSelectCombo(cmbMaxTPS, bean.getMaxTps());
            swsys.doSelectCombo(cmbEmptyMtd, bean.getEmptyMethod());
            swsys.doSelectCombo(cmbInstLmt, bean.getInstanceLimit());
            swsys.doSelectCombo(cmbStartMtd, bean.getStartMethod());
            swsys.doSelectCombo(cmbWaitLong, bean.getEmptyWaitLong());
            bEdit = true;
        }

    }

    private void makeMaxTPSCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("1次/秒", 1));
        mod.addElement(new listItem("2次/秒", 2));
        mod.addElement(new listItem("4次/秒", 4));
        mod.addElement(new listItem("5次/秒", 5));
        mod.addElement(new listItem("10次/秒", 10));
        mod.addElement(new listItem("20次/秒", 20));
        cmbMaxTPS.setModel(mod);
    }

    private void makeStartMtdCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("自动启动", iConst.MCES_MCST_AUTO));
        mod.addElement(new listItem("被动启动", iConst.MCES_MCST_MANUAL));
        cmbStartMtd.setModel(mod);
    }

    private void makeEmptyMtdCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("自动休眠", iConst.MCES_EMPTY_MTD_SLEEP));
        mod.addElement(new listItem("自动关闭", iConst.MCES_EMPTY_MTD_STOP));
        cmbEmptyMtd.setModel(mod);
    }

    private void makeEmptyWaitCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("10秒", 10000));
        mod.addElement(new listItem("30秒", 30000));
        mod.addElement(new listItem("1分钟", 60000));
        mod.addElement(new listItem("5分钟", 300000));
        cmbWaitLong.setModel(mod);
    }

    private void makeInstanceLmtCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("单线程", 1));
        mod.addElement(new listItem("2线程", 2));
        mod.addElement(new listItem("3线程", 3));
        mod.addElement(new listItem("4线程", 4));
        mod.addElement(new listItem("5线程", 5));
        mod.addElement(new listItem("无限制", 0));
        cmbInstLmt.setModel(mod);
    }

    private void geneTag()
    {
        String s = tagCreator.createDataTag(up, "ic_mce_config_main", "mceTag", 4, 4);
        txtTag.setText(s);
    }

    private boolean checkTag(boolean b)
    {
        String s = txtTag.getText().trim();
        if (s.equals(""))
        {
            fast.warn("标签不能为空");
            return false;
        }
        if (bEdit)
        {
            if (s.equals(bean.getMceTag()) == false)
            {
                boolean brp = _checkTagIsRepeat(s);
                if (brp)
                {
                    fast.warn("标签检测到已被占用");
                    return !brp;
                }
            }
        }
        else
        {
            boolean brp = _checkTagIsRepeat(s);
            if (brp)
            {
                fast.warn("标签检测到已被占用");
                return !brp;
            }
        }
        if (b)
        {
            fast.warn("检查OK");
        }
        return true;
    }

    private boolean _checkTagIsRepeat(String s)
    {
        MCEConfigDAO mcd = new MCEConfigDAO(up);
        if (mcd.getMCEConfigByTag(s) != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean getOK()
    {
        return bOK;
    }

    private void OK()
    {
        System.err.println("...dlg ok");
        String sname = txtName.getText().trim();
        if (sname.equals(""))
        {
            fast.warn("名称不能为空");
            return;
        }
        boolean b = checkTag(false);
        if (b == false)
        {
            System.err.println("tag false ");
            return;
        }
        //数据in
        bean.setEmptyMethod(swsys.getComboBoxSelected(cmbEmptyMtd));
        bean.setEmptyWaitLong(swsys.getComboBoxSelected(cmbWaitLong));
        bean.setInstanceLimit(swsys.getComboBoxSelected(cmbInstLmt));
        bean.setMaxTps(swsys.getComboBoxSelected(cmbMaxTPS));
        bean.setStartMethod(swsys.getComboBoxSelected(cmbStartMtd));
        bean.setMceTag(txtTag.getText().trim());
        bean.setMceDescp(txtDesp.getText().trim());
        bean.setMceName(sname);
        int r = 0;
        MCEConfigDAO mcd = new MCEConfigDAO(up);
        if (bEdit)
        {
            r = mcd.update(bean);
        }
        else
        {
            r = (int) mcd.save(bean);
        }
        if (r == JDAO.OPERATE_SUCCESS)
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
        txtName = new javax.swing.JTextField();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        txtTag = new javax.swing.JTextField();
        tbrTagTools = new javax.swing.JToolBar();
        btnGeneTag = new javax.swing.JButton();
        btnCheckTag = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtDesp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbMaxTPS = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbStartMtd = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cmbInstLmt = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cmbEmptyMtd = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cmbWaitLong = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MCE配置");
        setResizable(false);

        jLabel1.setText("配置名称");

        jLabel2.setText("配置标签");

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

        jLabel3.setText("简要描述");

        jLabel4.setText("MaxTPS");

        jLabel5.setText("启动模式");

        jLabel6.setText("实例限制");

        jLabel7.setText("空闲模式");

        jLabel8.setText("等候周期");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDesp)
                            .addComponent(cmbMaxTPS, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbStartMtd, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbInstLmt, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbEmptyMtd, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbWaitLong, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbMaxTPS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbStartMtd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbInstLmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbEmptyMtd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cmbWaitLong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGeneTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnGeneTagActionPerformed
    {//GEN-HEADEREND:event_btnGeneTagActionPerformed
        geneTag();
    }//GEN-LAST:event_btnGeneTagActionPerformed

    private void btnCheckTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCheckTagActionPerformed
    {//GEN-HEADEREND:event_btnCheckTagActionPerformed
        checkTag(true);
    }//GEN-LAST:event_btnCheckTagActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        OK();
    }//GEN-LAST:event_btnOKActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnCheckTag;
    private javax.swing.JButton btnGeneTag;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox<String> cmbEmptyMtd;
    private javax.swing.JComboBox<String> cmbInstLmt;
    private javax.swing.JComboBox<String> cmbMaxTPS;
    private javax.swing.JComboBox<String> cmbStartMtd;
    private javax.swing.JComboBox<String> cmbWaitLong;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar tbrTagTools;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTag;
    // End of variables declaration//GEN-END:variables
}
