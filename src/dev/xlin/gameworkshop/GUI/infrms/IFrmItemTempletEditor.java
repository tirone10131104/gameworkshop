package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.MDIPaneControl;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablockDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemTemplet;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.datablockDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockGUIPal;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.codeTools;
import java.util.ArrayList;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import net.sf.json.JSONObject;

public class IFrmItemTempletEditor extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private BeanItemTemplet bean = null;
    private ArrayList atabPals = new ArrayList();

    public IFrmItemTempletEditor(wakeup _up, JDesktopPane _desk, BeanItemTemplet _bean)
    {
        initComponents();
        up = _up;
        desk = _desk;
        bean = _bean;
        initGUI();
    }

    private void doSave(boolean showMsg)
    {
        for (int i = 0; i < atabPals.size(); i++)
        {
            iDatablockGUIPal ipal = (iDatablockGUIPal) atabPals.get(i);
            iAdtDocumentSave iads = (iAdtDocumentSave) ipal;
            iads.save();
        }
        if (showMsg)
        {
            fast.msg("保存完成");
        }
    }

    private void initGUI()
    {
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        setTitle(bean.getTempName());
        initTabs();
        MDIPaneControl.setMDIFrameOpen(MDIPaneControl.IFRM_ITEM_TEMPLET_DATA, bean.getOID() + "");
    }

    private void initTabs()
    {
        JSONObject jso = JSONObject.fromObject(bean.getDatablockHeader());
        objectClassDefine ocd = new objectClassDefine(up);
        BeanObjectClass boc = (BeanObjectClass) ocd.getRecordByID(bean.getOclsID());
        int[] ifs = codeTools.convertStrToArr(boc.getClassFuncs());
        datablockDefine dbd = new datablockDefine(up);
        for (int i = 0; i < ifs.length; i++)
        {
            int fid = ifs[i];
            int dbIndex = jso.getInt(fid + "");
            try
            {
                BeanDatablockDefine bdd = (BeanDatablockDefine) dbd.getRecordByID(fid);
                Object opal = Class.forName(bdd.getDbPalClass()).newInstance();
                iDatablockGUIPal idp = (iDatablockGUIPal) opal;
                idp.initData(up, dbIndex, bean.getOclsID());
                tabs.addTab(idp.getPalTitle(), (JPanel) idp);
                atabPals.add(idp);
            }
            catch (Exception excp)
            {
                excp.printStackTrace();
            }
        }
    }

    private void doTryClose()
    {
        for (int i = 0; i < atabPals.size(); i++)
        {
            iAdtDocumentSave iads = (iAdtDocumentSave) atabPals.get(i);
            if (iads.isNeedSave())
            {
                iDatablockFace idbf = (iDatablockFace) iads;
                int sel = fast.ask("是否保存[" + idbf.getDatablockName() + "]选项卡中的数据？");
                if (sel == fast.YES)
                {
                    iads.save();
                }
            }
        }
        MDIPaneControl.setMDIFrameClose(MDIPaneControl.IFRM_ITEM_TEMPLET_DATA, bean.getOID() + "");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
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

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSave.setText("保存数据");
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSave);
        jToolBar1.add(jSeparator1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1142, Short.MAX_VALUE)
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveActionPerformed
    {//GEN-HEADEREND:event_btnSaveActionPerformed
        doSave(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        doTryClose();
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSave;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
