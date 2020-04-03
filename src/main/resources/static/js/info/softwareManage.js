//初始化表格
var schoolUserTable = null;
var userMap = {};
var userId = 0;
var userAgent = navigator.userAgent; //用于判断浏览器类型
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
			 "title" : "安装配置",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
				  content = '<button class="btn btn-xs green" onclick="InstallConfig(\''+row.id+'\')"> 安装配置 </button>';
		         return content;
		      } 
		 }	
		,  {	
			 "title" : "截图",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
				  content = '<button class="btn btn-xs green" onclick="ScreenShotConfig(\''+row.id+'\')"> 截图 </button>';
		         return content;
		      } 
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

// 点击安装配置按钮
function InstallConfig(softwareId){
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
			   $("#uuid_text_attribute").attr("disabled", true);
			   $("#description_attribute").val(softwareData.brief_introduction);
			   $("#version_attribute").val(softwareData.latest_version);	
			   $("#version_attribute").attr("disabled", true);
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
	
	var fileCatcher = document.getElementById("editIconForm");
    var file_icon = document.getElementById("icon_icon");

    fileCatcher.addEventListener("submit",function (event) {
        event.preventDefault();
		updateIcon();
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
		$.ajax({
            url:"software/uploadIcon",
            type:"post",
            data:formData,
            dataType:"json",
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            success:function(data) {
                if(data.status == "success") {
					$("#iconModal_edit").modal("hide");
					querySchoolUser();
					showSuccessOrErrorModal(data.msg,"success");
                    stopPageLoading();
                } else {
                    stopPageLoading();
                    showSuccessOrErrorModal(data.msg,"error");
                }

            },
            error:function(e) {
                stopPageLoading();
                showSuccessOrErrorModal("设置软件图标信息请求出错了","error");
            }
        });
    };
}

//刷新表格
function querySchoolUser()
{	
	schoolUserTable.draw(); 
};
//新增软件按钮
function addSoftware(){
	var softKind = $("#cronKind").val();
	if ((softKind == null)||(softKind == 0))
	{
		showSuccessOrErrorModal("增加软件之前请先在软件类别下拉框指定一款软件类别！","warning");
		
		return;
	}
	
	$("#softdir_now_label").attr("style","display:none;");
	$("#softdir_now_text").attr("style","display:none;");
	$("#uuid_label").attr("style","display:none;");
	$("#uuid_text").attr("style","display:none;");
	$("#softLabel").attr("style","display:;");
	$("#soft").attr("style","display:;");
	$("#softwareForm")[0].reset();
	$("#recordId").val("");
	var  softwareKind = $("#cronKind option:checked").text();
	$("#softwareKindRead").val(softwareKind);
	$("#softwareKindRead").attr("disabled", true);
	$('#softwareModal_add').modal('show');
	
	var fileCatcher = document.getElementById("softwareForm");
    var file_icon = document.getElementById("icon");
    var file_soft = document.getElementById("soft");

    fileCatcher.addEventListener("submit",function (event) {
        event.preventDefault();
        sendFile();
        //$("#softwareModal_add").modal("hide");
		//schoolUserTable.draw();
		//showSuccessOrErrorModal("创建软件信息对象成功","success");
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
        var kind = $("#cronKind").val();
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
			"briefIntroduction":briefIntroduction,
			"userId":userId
        };
        formData.append("softwareForm",JSON.stringify(data));
		
		$.ajax({
            url:"software/uploadSoftware",
            type:"post",
            data:formData,
            dataType:"json",
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            success:function(data) {
                if(data.status == "success") {
					$("#softwareModal_add").modal("hide");
					querySchoolUser();
					showSuccessOrErrorModal(data.msg,"success");
                    stopPageLoading();
                } else {
                    stopPageLoading();
                    showSuccessOrErrorModal(data.msg,"error");
                }

            },
            error:function(e) {
                stopPageLoading();
                showSuccessOrErrorModal("创建软件信息对象请求出错了","error");
            }
        });
    };
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
                    
                    html += '<label class="control-label">';
                    html += '<input type="radio" name="type" id="'+data.dataList[i].id+'" value= "'+data.dataList[i].id+'" checked="checked">' +data.dataList[i].kind_name;
                    html += '</label>';
                    $("#type_radio").append(html);
					
					var html_attribute = "";
                    
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

function ScreenShotConfig(softwareId){
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
                $("#screenShotsId").val(softwareId);
                var softwareData = data.softwareData;
                $("#softwareName_screen").attr("disabled", true);
                $("#softwareName_screen").val(softwareData.name);
                $('#screenShots_edit').modal('show');
                stopPageLoading()
            } else {
                stopPageLoading();
                showSuccessOrErrorModal("获取软件信息失败","error");
            }

        },
        error:function(e) {
            stopPageLoading();
            showSuccessOrErrorModal("获取软件信息请求出错了","error");
        }
    });

    var fileList=[];
    var fileCatcher = document.getElementById("screenShotsForm");
    var files = document.getElementById("screenShots_list");

    fileCatcher.addEventListener("submit",function (event) {
        event.preventDefault();
        sendFile();
    });

    files.addEventListener("change",function () {
        for(var i = 0;i<files.files.length;i++){
            fileList.push(files.files[i]);
            var div="<img id='img"+i+"' width='10%' height='10%' src='"+window.URL.createObjectURL(files.files[i])+"'/>";
            $("#fileDiv").append(div);
        }
    });
    $('#screenShots_edit').on('hide.bs.modal', function () {
        document.getElementById("screenShots_list").value = "";
        $('#fileDiv').empty();
        document.getElementById("screenShotsForm").reset();
    });
    sendFile = function () {
        var formData = new FormData();
        fileList.forEach(function(file){
            formData.append("screenShots",file,file.name);
        });

        formData.append("softwareId",softwareId);

        $.ajax({
            url:"software/uploadBatchScreenShots",
            type:"post",
            data:formData,
            dataType:"json",
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            success:function(data) {
                if(data.status == "success") {
					$("#screenShots_edit").modal("hide");
					showSuccessOrErrorModal("设置截图信息成功","success");
                    stopPageLoading();
                	
                } else {
                    stopPageLoading();
                    showSuccessOrErrorModal("设置截图信息失败","error");
                }

            },
            error:function(e) {
                stopPageLoading();
                showSuccessOrErrorModal("设置截图信息请求出错了","error");
            }
        });
    };
}

$("select#cronKind").change(function(){
   schoolUserTable.draw();
});

$(document).ready(function(){
	//判断是否登录
	userMap = isLogined();
	if(userMap){//成功登录
		userId = userMap.id;
	}else{
		//parent.location.href = jQuery.getBasePath() + "/login.html";
	}
	$("#queryButton").hide();
	clearInterval(timer);
	showTime();
	timer = setInterval("showTime()",10000);
	showKind();
    //sendFile();
	//updateIcon();
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
	
	//检测新增软件的中文名是否重复
	$("#softwareName").on('change blur',function(e){
		    var softwareName = this.value;
			var kind = $("#cronKind").val();
			var self = this;
		    $.ajax({
			url:"software/querySoftwareNameIsRepeat",
			type:"post",
			data:{
				  softwareName:softwareName,
			      kind:kind
			     },
			dataType:"json",
			success:function(data) {
				console.log(data)
			    if(data.status=="success") {
			    	if(!data.flag){
			    		console.log(self)
		 		        if ($.testRemind.display == false && $.html5Validate.isRegex(self)) {
		 		            $(self).testRemind("当前类别该软件中文名已存在，请确认"); 
		 		            $(self).focus();
		 		        }    
			    	}
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求查询当前类别的软件中文名是否重名出错了","error"); 
			}
		});
	});
	
	//检测新增软件的英文名是否重复
	$("#softwareName_en").on('change blur',function(e){
		    var softwareName_en = this.value;
			var kind = $("#cronKind").val();
			var self = this;
		    $.ajax({
			url:"software/querySoftwareEnNameIsRepeat",
			type:"post",
			data:{
				   softwareName_en:softwareName_en,
				   kind:kind
				 },
			dataType:"json",
			success:function(data) {
				console.log(data)
			    if(data.status=="success") {
			    	if(!data.flag){
			    		console.log(self)
		 		        if ($.testRemind.display == false && $.html5Validate.isRegex(self)) {
		 		            $(self).testRemind("当前类别的该软件英文名已存在，请确认"); 
		 		            $(self).focus();
		 		        }    
			    	}
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求查询当前类别的软件英文名是否重名出错了","error"); 
			}
		});
	});
});