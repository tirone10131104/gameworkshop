package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfRegister;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanSkillDefine;
import dev.xlin.gameworkshop.progs.foundation.skillDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

/**
 *
 * @author 刘祎鹏
 */
public class DlgSkillDefine extends javax.swing.JDialog
{

    private boolean bOK = false;
    private wakeup up = null;
    private BeanSkillDefine bean = null;
    private int tpid = 0;
    private int parid = 0;
    private boolean bEdit = false;
    private skillDefine skDef = null;

    public DlgSkillDefine(java.awt.Frame parent, boolean modal, wakeup _up, BeanSkillDefine _bean, int _tpid, int _parid)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        tpid = _tpid;
        parid = _parid;
        skDef = new skillDefine(up);
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
            bean = new BeanSkillDefine();
            if (tpid != 0)
            {
                bean.setTypeOID(tpid);
                bean.setParentSkillOID(0);
            }
            if (parid != 0)
            {
                BeanSkillDefine bpar = (BeanSkillDefine) skDef.getRecordByID(parid);
                if (bpar != null)
                {
                    bean.setTypeOID(bpar.getTypeOID());
                    bean.setParentSkillOID(bpar.getOID());
                }
            }
            bEdit = false;
        }
        else
        {
            txtGetSkillImpl.setText(bean.getGetSkillImpl());
            txtGetJudgeImpl.setText(bean.getGetJudgeImpl());
            txtLevelChangeImpl.setText(bean.getLevelChangeImpl());
            txtLossSkillImpl.setText(bean.getLossSkillImpl());
            txtSkillDesp.setText(bean.getSkillDesp());
            txtSkillName.setText(bean.getSkillName());
            txtSkillTag.setText(bean.getSkillTag());
            txtSkillUpgrade.setText(bean.getUpgradeSkillImpl());
            txtUpgradeJudge.setText(bean.getUpgradeJudgeImpl());
            txtSkillTag.setEditable(false);
            fast.setCheckBoxValue(ckHide, bean.getHide());
            swsys.doSelectCombo(cmbSameData, bean.getSameDataMethod());
            swsys.doSelectCombo(cmbLevelInvoke, bean.getLevelInvokeType());
            swsys.doSelectCombo(cmbLevelType, bean.getLevelType());
            swsys.doSelectCombo(cmbSameData, bean.getSameDataMethod());
            bEdit = true;
        }
    }

    private void makeCombos()
    {
        DefaultComboBoxModel modLvTp = new DefaultComboBoxModel();
        int[] idsLvTp = constChk.getFinalInts(iConst.class, "SKL_LEVEL_TYPE_");
        for (int i = 0; i < idsLvTp.length; i++)
        {
            int id = idsLvTp[i];
            modLvTp.addElement(new listItem(iConst.translate(id), id));
        }
        cmbLevelType.setModel(modLvTp);
        DefaultComboBoxModel modInvk = new DefaultComboBoxModel();
        int[] idsInvk = constChk.getFinalInts(iConst.class, "SKL_INVK_TYPE_");
        for (int i = 0; i < idsInvk.length; i++)
        {
            int id = idsInvk[i];
            modInvk.addElement(new listItem(iConst.translate(id), id));
        }
        cmbLevelInvoke.setModel(modInvk);
        DefaultComboBoxModel modSmDt = new DefaultComboBoxModel();
        int[] idsSmDt = constChk.getFinalInts(iConst.class, "SKL_SAME_DATA_");
        for (int i = 0; i < idsSmDt.length; i++)
        {
            int id = idsSmDt[i];
            modSmDt.addElement(new listItem(iConst.translate(id), id));
        }
        cmbSameData.setModel(modSmDt);
        DefaultComboBoxModel modLvDt = new DefaultComboBoxModel();
        int[] idsLvDt = constChk.getFinalInts(iConst.class, "SKL_LV_DATA_");
        for (int i = 0; i < idsLvDt.length; i++)
        {
            int id = idsLvDt[i];
            modLvDt.addElement(new listItem(iConst.translate(id), id));
        }
        cmbLevelData.setModel(modLvDt);
    }

    private void OK()
    {
        bean.setGetJudgeImpl(txtGetJudgeImpl.getText().trim());
        bean.setGetSkillImpl(txtGetSkillImpl.getText().trim());
        bean.setHide(fast.readCheckBox(ckHide));
        bean.setLevelChangeImpl(txtLevelChangeImpl.getText().trim());
        bean.setLevelDataMethod(swsys.getComboBoxSelected(cmbLevelData));
        bean.setLevelInvokeType(swsys.getComboBoxSelected(cmbLevelInvoke));
        bean.setLevelType(swsys.getComboBoxSelected(cmbLevelType));
        bean.setLossSkillImpl(txtLossSkillImpl.getText().trim());
        bean.setSameDataMethod(swsys.getComboBoxSelected(cmbSameData));
        bean.setSkillDesp(txtSkillDesp.getText().trim());
        bean.setSkillName(txtSkillName.getText().trim());
        bean.setSkillTag(txtSkillTag.getText().trim());
        bean.setUpgradeJudgeImpl(txtUpgradeJudge.getText().trim());
        bean.setUpgradeSkillImpl(txtSkillUpgrade.getText().trim());
        int r = 0;
        if (bEdit == false)
        {
            r = skDef.createRecord(bean, false);
        }
        else
        {
            r = skDef.modifyRecord(bean, false);
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

    private void cancle()
    {
        bOK = false;
        setVisible(false);
    }

    public boolean getOK()
    {
        return bOK;
    }

    private void selectImplement(JTextField txt)
    {
        DlgSelectInterfaceImplements dlg = new DlgSelectInterfaceImplements(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanProgIntfRegister bpir = dlg.getSelectedRegister();
            txt.setText(bpir.getRegTag());
        }
        dlg.dispose();
        dlg = null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtSkillName = new javax.swing.JTextField();
        txtSkillTag = new javax.swing.JTextField();
        txtSkillDesp = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtGetJudgeImpl = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtGetSkillImpl = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtLevelChangeImpl = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtLossSkillImpl = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtUpgradeJudge = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtSkillUpgrade = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cmbLevelType = new javax.swing.JComboBox<>();
        cmbLevelInvoke = new javax.swing.JComboBox<>();
        cmbLevelData = new javax.swing.JComboBox<>();
        cmbSameData = new javax.swing.JComboBox<>();
        ckHide = new javax.swing.JCheckBox();
        btnSelGetJudge = new javax.swing.JButton();
        btnSelGetSkill = new javax.swing.JButton();
        btnSelLevelChange = new javax.swing.JButton();
        btnSelLossSkill = new javax.swing.JButton();
        btnSelUpgradeJudge = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("技能信息");
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

        jLabel14.setText("升级技能");

        jLabel1.setText("技能名称");

        txtGetJudgeImpl.setEditable(false);

        jLabel2.setText("技能标签");

        txtGetSkillImpl.setEditable(false);

        jLabel3.setText("技能描述");

        txtLevelChangeImpl.setEditable(false);

        jLabel4.setText("分级调用");

        txtLossSkillImpl.setEditable(false);

        jLabel5.setText("分级数据");

        txtUpgradeJudge.setEditable(false);

        jLabel6.setText("同类数据");

        txtSkillUpgrade.setEditable(false);

        jLabel7.setText("分级类型");

        jLabel8.setText("是否隐藏");

        jLabel9.setText("获取判断");

        jLabel10.setText("获取技能");

        jLabel11.setText("分级变动");

        jLabel12.setText("失去技能");

        jLabel13.setText("升级判断");

        cmbLevelType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbLevelInvoke.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbLevelData.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbSameData.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ckHide.setText("设置为隐藏");

        btnSelGetJudge.setText("选择");
        btnSelGetJudge.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelGetJudgeActionPerformed(evt);
            }
        });

        btnSelGetSkill.setText("选择");
        btnSelGetSkill.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelGetSkillActionPerformed(evt);
            }
        });

        btnSelLevelChange.setText("选择");
        btnSelLevelChange.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelLevelChangeActionPerformed(evt);
            }
        });

        btnSelLossSkill.setText("选择");
        btnSelLossSkill.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelLossSkillActionPerformed(evt);
            }
        });

        btnSelUpgradeJudge.setText("选择");
        btnSelUpgradeJudge.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelUpgradeJudgeActionPerformed(evt);
            }
        });

        jButton6.setText("选择");
        jButton6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton6ActionPerformed(evt);
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
                        .addComponent(txtSkillName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSkillTag))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSkillDesp))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbLevelType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbLevelInvoke, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbLevelData, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbSameData, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGetJudgeImpl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelGetJudge))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLevelChangeImpl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelLevelChange))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUpgradeJudge)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelUpgradeJudge))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ckHide)
                        .addGap(0, 347, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSkillUpgrade))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLossSkillImpl))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGetSkillImpl)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSelGetSkill, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSelLossSkill, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSkillName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSkillTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtSkillDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbLevelType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbLevelInvoke, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbLevelData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbSameData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(ckHide))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtGetJudgeImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelGetJudge))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtGetSkillImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelGetSkill))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtLevelChangeImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelLevelChange))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtLossSkillImpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelLossSkill))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtUpgradeJudge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelUpgradeJudge))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtSkillUpgrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        OK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void btnSelGetJudgeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelGetJudgeActionPerformed
    {//GEN-HEADEREND:event_btnSelGetJudgeActionPerformed
        selectImplement(txtGetJudgeImpl);
    }//GEN-LAST:event_btnSelGetJudgeActionPerformed

    private void btnSelGetSkillActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelGetSkillActionPerformed
    {//GEN-HEADEREND:event_btnSelGetSkillActionPerformed
        selectImplement(txtGetSkillImpl);
    }//GEN-LAST:event_btnSelGetSkillActionPerformed

    private void btnSelLevelChangeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelLevelChangeActionPerformed
    {//GEN-HEADEREND:event_btnSelLevelChangeActionPerformed
        selectImplement(txtLevelChangeImpl);
    }//GEN-LAST:event_btnSelLevelChangeActionPerformed

    private void btnSelLossSkillActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelLossSkillActionPerformed
    {//GEN-HEADEREND:event_btnSelLossSkillActionPerformed
        selectImplement(txtLossSkillImpl);
    }//GEN-LAST:event_btnSelLossSkillActionPerformed

    private void btnSelUpgradeJudgeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelUpgradeJudgeActionPerformed
    {//GEN-HEADEREND:event_btnSelUpgradeJudgeActionPerformed
        selectImplement(txtUpgradeJudge);
    }//GEN-LAST:event_btnSelUpgradeJudgeActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
    {//GEN-HEADEREND:event_jButton6ActionPerformed
        selectImplement(txtSkillUpgrade);
    }//GEN-LAST:event_jButton6ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSelGetJudge;
    private javax.swing.JButton btnSelGetSkill;
    private javax.swing.JButton btnSelLevelChange;
    private javax.swing.JButton btnSelLossSkill;
    private javax.swing.JButton btnSelUpgradeJudge;
    private javax.swing.JCheckBox ckHide;
    private javax.swing.JComboBox<String> cmbLevelData;
    private javax.swing.JComboBox<String> cmbLevelInvoke;
    private javax.swing.JComboBox<String> cmbLevelType;
    private javax.swing.JComboBox<String> cmbSameData;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtGetJudgeImpl;
    private javax.swing.JTextField txtGetSkillImpl;
    private javax.swing.JTextField txtLevelChangeImpl;
    private javax.swing.JTextField txtLossSkillImpl;
    private javax.swing.JTextField txtSkillDesp;
    private javax.swing.JTextField txtSkillName;
    private javax.swing.JTextField txtSkillTag;
    private javax.swing.JTextField txtSkillUpgrade;
    private javax.swing.JTextField txtUpgradeJudge;
    // End of variables declaration//GEN-END:variables
}
