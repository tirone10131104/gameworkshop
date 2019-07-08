package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.dlgItemCluster;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItemCluster;
import dev.xlin.gameworkshop.progs.foundation.itemCluster;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
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

public class ifrmItemCluster extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private boolean bInitTable = false;
    private List lcls = null;

    public ifrmItemCluster(wakeup _up, JDesktopPane _desk)
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
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_ITEM_CLUSTER, ckShowAllType.isSelected());
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeType.setModel(dtm);
        guiCommon.expandTree(treeType);
    }

    private int getSelectedType()
    {
        TreePath tph = treeType.getSelectionPath();
        if (tph == null)
        {
            return 0;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() == 0)
        {
            return 0;
        }
        return mtn.getNodeOID();
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
            mtm.addColumn("目标类型");
            mtm.addColumn("状态");
            bInitTable = true;
            tbClusters.setModel(mtm);
        }
        else
        {
            fast.clearTableModel(tbClusters);
            mtm = (myTableModel) tbClusters.getModel();
        }
        itemCluster icl = new itemCluster(up);
        int itp = getSelectedType();
        if (itp != 0)
        {
            lcls = icl.getItemClustersByType(itp, ckShowAllItem.isSelected());
            if (lcls != null)
            {
                for (int i = 0; i < lcls.size(); i++)
                {
                    beanItemCluster bic = (beanItemCluster) lcls.get(i);
                    Vector v = new Vector();
                    v.add(bic.getClusterName());
                    v.add(bic.getClusterTag());
                    v.add(bic.getDescript());
                    v.add(iConst.translate(bic.getTargetType()));
                    v.add(iConst.transDAOState(bic.getStatus()));
                    mtm.addRow(v);
                }
            }
        }
        else
        {
            lcls = null;
        }
        tbClusters.setModel(mtm);
    }

    private myTreeNode getSelectType()
    {
        TreePath tph = treeType.getSelectionPath();
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
        String sip = fast.input("请输入分类名称:");
        if (sip == null)
        {
            return;
        }
        sip = sip.trim();
        if (sip.equals(""))
        {
            return;
        }
        beanSttType bst = new beanSttType();
        bst.setTypeName(sip);
        bst.setSttID(systemType.CODE_STT_ITEM_CLUSTER);
        sttType stp = new sttType(up);
        int r = stp.createRecord(bst, false);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("创建分了操作失败", r);
        }
    }

    private void doEditType()
    {
        myTreeNode msel = getSelectType();
        if (msel == null)
        {
            return;
        }
        int oid = msel.getNodeOID();
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(oid);
        String sip = fast.input("请输入分类名称：", bst.getTypeName());
        if (sip == null)
        {
            return;
        }
        sip = sip.trim();
        if (sip.equals(""))
        {
            return;
        }
        bst.setTypeName(sip.trim());
        int r = stp.modifyRecord(bst, false);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("修改分类操作失败", r);
        }
    }

    private void doDisType()
    {
        myTreeNode msel = getSelectType();
        if (msel == null)
        {
            return;
        }
        int oid = msel.getNodeOID();
        //检查这个分类下是否还有物体
        itemCluster iclu = new itemCluster(up);
        if (iclu.getItemClustersByType(oid, true) != null)
        {
            fast.warn("不能失效这个分类，因为其下还有数据。");
            return;
        }
        int sel = fast.ask("是否将选择的分类失效？");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        int r = stp.deleteRecord(oid);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("失效分类操作失败", r);
        }
    }

    private void doRevertType()
    {
        myTreeNode msel = getSelectType();
        if (msel == null)
        {
            return;
        }
        int oid = msel.getNodeOID();
        int sel = fast.ask("是否将选择的分类恢复为正常状态？");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        int r = stp.revertBean(oid);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("恢复分类操作失败", r);
        }
    }

    private void doDestroyType()
    {
        myTreeNode msel = getSelectType();
        if (msel == null)
        {
            return;
        }
        int oid = msel.getNodeOID();
        int sel = fast.ask("是否将选择的分类销毁？销毁操作不可恢复");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        int r = stp.destroyBean(oid);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("销毁分类操作失败", r);
        }
    }

    private void doNewData()
    {
        int tpid = getSelectedType();
        if (tpid == 0)
        {
            return;
        }
        dlgItemCluster dlg = new dlgItemCluster(null, true, up, null, tpid);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private beanItemCluster getSelectedItemCluster()
    {
        int idx = tbClusters.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (beanItemCluster) lcls.get(idx);
    }

    private void doEditClu()
    {
        beanItemCluster bean = getSelectedItemCluster();
        if (bean == null)
        {
            return;
        }
        dlgItemCluster dlg = new dlgItemCluster(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDisClu()
    {
        beanItemCluster bean = getSelectedItemCluster();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要失效这个数据集？");
        if (sel != fast.YES)
        {
            return;
        }
        itemCluster icl = new itemCluster(up);
        int r = icl.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("操作发生错误", r);
        }
    }

    private void doRevClu()
    {
        beanItemCluster bean = getSelectedItemCluster();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要恢复这个数据集？");
        if (sel != fast.YES)
        {
            return;
        }
        itemCluster icl = new itemCluster(up);
        int r = icl.revertBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("操作发生错误", r);
        }
    }

    private void doDesClu()
    {
        beanItemCluster bean = getSelectedItemCluster();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要销毁这个数据集？\n这个销毁操作不可再进行恢复");
        if (sel != fast.YES)
        {
            return;
        }
        itemCluster icl = new itemCluster(up);
        int r = icl.destroyBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("操作发生错误", r);
        }
    }

    private void doMoveClu()
    {
        beanItemCluster bean = getSelectedItemCluster();
        if (bean == null)
        {
            return;
        }
        myTreeNode mtn = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_ITEM_CLUSTER, false);
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mtn);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            myTreeNode msel = dlg.getSelect();
            if (msel != null)
            {
                itemCluster icl = new itemCluster(up);
                int r = icl.moveToType(bean.getOID(), msel.getNodeOID());
                if (r == iDAO.OPERATE_SUCCESS)
                {
                    makeTable();
                }
                else
                {
                    fast.err("移动过程发生错误", r);
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

        popType = new javax.swing.JPopupMenu();
        miNewType = new javax.swing.JMenuItem();
        miEditType = new javax.swing.JMenuItem();
        miRemoveType = new javax.swing.JMenuItem();
        miRevertType = new javax.swing.JMenuItem();
        miDestroyType = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miNewCluster = new javax.swing.JMenuItem();
        popClus = new javax.swing.JPopupMenu();
        miNewClu = new javax.swing.JMenuItem();
        miEditClu = new javax.swing.JMenuItem();
        miDisClu = new javax.swing.JMenuItem();
        miRevClu = new javax.swing.JMenuItem();
        miDesClu = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miMoveClu = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        ckShowAllItem = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbClusters = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeType = new javax.swing.JTree();
        ckShowAllType = new javax.swing.JCheckBox();

        miNewType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewType.setText("新建分类");
        miNewType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewTypeActionPerformed(evt);
            }
        });
        popType.add(miNewType);

        miEditType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditType.setText("修改分类");
        miEditType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditTypeActionPerformed(evt);
            }
        });
        popType.add(miEditType);

        miRemoveType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRemoveType.setText("删除分类");
        miRemoveType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRemoveTypeActionPerformed(evt);
            }
        });
        popType.add(miRemoveType);

        miRevertType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevertType.setText("恢复分类");
        miRevertType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevertTypeActionPerformed(evt);
            }
        });
        popType.add(miRevertType);

        miDestroyType.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDestroyType.setText("销毁分类");
        miDestroyType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyTypeActionPerformed(evt);
            }
        });
        popType.add(miDestroyType);
        popType.add(jSeparator1);

        miNewCluster.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewCluster.setText("新建物体集合");
        miNewCluster.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewClusterActionPerformed(evt);
            }
        });
        popType.add(miNewCluster);

        miNewClu.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewClu.setText("新建物体集合");
        popClus.add(miNewClu);

        miEditClu.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditClu.setText("修改物体集合");
        miEditClu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditCluActionPerformed(evt);
            }
        });
        popClus.add(miEditClu);

        miDisClu.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisClu.setText("失效物体集合");
        miDisClu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisCluActionPerformed(evt);
            }
        });
        popClus.add(miDisClu);

        miRevClu.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevClu.setText("恢复物体集合");
        miRevClu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevCluActionPerformed(evt);
            }
        });
        popClus.add(miRevClu);

        miDesClu.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDesClu.setText("销毁物体集合");
        miDesClu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesCluActionPerformed(evt);
            }
        });
        popClus.add(miDesClu);
        popClus.add(jSeparator2);

        miMoveClu.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveClu.setText("移动至分类");
        miMoveClu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveCluActionPerformed(evt);
            }
        });
        popClus.add(miMoveClu);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("物体集定义管理");
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

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

        ckShowAllItem.setText("显示全部");
        ckShowAllItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllItemActionPerformed(evt);
            }
        });

        tbClusters.setModel(new javax.swing.table.DefaultTableModel(
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
        tbClusters.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbClustersMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbClusters);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ckShowAllItem)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ckShowAllItem)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel1);

        treeType.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                treeTypeMouseClicked(evt);
            }
        });
        treeType.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeTypeValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treeType);

        ckShowAllType.setText("显示全部分类");
        ckShowAllType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(ckShowAllType)
                .addGap(0, 190, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(ckShowAllType)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void miNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewTypeActionPerformed
    {//GEN-HEADEREND:event_miNewTypeActionPerformed
        doNewType();
    }//GEN-LAST:event_miNewTypeActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsItemCluster(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void miRemoveTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveTypeActionPerformed
    {//GEN-HEADEREND:event_miRemoveTypeActionPerformed
        doDisType();
    }//GEN-LAST:event_miRemoveTypeActionPerformed

    private void miEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditTypeActionPerformed
    {//GEN-HEADEREND:event_miEditTypeActionPerformed
        doEditType();
    }//GEN-LAST:event_miEditTypeActionPerformed

    private void treeTypeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeTypeMouseClicked
    {//GEN-HEADEREND:event_treeTypeMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popType.show(treeType, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_treeTypeMouseClicked

    private void miRevertTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertTypeActionPerformed
    {//GEN-HEADEREND:event_miRevertTypeActionPerformed
        doRevertType();
    }//GEN-LAST:event_miRevertTypeActionPerformed

    private void miDestroyTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyTypeActionPerformed
    {//GEN-HEADEREND:event_miDestroyTypeActionPerformed
        doDestroyType();
    }//GEN-LAST:event_miDestroyTypeActionPerformed

    private void treeTypeValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeTypeValueChanged
    {//GEN-HEADEREND:event_treeTypeValueChanged
        makeTable();
    }//GEN-LAST:event_treeTypeValueChanged

    private void ckShowAllTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllTypeActionPerformed
    {//GEN-HEADEREND:event_ckShowAllTypeActionPerformed
        makeTree();
    }//GEN-LAST:event_ckShowAllTypeActionPerformed

    private void ckShowAllItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllItemActionPerformed
    {//GEN-HEADEREND:event_ckShowAllItemActionPerformed
        makeTable();
    }//GEN-LAST:event_ckShowAllItemActionPerformed

    private void miNewClusterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewClusterActionPerformed
    {//GEN-HEADEREND:event_miNewClusterActionPerformed
        doNewData();
    }//GEN-LAST:event_miNewClusterActionPerformed

    private void miEditCluActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditCluActionPerformed
    {//GEN-HEADEREND:event_miEditCluActionPerformed
        doEditClu();
    }//GEN-LAST:event_miEditCluActionPerformed

    private void miDisCluActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisCluActionPerformed
    {//GEN-HEADEREND:event_miDisCluActionPerformed
        doDisClu();
    }//GEN-LAST:event_miDisCluActionPerformed

    private void miRevCluActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevCluActionPerformed
    {//GEN-HEADEREND:event_miRevCluActionPerformed
        doRevClu();
    }//GEN-LAST:event_miRevCluActionPerformed

    private void miDesCluActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesCluActionPerformed
    {//GEN-HEADEREND:event_miDesCluActionPerformed
        doDesClu();
    }//GEN-LAST:event_miDesCluActionPerformed

    private void miMoveCluActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveCluActionPerformed
    {//GEN-HEADEREND:event_miMoveCluActionPerformed
        doMoveClu();
    }//GEN-LAST:event_miMoveCluActionPerformed

    private void tbClustersMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbClustersMouseClicked
    {//GEN-HEADEREND:event_tbClustersMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popClus.show(tbClusters, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbClustersMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ckShowAllItem;
    private javax.swing.JCheckBox ckShowAllType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem miDesClu;
    private javax.swing.JMenuItem miDestroyType;
    private javax.swing.JMenuItem miDisClu;
    private javax.swing.JMenuItem miEditClu;
    private javax.swing.JMenuItem miEditType;
    private javax.swing.JMenuItem miMoveClu;
    private javax.swing.JMenuItem miNewClu;
    private javax.swing.JMenuItem miNewCluster;
    private javax.swing.JMenuItem miNewType;
    private javax.swing.JMenuItem miRemoveType;
    private javax.swing.JMenuItem miRevClu;
    private javax.swing.JMenuItem miRevertType;
    private javax.swing.JPopupMenu popClus;
    private javax.swing.JPopupMenu popType;
    private javax.swing.JTable tbClusters;
    private javax.swing.JTree treeType;
    // End of variables declaration//GEN-END:variables
}
