package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.beanKeyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanPropertyDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillLevel;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillUpgradeCondition;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataBean;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.keyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.gameworkshop.progs.foundation.skillDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author 刘祎鹏
 */
public class dlgSkillUpgradeCondition extends javax.swing.JDialog
{

    private boolean bEdit = false;
    private boolean bOk = false;
    private wakeup up = null;
    private beanSkillUpgradeCondition bean = null;
    private int tpid = 0;
    private int taroid = 0;

    public dlgSkillUpgradeCondition(java.awt.Frame parent, boolean modal, wakeup _up, beanSkillUpgradeCondition _bean)
    {
        super(parent, modal);
        initComponents();
        bean = _bean;
        up = _up;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        makeCombos();
        System.err.println("1");
        if (bean == null)
        {
            bean = new beanSkillUpgradeCondition();
            tpid = bean.getTargetType();
            swsys.doSelectCombo(cmbTarType, tpid);
            bEdit = false;
        }
        else
        {
            taroid = bean.getTargetOID();
            tpid = bean.getTargetType();
            txtDesp.setText(bean.getDescription());
            txtValue.setText(bean.getValue() + "");
            swsys.doSelectCombo(cmbTarType, tpid);
            swsys.doSelectCombo(cmbValueMethod, bean.getMethod());
            fast.setCheckBoxValue(ckHide, bean.getHide());
            bEdit = true;
        }
        makeCtrls();
        String stxt = guiCodes.makeTargetBeanDesp(up, tpid, taroid);
        txtTarOID.setText(stxt);
    }

    private void makeCtrls()
    {
        if (tpid == iConst.DT_REQ_TARTYPE_SKILL)
        {
            txtValue.setEditable(false);
        }
        else
        {
            txtValue.setEditable(true);
        }
    }

    private void makeCombos()
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
        //检查
        if (taroid == 0)
        {
            fast.warn("必须选择一个目标");
            return;
        }
        double dv = fast.testDoubleText(txtValue);
        if (dv == Double.MIN_VALUE)
        {
            fast.warn("必须填写正确的目标数量");
            return;
        }
        bean.setDescription(txtDesp.getText().trim());
        bean.setTargetOID(taroid);
        bean.setTargetType(tpid);
        bean.setHide(fast.readCheckBox(ckHide));
        bean.setValue(dv);
        bean.setMethod(swsys.getComboBoxSelected(cmbValueMethod));
        bOk = true;
        setVisible(false);
    }

    private void doCancle()
    {
        bean = null;
        bOk = false;
        setVisible(false);
    }

    public beanSkillUpgradeCondition getBean()
    {
        return bean;
    }

    private void doSelectTarget()
    {
        iDataBean idbean = guiCodes.selectTargetDataBean(tpid, up);
        if (idbean != null)
        {
            txtTarOID.setText("[" + iConst.translate(tpid) + "]" + idbean._getDataName() + "<" + idbean._getDataTag() + ">");
            taroid = idbean._getPKIDX();
            if (tpid == iConst.DT_REQ_TARTYPE_SKILL)
            {
                txtValue.setText(idbean._getDataStatus() +"");
            }
        }
        else
        {
            txtTarOID.setText("[" + iConst.translate(tpid) + "]null");
        }
    }

    private void doSelectTarType()
    {
        int osid = swsys.getComboBoxSelected(cmbTarType);
        if (osid != tpid)
        {
            int sel = fast.ask("是否确认切换目标类型，这个操作会清除掉原有的目标设置");
            if (sel != fast.YES)
            {
                swsys.doSelectCombo(cmbTarType, osid);
            }
            else
            {
                tpid = swsys.getComboBoxSelected(cmbTarType);
                txtValue.setText("0");
                txtTarOID.setText("");
                taroid = 0;
            }
        }
        makeCtrls();
    }

    public boolean getOK()
    {
        return bOk;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        txtDesp = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTarOID = new javax.swing.JTextField();
        btnSelectTar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cmbValueMethod = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtValue = new javax.swing.JTextField();
        btnCancle = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        ckHide = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbTarType = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("技能层级升级条件节点设置");

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

        btnCancle.setText("取消");
        btnCancle.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancleActionPerformed(evt);
            }
        });

        jLabel5.setText("隐藏数据");

        btnOK.setText("确定");
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOKActionPerformed(evt);
            }
        });

        ckHide.setText("设置为隐藏数据");

        jLabel1.setText("目标类型");

        jLabel6.setText("描述文本");

        cmbTarType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbTarType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTarTypeActionPerformed(evt);
            }
        });

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
                        .addGap(0, 323, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelectTarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelectTarActionPerformed
    {//GEN-HEADEREND:event_btnSelectTarActionPerformed
        doSelectTarget();
    }//GEN-LAST:event_btnSelectTarActionPerformed

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
