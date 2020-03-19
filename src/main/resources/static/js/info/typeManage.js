//初始化表格
var schoolUserTable = null;
var userMap = {};
var userId = 0;

function initSchoolUserTable() {
	console.log("usersData")
	schoolUserTable = $('#schoolUserTable').DataTable({
		// url
		"sAjaxSource" : "softwareKind/queryKindsList", 
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
						fnCallback(data.kindsData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					showSuccessOrErrorModal("获取软件种类信息失败","error");
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
			 "title" : "类别",  
			 "defaultContent" : "", 
			 "data" :"kind_name",
			 "width": "10%",
			 "class" : "text-center"  
		 }            
		,  {	
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
				  content = '<button class="btn btn-xs blue" onclick="showUserEditModal(\''+row.id+'\') " data-toggle="modal" data-target="#"> 编辑 </button>' +
                 '<button class="btn btn-xs red" onclick="deleteSchoolUser(\''+row.id+'\')"> 删除 </button>';
		         return content;
		      } 
		 }]
	});
}	
// 点击编辑按钮
function showUserEditModal(kindId){
	startPageLoading();
	var data = {"kindId":kindId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"softwareKind/getKindById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
               var kindData = data.kindData;
               console.log(kindData)
               $("#kindId").val(kindId);
			   $("#kindName_m").val(kindData.kind_name);
               $('#kindModal_add').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取软件种类信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("获取软件种类信息请求出错了","error"); 
		}
	});
}
//刷新表格
function querySchoolUser()
{	
	schoolUserTable.draw(); 
};
//新增用户按钮
function addSoftwareKind(){
	$("#kindForm")[0].reset();
	$("#kindId").val("");
}
//删除用户
function deleteSchoolUser(kindId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"softwareKind/deleteKind",
			type:"post",
			data:{"kindId":kindId},
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
			    showSuccessOrErrorModal("删除种类请求出错了","error"); 
			}
		});
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
	$("#kindForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var data = $("#kindForm").serialize();
		console.log(data);
		$.ajax({
			url:"softwareKind/saveKind",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	schoolUserTable.draw();
			    	$("#kindModal_add").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("保存软件种类请求出错了","error"); 
			}
		});
		
	}
	);
	$("#kindName_m").on('change blur',function(e){
		var kindName = this.value;
		var self = this;
		$.ajax({
			url:"softwareKind/queryKindNameIsRepeat",
			type:"post",
			data:{kindName:kindName},
			dataType:"json",
			success:function(data) {
				console.log(data)
			    if(data.status=="success") {
			    	if(!data.flag){			    	
		 		        if ($.testRemind.display == false && $.html5Validate.isRegex(self)) {
		 		            $(self).testRemind("该软件类别名已存在"); 
		 		            $(self).focus();
		 		        }    
			    	}
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("查询软件种类名请求出错了","error"); 
			}
		});
	});
	
});