package dev.xlin.gameworkshop.progs;

/**
 *
 * @author Administrator
 */
public class iReturn
{

    /**
     * 方法初始化失败，通常是因为参数格式不合法，或者为null
     */
    public static final int FUNCTION_INIT_FAIL = 0;

    /**
     * 本次方法调用完全成功
     */
    public static final int SUCCESS = 100;

    /**
     * 本次方法调用过程中发生了无法克服的数据库错误，通常是语法错误或者网络故障
     */
    public static final int DB_OPERATE_FAIL = 101;

    /**
     * 指定的标签与其他定义重叠。
     */
    public static final int BEAN_TAG_REPEAT = 102;

    /**
     * 当前数据没有准备好被销毁
     */
    public static final int BEAN_NOT_READY_DESTROY = 103;

    /**
     * 当前数据不能被删除
     */
    public static final int BEAN_CANT_DELETE = 104;

    /**
     * 当前数据已经是集合中序号最前的了
     */
    public static final int BEAN_NO_PREVIOUS = 105;

    /**
     * 当前数据已经是集合中序号末尾了
     */
    public static final int BEAN_NO_LAST = 106;

    public static final int BEAN_CANT_MOVE_UP = 107;
    public static final int BEAN_CANT_MOVE_DOWN = 108;
    public static final int BEAN_CANT_MOVE_TO_TYPE = 109;
    public static final int BEAN_TYPE_ERROR = 110;
    public static final int BEAN_PARENT_ERROR = 111;

    public static final int BEAN_CANT_REVERT = 112;

    public static final int BEAN_OID_REPEAT = 113;
    public static final int BEAN_DATA_RANGE_ERROR = 114;
    public static final int BEAN_DO_NOT_NEED_MOVE = 115;
    public static final int BEAN_FOREIGN_KEY_ERROR = 120;
    public static final int EXE_SQL_SYNTAX_ERROR = 121;
    public static final int SQL_CONNECTION_TIMEOUT = 122;

    public static final int BEAN_TAG_EMPTY = 130;
    public static final int BEAN_NAME_EMPTY = 131;
    public static final int BEAN_TYPE_EMPTY = 132;
    public static final int BEAN_STATUS_EMPTY = 133;

    /**
     * 传递的参数为null
     */
    public static final int PARAM_NULL = 140;

    //定义的子节点类型不准确
    public static final int STTP_CHILDREN_INCORRECT = 1002;
    //已经被引用了，暂时不能删除。
    public static final int STTP_CANT_DELETE = 1003;
    public static final int STTP_TAG_ERROR = 1004;
    //当设为ROOT的时候，不能将MAIN设为FALSE
    public static final int STTP_MAIN_ERROR = 1005;
    //系统中只有一种类型是MAIN ST
    public static final int STTP_MAIN_ONLY = 1006;
    //父级的TYPE错误。
    public static final int STT_PARENT_TYPE_ERROR = 1060;
    //父级分类无法移动到其子分类
    public static final int STT_CANT_MOVE_TO_CHILD = 1061;
    //这个分类类型无法创建子分类
    public static final int STT_CANT_HAVE_CHILD = 1062;
    //分类树指针检查错误
    public static final int STT_TYPE_POINTER_CHECK_FAIL = 1063;
    //目标分类不存在
    public static final int STT_TYPE_NOT_EXIST = 1064;
    //不同分类类型的不允许移动
    public static final int STT_CANT_MOVE_TO_DEFF_STTP = 1065;

    //物体类的数据功能目标不存在
    public static final int OCLS_FUNCS_ERROR = 5000;
    public static final int ITEM_TEMPLET_REPEAT = 5001;

    public static final int ITEM_ERR_5002 = 5002;
    public static final int ITEM_ERR_5003 = 5003;
    public static final int ITEM_ERR_5004 = 5004;
    public static final int ITEM_ERR_5005 = 5005;
    public static final int ITEM_ERR_5006 = 5006;
    public static final int ITEM_ERR_5007 = 5007;

    public static final int IEST_PARENT_NOTEXIST = 5010;
    public static final int IEST_OVER_MAX_LEVEL = 5011;
    //物体的引导数据块不见了
    public static final int ITEM_HEADER_DATA_NOTEXIST =  5012;
    

    public static final int DATA_BLOCK_COLUMN_NOTEXIST = 5100;
    public static final int DATA_BLOCK_PAGE_NOTEXIST = 5101;
    public static final int DATA_BLOCK_DATA_NOTEXIST = 5102;
    public static final int DATA_BLOCK_TYPE_UNKNOWN = 5103;
    public static final int DATA_BLOCK_TEMP_ORI_REPEAT = 5104;
    public static final int PROPERTY_DEF_NOT_EXIST = 5200;
    public static final int PROPERTY_DATA_REPEAT = 5201;
    //数据目标设置有问题
    public static final int DATA_TARGET_ERROR = 5202;
    //数据目标类型设置有问题
    public static final int DATA_TARGET_TYPE_ERROR = 5203;

    //特效数据配置有误
    public static final int EFFECT_DATA_CONFIG_ERROR = 5301;

    public static final int RECY_ITEM_NOTEXIST = 5401;
    public static final int RECY_QUANTITY_ERROR = 5402;

    //键数据设置值类型有错误
    public static final int KDT_DATA_TYPE_ERROR = 5450;

    //装配配置基本项重叠
    public static final int IEQCFG_SLOT_CONFIG_REPEAT = 5500;

    public static final int INTF_DEFINE_NOTEXIST = 5520;

    //元数据定义不存在
    public static final int MTDT_DEF_NOTEXIST = 5540;

    public static final int SKL_INVOKE_TYPE_ERROR = 5560;
    public static final int SKL_DATA_METHOD_ERROR = 5561;
    public static final int SKL_SAME_DATA_METHOD_ERROR = 5562;
    public static final int SKL_LEVEL_TYPE_ERROR = 5563;
    public static final int SKL_NOT_FOUND = 5564;
    public static final int SKL_CANT_MOVE_TO_DIF_TYPE = 5565;

    /**
     * **************************************************************
     * FOE SYSTEM 预留区间 7000-7399
     */
    /**
     * 休眠心跳配置错误
     */
    public static final int FOE_BTC_SLEEP_BEAT_ERROR = 7000;

    /**
     * 心跳间隔配置错误
     */
    public static final int FOE_BTC_BEAT_INTERVAL_ERROR = 7001;

    /**
     * 线程数量配置错误
     */
    public static final int FOE_BTC_THREAD_COUNT_ERROR = 7002;

    /**
     * *
     * 线程任务数量限制错误
     */
    public static final int FOE_BTC_THREAD_TASK_LIMIT_ERROR = 7003;

    /**
     * 任务处理模式配置错误
     */
    public static final int FOE_BTC_TASK_PROC_MTD_ERROR = 7004;

    /**
     * 多任务池任务分配模式配置错误
     */
    public static final int FOE_BTC_THREAD_TASK_ALLOC_MTD_ERROR = 7005;

    /**
     * 线程启动模式配置错误
     */
    public static final int FOE_BTC_THREAD_START_MTD_ERROR = 7006;

    /**
     * 线程任务池超期处理模式
     */
    public static final int FOE_BTC_TMOT_TASK_MTD_ERROR = 7007;

    /**
     * 心跳线程计划启动的配置错误
     */
    public static final int FOE_BTC_START_SCHEDULE_ERROR = 7008;

    /**
     * 堆积任务导致拒绝服务的限制数配置错误
     */
    public static final int FOE_BTC_REFUSE_LIMIT_ERROR = 7009;

    /**
     * FOE线程类型无法识别
     */
    public static final int FOE_THREAD_UNRECOGNIZED = 7010;
    /*
    *   队列线程任务总数限制设置错误
     */
    public static final int FOE_QUEUE_MAX_TASK_ERROR = 7011;
    /**
     * 队列线程任务超时处理方法错误
     */
    public static final int FOE_QUEUE_TMOT_METHOD_ERROR = 7012;
    /**
     * 队列线程任务执行时限设置错误
     */
    public static final int FOE_QUEUE_TASK_TIME_LIMIT_ERROR = 7013;

    /**
     * 线程休眠等待时限设置错误
     */
    public static final int FOE_QUEUE_SLEEP_WAIT_ERROR = 7014;

    /**
     * 线程配置不存在
     */
    public static final int FOES_CONFIG_NOT_EXISTED = 7200;

    /**
     * 线程配置不允许创建实例
     */
    public static final int FOES_CONFIG_NOT_ENABLE = 7201;

    /**
     * 线程数量配额已满
     */
    public static final int FOES_THREAD_INSTANCE_FULL = 7202;
    /**
     * 分发线程数量已满
     */
    public static final int FOES_DISTRIBUTE_FULL = 7203;

    /**
     * 线程已经被成功初始化过
     */
    public static final int FOES_ALREADY_INIT = 7204;

    /**
     * 任务没有准备启动
     */
    public static final int FOES_TASK_NOT_READY = 7205;
    /**
     * 任务不是全新的，不需要INIT
     */
    public static final int FOES_TASK_NOT_NEW = 7206;

    /**
     * 任务已经在监视线程中了
     */
    public static final int FOES_TASK_IN_P_WACT = 7207;
    /**
     * **************************************************************
     * FOE SYSTEM 结束
     */

    public static final int MCES_MCE_NOT_EXIST = 7400;
    public static final int MCES_MCE_ALREADY_IN = 7401;

    //当有任务序列的时候无法直接关闭微引擎
    public static final int MCES_CANT_STOP_MCE_WHEN_NOT_EMPTY = 7402;
    public static final int MCES_MCE_TPS_ERROR = 7403;
    public static final int MCES_MCE_START_MTD_ERROR = 7404;
    public static final int MCES_MCE_EMPTY_MTD_ERROR = 7405;
    public static final int MCES_MCE_WAIT_ERROR = 7406;
    public static final int MCES_MCE_INST_LMT_ERROR = 7407;
    public static final int MCES_MCS_INST_OUT_LIMIT = 7408;
    public static final int MCES_MCS_NOT_INITIALIZED =   7409;
    
    //反射设置INSTID出现错误
    public static final int MCES_MCS_MCE_INIT_REG_FAIL = 7410;
     
    ///////////////
    public static final int C_BARS_ITEM_REPEAT = 10000;
    public static final int C_BARS_ITEM_NOTEXIST = 10001;

    public static String translate(int i)
    {
        switch (i)
        {

            case BEAN_DATA_RANGE_ERROR:
                return "数据范围存在错误";
            case BEAN_CANT_REVERT:
                return "目标记录不需要被恢复";
            case BEAN_FOREIGN_KEY_ERROR:
                return "因为可能还有其他的数据设置使用了当前内容，因为数据完整和安全性的考虑阻止了操作";
            case EXE_SQL_SYNTAX_ERROR:
                return "数据库操作过程中出现语法错误";
            case SQL_CONNECTION_TIMEOUT:
                return "数据库网络连接超时，很可能数据库服务器已下线";
            case ITEM_ERR_5002:
                return "虚物品不可被设置为可装备物品";
            case ITEM_ERR_5003:
                return "装备目标类型编码设置错误";
            case ITEM_ERR_5004:
                return "装备槽位类型编码设置错误";
            case ITEM_ERR_5005:
                return "装备槽位尺寸编码设置错误";
            case ITEM_ERR_5006:
                return "堆叠数量限制小于0";
            case ITEM_ERR_5007:
                return "装配数量限制数量小于0 ";
            case IEST_PARENT_NOTEXIST:
                return "父节点不存在";
            case IEST_OVER_MAX_LEVEL:
                return "已是当前系统设定的最大层级";
            case BEAN_CANT_DELETE:
                return "不可被失效或者删除，通常因为这个数据还有关联或者下级数据。";
            case BEAN_NOT_READY_DESTROY:
                return "不可被销毁的数据，通常因为这个数据还没有被标记为失效。";
            case IEQCFG_SLOT_CONFIG_REPEAT:
                return "装配配置结构项重叠";
            case INTF_DEFINE_NOTEXIST:
                return "源接口定义不存在";
            case MTDT_DEF_NOTEXIST:
                return "元数据定义不存在";
            case SKL_DATA_METHOD_ERROR:
                return "技能数据使用方法错误";
            case SKL_INVOKE_TYPE_ERROR:
                return "技能分级调用方法错误";
            case SKL_SAME_DATA_METHOD_ERROR:
                return "技能同种数据使用方法错误";
            case SKL_LEVEL_TYPE_ERROR:
                return "技能分级类型设置错误";
            case SKL_NOT_FOUND:
                return "技能定义不存在";
            case SKL_CANT_MOVE_TO_DIF_TYPE:
                return "技能不能移动到不同的分类下";

            case FOE_BTC_BEAT_INTERVAL_ERROR:
                return "心跳间隔配置错误";
            case FOE_BTC_REFUSE_LIMIT_ERROR:
                return "堆积任务导致拒绝服务的限制数配置错误";
            case FOE_BTC_SLEEP_BEAT_ERROR:
                return "休眠心跳配置错误";
            case FOE_BTC_START_SCHEDULE_ERROR:
                return "心跳线程计划启动的配置错误";
            case FOE_BTC_TASK_PROC_MTD_ERROR:
                return "任务处理模式配置错误";
            case FOE_BTC_THREAD_COUNT_ERROR:
                return "线程数量配置错误";
            case FOE_BTC_THREAD_START_MTD_ERROR:
                return "线程启动模式配置错误";
            case FOE_BTC_THREAD_TASK_ALLOC_MTD_ERROR:
                return "多任务池任务分配模式配置错误";
            case FOE_BTC_THREAD_TASK_LIMIT_ERROR:
                return "线程任务数量限制错误";
            case FOE_BTC_TMOT_TASK_MTD_ERROR:
                return "线程任务池超期处理模式";
            case FOE_THREAD_UNRECOGNIZED:
                return "FOE线程类型无法识别";
            case FOE_QUEUE_MAX_TASK_ERROR:
                return "队列最大任务设置错误";
            case FOE_QUEUE_SLEEP_WAIT_ERROR:
                return "队列休眠等待时间设置错误";
            case FOE_QUEUE_TASK_TIME_LIMIT_ERROR:
                return "任务处理时限设置错误";
            case FOE_QUEUE_TMOT_METHOD_ERROR:
                return "任务处理超时处理方法设置错误";

            case MCES_MCE_NOT_EXIST:
                return "指定微引擎不存在";
            case MCES_MCE_ALREADY_IN:
                return "微引擎已经存在于容器中";
            case MCES_CANT_STOP_MCE_WHEN_NOT_EMPTY:
                return "当有任务序列的时候无法直接关闭微引擎";
            case MCES_MCS_INST_OUT_LIMIT:
                return "微引擎实例线程数量超过配置限制";
            case MCES_MCS_MCE_INIT_REG_FAIL:
                return "微引擎启动注册失败";
            default:
                return " " + i;
        }
    }
}
