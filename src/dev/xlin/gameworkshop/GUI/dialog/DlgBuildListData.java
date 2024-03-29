package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanBuildListItemData;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanKeyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanPropertyDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanSkillDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataBean;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.keyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;

public class DlgBuildListData extends javax.swing.JDialog
{

    private BeanBuildListItemData bean = null;
    private wakeup up = null;
    private boolean bOK = false;
    private boolean bEdit = false;
    private int targetType = 0;
    private int targetOID = 0;

    public DlgBuildListData(java.awt.Frame parent, boolean modal, wakeup _up, BeanBuildListItemData _bean)
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
        if (bean == null)
        {
            bean = new BeanBuildListItemData();
            targetType = swsys.getComboBoxSelected(cmbTarType);
            bEdit = false;
        }
        else
        {
            targetType = bean.getTargetType();
            targetOID = bean.getTargetOID();
            String sdtxt = guiCodes.makeTargetBeanDesp(up, targetType, targetOID);
            txtTarOID.setText(sdtxt);
            txtDesp.setText(bean.getDescription());
            txtQuantity.setText(bean.getQuantity() + "");
            swsys.doSelectCombo(cmbReqType, bean.getRequestType());
            swsys.doSelectCombo(cmbTarType, bean.getTargetType());
            fast.setCheckBoxValue(ckHide, bean.getHide());
        }
        makeCtrlsByTarType();
    }

    private void makeCombos()
    {
        DefaultComboBoxModel modReqType = new DefaultComboBoxModel();
        int[] idsReqs = constChk.getFinalInts(iConst.class, "TARGET_REQ_METD_");
        for (int i = 0; i < idsReqs.length; i++)
        {
            int id = idsReqs[i];
            listItem li = new listItem(iConst.translate(id), id);
            modReqType.addElement(li);
        }
        cmbReqType.setModel(modReqType);
        DefaultComboBoxModel modTarType = new DefaultComboBoxModel();
        int[] idsTars = constChk.getFinalInts(iConst.class, "DT_REQ_TARTYPE_");
        for (int i = 0; i < idsTars.length; i++)
        {
            int id = idsTars[i];
            listItem li = new listItem(iConst.translate(id), id);
            modTarType.addElement(li);
        }
        cmbTarType.setModel(modTarType);
    }

    private void selectTarget()
    {
        iDataBean idbean = guiCodes.selectTargetDataBean(targetType, up);
        if (idbean != null)
        {
            String s = "[" + iConst.translate(targetType) + "]" + idbean._getDataName() + "<" + idbean._getDataTag() + ">";
            txtTarOID.setText(s);
            if (targetType == iConst.DT_REQ_TARTYPE_SKILL)
            {
                BeanSkillDefine bskd = (BeanSkillDefine) idbean;
                txtQuantity.setText(bskd.getStatus() + "");
            }
            targetOID = idbean._getPKIDX();
        }
        makeCtrlsByTarType();
    }

    private void makeCtrlsByTarType()
    {
        if (targetType == iConst.DT_REQ_TARTYPE_SKILL)
        {
            txtQuantity.setEditable(false);
        }
        else
        {
            txtQuantity.setEditable(true);
        }
    }

    private void doOK()
    {
        if (targetOID == 0)
        {
            fast.warn("必须选择一个目标");
            return;
        }
        double dqt = fast.testDoubleText(txtQuantity);
        if (dqt == Double.MIN_VALUE)
        {
            fast.warn("必须填写正确的数值");
            return;
        }
        bean.setQuantity(dqt);
        bean.setDescription(txtDesp.getText());
        bean.setHide(fast.readCheckBox(ckHide));
        bean.setRequestType(swsys.getComboBoxSelected(cmbReqType));
        bean.setTargetType(swsys.getComboBoxSelected(cmbTarType));
        bean.setTargetOID(targetOID);
        bOK = true;
        setVisible(false);
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    private void doSelectTarType()
    {
        int tartp = swsys.getComboBoxSelected(cmbTarType);
        if (tartp != targetType)
        {
            int sel = fast.ask("是否切换目标类型？这个操作会清已选择的目标。");
            if (sel != fast.YES)
            {
                swsys.doSelectCombo(cmbTarType, targetType);
            }
            else
            {
                targetOID = 0;
                txtTarOID.setText("");
                targetType = tartp;
            }
        }
        makeCtrlsByTarType();
    }

    public boolean getOK()
    {
        return bOK;
    }

    public BeanBuildListItemData getBuildListItemData()
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDesp = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        txtTarOID = new javax.swing.JTextField();
        btnSelTarOID = new javax.swing.JButton();
        cmbReqType = new javax.swing.JComboBox<>();
        cmbTarType = new javax.swing.JComboBox<>();
        ckHide = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("建造数据");

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

        jLabel1.setText("需求方式");

        jLabel2.setText("目标类型");

        jLabel3.setText("指定目标");

        jLabel4.setText("需求数量");

        jLabel5.setText("描述文本");

        txtTarOID.setEditable(false);

        btnSelTarOID.setText("选择");
        btnSelTarOID.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelTarOIDActionPerformed(evt);
            }
        });

        cmbTarType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTarTypeActionPerformed(evt);
            }
        });

        ckHide.setText("设置为隐藏数据项");

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
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQuantity))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTarOID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelTarOID))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTarType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbReqType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ckHide)
                                .addGap(0, 363, Short.MAX_VALUE))
                            .addComponent(txtDesp))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbReqType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbTarType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTarOID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelTarOID))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckHide)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
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

    private void btnSelTarOIDActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelTarOIDActionPerformed
    {//GEN-HEADEREND:event_btnSelTarOIDActionPerformed
        selectTarget();
    }//GEN-LAST:event_btnSelTarOIDActionPerformed

    private void cmbTarTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTarTypeActionPerformed
    {//GEN-HEADEREND:event_cmbTarTypeActionPerformed
        doSelectTarType();
    }//GEN-LAST:event_cmbTarTypeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSelTarOID;
    private javax.swing.JCheckBox ckHide;
    private javax.swing.JComboBox<String> cmbReqType;
    private javax.swing.JComboBox<String> cmbTarType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtTarOID;
    // End of variables declaration//GEN-END:variables
}
