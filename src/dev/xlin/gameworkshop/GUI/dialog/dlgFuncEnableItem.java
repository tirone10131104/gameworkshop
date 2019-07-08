package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanFuncEnableListItem;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataBean;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;

/**
 * 功能启动项的设置对话框
 *
 * @author Administrator
 */
public class dlgFuncEnableItem extends javax.swing.JDialog
{

    private beanFuncEnableListItem bean = null;
    private wakeup up = null;
    private boolean bOK = false;
    private boolean bEdit = false;
    private int tarType = 0;
    private int tarID = 0;

    public dlgFuncEnableItem(java.awt.Frame parent, boolean modal, wakeup _up, beanFuncEnableListItem _bean)
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
        makeCombo();
        if (bean == null)
        {
            bean = new beanFuncEnableListItem();
            bEdit = false;
            tarID = bean.getTargetOID();
            tarType = bean.getTargetType();
            String sr = guiCodes.makeTargetBeanDesp(up, tarType, tarID);
            txtTarOID.setText(sr);
        }
        else
        {
            tarID = bean.getTargetOID();
            tarType = bean.getTargetType();
            txtValue.setText(bean.getValue() + "");
            fast.setCheckBoxValue(ckHide, bean.getHide());
            txtDesp.setText(bean.getDescription());
            String sr = guiCodes.makeTargetBeanDesp(up, tarType, tarID);
            txtTarOID.setText(sr);
            cmbTarType.setEnabled(false);
            btnSelectTar.setEnabled(false);
            bEdit = true;
        }
        swsys.doSelectCombo(cmbTarType, bean.getTargetType());
        swsys.doSelectCombo(cmbValueMethod, bean.getMethod());
        makeCtrlsByTarType();
    }

    private void makeCombo()
    {
        DefaultComboBoxModel modMethod = new DefaultComboBoxModel();
        int[] idsMtd = constChk.getFinalInts(iConst.class, "TARGET_REQ_METD_");
        for (int i = 0; i < idsMtd.length; i++)
        {
            int id = idsMtd[i];
            modMethod.addElement(new listItem(iConst.translate(id), id));
        }
        cmbValueMethod.setModel(modMethod);
        DefaultComboBoxModel modTarType = new DefaultComboBoxModel();
        int[] idsTTP = constChk.getFinalInts(iConst.class, "DT_REQ_TARTYPE_");
        for (int i = 0; i < idsTTP.length; i++)
        {
            int id = idsTTP[i];
            modTarType.addElement(new listItem(iConst.translate(id), id));
        }
        cmbTarType.setModel(modTarType);
    }

    private void doOK()
    {
        double dv = fast.testDoubleText(txtValue);
        if (dv == Double.MIN_VALUE)
        {
            fast.warn("需要输入正确的数值");
            return;
        }
        if (tarID == 0)
        {
            fast.warn("需要选择目标");
            return;
        }
        bean.setTargetOID(tarID);
        bean.setTargetType(tarType);
        bean.setHide(fast.readCheckBox(ckHide));
        bean.setMethod(swsys.getComboBoxSelected(cmbValueMethod));
        bean.setValue(dv);
        bean.setDescription(txtDesp.getText().trim());
        bOK = true;
        setVisible(false);
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

    public beanFuncEnableListItem getFuncEnableListItem()
    {
        return bean;
    }

    private void doSelectTarType()
    {
        if (tarType != swsys.getComboBoxSelected(cmbTarType))
        {
            int sel = fast.ask("是否确认要更改目标类型，这个操作会同时清除已选择的目标");
            if (sel != fast.YES)
            {
                swsys.doSelectCombo(cmbTarType, tarType);
            }
            else
            {
                tarID = 0;
                tarType = swsys.getComboBoxSelected(cmbTarType);
            }
        }
        showTarget();
        makeCtrlsByTarType();
    }
    
    private void showTarget ()
    {
        String stxt = guiCodes.makeTargetBeanDesp(up, tarType, tarID);
        txtTarOID.setText(stxt);
    }

    private void selectTarget()
    {
        iDataBean idbean = guiCodes.selectTargetDataBean(tarType, up);
        if (idbean != null)
        {
            String s = "[" + iConst.translate(tarType) + "]" + idbean._getDataName() + "<" + idbean._getDataTag() + ">";
            txtTarOID.setText(s);
            if (tarType == iConst.DT_REQ_TARTYPE_SKILL)
            {
                beanSkillDefine bskd = (beanSkillDefine) idbean;
                txtValue.setText(bskd.getStatus() + "");
            }
            tarID = idbean._getPKIDX();
        }
        makeCtrlsByTarType();
    }

    private void makeCtrlsByTarType()
    {
        if (tarType == iConst.DT_REQ_TARTYPE_SKILL)
        {
            txtValue.setEditable(false);
        }
        else
        {
            txtValue.setEditable(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbTarType = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtTarOID = new javax.swing.JTextField();
        btnSelectTar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cmbValueMethod = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtValue = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        ckHide = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        txtDesp = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("功能启动参数设置");
        setResizable(false);

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

        jLabel1.setText("目标类型");

        cmbTarType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbTarType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTarTypeActionPerformed(evt);
            }
        });

        jLabel2.setText("目标数据");

        txtTarOID.setEditable(false);

        btnSelectTar.setText("选择");
        btnSelectTar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelectTarActionPerformed(evt);
            }
        });

        jLabel3.setText("方法类型");

        cmbValueMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("目标数值");

        jLabel5.setText("隐藏数据");

        ckHide.setText("设置为隐藏数据");

        jLabel6.setText("描述文本");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTarType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTarOID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelectTar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbValueMethod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValue))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ckHide)
                        .addGap(0, 223, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDesp)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbTarType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTarOID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectTar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbValueMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(ckHide))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
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

    private void cmbTarTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTarTypeActionPerformed
    {//GEN-HEADEREND:event_cmbTarTypeActionPerformed
        doSelectTarType();
    }//GEN-LAST:event_cmbTarTypeActionPerformed

    private void btnSelectTarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelectTarActionPerformed
    {//GEN-HEADEREND:event_btnSelectTarActionPerformed
        selectTarget();
    }//GEN-LAST:event_btnSelectTarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSelectTar;
    private javax.swing.JCheckBox ckHide;
    private javax.swing.JComboBox<String> cmbTarType;
    private javax.swing.JComboBox<String> cmbValueMethod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtTarOID;
    private javax.swing.JTextField txtValue;
    // End of variables declaration//GEN-END:variables
}
