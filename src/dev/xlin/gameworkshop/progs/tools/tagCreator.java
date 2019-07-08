package dev.xlin.gameworkshop.progs.tools;

import dev.xlin.tols.data.wakeup;
import dev.xlin.tools.MD5Security;
import java.util.Hashtable;
import java.util.Random;

/**
 *
 * @author 刘祎鹏
 */
public class tagCreator
{

    public static String createDataTag(wakeup up, String table, String field, String prefix, String suffix, int seg, int let)
    {
        while (true)
        {
            String stg = createTag(seg, let);
            if (prefix.trim().equals("") == false)
            {
                stg = prefix + "-" + stg;
            }
            if (suffix.trim().equals("") == false)
            {
                stg = stg + "-" + suffix;
            }
            String sql = "select " + field + " from " + table + " where " + field + " = '" + stg + "'";
            try
            {
                Hashtable[] hts = up.querySQL(sql);
                if (hts == null)
                {
                    return stg;
                }
            }
            catch (Exception excp)
            {
                excp.printStackTrace();
                return null;
            }
        }
    }

    public static String createDataTag(wakeup up, String table, String field, int seg, int let)
    {
        return createDataTag(up, table, field, "", "", seg, let);
    }

    public static String createTag(int seg, int let)
    {
        if (seg <= 0 || seg > 10)
        {
            return "";
        }
        if (let <= 0 || let > 16)
        {
            return "";
        }
        String sr = "";
        Random r = new Random();
        for (int i = 0; i < seg; i++)
        {
            String s = createSeg(r, let);
            if (i != 0)
            {
                sr = sr + "-";
            }
            sr = sr + s;
        }
        return sr.toUpperCase();
    }

    private static String createSeg(Random r, int let)
    {
        long l = r.nextLong();
        String sm5 = MD5Security.MD5Encode(l + "");
        return sm5.substring(0, let);
    }
}

//
//LOG
//TIME:
//REC:
//
