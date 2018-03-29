/*!
 * jquery.base64.js 0.0.3 - https://github.com/yckart/jquery.base64.js
 * Makes Base64 en & -decoding simpler as it is.
 *
 * Based upon: https://gist.github.com/Yaffle/1284012
 *
 * Copyright (c) 2012 Yannick Albert (http://yckart.com)
 * Licensed under the MIT license (http://www.opensource.org/licenses/mit-license.php).
 * 2013/02/10
 **/
;(function(p){var s="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/",c="",m=[256],q=[256],o=0;var r={encode:function(b){var a=b.replace(/[\u0080-\u07ff]/g,function(e){var d=e.charCodeAt(0);return String.fromCharCode(192|d>>6,128|d&63);}).replace(/[\u0800-\uffff]/g,function(e){var d=e.charCodeAt(0);return String.fromCharCode(224|d>>12,128|d>>6&63,128|d&63);});return a;},decode:function(a){var b=a.replace(/[\u00e0-\u00ef][\u0080-\u00bf][\u0080-\u00bf]/g,function(e){var d=((e.charCodeAt(0)&15)<<12)|((e.charCodeAt(1)&63)<<6)|(e.charCodeAt(2)&63);return String.fromCharCode(d);}).replace(/[\u00c0-\u00df][\u0080-\u00bf]/g,function(e){var d=(e.charCodeAt(0)&31)<<6|e.charCodeAt(1)&63;return String.fromCharCode(d);});return b;}};while(o<256){var n=String.fromCharCode(o);c+=n;q[o]=o;m[o]=s.indexOf(n);++o;}function t(h,B,f,k,C,a){h=String(h);var e=0,b=0,g=h.length,j="",l=0;while(b<g){var A=h.charCodeAt(b);A=A<256?f[A]:-1;e=(e<<C)+A;l+=C;while(l>=a){l-=a;var d=e>>l;j+=k.charAt(d);e^=d<<l;}++b;}if(!B&&l>0){j+=k.charAt(e<<(a-l));}return j;}var i=p.base64=function(b,d,a){return d?i[b](d,a):b?null:this;};i.btoa=i.encode=function(b,a){b=i.raw===false||i.utf8encode||a?r.encode(b):b;b=t(b,false,q,s,8,6);return b+"====".slice((b.length%4)||4);};i.atob=i.decode=function(d,b){d=d.replace(/[^A-Za-z0-9\+\/\=]/g,"");d=String(d).split("=");var a=d.length;do{--a;d[a]=t(d[a],true,m,c,6,8);}while(a>0);d=d.join("");return i.raw===false||i.utf8decode||b?r.decode(d):d;};}(jQuery));