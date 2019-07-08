package dev.xlin.gameworkshop.GUI.panes;

import dev.xlin.gameworkshop.GUI.dialog.DlgItemEquipConfig;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemEquipConfig;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemEquipStruct;
import dev.xlin.gameworkshop.progs.foundation.datablockService;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockGUIPal;
import dev.xlin.gameworkshop.progs.foundation.itemEquipConfigList;
import dev.xlin.gameworkshop.progs.foundation.itemEquipStruct;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import java.util.List;
import java.util.Vector;

public class PalItemEquipConfigList extends javax.swing.JPanel implements iAdtDocumentSave, iDatablockFace, iDatablockGUIPal
{

    private wakeup up = null;
    private long dbIndex = 0;
    private boolean needSave = false;
    private boolean bInitTable = false;
    private itemEquipConfigList iecl = null;
    private int oclsID = 0;
    private List liecs = null;
    private List lecst = null;

    public PalItemEquipConfigList()
    {
        initComponents();
    }

    private void initData()
    {
        datablockService dbs = new datablockService(up);
        String sblock = dbs.loadData(dbIndex);
        xmlRight xr = new xmlRight();
        boolean b = xr.parseXMLfromString(sblock);
        if (b)
        {
            boolean b2 = iecl.revertFromXML(xr);
            if (b2)
            {
                makeTable();
            }
        }
        else
        {
        }
    }

    private void makeTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("配置名称");
            mtm.addColumn("配置描述");
            mtm.addColumn("装配目标");
            mtm.addColumn("装配类型");
            mtm.addColumn("装配位序");
            mtm.addColumn("装配数量");
            mtm.addColumn("限制类型");
            mtm.addColumn("限制目标");
            mtm.addColumn("状态");
            tbCfgs.setModel(mtm);
            bInitTable = true;
            itemEquipStruct ies = new itemEquipStruct(up);
            lecst = ies.getAllRecord();
        }
        else
        {
            fast.clearTableModel(tbCfgs);
            mtm = (myTableModel) tbCfgs.getModel();
        }
        liecs = iecl.getAllDataElements(ckShowAll.isSelected());
        if (liecs != null)
        {
            for (int i = 0; i < liecs.size(); i++)
            {
                BeanItemEquipConfig biec = (BeanItemEquipConfig) liecs.get(i);
                Vector v = new Vector();
                v.add(biec.getSlotName());
                v.add(biec.getSlotDesp());
                v.add(findEquipStructNode(biec.getEquipType()));
                v.add(findEquipStructNode(biec.getSlotType()));
                v.add(findEquipStructNode(biec.getSlotIndex()));
                v.add(biec.getSlotQuantity());
                if (biec.getLimitTarget() != 0)
                {
                    v.add(iConst.translate(biec.getLimitTarget()));
                }
                else
                {
                    v.add("不限制");
                }
                if (biec.getLimitTargetOIDS().equals(""))
                {
                    v.add("无目标");
                }
                else
                {
                    v.add("有目标");
                }
                v.add(iConst.transDAOState(biec.getStatus()));
                mtm.addRow(v);
            }
        }
        tbCfgs.setModel(mtm);
    }

    private String findEquipStructNode(int sid)
    {
        if (lecst == null)
        {
            return "<空>";
        }
        for (int i = 0; i < lecst.size(); i++)
        {
            BeanItemEquipStruct bean = (BeanItemEquipStruct) lecst.get(i);
            if (bean.getOID() == sid)
            {
                return bean.getEquipName();
            }
        }
        return "<空>";
    }

    private void doSave(boolean showMsg)
    {
        datablockService dbs = new datablockService(up);
        xmlRight xr = iecl.transToXML();
        String sxml = xr.transformToString();
        int r = dbs.updateData(dbIndex, sxml);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            iecl.resetSave();
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

    private void doNewEquip()
    {
        DlgItemEquipConfig dlg = new DlgItemEquipConfig(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanItemEquipConfig bean = dlg.getConfigBean();
            int r = iecl.appendDataElement(bean, 0);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTable();
                fast.msg("添加设置操作完成");
            }
            else
            {
                fast.err("添加设置操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanItemEquipConfig getSelectedConfig()
    {
        int idx = tbCfgs.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        BeanItemEquipConfig bean = (BeanItemEquipConfig) liecs.get(idx);
        return bean;
    }

    private void doEditEquip()
    {
        BeanItemEquipConfig bean = getSelectedConfig();
        if (bean == null)
        {
            return;
        }
        DlgItemEquipConfig dlg = new DlgItemEquipConfig(null, true, up, bean);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanItemEquipConfig bcfg = dlg.getConfigBean();
            int r = iecl.updateDataElement(bcfg);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTable();
                fast.msg("修改设置操作完成");
            }
            else
            {
                fast.err("修改设置操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDisEqp()
    {
        BeanItemEquipConfig bean = getSelectedConfig();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否设置为失效状态？");
        if (sel != fast.YES)
        {
            return;
        }
        int r = iecl.disableDataElement(bean);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
            fast.warn("失效操作完成");
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doRevEqp()
    {
        BeanItemEquipConfig bean = getSelectedConfig();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否恢复为有效状态？");
        if (sel != fast.YES)
        {
            return;
        }
        int r = iecl.revertDataElement(bean);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
            fast.warn("恢复操作完成");
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void doDesEqp()
    {
        BeanItemEquipConfig bean = getSelectedConfig();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否销毁选择的配置数据，这个操作无法被恢复？");
        if (sel != fast.YES)
        {
            return;
        }
        int r = iecl.destroyDataElement(bean);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
            fast.warn("销毁操作完成");
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void doMoveUp()
    {
        BeanItemEquipConfig bean = getSelectedConfig();
        if (bean == null)
        {
            return;
        }
        boolean b = iecl.moveDataUp(bean.getOID());
        if (b)
        {
            makeTable();
        }
    }

    private void doMoveDown()
    {
        BeanItemEquipConfig bean = getSelectedConfig();
        if (bean == null)
        {
            return;
        }
        boolean b = iecl.moveDataDown(bean.getOID());
        if (b)
        {
            makeTable();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popIEQ = new javax.swing.JPopupMenu();
        miNewEqp = new javax.swing.JMenuItem();
        miEditEqp = new javax.swing.JMenuItem();
        miDisEqp = new javax.swing.JMenuItem();
        miRevEqp = new javax.swing.JMenuItem();
        miDesEqp = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miMoveUp = new javax.swing.JMenuItem();
        miMoveDown = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCfgs = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        ckShowAll = new javax.swing.JCheckBox();
        btnNewEquip = new javax.swing.JButton();
        btnFastSave = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        btnPop = new javax.swing.JButton();

        miNewEqp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewEqp.setText("新建装配");
        miNewEqp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewEqpActionPerformed(evt);
            }
        });
        popIEQ.add(miNewEqp);

        miEditEqp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditEqp.setText("修改装配");
        miEditEqp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditEqpActionPerformed(evt);
            }
        });
        popIEQ.add(miEditEqp);

        miDisEqp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisEqp.setText("失效装配");
        miDisEqp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisEqpActionPerformed(evt);
            }
        });
        popIEQ.add(miDisEqp);

        miRevEqp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevEqp.setText("恢复装配");
        miRevEqp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevEqpActionPerformed(evt);
            }
        });
        popIEQ.add(miRevEqp);

        miDesEqp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesEqp.setText("销毁装配");
        miDesEqp.setToolTipText("");
        miDesEqp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesEqpActionPerformed(evt);
            }
        });
        popIEQ.add(miDesEqp);
        popIEQ.add(jSeparator1);

        miMoveUp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveUp.setText("上移");
        miMoveUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveUpActionPerformed(evt);
            }
        });
        popIEQ.add(miMoveUp);

        miMoveDown.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveDown.setText("下移");
        miMoveDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveDownActionPerformed(evt);
            }
        });
        popIEQ.add(miMoveDown);

        tbCfgs.setModel(new javax.swing.table.DefaultTableModel(
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
        tbCfgs.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbCfgs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbCfgs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbCfgsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbCfgs);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        ckShowAll.setText("显示全部");
        ckShowAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllActionPerformed(evt);
            }
        });
        jToolBar1.add(ckShowAll);

        btnNewEquip.setText("添加装配");
        btnNewEquip.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewEquipActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNewEquip);

        btnFastSave.setText("快速保存");
        btnFastSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFastSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFastSave);

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

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
        jToolBar4.add(btnPop);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 885, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jToolBar1, jToolBar4});

    }// </editor-fold>//GEN-END:initComponents

    private void ckShowAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllActionPerformed
    {//GEN-HEADEREND:event_ckShowAllActionPerformed
        makeTable();
    }//GEN-LAST:event_ckShowAllActionPerformed

    private void btnNewEquipActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewEquipActionPerformed
    {//GEN-HEADEREND:event_btnNewEquipActionPerformed
        doNewEquip();
    }//GEN-LAST:event_btnNewEquipActionPerformed

    private void miNewEqpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewEqpActionPerformed
    {//GEN-HEADEREND:event_miNewEqpActionPerformed
        doNewEquip();
    }//GEN-LAST:event_miNewEqpActionPerformed

    private void miEditEqpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditEqpActionPerformed
    {//GEN-HEADEREND:event_miEditEqpActionPerformed
        doEditEquip();
    }//GEN-LAST:event_miEditEqpActionPerformed

    private void tbCfgsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbCfgsMouseClicked
    {//GEN-HEADEREND:event_tbCfgsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popIEQ.show(tbCfgs, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbCfgsMouseClicked

    private void miDisEqpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisEqpActionPerformed
    {//GEN-HEADEREND:event_miDisEqpActionPerformed
        doDisEqp();
    }//GEN-LAST:event_miDisEqpActionPerformed

    private void miRevEqpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevEqpActionPerformed
    {//GEN-HEADEREND:event_miRevEqpActionPerformed
        doRevEqp();
    }//GEN-LAST:event_miRevEqpActionPerformed

    private void miDesEqpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesEqpActionPerformed
    {//GEN-HEADEREND:event_miDesEqpActionPerformed
        doDesEqp();
    }//GEN-LAST:event_miDesEqpActionPerformed

    private void miMoveUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveUpActionPerformed
    {//GEN-HEADEREND:event_miMoveUpActionPerformed
        doMoveUp();
    }//GEN-LAST:event_miMoveUpActionPerformed

    private void miMoveDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveDownActionPerformed
    {//GEN-HEADEREND:event_miMoveDownActionPerformed
        doMoveDown();
    }//GEN-LAST:event_miMoveDownActionPerformed

    private void btnFastSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFastSaveActionPerformed
    {//GEN-HEADEREND:event_btnFastSaveActionPerformed
        doSave(true);
    }//GEN-LAST:event_btnFastSaveActionPerformed

    private void btnPopMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopMouseClicked
    {//GEN-HEADEREND:event_btnPopMouseClicked
        if (evt.getButton() == evt.BUTTON1)
        {
            popIEQ.show(btnPop, evt.getX(), popIEQ.getY() + btnPop.getHeight());
        }
    }//GEN-LAST:event_btnPopMouseClicked

    private void btnPopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPopActionPerformed
    {//GEN-HEADEREND:event_btnPopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPopActionPerformed

    @Override
    public boolean isNeedSave()
    {
        return iecl.isNeedSave();
    }

    @Override
    public void resetSave()
    {
        iecl.resetSave();
    }

    @Override
    public void save()
    {
        doSave(false);
    }

    @Override
    public String getDatablockServiceTag()
    {
        return iecl.getDatablockServiceTag();
    }

    @Override
    public String getDatablockName()
    {
        return iecl.getDatablockName();
    }

    @Override
    public void initData(wakeup _up, long _dbOID, int _oclsID)
    {
        up = _up;
        dbIndex = _dbOID;
        oclsID = _oclsID;
        iecl = new itemEquipConfigList();
        iecl.initDatablcok(up);
        initData();
    }

    @Override
    public String getPalTitle()
    {
        return iecl.getDatablockName();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFastSave;
    private javax.swing.JButton btnNewEquip;
    private javax.swing.JButton btnPop;
    private javax.swing.JCheckBox ckShowAll;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JMenuItem miDesEqp;
    private javax.swing.JMenuItem miDisEqp;
    private javax.swing.JMenuItem miEditEqp;
    private javax.swing.JMenuItem miMoveDown;
    private javax.swing.JMenuItem miMoveUp;
    private javax.swing.JMenuItem miNewEqp;
    private javax.swing.JMenuItem miRevEqp;
    private javax.swing.JPopupMenu popIEQ;
    private javax.swing.JTable tbCfgs;
    // End of variables declaration//GEN-END:variables
}
