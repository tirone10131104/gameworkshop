package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.MDIPaneControl;
import dev.xlin.gameworkshop.GUI.dialog.DlgItemDefineInfo;
import dev.xlin.gameworkshop.GUI.dialog.DlgItemTempletInfo;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.GUI.guiFullTreeGuiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemCluster;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemEquipStruct;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanItemTemplet;
import dev.xlin.gameworkshop.progs.foundation.itemCluster;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.itemEquipStruct;
import dev.xlin.gameworkshop.progs.foundation.itemTempletService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class IFrmItemDefineMgr extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private boolean bInitTable = false;
    private List les = null;
    private HashMap hOclsCache = new HashMap();
    private List lsts = null;

    public IFrmItemDefineMgr(wakeup _up, JDesktopPane _desk)
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
        myTreeNode mrt = guiCodes.makeFullTypeTree(up, systemType.CODE_STT_ITEMS, ckShowAllType.isSelected(), 0);
        DefaultTreeModel dtm = new DefaultTreeModel(mrt);
        trTypes.setModel(dtm);
        guiCommon.expandTree(trTypes);
    }

    private void makeTable()
    {
        hOclsCache.clear();
        myTableModel mtm = new myTableModel();
        if (bInitTable == false)
        {
            mtm.addColumn("名称");
            mtm.addColumn("标签");
            mtm.addColumn("描述");
            mtm.addColumn("物类");
            mtm.addColumn("抽象");
            mtm.addColumn("堆叠");
            mtm.addColumn("隐藏");
            mtm.addColumn("锁定");
            mtm.addColumn("系统");
            mtm.addColumn("可堆叠");
            mtm.addColumn("堆叠上限");
            mtm.addColumn("可装备");
            mtm.addColumn("装备上限");
            mtm.addColumn("装配目标");
            mtm.addColumn("装配分类");
            mtm.addColumn("装配序位");
            mtm.addColumn("状态");
        }
        else
        {
            fast.clearTableModel(tbItems);
        }
        itemDefine sed = new itemDefine(up);
        myTreeNode mtn = getSelectType();
        if (mtn == null)
        {
            tbItems.setModel(mtm);
            return;
        }
        les = sed.getItemsByType(mtn.getNodeOID(), ckShowAllEquip.isSelected());
        objectClassDefine ocd = new objectClassDefine(up);
        if (les != null)
        {
            if (lsts == null)
            {
                itemEquipStruct ies = new itemEquipStruct(up);
                lsts = ies.getAllRecord();
            }
            for (int i = 0; i < les.size(); i++)
            {
                BeanItem bse = (BeanItem) les.get(i);
                Vector v = new Vector();
                v.add(bse.getItemName());
                v.add(bse.getItemTag());
                v.add(bse.getItemDesp());
                if (hOclsCache.containsKey(bse.getOclsID()))
                {
                    BeanObjectClass bocls = (BeanObjectClass) hOclsCache.get(bse.getOclsID());
                    v.add(bocls.getClassName());
                }
                else
                {
                    BeanObjectClass bocls = (BeanObjectClass) ocd.getRecordByID(bse.getOclsID());
                    if (bocls != null)
                    {
                        v.add(bocls.getClassName());
                        hOclsCache.put(bocls.getOID(), bocls);
                    }
                    else
                    {
                        v.add("?");
                    }
                }
                v.add(iConst.translateIBOL(bse.getAbstractItem()));
                v.add(iConst.translateIBOL(bse.getStack()));
                v.add(iConst.translateIBOL(bse.getHide()));
                v.add(iConst.translateIBOL(bse.getLocked()));
                v.add(iConst.translateIBOL(bse.getSystemItem()));
                v.add(iConst.translateIBOL(bse.getStack()));
                v.add(bse.getStackLimit());
                v.add(iConst.translateIBOL(bse.getEquipment()));
                v.add(bse.getEquipLimit());
                v.add(makeEquipStructNodeName(bse.getSlotRoot()));
                v.add(makeEquipStructNodeName(bse.getSlotType()));
                v.add(makeEquipStructNodeName(bse.getSlotIndex()));
                v.add(iConst.transDAOState(bse.getState()));
                mtm.addRow(v);
            }
        }
        tbItems.setModel(mtm);
    }

    private String makeEquipStructNodeName(int id)
    {
        if (id == 0)
        {
            return "[通用]";
        }
        else
        {
            BeanItemEquipStruct bies = findItemEquipStruct(id);
            if (bies == null)
            {
                return "[???]";
            }
            else
            {
                return bies.getEquipName();
            }
        }
    }

    private BeanItemEquipStruct findItemEquipStruct(int id)
    {
        if (lsts == null)
        {
            return null;
        }
        for (int i = 0; i < lsts.size(); i++)
        {
            BeanItemEquipStruct bean = (BeanItemEquipStruct) lsts.get(i);
            if (bean.getOID() == id)
            {
                return bean;
            }
        }
        return null;
    }

    private myTreeNode getSelectType()
    {
        TreePath tph = trTypes.getSelectionPath();
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

    private BeanItem getSelectItem()
    {
        int idx = tbItems.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        else
        {
            return (BeanItem) les.get(idx);
        }
    }

    private void doNewType()
    {
        int r = guiFullTreeGuiCodes.doNewType(trTypes, up, systemType.CODE_STT_ITEMS);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("创建分类失败", r);
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
        itemDefine sed = new itemDefine(up);
        if (sed.getItemsByType(oid, true) != null)
        {
            fast.warn("不能失效这个分类，因为其下还有物体。");
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

    private void doNewEquip()
    {
        myTreeNode msel = getSelectType();
        if (msel == null)
        {
            return;
        }
        DlgItemDefineInfo dlg = new DlgItemDefineInfo(null, true, up, null, msel.getNodeOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doEditEquip()
    {
        BeanItem bean = getSelectItem();
        if (bean == null)
        {
            return;
        }
        DlgItemDefineInfo dlg = new DlgItemDefineInfo(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDisEquip()
    {
        BeanItem bean = getSelectItem();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认失效[" + bean.getItemName() + "]这个数据项？");
        if (sel != fast.YES)
        {
            return;
        }
        itemDefine sed = new itemDefine(up);
        int r = sed.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doRevEquip()
    {
        BeanItem bean = getSelectItem();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认恢复[" + bean.getItemName() + "]这个数据项？");
        if (sel != fast.YES)
        {
            return;
        }
        itemDefine sed = new itemDefine(up);
        int r = sed.revertBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    private void doDestroyEquip()
    {
        BeanItem bean = getSelectItem();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认销毁[" + bean.getItemName() + "]这个数据项？\n销毁以后，数据无法恢复");
        if (sel != fast.YES)
        {
            return;
        }
        itemDefine sed = new itemDefine(up);
        int r = sed.destroyBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTable();
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void doEditData()
    {
        BeanItem bean = getSelectItem();
        if (bean == null)
        {
            return;
        }
        if (MDIPaneControl.isMDIFrameOpened(MDIPaneControl.IFRM_ITEM_DEFINE_DATA, bean.getOID() + ""))
        {
            fast.warn("不可重复开启");
            return;
        }
        IFrmItemDefineDataEditor ifde = new IFrmItemDefineDataEditor(up, bean, desk, this);
        desk.add(ifde);
        ifde.setVisible(true);
    }

    protected void postItemInfoUpdated()
    {
        makeTable();
    }

    private void doSaveAsTemplet()
    {
        BeanItem bean = getSelectItem();
        if (bean == null)
        {
            return;
        }
        DlgItemTempletInfo dlg = new DlgItemTempletInfo(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            BeanItemTemplet bit = dlg.getItemTemplet();
            itemTempletService its = new itemTempletService(up);
            int r = its.saveAsTemplet(bean.getOID(), bit.getTempName(), bit.getTempDesp(), dlg.getClearValue());
            if (r == iDAO.OPERATE_SUCCESS)
            {
                fast.msg("物体存储为模板操作成功");
            }
            else
            {
                fast.err("物体存储为模板操作失败", r);
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void doMoveToType()
    {
        BeanItem bean = getSelectItem();
        if (bean == null)
        {
            return;
        }
        myTreeNode mtn = guiCodes.makeFullTypeTree(up, systemType.CODE_STT_ITEMS, ckShowAllType.isSelected(), 0);
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mtn);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            myTreeNode msel = dlg.getSelect();
            if (msel != null)
            {
                itemDefine idef = new itemDefine(up);
                int r = idef.moveToType(bean.getOID(), msel.getNodeOID());
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

    private void doMoveType()
    {
        int r = guiFullTreeGuiCodes.doMoveType(trTypes, up, systemType.CODE_STT_ITEMS);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
        }
        else
        {
            fast.err("移动失败", r);
        }
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
        miMoveType = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miNewItem = new javax.swing.JMenuItem();
        popItem = new javax.swing.JPopupMenu();
        miNewItem2 = new javax.swing.JMenuItem();
        miEditItemData = new javax.swing.JMenuItem();
        miEditItem = new javax.swing.JMenuItem();
        miDisItem = new javax.swing.JMenuItem();
        miRevItem = new javax.swing.JMenuItem();
        miDesItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        miMoveItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        miNewFromThis = new javax.swing.JMenuItem();
        jToolBar1 = new javax.swing.JToolBar();
        btnNewType = new javax.swing.JButton();
        btnEditType = new javax.swing.JButton();
        btnDisType = new javax.swing.JButton();
        btnRevType = new javax.swing.JButton();
        btnDestroyType = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnNewEquip = new javax.swing.JButton();
        btnEditEquip = new javax.swing.JButton();
        btnEditData = new javax.swing.JButton();
        btnMoveEquip = new javax.swing.JButton();
        btnDisEquip = new javax.swing.JButton();
        btnRevEquip = new javax.swing.JButton();
        btnDestroyEquip = new javax.swing.JButton();
        btnSaveAsTemplet = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        ckShowAllType = new javax.swing.JCheckBox();
        ckShowAllEquip = new javax.swing.JCheckBox();
        jSplitPane1 = new javax.swing.JSplitPane();
        scpItems = new javax.swing.JScrollPane();
        tbItems = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        trTypes = new javax.swing.JTree();

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

        miRemoveType.setText("删除分类");
        miRemoveType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRemoveTypeActionPerformed(evt);
            }
        });
        popType.add(miRemoveType);

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

        miMoveType.setText("移动分类");
        miMoveType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveTypeActionPerformed(evt);
            }
        });
        popType.add(miMoveType);
        popType.add(jSeparator2);

        miNewItem.setText("新建物体");
        miNewItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewItemActionPerformed(evt);
            }
        });
        popType.add(miNewItem);

        miNewItem2.setText("新建物体");
        miNewItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewItem2ActionPerformed(evt);
            }
        });
        popItem.add(miNewItem2);

        miEditItemData.setText("编辑物体数据");
        miEditItemData.setToolTipText("");
        miEditItemData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditItemDataActionPerformed(evt);
            }
        });
        popItem.add(miEditItemData);

        miEditItem.setText("修改物体");
        miEditItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditItemActionPerformed(evt);
            }
        });
        popItem.add(miEditItem);

        miDisItem.setText("失效物体");
        miDisItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisItemActionPerformed(evt);
            }
        });
        popItem.add(miDisItem);

        miRevItem.setText("恢复物体");
        miRevItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevItemActionPerformed(evt);
            }
        });
        popItem.add(miRevItem);

        miDesItem.setText("销毁物体");
        miDesItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDesItemActionPerformed(evt);
            }
        });
        popItem.add(miDesItem);
        popItem.add(jSeparator3);

        miMoveItem.setText("移动至分类");
        miMoveItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveItemActionPerformed(evt);
            }
        });
        popItem.add(miMoveItem);
        popItem.add(jSeparator4);

        miNewFromThis.setText("以此为模板创建物体");
        popItem.add(miNewFromThis);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("ITEMS MGR");
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

        btnNewType.setText("创建分类");
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

        btnRevType.setText("恢复分类");
        btnRevType.setFocusable(false);
        btnRevType.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRevType.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRevType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRevTypeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRevType);

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

        btnNewEquip.setText("新建物体");
        btnNewEquip.setFocusable(false);
        btnNewEquip.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewEquip.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewEquip.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewEquipActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNewEquip);

        btnEditEquip.setText("修改物体");
        btnEditEquip.setFocusable(false);
        btnEditEquip.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditEquip.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditEquip.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditEquipActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditEquip);

        btnEditData.setText("编辑数据");
        btnEditData.setFocusable(false);
        btnEditData.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditData.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditDataActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditData);

        btnMoveEquip.setText("移动物体");
        btnMoveEquip.setFocusable(false);
        btnMoveEquip.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMoveEquip.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMoveEquip.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMoveEquipActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMoveEquip);

        btnDisEquip.setText("失效物体");
        btnDisEquip.setFocusable(false);
        btnDisEquip.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDisEquip.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDisEquip.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDisEquipActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDisEquip);

        btnRevEquip.setText("恢复物体");
        btnRevEquip.setFocusable(false);
        btnRevEquip.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRevEquip.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRevEquip.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRevEquipActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRevEquip);

        btnDestroyEquip.setText("销毁物体");
        btnDestroyEquip.setFocusable(false);
        btnDestroyEquip.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDestroyEquip.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDestroyEquip.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDestroyEquipActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDestroyEquip);

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

        ckShowAllType.setText("显示全部分类");
        ckShowAllType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllTypeActionPerformed(evt);
            }
        });

        ckShowAllEquip.setText("显示全部物体");
        ckShowAllEquip.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckShowAllEquipActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ckShowAllType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckShowAllEquip)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ckShowAllType)
                .addComponent(ckShowAllEquip))
        );

        jSplitPane1.setDividerLocation(288);
        jSplitPane1.setDividerSize(3);

        scpItems.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scpItemsMouseClicked(evt);
            }
        });

        tbItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tbItems.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbItems.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbItemsMouseClicked(evt);
            }
        });
        scpItems.setViewportView(tbItems);

        jSplitPane1.setRightComponent(scpItems);

        trTypes.setForeground(new java.awt.Color(255, 255, 255));
        trTypes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                trTypesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(trTypes);

        jSplitPane1.setLeftComponent(jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1350, Short.MAX_VALUE)
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
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewEquipActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewEquipActionPerformed
    {//GEN-HEADEREND:event_btnNewEquipActionPerformed
        doNewEquip();
    }//GEN-LAST:event_btnNewEquipActionPerformed

    private void btnNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewTypeActionPerformed
    {//GEN-HEADEREND:event_btnNewTypeActionPerformed
        doNewType();
    }//GEN-LAST:event_btnNewTypeActionPerformed

    private void btnEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditTypeActionPerformed
    {//GEN-HEADEREND:event_btnEditTypeActionPerformed
        doEditType();
    }//GEN-LAST:event_btnEditTypeActionPerformed

    private void btnDisTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDisTypeActionPerformed
    {//GEN-HEADEREND:event_btnDisTypeActionPerformed
        doDisType();
    }//GEN-LAST:event_btnDisTypeActionPerformed

    private void btnRevTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRevTypeActionPerformed
    {//GEN-HEADEREND:event_btnRevTypeActionPerformed
        doRevertType();
    }//GEN-LAST:event_btnRevTypeActionPerformed

    private void btnDestroyTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDestroyTypeActionPerformed
    {//GEN-HEADEREND:event_btnDestroyTypeActionPerformed
        doDestroyType();
    }//GEN-LAST:event_btnDestroyTypeActionPerformed

    private void ckShowAllTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllTypeActionPerformed
    {//GEN-HEADEREND:event_ckShowAllTypeActionPerformed
        makeTree();
    }//GEN-LAST:event_ckShowAllTypeActionPerformed

    private void ckShowAllEquipActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckShowAllEquipActionPerformed
    {//GEN-HEADEREND:event_ckShowAllEquipActionPerformed
        makeTable();
    }//GEN-LAST:event_ckShowAllEquipActionPerformed

    private void btnEditDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditDataActionPerformed
    {//GEN-HEADEREND:event_btnEditDataActionPerformed
        doEditData();
    }//GEN-LAST:event_btnEditDataActionPerformed

    private void trTypesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_trTypesMouseClicked
    {//GEN-HEADEREND:event_trTypesMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popType.show(trTypes, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            doEditType();
        }
        else
        {
            makeTable();
        }
    }//GEN-LAST:event_trTypesMouseClicked

    private void btnEditEquipActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditEquipActionPerformed
    {//GEN-HEADEREND:event_btnEditEquipActionPerformed
        doEditEquip();
    }//GEN-LAST:event_btnEditEquipActionPerformed

    private void btnMoveEquipActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMoveEquipActionPerformed
    {//GEN-HEADEREND:event_btnMoveEquipActionPerformed
        doMoveToType();
    }//GEN-LAST:event_btnMoveEquipActionPerformed

    private void btnDisEquipActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDisEquipActionPerformed
    {//GEN-HEADEREND:event_btnDisEquipActionPerformed
        doDisEquip();
    }//GEN-LAST:event_btnDisEquipActionPerformed

    private void btnRevEquipActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRevEquipActionPerformed
    {//GEN-HEADEREND:event_btnRevEquipActionPerformed
        doRevEquip();
    }//GEN-LAST:event_btnRevEquipActionPerformed

    private void btnDestroyEquipActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDestroyEquipActionPerformed
    {//GEN-HEADEREND:event_btnDestroyEquipActionPerformed
        doDestroyEquip();
    }//GEN-LAST:event_btnDestroyEquipActionPerformed

    private void btnSaveAsTempletActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveAsTempletActionPerformed
    {//GEN-HEADEREND:event_btnSaveAsTempletActionPerformed
        doSaveAsTemplet();
    }//GEN-LAST:event_btnSaveAsTempletActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsItemDefineMgr(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void miNewTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewTypeActionPerformed
    {//GEN-HEADEREND:event_miNewTypeActionPerformed
        doNewType();
    }//GEN-LAST:event_miNewTypeActionPerformed

    private void miEditTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditTypeActionPerformed
    {//GEN-HEADEREND:event_miEditTypeActionPerformed
        doEditType();
    }//GEN-LAST:event_miEditTypeActionPerformed

    private void miRemoveTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveTypeActionPerformed
    {//GEN-HEADEREND:event_miRemoveTypeActionPerformed
        doDisType();
    }//GEN-LAST:event_miRemoveTypeActionPerformed

    private void miRevertTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevertTypeActionPerformed
    {//GEN-HEADEREND:event_miRevertTypeActionPerformed
        doRevertType();
    }//GEN-LAST:event_miRevertTypeActionPerformed

    private void miDestroyTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDestroyTypeActionPerformed
    {//GEN-HEADEREND:event_miDestroyTypeActionPerformed
        doDestroyType();
    }//GEN-LAST:event_miDestroyTypeActionPerformed

    private void miNewItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewItemActionPerformed
    {//GEN-HEADEREND:event_miNewItemActionPerformed
        doNewEquip();
    }//GEN-LAST:event_miNewItemActionPerformed

    private void miEditItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditItemActionPerformed
    {//GEN-HEADEREND:event_miEditItemActionPerformed
        doEditEquip();
    }//GEN-LAST:event_miEditItemActionPerformed

    private void miDisItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisItemActionPerformed
    {//GEN-HEADEREND:event_miDisItemActionPerformed
        doDisEquip();
    }//GEN-LAST:event_miDisItemActionPerformed

    private void miRevItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevItemActionPerformed
    {//GEN-HEADEREND:event_miRevItemActionPerformed
        doRevEquip();
    }//GEN-LAST:event_miRevItemActionPerformed

    private void miDesItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDesItemActionPerformed
    {//GEN-HEADEREND:event_miDesItemActionPerformed
        doDestroyEquip();
    }//GEN-LAST:event_miDesItemActionPerformed

    private void miMoveItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveItemActionPerformed
    {//GEN-HEADEREND:event_miMoveItemActionPerformed
        doMoveToType();
    }//GEN-LAST:event_miMoveItemActionPerformed

    private void miNewItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewItem2ActionPerformed
    {//GEN-HEADEREND:event_miNewItem2ActionPerformed
        doNewEquip();
    }//GEN-LAST:event_miNewItem2ActionPerformed

    private void miEditItemDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditItemDataActionPerformed
    {//GEN-HEADEREND:event_miEditItemDataActionPerformed
        doEditData();
    }//GEN-LAST:event_miEditItemDataActionPerformed

    private void tbItemsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbItemsMouseClicked
    {//GEN-HEADEREND:event_tbItemsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popItem.show(tbItems, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            doEditData();
        }
    }//GEN-LAST:event_tbItemsMouseClicked

    private void miMoveTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveTypeActionPerformed
    {//GEN-HEADEREND:event_miMoveTypeActionPerformed
        doMoveType();
    }//GEN-LAST:event_miMoveTypeActionPerformed

    private void scpItemsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scpItemsMouseClicked
    {//GEN-HEADEREND:event_scpItemsMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popItem.show(scpItems, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_scpItemsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDestroyEquip;
    private javax.swing.JButton btnDestroyType;
    private javax.swing.JButton btnDisEquip;
    private javax.swing.JButton btnDisType;
    private javax.swing.JButton btnEditData;
    private javax.swing.JButton btnEditEquip;
    private javax.swing.JButton btnEditType;
    private javax.swing.JButton btnMoveEquip;
    private javax.swing.JButton btnNewEquip;
    private javax.swing.JButton btnNewType;
    private javax.swing.JButton btnRevEquip;
    private javax.swing.JButton btnRevType;
    private javax.swing.JButton btnSaveAsTemplet;
    private javax.swing.JCheckBox ckShowAllEquip;
    private javax.swing.JCheckBox ckShowAllType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem miDesItem;
    private javax.swing.JMenuItem miDestroyType;
    private javax.swing.JMenuItem miDisItem;
    private javax.swing.JMenuItem miEditItem;
    private javax.swing.JMenuItem miEditItemData;
    private javax.swing.JMenuItem miEditType;
    private javax.swing.JMenuItem miMoveItem;
    private javax.swing.JMenuItem miMoveType;
    private javax.swing.JMenuItem miNewFromThis;
    private javax.swing.JMenuItem miNewItem;
    private javax.swing.JMenuItem miNewItem2;
    private javax.swing.JMenuItem miNewType;
    private javax.swing.JMenuItem miRemoveType;
    private javax.swing.JMenuItem miRevItem;
    private javax.swing.JMenuItem miRevertType;
    private javax.swing.JPopupMenu popItem;
    private javax.swing.JPopupMenu popType;
    private javax.swing.JScrollPane scpItems;
    private javax.swing.JTable tbItems;
    private javax.swing.JTree trTypes;
    // End of variables declaration//GEN-END:variables
}
