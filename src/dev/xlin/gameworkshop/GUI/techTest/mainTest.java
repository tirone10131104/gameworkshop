package dev.xlin.gameworkshop.GUI.techTest;

public class mainTest
{

    public static void main(String[] args)
    {
        runtest r = new runtest();
        Thread t = new Thread(r);
        t.start();
        try
        {
            Thread.sleep(2000);
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
        System.err.println("try stop ");
        t.interrupt();
        try
        {
            Thread.sleep(2000);
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
        System.err.println("...t.ali = " + t.isAlive() + " intr = " + t.isInterrupted());
    }
}
