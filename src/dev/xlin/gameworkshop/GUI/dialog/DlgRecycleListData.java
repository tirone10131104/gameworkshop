package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanRecycleListItemData;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.tols.data.wakeup;
import java.awt.Toolkit;

public class DlgRecycleListData extends javax.swing.JDialog
{

    private wakeup up = null;
    private BeanRecycleListItemData bean = null;
    private boolean bOK = false;
    private boolean bEdit = false;
    private int itemOID = 0;

    public DlgRecycleListData(java.awt.Frame parent, boolean modal, wakeup _up, BeanRecycleListItemData _bean)
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
        if (bean == null)
        {
            bean = new BeanRecycleListItemData();
            bEdit = false;
        }
        else
        {
            txtDescp.setText(bean.getDescription());
            txtQuantity.setText(bean.getQuantity() + "");
            fast.setCheckBoxValue(ckHide, bean.getHide());
            itemDefine idef = new itemDefine(up);
            BeanItem bit = (BeanItem) idef.getRecordByID(bean.getItemOID());
            if (bit == null)
            {
                txtItem.setText("[null]");
            }
            else
            {
                txtItem.setText(bit.getItemName() + "<" + bit.getItemTag() + ">");
            }
            itemOID = bean.getItemOID();
            btnSelItem.setEnabled(false);
            bEdit = true;
        }
    }

    public boolean getOK()
    {
        return bOK;
    }

    public BeanRecycleListItemData getRecycleListItemData()
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

    private void doOK()
    {
        if (itemOID == 0)
        {
            fast.warn("必须选择一个目标物体");
            return;
        }
        bean.setDescription(txtDescp.getText());
        double dqt = fast.testDoubleText(txtQuantity);
        if (dqt == Double.MIN_VALUE)
        {
            fast.warn("必须填写正确的数量值");
            return;
        }
        bean.setQuantity(dqt);
        bean.setItemOID(itemOID);
        bean.setHide(fast.readCheckBox(ckHide));
        bOK = true;
        setVisible(false);
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    private void doSelectItem()
    {
        DlgSelectItemDefine dlg = new DlgSelectItemDefine(null, true, up);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanItem bit = dlg.getSelectedItemDefine();
            itemOID = bit.getOID();
            txtItem.setText(bit.getItemName() + "<" + bit.getItemTag() + ">");
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtItem = new javax.swing.JTextField();
        btnSelItem = new javax.swing.JButton();
        txtQuantity = new javax.swing.JTextField();
        txtDescp = new javax.swing.JTextField();
        ckHide = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("回收物体数据设置");

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

        jLabel1.setText("物体目标");

        jLabel2.setText("物体数量");

        jLabel3.setText("描述文本");

        txtItem.setEditable(false);

        btnSelItem.setText("选择物体");
        btnSelItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelItemActionPerformed(evt);
            }
        });

        ckHide.setText("设置为隐藏数据");

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
                        .addComponent(txtItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelItem))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQuantity))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ckHide)
                                .addGap(0, 400, Short.MAX_VALUE))
                            .addComponent(txtDescp))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelItem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDescp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckHide)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 226, Short.MAX_VALUE)
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

    private void btnSelItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelItemActionPerformed
    {//GEN-HEADEREND:event_btnSelItemActionPerformed
        doSelectItem();
    }//GEN-LAST:event_btnSelItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSelItem;
    private javax.swing.JCheckBox ckHide;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtDescp;
    private javax.swing.JTextField txtItem;
    private javax.swing.JTextField txtQuantity;
    // End of variables declaration//GEN-END:variables
}
