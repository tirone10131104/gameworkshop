package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanProgIntfRegister;
import dev.xlin.gameworkshop.progs.foundation.interfaceDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaceRegister;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.tols.data.wakeup;
import java.awt.Toolkit;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class DlgSelectImpls extends javax.swing.JDialog
{

    private wakeup up = null;
    private boolean bOK = false;
    private BeanProgIntfRegister bean = null;
    private String intfTag = null;
    private List limpls = null;
    private BeanProgIntfDefine bpidef = null;

    public DlgSelectImpls(java.awt.Frame parent, boolean modal, wakeup _up, String _intfTag)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        intfTag = _intfTag;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        interfaceDefine itdef = new interfaceDefine(up);
        bpidef = itdef.getInterfaceByTag(intfTag);
        if (bpidef == null)
        {
            fast.warn("接口定义不存在");
            bOK = false;
            setVisible(false);
        }
        makeTable();
    }

    private void makeTable()
    {
        myTableModel mtm = new myTableModel();
        mtm.addColumn("标签");
        mtm.addColumn("描述");
        mtm.addColumn("地址");
        interfaceRegister itreg = new interfaceRegister(up);
        limpls = itreg.getRegsByDef(bpidef.getOID(), false);
        if (limpls != null)
        {
            for (int i = 0; i < limpls.size(); i++)
            {
                BeanProgIntfRegister b = (BeanProgIntfRegister) limpls.get(i);
                Object[] os = fast.makeObjectArray(3);
                os[0] = b.getRegTag();
                os[1] = b.getRegDescription();
                os[2] = b.getImplAddress();
                mtm.addRow(os);
            }
        }
        tbImpls.setModel(mtm);
    }

    private void OK()
    {
        if (bean == null)
        {
            return;
        }
        bOK = true;
        setVisible(true);
    }

    private void cancle()
    {
        bean = null;
        bOK = false;
        setVisible(false);
    }

    private void selectImpl()
    {
        int idx = tbImpls.getSelectedRow();
        if (idx < 0)
        {
            bean = null;
            return;
        }
        bean = (BeanProgIntfRegister) limpls.get(idx);
    }

    public BeanProgIntfRegister getSelectedImplement()
    {
        return bean;
    }

    public boolean getOK()
    {
        return bOK;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        txtIntfName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtIntfAddr = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbImpls = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("选择接口实现");

        jLabel1.setText("接口名称");

        txtIntfName.setEditable(false);

        jLabel2.setText("接口地址");

        txtIntfAddr.setEditable(false);

        jLabel3.setText("实现列表");

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

        tbImpls.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbImpls.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbImpls.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                tbImplsMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbImpls);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIntfName))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIntfAddr))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIntfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIntfAddr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
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
        OK();;
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void tbImplsMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbImplsMouseReleased
    {//GEN-HEADEREND:event_tbImplsMouseReleased
        selectImpl();
    }//GEN-LAST:event_tbImplsMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbImpls;
    private javax.swing.JTextField txtIntfAddr;
    private javax.swing.JTextField txtIntfName;
    // End of variables declaration//GEN-END:variables
}
