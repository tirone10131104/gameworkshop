package dev.xlin.gameworkshop.progs;

import dev.xlin.tols.interfaces.iDAO;

/**
 *
 * @author Administrator
 */
public class iConst
{

    /**
     * 是
     */
    public static final int BOL_TRUE = 1;

    /**
     * 否
     */
    public static final int BOL_FALSE = 0;

    public static final int DATATP_INT = 200;
    public static final int DATATP_DOUBLE = 201;
    public static final int DATATP_STRING = 202;

    public static final int OCLS_FUNC_PROPERTY_LIST = 220;
    public static final int OCLS_FUNC_EFFECT_LIST = 221;
    public static final int OCLS_FUNC_USE_REQUEST_LIST = 222;
    public static final int OCLS_FUNC_BUILD_LIST = 223;
    public static final int OCLS_FUNC_RECYCLE_LIST = 224;
    public static final int SYS_DB_EQUIP_CONFIG_LIST = 225;
    public static final int SYS_DB_FUNCTION_DEFINE_DATA = 226;
    public static final int SYS_DB_SKILL_UPGRADE_CONDITION = 227;

    public static final int SHP_LV_FRIGAE = 1000;
    public static final int SHP_LV_DESTROYER = 1001;
    public static final int SHP_LV_CRUISER = 1002;
    public static final int SHP_LV_BATTLECRUISER = 1003;
    public static final int SHP_LV_BATTLESHIP = 1004;
    public static final int SHP_LV_DREADNOUGHT = 1005;

    public static final int EFT_TYPE_LOCAL = 1050;
    public static final int EFT_TYPE_ITEM = 1051;
    public static final int EFT_TYPE_OBJECT_CLASS = 1052;
    public static final int EFT_TYPE_SHIP = 1053;

    public static final int EFT_VALTYPE_PERCENT = 1070;
    public static final int EFT_VALTYPE_VALUE = 1071;

    public static final int ICLUS_TARTYPE_ITEMS = 1120;
    public static final int ICLUS_TARTYPE_OCLS = 1121;
    public static final int ICLUS_TARTYPE_IT_TPS = 1122;
    public static final int ICLUS_TARTYPE_OC_TPS = 1123;

    public static final int KDT_DTP_INT = 1130;
    public static final int KDT_DTP_DOUBLE = 1131;
    public static final int KDT_DTP_IBOL = 1132;
    public static final int KDT_DTP_EMPTY = 1133;

    public static final int IEQPC_TAGS_ITEMS = 1140;
    public static final int IEQPC_TAGS_OCS = 1141;
    public static final int IEQPC_TAGS_ICLUS = 1142;

    public static final int DBK_INST_MTD_REF = 1150;
    public static final int DBK_INST_MTD_COPY = 1151;
    public static final int DBK_INST_MTD_CUSTOM = 1152;

    public static final int EFT_CFLT_MTD_SUPERPOSITION = 1160;
    public static final int EFT_CFLT_MTD_MAX = 1161;
    public static final int EFT_CFLT_MTD_MIN = 1162;
    public static final int EFT_CFLT_MTD_AVE = 1163;
    public static final int EFT_CFLT_MTD_INSTEAD = 1164;
    public static final int EFT_CFLT_MTD_AVOID = 1165;

    public static final int FUNC_ACT_TARGET_SELF = 1180;
    public static final int FUNC_ACT_TARGET_MASTER = 1181;
    public static final int FUNC_ACT_TARGET_ROLE = 1182;
    public static final int FUNC_ACT_TARGET_TEAM = 1183;
    public static final int FUNC_ACT_TARGET_AIM = 1184;
    public static final int FUNC_ACT_TARGET_REGION = 1185;

    public static final int FUNC_ACT_SIDE_OUR = 1200;
    public static final int FUNC_ACT_SIDE_FRIENDLY = 1201;
    public static final int FUNC_ACT_SIDE_NEUTRALS = 1202;
    public static final int FUNC_ACT_SIDE_HOSTILE = 1203;
    public static final int FUNC_ACT_SIDE_ALL = 1204;
    public static final int FUNC_ACT_SIDE_NO_HOSTILE = 1205;
    public static final int FUNC_ACT_SIDE_NO_OUR = 1206;

    public static final int FUNC_EFT_TAR_PROP = 1220;
    public static final int FUNC_EFT_TAR_KEY = 1221;

    public static final int SKL_INVK_TYPE_LEVEL = 1280;
    public static final int SKL_INVK_TYPE_SERIAL = 1281;
    public static final int SKL_INVK_TYPE_MASTER = 1282;

    public static final int SKL_LV_DATA_COLLECTION = 1300;
    public static final int SKL_LV_DATA_LEVEL = 1301;

    public static final int SKL_SAME_DATA_MAX = 1320;
    public static final int SKL_SAME_DATA_MIN = 1321;
    public static final int SKL_SAME_DATA_MASTER = 1322;
    public static final int SKL_SAME_DATA_LEVEL = 1323;
    public static final int SKL_SAME_DATA_SUPERPOSITION = 1324;

    public static final int SKL_LEVEL_TYPE_SERIAL = 1340;
    public static final int SKL_LEVEL_TYPE_AUTO = 1341;
    public static final int SKL_LEVEL_TYPE_NO = 1342;

    public static final int TARGET_REQ_METD_USE = 1420;
    public static final int TARGET_REQ_METD_CHECK = 1421;

    public static final int DT_REQ_TARTYPE_ITEM = 1450;
    public static final int DT_REQ_TARTYPE_PROP = 1451;
    public static final int DT_REQ_TARTYPE_KEY = 1452;
    public static final int DT_REQ_TARTYPE_SKILL = 1453;

    public static final int MCES_EMPTY_MTD_STOP = 3400;
    public static final int MCES_EMPTY_MTD_SLEEP = 3401;

    public static final int MCES_MCST_AUTO = 3405;
    public static final int MCES_MCST_MANUAL = 3406;

    public static String translateIBOL(int i)
    {
        switch (i)
        {
            case BOL_TRUE:
                return "是";
            case BOL_FALSE:
                return "否";
        }
        return "?";
    }

    public static String transDAOState(int i)
    {
        switch (i)
        {
            case iDAO.OBJECT_STATE_ACTIVE:
                return "正常";
            case iDAO.OBJECT_STATE_DELETE:
                return "[失效]";

        }
        return "?";
    }

    public static String translate(int i)
    {
        switch (i)
        {
            case DATATP_STRING:
                return "字符串";
            case DATATP_INT:
                return "整数";
            case DATATP_DOUBLE:
                return "浮点数";

            case OCLS_FUNC_BUILD_LIST:
                return "建造需求数据";
            case OCLS_FUNC_EFFECT_LIST:
                return "特殊效果数据";
            case OCLS_FUNC_PROPERTY_LIST:
                return "基本属性数据";
            case OCLS_FUNC_RECYCLE_LIST:
                return "拆解回收数据";
            case OCLS_FUNC_USE_REQUEST_LIST:
                return "使用需求数据";
            case SYS_DB_EQUIP_CONFIG_LIST:
                return "物体装配数据";
            case EFT_TYPE_SHIP:
                return "舰船";
            case EFT_TYPE_OBJECT_CLASS:
                return "物类";
            case EFT_TYPE_LOCAL:
                return "自身";
            case EFT_TYPE_ITEM:
                return "装备";

            case EFT_VALTYPE_VALUE:
                return "数值";
            case EFT_VALTYPE_PERCENT:
                return "百分比";

            case ICLUS_TARTYPE_OCLS:
                return "物类";
            case ICLUS_TARTYPE_ITEMS:
                return "物体";
            case ICLUS_TARTYPE_IT_TPS:
                return "物体分类";
            case ICLUS_TARTYPE_OC_TPS:
                return "物类分类";

            case KDT_DTP_INT:
                return "整数";
            case KDT_DTP_IBOL:
                return "布尔";
            case KDT_DTP_EMPTY:
                return "空";
            case KDT_DTP_DOUBLE:
                return "浮点数";

            case IEQPC_TAGS_OCS:
                return "物类";
            case IEQPC_TAGS_ITEMS:
                return "物体";
            case IEQPC_TAGS_ICLUS:
                return "物体块";

            case DBK_INST_MTD_COPY:
                return "实例拷贝";
            case DBK_INST_MTD_CUSTOM:
                return "实例自定义";
            case DBK_INST_MTD_REF:
                return "仅引用";

            case EFT_CFLT_MTD_AVE:
                return "取均值";
            case EFT_CFLT_MTD_AVOID:
                return "回避冲突";
            case EFT_CFLT_MTD_INSTEAD:
                return "用新值取代";
            case EFT_CFLT_MTD_MAX:
                return "取大值";
            case EFT_CFLT_MTD_MIN:
                return "取小值";
            case EFT_CFLT_MTD_SUPERPOSITION:
                return "取叠加值";

            case FUNC_ACT_SIDE_ALL:
                return "全部方";
            case FUNC_ACT_SIDE_FRIENDLY:
                return "友方";
            case FUNC_ACT_SIDE_HOSTILE:
                return "敌方";
            case FUNC_ACT_SIDE_NEUTRALS:
                return "中立方";
            case FUNC_ACT_SIDE_NO_HOSTILE:
                return "非敌方";
            case FUNC_ACT_SIDE_NO_OUR:
                return "非我方";
            case FUNC_ACT_SIDE_OUR:
                return "我方";

            case FUNC_ACT_TARGET_AIM:
                return "目标物体";
            case FUNC_ACT_TARGET_MASTER:
                return "所属主物体";
            case FUNC_ACT_TARGET_REGION:
                return "范围";
            case FUNC_ACT_TARGET_ROLE:
                return "角色";
            case FUNC_ACT_TARGET_SELF:
                return "自身";
            case FUNC_ACT_TARGET_TEAM:
                return "团队";

            case FUNC_EFT_TAR_PROP:
                return "属性";
            case FUNC_EFT_TAR_KEY:
                return "键";

            case TARGET_REQ_METD_CHECK:
                return "检查";
            case TARGET_REQ_METD_USE:
                return "使用";

            case SKL_INVK_TYPE_LEVEL:
                return "调用分级";
            case SKL_INVK_TYPE_MASTER:
                return "调用主";
            case SKL_INVK_TYPE_SERIAL:
                return "级联调用";
            case SKL_LV_DATA_COLLECTION:
                return "主分合并";
            case SKL_LV_DATA_LEVEL:
                return "仅用分级数据";
            case SKL_SAME_DATA_LEVEL:
                return "使用分级数据";
            case SKL_SAME_DATA_MASTER:
                return "使用主数据";
            case SKL_SAME_DATA_MAX:
                return "取大值";
            case SKL_SAME_DATA_MIN:
                return "取小值";
            case SKL_SAME_DATA_SUPERPOSITION:
                return "取叠加值";

            case SKL_LEVEL_TYPE_SERIAL:
                return "连续定义";
            case SKL_LEVEL_TYPE_NO:
                return "无分级";
            case SKL_LEVEL_TYPE_AUTO:
                return "自动分级";

            case DT_REQ_TARTYPE_ITEM:
                return "物体";
            case DT_REQ_TARTYPE_KEY:
                return "键值";
            case DT_REQ_TARTYPE_PROP:
                return "属性";
            case DT_REQ_TARTYPE_SKILL:
                return "技能"; 
        }
        return "? as " + i;
    }

}

// <editor-fold defaultstate="collapsed" desc="FOE翻译">
//            case FOE_BT_THT_MTD_AUTO_BALANCE:
//                return "自动均衡";
//            case FOE_BT_THT_MTD_FORECE_BALANCE:
//                return "强制均衡";
//            case FOE_BT_THT_MTD_MANUAL:
//                return "手工指定";
//            case FOE_BT_THT_MTD_RANDOM:
//                return "随机分配";
//            case FOE_BT_THT_MTD_TURN:
//                return "轮流分配";
//
//            case FOE_BT_TASK_PROC_PARALLEL:
//                return "并行处理";
//            case FOE_BT_TASK_PROC_SERIAL:
//                return "顺序处理";
//            case FOE_BT_THD_START_AUTO:
//                return "自动启动";
//            case FOE_BT_THD_START_PASSIVE:
//                return "被动启动";
//            case FOE_BT_THD_START_SCHEDULE:
//                return "计划启动";
//
//            case FOE_TYPE_VECTOR_THREAD:
//                return "向量线程";
//            case FOE_TYPE_BEAT_THREAD:
//                return "心跳线程";
//            case FOE_TYPE_HEAP_THREAD:
//                return "堆处理线程";
//            case FOE_TYPE_QUEUE_THREAD:
//                return "队列处理线程";
//            case FOE_TYPE_SCHEDULE_THREAD:
//                return "计划任务";
//            case FOE_BT_THT_TMOT_WASTE:
//                return "废弃";
//            case FOE_BT_THT_TMOT_ACCOUMULATION:
//                return "堆积";
//
//            case FOE_VEC_TASK_PROC_PARALLEL:
//                return "并行处理";
//            case FOE_VEC_TASK_PROC_SERIAL:
//                return "顺序处理";
//            case FOE_VEC_TMOT_EXCEPTION:
//                return "抛出异常";
//            case FOE_VEC_TMOT_STOP:
//                return "终止线程";
//            case FOE_VEC_TMOT_WAIT:
//                return "等待";
//            case FOE_VEC_TMOT_WASTE:
//                return "废弃";
//
//            case FOE_HEAP_START_AUTO:
//                return "自动启动";
//            case FOE_HEAP_START_PASSIVE:
//                return "被动启动";
//            case FOE_HEAP_START_SCHEDULE:
//                return "计划启动";
//
//            case FOE_HEAP_TASK_PROC_PARALLEL:
//                return "并行执行";
//            case FOE_HEAP_TASK_PROC_SERIAL:
//                return "串行执行";
//
//            case FOE_HEAP_TMOT_EXCP_WAIT:
//                return "异常并等待";
//            case FOE_HEAP_TMOT_EXCP_WASTE:
//                return "异常并废弃";
//            case FOE_HEAP_TMOT_STOP_THREAD:
//                return "中止线程";
//            case FOE_HEAP_TMOT_WAIT:
//                return "等待";
//            case FOE_HEAP_TMOT_WASTE:
//                return "废弃";
//
//            case FOE_QUE_TMOT_EXCP_WAIT:
//                return "异常并等待";
//            case FOE_QUE_TMOT_EXCP_WASTE:
//                return "异常并废弃";
//            case FOE_QUE_TMOT_STOP_THREAD:
//                return "终止线程运行";
//            case FOE_QUE_TMOT_WAIT:
//                return "等待";
//            case FOE_QUE_TMOT_WASTE:
//                return "废弃";
//            case FOE_QUE_START_AUTO:
//                return "自动启动";
//            case FOE_QUE_START_PASSIVE:
//                return "被动启动";
//            case FOE_QUE_START_SCHEDULE:
//                return "计划启动";
//
//            case FOE_SCH_START_AUTO:
//                return "自动启动";
//            case FOE_SCH_START_PASSIVE:
//                return "被动启动";
//            case FOE_SCH_START_SCHEDULE:
//                return "计划启动";
//            case FOE_SCH_TASK_PROC_PARALLEL:
//                return "并行执行";
//            case FOE_SCH_TASK_PROC_SERIAL:
//                return "串行执行";
//            case FOE_SCH_TMOT_EXCP_WAIT:
//                return "异常并等待";
//            case FOE_SCH_TMOT_EXCP_WASTE:
//                return "异常并废弃";
//            case FOE_SCH_TMOT_STOP_THREAD:
//                return "终止线程";
//            case FOE_SCH_TMOT_WAIT:
//                return "等待";
//            case FOE_SCH_TMOT_WASTE:
//                return "废弃";
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="FOE常量">      
//
//    //心跳任务分配模式：随机
//    public static final int FOE_BT_THT_MTD_RANDOM = 3000;
//    //心跳任务分配模式：均衡
//    public static final int FOE_BT_THT_MTD_FORECE_BALANCE = 3001;
//    //心跳任务分配模式：自动均衡
//    public static final int FOE_BT_THT_MTD_AUTO_BALANCE = 3002;
//    //心跳任务分配模式：轮流
//    public static final int FOE_BT_THT_MTD_TURN = 3003;
//    //心跳任务分配模式：人工指定
//    public static final int FOE_BT_THT_MTD_MANUAL = 3004;
//
//    //心跳任务执行模式：串行执行
//    public static final int FOE_BT_TASK_PROC_SERIAL = 3010;
//    //心跳任务执行模式：并行执行
//    public static final int FOE_BT_TASK_PROC_PARALLEL = 3011;
//
//    //心跳线程启动模式：自动启动
//    public static final int FOE_BT_THD_START_AUTO = 3020;
//    //心跳线程启动模式：被动启动
//    public static final int FOE_BT_THD_START_PASSIVE = 3021;
//    //心跳线程启动模式：计划启动
//    public static final int FOE_BT_THD_START_SCHEDULE = 3022;
//
//    //心跳事务队列执行超期：放弃余下事务
//    public static final int FOE_BT_THT_TMOT_WASTE = 3030;
//    //心跳事务队列执行超期：堆积到下一个心跳事务队列
//    public static final int FOE_BT_THT_TMOT_ACCOUMULATION = 3031;
//
//    //向量线程任务执行模式：串行执行
//    public static final int FOE_VEC_TASK_PROC_SERIAL = 3040;
//    //向量线程任务执行模式：并行执行
//    public static final int FOE_VEC_TASK_PROC_PARALLEL = 3041;
//
//    //向量线程单任务超时：废弃
//    public static final int FOE_VEC_TMOT_WASTE = 3050;
//    //向量线程单任务超时：抛出异常
//    public static final int FOE_VEC_TMOT_EXCEPTION = 3051;
//    //向量线程单任务超时：停止线程
//    public static final int FOE_VEC_TMOT_STOP = 3052;
//    //向量线程单任务超时：等待
//    public static final int FOE_VEC_TMOT_WAIT = 3053;
//
//    ///////////////////堆线程
//    //堆线程启动：自动模式
//    public static final int FOE_HEAP_START_AUTO = 3060;
//    //堆线程启动：被动模式
//    public static final int FOE_HEAP_START_PASSIVE = 3061;
//    //堆线程启动：计划任务
//    public static final int FOE_HEAP_START_SCHEDULE = 3062;
//
//    //堆线程任务执行模式：串行执行
//    public static final int FOE_HEAP_TASK_PROC_SERIAL = 3070;
//    //堆线程任务执行模式：并行执行
//    public static final int FOE_HEAP_TASK_PROC_PARALLEL = 3071;
//
//    //等候，放弃，异常并等候，终止线程
//    //堆任务超时模式：等待
//    public static final int FOE_HEAP_TMOT_WAIT = 3080;
//    //堆任务超时模式：废弃
//    public static final int FOE_HEAP_TMOT_WASTE = 3081;
//    //堆任务超时模式：异常并等待
//    public static final int FOE_HEAP_TMOT_EXCP_WAIT = 3082;
//    //堆任务超时模式：异常并废弃
//    public static final int FOE_HEAP_TMOT_EXCP_WASTE = 3083;
//    //堆任务超时模式：中止堆线程
//    public static final int FOE_HEAP_TMOT_STOP_THREAD = 3084;
//
//    //队列超时：等待
//    public static final int FOE_QUE_TMOT_WAIT = 3090;
//    //队列超时：废弃
//    public static final int FOE_QUE_TMOT_WASTE = 3091;
//    //队列超时：异常并等待
//    public static final int FOE_QUE_TMOT_EXCP_WAIT = 3092;
//    //队列超时：异常并废弃
//    public static final int FOE_QUE_TMOT_EXCP_WASTE = 3093;
//    //队列超时：终止队列线程
//    public static final int FOE_QUE_TMOT_STOP_THREAD = 3094;
//
//    //队列线程：自启动
//    public static final int FOE_QUE_START_AUTO = 3100;
//    //队列线程：被动启动
//    public static final int FOE_QUE_START_PASSIVE = 3101;
//    //队列线程：计划启动
//    public static final int FOE_QUE_START_SCHEDULE = 3102;
//
//    //计划线程：自动启动
//    public static final int FOE_SCH_START_AUTO = 3110;
//    //计划线程：被动启动
//    public static final int FOE_SCH_START_PASSIVE = 3111;
//    //计划线程：计划启动
//    public static final int FOE_SCH_START_SCHEDULE = 3112;
//
//    //计划线程任务执行：串行
//    public static final int FOE_SCH_TASK_PROC_SERIAL = 3120;
//    //计划线程任务执行：并行
//    public static final int FOE_SCH_TASK_PROC_PARALLEL = 3121;
//
//    //计划任务超时：等待
//    public static final int FOE_SCH_TMOT_WAIT = 3130;
//    //计划任务超时：废弃
//    public static final int FOE_SCH_TMOT_WASTE = 3131;
//    //计划任务超时：异常并等待
//    public static final int FOE_SCH_TMOT_EXCP_WAIT = 3132;
//    //计划任务超时：异常并废弃
//    public static final int FOE_SCH_TMOT_EXCP_WASTE = 3133;
//    //计划任务超时：终止线程
//    public static final int FOE_SCH_TMOT_STOP_THREAD = 3134;
//
//    //FOE类型：心跳线程
//    public static final int FOE_TYPE_BEAT_THREAD = 3340;
//    //FOE类型：向量线程
//    public static final int FOE_TYPE_VECTOR_THREAD = 3341;
//    //FOE类型：堆线程
//    public static final int FOE_TYPE_HEAP_THREAD = 3342;
//    //FOE类型：队列线程
//    public static final int FOE_TYPE_QUEUE_THREAD = 3343;
//    //FOE类型：计划任务
//    public static final int FOE_TYPE_SCHEDULE_THREAD = 3344;
//
//    /**
//     * 线程状态：初始化中
//     */
//    public static final int FOES_THREAD_STATUS_INIT = 3350;
//    /**
//     * 线程状态：执行中
//     */
//    public static final int FOES_THREAD_STATUS_RUNNING = 3351;
//    /**
//     * 线程状态：暂停中
//     */
//    public static final int FOES_THREAD_STATUS_PAUSE = 3352;
//    /**
//     * 线程状态：已结束运行
//     */
//    public static final int FOES_THREAD_STATUS_STOPPED = 3353;
//
//    /**
//     * 任务状态：初始化中
//     */
//    public static final int FOES_TASK_STATUS_INIT = 3360;
//
//    /**
//     * 任务状态：已被分发准备就绪
//     */
//    public static final int FOES_TASK_STATUS_READY = 3361;
//    /**
//     * 任务状态：执行中
//     */
//    public static final int FOES_TASK_STATUS_RUNNING = 3362;
//    /**
//     * 任务状态：已完成
//     */
//    public static final int FOES_TAKS_STATUS_DONE = 3363;
// </editor-fold>     
