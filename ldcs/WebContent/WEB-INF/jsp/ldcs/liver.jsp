<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
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
		<s:message code="liver.title" text="主播个人信息管理"/>
	</h3>
	
	<div id="liver_tb" style="padding: 5px; height: auto">
		<div style="margin-bottom: 5px">
			<app:author path="/liver/add">
			<a href="javascript:$.ad.toAdd('liver_w',I18N.liver,'liver_add','${ctx }/liver/add');" class="easyui-linkbutton"
				iconCls="icon-add" plain="true"><s:message code='comm.add' /></a> 
			</app:author>
			<app:author path="/liver/update"> 
			<a href="javascript:$.ad.toUpdate('liver_grid','liver_w',I18N.liver,'liver_add','${ctx }/liver/update');liverUpdate()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true"><s:message code='comm.update' /></a>	 
			</app:author>
			<app:author path="/liver/delete">
			<a href="javascript:$.ad.doDelete('liver_grid','${ctx }/liver/delete')" class="easyui-linkbutton" iconCls="icon-remove"
				plain="true"><s:message code='comm.remove' /></a> 
			</app:author>
		</div>
		<div>
			<form id="liver_query_form">
				<s:message code="liver.name" text="姓名"/>
				: <input class="easyui-textbox" style="width: 100px"
					name="name_like_string">
				<s:message code="liver.liveName"/>
				: <input class="easyui-textbox" style="width: 100px"
					name="liveName_like_string">
				<s:message code="liver.broker"/>:
				<input class="easyui-combotree" name="broker.id_in_String" style="width: 160px"
					data-options="
						url:'${ctx }/user/memberTree',				
	                    multiple:true,
						loadFilter:userOrgTreeLoadFilter">
				<s:message code="liver.platform"/>
				: <input class="easyui-textbox" style="width: 100px"
					name="platform_like_string">
				<s:message code="liver.entryStatus"/>:
				<input class="easyui-combobox" name="entryStatus_in_String"
					style="width: 80px;"
					data-options="
	                    url:'${ctx }/dict/get?pvalue=liver.entryStatus',
	                    method:'get',
	                    valueField:'value',
	                    textField:'name',
	                    panelHeight:'auto',				
	                    multiple:true,
	                    editable:false
                    ">
			    <s:message code="liver.liveStatus"/>:
				<input class="easyui-combobox" name="liveStatus_in_String"
				style="width: 80px;"
				data-options="
                    url:'${ctx }/dict/get?pvalue=liver.liveStatus',
                    method:'get',
                    valueField:'value',
                    textField:'name',
                    panelHeight:'auto',				
	                    multiple:true,
                    editable:false
                   ">
				<a
					href="javascript:$.ad.gridQuery('liver_query_form','liver_grid')"
					class="easyui-linkbutton" iconCls="icon-search"><s:message
						code="comm.query" /></a>
			</form>
		</div>
	</div>

	<table class="easyui-datagrid" id="liver_grid" 
		data-options="rownumbers:true,singleSelect:false,pagination:true,multiSort:true,selectOnCheck:true,
				url:'${ctx }/liver/query',method:'post',toolbar:'#liver_tb',loadFilter:liverDataProcess">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'name',width:100"><s:message
						code="liver.name"/></th>
				<th
					data-options="field:'broker.id',width:100,sortable:'true',formatter:brokerFormatter"><s:message
						code="liver.broker"/></th>
				<th
					data-options="field:'platform',width:100,sortable:'true'"><s:message
						code="liver.platform"/></th>
				<th
					data-options="field:'roomNo',width:100"><s:message
						code="liver.roomNo"/></th>
				<th
					data-options="field:'liveName',width:100"><s:message
						code="liver.liveName"/></th>
				<th
					data-options="field:'entryStatus',width:100,align:'center',sortable:'true',formatter:entryStatusFormatter"><s:message
						code="liver.entryStatus"/></th>	
				<th
					data-options="field:'entryDate',width:100,align:'center',sortable:'true'"><s:message
						code="liver.entryDate"/></th>		
				<th
					data-options="field:'signStatus',width:100,align:'center',sortable:'true',formatter:signStatusFormatter"><s:message
						code="liver.signStatus"/></th>
				<th
					data-options="field:'liveStatus',width:100,align:'center',sortable:'true',formatter:liveStatusFormatter"><s:message
						code="liver.liveStatus"/></th>
				<th
					data-options="field:'phone',width:100"><s:message
						code="liver.phone"/></th>
			</tr>
		</thead>
	</table>
	<script type="text/javascript">
	
		function liverDataProcess(data){
			if(data && data.rows){
				var rows = data.rows;
				for(var i in rows){
					
					//经纪人处理
					if(rows[i].broker){
						if(rows[i].broker.$ref){
							rows[i].broker = eval(rows[i].broker.$ref.replace('$',"data"));
						}
					}
				}
			}
			return data;
		}
	
	
		function brokerFormatter(value,row,index){
			return row.broker ? row.broker.name : '';
		}
	
		function entryStatusFormatter(value,row,index){
			return $.ad.getDictName("liver.entryStatus",value);
		}
		
		function signStatusFormatter(value,row,index){
			return $.ad.getDictName("liver.signStatus",value);
		}

		function liveStatusFormatter(value,row,index){
			return $.ad.getDictName("liver.liveStatus",value);
		}
		
		function liverUpdate(){
			if(!$("#liver_w").window("options").closed){
				var selRows = $("#liver_grid").datagrid("getSelections");
				$("#brokerTreeInput").combotree('setValue', selRows[0].broker.id);
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
	
	<div id="liver_w" class="easyui-window" title='<s:message code="liver.add" />'
		data-options="modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false"
		style="width: 860px; height: 520px;">
		<div class="content">
				<form id="liver_add" method="post" action="${ctx }/liver/add">
				<table>
					<tr>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="name" style="width: 100%"
									data-options="label:'<s:message code="liver.name"/>:',required:true,validType:'length[1,30]'">
							</div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="idCardNo" style="width: 100%"
									data-options="label:'<s:message code="liver.idCardNo"/>:',validType:'length[0,30]'">
							</div>
                    	</td>
						<td>
							<div style="margin-bottom: 10px">
								<input id="brokerTreeInput" class="easyui-combotree" name="broker.id" style="width: 100%"
									data-options="label:'<s:message code="liver.broker"/>:',
										required:true,
										url:'${ctx }/user/memberTree',
										onBeforeSelect:brokerTreeBeforeSelect,
										loadFilter:userOrgTreeLoadFilter">
							</div>
						</td>
                    </tr>
					<tr>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="platform" style="width: 100%"
									data-options="label:'<s:message code="liver.platform"/>:',validType:'length[0,40]'">
							</div>
                    	</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="roomNo" style="width: 100%"
									data-options="label:'<s:message code="liver.roomNo"/>:',validType:'length[0,20]'">
							</div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="liveName" style="width: 100%"
									data-options="label:'<s:message code="liver.liveName"/>:',validType:'length[0,60]'">
							</div>
						</td>
                    </tr>
					<tr>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-combobox" name="entryStatus"
								style="width: 100%;"
								data-options="
				                    url:'${ctx }/dict/get?pvalue=liver.entryStatus',
				                    method:'get',
				                    valueField:'value',
				                    textField:'name',
				                    panelHeight:'auto',
				                    required:true,
				                    editable:false,
				                    label:'<s:message code="liver.entryStatus"/>:'
			                    ">
		                    </div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-datebox" name="entryDate" style="width: 100%"
									data-options="label:'<s:message code="liver.entryDate"/>:'">
							</div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-datebox" name="leaveDate" style="width: 100%"
									data-options="label:'<s:message code="liver.leaveDate"/>:'">
							</div>
						</td>
                    </tr>
					<tr>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-combobox" name="signStatus"
								style="width: 100%;"
								data-options="
				                    url:'${ctx }/dict/get?pvalue=liver.signStatus',
				                    method:'get',
				                    valueField:'value',
				                    textField:'name',
				                    panelHeight:'auto',
				                    required:true,
				                    editable:false,
				                    label:'<s:message code="liver.signStatus"/>:'
			                    ">
		                    </div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-combobox" name="liveStatus"
								style="width: 100%;"
								data-options="
				                    url:'${ctx }/dict/get?pvalue=liver.liveStatus',
				                    method:'get',
				                    valueField:'value',
				                    textField:'name',
				                    panelHeight:'auto',
				                    required:true,
				                    editable:false,
				                    label:'<s:message code="liver.liveStatus"/>:'
			                    ">
		                    </div>
						</td>
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-datebox" name="leaveDate" style="width: 100%"
									data-options="label:'<s:message code="liver.leaveDate"/>:'">
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
									data-options="label:'<s:message code="liver.birthday"/>:'">
							</div>
						</td>  
						<td>
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="phone" style="width: 100%"
									data-options="label:'<s:message code="liver.phone"/>:',validType:'length[0,30]'">
							</div>
						</td>                   
                    </tr>
                    <tr>
                    	<td colspan="6">
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="nativePlace" style="width: 100%"
									data-options="label:'<s:message code="liver.nativePlace"/>:',validType:'length[0,100]'">
							</div>
                    	</td>   
                    </tr>
                    <tr>
                    	<td colspan="6">
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="speciality" style="width: 100%;height:60px"
									data-options="label:'<s:message code="liver.speciality"/>:',multiline:true,validType:'length[0,200]'">
							</div>
                    	</td>   
                    </tr>
                    <tr>
                    	<td colspan="6">
							<div style="margin-bottom: 10px">
								<input class="easyui-textbox" name="remark" style="width: 100%;height:60px"
									data-options="label:'<s:message code="liver.remark"/>:',multiline:true,validType:'length[0,200]'">
							</div>
                    	</td>   
                    </tr>
                </table>
                    <input type="hidden" name="id" />
                    <input type="hidden" name="version" />
				</form>
				<div style="text-align: center; padding: 5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						onclick="$.ad.submitForm('liver_add','liver_grid','liver_w')" style="width: 80px">
						<s:message code="comm.submit" /></a> 
					<a href="javascript:void(0)"
						class="easyui-linkbutton" onclick="$.ad.clearForm('liver_add')"
						style="width: 80px"><s:message code="comm.clear" /></a>
				</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/jsp/frame/footer.jsp"%>
</body>
</html>