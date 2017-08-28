var liveDataIn = {
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
				if(checkNodes[i].type == "liver"){
					livers.push(checkNodes[i]);
					liverIds.push(checkNodes[i].id);			
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
							row = {brokerId:livers[i].broker.id,
								brokerName:livers[i].broker.name,
								date:vdate,
								giftEarning:0,
								liveDuration:0,
								liveName:livers[i].liveName,
								liverId:livers[i].id,
								liverName:livers[i].name,
								platform:livers[i].platform,
								popularity:0,
								roomNo:livers[i].roomNo,
								rss:0,
								rssGrowRate:0};
						}
						
						data.push(row);
					}
					
					$("#liveData_grid").loadData(data);
				});
		
	}
};