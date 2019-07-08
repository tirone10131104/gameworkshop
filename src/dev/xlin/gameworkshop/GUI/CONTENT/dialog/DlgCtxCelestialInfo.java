package dev.xlin.gameworkshop.GUI.CONTENT.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxCelestialWorld;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxStellarData;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldTypeConfig;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldTypeMain;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldTypeSet;
import dev.xlin.gameworkshop.progs.contents.progs.CelestialWorldData;
import dev.xlin.gameworkshop.progs.contents.progs.ctxTranslate;
import dev.xlin.gameworkshop.progs.contents.progs.StellarData;
import dev.xlin.gameworkshop.progs.contents.progs.WorldTypeConfig;
import dev.xlin.gameworkshop.progs.contents.progs.WorldTypeMain;
import dev.xlin.gameworkshop.progs.contents.progs.WorldTypeSet;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class DlgCtxCelestialInfo extends javax.swing.JDialog
{

    private boolean bEdit = false;
    private boolean bOK = false;
    private BeanCtxCelestialWorld bean = null;
    private int parentID = 0;
    private int stellarID = 0;
    private wakeup up = null;
    private CelestialWorldData cwdt = null;
    private BeanCtxCelestialWorld beanParent = null;

    public DlgCtxCelestialInfo(java.awt.Frame parent, boolean modal, wakeup _up, BeanCtxCelestialWorld _bean, int _stellarID, int _parid)
    {
        super(parent, modal);
        initComponents();
        bean = _bean;
        stellarID = _stellarID;
        parentID = _parid;
        up = _up;
        cwdt = new CelestialWorldData(up);
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        //获取恒星系信息
        StellarData sdt = new StellarData(up);
        BeanCtxStellarData bcst = (BeanCtxStellarData) sdt.getRecordByID(stellarID);
        if (bcst != null)
        {
            txtStellar.setText(bcst.getStName() + "<" + bcst.getStTag() + ">" + " " + ctxTranslate.translateConst(bcst.getSpectrumID()) + bcst.getSpectrumLevel());
        }
        System.err.println("pi  " + parentID);
        if (parentID != 0)
        {
            CelestialWorldData cwd = new CelestialWorldData(up);
            BeanCtxCelestialWorld bpar = (BeanCtxCelestialWorld) cwd.getRecordByID(parentID);
            if (bpar != null)
            {
                txtParent.setText("[" + ctxTranslate.translateConst(bpar.getWorldType()) + "]" + bpar.getWorldName() + "<" + bpar.getWorldCode() + ">");
                beanParent = bpar;
            }
        }
        if (bean == null)
        {
            bean = new BeanCtxCelestialWorld();
            makeCeleConstCombos();
            makeWorldMainCombo();
            makeWorldSetCombo();
            makeWorldConfigCombo();
            geneTag();
            bEdit = false;
        }
        else
        {
            makeCeleConstCombos();
            swsys.doSelectCombo(cmbCstType, bean.getWorldType());
            makeWorldMainCombo();
            swsys.doSelectCombo(cmbTypeMain, bean.getWorldTypeMainOID());
            makeWorldSetCombo();
            swsys.doSelectCombo(cmbTypeSet, bean.getWorldTypeSetOID());
            makeWorldConfigCombo();
            swsys.doSelectCombo(cmbTypeConfig, bean.getWorldTypeConfigOID());
            txtCode.setText(bean.getWorldCode());
            txtDesp.setText(bean.getDescp());
            txtName.setText(bean.getWorldName());
            cmbCstType.setEnabled(false);
            bEdit = true;
        }
    }

    private void makeCeleConstCombos()
    {
        int pcid = 0;
        if (beanParent != null)
        {
            pcid = beanParent.getWorldType();
        }
        ArrayList acs = (ArrayList) cwdt.getChildWroldConsts(pcid);
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        for (int i = 0; i < acs.size(); i++)
        {
            int ict = (int) acs.get(i);
            listItem li = new listItem(ctxTranslate.translateConst(ict), ict);
            mod.addElement(li);
        }
        cmbCstType.setModel(mod);
    }

    private int getTypeConstSelected()
    {
        listItem lsel = (listItem) cmbCstType.getSelectedItem();
        if (lsel == null)
        {
            return -1;
        }
        return lsel.getNodeOID();
    }

    private void makeWorldMainCombo()
    {
        int cstid = getTypeConstSelected();
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        if (cstid > 0)
        {
            WorldTypeMain wtm = new WorldTypeMain(up);
            List ls = wtm.getWorldMainlistByType(cstid, false);
            if (ls != null)
            {
                for (int i = 0; i < ls.size(); i++)
                {
                    BeanCtxWorldTypeMain bwmain = (BeanCtxWorldTypeMain) ls.get(i);
                    listItem li = new listItem(bwmain.getWmName() + "<" + bwmain.getWmTag() + ">", bwmain.getOID());
                    mod.addElement(li);
                }
            }
        }
        cmbTypeMain.setModel(mod);
    }

    private int getTypeMainSelected()
    {
        listItem li = (listItem) cmbTypeMain.getSelectedItem();
        if (li == null)
        {
            return -1;
        }
        return li.getNodeOID();
    }

    private void makeWorldSetCombo()
    {
        int mid = getTypeMainSelected();
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        if (mid > 0)
        {
            WorldTypeSet wts = new WorldTypeSet(up);
            List ls = wts.getWorldSetsByMainType(mid, false);
            if (ls != null)
            {
                for (int i = 0; i < ls.size(); i++)
                {
                    BeanCtxWorldTypeSet bset = (BeanCtxWorldTypeSet) ls.get(i);
                    listItem li = new listItem(bset.getSetName() + "<" + bset.getSetTag() + ">", bset.getOID());
                    mod.addElement(li);
                }
            }
        }
        cmbTypeSet.setModel(mod);
    }

    private int getTypeSetSelected()
    {
        listItem li = (listItem) cmbTypeSet.getSelectedItem();
        if (li == null)
        {
            return -1;
        }
        return li.getNodeOID();
    }

    private void makeWorldConfigCombo()
    {
        int sid = getTypeSetSelected();
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        if (sid > 0)
        {
            WorldTypeConfig wtc = new WorldTypeConfig(up);
            List ls = wtc.getConfigsByWorldSet(sid);
            if (ls != null)
            {
                for (int i = 0; i < ls.size(); i++)
                {
                    BeanCtxWorldTypeConfig bwtc = (BeanCtxWorldTypeConfig) ls.get(i);
                    listItem li = new listItem(bwtc.getCfgName() + "<" + bwtc.getCfgTag() + ">", bwtc.getOID());
                    mod.addElement(li);
                }
            }
        }
        cmbTypeConfig.setModel(mod);
    }

    public int getTypeConfigSelected()
    {
        listItem li = (listItem) cmbTypeConfig.getSelectedItem();
        if (li == null)
        {
            return -1;
        }
        return li.getNodeOID();
    }

    private void geneTag()
    {
        String stg = tagCreator.createDataTag(up, "tbc_celestial_world_data", "worldCode", 3, 5);
        txtCode.setText(stg);
    }

    private boolean checkTag(boolean rpsc)
    {
        String stg = txtCode.getText().trim();
        if (stg.equals(""))
        {
            fast.warn("标签不可为空");
            return false;
        }
        CelestialWorldData cwd = new CelestialWorldData(up);
        if (cwd.getCelestialWorldByCode(stg) != null)
        {
            fast.warn("标签被占用");
            return false;
        }
        if (rpsc)
        {
            fast.msg("标签可以使用");
        }
        return true;
    }

    public boolean getOK()
    {
        return bOK;
    }

    private void doOK()
    {
        System.err.println("..bedit = " + bEdit);
        if (bEdit == false)
        {
            if (checkTag(false) == false)
            {
                return;
            }
        }
        String sname = txtName.getText().trim();
        if (sname.equals(""))
        {
            return;
        }
        int cid = swsys.getComboBoxSelected(cmbTypeConfig);
        if (cid < 0)
        {
            fast.warn("必须选择一个天体环境配置");
            return;
        }
        bean.setWorldName(sname);
        bean.setWorldCode(txtCode.getText().trim());
        bean.setDescp(txtDesp.getText().trim());
        bean.setParentOID(parentID);
        bean.setStellarOID(stellarID);
        bean.setWorldType(swsys.getComboBoxSelected(cmbCstType));
        bean.setWorldTypeMainOID(swsys.getComboBoxSelected(cmbTypeMain));
        bean.setWorldTypeSetOID(swsys.getComboBoxSelected(cmbTypeSet));
        bean.setWorldTypeConfigOID(swsys.getComboBoxSelected(cmbTypeConfig));
        CelestialWorldData cwd = new CelestialWorldData(up);
        int r = 0;
        System.err.println(".bedit = " + bEdit);
        if (bEdit)
        {
            r = cwd.modifyRecord(bean, false);
        }
        else
        {
            r = cwd.createRecord(bean, false);
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

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cmbCstType = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtStellar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtParent = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCode = new javax.swing.JTextField();
        tbrTagTools = new javax.swing.JToolBar();
        btnGeneTag = new javax.swing.JButton();
        btnCheckTag = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtDesp = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbTypeMain = new javax.swing.JComboBox<>();
        cmbTypeSet = new javax.swing.JComboBox<>();
        cmbTypeConfig = new javax.swing.JComboBox<>();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("天体基本数据");
        setResizable(false);

        jLabel1.setText("名称");

        jLabel2.setText("星体");

        cmbCstType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCstTypeActionPerformed(evt);
            }
        });

        jLabel3.setText("上级");

        txtStellar.setEditable(false);
        txtStellar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtStellarActionPerformed(evt);
            }
        });

        jLabel4.setText("星系");

        txtParent.setEditable(false);

        jLabel5.setText("编码");

        txtCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodeActionPerformed(evt);
            }
        });

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

        jLabel6.setText("描述");

        jLabel7.setText("类型");

        jLabel8.setText("分类");

        jLabel9.setText("子类");

        cmbTypeMain.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTypeMainActionPerformed(evt);
            }
        });

        cmbTypeSet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTypeSetActionPerformed(evt);
            }
        });

        cmbTypeConfig.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTypeConfigActionPerformed(evt);
            }
        });

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
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStellar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCstType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtParent)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDesp))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTypeConfig, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTypeMain, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTypeSet, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 260, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle)))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtParent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtStellar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbCstType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbTypeMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cmbTypeSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cmbTypeConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtStellarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtStellarActionPerformed
    {//GEN-HEADEREND:event_txtStellarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStellarActionPerformed

    private void btnGeneTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnGeneTagActionPerformed
    {//GEN-HEADEREND:event_btnGeneTagActionPerformed
        geneTag();
    }//GEN-LAST:event_btnGeneTagActionPerformed

    private void btnCheckTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCheckTagActionPerformed
    {//GEN-HEADEREND:event_btnCheckTagActionPerformed
        checkTag(true);
    }//GEN-LAST:event_btnCheckTagActionPerformed

    private void txtCodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodeActionPerformed
    {//GEN-HEADEREND:event_txtCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodeActionPerformed

    private void cmbTypeMainActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTypeMainActionPerformed
    {//GEN-HEADEREND:event_cmbTypeMainActionPerformed
        makeWorldSetCombo();
        makeWorldConfigCombo();
    }//GEN-LAST:event_cmbTypeMainActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        doOK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        doCancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void cmbTypeConfigActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTypeConfigActionPerformed
    {//GEN-HEADEREND:event_cmbTypeConfigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTypeConfigActionPerformed

    private void cmbCstTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbCstTypeActionPerformed
    {//GEN-HEADEREND:event_cmbCstTypeActionPerformed
        makeWorldMainCombo();
        makeWorldSetCombo();
        makeWorldConfigCombo();
    }//GEN-LAST:event_cmbCstTypeActionPerformed

    private void cmbTypeSetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTypeSetActionPerformed
    {//GEN-HEADEREND:event_cmbTypeSetActionPerformed
        makeWorldConfigCombo();
    }//GEN-LAST:event_cmbTypeSetActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnCheckTag;
    private javax.swing.JButton btnGeneTag;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox<String> cmbCstType;
    private javax.swing.JComboBox<String> cmbTypeConfig;
    private javax.swing.JComboBox<String> cmbTypeMain;
    private javax.swing.JComboBox<String> cmbTypeSet;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JTextField txtCode;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtParent;
    private javax.swing.JTextField txtStellar;
    // End of variables declaration//GEN-END:variables
}
