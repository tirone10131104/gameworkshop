package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.swingTools2.listItem;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.JDAO;
import dev.xling.jmdbs.BeanMDBSMainControl;
import dev.xling.jmdbs.MDBSMainControl;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;

public class IFrmMDBSMainControl extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private boolean bOpenEdit = false;
    private JDesktopPane desk = null;

    public IFrmMDBSMainControl(wakeup _up, JDesktopPane _desk)
    {
        initComponents();
        up = _up;
        desk = _desk;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        initGCTimeCombo();
        initScanTimeCombo();
        initDssProcThdCtCombo();
        loadMainCtrl();
        disAllCtrl();
    }

    private void loadMainCtrl()
    {
        MDBSMainControl mmc = new MDBSMainControl(up);
        BeanMDBSMainControl bean = mmc.getMDBSMainControl();
        if (bean != null)
        {
            txtGCTime.setText(bean.getMdbGCWait() + "");
            txtProcThdCt.setText(bean.getDssProcThdCount() + "");
            txtScanTime.setText(bean.getDssScanWait() + "");
        }
    }

    private void initGCTimeCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("30秒", 30));
        mod.addElement(new listItem("60秒", 60));
        mod.addElement(new listItem("120秒", 120));
        mod.addElement(new listItem("180秒", 180));
        mod.addElement(new listItem("240秒", 240));
        mod.addElement(new listItem("300秒", 300));
        cmbGCTime.setModel(mod);
    }

    private void initScanTimeCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("30秒", 30));
        mod.addElement(new listItem("60秒", 60));
        mod.addElement(new listItem("120秒", 120));
        mod.addElement(new listItem("180秒", 180));
        mod.addElement(new listItem("240秒", 240));
        mod.addElement(new listItem("300秒", 300));
        cmbScanTime.setModel(mod);
    }

    private void initDssProcThdCtCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("1个", 1));
        mod.addElement(new listItem("2个", 2));
        mod.addElement(new listItem("3个", 3));
        mod.addElement(new listItem("4个", 4));
        mod.addElement(new listItem("5个", 5));
        cmbProcThdCt.setModel(mod);
    }

    private void disAllCtrl()
    {
        txtGCTime.setEnabled(false);
        txtProcThdCt.setEnabled(false);
        txtScanTime.setEnabled(false);
        cmbGCTime.setEnabled(false);
        cmbProcThdCt.setEnabled(false);
        cmbScanTime.setEnabled(false);
        btnOK.setEnabled(false);
        btnCancle.setEnabled(false);
    }

    private void openEdit()
    {
        txtGCTime.setEnabled(true);
        txtProcThdCt.setEnabled(true);
        txtScanTime.setEnabled(true);
        cmbGCTime.setEnabled(true);
        cmbProcThdCt.setEnabled(true);
        cmbScanTime.setEnabled(true);
        btnOK.setEnabled(true);
        btnCancle.setEnabled(true);
        bOpenEdit = true;
    }

    private void save()
    {
        int iGCT = fast.testIntegerText(txtGCTime, 0);
        if (iGCT <= 0)
        {
            fast.warn("自动清理时间填写错误");
            return;
        }
        int iPCT = fast.testIntegerText(txtProcThdCt, 0);
        if (iPCT <= 0)
        {
            fast.warn("DSS处理线程数量错误");
            return;
        }
        int iSCT = fast.testIntegerText(txtScanTime, 0);
        if (iSCT <= 0)
        {
            fast.warn("DSS自动扫描时间错误");
            return;
        }
        //准备存储
        MDBSMainControl mmc = new MDBSMainControl(up);
        BeanMDBSMainControl bean = new BeanMDBSMainControl();
        bean.setDssProcThdCount(iPCT);
        bean.setDssScanWait(iSCT);
        bean.setMdbGCWait(iGCT);
        int r = mmc.setMainControl(bean);
        if (r == JDAO.OPERATE_SUCCESS)
        {
            fast.msg("保存设置操作完成");
            bOpenEdit = false;
            btnOK.setEnabled(false);
            disAllCtrl();
        }
        else
        {
            fast.err("设置操作失败", r);
        }
    }

    private void cancle()
    {
        if (bOpenEdit)
        {
            int sel = fast.ask("正在编辑配置项，是否取消？");
            if (sel != fast.YES)
            {
                return;
            }
        }
        loadMainCtrl();
        disAllCtrl();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtGCTime = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbGCTime = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtScanTime = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbScanTime = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtProcThdCt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbProcThdCt = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        btnOpenEdit = new javax.swing.JButton();

        setClosable(true);
        setTitle("MDBS主配置");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });

        btnCancle.setText("放弃");
        btnCancle.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancleActionPerformed(evt);
            }
        });

        btnOK.setText("保存");
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOKActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("虚拟数据库部分");

        jLabel2.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("异步实体化部分");

        jLabel3.setText("自动化清理时间间隔");

        txtGCTime.setText("60");

        jLabel4.setText("秒");

        cmbGCTime.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbGCTimeActionPerformed(evt);
            }
        });

        jLabel5.setText("自动实体扫描时间");

        txtScanTime.setText("60");

        jLabel6.setText("秒");

        cmbScanTime.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbScanTimeActionPerformed(evt);
            }
        });

        jLabel7.setText("异步线程个数");

        txtProcThdCt.setText("3");

        jLabel8.setText("个");

        cmbProcThdCt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbProcThdCtActionPerformed(evt);
            }
        });

        btnOpenEdit.setText("启用编辑");
        btnOpenEdit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOpenEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtGCTime, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                    .addComponent(txtScanTime)
                                    .addComponent(txtProcThdCt))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbScanTime, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbProcThdCt, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbGCTime, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnOpenEdit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
                                .addComponent(btnOK)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancle)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtGCTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cmbGCTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtScanTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cmbScanTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtProcThdCt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(cmbProcThdCt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK)
                    .addComponent(btnOpenEdit))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsMDBMainControl(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnOpenEditActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOpenEditActionPerformed
    {//GEN-HEADEREND:event_btnOpenEditActionPerformed
        openEdit();
    }//GEN-LAST:event_btnOpenEditActionPerformed

    private void cmbGCTimeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbGCTimeActionPerformed
    {//GEN-HEADEREND:event_cmbGCTimeActionPerformed
        txtGCTime.setText(fast.readCombo(cmbGCTime) + "");
    }//GEN-LAST:event_cmbGCTimeActionPerformed

    private void cmbScanTimeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbScanTimeActionPerformed
    {//GEN-HEADEREND:event_cmbScanTimeActionPerformed
        txtScanTime.setText(fast.readCombo(cmbScanTime) + "");
    }//GEN-LAST:event_cmbScanTimeActionPerformed

    private void cmbProcThdCtActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbProcThdCtActionPerformed
    {//GEN-HEADEREND:event_cmbProcThdCtActionPerformed
        txtProcThdCt.setText(fast.readCombo(cmbProcThdCt) + "");
    }//GEN-LAST:event_cmbProcThdCtActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        save();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnOpenEdit;
    private javax.swing.JComboBox<String> cmbGCTime;
    private javax.swing.JComboBox<String> cmbProcThdCt;
    private javax.swing.JComboBox<String> cmbScanTime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField txtGCTime;
    private javax.swing.JTextField txtProcThdCt;
    private javax.swing.JTextField txtScanTime;
    // End of variables declaration//GEN-END:variables
}
