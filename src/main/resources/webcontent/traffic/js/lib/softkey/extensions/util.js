var util=window.util||(function(){var a={};a.newKeys=function(d){var c=[];for(var b=0;b<d.length;b++){c.push(a.newKey(d.charAt(b)));}return c.join("\n");};a.newKey=function(d,e,c){var b=[];var e=e||d;var c=c||"";b.push('<div unselectable="on" id="kb_b'+e+'" class="kbButton '+c+'">');b.push('<a unselectable="on" href="#">');if(/<(“[^”]*”|’[^’]*’|[^’”>])*>/.test(d)){b.push(d);}else{b.push('<span unselectable="on" class="charNormal">'+d+"</span>");}b.push("</a>");b.push("</div>");return b.join("\n");};return a;})();window.util=util;