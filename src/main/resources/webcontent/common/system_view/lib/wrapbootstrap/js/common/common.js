//修改默认验证规则
jQuery.extend(jQuery.validator.messages, {
    required: "请填写本字段",
    remote: "验证失败",
    email: "请输入正确的电子邮件",
    url: "请输入正确的网址",
    date: "请输入正确的日期",
    dateISO: "请输入正确的日期 (ISO).",
    number: "请输入正确的数字",
    digits: "请输入正确的整数",
    creditcard: "请输入正确的信用卡号",
    equalTo: "请再次输入相同的值",
    accept: "请输入指定的后缀名的字符串",
    maxlength: jQuery.validator.format("允许的最大长度为 {0} 个字符"),
    minlength: jQuery.validator.format("允许的最小长度为 {0} 个字符"),
    rangelength: jQuery.validator.format("允许的长度为{0}和{1}之间"),
    range: jQuery.validator.format("请输入介于 {0} 和 {1} 之间的值"),
    max: jQuery.validator.format("请输入一个最大为 {0} 的值"),
    min: jQuery.validator.format("请输入一个最小为 {0} 的值")
});

jQuery.validator.addMethod("regex", //addMethod第1个参数:方法名称
    function(value, element, params) { //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
        var exp = new RegExp(params); //实例化正则对象，参数为传入的正则表达式
        return exp.test(value); //测试是否匹配
    },
    "请输入正确的格式");

$.validator.addMethod('mobile', function( value, element ){
    // /^1\d{10}$/ 来自支付宝的正则
    return this.optional( element ) || /^1\d{10}$/.test( value );
}, '请输入正确的手机号码');

jQuery.validator.addMethod("pas", function(value, element) {
    var hh = /([0-9].*([a-zA-Z].*[!$@^&*?#%]|[!$@^&*?#%].*[a-zA-Z])|[a-zA-Z].*([0-9].*[!$@^&*?#%]|[!$@^&*?#%].*[0-9])|[!$@^&*?#%].*([0-9].*[a-zA-Z]|[a-zA-Z].*[0-9]))/;
    return (hh.exec(value))? true:false;
}, "密码由字母与数字组合并包含特殊字符（除字母、数字、空格外的字符）并且8位以上！");

var defaultDataTablesOptions = {
    dom: '<"html5buttons"B>lrtip',
    //columnDefs: [{ "bSortable": false, "aTargets": [ 1 ] }, { "bVisible": false, "aTargets": [ 0 ] }],
    //aaSorting: [1, "asc"],
    "processing": true,
    "serverSide": true,
    "displayLength": 4,
    ordering: false,
    searching: false,
    "language": {
        "lengthMenu": "每页显示 _MENU_ 条记录",
        "zeroRecords": "没有检索到数据",
        "info": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
        "infoEmpty": "没有数据",
        "infoFiltered": "(从 _MAX_ 条数据中检索)",
        "paginate": {
            "first": "首页",
            "previous": "前一页",
            "next": "后一页",
            "last": "尾页"
        }
    },
    buttons: [
        {extend: 'copy'},
        {extend: 'csv'},
        {extend: 'excel', title: 'ExampleFile'},
        {extend: 'pdf', title: 'ExampleFile'},
        {
            extend: 'print',
            customize: function (win) {
                $(win.document.body).addClass('white-bg');
                $(win.document.body).css('font-size', '10px');

                $(win.document.body).find('table')
                    .addClass('compact')
                    .css('font-size', 'inherit');
            }
        }
    ]
};

var dataTablesOptions_NoScrollX = {
    "scrollX": "100%",
    "scrollXInner": "100%",
    "scrollCollapse": true
}

function iCheckInit(name) {
    //i-checks init
    $(name).iCheck({
        checkboxClass: 'icheckbox_square-red',
        radioClass: 'iradio_square-red',
        increaseArea : '20%'
    });
}
function iCheckSelectAll(allName, itemName) {
    //i-checks row select
    $(allName).on('ifChecked', function (event) {
        $(itemName).iCheck('check');
    });
    $(allName).on('ifUnchecked', function (event) {
        $(itemName).iCheck('uncheck');
    });
    $(itemName).iCheck('uncheck');
}
