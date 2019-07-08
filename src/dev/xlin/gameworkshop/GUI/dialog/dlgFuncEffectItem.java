package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.beanFuncEffectListItem;
import dev.xlin.gameworkshop.progs.foundation.beans.beanKeyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanPropertyDefine;
import dev.xlin.gameworkshop.progs.foundation.keyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Tirone
 */
public class dlgFuncEffectItem extends javax.swing.JDialog
{

    private wakeup up = null;
    private beanFuncEffectListItem bean = null;
    private boolean bEdit = false;
    private boolean bOK = false;
    private int tartype = 0;
    private int tarid = 0;

    public dlgFuncEffectItem(java.awt.Frame parent, boolean modal, wakeup _up, beanFuncEffectListItem _bean)
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
            bean = new beanFuncEffectListItem();
            tartype = bean.getTargetType();
            tarid = bean.getTargetOID();
            swsys.doSelectCombo(cmbTarType, bean.getTargetType());
            bEdit = false;
        }
        else
        {
            cmbTarType.setEnabled(false);
            btnSelect.setEnabled(false);
            txtDesp.setText(bean.getDescription());
            txtValue.setText(bean.getEffectValue() + "");
            fast.setCheckBoxValue(ckHide, bean.getHide());
            tartype = bean.getTargetType();
            tarid = bean.getTargetOID();
            swsys.doSelectCombo(cmbMethod, bean.getValueMethod());
            swsys.doSelectCombo(cmbSameEftMethod, bean.getSameEffectMethod());
            swsys.doSelectCombo(cmbTarType, bean.getTargetType());
        }
        showTarget();
    }

    private void showTarget()
    {
        if (tartype == iConst.FUNC_EFT_TAR_KEY)
        {
            keyDataDefine kdef = new keyDataDefine(up);
            beanKeyDataDefine bkdd = (beanKeyDataDefine) kdef.getRecordByID(tarid);
            if (bkdd != null)
            {
                txtTarget.setText(bkdd.getKeyName() + "<" + bkdd.getKeyTag() + ">");
            }
            else
            {
                txtTarget.setText("键[null]");
            }
        }
        else if (tartype == iConst.FUNC_EFT_TAR_PROP)
        {
            propertyDefine pdef = new propertyDefine(up);
            beanPropertyDefine bpd = (beanPropertyDefine) pdef.getRecordByID(tarid);
            if (bpd != null)
            {
                txtTarget.setText(bpd.getPropName() + "<" + bpd.getPropTag() + ">");
            }
            else
            {
                txtTarget.setText("属性[null]");
            }
        }
        else
        {
            txtTarget.setText("-");
        }
    }

    private void makeCombo()
    {
        DefaultComboBoxModel modTar = new DefaultComboBoxModel();
        int[] idsTar = constChk.getFinalInts(iConst.class, "FUNC_EFT_TAR_");
        for (int i = 0; i < idsTar.length; i++)
        {
            int id = idsTar[i];
            listItem li = new listItem(iConst.translate(id), id);
            modTar.addElement(li);
        }
        cmbTarType.setModel(modTar);
        DefaultComboBoxModel modVmtd = new DefaultComboBoxModel();
        int[] idsVMTD = constChk.getFinalInts(iConst.class, "EFT_VALTYPE_");
        for (int i = 0; i < idsVMTD.length; i++)
        {
            int id = idsVMTD[i];
            listItem li = new listItem(iConst.translate(id), id);
            modVmtd.addElement(li);
        }
        cmbMethod.setModel(modVmtd);
        DefaultComboBoxModel modSMTD = new DefaultComboBoxModel();
        int[] idsSMTD = constChk.getFinalInts(iConst.class, "EFT_CFLT_MTD_");
        for (int i = 0; i < idsSMTD.length; i++)
        {
            int id = idsSMTD[i];
            listItem li = new listItem(iConst.translate(id), id);
            modSMTD.addElement(li);
        }
        cmbSameEftMethod.setModel(modSMTD);
    }

    private void doSelectTarget()
    {
        if (tartype == iConst.FUNC_EFT_TAR_KEY)
        {
            dlgKeyDataSelector dlg = new dlgKeyDataSelector(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanKeyDataDefine bkd = dlg.getSelectedKey();
                tarid = bkd.getOID();
                showTarget();
            }
            dlg.dispose();
            dlg = null;
        }
        else if (tartype == iConst.FUNC_EFT_TAR_PROP)
        {
            dlgPropertyDefSelector dlg = new dlgPropertyDefSelector(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanPropertyDefine bpd = dlg.getSelectedProp();
                tarid = bpd.getOID();
                showTarget();
            }
            dlg.dispose();
            dlg = null;
        }
    }

    private void doSelectTarType()
    {
        int sid = swsys.getComboBoxSelected(cmbTarType);
        if (sid != tartype)
        {
            int sel = fast.ask("是否确认将目标类型切换到 " + iConst.translate(sid) + "\n这个操作需要清除已选择的目标。");
            if (sel != fast.YES)
            {
                swsys.doSelectCombo(cmbTarType, tartype);
            }
            else
            {
                tartype = sid;
                tarid = 0;
                showTarget();
            }
        }
    }

    private void doOK()
    {
        if (tarid == 0)
        {
            fast.warn("必须选择一个目标");
            return;
        }
        double dv = fast.testDoubleText(txtValue);
        if (dv == Double.MIN_VALUE)
        {
            fast.warn("数值必须填写正确的格式");
            return;
        }
        bean.setDescription(txtDesp.getText().trim());
        bean.setTargetType(swsys.getComboBoxSelected(cmbTarType));
        bean.setTargetOID(tarid);
        bean.setSameEffectMethod(swsys.getComboBoxSelected(cmbSameEftMethod));
        bean.setValueMethod(swsys.getComboBoxSelected(cmbMethod));
        bean.setEffectValue(dv);
        bean.setHide(fast.readCheckBox(ckHide));
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

    public beanFuncEffectListItem getEffectListItem()
    {
        return bean;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtValue = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDesp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        ckHide = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbTarType = new javax.swing.JComboBox<>();
        txtTarget = new javax.swing.JTextField();
        btnSelect = new javax.swing.JButton();
        cmbMethod = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cmbSameEftMethod = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("功能效果值设置");
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

        jLabel2.setText("数据目标");

        jLabel3.setText("作用方法");

        jLabel4.setText("数据数值");

        ckHide.setText("设置为隐藏项");

        jLabel5.setText("描述文本");

        jLabel6.setText("是否隐藏");

        cmbTarType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbTarType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTarTypeActionPerformed(evt);
            }
        });

        txtTarget.setEditable(false);

        btnSelect.setText("选择");
        btnSelect.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelectActionPerformed(evt);
            }
        });

        cmbMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("同类方法");

        cmbSameEftMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbTarType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTarget, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelect))
                            .addComponent(cmbMethod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValue))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDesp))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ckHide)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbSameEftMethod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                    .addComponent(txtTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbSameEftMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(ckHide))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelectActionPerformed
    {//GEN-HEADEREND:event_btnSelectActionPerformed
        doSelectTarget();
    }//GEN-LAST:event_btnSelectActionPerformed

    private void cmbTarTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTarTypeActionPerformed
    {//GEN-HEADEREND:event_cmbTarTypeActionPerformed
        doSelectTarType();
    }//GEN-LAST:event_cmbTarTypeActionPerformed

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
    private javax.swing.JButton btnSelect;
    private javax.swing.JCheckBox ckHide;
    private javax.swing.JComboBox<String> cmbMethod;
    private javax.swing.JComboBox<String> cmbSameEftMethod;
    private javax.swing.JComboBox<String> cmbTarType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtTarget;
    private javax.swing.JTextField txtValue;
    // End of variables declaration//GEN-END:variables
}
