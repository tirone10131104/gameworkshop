package dev.xlin.gameworkshop.progs.foundation;

import dev.xlin.gameworkshop.progs.foundation.interfaces.iAdtXMLNode;
import dev.xlin.gameworkshop.progs.foundation.interfaces.iClone;
import dev.xlin.tols.xml.xmlRight;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author 刘祎鹏
 */
public class beanDataColumn implements iClone, iAdtXMLNode
{

    private String columnName = "";
    private ArrayList datas = new ArrayList();
    private String columnTag = "";
    private int OID = 0;

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("columnName", columnName);
        e.setAttribute("columnTag", columnTag);
        e.setAttribute("OID", OID + "");
        for (int i = 0; i < datas.size(); i++)
        {
            iAdtXMLNode ian = (iAdtXMLNode) datas.get(i);
            Element ecd = ian.transToXmlElement(xr);
            e.appendChild(ecd);
        }
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try
        {
            columnName = e.getAttribute("columnName");
            columnTag = e.getAttribute("columnTag");
            OID = foundationUtils.readElementValueAsInt(e, "OID");
            NodeList ncls = e.getChildNodes();
            datas.clear();
            for (int i = 0; i < ncls.getLength(); i++)
            {
                Element ecd = (Element) ncls.item(i);
                Object ond = foundationUtils.createDataElementByETAG(ecd.getNodeName());
                if (ond != null)
                {
                    iAdtXMLNode iaxn = (iAdtXMLNode) ond;
                    boolean b = iaxn.revertFromXmlElement(ecd);
                    if (b)
                    {
                        datas.add(iaxn);
                    }
                }
            }
            return true;
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            return false;
        }
    }

    @Override
    public String _getXmlNodeName()
    {
        return "B_COL";
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    protected ArrayList getDatas()
    {
        return datas;
    }

    protected void setDatas(ArrayList datas)
    {
        this.datas = datas;
    }

    public String getColumnTag()
    {
        return columnTag;
    }

    public void setColumnTag(String columnTag)
    {
        this.columnTag = columnTag;
    }

    @Override
    public Object cloneMe()
    {
        beanDataColumn bean = new beanDataColumn();
        bean.setColumnName(getColumnName());
        bean.setColumnTag(getColumnTag());
        ArrayList arl = new ArrayList();
        for (int i = 0; i < getDatas().size(); i++)
        {
            iClone ico = (iClone) getDatas().get(i);
            arl.add(ico.cloneMe());
        }
        bean.setDatas(arl);
        bean.setOID(OID);
        return bean;
    }

    public int getOID()
    {
        return OID;
    }

    public void setOID(int OID)
    {
        this.OID = OID;
    }

}

//
//LOG
//TIME:
//REC:
//
