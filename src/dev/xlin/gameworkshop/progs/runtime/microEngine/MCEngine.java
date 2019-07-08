package dev.xlin.gameworkshop.progs.runtime.microEngine;

import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.gameworkshop.progs.iReturn;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微处理引擎
 *
 * @author
 */
public class MCEngine implements Runnable
{

    public MCEngine(BeanMCEngineConfigMain _bean)
    {
        beanConfig = _bean;
        _readConfig();
    }

    //从BEAN CFG中获取配置数据，装载到MCE类成员变量，以便代码中快速获取
    private void _readConfig()
    {
        MCEID = beanConfig.getOID();
        maxTPS = beanConfig.getMaxTps();
        maxTickTime = 1000 / maxTPS;
        emptyWaitLimit = beanConfig.getEmptyWaitLong();
        if (beanConfig.getEmptyMethod() == iConst.MCES_EMPTY_MTD_SLEEP)
        {
            emptySleeping = true;
        }
        else if (beanConfig.getEmptyMethod() == iConst.MCES_EMPTY_MTD_STOP)
        {
            emptySleeping = false;
        }
        instanceLimit = beanConfig.getInstanceLimit();
    }

    private BeanMCEngineConfigMain beanConfig = null;
    //MCE引擎自身标示ID 
    private int MCEID = 0;
    //MCE的运行时进程ID
    private int instanceID = 0;
    //每秒最大TICK数量
    private int maxTPS = 10;
    //每个TICK的间隔时间
    private int maxTickTime = 1000 / maxTPS;

    private List tasks = new ArrayList();

    private Thread mceThreadMain = null;

    private long nowTick = 0;
    //当前MCE引擎工作状态
    private int status = 0;

    //空闲等候(单位毫秒）
    private int emptyWaitLimit = 3000;

    //空闲等候
    private boolean emptySleeping = false;

    //已空闲等候的时长
    private int emptyTimePeriod = 0;

    //任务ID映射
    private ConcurrentHashMap hTaskMapping = new ConcurrentHashMap();

    //主动线程中断标志
    private boolean reqStop = false;
    private int instanceLimit = 0;
 
    public int startEngine()
    {
        mceThreadMain = new Thread(this);
        mceThreadMain.start();
        return status;
    }

    @Override
    public void run()
    {
        while (true)
        {
            //记录本轮TICK起步时间点
            long inTime = new Date().getTime();
            //当前TICK时间计入，任务调度时需要用这时间点计算时间流逝长度。
            nowTick = inTime;
            if (tasks.size() != 0)
            {
                //当前任务列表有任务，则：
                //空闲时间清零
                emptyTimePeriod = 0;
            }
            //开发分发任务
            for (int i = 0; i < tasks.size(); i++)
            {
                iMCETask itask = (iMCETask) tasks.get(i);
                if (itask.getStatus() == iMCETask.TASK_STATUS_READY)
                {
                    itask.tick(nowTick);
                }
            }
            //任务调度执行完成，遍历一次任务列表，把标记为申请删除的，从任务执行列表中清除掉，避免任务进入下一轮
            for (int i = 0; i < tasks.size(); i++)
            {
                iMCETask imtf = (iMCETask) tasks.get(i);
                if (imtf.isRequestDelete())
                {
                    tasks.remove(i);
                    i = i - 1;
                }
            }
            long outTime = new Date().getTime();
            long period = outTime - inTime;
            long tickWait = maxTickTime - period;
            if (period < maxTickTime)
            {
                //计算当前线程需要休眠的时间长度（毫秒数）  
                try
                {
                    Thread.sleep(tickWait);
                }
                catch (Exception excp)
                {
                    Thread.interrupted();
                    if (reqStop)
                    {
                        System.err.println("MCE:" + MCEID + " 申请退出操作成功");
                        break;
                    }
                }
            }
            if (tasks.isEmpty())
            {
                //任务空闲状态
                //空闲时间段增加一个当前TICK的等候时长
                if (tickWait > 0)
                {
                    emptyTimePeriod = emptyTimePeriod + (int) tickWait;
                }
                //等候超时，根据配置进行动作
                if (emptyTimePeriod > emptyWaitLimit)
                {
                    if (beanConfig.getEmptyMethod() == iConst.MCES_EMPTY_MTD_STOP)
                    {
                        System.err.println("MCE::" + MCEID + "空闲等候期结束，自动关闭");
                        break;
                    }
                    else
                    {
                        //当空闲等候时长，大于等候限制的时候，本线程进入休眠
                        synchronized (this)
                        {
                            try
                            {
                                emptySleeping = true;
                                System.err.println("主线程MCE：" + MCEID + "进入自主休眠状态");
                                wait();
                            }
                            catch (Exception excp)
                            {
                                System.err.println("MCE线程：" + MCEID + "自主休眠中断");
                                notify();
                                Thread.interrupted();
                                if (reqStop)
                                {
                                    System.err.println("MCE(自主休眠中):" + MCEID + " 申请退出操作成功");
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int getMCEID()
    {
        return MCEID;
    }

    public int stopEngine()
    {
        if (tasks.isEmpty() == false)
        {
            return iReturn.MCES_CANT_STOP_MCE_WHEN_NOT_EMPTY;
        }
        mceThreadMain.interrupt();
        reqStop = true;
        return iReturn.SUCCESS;
    }

    //内部创建一个任务ID 
    private int _createTaskID()
    {
        Random r = new Random();
        while (true)
        {
            int id = r.nextInt();
            if (id <= 0)
            {
                continue;
            }
            if (hTaskMapping.containsKey(id) == false)
            {
                return id;
            }
        }
    }

    public long getNowTick()
    {
        return nowTick;
    }

    public boolean isEmptySleeping()
    {
        return emptySleeping;
    }

    /**
     * 向微引擎放入一个任务
     *
     * @param itask
     * @return
     */
    public int putTask(iMCETask itask)
    {
        if (itask != null)
        {
            if (tasks.contains(itask))
            {
                //避免任务重复放置
                return 0;
            }
            int tid = _createTaskID();
            itask.receiveTaskID(tid);
            hTaskMapping.put(tid, itask);
            tasks.add(itask);
            if (emptySleeping)
            {
                //如果当前任务正在休眠中。则打断休眠
                mceThreadMain.interrupt();
            }
            return tid;
        }
        return 0;
    }

    public int getInstanceLimit()
    {
        return instanceLimit;
    }

    public int getInstanceID()
    {
        return instanceID;
    }

}
