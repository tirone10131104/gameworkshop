package dev.xlin.gameworkshop.progs.tools;

public class propertyNode
{

    private String pk = "";
    private String pv = "";

    public propertyNode(String key, String value)
    {
        pk = key;
        pv = value;
    }

    public String getKey()
    {
        return pk;
    }

    public String getValue()
    {
        return pv;
    }

    public void setKey(String key)
    {
        pk = key;
    }

    public void setValue(String value)
    {
        pv = value;
    }

}
