//初始化表格
var branchTable = null;
var userMap = {};
var userId = 0;

function initBranchTable() {
	
	branchTable = $('#branchTable').DataTable({
		// url
		"sAjaxSource" : "branch/queryBranchsList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({ "name": "SoftId", "value": $("#cronSoftware").val()});
			aoData.push({ "name": "branch_name", "value": $("#branch_name").val()});
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
						fnCallback(data.BranchsData);
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
		,	 {	
			 "title" : "创建时间",  
			 "defaultContent" : "", 
			 "data" :"create_time",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	 {	
			 "title" : "描述",  
			 "defaultContent" : "", 
			 "data" :"description",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "操作人",  
			 "defaultContent" : "", 
			 "data" :"user_name",
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
				  content = '<button class="btn btn-xs blue" onclick="editBranch(\''+row.branch_id+'\') " data-toggle="modal" data-target="#"> 编辑 </button>' +
				 '<button class="btn btn-xs red" onclick="deleteBranch(\''+row.branch_id+'\')"> 删除 </button>';
		         return content;
		      } 
		 }]
	});
}	

function queryBranchs() {//条件查询软件分支
	branchTable.ajax.reload();  
}

// 点击编辑分支按钮
function editBranch(branchId){
	console.log(branchId)
	startPageLoading();
	var data = {"branchId":branchId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"branch/getBranchById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
			   $("#branchId").val(branchId);
               var branchData = data.branchData;
			   console.log(branchData)	
               var  softwareName = $("#cronSoftware option:checked").text();			   
			   $("#softwareName").val(softwareName);
               $("#branchName").val(branchData.branch);			
               $("#branchDescription").val(branchData.description);
			   $("#softwareName").attr("disabled", true);
               $('#branchModal_add').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal(data.msg,"error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("获取软件分支信息请求出错了","error"); 
		}
	});
}

//删除软件
function deleteBranch(branchId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"branch/checkBranchName",
			type:"post",
			data:{"branchId":branchId},
			dataType:"text",
			async:false,
			success:function(data) {
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
			    if(data.status=="success") {
			    } else {
			        showSuccessOrErrorModal(data.msg,"error"); 
					return;
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("检查分支是否为主分支请求出错了","error"); 
				return;
			}
		});
		$.ajax({
			url:"branch/deleteBranch",
			type:"post",
			data:{"branchId":branchId},
			dataType:"text",
			success:function(data) {
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
			    if(data.status=="success") {
			        showSuccessOrErrorModal(data.msg,"success"); 
			        branchTable.draw(); //刷新表格
			    } else {
			        showSuccessOrErrorModal(data.msg,"error"); 
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("删除分支请求出错了","error"); 
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

function addBranch(){
	var  softwareName = $("#cronSoftware option:checked").text();
	$("#softwareName").val(softwareName);
	$("#softwareName").attr("disabled", true);
	$("#branchId").val("");
	$("#branchName").val("");
	$("#branchDescription").val("");
	
}

$("select#cronKind").change(function(){
	$("#branch_name").val("");
    initSoftware();
    branchTable.draw();
});

$("select#cronSoftware").change(function(){
	$("#branch_name").val("");
    branchTable.draw();
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
	initBranchTable();
	// 表单验证(点击submit触发该方法)
	$("#softwareForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var data = $("#softwareForm").serialize();
		data+="&userId="+userId;
		data+="&softwareId="+$("#cronSoftware").val();;
		$.ajax({
			url:"branch/saveBranch",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	branchTable.draw();
			    	$("#branchModal_add").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("保存软件分支出错了","error"); 
			}
		});	
	});
	
	$("#branchName").on('change blur',function(e){
		    var branchName = this.value;
			var self = this;
			var softwareId = $("#cronSoftware").val();
		    $.ajax({
			url:"branch/queryBranchNameIsRepeat",
			type:"post",
			data:{branchName:branchName,
			      softwareId:softwareId},
			dataType:"json",
			success:function(data) {
				console.log(data)
			    if(data.status=="success") {
			    	if(!data.flag){
			    		console.log(self)
		 		        if ($.testRemind.display == false && $.html5Validate.isRegex(self)) {
		 		            $(self).testRemind("当前软件的该分支名已存在，请确认"); 
		 		            $(self).focus();
		 		        }    
			    	}
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求查询分支是否重名出错了","error"); 
			}
		});
	});
});