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
public class beanDataPage implements iClone, iAdtXMLNode
{

    private String pageName = "";
    private ArrayList columns = new ArrayList();
    private String pageTag = "";

    @Override
    public Element transToXmlElement(xmlRight xr)
    {
        Element e = xr.createElement(_getXmlNodeName());
        e.setAttribute("pageName", pageName);
        e.setAttribute("pageTag", pageTag);
        for (int i = 0; i < columns.size(); i++)
        {
            beanDataColumn bcol = (beanDataColumn) columns.get(i);
            Element ecol = bcol.transToXmlElement(xr);
            e.appendChild(ecol);
        }
        return e;
    }

    @Override
    public boolean revertFromXmlElement(Element e)
    {
        try
        {
            pageName = e.getAttribute("pageName");
            pageTag = e.getAttribute("pageTag");
            beanDataColumn bdc = new beanDataColumn();
            NodeList nls = e.getElementsByTagName(bdc._getXmlNodeName());
            columns.clear();
            for (int i = 0; i < nls.getLength(); i++)
            {
                Element ecol = (Element) nls.item(i);
                beanDataColumn bcol = new beanDataColumn();
                boolean b = bcol.revertFromXmlElement(ecol);
                if (b)
                {
                    columns.add(bcol);
                }
            }
            return true;
        }
        catch (Exception excp)
        {
            return false;
        }
    }

    @Override
    public String _getXmlNodeName()
    {
        return "B_PAGE";
    }

    public String getPageName()
    {
        return pageName;
    }

    public void setPageName(String pageName)
    {
        this.pageName = pageName;
    }

    protected ArrayList getColumns()
    {
        return columns;
    }

    protected void setColumns(ArrayList columns)
    {
        this.columns = columns;
    }

    public String getPageTag()
    {
        return pageTag;
    }

    public void setPageTag(String pageTag)
    {
        this.pageTag = pageTag;
    }

    @Override
    public Object cloneMe()
    {
        beanDataPage bean = new beanDataPage();
        bean.setPageName(pageName);
        bean.setPageTag(pageTag);
        ArrayList arl = new ArrayList();
        for (int i = 0; i < columns.size(); i++)
        {
            beanDataColumn bdc = (beanDataColumn) columns.get(i);
            arl.add(bdc.cloneMe());
        }
        bean.setColumns(arl);
        return bean;
    }

}

//
//LOG
//TIME:
//REC:
//
