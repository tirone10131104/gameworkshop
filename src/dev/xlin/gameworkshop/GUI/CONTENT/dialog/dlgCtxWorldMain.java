package dev.xlin.gameworkshop.GUI.CONTENT.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.systemIconLib;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldTypeMain;
import dev.xlin.gameworkshop.progs.contents.progs.ctxConst;
import dev.xlin.gameworkshop.progs.contents.progs.ctxTranslate;
import dev.xlin.gameworkshop.progs.contents.progs.worldTypeMain;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myCellRenderer;
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
public class dlgCtxWorldMain extends javax.swing.JDialog
{

    private wakeup up = null;
    private beanCtxWorldTypeMain bean = null;
    private int tpid = 0;
    private boolean bOK = false;
    private boolean bEdit = false;

    public dlgCtxWorldMain(java.awt.Frame parent, boolean modal, wakeup _up, beanCtxWorldTypeMain _bean, int _tpid)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        tpid = _tpid;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        makeWorldTypeCombo();
        if (bean == null)
        {
            bEdit = false;
            bean = new beanCtxWorldTypeMain();
            swsys.doSelectCombo(cmbConstType, tpid);
            geneTag();
        }
        else
        {
            bEdit = true;
            txtName.setText(bean.getWmName());
            txtTag.setText(bean.getWmTag());
            txtDesp.setText(bean.getDescp());
            swsys.doSelectCombo(cmbConstType, bean.getWorldTypeCst());
            fast.setCheckBoxValue(ckHide, bean.getHide());
            cmbConstType.setEnabled(false);
            txtTag.setEditable(false);
            btnCheckTag.setEnabled(false);
            btnGeneTag.setEnabled(false);
        }
    }

    private void makeWorldTypeCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        int[] ids = constChk.getFinalInts(ctxConst.class, "WORLD_TYPE_");
        for (int i = 0; i < ids.length; i++)
        {
            int id = ids[i];
            listItem li = new listItem(ctxTranslate.translateConst(id), id);
            li.setIco(systemIconLib.icoNode4);
            mod.addElement(li);
        }
        cmbConstType.setModel(mod);
        cmbConstType.setRenderer(new myCellRenderer());
    }

    private void OK()
    {
        bean.setDescp(txtDesp.getText().trim());
        bean.setHide(fast.readCheckBox(ckHide));
        bean.setWorldTypeCst(swsys.getComboBoxSelected(cmbConstType));
        bean.setWmName(txtName.getText().trim());
        bean.setWmTag(txtTag.getText().trim());
        int r = 0;
        worldTypeMain wtm = new worldTypeMain(up);
        if (bEdit == false)
        {
            r = wtm.createRecord(bean, false);
        }
        else
        {
            r = wtm.modifyRecord(bean, false);
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

    private void geneTag()
    {
        String stg = tagCreator.createDataTag(up, "tbc_world_type_main", "wmTag", 2, 4);
        txtTag.setText(stg);
    }

    private boolean checkTag(boolean rpsc)
    {
        String stg = txtTag.getText().trim();
        if (stg.equals(""))
        {
            fast.warn("标签不可为空");
            return false;
        }
        worldTypeMain wtm = new worldTypeMain(up);
        if (wtm.getWorldTypeMainByTag(stg) != null)
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTag = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbConstType = new javax.swing.JComboBox<>();
        ckHide = new javax.swing.JCheckBox();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        txtDesp = new javax.swing.JTextField();
        tbrTagTools = new javax.swing.JToolBar();
        btnGeneTag = new javax.swing.JButton();
        btnCheckTag = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("世界主类信息");
        setResizable(false);

        jLabel1.setText("分类名称");

        jLabel2.setText("分类标签");

        jLabel3.setText("所属类型");

        cmbConstType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ckHide.setText("设置为隐藏");

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

        jLabel4.setText("分类描述");

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ckHide)
                                .addGap(0, 293, Short.MAX_VALUE))
                            .addComponent(cmbConstType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
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
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbConstType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckHide)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbConstType, txtDesp, txtName, txtTag});

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

    private void btnGeneTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnGeneTagActionPerformed
    {//GEN-HEADEREND:event_btnGeneTagActionPerformed
        geneTag();
    }//GEN-LAST:event_btnGeneTagActionPerformed

    private void btnCheckTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCheckTagActionPerformed
    {//GEN-HEADEREND:event_btnCheckTagActionPerformed
        checkTag(true);
    }//GEN-LAST:event_btnCheckTagActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnCheckTag;
    private javax.swing.JButton btnGeneTag;
    private javax.swing.JButton btnOK;
    private javax.swing.JCheckBox ckHide;
    private javax.swing.JComboBox<String> cmbConstType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar tbrTagTools;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTag;
    // End of variables declaration//GEN-END:variables
}
