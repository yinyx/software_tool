//初始化表格
var schoolUserTable = null;
var userMap = {};
var userId = 0;

function cancelModifyWavePassword()
{
	$("#password_wave").val("");
	$("#password_wave_new").val("");
	$("#password_wave_new_c").val("");
}

function closeModal(){
	$("#password_wave").val("");
	$("#password_wave_new").val("");
	$("#password_wave_new_c").val("");
}

function initSchoolUserTable() {
	console.log("usersData")
	schoolUserTable = $('#schoolUserTable').DataTable({
		// url
		"sAjaxSource" : "user/queryUsersList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			
			$.ajax({
				type: "POST",
				url: sSource,
				dataType: "json",
				contentType: "application/json",
				data: JSON.stringify(aoData),
				success: function(data) 
				{
					console.log(JSON.stringify(data))
					if(data.status == "success")
					{
						fnCallback(data.usersData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					showSuccessOrErrorModal("获取用户信息失败","error");
				}
			});
		},
		// 列属性-类处理
		"columnDefs" : [ 
            {
	            targets: [0],
	        	render: function(data, type, row, meta) {// 在这里 时间格式化、内容太多优化显示、一列显示多列值 等问题进行优化
	        		return isNullThen(data);
	        	}
		 }],
		// 列属性
		"columns" : [ 
		 {	
			 "title" : "用户名",  
			 "defaultContent" : "", 
			 "data" :"phone",
			 "width": "10%",
			 "class" : "text-center"  
		 }            
		,  {	
			 "title" : "姓名",  
			 "defaultContent" : "", 
			 "data" :"user_name",
			 "width": "10%",
			 "class" : "text-center"  
		 },{	
			 "title" : "角色",  
			 "defaultContent" : "", 
			 "data" :"role",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="管理员";
		            if(data == 0){
		            	content = "app用户";
		            }
		            return content;
		      }   
		 },{	
			 "title" : "权限",  
			 "defaultContent" : "", 
			 "data" :"operate_authority",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="全部";
		            if(data == 1){
		            	content = "浏览";
		            }
		            return content;
		      }   
		 },{	
			 "title" : "状态",  
			 "defaultContent" : "", 
			 "data" :"has_del",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="未启用";
		            if(data == 0){
		            	content = "启用";
		            }
		            return content;
		      }   
		 },{	
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
				  content = '<button class="btn btn-xs blue" onclick="showUserEditModal(\''+row.id+'\') " data-toggle="modal" data-target="#"> 编辑 </button>' +
                 '<button class="btn btn-xs red" onclick="deleteSchoolUser(\''+row.id+'\')"> 删除 </button>'+
                 '<button class="btn btn-xs red" onclick="resetPassword(\''+row.id+'\')"> 重置密码 </button>';
		         return content;
		      } 
		 }]
	});
}	
// 点击编辑按钮
function showUserEditModal(userId){
	startPageLoading();
	var data = {"userId":userId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"user/getUserById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
               var usersData = data.usersData;
               console.log(usersData)
               $("#userId").val(userId);
			   $("#userName_m").val(usersData.phone);
			   $("#realName_m").val(usersData.user_name);

			   if(usersData.has_del == '0') {
       			   $("#s_on").prop("checked",true); 
       		   } else {
       			   $("#s_off").prop("checked",true); 
       		   }
			   if(usersData.operate_authority == '0') {
       			   $("#r_all").prop("checked",true); 
       		   } else {
       			   $("#r_read").prop("checked",true); 
       		   }
			   if(usersData.role == '1') {
       			   $("#r_admin").prop("checked",true); 
       		   } else {
       			   $("#r_app").prop("checked",true); 
       		   }
               $('#userModal_add').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取用户信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("获取用户信息请求出错了","error"); 
		}
	});
}
//刷新表格
function querySchoolUser()
{	
	schoolUserTable.draw(); 
};
//新增用户按钮
function addSchoolUser(){
	$("#userForm")[0].reset();
	$("#userId").val("");
	$("input[type='checkbox']").each(function(){
		$(this).attr("checked",'checked')
	});
}
//删除用户
function deleteSchoolUser(userId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"user/deleteUser",
			type:"post",
			data:{"userId":userId},
			dataType:"text",
			success:function(data) {
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
			    if(data.status=="success") {
			        showSuccessOrErrorModal(data.msg,"success"); 
			        schoolUserTable.draw(); //刷新表格
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
				console.error(e)
			    showSuccessOrErrorModal("删除用户请求出错了","error"); 
			}
		});
	});
	
}
//重置密码
function resetPassword(userId){
	showConfirmModal("是否为该用户重置密码！",function(){
		var data = {"id":userId};
		var dataObj = {
				"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
		}
		$.ajax({
			url:"user/resetPassword",
			type:"post",
			data:dataObj,
			dataType:"text",
			success:function(data) {
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
				console.log(data)
			    if(data.status=="success") {
			        showSuccessOrErrorModal(data.msg,"success"); 
			        schoolUserTable.draw(); //刷新表格
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("重置密码出错了","error"); 
			}
		});
	});
	
}

function initLine(){
		$.ajax({
			url:"sys/queryLine",
			type:"post",
			data:{},
			dataType:"text",
			success:function(data) {
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
			    if(data.status=="success") {
			    	var lineList = data.dataList;
			    	var str = "";
			        for (var int = 0; int < lineList.length; int++) {
						str+='<label class="control-label"><input  type="checkbox"  value="'+lineList[int].id+'" />'+lineList[int].name+'</label> ';
					}
			        $("#lineDiv").html(str);
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求出错了","error"); 
			}
		});
	
}

function showTime(){
	var newDateObj = new Date(); 
	var year = newDateObj.getFullYear();
	var month = newDateObj.getMonth()+1;
	if(month==13)
		{
		month =1;
		}
	var day = newDateObj.getDate();
	var week = newDateObj.getDay();
	var arr = new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
	var hour = newDateObj.getHours();
	var minute = newDateObj.getMinutes();
	var second = newDateObj.getSeconds();
	var showTime = year+"/"+month+"/"+day+" "+arr[week]+" "+hour+((minute<10)?":0":":")
	               +minute+((second<10)?":0":":")+second+((hour>12)?" 下午":" 上午");
	showTime = '<font color=red size=4>'+showTime+'</font>';
	
	var data = {"userId":userId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	
	$.ajax({
		url:"info/queryMarqueeInfo",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var allDeviceNum = data.allDeviceNum;
				var onlineDeviceNum = data.onlineDeviceNum;
				var offlineDeviceNum = data.offlineDeviceNum;
				var noReadAlarmNum = data.noReadAlarmNum;
				var noReadFaultNum = data.noReadFaultNum;
	            var showDevice ="系统接入设备："+allDeviceNum+"台     系统正常设备："
				           +onlineDeviceNum+"台     系统异常设备："+offlineDeviceNum+"台 ";
	            showDevice = '<font color=blue size=4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+showDevice+'</font>';
	            var showFault = "    未读故障信息："+noReadFaultNum+"条";
	            showFault = '<font color=green size=4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+showFault+'</font>';
	            var showAlarm = "    未读告警信息："+noReadAlarmNum+"条";
	            showAlarm = '<font color=red size=4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+showAlarm+'</font>';
	            var str=/*showTime + */showDevice + showFault + showAlarm;
	            $("#marqueeTitle").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    //showSuccessOrErrorModal("滚动栏请求出错了","error"); 
		}
	});		
}

$(document).ready(function(){
	//判断是否登录
	userMap = isLogined();
	if(userMap){//成功登录
		userId = userMap.id;
	}else{
		//parent.location.href = jQuery.getBasePath() + "/login.html";
	}
	clearInterval(timer);
	showTime();
	timer = setInterval("showTime()",10000);
	
	initSchoolUserTable();
	// 表单验证(点击submit触发该方法)
	$("#userForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var data = $("#userForm").serialize();
		console.log(data);
		$.ajax({
			url:"user/saveUser",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	schoolUserTable.draw();
			    	$("#userModal_add").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("保存用户请求出错了","error"); 
			}
		});
		
	}
	);
	$("#userName_m").on('change blur',function(e){
		var userName = this.value;
		var self = this;
		$.ajax({
			url:"user/queryUserNameIsRepeat",
			type:"post",
			data:{userName:userName},
			dataType:"json",
			success:function(data) {
				console.log(data)
			    if(data.status=="success") {
			    	if(!data.flag){
			    		console.log(self)
		 		        if ($.testRemind.display == false && $.html5Validate.isRegex(self)) {
		 		            $(self).testRemind("该用户名已存在"); 
		 		            $(self).focus();
		 		        }    
			    	}
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求出错了","error"); 
			}
		});
	});
	$("#editWavePwd").html5Validate(function() {
		var password_new = $("#password_wave_new").val();
		var data = {
				"id":userMap.id,
				"password" : password_new
			};
		var dataObj = {
				"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
		}
		$.ajax({
			url : "sys/saveWaveWord",
			type : "post",
			data :dataObj,
			dataType : "text",
			success : function(data) {
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
				if (data.status == "success") {
					showSuccessOrErrorModal(data.msg, "success");
					$("#editWavePwd")[0].reset();
					$("#waveModal").modal("hide");
				} else {
					showSuccessOrErrorModal(data.msg, "error");
				}
			},
			error : function(e) {
				showSuccessOrErrorModal("保存密码请求出错了", "error");
			}
		})
	}, {
		validate : function() {
			if (!verifyWavePwd($("#password_wave").val())) {
				$("#password_wave").testRemind("您输入的密码不正确!");
				return false;
			} else if ($("#password_wave_new").val() != $("#password_wave_new_c").val()) {
				$("#password_wave_new_c").testRemind("您2次输入的密码不相同!");
				return false;
			}
			return true;
		}
	});
	
});
function log(formstr){
	var res = {};
	formstr.split('&').forEach(function(i){
        var j = i.split('=');
        res[j[0]]=j[1];
    });
	console.log(res)
}

//验证密码
function verifyWavePwd(pwd) {
	var oldPwd = userMap.wave_psd;
	if (pwd == oldPwd) {
		return true;
	} else {
		return false;
	}
}

function showUpload(){
	
	$('#upload').click();
	$('#upload').change(function(){
		var file=$('#upload').val();
		var arr=file.split('\\');
		var fileName=arr[arr.length-1];
		$('#file').text(fileName);	
	});
	
		
}
function uploadExcelFile(){	
	//创建表单  
	var formData = new FormData($( "#uploadForm" )[0]);  
	var file = $("#upload").val();  
	var arr=file.split('\\');//注split可以用字符或字符串分割  
	var fileName=arr[arr.length-1];//这就是要取得的文件名称  
	formData.append("file",$("#upload")[0].files[0]);  
	if ($('#upload').val() == "") {  
		showSuccessOrErrorModal("请选择所要上传的文件","error");
	}else{  
        var index = file.lastIndexOf("."); 
        if(index < 0 ){  
        	showSuccessOrErrorModal("上传的文件格式不正确，请选择Excel文件(*.xls)！","error");                      
        }else{  
            var ext = file.substring(index + 1, file.length);  
            if(ext == "xls"){  
            	doUpload(formData,fileName);  
            }else{  
                showSuccessOrErrorModal("上传的文件格式不正确，请选择Excel文件(*.xls)！","error");  
            }  
        }  
	}  
} 
//上传
function doUpload(formData,fileName) {  
    $.ajax({  
         url:"sys/import.do" ,  
         type: 'POST',  
         data: formData,  
         async: false,  
         cache: false,  
         contentType: false,  
         processData: false, 
         dataType: "json",
         success: function (data) {  
        	if(data.status=="success"){
             
             $('#excel_improt').modal('hide');
             showSuccessOrErrorModal(data.msg,"success");
             schoolUserTable.draw(); //刷新表格
        	}else{
        		showSuccessOrErrorModal(data.msg,"error");
        	}
         },
         error: function (data) { 
        	stopPageLoading()
        	showSuccessOrErrorModal("请求出错了","error"); 
            
            
         }  
    });  
}

//模板下载
function excelfileDown(){
	var ACCESSTOKENID =$.cookie("ACCESSTOKEN");
	var url="sys/downFile.do?ACCESSTOKEN="+ACCESSTOKENID;
	location.href=url;
}