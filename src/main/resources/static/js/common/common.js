var userMap = {};
var timer;
function cancelModifyWavePassword()
{
	$("#password_edit").val("");
	$("#password_new").val("");
	$("#password_new_c").val("");
}

function closeModal(){
	$("#password_edit").val("");
	$("#password_new").val("");
	$("#password_new_c").val("");
}
$(document).ready(function(){
	//debugger;
	//判断是否登录
	userMap = isLogined();
	if(userMap){//成功登录
		$("#avatarNameId").html(userMap.user_name);
	}else{
		parent.location.href = jQuery.getBasePath() + "/login.html";
	}
	
	//配置DataTables默认参数
    $.extend(true, $.fn.dataTable.defaults, {
        "language": {
			"sProcessing": "正在加载数据...",
			"sLengthMenu": "_MENU_ 条记录",
			"sZeroRecords": "没有查到记录",
			"sInfo": "第  _START_ 条到第  _END_ 条记录，一共  _TOTAL_ 条记录",
			"sInfoEmpty": "0条记录",
			"emptyTable": "表中没有可用数据",
			"oPaginate": {
				"sPrevious": "<",
				"sNext": ">",
				"sFirst":"<<",
				"sLast":">>",
				"sJump":"确定"
			}
		},
        "dom": '<"top"l<"clear">>rt<"bottom"ip<"clear">>',
		"sScrollY":"350px",
		"scrollCollapse":true,
        "scrollX": true,
    	"serverSide":true,
		"bStateSave" : false,
		"ordering": false,
		"autoWidth":true,
		"lengthMenu" : [ [ 5, 15, 20],[ 5, 15, 20] ],
		"pageLength" : 15,
		/*"initComplete": function(settings, json) {
			
		 },*/
		 "drawCallback": function( settings ) {
			 stopPageLoading()
		 }
    });
    $.fn.dataTable.ext.errMode = function (s, h, m) {
        if (h == 1) {
            alert("连接服务器失败！");
        } else if (h == 7) {
            alert("返回数据错误！");
        }
    };
	loadMenu();
	onLoadTopMenu();
	$("#editUserPwd").html5Validate(function() {
		//保存密码
		saveNewPwd();
	}, {
		validate : function() {
			if (!verifyUserPwd($("#password_edit").val())) {
				$("#password_edit").testRemind("您输入的密码不正确!");
				return false;
			} else if ($("#password_new").val() != $("#password_new_c").val()) {
				$("#password_new_c").testRemind("您2次输入的密码不相同!");
				return false;
			}
			return true;
		}
	});
	
});

//加载菜单
function loadMenu(){
	var topMenuHtml = "<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"200\" onclick=\"onLoadLeftMenu('200')\"><a href=\"javascript:;\"><img alt=\"\" class=\"img-circle\" src=\"images/common/configs.png\"/>&nbsp;&nbsp;基本配置</a></li>";
	var leftMenuHtml200 = 
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"Info_TypeInfo\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"images/common/dir.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;类别信息</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"Info_SoftwareInfo\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"images/common/software.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;软件信息</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"Info_BranchInfo\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"images/common/sb.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;分支信息</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"Info_VersionInfo\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"images/common/version.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;版本信息</span><span class=\"selected\"></span></a>"+
		"</li>"+
        "<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"Info_PluginInfo\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" width=\"10%\" height=\"10%\" src=\"images/common/plugin.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;插件信息</span><span class=\"selected\"></span></a>"+
        "</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"Info_UserInfo\" onclick=\"onLoadContent(this)\"> <img alt=\"\" class=\"img-circle\" src=\"images/common/user.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;用户信息</span><span class=\"selected\"></span></a>"+
		"</li>";
	locache.set("200",leftMenuHtml200);
	locache.set("topMenuHtml",topMenuHtml);
}
//填充顶部导航
function onLoadTopMenu(id){
	var $topMenu = $(locache.get("topMenuHtml"));
	
	if(id != null && typeof(id) != "undefined"){
	    $topMenu.each(function(){
			if(id == $(this).attr("data-id")){
				$(this).addClass("active");
				$(this).append(' <span class="selected"></span>');
				return false;
			}
	    });	
	}else{
		//控制默认选中第一个，当前首页的url地址为空，会报错，因此默认加载系统管理
		$($topMenu[0]).addClass("active");
		$($topMenu[0]).append(' <span class="selected"></span>');
		id = $($topMenu[0]).attr("data-id");
	}
	$("#topMenu").html($topMenu);
	onLoadLeftMenu(id);
}

//根据系统菜单id，加载对应的左侧菜单
function onLoadLeftMenu(id){
	//顶部导航选中处理
	$("#topMenu li").each(function(){
		$(this).siblings().removeClass("active");
		$(this).find("span.selected").remove();
		
		if(id == $(this).attr("data-id")){
			$(this).addClass("active");
			$(this).append(' <span class="selected"></span>');
			return false;
		}
	});
	
	//填充左侧菜单栏
	var $leftSidebar = $(locache.get(id));//以后KEY改为id中截取一个字段来使用
	
	$($leftSidebar[0]).addClass("active open");
	$($leftSidebar[0]).find("ul li").first().addClass("active");
	$("#leftSidebar").html($leftSidebar);
	onLoadContent($($leftSidebar[0]).find("a")[0]);
}

//装载content部分内容
function onLoadContent(object){
	if(object){
		var ajaxUrl = object.getAttribute('data-url');
	    var comIds = object.getAttribute('data-comids');
		 var $this = $(object);
		 $("#leftSidebar li").removeClass("active open");
		 $this.parent().addClass("active");
		 $this.parent().parent().parent().addClass("active open");
		 ajaxGetContent(ajaxUrl);
		 // ajaxGetContent("views/info/articleEdit.html");
	}
  
}

//"views/"+
//异步加载content部分内容
function ajaxGetContent(url){
	startPageLoading();
	$("#loadDiv").load(url,function(){
		stopPageLoading();
		handleSidebarAndContentHeight();
	});
}

//对page-concent的高度重计算
var handleSidebarAndContentHeight = function () {
	var resBreakpointMd = App.getResponsiveBreakpoint('md');
	var content = $('.page-content');
	var sidebar = $('.page-sidebar');
	var height;
	var headerHeight = $('.page-header').outerHeight();
	var footerHeight = $('.page-footer').outerHeight();
	var sidebarHeight = sidebar.outerHeight();
	if (App.getViewPort().width < resBreakpointMd) {
	    height = App.getViewPort().height - headerHeight - footerHeight;
	} else {
	    height = sidebar.height() + 20;
	}
	if ((height + headerHeight + footerHeight) <= App.getViewPort().height) {
	    height = App.getViewPort().height - headerHeight - footerHeight;
	}
	if(sidebarHeight>height){
		height = sidebarHeight;
	}
	content.css('min-height', height);
};
//保存密码
function saveNewPwd() {
	var password_new = $("#password_new").val();
	var newpsd = $("#password_edit").val();
	var data = {
			"id":userMap.id,
			"newPassword" : hex_md5(password_new),
			"oldPassword":hex_md5(newpsd)
		};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url : "user/saveNewPassWord",
		type : "post",
		data : dataObj,
		dataType : "text",
		success : function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
			console.log(data)
			if (data.result == "success") {
				showSuccessOrErrorModal(data.reason, "success");
				userMap.password = password_new;
				$("#editUserPwd")[0].reset();
				$("#userPwdModal").modal("hide");
			} else {
				showSuccessOrErrorModal(data.reason, "error");
			}
		},
		error : function(e) {
			showSuccessOrErrorModal("保存密码请求出错了", "error");
		}
	})
}
//验证密码
function verifyUserPwd(pwd) {
	var oldPwd = userMap.password;
	if (hex_md5(pwd) == oldPwd) {
		return true;
	} else {
		return false;
	}
}