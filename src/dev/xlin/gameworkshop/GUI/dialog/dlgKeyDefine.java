package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.beanKeyDataChunk;
import dev.xlin.gameworkshop.progs.foundation.beans.beanKeyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.keyDataChunk;
import dev.xlin.gameworkshop.progs.foundation.keyDataDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class dlgKeyDefine extends javax.swing.JDialog
{

    private wakeup up = null;
    private beanKeyDataDefine bean = null;
    private int tpid = 0;
    private boolean bOK = false;
    private boolean bEdit = false;

    public dlgKeyDefine(java.awt.Frame parent, boolean modal, wakeup _up, beanKeyDataDefine _bean, int _tpid)
    {
        super(parent, modal);
        initComponents();
        bean = _bean;
        up = _up;
        tpid = _tpid;
        initGUI();
    }

    private void makeCombo()
    {
        DefaultComboBoxModel modDtp = new DefaultComboBoxModel();
        int[] ids = constChk.getFinalInts(iConst.class, "KDT_DTP_");
        for (int i = 0; i < ids.length; i++)
        {
            int id = ids[i];
            listItem li = new listItem(iConst.translate(id), id);
            modDtp.addElement(li);
        }
        cmbDataType.setModel(modDtp);
        DefaultComboBoxModel modChk = new DefaultComboBoxModel();
        keyDataChunk kdc = new keyDataChunk(up);
        List lks = kdc.getAllRecord();
        if (lks!= null)
        {
            for (int i = 0; i < lks.size(); i++)
            {
                beanKeyDataChunk bkdc = (beanKeyDataChunk) lks.get(i);
                listItem li = new listItem(bkdc.getChunkName(), bkdc.getOID());
                modChk.addElement(li);
            }
        }
        cmbDataChunk.setModel(modChk);
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        makeCombo();
        if (bean == null)
        {
            bean = new beanKeyDataDefine();
            bEdit = false;
        }
        else
        {
            txtDesp.setText(bean.getKeyDesp());
            txtName.setText(bean.getKeyName());
            txtTag.setText(bean.getKeyTag());
            fast.setCheckBoxValue(ckCacheLoad, bean.getCacheLoad());
            swsys.doSelectCombo(cmbDataType, bean.getDataType());
            swsys.doSelectCombo(cmbDataChunk, bean.getDataChunkID());
            txtTag.setEditable(false);
            bEdit = true;
        }
    }
    
    private void doOK ()
    {
        bean.setCacheLoad(fast.readCheckBox(ckCacheLoad));
        bean.setKeyTag(txtTag.getText().trim());
        bean.setKeyName(txtName.getText().trim());
        bean.setKeyDesp(txtDesp.getText().trim());
        bean.setDataChunkID(fast.readCombo(cmbDataChunk));
        bean.setDataType(fast.readCombo(cmbDataType));
        if (bean.getKeyTag().equals(""))
        {
            fast.warn("标签必须填写");
            return ;
        }
        if (bean.getKeyName().equals(""))
        {
            fast.warn("名称必须填写");
            return ;
        }
        keyDataDefine kdd = new keyDataDefine(up);
        int r = 0;
        if (bEdit)
        {
            r = kdd.modifyRecord(bean, false);
        }
        else
        {
            bean.setTypeOID(tpid);
            r = kdd.createRecord(bean, false);
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
    
    private void doCancle ()
    {
        bOK= false;
        setVisible(false);
    }
    
    public boolean getOK ()
    {
        return bOK;
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
        cmbDataChunk = new javax.swing.JComboBox<>();
        cmbDataType = new javax.swing.JComboBox<>();
        txtName = new javax.swing.JTextField();
        txtTag = new javax.swing.JTextField();
        txtDesp = new javax.swing.JTextField();
        ckCacheLoad = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("键值数据定义");

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

        jLabel1.setText("键值名称");

        jLabel2.setText("键值标签");

        jLabel3.setText("键值描述");

        jLabel4.setText("数据类型");

        jLabel5.setText("数据区块");

        ckCacheLoad.setText("预读取");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 320, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbDataType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDesp))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTag))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ckCacheLoad)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cmbDataChunk, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbDataType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbDataChunk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckCacheLoad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JCheckBox ckCacheLoad;
    private javax.swing.JComboBox<String> cmbDataChunk;
    private javax.swing.JComboBox<String> cmbDataType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTag;
    // End of variables declaration//GEN-END:variables
}
