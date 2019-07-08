package dev.xlin.gameworkshop.progs.foundation.interfaces;

import dev.xlin.gameworkshop.progs.foundation.beanDataPage;
import java.util.List;

public interface iDataPageOper
{

    public List getPageList();

    public beanDataPage getDataPageByIndex(int idx);

    public beanDataPage getDataPageByTag(String tag);

    public int appendDataPage(beanDataPage bean);

    public int updatePage(String name, int idx);

    public int getPageIndex(String tag);

    public String getPageTag(int index);

    public boolean removePageByIndex(int index);

    public boolean removePageByTag(String stg);

    public boolean movePageUp(int idx);

    public boolean movePageDown(int idx);

}
