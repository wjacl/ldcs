<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/jsp/frame/comm_css_js.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/frame/header.jsp"%>
	<h3>
		<s:message code="liveData.title"/>
	</h3>
	
	<div id="liveData_tb" style="padding: 5px; height: auto">
		<div style="margin-bottom: 5px">
			<a href="javascript:$('#liveData_grid').edatagrid('addRow')" class="easyui-linkbutton"
				iconCls="icon-add" plain="true"><s:message code='comm.add' /></a> 
			<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="javascript:$('#liveData_grid').edatagrid('destroyRow')"><s:message code='comm.remove' /></a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:$('#liveData_grid').edatagrid('saveRow')"><s:message code='comm.save' /></a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:$('#liveData_grid').edatagrid('cancelRow')"><s:message code='comm.cancel' /></a>
		</div>
		<div>
			<form id="liveData_query_form">
				<%-- <s:message code="liver.broker"/>
				: <input class="easyui-textbox" style="width: 100px"
					name="name_like_string">
				<s:message code="liver.liveName"/>
				: <input class="easyui-textbox" style="width: 100px"
					name="liveName_like_string"> --%>
				<s:message code="liver.broker"/>:
				<input class="easyui-combotree" name="brokerId_in_String" style="width: 160px"
					data-options="
						required:true,
						url:'${ctx }/user/memberTree',				
	                    multiple:true,
						loadFilter:userOrgTreeLoadFilter">
				<s:message code="liver"/>:
				<input class="easyui-combotree" name="liverId_in_String" style="width: 160px"
					data-options="
						required:true,
						url:'${ctx }/liver/tree',				
	                    multiple:true,
						loadFilter:liverOrgTreeLoadFilter">
				<s:message code="liver.platform"/>
				: <input class="easyui-textbox" style="width: 100px"
					name="platform_like_string">
				<a
					href="javascript:$.ad.gridQuery('liveData_query_form','liveData_grid')"
					class="easyui-linkbutton" iconCls="icon-search"><s:message
						code="comm.query" /></a>
			</form>
		</div>
	</div>

	<table id="liveData_grid" 
		data-options="rownumbers:true,singleSelect:true,pagination:true,multiSort:true,selectOnCheck:true,
				idField:'id',method:'post',toolbar:'#liveData_tb',loadFilter:liveDataDataProcess">
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
							required:true
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
							width:120
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
					data-options="field:'date',width:60,align:'center',sortable:'true',editor:'datebox'"><s:message
						code="liveData.date"/></th>	
				<th
					data-options="field:'rss',width:60,editor:'numberbox'"><s:message
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
					data-options="field:'giftEarning',width:100,editor:{type:'numberbox',
						options:{
							precision:2,
							min:0,
							max:99999999.99}}"><s:message
						code="liveData.giftEarning"/></th>
				<th
					data-options="field:'liveDuration',width:60,editor:'numberbox'"><s:message
						code="liveData.liveDuration"/></th>
				<th
					data-options="field:'remark',width:140,editor:{type:'textarea',
						options:{
							validType:'length[0,200]'}}"><s:message
						code="liveData.remark"/></th>
			</tr>
		</thead>
	</table>
	<script type="text/javascript">
	
		function liveDataDataProcess(data){
			return data;
		}
		
		function liverFormatter(value,row,index){
			return row.liverName? row.liverName : '';;
		}
	
		function brokerFormatter(value,row,index){
			return row.brokerName ? row.brokerName : '';
		}
		

		$('#liveData_grid').edatagrid({
			url:'${ctx }/liveData/query',
			saveUrl: '${ctx }/liveData/add',
			updateUrl: '${ctx }/liveData/update',
			destroyUrl: '${ctx }/liveData/delete',
			onBeforeEdit:liveDataGridBeforeEdit
		});
				
		function liveDataUpdate(){
			if(!$("#liveData_w").window("options").closed){
				var selRows = $("#liveData_grid").datagrid("getSelections");
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
		
		var currLiveDataGridEditRowIndex;
		
		function liveDataGridBeforeEdit(index){
			currLiveDataGridEditRowIndex = index;
		}
		
		
		function liverTreeBeforeSelect(node){
			if(node.type == "liver"){
				var row = $('#liveData_grid').datagrid("getSelected");
				row.liverName = node.text;
				row.brokerId = node.origin.broker.id;
				row.brokerName = node.origin.broker.name;
				row.platform = node.origin.platform;
				row.roomNo = node.origin.roomNo;
				row.liveName = node.origin.liveName;
				
				currLiveDataGridEditRowIndex
				var ed = $('#dg').datagrid('getEditor', {index:1,field:'birthday'});
				$(ed.target).datebox('setValue', '5/4/2012');
				return true;
			}
			
			return false;
		}
		
		function liverTreeOnSelect(node){
			var row = $('#liveData_grid').datagrid("getSelected");
			row.liverName = node.text;
			var editors = $('#liveData_grid').datagrid("getEditor",currLiveDataGridEditRowIndex);
			for(var i in editors){
				if(editors[i].field == "brokerId"){
					$(editors[i].target).combotree("setValue",node.origin.broker.id);
				}
				else if(editors[i].field == "platform"){
					$(editors[i].target).text("setValue",node.origin.platform);
				}
				else if(editors[i].field == "roomNo"){
					$(editors[i].target).text("setValue",node.origin.roomNo);
				}
				else if(editors[i].field == "liveName"){
					$(editors[i].target).text("setValue",node.origin.liveName);
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
	<%-- 
	<div id="liveData_w" class="easyui-window" title='<s:message code="liveData.add" />'
		data-options="modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false"
		style="width: 860px; height: 520px;">
		<div class="content">
				<form id="liveData_add" method="post" action="${ctx }/liveData/add">
				<table>
					<tr>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="name" style="width: 100%"
									data-options="label:'<s:message code="liveData.name"/>:',required:true,validType:'length[1,30]'">
							</div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="idCardNo" style="width: 100%"
									data-options="label:'<s:message code="liveData.idCardNo"/>:',validType:'length[0,30]'">
							</div>
                    	</td>
						<td>
							<div style="margin-bottom: 10px">
								<input id="brokerTreeInput" class="easyui-combotree" name="broker.id" style="width: 100%"
									data-options="label:'<s:message code="liveData.broker"/>:',
										required:true,
										url:'${ctx }/user/memberTree',
										onBeforeSelect:liverTreeBeforeSelect,
										loadFilter:liverOrgTreeLoadFilter">
							</div>
						</td>
                    </tr>
					<tr>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="platform" style="width: 100%"
									data-options="label:'<s:message code="liveData.platform"/>:',validType:'length[0,40]'">
							</div>
                    	</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="roomNo" style="width: 100%"
									data-options="label:'<s:message code="liveData.roomNo"/>:',validType:'length[0,20]'">
							</div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="liveName" style="width: 100%"
									data-options="label:'<s:message code="liveData.liveName"/>:',validType:'length[0,60]'">
							</div>
						</td>
                    </tr>
					<tr>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-combobox" name="entryStatus"
								style="width: 100%;"
								data-options="
				                    url:'${ctx }/dict/get?pvalue=liveData.entryStatus',
				                    method:'get',
				                    valueField:'value',
				                    textField:'name',
				                    panelHeight:'auto',
				                    required:true,
				                    editable:false,
				                    label:'<s:message code="liveData.entryStatus"/>:'
			                    ">
		                    </div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-datebox" name="entryDate" style="width: 100%"
									data-options="label:'<s:message code="liveData.entryDate"/>:'">
							</div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-datebox" name="leaveDate" style="width: 100%"
									data-options="label:'<s:message code="liveData.leaveDate"/>:'">
							</div>
						</td>
                    </tr>
					<tr>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-combobox" name="signStatus"
								style="width: 100%;"
								data-options="
				                    url:'${ctx }/dict/get?pvalue=liveData.signStatus',
				                    method:'get',
				                    valueField:'value',
				                    textField:'name',
				                    panelHeight:'auto',
				                    required:true,
				                    editable:false,
				                    label:'<s:message code="liveData.signStatus"/>:'
			                    ">
		                    </div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-combobox" name="liveStatus"
								style="width: 100%;"
								data-options="
				                    url:'${ctx }/dict/get?pvalue=liveData.liveStatus',
				                    method:'get',
				                    valueField:'value',
				                    textField:'name',
				                    panelHeight:'auto',
				                    required:true,
				                    editable:false,
				                    label:'<s:message code="liveData.liveStatus"/>:'
			                    ">
		                    </div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-datebox" name="leaveDate" style="width: 100%"
									data-options="label:'<s:message code="liveData.leaveDate"/>:'">
							</div>
						</td>
                    </tr>
                    <tr>
                    	<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-combobox" name="sex"
								style="width: 100%;"
								data-options="
				                    url:'${ctx }/dict/get?pvalue=sex',
				                    method:'get',
				                    valueField:'value',
				                    textField:'name',
				                    panelHeight:'auto',
				                    required:true,
				                    editable:false,
				                    label:'<s:message code="user.sex"/>:'
			                    ">
		                    </div>
                    	</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-datebox" name="birthday" style="width: 100%"
									data-options="label:'<s:message code="liveData.birthday"/>:'">
							</div>
						</td>  
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="phone" style="width: 100%"
									data-options="label:'<s:message code="liveData.phone"/>:',validType:'length[0,30]'">
							</div>
						</td>                   
                    </tr>
                    <tr>
                    	<td colspan="6">
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="nativePlace" style="width: 100%"
									data-options="label:'<s:message code="liveData.nativePlace"/>:',validType:'length[0,100]'">
							</div>
                    	</td>   
                    </tr>
                    <tr>
                    	<td colspan="6">
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="speciality" style="width: 100%;height:60px"
									data-options="label:'<s:message code="liveData.speciality"/>:',multiline:true,validType:'length[0,200]'">
							</div>
                    	</td>   
                    </tr>
                    <tr>
                    	<td colspan="6">
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="remark" style="width: 100%;height:60px"
									data-options="label:'<s:message code="liveData.remark"/>:',multiline:true,validType:'length[0,200]'">
							</div>
                    	</td>   
                    </tr>
                </table>
                    <input type="hidden" name="id" />
                    <input type="hidden" name="version" />
				</form>
				<div style="text-align: center; padding: 5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						onclick="$.ad.submitForm('liveData_add','liveData_grid','liveData_w')" style="width: 80px">
						<s:message code="comm.submit" /></a> 
					<a href="javascript:void(0)"
						class="easyui-linkbutton" onclick="$.ad.clearForm('liveData_add')"
						style="width: 80px"><s:message code="comm.clear" /></a>
				</div>
		</div>
	</div>
		--%>
	<%@ include file="/WEB-INF/jsp/frame/footer.jsp"%>
</body>
</html>