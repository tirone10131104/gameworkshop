package testPack.MDBTest;

import dev.xlin.gameworkshop.progs.fortest.beanTestDSS;
import dev.xlin.tols.data.DataConnectionInfo;
import dev.xlin.tols.data.DataConnections;
import dev.xlin.tols.data.dbState;
import dev.xling.jmdbs.MDBPackage;
import dev.xling.jmdbs.MDBRuntime;

/**
 * MDB的普通测试文件
 *
 * @author 22972
 */
public class MDBTest
{

    public static void main(String[] ss)
    {
        MDBTest mtest = new MDBTest();
        DataConnectionInfo DCI = new DataConnectionInfo();
        DCI.setAddress("39.108.149.122");
        DCI.setDBType(dbState.DBTYPE_MYSQL_8);
        DCI.setDatabaseName("ssws");
        DCI.setUserName("yang");
        DCI.setUserPwd("123456");
        DataConnections.setDataInfo(DCI);
        mtest.testVals();
    }

    public void testVals()
    {
        MDBRuntime mdr = new MDBRuntime();
        System.err.println("..testVal .MDRT started");

        beanTestDSS btd = new beanTestDSS();
        btd.setOID(-1);
        MDBPackage mpk = new MDBPackage(98765, btd);
        long regR = mdr.registeMDBPackage(mpk);
        System.err.println("regR = " + regR);
    }

}
