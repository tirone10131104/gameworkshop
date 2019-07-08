package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.MDIPaneControl;
import dev.xlin.gameworkshop.GUI.dialog.DlgItemTempletInfo;
import dev.xlin.gameworkshop.GUI.dialog.DlgItemTempletSelector;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablock;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablockDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemTemplet;
import dev.xlin.gameworkshop.progs.foundation.datablockDefine;
import dev.xlin.gameworkshop.progs.foundation.datablockService;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.codeTools;
import java.util.ArrayList;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockGUIPal;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.itemTempletService;
import dev.xlin.tols.interfaces.iDAO;
import net.sf.json.JSONObject;

public class IFrmItemDefineDataEditor extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private BeanItem bean = null;
    private JDesktopPane desk = null;
    private IFrmItemDefineMgr imgr = null;
    private ArrayList atabPals = new ArrayList();

    public IFrmItemDefineDataEditor(wakeup _up, BeanItem _bean, JDesktopPane _desk, IFrmItemDefineMgr _imgr)
    {
        initComponents();
        up = _up;
        bean = _bean;
        desk = _desk;
        imgr = _imgr;
        initGUI();
        MDIPaneControl.setMDIFrameOpen(MDIPaneControl.IFRM_ITEM_DEFINE_DATA, bean.getOID() + "");
    }

    private void initGUI()
    {
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        String stt = bean.getItemName() + "<" + bean.getItemTag() + ">" + "  - 数据编辑器";
        setTitle(stt);
        initTabs();
    }

    private void initTabs()
    {
        //数据获取来源变更
        datablockService dbsrv = new datablockService(up);
        BeanDatablock bdt = dbsrv.getDatabean(bean.getEquipData());
        JSONObject jso = JSONObject.fromObject(bdt.getDatablock());
        objectClassDefine ocd = new objectClassDefine(up);
        BeanObjectClass boc = (BeanObjectClass) ocd.getRecordByID(bean.getOclsID());
        int[] ifs = codeTools.convertStrToArr(boc.getClassFuncs());
        datablockDefine dbd = new datablockDefine(up);
        for (int i = 0; i < ifs.length; i++)
        {
            int fid = ifs[i];
            long dbIndex = jso.getLong(fid + "");
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

    private void doImportItemTemplet()
    {
        DlgItemTempletSelector dlg = new DlgItemTempletSelector(null, true, up, bean.getOclsID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            int sel = fast.ask("是否要导入这个模板，这个操作会清空已经有的设置数据。");
            if (sel != fast.YES)
            {
                return;
            }
            BeanItemTemplet bit = dlg.getTemplet();
            itemTempletService its = new itemTempletService(up);
            int r = its.setItemFromTemplet(bean.getOID(), bit.getOID());
            if (r == iDAO.OPERATE_SUCCESS)
            {
                tabs.removeAll();
                itemDefine idef = new itemDefine(up);
                bean = (BeanItem) idef.getRecordByID(bean.getOID());
                initTabs();
                fast.msg("导入模板操作成功");
                imgr.postItemInfoUpdated();
            }
            else
            {
                fast.err("导入模板出现错误", r);
            }
        }
        dlg.dispose();
        dlg = null;
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

    private void doSaveAsTemplet()
    {
        doSave(false);
        DlgItemTempletInfo dlg = new DlgItemTempletInfo(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanItemTemplet bit = dlg.getItemTemplet();
            itemTempletService its = new itemTempletService(up);
            int r = its.saveAsTemplet(bean.getOID(), bit.getTempName(), bit.getTempDesp(), dlg.getClearValue());
            if (r == iDAO.OPERATE_SUCCESS)
            {
                fast.msg("物体存储为模板操作成功");
            }
            else
            {
                fast.err("物体存储为模板操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        tabs = new javax.swing.JTabbedPane();
        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnSaveAsTemplet = new javax.swing.JButton();
        btnImportTemplet = new javax.swing.JButton();

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

        btnSaveAsTemplet.setText("存为模板");
        btnSaveAsTemplet.setFocusable(false);
        btnSaveAsTemplet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveAsTemplet.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveAsTemplet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveAsTempletActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSaveAsTemplet);

        btnImportTemplet.setText("导入模板");
        btnImportTemplet.setFocusable(false);
        btnImportTemplet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImportTemplet.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImportTemplet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnImportTempletActionPerformed(evt);
            }
        });
        jToolBar1.add(btnImportTemplet);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
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
        MDIPaneControl.setMDIFrameClose(MDIPaneControl.IFRM_ITEM_DEFINE_DATA, bean.getOID() + "");
        desk.remove(this);
        desk.repaint();
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnImportTempletActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnImportTempletActionPerformed
    {//GEN-HEADEREND:event_btnImportTempletActionPerformed
        doImportItemTemplet();
    }//GEN-LAST:event_btnImportTempletActionPerformed

    private void btnSaveAsTempletActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveAsTempletActionPerformed
    {//GEN-HEADEREND:event_btnSaveAsTempletActionPerformed
        doSaveAsTemplet();
    }//GEN-LAST:event_btnSaveAsTempletActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveActionPerformed
    {//GEN-HEADEREND:event_btnSaveActionPerformed
        doSave(true);
    }//GEN-LAST:event_btnSaveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnImportTemplet;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveAsTemplet;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
