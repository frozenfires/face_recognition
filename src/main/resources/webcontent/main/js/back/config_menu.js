/**
 * 系统参数配置
 */
window.systemConfig = {
     controllerStep         : 'back_controller'          // 中心控制类step
    ,controllerStep_paramid : 'controller_param'         // 中心控制类step，参数id 
    ,modifyPswd_stepid      : 'modifyPswd'               // 修改密码stepid
    ,ExitBack_stepid        : 'ExitBack'                 // 退出维护stepid
    ,softkey                : './js/lib/softkey'         // 定义软键盘路径
    // 用户组定义
    ,user_group             :{
		//二级菜单配置方式
		'admin':  [ 'yhgl', 'sldb','dbtj','pcdb','tpcl','rljs','face_wzcl'],
        // RDP系统不要包含交警系统菜单
        'rdp':    [ 'yhgl', 'sldb','dbtj','pcdb','tpcl','rljs']
    }
    ,getUsergroup           : function(){
        return (sessionStorage.role == 'undefined' || !sessionStorage.role)
                 ? 'admin' : sessionStorage.role;
    }
};

/**
 * 系统菜单配置
 */
window.systemMenu = [
//二级菜单配置方式
/*
{id:"yhgl",  icon:"fa fa-group"      , name:"用户管理", children: [
	{id:'yhgl',    name:'用户管理',   data_url:"sign.html" }
]},
{id:"sldb",  icon:"fa fa-inbox"     , name:"人脸对比", children: [
    {id:'sldb',    name:'人脸对比',   data_url:"match.html"  } ,
	{id:'dbtj',    name:'对比统计',   data_url:"match_table.html" } ,
	{id:'pcdb',    name:'批次对比',   data_url:"matchPlatform.html"}
]}
*/
//一级菜单配置方式
{id:"yhgl",       icon:"fa fa-group"      , name:"用户管理",  data_url:"sign.html",children: []},
{id:"sldb",       icon:"fa fa-inbox"      , name:"人脸对比",  data_url:"match.html",children: []},
{id:"dbtj",       icon:"fa fa-group"      , name:"对比统计",  data_url:"match_table.html",children: []},
{id:"pcdb",       icon:"fa fa-group"      , name:"批次对比",  data_url:"matchPlatform.html",children: []},
{id:"tpcl",       icon:"fa fa-group"      , name:"图片处理",  data_url:"photoDeal.html",children: []},
{id:"rljs",       icon:"fa fa-group"      , name:"人脸检索",  data_url:"retrieve.html",children: []},
{id:"face_wzcl",  icon:"fa fa-group"      , name:"违章处理",  data_url:"violation_index.html",children: []}
];

document.write('<link href="css/project_traffic.css" rel="stylesheet">');