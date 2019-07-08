package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfRegister;
import dev.xlin.gameworkshop.progs.foundation.interfaceDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaceRegister;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.awt.Toolkit;

public class dlgProgIntfReg extends javax.swing.JDialog
{

    private wakeup up = null;
    private beanProgIntfRegister bean = null;
    private int defOID = 0;
    private boolean bOK = false;
    private boolean bEdit = false;

    public dlgProgIntfReg(java.awt.Frame parent, boolean modal, wakeup _up, beanProgIntfRegister _bean, int _defOID)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        defOID = _defOID;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        if (bean == null)
        {
            bean = new beanProgIntfRegister();
            bEdit = false;
            bean.setDefOID(defOID);
            interfaceDefine idef = new interfaceDefine(up);
            beanProgIntfDefine bdef = (beanProgIntfDefine) idef.getRecordByID(defOID);
            txtTag.setText(bdef.getIntfTag() + "_");
            geneTag();
        }
        else
        {
            txtRegName.setText(bean.getRegName());
            txtDesp.setText(bean.getRegDescription());
            txtAddress.setText(bean.getImplAddress());
            txtTag.setText(bean.getRegTag());
            fast.setCheckBoxValue(ckPreLoad, bean.getPreLoad());
            bEdit = true;
            txtTag.setEditable(false);
        }
    }

    private void doOK()
    {
        String stg = txtTag.getText().trim();
        String sadd = txtAddress.getText().trim();
        if (stg.equals(""))
        {
            fast.warn("标签不能为空");
            return;
        }
        if (sadd.equals(""))
        {
            fast.warn("地址不能为空");
            return;
        }
        bean.setImplAddress(sadd);
        bean.setRegTag(stg);
        bean.setRegDescription(txtDesp.getText().trim());
        bean.setPreLoad(fast.readCheckBox(ckPreLoad));
        bean.setRegName(txtRegName.getText().trim());
        int r = 0;
        interfaceRegister ireg = new interfaceRegister(up);
        if (bEdit == false)
        {
            r = ireg.createRecord(bean, false);
        }
        else
        {
            r = ireg.modifyRecord(bean, false);
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

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    public boolean getOK()
    {
        return bOK;
    }

    private void geneTag()
    {
        String stg = tagCreator.createDataTag(up, "tb_prog_interface_register", "regTag", 4, 5);
        txtTag.setText(stg);
    }

    private boolean checkTag(boolean rpsc)
    {
        String stg = txtTag.getText().trim();
        if (stg.equals(""))
        {
            fast.warn("标签不能为空");
            return false;
        }
        interfaceRegister irg = new interfaceRegister(up);
        if (irg.getRegisterByTag(stg) != null)
        {
            fast.warn("标签已经被占用");
            return false;
        }
        if (rpsc)
        {
            fast.msg("经过检查，这个标签可以被使用");
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnOK = new javax.swing.JButton();
        btnCancle = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtTag = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        ckPreLoad = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        txtRegName = new javax.swing.JTextField();
        tbrTagTools = new javax.swing.JToolBar();
        btnGeneTag = new javax.swing.JButton();
        btnCheckTag = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDesp = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("接口程序实现设置");
        setResizable(false);

        btnOK.setText("确定");
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOKActionPerformed(evt);
            }
        });

        btnCancle.setText("取消");
        btnCancle.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancleActionPerformed(evt);
            }
        });

        jLabel1.setText("程序描述");

        jLabel2.setText("程序标签");

        txtTag.setToolTipText("");

        jLabel3.setText("程序地址");

        txtAddress.setToolTipText("");

        ckPreLoad.setText("预创建接口");

        jLabel4.setText("程序命名");

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

        txtDesp.setColumns(20);
        txtDesp.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        txtDesp.setLineWrap(true);
        txtDesp.setRows(5);
        txtDesp.setBorder(null);
        jScrollPane1.setViewportView(txtDesp);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRegName))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
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
                        .addComponent(txtAddress))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ckPreLoad)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtRegName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckPreLoad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    private javax.swing.JCheckBox ckPreLoad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar tbrTagTools;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextArea txtDesp;
    private javax.swing.JTextField txtRegName;
    private javax.swing.JTextField txtTag;
    // End of variables declaration//GEN-END:variables
}
