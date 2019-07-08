package testPack.MDBTest;

import dev.xlin.gameworkshop.progs.databaseTools;
import dev.xlin.tols.data.DataConnections;
import dev.xlin.tols.data.JDBSession;
import dev.xlin.tols.data.wakeup;
import dev.xling.jmdbs.DSSRuntime;
import dev.xling.jmdbs.MDBPackage;
import dev.xling.jmdbs.MDBRuntime;
import java.util.ArrayList;

/**
 * 测试DSS一套流程下地
 *
 * @author 22972
 */
public class DSSTest
{

    public static void main(String[] ss)
    {
        DSSTest dst = new DSSTest();
        wakeup up = DataConnections.requestConnection();
        up.close();
    }
 
}
