ScriptQueue=function(e){var d=this,a=arguments.callee;if("function"!=typeof e){e=function(){};}var h=[];d.load=function(c){g(c,e);};d.queue=function(i){var c=h.length;h[c]=i;if(!c){g(i,b);}};var g=function(m,k){var l,c=a.scripts;if(l=c.hash[m]){c=a.scripts[l];if(c[2]){setTimeout(function(){k(m,c[2]);},1);}else{c[1].push(k);}}else{l=c.length;c[l]=[m,[k],false];c.hash[m]=l;f(m);}};var f=function(k){if(document.body){var i=document.createElement("script"),c=document.getElementsByTagName("head")[0];i.type="text/javascript";i.charset="UTF-8";i.src=k;i.rSrc=k;i.onerror=i.onload=i.onreadystatechange=j;i.timeout=setTimeout(function(){j.call(i,{type:"error",q:k});},10000);c.appendChild(i);}else{document.write('<script onload="return 1;" src="'+k+'" type="text/javascript" charset="UTF-8"><\/script>');j.call({rSrc:k},{type:"load"});}};var b=function(k,i){e(k,i);var c;while((!c||c==k)&&h.length){c=h.shift();}if(c&&i){g(c,arguments.callee);}else{setTimeout(function(){e(null,i);},1);h.length=0;}};var j=function(o){var n=a.scripts,l=n.hash[this.rSrc],o=o||window.event,k;clearTimeout(this.timeout);n=n[l];if(n&&!n[2]){if(o&&"readystatechange"==o.type&&"loading"==this.readyState){}else{if(o&&("load"==o.type||"complete"==this.readyState||"loaded"==this.readyState)){n[2]=k=true;}else{if(!o||"error"==o.type||(o.toString&&o.toString().match(/error/i))){k=false;}}for(var c=0,m=n[1],p=m.length;c<p;c++){m[c](n[0],k);}if(!k){delete a.scripts.hash[this.rSrc];delete a.scripts[l];}}}};};ScriptQueue.scripts=[false];ScriptQueue.scripts.hash={};ScriptQueue.queue=function(c,b){if(!c.length){return;}var a=new ScriptQueue(b);for(var e=0,d=c.length;e<d;e++){a.queue(c[e]);}};ScriptQueue.load=function(b,a){if(b){(new ScriptQueue(a)).load(b);}};if(window.ScriptQueueIncludes instanceof Array){ScriptQueue.queue(window.ScriptQueueIncludes);}