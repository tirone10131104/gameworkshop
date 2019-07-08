package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItemEquipConfig;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItemCluster;
import dev.xlin.gameworkshop.progs.foundation.beans.beanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.itemCluster;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iBean;
import dev.xlin.tools.codeTools;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

public class dlgItemEquipConfig extends javax.swing.JDialog
{

    private wakeup up = null;
    private beanItemEquipConfig bean = null;
    private boolean bOK = false;
    private boolean bEdit = false;
    private boolean bInitGUI = false;
    private HashMap ltgs = new HashMap();

    private int lmtTarType = 0;

    public dlgItemEquipConfig(java.awt.Frame parent, boolean modal, wakeup _up, beanItemEquipConfig _bean)
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
        makeCombos();
        if (bean == null)
        {
            bean = new beanItemEquipConfig();
            lmtTarType = swsys.getComboBoxSelected(cmbLimitTarget);
            bEdit = false;
        }
        else
        {
            bEdit = true;
            txtEqpDesp.setText(bean.getSlotDesp());
            txtEqpName.setText(bean.getSlotName());
            txtEquipQts.setText(bean.getSlotQuantity() + "");
            swsys.doSelectCombo(cmbEquipType, bean.getEquipType());
            makeSlotTypeCombo();
            swsys.doSelectCombo(cmbSlotType, bean.getSlotType());
            makeSlotIndexCombo();
            swsys.doSelectCombo(cmbSlotIndex, bean.getSlotIndex());
            cmbEquipType.setEnabled(false);
            cmbSlotIndex.setEnabled(false);
            cmbSlotType.setEnabled(false);
            swsys.doSelectCombo(cmbLimitTarget, bean.getLimitTarget());
            lmtTarType = bean.getLimitTarget();
            int[] ids = codeTools.convertStrToArr(bean.getLimitTargetOIDS());
            DefaultListModel dlm = new DefaultListModel();
            ltgs = new HashMap();
            if (bean.getLimitTarget() == iConst.IEQPC_TAGS_OCS)
            {
                objectClassDefine ocd = new objectClassDefine(up);
                for (int i = 0; i < ids.length; i++)
                {
                    int id = ids[i];
                    beanObjectClass boc = (beanObjectClass) ocd.getRecordByID(id);
                    ltgs.put(boc.getOID(), boc);
                    listItem li = new listItem(boc.getClassName(), boc.getOID());
                    dlm.addElement(li);
                }
            }
            else if (bean.getLimitTarget() == iConst.IEQPC_TAGS_ITEMS)
            {
                itemDefine idef = new itemDefine(up);
                for (int i = 0; i < ids.length; i++)
                {
                    int id = ids[i];
                    beanItem bit = (beanItem) idef.getRecordByID(id);
                    ltgs.put(bit.getOID(), bit);
                    listItem li = new listItem(bit.getItemName(), bit.getOID());
                    dlm.addElement(li);
                }
            }
            else if (bean.getLimitTarget() == iConst.IEQPC_TAGS_ICLUS)
            {
                itemCluster iclu = new itemCluster(up);
                for (int i = 0; i < ids.length; i++)
                {
                    int id = ids[i];
                    beanItemCluster bic = (beanItemCluster) iclu.getRecordByID(id);
                    ltgs.put(bic.getOID(), bic);
                    listItem li = new listItem(bic.getClusterName(), id);
                    dlm.addElement(li);
                }
            }
            lstLmtTags.setModel(dlm);
        }
        bInitGUI = true;
    }

    private void makeCombos()
    {
        makeLmtTagTps();
        makeEquipTypeCombo();
        makeSlotTypeCombo();
        makeSlotIndexCombo();
    }

    private void makeLmtTagTps()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        listItem lall = new listItem("[无限制]", 0);
        mod.addElement(lall);
        int[] ids = constChk.getFinalInts(iConst.class, "IEQPC_TAGS_");
        for (int i = 0; i < ids.length; i++)
        {
            int id = ids[i];
            listItem li = new listItem(iConst.translate(id), id);
            mod.addElement(li);
        }
        cmbLimitTarget.setModel(mod);
    }

    private void makeEquipTypeCombo()
    {
        DefaultComboBoxModel modEQTP = guiCodes.makeItemEquipTypeModel(up);
        cmbEquipType.setModel(modEQTP);
    }

    private void makeSlotTypeCombo()
    {
        int ietp = swsys.getComboBoxSelected(cmbEquipType);
        DefaultComboBoxModel modSLTP = guiCodes.makeItemEquipChildModel(up, ietp);
        cmbSlotType.setModel(modSLTP);
    }

    private void makeSlotIndexCombo()
    {
        int isltp = swsys.getComboBoxSelected(cmbSlotType);
        DefaultComboBoxModel modSLIX = guiCodes.makeItemEquipChildModel(up, isltp);
        cmbSlotIndex.setModel(modSLIX);
    }

    private void doOK()
    {
        String sname = txtEqpName.getText().trim();
        if (sname.equals(""))
        {
            fast.warn("必须填写名称");
            return;
        }
        int iqt = fast.testIntegerText(txtEquipQts);
        if (iqt <= 0)
        {
            fast.warn("请填写正确的数量");
            return;
        }
        bean.setSlotName(sname);
        bean.setSlotDesp(txtEqpDesp.getText().trim());
        bean.setEquipType(swsys.getComboBoxSelected(cmbEquipType));
        bean.setSlotType(swsys.getComboBoxSelected(cmbSlotType));
        bean.setSlotIndex(swsys.getComboBoxSelected(cmbSlotIndex));
        bean.setLimitTarget(lmtTarType);
        bean.setSlotQuantity(iqt);
        String slmts = "";
        Set ks = ltgs.keySet();
        Iterator itor = ks.iterator();
        while (itor.hasNext())
        {
            int ikey = (int) itor.next();
            iBean ibn = (iBean) ltgs.get(ikey);
            int id = ibn.getOID();
            slmts = codeTools.addIntIntoString(slmts, id);
        }
        bean.setLimitTargetOIDS(slmts);
        bOK = true;
        setVisible(false);
    }

    public boolean getOK()
    {
        return bOK;
    }

    public beanItemEquipConfig getConfigBean()
    {
        return bean;
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    private void doSelectTarges()
    {
        int tartp = swsys.getComboBoxSelected(cmbLimitTarget);
        if (tartp == iConst.IEQPC_TAGS_ICLUS)
        {
            dlgSelectItemCluster dlg = new dlgSelectItemCluster(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanItemCluster bean = dlg.getSelectedBean();
                if (ltgs.containsKey(bean.getOID()) == false)
                {
                    ltgs.put(bean.getOID(), bean);
                    makeTarList();
                }
            }
            dlg.dispose();
            dlg = null;
        }
        else if (tartp == iConst.IEQPC_TAGS_ITEMS)
        {
            dlgSelectItemDefine dlg = new dlgSelectItemDefine(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanItem bit = dlg.getSelectedItemDefine();
                if (ltgs.containsKey(bit.getOID()) == false)
                {
                    ltgs.put(bit.getOID(), bit);
                    makeTarList();
                }
            }
            dlg.dispose();
            dlg = null;
        }
        else if (tartp == iConst.IEQPC_TAGS_OCS)
        {
            dlgSelectObjectClass dlg = new dlgSelectObjectClass(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanObjectClass boc = dlg.getSelectedObjectClass();
                if (ltgs.containsKey(boc.getOID()) == false)
                {
                    ltgs.put(boc.getOID(), boc);
                    makeTarList();
                }
            }
            dlg.dispose();
            dlg = null;
        }
    }

    private void makeTarList()
    {
        DefaultListModel dlm = new DefaultListModel();
        Set ks = ltgs.keySet();
        Iterator itor = ks.iterator();
        while (itor.hasNext())
        {
            int i = (int) itor.next();
            if (lmtTarType == iConst.IEQPC_TAGS_OCS)
            {
                beanObjectClass boc = (beanObjectClass) ltgs.get(i);
                listItem li = new listItem(boc.getClassName(), boc.getOID());
                dlm.addElement(li);
            }
            else if (lmtTarType == iConst.IEQPC_TAGS_ITEMS)
            {
                beanItem bit = (beanItem) ltgs.get(i);
                listItem li = new listItem(bit.getItemName(), bit.getOID());
                dlm.addElement(li);
            }
            else if (lmtTarType == iConst.IEQPC_TAGS_ICLUS)
            {
                beanItemCluster bic = (beanItemCluster) ltgs.get(i);
                listItem li = new listItem(bic.getClusterName(), bic.getOID());
                dlm.addElement(li);
            }
        }
        lstLmtTags.setModel(dlm);
    }

    private void doSelectTarTp()
    {
        if (bInitGUI == false)
        {
            return;
        }
        int tarid = swsys.getComboBoxSelected(cmbLimitTarget);
        if (tarid != lmtTarType)
        {
            int sel = fast.ask("是否确认切换目标类型，操作会清除已有数据。");
            if (sel == fast.YES)
            {
                ltgs = new HashMap();
                lmtTarType = tarid;
                makeTarList();
            }
            else
            {
                swsys.doSelectCombo(cmbLimitTarget, lmtTarType);
            }
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
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtEqpName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtEqpDesp = new javax.swing.JTextArea();
        cmbEquipType = new javax.swing.JComboBox<>();
        cmbSlotType = new javax.swing.JComboBox<>();
        cmbSlotIndex = new javax.swing.JComboBox<>();
        txtEquipQts = new javax.swing.JTextField();
        cmbLimitTarget = new javax.swing.JComboBox<>();
        btnSelectLmtTags = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstLmtTags = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("装配接口设置");

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

        jLabel1.setText("装配名称");

        jLabel2.setText("装配描述");

        jLabel3.setText("装配目标");

        jLabel4.setText("槽位类型");

        jLabel5.setText("槽位位序");

        jLabel6.setText("装配数量");

        jLabel7.setText("限制目标");

        jLabel8.setText("目标列表");

        txtEqpDesp.setColumns(20);
        txtEqpDesp.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        txtEqpDesp.setRows(5);
        jScrollPane1.setViewportView(txtEqpDesp);

        cmbEquipType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbEquipTypeActionPerformed(evt);
            }
        });

        cmbSlotType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSlotTypeActionPerformed(evt);
            }
        });

        cmbLimitTarget.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbLimitTargetActionPerformed(evt);
            }
        });

        btnSelectLmtTags.setText("选择");
        btnSelectLmtTags.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelectLmtTagsActionPerformed(evt);
            }
        });

        lstLmtTags.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(lstLmtTags);

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
                        .addComponent(txtEqpName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbEquipType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbSlotType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbSlotIndex, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEquipQts))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbLimitTarget, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSelectLmtTags)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtEqpName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbEquipType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbSlotType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbSlotIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEquipQts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbLimitTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(btnSelectLmtTags))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

    private void cmbSlotTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSlotTypeActionPerformed
    {//GEN-HEADEREND:event_cmbSlotTypeActionPerformed
        if (bInitGUI)
        {
            makeSlotIndexCombo();
        }
    }//GEN-LAST:event_cmbSlotTypeActionPerformed

    private void cmbEquipTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbEquipTypeActionPerformed
    {//GEN-HEADEREND:event_cmbEquipTypeActionPerformed
        if (bInitGUI)
        {
            makeSlotTypeCombo();
            makeSlotIndexCombo();
        }
    }//GEN-LAST:event_cmbEquipTypeActionPerformed

    private void btnSelectLmtTagsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelectLmtTagsActionPerformed
    {//GEN-HEADEREND:event_btnSelectLmtTagsActionPerformed
        doSelectTarges();
    }//GEN-LAST:event_btnSelectLmtTagsActionPerformed

    private void cmbLimitTargetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbLimitTargetActionPerformed
    {//GEN-HEADEREND:event_cmbLimitTargetActionPerformed
        doSelectTarTp();
    }//GEN-LAST:event_cmbLimitTargetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSelectLmtTags;
    private javax.swing.JComboBox<String> cmbEquipType;
    private javax.swing.JComboBox<String> cmbLimitTarget;
    private javax.swing.JComboBox<String> cmbSlotIndex;
    private javax.swing.JComboBox<String> cmbSlotType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> lstLmtTags;
    private javax.swing.JTextArea txtEqpDesp;
    private javax.swing.JTextField txtEqpName;
    private javax.swing.JTextField txtEquipQts;
    // End of variables declaration//GEN-END:variables
}
