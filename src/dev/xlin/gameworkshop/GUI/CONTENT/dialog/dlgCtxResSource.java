package dev.xlin.gameworkshop.GUI.CONTENT.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxBaseResource;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxCelestialWorld;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldCfgResItem;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldResSource;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldTypeConfig;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldTypeMain;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxWorldTypeSet;
import dev.xlin.gameworkshop.progs.contents.progs.baseResourceDefine;
import dev.xlin.gameworkshop.progs.contents.progs.celestialWorldData;
import dev.xlin.gameworkshop.progs.contents.progs.ctxTranslate;
import dev.xlin.gameworkshop.progs.contents.progs.worldResSource;
import dev.xlin.gameworkshop.progs.contents.progs.worldTypeConfig;
import dev.xlin.gameworkshop.progs.contents.progs.worldTypeConfigResource;
import dev.xlin.gameworkshop.progs.contents.progs.worldTypeMain;
import dev.xlin.gameworkshop.progs.contents.progs.worldTypeSet;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class dlgCtxResSource extends javax.swing.JDialog
{

    private wakeup up = null;
    private beanCtxWorldResSource bean = null;
    private int worldOID = 0;
    private boolean bEdit = false;
    private boolean bOK = false;
    private beanCtxCelestialWorld bworld = null;
    private List lress = null;
    private List rcfgItems = null;

    public dlgCtxResSource(java.awt.Frame parent, boolean modal, wakeup _up, beanCtxWorldResSource _bean, int _wldid)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        worldOID = _wldid;
        celestialWorldData cwd = new celestialWorldData(up);
        bworld = (beanCtxCelestialWorld) cwd.getRecordByID(worldOID);
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        showInfo();
        makeResCombo();
        makeCfgItemsCombo();
        if (bean == null)
        {
            bean = new beanCtxWorldResSource();
            txtCode.setText(tagCreator.createDataTag(up, "tbc_res_mine_source", "tagCode", 4, 4));
            loadResCtrls();
            bEdit = false;
            randomGene();
        }
        else
        {
            System.err.println(".11..in edit ");
            swsys.doSelectCombo(cmbRes, bean.getResOID());
            swsys.doSelectCombo(cmbCfgItem, bean.getCfgID());
            loadResCtrls();
            txtCode.setText(bean.getTagCode());
            scrCap.setValue((int) bean.getCapability());
            scrEff.setValue((int) (bean.getEfficiency() * 10000.0));
            scrEffDecln.setValue((int) (bean.getEffDecln() * 10000.0));
            scrDiff.setValue((int) (bean.getDifficulty() * 10000.0));
            scrDiffIncrs.setValue((int) (bean.getDiffIncrs() * 10000.0));
            scrSurveyLevel.setValue((int) (bean.getSurveyLevel() * 10000));
            fast.setCheckBoxValue(ckVisb, bean.getDefaultVisb());
            cmbRes.setEnabled(false);
            cmbCfgItem.setEnabled(false);
            bEdit = true;
        }
        moveScrolls();
    }

    private void makeResCombo()
    {
        worldTypeConfigResource wtcr = new worldTypeConfigResource(up);
        rcfgItems = wtcr.getResourceListByConfig(bworld.getWorldTypeConfigOID());
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        baseResourceDefine brd = new baseResourceDefine(up);
        if (rcfgItems != null)
        {
            for (int i = 0; i < rcfgItems.size(); i++)
            {
                beanCtxWorldCfgResItem bit = (beanCtxWorldCfgResItem) rcfgItems.get(i);
                if (findBaseResInTempList(bit.getResOID()) == null)
                {
                    beanCtxBaseResource bcbr = (beanCtxBaseResource) brd.getRecordByID(bit.getResOID());
                    listItem li = new listItem(bcbr.getResName(), bit.getOID());
                    dcbm.addElement(li);
                    lress.add(bcbr);
                }
            }
        }
        cmbRes.setModel(dcbm);
    }

    private beanCtxBaseResource findBaseResInTempList(int rsid)
    {
        if (lress != null)
        {
            for (int i = 0; i < lress.size(); i++)
            {
                beanCtxBaseResource bres = (beanCtxBaseResource) lress.get(i);
                if (bres.getOID() == rsid)
                {
                    return bres;
                }
            }
        }
        else
        {
            lress = new ArrayList();
        }
        return null;
    }

    private void showInfo()
    {
        txtWorld.setText(bworld.getWorldName() + "<" + bworld.getWorldCode() + ">");
        String spth = "[" + ctxTranslate.translateConst(bworld.getWorldType()) + "] ";
        worldTypeMain wtm = new worldTypeMain(up);
        worldTypeSet wts = new worldTypeSet(up);
        worldTypeConfig wtc = new worldTypeConfig(up);
        beanCtxWorldTypeMain bmain = (beanCtxWorldTypeMain) wtm.getRecordByID(bworld.getWorldTypeMainOID());
        beanCtxWorldTypeSet bset = (beanCtxWorldTypeSet) wts.getRecordByID(bworld.getWorldTypeSetOID());
        beanCtxWorldTypeConfig bcfg = (beanCtxWorldTypeConfig) wtc.getRecordByID(bworld.getWorldTypeConfigOID());
        spth = spth + bmain.getWmName() + "/" + bset.getSetName() + "/" + bcfg.getCfgName();
        txtCfg.setText(spth);
    }

    private void cancle()
    {
        bOK = false;
        setVisible(false);
    }

    private void loadResCtrls()
    {
        int idx = cmbCfgItem.getSelectedIndex();
        if (idx < 0)
        {
            return;
        }
        listItem lsel = (listItem) cmbCfgItem.getSelectedItem();
        int cid = lsel.getNodeOID();
        System.err.println("1ok ");
        beanCtxWorldCfgResItem bres = findCfgItemInList(cid);
        System.err.println(".brea.d = " + bres.getCfgDescp());
        scrCap.setMinimum((int) bres.getCapMin());
        scrCap.setMaximum((int) bres.getCapMax());
        scrDiff.setMinimum((int) (bres.getDiffcMin() * 10000.0));
        scrDiff.setMaximum((int) (bres.getDiffcMax() * 10000.0));
        scrEff.setMinimum((int) (bres.getEffiMin() * 10000.0));
        scrEff.setMaximum((int) (bres.getEffiMax() * 10000.0));
        scrSurveyLevel.setMinimum((int) (bres.getSurveyMin() * 10000.0));
        scrSurveyLevel.setMaximum((int) (bres.getSurveyMax() * 10000.0));
        fast.setCheckBoxValue(ckVisb, bean.getDefaultVisb());
        scrDiffIncrs.setMinimum(0);
        scrDiffIncrs.setMaximum(10000);
        scrDiffIncrs.setValue(0);
        scrEffDecln.setMinimum(0);
        scrEffDecln.setMaximum(10000);
        scrEffDecln.setValue(0);
        scrCap.setValue(scrCap.getMinimum());
        scrDiff.setValue(scrDiff.getMinimum());
        scrEff.setValue(scrEff.getMinimum());
        scrSurveyLevel.setValue(scrSurveyLevel.getMinimum());
        scrDanger.setMinimum((int) (bres.getDangerMin() * 10000.0));
        scrDanger.setMaximum((int) (bres.getDangerMax() * 10000.0));
        scrDangerIncrs.setMinimum(0);
        scrDangerIncrs.setMaximum(10000);
        System.err.println("2OK");
        randomGene();
    }

    private beanCtxWorldCfgResItem findCfgItemInList(int cid)
    {
        if (rcfgItems != null)
        {
            for (int i = 0; i < rcfgItems.size(); i++)
            {
                beanCtxWorldCfgResItem bri = (beanCtxWorldCfgResItem) rcfgItems.get(i);
                if (bri.getOID() == cid)
                {
                    return bri;
                }
            }
        }
        return null;
    }

    private void moveScrolls()
    {
        selCap();
        selEff();
        selDiff();
        selDiffIncrs();
        selEffDecln();
        selSurvey();
    }

    private void selSurvey()
    {
        txtSurvey.setText(scrSurveyLevel.getValue() / 100.0 + "%");
    }

    private void selCap()
    {
        txtCap.setText(scrCap.getValue() + "");
    }

    private void selEff()
    {
        txtEffs.setText(scrEff.getValue() / 100.0 + "%");
    }

    private void selDiff()
    {
        txtDiff.setText(scrDiff.getValue() / 100.0 + "%");
    }

    private void selEffDecln()
    {
        txtEffDecln.setText(scrEffDecln.getValue() / 100.0 + "%");
    }

    private void selDiffIncrs()
    {
        txtDiffIncrs.setText(scrDiffIncrs.getValue() / 100.0 + "%");
    }

    private void selSurveyLevel()
    {
        txtSurvey.setText(scrSurveyLevel.getValue() / 100.0 + "%");
    }

    private void selDanger()
    {
        txtDanger.setText(scrDanger.getValue() / 100.0 + "%");
    }

    private void selDangerIncr()
    {
        txtDangerIncrs.setText(scrDangerIncrs.getValue() / 100.0 + "%");
    }

    public boolean getOK()
    {
        return bOK;
    }

    private void OK()
    {
        int idx = cmbRes.getSelectedIndex();
        if (idx < 0)
        {
            fast.warn("必须选择一种资源");
            return;
        }
        int cfgidx = cmbCfgItem.getSelectedIndex();
        if (cfgidx < 0)
        {
            fast.warn("必须选择一个配置项");
            return;
        }
        listItem li = (listItem) cmbCfgItem.getSelectedItem();
        int cid = li.getNodeOID();
        beanCtxWorldCfgResItem bcfgi = findCfgItemInList(cid);
        bean.setCapability(scrCap.getValue());
        bean.setDiffIncrs(scrDiffIncrs.getValue() / 10000.0);
        bean.setDifficulty(scrDiff.getValue() / 10000.0);
        bean.setEffDecln(scrEffDecln.getValue() / 10000.0);
        bean.setEfficiency(scrEff.getValue() / 10000.0);
        bean.setResOID(bcfgi.getResOID());
        bean.setCfgID(bcfgi.getOID());
        bean.setTagCode(txtCode.getText().trim());
        bean.setWorldOID(worldOID);
        bean.setDefaultVisb(fast.readCheckBox(ckVisb));
        bean.setSurveyLevel(scrSurveyLevel.getValue() / 10000.0);
        bean.setDanger(scrDanger.getValue() / 10000.0);
        bean.setDangerIncrs(scrDangerIncrs.getValue() / 10000.0);
        worldResSource wrs = new worldResSource(up);
        int r = 0;
        if (bEdit)
        {
            //修改
            r = wrs.modifyRecord(bean, false);
        }
        else
        {
            //创建
            r = wrs.createRecord(bean, false);
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

    private void randomGene()
    {
        int idx = cmbCfgItem.getSelectedIndex();
        if (idx < 0)
        {
            fast.warn("必须选择一种资源配置");
            return;
        }
        listItem li = (listItem) cmbCfgItem.getSelectedItem();
        int cid = li.getNodeOID();
        beanCtxWorldCfgResItem bcfgi = findCfgItemInList(cid);
        worldResSource wrs = new worldResSource(up);
        beanCtxWorldResSource nbrs = wrs.generateResSource(bcfgi);
        scrCap.setValue((int) nbrs.getCapability());
        scrDiff.setValue((int) (nbrs.getDifficulty() * 10000.0));
        System.err.println("nbrs.diff = " + nbrs.getDifficulty());
        scrEff.setValue((int) (nbrs.getEfficiency() * 10000.0));
        scrEffDecln.setValue((int) (nbrs.getEffDecln() * 10000.0));
        scrDiffIncrs.setValue((int) (nbrs.getDiffIncrs() * 10000));
        scrSurveyLevel.setValue((int) (nbrs.getSurveyLevel() * 10000));
        scrDanger.setValue((int) (nbrs.getDanger() * 10000));
        scrDangerIncrs.setValue((int) (nbrs.getDangerIncrs() * 10000));
        fast.setCheckBoxValue(ckVisb, nbrs.getDefaultVisb());
        moveScrolls();
    }

    private void makeCfgItemsCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        int rsid = cmbRes.getSelectedIndex();
        if (rsid >= 0)
        {
            beanCtxBaseResource bcbr = (beanCtxBaseResource) lress.get(rsid);
            List lcfgs = findCfgsInList(bcbr.getOID());
            for (int i = 0; i < lcfgs.size(); i++)
            {
                beanCtxWorldCfgResItem bri = (beanCtxWorldCfgResItem) lcfgs.get(i);
                listItem li = new listItem(bri.getCfgDescp() + "<" + bri.getCriTag() + ">", bri.getOID());
                mod.addElement(li);
            }
        }
        cmbCfgItem.setModel(mod);
    }

    private List findCfgsInList(int rsid)
    {
        List rs = new ArrayList();
        if (rcfgItems != null)
        {
            for (int i = 0; i < rcfgItems.size(); i++)
            {
                beanCtxWorldCfgResItem bri = (beanCtxWorldCfgResItem) rcfgItems.get(i);
                if (bri.getResOID() == rsid)
                {
                    rs.add(bri);
                }
            }
        }
        return rs;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtWorld = new javax.swing.JTextField();
        txtCode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCfg = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbRes = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        scrCap = new javax.swing.JScrollBar();
        txtCap = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        scrEff = new javax.swing.JScrollBar();
        txtEffs = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        scrDiff = new javax.swing.JScrollBar();
        txtDiff = new javax.swing.JTextField();
        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        scrDiffIncrs = new javax.swing.JScrollBar();
        txtDiffIncrs = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        scrEffDecln = new javax.swing.JScrollBar();
        txtEffDecln = new javax.swing.JTextField();
        btnRanGene = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cmbCfgItem = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        scrSurveyLevel = new javax.swing.JScrollBar();
        txtSurvey = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        ckVisb = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        scrDanger = new javax.swing.JScrollBar();
        txtDanger = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        scrDangerIncrs = new javax.swing.JScrollBar();
        txtDangerIncrs = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("资源点数据设置");
        setResizable(false);

        jLabel1.setText("天体");

        jLabel2.setText("标签");

        txtWorld.setEditable(false);

        txtCode.setEditable(false);

        jLabel3.setText("配置");

        txtCfg.setEditable(false);

        jLabel4.setText("资源");

        cmbRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbResActionPerformed(evt);
            }
        });

        jLabel5.setText("总量");

        scrCap.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrCap.setVisibleAmount(0);
        scrCap.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scrCapAdjustmentValueChanged(evt);
            }
        });

        txtCap.setEditable(false);

        jLabel6.setText("效率");

        scrEff.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrEff.setVisibleAmount(0);
        scrEff.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scrEffAdjustmentValueChanged(evt);
            }
        });

        txtEffs.setEditable(false);

        jLabel7.setText("难度");

        scrDiff.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrDiff.setVisibleAmount(0);
        scrDiff.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scrDiffAdjustmentValueChanged(evt);
            }
        });

        txtDiff.setEditable(false);

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

        jLabel8.setText("难度增幅");

        scrDiffIncrs.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrDiffIncrs.setVisibleAmount(0);
        scrDiffIncrs.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scrDiffIncrsAdjustmentValueChanged(evt);
            }
        });

        txtDiffIncrs.setEditable(false);

        jLabel9.setText("效率递减");

        scrEffDecln.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrEffDecln.setVisibleAmount(0);
        scrEffDecln.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scrEffDeclnAdjustmentValueChanged(evt);
            }
        });

        txtEffDecln.setEditable(false);

        btnRanGene.setText("随机产生");
        btnRanGene.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRanGeneActionPerformed(evt);
            }
        });

        jLabel10.setText("配置节点");

        cmbCfgItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCfgItemActionPerformed(evt);
            }
        });

        jLabel11.setText("勘探系数");

        scrSurveyLevel.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrSurveyLevel.setVisibleAmount(0);
        scrSurveyLevel.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scrSurveyLevelAdjustmentValueChanged(evt);
            }
        });

        txtSurvey.setEditable(false);

        jLabel12.setText("默认可见");

        ckVisb.setText("设置为默认直接可见");

        jLabel13.setText("危险系数");

        scrDanger.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrDanger.setVisibleAmount(0);
        scrDanger.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scrDangerAdjustmentValueChanged(evt);
            }
        });

        txtDanger.setEditable(false);

        jLabel14.setText("危险增幅");

        scrDangerIncrs.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrDangerIncrs.setVisibleAmount(0);
        scrDangerIncrs.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scrDangerIncrsAdjustmentValueChanged(evt);
            }
        });

        txtDangerIncrs.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtWorld))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCode))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCfg))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbRes, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCfgItem, 0, 223, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(scrCap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCap, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(scrEff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEffs, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnOK)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancle))
                            .addComponent(btnRanGene, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ckVisb)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrDangerIncrs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrDanger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrSurveyLevel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrEffDecln, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(scrDiff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(scrDiffIncrs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDangerIncrs)
                            .addComponent(txtDanger)
                            .addComponent(txtSurvey)
                            .addComponent(txtEffDecln)
                            .addComponent(txtDiffIncrs)
                            .addComponent(txtDiff, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtCap, txtDiff, txtDiffIncrs, txtEffDecln, txtEffs});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtWorld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCfg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbRes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(cmbCfgItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(scrCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(scrEff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEffs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(scrDiff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(scrDiffIncrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiffIncrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(scrEffDecln, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEffDecln, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel11)
                    .addComponent(scrSurveyLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSurvey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel13)
                    .addComponent(scrDanger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDanger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14)
                    .addComponent(scrDangerIncrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDangerIncrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(ckVisb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(btnRanGene)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel7, scrDiff, txtDiff});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel6, scrEff, txtEffs});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel5, scrCap, txtCap});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, scrDiffIncrs, txtDiffIncrs});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel9, scrEffDecln, txtEffDecln});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel11, jLabel13, jLabel14, scrDanger, scrDangerIncrs, scrSurveyLevel, txtDanger, txtDangerIncrs, txtSurvey});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        OK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void cmbResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbResActionPerformed
    {//GEN-HEADEREND:event_cmbResActionPerformed
        System.err.println("step init ");
        makeCfgItemsCombo();
        System.err.println("1done ");
        loadResCtrls();
        System.err.println("2done ");
    }//GEN-LAST:event_cmbResActionPerformed

    private void scrCapAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scrCapAdjustmentValueChanged
    {//GEN-HEADEREND:event_scrCapAdjustmentValueChanged
        selCap();
    }//GEN-LAST:event_scrCapAdjustmentValueChanged

    private void scrEffAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scrEffAdjustmentValueChanged
    {//GEN-HEADEREND:event_scrEffAdjustmentValueChanged
        selEff();
    }//GEN-LAST:event_scrEffAdjustmentValueChanged

    private void scrDiffAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scrDiffAdjustmentValueChanged
    {//GEN-HEADEREND:event_scrDiffAdjustmentValueChanged
        selDiff();
    }//GEN-LAST:event_scrDiffAdjustmentValueChanged

    private void scrDiffIncrsAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scrDiffIncrsAdjustmentValueChanged
    {//GEN-HEADEREND:event_scrDiffIncrsAdjustmentValueChanged
        selDiffIncrs();
    }//GEN-LAST:event_scrDiffIncrsAdjustmentValueChanged

    private void scrEffDeclnAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scrEffDeclnAdjustmentValueChanged
    {//GEN-HEADEREND:event_scrEffDeclnAdjustmentValueChanged
        selEffDecln();
    }//GEN-LAST:event_scrEffDeclnAdjustmentValueChanged

    private void btnRanGeneActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRanGeneActionPerformed
    {//GEN-HEADEREND:event_btnRanGeneActionPerformed
        randomGene();
    }//GEN-LAST:event_btnRanGeneActionPerformed

    private void cmbCfgItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbCfgItemActionPerformed
    {//GEN-HEADEREND:event_cmbCfgItemActionPerformed
        loadResCtrls();
    }//GEN-LAST:event_cmbCfgItemActionPerformed

    private void scrSurveyLevelAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scrSurveyLevelAdjustmentValueChanged
    {//GEN-HEADEREND:event_scrSurveyLevelAdjustmentValueChanged
        selSurveyLevel();
    }//GEN-LAST:event_scrSurveyLevelAdjustmentValueChanged

    private void scrDangerAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scrDangerAdjustmentValueChanged
    {//GEN-HEADEREND:event_scrDangerAdjustmentValueChanged
        selDanger();
    }//GEN-LAST:event_scrDangerAdjustmentValueChanged

    private void scrDangerIncrsAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scrDangerIncrsAdjustmentValueChanged
    {//GEN-HEADEREND:event_scrDangerIncrsAdjustmentValueChanged
        selDangerIncr();
    }//GEN-LAST:event_scrDangerIncrsAdjustmentValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnRanGene;
    private javax.swing.JCheckBox ckVisb;
    private javax.swing.JComboBox<String> cmbCfgItem;
    private javax.swing.JComboBox<String> cmbRes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JScrollBar scrCap;
    private javax.swing.JScrollBar scrDanger;
    private javax.swing.JScrollBar scrDangerIncrs;
    private javax.swing.JScrollBar scrDiff;
    private javax.swing.JScrollBar scrDiffIncrs;
    private javax.swing.JScrollBar scrEff;
    private javax.swing.JScrollBar scrEffDecln;
    private javax.swing.JScrollBar scrSurveyLevel;
    private javax.swing.JTextField txtCap;
    private javax.swing.JTextField txtCfg;
    private javax.swing.JTextField txtCode;
    private javax.swing.JTextField txtDanger;
    private javax.swing.JTextField txtDangerIncrs;
    private javax.swing.JTextField txtDiff;
    private javax.swing.JTextField txtDiffIncrs;
    private javax.swing.JTextField txtEffDecln;
    private javax.swing.JTextField txtEffs;
    private javax.swing.JTextField txtSurvey;
    private javax.swing.JTextField txtWorld;
    // End of variables declaration//GEN-END:variables
}
