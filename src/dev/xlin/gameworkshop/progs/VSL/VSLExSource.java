/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.xlin.gameworkshop.progs.VSL;

import java.util.Random;

/**
 *
 * @author 22972
 */
public class VSLExSource
{

    private int count = 0;

    public static void main(String[] ss)
    {
        VSLExSource vex = new VSLExSource(100);
        int r = vex.caculate();
        System.err.println("vex rst = " + r);
    }

    public VSLExSource(int _count)
    {
        count = _count;
    }

    public int caculate()
    {
        if (count <= 0)
        {
            count = 1;
        }
        if (count > 100)
        {
            count = 100;
        }
        int istep = 0;
        int irst = 0;
        while (istep < count)
        {
            int i = genInt();
            irst = irst + i;
            if (irst >= 100)
            {
                irst = irst - 100;
            }
            istep = istep + 1;
        }
        return irst;
    }

    private int genInt()
    {
        Random r = new Random();
        return r.nextInt(100);
    }

}
