//初始化表格
var pluginTable = null;
var userMap = {};
var userId = 0;

function initTimeSelect(){
    $("#appPkt_date").datetimepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: true,//今日按钮
        format: "yyyy-mm-dd hh:ii"
    });
}

function initSchoolUserTable() {

    pluginTable = $('#pluginTable').DataTable({
		// url
		"sAjaxSource" : "plugin/queryPluginList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({"name":"Kind","value":$("#cronKind").val()});
            aoData.push({"name":"softId","value":$("#cronSoftware").val()});
            aoData.push({"name":"branchId","value":$("#cronBranch").val()});
			aoData.push({"name":"versionId","value":$("#cronVersion").val()});
			aoData.push({"name":"pluginName","value":$("#pluginName").val()});
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
						console.log(data.PluginData);
						fnCallback(data.PluginData);
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
                "title" : "版本",
                "defaultContent" : "",
                "data" :"history_version",
                "width": "10%",
                "class" : "text-center"
            },
            {
                "title" : "插件名",
                "defaultContent" : "",
                "data" :"plugin_name",
                "width": "10%",
                "class" : "text-center"
            }
            ,	 {
			 "title" : "上传时间",  
			 "defaultContent" : "", 
			 "data" :"upload_time",
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
				  content = '<button class="btn btn-xs blue" onclick="updatePlugin(\''+row.plugin_id+'\')" data-toggle="modal" data-target="#"> 更新插件 </button>' ;
		         return content;
		      } 
		 }]
	});
}

function upFileStyle(){
    $('#up_plugin').click();
    //file表单选中文件时,让file表单的val展示到showname这个展示框
    $('#up_plugin').change(function(){
        $('#showname').val($(this).val())
    });
}

function commitFileStyle(){
    $('#pluginPkt').click();
    //file表单选中文件时,让file表单的val展示到showname这个展示框
    $('#pluginPkt').change(function(){
        $('#show').val($(this).val())
    });
}


// 点击安装配置按钮
function InstallConfig(softwareId){
	console.log(softwareId);
	startPageLoading();
	var data = {"softwareId":softwareId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	};
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
	};
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
function updatePlugin(pluginId){
	startPageLoading();
	var data = {"pluginId":pluginId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	};
	$.ajax({
		url:"plugin/getPluginById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
			   $("#recordId_icon").val(pluginId);
               var pluginData = data.pluginData;
			   $("#softwareName_icon").val(pluginData.name).attr("disabled", true);
               $("#nowBranch").val(pluginData.branch).attr("disabled", true);
               $("#nowVersion").val(pluginData.history_version).attr("disabled", true);
               $("#upPluginName").val(pluginData.plugin_name);
               $("#upRelativePath").val(pluginData.relative_path);
               $("#update_description").val(pluginData.description);
               $('#pluginModal_edit').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading();
			   showSuccessOrErrorModal("获取插件信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading();
		   showSuccessOrErrorModal("获取插件信息请求出错了","error");
		}
	});
    var fileCatcher = document.getElementById("updatePluginForm");
    var upSoft = document.getElementById("up_plugin");

    fileCatcher.addEventListener("submit",function (event) {
        event.preventDefault();
        sendFile();
        $("#pluginModal_edit").modal('hide');
        pluginTable.draw();
    });
    $("#pluginModal_edit").on('hide.bs.modal', function () {
        document.getElementById("up_plugin").value = "";
        upSoft = null;
        document.getElementById("updatePluginForm").reset();
    });
    sendFile = function () {
        var formData = new FormData();
        formData.append("upFile",upSoft.files[0]);
        var pluginName =  $("#upPluginName").val();
        var relativePath =  $("#upRelativePath").val();
        var description =  $("#update_description").val();
		var pluginObj  = {
			"pluginId":pluginId,
			"pluginName":pluginName,
			"relativePath": relativePath,
			"description":description,
			"userId":userId
		};
        formData.append("pluginStr",JSON.stringify(pluginObj));
        $.ajax({
            url:"plugin/updatePlugin",
            type:"post",
            data:formData,
            dataType:"json",
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            success:function(data) {
                if(data.status == "success") {
                    $('#pluginModal_edit').modal('hide');
                    pluginTable.draw();
                    stopPageLoading();
                } else {
                    stopPageLoading();
                    showSuccessOrErrorModal("更新插件信息失败","error");
                }

            },
            error:function(e) {
                stopPageLoading();
                showSuccessOrErrorModal("更新插件信息出错了","error");
            }
        });
    };
}
function queryPlugin() {//条件查询同步日志
    pluginTable.ajax.reload();
}
//刷新表格
function querySchoolUser()
{
    pluginTable.draw();
};

function uploadPlugin(){
    var fileListenrer = document.getElementById("pluginForm");
    var pluginPkt = document.getElementById("pluginPkt");
    fileListenrer.addEventListener("submit",function (event) {
        event.preventDefault();
        sendFile();
    });

    $('#pluginModal_add').on('hide.bs.modal', function () {
        document.getElementById("pluginForm").reset();
    });
    sendFile = function () {
        var formData = new FormData();
        formData.append("pluginPkt",pluginPkt.files[0]);
        var kindId = $("#cronKind").val();
        var softwareId = $("#cronSoftware").val();
        var branchId = $("#cronBranch").val();
        var versionId = $("#cronVersion").val();
        var description = $("#description").val();
        var relativePath = $("#relativePath").val();
        var pluginName = $("#cronPluginName").val();
        var pluginObj = {
            "type":kindId,
            "softId":softwareId,
            "branchId":branchId,
			"versionId":versionId,
			"pluginName":pluginName,
            "description":description,
            "relativePath":relativePath,
            "userId":userId
        };
        formData.append("pluginObj",JSON.stringify(pluginObj));
        $.ajax({
            url:"plugin/addPlugin",
            type:"post",
            data:formData,
            dataType:"json",
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            success:function(data) {
                if(data.status == "success") {
                    $('#pluginModal_add').modal('hide');
                    pluginTable.draw();
                    showSuccessOrErrorModal("上传插件成功","success");
                    stopPageLoading();
                } else {
                    stopPageLoading();
                    showSuccessOrErrorModal("上传插件失败","error");
                }

            },
            error:function(e) {
                stopPageLoading();
                showSuccessOrErrorModal("上传插件出错了","error");
            }
        });
    };
}
//新增软件按钮
function addPlugin(){
        var kindName = $("#cronKind  option:checked").text();
        $("#kindName").val(kindName).attr("disabled",true);
        var softwareName = $("#cronSoftware  option:checked").text();
        $("#softwareName").val(softwareName).attr("disabled",true);
        var branchName = $("#cronBranch  option:checked").text();
        $("#branchName").val(branchName).attr("disabled",true);
        var version = $("#cronVersion option:checked").text();
        $("#version").val(version).attr("disabled",true);
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
function initVersion(){
    var branchId = $("#cronBranch").val();
    var data = {"branchId":branchId};
    var dataObj = {
        "paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
    };
    $.ajax({
        url:"plugin/queryVersionBySelect",
        type:"post",
        data:dataObj,
        dataType:"text",
        async:false,
        success:function(data) {
            data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
            if(data.status=="success") {
                var regulatorList = data.dataList;
                var str = "";
                str+='<option value="0">---所有版本---</option>';
                for (var int = 0; int < regulatorList.length; int++) {
                    str+= '<option value="'+regulatorList[int].history_id+'">'+regulatorList[int].history_version+'</option>';
                }
                $("#cronVersion").html(str);
            } else {
                showSuccessOrErrorModal(data.msg,"error");
            }
        },
        error:function(e) {
            showSuccessOrErrorModal("初始化版本列表下拉框请求出错了","error");
        }
    });
}

function queryVersions(){
    queryPlugin();
}

$("select#cronKind").change(function(){
    initSoftware();
    queryPlugin();
});
$("select#cronSoftware").change(function(){
    initBranch();
    queryPlugin();
});

$("select#cronBranch").change(function(){
    initVersion();
    queryPlugin();
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
	initTimeSelect();
	initKind();
	initSoftware();
    initBranch();
    initVersion();
	initSchoolUserTable();
    uploadPlugin();
    // 表单验证(点击submit触发该方法)
	$("#editAttributeForm").html5Validate(function(){
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
	
	//更新程序包配置信息
	$("#AppPktForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var data = $("#AppPktForm").serialize();
		$.ajax({
			url:"history/setVersionInstllconfigById",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	$("#AppPktModal").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("更新程序包配置信息请求出错了","error"); 
			}
		});	
	}
	);
});