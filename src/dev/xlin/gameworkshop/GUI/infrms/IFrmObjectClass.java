package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.DlgObjectClss;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.codeTools;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class IFrmObjectClass extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private boolean bInitTable = false;
    private List locs = null;

    public IFrmObjectClass(wakeup _up, JDesktopPane _desk)
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
        myTreeNode mrt = guiCodes.makeFlatTypeTree(up, systemType.CODE_STT_OBJ_CLS, ckShowAllType.isSelected());
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        treeType.setModel(dtm);
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
            mtm.addColumn("属性");
            mtm.addColumn("效果");
            mtm.addColumn("制造");
            mtm.addColumn("回收");
            mtm.addColumn("使用");
            mtm.addColumn("状态");
            bInitTable = true;
        }
        else
        {
            fast.clearTableModel(tbOCLS);
            mtm = (myTableModel) tbOCLS.getModel();
        }
        objectClassDefine ocd = new objectClassDefine(up);
        myTreeNode mtn = getSelectType();
        int tpid = 0;
        if (mtn != null)
        {
            tpid = mtn.getNodeOID();
        }
        locs = ocd.getObjectClassByType(tpid, ckShowAllOCLS.isSelected());
        if (locs != null)
        {
            for (int i = 0; i < locs.size(); i++)
            {
                BeanObjectClass boc = (BeanObjectClass) locs.get(i);
                Vector v = new Vector();
                v.add(boc.getClassName());
                v.add(boc.getClassTag());
                v.add(boc.getClassDesp());
                int[] ifcs = codeTools.convertStrToArr(boc.getClassFuncs());
                ArrayList afc = fast.makeIntArrayList(ifcs);
                if (afc.contains(iConst.OCLS_FUNC_PROPERTY_LIST))
                {
                    v.add("是");
                }
                else
                {
                    v.add("-");
                }
                if (afc.contains(iConst.OCLS_FUNC_EFFECT_LIST))
                {
                    v.add("是");
                }
                else
                {
                    v.add("-");
                }
                if (afc.contains(iConst.OCLS_FUNC_BUILD_LIST))
                {
                    v.add("是");
                }
                else
                {
                    v.add("-");
                }
                if (afc.contains(iConst.OCLS_FUNC_RECYCLE_LIST))
                {
                    v.add("是");
                }
                else
                {
                    v.add("-");
                }
                if (afc.contains(iConst.OCLS_FUNC_USE_REQUEST_LIST))
                {
                    v.add("是");
                }
                else
                {
                    v.add("-");
                }
                v.add(iConst.transDAOState(boc.getState()));
                mtm.addRow(v);
            }
        }
        tbOCLS.setModel(mtm);
    }

    private void doNewType()
    {
        String sip = fast.input("请输入一个分类名称");
        if (sip == null)
        {
            return;
        }
        if (sip.trim().equals(""))
        {
            return;
        }
        beanSttType bst = new beanSttType();
        bst.setTypeName(sip);
        bst.setSttID(systemType.CODE_STT_OBJ_CLS);
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

    private void doEditType()
    {
        myTreeNode mtn = getSelectType();
        if (mtn == null)
        {
            return;
        }
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(mtn.getNodeOID());
        String sip = fast.input("请输入分类名称", bst.getTypeName());
        if (sip == null)
        {
            return;
        }
        if (sip.trim().equals(""))
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
        myTreeNode mtn = getSelectType();
        if (mtn == null)
        {
            return;
        }
        int sel = fast.ask("确认删除这个分类？");
        if (sel != fast.YES)
        {
            return;
        }
        objectClassDefine ocd = new objectClassDefine(up);
        if (ocd.getObjectClassByType(mtn.getNodeOID(), true) != null)
        {
            fast.warn("这个分类下还有物类，不允许执行失效操作");
            return;
        }
        int r = ocd.deleteRecord(mtn.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doDestroyType()
    {
        myTreeNode mtn = getSelectType();
        if (mtn == null)
        {
            return;
        }
        int sel = fast.ask("是否销毁这个失效的分类?\n销毁操作不可再被恢复");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        int r = stp.destroyBean(mtn.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("销毁分类操作失败", r);
        }
    }

    private void doRevertType()
    {
        myTreeNode mtn = getSelectType();
        if (mtn == null)
        {
            return;
        }
        int sel = fast.ask("是否恢复这个失效的分类?");
        if (sel != fast.YES)
        {
            return;
        }
        sttType stp = new sttType(up);
        int r = stp.revertBean(mtn.getNodeOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("恢复分类操作失败", r);
        }
    }

    private void doNewOCLS()
    {
        myTreeNode mtn = getSelectType();
        if (mtn == null)
        {
            return;
        }
        DlgObjectClss dlg = new DlgObjectClss(null, true, up, null, mtn.getNodeOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanObjectClass getSelectObjectClass()
    {
        int idx = tbOCLS.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanObjectClass) locs.get(idx);
    }

    private void doEditOCLS()
    {
        BeanObjectClass boc = getSelectObjectClass();
        if (boc == null)
        {
            return;
        }
        DlgObjectClss dlg = new DlgObjectClss(null, true, up, boc, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDisOCLS()
    {
        BeanObjectClass boc = getSelectObjectClass();
        if (boc == null)
        {
            return;
        }
        int sel = fast.ask("是否失效这个选中的物类？");
        if (sel != fast.YES)
        {
            return;
        }
        objectClassDefine ocd = new objectClassDefine(up);
        int r = ocd.deleteRecord(boc.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("失效物类操作失败", r);
        }
    }

    private void doRevertOCLS()
    {
        BeanObjectClass boc = getSelectObjectClass();
        if (boc == null)
        {
            return;
        }
        if (boc.getState() != iDAO.OBJECT_STATE_ACTIVE)
        {
            fast.warn("没必要恢复这个物类");
            return;
        }
        int sel = fast.ask("是否恢复这个选中的物类？");
        if (sel != fast.YES)
        {
            return;
        }
        objectClassDefine ocd = new objectClassDefine(up);
        int r = ocd.revertBean(boc.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("恢复物类操作失败", r);
        }
    }

    private void doDestroyOCLS()
    {
        BeanObjectClass boc = getSelectObjectClass();
        if (boc == null)
        {
            return;
        }
        if (boc.getState() != iDAO.OBJECT_STATE_DELETE)
        {
            fast.warn("无法销毁不是失效的物类");
            return;
        }
        int sel = fast.ask("是否销毁这个选中的物类？\n销毁操作不可恢复。");
        if (sel != fast.YES)
        {
            return;
        }
        objectClassDefine ocd = new objectClassDefine(up);
        int r = ocd.destroyBean(boc.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("恢复物类操作失败", r);
        }
    }

    private void doMoveOCLS()
    {

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
        popOCLS = new javax.swing.JPopupMenu();
        miNewOcls = new javax.swing.JMenuItem();
        miEditOcls = new javax.swing.JMenuItem();
        miDisableOcls = new javax.swing.JMenuItem();
        miMoveOcls = new javax.swing.JMenuItem();
        miRevertOcls = new javax.swing.JMenuItem();
        miDestroyOcls = new javax.swing.JMenuItem();
        jToolBar1 = new javax.swing.JToolBar();
        btnNewType = new javax.swing.JButton();
        btnEditType = new javax.swing.JButton();
        btnDisType = new javax.swing.JButton();
        btnRevertType = new javax.swing.JButton();
        btnDestroyType = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnNewOCLS = new javax.swing.JButton();
        btnEditOCLS = new javax.swing.JButton();
        btnDisOCLS = new javax.swing.JButton();
        btnMoveOCLS = new javax.swing.JButton();
        btnRevertOCLS = new javax.swing.JButton();
        btnDesOCLS = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        ckShowAllType = new javax.swing.JCheckBox();
        ckShowAllOCLS = new javax.swing.JCheckBox();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeType = new javax.swing.JTree();
        scpOCLS = new javax.swing.JScrollPane();
        tbOCLS = new javax.swing.JTable();

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

        miNewOcls.setText("新建类型");
        miNewOcls.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewOclsActionPerformed(evt);
            }
        });
        popOCLS.add(miNewOcls);

        miEditOcls.setText("修改类型");
        miEditOcls.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditOclsActionPerformed(evt);
            }
        });
        popOCLS.add(miEditOcls);

        miDisableOcls.setText("失效类型");
        miDisableOcls.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisableOclsActionPerformed(evt);
            }
        });
        popOCLS.add(miDisableOcls);

        miMoveOcls.setText("移动至分类");
        miMoveOcls.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveOclsActionPerformed(evt);
            }
        });
        popOCLS.add(miMoveOcls);

        miRevertOcls.setText("恢复类型");
        miRevertOcls.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevertOclsActionPerformed(evt);
            }
        });
        popOCLS.add(miRevertOcls);

        miDestroyOcls.setText("销毁类型");
        miDestroyOcls.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyOclsActionPerformed(evt);
            }
        });
        popOCLS.add(miDestroyOcls);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("物体类型管理器");
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

        btnDisType.setText("失效分类");
        btnDisType.setFocusable(false);
        btnDisType.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDisType.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDisType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDisTypeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDisType);

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

        btnDestroyType.setText("销毁分类");
        btnDestroyType.setFocusable(false);
        btnDestroyType.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDestroyType.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDestroyType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDestroyTypeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDestroyType);
        jToolBar1.add(jSeparator1);

        btnNewOCLS.setText("新建类型");
        btnNewOCLS.setFocusable(false);
        btnNewOCLS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewOCLS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewOCLS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewOCLSActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNewOCLS);

        btnEditOCLS.setText("修改类型");
        btnEditOCLS.setFocusable(false);
        btnEditOCLS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditOCLS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditOCLS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditOCLSActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditOCLS);

        btnDisOCLS.setText("失效类型");
        btnDisOCLS.setFocusable(false);
        btnDisOCLS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDisOCLS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDisOCLS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDisOCLSActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDisOCLS);

        btnMoveOCLS.setText("移动类型");
        btnMoveOCLS.setFocusable(false);
        btnMoveOCLS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMoveOCLS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMoveOCLS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMoveOCLSActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMoveOCLS);

        btnRevertOCLS.setText("恢复类型");
        btnRevertOCLS.setFocusable(false);
        btnRevertOCLS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRevertOCLS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRevertOCLS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRevertOCLSActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRevertOCLS);

        btnDesOCLS.setText("销毁类型");
        btnDesOCLS.setFocusable(false);
        btnDesOCLS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDesOCLS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDesOCLS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDesOCLSActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDesOCLS);

        ckShowAllType.setText("显示全部分类");

        ckShowAllOCLS.setText("显示全部类型");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckShowAllOCLS)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ckShowAllType)
                .addComponent(ckShowAllOCLS))
        );

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

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

        jSplitPane1.setLeftComponent(jScrollPane1);

        scpOCLS.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scpOCLSMouseClicked(evt);
            }
        });

        tbOCLS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbOCLS.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbOCLS.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbOCLS.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbOCLSMouseClicked(evt);
            }
        });
        scpOCLS.setViewportView(tbOCLS);

        jSplitPane1.setRightComponent(scpOCLS);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewTypeActionPerformed
    {//GEN-HEADEREND:event_btnNewTypeActionPerformed
        doNewType();
    }//GEN-LAST:event_btnNewTypeActionPerformed

    private void btnDestroyTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDestroyTypeActionPerformed
    {//GEN-HEADEREND:event_btnDestroyTypeActionPerformed
        doDestroyType();
    }//GEN-LAST:event_btnDestroyTypeActionPerformed

    private void btnEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditTypeActionPerformed
    {//GEN-HEADEREND:event_btnEditTypeActionPerformed
        doEditType();
    }//GEN-LAST:event_btnEditTypeActionPerformed

    private void btnDisTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDisTypeActionPerformed
    {//GEN-HEADEREND:event_btnDisTypeActionPerformed
        doDisType();
    }//GEN-LAST:event_btnDisTypeActionPerformed

    private void btnRevertTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRevertTypeActionPerformed
    {//GEN-HEADEREND:event_btnRevertTypeActionPerformed
        doRevertType();
    }//GEN-LAST:event_btnRevertTypeActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsObjectClass(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnNewOCLSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewOCLSActionPerformed
    {//GEN-HEADEREND:event_btnNewOCLSActionPerformed
        doNewOCLS();
    }//GEN-LAST:event_btnNewOCLSActionPerformed

    private void btnEditOCLSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditOCLSActionPerformed
    {//GEN-HEADEREND:event_btnEditOCLSActionPerformed
        doEditOCLS();
    }//GEN-LAST:event_btnEditOCLSActionPerformed

    private void btnDisOCLSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDisOCLSActionPerformed
    {//GEN-HEADEREND:event_btnDisOCLSActionPerformed
        doDisOCLS();
    }//GEN-LAST:event_btnDisOCLSActionPerformed

    private void btnMoveOCLSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMoveOCLSActionPerformed
    {//GEN-HEADEREND:event_btnMoveOCLSActionPerformed
        doMoveOCLS();
    }//GEN-LAST:event_btnMoveOCLSActionPerformed

    private void btnRevertOCLSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRevertOCLSActionPerformed
    {//GEN-HEADEREND:event_btnRevertOCLSActionPerformed
        doRevertOCLS();
    }//GEN-LAST:event_btnRevertOCLSActionPerformed

    private void btnDesOCLSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDesOCLSActionPerformed
    {//GEN-HEADEREND:event_btnDesOCLSActionPerformed
        doDestroyOCLS();
    }//GEN-LAST:event_btnDesOCLSActionPerformed

    private void treeTypeValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeTypeValueChanged
    {//GEN-HEADEREND:event_treeTypeValueChanged
        makeTable();
    }//GEN-LAST:event_treeTypeValueChanged

    private void treeTypeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeTypeMouseClicked
    {//GEN-HEADEREND:event_treeTypeMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popType.show(treeType, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >=2)
        {
            doEditType();
        }
    }//GEN-LAST:event_treeTypeMouseClicked

    private void scpOCLSMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scpOCLSMouseClicked
    {//GEN-HEADEREND:event_scpOCLSMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popOCLS.show(scpOCLS, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1)
        {
            doNewOCLS();
        }
    }//GEN-LAST:event_scpOCLSMouseClicked

    private void tbOCLSMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbOCLSMouseClicked
    {//GEN-HEADEREND:event_tbOCLSMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popOCLS.show(tbOCLS, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >=2)
        {
            doEditOCLS();
        }
    }//GEN-LAST:event_tbOCLSMouseClicked

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
        doDisType();
    }//GEN-LAST:event_miDisableTypeActionPerformed

    private void miRevertTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertTypeActionPerformed
    {//GEN-HEADEREND:event_miRevertTypeActionPerformed
        doRevertType();
    }//GEN-LAST:event_miRevertTypeActionPerformed

    private void miDestroyTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyTypeActionPerformed
    {//GEN-HEADEREND:event_miDestroyTypeActionPerformed
        doDestroyType();
    }//GEN-LAST:event_miDestroyTypeActionPerformed

    private void miNewOclsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewOclsActionPerformed
    {//GEN-HEADEREND:event_miNewOclsActionPerformed
        doNewOCLS();
    }//GEN-LAST:event_miNewOclsActionPerformed

    private void miEditOclsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditOclsActionPerformed
    {//GEN-HEADEREND:event_miEditOclsActionPerformed
        doEditOCLS();
    }//GEN-LAST:event_miEditOclsActionPerformed

    private void miDisableOclsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisableOclsActionPerformed
    {//GEN-HEADEREND:event_miDisableOclsActionPerformed
       doDisOCLS();
    }//GEN-LAST:event_miDisableOclsActionPerformed

    private void miMoveOclsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveOclsActionPerformed
    {//GEN-HEADEREND:event_miMoveOclsActionPerformed
        doMoveOCLS();
    }//GEN-LAST:event_miMoveOclsActionPerformed

    private void miRevertOclsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertOclsActionPerformed
    {//GEN-HEADEREND:event_miRevertOclsActionPerformed
        doRevertOCLS();
    }//GEN-LAST:event_miRevertOclsActionPerformed

    private void miDestroyOclsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyOclsActionPerformed
    {//GEN-HEADEREND:event_miDestroyOclsActionPerformed
        doDestroyOCLS();
    }//GEN-LAST:event_miDestroyOclsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDesOCLS;
    private javax.swing.JButton btnDestroyType;
    private javax.swing.JButton btnDisOCLS;
    private javax.swing.JButton btnDisType;
    private javax.swing.JButton btnEditOCLS;
    private javax.swing.JButton btnEditType;
    private javax.swing.JButton btnMoveOCLS;
    private javax.swing.JButton btnNewOCLS;
    private javax.swing.JButton btnNewType;
    private javax.swing.JButton btnRevertOCLS;
    private javax.swing.JButton btnRevertType;
    private javax.swing.JCheckBox ckShowAllOCLS;
    private javax.swing.JCheckBox ckShowAllType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem miDestroyOcls;
    private javax.swing.JMenuItem miDestroyType;
    private javax.swing.JMenuItem miDisableOcls;
    private javax.swing.JMenuItem miDisableType;
    private javax.swing.JMenuItem miEditOcls;
    private javax.swing.JMenuItem miEditType;
    private javax.swing.JMenuItem miMoveOcls;
    private javax.swing.JMenuItem miNewOcls;
    private javax.swing.JMenuItem miNewType;
    private javax.swing.JMenuItem miRevertOcls;
    private javax.swing.JMenuItem miRevertType;
    private javax.swing.JPopupMenu popOCLS;
    private javax.swing.JPopupMenu popType;
    private javax.swing.JScrollPane scpOCLS;
    private javax.swing.JTable tbOCLS;
    private javax.swing.JTree treeType;
    // End of variables declaration//GEN-END:variables
}
