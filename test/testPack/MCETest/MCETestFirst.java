package testPack.MCETest;

import dev.xlin.gameworkshop.progs.databaseTools;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.runtime.microEngine.BeanMCEngineConfigMain;
import dev.xlin.gameworkshop.progs.runtime.microEngine.MCEConfigDAO;
import dev.xlin.gameworkshop.progs.runtime.microEngine.MCERuntimeContainer;
import dev.xlin.gameworkshop.progs.runtime.microEngine.MCETaskExample;
import dev.xlin.gameworkshop.progs.runtime.microEngine.MCEngine;
import dev.xlin.tols.data.wakeup;

/**
 *
 * @author 刘祎鹏
 */
public class MCETestFirst
{

    private String simpTest = "MCE_TEST";

    public static void main(String[] ss)
    {
        MCETestFirst test = new MCETestFirst();
        test.testMCEReflect();
//        test.testMCEMultyInsts();
//        test.testMCEAutoStop();
//        test.testReqMCEStop();
//        test.testPutTaskFromCNT();
//        test.testAntiRepeatMCE();
//        test.testAntiRepeatTask();
//        test.testMultyMCEs();
//        test.startMCE();
    }

    public  void testMCEReflect()
    {
        BeanMCEngineConfigMain bean = loadTestMCE(simpTest);
        MCEngine mce1 = new MCEngine(bean);
        MCERuntimeContainer mrc = new MCERuntimeContainer();
        int r1 = mrc.registMCEngine(mce1);
        System.err.println("r1 = " + r1 +" mc .instid = " + mce1.getInstanceID() +" mce config id = " + mce1.getMCEID());
    }

    private void testMCEMultyInsts()
    {
        BeanMCEngineConfigMain bean = loadTestMCE(simpTest);
        MCEngine mce1 = new MCEngine(bean);
        MCEngine mce2 = new MCEngine(bean);
        MCEngine mce3 = new MCEngine(bean);
        MCERuntimeContainer mrc = new MCERuntimeContainer();
        int r1 = mrc.registMCEngine(mce1);
        System.err.println("r1 = " + r1);
        int r2 = mrc.registMCEngine(mce2);
        System.err.println("r2 = " + r2);
        int r3 = mrc.registMCEngine(mce3);
        System.err.println("r3 = " + r3);
    }

    //测试自动关闭
    private void testMCEAutoStop()
    {
        BeanMCEngineConfigMain bean = new BeanMCEngineConfigMain();
        bean.setEmptyMethod(iConst.MCES_EMPTY_MTD_STOP);
        MCEngine mce = new MCEngine(bean);
        MCETaskExample mxa = new MCETaskExample();
        mce.putTask(mxa);
        mce.startEngine();
    }

    //从数据库中获取一个配置
    //"MCE_TEST"
    private BeanMCEngineConfigMain loadTestMCE(String tag)
    {
        wakeup up = databaseTools.connectDB();
        MCEConfigDAO mcd = new MCEConfigDAO(up);
        BeanMCEngineConfigMain bean = mcd.getMCEConfigByTag(tag);
        up.close();
        return bean;
    }

    private void testReqMCEStop()
    {
        BeanMCEngineConfigMain bean = loadTestMCE(simpTest);
        MCEngine mce = new MCEngine(bean);
        MCETaskExample mxa = new MCETaskExample();
        mce.putTask(mxa);
        mce.startEngine();

        try
        {
            Thread.sleep(10000);
        }
        catch (Exception excp)
        {

        }
        int r = mce.stopEngine();
        System.err.println("r = " + r);
    }

    private void testPutTaskFromCNT()
    {
        BeanMCEngineConfigMain bean = loadTestMCE(simpTest);
        MCEngine mce1 = new MCEngine(bean);
        MCERuntimeContainer mcrc = new MCERuntimeContainer();
        int mcid = mcrc.registMCEngine(mce1);
        MCETaskExample mxa = new MCETaskExample();
        int tid = mcrc.putTask(mxa, mcid);
        System.err.println("tid = " + tid);

    }

    //测试反制重复放MCE到容器里
    private void testAntiRepeatMCE()
    {
        BeanMCEngineConfigMain bean = loadTestMCE(simpTest);
        MCEngine mce1 = new MCEngine(bean);
        MCERuntimeContainer mcrc = new MCERuntimeContainer();
        int r = mcrc.registMCEngine(mce1);
        System.err.println("r1 = " + r);
        r = mcrc.registMCEngine(mce1);
        System.err.println("r2 = " + r);

        mcrc.registMCEngine(mce1);
    }

    //测试能否反制重复任务放入（针对一个MCE）
    //但是把一个任务放入不同的MCE，那这个管不着了。
    private void testAntiRepeatTask()
    {
        BeanMCEngineConfigMain bean = loadTestMCE(simpTest);
        MCEngine mce1 = new MCEngine(bean);
        MCETaskExample exa1a = new MCETaskExample();
        int r1 = mce1.putTask(exa1a);
        System.err.println("R1 = " + r1);
        int r2 = mce1.putTask(exa1a);
        System.err.println("R2 = " + r2);
    }

    //测试同时存在多个MCE实例
    private void testMultyMCEs()
    {
        BeanMCEngineConfigMain bean = loadTestMCE(simpTest);
        MCEngine mce1 = new MCEngine(bean);
        MCEngine mce2 = new MCEngine(bean);

        MCETaskExample exa1a = new MCETaskExample();
        MCETaskExample exa2a = new MCETaskExample();
        mce1.putTask(exa1a);
        mce2.putTask(exa2a);

        mce1.startEngine();
        mce2.startEngine();

        try
        {
            Thread.sleep(5000);
            System.out.println("准备加任务-MCE2");
            MCETaskExample exa2 = new MCETaskExample();
            mce2.putTask(exa2);
        }
        catch (Exception excp)
        {
        }
        try
        {
            Thread.sleep(1000);
            System.out.println("准备加任务-MCE1");
            MCETaskExample exa21 = new MCETaskExample();
            mce1.putTask(exa21);
        }
        catch (Exception excp)
        {
        }
    }

    //测试单个MCE运行流程
    private void startMCE()
    {
        BeanMCEngineConfigMain bean = loadTestMCE(simpTest);
        MCEngine mce = new MCEngine(bean);
        for (int i = 0; i < 3; i++)
        {
            MCETaskExample exa = new MCETaskExample();
            mce.putTask(exa);
        }
        mce.startEngine();

        try
        {
            Thread.sleep(5000);
            System.out.println("准备加任务打断MCE");
            MCETaskExample exa2 = new MCETaskExample();
            mce.putTask(exa2);
        }
        catch (Exception excp)
        {
        }

    }
}
