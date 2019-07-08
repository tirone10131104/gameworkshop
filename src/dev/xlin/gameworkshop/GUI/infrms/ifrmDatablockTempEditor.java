package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.MDIPaneControl;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.beanDatablockDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanDatablockTemplet;
import dev.xlin.gameworkshop.progs.foundation.datablockDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockGUIPal;
import dev.xlin.tols.data.wakeup;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;

public class ifrmDatablockTempEditor extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private beanDatablockTemplet bean = null;
    private iAdtDocumentSave isave = null;
    public ifrmDatablockTempEditor(JDesktopPane _desk, wakeup _up, beanDatablockTemplet _bean)
    {
        initComponents();
        up = _up;
        bean = _bean;
        desk = _desk;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        setTitle("[数据区块模板编辑]：" + bean.getTempName());
        MDIPaneControl.setMDIFrameOpen(MDIPaneControl.IFRM_DATABLOCK_TEMP , bean.getOID() + "");
        datablockDefine dbd = new datablockDefine(up);
        try
        {
            beanDatablockDefine bdd = (beanDatablockDefine) dbd.getRecordByID(bean.getDbType());
            Object opal = Class.forName(bdd.getDbPalClass()).newInstance();
            iDatablockGUIPal idp = (iDatablockGUIPal) opal;
            isave = (iAdtDocumentSave) opal;
            idp.initData(up, bean.getDatablock(), bean.getOclsID());
            tabs.addTab(idp.getPalTitle(), (JPanel) idp);
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            fast.err("数据初始化失败");
        }
    }

    private void doClose()
    {
        if (isave.isNeedSave())
        {
            int sel = fast.ask("是否在关闭前保存当前所做的修改？");
            if (sel == fast.YES)
            {
                isave.save();
            }
        }
        MDIPaneControl.setMDIFrameClose(MDIPaneControl.IFRM_DATABLOCK_TEMP,bean.getOID() + "");
        desk.remove(this);
        desk.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        tabs = new javax.swing.JTabbedPane();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 1264, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        doClose();
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
