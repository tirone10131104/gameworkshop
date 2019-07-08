package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablock;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablockDefine;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanObjectClass;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXML;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iDatablockInitInstance;
import dev.xlin.tols.data.JDBSession;
import dev.xlin.tols.data.jcommon;
import dev.xlin.tols.data.session;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.JDAO;
import dev.xlin.tols.interfaces.iDAO;
import dev.xlin.tols.xml.xmlRight;
import dev.xlin.tools.OIDCreator;
import dev.xlin.tools.codeTools;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;

/**
 *
 * @author 刘祎鹏
 */
public class datablockService
{

    private wakeup up = null;
    private session sn = null;

    public datablockService(wakeup _up)
    {
        up = _up;
        sn = new session(up);
    }

    /**
     * 通常，多种数据区块的引导数据被整合为一个JSON字符串，此函数通过解析这个字符串，并通过数据区块类型DTTP，从中取得正确的数据区块引导OID
     *
     * @param sinit
     * @param dttp
     * @return
     */
    public int loadDatablockIndexByType(String sinit, int dttp)
    {
        try
        {
            JSONObject jso = JSONObject.fromObject(sinit);
            int idx = jso.getInt(dttp + "");
            return idx;
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return -1;
        }
    }

    public String initDatablockStruct(ArrayList<Integer> aheaders, boolean inDbTask) throws Exception
    {
        try
        {
            int[] ids = new int[aheaders.size()];
            for (int i = 0; i < aheaders.size(); i++)
            {
                ids[i] = aheaders.get(i);
            }
            return initDatablockProg(ids, inDbTask);
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            throw excp;
        }
    }

    private String initDatablockProg(int[] ids, boolean inDbTask) throws Exception
    {
        try
        {
            String sr = "";
            if (inDbTask == false)
            {
                up.setAutoCommit(false);
            }
            JSONObject jso = new JSONObject();
            for (int i = 0; i < ids.length; i++)
            {
                int id = ids[i];
                long tgid = makeDatablock(id);
                if (tgid < 0)
                {
                    up.roolBack();
                    up.setAutoCommit(true);
                    throw new Exception("数据：" + id + "创建初始化失败");
                }
                jso.put(id, tgid);
            }
            if (inDbTask == false)
            {
                up.commit();
                up.setAutoCommit(true);
            }
            return jso.toString();
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            up.roolBack();
            up.setAutoCommit(true);
            throw excp;
        }
    }

    public String initDatablockStruct(int oclsid, boolean inDbTask) throws Exception
    {
        try
        {
            objectClassDefine ocd = new objectClassDefine(up);
            BeanObjectClass bean = (BeanObjectClass) ocd.getRecordByID(oclsid);
            int[] ids = codeTools.convertStrToArr(bean.getClassFuncs());
            return initDatablockProg(ids, inDbTask);
        }
        catch (Exception excp)
        {
            throw excp;
        }
    }

    private long makeDatablock(int dtpid)
    {
        iAdtXML iamx = null;
        datablockDefine dbdef = new datablockDefine(up);
        BeanDatablockDefine bdf = (BeanDatablockDefine) dbdef.getRecordByID(dtpid);
        try
        {
            Object oc = Class.forName(bdf.getDbAdtClass()).newInstance();
            iDatablockInitInstance idbins = (iDatablockInitInstance) oc;
            idbins.initDatablcok(up);
            iamx = (iAdtXML) idbins;
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return -1;
        }
        if (iamx == null)
        {
            return -1;
        }
        xmlRight xr = iamx.transToXML();
        String sxml = xr.transformToString();
        datablockService dbs = new datablockService(up);
        long oid = dbs.saveData(sxml);
        return oid;
    }

    public long saveData(String data )
    {
        BeanDatablock bean = new BeanDatablock();
        long oid = OIDCreator.createOID_Long_simple(up, "tb_datablock", "OID");
        bean.setOID(oid);
        bean.setDatablock(data);
        JDBSession jsn = new JDBSession (up);
        int r = jsn.save(bean);
        if (r == JDAO.OPERATE_SUCCESS)
        {
            return oid;
        }
        else
        {
            return -r;
        } 
    }

    public BeanDatablock getDatabean(long oid)
    {
        String sql = "select * from tb_datablock where OID = " + oid;
        List ls = sn.querySQL(sql, BeanDatablock.class);
        if (ls == null)
        {
            return null;
        }
        return (BeanDatablock) ls.get(0);
    }

    public String loadData(long oid)
    { 
        JDBSession jsn = new JDBSession (up);
        BeanDatablock bean = (BeanDatablock) jsn.get(BeanDatablock.class, oid);
        if (bean == null )
        {
            return null;
        }
        return bean.getDatablock();
    }
 
    public int updateData(long oid, String data)
    {
        BeanDatablock bean = getDatabean(oid);
        if (bean == null)
        {
            return iDAO.OBJECT_RECORD_NOTEXIST;
        }
        bean.setDatablock(data); 
        JDBSession jsn = new JDBSession (up);
        return jsn.update(bean);
    }

    public int removeData(long oid)
    {
        String sql = "delete from tb_datablock where OID = " + oid;
        JDBSession jsn = new JDBSession (up);
        return jsn.execute(sql);
    }

}

//
//LOG
//TIME:
//REC:
//
