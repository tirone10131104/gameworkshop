package dev.xlin.gameworkshop.progs.fortest;

import dev.xling.jmdbs.MDBPackage;
import dev.xling.jmdbs.MDBRuntime;
import dev.xling.jmdbs.MDBValidator;

public class tbTestValidator implements MDBValidator
{
    private static int TPID = 98765;
    @Override
    public long checkRegiste(MDBPackage mdbp)
    {
        System.err.println("..checkRegiste");
        beanTestDSS bean = (beanTestDSS) mdbp.getDataObject();
        //基本检查1
        if (bean.getOID() < 0)
        {
            return -1;
        }
        //基本检查2
        if (bean.getTestName().trim().equals(""))
        {
            return -2;
        }
        //反重复检查1 
        MDBRuntime.getDataPackage( TPID, 0);
        
        //反重复检查2

        return 0;
    }

    @Override
    public long checkUpdate(MDBPackage mdbp)
    {
        System.err.println("..checkUpdate");
        return 0;
    }

    @Override
    public long checkDelete(MDBPackage mdbp)
    {
        System.err.println("..checkDelete");
        return 0;
    }

}



























