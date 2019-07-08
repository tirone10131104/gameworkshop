package dev.xlin.gameworkshop.progs.fortest;

import dev.xlin.tols.data.annotations.JDBId;
import dev.xlin.tols.data.annotations.JDBTable;
import dev.xling.jmdbs.MDBDataObject;

@JDBTable(tableName = "tb_test_dss")
public class beanTestDSS implements MDBDataObject
{

    @JDBId
    private int OID = 0;
    private String testName = "";
    private int status = 0;
    private int userID = 0;
    private int targetID = 0;
    private int taskID = 0;
    private String taskInfo = "";

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

    public String getTestName()
    {
        return testName;
    }

    public void setTestName(String testName)
    {
        this.testName = testName;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public int getTargetID()
    {
        return targetID;
    }

    public void setTargetID(int targetID)
    {
        this.targetID = targetID;
    }

    public int getTaskID()
    {
        return taskID;
    }

    public void setTaskID(int taskID)
    {
        this.taskID = taskID;
    }

    public String getTaskInfo()
    {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo)
    {
        this.taskInfo = taskInfo;
    }

    @Override
    public Object copyData()
    {
        beanTestDSS bean = new beanTestDSS();
        bean.setOID(OID);
        bean.setStatus(status);
        bean.setTargetID(targetID);
        bean.setTaskID(taskID);
        bean.setTaskInfo(taskInfo);
        bean.setTestName(testName);
        bean.setUserID(userID);
        return bean;
    }

    @Override
    public boolean updateData(MDBDataObject data)
    {
        beanTestDSS bean = (beanTestDSS) data;
        setTargetID(bean.getTargetID());
        setTaskID(bean.getTaskID());
        setTaskInfo(bean.getTaskInfo());
        setTestName(bean.getTestName());
        setUserID(bean.getUserID());
        return true;
    }

}
