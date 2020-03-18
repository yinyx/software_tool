jQuery.extend(jQuery, {
	getBasePath : function() 
	{
		var curWwwPath = window.document.location.href;// 获取当前网址，如：
		// http://localhost:8083/uimcardprj/share/meun.jsp
		var pathName = window.document.location.pathname;// 获取主机地址之后的目录，如：
		// uimcardprj/share/meun.jsp
		var pos = curWwwPath.indexOf(pathName);
		var localhostPaht = curWwwPath.substring(0, pos);// 获取主机地址，如：
		// http://localhost:8083
		var projectName = pathName.substring(0, pathName.substr(1).indexOf("/") + 1);// 获取带"/"的项目名，如：/uimcardprj
		var basePath = localhostPaht + projectName;
		return basePath;
	}
});

/**
 * 悬浮框展示 
 * 悬浮框写法
 * <a  name='需要展示信息【图片给图片地址（数据库内的名称）】' 
 * data-toggle='popover' rel='1'（必写1是文字 2是图片）  
 * data-placement='top'（弹出方向表格建议top）>
 * by cjj
 */
//
function showimg()//刷新表单同时调用本方法渲染悬浮框
{
	$("[data-toggle='popover']").each(function()
	{
		var ele = $(this);
		if(ele.context.rel=="2"||ele.context.rel==2)
		{
			var imgPath =IMGPATH+ele.context.name+"?t="+new Date().getTime();
			if(ele.context.name==null||ele.context.name=="null")
			{
				imgPath = "/manage/image/user/noImg.png";
			}
			ele.popover({
				trigger : 'hover',//鼠标以上时触发弹出提示框
				html:true,//开启html 为true的话，data-content里就能放html代码了
				content:"<img src='"+imgPath+"'height='200' width='200'>"         
			}); 
		}
		else
		{
			ele.popover({
				trigger : 'hover',//鼠标以上时触发弹出提示框
				html:true,//开启html 为true的话，data-content里就能放html代码了
				content:"<a style='word-wrap: break-word; word-break: normal;'>"+ele.attr("hov")+"</a>"         
			});
		}
	});
}


//引用bootstrap后的select重构dom
function getNewSelect(select_id)
{
	var $selectUl = $($($("button[data-id='"+select_id+"']").parent().children().get(1)).find("ul"));
	var size=$("#"+select_id).attr("data-size")==undefined?6:$("#"+select_id).attr("data-size");
	$selectUl.attr("style","max-height:"+((size)*34)+"px;overflow-y:auto;");
	return $selectUl;
}
/*
 * 获取数据加载新的select  
 * 将需要加载select的数据和id一次传入 
 * data数据是后台返回的数据 格式必须至少包含id和name（id  name 随便）
 * 页面中select样式加入  class="bs-select form-control" data-live-search="true" data-size="6"
 * data-size是显示超过六条之后就会加入滚动条
 * <link href="${base}/js/library/metronic-4.7/assets/global/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css" />
 * <script src="${base}/js/library/metronic-4.7/assets/global/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>
 * <script src="${base}/js/library/metronic-4.7/assets/pages/scripts/components-bootstrap-select.min.js" type="text/javascript"></script>
 */
function createSelectDate(data,select_id,_showValue,_showName)
{
	var $newSelect=getNewSelect(select_id);
	if(typeof(_showValue)=="undefined"||typeof(_showName)=="undefined"){
		for(var key in data[0])
		{
			_showValue=_showValue==null?key:_showValue;
			_showName=_showValue==null?_showName:key;
		}
	}
	$.each(data, function(index, value) 
	{
		var showValue=eval("value."+_showValue);
		var showName=eval("value."+_showName);
		//想原select中添加元素
		$("#"+select_id).append("<option value='"+showValue+"'>"+ showName+ "</option>");
		//向改装后的select添加数据
		if($newSelect.children().length<=data.length)
		{
			$newSelect.append("<li data-original-index="+(index+1)+" class><a tabindex='0' class style data-tokens='null' role='option' aria-disabled='false' aria-selected='false'><span class='text'>"+showName+"</span><span class='fa fa-check check-mark'></span></a></li>");
		}
		clearSelectInput();
	});	
}

//去掉select搜索框的样式
function clearSelectInput()
{
	//清除输入框
	$("div[class='bs-searchbox']").remove();
}
/**
 * 用这种select的清空需要特殊处理.
 * 暂时清空所有的select的样式内容。
 * select_id 下拉框ID
 * content 自定义默认显示内容
 */
function clearSelectContent(select_id,content)
{
	if(typeof(content)=="undefined")
	{
		content="请选择...";
	}
	$("button[role='button'][data-id='"+select_id+"']").attr("title",content);//selected active
	$("button[role='button'][data-id='"+select_id+"'] span:first").text(content);
	var $selectUl = getNewSelect("site_id");
	$selectUl.find("li[class='selected']").removeAttr("class");
	$selectUl.find("li:first").attr("class","selected");
//	$("ul li[class='selected']").removeAttr("class");
//	$("ul[role='listbox'] li:first").attr("class","selected");
	$("#"+select_id).find("option[selected='selected']").removeAttr("selected");
}

/**
 * 设置select的默认值
 */
function setSelectValue(select_id,show_id)
{
	var title = "";
	var $title=$("button[data-id='"+select_id+"']");
	var children=$("#"+select_id).children();
	$.each(children,function(index,value){
		if(show_id==value.value){
			title = value.text;
			$(value).attr("selected","true");
			return false;
		}
	});
	$title.attr("title",title);
	$($title.children().get(0)).text(title);
}

/**
 * 清空select所有option内容
 */
function clearSelect(select_id)
{
	var $newSelect = getNewSelect(select_id);
	var $select=$("#"+select_id);
	$.each($newSelect.children(),function(index,$dom){
		if(index>0)
		{
			$dom.remove();
		}
	});
	$.each($select.children(),function(index,$dom){
		if(index>0)
		{
			$dom.remove();
		}
	});
}
/**
 * 时间格式化
 */
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function setSelectLength(select_id,length)
{
	$("button[role='button'][data-id='"+select_id+"']").attr("style","width:"+length+";");//selected active
}

/**
 * 
 * 功能描述：判断文件大小
 *
 * @param  data  <上传的input的id>
 * 
 * @param  type  <上传文件类型数组>
 *
 * @param  size  <大小限制字节1m=1024>
 * 
 * @author chenjunjin
 *
 * @date 2017-3-24 10:42:25
 */
function checkFileSizeAndType(data,type,size)
{
  var file = document.getElementById(data); 
  var length = file.files.length;
  var checkType = false;
  var checkSize = false;
  var checkResult = [];
  for(var i=0; i<length; i++)
    {
     checkFile = file.files[i];
     var fileType = checkFile.name.substring(checkFile.name.lastIndexOf("."));//获得文件类型
     if(size >= checkFile.size)
     {
       checkSize = true;
     }
     for(var i=0; i<type.length; i++)
     {
       if(type[i]==fileType)
       {   
         checkType = true;
         break;//结束循环 
       } 
     
     }
     checkResult.push( { "checkSize": checkSize, "checkType": checkType });

  }
  return checkResult;
}
