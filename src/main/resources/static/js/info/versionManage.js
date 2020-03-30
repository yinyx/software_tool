//初始化表格
var branchTable = null;
var userMap = {};
var userId = 0;

function initSchoolUserTable() {
	
	branchTable = $('#versionTable').DataTable({
		// url
		"sAjaxSource" : "history/queryHistoryList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({"name":"Kind","value":$("#cronKind").val()});
            aoData.push({"name":"softId","value":$("#cronSoftware").val()});
            aoData.push({"name":"branchId","value":$("#cronBranch").val()});
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
						console.log(data.VersionData);
						fnCallback(data.VersionData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					showSuccessOrErrorModal("获取软件版本信息失败","error");
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
			 "title" : "所属软件",  
			 "defaultContent" : "", 
			 "data" :"name",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	{	
			 "title" : "分支名称",  
			 "defaultContent" : "", 
			 "data" :"branch",
			 "width": "10%",
			 "class" : "text-center"  
		 }
		 ,	{
                "title" : "版本号",
                "defaultContent" : "",
                "data" :"history_version",
                "width": "10%",
                "class" : "text-center"
            }
            ,	 {
			 "title" : "创建时间",  
			 "defaultContent" : "", 
			 "data" :"upload_date",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	 {	
			 "title" : "更新内容",  
			 "defaultContent" : "", 
			 "data" :"appPkt_new",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "操作人",  
			 "defaultContent" : "", 
			 "data" :"operator",
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
				  content = '<button class="btn btn-xs blue" onclick="EditIcon(\''+row.history_id+'\') " data-toggle="modal" data-target="#"> 编辑 </button>' +
				 '<button class="btn btn-xs red" onclick="deleteVersion(\''+row.history_id+'\')"> 删除版本 </button>';
		         return content;
		      } 
		 }]
	});
}	

// 点击安装配置按钮
function InstallConfig(softwareId){
	console.log(softwareId)
	startPageLoading();
	var data = {"softwareId":softwareId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"software/getInstallConfigBySoftwareId",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
			   $("#softwareId_attribute").val(softwareId);
               var InstallConfig = data.InstallConfig;
			   console.log(InstallConfig)
			   
			   	var InstallTypeList = document.getElementById("cronInstallType_installAttribute");
				var ops = InstallTypeList.options;
				for(var i=0;i<ops.length; i++){
				    var tempValue = ops[i].value;
				    if(tempValue == InstallConfig.type) //这里是你要选的值
				    {
				       ops[i].selected = true;
				       break;
				    }
				}	
				
			    var MultiFlagList = document.getElementById("cronMultiFlag");
				var ops1 = MultiFlagList.options;
				console.log(MultiFlagList)
				console.log(ops1)
					for(var i=0;i<ops1.length; i++){
				    var tempValue1 = ops1[i].value;
				    if(tempValue1 == InstallConfig.Is_multi) //这里是你要选的值
				    {
				       ops1[i].selected = true;
				       break;
				    }
				} 	
			   
			   $("#Installer_installAttribute").val(InstallConfig.installer);
               $("#Uninstaller_installAttribute").val(InstallConfig.uninstaller);			
               $("#KeyFile_installAttribute").val(InstallConfig.KeyFile);
   			   
               $('#installAttributeModal_edit').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal(data.msg,"error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("获取软件安装配置信息请求出错了","error"); 
		}
	});
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
function EditIcon(historyId){
	console.log(historyId);
	startPageLoading();
	var data = {"historyId":historyId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	};
	$.ajax({
		url:"history/getVersionById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
			   $("#recordId_icon").val(historyId);
               var versionData = data.versionData;
               $("#softwareName_icon").attr("disabled", true);
               $("#icon_now_text").attr("disabled", true);
			   $("#softwareName_icon").val(versionData.name);
               $("#icon_now_text").val(versionData.icon);
               var html = '<img src='+versionData.icon+'>';
               $("#icon_now_show").html(html);			   
               $('#iconModal_edit').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading();
			   showSuccessOrErrorModal("获取软件信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("获取软件信息请求出错了","error"); 
		}
	});
}
function queryVersion() {//条件查询同步日志
    branchTable.ajax.reload();
}
//刷新表格
function querySchoolUser()
{
    branchTable.draw();
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
function deleteVersion(historyId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"history/deleteVersion",
			type:"post",
			data:{"historyId":historyId},
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
	
	$.ajax({
		url:"software/queryMarqueeInfo",
		type:"post",
		data:{},
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
			    var eachNum = data.eachSoftwareNum;
	            var allSoftwareNum = data.allSoftwareNum
				var showAll = "系统录入软件："+allSoftwareNum+"款     其中，";
				showAll = '<font color=blue size=4>&nbsp;&nbsp;&nbsp;&nbsp;'+showAll+'</font>';
			   var showEach = "";
               for(var key in eachNum){    
			     var originText = key +": "+eachNum[key]+"款"
                 showEach+='<font color=green size=4>&nbsp;&nbsp;&nbsp;&nbsp;'+originText+'</font>';;
			   }  
				var str = showAll+showEach;
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
		async:false,
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var regulatorList = data.dataList;
		    	var str = "";
                str+='<option value="0">---所有类别---</option>';
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

function initSoftware(){
	var kindId = $("#cronKind").val();
	var data = {"kindId":kindId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"software/querySoftwaresByKind",
		type:"post",
		data:dataObj,
		dataType:"text",
		async:false,
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var regulatorList = data.dataList;
		    	var str = "";
                str+='<option value="0">---所有软件---</option>';
		        for (var int = 0; int < regulatorList.length; int++) {
					str+= '<option value="'+regulatorList[int].id+'">'+regulatorList[int].name+'</option>';
				}
		        $("#cronSoftware").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("初始化软件列表下拉框请求出错了","error"); 
		}
	});	
}

function initBranch(){
    var softId = $("#cronSoftware").val();
    var data = {"softId":softId};
    var dataObj = {
        "paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
    };
    $.ajax({
        url:"history/queryBranchBySelect",
        type:"post",
        data:dataObj,
        dataType:"text",
        async:false,
        success:function(data) {
            data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
            if(data.status=="success") {
                var regulatorList = data.dataList;
                var str = "";
                str+='<option value="0">---所有分支---</option>';
                for (var int = 0; int < regulatorList.length; int++) {
                    str+= '<option value="'+regulatorList[int].branch_id+'">'+regulatorList[int].branch+'</option>';
                }
                $("#cronBranch").html(str);
            } else {
                showSuccessOrErrorModal(data.msg,"error");
            }
        },
        error:function(e) {
            showSuccessOrErrorModal("初始化软件列表下拉框请求出错了","error");
        }
    });
}

$("select#cronKind").change(function(){
   initSoftware();
});
$("select#cronSoftware").change(function(){
    initBranch();
});

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
	initKind();
	initSoftware();
    initBranch();
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
	
	//更新安装配置信息
	$("#editInstallAttributeForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var data = $("#editInstallAttributeForm").serialize();
		data+="&installType="+$("#cronInstallType_installAttribute").val();
		data+="&multiFlag="+$("#cronMultiFlag").val();
		$.ajax({
			url:"software/updateInstallAttribute",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	schoolUserTable.draw();
			    	$("#installAttributeModal_edit").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("更新安装配置信息请求出错了","error"); 
			}
		});	
	}
	);
});