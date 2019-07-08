package dev.xlin.gameworkshop.GUI;

import java.util.ArrayList;

/**
 *
 * @author 刘祎鹏
 */
public class MDIPaneControl
{

    private static ArrayList frmItemDefineDataCache = new ArrayList();
    private static ArrayList frmDatablockTemplectCache = new ArrayList();
    private static ArrayList frmItemTempletDataCache = new ArrayList();
    private static ArrayList frmShipModelDataCache = new ArrayList();
    private static ArrayList frmSkillLevelDataCache = new ArrayList();
    private static ArrayList frmCtxUniEditor = new ArrayList();

    public static final int IFRM_ITEM_DEFINE_DATA = 10;
    public static final int IFRM_DATABLOCK_TEMP = 11;
    public static final int IFRM_ITEM_TEMPLET_DATA = 12;
    public static final int IFRM_SHIP_MODEL_DATA = 13;
    public static final int IFRM_SKILL_LEVEL_DATA = 14;
    public static final int IFRM_CTX_UNI_EDITOR = 1000;

    private static ArrayList<iMDIFrameFeature> MDIFrameHolder = new ArrayList();

    public static boolean isMDIFrameOpened(int type, String oid)
    {
        ArrayList arr = tranType(type);
        if (arr == null)
        {
            return false;
        }
        if (arr.contains(oid))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void appendMDIFrame(iMDIFrameFeature imdf)
    {
        MDIFrameHolder.add(imdf);
    }

    public static boolean removeMDIFrameFeature(int type, String sid)
    {
        for (int i = 0; i < MDIFrameHolder.size(); i++)
        {
            iMDIFrameFeature imdf = MDIFrameHolder.get(i);
            if (imdf.getMDIFrameType() == type && imdf.getMDIFrameSID().trim().equals(sid.trim()))
            {
                MDIFrameHolder.remove(i);
                return true;
            }
        }
        return false;
    }

    public static iMDIFrameFeature findMDI(int type, String sid)
    {
        for (int i = 0; i < MDIFrameHolder.size(); i++)
        {
            iMDIFrameFeature imdf = MDIFrameHolder.get(i);
            if (imdf.getMDIFrameType() == type && imdf.getMDIFrameSID().trim().equals(sid.trim()))
            {
                return imdf;
            }
        }
        return null;
    }

    public static void setMDIFrameOpen(int type, String oid)
    {
        ArrayList arr = tranType(type);
        if (arr == null)
        {
            return;
        }
        if (arr.contains(oid))
        {
            return;
        }
        else
        {
            arr.add(oid);
        }
    }

    public static void setMDIFrameClose(int type, String oid)
    {
        ArrayList arr = tranType(type);
        if (arr == null)
        {
            return;
        }
        if (arr.contains(oid))
        {
            arr.remove(oid);
        }
    }

    private static ArrayList tranType(int type)
    {
        if (type == IFRM_DATABLOCK_TEMP)
        {
            return frmDatablockTemplectCache;
        }
        else if (type == IFRM_ITEM_DEFINE_DATA)
        {
            return frmItemDefineDataCache;
        }
        else if (type == IFRM_ITEM_TEMPLET_DATA)
        {
            return frmItemTempletDataCache;
        }
        else if (type == IFRM_SHIP_MODEL_DATA)
        {
            return frmShipModelDataCache;
        }
        else if (type == IFRM_SKILL_LEVEL_DATA)
        {
            return frmSkillLevelDataCache;
        }
        else if (type == IFRM_CTX_UNI_EDITOR)
        {
            return frmCtxUniEditor;
        }
        return null;
    }

}

//
//LOG
//TIME:
//REC:
//
