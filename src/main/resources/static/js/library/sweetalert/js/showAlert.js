//普通弹出框，content为弹出的信息
function showInfoModal(content) 
{
	swal({
		title : "",
		text : content,
		confirmButtonText : "确认",
		closeOnConfirm : true
	});
}

// 传入无参函数
// 判断型弹出框，content为弹出的信息，funt为点击确认后执行的具体方法
function showConfirmModal(content, funt) 
{
	swal({
		title : "",
		text : content,
		type : "warning",
		showCancelButton : true,
		cancelButtonText : "取消",
		confirmButtonText : "确认",
		closeOnConfirm : false
	}, function() {
		swal.close();
		funt();
	});
}

// 传入1个参数的函数
// 判断型弹出框，content为弹出的信息，funt为点击确认后执行的具体方法，data为funt方法中的参数
function showConfirmModal(content, funt, data) 
{
	swal({
		title : "",
		text : content,
		type : "warning",
		showCancelButton : true,
		cancelButtonText : "取消",
		confirmButtonText : "确认",
		closeOnConfirm : false
	}, function() {
		swal.close();
		funt(data);
	});
}

// 传入2个参数的函数
// 判断型弹出框，content为弹出的信息，funt为点击确认后执行的具体方法，data1，data2为funt方法中的参数
function showConfirmModal(content, funt, data1, data2) 
{
	swal({
		title : "",
		text : content,
		type : "warning",
		showCancelButton : true,
		cancelButtonText : "取消",
		confirmButtonText : "确认",
		closeOnConfirm : false
	}, function() {
		swal.close();
		funt(data1, data2);
	});
}

// 传入3个参数的函数
// 判断型弹出框，content为弹出的信息，funt为点击确认后执行的具体方法，data1，data2，data3为funt方法中的参数
function showConfirmModal(content, funt, data1, data2, data3) 
{
	swal({
		title : "",
		text : content,
		type : "warning",
		showCancelButton : true,
		cancelButtonText : "取消",
		confirmButtonText : "确认",
		closeOnConfirm : false
	}, function() {
		swal.close();
		funt(data1, data2, data3);
	});
}

// 成功或失败型弹出框，content为弹出的信息，textType为弹出框类型
function showSuccessOrErrorModal(content,textType) 
{
	swal({
		title : "",
		text : content,
		type : textType,
		confirmButtonText : "确认",
		closeOnConfirm : true
	});
}