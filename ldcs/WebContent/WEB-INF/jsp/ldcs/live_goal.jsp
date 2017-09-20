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
		主播月度目标管理
	</h3>
	
	<div id="liveGoal_tb" style="padding: 5px; height: auto">
		<div style="margin-bottom: 5px">
			<app:author path="/liveGoal/add">
				<a href="#" onclick="javascript:$('#liveGoal_grid').edatagrid('addRow')" class="easyui-linkbutton"
					iconCls="icon-add" plain="true"><s:message code='comm.add' /></a> 
			</app:author>
			<app:author path="/liveGoal/delete">
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="javascript:$('#liveGoal_grid').edatagrid('destroyRow')"><s:message code='comm.remove' /></a>
			</app:author>
			<app:author path="/liveGoal/add;/liveGoal/update">
				<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:$('#liveGoal_grid').edatagrid('saveRow')"><s:message code='comm.save' /></a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:$('#liveGoal_grid').edatagrid('cancelRow')"><s:message code='comm.cancel' /></a>
				<!-- <a  plain="true" 
					href="javascript:liveDataIn.loadData()"
					class="easyui-linkbutton" iconCls="icon-edit">批量生成录入数据</a> -->
				<a plain="true" 
					 href="#" onclick="javascript:exportTemplate()"
					class="easyui-linkbutton" iconCls="icon-down">导出录入表格</a>
				<a plain="true" 
					 href="#" onclick="javascript:$('#liveData_import').form('clear');$('#liveData_w').window('open');"
					class="easyui-linkbutton" iconCls="icon-up">Excel批量导入</a>
			</app:author>
		</div>
		<div>
			<form id="liveGoal_query_form">
				<s:message code="liver.broker"/>:
				<input class="easyui-combotree" name="brokerId_in_String" style="width: 160px"
					data-options="
						url:'${ctx }/user/memberTree',				
	                    multiple:true,
						loadFilter:userOrgTreeLoadFilter,
						panelWidth:180">
				<s:message code="liver"/>:
				<input class="easyui-combotree" name="liverId_in_String" style="width: 160px"
					data-options="
						url:'${ctx }/liver/tree',				
	                    multiple:true,
						loadFilter:liverOrgTreeLoadFilter,
						panelWidth:180">
				月份(YYYYMM)
				: <input class="easyui-numberbox" style="width: 80px" 
					data-options="min:201601,value:currMonth"
					name="month_gte_intt">
					-
				  <input class="easyui-numberbox" style="width: 80px"
						data-options="min:201601,value:currMonth"
					name="month_lte_intt">
				<a
					href="javascript:$.ad.gridQuery('liveGoal_query_form','liveGoal_grid')"
					class="easyui-linkbutton" iconCls="icon-search"><s:message
						code="comm.query" /></a>
				<a
					href="javascript:doExport('liveGoal_grid')"
					class="easyui-linkbutton" iconCls="icon-down">导 出</a>
			</form>
		</div>
	</div>

	<table id="liveGoal_grid" 
		data-options="rownumbers:true,singleSelect:true,pagination:true,multiSort:true,selectOnCheck:true,checkOnSelect:true,
				sortName:'month,brokerId,liverId',sortOrder:'desc,asc,asc',
				idField:'id',method:'post',toolbar:'#liveGoal_tb',loadFilter:liveGoalDataProcess">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'liverId',width:100,sortable:'true',formatter:liverFormatter,
					editor:{
						type:'combotree',
						options:{
							url:'${ctx }/liver/tree',
							onBeforeSelect:liverTreeBeforeSelect,
							loadFilter:liverOrgTreeLoadFilter,
							onSelect:liverTreeOnSelect,
							required:true,
							panelWidth:180
						}
					}"><s:message
						code="liveData.liver"/></th>
				<th
					data-options="field:'brokerId',width:100,sortable:'true',formatter:brokerFormatter,
					editor:{
						type:'combotree',
						options:{
							url:'${ctx }/user/memberTree',
							onBeforeSelect:brokerTreeBeforeSelect,
							loadFilter:userOrgTreeLoadFilter,
							required:true,
							panelWidth:180
						}
					}"><s:message
						code="liver.broker"/></th>
				<th
					data-options="field:'month',width:80,align:'center',sortable:'true',editor:{type:'numberbox',
						options:{
							min:201601}}">月份</th>	
				<th
					data-options="field:'giftEarning',width:120,editor:{type:'numberbox',
						options:{
							precision:2,
							min:0,
							max:99999999.99}}">礼物收益目标(元)</th>
				<th
					data-options="field:'liveDuration',width:120,editor:'numberbox',formatter:ldcsComm.minuteFormat">直播时长目标(分钟)</th>
				<th
					data-options="field:'remark',width:240,editor:{type:'textarea',
						options:{
							validType:'length[0,200]'}}"><s:message
						code="liveData.remark"/></th>
			</tr>
		</thead>
	</table>
	<form id="exportForm" action="${ctx }/liveGoal/export" method="post">
		<input type="hidden" name="brokerId_in_String"/>
		<input type="hidden" name="liverId_in_String"/>
		<input type="hidden" name="month_gte_intt"/>
		<input type="hidden" name="month_lte_intt"/>
		<input type="hidden" name="sort" value="brokerId,liverId,month"/>
		<input type="hidden" name="order" value="ASC,ASC,DESC"/>
	</form>
	<script type="text/javascript">	
		function doExport(gridId){
			var queryParams = $("#" + gridId).datagrid("options").queryParams;
			$("#exportForm").form("load",queryParams);
			$("#exportForm").form({url:ctx + "/liveGoal/export"});
			$('#exportForm').form('submit');
		}
		
		function exportTemplate(){
			var jsonData = $("#liveGoal_query_form").serializeJson();
			$.ad.joinArrayInObjectToString(jsonData);
			$("#exportForm").form("load",jsonData);
			$("#exportForm").form({url:ctx + "/liveGoal/exportTemplate"});
			$('#exportForm').form('submit');
		}
		
		function doImport(){
			$("#liveData_import").form('submit',{success:function(data){
				data = eval('(' + data + ')');
				$.sm.show("导入完成！");
				$('#liveData_w').window('close');
				$("#liveGoal_grid").edatagrid("loadData",data);
			}});
		}
		
		function liveGoalDataProcess(data){
			return data;
		}
		
		function liverFormatter(value,row,index){
			return row.liverName? row.liverName : '';;
		}
	
		function brokerFormatter(value,row,index){
			return row.brokerName ? row.brokerName : '';
		}
		

		$('#liveGoal_grid').edatagrid({
			url:'${ctx }/liveGoal/query',
			saveUrl: '${ctx }/liveGoal/add',
			updateUrl: '${ctx }/liveGoal/update',
			destroyUrl: '${ctx }/liveGoal/delete',
			onBeforeEdit:liveGoalGridBeforeEdit
		});
				
		function liveGoalUpdate(){
			if(!$("#liveGoal_w").window("options").closed){
				var selRows = $("#liveGoal_grid").datagrid("getSelections");
				$("#brokerTreeInput").combotree('setValue', selRows[0].broker.id);
			}
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
		var currliveGoalGridEditRowIndex;
		
		function liveGoalGridBeforeEdit(index){
			currliveGoalGridEditRowIndex = index;
		}
		
		
		function liverTreeBeforeSelect(node){
			return node.type == "liver";
		}
		
		function liverTreeOnSelect(node){
			var row = $('#liveGoal_grid').edatagrid("getSelected");
			row.liverName = node.text;
			var editors = $('#liveGoal_grid').edatagrid("getEditors",currliveGoalGridEditRowIndex);
			for(var i in editors){
				if(editors[i].field == "brokerId"){
					$(editors[i].target).combotree("setValue",node.origin.broker.id);
					row.brokerName = node.origin.broker.name;
				}
				else if(editors[i].field == "month"){
					$(editors[i].target).numberbox("setValue",currMonth);
				}
			}
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
		
		function brokerTreeBeforeSelect(node){
			return node.userType != undefined && node.userType == "B";
		}
	</script>
	
	<div id="liveData_w" class="easyui-window" title='主播月度目标导入'
		data-options="modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false"
		style="width: 400px; height: 180px;">
		<div class="content">
				<form id="liveData_import" method="post" action="${ctx }/liveGoal/import" enctype="multipart/form-data">
				
							<div style="margin-bottom: 10px">
								<input class="easyui-filebox" name="myfile" 
									data-options="prompt:'请选择要导入的文件...',required:true,buttonText:'选择文件'" style="width:100%">
							</div>
				</form>
				<div style="text-align: center; padding: 5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						onclick="doImport()" style="width: 80px">
						<s:message code="comm.submit" /></a> 
				</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/jsp/frame/footer.jsp"%>
</body>
</html>