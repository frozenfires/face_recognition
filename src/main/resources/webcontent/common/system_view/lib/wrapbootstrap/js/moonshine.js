var moonshine = moonshine || {};

function mergeobj(o1, o2) {
    for (var key in o2) {
        o1[key] = o2[key]
    }
    return o1;
}


/********* ajax *********/
moonshine = mergeobj(moonshine, function () {

    var that = {};

    that.swal = (function () {
        var thatin = {};

        thatin.info=function(title, text, callback){
            swal({
                title: title,
                text: text,
                type: "info",
                confirmButtonText: "确定",
                confirmButtonColor: "#ff4a00"
            },function(){
                if(callback!=null) callback();
            });
        };
        thatin.warning=function(title, text, callback){
            swal({
                title: title,
                text: text,
                type: "warning",
                confirmButtonText: "确定",
                confirmButtonColor: "#ff4a00"
            },function(){
                if(callback!=null) callback();
            });
        };
        thatin.success=function(title, text, callback){
            swal({
                title: title,
                text: text,
                type: "success",
                confirmButtonText: "确定",
                confirmButtonColor: "#ff4a00"
            },function(){
                if(callback!=null) callback();
            });
        };
        thatin.error=function(title, text, callback){
            swal({
                title: title,
                text: text,
                type: "error",
                confirmButtonText: "确定",
                confirmButtonColor: "#ff4a00"
            },function(){
                if(callback!=null) callback();
            });
        };
        thatin.confirm=function(title, text, callback){
            swal({
                title: title,
                text: text,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#ff4a00",
                confirmButtonText: "是的,确认！",
                cancelButtonText: "取消",
                closeOnConfirm: false
            },function(){
                if(callback!=null) callback();
            });
        };

        return thatin;

    }());

    return that;

}());


/********* ajax *********/
moonshine = mergeobj(moonshine, function () {

    var that = {};

    that.ajax = (function () {
        var thatin = {};

        /**
         * ajax提交服务器公共方法
         * url :提交服务的地址、
         * data: json 对象
         * responseFunction 回调方法
         */
        thatin.post=function(url, data, callback){
            var responseF=eval(callback);
            console.info("url="+url+", jsonData=" + data);
            $.ajax({
                "type": "post",
                "url": url,
                "data": data,
                "success": function(resp) {
                    if(responseF != null) responseF(resp, true);
                },
                "error": function(resp){
                    console.error( resp.responseText );
                    if(responseF != null) responseF(resp, false);
                }
            });
        };

        thatin.get=function(url, data, callback){
            var responseF=eval(callback);
            console.info("url="+url+", jsonData=" + data);
            $.ajax({
                "type": "get",
                "url": url,
                "data": data,
                "cache": false,
                "success": function(resp) {
                    if(responseF != null) responseF(resp, true);
                },
                "error": function(resp){
                    console.error( resp.responseText );
                    if(responseF != null) responseF(resp, false);
                }
            });
        };

        thatin.reset=function(formClass){
            $(formClass+" input[type='text']").val('');
            $(formClass+" input[type='password']").val('');
            $(formClass+' textarea').val('');
            // console.debug("$('#'+formName+' .select')=" + $('#'+formName+' .select').length);
            //$(formClass+' .select').selectValue('');
        };

        thatin.loadOptions=function(selecter, url, keyName, valueName, selectedkey){
            moonshine.ajax.get(url, null, function (resp, success) {
                if(success) {
                    selecter.loadOption(resp.content, keyName, valueName, selectedkey);
                }else {
                    console.error("loadOptions error: " + url);
                }
            })
        };


        return thatin;

    }());

    return that;

}());


/********* utils *********/
moonshine = mergeobj(moonshine, function () {

    var that = {};

    that.utils = (function () {
        var thatin = {};

        /**
         * 回填界面数据
         * @param jsonData json 数据
         * @param formName  form表单名称
         */
        thatin.fillData=function(jsonData, formClass){

            $(formClass+' input').val('');

            //$(formClass+' textarea').val('');
            // console.debug("$('#'+formName+' .select')=" + $('#'+formName+' .select').length);
            //$(formClass+' .select').selectValue('');
            $.each(jsonData, function(key,value){
                var inputName=formClass+" input[name='"+key+"']";
                var inputObj = $(inputName);
                // console.debug('fillData...' + key + ',value='+value + ',class=' + inputObj.attr('class'));
                //if(inputObj.hasClass('select')){
                //    inputObj.selectValue(value);
                //}
                if(inputObj.attr('type') === 'checkbox'){
                    value = value || '';
                    inputObj.attr("checked",  value !== '');
                }
                else{
                    //alert(inputObj.attr("id") + ":" + value);
                    inputObj.val(value);
                }
            });
        };

        /**
         * 对formarray 序列化成json对象
         */
        thatin.arrayToJson=function(array){
            var serializeObj={};
            $(array).each(function(){
                if(serializeObj[this.name]){
                    serializeObj[this.name]= serializeObj[this.name]+","+this.value;
                }else{
                    serializeObj[this.name]=this.value;
                }
            });
            return serializeObj;
        };

        /**
         * 增加参数
         * @param jsonData json对象
         * @param key
         * @param value
         */
        thatin.addParameter=function(jsonData,key,value){
            jsonData[key]=value || '';
        };

        /**
         * 获取指定参数的值
         */
        thatin.getParameter = function(jsonData,key){
            return jsonData[key];
        }

        /**
         * 打印json对象
         */
        thatin.objToString = function (obj) {
            // 用来保存所有的属性名称和值
            var props = "";
            // 开始遍历
            for (var p in obj) { // 方法
                if (typeof ( obj [p]) == " function ") {
                    //obj [p]();
                } else { // p 为属性名称，obj[p]为对应属性的值
                    props += p + " = " + obj [p] + "<br>";
                }
            } // 最后显示所有的属性
            return props;
        }

        thatin.getICheckSelectedVal = function (className) {
            var str="";
            var ids="";
            $(className).each(function () {
                if ($(this).is(':checked') && $(this).val()!="") {
                    str+=$(this).val()+",";
                }
            });
            ids = this.removeEndChar(str, ',');
            return ids;
        }

        thatin.removeEndChar = function (str, chr) {
            if(str.substr(str.length-1) == chr){
                str = str.substr(0,str.length-1);
            }
            return str;
        }
        thatin.contains = function (str, substr) {
           return str.indexOf(substr) > 0;
        }

        return thatin;

    }());

    return that;

}());