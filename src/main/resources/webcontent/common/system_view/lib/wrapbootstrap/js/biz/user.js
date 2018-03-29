(function () {

    /**** options ****/
    var appOptions = {
        code: "user",
        name: "用户",
        addMethod: add,
        updateMethod: update,
        deleteMethod: del,
        queryMethod: query,
        initAddMethod: initAdd,
        initUpdateMethod: initUpdate,
        validateOptions: {
            rules: {
                loginName: {
                    required: true,
                    maxlength: 10,
                    remote: {
                        url: "/user/checkLoginName",
                        type: "get",
                        data: {
                            loginName: function () {
                                return $(".form-add input[name='loginName']").val();
                            }
                        }
                    }
                },
                plainPassword: {
                    //required: true,
                    rangelength: "6, 10"
                },
                name: {
                    required: true,
                    maxlength: 10
                },
                mobile: {
                    mobile: true
                },
                email: {
                    email: true
                }
            }
        },
        tableOptions: {
            "columns": [
                {
                    "data": "id",
                    className: "text-center",
                    "render": function (data, type, full, meta) {
                        return '<input type="checkbox" class="i-checks" value="' + data + '" />';
                    }
                },
                {"data": "loginName"},
                {"data": "name"},
                {"data": "mobile"},
                {"data": "email"},
                {"data": "role.roleName"},
                {
                    "data": "status",
                    "render": function (data, type, full, meta) {
                        return data == '0' ? "正常" : "禁用";
                    }
                }
            ],
            "drawCallback": function () {
                iCheckInit(".i-checks");
                iCheckSelectAll(".i-checks-all", ".i-checks");
            },
            "serverData": queryData
        }
    }


    /**** init ****/
    $(function () {
        appInit(appOptions);

        var selecter = $(".form-add select[name='role.id']");
        moonshine.ajax.loadOptions(selecter, "/role/list", "id", "roleName");
    });

})();