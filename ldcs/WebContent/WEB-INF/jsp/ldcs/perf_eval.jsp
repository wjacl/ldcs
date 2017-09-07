<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="appfn" uri="http://wja.com/jsp/app/functions"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/jsp/frame/comm_css_js.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/frame/header.jsp"%>
	<h3>
		经纪人绩效评定
	</h3>
	
	<div id="liveData_tj_tb" style="padding: 10px;height:80px">
		<div style="margin-bottom: 5px">
				<a href="javascript:edit()" class="easyui-linkbutton"
					iconCls="icon-edit" plain="true">编辑</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:save()"><s:message code='comm.save' /></a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:cancel()"><s:message code='comm.cancel' /></a>
		</div>
		<div>
			<form id="liveData_tj_query_form">
				<s:message code="liver.broker"/>:
				<input class="easyui-combotree" name="brokerId_in_String" style="width: 160px"
					data-options="
						url:'${ctx }/user/memberTree',				
	                    multiple:true,
						loadFilter:userOrgTreeLoadFilter,
						panelWidth:180">
				月份(YYYYMM)
				: <input class="easyui-numberbox" style="width: 80px" 
					data-options="min:201601,value:currMonth"
					name="month_eq_intt">
				<a
					href="javascript:treeReload('liveData_tj_query_form','liveData_tj_grid')"
					class="easyui-linkbutton" iconCls="icon-search">生成评定表</a>
			</form>
		</div>
	</div>

	<table id="liveData_tj_grid" class="easyui-treegrid"
		data-options="rownumbers:true,singleSelect:true,multiSort:true,treeField: 'liverName',onDblClickRow:onDblClickRow,
				sortName:'month,brokerId,liverId',sortOrder:'desc,asc,asc',url:'${ctx }/perfEval/evalList',
				idField:'id',method:'post',toolbar:'#liveData_tj_tb',loadFilter:gridDataLoadFilter">
		<thead>
			<tr>
				<th rowspan="2" data-options="field:'liverName',width:110">经纪人</th>
				<th rowspan="2" 
					data-options="field:'month',width:70,align:'center',sortable:'true'">月份</th>
				<th colspan="3">礼物收益完成情况(元)</th>
				<th colspan="3">直播时长完成情况(分钟)</th>
				<th rowspan="2"
					data-options="field:'comm',width:80,align:'right'">礼物提成</th>
				<th rowspan="2"
					data-options="field:'gradeProp',width:60,formatter:valuePercFormatter,align:'right',
						editor:{type:'numberbox',
						options:{
							precision:2}},
						styler:zhuozhongStyle">部门评分</th>
				<th rowspan="2"
					data-options="field:'perfEval',width:60,formatter:valuePercFormatter,align:'right'">权重得分</th>
				<th rowspan="2"
					data-options="field:'perfComm',width:80,align:'right',
						styler:zhuozhongStyle">绩效提成</th>
				<th rowspan="2"
					data-options="field:'remark',width:100,editor:'text'">评分说明</th>
				<th rowspan="2"
					data-options="field:'perfEvalText',width:'auto'">权重计算明细</th>
				<th rowspan="2"
					data-options="field:'commMess',width:'auto'">礼物提成明细</th>
			</tr>
			<tr>
				<th
					data-options="field:'giftEarning',width:70,align:'right'">收益</th>	
				<th
					data-options="field:'giftEarningGoal',width:70,align:'right'">目标</th>
				<th
					data-options="field:'gflvText',width:80,align:'right'">完成率</th>
				<th
					data-options="field:'liveDuration',width:70,align:'right'">时长</th>
				<th
					data-options="field:'liveDurationGoal',width:70,align:'right'">目标</th>
				<th
					data-options="field:'dulvText',width:80,align:'right'">完成率</th>
			</tr>
		</thead>
	</table>
	<form id="exportForm" action="${ctx }/liveData_tj/export" method="post">
		<input type="hidden" name="liverId_in_String"/>
		<input type="hidden" name="month_gte_intt"/>
		<input type="hidden" name="month_lte_intt"/>
		<input type="hidden" name="sort" value="month,brokerId,liverId"/>
		<input type="hidden" name="order" value="DESC,ASC,ASC"/>
	</form>
	<script type="text/javascript">	
	function treeReload(formId,gridId){
		var jsonData = $("#" + formId).serializeJson();
		//为解决数组多值，mvc中map接收只能接收到一个的问题，而将数组转为以","间隔的字符串来传递。
		for(var i in jsonData){
			if(jsonData[i] instanceof Array){
				jsonData[i] = jsonData[i].join(",");
			}
		}
		$('#' + gridId).treegrid('load',jsonData);
	}
	
	var editingId;
	function edit(){
		if (editingId != undefined){
			$('#liveData_tj_grid').treegrid('select', editingId);
			$('#liveData_tj_grid').treegrid('beginEdit', editingId);
			return;
		}
		var row = $('#liveData_tj_grid').treegrid('getSelected');
		if (row){
			if(row._parentId != null){
				$.sm.alert("主播数据行不可编辑！");
				return;
			}
			editingId = row.id
			$('#liveData_tj_grid').treegrid('beginEdit', editingId);
		}
	}
	
	function onDblClickRow(row){
		if(row._parentId != null){
			return;
		}
		if(editingId != undefined){
			if(editingId != row.id){
				save();
				editingId = row.id;
				edit();
			}
		}
		else{
			edit();
		}
	} 
	
	function save(){
		if (editingId != undefined){
			var t = $('#liveData_tj_grid');
			t.treegrid('endEdit', editingId);
			$('#liveData_tj_grid').treegrid('select', editingId);
			var row = $('#liveData_tj_grid').treegrid('getSelected');
			row.perfEval = row.gflv * msta.giftProp / 100 + row.dulv * msta.durationProp / 100 + parseFloat(row.gradeProp);
			row.perfEvalText = "(" + row.gflv + "% * " + msta.giftProp + "%) + (" + row.dulv + "% * " + msta.durationProp + "%) + " + row.gradeProp + "% = " + row.perfEval + "%";
			row.perfComm = Math.round(row.comm * row.perfEval) / 100,
			t.treegrid('refresh', editingId);
			$.ajax({
				  url: ctx + "/perfEval/add",
				  method:"post",
				  async: false,
				  dataType:"json",
				  data:row,
				  success:function(data){
						$.sm.handleResult(data);
					}
				 });
			editingId = undefined;
		}
	}
	function cancel(){
		if (editingId != undefined){
			$('#liveData_tj_grid').treegrid('cancelEdit', editingId);
			editingId = undefined;
		}
	}
	
	
	//单元格样式着重处理
	function zhuozhongStyle(value,row,index){
		return 'background-color:#ffee00;color:red;font-weight:700;';
	}
	
		var standards = [];
		var msta = null;
		function computeGiftComm(month,gift,goal){
			var res = {mess:"",comm:0};
			if(goal == undefined){
				res.mess = "没有定目标！";
				return res;
			}
			
			if(gift < goal){
				res.mess = "未完成目标!";
			}
			else{
				msta = standards[month];
				if(msta == undefined){
					 $.ajax({
						  url: ctx + "/perfStandard/getByMonth",
						  async: false,
						  dataType:"json",
						  data:{month:month},
						  success:function(data){
								standards[month] = data;
								msta = data;
							}
						 });
				}
				
				var count = 0;
				var levelInterval = goal * msta.commLevelInterval / 100;
				var compValue = goal;
				var commProp = msta.commLevelBaseProp;
				while(gift > 0){
					if(count == 0){
						res.comm += goal * commProp / 100;
						res.mess = "(" + goal + " * " + commProp + "%) ";
					}
					else {
						if(gift > levelInterval){
							compValue = levelInterval;
						}
						else {
							compValue = gift;
						}
						commProp += msta.commLevelPropInterval;
						res.comm += compValue * commProp / 100; 
						res.mess += " + (" + compValue + " * " + commProp + "%) ";
					}

					gift -= compValue;
					count++;
				}
				res.mess += " = " + res.comm;
			}
			return res;
		}
		
		function gridDataLoadFilter(data){
			
			var lastPid = null;
			var lastRow = null;
			var count = 0;
			var giftOkCount = 0;
			var duraOKCount = 0;
			var giftComm = 0;
			var brokers = [];
			for(var i in data.rows){
				var row = data.rows[i];
				row.gflv = 0;
				if(row.giftEarning && row.giftEarningGoal){
					row.gflv = Math.round(row.giftEarning / row.giftEarningGoal * 10000) / 100;
					row.gflvText = row.gflv + "%";
				}
				row.dulv = 0;
				if(row.liveDuration && row.liveDurationGoal){
					row.dulv = Math.round(row.liveDuration / row.liveDurationGoal * 10000) / 100;
					row.dulvText = row.dulv + "%";
				}
				//礼物提成
				var commRes = computeGiftComm(row.month,row.giftEarning,row.giftEarningGoal);
				row.comm = commRes.comm;
				row.commMess = commRes.mess;
				row.id = i + "";
				row._parentId = row.brokerId + "-" + row.month;
				
				if(lastPid != row._parentId){	
					if(lastPid != null){
						var xgflv = Math.round(giftOkCount / count * 10000) / 100;
						var xdulv = Math.round(duraOKCount / count * 10000) / 100;
						var xperfEval = xgflv * msta.giftProp / 100 + xdulv * msta.durationProp / 100 + msta.gradeProp;
						brokers.push({
							id:lastPid,
							_parentId:null,
							month:lastRow.month,
							brokerId:lastRow.brokerId,
							brokerName:lastRow.brokerName,
							liverName:lastRow.brokerName,
							gflv:xgflv,
							gflvText: giftOkCount + " / " + count + " = " + xgflv + "%",
							dulv:xdulv,
							dulvText: duraOKCount + " / " + count + " = " + xdulv + "%",
							comm:giftComm,
							commMess:giftComm,
							gradeProp:0,
							perfEval:xperfEval,
							perfEvalText : "(" + xgflv + "% * " + msta.giftProp + "%) + (" + xdulv + "% * " + msta.durationProp + "%) + " + msta.gradeProp + "% = " + xperfEval + "%",
							perfComm : Math.round(giftComm * xperfEval) / 100,
							remark:"",
							state:"closed"
						});
					}
					
					lastPid = row._parentId;
					giftOkCount = 0;
					duraOKCount = 0;
					count = 0;
					giftComm = 0;
				}
				
				count++;
				giftComm += commRes.comm;
				if(row.gflv >= 100){
					giftOkCount++;
				}
				if(row.dulv >= 100){
					duraOKCount++;
				}
				lastRow = row;
			}
			
			if(count > 0){
				var xgflv = Math.round(giftOkCount / count * 10000) / 100;
				var xdulv = Math.round(duraOKCount / count * 10000) / 100;
				var xperfEval = xgflv * msta.giftProp / 100 + xdulv * msta.durationProp / 100 + msta.gradeProp;
				brokers.push({
					id:lastPid,
					_parentId:null,
					month:lastRow.month,
					brokerId:lastRow.brokerId,
					brokerName:lastRow.brokerName,
					liverName:lastRow.brokerName,
					gflv:xgflv,
					gflvText: giftOkCount + " / " + count + " = " + xgflv + "%",
					dulv:xdulv,
					dulvText: duraOKCount + " / " + count + " = " + xdulv + "%",
					comm:giftComm,
					commMess:giftComm,
					gradeProp:0,
					perfEval:xperfEval,
					perfEvalText : "(" + xgflv + "% * " + msta.giftProp + "%) + (" + xdulv + "% * " + msta.durationProp + "%) + " + msta.gradeProp + "%",
					perfComm : Math.round(giftComm * xperfEval) / 100,
					remark:"",
					state:"closed"
				});
			}
			var rows = brokers.concat(data.rows);
			return {total:rows.length,rows:rows};
		}
	
		function userOrgTreeLoadFilter(data){
			//去掉非经纪人
			for(var i = data.length - 1; i >=0; i--){
				if(data[i].userType != undefined && data[i].userType != "B"){
					data.splice(i,1);
				}
			} 
			return $.ad.standardIdPidNameArrayToEasyUITree(data);
		}
		
		function doExport(gridId){
			var queryParams = $("#" + gridId).datagrid("options").queryParams;
			$("#exportForm").form("load",queryParams);
			$("#exportForm").form({url:ctx + "/liveData/tjexport"});
			$('#exportForm').form('submit');
		}
		
		function valuePercFormatter(value,row,index){
			if(value){
				return value + "%";
			}
			return "";
		}
		
		function liverFormatter(value,row,index){
			return row.liverName? row.liverName : '';
		}
	
		function brokerFormatter(value,row,index){
			return row.brokerName ? row.brokerName : '';
		}

		function liverOrgTreeLoadFilter(data){
			for(var i in data){
				if(data[i].type == "liver" && data[i].origin.broker){
					if(data[i].origin.broker.$ref)
						data[i].origin.broker = eval(data[i].origin.broker.$ref.replace('$',"data"));
				}
			}
			
			var nodes = $.ad.standardIdPidNameArrayToEasyUITree(data);
			removeHasNoLiverNodes(nodes);
			return nodes;
		}
		
		function removeHasNoLiverNodes(data){
			for(var i = data.length - 1; i >= 0; i--){
				if(data[i].type != "liver" && data[i].children != undefined){
					removeHasNoLiverNodes(data[i].children);
				}
				
				if(data[i].type != "liver" && (data[i].children == undefined || data[i].children.length == 0)){
					data.splice(i,1);
				}
			}
		}
		
		var today = '${appfn:today()}';
		var ss = today.split("-");
		var currMonth = ss[0] + ss[1];		

	</script>
	<%@ include file="/WEB-INF/jsp/frame/footer.jsp"%>
</body>
</html>