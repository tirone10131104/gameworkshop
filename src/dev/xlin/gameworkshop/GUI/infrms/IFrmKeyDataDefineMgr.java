package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.DlgKeyDefine;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.GUI.guiFullTreeGuiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanKeyDataChunk;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanKeyDataDefine;
import dev.xlin.gameworkshop.progs.foundation.keyDataChunk;
import dev.xlin.gameworkshop.progs.foundation.keyDataDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.dlgTools.dlgSelectTreeNode;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import java.util.Vector;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class IFrmKeyDataDefineMgr extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private List lkeys = null;
    private boolean bInitTable = false;

    public IFrmKeyDataDefineMgr(wakeup _up, JDesktopPane _desk)
    {
        initComponents();
        up = _up;
        desk = _desk;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        makeTree();
        makeTable();
    }

    private void makeTree()
    {
        myTreeNode mrt = guiCodes.makeFullTypeTree(up, systemType.CODE_STT_KEY_DEFINE, ckShowAllType.isSelected(), 0);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeTps.setModel(dtm);
        guiCommon.expandTree(treeTps);
    }

    private void makeTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("名称");
            mtm.addColumn("标签");
            mtm.addColumn("描述");
            mtm.addColumn("数据类型");
            mtm.addColumn("缓冲预读");
            mtm.addColumn("数据区块");
            mtm.addColumn("状态");
            tbKeys.setModel(mtm);
            bInitTable = true;
        }
        else
        {
            mtm = (myTableModel) tbKeys.getModel();
            fast.clearTableModel(tbKeys);
        }
        int tpid = 0;
        myTreeNode msel = getSelectedType();
        if (msel != null)
        {
            tpid = msel.getNodeOID();
        }
        keyDataDefine kdd = new keyDataDefine(up);
        keyDataChunk kdc = new keyDataChunk(up);
        List lkcs = kdc.getAllRecord();
        lkeys = kdd.getKeyListByType(tpid, ckShowAllKey.isSelected());
        if (lkeys != null)
        {
            for (int i = 0; i < lkeys.size(); i++)
            {
                BeanKeyDataDefine bean = (BeanKeyDataDefine) lkeys.get(i);
                Vector v = new Vector();
                v.add(bean.getKeyName());
                v.add(bean.getKeyTag());
                v.add(bean.getKeyDesp());
                v.add(iConst.translate(bean.getDataType()));
                v.add(iConst.translateIBOL(bean.getCacheLoad()));
                BeanKeyDataChunk bkdc = findChunkInList(lkcs, bean.getDataChunkID());
                if (bkdc == null)
                {
                    v.add("-");
                }
                else
                {
                    v.add(bkdc.getChunkName());
                }
                v.add(iConst.transDAOState(bean.getStatus()));
                mtm.addRow(v);
            }
        }
        tbKeys.setModel(mtm);
    }

    private BeanKeyDataChunk findChunkInList(List lcks, int oid)
    {
        if (lcks != null)
        {
            for (int i = 0; i < lcks.size(); i++)
            {
                BeanKeyDataChunk bean = (BeanKeyDataChunk) lcks.get(i);
                if (bean.getOID() == oid)
                {
                    return bean;
                }
            }
        }
        return null;
    }

    private myTreeNode getSelectedType()
    {
        TreePath tph = treeTps.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() == 0)
        {
            return null;
        }
        return mtn;
    }

    private void doNewType()
    {
        int r = guiFullTreeGuiCodes.doNewType(treeTps, up , systemType.CODE_STT_KEY_DEFINE);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.warn("创建分类未完成");
        }
    }

    private void doEditType()
    {
        int r = guiFullTreeGuiCodes.doEditType(treeTps, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.warn("修改分类未完成");
        }
    }

    private void doDisType()
    {
        myTreeNode msel = guiFullTreeGuiCodes.getSelectedType(treeTps);
        //检查一下这个分类下是否还有KEY的定义
        keyDataDefine kdd = new keyDataDefine(up);
        List lks = kdd.getKeyListByType(msel.getNodeOID(), true);
        if (lks != null)
        {
            fast.warn("这个分类下还有存在的键值定义。不能执行失效操作。");
            return;
        }
        int r = guiFullTreeGuiCodes.doDisType(treeTps, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doRevType()
    {
        int r = guiFullTreeGuiCodes.doRevType(treeTps, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.warn("恢复分类未完成");
        }
    }

    private void doDesType()
    {
        int r = guiFullTreeGuiCodes.doDesType(treeTps, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.warn("销毁分类未完成");
        }
    }

    private void doMoveType()
    {
        int r = guiFullTreeGuiCodes.doMoveType(treeTps, up , systemType.CODE_STT_KEY_DEFINE);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.warn("移动分类未完成");
        }
    }

    private void doNewKey()
    {
        myTreeNode msel = getSelectedType();
        if (msel == null)
        {
            return;
        }
        int tpid = msel.getNodeOID();
        DlgKeyDefine dlg = new DlgKeyDefine(null, true, up, null, tpid);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanKeyDataDefine getSelectedDataDef()
    {
        int idx = tbKeys.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanKeyDataDefine) lkeys.get(idx);
    }

    private void doEditKey()
    {
        BeanKeyDataDefine bean = getSelectedDataDef();
        if (bean == null)
        {
            return;
        }
        DlgKeyDefine dlg = new DlgKeyDefine(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDisKey()
    {
        BeanKeyDataDefine bean = getSelectedDataDef();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将选择的这个键值定义设为失效状态？");
        if (sel != fast.YES)
        {
            return;
        }
        keyDataDefine kdf = new keyDataDefine(up);
        int r = kdf.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doRevKey()
    {
        BeanKeyDataDefine bean = getSelectedDataDef();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将选择的这个键值定义恢复为有效状态？");
        if (sel != fast.YES)
        {
            return;
        }
        keyDataDefine kdf = new keyDataDefine(up);
        int r = kdf.revertBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void doDesKey()
    {
        BeanKeyDataDefine bean = getSelectedDataDef();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将选择的这个键值定义销毁？\n这个操作不可被恢复");
        if (sel != fast.YES)
        {
            return;
        }
        keyDataDefine kdf = new keyDataDefine(up);
        int r = kdf.destroyBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void doMoveKey()
    {
        BeanKeyDataDefine bean = getSelectedDataDef();
        if (bean == null)
        {
            return;
        }
        myTreeNode mrt = guiCodes.makeFullTypeTree(up, systemType.CODE_STT_KEY_DEFINE, false, 0);
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mrt);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            myTreeNode msel = dlg.getSelect();
            int tpid = msel.getNodeOID();
            if (tpid != 0)
            {
                keyDataDefine kdf = new keyDataDefine(up);
                int r = kdf.moveToType(bean.getOID(), tpid);
                if (r == iDAO.OPERATE_SUCCESS)
                {
                    makeTable();
                }
                else
                {
                    fast.err("移动键值操作失败", r);
                }
            }
        }
        dlg.dispose();
        dlg = null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popTree = new javax.swing.JPopupMenu();
        miNewType = new javax.swing.JMenuItem();
        miEditType = new javax.swing.JMenuItem();
        miDisType = new javax.swing.JMenuItem();
        miRevType = new javax.swing.JMenuItem();
        miDesType = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miMoveType = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miNewKey = new javax.swing.JMenuItem();
        popKeys = new javax.swing.JPopupMenu();
        miNewKey2 = new javax.swing.JMenuItem();
        miEditKey = new javax.swing.JMenuItem();
        miDisKey = new javax.swing.JMenuItem();
        miRevKey = new javax.swing.JMenuItem();
        miDesKey = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        miMoveKey = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        ckShowAllType = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeTps = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        ckShowAllKey = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbKeys = new javax.swing.JTable();

        miNewType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewType.setText("新建分类");
        miNewType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewTypeActionPerformed(evt);
            }
        });
        popTree.add(miNewType);

        miEditType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditType.setText("修改分类");
        miEditType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditTypeActionPerformed(evt);
            }
        });
        popTree.add(miEditType);

        miDisType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisType.setText("失效分类");
        miDisType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisTypeActionPerformed(evt);
            }
        });
        popTree.add(miDisType);

        miRevType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevType.setText("恢复分类");
        miRevType.setToolTipText("");
        miRevType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevTypeActionPerformed(evt);
            }
        });
        popTree.add(miRevType);

        miDesType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesType.setText("销毁分类");
        miDesType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesTypeActionPerformed(evt);
            }
        });
        popTree.add(miDesType);
        popTree.add(jSeparator1);

        miMoveType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveType.setText("移动分类");
        miMoveType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveTypeActionPerformed(evt);
            }
        });
        popTree.add(miMoveType);
        popTree.add(jSeparator2);

        miNewKey.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewKey.setText("添加键定义");
        miNewKey.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewKeyActionPerformed(evt);
            }
        });
        popTree.add(miNewKey);

        miNewKey2.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewKey2.setText("新建键值");
        miNewKey2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewKey2ActionPerformed(evt);
            }
        });
        popKeys.add(miNewKey2);

        miEditKey.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditKey.setText("修改键值");
        miEditKey.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditKeyActionPerformed(evt);
            }
        });
        popKeys.add(miEditKey);

        miDisKey.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisKey.setText("失效键值");
        miDisKey.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisKeyActionPerformed(evt);
            }
        });
        popKeys.add(miDisKey);

        miRevKey.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevKey.setText("恢复键值");
        miRevKey.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevKeyActionPerformed(evt);
            }
        });
        popKeys.add(miRevKey);

        miDesKey.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesKey.setText("销毁键值");
        miDesKey.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesKeyActionPerformed(evt);
            }
        });
        popKeys.add(miDesKey);
        popKeys.add(jSeparator3);

        miMoveKey.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveKey.setText("移动键值");
        miMoveKey.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveKeyActionPerformed(evt);
            }
        });
        popKeys.add(miMoveKey);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("键数据定义管理");
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

        jSplitPane1.setDividerLocation(388);
        jSplitPane1.setDividerSize(3);

        ckShowAllType.setText("显示全部");
        ckShowAllType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllTypeActionPerformed(evt);
            }
        });

        treeTps.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                treeTpsMouseClicked(evt);
            }
        });
        treeTps.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeTpsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treeTps);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllType)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ckShowAllType)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel1);

        ckShowAllKey.setText("显示全部");
        ckShowAllKey.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllKeyActionPerformed(evt);
            }
        });

        tbKeys.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbKeys.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbKeys.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbKeysMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbKeys);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllKey)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(ckShowAllKey)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1183, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ckShowAllTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllTypeActionPerformed
    {//GEN-HEADEREND:event_ckShowAllTypeActionPerformed
        makeTree();
    }//GEN-LAST:event_ckShowAllTypeActionPerformed

    private void treeTpsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeTpsMouseClicked
    {//GEN-HEADEREND:event_treeTpsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popTree.show(treeTps, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_treeTpsMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
guiIFrameControl.setIsKeyDefMgr(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void miNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewTypeActionPerformed
    {//GEN-HEADEREND:event_miNewTypeActionPerformed
        doNewType();
    }//GEN-LAST:event_miNewTypeActionPerformed

    private void miEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditTypeActionPerformed
    {//GEN-HEADEREND:event_miEditTypeActionPerformed
        doEditType();
    }//GEN-LAST:event_miEditTypeActionPerformed

    private void miDisTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisTypeActionPerformed
    {//GEN-HEADEREND:event_miDisTypeActionPerformed
        doDisType();
    }//GEN-LAST:event_miDisTypeActionPerformed

    private void miRevTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevTypeActionPerformed
    {//GEN-HEADEREND:event_miRevTypeActionPerformed
        doRevType();
    }//GEN-LAST:event_miRevTypeActionPerformed

    private void miDesTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesTypeActionPerformed
    {//GEN-HEADEREND:event_miDesTypeActionPerformed
        doDesType();
    }//GEN-LAST:event_miDesTypeActionPerformed

    private void miNewKeyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewKeyActionPerformed
    {//GEN-HEADEREND:event_miNewKeyActionPerformed
        doNewKey();
    }//GEN-LAST:event_miNewKeyActionPerformed

    private void treeTpsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeTpsValueChanged
    {//GEN-HEADEREND:event_treeTpsValueChanged
        makeTable();
    }//GEN-LAST:event_treeTpsValueChanged

    private void ckShowAllKeyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllKeyActionPerformed
    {//GEN-HEADEREND:event_ckShowAllKeyActionPerformed
        makeTable();
    }//GEN-LAST:event_ckShowAllKeyActionPerformed

    private void miNewKey2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewKey2ActionPerformed
    {//GEN-HEADEREND:event_miNewKey2ActionPerformed
        doNewKey();
    }//GEN-LAST:event_miNewKey2ActionPerformed

    private void miEditKeyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditKeyActionPerformed
    {//GEN-HEADEREND:event_miEditKeyActionPerformed
        doEditKey();
    }//GEN-LAST:event_miEditKeyActionPerformed

    private void tbKeysMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbKeysMouseClicked
    {//GEN-HEADEREND:event_tbKeysMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popKeys.show(tbKeys, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbKeysMouseClicked

    private void miDisKeyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisKeyActionPerformed
    {//GEN-HEADEREND:event_miDisKeyActionPerformed
        doDisKey();
    }//GEN-LAST:event_miDisKeyActionPerformed

    private void miRevKeyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevKeyActionPerformed
    {//GEN-HEADEREND:event_miRevKeyActionPerformed
        doRevKey();
    }//GEN-LAST:event_miRevKeyActionPerformed

    private void miDesKeyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesKeyActionPerformed
    {//GEN-HEADEREND:event_miDesKeyActionPerformed
        doDesKey();
    }//GEN-LAST:event_miDesKeyActionPerformed

    private void miMoveKeyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveKeyActionPerformed
    {//GEN-HEADEREND:event_miMoveKeyActionPerformed
        doMoveKey();
    }//GEN-LAST:event_miMoveKeyActionPerformed

    private void miMoveTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveTypeActionPerformed
    {//GEN-HEADEREND:event_miMoveTypeActionPerformed
        doMoveType();
    }//GEN-LAST:event_miMoveTypeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ckShowAllKey;
    private javax.swing.JCheckBox ckShowAllType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem miDesKey;
    private javax.swing.JMenuItem miDesType;
    private javax.swing.JMenuItem miDisKey;
    private javax.swing.JMenuItem miDisType;
    private javax.swing.JMenuItem miEditKey;
    private javax.swing.JMenuItem miEditType;
    private javax.swing.JMenuItem miMoveKey;
    private javax.swing.JMenuItem miMoveType;
    private javax.swing.JMenuItem miNewKey;
    private javax.swing.JMenuItem miNewKey2;
    private javax.swing.JMenuItem miNewType;
    private javax.swing.JMenuItem miRevKey;
    private javax.swing.JMenuItem miRevType;
    private javax.swing.JPopupMenu popKeys;
    private javax.swing.JPopupMenu popTree;
    private javax.swing.JTable tbKeys;
    private javax.swing.JTree treeTps;
    // End of variables declaration//GEN-END:variables
}
