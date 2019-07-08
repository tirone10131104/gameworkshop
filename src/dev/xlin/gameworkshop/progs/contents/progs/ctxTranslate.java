package dev.xlin.gameworkshop.progs.contents.progs;

/**
 *
 * @author 刘祎鹏
 */
public class ctxTranslate
{

    public static String transGreekCharactor(int i)
    {
        try
        {
            String s = "αβγδεζηθικλμνξοπρστυφχψω";
            return s.substring(i, i + 1);
        }
        catch (Exception excep)
        {
            return (i+1) +"";
        }
    }

    public static String translateReturn(int i)
    {
        switch (i)
        {
            case ctxReturn.WLDTPS_CONST_ERROR:
                return "世界主类型错误";
            case ctxReturn.WLDTPS_WMAIN_ERROR:
                return "世界分类错误";
            case ctxReturn.WLDTPS_WSET_ERROR:
                return "世界子类错误";
            case ctxReturn.WLD_RES_NOT_EXIST:
                return "资源引用错误";
            case ctxReturn.WLDTPS_RES_CONFIG_NOTEXIST:
                return "资源配置不存在";
        }
        return "?";
    }

    public static String translateConst(int i)
    {
        switch (i)
        {
            case ctxConst.WORLD_TYPE_MOON:
                return "卫星";
            case ctxConst.WORLD_TYPE_ASTEROID:
                return "小行星";
            case ctxConst.WORLD_TYPE_PLANET:
                return "行星";
            case ctxConst.SPC_A:
                return "A";
            case ctxConst.SPC_B:
                return "B";
            case ctxConst.SPC_F:
                return "F";
            case ctxConst.SPC_G:
                return "G";
            case ctxConst.SPC_K:
                return "K";
            case ctxConst.SPC_M:
                return "M";
            case ctxConst.SPC_O:
                return "O";
        }
        return "?";
    }
}
