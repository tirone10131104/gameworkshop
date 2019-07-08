package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanFuncControl;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfRegister;
import dev.xlin.gameworkshop.progs.foundation.interfaceDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaceRegister;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.codeTools;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Administrator
 */
public class DlgFuncControlNode extends javax.swing.JDialog
{

    private wakeup up = null;
    private BeanFuncControl bean = null;
    private boolean bOK = false;
    private boolean bEdit = false;
    private interfaceRegister intfreg = new interfaceRegister(up);
    private interfaceDefine intfdef = new interfaceDefine(up);

    public DlgFuncControlNode(java.awt.Frame parent, boolean modal, wakeup _up, BeanFuncControl _bean)
    {
        super(parent, modal);
        initComponents();
        bean = _bean;
        up = _up;
        intfreg = new interfaceRegister(up);
        intfdef = new interfaceDefine(up);
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
            bean = new BeanFuncControl();
            bEdit = false;
        }
        else
        {
            txtCooldown.setText(bean.getCooldown() + "");
            txtDesp.setText(bean.getDescription());
            txtName.setText(bean.getFuncName());
            txtPeriod.setText(bean.getPeriod() + "");
            txtTag.setText(bean.getFuncTag());
            txtDisable.setText(transIntfImplText(bean.getDisableImplements()));
            txtEnable.setText(transIntfImplText(bean.getEnableImplements()));
            txtLoop.setText(transIntfImplText(bean.getLoopImplenments()));
            txtInitEnv.setText(transIntfImplText(bean.getInitFuncEnv()));
            fast.setCheckBoxValue(ckAutoloop, bean.getAutoLoop());
            swsys.doSelectCombo(cmbRange, bean.getTargetRange());
            swsys.doSelectCombo(cmbActSide, bean.getActionSide());
            bEdit = true;
        }
    }

    private String transIntfImplText(String implTag)
    {
        if (implTag.equals(""))
        {
            return "";
        }
        BeanProgIntfRegister breg = intfreg.getRegisterByTag(implTag);
        if (breg == null)
        {
            return "[Error]";
        }
        BeanProgIntfDefine bdef = (BeanProgIntfDefine) intfdef.getRecordByID(breg.getDefOID());
        String sr = bdef.getIntfTag() + "::" + breg.getRegTag();
        return sr;
    }

    private void makeCombos()
    {
        DefaultComboBoxModel modRag = new DefaultComboBoxModel();
        int[] idsRag = constChk.getFinalInts(iConst.class, "FUNC_ACT_TARGET_");
        for (int i = 0; i < idsRag.length; i++)
        {
            int id = idsRag[i];
            listItem li = new listItem(iConst.translate(id), id);
            modRag.addElement(li);
        }
        cmbRange.setModel(modRag);
        DefaultComboBoxModel modSide = new DefaultComboBoxModel();
        int[] idsSide = constChk.getFinalInts(iConst.class, "FUNC_ACT_SIDE_");
        for (int i = 0; i < idsSide.length; i++)
        {
            int id = idsSide[i];
            listItem li = new listItem(iConst.translate(id), id);
            modSide.addElement(li);
        }
        cmbActSide.setModel(modSide);
    }

    private void doSelEnb()
    {
        DlgSelectInterfaceImplements dlg = new DlgSelectInterfaceImplements(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanProgIntfRegister breg = dlg.getSelectedRegister();
            bean.setEnableImplements(breg.getRegTag());
            txtEnable.setText(transIntfImplText(bean.getEnableImplements()));
        }
        dlg.dispose();
        dlg = null;
    }

    private void doSelDis()
    {
        DlgSelectInterfaceImplements dlg = new DlgSelectInterfaceImplements(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanProgIntfRegister breg = dlg.getSelectedRegister();
            bean.setDisableImplements(breg.getRegTag());
            txtDisable.setText(transIntfImplText(bean.getDisableImplements()));
        }
        dlg.dispose();
        dlg = null;
    }

    private void doSelInitInv()
    {
        DlgSelectInterfaceImplements dlg = new DlgSelectInterfaceImplements(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanProgIntfRegister breg = dlg.getSelectedRegister();
            bean.setInitFuncEnv(breg.getRegTag());
            txtInitEnv.setText(transIntfImplText(bean.getInitFuncEnv()));
        }
        dlg.dispose();
        dlg = null;
    }

    private void doSelLoop()
    {
        DlgSelectInterfaceImplements dlg = new DlgSelectInterfaceImplements(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanProgIntfRegister breg = dlg.getSelectedRegister();
            bean.setLoopImplenments(breg.getRegTag());
            txtLoop.setText(transIntfImplText(bean.getLoopImplenments()));
        }
        dlg.dispose();
        dlg = null;
    }

    private void doOK ()
    {
        bean.setActionSide(swsys.getComboBoxSelected(cmbActSide));
        bean.setAutoLoop(fast.readCheckBox(ckAutoloop));
        bean.setTargetRange(swsys.getComboBoxSelected(cmbRange));
        int icodn = fast.testIntegerText(txtCooldown);
        if (icodn  < 0)
        {
            fast.warn("输入正确的冷却周期");
            return ;
        }
        bean.setCooldown(icodn);
        int iperid = fast.testIntegerText(txtPeriod);
        if (iperid <0)
        {
            fast.warn("输入正确的作用周期");
            return ;
        }
        bean.setPeriod(iperid);
        bean.setDescription(txtDesp.getText().trim());
        bean.setFuncName(txtName.getText().trim());
        bean.setFuncTag(txtTag.getText().trim());
        bOK = true;
        setVisible(false);
    }
    
    private void doCancle ()
    {
        bOK = false;
        setVisible(false);
    }
    
    public boolean getOK ()
    {
        return bOK;
    }
    
    public BeanFuncControl getFuncControl ()
    {
        return bean ; 
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel10 = new javax.swing.JLabel();
        txtDisable = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtInitEnv = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTag = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPeriod = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCooldown = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtDesp = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtEnable = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtLoop = new javax.swing.JTextField();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        ckAutoloop = new javax.swing.JCheckBox();
        cmbRange = new javax.swing.JComboBox<>();
        btnEnableImpl = new javax.swing.JButton();
        btnLoopImpl = new javax.swing.JButton();
        btnDisableImpl = new javax.swing.JButton();
        btnSelInitEvn = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        cmbActSide = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("功能控制节点设置");
        setResizable(false);

        jLabel10.setText("关闭接口");

        txtDisable.setEditable(false);

        jLabel11.setText("环境接口");

        txtInitEnv.setEditable(false);

        jLabel1.setText("功能名称");

        jLabel2.setText("功能标签");

        jLabel3.setText("持续周期");

        jLabel4.setText("冷却周期");

        jLabel5.setText("自动重复");

        jLabel6.setText("描述文本");

        jLabel7.setText("作用范围");

        jLabel8.setText("启动接口");

        txtEnable.setEditable(false);
        txtEnable.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtEnableActionPerformed(evt);
            }
        });

        jLabel9.setText("循环接口");

        txtLoop.setEditable(false);

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

        ckAutoloop.setText("设为自动重复功能");

        cmbRange.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnEnableImpl.setText("...");
        btnEnableImpl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEnableImplActionPerformed(evt);
            }
        });

        btnLoopImpl.setText("...");
        btnLoopImpl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnLoopImplActionPerformed(evt);
            }
        });

        btnDisableImpl.setText("...");
        btnDisableImpl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDisableImplActionPerformed(evt);
            }
        });

        btnSelInitEvn.setText("...");
        btnSelInitEvn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelInitEvnActionPerformed(evt);
            }
        });

        jLabel12.setText("作用方面");

        cmbActSide.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                        .addComponent(txtName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTag))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPeriod))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCooldown))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDesp))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbRange, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtInitEnv))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDisable))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLoop))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEnable)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEnableImpl)
                                    .addComponent(btnLoopImpl, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addComponent(btnDisableImpl, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(btnSelInitEvn, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 360, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ckAutoloop)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbActSide, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCooldown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(ckAutoloop))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cmbActSide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtEnable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEnableImpl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtLoop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoopImpl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtDisable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDisableImpl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtInitEnv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelInitEvn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtEnableActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtEnableActionPerformed
    {//GEN-HEADEREND:event_txtEnableActionPerformed
        doSelEnb();
    }//GEN-LAST:event_txtEnableActionPerformed

    private void btnEnableImplActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEnableImplActionPerformed
    {//GEN-HEADEREND:event_btnEnableImplActionPerformed
        doSelEnb();
    }//GEN-LAST:event_btnEnableImplActionPerformed

    private void btnLoopImplActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnLoopImplActionPerformed
    {//GEN-HEADEREND:event_btnLoopImplActionPerformed
        doSelLoop();
    }//GEN-LAST:event_btnLoopImplActionPerformed

    private void btnDisableImplActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDisableImplActionPerformed
    {//GEN-HEADEREND:event_btnDisableImplActionPerformed
        doSelDis();
    }//GEN-LAST:event_btnDisableImplActionPerformed

    private void btnSelInitEvnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelInitEvnActionPerformed
    {//GEN-HEADEREND:event_btnSelInitEvnActionPerformed
        doSelInitInv();
    }//GEN-LAST:event_btnSelInitEvnActionPerformed

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
    private javax.swing.JButton btnDisableImpl;
    private javax.swing.JButton btnEnableImpl;
    private javax.swing.JButton btnLoopImpl;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSelInitEvn;
    private javax.swing.JCheckBox ckAutoloop;
    private javax.swing.JComboBox<String> cmbActSide;
    private javax.swing.JComboBox<String> cmbRange;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtCooldown;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtDisable;
    private javax.swing.JTextField txtEnable;
    private javax.swing.JTextField txtInitEnv;
    private javax.swing.JTextField txtLoop;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPeriod;
    private javax.swing.JTextField txtTag;
    // End of variables declaration//GEN-END:variables
}
