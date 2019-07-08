package dev.xlin.gameworkshop.GUI;

import dev.xlin.gameworkshop.GUI.dialog.dlgSelectIntfImplsByISET;
import dev.xlin.gameworkshop.GUI.techTest.runtest;
import dev.xlin.gameworkshop.progs.databaseTools;
import dev.xlin.gameworkshop.progs.foundation.beans.beanProgIntfRegister;
import dev.xlin.tols.data.wakeup;

public class frmGUITest extends javax.swing.JDialog
{

    private wakeup up = null;

    public frmGUITest(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        up = databaseTools.connectDB();
    }

    private void test1()
    {
        dlgSelectIntfImplsByISET dlg = new dlgSelectIntfImplsByISET(null, true, up, "");
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            beanProgIntfRegister bpir = dlg.getSelectedReg();
            fast.msg(bpir.getRegDescription() + " adr= " + bpir.getImplAddress());
        }
        dlg.dispose();
        dlg = null;
    }
    private runtest rts = null;

    private void startYieldThread()
    {
        if (rts != null)
        {
            fast.warn("启动测试过一次啦");
            return;
        }
        rts = new runtest();
        Thread t = new Thread(rts);
        t.start();
    }

    private void stopYieldThread()
    {
        if (rts == null )
        {
            return ; 
        }
        rts.reqeustStop();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnTest1 = new javax.swing.JButton();
        btnTestYield = new javax.swing.JButton();
        btnStopYield = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnTest1.setText("测试dlgSelectIntfImplsByISET");
        btnTest1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTest1ActionPerformed(evt);
            }
        });

        btnTestYield.setText("yield");
        btnTestYield.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTestYieldActionPerformed(evt);
            }
        });

        btnStopYield.setText("gaga");
        btnStopYield.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnStopYieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTest1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTestYield)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStopYield)))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTest1)
                .addGap(81, 81, 81)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTestYield)
                    .addComponent(btnStopYield))
                .addContainerGap(163, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTest1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTest1ActionPerformed
    {//GEN-HEADEREND:event_btnTest1ActionPerformed
        test1();
    }//GEN-LAST:event_btnTest1ActionPerformed

    private void btnTestYieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTestYieldActionPerformed
    {//GEN-HEADEREND:event_btnTestYieldActionPerformed
        startYieldThread();
    }//GEN-LAST:event_btnTestYieldActionPerformed

    private void btnStopYieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnStopYieldActionPerformed
    {//GEN-HEADEREND:event_btnStopYieldActionPerformed
        stopYieldThread();
    }//GEN-LAST:event_btnStopYieldActionPerformed
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(frmGUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmGUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmGUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmGUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                frmGUITest dialog = new frmGUITest(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStopYield;
    private javax.swing.JButton btnTest1;
    private javax.swing.JButton btnTestYield;
    // End of variables declaration//GEN-END:variables
}
