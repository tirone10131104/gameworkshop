package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xling.jmdbs.BeanMdbTypeDefine;
import dev.xling.jmdbs.MDBDataTypeDefine;
import dev.xling.jmdbs.mdbsConst;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;

public class dlgMDBTypeDefine extends javax.swing.JDialog
{

    private wakeup up = null;
    private BeanMdbTypeDefine bean = null;
    private boolean bEdit = false;
    private boolean bOK = false;
    private int typeOID = 0;

    public dlgMDBTypeDefine(java.awt.Frame parent, boolean modal, wakeup _up, BeanMdbTypeDefine _bean, int _typeid)
    {
        super(parent, modal);
        initComponents();
        bean = _bean;
        up = _up;
        typeOID = _typeid;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        initMethodCombo();
        initGarbageTimeCombo();
        if (bean == null)
        {
            bEdit = false;
            bean = new BeanMdbTypeDefine();
            bean.setTypeOID(typeOID);
            swsys.doSelectCombo(cmbInitMethod, mdbsConst.MDBS_INIT_NO);
            selectedInitMethod();
            setAutoGarbage();
        }
        else
        {
            txtDssDao.setText(bean.getDssDaoPath());
            txtInitPath.setText(bean.getInitPath());
            txtName.setText(bean.getTypeName());
            txtTag.setText(bean.getTypeTag());
            txtTableName.setText(bean.getTableName());
            txtIdField.setText(bean.getIdField());
            txtStatusField.setText(bean.getStatusField());
            txtEntityPath.setText(bean.getEntityPath());
            txtGarbageTime.setText(bean.getGarbageTime() + "");
            fast.setCheckBoxValue(ckAllowGC, bean.getAllowGC());
            fast.setCheckBoxValue(ckAllowEntity, bean.getAllowEntity());
            swsys.doSelectCombo(cmbInitMethod, bean.getInitMethod());
            txtValidatePath.setText(bean.getValidatorPath());
            fast.setCheckBoxValue(chkValReg, bean.getCheckReg());
            fast.setCheckBoxValue(chkValDelete, bean.getCheckDelete());
            fast.setCheckBoxValue(chkValUpdate, bean.getCheckUpdate());
            selectedInitMethod();
            setAutoGarbage();
            bEdit = true;
        }
    }

    private void initMethodCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem(mdbsConst.translate(mdbsConst.MDBS_INIT_NO), mdbsConst.MDBS_INIT_NO));
        mod.addElement(new listItem(mdbsConst.translate(mdbsConst.MDBS_INIT_AUTO_ALL), mdbsConst.MDBS_INIT_AUTO_ALL));
        mod.addElement(new listItem(mdbsConst.translate(mdbsConst.MDBS_INIT_AUTO_ACTIVE), mdbsConst.MDBS_INIT_AUTO_ACTIVE));
        mod.addElement(new listItem(mdbsConst.translate(mdbsConst.MDBS_INIT_MANUAL), mdbsConst.MDBS_INIT_MANUAL));
        cmbInitMethod.setModel(mod);
    }

    private void initGarbageTimeCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem(30 + "", 30));
        mod.addElement(new listItem(60 + "", 60));
        mod.addElement(new listItem(120 + "", 120));
        mod.addElement(new listItem(180 + "", 180));
        mod.addElement(new listItem(240 + "", 240));
        mod.addElement(new listItem(300 + "", 300));
        cmbGrabageTime.setModel(mod);
    }

    private void geneTag()
    {
        String s = tagCreator.createDataTag(up, "ic_mce_config_main", "mceTag", 4, 4);
        txtTag.setText(s);
    }

    private boolean checkTag(boolean b)
    {
        String s = txtTag.getText().trim();
        if (s.equals(""))
        {
            fast.warn("标签不能为空");
            return false;
        }
        if (bEdit)
        {
            if (s.equals(bean.getTypeTag()) == false)
            {
                boolean brp = _checkTagIsRepeat(s);
                if (brp)
                {
                    fast.warn("标签检测到已被占用");
                    return !brp;
                }
            }
        }
        else
        {
            boolean brp = _checkTagIsRepeat(s);
            if (brp)
            {
                fast.warn("标签检测到已被占用");
                return !brp;
            }
        }
        if (b)
        {
            fast.warn("检查OK");
        }
        return true;
    }

    private boolean _checkTagIsRepeat(String s)
    {
        MDBDataTypeDefine mtd = new MDBDataTypeDefine(up);
        if (mtd.getTypeDefineByTag(s) != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void disableAllInitTxt()
    {
        txtTableName.setEnabled(false);
        txtIdField.setEnabled(false);
        txtStatusField.setEnabled(false);
        txtInitPath.setEnabled(false);
        txtEntityPath.setEnabled(false);
    }

    private void selectedInitMethod()
    {
        disableAllInitTxt();
        int sid = swsys.getComboBoxSelected(cmbInitMethod);
        if (sid == mdbsConst.MDBS_INIT_MANUAL)
        {
            txtInitPath.setEnabled(true);
        }
        else if (sid == mdbsConst.MDBS_INIT_AUTO_ALL)
        {
            txtTableName.setEnabled(true);
            txtIdField.setEnabled(true);
            txtEntityPath.setEnabled(true);
        }
        else if (sid == mdbsConst.MDBS_INIT_AUTO_ACTIVE)
        {
            txtTableName.setEnabled(true);
            txtIdField.setEnabled(true);
            txtStatusField.setEnabled(true);
            txtEntityPath.setEnabled(true);
        }
    }

    private void setAutoGarbage()
    {
        boolean b = ckAllowGC.isSelected();
        txtGarbageTime.setEnabled(b);
        cmbGrabageTime.setEnabled(b);
        if (b == false)
        {
            txtGarbageTime.setText("0");
        }
    }

    private void OK()
    {
        String sname = txtName.getText().trim();
        if (sname.equals(""))
        {
            fast.warn("名称不可为空");
            return;
        }
        String stag = txtTag.getText().trim();
        boolean btg = checkTag(false);
        if (btg == false)
        {
            return;
        }
        bean.setAllowEntity(fast.readCheckBox(ckAllowEntity));
        bean.setAllowGC(fast.readCheckBox(ckAllowGC));
        bean.setDssDaoPath(txtDssDao.getText().trim());
        bean.setInitPath(txtInitPath.getText().trim());
        bean.setTypeName(sname);
        bean.setTypeTag(stag);
        bean.setInitMethod(fast.readCombo(cmbInitMethod));
        bean.setTableName(txtTableName.getText().trim());
        bean.setIdField(txtIdField.getText().trim());
        bean.setStatusField(txtStatusField.getText().trim());
        bean.setEntityPath(txtEntityPath.getText().trim());
        bean.setValidatorPath(txtValidatePath.getText().trim());
        bean.setCheckReg(fast.readCheckBox(chkValReg));
        bean.setCheckDelete(fast.readCheckBox(chkValDelete));
        bean.setCheckUpdate(fast.readCheckBox(chkValUpdate));
        //垃圾清理时限读取
        if (ckAllowGC.isSelected())
        {
            int igt = fast.testIntegerText(txtGarbageTime, -1);
            if (igt < 0)
            {
                fast.warn("垃圾清理时限设置错误");
                return;
            }
            bean.setGarbageTime(igt);
        }
        //数据库操作
        MDBDataTypeDefine mtd = new MDBDataTypeDefine(up);
        long r = 0;
        if (bEdit)
        {
            //修改
            r = mtd.update(bean);
            if (r != mtd.OPERATE_SUCCESS)
            {
                fast.err("修改数据操作失败", (int) r);
            }
        }
        else
        {
            //新建
            r = mtd.save(bean);
            if (r < 0)
            {
                fast.err("新建数据操作失败", -(int) r);
            }
        }
        bOK = true;
        setVisible(false);
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

    private void selectGarbageTime()
    {
        int igt = swsys.getComboBoxSelected(cmbGrabageTime);
        txtGarbageTime.setText(igt + "");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtTag = new javax.swing.JTextField();
        tbrTagTools = new javax.swing.JToolBar();
        btnGeneTag = new javax.swing.JButton();
        btnCheckTag = new javax.swing.JButton();
        txtDssDao = new javax.swing.JTextField();
        txtInitPath = new javax.swing.JTextField();
        ckAllowEntity = new javax.swing.JCheckBox();
        ckAllowGC = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        cmbInitMethod = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtTableName = new javax.swing.JTextField();
        txtIdField = new javax.swing.JTextField();
        txtStatusField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtEntityPath = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cmbGrabageTime = new javax.swing.JComboBox<>();
        txtGarbageTime = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        chkValReg = new javax.swing.JCheckBox();
        chkValUpdate = new javax.swing.JCheckBox();
        chkValDelete = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        txtValidatePath = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MDB数据类型设置");
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

        jLabel1.setText("类型名称");

        jLabel2.setText("类型标签");

        jLabel3.setText("操作类路径");

        jLabel4.setText("初路径始化");

        jLabel5.setText("实体化");

        jLabel6.setText("垃圾清理");

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

        ckAllowEntity.setText("允许自动实体化");

        ckAllowGC.setText("允许垃圾清理");
        ckAllowGC.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckAllowGCActionPerformed(evt);
            }
        });

        jLabel7.setText("初始化模式");

        cmbInitMethod.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbInitMethodActionPerformed(evt);
            }
        });

        jLabel8.setText("数据表");

        jLabel9.setText("键字段");

        jLabel10.setText("状态字段");

        jLabel11.setText("实体类路径");

        jLabel12.setForeground(new java.awt.Color(0, 0, 153));
        jLabel12.setText("时限（秒）");

        cmbGrabageTime.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbGrabageTimeActionPerformed(evt);
            }
        });

        txtGarbageTime.setText("0");
        txtGarbageTime.setToolTipText("");

        jLabel13.setText("数据验证");

        chkValReg.setText("验证注册");

        chkValUpdate.setText("验证修改");

        chkValDelete.setText("验证删除");

        jLabel14.setText("验证路径");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
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
                        .addComponent(txtTag)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDssDao))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtInitPath)
                            .addComponent(cmbInitMethod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTableName))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ckAllowGC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGarbageTime, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbGrabageTime, 0, 202, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStatusField))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ckAllowEntity)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chkValReg)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkValUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkValDelete))
                            .addComponent(txtValidatePath)
                            .addComponent(txtEntityPath))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel8, jLabel9});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDssDao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(ckAllowEntity))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(ckAllowGC)
                    .addComponent(jLabel12)
                    .addComponent(cmbGrabageTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGarbageTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbInitMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtInitPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtTableName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtStatusField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtEntityPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(chkValReg)
                    .addComponent(chkValUpdate)
                    .addComponent(chkValDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtValidatePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGeneTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnGeneTagActionPerformed
    {//GEN-HEADEREND:event_btnGeneTagActionPerformed
        geneTag();
    }//GEN-LAST:event_btnGeneTagActionPerformed

    private void btnCheckTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCheckTagActionPerformed
    {//GEN-HEADEREND:event_btnCheckTagActionPerformed
        checkTag(true);
    }//GEN-LAST:event_btnCheckTagActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        OK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void cmbInitMethodActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbInitMethodActionPerformed
    {//GEN-HEADEREND:event_cmbInitMethodActionPerformed
        selectedInitMethod();
    }//GEN-LAST:event_cmbInitMethodActionPerformed

    private void ckAllowGCActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckAllowGCActionPerformed
    {//GEN-HEADEREND:event_ckAllowGCActionPerformed
        setAutoGarbage();
    }//GEN-LAST:event_ckAllowGCActionPerformed

    private void cmbGrabageTimeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbGrabageTimeActionPerformed
    {//GEN-HEADEREND:event_cmbGrabageTimeActionPerformed
        selectGarbageTime();
    }//GEN-LAST:event_cmbGrabageTimeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnCheckTag;
    private javax.swing.JButton btnGeneTag;
    private javax.swing.JButton btnOK;
    private javax.swing.JCheckBox chkValDelete;
    private javax.swing.JCheckBox chkValReg;
    private javax.swing.JCheckBox chkValUpdate;
    private javax.swing.JCheckBox ckAllowEntity;
    private javax.swing.JCheckBox ckAllowGC;
    private javax.swing.JComboBox<String> cmbGrabageTime;
    private javax.swing.JComboBox<String> cmbInitMethod;
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar tbrTagTools;
    private javax.swing.JTextField txtDssDao;
    private javax.swing.JTextField txtEntityPath;
    private javax.swing.JTextField txtGarbageTime;
    private javax.swing.JTextField txtIdField;
    private javax.swing.JTextField txtInitPath;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtStatusField;
    private javax.swing.JTextField txtTableName;
    private javax.swing.JTextField txtTag;
    private javax.swing.JTextField txtValidatePath;
    // End of variables declaration//GEN-END:variables
}
