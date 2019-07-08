package dev.xlin.gameworkshop.progs.tools;

import java.util.Random;

/**
 *
 * @author 刘祎鹏
 */
public class randomUtil
{

    public static double genarate2DValue(long seed, int x, int y)
    {  
        long nsd = seedUtil.getMatrixSeed(seed, x, y); 
        Random r = new Random(nsd);
        return r.nextDouble();
    }

}

//
//LOG
//TIME:
//REC:
//
