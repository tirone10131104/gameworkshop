package dev.xlin.gameworkshop.GUI.CONTENT;

import dev.xlin.gameworkshop.GUI.CONTENT.dialog.DlgCtxCelestialInfo;
import dev.xlin.gameworkshop.GUI.CONTENT.dialog.DlgCtxConstellation;
import dev.xlin.gameworkshop.GUI.CONTENT.dialog.DlgCtxResSource;
import dev.xlin.gameworkshop.GUI.CONTENT.dialog.DlgCtxStellarData;
import dev.xlin.gameworkshop.GUI.CONTENT.dialog.DlgCtxStellarRegion;
import dev.xlin.gameworkshop.GUI.MDIPaneControl;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.iMDIFrameFeature;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxBaseResource;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxCelestialWorld;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxConstellation;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxMineSource;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxStellarData;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxStellarRegion;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldCfgResItem;
import dev.xlin.gameworkshop.progs.contents.beans.BeanCtxWorldResSource;
import dev.xlin.gameworkshop.progs.contents.progs.BaseResourceDefine;
import dev.xlin.gameworkshop.progs.contents.progs.CelestialWorldData;
import dev.xlin.gameworkshop.progs.contents.progs.ConstellationData;
import dev.xlin.gameworkshop.progs.contents.progs.ctxConst;
import dev.xlin.gameworkshop.progs.contents.progs.ctxTranslate;
import dev.xlin.gameworkshop.progs.contents.progs.StellarData;
import dev.xlin.gameworkshop.progs.contents.progs.StellarRegion;
import dev.xlin.gameworkshop.progs.contents.progs.WorldResSource;
import dev.xlin.gameworkshop.progs.contents.progs.WorldTypeConfigResource;
import dev.xlin.swingTools2.dlgTools.dlgSelectListItem;
import dev.xlin.swingTools2.dlgTools.dlgSelectTreeNode;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myTableModel;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class ifrmGlaxyEditor extends javax.swing.JInternalFrame implements iMDIFrameFeature
{

    private wakeup up = null;
    private int glxid = 0;
    private JDesktopPane desk = null;
    public static final int NODE_REGION_ROOT = 10;
    public static final int NODE_REGION_DATA = 11;
    public static final int NODE_STELLAR_ROOT = 20;
    public static final int NODE_STELLAR_DATA = 21;
    public static final int NODE_WLD_ROOT = 30;
    public static final int NODE_WLD_PLANET = 31;
    public static final int NODE_WLD_MOON = 32;
    public static final int NODE_WLD_ASTEROID = 33;
    public static final int NODE_CONS_DATA = 25;

    private List lregs = null;
    private HashMap hconss = null;
    private HashMap hstes = new HashMap();
    //资源定义数据列
    private List lress = null;
    //资源点数据列
    private List lreSrcs = null;
    //资源配置数据列
    private List rcfgItems = null;

    public ifrmGlaxyEditor(JDesktopPane _desk, wakeup _up, int _glxid)
    {
        initComponents();
        up = _up;
        glxid = _glxid;
        desk = _desk;
        initGUI();
    }

    private void initGUI()
    {
        makeRegionsTree();
        makeStellarTree();
        makeStellarSystemTree();
        makeRessTable();
        this.setLocation(100, 30);
//        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
//                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
    }

    private void makeRegionsTree()
    {
        myTreeNode mtn = new myTreeNode("[星域数据库]", 0, NODE_REGION_ROOT);
        StellarRegion sr = new StellarRegion(up);
        ConstellationData csd = new ConstellationData(up);
        lregs = sr.getRegionsByGlaxy(glxid);
        hconss = new HashMap();
        if (lregs != null)
        {
            for (int i = 0; i < lregs.size(); i++)
            {
                BeanCtxStellarRegion bcsr = (BeanCtxStellarRegion) lregs.get(i);
                myTreeNode msr = new myTreeNode(bcsr.getRegionName(), bcsr.getOID(), NODE_REGION_DATA);
                List lcs = csd.queryConstellationsByRegion(bcsr.getOID());
                if (lcs != null)
                {
                    for (int j = 0; j < lcs.size(); j++)
                    {
                        BeanCtxConstellation bcct = (BeanCtxConstellation) lcs.get(j);
                        hconss.put(bcct.getOID(), bcct);
                        myTreeNode mcst = new myTreeNode(bcct.getConName(), bcct.getOID(), NODE_CONS_DATA);
                        msr.add(mcst);
                    }
                }
                mtn.add(msr);
            }
        }
        DefaultTreeModel dtm = new DefaultTreeModel(mtn);
        trRegions.setModel(dtm);
        guiCommon.expandTree(trRegions);
    }

    private void makeStellarTree()
    {
        myTreeNode mtn = new myTreeNode("[恒星数据库]", 0, NODE_STELLAR_ROOT);
        BeanCtxConstellation bccs = getSelectedConstellation();
        System.err.println(".bccs = " + bccs);
        hstes.clear();
        if (bccs != null)
        {
            StellarData std = new StellarData(up);
            List lsd = std.getStellarsByRegion(bccs.getRegionOID(), bccs.getOID());
            if (lsd != null)
            {
                System.err.println("lsd.siz = " + lsd.size());
                for (int i = 0; i < lsd.size(); i++)
                {
                    BeanCtxStellarData bsd = (BeanCtxStellarData) lsd.get(i);
                    myTreeNode msd = new myTreeNode(bsd.getStName() + "<" + bsd.getStTag() + ">" + " [" + ctxTranslate.transGreekCharactor(bsd.getConIndex()) + "(" + (bsd.getConIndex() + 1) + ")" + "]", bsd.getOID(), NODE_STELLAR_DATA);
                    mtn.add(msd);
                    hstes.put(bsd.getOID(), bsd);
                }
            }
        }
        DefaultTreeModel dtm = new DefaultTreeModel(mtn);
        trStes.setModel(dtm);
    }

    private void makeStellarSystemTree()
    {
        myTreeNode mtn = new myTreeNode("[恒星系数据库]", 0, NODE_WLD_ROOT);
        DefaultTreeModel dtm = new DefaultTreeModel(mtn);
        BeanCtxStellarData bst = getSelectedStellarData();
        if (bst != null)
        {
            CelestialWorldData cwd = new CelestialWorldData(up);
            List ls = cwd.getCelestialsByParent(bst.getOID(), 0, 0);
            if (ls != null)
            {
                for (int i = 0; i < ls.size(); i++)
                {
                    BeanCtxCelestialWorld bccw = (BeanCtxCelestialWorld) ls.get(i);
                    dMakeWorldTree(bccw, mtn, cwd);
                }
            }
        }
        trWorlds.setModel(dtm);
        guiCommon.expandTree(trWorlds);
    }

    private void dMakeWorldTree(BeanCtxCelestialWorld bccw, myTreeNode mpar, CelestialWorldData cwd)
    {
        String swtp = "";
        int wtpNtp = 0;
        if (bccw.getWorldType() == ctxConst.WORLD_TYPE_MOON)
        {
            wtpNtp = NODE_WLD_MOON;
            swtp = "[卫星]";
        }
        else if (bccw.getWorldType() == ctxConst.WORLD_TYPE_PLANET)
        {
            wtpNtp = NODE_WLD_PLANET;
            swtp = "[行星]";
        }
        else if (bccw.getWorldType() == ctxConst.WORLD_TYPE_ASTEROID)
        {
            wtpNtp = NODE_WLD_ASTEROID;
            swtp = "[小行星]";
        }
        myTreeNode mtn = new myTreeNode(swtp + bccw.getWorldName(), bccw.getOID(), wtpNtp);
        List lws = cwd.getCelestialsByParent(bccw.getStellarOID(), bccw.getOID(), 0);
        if (lws != null)
        {
            for (int i = 0; i < lws.size(); i++)
            {
                BeanCtxCelestialWorld bwld = (BeanCtxCelestialWorld) lws.get(i);
                dMakeWorldTree(bwld, mtn, cwd);
            }
        }
        mpar.add(mtn);
    }

    private void makeRessTable()
    {
        myTableModel mtm = new myTableModel();
        mtm.addColumn("RES");
        mtm.addColumn("CAP");
        mtm.addColumn("DIF");
        mtm.addColumn("EFF");
        mtm.addColumn("DIC");
        mtm.addColumn("EDC");
        mtm.addColumn("DAG");
        BeanCtxCelestialWorld bccw = getSelectedWorld();
        if (bccw != null)
        {
            WorldResSource wrs = new WorldResSource(up);
            int idx = cmbWldRes.getSelectedIndex();
            if (idx >= 0)
            {
                int rsid = 0;
                int cid = 0;
                if (idx > 0)
                {
                    BeanCtxBaseResource bres = (BeanCtxBaseResource) lress.get(idx - 1);
                    rsid = bres.getOID();
                    int cidx = cmbCfgItem.getSelectedIndex();
                    if (cidx > 0)
                    {
                        listItem li = (listItem) cmbCfgItem.getSelectedItem();
                        int cfgid = li.getNodeOID();
                        BeanCtxWorldCfgResItem bri = findCfgItemInTempList(cfgid);
                        cid = bri.getOID();
                    }
                } 
                lreSrcs = wrs.getResSourceByWorld(bccw.getOID(), rsid, cid);
                if (lreSrcs != null)
                {
                    for (int i = 0; i < lreSrcs.size(); i++)
                    {
                        BeanCtxWorldResSource bsrc = (BeanCtxWorldResSource) lreSrcs.get(i);
                        Object[] row = new Object[7];
                        BeanCtxBaseResource brs = findBaseResInTempList(bsrc.getResOID());
                        row[0] = brs.getResName();
                        row[1] = bsrc.getCapability();
                        row[2] = fast.makeRound2(bsrc.getDifficulty() * 100) + "%";
                        row[3] = fast.makeRound2(bsrc.getEfficiency() * 100) + "%";
                        row[4] = fast.makeRound2(bsrc.getDiffIncrs() * 100) + "%";
                        row[5] = fast.makeRound2(bsrc.getEffDecln() * 100) + "%";
                        row[6] = fast.makeRound2(bsrc.getDanger() *100) +"%";
                        mtm.addRow(row);
                    }
                }
            }
        }
        tbRess.setModel(mtm);
    }

    private BeanCtxWorldCfgResItem findCfgItemInTempList (int cid )
    {
        if (rcfgItems!= null)
        {
            for (int i = 0; i < rcfgItems.size(); i++)
            {
                BeanCtxWorldCfgResItem bri = (BeanCtxWorldCfgResItem) rcfgItems.get(i);
                if (bri.getOID() == cid )
                {
                    return bri;
                }
            }
        }
        return null;
    }
    
    private BeanCtxBaseResource findBaseResInTempList(int rsid)
    {
        for (int i = 0; i < lress.size(); i++)
        {
            BeanCtxBaseResource bres = (BeanCtxBaseResource) lress.get(i);
            if (bres.getOID() == rsid)
            {
                return bres;
            }
        }
        return null;
    }

    private void newRegion()
    {
        DlgCtxStellarRegion dlg = new DlgCtxStellarRegion(null, true, up, null, glxid);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeRegionsTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanCtxStellarRegion getSelectedRegion()
    {
        TreePath tph = trRegions.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() != NODE_REGION_DATA)
        {
            return null;
        }
        int oid = mtn.getNodeOID();
        if (oid == 0)
        {
            return null;
        }
        return findRegion(oid);
    }

    private BeanCtxStellarRegion findRegion(int oid)
    {
        if (lregs != null)
        {
            for (int i = 0; i < lregs.size(); i++)
            {
                BeanCtxStellarRegion bean = (BeanCtxStellarRegion) lregs.get(i);
                if (bean.getOID() == oid)
                {
                    return bean;
                }
            }
        }
        return null;
    }

    private void editRegion()
    {
        BeanCtxStellarRegion bean = getSelectedRegion();
        if (bean == null)
        {
            return;
        }
        DlgCtxStellarRegion dlg = new DlgCtxStellarRegion(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeRegionsTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void removeRegion()
    {
        BeanCtxStellarRegion bean = getSelectedRegion();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确定删除[" + bean.getRegionName() + "]这个星域？");
        if (sel != fast.YES)
        {
            return;
        }
        StellarRegion srg = new StellarRegion(up);
        int r = srg.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeRegionsTree();
        }
        else
        {
            fast.err("删除操作失败", r);
        }
    }

    private void newCons()
    {
        BeanCtxStellarRegion bcsr = getSelectedRegion();
        if (bcsr == null)
        {
            return;
        }
        DlgCtxConstellation dlg = new DlgCtxConstellation(null, true, up, null, bcsr.getOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeRegionsTree();
        }
        dlg.dispose();
        dlg = null;
    }

    public BeanCtxConstellation getSelectedConstellation()
    {
        TreePath tph = trRegions.getSelectionPath();
        if (tph == null)
        {
            System.err.println("..getSelectedConstellation . tph = null ");
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() != NODE_CONS_DATA)
        {
            System.err.println("..getSelectedConstellation.. not cons ");
            return null;
        }
        return (BeanCtxConstellation) hconss.get(mtn.getNodeOID());
    }

    private void editCons()
    {
        BeanCtxConstellation bean = getSelectedConstellation();
        if (bean == null)
        {
            return;
        }
        DlgCtxConstellation dlg = new DlgCtxConstellation(null, true, up, bean, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeRegionsTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void removeCons()
    {
        BeanCtxConstellation bean = getSelectedConstellation();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认要删除这个星座节点数据？");
        if (sel != fast.YES)
        {
            return;
        }
        ConstellationData cdt = new ConstellationData(up);
        int r = cdt.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeRegionsTree();
        }
        else
        {
            fast.err("删除操作失败", r);
        }
    }

    private void moveConsToRegion()
    {
        BeanCtxConstellation bean = getSelectedConstellation();
        if (bean == null)
        {
            return;
        }
        StellarRegion strg = new StellarRegion(up);
        List lrs = strg.getRegionsByGlaxy(this.glxid);
        List ls = new ArrayList();
        for (int i = 0; i < lrs.size(); i++)
        {
            BeanCtxStellarRegion bcsr = (BeanCtxStellarRegion) lrs.get(i);
            if (bcsr.getOID() == bean.getRegionOID())
            {
                continue;
            }
            listItem li = new listItem(bcsr.getRegionName() + "<" + bcsr.getRegionTag() + ">", bcsr.getOID());
            ls.add(li);
        }
        dlgSelectListItem dlg = new dlgSelectListItem(null, true, ls, 0);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            listItem lsel = dlg.getSelectedListItem();
            int NRID = lsel.getNodeOID();
            ConstellationData csdt = new ConstellationData(up);
            int r = csdt.moveToRegion(bean.getOID(), NRID);
            if (r != iDAO.OPERATE_SUCCESS)
            {
                fast.err("移动操作失败", r);
            }
            else
            {
                makeRegionsTree();
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void newStellar()
    {
        BeanCtxConstellation bcon = getSelectedConstellation();
        int consid = 0;
        if (bcon != null)
        {
            consid = bcon.getOID();
        }
        DlgCtxStellarData dlg = new DlgCtxStellarData(null, true, up, null, glxid, consid);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeStellarTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void regionSelected()
    {
        System.err.println(".regionSelected");
        makeStellarTree();
    }

    private void stellarNodeSelected()
    {
        makeStellarSystemTree();
    }

    private BeanCtxStellarData getSelectedStellarData()
    {
        TreePath tph = trStes.getSelectionPath();
        if (tph == null)
        {
            System.err.println("tph == null ");
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() != NODE_STELLAR_DATA)
        {
            System.err.println("..not ste data ");
            return null;
        }
        return (BeanCtxStellarData) hstes.get(mtn.getNodeOID());
    }

    private void editStellarData()
    {
        BeanCtxStellarData obean = getSelectedStellarData();
        if (obean == null)
        {
            return;
        }
        DlgCtxStellarData dlg = new DlgCtxStellarData(null, true, up, obean, glxid, obean.getConOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeStellarTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void removeStellarData()
    {
        BeanCtxStellarData obean = getSelectedStellarData();
        if (obean == null)
        {
            return;
        }
        int sel = fast.ask("是否确认删除这个恒星？\n如果恒星下还有任何数据，或者被引用，删除操作都会被自动阻止。\n\n请确认？");
        if (sel != fast.YES)
        {
            return;
        }
        StellarData sdt = new StellarData(up);
        int r = sdt.deleteRecord(obean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeStellarTree();
        }
        else
        {
            fast.err("删除操作失败", r);
        }
    }

    private void moveStellarUp()
    {
        BeanCtxStellarData obean = getSelectedStellarData();
        if (obean == null)
        {
            return;
        }
        StellarData sdt = new StellarData(up);
        int r = sdt.moveStellarUp(obean.getOID());
        System.err.println("M UP = " + r);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeStellarTree();
        }
    }

    private void moveStellarDown()
    {
        BeanCtxStellarData obean = getSelectedStellarData();
        if (obean == null)
        {
            return;
        }
        StellarData sdt = new StellarData(up);
        int r = sdt.moveStellarDown(obean.getOID());
        System.err.println("M DOWN = " + r);
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeStellarTree();
        }
    }

    //执行恒星系移动到指定星座的操作
    private void moveSteToCons()
    {
        BeanCtxStellarData obean = getSelectedStellarData();
        if (obean == null)
        {
            return;
        }
        myTreeNode mtn = new myTreeNode("[星域数据库]", 0, 0);
        StellarRegion sr = new StellarRegion(up);
        ConstellationData csd = new ConstellationData(up);
        List _lrg = sr.getRegionsByGlaxy(glxid);
        if (_lrg != null)
        {
            for (int i = 0; i < _lrg.size(); i++)
            {
                BeanCtxStellarRegion bcsr = (BeanCtxStellarRegion) _lrg.get(i);
                myTreeNode msr = new myTreeNode(bcsr.getRegionName(), bcsr.getOID(), 0);
                List lcs = csd.queryConstellationsByRegion(bcsr.getOID());
                if (lcs != null)
                {
                    for (int j = 0; j < lcs.size(); j++)
                    {
                        BeanCtxConstellation bcct = (BeanCtxConstellation) lcs.get(j);
                        if (bcct.getOID() == obean.getConOID())
                        {
                            continue;
                        }
                        myTreeNode mcst = new myTreeNode("[星座]" + bcct.getConName(), bcct.getOID(), 1);
                        msr.add(mcst);
                    }
                }
                mtn.add(msr);
            }
        }
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mtn);
        dlg.setTitle("选择一个需要移动到的星座");
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            myTreeNode msel = dlg.getSelect();
            if (msel != null)
            {
                int ncid = msel.getNodeOID();
                StellarData sdt = new StellarData(up);
                int r = sdt.moveStellarToConstellation(obean.getOID(), ncid);
                if (r == iDAO.OPERATE_SUCCESS)
                {
                    makeStellarTree();
                }
                else
                {
                    fast.err("移动操作失败", r);
                }
            }
            else
            {
                fast.warn("请选择一个目标星座");
            }
        }
        dlg.dispose();
        dlg = null;
    }

    private void newWorld()
    {
        //获取STELLAR
        BeanCtxStellarData bstellar = getSelectedStellarData();
        if (bstellar == null)
        {
            fast.warn("必须选择一个恒星系");
            return;
        }
        //获取父节点 
        BeanCtxCelestialWorld bpar = getSelectedWorld();
        int pid = 0;
        if (bpar != null)
        {
            pid = bpar.getOID();
        }
        DlgCtxCelestialInfo dlg = new DlgCtxCelestialInfo(null, true, up, null, bstellar.getOID(), pid);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeStellarSystemTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanCtxCelestialWorld getSelectedWorld()
    {
        TreePath tph = trWorlds.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        int ntp = mtn.getNodeType();
        if (ntp == NODE_WLD_ROOT)
        {
            return null;
        }
        CelestialWorldData cwd = new CelestialWorldData(up);
        return (BeanCtxCelestialWorld) cwd.getRecordByID(mtn.getNodeOID());
    }

    private void editWorld()
    {
        BeanCtxCelestialWorld bean = getSelectedWorld();
        if (bean == null)
        {
            return;
        }
        DlgCtxCelestialInfo dlg = new DlgCtxCelestialInfo(null, true, up, bean, bean.getStellarOID(), bean.getParentOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeStellarSystemTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void removeWorld()
    {
        BeanCtxCelestialWorld bean = getSelectedWorld();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否删除这个天体？");
        if (sel != fast.YES)
        {
            return;
        }
        CelestialWorldData cwd = new CelestialWorldData(up);
        int r = cwd.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeStellarSystemTree();
        }
        else
        {
            fast.err("删除操作失败", r);
        }
    }

    private void appendResSource()
    {
        BeanCtxCelestialWorld bccw = getSelectedWorld();
        if (bccw == null)
        {
            return;
        }
        DlgCtxResSource dlg = new DlgCtxResSource(null, true, up, null, bccw.getOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeRessTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void preLoadWorldRss()
    {
        BeanCtxCelestialWorld bccw = getSelectedWorld();
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("<所有资源>", 0));
        if (bccw != null)
        {
            WorldTypeConfigResource wtcr = new WorldTypeConfigResource(up);
            rcfgItems = wtcr.getResourceListByConfig(bccw.getWorldTypeConfigOID());
            BaseResourceDefine brd = new BaseResourceDefine(up);
            lress = new ArrayList();
            if (rcfgItems != null)
            {
                for (int i = 0; i < rcfgItems.size(); i++)
                {
                    BeanCtxWorldCfgResItem bit = (BeanCtxWorldCfgResItem) rcfgItems.get(i);
                    if (findBaseResInTempList(bit.getResOID()) == null)
                    {
                        BeanCtxBaseResource bcbr = (BeanCtxBaseResource) brd.getRecordByID(bit.getResOID());
                        listItem li = new listItem(bcbr.getResName(), bcbr.getOID());
                        lress.add(bcbr);
                        mod.addElement(li);
                    }
                }
            }
        }
        cmbWldRes.setModel(mod);
        makeResCfgCombo();
        makeRessTable();
    }

    private void makeResCfgCombo()
    {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        mod.addElement(new listItem("<所有配置>", 0));
        int idx = cmbWldRes.getSelectedIndex();
        if (idx >  0)
        {
            BeanCtxBaseResource bres = (BeanCtxBaseResource) lress.get(idx-1);
            List ls = findResConfigItems(bres.getOID());
            if (ls != null)
            {
                for (int i = 0; i < ls.size(); i++)
                {
                    BeanCtxWorldCfgResItem bri = (BeanCtxWorldCfgResItem) ls.get(i);
                    listItem li = new listItem(bri.getCfgDescp() + "<" + bri.getCriTag() + ">", bri.getOID());
                    mod.addElement(li);
                }
            }
        }
        cmbCfgItem.setModel(mod);
    }

    private List findResConfigItems(int rsid)
    {
        List ls = new ArrayList();
        if (rcfgItems != null)
        {
            for (int i = 0; i < rcfgItems.size(); i++)
            {
                BeanCtxWorldCfgResItem bri = (BeanCtxWorldCfgResItem) rcfgItems.get(i);
                if (bri.getResOID() == rsid)
                {
                    ls.add(bri);
                }
            }
        }
        return ls;
    }

    private BeanCtxWorldResSource getSelectedSrc()
    {
        int idx = tbRess.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        return (BeanCtxWorldResSource) lreSrcs.get(idx);
    }

    private void editRess()
    {
        BeanCtxWorldResSource bsrc = getSelectedSrc();
        if (bsrc == null)
        {
            return;
        }
        DlgCtxResSource dlg = new DlgCtxResSource(null, true, up, bsrc, bsrc.getWorldOID());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeRessTable();
        }
        dlg.dispose();
        dlg = null;
    }

    private void removeRess()
    {
        BeanCtxWorldResSource bsrc = getSelectedSrc();
        if (bsrc == null)
        {
            return;
        }
        int sel = fast.ask("是否删除这个资源点数据？");
        if (sel != fast.YES)
        {
            return;
        }
        WorldResSource wrs = new WorldResSource(up);
        int r = wrs.deleteRecord(bsrc.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeRessTable();
            fast.msg("删除资源点数据操作成功");
        }
        else
        {
            fast.err("删除资源点数据操作失败", r);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popRegion = new javax.swing.JPopupMenu();
        miNewRegion = new javax.swing.JMenuItem();
        miEditRegion = new javax.swing.JMenuItem();
        miRemoveRegion = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miNewCons = new javax.swing.JMenuItem();
        miEditCons = new javax.swing.JMenuItem();
        miRemoveCons = new javax.swing.JMenuItem();
        miMoveCons = new javax.swing.JMenuItem();
        miNewStellar = new javax.swing.JMenuItem();
        popStellar = new javax.swing.JPopupMenu();
        miNewStellarNode = new javax.swing.JMenuItem();
        miEditStellar = new javax.swing.JMenuItem();
        miRemoveStellar = new javax.swing.JMenuItem();
        miMoveStellarUp = new javax.swing.JMenuItem();
        miMoveStellarDown = new javax.swing.JMenuItem();
        miMoveStellarToConstellation = new javax.swing.JMenuItem();
        popWorlds = new javax.swing.JPopupMenu();
        miNewWld = new javax.swing.JMenuItem();
        miEditWld = new javax.swing.JMenuItem();
        miRemoveWld = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miAppendResSrc = new javax.swing.JMenuItem();
        popRes = new javax.swing.JPopupMenu();
        miNewRess = new javax.swing.JMenuItem();
        miEditRess = new javax.swing.JMenuItem();
        miRemoveRess = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        palRegions = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        trRegions = new javax.swing.JTree();
        palStes = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        trStes = new javax.swing.JTree();
        jSplitPane3 = new javax.swing.JSplitPane();
        palUniMap = new javax.swing.JPanel();
        palUniEditor = new javax.swing.JPanel();
        splStellarStruct = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        scpRess = new javax.swing.JScrollPane();
        tbRess = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cmbWldRes = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cmbCfgItem = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        trWorlds = new javax.swing.JTree();

        miNewRegion.setText("新建星域");
        miNewRegion.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewRegionActionPerformed(evt);
            }
        });
        popRegion.add(miNewRegion);

        miEditRegion.setText("修改星域");
        miEditRegion.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditRegionActionPerformed(evt);
            }
        });
        popRegion.add(miEditRegion);

        miRemoveRegion.setText("删除星域");
        miRemoveRegion.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRemoveRegionActionPerformed(evt);
            }
        });
        popRegion.add(miRemoveRegion);
        popRegion.add(jSeparator1);

        miNewCons.setText("新建星座");
        miNewCons.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewConsActionPerformed(evt);
            }
        });
        popRegion.add(miNewCons);

        miEditCons.setText("修改星座");
        miEditCons.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditConsActionPerformed(evt);
            }
        });
        popRegion.add(miEditCons);

        miRemoveCons.setText("删除星座");
        miRemoveCons.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRemoveConsActionPerformed(evt);
            }
        });
        popRegion.add(miRemoveCons);

        miMoveCons.setText("移动星座到...");
        miMoveCons.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveConsActionPerformed(evt);
            }
        });
        popRegion.add(miMoveCons);

        miNewStellar.setText("新建恒星系");
        miNewStellar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewStellarActionPerformed(evt);
            }
        });
        popRegion.add(miNewStellar);

        miNewStellarNode.setText("创建恒星");
        miNewStellarNode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewStellarNodeActionPerformed(evt);
            }
        });
        popStellar.add(miNewStellarNode);

        miEditStellar.setText("修改信息");
        miEditStellar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditStellarActionPerformed(evt);
            }
        });
        popStellar.add(miEditStellar);

        miRemoveStellar.setText("删除恒星");
        miRemoveStellar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRemoveStellarActionPerformed(evt);
            }
        });
        popStellar.add(miRemoveStellar);

        miMoveStellarUp.setText("上移...");
        miMoveStellarUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveStellarUpActionPerformed(evt);
            }
        });
        popStellar.add(miMoveStellarUp);

        miMoveStellarDown.setText("下移...");
        miMoveStellarDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveStellarDownActionPerformed(evt);
            }
        });
        popStellar.add(miMoveStellarDown);

        miMoveStellarToConstellation.setText("移动至其他星座");
        miMoveStellarToConstellation.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMoveStellarToConstellationActionPerformed(evt);
            }
        });
        popStellar.add(miMoveStellarToConstellation);

        miNewWld.setText("创建天体");
        miNewWld.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewWldActionPerformed(evt);
            }
        });
        popWorlds.add(miNewWld);

        miEditWld.setText("修改天体");
        miEditWld.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditWldActionPerformed(evt);
            }
        });
        popWorlds.add(miEditWld);

        miRemoveWld.setText("删除天体");
        miRemoveWld.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRemoveWldActionPerformed(evt);
            }
        });
        popWorlds.add(miRemoveWld);
        popWorlds.add(jSeparator2);

        miAppendResSrc.setText("添加资源设置");
        miAppendResSrc.setToolTipText("");
        miAppendResSrc.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miAppendResSrcActionPerformed(evt);
            }
        });
        popWorlds.add(miAppendResSrc);

        miNewRess.setText("新建资源");
        miNewRess.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewRessActionPerformed(evt);
            }
        });
        popRes.add(miNewRess);

        miEditRess.setText("修改资源");
        miEditRess.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditRessActionPerformed(evt);
            }
        });
        popRes.add(miEditRess);

        miRemoveRess.setText("删除资源");
        miRemoveRess.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRemoveRessActionPerformed(evt);
            }
        });
        popRes.add(miRemoveRess);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("UNI");

        jSplitPane1.setDividerLocation(233);
        jSplitPane1.setDividerSize(4);
        jSplitPane1.setResizeWeight(0.2);

        jSplitPane2.setDividerLocation(233);
        jSplitPane2.setDividerSize(4);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(0.35);

        trRegions.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                trRegionsMouseClicked(evt);
            }
        });
        trRegions.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trRegionsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(trRegions);

        javax.swing.GroupLayout palRegionsLayout = new javax.swing.GroupLayout(palRegions);
        palRegions.setLayout(palRegionsLayout);
        palRegionsLayout.setHorizontalGroup(
            palRegionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );
        palRegionsLayout.setVerticalGroup(
            palRegionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );

        jSplitPane2.setTopComponent(palRegions);

        trStes.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                trStesMouseClicked(evt);
            }
        });
        trStes.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trStesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(trStes);

        javax.swing.GroupLayout palStesLayout = new javax.swing.GroupLayout(palStes);
        palStes.setLayout(palStesLayout);
        palStesLayout.setHorizontalGroup(
            palStesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );
        palStesLayout.setVerticalGroup(
            palStesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
        );

        jSplitPane2.setRightComponent(palStes);

        jSplitPane1.setLeftComponent(jSplitPane2);

        jSplitPane3.setDividerLocation(760);
        jSplitPane3.setDividerSize(4);
        jSplitPane3.setResizeWeight(0.85);

        palUniEditor.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout palUniEditorLayout = new javax.swing.GroupLayout(palUniEditor);
        palUniEditor.setLayout(palUniEditorLayout);
        palUniEditorLayout.setHorizontalGroup(
            palUniEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 759, Short.MAX_VALUE)
        );
        palUniEditorLayout.setVerticalGroup(
            palUniEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 683, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout palUniMapLayout = new javax.swing.GroupLayout(palUniMap);
        palUniMap.setLayout(palUniMapLayout);
        palUniMapLayout.setHorizontalGroup(
            palUniMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(palUniEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        palUniMapLayout.setVerticalGroup(
            palUniMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(palUniEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSplitPane3.setLeftComponent(palUniMap);

        splStellarStruct.setDividerSize(4);
        splStellarStruct.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splStellarStruct.setResizeWeight(0.6);

        scpRess.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scpRessMouseClicked(evt);
            }
        });

        tbRess.setModel(new javax.swing.table.DefaultTableModel(
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
        tbRess.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbRess.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbRessMouseClicked(evt);
            }
        });
        scpRess.setViewportView(tbRess);

        jLabel1.setText("资源");

        cmbWldRes.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cmbWldResItemStateChanged(evt);
            }
        });
        cmbWldRes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbWldResActionPerformed(evt);
            }
        });

        jLabel2.setText("配置");

        cmbCfgItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCfgItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpRess, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbWldRes, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCfgItem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(cmbWldRes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cmbCfgItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpRess, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
        );

        splStellarStruct.setRightComponent(jPanel1);

        trWorlds.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                trWorldsMouseClicked(evt);
            }
        });
        trWorlds.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                trWorldsValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(trWorlds);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
        );

        splStellarStruct.setLeftComponent(jPanel2);

        jSplitPane3.setRightComponent(splStellarStruct);

        jSplitPane1.setRightComponent(jSplitPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void miNewRegionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewRegionActionPerformed
    {//GEN-HEADEREND:event_miNewRegionActionPerformed
        newRegion();
    }//GEN-LAST:event_miNewRegionActionPerformed

    private void miEditRegionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditRegionActionPerformed
    {//GEN-HEADEREND:event_miEditRegionActionPerformed
        editRegion();
    }//GEN-LAST:event_miEditRegionActionPerformed

    private void miRemoveRegionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveRegionActionPerformed
    {//GEN-HEADEREND:event_miRemoveRegionActionPerformed
        removeRegion();
    }//GEN-LAST:event_miRemoveRegionActionPerformed

    private void trRegionsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_trRegionsMouseClicked
    {//GEN-HEADEREND:event_trRegionsMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popRegion.show(trRegions, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() >= 2)
        {
            editRegion();
        }
    }//GEN-LAST:event_trRegionsMouseClicked

    private void miNewConsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewConsActionPerformed
    {//GEN-HEADEREND:event_miNewConsActionPerformed
        newCons();
    }//GEN-LAST:event_miNewConsActionPerformed

    private void miEditConsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditConsActionPerformed
    {//GEN-HEADEREND:event_miEditConsActionPerformed
        editCons();
    }//GEN-LAST:event_miEditConsActionPerformed

    private void miRemoveConsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveConsActionPerformed
    {//GEN-HEADEREND:event_miRemoveConsActionPerformed
        removeCons();
    }//GEN-LAST:event_miRemoveConsActionPerformed

    private void miMoveConsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveConsActionPerformed
    {//GEN-HEADEREND:event_miMoveConsActionPerformed
        moveConsToRegion();
    }//GEN-LAST:event_miMoveConsActionPerformed

    private void miNewStellarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewStellarActionPerformed
    {//GEN-HEADEREND:event_miNewStellarActionPerformed
        newStellar();
    }//GEN-LAST:event_miNewStellarActionPerformed

    private void trRegionsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trRegionsValueChanged
    {//GEN-HEADEREND:event_trRegionsValueChanged
        regionSelected();
    }//GEN-LAST:event_trRegionsValueChanged

    private void trStesValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trStesValueChanged
    {//GEN-HEADEREND:event_trStesValueChanged
        stellarNodeSelected();
    }//GEN-LAST:event_trStesValueChanged

    private void trStesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_trStesMouseClicked
    {//GEN-HEADEREND:event_trStesMouseClicked
        if (evt.getButton() != MouseEvent.BUTTON1)
        {
            popStellar.show(trStes, evt.getX(), evt.getY());
        }
        else if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() >= 2)
        {
            editStellarData();
        }
    }//GEN-LAST:event_trStesMouseClicked

    private void miEditStellarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditStellarActionPerformed
    {//GEN-HEADEREND:event_miEditStellarActionPerformed
        editStellarData();
    }//GEN-LAST:event_miEditStellarActionPerformed

    private void miNewStellarNodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewStellarNodeActionPerformed
    {//GEN-HEADEREND:event_miNewStellarNodeActionPerformed
        newStellar();
    }//GEN-LAST:event_miNewStellarNodeActionPerformed

    private void miRemoveStellarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveStellarActionPerformed
    {//GEN-HEADEREND:event_miRemoveStellarActionPerformed
        removeStellarData();
    }//GEN-LAST:event_miRemoveStellarActionPerformed

    private void miMoveStellarUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveStellarUpActionPerformed
    {//GEN-HEADEREND:event_miMoveStellarUpActionPerformed
        moveStellarUp();
    }//GEN-LAST:event_miMoveStellarUpActionPerformed

    private void miMoveStellarDownActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveStellarDownActionPerformed
    {//GEN-HEADEREND:event_miMoveStellarDownActionPerformed
        moveStellarDown();
    }//GEN-LAST:event_miMoveStellarDownActionPerformed

    private void miMoveStellarToConstellationActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMoveStellarToConstellationActionPerformed
    {//GEN-HEADEREND:event_miMoveStellarToConstellationActionPerformed
        moveSteToCons();
    }//GEN-LAST:event_miMoveStellarToConstellationActionPerformed

    private void trWorldsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_trWorldsMouseClicked
    {//GEN-HEADEREND:event_trWorldsMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popWorlds.show(trWorlds, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_trWorldsMouseClicked

    private void miNewWldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewWldActionPerformed
    {//GEN-HEADEREND:event_miNewWldActionPerformed
        newWorld();
    }//GEN-LAST:event_miNewWldActionPerformed

    private void miEditWldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditWldActionPerformed
    {//GEN-HEADEREND:event_miEditWldActionPerformed
        editWorld();
    }//GEN-LAST:event_miEditWldActionPerformed

    private void miRemoveWldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveWldActionPerformed
    {//GEN-HEADEREND:event_miRemoveWldActionPerformed
        removeWorld();
    }//GEN-LAST:event_miRemoveWldActionPerformed

    private void miAppendResSrcActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miAppendResSrcActionPerformed
    {//GEN-HEADEREND:event_miAppendResSrcActionPerformed
        appendResSource();
    }//GEN-LAST:event_miAppendResSrcActionPerformed

    private void trWorldsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_trWorldsValueChanged
    {//GEN-HEADEREND:event_trWorldsValueChanged
        preLoadWorldRss();
    }//GEN-LAST:event_trWorldsValueChanged

    private void cmbWldResActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbWldResActionPerformed
    {//GEN-HEADEREND:event_cmbWldResActionPerformed
        makeResCfgCombo();
        makeRessTable();
    }//GEN-LAST:event_cmbWldResActionPerformed

    private void cmbWldResItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbWldResItemStateChanged
    {//GEN-HEADEREND:event_cmbWldResItemStateChanged

    }//GEN-LAST:event_cmbWldResItemStateChanged

    private void tbRessMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tbRessMouseClicked
    {//GEN-HEADEREND:event_tbRessMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popRes.show(tbRess, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbRessMouseClicked

    private void miNewRessActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewRessActionPerformed
    {//GEN-HEADEREND:event_miNewRessActionPerformed
        appendResSource();
    }//GEN-LAST:event_miNewRessActionPerformed

    private void miEditRessActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditRessActionPerformed
    {//GEN-HEADEREND:event_miEditRessActionPerformed
        editRess();
    }//GEN-LAST:event_miEditRessActionPerformed

    private void miRemoveRessActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveRessActionPerformed
    {//GEN-HEADEREND:event_miRemoveRessActionPerformed
        removeRess();
    }//GEN-LAST:event_miRemoveRessActionPerformed

    private void cmbCfgItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbCfgItemActionPerformed
    {//GEN-HEADEREND:event_cmbCfgItemActionPerformed
        makeRessTable();
    }//GEN-LAST:event_cmbCfgItemActionPerformed

    private void scpRessMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scpRessMouseClicked
    {//GEN-HEADEREND:event_scpRessMouseClicked
        if (evt.getButton() == evt.BUTTON3)
        {
            popRes.show(scpRess, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_scpRessMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbCfgItem;
    private javax.swing.JComboBox<String> cmbWldRes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JMenuItem miAppendResSrc;
    private javax.swing.JMenuItem miEditCons;
    private javax.swing.JMenuItem miEditRegion;
    private javax.swing.JMenuItem miEditRess;
    private javax.swing.JMenuItem miEditStellar;
    private javax.swing.JMenuItem miEditWld;
    private javax.swing.JMenuItem miMoveCons;
    private javax.swing.JMenuItem miMoveStellarDown;
    private javax.swing.JMenuItem miMoveStellarToConstellation;
    private javax.swing.JMenuItem miMoveStellarUp;
    private javax.swing.JMenuItem miNewCons;
    private javax.swing.JMenuItem miNewRegion;
    private javax.swing.JMenuItem miNewRess;
    private javax.swing.JMenuItem miNewStellar;
    private javax.swing.JMenuItem miNewStellarNode;
    private javax.swing.JMenuItem miNewWld;
    private javax.swing.JMenuItem miRemoveCons;
    private javax.swing.JMenuItem miRemoveRegion;
    private javax.swing.JMenuItem miRemoveRess;
    private javax.swing.JMenuItem miRemoveStellar;
    private javax.swing.JMenuItem miRemoveWld;
    private javax.swing.JPanel palRegions;
    private javax.swing.JPanel palStes;
    private javax.swing.JPanel palUniEditor;
    private javax.swing.JPanel palUniMap;
    private javax.swing.JPopupMenu popRegion;
    private javax.swing.JPopupMenu popRes;
    private javax.swing.JPopupMenu popStellar;
    private javax.swing.JPopupMenu popWorlds;
    private javax.swing.JScrollPane scpRess;
    private javax.swing.JSplitPane splStellarStruct;
    private javax.swing.JTable tbRess;
    private javax.swing.JTree trRegions;
    private javax.swing.JTree trStes;
    private javax.swing.JTree trWorlds;
    // End of variables declaration//GEN-END:variables

    @Override
    public int getMDIFrameType()
    {
        return MDIPaneControl.IFRM_CTX_UNI_EDITOR;
    }

    @Override
    public String getMDIFrameSID()
    {
        return glxid + "";
    }
}
