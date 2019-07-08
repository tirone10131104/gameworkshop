package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanPropertyData;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanPropertyDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.tols.data.wakeup;
import java.awt.Toolkit;

public class DlgPropertyData extends javax.swing.JDialog
{

    private boolean bOK = false;
    private wakeup up = null;
    private BeanPropertyData bean = null;
    private boolean bEdit = false;
    private int propID = 0;

    public DlgPropertyData(java.awt.Frame parent, boolean modal, wakeup _up, BeanPropertyData _bean)
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
        if (bean == null)
        {
            bean = new BeanPropertyData();
            bEdit = false;
        }
        else
        {
            bEdit = true;
            txtDesp.setText(bean.getPropDescription());
            guiCodes.setCheckBoxSelection(bean.getHide(), ckHide);
            txtValue.setText(bean.getPropValue());
            propertyDefine pdef = new propertyDefine(up);
            BeanPropertyDefine bpdef = (BeanPropertyDefine) pdef.getRecordByID(bean.getPropID());
            txtProp.setText(bpdef.getPropName() + "<" + bpdef.getPropTag() + ">");
            propID = bpdef.getOID();
            btnSelectProp.setEnabled(false);
        }
    }

    private void doOK()
    {
        if (propID == 0)
        {
            fast.warn("必须选择一个属性");
            return ;
        }
        if (bEdit == false)
        {
            bean.setPropID(propID);
        }
        bean.setPropDescription(txtDesp.getText().trim());
        bean.setPropValue(txtValue.getText().trim());
        bean.setHide(guiCodes.getCheckBoxSelection(ckHide));
        bOK = true;
        setVisible(false);
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    private void doSelectProperty()
    {
        DlgPropertyDefSelector dlg = new DlgPropertyDefSelector(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanPropertyDefine bpd = dlg.getSelectedProp();
            propID = bpd.getOID();
            txtProp.setText(bpd.getPropName() +"<"+bpd.getPropTag()+">");
        }
        dlg.dispose();
        dlg = null;
    }

    public boolean getOK()
    {
        return bOK;
    }
    
    public BeanPropertyData getPropertyData ()
    {
        return bean;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        txtProp = new javax.swing.JTextField();
        btnSelectProp = new javax.swing.JButton();
        ckHide = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        txtDesp = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtValue = new javax.swing.JTextField();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("属性值设置");
        setResizable(false);

        jLabel1.setText("属性名称");

        txtProp.setEditable(false);

        btnSelectProp.setText("选择");
        btnSelectProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelectPropActionPerformed(evt);
            }
        });

        ckHide.setText("作为隐藏属性");

        jLabel2.setText("描述文本");

        jLabel3.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("属性值");

        txtValue.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        txtValue.setForeground(new java.awt.Color(255, 0, 0));

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDesp))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtProp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelectProp))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ckHide)
                                .addGap(0, 231, Short.MAX_VALUE))
                            .addComponent(txtValue)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtProp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectProp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckHide)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 159, Short.MAX_VALUE)
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

    private void btnSelectPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelectPropActionPerformed
    {//GEN-HEADEREND:event_btnSelectPropActionPerformed
        doSelectProperty();
    }//GEN-LAST:event_btnSelectPropActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSelectProp;
    private javax.swing.JCheckBox ckHide;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtProp;
    private javax.swing.JTextField txtValue;
    // End of variables declaration//GEN-END:variables
}
