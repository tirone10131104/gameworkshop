package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItem;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.swingTools2.dlgTools.dlgSelectTreeNode;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class dlgItemDefineInfo extends javax.swing.JDialog
{

    private wakeup up = null;
    private session sn = null;
    private boolean bOK = false;
    private boolean bEdit = false;
    private beanItem bean = null;
    private int tpid = 0;
    private beanObjectClass BOC = null;
    private boolean bInit = false;

    public dlgItemDefineInfo(java.awt.Frame parent, boolean modal, wakeup _up, beanItem _bean, int _tpid)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        tpid = _tpid;
        initGUI();
        bInit = true;
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        makeEquipTypeCombo();
        makeEquipRootCombo();
        makeSlotTypeCombo();
        makeSlotIndexCombo();
        System.err.println(".bean = " + bean );
        if (bean == null)
        {
            bean = new beanItem();
            bEdit = false;
            doAbstractCtrl();
            geneTag();
        }
        else
        {
            txtDesp.setText(bean.getItemDesp());
            txtName.setText(bean.getItemName());
            txtTag.setText(bean.getItemTag());
            txtTag.setEnabled(false);
            btnGeneTag.setEnabled(false);
            btnCheckTag.setEnabled(false);
            btnSelObjCls.setEnabled(false);
            objectClassDefine ocd = new objectClassDefine(up);
            BOC = (beanObjectClass) ocd.getRecordByID(bean.getOclsID());
            txtObjCls.setText(BOC.getClassName() + "<" + BOC.getClassTag() + ">");
            //额外内容物体配置信息的控件展示
            initConfigCtrls();
            tryLockConfigCtrls();
            bEdit = true;
        }
    }

    private void initConfigCtrls()
    {
        fast.setCheckBoxValue(ckHide, bean.getHide());
        fast.setCheckBoxValue(ckLock, bean.getLocked());
        txtStackLimit.setText(bean.getStackLimit() + "");
        swsys.doSelectCombo(cmbSlotRoot, bean.getSlotRoot());
        makeSlotTypeCombo();
        swsys.doSelectCombo(cmbSlotType, bean.getSlotType());
        makeSlotIndexCombo();
        swsys.doSelectCombo(cmbSlotIndex, bean.getSlotIndex());
        txtCap.setText(bean.getCapUse() + "");
//        bEdit = true;
        fast.setCheckBoxValue(ckAbstract, bean.getAbstractItem());
        if (ckAbstract.isSelected())
        {
            doAbstractCtrl();
        }
        fast.setCheckBoxValue(ckStack, bean.getStack());
        if (ckStack.isSelected())
        {
            doStackLimitCtrl();
        }
        fast.setCheckBoxValue(ckEquipment, bean.getEquipment());
        if (ckEquipment.isSelected())
        {
            doEquipmentCtrls();
        }
        fast.setCheckBoxValue(ckContainerItem, bean.getContainerItem());
        if (ckContainerItem.isSelected())
        {
            doContainerCtrl();
        }
    }

    private void makeEquipRootCombo()
    {
        DefaultComboBoxModel modEQTP = guiCodes.makeItemEquipTypeModel(up);
        cmbEquipRoot.setModel(modEQTP);
    }

    private void makeEquipTypeCombo()
    {
        DefaultComboBoxModel modEQTP = guiCodes.makeItemEquipTypeModel(up);
        cmbSlotRoot.setModel(modEQTP);
    }

    private void makeSlotTypeCombo()
    {
        int ietp = swsys.getComboBoxSelected(cmbSlotRoot);
        DefaultComboBoxModel modSLTP = guiCodes.makeItemEquipChildModel(up, ietp);
        cmbSlotType.setModel(modSLTP);
    }

    private void makeSlotIndexCombo()
    {
        int isltp = swsys.getComboBoxSelected(cmbSlotType);
        DefaultComboBoxModel modSLIX = guiCodes.makeItemEquipChildModel(up, isltp);
        cmbSlotIndex.setModel(modSLIX);
    }

    private void doOK()
    {
        bean.setItemDesp(txtDesp.getText().trim());
        bean.setItemName(txtName.getText().trim());
        bean.setHide(fast.readCheckBox(ckHide));
        bean.setAbstractItem(fast.readCheckBox(ckAbstract));
        bean.setStack(fast.readCheckBox(ckStack));
        bean.setLocked(fast.readCheckBox(ckLock));
        bean.setStackLimit(fast.testIntegerText(txtStackLimit));
        bean.setSlotRoot(swsys.getComboBoxSelected(cmbSlotRoot));
        bean.setSlotType(swsys.getComboBoxSelected(cmbSlotType));
        bean.setSlotIndex(swsys.getComboBoxSelected(cmbSlotIndex));
        bean.setEquipment(fast.readCheckBox(ckEquipment));
        bean.setEquipLimit(fast.testIntegerText(txtEquipLimt));
        bean.setContainerItem(fast.readCheckBox(ckContainerItem));
        bean.setEquipRoot(swsys.getComboBoxSelected(cmbEquipRoot));
        bean.setCapUse(fast.testDoubleText(txtCap));
        int r = 0;
        itemDefine sed = new itemDefine(up);
        System.err.println("..bedit = "  + bEdit);
        if (bEdit)
        {
            r = sed.modifyRecord(bean, false);
        }
        else
        {
            bean.setItemTag(txtTag.getText().trim());
            if (BOC == null)
            {
                fast.warn("必须选择一个物类");
                return;
            }
            bean.setOclsID(BOC.getOID());
            bean.setTpid(tpid);
            r = sed.createRecord(bean, false);
        }
        if (r == iDAO.OPERATE_SUCCESS)
        {
            bOK = true;
            setVisible(false);
        }
        else
        {
            fast.err("装备信息操作失败", r);
        }
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    private void doSelectObjCls()
    {
        myTreeNode mrt = new myTreeNode("物类数据库", 0, 0);
        sttType stp = new sttType(up);
        List lstp = stp.getRootsByDef(systemType.CODE_STT_OBJ_CLS, false);
        if (lstp == null)
        {
            fast.warn("当前系统中还没有定义物类。");
            return;
        }
        objectClassDefine ocd = new objectClassDefine(up);
        for (int i = 0; i < lstp.size(); i++)
        {
            beanSttType bst = (beanSttType) lstp.get(i);
            myTreeNode mst = new myTreeNode("[分类]" + bst.getTypeName(), 0, 0);
            List lcs = ocd.getObjectClassByType(bst.getOID(), false);
            if (lcs != null)
            {
                for (int j = 0; j < lcs.size(); j++)
                {
                    beanObjectClass boc = (beanObjectClass) lcs.get(j);
                    myTreeNode moc = new myTreeNode(boc.getClassName() + "<" + boc.getClassTag() + ">", boc.getOID(), 1);
                    mst.add(moc);
                }
                mrt.add(mst);
            }
        }
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mrt);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            myTreeNode msel = dlg.getSelect();
            if (msel != null)
            {
                if (msel.getNodeOID() != 0)
                {
                    BOC = (beanObjectClass) ocd.getRecordByID(msel.getNodeOID());
                    txtObjCls.setText((String) msel.toString());
                    //物类的配置数据导入
                    bean.setAbstractItem(BOC.getAbstractItem());
                    bean.setContainerItem(BOC.getContainerItem());
                    bean.setEquipRoot(BOC.getEquipRoot());
                    bean.setSlotRoot(BOC.getSlotRoot());
                    bean.setEquipment(BOC.getEquipment());
                    bean.setSlotType(BOC.getSlotType());
                    bean.setSlotIndex(BOC.getSlotIndex());
                    bean.setStack(BOC.getStack());
                    bean.setStackLimit(BOC.getStackLimit());
                    //导入以后，进行展示
                    initConfigCtrls();
                    tryLockConfigCtrls();
                }
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void tryLockConfigCtrls()
    {
        if (BOC == null)
        {
            return;
        }
        if (BOC.getStrictConfig() == iConst.BOL_TRUE)
        {
            ckAbstract.setEnabled(false);
            ckContainerItem.setEnabled(false);
            ckStack.setEnabled(false);
            txtStackLimit.setEnabled(false);
            cmbStackNum.setEnabled(false);
            ckEquipment.setEnabled(false);
            cmbEquipRoot.setEnabled(false);
            cmbSlotType.setEnabled(false);
            cmbSlotIndex.setEnabled(false);
            cmbEquipRoot.setEnabled(false);
            cmbStackNum.setEnabled(false);
        }
    }

    public boolean getOK()
    {
        return bOK;
    }

    private void doEquipmentCtrls()
    {
        boolean b = ckEquipment.isSelected();
        txtEquipLimt.setEnabled(b);
        cmbEqupLimit.setEnabled(b);
        cmbSlotIndex.setEnabled(b);
        cmbSlotType.setEnabled(b);
        cmbSlotRoot.setEnabled(b);
        txtEquipLimt.setText("0");
        swsys.doSelectCombo(cmbSlotRoot, 0);
    }

    private void doStackLimitCtrl()
    {
        boolean b = ckStack.isSelected();
        txtStackLimit.setEnabled(b);
        cmbStackNum.setEnabled(b);
        if (b == false)
        {
            txtStackLimit.setText("1");
            boolean abcb = !ckAbstract.isSelected();
            ckEquipment.setSelected(false);
            ckContainerItem.setSelected(false);
            doEquipmentCtrls();
            doContainerCtrl();
            ckEquipment.setEnabled(abcb);
            ckContainerItem.setEnabled(abcb);
        }
        else
        {
            ckEquipment.setSelected(false);
            ckContainerItem.setSelected(false);
            doEquipmentCtrls();
            doContainerCtrl();
            ckEquipment.setEnabled(false);
            ckContainerItem.setEnabled(false);
        }
    }

    private void doContainerCtrl()
    {
        boolean b = ckContainerItem.isSelected();
        cmbEquipRoot.setEnabled(b);
        if (b == false)
        {
            swsys.doSelectCombo(cmbEquipRoot, 0);
        }
    }

    private void doAbstractCtrl()
    {
        boolean b = ckAbstract.isSelected();
        ckStack.setSelected(!b);
        ckStack.setEnabled(!b);
        doStackLimitCtrl();
    }

    private void geneTag()
    {
        String stg = tagCreator.createDataTag(up, "tbc_world_type_main", "wmTag", "ITEM", "", 3, 4);
        txtTag.setText(stg);
    }

    private boolean checkTag(boolean rpsc)
    {
        String stg = txtTag.getText().trim();
        if (stg.equals(""))
        {
            fast.warn("标签不可为空");
            return false;
        }
        itemDefine idef = new itemDefine(up);
        if (idef.getItemDefineByTag(stg) != null)
        {
            fast.warn("标签被占用");
            return false;
        }
        if (rpsc)
        {
            fast.msg("标签可以使用");
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel3 = new javax.swing.JLabel();
        txtTag = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        btnCancle = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtObjCls = new javax.swing.JTextField();
        btnSelObjCls = new javax.swing.JButton();
        ckHide = new javax.swing.JCheckBox();
        ckLock = new javax.swing.JCheckBox();
        ckStack = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        txtStackLimit = new javax.swing.JTextField();
        ckEquipment = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        cmbSlotType = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cmbSlotIndex = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtEquipLimt = new javax.swing.JTextField();
        cmbEqupLimit = new javax.swing.JComboBox<>();
        cmbStackNum = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDesp = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        cmbSlotRoot = new javax.swing.JComboBox<>();
        ckAbstract = new javax.swing.JCheckBox();
        ckContainerItem = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        cmbEquipRoot = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        txtCap = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        btnGeneTag = new javax.swing.JButton();
        btnCheckTag = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("物体基本信息设置");
        setResizable(false);

        jLabel3.setText("物体描述");

        jLabel2.setText("物体标签");

        jLabel1.setText("物体名称");

        btnOK.setText("确定");
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOKActionPerformed(evt);
            }
        });

        btnCancle.setText("取消");
        btnCancle.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancleActionPerformed(evt);
            }
        });

        jLabel4.setText("选择物类");

        txtObjCls.setEditable(false);

        btnSelObjCls.setText("选择");
        btnSelObjCls.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelObjClsActionPerformed(evt);
            }
        });

        ckHide.setText("隐藏物体");

        ckLock.setText("设为锁定");

        ckStack.setText("可堆叠");
        ckStack.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckStackActionPerformed(evt);
            }
        });

        jLabel5.setText("堆叠最大数量");

        txtStackLimit.setText("1");

        ckEquipment.setText("可装备");
        ckEquipment.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckEquipmentActionPerformed(evt);
            }
        });

        jLabel6.setText("装配类型");

        cmbSlotType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSlotType.setEnabled(false);
        cmbSlotType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSlotTypeActionPerformed(evt);
            }
        });

        jLabel7.setText("装配序位");

        cmbSlotIndex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSlotIndex.setEnabled(false);

        jLabel8.setText("装配数量限制");

        txtEquipLimt.setText("0");
        txtEquipLimt.setEnabled(false);

        cmbEqupLimit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        cmbEqupLimit.setEnabled(false);
        cmbEqupLimit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbEqupLimitActionPerformed(evt);
            }
        });

        cmbStackNum.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "255", "999", "1000", "9999", "10000", "99999", "100000", "999999", " " }));
        cmbStackNum.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbStackNumActionPerformed(evt);
            }
        });

        txtDesp.setColumns(20);
        txtDesp.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        txtDesp.setLineWrap(true);
        txtDesp.setRows(5);
        txtDesp.setBorder(null);
        jScrollPane1.setViewportView(txtDesp);

        jLabel9.setText("装配目标");

        cmbSlotRoot.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSlotRoot.setEnabled(false);
        cmbSlotRoot.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSlotRootActionPerformed(evt);
            }
        });

        ckAbstract.setSelected(true);
        ckAbstract.setText("抽象物体");
        ckAbstract.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckAbstractActionPerformed(evt);
            }
        });

        ckContainerItem.setText("是装备容器");
        ckContainerItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckContainerItemActionPerformed(evt);
            }
        });

        jLabel10.setText("装配模板");

        cmbEquipRoot.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel11.setText("容量单位");

        txtCap.setText("0");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnGeneTag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dev/xlin/gameworkshop/GUI/res/gener.png"))); // NOI18N
        btnGeneTag.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnGeneTagActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGeneTag);

        btnCheckTag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dev/xlin/gameworkshop/GUI/res/prot.png"))); // NOI18N
        btnCheckTag.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCheckTagActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCheckTag);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTag)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbEquipRoot, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(ckStack)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtStackLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(ckEquipment)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEquipLimt))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel9))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbSlotRoot, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cmbSlotType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cmbSlotIndex, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(ckContainerItem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCap)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbStackNum, 0, 165, Short.MAX_VALUE)
                                    .addComponent(cmbEqupLimit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(ckAbstract)
                                .addGap(0, 327, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtObjCls)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ckHide)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ckLock)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelObjCls)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtObjCls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelObjCls))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckHide)
                    .addComponent(ckLock))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckAbstract)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckStack)
                    .addComponent(jLabel5)
                    .addComponent(txtStackLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbStackNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckEquipment)
                    .addComponent(jLabel8)
                    .addComponent(txtEquipLimt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbEqupLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cmbSlotRoot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbSlotType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbSlotIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckContainerItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cmbEquipRoot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        doOK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        doCancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void btnSelObjClsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelObjClsActionPerformed
    {//GEN-HEADEREND:event_btnSelObjClsActionPerformed
        doSelectObjCls();
    }//GEN-LAST:event_btnSelObjClsActionPerformed

    private void cmbSlotRootActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSlotRootActionPerformed
    {//GEN-HEADEREND:event_cmbSlotRootActionPerformed
        if (bInit)
        {
            makeSlotTypeCombo();
            makeSlotIndexCombo();
        }
    }//GEN-LAST:event_cmbSlotRootActionPerformed

    private void cmbSlotTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSlotTypeActionPerformed
    {//GEN-HEADEREND:event_cmbSlotTypeActionPerformed
        if (bInit)
        {
            makeSlotIndexCombo();
        }
    }//GEN-LAST:event_cmbSlotTypeActionPerformed

    private void ckEquipmentActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckEquipmentActionPerformed
    {//GEN-HEADEREND:event_ckEquipmentActionPerformed
        doEquipmentCtrls();
    }//GEN-LAST:event_ckEquipmentActionPerformed

    private void cmbStackNumActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbStackNumActionPerformed
    {//GEN-HEADEREND:event_cmbStackNumActionPerformed
        txtStackLimit.setText(cmbStackNum.getSelectedItem() + "");
    }//GEN-LAST:event_cmbStackNumActionPerformed

    private void cmbEqupLimitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbEqupLimitActionPerformed
    {//GEN-HEADEREND:event_cmbEqupLimitActionPerformed
        txtEquipLimt.setText(cmbEqupLimit.getSelectedItem() + "");
    }//GEN-LAST:event_cmbEqupLimitActionPerformed

    private void ckStackActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckStackActionPerformed
    {//GEN-HEADEREND:event_ckStackActionPerformed
        doStackLimitCtrl();
    }//GEN-LAST:event_ckStackActionPerformed

    private void ckAbstractActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckAbstractActionPerformed
    {//GEN-HEADEREND:event_ckAbstractActionPerformed
        doAbstractCtrl();
    }//GEN-LAST:event_ckAbstractActionPerformed

    private void ckContainerItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckContainerItemActionPerformed
    {//GEN-HEADEREND:event_ckContainerItemActionPerformed
        doContainerCtrl();
    }//GEN-LAST:event_ckContainerItemActionPerformed

    private void btnGeneTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnGeneTagActionPerformed
    {//GEN-HEADEREND:event_btnGeneTagActionPerformed
        geneTag();
    }//GEN-LAST:event_btnGeneTagActionPerformed

    private void btnCheckTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCheckTagActionPerformed
    {//GEN-HEADEREND:event_btnCheckTagActionPerformed
        checkTag(true);
    }//GEN-LAST:event_btnCheckTagActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnCheckTag;
    private javax.swing.JButton btnGeneTag;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSelObjCls;
    private javax.swing.JCheckBox ckAbstract;
    private javax.swing.JCheckBox ckContainerItem;
    private javax.swing.JCheckBox ckEquipment;
    private javax.swing.JCheckBox ckHide;
    private javax.swing.JCheckBox ckLock;
    private javax.swing.JCheckBox ckStack;
    private javax.swing.JComboBox<String> cmbEquipRoot;
    private javax.swing.JComboBox<String> cmbEqupLimit;
    private javax.swing.JComboBox<String> cmbSlotIndex;
    private javax.swing.JComboBox<String> cmbSlotRoot;
    private javax.swing.JComboBox<String> cmbSlotType;
    private javax.swing.JComboBox<String> cmbStackNum;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField txtCap;
    private javax.swing.JTextArea txtDesp;
    private javax.swing.JTextField txtEquipLimt;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtObjCls;
    private javax.swing.JTextField txtStackLimit;
    private javax.swing.JTextField txtTag;
    // End of variables declaration//GEN-END:variables
}
