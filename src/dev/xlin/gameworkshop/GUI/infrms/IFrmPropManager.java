package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.DlgPropDefine;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanPropertyDefine;
import dev.xlin.gameworkshop.progs.foundation.propertyDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import java.util.Vector;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Administrator
 */
public class IFrmPropManager extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private boolean bTable = false;

    private List lps = null;

    public IFrmPropManager(wakeup _up, JDesktopPane _desk)
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
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_SYS_PROP, ckShowAllTypes.isSelected());
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeTypes.setModel(dtm);
    }

    private myTreeNode getSelectType()
    {
        TreePath tph = treeTypes.getSelectionPath();
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

    private void makeTable()
    {
        myTableModel mtm = null;
        if (bTable == false)
        {
            mtm = new myTableModel();
            mtm.addColumn("属性名");
            mtm.addColumn("标签");
            mtm.addColumn("描述");
            mtm.addColumn("类型");
            mtm.addColumn("状态");
            tbProps.setModel(mtm);
            bTable = true;
        }
        myTreeNode msel = getSelectType();
        if (msel == null)
        {
            return;
        }
        fast.clearTableModel(tbProps);
        mtm = (myTableModel) tbProps.getModel();
        propertyDefine pdef = new propertyDefine(up);
        lps = pdef.getPropsByType(msel.getNodeOID(), ckShowAllProps.isSelected());
        if (lps != null)
        {
            for (int i = 0; i < lps.size(); i++)
            {
                BeanPropertyDefine bpd = (BeanPropertyDefine) lps.get(i);
                Vector v = new Vector();
                v.add(bpd.getPropName());
                v.add(bpd.getPropTag());
                v.add(bpd.getPropDesp());
                v.add(iConst.translate(bpd.getDataType()));
                v.add(iConst.transDAOState(bpd.getState()));
                mtm.addRow(v);
            }
        }
        tbProps.setModel(mtm);
    }

    private void doNewProp()
    {
        myTreeNode msel = getSelectType();
        if (msel == null)
        {
            fast.warn("请选择一个属性分类");
            return;
        }
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(msel.getNodeOID());
        if (bst.getState() != iDAO.OBJECT_STATE_ACTIVE)
        {
            fast.msg("不能在已废弃的分类下添加属性。");
            return;
        }
        DlgPropDefine dlg = new DlgPropDefine(null, true, up, null, msel.getNodeOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
            fast.msg("新建属性操作成功");
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanPropertyDefine getSelectProp()
    {
        int idx = tbProps.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanPropertyDefine) lps.get(idx);
    }

    private void doEditProp()
    {
        BeanPropertyDefine bean = getSelectProp();
        if (bean == null)
        {
            return;
        }
        DlgPropDefine dlg = new DlgPropDefine(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
            fast.msg("修改属性操作成功");
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDisProp()
    {
        BeanPropertyDefine bean = getSelectProp();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将所选的属性失效？");
        if (sel != fast.YES)
        {
            return;
        }
        propertyDefine pdef = new propertyDefine(up);
        int r = pdef.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
            fast.msg("失效操作成功");
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doNewType()
    {
        String s = fast.input("输入一个分类名称");
        if (s == null)
        {
            return;
        }
        if (s.trim().equals(""))
        {
            return;
        }
        beanSttType bst = new beanSttType();
        bst.setTypeName(s.trim());
        bst.setSttID(systemType.CODE_STT_SYS_PROP);
        sttType stp = new sttType(up);
        int r = stp.createRecord(bst, false);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("创建分类失败");
        }
    }

    private void doEditType()
    {
        myTreeNode mtn = getSelectType();
        if (mtn == null)
        {
            return;
        }
        int oid = mtn.getNodeOID();
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(oid);
        String s = fast.input("输入一个分类名称", bst.getTypeName());
        if (s == null)
        {
            return;
        }
        if (s.trim().equals(""))
        {
            return;
        }
        bst.setTypeName(s.trim());
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

    private void doDelType()
    {
        myTreeNode mtn = getSelectType();
        if (mtn == null)
        {
            return;
        }
        propertyDefine pdef = new propertyDefine(up);
        List ls = pdef.getPropsByType(mtn.getNodeOID(), true);
        if (ls != null)
        {
            fast.warn("这个分类不能删除，因为还有属性处于这个分类之中。");
            return;
        }
        //执行删除
        int sel = fast.ask("是否确定删除这个分类？");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        int r = stp.deleteRecord(mtn.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("删除分类操作失败", r);
        }
    }

    private void doMoveProp()
    {

    }

    private void doRevertType()
    {
        myTreeNode mtn = getSelectType();
        if (mtn == null)
        {
            return;
        }
        int sel = fast.ask("是否要恢复这个分类？");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(mtn.getNodeOID());
        if (bst.getState() == iDAO.OBJECT_STATE_ACTIVE)
        {
            fast.warn("没有恢复的必要");
            return;
        }
        int r = stp.revertBean(mtn.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
            fast.msg("恢复操作成功");
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void doDestroyType()
    {
        myTreeNode mtn = getSelectType();
        if (mtn == null)
        {
            return;
        }
        int sel = fast.ask("是否要销毁这个分类？");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(mtn.getNodeOID());
        if (bst.getState() == iDAO.OBJECT_STATE_ACTIVE)
        {
            fast.warn("只有失效的分类才能执行销毁操作");
            return;
        }
        int r = stp.destroyBean(mtn.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
            fast.msg("销毁操作成功");
        }
        else
        {
            fast.err("销毁操作失败", r);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popType = new javax.swing.JPopupMenu();
        miNewType = new javax.swing.JMenuItem();
        miEditType = new javax.swing.JMenuItem();
        miDisableType = new javax.swing.JMenuItem();
        miRevertType = new javax.swing.JMenuItem();
        miDestroyType = new javax.swing.JMenuItem();
        popProp = new javax.swing.JPopupMenu();
        miNewProp = new javax.swing.JMenuItem();
        miEditProp = new javax.swing.JMenuItem();
        miMoveProp = new javax.swing.JMenuItem();
        miDisableProp = new javax.swing.JMenuItem();
        miRevertProp = new javax.swing.JMenuItem();
        miDestroyProp = new javax.swing.JMenuItem();
        jToolBar1 = new javax.swing.JToolBar();
        btnNewType = new javax.swing.JButton();
        btnEditType = new javax.swing.JButton();
        btnDelType = new javax.swing.JButton();
        btnRevertType = new javax.swing.JButton();
        btnDesType = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnNewProp = new javax.swing.JButton();
        btnEditProp = new javax.swing.JButton();
        btnDisProp = new javax.swing.JButton();
        btnMoveProp = new javax.swing.JButton();
        btnRevertProp = new javax.swing.JButton();
        btnDestroyProp = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        treeTypes = new javax.swing.JTree();
        scpProp = new javax.swing.JScrollPane();
        tbProps = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        ckShowAllTypes = new javax.swing.JCheckBox();
        ckShowAllProps = new javax.swing.JCheckBox();

        miNewType.setText("新建分类");
        miNewType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewTypeActionPerformed(evt);
            }
        });
        popType.add(miNewType);

        miEditType.setText("修改分类");
        miEditType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditTypeActionPerformed(evt);
            }
        });
        popType.add(miEditType);

        miDisableType.setText("失效分类");
        miDisableType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisableTypeActionPerformed(evt);
            }
        });
        popType.add(miDisableType);

        miRevertType.setText("恢复分类");
        miRevertType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevertTypeActionPerformed(evt);
            }
        });
        popType.add(miRevertType);

        miDestroyType.setText("销毁分类");
        miDestroyType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyTypeActionPerformed(evt);
            }
        });
        popType.add(miDestroyType);

        miNewProp.setText("新建属性");
        miNewProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewPropActionPerformed(evt);
            }
        });
        popProp.add(miNewProp);

        miEditProp.setText("修改属性");
        miEditProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditPropActionPerformed(evt);
            }
        });
        popProp.add(miEditProp);

        miMoveProp.setText("移动至分类");
        miMoveProp.setToolTipText("");
        miMoveProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMovePropActionPerformed(evt);
            }
        });
        popProp.add(miMoveProp);

        miDisableProp.setText("失效属性");
        miDisableProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisablePropActionPerformed(evt);
            }
        });
        popProp.add(miDisableProp);

        miRevertProp.setText("恢复属性");
        popProp.add(miRevertProp);

        miDestroyProp.setText("销毁属性");
        miDestroyProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyPropActionPerformed(evt);
            }
        });
        popProp.add(miDestroyProp);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("属性管理工具");
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

        btnNewType.setText("添加分类");
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

        btnEditType.setText("修改分类");
        btnEditType.setFocusable(false);
        btnEditType.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditType.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditTypeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditType);

        btnDelType.setText("删除分类");
        btnDelType.setFocusable(false);
        btnDelType.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelType.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDelType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDelTypeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDelType);

        btnRevertType.setText("恢复分类");
        btnRevertType.setFocusable(false);
        btnRevertType.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRevertType.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRevertType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRevertTypeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRevertType);

        btnDesType.setText("销毁分类");
        btnDesType.setFocusable(false);
        btnDesType.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDesType.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDesType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDesTypeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDesType);
        jToolBar1.add(jSeparator1);

        btnNewProp.setText("添加属性");
        btnNewProp.setFocusable(false);
        btnNewProp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewProp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewPropActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNewProp);

        btnEditProp.setText("修改属性");
        btnEditProp.setFocusable(false);
        btnEditProp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditProp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditPropActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditProp);

        btnDisProp.setText("失效属性");
        btnDisProp.setFocusable(false);
        btnDisProp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDisProp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDisProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDisPropActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDisProp);

        btnMoveProp.setText("移动属性");
        btnMoveProp.setFocusable(false);
        btnMoveProp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMoveProp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMoveProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMovePropActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMoveProp);

        btnRevertProp.setText("恢复属性");
        btnRevertProp.setFocusable(false);
        btnRevertProp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRevertProp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRevertProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRevertPropActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRevertProp);

        btnDestroyProp.setText("销毁属性");
        btnDestroyProp.setFocusable(false);
        btnDestroyProp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDestroyProp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDestroyProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDestroyPropActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDestroyProp);

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

        treeTypes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                treeTypesMouseClicked(evt);
            }
        });
        treeTypes.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeTypesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(treeTypes);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(jPanel1);

        scpProp.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scpPropMouseClicked(evt);
            }
        });

        tbProps.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbProps.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbProps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbProps.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbPropsMouseClicked(evt);
            }
        });
        scpProp.setViewportView(tbProps);

        jSplitPane1.setRightComponent(scpProp);

        ckShowAllTypes.setText("显示全部分类");
        ckShowAllTypes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllTypesActionPerformed(evt);
            }
        });

        ckShowAllProps.setText("显示全部属性");
        ckShowAllProps.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllPropsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllTypes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckShowAllProps)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ckShowAllTypes)
                .addComponent(ckShowAllProps))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 836, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSplitPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsPropMgr(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewTypeActionPerformed
    {//GEN-HEADEREND:event_btnNewTypeActionPerformed
        doNewType();
    }//GEN-LAST:event_btnNewTypeActionPerformed

    private void btnDelTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDelTypeActionPerformed
    {//GEN-HEADEREND:event_btnDelTypeActionPerformed
        doDelType();
    }//GEN-LAST:event_btnDelTypeActionPerformed

    private void btnEditPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditPropActionPerformed
    {//GEN-HEADEREND:event_btnEditPropActionPerformed
        doEditProp();
    }//GEN-LAST:event_btnEditPropActionPerformed

    private void btnEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditTypeActionPerformed
    {//GEN-HEADEREND:event_btnEditTypeActionPerformed
        doEditType();
    }//GEN-LAST:event_btnEditTypeActionPerformed

    private void btnNewPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewPropActionPerformed
    {//GEN-HEADEREND:event_btnNewPropActionPerformed
        doNewProp();
    }//GEN-LAST:event_btnNewPropActionPerformed

    private void btnDisPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDisPropActionPerformed
    {//GEN-HEADEREND:event_btnDisPropActionPerformed
        doDisProp();
    }//GEN-LAST:event_btnDisPropActionPerformed

    private void btnMovePropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMovePropActionPerformed
    {//GEN-HEADEREND:event_btnMovePropActionPerformed
        doMoveProp();
    }//GEN-LAST:event_btnMovePropActionPerformed

    private void ckShowAllTypesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllTypesActionPerformed
    {//GEN-HEADEREND:event_ckShowAllTypesActionPerformed
        makeTree();
    }//GEN-LAST:event_ckShowAllTypesActionPerformed

    private void ckShowAllPropsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllPropsActionPerformed
    {//GEN-HEADEREND:event_ckShowAllPropsActionPerformed
        makeTable();
    }//GEN-LAST:event_ckShowAllPropsActionPerformed

    private void btnRevertTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRevertTypeActionPerformed
    {//GEN-HEADEREND:event_btnRevertTypeActionPerformed
        doRevertType();
    }//GEN-LAST:event_btnRevertTypeActionPerformed

    private void btnDesTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDesTypeActionPerformed
    {//GEN-HEADEREND:event_btnDesTypeActionPerformed
        doDestroyType();
    }//GEN-LAST:event_btnDesTypeActionPerformed

    private void treeTypesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeTypesMouseClicked
    {//GEN-HEADEREND:event_treeTypesMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popType.show(treeTypes, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            doEditType();
        }
    }//GEN-LAST:event_treeTypesMouseClicked

    private void miNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewTypeActionPerformed
    {//GEN-HEADEREND:event_miNewTypeActionPerformed
        doNewType();
    }//GEN-LAST:event_miNewTypeActionPerformed

    private void miEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditTypeActionPerformed
    {//GEN-HEADEREND:event_miEditTypeActionPerformed
        doEditType();
    }//GEN-LAST:event_miEditTypeActionPerformed

    private void miDisableTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisableTypeActionPerformed
    {//GEN-HEADEREND:event_miDisableTypeActionPerformed
        doDelType();
    }//GEN-LAST:event_miDisableTypeActionPerformed

    private void miRevertTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertTypeActionPerformed
    {//GEN-HEADEREND:event_miRevertTypeActionPerformed
        doRevertType();
    }//GEN-LAST:event_miRevertTypeActionPerformed

    private void miDestroyTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyTypeActionPerformed
    {//GEN-HEADEREND:event_miDestroyTypeActionPerformed
        doDestroyType();
    }//GEN-LAST:event_miDestroyTypeActionPerformed

    private void miNewPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewPropActionPerformed
    {//GEN-HEADEREND:event_miNewPropActionPerformed
        doNewProp();
    }//GEN-LAST:event_miNewPropActionPerformed

    private void miEditPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditPropActionPerformed
    {//GEN-HEADEREND:event_miEditPropActionPerformed
        doEditProp();
    }//GEN-LAST:event_miEditPropActionPerformed

    private void miMovePropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMovePropActionPerformed
    {//GEN-HEADEREND:event_miMovePropActionPerformed
        doMoveProp();
    }//GEN-LAST:event_miMovePropActionPerformed

    private void miDisablePropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisablePropActionPerformed
    {//GEN-HEADEREND:event_miDisablePropActionPerformed
        doDisProp();
    }//GEN-LAST:event_miDisablePropActionPerformed

    private void miDestroyPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyPropActionPerformed
    {//GEN-HEADEREND:event_miDestroyPropActionPerformed

    }//GEN-LAST:event_miDestroyPropActionPerformed

    private void btnDestroyPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDestroyPropActionPerformed
    {//GEN-HEADEREND:event_btnDestroyPropActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDestroyPropActionPerformed

    private void btnRevertPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRevertPropActionPerformed
    {//GEN-HEADEREND:event_btnRevertPropActionPerformed

    }//GEN-LAST:event_btnRevertPropActionPerformed

    private void scpPropMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scpPropMouseClicked
    {//GEN-HEADEREND:event_scpPropMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popProp.show(scpProp, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            doNewProp();
        }
    }//GEN-LAST:event_scpPropMouseClicked

    private void tbPropsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbPropsMouseClicked
    {//GEN-HEADEREND:event_tbPropsMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popProp.show(tbProps, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            doEditProp();
        }
    }//GEN-LAST:event_tbPropsMouseClicked

    private void treeTypesValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeTypesValueChanged
    {//GEN-HEADEREND:event_treeTypesValueChanged
        makeTable();
    }//GEN-LAST:event_treeTypesValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelType;
    private javax.swing.JButton btnDesType;
    private javax.swing.JButton btnDestroyProp;
    private javax.swing.JButton btnDisProp;
    private javax.swing.JButton btnEditProp;
    private javax.swing.JButton btnEditType;
    private javax.swing.JButton btnMoveProp;
    private javax.swing.JButton btnNewProp;
    private javax.swing.JButton btnNewType;
    private javax.swing.JButton btnRevertProp;
    private javax.swing.JButton btnRevertType;
    private javax.swing.JCheckBox ckShowAllProps;
    private javax.swing.JCheckBox ckShowAllTypes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem miDestroyProp;
    private javax.swing.JMenuItem miDestroyType;
    private javax.swing.JMenuItem miDisableProp;
    private javax.swing.JMenuItem miDisableType;
    private javax.swing.JMenuItem miEditProp;
    private javax.swing.JMenuItem miEditType;
    private javax.swing.JMenuItem miMoveProp;
    private javax.swing.JMenuItem miNewProp;
    private javax.swing.JMenuItem miNewType;
    private javax.swing.JMenuItem miRevertProp;
    private javax.swing.JMenuItem miRevertType;
    private javax.swing.JPopupMenu popProp;
    private javax.swing.JPopupMenu popType;
    private javax.swing.JScrollPane scpProp;
    private javax.swing.JTable tbProps;
    private javax.swing.JTree treeTypes;
    // End of variables declaration//GEN-END:variables
}
