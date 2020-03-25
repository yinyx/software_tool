//初始化表格
var schoolUserTable = null;
var userMap = {};
var userId = 0;

function initSchoolUserTable() {
	
	schoolUserTable = $('#schoolUserTable').DataTable({
		// url
		"sAjaxSource" : "software/querySoftwaresList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({ "name": "Kind", "value": $("#cronKind").val()});
			$.ajax({
				type: "POST",
				url: sSource,
				dataType: "json",
				contentType: "application/json",
				data: JSON.stringify(aoData),
				success: function(data) 
				{
					
					if(data.status == "success")
					{
						fnCallback(data.SoftwaresData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					showSuccessOrErrorModal("获取软件信息失败","error");
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
			 "title" : "图标",  
			 "defaultContent" : "", 
			 "data" :"icon",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 console.log(data)
				 var pic = data;
				 var content = "";
				  content = '<img src='+pic+'>';
				 return content;
		      }   
		 }  
		,	{	
			 "title" : "名称",  
			 "defaultContent" : "", 
			 "data" :"name",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	 {	
			 "title" : "英文名称",  
			 "defaultContent" : "", 
			 "data" :"name_en",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
	/* 	,	 {	
			 "title" : "简介",  
			 "defaultContent" : "", 
			 "data" :"brief_introduction",
			 "width": "10%",
			 "class" : "text-center"  
		 }   */
		,	 {	
			 "title" : "大小",  
			 "defaultContent" : "", 
			 "data" :"size",
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
				  content = '<button class="btn btn-xs blue" onclick="EditIcon(\''+row.id+'\') " data-toggle="modal" data-target="#"> 编辑图标 </button>' +
                 '<button class="btn btn-xs green" onclick="EditAttribute(\''+row.id+'\')"> 编辑属性 </button>'+
				 '<button class="btn btn-xs red" onclick="deleteSchoolUser(\''+row.id+'\')"> 删除 </button>';
		         return content;
		      } 
		 }]
	});
}	

function querySoftwares() {//条件查询同步日志
	schoolUserTable.ajax.reload();  
}

// 点击编辑属性按钮
function EditAttribute(softwareId){
	console.log(softwareId)
	startPageLoading();
	var data = {"softwareId":softwareId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"software/getSoftwareById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
			   $("#recordId_attribute").val(softwareId);
               var softwareData = data.softwareData;
			   $("#softwareName_attribute").val(softwareData.name);	
               $("#softwareName_enattribute").val(softwareData.name_en);
               $("#uuid_text_attribute").val(softwareData.soft_id);
			   $("#description_attribute").val(softwareData.brief_introduction);
			   $("#version_attribute").val(softwareData.latest_version);	
			   $("input[type='radio']").each(function(){
				   if($(this).val()==softwareData.kind){
					   $(this).prop("checked",true)
				   }
				   else{
					   $(this).prop("checked",false)
				   }
			   });
			   	var InstallTypeList = document.getElementById("cronInstallType_attribute");
				var ops = InstallTypeList.options;
				for(var i=0;i<ops.length; i++){
				    var tempValue = ops[i].value;
				    if(tempValue == softwareData.install_type) //这里是你要选的值
				    {
				       ops[i].selected = true;
				       break;
				    }
				}			   
               $('#attributeModal_edit').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取软件信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("获取软件信息请求出错了","error"); 
		}
	});
}

// 点击编辑图标按钮
function EditIcon(softwareId){
	console.log(softwareId)
	startPageLoading();
	var data = {"softwareId":softwareId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"software/getSoftwareById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
			   $("#recordId_icon").val(softwareId);
               var softwareData = data.softwareData;
               $("#softwareName_icon").attr("disabled", true);
               $("#icon_now_text").attr("disabled", true);
			   $("#softwareName_icon").val(softwareData.name);
               $("#icon_now_text").val(softwareData.icon);			   
               var html = '<img src='+softwareData.icon+'>';
               $("#icon_now_show").html(html);			   
               $('#iconModal_edit').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取软件信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("获取软件信息请求出错了","error"); 
		}
	});
}

//刷新表格
function querySchoolUser()
{	
	schoolUserTable.draw(); 
};
//新增软件按钮
function addSoftware(){
	$("#softdir_now_label").attr("style","display:none;");
	$("#softdir_now_text").attr("style","display:none;");
	$("#uuid_label").attr("style","display:none;");
	$("#uuid_text").attr("style","display:none;");
	$("#softLabel").attr("style","display:;");
	$("#soft").attr("style","display:;");
	$("#softwareForm")[0].reset();
	$("#recordId").val("");
}

//删除软件
function deleteSchoolUser(softwareId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"software/deleteWholeSoftware",
			type:"post",
			data:{"softwareId":softwareId},
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
				
			    showSuccessOrErrorModal("删除软件请求出错了","error"); 
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

function initKind(){
	$.ajax({
		url:"softwareKind/queryAllKinds",
		type:"post",
		data:{},
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var regulatorList = data.dataList;
		    	var str = "";
				str+='<option value="0">---所有软件---</option>';
		        for (var int = 0; int < regulatorList.length; int++) {
					str+= '<option value="'+regulatorList[int].id+'">'+regulatorList[int].kind_name+'</option>';
				}
		        $("#cronKind").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("初始化软件类别列表下拉框请求出错了","error"); 
		}
	});	
}

function sendFile() {
    var fileCatcher = document.getElementById("softwareForm");
    var file_icon = document.getElementById("icon");
    var file_soft = document.getElementById("soft");

    fileCatcher.addEventListener("submit",function (event) {
        event.preventDefault();
        sendFile();
        alert("添加成功");
        window.location.reload();
    });

    sendFile = function () {
        var formData = new FormData();
        var request = new XMLHttpRequest();
		formData.append("icon",file_icon.files[0]);
        formData.append("soft",file_soft.files[0]);
        var name = $(":input[name='softwareName']").val();
        var nameEn = $(":input[name='softwareName_en']").val();
        var introduction = $(":input[name='description']").val();
        var latestVersion = $(":input[name='version']").val();
        var kind = $(":radio[name=\"type\"]:checked").val();
		var installType = $("#cronInstallType").val();
		var briefIntroduction = $("#description").val();
		var recordId = $("#recordId").val();
		
        var data = {
			"id":recordId,
            "name":name,
            "nameEn":nameEn,
            "introduction":introduction,
            "latestVersion":latestVersion,
            "kind":kind,
			"installType":installType,
			"briefIntroduction":briefIntroduction
        };
        formData.append("softwareForm",JSON.stringify(data));

        request.open("POST","software/uploadSoftware");
        request.send(formData);
    };
}

function updateIcon() {
    var fileCatcher = document.getElementById("editIconForm");
    var file_icon = document.getElementById("icon_icon");

    fileCatcher.addEventListener("submit",function (event) {
        event.preventDefault();
        updateIcon();
        alert("更新图标成功");
        window.location.reload();
    });

    updateIcon = function () {
        var formData = new FormData();
        var request = new XMLHttpRequest();
		formData.append("icon",file_icon.files[0]);
		var recordId = $("#recordId_icon").val();
		var iconpath = $("#icon_now_text").val();
		var data = {
			"id":recordId,
			"iconpath":iconpath
        };
		formData.append("editIconForm",JSON.stringify(data));
        request.open("POST","software/uploadIcon");
        request.send(formData);
    };
}

function showKind(){
    $.ajax({
        url:"softwareKind/queryAllKinds",
        type:"post",
        dataType: "text",
        contentType: "application/json",
        success:function (data) {
            data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
            if(data.status == "success")
            {
                for (var i = 0; i < data.dataList.length; i++) {
                    var html = "";
                    console.log(data.dataList[i].id);
                    html += '<label class="control-label">';
                    html += '<input type="radio" name="type" id="'+data.dataList[i].id+'" value= "'+data.dataList[i].id+'" checked="checked">' +data.dataList[i].kind_name;
                    html += '</label>';
                    $("#type_radio").append(html);
					
					var html_attribute = "";
                    console.log(data.dataList[i].id);
                    html_attribute += '<label class="control-label">';
                    html_attribute += '<input type="radio" name="type_attribute" id="'+data.dataList[i].id+'" value= "'+data.dataList[i].id+'" checked="checked">' +data.dataList[i].kind_name;
                    html_attribute += '</label>';
					$("#type_radio_attribute").append(html_attribute);
                }
            }else{
                showSuccessOrErrorModal(data.msg,"error");
            }
        }
    })

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
	showKind();
    sendFile();
	updateIcon();
	initKind();
	initSchoolUserTable();
	// 表单验证(点击submit触发该方法)
	$("#editAttributeForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var data = $("#editAttributeForm").serialize();
		var kind = $(":radio[name=\"type_attribute\"]:checked").val();
		data+="&kind="+kind;
		data+="&installType="+$("#cronInstallType_attribute").val();
		$.ajax({
			url:"software/updateSoftware",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	schoolUserTable.draw();
			    	$("#attributeModal_edit").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("更新软件请求出错了","error"); 
			}
		});	
	}
	);
	
	//更新图标
	$("#editIconForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var data = $("#editAttributeForm").serialize();
		var kind = $(":radio[name=\"type_attribute\"]:checked").val();
		data+="&kind="+kind;
		data+="&installType="+$("#cronInstallType_attribute").val();
		$.ajax({
			url:"software/updateSoftware",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	schoolUserTable.draw();
			    	$("#attributeModal_edit").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("更新软件请求出错了","error"); 
			}
		});	
	}
	);
});