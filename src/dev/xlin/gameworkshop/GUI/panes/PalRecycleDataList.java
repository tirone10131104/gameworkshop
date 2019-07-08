package dev.xlin.gameworkshop.GUI.panes;

import dev.xlin.gameworkshop.GUI.dialog.DlgRecycleListData;
import dev.xlin.gameworkshop.GUI.dialog.DlgRecycleRequestData;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiDBLKPalCode;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanRecycleListItemData;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanRecycleRequestListData;
import dev.xlin.gameworkshop.progs.foundation.datablockService;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtDocumentSave;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockGUIPal;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.recycleListData;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import dev.xlin.tools.constChk;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class PalRecycleDataList extends javax.swing.JPanel implements iAdtDocumentSave, iDatablockFace, iDatablockGUIPal
{

    private recycleListData recyData = null;
    private wakeup up = null;
    private int oclsID = 0;
    private long dbIndex = 0;
    private boolean bInitTableRecy = false;
    private boolean bInitTableReq = false;
    private List ldatas = null;
    private List lreqs = null;

    public PalRecycleDataList()
    {
        initComponents();
    }

    private void makeTree()
    {
        myTreeNode mrt = guiDBLKPalCode.makeDatablockStructTree(recyData);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trStruct.setModel(dtm);
        guiCommon.expandTree(trStruct);
    }

    private void doNewPage()
    {
        int r = guiDBLKPalCode.doNewPage(recyData);
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
        int r = guiDBLKPalCode.doNewCol(getSelectedPage(), recyData);
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
        int r = guiDBLKPalCode.doEditPage(getSelectedPage(), recyData);
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
        boolean b = guiDBLKPalCode.doDelPage(mpage, recyData);
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
        int r = guiDBLKPalCode.doEditCol(mcol, recyData);
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
        int r = guiDBLKPalCode.doDelCol(getSelectedCol(), recyData);
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
        boolean b = guiDBLKPalCode.doMovePageUp(mtn, recyData);
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
        boolean b = guiDBLKPalCode.doMovePageDown(mtn, recyData);
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
        boolean b = guiDBLKPalCode.doMoveColUp(getSelectedCol(), recyData);
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
        boolean b = guiDBLKPalCode.doMoveColUp(getSelectedCol(), recyData);
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
        boolean b = guiDBLKPalCode.doMoveDataUp(getSelectedDataElement(), recyData);
        if (b)
        {
            makeTableRecy();
        }
        else
        {
            fast.warn("不能移动了");
        }
    }

    private void doMoveDataDown()
    {
        boolean b = guiDBLKPalCode.doMoveDataDown(getSelectedDataElement(), recyData);
        if (b)
        {
            makeTableRecy();
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
        boolean b = guiDBLKPalCode.doMoveDataToCol(getSelectedDataElement(), recyData);
        if (b)
        {
            makeTableRecy();
        }
        else
        {
            fast.err("移动操作失败");
        }
    }

    private void doMoveColToPage()
    {
        boolean b = guiDBLKPalCode.doMoveColToPage(getSelectedCol(), recyData);
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

    private void doDisData()
    {
        iDataElement ide = getSelectedDataElement();
        int r = guiDBLKPalCode.disData(ide, recyData);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTableRecy();
        }
        else
        {
            fast.err("数据失效操作失败", r);
        }
    }

    private void doRevData()
    {
        iDataElement ide = getSelectedDataElement();
        int r = guiDBLKPalCode.revertData(ide, recyData);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTableRecy();
        }
        else
        {
            fast.err("数据恢复操作失败", r);
        }
    }

    private void doDestroyData()
    {
        iDataElement ide = getSelectedDataElement();
        int r = guiDBLKPalCode.destroyData(ide, recyData);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTableRecy();
        }
        else
        {
            fast.err("数据销毁操作失败", r);
        }
    }

    private void doSaveAsTemplet()
    {
        int r = guiDBLKPalCode.guiSaveAsTemplet(up, oclsID, recyData, dbIndex);
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
        boolean b = guiDBLKPalCode.guiImportTemplet(up, recyData);
        if (b)
        {
            makeTree();
            makeTableRecy();
        }
        else
        {
            fast.err("导入数据操作失败或者未完成");
        }
    }

    private void doSave(boolean showMsg)
    {
        datablockService dbs = new datablockService(up);
        xmlRight xr = recyData.transToXML();
        String sxml = xr.transformToString();
        int r = dbs.updateData(dbIndex, sxml);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            recyData.resetSave();
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

    private void makeTableReq()
    {
        myTableModel mtm = null;
        if (bInitTableReq)
        {
            mtm = (myTableModel) tbReqs.getModel();
            fast.clearTableModel(tbReqs);
        }
        else
        {
            mtm = new myTableModel();
            mtm.addColumn("目标类型");
            mtm.addColumn("目标");
            mtm.addColumn("方法");
            mtm.addColumn("数量");
            mtm.addColumn("描述");
            mtm.addColumn("隐藏");
            mtm.addColumn("状态");
            bInitTableReq = true;
            tbReqs.setModel(mtm);
        }
        lreqs = recyData.getRecycleRequestList(ckShowAllReqs.isSelected(), swsys.getComboBoxSelected(cmbReqTarType));
        for (int i = 0; i < lreqs.size(); i++)
        {
            BeanRecycleRequestListData brr = (BeanRecycleRequestListData) lreqs.get(i);
            Object[] ros = fast.makeObjectArray(7);
            ros[0] = iConst.translate(brr.getTargetType());
            String s = guiCodes.makeTargetBeanDesp(up, brr.getTargetType(), brr.getTargetOID());
            ros[1] = s;
            ros[2] = iConst.translate(brr.getRequestMethod());
            ros[3] = brr.getQuantity();
            ros[4] = brr.getDescription();
            ros[5] = iConst.translateIBOL(brr.getHide());
            ros[6] = iConst.transDAOState(brr.getStatus());
            mtm.addRow(ros);
        }
        tbReqs.setModel(mtm);
    }

    private void makeTableRecy()
    {
        myTableModel mtm = null;
        if (bInitTableRecy)
        {
            mtm = (myTableModel) tbDatas.getModel();
            fast.clearTableModel(tbDatas);
        }
        else
        {
            mtm = new myTableModel();
            mtm.addColumn("物体");
            mtm.addColumn("数量");
            mtm.addColumn("描述");
            mtm.addColumn("隐藏");
            mtm.addColumn("状态");
            bInitTableRecy = true;
        }
        myTreeNode mcol = getSelectedCol();
        if (mcol != null)
        {
            ldatas = recyData.getDataElementList(mcol.getNodeOID(), ckShowAll.isSelected());
            if (ldatas != null)
            {
                itemDefine idef = new itemDefine(up);
                for (int i = 0; i < ldatas.size(); i++)
                {
                    BeanRecycleListItemData bcy = (BeanRecycleListItemData) ldatas.get(i);
                    Vector v = new Vector();
                    BeanItem bit = (BeanItem) idef.getRecordByID(bcy.getItemOID());
                    v.add(bit.getItemName());
                    v.add(bcy.getQuantity());
                    v.add(bcy.getDescription());
                    v.add(iConst.translateIBOL(bcy.getHide()));
                    v.add(iConst.transDAOState(bcy.getStatus()));
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
        DlgRecycleListData dlg = new DlgRecycleListData(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanRecycleListItemData bean = dlg.getRecycleListItemData();
            int r = recyData.appendDataElement(bean, mcol.getNodeOID());
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTableRecy();
            }
            else
            {
                fast.err("新建操作失败", r);
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
        DlgRecycleListData dlg = new DlgRecycleListData(null, true, up, (BeanRecycleListItemData) ide);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanRecycleListItemData bean = dlg.getRecycleListItemData();
            int r = recyData.updateDataElement(bean);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTableRecy();
            }
            else
            {
                fast.err("修改操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void newReq()
    {
        DlgRecycleRequestData dlg = new DlgRecycleRequestData(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanRecycleRequestListData bean = dlg.getRecycleRequestListData();
            int r = recyData.appendRecycleRequestItem(bean);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTableReq();
            }
            else
            {
                fast.err("添加数据操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanRecycleRequestListData getSelectedReq()
    {
        int idx = tbReqs.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanRecycleRequestListData) lreqs.get(idx);
    }

    private void editReq()
    {
        BeanRecycleRequestListData bean = getSelectedReq();
        if (bean == null)
        {
            return;
        }
        DlgRecycleRequestData dlg = new DlgRecycleRequestData(null, true, up, bean);
        dlg.setVisible(true );
        if (dlg.getOK())
        {
            BeanRecycleRequestListData brrld = dlg.getRecycleRequestListData();
            int r = recyData.updateRecycleRequestItem(brrld);
            if (r == iDAO.OPERATE_SUCCESS)
            {
                makeTableReq();
            }
            else
            {
                fast.err("修改数据操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void disableReq()
    {
        BeanRecycleRequestListData bean = getSelectedReq();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将这个选定的需求数据设置为失效");
        if (sel != fast.YES)
        {
            return;
        }
        int r = recyData.disableRecycleRequestItem(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTableReq();
        }
        else
        {
            fast.err("失效数据操作失败", r);
        }
    }

    private void revertReq()
    {
        BeanRecycleRequestListData bean = getSelectedReq();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将这个选定的需求数据恢复为有效");
        if (sel != fast.YES)
        {
            return;
        }
        int r = recyData.revertRecycleRequestItem(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTableReq();
        }
        else
        {
            fast.err("失效数据操作失败", r);
        }
    }

    private void destroyReq()
    {
        BeanRecycleRequestListData bean = getSelectedReq();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否将这个选定的需求数据销毁？");
        if (sel != fast.YES)
        {
            return;
        }
        int r = recyData.destroyRecycleRequestItem(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTableReq();
        }
        else
        {
            fast.err("销毁数据操作失败", r);
        }
    }

    private void moveReqUp()
    {
        BeanRecycleRequestListData bean = getSelectedReq();
        if (bean == null)
        {
            return;
        }
        int r = recyData.moveRecycleRequestUp(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTableReq();
        }
    }

    private void moveReqDown()
    {
        BeanRecycleRequestListData bean = getSelectedReq();
        if (bean == null)
        {
            return;
        }
        int r = recyData.moveRecycleRequestDown(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTableReq();
        }
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
        popReqs = new javax.swing.JPopupMenu();
        miNewReq = new javax.swing.JMenuItem();
        miEditReq = new javax.swing.JMenuItem();
        miDisableReq = new javax.swing.JMenuItem();
        miRevertReq = new javax.swing.JMenuItem();
        miDestroyReq = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        miMoveReqUp = new javax.swing.JMenuItem();
        miMoveReqDown = new javax.swing.JMenuItem();
        tabs = new javax.swing.JTabbedPane();
        palRecyList = new javax.swing.JPanel();
        spRecyList = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        trStruct = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        ckShowAll = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDatas = new javax.swing.JTable();
        palRecyReqs = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        ckShowAllReqs = new javax.swing.JCheckBox();
        cmbReqTarType = new javax.swing.JComboBox<>();
        btnNewRequest = new javax.swing.JButton();
        btnEditReq = new javax.swing.JButton();
        btnDisable = new javax.swing.JButton();
        btnRevert = new javax.swing.JButton();
        btnDestroy = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btnReqUp = new javax.swing.JButton();
        btnReqDown = new javax.swing.JButton();
        scpReqs = new javax.swing.JScrollPane();
        tbReqs = new javax.swing.JTable();
        jToolBar3 = new javax.swing.JToolBar();
        btnPopReq = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnImportTemplet = new javax.swing.JButton();
        btnSaveAsTemplet = new javax.swing.JButton();

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
        miNewDataFromCol.setText("添加回收数据");
        miNewDataFromCol.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewDataFromColActionPerformed(evt);
            }
        });
        popStruct.add(miNewDataFromCol);

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

        miNewReq.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewReq.setText("添加需求");
        miNewReq.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewReqActionPerformed(evt);
            }
        });
        popReqs.add(miNewReq);

        miEditReq.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditReq.setText("修改需求");
        miEditReq.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditReqActionPerformed(evt);
            }
        });
        popReqs.add(miEditReq);

        miDisableReq.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisableReq.setText("失效需求");
        miDisableReq.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisableReqActionPerformed(evt);
            }
        });
        popReqs.add(miDisableReq);

        miRevertReq.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevertReq.setText("恢复需求");
        miRevertReq.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevertReqActionPerformed(evt);
            }
        });
        popReqs.add(miRevertReq);

        miDestroyReq.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDestroyReq.setText("销毁需求");
        miDestroyReq.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDestroyReqActionPerformed(evt);
            }
        });
        popReqs.add(miDestroyReq);
        popReqs.add(jSeparator8);

        miMoveReqUp.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveReqUp.setText("上移");
        miMoveReqUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveReqUpActionPerformed(evt);
            }
        });
        popReqs.add(miMoveReqUp);

        miMoveReqDown.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miMoveReqDown.setText("下移");
        miMoveReqDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveReqDownActionPerformed(evt);
            }
        });
        popReqs.add(miMoveReqDown);

        spRecyList.setDividerLocation(288);
        spRecyList.setDividerSize(3);

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

        spRecyList.setLeftComponent(jScrollPane1);

        ckShowAll.setText("显示全部数据");
        ckShowAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAll)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ckShowAll)
                .addGap(2, 2, 2)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE))
        );

        spRecyList.setRightComponent(jPanel1);

        javax.swing.GroupLayout palRecyListLayout = new javax.swing.GroupLayout(palRecyList);
        palRecyList.setLayout(palRecyListLayout);
        palRecyListLayout.setHorizontalGroup(
            palRecyListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 868, Short.MAX_VALUE)
            .addGroup(palRecyListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(spRecyList))
        );
        palRecyListLayout.setVerticalGroup(
            palRecyListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 612, Short.MAX_VALUE)
            .addGroup(palRecyListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(spRecyList))
        );

        tabs.addTab("拆解物体列表", palRecyList);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        ckShowAllReqs.setText("显示全部");
        ckShowAllReqs.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllReqsActionPerformed(evt);
            }
        });
        jToolBar2.add(ckShowAllReqs);

        cmbReqTarType.setMaximumSize(new java.awt.Dimension(130, 32767));
        cmbReqTarType.setMinimumSize(new java.awt.Dimension(130, 28));
        cmbReqTarType.setPreferredSize(new java.awt.Dimension(130, 28));
        cmbReqTarType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbReqTarTypeActionPerformed(evt);
            }
        });
        jToolBar2.add(cmbReqTarType);

        btnNewRequest.setText("添加需求");
        btnNewRequest.setFocusable(false);
        btnNewRequest.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewRequest.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewRequest.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewRequestActionPerformed(evt);
            }
        });
        jToolBar2.add(btnNewRequest);

        btnEditReq.setText("修改需求");
        btnEditReq.setFocusable(false);
        btnEditReq.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditReq.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditReq.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditReqActionPerformed(evt);
            }
        });
        jToolBar2.add(btnEditReq);

        btnDisable.setText("失效需求");
        btnDisable.setFocusable(false);
        btnDisable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDisable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDisable.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDisableActionPerformed(evt);
            }
        });
        jToolBar2.add(btnDisable);

        btnRevert.setText("恢复需求");
        btnRevert.setFocusable(false);
        btnRevert.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRevert.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRevert.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRevertActionPerformed(evt);
            }
        });
        jToolBar2.add(btnRevert);

        btnDestroy.setText("销毁需求");
        btnDestroy.setToolTipText("");
        btnDestroy.setFocusable(false);
        btnDestroy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDestroy.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDestroy.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDestroyActionPerformed(evt);
            }
        });
        jToolBar2.add(btnDestroy);
        jToolBar2.add(jSeparator6);

        btnReqUp.setText("上移");
        btnReqUp.setFocusable(false);
        btnReqUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReqUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReqUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReqUpActionPerformed(evt);
            }
        });
        jToolBar2.add(btnReqUp);

        btnReqDown.setText("下移");
        btnReqDown.setFocusable(false);
        btnReqDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReqDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReqDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReqDownActionPerformed(evt);
            }
        });
        jToolBar2.add(btnReqDown);

        scpReqs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                scpReqsMouseReleased(evt);
            }
        });

        tbReqs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbReqs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbReqs.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                tbReqsMouseReleased(evt);
            }
        });
        scpReqs.setViewportView(tbReqs);

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

        javax.swing.GroupLayout palRecyReqsLayout = new javax.swing.GroupLayout(palRecyReqs);
        palRecyReqs.setLayout(palRecyReqsLayout);
        palRecyReqsLayout.setHorizontalGroup(
            palRecyReqsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(palRecyReqsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(scpReqs, javax.swing.GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
        );
        palRecyReqsLayout.setVerticalGroup(
            palRecyReqsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(palRecyReqsLayout.createSequentialGroup()
                .addGroup(palRecyReqsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(scpReqs, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE))
        );

        palRecyReqsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jToolBar2, jToolBar3});

        tabs.addTab("拆解需求列表", palRecyReqs);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tabs))
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
        makeTableRecy();
    }//GEN-LAST:event_trStructValueChanged

    private void ckShowAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllActionPerformed
    {//GEN-HEADEREND:event_ckShowAllActionPerformed
        makeTableRecy();
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

    private void btnReqUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReqUpActionPerformed
    {//GEN-HEADEREND:event_btnReqUpActionPerformed
        moveReqUp();
    }//GEN-LAST:event_btnReqUpActionPerformed

    private void btnPopReqMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPopReqMouseReleased
    {//GEN-HEADEREND:event_btnPopReqMouseReleased
        if (evt.getButton() == evt.BUTTON1)
        {
            popReqs.show(btnPopReq, evt.getX(), popReqs.getY() + btnPopReq.getHeight());
        }
    }//GEN-LAST:event_btnPopReqMouseReleased

    private void tbReqsMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbReqsMouseReleased
    {//GEN-HEADEREND:event_tbReqsMouseReleased
        if (evt.getButton() == evt.BUTTON3)
        {
            popReqs.show(tbReqs, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbReqsMouseReleased

    private void scpReqsMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scpReqsMouseReleased
    {//GEN-HEADEREND:event_scpReqsMouseReleased
        if (evt.getButton() == evt.BUTTON3)
        {
            popReqs.show(scpReqs, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_scpReqsMouseReleased

    private void btnNewRequestActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewRequestActionPerformed
    {//GEN-HEADEREND:event_btnNewRequestActionPerformed
        newReq();
    }//GEN-LAST:event_btnNewRequestActionPerformed

    private void ckShowAllReqsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllReqsActionPerformed
    {//GEN-HEADEREND:event_ckShowAllReqsActionPerformed
        makeTableReq();
    }//GEN-LAST:event_ckShowAllReqsActionPerformed

    private void cmbReqTarTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbReqTarTypeActionPerformed
    {//GEN-HEADEREND:event_cmbReqTarTypeActionPerformed
        makeTableReq();
    }//GEN-LAST:event_cmbReqTarTypeActionPerformed

    private void btnEditReqActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditReqActionPerformed
    {//GEN-HEADEREND:event_btnEditReqActionPerformed
        editReq();
    }//GEN-LAST:event_btnEditReqActionPerformed

    private void btnDisableActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDisableActionPerformed
    {//GEN-HEADEREND:event_btnDisableActionPerformed
        disableReq();
    }//GEN-LAST:event_btnDisableActionPerformed

    private void btnRevertActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRevertActionPerformed
    {//GEN-HEADEREND:event_btnRevertActionPerformed
        revertReq();
    }//GEN-LAST:event_btnRevertActionPerformed

    private void btnDestroyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDestroyActionPerformed
    {//GEN-HEADEREND:event_btnDestroyActionPerformed
        destroyReq();
    }//GEN-LAST:event_btnDestroyActionPerformed

    private void btnReqDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReqDownActionPerformed
    {//GEN-HEADEREND:event_btnReqDownActionPerformed
        moveReqDown();
    }//GEN-LAST:event_btnReqDownActionPerformed

    private void miNewReqActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewReqActionPerformed
    {//GEN-HEADEREND:event_miNewReqActionPerformed
        newReq();
    }//GEN-LAST:event_miNewReqActionPerformed

    private void miEditReqActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditReqActionPerformed
    {//GEN-HEADEREND:event_miEditReqActionPerformed
        editReq();
    }//GEN-LAST:event_miEditReqActionPerformed

    private void miDisableReqActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisableReqActionPerformed
    {//GEN-HEADEREND:event_miDisableReqActionPerformed
        disableReq();
    }//GEN-LAST:event_miDisableReqActionPerformed

    private void miRevertReqActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertReqActionPerformed
    {//GEN-HEADEREND:event_miRevertReqActionPerformed
        revertReq();
    }//GEN-LAST:event_miRevertReqActionPerformed

    private void miDestroyReqActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyReqActionPerformed
    {//GEN-HEADEREND:event_miDestroyReqActionPerformed
        destroyReq();
    }//GEN-LAST:event_miDestroyReqActionPerformed

    private void miMoveReqUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveReqUpActionPerformed
    {//GEN-HEADEREND:event_miMoveReqUpActionPerformed
        moveReqUp();
    }//GEN-LAST:event_miMoveReqUpActionPerformed

    private void miMoveReqDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveReqDownActionPerformed
    {//GEN-HEADEREND:event_miMoveReqDownActionPerformed
        moveReqDown();
    }//GEN-LAST:event_miMoveReqDownActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDestroy;
    private javax.swing.JButton btnDisable;
    private javax.swing.JButton btnEditReq;
    private javax.swing.JButton btnImportTemplet;
    private javax.swing.JButton btnNewRequest;
    private javax.swing.JButton btnPopReq;
    private javax.swing.JButton btnReqDown;
    private javax.swing.JButton btnReqUp;
    private javax.swing.JButton btnRevert;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveAsTemplet;
    private javax.swing.JCheckBox ckShowAll;
    private javax.swing.JCheckBox ckShowAllReqs;
    private javax.swing.JComboBox<String> cmbReqTarType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JMenuItem miDeleteCol;
    private javax.swing.JMenuItem miDeletePage;
    private javax.swing.JMenuItem miDestroyData;
    private javax.swing.JMenuItem miDestroyReq;
    private javax.swing.JMenuItem miDisData;
    private javax.swing.JMenuItem miDisableReq;
    private javax.swing.JMenuItem miEditCol;
    private javax.swing.JMenuItem miEditData;
    private javax.swing.JMenuItem miEditPage;
    private javax.swing.JMenuItem miEditReq;
    private javax.swing.JMenuItem miMoveCol;
    private javax.swing.JMenuItem miMoveColDown;
    private javax.swing.JMenuItem miMoveColToPage;
    private javax.swing.JMenuItem miMoveColUp;
    private javax.swing.JMenuItem miMoveData;
    private javax.swing.JMenuItem miMoveDataDown;
    private javax.swing.JMenuItem miMoveDataUp;
    private javax.swing.JMenuItem miMovePageDown;
    private javax.swing.JMenuItem miMovePageUp;
    private javax.swing.JMenuItem miMoveReqDown;
    private javax.swing.JMenuItem miMoveReqUp;
    private javax.swing.JMenuItem miNewCol;
    private javax.swing.JMenuItem miNewData;
    private javax.swing.JMenuItem miNewDataFromCol;
    private javax.swing.JMenuItem miNewPage;
    private javax.swing.JMenuItem miNewReq;
    private javax.swing.JMenuItem miRevertData;
    private javax.swing.JMenuItem miRevertReq;
    private javax.swing.JPanel palRecyList;
    private javax.swing.JPanel palRecyReqs;
    private javax.swing.JPopupMenu popDatas;
    private javax.swing.JPopupMenu popReqs;
    private javax.swing.JPopupMenu popStruct;
    private javax.swing.JScrollPane scpReqs;
    private javax.swing.JSplitPane spRecyList;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tbDatas;
    private javax.swing.JTable tbReqs;
    private javax.swing.JTree trStruct;
    // End of variables declaration//GEN-END:variables
    @Override
    public boolean isNeedSave()
    {
        return recyData.isNeedSave();
    }

    @Override
    public void resetSave()
    {
        recyData.resetSave();
    }

    @Override
    public void save()
    {
        doSave(false);
    }

    @Override
    public String getDatablockServiceTag()
    {
        return recyData.getDatablockServiceTag();
    }

    @Override
    public String getDatablockName()
    {
        return recyData.getDatablockName();
    }

    @Override
    public void initData(wakeup _up, long _dbOID, int _oclsID)
    {
        up = _up;
        dbIndex = _dbOID;
        oclsID = _oclsID;
        recyData = new recycleListData();
        recyData.initDatablcok(up);
        initDatablock();
        makeCombo();
        makeTree();
        makeTableRecy();
        makeTableReq();
    }

    private void initDatablock()
    {
        datablockService dbs = new datablockService(up);
        String sblock = dbs.loadData(dbIndex);
        xmlRight xr = new xmlRight();
        boolean b = xr.parseXMLfromString(sblock);
        if (b)
        {
            boolean b2 = recyData.revertFromXML(xr);
        }
        else
        {
        }
    }

    @Override
    public String getPalTitle()
    {
        return recyData.getDatablockName();
    }

    private void makeCombo()
    {
        DefaultComboBoxModel modReq = new DefaultComboBoxModel();
        modReq.addElement(new listItem("[全部]", 0));
        int[] idsReq = constChk.getFinalInts(iConst.class, "DT_REQ_TARTYPE_");
        for (int i = 0; i < idsReq.length; i++)
        {
            int id = idsReq[i];
            modReq.addElement(new listItem(iConst.translate(id), id));
        }
        cmbReqTarType.setModel(modReq);
    }
}
