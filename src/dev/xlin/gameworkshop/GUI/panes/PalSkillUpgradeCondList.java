package dev.xlin.gameworkshop.GUI.panes;

import dev.xlin.gameworkshop.GUI.dialog.DlgSkillUpgradeCondition;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiDBLKPalCode;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanSkillUpgradeCondition;
import dev.xlin.gameworkshop.progs.foundation.datablockService;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataBean;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockGUIPal;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.keyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.gameworkshop.progs.foundation.skillDefine;
import dev.xlin.gameworkshop.progs.foundation.skillUpgradeDataList;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import dev.xlin.tools.constChk;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class PalSkillUpgradeCondList extends javax.swing.JPanel implements iAdtDocumentSave, iDatablockFace, iDatablockGUIPal
{

    private skillUpgradeDataList sudl = null;
    private wakeup up = null;
    private long dbIndex = 0;
    private boolean needSave = false;
    private int oclsID = 0;
    private propertyDefine pdef = null;
    private keyDataDefine kdef = null;
    private itemDefine idef = null;
    private List ludl = null;
    private boolean bInitTable = false;

    private void initData()
    {
        pdef = new propertyDefine(up);
        kdef = new keyDataDefine(up);
        idef = new itemDefine(up);
        datablockService dbs = new datablockService(up);
        String sblock = dbs.loadData(dbIndex);
        xmlRight xr = new xmlRight();
        boolean b = xr.parseXMLfromString(sblock);
        if (b)
        {
            boolean b2 = sudl.revertFromXML(xr);
        }
        else
        {
        }
    }

    public PalSkillUpgradeCondList()
    {
        initComponents();
    }

    private void doSave(boolean showMsg)
    {
        datablockService dbs = new datablockService(up);
        xmlRight xr = sudl.transToXML();
        String sxml = xr.transformToString();
        int r = dbs.updateData(dbIndex, sxml);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            sudl.resetSave();
            if (showMsg)
            {
                fast.msg("保存完成");
            }
        }
        else
        {
            if (showMsg)
            {
                fast.err("保存失败", r);
            }
        }
    }

    private void makeTable()
    {
        int tpid = swsys.getComboBoxSelected(cmbType);
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("目标类型");
            mtm.addColumn("目标");
            mtm.addColumn("方法");
            mtm.addColumn("数量");
            mtm.addColumn("隐藏");
            mtm.addColumn("状态");
            tbUDL.setModel(mtm);
            bInitTable = true;
        }
        else
        {
            fast.clearTableModel(tbUDL);
            mtm = (myTableModel) tbUDL.getModel();
        }
        ludl = sudl.getAllDataElements(ckShowAll.isSelected());
        if (ludl != null)
        {
            for (int i = 0; i < ludl.size(); i++)
            {
                BeanSkillUpgradeCondition bsuc = (BeanSkillUpgradeCondition) ludl.get(i);
                if (tpid != 0 && tpid != bsuc.getTargetType())
                {
                    continue;
                }
                Object[] ros = fast.makeObjectArray(6);
                ros[0] = iConst.translate(bsuc.getTargetType());
                String s = guiCodes.makeTargetBeanDesp(up, bsuc.getTargetType(), bsuc.getTargetOID());
                ros[1] = s;
                ros[2] = iConst.translate(bsuc.getMethod());
                ros[3] = bsuc.getValue() + "";
                ros[4] = iConst.translateIBOL(bsuc.getHide());
                ros[5] = iConst.transDAOState(bsuc.getStatus());
                mtm.addRow(ros);
            }
        }
        tbUDL.setModel(mtm);
    }

    private void makeFltCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("[全部]", 0));
        int[] ids = constChk.getFinalInts(iConst.class, "SKL_UPG_TYPE_");
        for (int i = 0; i < ids.length; i++)
        {
            int id = ids[i];
            mod.addElement(new listItem(iConst.translate(id), id));
        }
        cmbType.setModel(mod);
    }

    private void newCond()
    {
        DlgSkillUpgradeCondition dlg = new DlgSkillUpgradeCondition(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            int r = sudl.appendDataElement(dlg.getBean(), 0);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTable();
            }
            else
            {
                fast.err("添加数据操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanSkillUpgradeCondition getSelectedCond()
    {
        int idx = tbUDL.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanSkillUpgradeCondition) ludl.get(idx);
    }

    private void editCond()
    {
        BeanSkillUpgradeCondition bean = getSelectedCond();
        if (bean == null)
        {
            return;
        }
        DlgSkillUpgradeCondition dlg = new DlgSkillUpgradeCondition(null, true, up, bean);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanSkillUpgradeCondition brst = dlg.getBean();
            int r = sudl.updateDataElement(brst);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTable();
            }
            else
            {
                fast.err("修改操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void disableCond()
    {
        BeanSkillUpgradeCondition bean = getSelectedCond();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要设置这个节点为失效状态");
        if (sel != fast.YES)
        {
            return;
        }
        int r = sudl.disableDataElement(bean);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void revertCond()
    {
        BeanSkillUpgradeCondition bean = getSelectedCond();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要设置这个节点恢复为有效状态");
        if (sel != fast.YES)
        {
            return;
        }
        int r = sudl.revertDataElement(bean);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void destroyCond()
    {
        BeanSkillUpgradeCondition bean = getSelectedCond();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要销毁这个节点");
        if (sel != fast.YES)
        {
            return;
        }
        int r = sudl.destroyDataElement(bean);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void moveCondUp()
    {
        BeanSkillUpgradeCondition bean = getSelectedCond();
        if (bean == null)
        {
            return;
        }
        boolean b = sudl.moveDataUp(bean.getOID());
        if (b)
        {
            makeTable();
        }
    }

    private void moveCondDown()
    {
        BeanSkillUpgradeCondition bean = getSelectedCond();
        if (bean == null)
        {
            return;
        }
        boolean b = sudl.moveDataDown(bean.getOID());
        if (b)
        {
            makeTable();
        }
    }

    private void doSaveAsTemplet()
    {
        int r = guiDBLKPalCode.guiSaveAsTemplet(up, oclsID, sudl, dbIndex);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            fast.msg("已经将这个数据配置保存为模板");
        }
        else
        {
            fast.err("保存模板过程操作失败或者未完成", r);
        }
    }

    private void doImportTemplet()
    {
        boolean b = guiDBLKPalCode.guiImportTemplet(up, sudl);
        if (b)
        {
            makeTable();
        }
        else
        {
            fast.err("导入数据操作失败或者未完成");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popSU = new javax.swing.JPopupMenu();
        miNewCond = new javax.swing.JMenuItem();
        miEditCond = new javax.swing.JMenuItem();
        miDisableCond = new javax.swing.JMenuItem();
        miRevertCond = new javax.swing.JMenuItem();
        miDestroyCond = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miMoveUp = new javax.swing.JMenuItem();
        miMoveDown = new javax.swing.JMenuItem();
        jToolBar1 = new javax.swing.JToolBar();
        ckShowAll = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        cmbType = new javax.swing.JComboBox<>();
        btnAdd = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnImportTemplet = new javax.swing.JButton();
        btnSaveAsTemplet = new javax.swing.JButton();
        scpTbUDL = new javax.swing.JScrollPane();
        tbUDL = new javax.swing.JTable();
        jToolBar2 = new javax.swing.JToolBar();
        btnPop = new javax.swing.JButton();

        miNewCond.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewCond.setText("新建条件");
        miNewCond.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewCondActionPerformed(evt);
            }
        });
        popSU.add(miNewCond);

        miEditCond.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditCond.setText("修改条件");
        miEditCond.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditCondActionPerformed(evt);
            }
        });
        popSU.add(miEditCond);

        miDisableCond.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisableCond.setText("失效条件");
        miDisableCond.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisableCondActionPerformed(evt);
            }
        });
        popSU.add(miDisableCond);

        miRevertCond.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevertCond.setText("恢复条件");
        miRevertCond.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevertCondActionPerformed(evt);
            }
        });
        popSU.add(miRevertCond);

        miDestroyCond.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDestroyCond.setText("销毁条件");
        miDestroyCond.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyCondActionPerformed(evt);
            }
        });
        popSU.add(miDestroyCond);
        popSU.add(jSeparator2);

        miMoveUp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveUp.setText("上移");
        miMoveUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveUpActionPerformed(evt);
            }
        });
        popSU.add(miMoveUp);

        miMoveDown.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveDown.setText("下移");
        miMoveDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveDownActionPerformed(evt);
            }
        });
        popSU.add(miMoveDown);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        ckShowAll.setText("显示全部数据");
        ckShowAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllActionPerformed(evt);
            }
        });
        jToolBar1.add(ckShowAll);
        jToolBar1.add(jSeparator3);

        jLabel1.setText("分类");
        jToolBar1.add(jLabel1);

        cmbType.setMaximumSize(new java.awt.Dimension(130, 32767));
        cmbType.setMinimumSize(new java.awt.Dimension(130, 28));
        cmbType.setPreferredSize(new java.awt.Dimension(130, 28));
        cmbType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTypeActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbType);

        btnAdd.setText("添加数据");
        btnAdd.setFocusable(false);
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAdd);

        btnSave.setText("立即保存");
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

        scpTbUDL.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scpTbUDLMouseClicked(evt);
            }
        });

        tbUDL.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbUDLMouseClicked(evt);
            }
        });
        scpTbUDL.setViewportView(tbUDL);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnPop.setForeground(new java.awt.Color(0, 0, 204));
        btnPop.setText("↓");
        btnPop.setFocusable(false);
        btnPop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPop.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPopMouseClicked(evt);
            }
        });
        btnPop.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopActionPerformed(evt);
            }
        });
        jToolBar2.add(btnPop);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(scpTbUDL, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(scpTbUDL, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jToolBar1, jToolBar2});

    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveActionPerformed
    {//GEN-HEADEREND:event_btnSaveActionPerformed
        doSave(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnImportTempletActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnImportTempletActionPerformed
    {//GEN-HEADEREND:event_btnImportTempletActionPerformed
        doImportTemplet();
    }//GEN-LAST:event_btnImportTempletActionPerformed

    private void btnSaveAsTempletActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveAsTempletActionPerformed
    {//GEN-HEADEREND:event_btnSaveAsTempletActionPerformed
        doSaveAsTemplet();
    }//GEN-LAST:event_btnSaveAsTempletActionPerformed

    private void ckShowAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllActionPerformed
    {//GEN-HEADEREND:event_ckShowAllActionPerformed
        makeTable();
    }//GEN-LAST:event_ckShowAllActionPerformed

    private void cmbTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTypeActionPerformed
    {//GEN-HEADEREND:event_cmbTypeActionPerformed
        makeTable();
    }//GEN-LAST:event_cmbTypeActionPerformed

    private void btnPopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPopActionPerformed
    {//GEN-HEADEREND:event_btnPopActionPerformed

    }//GEN-LAST:event_btnPopActionPerformed

    private void tbUDLMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbUDLMouseClicked
    {//GEN-HEADEREND:event_tbUDLMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popSU.show(tbUDL, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbUDLMouseClicked

    private void scpTbUDLMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scpTbUDLMouseClicked
    {//GEN-HEADEREND:event_scpTbUDLMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popSU.show(scpTbUDL, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_scpTbUDLMouseClicked

    private void btnPopMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopMouseClicked
    {//GEN-HEADEREND:event_btnPopMouseClicked
        if (evt.getButton() == evt.BUTTON1)
        {
            popSU.show(btnPop, evt.getX(), popSU.getY() + btnPop.getHeight());
        }
    }//GEN-LAST:event_btnPopMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddActionPerformed
    {//GEN-HEADEREND:event_btnAddActionPerformed
        newCond();
    }//GEN-LAST:event_btnAddActionPerformed

    private void miNewCondActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewCondActionPerformed
    {//GEN-HEADEREND:event_miNewCondActionPerformed
        newCond();
    }//GEN-LAST:event_miNewCondActionPerformed

    private void miEditCondActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditCondActionPerformed
    {//GEN-HEADEREND:event_miEditCondActionPerformed
        editCond();
    }//GEN-LAST:event_miEditCondActionPerformed

    private void miDisableCondActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisableCondActionPerformed
    {//GEN-HEADEREND:event_miDisableCondActionPerformed
        disableCond();
    }//GEN-LAST:event_miDisableCondActionPerformed

    private void miRevertCondActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertCondActionPerformed
    {//GEN-HEADEREND:event_miRevertCondActionPerformed
        revertCond();
    }//GEN-LAST:event_miRevertCondActionPerformed

    private void miDestroyCondActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyCondActionPerformed
    {//GEN-HEADEREND:event_miDestroyCondActionPerformed
        destroyCond();
    }//GEN-LAST:event_miDestroyCondActionPerformed

    private void miMoveUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveUpActionPerformed
    {//GEN-HEADEREND:event_miMoveUpActionPerformed
        moveCondUp();
    }//GEN-LAST:event_miMoveUpActionPerformed

    private void miMoveDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveDownActionPerformed
    {//GEN-HEADEREND:event_miMoveDownActionPerformed
        moveCondDown();
    }//GEN-LAST:event_miMoveDownActionPerformed

    @Override
    public boolean isNeedSave()
    {
        return sudl.isNeedSave();
    }

    @Override
    public void resetSave()
    {
        sudl.resetSave();
    }

    @Override
    public void save()
    {
        doSave(false);
    }

    @Override
    public String getDatablockServiceTag()
    {
        return sudl.getDatablockServiceTag();
    }

    @Override
    public String getDatablockName()
    {
        return sudl.getDatablockName();
    }

    @Override
    public void initData(wakeup _up, long _dbOID, int _oclsID)
    {
        up = _up;
        dbIndex = _dbOID;
        oclsID = _oclsID;
        sudl = new skillUpgradeDataList();
        sudl.initDatablcok(up);
        initData();
        makeFltCombo();
        makeTable();
    }

    @Override
    public String getPalTitle()
    {
        return sudl.getDatablockName();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnImportTemplet;
    private javax.swing.JButton btnPop;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveAsTemplet;
    private javax.swing.JCheckBox ckShowAll;
    private javax.swing.JComboBox<String> cmbType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JMenuItem miDestroyCond;
    private javax.swing.JMenuItem miDisableCond;
    private javax.swing.JMenuItem miEditCond;
    private javax.swing.JMenuItem miMoveDown;
    private javax.swing.JMenuItem miMoveUp;
    private javax.swing.JMenuItem miNewCond;
    private javax.swing.JMenuItem miRevertCond;
    private javax.swing.JPopupMenu popSU;
    private javax.swing.JScrollPane scpTbUDL;
    private javax.swing.JTable tbUDL;
    // End of variables declaration//GEN-END:variables
}
