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
	<script type="text/javascript" src="${ctx }/js/app/ldcs/live_data_in.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/frame/header.jsp"%>
	<h3>
		<s:message code="liveData.title"/>
	</h3>
	
	<div id="liveData_tb" style="padding: 5px; height: auto">
		<div style="margin-bottom: 5px">
			<app:author path="/liveData/add">
				<a href="javascript:$('#liveData_grid').edatagrid('addRow')" class="easyui-linkbutton"
					iconCls="icon-add" plain="true"><s:message code='comm.add' /></a> 
			</app:author>
			<app:author path="/liveData/delete">
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="javascript:$('#liveData_grid').edatagrid('destroyRow')"><s:message code='comm.remove' /></a>
			</app:author>
			<app:author path="/liveData/add;/liveData/update">
				<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:$('#liveData_grid').edatagrid('saveRow')"><s:message code='comm.save' /></a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:$('#liveData_grid').edatagrid('cancelRow')"><s:message code='comm.cancel' /></a>
			</app:author>
		</div>
		<div>
			<form id="liveData_query_form" method="post" action="${ctx }/liveData/exportTemplate">
				<s:message code="liver"/>:
				<input id="liverCombotree" class="easyui-combotree" name="liverId_in_String" style="width: 160px"
					data-options="
						url:'${ctx }/liver/tree',				
	                    multiple:true,
						loadFilter:liverOrgTreeLoadFilter,
						panelWidth:180">
				<s:message code="liveData.date"/>
				: <input class="easyui-datebox" style="width: 100px" id="date" data-options="required:true,value:'${appfn:today()}'"
						 name="date_eq_date">
				
			<app:author path="/liveData/add;/liveData/update">
				<a
					href="javascript:liveDataIn.loadData()"
					class="easyui-linkbutton" iconCls="icon-edit">生成录入表格</a>
				<a
					href="javascript:liveDataIn.exportTemplate()"
					class="easyui-linkbutton" iconCls="icon-down">导出录入表格</a>
				<a
					href="javascript:$('#liveData_import').form('clear');$('#liveData_w').window('open');"
					class="easyui-linkbutton" iconCls="icon-edit">Excel批量导入</a>
			</app:author>
			</form>
		</div>
	</div>

	<table id="liveData_grid" 
		data-options="rownumbers:true,singleSelect:true,multiSort:true,selectOnCheck:true,checkOnSelect:true,
				idField:'id',method:'post',toolbar:'#liveData_tb'">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'liverId',width:70,sortable:'true',formatter:liverFormatter,
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
					data-options="field:'brokerId',width:80,sortable:'true',formatter:brokerFormatter,
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
					data-options="field:'platform',width:80,sortable:'true',editor:'text'"><s:message
						code="liver.platform"/></th>
				<th
					data-options="field:'roomNo',width:80,editor:'text'"><s:message
						code="liver.roomNo"/></th>
				<th
					data-options="field:'liveName',width:100,editor:'text'"><s:message
						code="liver.liveName"/></th>
				<th
					data-options="field:'date',width:90,align:'center',sortable:'true',editor:'datebox'"><s:message
						code="liveData.date"/></th>	
				<th
					data-options="field:'rss',width:68,editor:'numberbox'"><s:message
						code="liveData.rss"/></th>
				<th
					data-options="field:'rssGrowRate',width:60,editor:{type:'numberbox',
						options:{
							precision:4,
							min:-999.9999,
							max:999.9999}}"><s:message
						code="liveData.rssGrowRate"/></th>
				<th
					data-options="field:'popularity',width:60,editor:'numberbox'"><s:message
						code="liveData.popularity"/></th>
				<th
					data-options="field:'giftEarning',width:80,editor:{type:'numberbox',
						options:{
							precision:2,
							min:0,
							max:99999999.99}}"><s:message
						code="liveData.giftEarning"/></th>
				<th
					data-options="field:'liveDuration',width:90,editor:'numberbox'"><s:message
						code="liveData.liveDuration"/></th>
				<th
					data-options="field:'remark',width:140,editor:{type:'textarea',
						options:{
							validType:'length[0,200]'}}"><s:message
						code="liveData.remark"/></th>
			</tr>
		</thead>
	</table>
	<form id="exportTemplateForm" method="post" action="${ctx }/liveData/exportTemplate">
		<input type="hidden" name="liverId_in_String" />
		<input type="hidden" name="date_eq_date" />
	</form>
	<script type="text/javascript">	
		
		function liverFormatter(value,row,index){
			return row.liverName? row.liverName : '';;
		}
	
		function brokerFormatter(value,row,index){
			return row.brokerName ? row.brokerName : '';
		}
		

		$('#liveData_grid').edatagrid({
			saveUrl: '${ctx }/liveData/add',
			updateUrl: '${ctx }/liveData/update',
			destroyUrl: '${ctx }/liveData/delete',
			onBeforeEdit:liveDataGridBeforeEdit
		});


		var treeLivers;
		
		function liverOrgTreeLoadFilter(data){
			treeLivers = [];
			for(var i in data){
				if(data[i].type == "liver" && data[i].origin.broker){
					if(data[i].origin.broker.$ref)
						data[i].origin.broker = eval(data[i].origin.broker.$ref.replace('$',"data"));
				}
			}
			
			var nodes = $.ad.standardIdPidNameArrayToEasyUITree(data);
			removeHasNoLiverNodes(nodes);
			treeLivers.reverse();
			return nodes;
		}
		
		
		function removeHasNoLiverNodes(data){
			for(var i = data.length - 1; i >= 0; i--){			
				if(data[i].type == "liver"){
					treeLivers.push(data[i]);
				}
				
				if(data[i].type != "liver" && data[i].children != undefined){
					removeHasNoLiverNodes(data[i].children);
				}
				
				if(data[i].type != "liver" && (data[i].children == undefined || data[i].children.length == 0)){
					data.splice(i,1);
				}
			}
		}
		
		var today = '${appfn:today()}';
		
		var currLiveDataGridEditRowIndex;
		
		function liveDataGridBeforeEdit(index){
			currLiveDataGridEditRowIndex = index;
		}
		
		
		function liverTreeBeforeSelect(node){
			return node.type == "liver";
		}
		
		function liverTreeOnSelect(node){
			var row = $('#liveData_grid').edatagrid("getSelected");
			row.liverName = node.text;
			var editors = $('#liveData_grid').edatagrid("getEditors",currLiveDataGridEditRowIndex);
			for(var i in editors){
				if(editors[i].field == "brokerId"){
					$(editors[i].target).combotree("setValue",node.origin.broker.id);
					row.brokerName = node.origin.broker.name;
				}
				else if(editors[i].field == "platform"){
					$(editors[i].target).val(node.origin.platform);
				}
				else if(editors[i].field == "roomNo"){
					$(editors[i].target).val(node.origin.roomNo);
				}
				else if(editors[i].field == "liveName"){
					$(editors[i].target).val(node.origin.liveName);
				}
				else if(editors[i].field == "date"){
					$(editors[i].target).datebox("setValue",today);
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
	<div id="liveData_w" class="easyui-window" title='直播数据导入'
		data-options="modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false"
		style="width: 400px; height: 180px;">
		<div class="content">
				<form id="liveData_import" method="post" action="${ctx }/liveData/import" enctype="multipart/form-data">
				
							<div style="margin-bottom: 10px">
								<input class="easyui-filebox" name="myfile" 
									data-options="prompt:'请选择要导入的文件...',required:true,buttonText:'选择文件'" style="width:100%">
							</div>
				</form>
				<div style="text-align: center; padding: 5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						onclick="liveDataIn.doImport()" style="width: 80px">
						<s:message code="comm.submit" /></a> 
				</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/jsp/frame/footer.jsp"%>
</body>
</html>