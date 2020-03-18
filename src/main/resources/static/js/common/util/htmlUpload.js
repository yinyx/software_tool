/**
 * h5大文件分片上传
 * 支持断点续传
 * by wangyajun
 */
var BSHfileUpload = function()
{ 
  
	/**上传访问服务地址**/
	var common_upload_url = "http://192.168.1.14:8080/FileServer";
    //私有变量和函数
    var Fileurl = null;//文件名称
	var paragraph = 1024*200;//每次分片传输文件的大小 200k
	var blob = null;//分片数据的载体Blob对象
	var fileList = null;//传输的文件
	var uploadState = 0;// 0: 无上传/取消， 1： 上传中， 2： 暂停  ， 3：上传成功
	var fr;
	var progress;
	var progressOne;
	var progressTwo; 
	/**
	 * 仅仅获得该文件存在服务器文件大小，并获得上传的文件名称
	 * @param data 上传的input的id
	 * @param fileUploadType 上传的文件用途 0:测试，1:用户，2:课程，3:站点(除此之外新增，请与此开发人员联系)
	 * @param fileSavePath 二级目录
	 */
	getFileInfos = function(tagid,fileUploadType,fileSavePath){
		fileList = document.getElementById(tagid);
		var fileInfos = [];
		var length = fileList.files.length;
        for(var i=0; i<length; i++)
        {
            file = fileList.files[i];
            
            var startSize = 0;
            var endSize = 0;
            var date = file.lastModifiedDate;
            var lastModifyTime = date.Format("yyyy-MM-dd hh:mm:ss");
            //获取当前文件已经上传大小
			$.ajax({
				  type: 'POST',
				  url: common_upload_url+"/file/getChunkedFileInfo",
				  data:  {"fileName":file.name,"fileSize":file.size,"lastModifyTime":lastModifyTime,"fileUploadType":fileUploadType,"saveFilePathKey" : fileSavePath},
				  async: false,//同步
				  dataType: "json",
				  success:   function(data)
				  {	  
					  if(data.status == "success")
		              {
		                	if(data.size != -1)
		                	{
		                		 endSize = Number(data.size);
		                	}
		                	else
		                	{
		                		 endSize = 0;
		                	}
		                   
		              }
		              else
		              {	
		                	showSuccessOrErrorModal(data.msg, "error");
		              }
					fileInfos.push( { "name": data.name, "size": endSize, "fileUploadType": fileUploadType ,"fileSavePath": fileSavePath , "downLoadPath": data.downLoadPath, "resourcePath": data.resourcePath});
				  }
			 });   
        }
	return fileInfos;
  }
	
	/**
	 * 仅仅获得该文件存在服务器文件大小，并获得上传的文件名称-BLOB
	 * @author ZhengZhongYi
	 * @date 2017-10-10 
	 * @param blob对象
	 * @param fileUploadType 上传的文件用途 0:测试，1:用户，2:课程，3:站点(除此之外新增，请与此开发人员联系)
	 * @param fileSavePath 二级目录
	 */
	getBlobFileInfos = function(blob,fileUploadType,fileSavePath){
            file = blob;
            var fileInfos = [];
            
            var startSize = 0;
            var endSize = 0;
            var date = file.lastModifiedDate;
            var lastModifyTime = date.Format("yyyy-MM-dd hh:mm:ss");
            //获取当前文件已经上传大小
			$.ajax({
				  type: 'POST',
				  url: common_upload_url+"/file/getChunkedFileInfo",
				  data:  {"fileName":file.name,"fileSize":file.size,"lastModifyTime":lastModifyTime,"fileUploadType":fileUploadType,"saveFilePathKey" : fileSavePath},
				  async: false,//同步
				  dataType: "json",
				  success:   function(data)
				  {	  
					  if(data.status == "success")
		              {
		                	if(data.size != -1)
		                	{
		                		 endSize = Number(data.size);
		                	}
		                	else
		                	{
		                		 endSize = 0;
		                	}
		                   
		              }
		              else
		              {	
		                	showSuccessOrErrorModal(data.msg, "error");
		              }
					fileInfos.push( { "name": data.name, "size": endSize, "fileUploadType": fileUploadType ,"fileSavePath": fileSavePath , "downLoadPath": data.downLoadPath, "resourcePath": data.resourcePath});
				  }
			 });   
	return fileInfos;
  }
	
 /**
 * 上传初始入口
 * @param tagid 上传的input的id
 */						
uploadFiles = function (tagid,progressBarDivId,progressBarBackgroundId,progressBarNumberId,fileInfos)
{
	fileList = document.getElementById(tagid);
	progress = progressBarDivId;
	progressOne = progressBarBackgroundId;
	progressTwo = progressBarNumberId;
	$("#"+progress).prop("hidden",true);//进度条
	$("#"+progressOne).attr("style","width: 0%;");
	$("#"+progressTwo).text("0%");
    if(fileList.files.length>0)
    {
        for(var i = 0; i< fileList.files.length; i++)
        {
        	uploadState = 1;
            var file = fileList.files[i];
            Fileurl = fileInfos[i].name;
			uploadFile(file,0,fileInfos[i].size,i,fileInfos[i].fileUploadType,fileInfos[i].fileSavePath);
        }
    }
    else
    {
    	showInfoModal("请选择上传文件!");
    }
}

uploadBlobbFiles = function(dataUrl,progressBarDivId,progressBarBackgroundId,progressBarNumberId,fileInfo){
	progress = progressBarDivId;
	progressOne = progressBarBackgroundId;
	progressTwo = progressBarNumberId;
	$("#"+progress).prop("hidden",true);//进度条
	$("#"+progressOne).attr("style","width: 0%;");
	$("#"+progressTwo).text("0%");
	var data = {};
	data.dataUrl = dataUrl.replace("data:image/jpeg;base64,","");
	if(fileInfo != null){
		data.flieUrl = fileInfo.fileSavePath;
	} else {
		data.flieUrl = "file";
	}
	var resourcePath = "";
	if(dataUrl!=""&&dataUrl!=null){
		$.ajax({
			  type: 'POST',
			  url: common_upload_url+"/file/appendB64Server",
			  data:  data,
			  async: false,//同步
			  dataType: "json",
			  success:   function(data){	  
			  	console.log(data);
			  	resourcePath = data.resourcePath;
			  	$("#"+progressOne).attr("style","width: 100%;");
	        	$("#"+progressTwo).text("100%"); 
			  },
			  error:function(e){
				  showSuccessOrErrorModal("头像上传文件服务器时出错了", "error");
			  }
		 });
	}
	 
	return resourcePath;
}


/**
 * 上传头像初始入口 BLOB版
 * @param BLOB对象
 * @author ZhongZhengYi
 */						
uploadBlobbHeadFiles = function (dataUrl){
    var data = {};
    if(user.head_portrait_path==""||user.head_portrait_path==null){
    	data.flieUrl = "sys/coverPic/"+user.user_code+"_avatar.jpg";
    }else{
    	data.flieUrl = user.head_portrait_path.split("resource/")[1];
    }
	data.dataUrl = dataUrl.replace("data:image/jpeg;base64,","");
	var resourcePath = "";
	if(dataUrl!=""&&dataUrl!=null){
		$.ajax({
			  type: 'POST',
			  url: common_upload_url+"/file/appendB64Server",
			  data:  data,
			  async: false,//同步
			  dataType: "json",
			  success:   function(data){	  
			  	console.log(data);
			  	resourcePath = data.resourcePath;
			  	//实时更新 top头像
			  	$("#avatarIndex").attr("src",dataUrl);
			  },
			  error:function(e){
				  showSuccessOrErrorModal("头像上传文件服务器时出错了", "error");
			  }
		 });
	}
	 
	return resourcePath;
}
/**
 * 分片上传文件
 * @param file 文件
 * @param startSize 起始位置 0
 * @param endSize 已经上传的位置 
 * @param j 用于列表上传 文件列表序列
 * @param fileUploadType 文件所属类型
 */
uploadFile = function (file,startSize,endSize,j,fileUploadType,fileSavePath)
{
		$("#"+progress).prop("hidden",false);//进度条
        var date = file.lastModifiedDate;
        var lastModifyTime =  date.Format("yyyy-MM-dd hh:mm:ss");
        var reader = new FileReader();
        reader.onload = function loaded(evt)
		{	
        	
            // 构造 xmlHttpRequest 对象，发送文件 Binary 数据
            var xhr = new XMLHttpRequest(); 
                xhr.sendAsBinary = function(text)
                {		
                    var data = new ArrayBuffer(text.length);
                    var ui8a = new Uint8Array(data, 0);
                    for (var i = 0; i < text.length; i++) 
                    {
						ui8a[i] = (text.charCodeAt(i) & 0xff);
					}
                    
                    this.send(ui8a);				
                }

            xhr.onreadystatechange = function()
            {
                if(xhr.readyState==4)
                {
                    //表示服务器的相应代码是200；正确返回了数据   
                   if(xhr.status==200)
                   { 
                       //纯文本数据的接受方法   
                       var message=xhr.responseText; 
                       message = Number(message);
                       uploadProgress(file,startSize,message,j,fileUploadType,fileSavePath);//显示进度
                   } 
                   else
                   {
                       showSuccessOrErrorModal("上传出错，服务器响应错误！", "error");
                   }  
                }  
            };//创建回调方法
            
            xhr.open("POST", 
            		common_upload_url+"/file/appendUploadServer?fileName=" + encodeURIComponent(file.name)+"&fileSize="+file.size+"&lastModifyTime="+lastModifyTime+"&Fileurl="+Fileurl+"&fileUploadType="+fileUploadType+"&saveFilePathKey="+fileSavePath,
            		true); 
            xhr.overrideMimeType("application/octet-stream;charset=utf-8"); 
            xhr.sendAsBinary(evt.target.result)
        };
        if(endSize < file.size) 
        {
            //处理文件发送（字节）
            startSize = endSize;
            if(paragraph > (file.size - endSize))
            {
                endSize = file.size;
            }
            else
            {
                endSize += paragraph ;
            }
            
            
            if (file.webkitSlice) 
            {
              //webkit浏览器
                blob = file.webkitSlice(startSize, endSize);
            }
            else
            {
                blob = file.slice(startSize, endSize);
            }
            reader.readAsBinaryString(blob);        
        }
        else//单文件上传成功
        {
        	uploadState = 3;
        	$("#"+progressOne).attr("style","width: 100%;");
        	$("#"+progressTwo).text("100%"); 
        }
}

/*
暂停上传
*/
pauseUpload = function (){
    uploadState = 2;
}
//显示处理进程
uploadProgress = function (file,startSize,uploadLen,i,fileUploadType,fileSavePath) {
    var percentComplete = Math.round(uploadLen * 100 / file.size);
    $("#"+progressOne).attr("style","width:"+ percentComplete+"%;");
	$("#"+progressTwo).text(percentComplete+"%");

    //续传
    if(uploadState == 1)
    {
        uploadFile(file,startSize,uploadLen,i,fileUploadType,fileSavePath);
    }
}
  
    //公有/特权方法
   return { 
        getFileurl : function(){ 
            return Fileurl; 
        },
		
		registerFileurl : function(data){      
                Fileurl = data; 
        },
			
		getParagraph : function(){ 
            return paragraph; 
        }, 
		
		getFileList : function(){ 
            return fileList; 
        }, 

		registeFileList : function(data){   
            fileList = data; 
        },
		
		getUploadState : function(){ 
            return uploadState; 
        }, 

		registerUploadState : function(data){ 
            uploadState = data; 
        }, 

		getProgress : function(){ 
            return progress; 
        }, 
		
		registerProgress : function(data){ 
            progress = data; 
        } 
    }; 
}(); 



/**
 * 
 * 功能描述：判断文件大小
 *
 * @param  data  <上传的input的id>
 * 
 * @param  type  <上传文件类型数组>
 *
 * @param  size  <大小限制字节1m=1024*1024>
 * 
 * @author wangyajun
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


//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { 
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



