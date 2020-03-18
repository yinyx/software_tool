/*
 * function.JS
 * 公共方法
 */

/**
 * 获取下拉框数据数据
 * 
 * @Author : ZhengZhongYi
 * @Date : 2017-9-20
 * @param1 : 院系,部门 manage/org/queryFacultyOrDepartment.do?type (1院系,2部门)
 * @param2 : 角色 manage/schoolUser/queryRole.do
 * @param3 : 字典表 
 * @param falg: 是否是多选框 默认为false (暂未使用)
 * @returns: html,下拉框dom
 * 
 * 
 */

//是否登录
function isLogined(){

	try{
		var userMap = $.parseJSON(decrypt($.cookie('userMap'),"abcd1234abcd1234"));
		if(userMap.phone){
			return userMap;
		}else{
			return false;
		}
	}
	catch(err){
		return false;
	}
	/*
	try{
		var user = $.parseJSON(decrypt($.cookie('user'),"abcd1234abcd1234"));
		if(user.phone){
			return user;
		}else{
			return false;
		}
	}
	catch(err){
		return false;
	}*/
	/*var userMap = $.cookie('userMap')?$.parseJSON(decrypt($.cookie('userMap'),"abcd1234abcd1234") ):false;
	if(userMap.){
		return userMap;
	}else{
		
	}*/
}
//登出
function logout(){
	$.cookie('userMap', null);
	parent.location.href = jQuery.getBasePath() + "/login";
}
function getDropDownData(url,isCode) {
	//设置默认参数
	url=url||''; 
	isCode=isCode||false; 
	var html = "";
	if(url!=''){
		$.ajax({
			url : url,
			type : "post",
			dataType : "json",
			async : false,
			success : function(data) {
				//console.log(data)
				if (data.status == "success") {
					var dataList = data.dataList;
					for (var i = 0; i < dataList.length; i++) {
						var val;
						if (isCode) {
							val = dataList[i].dataCode;
						} else {
							val = dataList[i].dataId;
						}
						if(dataList[i].status==0){
							
							html += '<option value="' + val + '" data-subtext="未启用">'
							+ dataList[i].dataName + '</option>';
						}else{
							html += '<option value="' + val + '">'
							+ dataList[i].dataName + '</option>';
						}
					
					}
				} else {
					showSuccessOrErrorModal("获取数据失败", "error");
				}
			},
			error : function(e) {
				showSuccessOrErrorModal("请求出错了", "error");
			}
		});
	}
	return html;
}

/**
 * 分类获取下拉框数据数据
 * 
 * @Author : ChenYi
 * @Date : 2017-9-20
 * @param1 : types(类型数组)
 * @returns: dropDownDatas
 */
function getDropDownByTypes(types) {
	var dropDownMap;
	$.ajax({
		url : "common/queryDropDownByTypes.do",
		type : "post",
		data : {"types" : types},
		dataType : "json",
		async : false,
	    traditional: true, 
		success : function(data) {
			if (data.status == "success") {
				dropDownMap = data.dropDownMap;
			} else {
				showSuccessOrErrorModal("获取数据失败", "error");
			}
		},
		error : function(e) {
			showSuccessOrErrorModal("请求出错了", "error");
		}
	});
	return dropDownMap;
}

/**
 * 组装下拉框
 * 
 * @Author : ChenYi
 * @Date : 2017-9-20
 * @param1 : dataList
 * @param2 : isCode
 * @returns: dropDownDatas
 */
function assembleDropDown(dataList, isCode) {
	var html;
	for (var i = 0; i < dataList.length; i++) {
		var val;
		if (isCode) {
			val = dataList[i].dataCode;
		} else {
			val = dataList[i].dataId;
		}
		if(dataList[i].status==0){
			html += '<option value="' + val + '" data-subtext="未启用">'
			+ dataList[i].dataName + '</option>';
		}else{
			html += '<option value="' + val + '">'
			+ dataList[i].dataName + '</option>';
		}
	}
	return html;
}

/**
 *  判断字符串是否包含某子字符串
 *  @Author	: 	ChenYi
 *  @Date	:   2017-9-20
 *  @param	:   str 源字符串
 *  @param	:   substr 待验证子字符串
 *  @returns:   boolean 是否包含
 * 
 */
function isContains(str, substr) {
    return str.indexOf(substr) >= 0;
}
/**
 *  身份证号 正则判断函数
 *  @Author	: 	ZhengZhongYi
 *  @Date	:   2017-9-23
 *  @param	:   code 源字符串
 *  @returns:   obj.pass 是否验证通过
 *   @returns:  obj.tip 验证不通过原因
 * 
 */
var validIdentity = function(code){
	var obj = {};
	var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
	obj.tip = "";
	obj.pass= true;
	if(!code || !/(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/i.test(code)){
		obj.tip = "身份证号格式错误";
		obj.pass = false;
	}else if(!city[code.substr(0,2)]){
		obj.tip = "地址编码错误";
		obj.pass = false;
	}else{
		if(code.length == 18){
			code = code.split('');
			var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
			var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
			var sum = 0;
			var ai = 0;
			var wi = 0;
			for (var i = 0; i < 17; i++){
				ai = code[i];
				wi = factor[i];
				sum += ai * wi;
			}
			var last = parity[sum % 11];
			if(parity[sum % 11] != code[17]){
				obj.tip = "校验位错误";
				obj.pass =false;
			}
		}
	}
	return obj;
}

/**
 * 获取URL地址中，根据key获取对应的value值
 * @param _name
 * @returns
 */
function getUrlParam(_name){
	var reg = new RegExp("(^|&)" + _name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) {
	   return unescape(r[2]);
	}else{
		return null;
	}
}

/**
 * 浮点数相加
 * @param arg1
 * @param arg2
 * @returns
 */
function floatPlus(arg1,arg2) { 
	var r1,r2,m; 
	try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	m=Math.pow(10,Math.max(r1,r2)) 
	return parseFloat((arg1*m+arg2*m)/m); 
}

/**
 * 浮点数相减
 * @param arg1
 * @param arg2
 * @returns
 */
function floatMinus(arg1,arg2) { 
	 var r1,r2,m,n; 
	 try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	 try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	 m=Math.pow(10,Math.max(r1,r2)); 
	 n=(r1>=r2)?r1:r2; 
	 return parseFloat(((arg1*m-arg2*m)/m).toFixed(n));
}

/**
 * 在url地址中新增修改指定参数值信息
 * par变量名称
 * par_value变量取值
 * */
function changeURLParam(destiny, par, par_value) { 
    var pattern = par+'=([^&]*)'; 
    var replaceText = par+'='+par_value; 
    if (destiny.match(pattern)) { 
      var tmp = '/\\'+par+'=[^&]*/'; 
      tmp = destiny.replace(eval(tmp), replaceText); 
      return (tmp); 
      } else { 
    	  if (destiny.match('[\?]')) {
    		  return destiny+'&'+ replaceText; 
    		  } else { 
    			  return destiny+'?'+replaceText; 
    			  } 
    	  } 
    return destiny+'\n'+par+'\n'+par_value; 
 } 

/**
 *  展示加载条
 *  @Author	: 	ZhengZhongYi
 *  @Date	:   2017-10-08
 *  @param	:   options
 * 
 */
function startPageLoading(options) {
	options = $.extend(true, {
        type: 'img', // loading图片还是css3动画 'css3'|'img' 其他入参全使用 img
        message: "Loading..." // loading显示的文字信息(仅在img模式下有效)
    }, options);
    if (options.type=="css3") {
        $('.page-spinner-bar').remove();
        $('body').append('<div class="page-spinner-bar"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div>');
    } else {
        $('.page-loading').remove();
        $('body').append('<div class="page-loading"><img src="static/images/common/loading-spinner-grey.gif"/>&nbsp;&nbsp;<span>' + options.message + '</span></div>');
    }
}
/**
 *  关闭加载条
 *  @Author	: 	ZhengZhongYi
 *  @Date	:   2017-10-08
 *  @param	:   
 * 
 */
function stopPageLoading() {
	$('.page-loading, .page-spinner-bar').remove();
}
/**
 *  字符为空处理
 *  @Author	: 	ZhengZhongYi
 *  @Date	:   2017-10-20
 *  @param	:   stra 待处理字符
 * 	@param	:   strb 为空时显示字符 （默认为“--”）
 *  @return :   处理后的字符
 */
function isNullThen(stra,strb){
	var str = "";
	strb=strb||'--'; 
	if(stra==null||stra==""){
		str = strb;
	}else{
		str = stra;
	}
	return str;
}


/**
 * 对Date的扩展，将 Date 转化为指定格式的String
 * 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
 * 例子： 
 * (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
 * (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
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


/**
 * 判断当前浏览器版本和类型
 * **/
function BrowserType() {
	//取得浏览器的userAgent值
	var userAgent = navigator.userAgent;
	//判断是否是Opera
	var isOpera = userAgent.indexOf("Opera") > -1;
	//判断是否是IE浏览器
	var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera;
	//判断是否IE的Edge浏览器
	var isEdage = userAgent.indexOf("Windows NT 6.1;Trident/7.0;") > -1 && !isIE;
	//判断是否Firefox
	var isFF = userAgent.indexOf("Firefox") > -1;
	//判断是否是Safari浏览器
	var isSafiri = userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Chrome") == -1;
	//判断Chrome
	var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1;
	
	if(isIE) {
		var regIE = new RegExp("MSIE (\\d+\\.\\d+);");
		regIE.test(userAgent);
		var IEVersion = parseFloat(RegExp["$1"]);
		if(IEVersion == 7) {
			return "IE7";
		} else if(IEVersion == 8) {
			return "IE8";
		} else if(IEVersion == 9) {
			return "IE9";
		} else if(IEVersion == 10) {
			return "IE10";
		} else if(IEVersion == 11) {
			return "IE11";
		} else {
			//IE 版本过滤
			return "0";
		}
	}
	
	if (isFF) {
		return "FF";
	}
	
	if(isOpera) {
		return "Opera";
	}
	
	if(isSafari) {
		return "Safari";
	}
	
	if(isChrome) {
		return "Chrome";
	}
	
	if(isEdge) {
		return "Edge";
	}
}

/**
 * 判断是否是IE
 * **/
function isIE() {
	var userAgent = navigator.userAgent;
	//判断是否是Opera
	var isOpera = userAgent.indexOf("Opera") > -1;
	var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera;
	if(isIE) {
		return true;
	} else {
		return false;
	}
}

/**
 * 获取IE版本号
 * **/
function IEVersion() {
	var userAgent = navigator.userAgent;
	//判断是否是Opera
	var isOpera = userAgent.indexOf("Opera") > -1;
	var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera;
	var isEdge = userAgent.indexOf("Windows NT 6.1;Trident/7.0") > -1 && !isIE;
	if(isIE) {
		var regIE = new RegExp("MSIE (\\d+\\.\\d+);");
		regIE.test(userAgent);
		var IEVersion = parseFloat(RegExp["$1"]);
		if(IEVersion == 7 || IEVersion == 8 || IEVersion == 9 || IEVersion == 10 || IEVersion == 11) {
			return IEVersion;
		} else {
			return 0;
		}
	} else if(isEdge){
		return 9999;
	} else {
		return -1;
	}
}

/**
 * 将图片转换成base64编码
 * **/
function getBase64Image(img) {
	var canvas = document.createElement("canvas");
	canvas.width = img.width;
	canvas.height = img.height;
	var ctx = canvas.getContext("2d");
	ctx.drawImage(img,0,0,img.width,img.height);
	var ext = img.src.substring(img.src.lastIndexOf(".")+1).toLowerCase();
	var dataUrl = canvas.toDataURL("image/jpeg");
	return dataUrl;
}

/**
 * 根据text设置为选中
 * el select id 
 * **/
function setSelectByText(el,text) {
	$('#'+el+' option:contains('+text+')').each(function(){
		if($(this).text() == text) {
			$(this).attr('selected',true);
		}
	});
}

/**
 * 根据层次获取专业下拉选项
 * levelObject 层次的jquery对象
 * majorObject 专业的jquery对象
 */
function getMajorOptions(levelObject,majorObject){
	levelObject.change(function(){
		var html="<option value=''>全部</option>";	
		var $level=levelObject.val();
			/**if($level==''||$level==null){
				return;	
			}*/	
		   $.ajax({
			 type:"post",
			  url:"manage/applyDegree/getMajorContent.do",
			 data:{"id":$level},
		 dataType:"json",
		  success:function(data){
			  if(data.status=="success"){
				
				   $(data.resMap).each(function(){
					html+='<option value='+this.id+'>'+this.major+'</option>'   
				   });
				   majorObject.html(html).selectpicker("refresh");
			
			  }
		  },
	        error:function(){
	        	showSuccessOrErrorModal("查询的专业数据异常！","error");
	        }		 
			 		 
	 });
	});
  }


function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

/***************************************************** 
* AES加密 
* @param content 加密内容 
* @param key 加密密码，由字母或数字组成 
　　　　　　此方法使用AES-128-ECB加密模式，key需要为16位 
　　　　　　加密解密key必须相同，如：abcd1234abcd1234 
* @return 加密密文 
****************************************************/

function encrypt(content, key){
    var sKey = CryptoJS.enc.Utf8.parse(key);
    var sContent = CryptoJS.enc.Utf8.parse(content);
    var encrypted = CryptoJS.AES.encrypt(sContent, sKey, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
    return encrypted.toString();
}

/***************************************************** 
* AES解密 
* @param content 加密密文 
* @param key 加密密码，由字母或数字组成 
　　　　　　此方法使用AES-128-ECB加密模式，key需要为16位 
　　　　　　加密解密key必须相同，如：abcd1234abcd1234 
* @return 解密明文 
****************************************************/

function decrypt(content, key){
    var sKey = CryptoJS.enc.Utf8.parse(key);
    var decrypt = CryptoJS.AES.decrypt(content, sKey, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
    return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}


