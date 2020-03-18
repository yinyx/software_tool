var oldUrl = locache.get("oldUrl");
$(document).ready(function(){
	
	jQuery.ajaxSetup({
		beforeSend:function(xhr,settings){
			var _this = this;
			var _url = _this.url;
/*			if(_url.indexOf("ACCESSTOKEN") != -1) {
				return ;
			}
			if(_url.indexOf("?") != -1){
				_this.url = _url + "&ACCESSTOKEN=" + accessTokenId;
			}else{
				_this.url = _url + "?ACCESSTOKEN=" + accessTokenId;
			}*/
			var accessTokenId = $.cookie('ACCESSTOKEN');
			if (!settings.crossDomain && settings.type != null && settings.type.toLowerCase() == "post") { 
				xhr.setRequestHeader("ACCESSTOKEN", accessTokenId);
			} else {
				_this.url = changeURLParam(_url,"ACCESSTOKEN",accessTokenId);
			}
		},
		complete :function(xhr,textStatus,settings){
			try{
				var loginStatus = xhr.getResponseHeader("loginStatus");
				var tokenStatus = xhr.getResponseHeader("tokenStatus");
				if(loginStatus == 'accessDenied'){
					 showSuccessOrErrorModal("登录超时，请重新登录","warning");
					 setTimeout(function(){
						 //top.location.href=base+"/login";
						 location.reload(true);
					 },2000);
				} else if (tokenStatus == 'accessDenied') {
					var accessTokenId = $.cookie('ACCESSTOKEN');
					if (accessTokenId != null) {
						$.extend(settings, {
							global: false,
							headers: {ACCESSTOKEN: accessTokenId}
						});
						$.ajax(settings);
					}
				}
			}catch(e){
				 showSuccessOrErrorModal(e,"error");
				 
			}
		}
	});
	
	//针对form表单提交的进行自动添加token $this.attr("method") != null && $this.attr("method").toLowerCase() == "post"
	$("form").submit(function() {
		var $this = $(this);
		if ($this.find("input[name='ACCESSTOKEN']").size() == 0) {
			var accessTokenId = $.cookie('ACCESSTOKEN');
			if (accessTokenId != null) {
				$this.append('<input type="hidden" name="ACCESSTOKEN" value="' + accessTokenId + '" \/>');
			}
		}
	});
	
	
	
	//清除缓存
	//locache.flush();
	//刷新操作
	if((oldUrl != null && location.href.indexOf("#") != -1) && oldUrl == location.href.split("#")[1]){
		popstateEvent();
	}else{
		loadMenu();
		onLoadTopMenu();
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
				"sPrevious": "上一页 ",
				"sNext": " 下一页",
				"sFirst":"首页",
				"sLast":"尾页",
				"sJump":"确定"
			}
		},
        "dom": '<"top"l<"clear">>rt<"bottom"ip<"clear">>',
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
	//判断数组是否包含指定元素
    Array.prototype.contains = function ( needle ){
	  for (i in this) {
	    if (this[i] == needle) return true;
	  }
	  return false;
    }
    if(isIE() && IEVersion() <= 9){
		window.attachEvent("popstate",function(){
			popstateEvent();
		});
	}else {
	    //回退监听处理
		window.addEventListener("popstate",function(){
			popstateEvent();
		});
	}
});
//插件 刷新selectpicker
(function($) {
	$.fn.extend({
		refreshSelect: function() {
			 var $this = $(this);
			 var thisDom = $this.prop("outerHTML");
	         var $parDom = $this.parent("div");
	         /*console.log($parDom)
	         console.log($parDom.children().length)
	         console.log($parDom.children("select").length)
	         console.log($parDom.children(".bootstrap-select").length)*/
	       //首先判断this是不是bootstrap-select对象
	         if($parDom.children().length=="2"&&$parDom.children("select").length=="1"&&$parDom.children(".bootstrap-select").length=="1"){
	        	 $parDom.empty();
		         $parDom.html(thisDom);
		         $parDom.children("select").selectpicker({});
	         }else{
	        	 $parDom.html("--refreshSelect()方法使用错误--");
	         }
		}
	});
})(jQuery);

//加载菜单
function loadMenu(){
	//缓存中没有数据
	if(!(!!locache.get("menuData"))) {
		//服务器获取
		getAjaxMenuData();
		var menuData = locache.get("menuData");
		buildMenuDiv(menuData);
	} else {
		var menuData = locache.get("menuData");
		buildMenuDiv(menuData);
	}
}

//服务器获取菜单列表
function getAjaxMenuData(){
//{"id":"001","name":"首页","menuType":0,"comIds":"001","children":[]},
	var menuData = [
	                {"id":"100","name":"系统管理","menuType":0,"comIds":"100","children":[{"id":"110","name":"机构管理","menuType":1,"comIds":"100,110","pid":"100","children":[{"id":"112","name":"学校信息","resourcePath":"/manage/org/toSchoolInfo.do","comIds":"100,110,112","pid":"110","menuType":1},
	                                                                                                       {"id":"111","name":"部门管理","resourcePath":"/manage/org/toDepartmentManage.do","comIds":"100,110,111","pid":"110","menuType":1},
		                                                                                                   {"id":"113","name":"院系管理","resourcePath":"/manage/org/toFacultyManage.do","comIds":"100,110,113","pid":"110","menuType":1},
		                                                                                                   {"id":"114","name":"教学点管理","resourcePath":"/manage/point/toPointManage.do","comIds":"100,110,114","pid":"110","menuType":1},
		                                                                                                   {"id":"115","name":"教学点信息","resourcePath":"/manage/point/toPointInfo.do","comIds":"100,110,115","pid":"110","menuType":1},
		                                                                                                   {"id":"116","name":"定时任务信息","resourcePath":"/sys/schedule/toScheduleJobContent.do","comIds":"100,110,116","pid":"110","menuType":1}
		                                                                                                   ]},
		                                                        {"id":"120","name":"用户权限","menuType":1,"comIds":"100,120","pid":"100","children":[{"id":"121","name":"菜单管理","resourcePath":"/manage/resource/toResourcePage.do","comIds":"100,120,121","pid":"120","menuType":1},
		                                                                                                   {"id":"122","name":"角色管理","resourcePath":"/manage/roles/toRolesManage.do","comIds":"100,120,122","pid":"120","menuType":1},
		                                                                                                   {"id":"123","name":"学校用户管理","resourcePath":"/manage/schoolUser/toUserManage.do","comIds":"100,120,123","pid":"120","menuType":1},
		                                                                                                   {"id":"124","name":"教学点用户管理","resourcePath":"/manage/teachPoint/toTeachUserManage.do","comIds":"100,120,124","pid":"120","menuType":1},
		                                                                                                   {"id":"125","name":"账号管理","resourcePath":"/manage/account/toAccountManageInfo.do","comIds":"100,120,125","pid":"120","menuType":1}
		                                                                                                   ]}
		                                                       ]},
	                {"id":"200","name":"学科管理","menuType":0,"comIds":"200","children":[{"id":"210","name":"基础设置","menuType":1,"comIds":"200,210","pid":"200","children":[{"id":"211","name":"教育类型","resourcePath":"/manage/dictionary/toEducationType.do","comIds":"200,210,211","pid":"210","menuType":1},
		                                                                                                   {"id":"212","name":"招生对象","resourcePath":"/manage/dictionary/toEnrollmentTarget.do","comIds":"200,210,212","pid":"210","menuType":1},
		                                                                                                   {"id":"213","name":"学历","resourcePath":"/manage/dictionary/toEducationalBackground.do","comIds":"200,210,213","pid":"210","menuType":1},
		                                                                                                   {"id":"214","name":"层次","resourcePath":"/manage/level/toLevel.do","comIds":"200,210,214","pid":"210","menuType":1},
		                                                                                                   {"id":"214-0","name":"专业","resourcePath":"/manage/major/toMajor.do","comIds":"200,210,214-0","pid":"210","menuType":1},
		                                                                                                   {"id":"215","name":"学生类型","resourcePath":"/manage/dictionary/toStudentType.do","comIds":"200,210,215","pid":"210","menuType":1},
		                                                                                                   {"id":"216-0","name":"学生来源","resourcePath":"/manage/dictionary/toStudentSource.do","comIds":"200,210,216-0","pid":"210","menuType":1},
		                                                                                                   {"id":"216-2","name":"学制","resourcePath":"/manage/dictionary/toEducationalSystem.do","comIds":"200,210,216-2","pid":"210","menuType":1},
		                                                                                                   {"id":"216-1","name":"学科分类","resourcePath":"/manage/dictionary/toSubjectType.do","comIds":"200,210,216-1","pid":"210","menuType":1},
		                                                                                                   {"id":"217","name":"课程类别","resourcePath":"/manage/dictionary/toCourseType.do","comIds":"200,210,217","pid":"210","menuType":1},
		                                                                                                   {"id":"218","name":"课程模块","resourcePath":"/manage/courseModule/toCourseModule.do","comIds":"200,210,218","pid":"210","menuType":1},
		                                                                                                   {"id":"219-0","name":"模块方向","resourcePath":"/manage/moduleDir/toModuleDir.do","comIds":"200,210,219-0","pid":"210","menuType":1},
		                                                                                                   {"id":"219-1","name":"课程性质","resourcePath":"/manage/dictionary/toCourseNature.do","comIds":"200,210,219-1","pid":"210","menuType":1}
		                                                                                                   ]},
                                                              {"id":"220","name":"课程管理","menuType":1,"comIds":"200,220","pid":"200","children":[{"id":"221","name":"课程设置","resourcePath":"/manage/course/toCourseManage.do","comIds":"200,220,221","pid":"220","menuType":1},
                                      		                                                                                                  {"id":"222","name":"课程版本","resourcePath":"/manage/courseVersion/toCourseVersion.do","comIds":"200,220,222","pid":"220","menuType":1},
                                    		                                                                                                  {"id":"223","name":"考核指标","resourcePath":"/manage/examMode/toCourseExamMode.do","comIds":"200,220,223","pid":"220","menuType":1}        
                                                                                                         ]},
                                                              {"id":"230","name":"教材管理","menuType":1,"comIds":"200,230","pid":"200","children":[{"id":"231","name":"审核教材","resourcePath":"/manage/teachMaterial/toTeachMaterial.do","comIds":"200,230,231","pid":"230","menuType":1},
                                                                                   		                                                         {"id":"232","name":"教材管理","resourcePath":"/manage/textBook/toTextBook.do","comIds":"200,230,232","pid":"230","menuType":1},
                                                                                 		                                                         {"id":"233","name":"课程教材管理","resourcePath":"/manage/courseTextBook/toCourseTextBook.do","comIds":"200,230,233","pid":"230","menuType":1}
                                                                                                                                                      ]},
	                                                         {"id":"240","name":"培养方案","menuType":1,"comIds":"200,240","pid":"200","children":[{"id":"241","name":"培养方案管理","resourcePath":"/manage/trainingProgram/toTrainingProgramManage.do","comIds":"200,240,241","pid":"240","menuType":1}
	                                                                                                   ]}, 
                                                             {"id":"250","name":"学科统计查询","menuType":1,"comIds":"200,250","pid":"200","children":[{"id":"251","name":"查询课程","resourcePath":"/manage/org/toDepartmentManage.do","comIds":"200,250,251","pid":"250","menuType":1},
                                                                                                            {"id":"252","name":"查询专业","resourcePath":"#","comIds":"200,250,252","pid":"250","menuType":1},
                                                                                                            {"id":"253","name":"查询培养方案","resourcePath":"#","comIds":"200,250,253","pid":"250","menuType":1}
                                                                                                            ]},
                                                            ]},
                    {"id":"300","name":"招生管理","menuType":0,"comIds":"300","children":
                                                                [{"id":"310","name":"报名管理","menuType":1,"comIds":"300,310","pid":"300","children":
                                                                	[	{"id":"311","name":"学习批次","resourcePath":"/manage/learningBatch/toLearningBatch.do","comIds":"300,310,311","pid":"310","menuType":1},
                                                            	    	{"id":"312","name":"招生批次","resourcePath":"/manage/enrollBatch/toEnrollBatch.do","comIds":"300,310,312","pid":"310","menuType":1},
                                                            			{"id":"313","name":"录入报名表","resourcePath":"/manage/regManage/toRegManage.do","comIds":"300,310,313","pid":"310","menuType":1},
                                                            			{"id":"314","name":"初审报名表","resourcePath":"/manage/regVerify/toPreRegVerify.do","comIds":"300,310,314","pid":"310","menuType":1},
                                                            			{"id":"315","name":"复审报名表","resourcePath":"/manage/regVerify/toReRegVerify.do","comIds":"300,310,315","pid":"310","menuType":1},
                                                            			{"id":"316","name":"生成学号","resourcePath":"/manage/regManage/toGenerateStudentCode.do","comIds":"300,310,316","pid":"310","menuType":1},
                                                            			{"id":"317","name":"终审报名表","resourcePath":"/manage/regVerify/toFinalRegVerify.do","comIds":"300,310,317","pid":"310","menuType":1}
                                                            			]},
                                                                {"id":"320","name":"招生查询统计","menuType":1,"comIds":"300,320","pid":"300","children":
                                                                	[	{"id":"321","name":"查询报名信息","resourcePath":"/manage/regCount/toRegInspect.do","comIds":"300,320,321","pid":"320","menuType":1},
                                                                        {"id":"322","name":"招生统计","resourcePath":"/manage/regCount/toRegCount.do","comIds":"300,320,322","pid":"320","menuType":1}
                                                                        ]}
                                                               ]},
	                {"id":"400","name":"教务管理","menuType":0,"comIds":"400","children":[{"id":"420","name":"教师授课管理","menuType":1,"comIds":"400,420","pid":"400","children":[
	                                                                                           {"id":"421","name":"教师管理","resourcePath":"/manage/teacherManage/toTeacherManage.do","comIds":"400,420,421","pid":"420","menuType":1},
	                                                                                           {"id":"422","name":"课程教师团队","resourcePath":"/manage/courseTeacherTeam/toCourseTeacherTeam.do","comIds":"400,420,422","pid":"420","menuType":1},
	            																			   {"id":"423","name":"学务导师分配","resourcePath":"/manage/stuAcademicAdvisor/toStuAcademicAdvisor.do","comIds":"400,420,423","pid":"420","menuType":1},
                                                           									   {"id":"424","name":"班级管理","resourcePath":"/manage/class/toClassManage.do","comIds":"400,420,424","pid":"420","menuType":1}]
	                																	},{"id":"430","name":"选课管理","menuType":1,"comIds":"400,430","pid":"400","children":[
	                																	       {"id":"431","name":"选课批次","resourcePath":"/manage/courseBatch/toCourseBatch.do","comIds":"400,430,431","pid":"430","menuType":1},
	                															               {"id":"432","name":"退课控制","resourcePath":"/manage/dictionary/toCourseRefundControl.do","comIds":"400,430,432","pid":"430","menuType":1},
	                															               {"id":"433","name":"考试次数","resourcePath":"/manage/dictionary/toExamTimes.do","comIds":"400,430,433","pid":"430","menuType":1},
	                															               {"id":"434","name":"代学生选课","resourcePath":"/manage/chooseCourse/toChooseCourseForStudent.do","comIds":"400,430,434","pid":"430","menuType":1},
	                															               {"id":"435","name":"申请退课","resourcePath":"/manage/courseRefundApply/toCourseRefundApply.do","comIds":"400,430,435","pid":"430","menuType":1},
	                															               {"id":"436","name":"退课审核","resourcePath":"/manage/courseRefundVerify/toCourseRefundVerify.do","comIds":"400,430,436","pid":"430","menuType":1},]
	                																      },{"id":"430","name":"教务查询统计","menuType":1,"comIds":"400,430","pid":"400","children":[]
	                																	  }
	                																	]},
	                {"id":"500","name":"考务管理","menuType":0,"comIds":"500","children":[]},
	                {"id":"600","name":"学籍管理","menuType":0,"comIds":"600","children":[{"id":"620","name":"学籍信息","menuType":1,"comIds":"600,620","pid":"600","children":[
 	                                                                                           {"id":"621","name":"变更学籍信息","resourcePath":"/manage/changeRoll/toChangeRollInforList.do","comIds":"600,620,621","pid":"620","menuType":1},
                                                                                               {"id":"622","name":"管理学籍信息","resourcePath":"/manage/manageRoll/toManageRollInforList.do","comIds":"600,620,622","pid":"620","menuType":1},
                                                                                               {"id":"623","name":"导入电子照片","resourcePath":"/manage/electronicPhoto/toUploadElectronicPhoto.do","comIds":"600,620,623","pid":"620","menuType":1}]
	                																	},{"id":"630","name":"学籍异动","menuType":1,"comIds":"600,630","pid":"600","children":[
 	                                                                                           {"id":"631","name":"转专业控制","resourcePath":"/manage/modify/toTransferProfessionalControlInfo.do","comIds":"600,630,631","pid":"630","menuType":1},
                                                                                               {"id":"632","name":"初审学籍异动","resourcePath":"/manage/firstTrialRollChange/tofirstTrialRollChange.do","comIds":"600,630,632","pid":"630","menuType":1},
                                                                                               {"id":"633","name":"复审学籍异动","resourcePath":"/manage/firstTrialRollChange/toreviewRollChange.do","comIds":"600,630,633","pid":"630","menuType":1}]
	                																	},
                                                                                          {"id":"610","name":"学分转换","menuType":1,"comIds":"600,610","pid":"600","children":[
                                                                                           {"id":"611","name":"学分转换类型","resourcePath":"/manage/credit/toCreditTransfer.do","comIds":"600,610,611","pid":"610","menuType":1},
                                                                                           {"id":"612","name":"外部成果类型","resourcePath":"/manage/achievementType/toAchievementType.do","comIds":"600,610,612","pid":"610","menuType":1},
                                                                                           {"id":"613","name":"转换证书机构","resourcePath":"/manage/certificate/toCertificateOrganizeManage.do","comIds":"600,610,613","pid":"610","menuType":1},
                                                                                           {"id":"614","name":"外部成果名录","resourcePath":"/manage/Results/toListOfExternalResults.do","comIds":"600,610,614","pid":"610","menuType":1},
                                                                                           {"id":"615","name":"学分转换规则","resourcePath":"/manage/transferRule/toTransferRule.do","comIds":"600,610,615","pid":"610","menuType":1},
                                                                                           {"id":"616","name":"申请学分转换","resourcePath":"/manage/creditTransform/toApplyCreditTransform.do","comIds":"600,610,616","pid":"610","menuType":1},
                                                                                           {"id":"617","name":"审核学分转换","resourcePath":"/manage/Audit/toAuditCreditConversion.do","comIds":"600,610,617","pid":"610","menuType":1}]
                                                                                          },
                                                                                          {"id":"640","name":"毕业管理","menuType":1,"comIds":"600,640","pid":"600","children":[
                                                                                           {"id":"641","name":"毕业批次","resourcePath":"/manage/graduatedBatch/toGraduatedBatch.do","comIds":"600,640,641","pid":"640","menuType":1},
                                                                                           {"id":"642","name":"毕业预审名单","resourcePath":"/manage/graduatedPre/toGraduatedPre.do","comIds":"600,640,642","pid":"640","menuType":1},
                                                                                           {"id":"643","name":"申请毕业","resourcePath":"/manage/graduatedApply/toGraduatedApply.do","comIds":"600,640,643","pid":"640","menuType":1},
                                                                                           {"id":"644","name":"审核毕业","resourcePath":"/manage/graduatedCheck/toGraduatedCheck.do","comIds":"600,640,644","pid":"640","menuType":1},
                                                                                           {"id":"645","name":"生成电子注册号","resourcePath":"/manage/graduationManagement/displayPage.do","comIds":"600,640,645","pid":"640","menuType":1},
                                                                                           {"id":"646","name":"管理毕业生信息","resourcePath":"/manage/graduatedInfo/toGraduatedInfoManage.do","comIds":"600,640,646","pid":"640","menuType":1},
                                                                                           {"id":"647","name":"发放毕业证书","resourcePath":"/manage/issue/toIssueACertificateOfGraduation.do","comIds":"600,640,647","pid":"640","menuType":1},
                                                                                           {"id":"647","name":"领取毕业证书","resourcePath":"/manage/issue/toPickupIssueACertificateOfGraduation.do","comIds":"600,640,647","pid":"640","menuType":1},
                                                                                           {"id":"648","name":"毕业证明书","resourcePath":"/manage/certificate/toGraduationCertificate.do","comIds":"600,640,648","pid":"640","menuType":1}]
                                                                                          },
                                                                                          {"id":"650","name":"学位管理","menuType":1,"comIds":"600,650","pid":"600","children":[
	                                                                                          {"id":"651","name":"学位批次","resourcePath":"/manage/degree/toDegreeBatch.do","comIds":"600,650,651","pid":"650","menuType":1},
	                                                                                          {"id":"652","name":"申请学位","resourcePath":"/manage/applyDegree/toApplyDegree.do","comIds":"600,650,652","pid":"650","menuType":1},
	                                                                                          {"id":"653","name":"审核学位","resourcePath":"/manage/auditDegree/toAuditDegree.do","comIds":"600,650,653","pid":"650","menuType":1},
	                                                                                          {"id":"654","name":"生成学位证号","resourcePath":"/manage/generateDegree/toGenerateDegree.do","comIds":"600,650,654","pid":"650","menuType":1},
	                                                                                          {"id":"655","name":"生成学位证书","resourcePath":"#","comIds":"600,650,655","pid":"650","menuType":1},
	                                                                                          {"id":"656","name":"发放学位证书","resourcePath":"/manage/degree/toCertificateOfAcademicDegree.do","comIds":"600,650,656","pid":"650","menuType":1},
	                                                                                          {"id":"656","name":"领取学位证书","resourcePath":"/manage/degree/toPickupCertificateOfAcademicDegree.do","comIds":"600,650,656","pid":"650","menuType":1},
	                                                                                          {"id":"657","name":"学位证明书","resourcePath":"/manage/degreeCertificate/toDegreeCertificateInfoManage.do","comIds":"600,650,657","pid":"650","menuType":1}]
                                                                                          }
	                                                                                  ]},
	                {"id":"700","name":"财务管理","menuType":0,"comIds":"700","children":
																	                	[   {"id":"710","name":"收费标准管理","menuType":1,"comIds":"700,710","pid":"700","children":
																	                		[	{"id":"711","name":"缴费类型","resourcePath":"/manage/paytype/toPayType.do","comIds":"700,710,711","pid":"710","menuType":1},
																	                			{"id":"712","name":"设置收费标准","resourcePath":"/manage/payrule/toPayRule.do","comIds":"700,710,712","pid":"710","menuType":1}
																	                			]},
																	                	{"id":"720","name":"缴费管理","menuType":1,"comIds":"700,720","pid":"700","children":
																	                		[	{"id":"721","name":"批量缴费汇款","resourcePath":"/manage/remittance/toRemittance.do","comIds":"700,720,721","pid":"720","menuType":1},
																	                			{"id":"722","name":"审核汇款单","resourcePath":"/manage/remittance/toRemittanceVerify.do","comIds":"700,720,722","pid":"720","menuType":1}
																	                			]},
																	                	{"id":"730","name":"退费管理","menuType":1,"comIds":"700,730","pid":"700","children":
																	                		[	{"id":"731","name":"直接退费","resourcePath":"/manage/refund/toRefund.do","comIds":"700,730,731","pid":"730","menuType":1},
																	                			{"id":"732","name":"退费审核","resourcePath":"/manage/refund/toRefundVerify.do","comIds":"700,730,732","pid":"730","menuType":1}
																	                			]},
																	                	{"id":"740","name":"发票管理","menuType":1,"comIds":"700,740","pid":"700","children":
																	                		[	{"id":"741","name":"导出开票信息","resourcePath":"/manage/invoice/toExportInvoice.do","comIds":"700,740,741","pid":"740","menuType":1},
																	                			{"id":"742","name":"导入发票信息","resourcePath":"/manage/invoice/toImportInvoice.do","comIds":"700,740,742","pid":"740","menuType":1},
																	                			{"id":"743","name":"作废发票","resourcePath":"/manage/invoice/toInvalidInvoice.do","comIds":"700,740,743","pid":"740","menuType":1}
																	                			]},
																	                	{"id":"750","name":"财务查询统计","menuType":1,"comIds":"700,750","pid":"700","children":
																	                		[	{"id":"751","name":"查询收费标准","resourcePath":"/manage/remittance/toRemittance.do","comIds":"700,750,751","pid":"750","menuType":1},
																	                			{"id":"752","name":"查询汇款单","resourcePath":"/manage/remittance/toRemittance.do","comIds":"700,750,752","pid":"750","menuType":1}
																	                			]}
																
																	                	]},
																	                	 {"id":"800","name":"财务管理","menuType":0,"comIds":"800","children":
																			                	[   {"id":"710","name":"收费标准管理","menuType":1,"comIds":"700,710","pid":"700","children":
																			                		[	{"id":"711","name":"缴费类型","resourcePath":"/manage/paytype/toPayType.do","comIds":"700,710,711","pid":"710","menuType":1},
																			                			{"id":"712","name":"设置收费标准","resourcePath":"/manage/payrule/toPayRule.do","comIds":"700,710,712","pid":"710","menuType":1}
																			                			]},
																			                	{"id":"720","name":"缴费管理","menuType":1,"comIds":"700,720","pid":"700","children":
																			                		[	{"id":"721","name":"批量缴费汇款","resourcePath":"/manage/remittance/toRemittance.do","comIds":"700,720,721","pid":"720","menuType":1},
																			                			{"id":"722","name":"审核汇款单","resourcePath":"/manage/remittance/toRemittanceVerify.do","comIds":"700,720,722","pid":"720","menuType":1}
																			                			]},
																			                	{"id":"730","name":"退费管理","menuType":1,"comIds":"700,730","pid":"700","children":
																			                		[	{"id":"731","name":"直接退费","resourcePath":"/manage/refund/toRefund.do","comIds":"700,730,731","pid":"730","menuType":1},
																			                			{"id":"732","name":"退费审核","resourcePath":"/manage/refund/toRefundVerify.do","comIds":"700,730,732","pid":"730","menuType":1}
																			                			]},
																			                	{"id":"740","name":"发票管理","menuType":1,"comIds":"700,740","pid":"700","children":
																			                		[	{"id":"741","name":"导出开票信息","resourcePath":"/manage/invoice/toExportInvoice.do","comIds":"700,740,741","pid":"740","menuType":1},
																			                			{"id":"742","name":"导入发票信息","resourcePath":"/manage/invoice/toImportInvoice.do","comIds":"700,740,742","pid":"740","menuType":1},
																			                			{"id":"743","name":"作废发票","resourcePath":"/manage/invoice/toInvalidInvoice.do","comIds":"700,740,743","pid":"740","menuType":1}
																			                			]},
																			                	{"id":"750","name":"财务查询统计","menuType":1,"comIds":"700,750","pid":"700","children":
																			                		[	{"id":"751","name":"查询收费标准","resourcePath":"/manage/remittance/toRemittance.do","comIds":"700,750,751","pid":"750","menuType":1},
																			                			{"id":"752","name":"查询汇款单","resourcePath":"/manage/remittance/toRemittance.do","comIds":"700,750,752","pid":"750","menuType":1}
																			                			]}
																		
																			                	]}
	              ];
	locache.set("menuData",menuData);
	//服务器获取菜单列表
	/*$.ajax({
		type : "post",
		url : base+"/manage/resource/getMenuData.do",
		dataType : "json",
        cache : false,
        async : false,
        scriptCharset : "utf-8",
		success : function(data){
			console.log(data)
			if(data.status == "success"){
				locache.set("menuData",data.menuData);
			}
			
		},
		error:function(data){
		  showSuccessOrErrorModal(data.msg,"error");
		}
	});*/
	
}

//根据菜单数组生成菜单，然后存储到缓存中
function buildMenuDiv(data) {
	if(data != null && typeof(data) != "undefined") {
		var topMenuHtml = "<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"100\" onclick=\"onLoadLeftMenu('100')\"><a href=\"javascript:;\">系统管理</a></li>"+
							"<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"200\" onclick=\"onLoadLeftMenu('200')\"><a href=\"javascript:;\">信息管理</a></li>"+
							"<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"300\" onclick=\"onLoadLeftMenu('300')\"><a href=\"javascript:;\">选课管理</a></li>";
		
		var leftMenuHtml100 = 
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/basicInfos/toUserManage.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">基本信息</span><span class=\"selected\"></span></a>"+
			"</li>"+
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/schoolUser/toUserManage.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">用户管理</span><span class=\"selected\"></span></a>"+
			"</li>";
		var leftMenuHtml200 = 
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/info/toArticleEdit.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">文章编辑</span><span class=\"selected\"></span></a>"+
			"</li>"+
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/info/toArticleManage.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">文章管理</span><span class=\"selected\"></span></a>"+
			"</li>"+
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/info/toLinkManage.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">链接管理</span><span class=\"selected\"></span></a>"+
			"</li>"+
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/info/toContentManage.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">留言管理</span><span class=\"selected\"></span></a>"+
			"</li>"+
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/info/toImageUpload.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">媒体库管理</span><span class=\"selected\"></span></a>"+
			"</li>";
		var leftMenuHtml300 = 
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/course/toCourseBatch.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">选课控制</span><span class=\"selected\"></span></a>"+
			"</li>"+
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/course/toStudent.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">学生管理</span><span class=\"selected\"></span></a>"+
			"</li>"+
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/coursem/toCourse.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">课程管理</span><span class=\"selected\"></span></a>"+
			"</li>"+
			"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"/manage/course/toClass.do\" onclick=\"onLoadContent(this)\"> <i></i> <span class=\"title\">班级管理</span><span class=\"selected\"></span></a>"+
			"</li>";
		locache.set("100",leftMenuHtml100);
		locache.set("200",leftMenuHtml200);
		locache.set("300",leftMenuHtml300);
		locache.set("topMenuHtml",topMenuHtml);
	}
}

//显示导入模态框
function showImportModal(){
	$("#importModal").modal('show');
}

//下载导入模板
function toDownloadTemplate(fileName){
	var url = encodeURI( "downloadSpecifiedImportTemplate.do?fileName="+fileName+"&ACCESSTOKEN="+accessTokenId);
	$("#downloadTemplateForm").attr('action',url);
	$("#downloadTemplateForm").attr('target','');
	$("#downloadTemplateForm").submit();
}

//导入信息
function toUploadFile(url){
	//采用formdata进行ajax请求上传文件 ie10以上
	var fileName = $("#importFile").val();
	if(fileName == null || fileName == ""){
		showInfoModal("请选择导入文件");
		return;
	}
	var subfix = fileName.lastIndexOf(".");
	if(fileName.substr(subfix) != ".xls" && fileName.substr(subfix) != ".xlsx"){
		showInfoModal("请选择excel文件");
		return;
	}
	/*
	var formData = new FormData($("#importForm")[0]);
	$.ajax({
		url:base + "/manage/invoice/importInvoiceListExcel",
		type:"post",
		data: formData,
		dataType : "json",
		contentType:false,
		processData:false,
		cache: false,
		success : function(data){
			if(data.status == "success"){
				showSuccessOrErrorModal(data.msg,"success");
				invoiceTable.draw();
				$("#importModal").modal("hide");
			}else{
				showSuccessOrErrorModal(data.msg,"error");
			}
		},
		error : function(data){
			showSuccessOrErrorModal(data.msg,"error");
		}
	});*/
	//ajaxSubmit方式提交请求
	$("#importForm").ajaxSubmit({
		url: url,
		type:"post",
		dataType : "json",
		cache: false,
		async:false,
		success : function(data){
			if(data.status == "success"){
				showSuccessOrErrorModal(data.msg,"success");
				$("#importModal").modal("hide");
			}else{
				showSuccessOrErrorModal(data.msg,"error");
			}
		},
		error : function(data){
			showSuccessOrErrorModal(data.msg,"error");
		}
	});
	
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

//装载content部分内容
function onLoadContent(object){
	if(object){
		var ajaxUrl = object.getAttribute('data-url');
	    var comIds = object.getAttribute('data-comids');
		 var $this = $(object);
		 $("#leftSidebar li").removeClass("active open");
		 $this.parent().addClass("active");
		 $this.parent().parent().parent().addClass("active open");
		 ajaxGetContent(ajaxUrl,comIds,false);
	}
    
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

//模态框m-top位置重算,达到垂直居中目的
function centerModals(){
  $('.modal').each(function(i){
    var $clone = $(this).clone().css('display', 'block').appendTo('body');
    var top = Math.round(($clone.height() - $clone.find('.modal-content').height()) / 2);
    top = top > 50 ? top : 0;
    $clone.remove();
    $(this).find('.modal-content').css("margin-top", top);
  });
}

//异步加载content部分内容
function ajaxGetContent(ajaxUrl,comIds,backStatus){
	locache.set("oldUrl", ajaxUrl);
	startPageLoading();
	$.ajax({
			type : "post",
			url : jQuery.getBasePath() + ajaxUrl,
			dataType : "html",
			success : function(data) {
			    if(backStatus != true){
			    	if(isIE() && IEVersion() <= 9){
			    		
			    	}else {
                        if (location.href.indexOf("#") != -1) {
                            history.pushState(comIds, null, location.href.split("#")[0] + "#" + ajaxUrl);
                        } else {
                            history.pushState(comIds, null, location.href + "#" + ajaxUrl);
                        }
                    }

			    }
				$("#loadDiv").html(data);
				handleSidebarAndContentHeight();
				$(".modal:not('.modal-scroll')").on('show.bs.modal', centerModals);
				stopPageLoading();
			},
			error : function(e) {
				stopPageLoading();
				$("#loadDiv").load("errorView/building", function(){
					handleSidebarAndContentHeight();
				});
				
			},
			statusCode: {
				404: function() { 
					stopPageLoading();
					$("#loadDiv").load("errorView/404", function(){
						handleSidebarAndContentHeight();
					});
				},
				403: function() { 
					stopPageLoading();
					$("#loadDiv").load("errorView/building", function(){
						handleSidebarAndContentHeight();
					}); 
				} 
			}
		});
}

//刷新 后退处理事件
function popstateEvent(){
	var currentState = history.state;
	if(currentState != null && typeof(currentState) != "undefined"){
		//顶部导航选中处理
		//onLoadTopMenu(currentState.split(",")[0]);
		var $topMenu = $(locache.get("topMenuHtml"));
		$("#topMenu").html($topMenu);
		$("#topMenu li").each(function(){
			$(this).siblings().removeClass("active");
			$(this).find("span.selected").remove();
			if(currentState.split(",")[0] == $(this).attr("data-id")){
				$(this).addClass("active");
				$(this).append(' <span class="selected"></span>');
				return false;
			}
		});
		
		//左边菜单加载并选中
		var $leftSidebar = $(locache.get(currentState.split(",")[0]));//以后KEY改为id中截取一个字段来使用
		$("#leftSidebar").html($leftSidebar);
		$("#leftSidebar li").each(function(){
			$(this).siblings().removeClass("active open");
			$(this).children("ul li.active").removeClass("active");
			if(currentState.split(",")[1] == $(this).children("a").attr("data-id")){
				$(this).addClass("active open");
				$("a[data-id='"+currentState.split(",")[2]+"']").parent().addClass("active");
				return false;
			}
		});
		
		if(location.href.indexOf("#") == -1){
	        window.location.reload();
	    }else{
	      var backUrl = location.href.split("#")[1];
	      ajaxGetContent(backUrl,currentState,true);
	    }
	}else{
		//window.location.reload();
		//防止当histroy.state为空，加载首页
		loadMenu();
		onLoadTopMenu();
	}
}

//退出
function logout() {
	showConfirmModal("是否确定退出？",function(){
		try{
			localStorage.clear();
		}catch(e){
			
		}
		var _url = base+ "/logout";
		top.location.href = changeURLParam(_url,"ACCESSTOKEN",accessTokenId);
		//$.cookie("ACCESSTOKEN",null);
	});
	
}