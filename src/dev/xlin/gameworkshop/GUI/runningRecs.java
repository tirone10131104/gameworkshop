package dev.xlin.gameworkshop.GUI;

import dev.xlin.tols.xml.CO2;
import dev.xlin.tols.xml.xmlRight;
import java.util.HashMap;
import java.util.TimerTask;
import javax.swing.JFrame;
import org.w3c.dom.Document;

/**
 *
 * @author 刘祎鹏
 */
public class runningRecs extends TimerTask
{

    private frmMain frm = null;
    public static boolean softInit = true;
    private static int isect = 10;
    private static HashMap h = null;
    private static int tm = 0;
    private static int ct = 0;

    public runningRecs(frmMain _frmMain)
    {
        frm = _frmMain;
    }

    @Override
    public void run()
    {
        try
        {
            xmlRight xr = new xmlRight();
            if (softInit)
            {
                boolean b = xr.loadXmlFiel("rec.xml");
                if (b)
                {
                    CO2 c = new CO2();
                    h = (HashMap) c.revertXmlToObject(xr.getDocument());
                    tm = (int) h.get("time");
                    ct = (int) h.get("count");
                }
                else
                {
                    h = new HashMap();
                }
                ct = ct + 1;
                softInit = false;
            }
            tm = tm + 1;
            h.put("time", tm);
            h.put("count", ct);
            CO2 csave = new CO2();
            Document docSav = csave.transObjectToXML(h);
            frm.postRunningRec(tm, ct);

            isect++;
            if (isect >= 3)
            {
                xmlRight xrSave = new xmlRight();
                xrSave.setDocument(docSav);
                boolean bsv = xrSave.storeXMLfile("rec.xml");
                isect = 0;
            }

        }
        catch (Exception excp)
        {
            excp.printStackTrace();
        }

    }

}

//
//LOG
//TIME:
//REC:
//
