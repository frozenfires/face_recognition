
(function (jQuery){

  /**
   * 为下拉列表动态添加option选项
   */
  jQuery.fn.loadOption = function(items, keyname, valuename, selectedkey){
    var target = this.find('.options'),
        keyname = keyname || 'k',
        valuename = valuename || 'v';
    if(target.length > 0){
      var optionhtml = [];
      for(var i=0; i< items.length; i++){
        var item = items[i];
        optionhtml.push('<li><a href="javascript:void(0);" value="'+item[keyname]+'">'+item[valuename]+'</a></li>');
        if(item.selected){
          this.find('.item').text(item[valuename]);
          this.attr('value', item[keyname])
              .attr('text', item[valuename]);
        }
      }
      target.html(optionhtml.join('\n'));
    }
    else if(this.prop('tagName') === 'SELECT'){
      var optionhtml = [];
      // 添加预留option
      this.find('option[empty]').each(function(){
        optionhtml.push(this.outerHTML);
      });

      for(var i=0; i< items.length; i++){
        var item = items[i];
        if(item[keyname]==selectedkey){
          optionhtml.push('<option value="'+item[keyname]+'" selected>'+item[valuename]+'</option>');
        } else{
          optionhtml.push('<option value="'+item[keyname]+'">'+item[valuename]+'</option>');
        }
      }
      this.html(optionhtml.join('\n'));
    }

    return this;
  };

  /**
   * 下拉列表设值
   */
  jQuery.fn.selectValue = function(){
    var value = arguments[0];
    if(value || value === ''){
      var selectObj = this,
        setName = '', setValue = '';

      selectObj.find('.options li a').each(function(){
        // console.debug('111111111111111');
        var oname=$(this).text(),
          ovalue = $(this).attr('value');
        // console.debug('ovalue='+ovalue+',oname='+oname);
        if(ovalue === value){
          setName =oname;
          setValue = ovalue;
        }
      });

      // console.debug('namej='+selectObj.attr('name')+ ',setName='+setName+',setValue='+setValue)
      selectObj.find('.item').text(setName);
      selectObj.attr('value', setValue)
            .attr('text', setName);
          
      return this;
    }else{

      return this.attr('value');
    }
  };

    return jQuery;
})(jQuery);
