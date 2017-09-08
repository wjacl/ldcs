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
	
	<div id="liveData_tj_tb" style="padding: 5px;height:70px">
		<div style="margin-bottom: 5px">
			<app:author path="/perfEval/update">
				<a  href="#" onclick="javascript:edit();" class="easyui-linkbutton"
					iconCls="icon-edit" plain="true">编辑</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:save()"><s:message code='comm.save' /></a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:cancel()"><s:message code='comm.cancel' /></a>
			</app:author>
		</div>
		<div>
			<form id="liveData_tj_query_form">
				<s:message code="liver.broker"/>:
				<input class="easyui-combotree" id="brokerId_in_String" name="brokerId_in_String" style="width: 160px"
					data-options="
						url:'${ctx }/user/memberTree',				
	                    multiple:true,
						loadFilter:userOrgTreeLoadFilter,
						panelWidth:180">
				月份(YYYYMM)
				: <input class="easyui-numberbox" style="width: 80px" id="monthInput"
					data-options="min:201601,value:currMonth"
					name="month_gte_intt">
				- <input class="easyui-numberbox" style="width: 80px"
					data-options="min:201601,value:currMonth"
					name="month_lte_intt">				
			<app:author path="/perfEval/query">
				<a
					href="javascript:treeReload('liveData_tj_query_form','liveData_tj_grid')"
					class="easyui-linkbutton" iconCls="icon-search">查询</a> 
			</app:author>
			<app:author path="/perfEval/greateMonthEvaData">
				<a
					href="javascript:greateData('liveData_tj_query_form','liveData_tj_grid')"
					class="easyui-linkbutton" iconCls="icon-edit">生成评定数据</a>
			</app:author>
			<app:author path="/perfEval/export">
				<a
					href="javascript:doExport('liveData_tj_query_form')"
					class="easyui-linkbutton" iconCls="icon-edit">导 出</a>
			</app:author>
			</form>
		</div>
	</div>

	<table id="liveData_tj_grid" class="easyui-treegrid"
		data-options="rownumbers:true,pagination:true,singleSelect:true,multiSort:true,treeField: 'brokerName',onDblClickRow:onDblClickRow,
				sortName:'month,brokerName',sortOrder:'desc,asc',onBeforeExpand:onBeforeExpand,
				idField:'id',method:'post',toolbar:'#liveData_tj_tb',loadFilter:gridDataLoadFilter">
		<thead>
			<tr>
				<th rowspan="2" data-options="field:'brokerName',width:110">经纪人</th>
				<th rowspan="2" 
					data-options="field:'month',width:70,align:'center',sortable:'true'">月份</th>
				<th rowspan="2"
					data-options="field:'gradeProp',width:60,formatter:valuePercFormatter,align:'right',
						editor:{type:'numberbox',
						options:{
							precision:2}},
						styler:zhuozhongStyle">部门评分</th>
				<th rowspan="2"
					data-options="field:'remark',width:160,editor:'text'">评分说明</th>
				<th rowspan="2"
					data-options="field:'perfEvalText',width:'auto'">权重得分</th>
				<th rowspan="2"
					data-options="field:'comm',width:80,align:'right'">礼物提成</th>
				<th rowspan="2"
					data-options="field:'perfComm',width:80,align:'right',
						styler:zhuozhongStyle">绩效提成</th>
				<th colspan="3">礼物收益完成情况(元)</th>
				<th colspan="3">直播时长完成情况(分钟)</th>
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
	<form id="exportForm" action="" method="post">
		<input type="hidden" name="brokerId_in_String"/>
		<input type="hidden" name="month_gte_intt"/>
		<input type="hidden" name="month_lte_intt"/>
		<input type="hidden" name="sort" value="month,brokerName"/>
		<input type="hidden" name="order" value="DESC,ASC"/>
	</form>
	<script type="text/javascript">	
	var msta = null;
	var monthPerfStandards = [];
	function getMonthPerfStandard(month){
		if(monthPerfStandards[month] == undefined){
		$.ajax({
			  url: ctx + "/perfStandard/getByMonth",
			  async: false,
			  dataType:"json",
			  data:{month:month},
			  success:function(data){
				  monthPerfStandards[month] = data;
				}
			 });
		}
		
		return monthPerfStandards[month];
	}
	
	function greateData(formId,gridId){
		var jsonData = $("#" + formId).serializeJson();
		$.ad.joinArrayInObjectToString(jsonData);
		var v = jsonData.month_gte_intt;
		if(v == ""){
			$.sm.alert("请在开始月份处录入要生成数据的月份！");
			return;
		}
		$.ajax({
			  url: ctx + "/perfEval/greateMonthEvaData",
			  method:"post",
			  async: false,
			  dataType:"json",
			  data:{month:v,brokerId_in_String:jsonData.brokerId_in_String},
			  success:function(data){
				  $.sm.handleResult(data);
				  if(data.status == $.sm.ResultStatus_Ok){
				  	treeReload(formId,gridId);
				  }
				}
			 });
	}
	
	function treeReload(formId,gridId){
		var jsonData = $("#" + formId).serializeJson();
		$.ad.joinArrayInObjectToString(jsonData);
		
		$('#' + gridId).treegrid('options').url='${ctx }/perfEval/query';
		$('#' + gridId).treegrid('load',jsonData);
	}
	
	function onBeforeExpand(row){
		if(!row.childrenLoaded){
			$.ajax({
				  url: ctx + "/perfEval/getBrokerPerfLiverDetail",
				  method:"post",
				  async: false,
				  dataType:"json",
				  data:{brokerId:row.brokerId,month:row.month},
				  success:function(data){
					  for(var i in data){
						  data[i].brokerName = data[i].liverName;
					  }
					  $('#liveData_tj_grid').treegrid('append',{
							parent: row.id,
							data: data
						});
						row.childrenLoaded = true;
					}
				 });
		}
		return true;
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
			msta = getMonthPerfStandard(row.month);
			row.perfEval = row.gflv * msta.giftProp / 100 + row.dulv * msta.durationProp / 100 + parseFloat(row.gradeProp);
			row.perfEvalText = "(" + row.gflv + "% * " + msta.giftProp + "%) + (" + row.dulv + "% * " + msta.durationProp + "%) + " + row.gradeProp + "% = " + row.perfEval + "%";
			row.perfComm = Math.round(row.comm * row.perfEval) / 100,
			t.treegrid('refresh', editingId);
			$.ajax({
				  url: ctx + "/perfEval/update",
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
	
		function gridDataLoadFilter(data){
			var row;
			for(var i in data.rows){
				row = data.rows[i];
				row.children = [];
				row.state = "closed";
				row.childrenLoaded = false;
			}
			
			return data;
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
		
		function doExport(formId){
			var queryParams = $("#" + formId).serializeJson();
			$.ad.joinArrayInObjectToString(queryParams);
			$("#exportForm").form("load",queryParams);
			$("#exportForm").form({url:ctx + "/perfEval/export"});
			$('#exportForm').form('submit');
		}
		
		function valuePercFormatter(value,row,index){
			if(value){
				return value + "%";
			}
			return "";
		}
		
		var today = '${appfn:today()}';
		var ss = today.split("-");
		var currMonth = ss[0] + ss[1];		

	</script>
	<%@ include file="/WEB-INF/jsp/frame/footer.jsp"%>
</body>
</html>