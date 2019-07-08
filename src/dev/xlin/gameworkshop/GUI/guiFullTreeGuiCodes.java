/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.xlin.gameworkshop.GUI;

import dev.xlin.gameworkshop.progs.foundation.keyDataDefine;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.gameworkshop.progs.tools.systemType;
import dev.xlin.swingTools2.dlgTools.dlgSelectTreeNode;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 *
 * @author Administrator
 */
public class guiFullTreeGuiCodes
{

    public static myTreeNode getSelectedType(JTree tree)
    {
        TreePath tph = tree.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() == 0)
        {
            return null;
        }
        return mtn;
    }

    public static int doNewType(JTree tree, wakeup up , int sttid )
    {
        String s = fast.input("输入分类名称");
        if (s == null)
        {
            return iDAO.OPERATE_FAIL;
        }
        s = s.trim();
        if (s.equals(""))
        {
            return iDAO.OPERATE_FAIL;
        }
        myTreeNode msel = getSelectedType(tree);
        int parid = 0;
        if (msel != null)
        {
            parid = msel.getNodeOID();
        }
        beanSttType bst = new beanSttType();
        bst.setSttID(sttid);
        bst.setTypeParent(parid);
        bst.setTypeName(s);
        sttType stp = new sttType(up);
        int r = stp.createRecord(bst, false);
        return r;
    }

    public static int doEditType(JTree tree, wakeup up)
    {
        myTreeNode msel = getSelectedType(tree);
        if (msel == null)
        {
            return iDAO.OPERATE_FAIL;
        }
        int sid = msel.getNodeOID();
        if (sid == 0)
        {
            return iDAO.OPERATE_FAIL;
        }
        sttType stp = new sttType(up);
        beanSttType bst = (beanSttType) stp.getRecordByID(sid);
        String s = fast.input("输入分类名称", bst.getTypeName());
        if (s == null)
        {
            return iDAO.OPERATE_FAIL;
        }
        s = s.trim();
        if (s.equals(""))
        {
            return iDAO.OPERATE_FAIL;
        }
        bst.setTypeName(s);
        int r = stp.modifyRecord(bst, false);
        return r;
    }

    public static int doDisType(JTree tree, wakeup up)
    {
        myTreeNode msel = getSelectedType(tree);
        if (msel == null)
        {
            return iDAO.OPERATE_FAIL;
        }
        int sel = fast.ask("是否将这个分类设置为失效？");
        if (sel != fast.YES)
        {
            return iDAO.OPERATE_FAIL;
        }
        sttType stp = new sttType(up);
        int r = stp.deleteRecord(msel.getNodeOID());
        return r;
    }

    public static int doRevType(JTree tree, wakeup up)
    {
        myTreeNode msel = getSelectedType(tree);
        if (msel == null)
        {
            return iDAO.OPERATE_FAIL;
        }
        int sel = fast.ask("是否将这个分类恢复为有效？");
        if (sel != fast.YES)
        {
            return iDAO.OPERATE_FAIL;
        }
        sttType stp = new sttType(up);
        int r = stp.revertBean(msel.getNodeOID());
        return r;
    }

    public static int doDesType(JTree tree, wakeup up)
    {
        myTreeNode msel = getSelectedType(tree);
        if (msel == null)
        {
            return iDAO.OPERATE_FAIL;
        }
        int sel = fast.ask("是否将这个分类销毁？ \n这个操作不可被恢复");
        if (sel != fast.YES)
        {
            return iDAO.OPERATE_FAIL;
        }
        sttType stp = new sttType(up);
        int r = stp.destroyBean(msel.getNodeOID());
        return r;
    }

    public static int doMoveType(JTree tree, wakeup up , int sttID )
    {
        myTreeNode msel = getSelectedType(tree);
        if (msel == null)
        {
            return iDAO.OPERATE_FAIL;
        }
        myTreeNode mrt = guiCodes.makeFullTypeTree(up, sttID, false, msel.getNodeOID());
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mrt);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            myTreeNode mtn = dlg.getSelect();
            if (msel.getNodeOID() != 0)
            {
                sttType stp = new sttType(up);
                beanSttType bost = (beanSttType) stp.getRecordByID(msel.getNodeOID());
                bost.setTypeParent(mtn.getNodeOID());
                int r = stp.modifyRecord(bost, false);
                dlg.dispose();
                dlg = null;
                return r ; 
            }
        }
        dlg.dispose();
        dlg = null;
        return iDAO.OPERATE_FAIL;
    }

}
