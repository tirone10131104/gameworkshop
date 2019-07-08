package dev.xlin.gameworkshop.progs.runtime.microEngine;

/**
 * 微引擎任务标准接口格式
 *
 * @author 刘祎鹏
 */
public interface iMCETask
{

    public static final int TASK_STATUS_NEW = -1;
    public static final int TASK_STATUS_READY = 0;
    public static final int TASK_STATUS_PROCESSING = 1;

    /**
     * 带数据初始化任务
     *
     * @param o
     */
    public void initTask(Object o);

    /**
     * 获取本任务在引擎任务列表中的注册ID
     *
     * @return
     */
    public int getTaskID();

    public void receiveTaskID(int id);

    /**
     *
     * @param t
     */
    public void tick(long t);

    /**
     * 获取任务状态
     *
     * @return
     */
    public int getStatus();

    /**
     * 是否向MCE申请任务删除，当函数返回TRUE的时候，引擎在每个TICK结束的时候，删除掉任务节点
     *
     * @return
     */
    public boolean isRequestDelete();

    public double getProgress();

    public Object getTaskData();

}
