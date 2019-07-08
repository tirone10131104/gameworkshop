 
package dev.xlin.gameworkshop.GUI.panes;

import dev.xlin.gameworkshop.GUI.dialog.DlgUseRequestListData;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiDBLKPalCode;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanUseRequestData;
import dev.xlin.gameworkshop.progs.foundation.datablockService;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockGUIPal;
import dev.xlin.gameworkshop.progs.foundation.useRequestListData;
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

/**
 *
 * @author Tirone
 */
public class PalUseRequestDataList extends javax.swing.JPanel implements iAdtDocumentSave, iDatablockFace, iDatablockGUIPal
{

    private useRequestListData useReqs = null;
    private wakeup up = null;
    private int oclsID = 0;
    private long dbIndex = 0;
    private boolean bInitTable = false;
    private List ldatas = null;

    public PalUseRequestDataList()
    {
        initComponents();
    }

    private void makeTree()
    {
        myTreeNode mrt = guiDBLKPalCode.makeDatablockStructTree(useReqs);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trStruct.setModel(dtm);
        guiCommon.expandTree(trStruct);
    }

    private void doNewPage()
    {
        int r = guiDBLKPalCode.doNewPage(useReqs);
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
        int r = guiDBLKPalCode.doNewCol(getSelectedPage(), useReqs);
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
        int r = guiDBLKPalCode.doEditPage(getSelectedPage(), useReqs);
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
        boolean b = guiDBLKPalCode.doDelPage(mpage, useReqs);
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
        int r = guiDBLKPalCode.doEditCol(mcol, useReqs);
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
        int r = guiDBLKPalCode.doDelCol(getSelectedCol(), useReqs);
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
        boolean b = guiDBLKPalCode.doMovePageUp(mtn, useReqs);
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
        boolean b = guiDBLKPalCode.doMovePageDown(mtn, useReqs);
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
        boolean b = guiDBLKPalCode.doMoveColUp(getSelectedCol(), useReqs);
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
        boolean b = guiDBLKPalCode.doMoveColUp(getSelectedCol(), useReqs);
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
        boolean b = guiDBLKPalCode.doMoveDataUp(getSelectedDataElement(), useReqs);
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
        boolean b = guiDBLKPalCode.doMoveDataDown(getSelectedDataElement(), useReqs);
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
        return guiDBLKPalCode.getSelectedDataElement(tbDatas, ldatas);
    }

    private void doMoveDataToCol()
    {
        boolean b = guiDBLKPalCode.doMoveDataToCol(getSelectedDataElement(), useReqs);
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
        boolean b = guiDBLKPalCode.doMoveColToPage(getSelectedCol(), useReqs);
        if (b)
        {
            makeTree();
        }
        else
        {
            fast.err("移动栏目操作失败");
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

    private void doDisData()
    {
        iDataElement ide = getSelectedDataElement();
        int r = guiDBLKPalCode.disData(ide, useReqs);
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
        int r = guiDBLKPalCode.revertData(ide, useReqs);
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
        int r = guiDBLKPalCode.destroyData(ide, useReqs);
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
        int r = guiDBLKPalCode.guiSaveAsTemplet(up, oclsID, useReqs, dbIndex);
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
        boolean b = guiDBLKPalCode.guiImportTemplet(up, useReqs);
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

    private void doSave(boolean showMsg)
    {
        datablockService dbs = new datablockService(up);
        xmlRight xr = useReqs.transToXML();
        String sxml = xr.transformToString();
        int r = dbs.updateData(dbIndex, sxml);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            useReqs.resetSave();
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
            mtm = (myTableModel) tbDatas.getModel();
            fast.clearTableModel(tbDatas);
        }
        else
        {
            mtm = new myTableModel();
            mtm.addColumn("需求类型");
            mtm.addColumn("目标类型");
            mtm.addColumn("目标");
            mtm.addColumn("数量");
            mtm.addColumn("描述");
            mtm.addColumn("隐藏");
            mtm.addColumn("状态");
            bInitTable = true;
        }
        myTreeNode mcol = getSelectedCol();
        if (mcol != null)
        {
            ldatas = useReqs.getDataElementList(mcol.getNodeOID(), ckShowAll.isSelected());
            if (ldatas != null)
            {
                for (int i = 0; i < ldatas.size(); i++)
                {
                    BeanUseRequestData bean = (BeanUseRequestData) ldatas.get(i);
                    Vector v = new Vector();
                    v.add(iConst.translate(bean.getRequestType()));
                    v.add(iConst.translate(bean.getTargetType()));
                    String sname = guiCodes.makeTargetBeanDesp(up, bean.getTargetType(), bean.getTargetOID());
                    v.add(sname);
                    v.add(bean.getQuantity());
                    v.add(bean.getDescription());
                    v.add(iConst.translateIBOL(bean.getHide()));
                    v.add(iConst.transDAOState(bean.getStatus()));
                    mtm.addRow(v);
                }
            }
        }
        tbDatas.setModel(mtm);
    }

    private void doNewData()
    {
        myTreeNode mcol = getSelectedCol();
        if (mcol == null)
        {
            return;
        }
        DlgUseRequestListData dlg = new DlgUseRequestListData(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanUseRequestData bean = dlg.getUseRequestItemData();
            int r = useReqs.appendDataElement(bean, mcol.getNodeOID());
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTable();
            }
            else
            {
                fast.err("修改数据操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void doEditData()
    {
        iDataElement ide = getSelectedDataElement();
        if (ide == null)
        {
            return;
        }
        DlgUseRequestListData dlg = new DlgUseRequestListData(null, true, up, (BeanUseRequestData) ide);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanUseRequestData bean = dlg.getUseRequestItemData();
            int r = useReqs.updateDataElement(bean);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTable();
            }
            else
            {
                fast.err("修改数据操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popDatas = new javax.swing.JPopupMenu();
        miNewData = new javax.swing.JMenuItem();
        miEditData = new javax.swing.JMenuItem();
        miDisData = new javax.swing.JMenuItem();
        miRevertData = new javax.swing.JMenuItem();
        miDestroyData = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        miMoveData = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        miMoveDataUp = new javax.swing.JMenuItem();
        miMoveDataDown = new javax.swing.JMenuItem();
        popStruct = new javax.swing.JPopupMenu();
        miNewPage = new javax.swing.JMenuItem();
        miEditPage = new javax.swing.JMenuItem();
        miDeletePage = new javax.swing.JMenuItem();
        miMovePageUp = new javax.swing.JMenuItem();
        miMovePageDown = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miNewCol = new javax.swing.JMenuItem();
        miEditCol = new javax.swing.JMenuItem();
        miMoveCol = new javax.swing.JMenuItem();
        miDeleteCol = new javax.swing.JMenuItem();
        miMoveColUp = new javax.swing.JMenuItem();
        miMoveColDown = new javax.swing.JMenuItem();
        miMoveColToPage = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        miNewDataFromCol = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        trStruct = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDatas = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        ckShowAll = new javax.swing.JCheckBox();
        btnSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnImportTemplet = new javax.swing.JButton();
        btnSaveAsTemplet = new javax.swing.JButton();
        jToolBar3 = new javax.swing.JToolBar();
        btnPopReq = new javax.swing.JButton();

        miNewData.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewData.setText("增添数据项");
        miNewData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewDataActionPerformed(evt);
            }
        });
        popDatas.add(miNewData);

        miEditData.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditData.setText("修改数据项");
        miEditData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditDataActionPerformed(evt);
            }
        });
        popDatas.add(miEditData);

        miDisData.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisData.setText("失效数据项");
        miDisData.setToolTipText("");
        miDisData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisDataActionPerformed(evt);
            }
        });
        popDatas.add(miDisData);

        miRevertData.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevertData.setText("恢复数据项");
        miRevertData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevertDataActionPerformed(evt);
            }
        });
        popDatas.add(miRevertData);

        miDestroyData.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDestroyData.setText("销毁数据项");
        miDestroyData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyDataActionPerformed(evt);
            }
        });
        popDatas.add(miDestroyData);
        popDatas.add(jSeparator3);

        miMoveData.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveData.setText("移动数据项");
        miMoveData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveDataActionPerformed(evt);
            }
        });
        popDatas.add(miMoveData);
        popDatas.add(jSeparator5);

        miMoveDataUp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveDataUp.setText("上移");
        miMoveDataUp.setToolTipText("");
        miMoveDataUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveDataUpActionPerformed(evt);
            }
        });
        popDatas.add(miMoveDataUp);

        miMoveDataDown.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveDataDown.setText("下移");
        miMoveDataDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveDataDownActionPerformed(evt);
            }
        });
        popDatas.add(miMoveDataDown);

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
        popStruct.add(jSeparator2);

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

        miNewDataFromCol.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewDataFromCol.setForeground(new java.awt.Color(255, 0, 0));
        miNewDataFromCol.setText("添加需求数据");
        miNewDataFromCol.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewDataFromColActionPerformed(evt);
            }
        });
        popStruct.add(miNewDataFromCol);

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

        trStruct.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                trStructMouseClicked(evt);
            }
        });
        trStruct.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trStructValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(trStruct);

        jSplitPane1.setLeftComponent(jScrollPane1);

        tbDatas.setModel(new javax.swing.table.DefaultTableModel(
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
        tbDatas.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbDatasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbDatas);

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

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnPopReq.setForeground(new java.awt.Color(0, 0, 204));
        btnPopReq.setText("↓");
        btnPopReq.setFocusable(false);
        btnPopReq.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopReq.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPopReq.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                btnPopReqMouseReleased(evt);
            }
        });
        jToolBar3.add(btnPopReq);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jToolBar1, jToolBar3});

        jSplitPane1.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
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
    }//GEN-LAST:event_trStructMouseClicked

    private void trStructValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trStructValueChanged
    {//GEN-HEADEREND:event_trStructValueChanged
        makeTable();
    }//GEN-LAST:event_trStructValueChanged

    private void ckShowAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllActionPerformed
    {//GEN-HEADEREND:event_ckShowAllActionPerformed
        makeTable();
    }//GEN-LAST:event_ckShowAllActionPerformed

    private void tbDatasMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbDatasMouseClicked
    {//GEN-HEADEREND:event_tbDatasMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popDatas.show(tbDatas, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbDatasMouseClicked

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

    private void miNewDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewDataActionPerformed
    {//GEN-HEADEREND:event_miNewDataActionPerformed
        doNewData();
    }//GEN-LAST:event_miNewDataActionPerformed

    private void miEditDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditDataActionPerformed
    {//GEN-HEADEREND:event_miEditDataActionPerformed
        doEditData();
    }//GEN-LAST:event_miEditDataActionPerformed

    private void miDisDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisDataActionPerformed
    {//GEN-HEADEREND:event_miDisDataActionPerformed
        doDisData();
    }//GEN-LAST:event_miDisDataActionPerformed

    private void miRevertDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertDataActionPerformed
    {//GEN-HEADEREND:event_miRevertDataActionPerformed
        doRevData();
    }//GEN-LAST:event_miRevertDataActionPerformed

    private void miDestroyDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyDataActionPerformed
    {//GEN-HEADEREND:event_miDestroyDataActionPerformed
        doDestroyData();
    }//GEN-LAST:event_miDestroyDataActionPerformed

    private void miMoveDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveDataActionPerformed
    {//GEN-HEADEREND:event_miMoveDataActionPerformed
        doMoveDataToCol();
    }//GEN-LAST:event_miMoveDataActionPerformed

    private void miMoveDataUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveDataUpActionPerformed
    {//GEN-HEADEREND:event_miMoveDataUpActionPerformed
        doMoveDataUp();
    }//GEN-LAST:event_miMoveDataUpActionPerformed

    private void miMoveDataDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveDataDownActionPerformed
    {//GEN-HEADEREND:event_miMoveDataDownActionPerformed
        doMoveDataDown();
    }//GEN-LAST:event_miMoveDataDownActionPerformed

    private void miNewPageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewPageActionPerformed
    {//GEN-HEADEREND:event_miNewPageActionPerformed
        doNewPage();
    }//GEN-LAST:event_miNewPageActionPerformed

    private void miEditPageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditPageActionPerformed
    {//GEN-HEADEREND:event_miEditPageActionPerformed
        doEditPage();
    }//GEN-LAST:event_miEditPageActionPerformed

    private void miDeletePageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDeletePageActionPerformed
    {//GEN-HEADEREND:event_miDeletePageActionPerformed
        doDelPage();
    }//GEN-LAST:event_miDeletePageActionPerformed

    private void miMovePageUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMovePageUpActionPerformed
    {//GEN-HEADEREND:event_miMovePageUpActionPerformed
        doMovePageUp();
    }//GEN-LAST:event_miMovePageUpActionPerformed

    private void miMovePageDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMovePageDownActionPerformed
    {//GEN-HEADEREND:event_miMovePageDownActionPerformed
        doMovePageDown();
    }//GEN-LAST:event_miMovePageDownActionPerformed

    private void miNewColActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewColActionPerformed
    {//GEN-HEADEREND:event_miNewColActionPerformed
        doNewCol();
    }//GEN-LAST:event_miNewColActionPerformed

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

    private void miMoveColUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveColUpActionPerformed
    {//GEN-HEADEREND:event_miMoveColUpActionPerformed
        doMoveColUp();
    }//GEN-LAST:event_miMoveColUpActionPerformed

    private void miMoveColDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveColDownActionPerformed
    {//GEN-HEADEREND:event_miMoveColDownActionPerformed
        doMoveColDown();
    }//GEN-LAST:event_miMoveColDownActionPerformed

    private void miMoveColToPageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveColToPageActionPerformed
    {//GEN-HEADEREND:event_miMoveColToPageActionPerformed
        doMoveColToPage();
    }//GEN-LAST:event_miMoveColToPageActionPerformed

    private void miNewDataFromColActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewDataFromColActionPerformed
    {//GEN-HEADEREND:event_miNewDataFromColActionPerformed
        doNewData();
    }//GEN-LAST:event_miNewDataFromColActionPerformed

    private void btnPopReqMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopReqMouseReleased
    {//GEN-HEADEREND:event_btnPopReqMouseReleased
        if (evt.getButton() == evt.BUTTON1)
        {
            popDatas.show(btnPopReq, evt.getX(), popDatas.getY() + btnPopReq.getHeight());
        }
    }//GEN-LAST:event_btnPopReqMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnImportTemplet;
    private javax.swing.JButton btnPopReq;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveAsTemplet;
    private javax.swing.JCheckBox ckShowAll;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JMenuItem miDeleteCol;
    private javax.swing.JMenuItem miDeletePage;
    private javax.swing.JMenuItem miDestroyData;
    private javax.swing.JMenuItem miDisData;
    private javax.swing.JMenuItem miEditCol;
    private javax.swing.JMenuItem miEditData;
    private javax.swing.JMenuItem miEditPage;
    private javax.swing.JMenuItem miMoveCol;
    private javax.swing.JMenuItem miMoveColDown;
    private javax.swing.JMenuItem miMoveColToPage;
    private javax.swing.JMenuItem miMoveColUp;
    private javax.swing.JMenuItem miMoveData;
    private javax.swing.JMenuItem miMoveDataDown;
    private javax.swing.JMenuItem miMoveDataUp;
    private javax.swing.JMenuItem miMovePageDown;
    private javax.swing.JMenuItem miMovePageUp;
    private javax.swing.JMenuItem miNewCol;
    private javax.swing.JMenuItem miNewData;
    private javax.swing.JMenuItem miNewDataFromCol;
    private javax.swing.JMenuItem miNewPage;
    private javax.swing.JMenuItem miRevertData;
    private javax.swing.JPopupMenu popDatas;
    private javax.swing.JPopupMenu popStruct;
    private javax.swing.JTable tbDatas;
    private javax.swing.JTree trStruct;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean isNeedSave()
    {
        return useReqs.isNeedSave();
    }

    @Override
    public void resetSave()
    {
        useReqs.resetSave();
    }

    @Override
    public void save()
    {
        doSave(false);
    }

    @Override
    public String getDatablockServiceTag()
    {
        return useReqs.getDatablockServiceTag();
    }

    @Override
    public String getDatablockName()
    {
        return useReqs.getDatablockName();
    }

    @Override
    public void initData(wakeup _up, long _dbOID, int _oclsID)
    {
        up = _up;
        dbIndex = _dbOID;
        oclsID = _oclsID;
        useReqs = new useRequestListData();
        useReqs.initDatablcok(up);
        initDatablock();
        makeTree();
        makeTable();
    }

    private void initDatablock()
    {
        datablockService dbs = new datablockService(up);
        String sblock = dbs.loadData(dbIndex);
        xmlRight xr = new xmlRight();
        boolean b = xr.parseXMLfromString(sblock);
        if (b)
        {
            boolean b2 = useReqs.revertFromXML(xr);
        }
        else
        {
        }
    }

    @Override
    public String getPalTitle()
    {
        return useReqs.getDatablockName();
    }
}
