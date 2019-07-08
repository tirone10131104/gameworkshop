package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanFuncPropListItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanPropertyDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.tols.data.wakeup;
import java.awt.Toolkit;

/**
 *
 * @author Tirone
 */
public class DlgFuncPropItem extends javax.swing.JDialog
{

    private boolean bOK = false;
    private BeanFuncPropListItem bean = null;
    private boolean bEdit = false;
    private wakeup up = null;
    private BeanPropertyDefine bprop = null;
    private propertyDefine pdef = null;

    public DlgFuncPropItem(java.awt.Frame parent, boolean modal, wakeup _up, BeanFuncPropListItem _bean)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        pdef = new propertyDefine(up);
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        if (bean == null)
        {
            bean = new BeanFuncPropListItem();
            bEdit = false;
        }
        else
        {
            txtDesp.setText(bean.getDescription());
            txtValue.setText(bean.getValue() + "");
            fast.setCheckBoxValue(ckHide, bean.getHide());
            bprop = (BeanPropertyDefine) pdef.getRecordByID(bean.getPropOID());
            showPorp();
            btnSelProp.setEnabled(false);
        }
    }

    private void doSelectProp()
    {
        DlgPropertyDefSelector dlg = new DlgPropertyDefSelector(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            bprop = dlg.getSelectedProp();
            showPorp();
        }
        dlg.dispose();
        dlg = null;
    }

    private void showPorp()
    {
        if (bprop == null)
        {
            txtProp.setText("-");
        }
        else
        {
            txtProp.setText(bprop.getPropName() + "<" + bprop.getPropTag() + ">");
        }
    }

    private void doOK()
    {
        if (bprop == null)
        {
            fast.err("属性不能为空");
            return;
        }
        double dv = fast.testDoubleText(txtValue);
        if (dv == Double.MIN_VALUE)
        {
            fast.err("属性值格式不正确");
            return;
        }
        bean.setDescription(txtDesp.getText().trim());
        bean.setHide(fast.readCheckBox(ckHide));
        bean.setPropOID(bprop.getOID());
        bean.setValue(dv);
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

    public BeanFuncPropListItem getFuncPropListItem()
    {
        return bean;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ckHide = new javax.swing.JCheckBox();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        txtProp = new javax.swing.JTextField();
        btnSelProp = new javax.swing.JButton();
        txtValue = new javax.swing.JTextField();
        txtDesp = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("功能属性值设置");

        jLabel1.setText("属性目标");

        jLabel2.setText("属性数值");

        jLabel3.setText("描述文本");

        jLabel4.setText("是否隐藏");

        ckHide.setText("设置为隐藏项");

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

        txtProp.setEditable(false);

        btnSelProp.setText("选择属性");
        btnSelProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelPropActionPerformed(evt);
            }
        });

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
                        .addComponent(txtProp, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelProp))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ckHide)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValue))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDesp)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtProp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelProp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ckHide))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelPropActionPerformed
    {//GEN-HEADEREND:event_btnSelPropActionPerformed
        doSelectProp();
    }//GEN-LAST:event_btnSelPropActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        doOK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        doCancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSelProp;
    private javax.swing.JCheckBox ckHide;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtProp;
    private javax.swing.JTextField txtValue;
    // End of variables declaration//GEN-END:variables
}
