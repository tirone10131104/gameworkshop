package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.MDIPaneControl;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.foundation.beans.beanDatablockDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanSkillLevel;
import dev.xlin.gameworkshop.progs.foundation.datablockDefine;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockGUIPal;
import dev.xlin.gameworkshop.progs.foundation.skillDefine;
import dev.xlin.gameworkshop.progs.foundation.skillLevel;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.tols.data.wakeup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import net.sf.json.JSONObject;

/**
 *
 * @author 刘祎鹏
 */
public class ifrmSkillLevelEditor extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private beanSkillLevel bean = null;
    private JDesktopPane desk = null;
    skillDefine skdef = null;
    skillLevel sklvl = null;
    private ArrayList atabPals = new ArrayList();

    public ifrmSkillLevelEditor(wakeup _up, beanSkillLevel _bean, JDesktopPane _desk)
    {
        initComponents();
        up = _up;
        bean = _bean;
        desk = _desk;
        skdef = new skillDefine(up);
        sklvl = new skillLevel(up);
        initGUI();
        MDIPaneControl.setMDIFrameOpen(MDIPaneControl.IFRM_SKILL_LEVEL_DATA, bean.getOID() + "");
    }

    private void initGUI()
    {
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        beanSkillDefine bskd = (beanSkillDefine) skdef.getRecordByID(bean.getSkillOID());
        String stt = bskd.getSkillName() + "<" + bskd.getSkillTag() + "> 层级：" + bean.getLevelIdx() + " (" + bean.getLevelName() + ") 数据编辑";
        setTitle(stt);
        initTabs();
    }

    private void initTabs()
    {
        JSONObject jso = JSONObject.fromObject(bean.getDataHeader());
        datablockDefine dbd = new datablockDefine(up);
        Set ks = jso.keySet();
        Iterator itor = ks.iterator();
        while (itor.hasNext())
        {
            String skey = (String) itor.next();
            int defid = Integer.parseInt(skey);
            System.err.println("def.id = " + defid);
            int vid = jso.getInt(skey);
            beanDatablockDefine bdd = (beanDatablockDefine) dbd.getRecordByID(defid);
            try
            {
                Object opal = Class.forName(bdd.getDbPalClass()).newInstance();
                iDatablockGUIPal idp = (iDatablockGUIPal) opal;
                idp.initData(up, vid, 0);
                tabs.addTab(idp.getPalTitle(), (JPanel) idp);
                atabPals.add(idp);
            }
            catch (Exception excp)
            {
                excp.printStackTrace();
                fast.err("["+ bdd.getDbName() +"]数据操作面板载入过程中发生错误.\n" + excp.getLocalizedMessage());
            }
        }
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
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
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
        MDIPaneControl.setMDIFrameClose(MDIPaneControl.IFRM_SKILL_LEVEL_DATA, bean.getOID() + "");
        desk.remove(this);
        desk.repaint();
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
