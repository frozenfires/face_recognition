/**
 * 系统参数配置
 */
window.systemConfig = {
    theme                   : 'traffic'                         // 系统皮肤主题,默认为标准化主题
    ,title                  : '欢迎您使用“中国邮政储蓄银行智慧柜台（ATS）”'  // 顶部标题字样
    ,controllerStep         : 'back_controller'          // 中心控制类step
    ,controllerStep_paramid : 'controller_param'         // 中心控制类step，参数id 
    ,modifyPswd_stepid      : 'modifyPswd'               // 修改密码stepid
    ,ExitBack_stepid        : 'ExitBack'                 // 退出维护stepid
    ,user_groupid           : 'user_group'               // 用户组标签id
    // 用户组定义
    ,user_group             :{
          admin: ['xtgl', 'yhgl', 'zkkz']
        , user:  ['xtgl', 'yhgl', 'zkkz', 'qjjc', 'zx', 'fw']
    }
};

/**
 * 系统菜单配置
 */
window.systemMenu = [

{id:"xjjy",  icon:"fa fa-database"   , name:"现金交易", children: [
    {id:'kqk',    name:'卡取款'},
    {id:'kck',   name:'卡存款'},
    {id:'xykxjhk',  name:'信用卡现金还款'},
    {id:'xjhk',  name:'现金汇款'}
]},
{id:"czjy",  icon:"fa fa-group"      , name:"存折交易", children: [
    {id:'czck',    name:'存折存款'},
    {id:'czqk',   name:'存折取款'},
    {id:'czyecx',  name:'存折余额查询'},
    {id:'czbd',  name:'存折补登'},
    {id:'czzz',  name:'存折转账'}
]},
{id:"zhcx",  icon:"fa fa-search"     , name:"账户查询", children: [
    {id:'yecx',    name:'余额查询'},
    {id:'mxcx',   name:'明细查询'},
    {id:'zhqycx',  name:'账户签约查询'},
    {id:'lktzhhcx',  name:'绿卡子账户查询'}
]},
{id:"zhgl",  icon:"fa fa-pie-chart"  , name:"账户管理", children: [
    {id:'ksq',    name:'卡申请'},
    {id:'gm',   name:'修改密码'},
    {id:'zzxesz',  name:'转账限额设置'},
    {id:'mmgs',  name:'卡折挂失'},
    {id:'dzdcxdy',  name:'对账单查询打印'},
    {id:'ktdxtz',  name:'开通短信通知'},
    {id:'czbd',  name:'存折补登'}
]},
{id:"dzyh",  icon:"fa fa-bank"       , name:"电子银行", children: [
    {id:'dzyhkt',    name:'电子银行开通'},
    {id:'xgkhsjh',   name:'修改开户手机号'},
    {id:'dzqdktcx',  name:'电子渠道开通查询'},
    {id:'dzqdxexg',  name:'电子渠道限额修改'}
]},
{id:"zzhk",  icon:"fa fa-exchange"   , name:"转账汇款", children: [
    {id:'bhzz',    name:'本行转账'},
    {id:'thzz',   name:'跨行转账'},
    {id:'zzxesz',  name:'转账设置限额'},
    {id:'hqzdq',  name:'绿卡通活期转定期'},
    {id:'dqzhq',  name:'绿卡通定期转活期'}
]},
{id:"jj",  icon:"fa fa-calculator" , name:"基金    ", children: [
    {id:'fxpg',    name:'风险评估'},
    {id:'jjtkh',   name:'基金TA开户'},
    {id:'jjrg',  name:'基金认购'},
    {id:'jjcd',  name:'基金撤单'},
    {id:'jjsh',  name:'基金赎回'},
    {id:'jjdt',  name:'基金定投'},
    {id:'jjdtbg',  name:'基金定投变更'},
    {id:'jjdtcx',  name:'基金定投撤消'},
    {id:'zsjjcpcx',  name:'在售基金产品查询'},
    {id:'cyjjcpcx',  name:'持有基金产品查询'},
    {id:'jjdtdecx',  name:'基金定投查询'},
    {id:'jjjymxcx',  name:'基金交易明细查询'},
    {id:'cxtazh',  name:'查询TA账户'},
    {id:'zjzhcx',  name:'中间账户查询'}
]},
{id:"lc",  icon:"fa fa-area-chart" , name:"理财    ", children: [
	{id:'fxpg',    name:'风险评估'},
    {id:'lcqy',   name:'理财签约'},
    {id:'lcgm',  name:'理财购买'},
    {id:'lccd',  name:'理财撤单'},
    {id:'lczz',  name:'理财终止'},
    {id:'zslccpcx',  name:'在售理财产品查询'},
    {id:'cylccpcx',  name:'持有理财产品查询'},
]},
{id:"xyk",  icon:"fa fa-credit-card", name:"信用卡  ", children: [
     {id:'xykhk',  name:'信用卡还款'},
     {id:'xykxjhk',  name:'信用卡现金还款'}
]},

{id:"tran_9",  icon:"fa fa-money"      , name:"个人贷款", children: []},
{id:"tran_10", icon:"fa fa-globe"      , name:"国际业务", children: []},
{id:"tran_11", icon:"fa fa-newspaper-o", name:"对公业务", children: []},
{id:"tran_12", icon:"fa fa-cubes"      , name:"贵金属  ", children: []}


];