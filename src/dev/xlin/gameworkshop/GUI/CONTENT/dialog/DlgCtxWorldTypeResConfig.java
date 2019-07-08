package dev.xlin.gameworkshop.GUI.CONTENT.dialog;

import dev.xlin.gameworkshop.GUI.CONTENT.ctxGuiUtils;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxBaseResource;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldCfgResItem;
import dev.xlin.gameworkshop.progs.contents.progs.BaseResourceDefine;
import dev.xlin.gameworkshop.progs.contents.progs.WorldTypeConfigResource;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.awt.Toolkit;
import java.util.Random;

/**
 *
 * @author 刘祎鹏
 */
public class DlgCtxWorldTypeResConfig extends javax.swing.JDialog
{

    private BeanCtxWorldCfgResItem bean = null;
    private wakeup up = null;
    private int cfgOID = 0;
    private boolean bOK = false;
    private boolean bEdit = false;
    private int resOID = 0;

    public DlgCtxWorldTypeResConfig(java.awt.Frame parent, boolean modal, wakeup _up, BeanCtxWorldCfgResItem _bean, int _cfgID)
    {
        super(parent, modal);
        initComponents();
        up = _up;
        bean = _bean;
        cfgOID = _cfgID;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        setIconImage(frmMain.getImageIcon());
        if (bean == null)
        {
            bean = new BeanCtxWorldCfgResItem();
            resOID = 0;
            txtRes.setText(showRes());
            randomCaps();
            bEdit = false;
            showScrs();
        }
        else
        {
            bEdit = true;
            resOID = bean.getResOID();
            txtRes.setText(showRes());
            txtDesp.setText(bean.getCfgDescp());
            txtCapMax.setText(bean.getCapMax() + "");
            txtCapMin.setText(bean.getCapMin() + "");
            scbCntMax.setValue(bean.getCountMax());
            scbCntMin.setValue(bean.getCountMin());
            scbDiffMax.setValue((int) (bean.getDiffcMax() * 10000.0));
            scbDiffMin.setValue((int) (bean.getDiffcMin() * 10000.0));
            scbEffMax.setValue((int) (bean.getEffiMax() * 10000.0));
            scbEffMin.setValue((int) (bean.getEffiMin() * 10000.0));
            scbProb.setValue((int) (bean.getProbability() * 10000));
            scbVisb.setValue((int) (bean.getDefaultVisb() * 10000));
            scbSurveyMin.setValue((int) (bean.getSurveyMin() * 10000));
            scbSurveyMax.setValue((int) (bean.getSurveyMax() * 10000));
            scbDangerMax.setValue((int) (bean.getDangerMax() * 10000));
            scbDangerMin.setValue((int) (bean.getDangerMin() * 10000));
            txtRes.setEnabled(false);
            showScrs();
        }
    }

    private void showScrs()
    {
        labCntMax.setText(scbCntMax.getValue() + "");
        labCntMin.setText(scbCntMin.getValue() + "");
        labDiffMax.setText(scbDiffMax.getValue() / 100.0 + "%");
        labDiffMin.setText(scbDiffMin.getValue() / 100.0 + "%");
        labEffMax.setText(scbEffMax.getValue() / 100.0 + "%");
        labEffMin.setText(scbEffMin.getValue() / 100.0 + "%");
        labProb.setText(scbProb.getValue() / 100.0 + "%");
        labVisb.setText(scbVisb.getValue() / 100.0 + "%");
        labSurveyMin.setText(scbSurveyMin.getValue() / 100.0 + "%");
        labSurveyMax.setText(scbSurveyMax.getValue() / 100.0 + "%");
        labDangerMax.setText(scbDangerMax.getValue() / 100.0 + "%");
        labDangerMin.setText(scbDangerMin.getValue() / 100.0 + "%");
    }

    private void selectRes()
    {
        if (bEdit)
        {
            return;
        }
        myTreeNode mtn = ctxGuiUtils.selectBaseResource(up);
        System.err.println(" mtn = " + mtn);
        if (mtn == null)
        {
            txtRes.setText("-");
            resOID = 0;
            return;
        }
        else
        {
            resOID = mtn.getNodeOID();
            txtRes.setText(showRes());
            txtDesp.setText(txtRes.getText());
        }
    }

    private String showRes()
    {
        BaseResourceDefine brd = new BaseResourceDefine(up);
        BeanCtxBaseResource bbr = (BeanCtxBaseResource) brd.getRecordByID(resOID);
        if (bbr != null)
        {
            return bbr.getResName();
        }
        else
        {
            return "-";
        }
    }

    private long randomCap(Random r)
    {
        while (true)
        {
            long l = r.nextInt();
            if (l > 0)
            {
                return l;
            }
        }
    }

    private void randomCaps()
    {
        BeanCtxWorldCfgResItem b = WorldTypeConfigResource.generateResItemData();
        txtCapMax.setText(b.getCapMax() + "");
        txtCapMin.setText(b.getCapMin() + "");
        scbDiffMax.setValue((int) (b.getDiffcMax() * 10000));
        scbDiffMin.setValue((int) (b.getDiffcMin() * 10000));
        scbEffMax.setValue((int) (b.getEffiMax() * 10000));
        scbEffMin.setValue((int) (b.getEffiMin() * 10000));
        scbSurveyMax.setValue((int) (b.getSurveyMax() * 10000));
        scbSurveyMin.setValue((int) (b.getSurveyMin() * 10000));
        scbDangerMax.setValue((int) (b.getDangerMax() * 10000));
        scbDangerMin.setValue((int) (b.getDangerMin() * 10000));
        scbCntMax.setValue(b.getCountMax());
        scbCntMin.setValue(b.getCountMin());
        scbVisb.setValue((int) (b.getDefaultVisb() * 10000));
        scbProb.setValue((int) (b.getProbability() * 10000));
        showScrs();
    }

    private void OK()
    {
        if (resOID == 0)
        {
            fast.warn("必须选择一个资源");
            return;
        }
        long lcmx = fast.testLongText(txtCapMax);
        if (lcmx < 0)
        {
            fast.warn("容量数值错误");
            return;
        }
        long lcmi = fast.testLongText(txtCapMin);
        if (lcmi < 0)
        {
            fast.warn("容量数值错误");
            return;
        }
        bean.setResOID(resOID);
        bean.setCapMax(lcmx);
        bean.setCapMin(lcmi);
        bean.setCfgDescp(txtDesp.getText().trim());
        bean.setCountMax(scbCntMax.getValue());
        bean.setCountMin(scbCntMin.getValue());
        bean.setDiffcMax((double) (scbDiffMax.getValue() / 10000.0));
        bean.setDiffcMin((double) (scbDiffMin.getValue() / 10000.0));
        bean.setEffiMax((double) (scbEffMax.getValue() / 10000.0));
        bean.setEffiMin((double) (scbEffMin.getValue() / 10000.0));
        bean.setProbability((double) (scbProb.getValue() / 10000.0));
        bean.setDefaultVisb((double) (scbVisb.getValue() / 10000.0));
        bean.setSurveyMax((double) (scbSurveyMax.getValue() / 10000.0));
        bean.setSurveyMin((double) (scbSurveyMin.getValue() / 10000.0));
        bean.setDangerMax((double) (scbDangerMax.getValue() / 10000.0));
        bean.setDangerMin((double) (scbDangerMin.getValue() / 10000.0));
        int r = 0;
        WorldTypeConfigResource wtcr = new WorldTypeConfigResource(up);
        if (bEdit == false)
        {
            bean.setCfgOID(cfgOID);
            r = wtcr.createRecord(bean, false);
        }
        else
        {
            r = wtcr.modifyRecord(bean, false);
        }
        if (r == iDAO.OPERATE_SUCCESS)
        {
            bOK = true;
            setVisible(false);
        }
        else
        {
            fast.err("配置数据操作失败", r);
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancle = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        txtRes = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDesp = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        scbProb = new javax.swing.JScrollBar();
        labProb = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCapMin = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCapMax = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        scbDiffMin = new javax.swing.JScrollBar();
        labDiffMin = new javax.swing.JLabel();
        scbDiffMax = new javax.swing.JScrollBar();
        labDiffMax = new javax.swing.JLabel();
        scbEffMin = new javax.swing.JScrollBar();
        labEffMin = new javax.swing.JLabel();
        scbEffMax = new javax.swing.JScrollBar();
        labEffMax = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        scbCntMin = new javax.swing.JScrollBar();
        labCntMin = new javax.swing.JLabel();
        scbCntMax = new javax.swing.JScrollBar();
        labCntMax = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnRanCap = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        scbVisb = new javax.swing.JScrollBar();
        labVisb = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        scbSurveyMin = new javax.swing.JScrollBar();
        labSurveyMin = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        scbSurveyMax = new javax.swing.JScrollBar();
        labSurveyMax = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        scbDangerMin = new javax.swing.JScrollBar();
        labDangerMin = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        scbDangerMax = new javax.swing.JScrollBar();
        labDangerMax = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("资源点配置");
        setResizable(false);

        btnCancle.setText("取消");
        btnCancle.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancleActionPerformed(evt);
            }
        });

        jLabel1.setText("资源选择");

        txtRes.setEditable(false);
        txtRes.setForeground(new java.awt.Color(0, 0, 255));
        txtRes.setText("- 点击选择 -");
        txtRes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtRes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtRes.setFocusable(false);
        txtRes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                txtResMouseReleased(evt);
            }
        });

        jLabel2.setText("配置描述");

        txtDesp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel3.setText("出现概率");

        scbProb.setMaximum(10000);
        scbProb.setMinimum(1);
        scbProb.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbProb.setValue(5000);
        scbProb.setVisibleAmount(0);
        scbProb.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbProbAdjustmentValueChanged(evt);
            }
        });

        labProb.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labProb.setText("50");

        jLabel5.setText("最小储量");

        txtCapMin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel6.setText("最大储量");

        txtCapMax.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel7.setText("最大难度");

        jLabel8.setText("最小难度");

        jLabel9.setText("最小效率");

        jLabel10.setText("最大效率");

        scbDiffMin.setMaximum(10000);
        scbDiffMin.setMinimum(1);
        scbDiffMin.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbDiffMin.setValue(3000);
        scbDiffMin.setVisibleAmount(0);
        scbDiffMin.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbDiffMinAdjustmentValueChanged(evt);
            }
        });

        labDiffMin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labDiffMin.setText("30");

        scbDiffMax.setMaximum(10000);
        scbDiffMax.setMinimum(1);
        scbDiffMax.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbDiffMax.setValue(6000);
        scbDiffMax.setVisibleAmount(0);
        scbDiffMax.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbDiffMaxAdjustmentValueChanged(evt);
            }
        });

        labDiffMax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labDiffMax.setText("60");

        scbEffMin.setMaximum(10000);
        scbEffMin.setMinimum(1);
        scbEffMin.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbEffMin.setValue(3000);
        scbEffMin.setVisibleAmount(0);
        scbEffMin.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbEffMinAdjustmentValueChanged(evt);
            }
        });

        labEffMin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labEffMin.setText("30");

        scbEffMax.setMaximum(10000);
        scbEffMax.setMinimum(1);
        scbEffMax.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbEffMax.setValue(6000);
        scbEffMax.setVisibleAmount(0);
        scbEffMax.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbEffMaxAdjustmentValueChanged(evt);
            }
        });

        labEffMax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labEffMax.setText("60");

        jLabel15.setText("最小数量");

        jLabel16.setText("最大数量");

        scbCntMin.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbCntMin.setValue(1);
        scbCntMin.setVisibleAmount(0);
        scbCntMin.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbCntMinAdjustmentValueChanged(evt);
            }
        });

        labCntMin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labCntMin.setText("1");

        scbCntMax.setMinimum(1);
        scbCntMax.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbCntMax.setValue(5);
        scbCntMax.setVisibleAmount(0);
        scbCntMax.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbCntMaxAdjustmentValueChanged(evt);
            }
        });

        labCntMax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labCntMax.setText("5");

        btnOK.setText("确定");
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOKActionPerformed(evt);
            }
        });

        btnRanCap.setText("随机");
        btnRanCap.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRanCapActionPerformed(evt);
            }
        });

        jLabel4.setText("可见比例");

        scbVisb.setMaximum(10000);
        scbVisb.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbVisb.setValue(4000);
        scbVisb.setVisibleAmount(0);
        scbVisb.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbVisbAdjustmentValueChanged(evt);
            }
        });

        labVisb.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labVisb.setText("40");

        jLabel11.setText("勘测下限");

        scbSurveyMin.setMaximum(10000);
        scbSurveyMin.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbSurveyMin.setValue(2000);
        scbSurveyMin.setVisibleAmount(0);
        scbSurveyMin.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbSurveyMinAdjustmentValueChanged(evt);
            }
        });

        labSurveyMin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labSurveyMin.setText("20");

        jLabel12.setText("勘测上限");

        scbSurveyMax.setMaximum(10000);
        scbSurveyMax.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbSurveyMax.setValue(8000);
        scbSurveyMax.setVisibleAmount(0);
        scbSurveyMax.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbSurveyMaxAdjustmentValueChanged(evt);
            }
        });

        labSurveyMax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labSurveyMax.setText("80");

        jLabel13.setText("危险下限");

        scbDangerMin.setMaximum(10000);
        scbDangerMin.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbDangerMin.setValue(2000);
        scbDangerMin.setVisibleAmount(0);
        scbDangerMin.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbDangerMinAdjustmentValueChanged(evt);
            }
        });

        labDangerMin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labDangerMin.setText("20");

        jLabel14.setText("危险下限");

        scbDangerMax.setMaximum(10000);
        scbDangerMax.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scbDangerMax.setValue(8000);
        scbDangerMax.setVisibleAmount(0);
        scbDangerMax.addAdjustmentListener(new java.awt.event.AdjustmentListener()
        {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
            {
                scbDangerMaxAdjustmentValueChanged(evt);
            }
        });

        labDangerMax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labDangerMax.setText("80");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRes))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDesp))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scbProb, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labProb, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scbEffMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labEffMax, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scbDiffMin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labDiffMin, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scbDiffMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labDiffMax, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scbEffMin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labEffMin, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scbCntMin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labCntMin, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCapMax))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCapMin)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRanCap))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scbDangerMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scbDangerMin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scbSurveyMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scbSurveyMin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scbCntMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scbVisb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labDangerMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labDangerMin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labSurveyMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labSurveyMin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labVisb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labCntMax, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtRes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtCapMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(txtCapMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel6))
                    .addComponent(btnRanCap))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(scbProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labProb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(scbDiffMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labDiffMin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(scbDiffMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labDiffMax))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(scbEffMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labEffMin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(scbEffMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labEffMax))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(scbCntMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labCntMin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(scbCntMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labCntMax))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(scbVisb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labVisb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(scbSurveyMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labSurveyMin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(scbSurveyMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labSurveyMax))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(scbDangerMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labDangerMin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(scbDangerMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labDangerMax))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancle)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel3, labProb, scbProb});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel10, jLabel7, jLabel8, jLabel9, labDiffMax, labDiffMin, labEffMax, labEffMin, scbDiffMax, scbDiffMin, scbEffMax, scbEffMin});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel15, jLabel16, labCntMax, labCntMin, scbCntMax, scbCntMin});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void scbProbAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbProbAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbProbAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbProbAdjustmentValueChanged

    private void scbDiffMinAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbDiffMinAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbDiffMinAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbDiffMinAdjustmentValueChanged

    private void scbDiffMaxAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbDiffMaxAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbDiffMaxAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbDiffMaxAdjustmentValueChanged

    private void scbEffMinAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbEffMinAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbEffMinAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbEffMinAdjustmentValueChanged

    private void scbEffMaxAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbEffMaxAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbEffMaxAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbEffMaxAdjustmentValueChanged

    private void scbCntMinAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbCntMinAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbCntMinAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbCntMinAdjustmentValueChanged

    private void scbCntMaxAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbCntMaxAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbCntMaxAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbCntMaxAdjustmentValueChanged

    private void txtResMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtResMouseReleased
    {//GEN-HEADEREND:event_txtResMouseReleased
        selectRes();
    }//GEN-LAST:event_txtResMouseReleased

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        OK();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleActionPerformed
    {//GEN-HEADEREND:event_btnCancleActionPerformed
        cancle();
    }//GEN-LAST:event_btnCancleActionPerformed

    private void btnRanCapActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRanCapActionPerformed
    {//GEN-HEADEREND:event_btnRanCapActionPerformed
        randomCaps();
    }//GEN-LAST:event_btnRanCapActionPerformed

    private void scbVisbAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbVisbAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbVisbAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbVisbAdjustmentValueChanged

    private void scbSurveyMinAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbSurveyMinAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbSurveyMinAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbSurveyMinAdjustmentValueChanged

    private void scbSurveyMaxAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbSurveyMaxAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbSurveyMaxAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbSurveyMaxAdjustmentValueChanged

    private void scbDangerMinAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbDangerMinAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbDangerMinAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbDangerMinAdjustmentValueChanged

    private void scbDangerMaxAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)//GEN-FIRST:event_scbDangerMaxAdjustmentValueChanged
    {//GEN-HEADEREND:event_scbDangerMaxAdjustmentValueChanged
        showScrs();
    }//GEN-LAST:event_scbDangerMaxAdjustmentValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnRanCap;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labCntMax;
    private javax.swing.JLabel labCntMin;
    private javax.swing.JLabel labDangerMax;
    private javax.swing.JLabel labDangerMin;
    private javax.swing.JLabel labDiffMax;
    private javax.swing.JLabel labDiffMin;
    private javax.swing.JLabel labEffMax;
    private javax.swing.JLabel labEffMin;
    private javax.swing.JLabel labProb;
    private javax.swing.JLabel labSurveyMax;
    private javax.swing.JLabel labSurveyMin;
    private javax.swing.JLabel labVisb;
    private javax.swing.JScrollBar scbCntMax;
    private javax.swing.JScrollBar scbCntMin;
    private javax.swing.JScrollBar scbDangerMax;
    private javax.swing.JScrollBar scbDangerMin;
    private javax.swing.JScrollBar scbDiffMax;
    private javax.swing.JScrollBar scbDiffMin;
    private javax.swing.JScrollBar scbEffMax;
    private javax.swing.JScrollBar scbEffMin;
    private javax.swing.JScrollBar scbProb;
    private javax.swing.JScrollBar scbSurveyMax;
    private javax.swing.JScrollBar scbSurveyMin;
    private javax.swing.JScrollBar scbVisb;
    private javax.swing.JTextField txtCapMax;
    private javax.swing.JTextField txtCapMin;
    private javax.swing.JTextField txtDesp;
    private javax.swing.JTextField txtRes;
    // End of variables declaration//GEN-END:variables
}
