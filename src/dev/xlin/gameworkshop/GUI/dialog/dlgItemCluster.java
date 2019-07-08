package dev.xlin.gameworkshop.GUI.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiCodes;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItem;
import dev.xlin.gameworkshop.progs.foundation.beans.beanItemCluster;
import dev.xlin.gameworkshop.progs.foundation.beans.beanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.itemCluster;
import dev.xlin.gameworkshop.progs.foundation.itemDefine;
import dev.xlin.gameworkshop.progs.foundation.objectClassDefine;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.dlgTools.dlgSelectTreeNode;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tools.codeTools;
import dev.xlin.tools.constChk;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

public class dlgItemCluster extends javax.swing.JDialog
{

    private wakeup up = null;
    private boolean bOK = false;
    private boolean bEdit = false;
    private beanItemCluster bean = null;
    private int tartype = 0;
    private HashMap hds = new HashMap();
    private int typeID = 0;
    private boolean initDone = false;

    public dlgItemCluster(java.awt.Frame parent, boolean modal, wakeup _up, beanItemCluster _bean, int _typeID)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        typeID = _typeID;
        initGUI();
        initDone = true;
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        makeCombo();
        if (bean == null)
        {
            bean = new beanItemCluster();
            bEdit = false;
            tartype = swsys.getComboBoxSelected(cmbTartype);
        }
        else
        {
            txtDesp.setText(bean.getDescript());
            txtName.setText(bean.getClusterName());
            txtTag.setText(bean.getClusterTag());
            txtTag.setEditable(false);
            swsys.doSelectCombo(cmbTartype, bean.getTargetType());
            tartype = bean.getTargetType();
            loadDataList();
            makeDataList();
            bEdit = true;
        }
    }

    private void loadDataList()
    {
        hds = new HashMap();
        int[] ids = codeTools.convertStrToArr(bean.getOCLS());
        if (bean.getTargetType() == iConst.ICLUS_TARTYPE_OCLS)
        {
            objectClassDefine ocd = new objectClassDefine(up);
            for (int i = 0; i < ids.length; i++)
            {
                int id = ids[i];
                beanObjectClass boc = ocd.getObjectClassByOID(id);
                hds.put(boc.getOID(), boc);
            }
        }
        else if (bean.getTargetType() == iConst.ICLUS_TARTYPE_ITEMS)
        {
            itemDefine idef = new itemDefine(up);
            for (int i = 0; i < ids.length; i++)
            {
                int id = ids[i];
                beanItem bit = (beanItem) idef.getRecordByID(id);
                hds.put(bit.getOID(), bit);
            }
        }
         else if (bean.getTargetType() == iConst.ICLUS_TARTYPE_OC_TPS || bean.getTargetType() == iConst.ICLUS_TARTYPE_IT_TPS)
         {
             sttType stp = new sttType(up);
             for (int i = 0; i < ids.length; i++)
             {
                 int id = ids[i];
                 beanSttType bst = (beanSttType) stp.getRecordByID(id);
                 hds.put(bst.getOID(), bst);
             }
         }
        else
        {
            //
        }
    }

    private void makeCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        int[] ids = constChk.getFinalInts(iConst.class, "ICLUS_TARTYPE_");
        for (int i = 0; i < ids.length; i++)
        {
            int id = ids[i];
            listItem li = new listItem(iConst.translate(id), id);
            mod.addElement(li);
        }
        cmbTartype.setModel(mod);
    }

    private void makeDataList()
    {
        DefaultListModel dlm = new DefaultListModel();
        if (hds != null)
        {
            Set ks = hds.keySet();
            Iterator itor = ks.iterator();
            while (itor.hasNext())
            {
                int i = (int) itor.next();
                if (tartype == iConst.ICLUS_TARTYPE_OCLS)
                {
                    beanObjectClass boc = (beanObjectClass) hds.get(i);
                    listItem li = new listItem(boc.getClassName(), boc.getOID());
                    dlm.addElement(li);
                }
                else if (tartype == iConst.ICLUS_TARTYPE_ITEMS)
                {
                    beanItem bit = (beanItem) hds.get(i);
                    listItem li = new listItem(bit.getItemName(), bit.getOID());
                    dlm.addElement(li);
                }
                else if (tartype == iConst.ICLUS_TARTYPE_OC_TPS || tartype == iConst.ICLUS_TARTYPE_IT_TPS)
                {
                    beanSttType bst = (beanSttType) hds.get(i);
                    listItem li = new listItem(bst.getTypeName(), bst.getOID());
                    dlm.addElement(li);
                }
            }
        }
        lstData.setModel(dlm);
    }

    public boolean getOK()
    {
        return bOK;
    }

    private void doOK()
    {
        String stn = txtName.getText().trim();
        String stg = txtTag.getText().trim();
        if (stn.equals(""))
        {
            fast.warn("名字不能为空");
            return;
        }
        if (stg.equals(""))
        {
            fast.warn("标签不能为空");
            return;
        }
        bean.setClusterName(stn);
        bean.setClusterTag(stg);
        bean.setDescript(txtDesp.getText().trim());
        bean.setTargetType(tartype);
        //组装IDS
        Set ks = hds.keySet();
        Iterator itor = ks.iterator();
        String sids = "";
        while (itor.hasNext())
        {
            int id = (int) itor.next();
            sids = codeTools.addIntIntoString(sids, id);
        }
        bean.setOCLS(sids);
        int r = 0;
        itemCluster iclu = new itemCluster(up);
        if (bEdit)
        {
            r = iclu.modifyRecord(bean, false);
        }
        else
        {
            bean.setTypeID(typeID);
            r = iclu.createRecord(bean, false);
        }
        if (r == iDAO.OPERATE_SUCCESS)
        {
            bOK = true;
            setVisible(false);
        }
        else
        {
            fast.err("操作发生错误", r);
        }
    }

    private void doCancle()
    {
        bOK = false;
        setVisible(false);
    }

    private void doAdd()
    {
        if (hds == null)
        {
            hds = new HashMap();
        }
        if (tartype == iConst.ICLUS_TARTYPE_OCLS)
        {
            dlgSelectObjectClass dlg = new dlgSelectObjectClass(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanObjectClass boc = dlg.getSelectedObjectClass();
                if (hds.containsKey(boc.getOID()))
                {
                    fast.warn("选择重复");
                    return;
                }
                else
                {
                    hds.put(boc.getOID(), boc);
                    makeDataList();
                }
            }
            dlg.dispose();
            dlg = null;
        }
        else if (tartype == iConst.ICLUS_TARTYPE_ITEMS)
        {
            dlgSelectItemDefine dlg = new dlgSelectItemDefine(null, true, up);
            dlg.setVisible(true);
            if (dlg.getOK())
            {
                beanItem bit = dlg.getSelectedItemDefine();
                if (hds.containsKey(bit.getOID()))
                {
                    fast.warn("选择重复");
                    return;
                }
                else
                {
                    hds.put(bit.getOID(), bit);
                    makeDataList();
                }
            }
            dlg.dispose();
            dlg = null;
        }
        else if (tartype == iConst.ICLUS_TARTYPE_OC_TPS)
        {
            doSelectTypeNode(systemType.CODE_STT_OBJ_CLS);
        }
        else if (tartype == iConst.ICLUS_TARTYPE_IT_TPS)
        {
            doSelectTypeNode(systemType.CODE_STT_ITEMS);
        }
        else
        {

        }
    }

    private void doSelectTypeNode(int systp)
    {
        myTreeNode mtn = guiCodes.makeFlatTypeTree(up, systp, false);
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mtn);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            myTreeNode msel = dlg.getSelect();
            sttType stp = new sttType(up);
            beanSttType btp = (beanSttType) stp.getRecordByID(msel.getNodeOID());
            if (btp != null)
            {
                if (hds.containsKey(msel.getNodeOID()))
                {
                    fast.warn("选择重复");
                    return;
                }
                else
                {
                    hds.put(msel.getNodeOID(), btp);
                    makeDataList();
                }
            }
        }
        dlg.dispose();
        dlg = null;
    }

    public listItem getSelectItem()
    {
        int idx = lstData.getSelectedIndex();
        DefaultListModel dlm = (DefaultListModel) lstData.getModel();
        listItem li = (listItem) dlm.get(idx);
        return li;
    }

    private void doRemove()
    {
        listItem li = getSelectItem();
        if (li == null)
        {
            return;
        }
        int sel = fast.ask("确认删除？");
        if (sel != fast.YES)
        {
            return;
        }
        hds.remove(li.getNodeOID());
        makeDataList();
    }

    private void doSelectTarType()
    {
        if (initDone)
        {
            int sid = swsys.getComboBoxSelected(cmbTartype);
            if (sid != tartype)
            {
                int sel = fast.ask("是否切换目标数据的类型，切换会同步清空已选择的所有数据。");
                if (sel != fast.YES)
                {
                    swsys.doSelectCombo(cmbTartype, tartype);
                }
                else
                {
                    tartype = sid;
                    hds.clear();
                    makeDataList();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstData = new javax.swing.JList<>();
        btnAddData = new javax.swing.JButton();
        btnRemoveData = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        txtTag = new javax.swing.JTextField();
        txtDesp = new javax.swing.JTextField();
        cmbTartype = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("物体数据集设置");

        btnCancle.setText("取消");
        btnCancle.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancleActionPerformed(evt);
            }
        });

        btnOK.setText("确定");
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOKActionPerformed(evt);
            }
        });

        jLabel1.setText("数据名称");

        jLabel2.setText("数据标签");

        jLabel3.setText("数据描述");

        jLabel4.setText("数据类型");

        jLabel5.setText("数据列表");
        jLabel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        lstData.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(lstData);

        btnAddData.setText("添加数据");
        btnAddData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddDataActionPerformed(evt);
            }
        });

        btnRemoveData.setText("移除数据");
        btnRemoveData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveDataActionPerformed(evt);
            }
        });

        cmbTartype.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTartypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddData, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnRemoveData, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTag))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTartype, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDesp)))
                .addContainerGap())
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbTartype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancle)
                            .addComponent(btnOK)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAddData)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveData)
                        .addGap(0, 0, Short.MAX_VALUE)))
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

    private void btnAddDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddDataActionPerformed
    {//GEN-HEADEREND:event_btnAddDataActionPerformed
        doAdd();
    }//GEN-LAST:event_btnAddDataActionPerformed

    private void btnRemoveDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRemoveDataActionPerformed
    {//GEN-HEADEREND:event_btnRemoveDataActionPerformed
        doRemove();
    }//GEN-LAST:event_btnRemoveDataActionPerformed

    private void cmbTartypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTartypeActionPerformed
    {//GEN-HEADEREND:event_cmbTartypeActionPerformed
        doSelectTarType();
    }//GEN-LAST:event_cmbTartypeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddData;
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnRemoveData;
    private javax.swing.JComboBox<String> cmbTartype;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList<String> lstData;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTag;
    // End of variables declaration//GEN-END:variables
}
