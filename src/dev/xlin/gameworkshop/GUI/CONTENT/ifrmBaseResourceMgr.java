package dev.xlin.gameworkshop.GUI.CONTENT;

import dev.xlin.gameworkshop.GUI.CONTENT.dialog.DlgBaseResource;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.GUI.guiFullTreeGuiCodes;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxBaseResource;
import dev.xlin.gameworkshop.progs.contents.progs.BaseResourceDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.dlgTools.dlgSelectTreeNode;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Tirone
 */
public class ifrmBaseResourceMgr extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private List lrs = null;
    private JDesktopPane desk = null;
    private boolean bInitTable = false;

    public ifrmBaseResourceMgr(wakeup _up, JDesktopPane _desk)
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
        makeTypeTree();
        makeResTable();
    }

    private void makeTypeTree()
    {
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, 101, ckShowAllType.isSelected());
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
        return mtn.getNodeOID();
    }

    private void makeResTable()
    {
        myTableModel mtm = null;
        if (bInitTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("名字");
            mtm.addColumn("标签");
            mtm.addColumn("物体");
            mtm.addColumn("状态");
            tbRes.setModel(mtm);
            bInitTable = true;
        }
        else
        {
            fast.clearTableModel(tbRes);
            mtm = (myTableModel) tbRes.getModel();
        }
        int tpid = getSelectedType();
        BaseResourceDefine brdef = new BaseResourceDefine(up);
        lrs = brdef.getResListByType(tpid, ckShowRes.isSelected());
        itemDefine idef = new itemDefine(up);
        if (lrs != null)
        {
            for (int i = 0; i < lrs.size(); i++)
            {
                BeanCtxBaseResource bean = (BeanCtxBaseResource) lrs.get(i);
                Object[] os = fast.makeObjectArray(4);
                os[0] = bean.getResName();
                os[1] = bean.getResTag();
                BeanItem bit = (BeanItem) idef.getRecordByID(bean.getItemOID());
                os[2] = bit.getItemName() + "<" + bit.getItemTag() + ">";
                os[3] = iConst.transDAOState(bean.getStatus());
                mtm.addRow(os);
            }
        }
        tbRes.setModel(mtm);
    }

    private void newType()
    {
        int r = guiFullTreeGuiCodes.doNewType(treeType, up, BaseResourceDefine.STT_DEF_CTX_RES);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("创建分类失败", r);
        }
    }

    private void newRes()
    {
        int tpid = getSelectedType();
        if (tpid == 0)
        {
            return;
        }
        DlgBaseResource dlg = new DlgBaseResource(null, true, up, null, tpid);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeResTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void editType()
    {
        int r = guiFullTreeGuiCodes.doEditType(treeType, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("修改失败");
        }
    }

    private void disType()
    {
        int tpid = getSelectedType();
        if (tpid == 0)
        {
            return;
        }
        BaseResourceDefine brd = new BaseResourceDefine(up);
        if (brd.getResListByType(tpid, true) != null)
        {
            fast.warn("分类下还有资源数据，不可被失效");
            return;
        }
        int r = guiFullTreeGuiCodes.doDisType(treeType, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("失效操作失败");
        }
    }

    private void revType()
    {
        int r = guiFullTreeGuiCodes.doRevType(treeType, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void desType()
    {
        int r = guiFullTreeGuiCodes.doDesType(treeType, up);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTypeTree();
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private BeanCtxBaseResource getSelectedRes()
    {
        int idx = tbRes.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanCtxBaseResource) lrs.get(idx);
    }

    private void editRes()
    {
        BeanCtxBaseResource bean = getSelectedRes();
        if (bean == null)
        {
            return;
        }
        DlgBaseResource dlg = new DlgBaseResource(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeResTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void disRes()
    {
        BeanCtxBaseResource bean = getSelectedRes();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将选择的资源设置为失效");
        if (sel != fast.YES)
        {
            return;
        }
        BaseResourceDefine brdef = new BaseResourceDefine(up);
        int r = brdef.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeResTable();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void revRes()
    {
        BeanCtxBaseResource bean = getSelectedRes();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将选择的资源恢复为有效");
        if (sel != fast.YES)
        {
            return;
        }
        BaseResourceDefine brdef = new BaseResourceDefine(up);
        int r = brdef.revertBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeResTable();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void desRes()
    {
        BeanCtxBaseResource bean = getSelectedRes();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将选择的资源销毁？\n这个操作无法被恢复");
        if (sel != fast.YES)
        {
            return;
        }
        BaseResourceDefine brdef = new BaseResourceDefine(up);
        int r = brdef.destroyBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeResTable();
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void moveRes()
    {
        BeanCtxBaseResource bean = getSelectedRes();
        if (bean == null)
        {
            return;
        }
        myTreeNode mrt = guiCodes.makeFullTypeTree(up, BaseResourceDefine.STT_DEF_CTX_RES, false, bean.getTypeOID());
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mrt);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            myTreeNode msel = dlg.getSelect();
            int tpid = msel.getNodeOID();
            BaseResourceDefine brdef = new BaseResourceDefine(up);
            int r = brdef.moveResrouceToType(bean.getOID(), tpid);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeResTable();
            }
            else
            {
                fast.err("移动操作失败", r);
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
        miDisType = new javax.swing.JMenuItem();
        miRevType = new javax.swing.JMenuItem();
        miDesType = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miNewResFromType = new javax.swing.JMenuItem();
        popRes = new javax.swing.JPopupMenu();
        miNewRes = new javax.swing.JMenuItem();
        miEditRes = new javax.swing.JMenuItem();
        miDisRes = new javax.swing.JMenuItem();
        miRevRes = new javax.swing.JMenuItem();
        miDesRes = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        miMoveRes = new javax.swing.JMenuItem();
        jToolBar1 = new javax.swing.JToolBar();
        btnNewType = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnNewRes = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        ckShowAllType = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeType = new javax.swing.JTree();
        jToolBar4 = new javax.swing.JToolBar();
        btnPopType = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        ckShowRes = new javax.swing.JCheckBox();
        scpRes = new javax.swing.JScrollPane();
        tbRes = new javax.swing.JTable();
        jToolBar5 = new javax.swing.JToolBar();
        btnPopRes = new javax.swing.JButton();

        miNewType.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miNewType.setText("新建分类");
        miNewType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewTypeActionPerformed(evt);
            }
        });
        popType.add(miNewType);

        miEditType.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miEditType.setText("修改分类");
        miEditType.setToolTipText("");
        miEditType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditTypeActionPerformed(evt);
            }
        });
        popType.add(miEditType);

        miDisType.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miDisType.setText("失效分类");
        miDisType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisTypeActionPerformed(evt);
            }
        });
        popType.add(miDisType);

        miRevType.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miRevType.setText("恢复分类");
        miRevType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevTypeActionPerformed(evt);
            }
        });
        popType.add(miRevType);

        miDesType.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miDesType.setText("销毁分类");
        miDesType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesTypeActionPerformed(evt);
            }
        });
        popType.add(miDesType);
        popType.add(jSeparator2);

        miNewResFromType.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miNewResFromType.setText("新建资源");
        miNewResFromType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewResFromTypeActionPerformed(evt);
            }
        });
        popType.add(miNewResFromType);

        miNewRes.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miNewRes.setText("新建资源");
        miNewRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewResActionPerformed(evt);
            }
        });
        popRes.add(miNewRes);

        miEditRes.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miEditRes.setText("修改资源");
        miEditRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditResActionPerformed(evt);
            }
        });
        popRes.add(miEditRes);

        miDisRes.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miDisRes.setText("失效资源");
        miDisRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisResActionPerformed(evt);
            }
        });
        popRes.add(miDisRes);

        miRevRes.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miRevRes.setText("恢复资源");
        miRevRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevResActionPerformed(evt);
            }
        });
        popRes.add(miRevRes);

        miDesRes.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miDesRes.setText("销毁资源");
        miDesRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesResActionPerformed(evt);
            }
        });
        popRes.add(miDesRes);
        popRes.add(jSeparator3);

        miMoveRes.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        miMoveRes.setText("移动资源");
        miMoveRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveResActionPerformed(evt);
            }
        });
        popRes.add(miMoveRes);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("基础资源管理");
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

        btnNewType.setText("新建分类");
        btnNewType.setFocusable(false);
        btnNewType.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewType.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewTypeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNewType);
        jToolBar1.add(jSeparator1);

        btnNewRes.setText("新建资源");
        btnNewRes.setFocusable(false);
        btnNewRes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewRes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewResActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNewRes);

        jSplitPane1.setDividerLocation(300);

        ckShowAllType.setText("显示全部分类");
        ckShowAllType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllTypeActionPerformed(evt);
            }
        });

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

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        btnPopType.setForeground(new java.awt.Color(0, 0, 204));
        btnPopType.setText("↓");
        btnPopType.setFocusable(false);
        btnPopType.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopType.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPopType.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPopTypeMouseClicked(evt);
            }
        });
        btnPopType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopTypeActionPerformed(evt);
            }
        });
        jToolBar4.add(btnPopType);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ckShowAllType)
                    .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ckShowAllType, jToolBar4});

        jSplitPane1.setLeftComponent(jPanel1);

        ckShowRes.setText("显示全部资源");
        ckShowRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowResActionPerformed(evt);
            }
        });

        scpRes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scpResMouseClicked(evt);
            }
        });

        tbRes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbRes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbRes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbResMouseClicked(evt);
            }
        });
        scpRes.setViewportView(tbRes);

        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);

        btnPopRes.setForeground(new java.awt.Color(0, 0, 204));
        btnPopRes.setText("↓");
        btnPopRes.setFocusable(false);
        btnPopRes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopRes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPopRes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPopResMouseClicked(evt);
            }
        });
        btnPopRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopResActionPerformed(evt);
            }
        });
        jToolBar5.add(btnPopRes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowRes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(scpRes, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ckShowRes)
                    .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(scpRes, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ckShowRes, jToolBar5});

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSplitPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPopTypeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopTypeMouseClicked
    {//GEN-HEADEREND:event_btnPopTypeMouseClicked
        if (evt.getButton() == evt.BUTTON1)
        {
            popType.show(btnPopType, evt.getX(), popType.getY() + btnPopType.getHeight());
        }
    }//GEN-LAST:event_btnPopTypeMouseClicked

    private void btnPopTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPopTypeActionPerformed
    {//GEN-HEADEREND:event_btnPopTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPopTypeActionPerformed

    private void btnPopResMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopResMouseClicked
    {//GEN-HEADEREND:event_btnPopResMouseClicked
        if (evt.getButton() == evt.BUTTON1)
        {
            popRes.show(btnPopRes, evt.getX(), popRes.getY() + btnPopRes.getHeight());
        }
    }//GEN-LAST:event_btnPopResMouseClicked

    private void btnPopResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPopResActionPerformed
    {//GEN-HEADEREND:event_btnPopResActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPopResActionPerformed

    private void btnNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewTypeActionPerformed
    {//GEN-HEADEREND:event_btnNewTypeActionPerformed
        newType();
    }//GEN-LAST:event_btnNewTypeActionPerformed

    private void btnNewResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewResActionPerformed
    {//GEN-HEADEREND:event_btnNewResActionPerformed
        newRes();
    }//GEN-LAST:event_btnNewResActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsCtxResDef(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void miNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewTypeActionPerformed
    {//GEN-HEADEREND:event_miNewTypeActionPerformed
        newType();
    }//GEN-LAST:event_miNewTypeActionPerformed

    private void miEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditTypeActionPerformed
    {//GEN-HEADEREND:event_miEditTypeActionPerformed
        editType();
    }//GEN-LAST:event_miEditTypeActionPerformed

    private void miDisTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisTypeActionPerformed
    {//GEN-HEADEREND:event_miDisTypeActionPerformed
        disType();
    }//GEN-LAST:event_miDisTypeActionPerformed

    private void miRevTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevTypeActionPerformed
    {//GEN-HEADEREND:event_miRevTypeActionPerformed
        revType();
    }//GEN-LAST:event_miRevTypeActionPerformed

    private void miDesTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesTypeActionPerformed
    {//GEN-HEADEREND:event_miDesTypeActionPerformed
        desType();
    }//GEN-LAST:event_miDesTypeActionPerformed

    private void treeTypeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeTypeMouseClicked
    {//GEN-HEADEREND:event_treeTypeMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popType.show(treeType, evt.getX(), evt.getY());
        }
        else if (evt.getClickCount() >= 2 && evt.getButton() == evt.BUTTON1)
        {
            editType();
        }
    }//GEN-LAST:event_treeTypeMouseClicked

    private void treeTypeValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeTypeValueChanged
    {//GEN-HEADEREND:event_treeTypeValueChanged
        makeResTable();
    }//GEN-LAST:event_treeTypeValueChanged

    private void ckShowAllTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllTypeActionPerformed
    {//GEN-HEADEREND:event_ckShowAllTypeActionPerformed
        makeTypeTree();
        makeResTable();
    }//GEN-LAST:event_ckShowAllTypeActionPerformed

    private void miNewResFromTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewResFromTypeActionPerformed
    {//GEN-HEADEREND:event_miNewResFromTypeActionPerformed
        newRes();
    }//GEN-LAST:event_miNewResFromTypeActionPerformed

    private void miNewResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewResActionPerformed
    {//GEN-HEADEREND:event_miNewResActionPerformed
        newRes();
    }//GEN-LAST:event_miNewResActionPerformed

    private void miEditResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditResActionPerformed
    {//GEN-HEADEREND:event_miEditResActionPerformed
        editRes();
    }//GEN-LAST:event_miEditResActionPerformed

    private void miDisResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisResActionPerformed
    {//GEN-HEADEREND:event_miDisResActionPerformed
        disRes();
    }//GEN-LAST:event_miDisResActionPerformed

    private void miRevResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevResActionPerformed
    {//GEN-HEADEREND:event_miRevResActionPerformed
        revRes();
    }//GEN-LAST:event_miRevResActionPerformed

    private void miDesResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesResActionPerformed
    {//GEN-HEADEREND:event_miDesResActionPerformed
        desRes();
    }//GEN-LAST:event_miDesResActionPerformed

    private void miMoveResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveResActionPerformed
    {//GEN-HEADEREND:event_miMoveResActionPerformed
        moveRes();
    }//GEN-LAST:event_miMoveResActionPerformed

    private void tbResMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbResMouseClicked
    {//GEN-HEADEREND:event_tbResMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popRes.show(tbRes, evt.getX(), evt.getY());
        }
        else if(evt.getButton() == evt.BUTTON1 && evt.getClickCount() >=2)
        {
            editRes();
        }
    }//GEN-LAST:event_tbResMouseClicked

    private void scpResMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scpResMouseClicked
    {//GEN-HEADEREND:event_scpResMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popRes.show(scpRes, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_scpResMouseClicked

    private void ckShowResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowResActionPerformed
    {//GEN-HEADEREND:event_ckShowResActionPerformed
        makeResTable();
    }//GEN-LAST:event_ckShowResActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewRes;
    private javax.swing.JButton btnNewType;
    private javax.swing.JButton btnPopRes;
    private javax.swing.JButton btnPopType;
    private javax.swing.JCheckBox ckShowAllType;
    private javax.swing.JCheckBox ckShowRes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JMenuItem miDesRes;
    private javax.swing.JMenuItem miDesType;
    private javax.swing.JMenuItem miDisRes;
    private javax.swing.JMenuItem miDisType;
    private javax.swing.JMenuItem miEditRes;
    private javax.swing.JMenuItem miEditType;
    private javax.swing.JMenuItem miMoveRes;
    private javax.swing.JMenuItem miNewRes;
    private javax.swing.JMenuItem miNewResFromType;
    private javax.swing.JMenuItem miNewType;
    private javax.swing.JMenuItem miRevRes;
    private javax.swing.JMenuItem miRevType;
    private javax.swing.JPopupMenu popRes;
    private javax.swing.JPopupMenu popType;
    private javax.swing.JScrollPane scpRes;
    private javax.swing.JTable tbRes;
    private javax.swing.JTree treeType;
    // End of variables declaration//GEN-END:variables
}
