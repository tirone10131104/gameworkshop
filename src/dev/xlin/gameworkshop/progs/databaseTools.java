package dev.xlin.gameworkshop.progs;

import dev.xlin.tols.data.dbReturn;
import dev.xlin.tols.data.dbState;
import dev.xlin.tols.data.wakeup;

/**
 *
 * @author 三峡大学软件工程中心 刘祎鹏
 */
public class databaseTools
{

//    public static wakeup connentDB(String user, String pwd)
//    {
//        wakeup up = new wakeup(false);
//        int dtype = dbState.DBTYPE_MYSQL_5;
//        String sip = "127.0.0.1";
//        int r = up.createConnection(dtype, "jdbc:mysql://localhost:3306/", user, pwd);
//        if (r != dbReturn.CONNECTION_CREATE_SUCCESS)
//        {
//            return null;
//        }
//        int i = up.useDB("jinmao_meeting");
//        if (i != 5808)
//        {
//            return null;
//        }
//        return up;
//    }
    /**
     *
     * @return
     */
    public static wakeup connectDB()
    {
        wakeup up = new wakeup(true);
        int dtype = dbState.DBTYPE_MYSQL_5;
//        String sip = "127.0.0.1";
//        String uname = "jm@No1!";
//        String upwd = "WYbmsyZSzqxhZEDY^_^2017!";
////
//        String sip = "121.42.151.163";
//        String uname = "yang";
//        String upwd = "123456";

//        String sip = "127.0.0.1";
//        String uname = "root";
//        String upwd = "admin";
        //私有阿里云39.108.149.122
        String sip = "39.108.149.122";
        String uname = "developeID";
        String upwd = "maomao10131104LL";
        int r = up.createConnection(dtype, "jdbc:mysql://" + sip + ":3306/", uname, upwd);
        if (r != dbReturn.CONNECTION_CREATE_SUCCESS)
        {
            return null;
        }
        int i = up.useDB("ssws");
        if (i != 5808)
        {
            return null;
        }
        return up;
    }

    /**
     *
     * @param up
     */
    public static void disconnect(wakeup up)
    {
        up.close();
        return;
    }

}
