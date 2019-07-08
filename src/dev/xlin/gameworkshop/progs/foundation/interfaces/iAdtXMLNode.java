package dev.xlin.gameworkshop.progs.foundation.interfaces;

import dev.xlin.tols.xml.xmlRight;
import org.w3c.dom.Element;

/**
 *
 * @author 刘祎鹏
 */
public interface iAdtXMLNode
{

    public Element transToXmlElement(xmlRight xr);

    public boolean revertFromXmlElement(Element e);
    public  String _getXmlNodeName ();
    
}

//
//LOG
//TIME:
//REC:
//
