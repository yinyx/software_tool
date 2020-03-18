/**
 * jixingwang
 * 
 * 页面初始化时创建遮罩层
 * 
 * bodyId 为本页面的body id
 * 
 * txt 为自定义的提示文字
 */
function initMask(bodyId,txt)
{
	$("#" + bodyId).append('<div id="maskLayerWaitLoading" class="modal fade loadingModal" tabindex="-1" data-backdrop="static" data-keyboard="false" data-attention-animation="false">'
						  + '<div class="modal-body"><img src="' + jQuery.getBasePath() + '/js/library/metronic-4.7/assets/global/img/loading-spinner-grey.gif" alt="" class="loading">'
						  + '<span> &nbsp;&nbsp;' + txt + '</span></div></div>');
}

/**
 * jixingwang
 * 
 * 显示遮罩层
 */
function showMaskLayer()
{
	$("#maskLayerWaitLoading").modal("show");
}

/**
 * jixingwang
 * 
 * 隐藏遮罩层
 */
function hideMaskLayer()
{
	$("#maskLayerWaitLoading").modal("hide");
}