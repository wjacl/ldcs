
/**
 * 组织机构用户树的常用处理方法
 */
var org_user_tree = {
	
	/**
	 * 将组织的列表数据转为easyui-tree数据结构
	 * @param data
	 */
	orgDataConvertToEasyTreeData : function (rows){
		function exists(rows, pid){
			for(var i=0; i<rows.length; i++){
				if (rows[i].id == pid) return true;
			}
			return false;
		}
		
		var nodes = [];
		// get the top level nodes
		for(var i=0; i<rows.length; i++){
			var row = rows[i];
			row.text = row.name;
			if (!exists(rows, row.pid)){
				nodes.push(row);
			}
		}
		
		var toDo = [];
		for(var i=0; i<nodes.length; i++){
			toDo.push(nodes[i]);
		}
		while(toDo.length){
			var node = toDo.shift();	// the parent node
			// get the children nodes
			for(var i=0; i<rows.length; i++){
				var row = rows[i];
				if (row.pid == node.id){
					if (node.children){
						node.children.push(row);
					} else {
						node.children = [row];
					}
					toDo.push(row);
				}
			}
		}
		return nodes;
	}
}