package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanEffectData;
import dev.xlin.gameworkshop.progs.foundation.beans.beanPropertyDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.beanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;

public class dlgEffectData extends javax.swing.JDialog
{

    private wakeup up = null;
    private int coid = 0;
    private beanEffectData bean = null;
    private boolean bEdit = false;
    private boolean bOK = false;
    private int targetOID = 0;
    private int propertyOID = 0;
    private int tarType = 0;
    private boolean bInitDone = false;

    public dlgEffectData(java.awt.Frame parent, boolean modal, wakeup _up, beanEffectData _bean, int _coid)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        coid = _coid;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        makeCombos();
        if (bean == null)
        {
            bEdit = false;
            bean = new beanEffectData();
            tarType = swsys.getComboBoxSelected(cmbTargetType);
        }
        else
        {
            bEdit = true;
            tarType = bean.getTargetType();
            propertyOID= bean.getPropID();
            targetOID = bean.getTargetOID();
            swsys.doSelectCombo(cmbTargetType, tarType);
            swsys.doSelectCombo(cmbValueType, bean.getValueType());
            fast.setCheckBoxValue(ckHide, bean.getHide());
            txtDescp.setText(bean.getDescription());
            txtValue.setText(bean.getEffectValue()+"");
            if (bean.getTargetOID()!= 0)
            {
                if (bean.getTargetType() == iConst.EFT_TYPE_OBJECT_CLASS)
                {
                    objectClassDefine ocd = new objectClassDefine( up);
                    beanObjectClass boc = (beanObjectClass) ocd.getRecordByID(bean.getTargetOID());
                    if (boc != null)
                    {
                        txtTargetOID.setText("[物类]" + boc.getClassName() +"<"+boc.getClassTag()+">");
                    }
                    else
                    {
                        txtTargetOID.setText("[null]");
                    }
                }
                else if (bean.getTargetType() == iConst.EFT_TYPE_ITEM)
                {
                    itemDefine idef = new itemDefine(up);
                    beanItem bit = (beanItem) idef.getRecordByID(bean.getTargetOID());
                    if (bit != null)
                    {
                        txtTargetOID.setText("[物体]" + bit.getItemName() +"<"+bit.getItemTag()+">");
                    }
                    else
                    {
                        txtTargetOID.setText("[null]");
                    }
                }
            }
            else
            {
                txtTargetOID.setText("");
            }
            if(bean.getPropID() != 0)
            {
                propertyDefine pdef = new propertyDefine (up);
                beanPropertyDefine bpd = (beanPropertyDefine) pdef.getRecordByID(bean.getPropID());
                if (bpd != null)
                {
                    txtPropOID.setText(bpd.getPropName() +"<"+bpd.getPropTag()+">");
                }
                else
                {
                    txtPropOID.setText("[null]");
                }
            }
            else
            {
                txtPropOID.setText("");
            }
        }
        bInitDone = true;
    }

    private void makeCombos()
    {
        DefaultComboBoxModel cbmTarType = new DefaultComboBoxModel();
        int[] idsTarType = constChk.getFinalInts(iConst.class, "EFT_TYPE_");
        for (int i = 0; i < idsTarType.length; i++)
        {
            int id = idsTarType[i];
            listItem li = new listItem(iConst.translate(id), id);
            cbmTarType.addElement(li);
        }
        cmbTargetType.setModel(cbmTarType);
        DefaultComboBoxModel cbmValType = new DefaultComboBoxModel();
        int[] idsValType = constChk.getFinalInts(iConst.class, "EFT_VALTYPE_");
        for (int i = 0; i < idsValType.length; i++)
        {
            int id = idsValType[i];
            listItem li = new listItem(iConst.translate(id), id);
            cbmValType.addElement(li);
        }
        cmbValueType.setModel(cbmValType);
    }

    private void doOK()
    {
        //检查项
        int itarType = swsys.getComboBoxSelected(cmbTargetType);
        if (itarType == iConst.EFT_TYPE_ITEM || itarType == iConst.EFT_TYPE_OBJECT_CLASS)
        {
            if (targetOID == 0)
            {
                fast.warn("必须选择一个目标");
                return;
            }
        }
        if (propertyOID == 0)
        {
            fast.warn("必须选择一个属性作为效果的目标参数");
            return;
        }
        if (txtDescp.getText().trim().equals(""))
        {
            fast.warn("必须填写一个效果说明文本");
            return;
        }
        double d = fast.testDoubleText(txtValue);
        if (d == Double.MIN_VALUE)
        {
            fast.warn("必须填写一个效果数值");
            return;
        }
        bean.setTargetType(itarType);
        bean.setHide(guiCodes.getCheckBoxSelection(ckHide));
        bean.setDescription(txtDescp.getText().trim());
        bean.setEffectValue(fast.testDoubleText(txtValue));
        bean.setValueType(swsys.getComboBoxSelected(cmbValueType));
        bean.setTargetOID(targetOID);
        bean.setPropID(propertyOID);
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

    private int getSelectedTarType()
    {
        listItem lsel = (listItem) cmbTargetType.getSelectedItem();
        if (lsel == null)
        {
            return -1;
        }
        else
        {
            return lsel.getNodeOID();
        }
    }

    private void doSelectTarObject()
    {
        int sid = getSelectedTarType();
        if (sid != iConst.EFT_TYPE_OBJECT_CLASS && sid != iConst.EFT_TYPE_ITEM)
        {
            fast.warn("当前目标类型并不需要选择目标。");
            return;
        }
        if (sid == iConst.EFT_TYPE_OBJECT_CLASS)
        {
            dlgSelectObjectClass dlg = new dlgSelectObjectClass(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanObjectClass boc = dlg.getSelectedObjectClass();
                txtTargetOID.setText("[物类]" + boc.getClassName() + "<" + boc.getClassTag() + ">");
                targetOID = boc.getOID();
            }
            dlg.dispose();
            dlg = null;
        }
        else if (sid == iConst.EFT_TYPE_ITEM)
        {
            dlgSelectItemDefine dlg = new dlgSelectItemDefine(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanItem bse = dlg.getSelectedItemDefine();
                txtTargetOID.setText("[装备]" + bse.getItemName() + "<" + bse.getItemTag() + ">");
                targetOID = bse.getOID();
            }
            dlg.dispose();
            dlg = null;
        }
    }

    private void doSelectProp()
    {
        dlgPropertyDefSelector dlg = new dlgPropertyDefSelector(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            beanPropertyDefine bpd = dlg.getSelectedProp();
            txtPropOID.setText(bpd.getPropName() + "<" + bpd.getPropTag() + ">");
            propertyOID = bpd.getOID();
        }
        dlg.dispose();
        dlg = null;
    }

    public beanEffectData getEffectDataBean()
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

    private void doSelValType()
    {
        if (bInitDone)
        {
            int ivtp = swsys.getComboBoxSelected(cmbTargetType);
            if (ivtp != tarType)
            {
                int sel = fast.ask("是否切换目标类型？\n切换以后，将会清除效果目标。");
                if (sel == fast.YES)
                {
                    txtTargetOID.setText("");
                    targetOID = 0;
                }
                else
                {
                    swsys.doSelectCombo(cmbTargetType, tarType);
                }
            }
        }
        tarType = swsys.getComboBoxSelected(cmbTargetType);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbTargetType = new javax.swing.JComboBox<>();
        txtPropOID = new javax.swing.JTextField();
        cmbValueType = new javax.swing.JComboBox<>();
        txtValue = new javax.swing.JTextField();
        txtDescp = new javax.swing.JTextField();
        ckHide = new javax.swing.JCheckBox();
        btnSelectProp = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtTargetOID = new javax.swing.JTextField();
        btnSelectTarget = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("特效值设置");
        setResizable(false);

        jLabel1.setText("特效范围");

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

        jLabel3.setText("目标属性");

        jLabel4.setText("增效方式");

        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("特效数值");

        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("描述文本");

        cmbTargetType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTargetTypeActionPerformed(evt);
            }
        });

        txtPropOID.setEditable(false);

        txtValue.setForeground(new java.awt.Color(255, 0, 0));

        txtDescp.setForeground(new java.awt.Color(255, 0, 0));

        ckHide.setText("隐藏特效");

        btnSelectProp.setText("选择");
        btnSelectProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelectPropActionPerformed(evt);
            }
        });

        jLabel2.setText("特效目标");

        txtTargetOID.setEditable(false);

        btnSelectTarget.setText("选择");
        btnSelectTarget.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelectTargetActionPerformed(evt);
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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTargetType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTargetOID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelectTarget)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescp)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValue)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbValueType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(ckHide)
                                .addGap(0, 466, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPropOID)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelectProp)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbTargetType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTargetOID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectTarget))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPropOID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectProp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbValueType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDescp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckHide)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
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

    private void btnSelectTargetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelectTargetActionPerformed
    {//GEN-HEADEREND:event_btnSelectTargetActionPerformed
        doSelectTarObject();
    }//GEN-LAST:event_btnSelectTargetActionPerformed

    private void btnSelectPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelectPropActionPerformed
    {//GEN-HEADEREND:event_btnSelectPropActionPerformed
        doSelectProp();
    }//GEN-LAST:event_btnSelectPropActionPerformed

    private void cmbTargetTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTargetTypeActionPerformed
    {//GEN-HEADEREND:event_cmbTargetTypeActionPerformed
        doSelValType();
    }//GEN-LAST:event_cmbTargetTypeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSelectProp;
    private javax.swing.JButton btnSelectTarget;
    private javax.swing.JCheckBox ckHide;
    private javax.swing.JComboBox<String> cmbTargetType;
    private javax.swing.JComboBox<String> cmbValueType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtDescp;
    private javax.swing.JTextField txtPropOID;
    private javax.swing.JTextField txtTargetOID;
    private javax.swing.JTextField txtValue;
    // End of variables declaration//GEN-END:variables
}
