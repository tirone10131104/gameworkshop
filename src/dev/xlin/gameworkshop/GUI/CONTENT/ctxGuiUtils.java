package dev.xlin.gameworkshop.GUI.CONTENT;

import dev.xlin.gameworkshop.progs.contents.beans.beanCtxBaseResource;
import dev.xlin.gameworkshop.progs.contents.progs.baseResourceDefine;
import dev.xlin.gameworkshop.progs.tools.beanSttType;
import dev.xlin.gameworkshop.progs.tools.sttType;
import dev.xlin.swingTools2.dlgTools.dlgSelectTreeNode;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import java.util.List;

/**
 *
 * @author 刘祎鹏
 */
public class ctxGuiUtils
{

    public static myTreeNode selectBaseResource(wakeup up)
    {
        //制作资源列表
        baseResourceDefine brd = new baseResourceDefine(up);
        sttType stp = new sttType(up);
        List ltps = stp.getRootsByTag("SST_C_BASE_RES", false);
        myTreeNode mrt = new myTreeNode("[基础资源数据]", 0, 0);
        if (ltps != null)
        {
            for (int i = 0; i < ltps.size(); i++)
            {
                beanSttType bst = (beanSttType) ltps.get(i);
                myTreeNode mst = new myTreeNode("[分类]" + bst.getTypeName(), 0, 0);
                List lrs = brd.getResListByType(bst.getOID(), false);
                if (lrs != null)
                {
                    for (int j = 0; j < lrs.size(); j++)
                    {
                        beanCtxBaseResource bcbr = (beanCtxBaseResource) lrs.get(j);
                        myTreeNode mrs = new myTreeNode(bcbr.getResName(), bcbr.getOID(), 1);
                        mst.add(mrs);
                    }
                }
                mrt.add(mst);
            }
        }
        dlgSelectTreeNode dlg = new dlgSelectTreeNode(null, true, mrt);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            return dlg.getSelect();
        }
        dlg.dispose();
        dlg = null;
        return null;
    }
}

//
//LOG
//TIME:
//REC:
//
