var liveDataIn = {
		
	doImport:function(){
		$("#liveData_import").form('submit',{success:function(data){
			data = eval('(' + data + ')');
			$.sm.show("导入完成！");
			$('#liveData_w').window('close');
			var faildData = [];
			for(var i = data.length - 1; i >=0; i--){
				if(data[i].id == undefined || data[i].id == null || data[i].id == ""){
					data[i].rowNum = i + 1;
					faildData.push(data[i]);
					data.splice(i,1);
				}
			}
			$("#liveData_grid").edatagrid("loadData",data);
		}});
	},
	
	exportTemplate: function(){
		var jsonData = $("#liveData_query_form").serializeJson();
		$.ad.joinArrayInObjectToString(jsonData);
		$("#exportTemplateForm").form("load",jsonData);
		$('#exportTemplateForm').form('submit');
	},

	loadData:function(){
		var vdate = $("#date").datebox('getValue');
		if(vdate == ""){
			$.sm.alert("请指定日期！");
			return;
		}
		var checkedNodes = $("#liverCombotree").combotree('tree').tree("getChecked");
		var livers = [];
		var liverIds = [];
		if(checkedNodes.length > 0){
			for(var i in checkedNodes){
				if(checkedNodes[i].type == "liver"){
					livers.push(checkedNodes[i]);
					liverIds.push(checkedNodes[i].id);			
				}
			}
		}
		else {
			livers = treeLivers;
		}
		var para = {date_eq_date:vdate};
		if(liverIds.length > 0){
			para.liverId_in_String = liverIds.join(",");
		}
		
		$.getJSON(ctx + "/liveData/list",
				para,
				function(json){
					var data = [];
					for(var i in livers){
						var row = null;
						for(var j in json){
							if(json[j].liverId == livers[i].id){
								row = json[j];
								json.splice(j,1);
							}
						}
						if(row == null){
							row = {brokerId:livers[i].origin.broker.id,
									brokerName:livers[i].origin.broker.name,
									date:vdate,
									liveName:livers[i].origin.liveName,
									liverId:livers[i].origin.id,
									liverName:livers[i].origin.name,
									platform:livers[i].origin.platform,
									roomNo:livers[i].origin.roomNo};
							/*row = {brokerId:livers[i].origin.broker.id,
								brokerName:livers[i].origin.broker.name,
								date:vdate,
								giftEarning:0,
								liveDuration:0,
								liveName:livers[i].origin.liveName,
								liverId:livers[i].origin.id,
								liverName:livers[i].origin.name,
								platform:livers[i].origin.platform,
								popularity:0,
								roomNo:livers[i].origin.roomNo,
								rss:0,
								rssGrowRate:0};*/
						}
						
						data.push(row);
					}
					
					$("#liveData_grid").edatagrid("loadData",data);
				});
		
	}
};