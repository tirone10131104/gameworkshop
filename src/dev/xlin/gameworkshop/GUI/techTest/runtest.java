package dev.xlin.gameworkshop.GUI.techTest;

import static java.lang.System.in;
import java.util.Scanner;

/**
 *
 * @author 22972
 */
public class runtest implements Runnable
{

    private boolean stopFlag = false;

    @Override
    public void run()
    {
        System.err.println("in ");
        while (true)
        {
            if (stopFlag)
            {
                break;
            }
            try
            {
                Thread.currentThread().sleep(10000);
            }
            catch (Exception excp)
            {
                excp.printStackTrace();
                System.err.println(".....in excp .stopped");
                break;
            }
            System.err.println("11233");
        }
        System.err.println("..itrpt = " + Thread.currentThread().isInterrupted() +" alive = " + Thread.currentThread().isAlive());
        System.err.println(".thread end ");
    }

    public void reqeustStop()
    {
        stopFlag = true;
    }

}
