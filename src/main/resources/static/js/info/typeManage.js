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
			aoData.push({ "name": "KindName", "value": $("#kind_name").val()});
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
			 "title" : "软件类别（中文）",  
			 "defaultContent" : "", 
			 "data" :"kind_name",
			 "width": "10%",
			 "class" : "text-center"  
		 }            
		,  {	
			 "title" : "软件类别（英文）",  
			 "defaultContent" : "", 
			 "data" :"name_en",
			 "width": "10%",
			 "class" : "text-center"  
		 }            
		, {	
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

function querySoftwares(){
	querySchoolUser();
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
			   $("#name_en_m").val(kindData.name_en);
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
		
	}, {
		validate : function() {
			var self = $("#name_en_m").val();
			var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
			if (reg.test(self)) {
				$("#name_en_m").testRemind("软件英文名中不能含有中文!");
				return false;
			}
			return true;
		}
	});
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
	
	$("#name_en_m").on('change blur',function(e){
		var kindNameEn = this.value;
		var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
		var self = this;
　　    if (reg.test(kindNameEn))
          {
			if ($.testRemind.display == false && $.html5Validate.isRegex(self)) {
				$(self).testRemind("软件英文名中不能含有中文!"); 
				$(self).focus();
			} 
		  } 
		$.ajax({
			url:"softwareKind/queryKindNameEnIsRepeat",
			type:"post",
			data:{
				        kindNameEn:kindNameEn
						},
			dataType:"json",
			success:function(data) {
				console.log(data)
			    if(data.status=="success") {
			    	if(!data.flag){		
						if ($.testRemind.display == false && $.html5Validate.isRegex(self)) {
		 		            $(self).testRemind("该软件类别英文名已存在"); 
		 		            $(self).focus();
		 		        } 							
			    	}
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("查询软件种类英文名请求出错了","error"); 
			}
		});
		
	});
	
});