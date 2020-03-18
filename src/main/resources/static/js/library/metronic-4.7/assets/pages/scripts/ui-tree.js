//获取选择的节点以及其父节点
function getids(str)
{
	var  ids = [];
	var power = [];
	var obj=$('#' + str).jstree().get_checked(true); //获取所有选中的节点对象aria-selected
	for(var i = 0; i < obj.length; i++)
	{//遍历所有所选节点
		ids.push(obj[i].id);
		var  parents = obj[i].parents;
		for(var k = 0; k < parents.length; k++)
		{
			ids.push(parents[k]);
		}
	}
	//遍历当前数组 
	for(var i = 0; i < ids.length; i++)
	{ 
		//如果当前数组的第i已经保存进了临时数组，那么跳过， 
		//否则把当前项push到临时数组里面 
		if (power.indexOf(ids[i]) == -1 && ids[i] != "#" && ids[i] != -1) 
			power.push(ids[i]); 
	} 
	return power;
}