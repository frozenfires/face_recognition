//APP
/////////////////////////////////////////////////////////////////////////////////////

//$( document ).on( "idle.idleTimer", function(event, elem, obj){
//    //$(".btn-query").click();
//    query();
//});

var oTable = null;
var code;

function initAdd(appOptions) {
    moonshine.ajax.reset(".form-add");
    $(".modal-add").modal("show");
}

function add(appOptions) {
    var form = $(".form-add");
    if( !form.valid() ) {
        return;
    }
    moonshine.ajax.post("/"+appOptions.code+"/add", form.serializeArray(), function (resp, success) {
        if(success) {
            moonshine.swal.success("添加"+appOptions.name+"成功", "", function () {
                $(".modal-add").modal("hide");
                query();
            });
        }else {
            moonshine.swal.error("添加"+appOptions.name+"失败", resp.responseText);
        }
    })
}

function initUpdate(appOptions) {
    var id = moonshine.utils.getICheckSelectedVal(".i-checks");
    if(id == "") {
        moonshine.swal.info("请选择修改项", "一次只能选择一项");
        return;
    }
    if(moonshine.utils.contains(id, ",")) {
        moonshine.swal.warning("不可同时选择多项", "一次只能选择一项");
        return;
    }

    $(".modal-update").modal("show");
    moonshine.ajax.get("/"+appOptions.code+"/update/"+id, null, function (resp, success) {
        if(success) {
            var jsonData = resp;
            //回填
            moonshine.utils.fillData(jsonData, ".form-update");
            //定制处理***
            var selecter =  $(".form-update select[name='role.id']");
            moonshine.ajax.loadOptions(selecter, "/role/list", "id", "roleName", jsonData.role.id);

        }else {
            moonshine.swal.error("获取"+appOptions.name+"信息失败", resp.responseText, function () {
                $(".modal-update").modal("hide");
            });
        }
    })
}

function update(appOptions) {
    var form = $(".form-update");
    if( !form.valid() ) {
        return;
    }
    moonshine.ajax.post("/"+appOptions.code+"/update", form.serializeArray(), function (resp, success) {
        if(success) {
            moonshine.swal.success("修改"+appOptions.name+"成功", "", function () {
                $(".modal-update").modal("hide");
                query();
            });
        }else {
            moonshine.swal.error("修改"+appOptions.name+"失败", resp.responseText, function () {
                $(".modal-update").modal("hide");
            });
        }
    })
}

function del(appOptions) {
    var ids = moonshine.utils.getICheckSelectedVal(".i-checks");
    if(ids == "") {
        moonshine.swal.info("请选择删除项", "一次可以选择多项");
        return;
    }
    moonshine.swal.confirm("确认删除吗?", "删除将无法恢复这项记录!", function(){
        moonshine.ajax.post("/"+appOptions.code+"/delete", "ids="+ids, function (resp, success) {
            if(success) {
                moonshine.swal.error("删除"+appOptions.name+"成功", "", function () {
                    query();
                });
            }else {
                moonshine.swal.error(resp.responseText);
            }
        })
    });
}

function query(appOptions) {
    if (oTable == null) {
        $.extend(defaultDataTablesOptions, dataTablesOptions_NoScrollX);
        $.extend(defaultDataTablesOptions, appOptions.tableOptions);
        code = appOptions.code;
        oTable = $('.dataTables-list').DataTable(defaultDataTablesOptions);
    } else {
        oTable.clear();
        oTable.draw();
    }
}

function queryData(sSource, aoData, fnCallback) {
    var form = $(".form-query");
    var paramArray=aoData.concat(form.serializeArray());
    moonshine.ajax.get("/"+code+"/list", paramArray, function (resp, success) {
        if(success) {
            fnCallback(resp);
        }else {
            alert("error");
        }
    })
}

function appInit(appOptions) {
    //$('.dataTables-list').DataTable(dataTablesOptions);
    $(".form-add").validate(appOptions.validateOptions);
    $(".form-update").validate(appOptions.validateOptions);

    // Set idle time
    $( document ).idleTimer( 500 );

    iCheckInit(".i-checks");
    //iCheckSelectAll(".i-checks-all", ".i-checks");

    //toolbar - add
    $(".tb-btn-add").bind("click", function () {
        appOptions.initAddMethod(appOptions);
    });
    //toolbar - update
    $(".tb-btn-update").bind("click", function () {
        appOptions.initUpdateMethod(appOptions);
    });
    //toolbar - delete
    $(".tb-btn-delete").bind("click", function () {
        appOptions.deleteMethod(appOptions);
    });
    //form - add
    $(".btn-add").bind("click", function () {
        appOptions.addMethod(appOptions);
    });
    //form - update
    $(".btn-update").bind("click", function () {
        appOptions.updateMethod(appOptions);
    });
    //form - query
    $(".btn-query").bind("click", function(){
        appOptions.queryMethod(appOptions);
    });
    //form - reset
    $(".btn-reset").bind("click", function(){
        moonshine.ajax.reset(".form-query");
    });
}

/////////////////////////////////////////////////////////////////////////////////////