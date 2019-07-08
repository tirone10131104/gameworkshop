package dev.xlin.gameworkshop.GUI.CONTENT.dialog;

import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxConstellation;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxStellarData;
import dev.xlin.gameworkshop.progs.contents.beans.beanCtxStellarRegion;
import dev.xlin.gameworkshop.progs.contents.beans.poObjectPhysic;
import dev.xlin.gameworkshop.progs.contents.progs.constellationData;
import dev.xlin.gameworkshop.progs.contents.progs.ctxConst;
import dev.xlin.gameworkshop.progs.contents.progs.ctxTranslate;
import dev.xlin.gameworkshop.progs.contents.progs.stellarData;
import dev.xlin.gameworkshop.progs.contents.progs.stellarRegion;
import dev.xlin.gameworkshop.progs.tools.tagCreator;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.swsys;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class dlgCtxStellarData extends javax.swing.JDialog
{

    private wakeup up = null;
    private stellarData stdt = null;
    private beanCtxStellarData bean = null;
    private int glaxyID = 0;
    private boolean bOK = false;
    private boolean bEdit = false;
    private poObjectPhysic pop = null;
    private int constOID = 0;

    public dlgCtxStellarData(java.awt.Frame parent, boolean modal, wakeup _up, beanCtxStellarData _bean, int _glxID, int _constID)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        glaxyID = _glxID;
        constOID = _constID;
        stdt = new stellarData(up);
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        constellationData csdt = new constellationData(up);

        makeRegionCombo();
        makeConstCombo();
        doMakeGreekChrCombo();
        doMakeSpectrumCombo();
        doMakeSpectrumLevelCombo();
        doMakeMassRegion();
        doMakeTempretureRegion();
        if (bean == null)
        {
            bEdit = false;
            bean = new beanCtxStellarData();
            if (constOID != 0)
            {
                beanCtxConstellation bccd = (beanCtxConstellation) csdt.getRecordByID(constOID);
                System.err.println("bccd = " + bccd);
                if (bccd != null)
                {
                    swsys.doSelectCombo(cmbRegion, bccd.getRegionOID());
                    swsys.doSelectCombo(cmbConstellation, bccd.getOID());
                    //寻找一个新IDX
                    tryAllocteConsIndex();
                }
            }
        }
        else
        {
            bEdit = true;
            txtDesp.setText(bean.getStDesp());
            txtSteName.setText(bean.getStName());
            txtTag.setText(bean.getStTag());
            swsys.doSelectCombo(cmbRegion, bean.getRegionOID());
            swsys.doSelectCombo(cmbConstellation, bean.getConOID());
            swsys.doSelectCombo(cmbConstellation, bean.getConIndex());
            swsys.doSelectCombo(cmbSpectrum, bean.getSpectrumID());
            swsys.doSelectCombo(cmbLevel, bean.getSpectrumLevel());
            doMakeTempretureRegion();
            doMakeMassRegion();
            scrTempreture.setValue((int) bean.getTempreture());
            scrMass.setValue((int) (bean.getMass() * 100));
            labAbsMag.setText(fast.makeRound2(bean.getAbsMang()) + "");
            labDensity.setText(fast.makeRound2(bean.getDensity()) + "kg/m3");
            labDia.setText(fast.makeRound2(bean.getDiameter()) + "km");
            labGravity.setText(fast.makeRound2(bean.getGravity()) + "m/s2");
            labGravity1.setText(fast.makeRound2(bean.getCosmic1()) + "km/s");
            labGravity2.setText(fast.makeRound2(bean.getCosmic2()) + "km/s");
            labLuminosity.setText(fast.makeRound2(bean.getLuminosity()) + "");
            cmbRegion.setEnabled(false);
            cmbConIndex.setEnabled(false);
            cmbConstellation.setEnabled(false);
        }
        cmbConIndex.setEnabled(false);
    }

    private void tryAllocteConsIndex()
    {
        if (bEdit == false)
        {
            //在新建条件下。
            constOID = swsys.getComboBoxSelected(cmbConstellation);
            stellarData sdt = new stellarData(up);
            int midx = sdt.allocateConstellationMaxIndex(constOID);
            System.err.println(".midx = " + midx);
            swsys.doSelectCombo(cmbConIndex, midx);
        }
        else
        {
            swsys.doSelectCombo(cmbConIndex, bean.getConIndex());
        }
    }

    //星域下拉列表
    private void makeRegionCombo()
    {
        System.err.println("makeRegionCombo");
        stellarRegion srg = new stellarRegion(up);
        List lrs = srg.getRegionsByGlaxy(glaxyID);
        DefaultComboBoxModel cbm = new DefaultComboBoxModel();
        if (lrs != null)
        {
            for (int i = 0; i < lrs.size(); i++)
            {
                beanCtxStellarRegion bcsr = (beanCtxStellarRegion) lrs.get(i);
                listItem li = new listItem(bcsr.getRegionName() + "<" + bcsr.getRegionTag() + ">", bcsr.getOID());
                cbm.addElement(li);
            }
        }
        cmbRegion.setModel(cbm);
    }

    //星座下拉列表
    private void makeConstCombo()
    {
        System.err.println("makeConstCombo");
        listItem lsrg = (listItem) cmbRegion.getSelectedItem();
        DefaultComboBoxModel cbm = new DefaultComboBoxModel();
        if (lsrg != null)
        {
            constellationData csdt = new constellationData(up);
            List lcst = csdt.queryConstellationsByRegion(lsrg.getNodeOID());
            if (lcst != null)
            {
                for (int i = 0; i < lcst.size(); i++)
                {
                    beanCtxConstellation bcc = (beanCtxConstellation) lcst.get(i);
                    listItem li = new listItem(bcc.getConName() + "<" + bcc.getConTag() + ">", bcc.getOID());
                    cbm.addElement(li);
                }
            }
        }
        cmbConstellation.setModel(cbm);
    }

    private void geneTag()
    {
        System.err.println("geneTag ");
        String stg = tagCreator.createDataTag(up, "tbc_stellar_data", "stTag", 2, 5);
        txtTag.setText(stg);
        System.err.println("tag = " + stg);
    }

    private void doMakeSpectrumCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        int[] ispcod =
        {
            ctxConst.SPC_O, ctxConst.SPC_B, ctxConst.SPC_A, ctxConst.SPC_F, ctxConst.SPC_G, ctxConst.SPC_K, ctxConst.SPC_M
        };
        for (int i = 0; i < ispcod.length; i++)
        {
            int spc = ispcod[i];
            listItem li = new listItem(ctxTranslate.translateConst(spc), spc);
            mod.addElement(li);
        }
        cmbSpectrum.setModel(mod);
    }

    private void doMakeSpectrumLevelCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        for (int i = 0; i < 10; i++)
        {
            listItem li = new listItem(i + "", i);
            mod.addElement(li);
        }
        cmbLevel.setModel(mod);
    }

    //根据星光谱类型，制作表面温度区间计算
    private void doMakeTempretureRegion()
    {
        int ispc = swsys.getComboBoxSelected(cmbSpectrum);
        int ilvl = swsys.getComboBoxSelected(cmbLevel);
        double dmax = 1;
        double dmin = 0;
        switch (ispc)
        {
            case ctxConst.SPC_A:
                dmax = 10000;
                dmin = 7500;
                break;
            case ctxConst.SPC_B:
                dmax = 30000;
                dmin = 10000;
                break;
            case ctxConst.SPC_F:
                dmax = 7500;
                dmin = 6000;
                break;
            case ctxConst.SPC_G:
                dmax = 6000;
                dmin = 5000;
                break;
            case ctxConst.SPC_K:
                dmax = 5000;
                dmin = 3500;
                break;
            case ctxConst.SPC_M:
                dmax = 3500;
                dmin = 2000;
                break;
            case ctxConst.SPC_O:
                dmax = 60000;
                dmin = 30000;
                break;
        }
        double spcLv = (dmax - dmin) / 10;
        double scopMin = dmax - spcLv * ilvl - spcLv;
        double scopMax = dmax - spcLv * ilvl;
        scrTempreture.setMaximum((int) scopMax);
        scrTempreture.setMinimum((int) scopMin);
        System.err.println("max = " + scopMax + " min = " + scopMin);
        scrTempreture.setValue(scrTempreture.getMinimum());
        showScrLables();
    }

    private void doMakeGreekChrCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        for (int i = 0; i < 24; i++)
        {
            listItem li = new listItem(ctxTranslate.transGreekCharactor(i) + "  (" + (i + 1) + ")", i);
            mod.addElement(li);
        }
        //额外顺序
        for (int i = 24; i < 50; i++)
        {
            listItem li = new listItem((i + 1) + "", i);
            mod.addElement(li);
        }
        cmbConIndex.setModel(mod);
    }

    private void showScrLables()
    {
        labTemp.setText(scrTempreture.getValue() + "");
        labMass.setText(scrMass.getValue() / 100.0 + "");
        doCalStarPhy();
    }

    private void doCalStarPhy()
    {
        //计算恒星的物理状态
        //计算恒星光度
        double PI = 3.1415386;
        double dtemp = (double) scrTempreture.getValue();
        if (dtemp == 0)
        {
            System.err.println("tmp = 0");
            return;
        }
        double dmass = (double) scrMass.getValue() / 100.0;
        if (dmass == 0)
        {
            System.err.println("mas = 0 ");
            return;
        }
        pop = stdt.calculateStarPhysicData(dmass, dtemp);
        labDensity.setText(fast.makeRound2(pop.getDensity()) + "kg/m3");
        labDia.setText(fast.makeRound2(pop.getDiameter()) + "km");
        labGravity.setText(fast.makeRound2(pop.getGravity()) + "m/s2");
        labGravity1.setText(fast.makeRound2(pop.getCosmic1()) + "km/s");
        labGravity2.setText(fast.makeRound2(pop.getCosmic2()) + "km/s");
        labLuminosity.setText(fast.makeRound2(pop.getLuminosity()) + "");
        labAbsMag.setText(fast.makeRound2(pop.getAbsMang()) + "");
    }

    private boolean checkTag(boolean rpsc)
    {
        String stg = txtTag.getText().trim();
        if (stg.equals(""))
        {
            fast.warn("标签不可为空");
            return false;
        }
        stellarRegion srg = new stellarRegion(up);
        if (srg.getRegionByTag(stg) != null)
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

    private void doSelectStarType()
    {
        doMakeTempretureRegion();
        doMakeMassRegion();
    }

    //根据星光谱类型，制作质量区间计算
    private void doMakeMassRegion()
    {
        int ispc = swsys.getComboBoxSelected(cmbSpectrum);
        int ilvl = swsys.getComboBoxSelected(cmbLevel);
        double dmax = 1;
        double dmin = 0;
        switch (ispc)
        {
            case ctxConst.SPC_O:
                dmax = 40;
                dmin = 20;
                break;
            case ctxConst.SPC_B:
                dmax = 20;
                dmin = 5;
                break;
            case ctxConst.SPC_A:
                dmax = 5;
                dmin = 2;
                break;
            case ctxConst.SPC_F:
                dmax = 2;
                dmin = 1.2;
                break;
            case ctxConst.SPC_G:
                dmax = 1.2;
                dmin = 0.8;
                break;
            case ctxConst.SPC_K:
                dmax = 0.8;
                dmin = 0.4;
                break;
            case ctxConst.SPC_M:
                dmax = 0.4;
                dmin = 0.1;
                break;
        }
        double spcLv = (dmax - dmin) / 10;
        double scopMin = dmax - spcLv * ilvl - spcLv;
        double scopMax = dmax - spcLv * ilvl;
        scrMass.setMaximum((int) (scopMax * 100.0));
        scrMass.setMinimum((int) (scopMin * 100.0));
        System.err.println("max = " + scopMax + " min = " + scopMin);
        scrMass.setValue(scrMass.getMinimum());
        System.err.println("scrm.= " + scrMass.getMaximum() + " min = " + scrMass.getMinimum());
        showScrLables();
    }

    private void OK()
    {
        if (pop == null)
        {
            doCalStarPhy();
        }
        //检查基本内容
        int rid = fast.readCombo(cmbRegion);
        if (rid == fast.COMBO_VALUE_ERROR)
        {
            fast.warn("请选择一个星域");
            return;
        }
        int cid = fast.readCombo(cmbConstellation);
        if (cid == fast.COMBO_VALUE_ERROR)
        {
            fast.warn("请选择一个星座");
            return;
        }
        int cix = fast.readCombo(cmbConIndex);
        if (cix == fast.COMBO_VALUE_ERROR)
        {
            fast.warn("请选择一个星座序号");
            return;
        }
        if (checkTag(false) == false)
        {
            return;
        }
        if (txtSteName.getText().trim().equals(""))
        {
            fast.warn("必须填写一个名称");
            return;
        }
        //数据注入
        System.err.println("bean = " + bean + " pop = " + pop);
        bean.setAbsMang(pop.getAbsMang());
        bean.setCosmic1(pop.getCosmic1());
        bean.setCosmic2(pop.getCosmic2());
        bean.setDensity(pop.getDensity());
        bean.setDiameter(pop.getDiameter());
        bean.setGravity(pop.getGravity());
        bean.setLuminosity(pop.getLuminosity());
        bean.setMass((double) scrMass.getValue() / 100.00);
        bean.setSpectrumID(fast.readCombo(cmbSpectrum));
        bean.setStName(txtSteName.getText().trim());
        bean.setStTag(txtTag.getText().trim());
        bean.setTempreture(scrTempreture.getValue());
        bean.setSpectrumLevel(fast.readCombo(cmbLevel));
        System.err.println(".bean . lv = " + bean.getSpectrumLevel());
        //分情况操作DAO。
        int r = 0;
        stellarData std = new stellarData(up);
        if (bEdit)
        {
            r = std.modifyRecord(bean, false);
        }
        else
        {
            bean.setGlaxyOID(glaxyID);
            bean.setRegionOID(rid);
            bean.setConOID(cid);
            bean.setConIndex(cix);
            r = std.createRecord(bean, false);
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

    private void cancle()
    {
        bOK = false;
        setVisible(false);
    }

    public boolean getOK()
    {
        return bOK;
    }

    private void constComboSelected()
    {
        doMakeGreekChrCombo();
        tryAllocteConsIndex();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSteName = new javax.swing.JTextField();
        tbrTagTools = new javax.swing.JToolBar();
        btnGeneTag = new javax.swing.JButton();
        btnCheckTag = new javax.swing.JButton();
        txtTag = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDesp = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        cmbRegion = new javax.swing.JComboBox<>();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel22 = new javax.swing.JLabel();
        labAbsMag = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        cmbSpectrum = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cmbLevel = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        scrTempreture = new javax.swing.JScrollBar();
        labTemp = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        labDia = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        labLuminosity = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        labDensity = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        labGravity = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        labGravity1 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        labGravity2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        scrMass = new javax.swing.JScrollBar();
        labMass = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbConstellation = new javax.swing.JComboBox<>();
        txtCoord = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        cmbConIndex = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("恒星基本信息");
        setResizable(false);

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

        jLabel1.setText("恒星名称");

        jLabel2.setText("恒星标签");

        jLabel3.setText("恒星描述");

        tbrTagTools.setBorder(null);
        tbrTagTools.setFloatable(false);
        tbrTagTools.setRollover(true);

        btnGeneTag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dev/xlin/gameworkshop/GUI/res/gener.png"))); // NOI18N
        btnGeneTag.setToolTipText("自动创建标签");
        btnGeneTag.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnGeneTagActionPerformed(evt);
            }
        });
        tbrTagTools.add(btnGeneTag);

        btnCheckTag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dev/xlin/gameworkshop/GUI/res/prot.png"))); // NOI18N
        btnCheckTag.setToolTipText("检查标签合法性");
        btnCheckTag.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCheckTagActionPerformed(evt);
            }
        });
        tbrTagTools.add(btnCheckTag);

        txtDesp.setColumns(20);
        txtDesp.setFont(new java.awt.Font("宋体", 0, 12)); // NOI18N
        txtDesp.setRows(3);
        jScrollPane1.setViewportView(txtDesp);

        jLabel4.setText("所属星域");

        cmbRegion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbRegion.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbRegionActionPerformed(evt);
            }
        });

        jLabel22.setText("绝对星等");

        labAbsMag.setBackground(new java.awt.Color(0, 0, 255));
        labAbsMag.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        labAbsMag.setForeground(new java.awt.Color(0, 0, 255));
        labAbsMag.setText("0");

        jLabel8.setText("光谱类型");

        cmbSpectrum.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSpectrum.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cmbSpectrumItemStateChanged(evt);
            }
        });
        cmbSpectrum.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSpectrumActionPerformed(evt);
            }
        });

        jLabel9.setText("级别");

        cmbLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbLevel.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cmbLevelItemStateChanged(evt);
            }
        });
        cmbLevel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbLevelActionPerformed(evt);
            }
        });

        jLabel10.setText("表面温度");

        scrTempreture.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrTempreture.setVisibleAmount(0);
        scrTempreture.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scrTempretureAdjustmentValueChanged(evt);
            }
        });

        labTemp.setBackground(new java.awt.Color(0, 0, 255));
        labTemp.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        labTemp.setForeground(new java.awt.Color(0, 0, 255));
        labTemp.setText("0");

        jLabel11.setText("直径");

        labDia.setBackground(new java.awt.Color(0, 0, 255));
        labDia.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        labDia.setForeground(new java.awt.Color(0, 0, 255));
        labDia.setText("0");

        jLabel12.setText("密度");

        labLuminosity.setBackground(new java.awt.Color(0, 0, 255));
        labLuminosity.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        labLuminosity.setForeground(new java.awt.Color(0, 0, 255));
        labLuminosity.setText("0");

        jLabel13.setText("光度");

        labDensity.setBackground(new java.awt.Color(0, 0, 255));
        labDensity.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        labDensity.setForeground(new java.awt.Color(0, 0, 255));
        labDensity.setText("0");

        jLabel15.setText("引力");

        labGravity.setBackground(new java.awt.Color(0, 0, 255));
        labGravity.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        labGravity.setForeground(new java.awt.Color(0, 0, 255));
        labGravity.setText("0");

        jLabel14.setText("第一宇宙速度");

        labGravity1.setBackground(new java.awt.Color(0, 0, 255));
        labGravity1.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        labGravity1.setForeground(new java.awt.Color(0, 0, 255));
        labGravity1.setText("0");

        jLabel16.setText("第二宇宙速度");

        labGravity2.setBackground(new java.awt.Color(0, 0, 255));
        labGravity2.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        labGravity2.setForeground(new java.awt.Color(0, 0, 255));
        labGravity2.setText("0");

        jLabel17.setText("坐标");

        jLabel18.setText("质量");

        scrMass.setBlockIncrement(1);
        scrMass.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrMass.setVisibleAmount(0);
        scrMass.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scrMassAdjustmentValueChanged(evt);
            }
        });

        labMass.setBackground(new java.awt.Color(0, 0, 255));
        labMass.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        labMass.setForeground(new java.awt.Color(0, 0, 255));
        labMass.setText("0");

        jLabel5.setText("星座");

        cmbConstellation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbConstellation.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbConstellationActionPerformed(evt);
            }
        });

        txtCoord.setEditable(false);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel6.setText("序号");

        cmbConIndex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                        .addComponent(txtSteName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTag)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbConstellation, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbConIndex, 0, 107, Short.MAX_VALUE))))
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrMass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSpectrum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrTempreture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labTemp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labMass, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labDia, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labLuminosity, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labGravity, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labGravity1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labGravity2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labDensity, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labAbsMag, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4))
                    .addComponent(jSeparator3)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCoord)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSteName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(tbrTagTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(cmbConstellation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cmbConIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labTemp)
                    .addComponent(scrTempreture, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(cmbLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSpectrum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(labMass)
                    .addComponent(scrMass, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel22)
                                .addComponent(labAbsMag))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labDia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labDensity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labLuminosity, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labGravity, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(labGravity1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labGravity2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jSeparator4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtCoord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, tbrTagTools, txtTag});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbLevel, cmbSpectrum, jLabel10, jLabel8, jLabel9, labTemp, scrTempreture});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        OK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnGeneTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnGeneTagActionPerformed
    {//GEN-HEADEREND:event_btnGeneTagActionPerformed
        geneTag();
    }//GEN-LAST:event_btnGeneTagActionPerformed

    private void btnCheckTagActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCheckTagActionPerformed
    {//GEN-HEADEREND:event_btnCheckTagActionPerformed
        checkTag(true);
    }//GEN-LAST:event_btnCheckTagActionPerformed

    private void cmbSpectrumItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbSpectrumItemStateChanged
    {//GEN-HEADEREND:event_cmbSpectrumItemStateChanged
        doSelectStarType();
    }//GEN-LAST:event_cmbSpectrumItemStateChanged

    private void cmbSpectrumActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSpectrumActionPerformed
    {//GEN-HEADEREND:event_cmbSpectrumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSpectrumActionPerformed

    private void cmbLevelItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbLevelItemStateChanged
    {//GEN-HEADEREND:event_cmbLevelItemStateChanged
        doSelectStarType();
    }//GEN-LAST:event_cmbLevelItemStateChanged

    private void scrTempretureAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scrTempretureAdjustmentValueChanged
    {//GEN-HEADEREND:event_scrTempretureAdjustmentValueChanged
        showScrLables();
    }//GEN-LAST:event_scrTempretureAdjustmentValueChanged

    private void scrMassAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scrMassAdjustmentValueChanged
    {//GEN-HEADEREND:event_scrMassAdjustmentValueChanged
        showScrLables();
    }//GEN-LAST:event_scrMassAdjustmentValueChanged

    private void cmbConstellationActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbConstellationActionPerformed
    {//GEN-HEADEREND:event_cmbConstellationActionPerformed
        constComboSelected();
    }//GEN-LAST:event_cmbConstellationActionPerformed

    private void cmbRegionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbRegionActionPerformed
    {//GEN-HEADEREND:event_cmbRegionActionPerformed
        makeConstCombo();
        constComboSelected();
    }//GEN-LAST:event_cmbRegionActionPerformed

    private void cmbLevelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbLevelActionPerformed
    {//GEN-HEADEREND:event_cmbLevelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbLevelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnCheckTag;
    private javax.swing.JButton btnGeneTag;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox<String> cmbConIndex;
    private javax.swing.JComboBox<String> cmbConstellation;
    private javax.swing.JComboBox<String> cmbLevel;
    private javax.swing.JComboBox<String> cmbRegion;
    private javax.swing.JComboBox<String> cmbSpectrum;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel labAbsMag;
    private javax.swing.JLabel labDensity;
    private javax.swing.JLabel labDia;
    private javax.swing.JLabel labGravity;
    private javax.swing.JLabel labGravity1;
    private javax.swing.JLabel labGravity2;
    private javax.swing.JLabel labLuminosity;
    private javax.swing.JLabel labMass;
    private javax.swing.JLabel labTemp;
    private javax.swing.JScrollBar scrMass;
    private javax.swing.JScrollBar scrTempreture;
    private javax.swing.JToolBar tbrTagTools;
    private javax.swing.JTextField txtCoord;
    private javax.swing.JTextArea txtDesp;
    private javax.swing.JTextField txtSteName;
    private javax.swing.JTextField txtTag;
    // End of variables declaration//GEN-END:variables
}
