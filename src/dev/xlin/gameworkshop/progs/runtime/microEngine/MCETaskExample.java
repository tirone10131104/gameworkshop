package dev.xlin.gameworkshop.progs.runtime.microEngine;

import java.util.Random;

/**
 *
 * @author 22972
 */
public class MCETaskExample implements iMCETask, Runnable
{
    
    private int data = 0;
    private int count = 0;
    private boolean reqDelete = false;
    private int taskStatus = 0;
    private int taskID = 0;

    public MCETaskExample()
    {
        Random r = new Random();
        data = r.nextInt();
    }

    @Override
    public void initTask(Object o)
    {
        taskStatus = TASK_STATUS_READY;
    }

    @Override
    public int getTaskID()
    {
        return taskID;
    }

    @Override
    public void tick(long t)
    {
        if (taskStatus != TASK_STATUS_READY)
        {
            return;
        }
        taskStatus = TASK_STATUS_PROCESSING;
        data = data + 1;
        count = count + 1;
//        try
//        {
//            Thread.sleep(1000);
//        }
//        catch (Exception excp)
//        {
//
//        }
        if (count >= 10)
        {
            reqDelete = true;
            System.out.println(".task:" + getTaskID() + " 处理完结，申请退出序列");
        }
        taskStatus = TASK_STATUS_READY;
    }

    @Override
    public int getStatus()
    {
        return taskStatus;
    }

    @Override
    public boolean isRequestDelete()
    {
        if (reqDelete)
        {
            System.err.println("tid = " + taskID +" REQ DEL");
        }
        return reqDelete;
    }

    @Override
    public void run()
    {
        taskStatus = TASK_STATUS_PROCESSING;
        data = data + 1;
        System.err.println("TASK.= " + data);
        try
        {
            Thread.sleep(2000);
        }
        catch (Exception excp)
        {

        }
        if (data >= 10)
        {
            reqDelete = true;
        }
        taskStatus = TASK_STATUS_READY;
    }

    @Override
    public void receiveTaskID(int id)
    {
        taskID = id;
    }

    @Override
    public double getProgress()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getTaskData()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
