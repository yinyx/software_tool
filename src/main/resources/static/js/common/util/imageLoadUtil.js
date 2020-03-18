/*
 * JavaScript  	ImageLoadutil 
 *
 * Author  		zhangChao
 * 
 * Date    		2017-3-24 10:35:35
 * 
 * Version 		1.0
 * 
 * Describe 	process Image load for optimization
 */
 
 
 
/**
 *  [图片加载优化处理工具类]
 *  
 *  @Author	: 	zhangChao
 *  
 *  @Date	:   2017-3-24 10:47:36
 *  
 *  @param	:   {Object}  bshImgUtil  [工具类对象]
 * 
 */
 (function(bshImgUtil){
 	
 	
 	/**
     * [获取字符串长度]
     *
     * @Author :   zhangChao
     *
     * @Date   :   2016-11-25 09:22:29
     *
     * @param  :   {String}      [图片的 Src 地址]
     * 
     * @param  :   {String}		 [图片标签的ID]
     * 
     * @param  ：  {String}		 [图片加载异常显示的图片处理地址]
     *
     * @return :   {      }      [无返回值,处理成功之后即可正确处理图片，处理失败之后，可查看响应的日志信息]
     *
     */
 	bshImgUtil.loadProcess = function(imgUrl,imgId,errorImgUrl)
 							 {
	 							 	/** 1. 参数校验处理 */
	 							 	
	 							 	if(!imgUrl)
	 							 	{
	 							 		// 日志信息通知
	 							 		console.log("[bshImgUtil.loadProcess]- param : imgUrl is nullOrBlank!");
	 							 		
	 							 		return;
	 							 	}
	 							 	
	 							 	if(!imgId)
	 							 	{
	 							 		// 日志信息通知
	 							 		console.log("[bshImgUtil.loadProcess]- param : imgId is nullOrBlank!");
	 							 		
	 							 		return;
	 							 	}
	 							 	
	 							 	if(!errorImgUrl)
	 							 	{
	 							 		// 日志信息通知
	 							 		console.log("[bshImgUtil.loadProcess]- param : errorImgUrl is nullOrBlank!");
	 							 		
	 							 		return;
	 							 	}
	 							 	
	 							 	
	 							 	/** 2. 正则判断浏览器类型*/
	 							 	
	 							 	// 存储对象定义
	 							 	var Browser = new Object();
	 							 	
									Browser.userAgent=window.navigator.userAgent.toLowerCase();
									
									// IE
									Browser.ie=/msie/.test(Browser.userAgent);
									// firefox
									Browser.Moz=/gecko/.test(Browser.userAgent);
									
									
									/**3. 图片加载就绪判断处理 */ 
									
									// 图片地址
									var dataUrl = imgUrl;
									
									// 图片对象
								    var img = new Image();
								    
								    /**根据浏览器不同类型 判断图片就绪事件*/
								    // ie
								    if(Browser.ie) 
									{
								        img.onreadystatechange =function()
										{  
								            if(img.readyState=="complete"||img.readyState=="loaded")
											{
												bshImgUtil.loadReady(img,imgId);
								            }
								        };      
								    }
								    // firefox chrome
									else if(Browser.Moz)
									{
								        img.onload=function()
										{
								            if(img.complete==true)
											{
								                bshImgUtil.loadReady(img,imgId);
								            }
								        };       
								    }
								    
								    
								    //如果因为网络或图片的原因发生异常，则显示该图片
								    img.onerror=function()
								    			{
								    				console.log("[bshImgUtil.loadProcess]-error : image is not found or server is error");
								    				img.src=errorImgUrl;
								    			};
								    
								    /**4. 图片地址赋值处理*/
								    img.src=dataUrl;
 							 };
 							 
 							 
 	bshImgUtil.loadReady = function(imgObj,imgId)
 							 {
 							 		/** 1. 参数校验处理 */
	 							 	if(!imgObj)
	 							 	{
	 							 		// 日志信息通知
	 							 		console.log("[bshImgUtil.loadReady]- param : imgObj is nullOrBlank!");
	 							 		
	 							 		return;
	 							 	}
	 							 	
	 							 	if(!imgId)
	 							 	{
	 							 		// 日志信息通知
	 							 		console.log("[bshImgUtil.loadReady]- param : imgId is nullOrBlank!");
	 							 		
	 							 		return;
	 							 	}
	 							 	
	 							 	
	 							 	/**2. 替换图片地址*/
	 							 	
	 							 	//document.getElementById(imgId).style.cssText="";
	 							 	
									document.getElementById(imgId).src=imgObj.src;
 							 };
 	
 
 })(window.bshImgUtil={});
 