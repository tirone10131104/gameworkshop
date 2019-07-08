package dev.xlin.gameworkshop.GUI;

import dev.xlin.gameworkshop.GUI.dialog.dlgDBTempletInfo;
import dev.xlin.gameworkshop.GUI.dialog.dlgDataColumn;
import dev.xlin.gameworkshop.GUI.dialog.dlgDataPage;
import dev.xlin.gameworkshop.GUI.dialog.dlgDatablockTempletSelector;
import dev.xlin.gameworkshop.progs.foundation.beanDataColumn;
import dev.xlin.gameworkshop.progs.foundation.beanDataPage;
import dev.xlin.gameworkshop.progs.foundation.beans.beanDatablock;
import dev.xlin.gameworkshop.progs.foundation.beans.beanDatablockDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.beanDatablockTemplet;
import dev.xlin.gameworkshop.progs.foundation.datablockDefine;
import dev.xlin.gameworkshop.progs.foundation.datablockService;
import dev.xlin.gameworkshop.progs.foundation.datablockTemplet;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXML;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataColumnOper;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElement;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataElementOper;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDataPageOper;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockClearValue;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockCopy;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockFace;
import dev.xlin.swingTools2.dlgTools.dlgComboSelect;
import dev.xlin.swingTools2.listItem;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;

public class guiDBLKPalCode
{

    public static int NODE_BLST_ROOT = 0;
    public static int NODE_BLST_PAGE = 1;
    public static int NODE_BLST_COLUMN = 2;

    public static int doNewPage(iDataPageOper idpo)
    {
        dlgDataPage dlg = new dlgDataPage(null, true, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            beanDataPage bpage = dlg.getPageBean();
            int r = idpo.appendDataPage(bpage);
            return r;
        }
        dlg.dispose();
        dlg = null;
        return 0;
    }

    public static int doNewCol(myTreeNode mpage, iDataColumnOper idco)
    {
        if (mpage == null)
        {
            return 0;
        }
        int pgid = mpage.getNodeOID();
        dlgDataColumn dlg = new dlgDataColumn(null, true, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            beanDataColumn bdc = dlg.getColumn();
            int r = idco.appendColumn(bdc, pgid);
            return r;
        }
        dlg.dispose();
        dlg = null;
        return 0;
    }

    public static int doEditPage(myTreeNode mpage, iDataPageOper idpo)
    {
        if (mpage == null)
        {
            return 0;
        }
        beanDataPage bdp = idpo.getDataPageByIndex(mpage.getNodeOID());
        dlgDataPage dlg = new dlgDataPage(null, true, bdp);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            beanDataPage bpage = dlg.getPageBean();
            int r = idpo.updatePage(bpage.getPageName(), mpage.getNodeOID());
            return r;
        }
        dlg.dispose();
        dlg = null;
        return 0;
    }

    public static boolean doDelPage(myTreeNode mpage, iDataPageOper idpo)
    {
        if (mpage == null)
        {
            return false;
        }
        int sel = fast.ask("是否确认删除这个分页，这样操作不可恢复");
        if (sel != fast.YES)
        {
            return false;
        }
        boolean b = idpo.removePageByIndex(mpage.getNodeOID());
        return b;
    }

    public static int doEditCol(myTreeNode mcol, iDataColumnOper idco)
    {
        if (mcol == null)
        {
            return 0;
        }
        beanDataColumn bdc = idco.getColumnByOID(mcol.getNodeOID());
        dlgDataColumn dlg = new dlgDataColumn(null, true, bdc);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            int r = idco.updateColumn(bdc);
            return r;
        }
        dlg.dispose();
        dlg = null;
        return 0;
    }

    public static int doDelCol(myTreeNode mcol, iDataColumnOper idco)
    {
        if (mcol == null)
        {
            return 0;
        }
        int sel = fast.ask("是否删除这个栏目？这个操作不可恢复");
        if (sel != fast.YES)
        {
            return 0;
        }
        int r = idco.removeColumn(mcol.getNodeOID());
        return r;
    }

    public static boolean doMovePageUp(myTreeNode mpage, iDataPageOper idpo)
    {
        if (mpage == null)
        {
            return false;
        }
        boolean b = idpo.movePageUp(mpage.getNodeOID());
        return b;
    }

    public static boolean doMovePageDown(myTreeNode mpage, iDataPageOper idpo)
    {
        if (mpage == null)
        {
            return false;
        }
        boolean b = idpo.movePageDown(mpage.getNodeOID());
        return b;
    }

    public static boolean doMoveColUp(myTreeNode mtn, iDataColumnOper idco)
    {
        if (mtn == null)
        {
            return false;
        }
        boolean b = idco.moveColumnUp(mtn.getNodeOID());
        return b;
    }

    public static boolean doMoveColDown(myTreeNode mtn, iDataColumnOper idco)
    {
        if (mtn == null)
        {
            return false;
        }
        boolean b = idco.moveColumnDown(mtn.getNodeOID());
        return b;
    }

    public static boolean doMoveDataUp(iDataElement ide, iDataElementOper ideo)
    {
        if (ide == null)
        {
            return false;
        }
        boolean b = ideo.moveDataUp(ide._getDataOID());
        return b;
    }

    public static boolean doMoveDataDown(iDataElement ide, iDataElementOper ideo)
    {
        if (ide == null)
        {
            return false;
        }
        boolean b = ideo.moveDataDown(ide._getDataOID());
        return b;
    }

    public static iDataElement getSelectedDataElement(JTable tbDatas, List ldatas)
    {
        int idx = tbDatas.getSelectedRow();
        if (idx < 0)
        {
            return null;
        }
        Object o = ldatas.get(idx);
        return (iDataElement) o;
    }

    public static boolean doMoveDataToCol(iDataElement ide, iDatablockFace idf)
    {
        if (ide == null)
        {
            return false;
        }
        iDataColumnOper idco = (iDataColumnOper) idf;
        iDataPageOper idpo = (iDataPageOper) idf;
        iDataElementOper ideo = (iDataElementOper) idf;
        int colOID = ideo.getDataElementColumnOID(ide._getDataOID());
        int pageIdx = idco.getColumnPageIndex(colOID);
        List lcos = idco.getColumnListByPageIndex(pageIdx);
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        for (int i = 0; i < lcos.size(); i++)
        {
            beanDataColumn bdc = (beanDataColumn) lcos.get(i);
            if (bdc.getOID() == colOID)
            {
                continue;
            }
            listItem li = new listItem(bdc.getColumnName(), bdc.getOID());
            mod.addElement(li);
        }
        dlgComboSelect dlg = new dlgComboSelect(null, true, mod, 0);
        dlg.setMainTitle("选择一个需要移动到的栏目");
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            listItem lsel = dlg.getSelectItem();
            int coid = lsel.getNodeOID();
            boolean b = ideo.moveDataToColumn(ide._getDataOID(), coid);
            return b;
        }
        dlg.dispose();
        dlg = null;
        return false;
    }

    public static boolean doMoveColToPage(myTreeNode mcol, iDatablockFace idf)
    {
        if (mcol == null)
        {
            return false;
        }
        iDataColumnOper idco = (iDataColumnOper) idf;
        iDataPageOper idpo = (iDataPageOper) idf;
        int pageidx = idco.getColumnPageIndex(mcol.getNodeOID());
        List lps = idpo.getPageList();
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        for (int i = 0; i < lps.size(); i++)
        {
            beanDataPage bpage = (beanDataPage) lps.get(i);
            if (i == pageidx)
            {
                continue;
            }
            listItem li = new listItem(bpage.getPageName(), i);
            mod.addElement(li);
        }
        dlgComboSelect dlg = new dlgComboSelect(null, true, mod, 0);
        dlg.setMainTitle("请选择一个分页");
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            listItem lsel = dlg.getSelectItem();
            boolean b = idco.moveColumnToPage(mcol.getNodeOID(), lsel.getNodeOID());
            return b;
        }
        dlg.dispose();
        dlg = null;
        return false;
    }

    public static int disData(iDataElement ide, iDataElementOper ideo)
    {
        if (ide == null)
        {
            return 0;
        }
        int sel = fast.ask("是否将数据：" + ide._getDataTitle() + "失效？");
        if (sel == fast.YES)
        {
            int r = ideo.disableDataElement(ide);
            return r;
        }
        return 0;
    }

    public static int revertData(iDataElement ide, iDataElementOper ideo)
    {
        if (ide == null)
        {
            return 0;
        }
        int sel = fast.ask("是否将数据：" + ide._getDataTitle() + "恢复为正常状态？");
        if (sel == fast.YES)
        {
            int r = ideo.revertDataElement(ide);
            return r;
        }
        return 0;
    }

    public static int destroyData(iDataElement ide, iDataElementOper ideo)
    {
        if (ide == null)
        {
            return 0;
        }
        if (ide._getStatus() != iDAO.OBJECT_STATE_DELETE)
        {
            fast.warn("只能将失效的数据进行销毁操作");
            return 0;
        }
        int sel = fast.ask("是否将数据：" + ide._getDataTitle() + "销毁？\n执行销毁操作以后必须执行保存才能生效！");
        if (sel == fast.YES)
        {
            int r = ideo.destroyDataElement(ide);
            return r;
        }
        return 0;
    }

    public static myTreeNode makeDatablockStructTree(Object odata)
    {
        myTreeNode mrt = new myTreeNode("[数据结构目录]", 0, NODE_BLST_ROOT);
        iDataPageOper idpo = (iDataPageOper) odata;
        iDataColumnOper idco = (iDataColumnOper) odata;
        List lpgs = idpo.getPageList();
        for (int i = 0; i < lpgs.size(); i++)
        {
            beanDataPage bpage = (beanDataPage) lpgs.get(i);
            myTreeNode mpage = new myTreeNode("[分页]" + bpage.getPageName(), i, NODE_BLST_PAGE);
            List lcs = idco.getColumnListByPageIndex(i);
            for (int j = 0; j < lcs.size(); j++)
            {
                beanDataColumn bcol = (beanDataColumn) lcs.get(j);
                myTreeNode mcol = new myTreeNode("[栏目]" + bcol.getColumnName(), bcol.getOID(), NODE_BLST_COLUMN);
                mpage.add(mcol);
            }
            mrt.add(mpage);
        }
        return mrt;
    }

    public static int guiSaveAsTemplet(wakeup up, int oclsID, iAdtXML iadt, long dbIndex)
    {
        iDatablockCopy idcp = (iDatablockCopy) iadt;
        iAdtXML ia = (iAdtXML) idcp.datablockCopy();
        datablockService dbs = new datablockService(up);
        beanDatablock bdb = dbs.getDatabean(dbIndex);
        if (bdb == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        dlgDBTempletInfo dlg = new dlgDBTempletInfo(null, true, up, null);
        dlg.setVisible(true);
        int r = 0;
        if (dlg.getOK())
        {
            beanDatablockTemplet btmp = dlg.getTempletBean();
            if (dlg.getClearValue())
            {
                //数据清除
                iDatablockClearValue idcv = (iDatablockClearValue) ia;
                idcv.setAllValueClear();
            }
            //数据填充
            datablockDefine dbdef = new datablockDefine(up);
            iDatablockFace idbface = (iDatablockFace) ia;
            beanDatablockDefine bdef = dbdef.getDataDefineByTag(idbface.getDatablockServiceTag());
            btmp.setDbType(bdef.getOID());
            btmp.setOclsID(oclsID);
            btmp.setOriID(dbIndex);
            datablockTemplet dbp = new datablockTemplet(up);
            r = dbp.saveAsTemplet(btmp, ia );
            return r;
        }
        dlg.dispose();
        dlg = null;
        return r;
    }

    public static boolean guiImportTemplet(wakeup up, iAdtXML iadt)
    {
        boolean b = false;
        iDatablockFace idbface = (iDatablockFace) iadt;
        dlgDatablockTempletSelector dlg = new dlgDatablockTempletSelector(null, true, up, idbface.getDatablockServiceTag());
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            beanDatablockTemplet btmp = dlg.getTemplet();
            int sel = fast.ask("是否要使用模板[" + btmp.getTempName() + "]？\n使用这个模板将会清除现有的数据。\n \n是否确认？");
            if (sel == fast.YES)
            {
                datablockService dbs = new datablockService(up);
                String sdt = dbs.loadData(btmp.getDatablock());
                xmlRight xr = new xmlRight();
                b = xr.parseXMLfromString(sdt);
                if (b)
                {
                    b = iadt.revertFromXML(xr);
                }
            }
        }
        dlg.dispose();
        dlg = null;
        return b;
    }
}
