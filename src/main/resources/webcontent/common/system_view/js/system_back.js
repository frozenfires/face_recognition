var back=window.back||(function(){var h={};var k;var i=true;h.disabledMenu=function(){k="disable";$(".tran").addClass("disabled");};h.enabledMenu=function(){k="active";$(".tran").removeClass("disabled");};function e(){try{console.popConsole();}catch(r){alert(r);}}window.InputData_Getdata=function(s){try{DataValue=document.getElementById(s).innerHTML;if(DataValue==""){DataValue=document.getElementById(s).value;}NtScreen.GetData(DataValue);}catch(r){alert(r);}};window.KeybdInputData_onkeypress=function(r,s){if(s==="91"){return;}var v={27:"esc",9:"tab",32:"space",13:"return",8:"backspace",145:"scroll",20:"capslock",144:"numlock",19:"pause",45:"insert",36:"home",46:"del",35:"end",33:"pageup",34:"pagedown",37:"left",38:"up",39:"right",40:"down",109:"-",112:"f1",113:"f2",114:"f3",115:"f4",116:"f5",117:"f6",118:"f7",119:"f8",120:"f9",121:"f10",122:"f11",123:"f12",191:"/",48:"0",49:"1",50:"2",51:"3",52:"4",53:"5",54:"6",55:"7",56:"8",57:"9",17:"r",186:";",187:"=",16:"Shift"};var t=v[s]||s;if(t+""==="undefined"||!t){t=s;}try{if(t==="f9"){e();}kbdOnkeypress(t);}catch(u){}};function j(){var r="";try{r=document.getElementById("language").innerHTML;if(""==r.length){setTimeout("windowsOnLoadjs();",5);}else{windowsOnLoad();}}catch(s){}}function f(){p();try{refreshBtnStatus();}catch(r){}try{tranBtnEvent("a");}catch(r){}try{tranBtnEvent("button");}catch(r){}try{$("a").mousemove(function(){return false;}).attr("hidefocus",true);}catch(r){}try{if(Agent){Agent.report(Config.monitorurl);}}catch(r){}}if(typeof Agent!=="undefined"){Agent.init(Config.monitorurl);}var a=0;function o(){if(a++>50){alert("程序异常，请联系管理员-91！");}var r=document.getElementsByTagName("tt")[0];if(r&&(r.innerHTML===""||!r.innerHTML)&&a<50){window.setTimeout(o,5);}else{try{windowsOnLoad();}catch(s){}try{f();}catch(s){}}}o();function q(r){if(k=="disable"){return;}try{NtScreen.FinishInput(this.id);}catch(s){alert("流程调整出错"+s);}}function n(s){try{$("#"+systemConfig.controllerStep_paramid).html(s);NtScreen.FinishInput(systemConfig.controllerStep);}catch(r){alert("流程调用出错"+systemConfig.controllerStep+"->"+s+"\n"+r);}}function d(r){if($(".ctrl.disable_shutcut").length>0){return;}n(systemConfig.modifyPswd_stepid);}function l(r){if($(".ctrl.disable_shutcut").length>0){return;}Ethink.confirm("锁定屏幕","请确认是否锁定屏幕！",function(){$("#system_lock").show();$("#User_Id").val($("#TellerNumVal").html());});}function g(r){if($(".ctrl.disable_shutcut").length>0){return;}Ethink.confirm("退出","请确认是否退出维护！",function(){n(systemConfig.ExitBack_stepid);});}function b(r){document.getElementById("Nt_Sqlite3").QueryData("D:\\RDP\\NtData.db",r);return document.getElementById("Nt_Sqlite3").sResult;}function c(s){var r=document.getElementById("Nt_Sqlite3").ExecSql("D:\\RDP\\NtData.db",s);return r;}function m(s){var w=function(A){Ethink.error(A);$("#lock_softkey").hide();};var x=document.getElementById("User_Id").value;var t=document.getElementById("User_Pswd").value;var z='select User_Pswd from USERINFO where User_Id = "'+x+'";';var u;try{u=b(z);}catch(v){w(v);return;}var y=u.split("|");var r=y[1];if(x===""||x===undefined){w("柜员号不存在！");}else{if(r!=t){w("密码不正确！");}else{$("#system_lock").hide();$("#User_Id").val("");$("#User_Pswd").val("");}}}function p(t){var x=$("#"+systemConfig.user_groupid).html();var w='select User_Level from USERINFO where User_Id = "'+x+'";';var s;try{s=b(w);}catch(v){console.info(v);s="user";}s=s.substring(11);if(s&&s!=""){var u=systemConfig.user_group[s];for(var r=0;u&&r<u.length;r++){$(".menu li a#"+u[r]).parent("li").show();}}else{$(".menu li").show();}}(function(r){$(".tran").click(q);try{document.onkeydown=function(){KeybdInputData_onkeypress("",event.keyCode);};}catch(s){}$("#lockscreen").click(l);$("#logout").click(g);$("#lock_login").click(m);$("#modifyPswd").click(d);$("#User_Pswd").click(function(){VirtualKeyboard.open(this.id,"lock_softkey");$("#lock_softkey").show();});})();return h;})();window.back=back;