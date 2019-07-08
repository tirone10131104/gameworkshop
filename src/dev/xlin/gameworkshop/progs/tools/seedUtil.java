package dev.xlin.gameworkshop.progs.tools;

import java.util.Random;
import java.util.stream.LongStream;

/**
 *
 * @author 刘祎鹏
 */
public class seedUtil
{

    private static long tryPlusSeedParam(long seed, int param)
    {
        try
        {
            if (Long.MAX_VALUE - param >= seed)
            {
                return seed + param;
            }
            else
            {
                return Long.MAX_VALUE - seed + param;
            }
        }
        catch (Exception excp)
        {
            return 0;
        }
    }
    //线性随机种子

    public static long getLinearSeed(long seed, int index)
    {
        long lsd =  tryPlusSeedParam(seed, index);
        Random r = new Random(lsd);
        return r.nextLong();
    }

    //矩阵随机种子
    public static long getMatrixSeed(long seed, int x, int y)
    {
        long sdx = getLinearSeed(seed, x);
        long sdy = getLinearSeed(sdx, 2 * y);
        return sdy;
    }

    //3D随机种子
    public static long get3DSeed(long seed, int x, int y, int z)
    {
        long smx = getMatrixSeed(seed, x, y);
        long s3d = getLinearSeed(smx,3 *z);
        return s3d;
    }

}

//
//LOG
//TIME:
//REC:
//
