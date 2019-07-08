package dev.xlin.gameworkshop.GUI;

import dev.xlin.gameworkshop.GUI.CONTENT.ifrmBaseResourceMgr;
import dev.xlin.gameworkshop.GUI.CONTENT.ifrmCtxWorldConfig;
import dev.xlin.gameworkshop.GUI.CONTENT.ifrmGlaxyEditor;
import dev.xlin.gameworkshop.GUI.infrms.ifrmDatablockDefine;
import dev.xlin.gameworkshop.GUI.infrms.ifrmDatablockTempMgr;
import dev.xlin.gameworkshop.GUI.infrms.ifrmItemCluster;
import dev.xlin.gameworkshop.GUI.infrms.ifrmObjectClass;
import dev.xlin.gameworkshop.GUI.infrms.ifrmPropManager;
import dev.xlin.gameworkshop.GUI.infrms.ifrmItemDefineMgr;
import dev.xlin.gameworkshop.GUI.infrms.ifrmItemEquipStruct;
import dev.xlin.gameworkshop.GUI.infrms.ifrmItemTempletMgr;
import dev.xlin.gameworkshop.GUI.infrms.ifrmKeyDataChunkMgr;
import dev.xlin.gameworkshop.GUI.infrms.ifrmKeyDataDefineMgr;
import dev.xlin.gameworkshop.GUI.infrms.ifrmMCESMgr;
import dev.xlin.gameworkshop.GUI.infrms.ifrmMDBSMainControl;
import dev.xlin.gameworkshop.GUI.infrms.ifrmMDBTypes;
import dev.xlin.gameworkshop.GUI.infrms.ifrmProgInterfaceMgr;
import dev.xlin.gameworkshop.GUI.infrms.ifrmProgIntfSets;
import dev.xlin.gameworkshop.GUI.infrms.ifrmSkillDefine;
import dev.xlin.gameworkshop.progs.databaseTools;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Enumeration;
import java.util.Timer;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class frmMain extends javax.swing.JFrame
{

    private wakeup up = null;
    public static ImageIcon icon = null;

    public frmMain()
    {
        Timer tmr = new Timer();
        tmr.schedule(new runningRecs(this), 0, 1000);
        dealLAF();
        initGlobalFontSetting(new Font("微软雅黑", 0, 12));
        initComponents();
        //icon = new ImageIcon(this.getClass().getResource("/dev/xlin/starshipworkshop/GUI/res/mainOri.png"));
        icon = new ImageIcon(this.getClass().getResource("/dev/xlin/gameworkshop/GUI/res/main.png"));
        this.setIconImage(icon.getImage());
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        //tbrMain.putClientProperty("ToolBar.isPaintPlainBackground", Boolean.TRUE);
        up = databaseTools.connectDB();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainNavTree();

    }

    private void mainNavTree()
    {
        myTreeNode mroot = new myTreeNode("GMWS", navConst.SYSTEM_ROOT, 0);
        //基础数据部分
        myTreeNode mdata = new myTreeNode("基础数据", navConst.DATA_ROOT, 0);
        myTreeNode mdata_prop = new myTreeNode("属性管理", navConst.DATA_PROPERTY, 0);
        myTreeNode mdata_OCLS = new myTreeNode("物类管理", navConst.DATA_DATA_BLOCK, 0);
        myTreeNode mdata_DBLOCK = new myTreeNode("数据区块", navConst.DATA_DATA_BLOCK, 0);
        myTreeNode mdata_ITEM = new myTreeNode("物体管理", navConst.DATA_ITEM_MGR, 0);
        myTreeNode mdata_SKLS = new myTreeNode("技术管理", navConst.DATA_SKILL_MGR, 0);
        myTreeNode mdata_ICLUS = new myTreeNode("物体集合", navConst.DATA_ITEM_CLUSTER, 0);
        myTreeNode mdata_KEYMGR = new myTreeNode("键值管理", navConst.DATA_KEY_MGR, 0);
        myTreeNode mdata_KEYCHK = new myTreeNode("键数据区块", navConst.DATA_KEY_CHUNK, 0);
        myTreeNode mdata_DBLK_TMP = new myTreeNode("数据区块模板", navConst.DATA_BLOCK_TEMP, 0);
        myTreeNode mdata_ITEM_TMP = new myTreeNode("物体模板", navConst.DATA_ITEM_TEMP, 0);
        myTreeNode mdata_EQP_ST = new myTreeNode("装配体系", navConst.DATA_EQUIP_STRUCT, 0);
        myTreeNode mdata_ITF_MGR = new myTreeNode("接口管理", navConst.DATA_INTF_MGR, 0);
        myTreeNode mdata_ITF_SET = new myTreeNode("接口集合", navConst.DATA_INTF_SET_MGR, 0);
        myTreeNode mdata_MCE_MGR = new myTreeNode("MCE管理", navConst.DATA_MCE_MANAGER, 0);
        myTreeNode mdata_MDB_TP = new myTreeNode("MDB类型", navConst.DATA_MDBTP_MGR, 0);
        myTreeNode mdata_MDB_MC = new myTreeNode("MDBS配置", navConst.DATA_MDBS_MC, 0);
        mdata.add(mdata_prop);
        mdata.add(mdata_OCLS);
        mdata.add(mdata_DBLOCK);
        mdata.add(mdata_ITEM);
        mdata.add(mdata_SKLS);
        mdata.add(mdata_ICLUS);
        mdata.add(mdata_KEYMGR);
        mdata.add(mdata_KEYCHK);
        mdata.add(mdata_DBLK_TMP);
        mdata.add(mdata_ITEM_TMP);
        mdata.add(mdata_EQP_ST);
        mdata.add(mdata_ITF_MGR);
        mdata.add(mdata_ITF_SET);
        mdata.add(mdata_MCE_MGR);
        mdata.add(mdata_MDB_TP);
        mdata.add(mdata_MDB_MC);
        mroot.add(mdata);

        //CTX A，星系世界编辑系统部分
        myTreeNode mctxGLX = new myTreeNode("星系世界", navConst.CTX_A_ROOT, 0);
        myTreeNode mctxGLX_RES = new myTreeNode("资源管理", navConst.CTX_A_RES_DEF, 0);
        myTreeNode mctxGLX_WLD_CFG = new myTreeNode("世界配置", navConst.CTX_A_WORLD_CONFIG, 0);
        myTreeNode mctxGLX_GLX_EDT = new myTreeNode("星系编辑器", navConst.CTX_GLAXY_EDITOR, 0);
        mctxGLX.add(mctxGLX_RES);
        mctxGLX.add(mctxGLX_WLD_CFG);
        mctxGLX.add(mctxGLX_GLX_EDT);
        mroot.add(mctxGLX);

        DefaultTreeModel dtm = new DefaultTreeModel(mroot);
        treeNav.setModel(dtm);
        guiCommon.expandTree(treeNav);
    }

    public static Image getImageIcon()
    {
        return icon.getImage();
    }

    public static ImageIcon getIcon()
    {
        return icon;
    }

    public void postRunningRec(int tm, int ct)
    {
        String ss = "SSWS 运行：" + ct + "次 时长：";
        int isec = tm;
        int ihour = 0;
        int imin = 0;
        int isecond = 0;
        if (isec < 60)
        {
            ihour = 0;
            imin = 0;
            isecond = isec;
        }
        else if (isec >= 60 && isec < 3600)
        {
            ihour = 0;
            imin = isec / 60;
            isecond = isec - 60 * imin;
        }
        else
        {
            ihour = isec / 3600;
            imin = (isec - ihour * 3600) / 60;
            isecond = (isec - ihour * 3600 - imin * 60);
        }
        ss = ss + ihour + "小时" + imin + "分" + isecond + "秒";
        setTitle(ss);
    }

    private void initGlobalFontSetting(Font fnt)
    {
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();)
        {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
            {
                UIManager.put(key, fontRes);
            }
        }
    }

    private void dealLAF()
    {
//        try
//        {
//            //设置本属性将改变窗口边框样式定义
//            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
//            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
//            UIManager.put("ToolBar.isPaintPlainBackground", Boolean.TRUE);
//            UIManager.put("RootPane.setupButtonVisible", false);
//            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
//             UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(3, 2, 2, 20));
//        }
//        catch (Exception e)
//        {
//            //TODO exception
//        }

        try
        {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e)
        {
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(true);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground", "true");

    }

    private void doPropMgr()
    {
        if (guiIFrameControl.isIsPropMgr() == false)
        {
            ifrmPropManager ifmPropMgr = new ifrmPropManager(up, this.deskMain);
            deskMain.add(ifmPropMgr);
            ifmPropMgr.setVisible(true);
            guiIFrameControl.setIsPropMgr(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmPropManager.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doMCEManager()
    {
        if (guiIFrameControl.isIsMCEManager() == false)
        {
            ifrmMCESMgr ifmMCE = new ifrmMCESMgr(up, deskMain);
            deskMain.add(ifmMCE);
            ifmMCE.setVisible(true);
            guiIFrameControl.setIsMCEManager(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmMCESMgr.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doObjCls()
    {
        if (guiIFrameControl.isIsObjectClass() == false)
        {
            ifrmObjectClass ifmObjCls = new ifrmObjectClass(up, deskMain);
            deskMain.add(ifmObjCls);
            ifmObjCls.setVisible(true);
            guiIFrameControl.setIsObjectClass(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmObjectClass.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doItemDefineMgr()
    {
        if (guiIFrameControl.isIsItemDefineMgr() == false)
        {
            ifrmItemDefineMgr ifmShipEqpMgr = new ifrmItemDefineMgr(up, deskMain);
            deskMain.add(ifmShipEqpMgr);
            ifmShipEqpMgr.setVisible(true);
            guiIFrameControl.setIsItemDefineMgr(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmItemDefineMgr.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doDatablockDefine()
    {
        if (guiIFrameControl.isIsDatablockDefine() == false)
        {
            ifrmDatablockDefine ifrmDbDefine = new ifrmDatablockDefine(up, deskMain);
            deskMain.add(ifrmDbDefine);
            ifrmDbDefine.setVisible(true);
            guiIFrameControl.setIsDatablockDefine(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmDatablockDefine.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doItemClusterMgr()
    {
        if (guiIFrameControl.isIsItemCluster() == false)
        {
            ifrmItemCluster ifrmItCls = new ifrmItemCluster(up, deskMain);
            deskMain.add(ifrmItCls);
            ifrmItCls.setVisible(true);
            guiIFrameControl.setIsItemCluster(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmItemCluster.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doKeyDefMgr()
    {
        if (guiIFrameControl.isIsKeyDefMgr() == false)
        {
            ifrmKeyDataDefineMgr ifrmKeyDef = new ifrmKeyDataDefineMgr(up, deskMain);
            deskMain.add(ifrmKeyDef);
            ifrmKeyDef.setVisible(true);
            guiIFrameControl.setIsKeyDefMgr(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmKeyDataDefineMgr.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doKeyChunkMgr()
    {
        if (guiIFrameControl.isIsKeyChunkMgr() == false)
        {
            ifrmKeyDataChunkMgr ifrmKeyChunk = new ifrmKeyDataChunkMgr(up, deskMain);
            deskMain.add(ifrmKeyChunk);
            ifrmKeyChunk.setVisible(true);
            guiIFrameControl.setIsKeyChunkMgr(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmKeyDataChunkMgr.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doDblockTmpMgr()
    {
        if (guiIFrameControl.isIsDblockTempMgr() == false)
        {
            ifrmDatablockTempMgr ifrmDblTmpMgr = new ifrmDatablockTempMgr(up, deskMain);
            deskMain.add(ifrmDblTmpMgr);
            ifrmDblTmpMgr.setVisible(true);
            guiIFrameControl.setIsDblockTempMgr(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmDatablockTempMgr.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doIntfSetMgr()
    {
        if (guiIFrameControl.isIsIntfSetMgr() == false)
        {
            ifrmProgIntfSets ifrmPITS = new ifrmProgIntfSets(up, deskMain);
            deskMain.add(ifrmPITS);
            ifrmPITS.setVisible(true);
            guiIFrameControl.setIsIntfSetMgr(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmProgIntfSets.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doItemTempMgr()
    {
        if (guiIFrameControl.isIsItemTempMgr() == false)
        {
            ifrmItemTempletMgr ifrmItTmpMgr = new ifrmItemTempletMgr(up, deskMain);
            deskMain.add(ifrmItTmpMgr);
            ifrmItTmpMgr.setVisible(true);
            guiIFrameControl.setIsItemTempMgr(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmItemTempletMgr.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doItemEquipStruct()
    {
        if (guiIFrameControl.isIsItemEquipStruct() == false)
        {
            ifrmItemEquipStruct ifrmItEqSt = new ifrmItemEquipStruct(up, deskMain);
            deskMain.add(ifrmItEqSt);
            ifrmItEqSt.setVisible(true);
            guiIFrameControl.setIsItemEquipStruct(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmItemEquipStruct.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doProgIntfMgr()
    {
        if (guiIFrameControl.isIsProgInterfaceMgr() == false)
        {
            ifrmProgInterfaceMgr ifrmPrgIntfMgr = new ifrmProgInterfaceMgr(up, deskMain);
            deskMain.add(ifrmPrgIntfMgr);
            ifrmPrgIntfMgr.setVisible(true);
            guiIFrameControl.setIsProgInterfaceMgr(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmProgInterfaceMgr.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doSkillDef()
    {
        if (guiIFrameControl.isIsSkillDefine() == false)
        {
            ifrmSkillDefine ifrmSklDef = new ifrmSkillDefine(up, deskMain);
            deskMain.add(ifrmSklDef);
            ifrmSklDef.setVisible(true);
            guiIFrameControl.setIsSkillDefine(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmSkillDefine.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doMDBMC()
    {
        if (guiIFrameControl.isIsMDBMainControl()== false)
        {
            ifrmMDBSMainControl ifrmMDBMC = new ifrmMDBSMainControl(up ,deskMain);
            deskMain.add(ifrmMDBMC);
            ifrmMDBMC.setVisible(true);
            guiIFrameControl.setIsMDBMainControl(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmMDBSMainControl.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doMDBTP()
    {
        if (guiIFrameControl.isIsMDBTPMgr() == false)
        {
            ifrmMDBTypes ifrmMDBTP = new ifrmMDBTypes(up, deskMain);
            deskMain.add(ifrmMDBTP);
            ifrmMDBTP.setVisible(true);
            guiIFrameControl.setIsMDBTPMgr(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmMDBTypes.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doCtxResDef()
    {
        if (guiIFrameControl.isIsCtxResDef() == false)
        {
            ifrmBaseResourceMgr ifrmBaseRes = new ifrmBaseResourceMgr(up, deskMain);
            deskMain.add(ifrmBaseRes);
            ifrmBaseRes.setVisible(true);
            guiIFrameControl.setIsCtxResDef(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmBaseResourceMgr.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private void doCtxUniEditor()
    {
        int uniid = 1;
        if (MDIPaneControl.isMDIFrameOpened(MDIPaneControl.IFRM_CTX_UNI_EDITOR, uniid + "") == false)
        {
            ifrmGlaxyEditor ifrmEdit = new ifrmGlaxyEditor(deskMain, up, uniid);
            deskMain.add(ifrmEdit);
            ifrmEdit.setVisible(true);
            MDIPaneControl.setMDIFrameOpen(MDIPaneControl.IFRM_CTX_UNI_EDITOR, uniid + "");
        }
        else
        {
            iMDIFrameFeature imdf = MDIPaneControl.findMDI(MDIPaneControl.IFRM_CTX_UNI_EDITOR, uniid + "");
            if (imdf != null)
            {
                deskMain.remove((JInternalFrame) imdf);
                MDIPaneControl.setMDIFrameClose(MDIPaneControl.IFRM_CTX_UNI_EDITOR, uniid + "");
                MDIPaneControl.removeMDIFrameFeature(MDIPaneControl.IFRM_CTX_UNI_EDITOR, uniid + "");
                deskMain.repaint();
                imdf = null;
            }
        }

    }

    private void doCtxWorldTypeConfigs()
    {
        if (guiIFrameControl.isIsCtxWorldTypes() == false)
        {
            ifrmCtxWorldConfig ifrmWldTps = new ifrmCtxWorldConfig(up, deskMain);
            deskMain.add(ifrmWldTps);
            ifrmWldTps.setVisible(true);
            guiIFrameControl.setIsCtxWorldTypes(true);
        }
        else
        {
            JInternalFrame jif = findIFrameInDesk(deskMain, ifrmCtxWorldConfig.class);
            if (jif != null)
            {
                deskMain.setComponentZOrder(jif, 0);
                deskMain.repaint();
            }
        }
    }

    private JInternalFrame findIFrameInDesk(JDesktopPane desk, Class cls)
    {
        JInternalFrame[] jifs = desk.getAllFrames();
        for (int i = 0; i < jifs.length; i++)
        {
            JInternalFrame jif = jifs[i];
            if (jif.getClass().equals(cls))
            {
                return jif;
            }
        }
        return null;
    }

    //左侧导航树的功能被点击中
    private void selectNav()
    {
        TreePath tph = treeNav.getSelectionPath();
        if (tph == null)
        {
            return;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        int sid = mtn.getNodeOID();
        switch (sid)
        {
            case navConst.DATA_ROOT:
                break;
            case navConst.DATA_SKILL_MGR:
                doSkillDef();
                break;
            case navConst.DATA_PROPERTY:
                doPropMgr();
                break;
            case navConst.DATA_OBJECT_CLASS:
                doObjCls();
                break;
            case navConst.DATA_KEY_MGR:
                doKeyDefMgr();
                break;
            case navConst.DATA_KEY_CHUNK:
                doKeyChunkMgr();
                break;
            case navConst.DATA_ITEM_TEMP:
                doItemTempMgr();
                break;
            case navConst.DATA_ITEM_MGR:
                doItemDefineMgr();
                break;
            case navConst.DATA_ITEM_CLUSTER:
                doItemClusterMgr();
                break;
            case navConst.DATA_INTF_MGR:
                doProgIntfMgr();
                break;
            case navConst.DATA_EQUIP_STRUCT:
                doItemEquipStruct();
                break;
            case navConst.DATA_DATA_BLOCK:
                doDatablockDefine();
                break;
            case navConst.DATA_BLOCK_TEMP:
                doDblockTmpMgr();
                break;
            case navConst.DATA_INTF_SET_MGR:
                doIntfSetMgr();
                break;
            case navConst.DATA_MCE_MANAGER:
                doMCEManager();
                break;
            case navConst.DATA_MDBTP_MGR:
                doMDBTP();
                break;
            case navConst.DATA_MDBS_MC:
                doMDBMC();
                break;
            //CTX-A部分 
            case navConst.CTX_A_RES_DEF:
                doCtxResDef();
                break;
            case navConst.CTX_A_WORLD_CONFIG:
                doCtxWorldTypeConfigs();
                break;
            case navConst.CTX_GLAXY_EDITOR:
                doCtxUniEditor();
                break;

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        tbrMain = new javax.swing.JToolBar();
        btnPropMgr = new javax.swing.JButton();
        btnObjCls = new javax.swing.JButton();
        btnDBlockDefine = new javax.swing.JButton();
        btnShipEquip = new javax.swing.JButton();
        spMain = new javax.swing.JSplitPane();
        deskMain = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeNav = new javax.swing.JTree();
        jMenuBar1 = new javax.swing.JMenuBar();
        muSystem = new javax.swing.JMenu();
        miExit = new javax.swing.JMenuItem();
        muSysData = new javax.swing.JMenu();
        miProps = new javax.swing.JMenuItem();
        miOcls = new javax.swing.JMenuItem();
        miDatablock = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miItems = new javax.swing.JMenuItem();
        miSkillDef = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miItemCluster = new javax.swing.JMenuItem();
        miKeyDefMgr = new javax.swing.JMenuItem();
        miKeyChunkMgr = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        miDBLTempMgr = new javax.swing.JMenuItem();
        miItemTempMgr = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        miItemEquipSt = new javax.swing.JMenuItem();
        miProgIntfMgr = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        miMCE = new javax.swing.JMenuItem();
        miMDBTP = new javax.swing.JMenuItem();
        muContents = new javax.swing.JMenu();
        miCtxResDef = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        miWLDTPCFG = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        miUniWorldEditorGuide = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tbrMain.setFloatable(false);
        tbrMain.setRollover(true);

        btnPropMgr.setText("PROPS");
        btnPropMgr.setFocusable(false);
        btnPropMgr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPropMgr.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPropMgr.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPropMgrActionPerformed(evt);
            }
        });
        tbrMain.add(btnPropMgr);

        btnObjCls.setText("OCLS");
        btnObjCls.setFocusable(false);
        btnObjCls.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnObjCls.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnObjCls.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnObjClsActionPerformed(evt);
            }
        });
        tbrMain.add(btnObjCls);

        btnDBlockDefine.setText("DBLOCK");
        btnDBlockDefine.setFocusable(false);
        btnDBlockDefine.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDBlockDefine.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDBlockDefine.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDBlockDefineActionPerformed(evt);
            }
        });
        tbrMain.add(btnDBlockDefine);

        btnShipEquip.setText("ITEMS");
        btnShipEquip.setFocusable(false);
        btnShipEquip.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShipEquip.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnShipEquip.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnShipEquipActionPerformed(evt);
            }
        });
        tbrMain.add(btnShipEquip);

        spMain.setDividerLocation(188);
        spMain.setDividerSize(3);

        deskMain.setBackground(new java.awt.Color(0, 0, 51));

        javax.swing.GroupLayout deskMainLayout = new javax.swing.GroupLayout(deskMain);
        deskMain.setLayout(deskMainLayout);
        deskMainLayout.setHorizontalGroup(
            deskMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1174, Short.MAX_VALUE)
        );
        deskMainLayout.setVerticalGroup(
            deskMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 667, Short.MAX_VALUE)
        );

        spMain.setRightComponent(deskMain);

        treeNav.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeNavValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treeNav);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
        );

        spMain.setLeftComponent(jPanel1);

        muSystem.setText("系统");

        miExit.setText("退出...");
        muSystem.add(miExit);

        jMenuBar1.add(muSystem);

        muSysData.setText("数据");

        miProps.setText("属性数据");
        miProps.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miPropsActionPerformed(evt);
            }
        });
        muSysData.add(miProps);

        miOcls.setText("物类管理");
        miOcls.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miOclsActionPerformed(evt);
            }
        });
        muSysData.add(miOcls);

        miDatablock.setText("数据区块");
        miDatablock.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDatablockActionPerformed(evt);
            }
        });
        muSysData.add(miDatablock);
        muSysData.add(jSeparator1);

        miItems.setText("物体管理");
        miItems.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miItemsActionPerformed(evt);
            }
        });
        muSysData.add(miItems);

        miSkillDef.setText("技术管理");
        miSkillDef.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miSkillDefActionPerformed(evt);
            }
        });
        muSysData.add(miSkillDef);
        muSysData.add(jSeparator2);

        miItemCluster.setText("物体块管理");
        miItemCluster.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miItemClusterActionPerformed(evt);
            }
        });
        muSysData.add(miItemCluster);

        miKeyDefMgr.setText("键值定义管理");
        miKeyDefMgr.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miKeyDefMgrActionPerformed(evt);
            }
        });
        muSysData.add(miKeyDefMgr);

        miKeyChunkMgr.setText("键值数据块管理");
        miKeyChunkMgr.setToolTipText("");
        miKeyChunkMgr.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miKeyChunkMgrActionPerformed(evt);
            }
        });
        muSysData.add(miKeyChunkMgr);
        muSysData.add(jSeparator3);

        miDBLTempMgr.setText("数据区块模板");
        miDBLTempMgr.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDBLTempMgrActionPerformed(evt);
            }
        });
        muSysData.add(miDBLTempMgr);

        miItemTempMgr.setText("物体数据模板");
        miItemTempMgr.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miItemTempMgrActionPerformed(evt);
            }
        });
        muSysData.add(miItemTempMgr);
        muSysData.add(jSeparator4);

        miItemEquipSt.setText("物体装配体系");
        miItemEquipSt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miItemEquipStActionPerformed(evt);
            }
        });
        muSysData.add(miItemEquipSt);

        miProgIntfMgr.setText("程序接口管理");
        miProgIntfMgr.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miProgIntfMgrActionPerformed(evt);
            }
        });
        muSysData.add(miProgIntfMgr);
        muSysData.add(jSeparator7);

        miMCE.setText("MCE配置管理");
        miMCE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMCEActionPerformed(evt);
            }
        });
        muSysData.add(miMCE);

        miMDBTP.setText("MDB类型管理");
        miMDBTP.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miMDBTPActionPerformed(evt);
            }
        });
        muSysData.add(miMDBTP);

        jMenuBar1.add(muSysData);

        muContents.setText("内容");

        miCtxResDef.setText("资源定义管理");
        miCtxResDef.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miCtxResDefActionPerformed(evt);
            }
        });
        muContents.add(miCtxResDef);
        muContents.add(jSeparator5);

        miWLDTPCFG.setText("世界类型配置管理");
        miWLDTPCFG.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miWLDTPCFGActionPerformed(evt);
            }
        });
        muContents.add(miWLDTPCFG);
        muContents.add(jSeparator6);

        miUniWorldEditorGuide.setForeground(new java.awt.Color(204, 0, 204));
        miUniWorldEditorGuide.setText("(世界编辑器)");
        miUniWorldEditorGuide.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miUniWorldEditorGuideActionPerformed(evt);
            }
        });
        muContents.add(miUniWorldEditorGuide);

        jMenuBar1.add(muContents);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbrMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(spMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(tbrMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(spMain))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPropMgrActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPropMgrActionPerformed
    {//GEN-HEADEREND:event_btnPropMgrActionPerformed
        doPropMgr();
    }//GEN-LAST:event_btnPropMgrActionPerformed

    private void btnObjClsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnObjClsActionPerformed
    {//GEN-HEADEREND:event_btnObjClsActionPerformed
        doObjCls();
    }//GEN-LAST:event_btnObjClsActionPerformed

    private void btnShipEquipActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnShipEquipActionPerformed
    {//GEN-HEADEREND:event_btnShipEquipActionPerformed
        doItemDefineMgr();
    }//GEN-LAST:event_btnShipEquipActionPerformed

    private void btnDBlockDefineActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDBlockDefineActionPerformed
    {//GEN-HEADEREND:event_btnDBlockDefineActionPerformed
        doDatablockDefine();
    }//GEN-LAST:event_btnDBlockDefineActionPerformed

    private void miItemClusterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miItemClusterActionPerformed
    {//GEN-HEADEREND:event_miItemClusterActionPerformed
        doItemClusterMgr();
    }//GEN-LAST:event_miItemClusterActionPerformed

    private void miKeyDefMgrActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miKeyDefMgrActionPerformed
    {//GEN-HEADEREND:event_miKeyDefMgrActionPerformed
        doKeyDefMgr();
    }//GEN-LAST:event_miKeyDefMgrActionPerformed

    private void miKeyChunkMgrActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miKeyChunkMgrActionPerformed
    {//GEN-HEADEREND:event_miKeyChunkMgrActionPerformed
        doKeyChunkMgr();
    }//GEN-LAST:event_miKeyChunkMgrActionPerformed

    private void miPropsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miPropsActionPerformed
    {//GEN-HEADEREND:event_miPropsActionPerformed
        doPropMgr();
    }//GEN-LAST:event_miPropsActionPerformed

    private void miOclsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miOclsActionPerformed
    {//GEN-HEADEREND:event_miOclsActionPerformed
        doObjCls();
    }//GEN-LAST:event_miOclsActionPerformed

    private void miDBLTempMgrActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDBLTempMgrActionPerformed
    {//GEN-HEADEREND:event_miDBLTempMgrActionPerformed
        doDblockTmpMgr();
    }//GEN-LAST:event_miDBLTempMgrActionPerformed

    private void miItemTempMgrActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miItemTempMgrActionPerformed
    {//GEN-HEADEREND:event_miItemTempMgrActionPerformed
        doItemTempMgr();
    }//GEN-LAST:event_miItemTempMgrActionPerformed

    private void miDatablockActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDatablockActionPerformed
    {//GEN-HEADEREND:event_miDatablockActionPerformed
        doDatablockDefine();
    }//GEN-LAST:event_miDatablockActionPerformed

    private void miItemsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miItemsActionPerformed
    {//GEN-HEADEREND:event_miItemsActionPerformed
        doItemDefineMgr();
    }//GEN-LAST:event_miItemsActionPerformed

    private void miItemEquipStActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miItemEquipStActionPerformed
    {//GEN-HEADEREND:event_miItemEquipStActionPerformed
        doItemEquipStruct();
    }//GEN-LAST:event_miItemEquipStActionPerformed

    private void miProgIntfMgrActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miProgIntfMgrActionPerformed
    {//GEN-HEADEREND:event_miProgIntfMgrActionPerformed
        doProgIntfMgr();
    }//GEN-LAST:event_miProgIntfMgrActionPerformed

    private void miSkillDefActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miSkillDefActionPerformed
    {//GEN-HEADEREND:event_miSkillDefActionPerformed
        doSkillDef();
    }//GEN-LAST:event_miSkillDefActionPerformed

    private void miCtxResDefActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miCtxResDefActionPerformed
    {//GEN-HEADEREND:event_miCtxResDefActionPerformed
        doCtxResDef();
    }//GEN-LAST:event_miCtxResDefActionPerformed

    private void miWLDTPCFGActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miWLDTPCFGActionPerformed
    {//GEN-HEADEREND:event_miWLDTPCFGActionPerformed
        doCtxWorldTypeConfigs();
    }//GEN-LAST:event_miWLDTPCFGActionPerformed

    private void miUniWorldEditorGuideActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miUniWorldEditorGuideActionPerformed
    {//GEN-HEADEREND:event_miUniWorldEditorGuideActionPerformed
        doCtxUniEditor();
    }//GEN-LAST:event_miUniWorldEditorGuideActionPerformed

    private void treeNavValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeNavValueChanged
    {//GEN-HEADEREND:event_treeNavValueChanged
        selectNav();
    }//GEN-LAST:event_treeNavValueChanged

    private void miMCEActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMCEActionPerformed
    {//GEN-HEADEREND:event_miMCEActionPerformed
        doMCEManager();
    }//GEN-LAST:event_miMCEActionPerformed

    private void miMDBTPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miMDBTPActionPerformed
    {//GEN-HEADEREND:event_miMDBTPActionPerformed
        doMDBTP();
    }//GEN-LAST:event_miMDBTPActionPerformed

    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try
//        {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
//            {
//                if ("Nimbus".equals(info.getName()))
//                {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        }
//        catch (ClassNotFoundException ex)
//        {
//            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch (InstantiationException ex)
//        {
//            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch (IllegalAccessException ex)
//        {
//            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch (javax.swing.UnsupportedLookAndFeelException ex)
//        {
//            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDBlockDefine;
    private javax.swing.JButton btnObjCls;
    private javax.swing.JButton btnPropMgr;
    private javax.swing.JButton btnShipEquip;
    private javax.swing.JDesktopPane deskMain;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JMenuItem miCtxResDef;
    private javax.swing.JMenuItem miDBLTempMgr;
    private javax.swing.JMenuItem miDatablock;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miItemCluster;
    private javax.swing.JMenuItem miItemEquipSt;
    private javax.swing.JMenuItem miItemTempMgr;
    private javax.swing.JMenuItem miItems;
    private javax.swing.JMenuItem miKeyChunkMgr;
    private javax.swing.JMenuItem miKeyDefMgr;
    private javax.swing.JMenuItem miMCE;
    private javax.swing.JMenuItem miMDBTP;
    private javax.swing.JMenuItem miOcls;
    private javax.swing.JMenuItem miProgIntfMgr;
    private javax.swing.JMenuItem miProps;
    private javax.swing.JMenuItem miSkillDef;
    private javax.swing.JMenuItem miUniWorldEditorGuide;
    private javax.swing.JMenuItem miWLDTPCFG;
    private javax.swing.JMenu muContents;
    private javax.swing.JMenu muSysData;
    private javax.swing.JMenu muSystem;
    private javax.swing.JSplitPane spMain;
    private javax.swing.JToolBar tbrMain;
    private javax.swing.JTree treeNav;
    // End of variables declaration//GEN-END:variables
}
