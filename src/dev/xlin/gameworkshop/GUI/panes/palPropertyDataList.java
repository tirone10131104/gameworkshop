package dev.xlin.gameworkshop.GUI.panes;

import dev.xlin.gameworkshop.GUI.dialog.dlgPropertyData;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.guiDBLKPalCode;
import dev.xlin.gameworkshop.progs.foundation.beans.beanPropertyData;
import dev.xlin.gameworkshop.progs.foundation.datablockService;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.propertyListData;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import java.util.List;
import java.util.Vector;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockGUIPal;

public class palPropertyDataList extends javax.swing.JPanel implements iAdtDocumentSave, iDatablockFace, iDatablockGUIPal
{

    private wakeup up = null;
    private long dbIndex = 0;
    private boolean needSave = false;
    private boolean bInitTable = false;
    propertyListData propData = null;

    private int oclsID = 0;
    private List lprops = null;

    public palPropertyDataList()
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
            boolean b2 = propData.revertFromXML(xr);
        }
        else
        {
        }
    }

    private void makeTree()
    {
        myTreeNode mrt = guiDBLKPalCode.makeDatablockStructTree(propData);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trStruct.setModel(dtm);
        guiCommon.expandTree(trStruct);
    }

    private void doNewPage()
    {
        int r = guiDBLKPalCode.doNewPage(propData);
        if (r >= 0)
        {
            makeTree();
        }
        else
        {
            fast.err("新增页面失败，或者取消操作。", r);
        }
    }

    private void doNewCol()
    {
        int r = guiDBLKPalCode.doNewCol(getSelectedPage(), propData);
        if (r > 0)
        {
            makeTree();
        }
        else
        {
            fast.err("添加栏目操作失败", -r);
        }
    }

    private void doEditPage()
    {
        int r = guiDBLKPalCode.doEditPage(getSelectedPage(), propData);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("更新分页数据失败", r);
        }
    }

    private void doDelPage()
    {
        myTreeNode mpage = getSelectedPage();
        boolean b = guiDBLKPalCode.doDelPage(mpage, propData);
        if (b)
        {
            makeTree();
        }
        else
        {
            fast.err("删除分页操作失败");
        }
    }

    private void doEditCol()
    {
        myTreeNode mcol = getSelectedCol();
        int r = guiDBLKPalCode.doEditCol(mcol, propData);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("修改栏目数据操作失败", r);
        }
    }

    private void doDelCol()
    {
        int r = guiDBLKPalCode.doDelCol(getSelectedCol(), propData);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("删除栏目操作失败", r);
        }
    }

    private void doMovePageUp()
    {
        myTreeNode mtn = getSelectedPage();
        boolean b = guiDBLKPalCode.doMovePageUp(mtn, propData);
        if (b)
        {
            makeTree();
        }
        else
        {
            fast.warn("不能移动了");
        }
    }

    private void doMovePageDown()
    {
        myTreeNode mtn = getSelectedPage();
        boolean b = guiDBLKPalCode.doMovePageDown(mtn, propData);
        if (b)
        {
            makeTree();
        }
        else
        {
            fast.warn("不能移动了");
        }
    }

    private void doMoveColUp()
    {
        boolean b = guiDBLKPalCode.doMoveColUp(getSelectedCol(), propData);
        if (b)
        {
            makeTree();
        }
        else
        {
            fast.warn("不能移动了");
        }
    }

    private void doMoveColDown()
    {
        boolean b = guiDBLKPalCode.doMoveColUp(getSelectedCol(), propData);
        if (b)
        {
            makeTree();
        }
        else
        {
            fast.warn("不能移动了");
        }
    }

    private void doMoveDataUp()
    {
        boolean b = guiDBLKPalCode.doMoveDataUp(getSelectedDataElement(), propData);
        if (b)
        {
            makeTable();
        }
        else
        {
            fast.warn("不能移动了");
        }
    }

    private void doMoveDataDown()
    {
        boolean b = guiDBLKPalCode.doMoveDataDown(getSelectedDataElement(), propData);
        if (b)
        {
            makeTable();
        }
        else
        {
            fast.warn("不能移动了");
        }
    }

    public iDataElement getSelectedDataElement()
    {
        return guiDBLKPalCode.getSelectedDataElement(tbProps, lprops);
    }

    private void doMoveDataToCol()
    {
        boolean b = guiDBLKPalCode.doMoveDataToCol(getSelectedDataElement(), propData);
        if (b)
        {
            makeTable();
        }
        else
        {
            fast.err("移动操作失败");
        }
    }

    private void doMoveColToPage()
    {
        boolean b = guiDBLKPalCode.doMoveColToPage(getSelectedCol(), propData);
        if (b)
        {
            makeTree();
        }
        else
        {
            fast.err("移动栏目操作失败");
        }
    }

    private void doDisData()
    {
        iDataElement ide = getSelectedDataElement();
        int r = guiDBLKPalCode.disData(ide, propData);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("数据失效操作失败", r);
        }
    }

    private void doRevData()
    {
        iDataElement ide = getSelectedDataElement();
        int r = guiDBLKPalCode.revertData(ide, propData);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("数据恢复操作失败", r);
        }
    }

    private void doDestroyData()
    {
        iDataElement ide = getSelectedDataElement();
        int r = guiDBLKPalCode.destroyData(ide, propData);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("数据销毁操作失败", r);
        }
    }

    private void doSaveAsTemplet()
    {
        int r = guiDBLKPalCode.guiSaveAsTemplet(up, oclsID, propData, dbIndex);
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
        boolean b = guiDBLKPalCode.guiImportTemplet(up, propData);
        if (b)
        {
            makeTree();
            makeTable();
        }
        else
        {
            fast.err("导入数据操作失败或者未完成");
        }
    }

    private myTreeNode getSelectedCol()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return null;
        }
        if (msel.getNodeType() != guiDBLKPalCode.NODE_BLST_COLUMN)
        {
            return null;
        }
        return msel;
    }

    private myTreeNode getSelectedPage()
    {
        myTreeNode msel = getSelectedNode();
        if (msel == null)
        {
            return null;
        }
        if (msel.getNodeType() != guiDBLKPalCode.NODE_BLST_PAGE)
        {
            return null;
        }
        return msel;
    }

    private myTreeNode getSelectedNode()
    {
        TreePath tph = trStruct.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        return (myTreeNode) tph.getLastPathComponent();
    }

    private void makeTable()
    {
        myTableModel mtm = null;
        if (bInitTable)
        {
            mtm = (myTableModel) tbProps.getModel();
            fast.clearTableModel(tbProps);
        }
        else
        {
            mtm = new myTableModel();
            mtm.addColumn("属性名称");
            mtm.addColumn("标签");
            mtm.addColumn("数据类型");
            mtm.addColumn("值");
            mtm.addColumn("公开描述");
            mtm.addColumn("隐藏");
            mtm.addColumn("状态");
            bInitTable = false;
        }
        myTreeNode mcol = getSelectedCol();
        if (mcol != null)
        {
            int icol = mcol.getNodeOID();
            lprops = propData.getDataElementList(icol, ckShowAllProps.isSelected());
            if (lprops != null)
            {
                for (int i = 0; i < lprops.size(); i++)
                {
                    beanPropertyData bpd = (beanPropertyData) lprops.get(i);
                    Vector v = new Vector();
                    v.add(bpd.getPropName());
                    v.add(bpd.getPropTag());
                    v.add(iConst.translate(bpd.getDatatype()));
                    v.add(bpd.getPropValue());
                    v.add(bpd.getPropDescription());
                    v.add(iConst.translateIBOL(bpd.getHide()));
                    v.add(iConst.transDAOState(bpd.getState()));
                    mtm.addRow(v);
                }
            }
        }
        tbProps.setModel(mtm);
    }

    private void doSave(boolean showMsg)
    {
        datablockService dbs = new datablockService(up);
        xmlRight xr = propData.transToXML();
        String sxml = xr.transformToString();
        int r = dbs.updateData(dbIndex, sxml);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            propData.resetSave();
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

    private void doNewProp()
    {
        myTreeNode mcol = getSelectedCol();
        if (mcol == null)
        {
            return;
        }
        dlgPropertyData dlg = new dlgPropertyData(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            beanPropertyData bpd = dlg.getPropertyData();
            int r = propData.appendDataElement(bpd, mcol.getNodeOID());
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTable();
            }
            else
            {
                fast.err("新增属性操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private beanPropertyData getSelectedPropertyData()
    {
        int idx = tbProps.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (beanPropertyData) lprops.get(idx);
    }

    private void doEditProp()
    {
        beanPropertyData bpd = getSelectedPropertyData();
        if (bpd == null)
        {
            return;
        }
        if (bpd.getState() != iDAO.OBJECT_STATE_ACTIVE)
        {
            return;
        }
        dlgPropertyData dlg = new dlgPropertyData(null, true, up, bpd);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            beanPropertyData bdata = dlg.getPropertyData();
            int r = propData.updateDataElement(bdata);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTable();
            }
            else
            {
                fast.err("修改属性数据操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popStruct = new javax.swing.JPopupMenu();
        miNewPage = new javax.swing.JMenuItem();
        miEditPage = new javax.swing.JMenuItem();
        miDeletePage = new javax.swing.JMenuItem();
        miMovePageUp = new javax.swing.JMenuItem();
        miMovePageDown = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miNewCol = new javax.swing.JMenuItem();
        miEditCol = new javax.swing.JMenuItem();
        miMoveCol = new javax.swing.JMenuItem();
        miDeleteCol = new javax.swing.JMenuItem();
        miMoveColUp = new javax.swing.JMenuItem();
        miMoveColDown = new javax.swing.JMenuItem();
        miMoveColToPage = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        miNewPropFromCol = new javax.swing.JMenuItem();
        popProps = new javax.swing.JPopupMenu();
        miNewProp = new javax.swing.JMenuItem();
        miEditProp = new javax.swing.JMenuItem();
        miDisProp = new javax.swing.JMenuItem();
        miRevertProp = new javax.swing.JMenuItem();
        miDestroyProp = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        miMoveProp = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miMoveUp = new javax.swing.JMenuItem();
        miMoveDown = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        trStruct = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProps = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        ckShowAllProps = new javax.swing.JCheckBox();
        btnSave = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        btnImportTemplet = new javax.swing.JButton();
        btnExportTemplet = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        btnPopProps = new javax.swing.JButton();

        miNewPage.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewPage.setText("添加分页");
        miNewPage.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewPageActionPerformed(evt);
            }
        });
        popStruct.add(miNewPage);

        miEditPage.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditPage.setText("修改分页");
        miEditPage.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditPageActionPerformed(evt);
            }
        });
        popStruct.add(miEditPage);

        miDeletePage.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDeletePage.setText("删除分页");
        miDeletePage.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDeletePageActionPerformed(evt);
            }
        });
        popStruct.add(miDeletePage);

        miMovePageUp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMovePageUp.setText("上移页");
        miMovePageUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMovePageUpActionPerformed(evt);
            }
        });
        popStruct.add(miMovePageUp);

        miMovePageDown.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMovePageDown.setText("下移页");
        miMovePageDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMovePageDownActionPerformed(evt);
            }
        });
        popStruct.add(miMovePageDown);
        popStruct.add(jSeparator1);

        miNewCol.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewCol.setText("新增栏目");
        miNewCol.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewColActionPerformed(evt);
            }
        });
        popStruct.add(miNewCol);

        miEditCol.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditCol.setText("修改栏目");
        miEditCol.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditColActionPerformed(evt);
            }
        });
        popStruct.add(miEditCol);

        miMoveCol.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveCol.setText("移动栏目");
        miMoveCol.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveColActionPerformed(evt);
            }
        });
        popStruct.add(miMoveCol);

        miDeleteCol.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDeleteCol.setText("删除栏目");
        miDeleteCol.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDeleteColActionPerformed(evt);
            }
        });
        popStruct.add(miDeleteCol);

        miMoveColUp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveColUp.setText("上移栏目");
        miMoveColUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveColUpActionPerformed(evt);
            }
        });
        popStruct.add(miMoveColUp);

        miMoveColDown.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveColDown.setText("下移栏目");
        miMoveColDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveColDownActionPerformed(evt);
            }
        });
        popStruct.add(miMoveColDown);

        miMoveColToPage.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveColToPage.setText("移动栏目到页");
        miMoveColToPage.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveColToPageActionPerformed(evt);
            }
        });
        popStruct.add(miMoveColToPage);
        popStruct.add(jSeparator4);

        miNewPropFromCol.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewPropFromCol.setForeground(new java.awt.Color(255, 0, 0));
        miNewPropFromCol.setText("添加属性数据");
        miNewPropFromCol.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewPropFromColActionPerformed(evt);
            }
        });
        popStruct.add(miNewPropFromCol);

        miNewProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewProp.setText("增添属性");
        miNewProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewPropActionPerformed(evt);
            }
        });
        popProps.add(miNewProp);

        miEditProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditProp.setText("修改属性");
        miEditProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditPropActionPerformed(evt);
            }
        });
        popProps.add(miEditProp);

        miDisProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisProp.setText("失效属性");
        miDisProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisPropActionPerformed(evt);
            }
        });
        popProps.add(miDisProp);

        miRevertProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevertProp.setText("恢复属性");
        miRevertProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevertPropActionPerformed(evt);
            }
        });
        popProps.add(miRevertProp);

        miDestroyProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDestroyProp.setText("销毁属性");
        miDestroyProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyPropActionPerformed(evt);
            }
        });
        popProps.add(miDestroyProp);
        popProps.add(jSeparator3);

        miMoveProp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveProp.setText("移动属性");
        miMoveProp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMovePropActionPerformed(evt);
            }
        });
        popProps.add(miMoveProp);
        popProps.add(jSeparator2);

        miMoveUp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveUp.setText("上移");
        miMoveUp.setToolTipText("");
        miMoveUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveUpActionPerformed(evt);
            }
        });
        popProps.add(miMoveUp);

        miMoveDown.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveDown.setText("下移");
        miMoveDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveDownActionPerformed(evt);
            }
        });
        popProps.add(miMoveDown);

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

        trStruct.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                trStructMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(trStruct);

        jSplitPane1.setLeftComponent(jScrollPane1);

        tbProps.setModel(new javax.swing.table.DefaultTableModel(
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
        tbProps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbProps.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbPropsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbProps);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        ckShowAllProps.setText("显示全部属性");
        jToolBar1.add(ckShowAllProps);

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
        jToolBar1.add(jSeparator5);

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

        btnExportTemplet.setText("存为模板");
        btnExportTemplet.setFocusable(false);
        btnExportTemplet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExportTemplet.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExportTemplet.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExportTempletActionPerformed(evt);
            }
        });
        jToolBar1.add(btnExportTemplet);

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        btnPopProps.setForeground(new java.awt.Color(0, 0, 204));
        btnPopProps.setText("↓");
        btnPopProps.setFocusable(false);
        btnPopProps.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopProps.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPopProps.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPopPropsMouseClicked(evt);
            }
        });
        btnPopProps.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopPropsActionPerformed(evt);
            }
        });
        jToolBar4.add(btnPopProps);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jToolBar1, jToolBar4});

        jSplitPane1.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void trStructMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_trStructMouseClicked
    {//GEN-HEADEREND:event_trStructMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popStruct.show(trStruct, evt.getX(), evt.getY());
        }
        else
        {
            makeTable();
        }
    }//GEN-LAST:event_trStructMouseClicked

    private void miNewPageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewPageActionPerformed
    {//GEN-HEADEREND:event_miNewPageActionPerformed
        doNewPage();
    }//GEN-LAST:event_miNewPageActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveActionPerformed
    {//GEN-HEADEREND:event_btnSaveActionPerformed
        doSave(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void miNewColActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewColActionPerformed
    {//GEN-HEADEREND:event_miNewColActionPerformed
        doNewCol();
    }//GEN-LAST:event_miNewColActionPerformed

    private void miEditPageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditPageActionPerformed
    {//GEN-HEADEREND:event_miEditPageActionPerformed
        doEditPage();
    }//GEN-LAST:event_miEditPageActionPerformed

    private void miDeletePageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDeletePageActionPerformed
    {//GEN-HEADEREND:event_miDeletePageActionPerformed
        doDelPage();
    }//GEN-LAST:event_miDeletePageActionPerformed

    private void miEditColActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditColActionPerformed
    {//GEN-HEADEREND:event_miEditColActionPerformed
        doEditCol();
    }//GEN-LAST:event_miEditColActionPerformed

    private void miMoveColActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveColActionPerformed
    {//GEN-HEADEREND:event_miMoveColActionPerformed

    }//GEN-LAST:event_miMoveColActionPerformed

    private void miDeleteColActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDeleteColActionPerformed
    {//GEN-HEADEREND:event_miDeleteColActionPerformed
        doDelCol();
    }//GEN-LAST:event_miDeleteColActionPerformed

    private void miNewPropFromColActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewPropFromColActionPerformed
    {//GEN-HEADEREND:event_miNewPropFromColActionPerformed
        doNewProp();
    }//GEN-LAST:event_miNewPropFromColActionPerformed

    private void miNewPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewPropActionPerformed
    {//GEN-HEADEREND:event_miNewPropActionPerformed
        doNewProp();
    }//GEN-LAST:event_miNewPropActionPerformed

    private void tbPropsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbPropsMouseClicked
    {//GEN-HEADEREND:event_tbPropsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popProps.show(tbProps, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbPropsMouseClicked

    private void miEditPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditPropActionPerformed
    {//GEN-HEADEREND:event_miEditPropActionPerformed
        doEditProp();
    }//GEN-LAST:event_miEditPropActionPerformed

    private void btnExportTempletActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExportTempletActionPerformed
    {//GEN-HEADEREND:event_btnExportTempletActionPerformed
        doSaveAsTemplet();
    }//GEN-LAST:event_btnExportTempletActionPerformed

    private void btnImportTempletActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnImportTempletActionPerformed
    {//GEN-HEADEREND:event_btnImportTempletActionPerformed
        doImportTemplet();
    }//GEN-LAST:event_btnImportTempletActionPerformed

    private void miMoveColDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveColDownActionPerformed
    {//GEN-HEADEREND:event_miMoveColDownActionPerformed
        doMoveColDown();
    }//GEN-LAST:event_miMoveColDownActionPerformed

    private void miMovePageUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMovePageUpActionPerformed
    {//GEN-HEADEREND:event_miMovePageUpActionPerformed
        doMovePageUp();
    }//GEN-LAST:event_miMovePageUpActionPerformed

    private void miMovePageDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMovePageDownActionPerformed
    {//GEN-HEADEREND:event_miMovePageDownActionPerformed
        doMovePageDown();
    }//GEN-LAST:event_miMovePageDownActionPerformed

    private void miMoveColUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveColUpActionPerformed
    {//GEN-HEADEREND:event_miMoveColUpActionPerformed
        doMoveColUp();
    }//GEN-LAST:event_miMoveColUpActionPerformed

    private void miMoveUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveUpActionPerformed
    {//GEN-HEADEREND:event_miMoveUpActionPerformed
        doMoveDataUp();
    }//GEN-LAST:event_miMoveUpActionPerformed

    private void miMoveDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveDownActionPerformed
    {//GEN-HEADEREND:event_miMoveDownActionPerformed
        doMoveDataDown();
    }//GEN-LAST:event_miMoveDownActionPerformed

    private void miMovePropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMovePropActionPerformed
    {//GEN-HEADEREND:event_miMovePropActionPerformed
        doMoveDataToCol();
    }//GEN-LAST:event_miMovePropActionPerformed

    private void miMoveColToPageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveColToPageActionPerformed
    {//GEN-HEADEREND:event_miMoveColToPageActionPerformed
        doMoveColToPage();
    }//GEN-LAST:event_miMoveColToPageActionPerformed

    private void miDisPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisPropActionPerformed
    {//GEN-HEADEREND:event_miDisPropActionPerformed
        doDisData();
    }//GEN-LAST:event_miDisPropActionPerformed

    private void miRevertPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertPropActionPerformed
    {//GEN-HEADEREND:event_miRevertPropActionPerformed
        doRevData();
    }//GEN-LAST:event_miRevertPropActionPerformed

    private void miDestroyPropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyPropActionPerformed
    {//GEN-HEADEREND:event_miDestroyPropActionPerformed
        doDestroyData();
    }//GEN-LAST:event_miDestroyPropActionPerformed

    private void btnPopPropsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopPropsMouseClicked
    {//GEN-HEADEREND:event_btnPopPropsMouseClicked
        if (evt.getButton() == evt.BUTTON1)
        {
            popProps.show(btnPopProps, evt.getX(), popProps.getY() + btnPopProps.getHeight());
        }
    }//GEN-LAST:event_btnPopPropsMouseClicked

    private void btnPopPropsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPopPropsActionPerformed
    {//GEN-HEADEREND:event_btnPopPropsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPopPropsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportTemplet;
    private javax.swing.JButton btnImportTemplet;
    private javax.swing.JButton btnPopProps;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox ckShowAllProps;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JMenuItem miDeleteCol;
    private javax.swing.JMenuItem miDeletePage;
    private javax.swing.JMenuItem miDestroyProp;
    private javax.swing.JMenuItem miDisProp;
    private javax.swing.JMenuItem miEditCol;
    private javax.swing.JMenuItem miEditPage;
    private javax.swing.JMenuItem miEditProp;
    private javax.swing.JMenuItem miMoveCol;
    private javax.swing.JMenuItem miMoveColDown;
    private javax.swing.JMenuItem miMoveColToPage;
    private javax.swing.JMenuItem miMoveColUp;
    private javax.swing.JMenuItem miMoveDown;
    private javax.swing.JMenuItem miMovePageDown;
    private javax.swing.JMenuItem miMovePageUp;
    private javax.swing.JMenuItem miMoveProp;
    private javax.swing.JMenuItem miMoveUp;
    private javax.swing.JMenuItem miNewCol;
    private javax.swing.JMenuItem miNewPage;
    private javax.swing.JMenuItem miNewProp;
    private javax.swing.JMenuItem miNewPropFromCol;
    private javax.swing.JMenuItem miRevertProp;
    private javax.swing.JPopupMenu popProps;
    private javax.swing.JPopupMenu popStruct;
    private javax.swing.JTable tbProps;
    private javax.swing.JTree trStruct;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean isNeedSave()
    {
        return propData.isNeedSave();
    }

    @Override
    public void resetSave()
    {
        propData.resetSave();
    }

    @Override
    public void save()
    {
        doSave(false);
    }

    @Override
    public String getDatablockServiceTag()
    {
        return propData.getDatablockServiceTag();
    }

    @Override
    public String getDatablockName()
    {
        return propData.getDatablockName();
    }

    @Override
    public void initData(wakeup _up, long _dbOID, int _oclsID)
    {
        up = _up;
        dbIndex = _dbOID;
        oclsID = _oclsID;
        propData = new propertyListData();
        propData.initDatablcok(up);
        initData();
        makeTree();
        makeTable();
    }

    @Override
    public String getPalTitle()
    {
        return propData.getDatablockName();
    }

}
