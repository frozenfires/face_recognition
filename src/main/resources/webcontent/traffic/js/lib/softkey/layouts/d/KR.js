VirtualKeyboard.Langs.KR=new function(){var a=this;a.Jamo={"ㄱ":[14,44032,1],"ㄲ":[6,44620,2],"ㄳ":[4,-1,3],"ㄴ":[14,45208,4],"ㄵ":[4,-1,5],"ㄶ":[4,-1,6],"ㄷ":[6,45796,7],"ㄸ":[2,46384,0],"ㄹ":[14,46972,8],"ㄺ":[4,-1,9],"ㄻ":[4,-1,10],"ㄼ":[4,-1,11],"ㄽ":[4,-1,12],"ㄾ":[4,-1,13],"ㄿ":[4,-1,14],"ㅀ":[4,-1,15],"ㅁ":[6,47560,16],"ㅂ":[14,48148,17],"ㅃ":[2,48736,0],"ㅄ":[4,-1,18],"ㅅ":[14,49324,19],"ㅆ":[6,49912,20],"ㅇ":[6,50500,21],"ㅈ":[6,51088,22],"ㅉ":[2,51676,0],"ㅊ":[6,52264,23],"ㅋ":[6,52852,24],"ㅌ":[6,53440,25],"ㅍ":[6,54028,26],"ㅎ":[6,54616,27],"ㅏ":[1,0,0],"ㅐ":[1,28,0],"ㅑ":[1,56,0],"ㅒ":[1,84,0],"ㅓ":[1,112,0],"ㅔ":[1,140,0],"ㅕ":[1,168,0],"ㅖ":[1,196,0],"ㅗ":[1,224,0],"ㅛ":[1,336,0],"ㅜ":[1,364,0],"ㅠ":[1,476,0],"ㅡ":[1,504,0],"ㅣ":[1,560,0]};a.VV2V=[0,0,0,0,0,0,0,0,0,224,224,224,0,0,364,364,364,0,0,504,0];a.V2VV=[0,0,0,0,0,0,0,0,{"ㅏ":252,"ㅐ":280,"ㅣ":308},0,0,0,0,{"ㅓ":392,"ㅔ":420,"ㅣ":448},0,0,0,0,{"ㅣ":532},0,0];a.CV2C="ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ".split("");a.C2CC={"ㄱ":"ㄲ","ㄷ":"ㄸ","ㅂ":"ㅃ","ㅅ":"ㅆ","ㅈ":"ㅉ"};a.CC2C={"ㄲ":"ㄱ","ㄸ":"ㄷ","ㅃ":"ㅂ","ㅆ":"ㅅ","ㅉ":"ㅈ"};a.PP2P=[0,0,1,1,0,4,4,0,0,8,8,8,8,8,8,8,0,0,17,0,19,0,0,0,0,0,0,0];a.PP2PC=[0,[0,44032],[0,44620],[1,49324],[0,45208],[4,51088],[4,54616],[0,45796],[0,46972],[8,44032],[8,47560],[8,48148],[8,49324],[8,53440],[8,54028],[8,54616],[0,47560],[0,48148],[17,49324],[0,49324],[0,49912],[0,50500],[0,51088],[0,52264],[0,52852],[0,53440],[0,54028],[0,54616]];a.P2PP=[0,{"ㄱ":2,"ㅅ":3},0,0,{"ㅈ":5,"ㅎ":6},0,0,0,{"ㄱ":9,"ㅁ":10,"ㅂ":11,"ㅅ":12,"ㅌ":13,"ㅍ":14,"ㅎ":15},0,0,0,0,0,0,0,0,{"ㅅ":18},0,{"ㅅ":20},0,0,0,0,0,0,0,0];a.flags=0;a.parseHangul=function(c){if(c==""||c.length>1){return null;}var b=c.charCodeAt();if(b<12593||b>55203){return null;}else{if(b<12623&&b>12592){return[a.Jamo[c][1],-1,0];}}b-=44032;var d=[];d[0]=44032+588*(b/588>>0);b%=588;d[1]=28*(b/28>>0);d[2]=b%28;return d;};a.charProcessor=function(k,f,d,h,g){var n=a.Jamo[k];if(!h){h=a.parseHangul(f);}if(h==null){if(!n){return[k,0];}else{if(n[0]&2){return[k,1];}else{return[k,0];}}}else{if(k=="\u0008"){if(h[2]){return[String.fromCharCode(h[0]+h[1]+a.PP2P[h[2]]),1];}else{if(h[1]>-1){var j=a.VV2V[h[1]/28];if(j){return[String.fromCharCode(h[0]+j),1];}else{return[a.CV2C[(h[0]-44032)/588],1];}}else{if(a.CC2C[f]){return[a.CC2C[f],1];}else{a.flags=0;return["",0];}}}}else{if(!n){a.flags=0;return[f+k,0];}else{if(h[2]){if(n[0]&2){var b=a.P2PP[h[2]][k];if(b){return[String.fromCharCode(h[0]+h[1]+b),1];}else{return[f+k,1];}}else{if(n[0]&1){if(g&&h[2]==21){return[f+String.fromCharCode(50500+n[1]),1];}return[String.fromCharCode(h[0]+h[1]+a.PP2PC[h[2]][0])+String.fromCharCode(a.PP2PC[h[2]][1]+a.Jamo[k][1]),1];}else{return[f+k,0];}}}else{if(h[1]>-1){a.flags&=~3;if(n[0]&4){return[String.fromCharCode(h[0]+h[1]+n[2]),1];}else{if(n[0]&1){if(g){var i;if(a.flags&4&&(i="\u3153\u3154\u3163".indexOf(k))!=-1){a.flags&=~4;return[String.fromCharCode(h[0]+[392,308,448][i]),1];}}var m=a.V2VV[h[1]/28][k];if(m){return[String.fromCharCode(h[0]+m),1];}else{if(g){return[f+String.fromCharCode(50500+n[1]),1];}else{return[f+k,0];}}}else{return[f+k,1];}}}else{if(n[0]&1){return[String.fromCharCode(a.Jamo[f][1]+n[1]),1];}else{if(f==k&&a.C2CC[f]){return[a.C2CC[f],1];}else{return[f+k,1];}}}}}}}};};