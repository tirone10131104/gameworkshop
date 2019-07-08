package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.codeTools;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;

public class dlgObjectClss extends javax.swing.JDialog
{

    private wakeup up = null;
    private beanObjectClass bean = null;
    private int tpid = 0;
    private boolean bOK = false;
    private boolean bEdit = false;
    private boolean bInit = false;

    public dlgObjectClss(java.awt.Frame parent, boolean modal, wakeup _up, beanObjectClass _bean, int _tpid)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        tpid = _tpid;
        initGUI();
    }

    public boolean getOK()
    {
        return bOK;
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
        if (bean == null)
        {
            bean = new beanObjectClass();
            bEdit = false;
            doAbstractCtrl();
        }
        else
        {
            bEdit = true;
            txtName.setText(bean.getClassName());
            txtDesp.setText(bean.getClassDesp());
            txtTag.setText(bean.getClassTag());
            int[] ifcs = codeTools.convertStrToArr(bean.getClassFuncs());
            for (int i = 0; i < ifcs.length; i++)
            {
                int id = ifcs[i];
                switch (id)
                {
                    case iConst.OCLS_FUNC_BUILD_LIST:
                        ckBuild.setSelected(true);
                        break;
                    case iConst.OCLS_FUNC_EFFECT_LIST:
                        ckEffect.setSelected(true);
                        break;
                    case iConst.OCLS_FUNC_PROPERTY_LIST:
                        ckProperty.setSelected(true);
                        break;
                    case iConst.OCLS_FUNC_RECYCLE_LIST:
                        ckRecycle.setSelected(true);
                        break;
                    case iConst.OCLS_FUNC_USE_REQUEST_LIST:
                        ckUseRequest.setSelected(true);
                        break;
                    case iConst.SYS_DB_EQUIP_CONFIG_LIST:
                        ckEquip.setSelected(true);
                        break;
                    case iConst.SYS_DB_FUNCTION_DEFINE_DATA:
                        ckFunction.setSelected(true);
                        break;
                }
            }
            txtTag.setEditable(false);
            fast.setCheckBoxValue(ckStrictConfig, bean.getStrictConfig());
            //CONFIG 部分  
            if (bean.getAbstractItem() == 0 && bean.getStack() == 0 && bean.getEquipment() == 0 && bean.getContainerItem() == 0)
            {
                doAbstractCtrl();
            }
            else
            {
                txtStackLimit.setText(bean.getStackLimit() + "");
                swsys.doSelectCombo(cmbSlotRoot, bean.getSlotRoot());
                makeSlotTypeCombo();
                swsys.doSelectCombo(cmbSlotType, bean.getSlotType());
                makeSlotIndexCombo();
                swsys.doSelectCombo(cmbSlotIndex, bean.getSlotIndex());
                bEdit = true;
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
        }
    }

    private void doOK()
    {
        bean.setClassDesp(txtDesp.getText().trim());
        bean.setClassName(txtName.getText().trim());
        String sfuc = "";
        if (ckBuild.isSelected())
        {
            sfuc = codeTools.addIntIntoString(sfuc, iConst.OCLS_FUNC_BUILD_LIST);
        }
        if (ckEffect.isSelected())
        {
            sfuc = codeTools.addIntIntoString(sfuc, iConst.OCLS_FUNC_EFFECT_LIST);
        }
        if (ckProperty.isSelected())
        {
            sfuc = codeTools.addIntIntoString(sfuc, iConst.OCLS_FUNC_PROPERTY_LIST);
        }
        if (ckRecycle.isSelected())
        {
            sfuc = codeTools.addIntIntoString(sfuc, iConst.OCLS_FUNC_RECYCLE_LIST);
        }
        if (ckUseRequest.isSelected())
        {
            sfuc = codeTools.addIntIntoString(sfuc, iConst.OCLS_FUNC_USE_REQUEST_LIST);
        }
        if (ckEquip.isSelected())
        {
            sfuc = codeTools.addIntIntoString(sfuc, iConst.SYS_DB_EQUIP_CONFIG_LIST);
        }
        if (ckFunction.isSelected())
        {
            sfuc = codeTools.addIntIntoString(sfuc, iConst.SYS_DB_FUNCTION_DEFINE_DATA);
        }
        bean.setClassFuncs(sfuc);
        //config部分
        bean.setAbstractItem(fast.readCheckBox(ckAbstract));
        bean.setStack(fast.readCheckBox(ckStack)); 
        bean.setStackLimit(fast.testIntegerText(txtStackLimit));
        bean.setSlotRoot(swsys.getComboBoxSelected(cmbSlotRoot));
        bean.setSlotType(swsys.getComboBoxSelected(cmbSlotType));
        bean.setSlotIndex(swsys.getComboBoxSelected(cmbSlotIndex));
        bean.setEquipment(fast.readCheckBox(ckEquipment)); 
        bean.setContainerItem(fast.readCheckBox(ckContainerItem));
        bean.setEquipRoot(swsys.getComboBoxSelected(cmbEquipRoot)); 
        bean.setStrictConfig(fast.readCheckBox(ckStrictConfig)); 
        int r = 0;
        objectClassDefine ocd = new objectClassDefine(up);
        if (bEdit)
        {
            r = ocd.modifyRecord(bean, false);
        }
        else
        {
            bean.setClassTag(txtTag.getText().trim());
            bean.setStpID(tpid);
            r = ocd.createRecord(bean, false);
        }
        if (r == iDAO.OPERATE_SUCCESS)
        {
            bOK = true;
            setVisible(false);
        }
        else
        {
            fast.err("操作失败", r);
        }
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
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

    private void doAbstractCtrl()
    {
        boolean b = ckAbstract.isSelected();
        ckStack.setSelected(!b);
        ckStack.setEnabled(!b);
        doStackLimitCtrl();
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

    private void doEquipmentCtrls()
    {
        boolean b = ckEquipment.isSelected();
        cmbSlotIndex.setEnabled(b);
        cmbSlotType.setEnabled(b);
        cmbSlotRoot.setEnabled(b);
        swsys.doSelectCombo(cmbSlotRoot, 0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnOK = new javax.swing.JButton();
        btnCancle = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ckProperty = new javax.swing.JCheckBox();
        ckEffect = new javax.swing.JCheckBox();
        ckBuild = new javax.swing.JCheckBox();
        ckRecycle = new javax.swing.JCheckBox();
        ckUseRequest = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        txtName = new javax.swing.JTextField();
        txtTag = new javax.swing.JTextField();
        txtDesp = new javax.swing.JTextField();
        ckEquip = new javax.swing.JCheckBox();
        ckFunction = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JSeparator();
        ckAbstract = new javax.swing.JCheckBox();
        ckStack = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        txtStackLimit = new javax.swing.JTextField();
        cmbStackNum = new javax.swing.JComboBox<>();
        ckEquipment = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        cmbSlotRoot = new javax.swing.JComboBox<>();
        cmbSlotType = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbSlotIndex = new javax.swing.JComboBox<>();
        ckContainerItem = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        cmbEquipRoot = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        ckStrictConfig = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("物类设置");

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

        jLabel1.setText("类型名称");

        jLabel2.setText("类型标签");

        jLabel3.setText("类型描述");

        jLabel4.setText("系统数据");

        ckProperty.setText("基本属性数据");

        ckEffect.setText("特殊效果数据");

        ckBuild.setText("建造需求数据");

        ckRecycle.setText("拆解回收数据");

        ckUseRequest.setText("使用需求数据");

        ckEquip.setForeground(new java.awt.Color(51, 51, 255));
        ckEquip.setText("装配控制数据");

        ckFunction.setForeground(new java.awt.Color(51, 51, 255));
        ckFunction.setText("主动功能控制");

        ckAbstract.setSelected(true);
        ckAbstract.setText("抽象物体");
        ckAbstract.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckAbstractActionPerformed(evt);
            }
        });

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

        cmbStackNum.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "255", "999", "1000", "9999", "10000", "99999", "100000", "999999", " " }));
        cmbStackNum.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbStackNumActionPerformed(evt);
            }
        });

        ckEquipment.setText("可装备");
        ckEquipment.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckEquipmentActionPerformed(evt);
            }
        });

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

        cmbSlotType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSlotType.setEnabled(false);
        cmbSlotType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSlotTypeActionPerformed(evt);
            }
        });

        jLabel6.setText("装配类型");

        jLabel7.setText("装配序位");

        cmbSlotIndex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSlotIndex.setEnabled(false);

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

        jLabel8.setText("严格物类");

        ckStrictConfig.setForeground(new java.awt.Color(255, 51, 51));
        ckStrictConfig.setText("设置为严格物类配置，这个物类下物体不可修改配置");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
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
                                    .addComponent(ckEquipment, javax.swing.GroupLayout.Alignment.LEADING)
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
                                    .addComponent(ckContainerItem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbStackNum, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ckAbstract)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ckStrictConfig)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2)
                            .addComponent(jSeparator1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnOK)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancle))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtName))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTag))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDesp))
                            .addComponent(jSeparator3)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(ckEquip)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ckFunction))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(ckProperty)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ckEffect)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ckBuild)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ckRecycle)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ckUseRequest))))
                                    .addComponent(jLabel4))
                                .addGap(0, 35, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckProperty)
                    .addComponent(ckEffect)
                    .addComponent(ckBuild)
                    .addComponent(ckRecycle)
                    .addComponent(ckUseRequest))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckEquip)
                    .addComponent(ckFunction))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckAbstract)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckStack)
                    .addComponent(jLabel5)
                    .addComponent(txtStackLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbStackNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckEquipment)
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
                    .addComponent(jLabel8)
                    .addComponent(ckStrictConfig))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void ckAbstractActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckAbstractActionPerformed
    {//GEN-HEADEREND:event_ckAbstractActionPerformed
        doAbstractCtrl();
    }//GEN-LAST:event_ckAbstractActionPerformed

    private void ckStackActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckStackActionPerformed
    {//GEN-HEADEREND:event_ckStackActionPerformed
        doStackLimitCtrl();
    }//GEN-LAST:event_ckStackActionPerformed

    private void cmbStackNumActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbStackNumActionPerformed
    {//GEN-HEADEREND:event_cmbStackNumActionPerformed
        txtStackLimit.setText(cmbStackNum.getSelectedItem() + "");
    }//GEN-LAST:event_cmbStackNumActionPerformed

    private void ckEquipmentActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckEquipmentActionPerformed
    {//GEN-HEADEREND:event_ckEquipmentActionPerformed
        doEquipmentCtrls();
    }//GEN-LAST:event_ckEquipmentActionPerformed

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

    private void ckContainerItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckContainerItemActionPerformed
    {//GEN-HEADEREND:event_ckContainerItemActionPerformed
        doContainerCtrl();
    }//GEN-LAST:event_ckContainerItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JCheckBox ckAbstract;
    private javax.swing.JCheckBox ckBuild;
    private javax.swing.JCheckBox ckContainerItem;
    private javax.swing.JCheckBox ckEffect;
    private javax.swing.JCheckBox ckEquip;
    private javax.swing.JCheckBox ckEquipment;
    private javax.swing.JCheckBox ckFunction;
    private javax.swing.JCheckBox ckProperty;
    private javax.swing.JCheckBox ckRecycle;
    private javax.swing.JCheckBox ckStack;
    private javax.swing.JCheckBox ckStrictConfig;
    private javax.swing.JCheckBox ckUseRequest;
    private javax.swing.JComboBox<String> cmbEquipRoot;
    private javax.swing.JComboBox<String> cmbSlotIndex;
    private javax.swing.JComboBox<String> cmbSlotRoot;
    private javax.swing.JComboBox<String> cmbSlotType;
    private javax.swing.JComboBox<String> cmbStackNum;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtStackLimit;
    private javax.swing.JTextField txtTag;
    // End of variables declaration//GEN-END:variables
}
