package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.databaseTools;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanDatablockDefine;
import dev.xlin.tols.data.wakeup;
import org.w3c.dom.Element;

/**
 *
 * @author 刘祎鹏
 */
public class foundationUtils
{

    public static double readElementValueAsDouble(Element e, String key)
    {
        try
        {
            return Double.parseDouble(e.getAttribute(key));
        }
        catch (Exception excp)
        {
            return 0;
        }
    }

    public static int readElementValueAsInt(Element e, String key)
    {
        try
        {
            return Integer.parseInt(e.getAttribute(key));
        }
        catch (Exception excp)
        {
            return 0;
        }
    }

    public static Object createDataElementByETAG(String tag )
    {
        wakeup up = databaseTools.connectDB();
        tag = tag.trim();
        datablockDefine dbd = new datablockDefine(up);
        BeanDatablockDefine bdd = dbd.getDataDefineByXmlNodeTag(tag);
        if (bdd == null)
        {
            databaseTools.disconnect(up);
            return null;
        }
        try 
        {
            Class cls = Class.forName(bdd.getDataBeanClass());
            Object obean = cls.newInstance();
            databaseTools.disconnect(up);
            return obean;
        }
        catch(Exception excp )
        {
            excp.printStackTrace();
            databaseTools.disconnect(up);
            return null;
        }
    }

}

//
//LOG
//TIME:
//REC:
//
