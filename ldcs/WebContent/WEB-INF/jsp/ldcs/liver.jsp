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
		<s:message code="liver.title" text="主播个人信息管理"/>
	</h3>
	
	<div id="liver_tb" style="padding: 5px; height: auto">
		<div style="margin-bottom: 5px">
			<a href="javascript:$.ad.toAdd('liver_w',I18N.liver,'liver_add','${ctx }/liver/add');" class="easyui-linkbutton easyui-tooltip" title="<s:message code='comm.add' />"
				iconCls="icon-add" plain="true"></a> 
			<a href="javascript:$.ad.toUpdate('liver_grid','liver_w',I18N.liver,'liver_add','${ctx }/liver/update');liverUpdate()"
				class="easyui-linkbutton easyui-tooltip" title="<s:message code='comm.update' />" iconCls="icon-edit" plain="true"></a>
			<a href="javascript:$.ad.doDelete('liver_grid','${ctx }/liver/remove')" class="easyui-linkbutton easyui-tooltip" title="<s:message code='comm.remove' />" iconCls="icon-remove"
				plain="true"></a>
		</div>
		<div>
			<form id="liver_query_form">
				<s:message code="liver.name" text="姓名"/>
				: <input class="easyui-textbox" style="width: 120px"
					name="name_like_string">
				<s:message code="liver.stageName" text="艺名"/>
				: <input class="easyui-textbox" style="width: 120px"
					name="stageName_like_string">
				<a
					href="javascript:$.ad.gridQuery('liver_query_form','liver_grid')"
					class="easyui-linkbutton" iconCls="icon-search"><s:message
						code="comm.query" /></a>
			</form>
		</div>
	</div>

	<table class="easyui-datagrid" id="liver_grid" style="width: 720px;"
		data-options="rownumbers:true,singleSelect:false,pagination:true,multiSort:true,selectOnCheck:true,
				url:'${ctx }/liver/query',method:'post',toolbar:'#liver_tb'">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'name',width:100"><s:message
						code="liver.name"  text="姓名"/></th>
				<th data-options="field:'stageName',width:100"><s:message
						code="liver.stageName"  text="姓名"/></th>
				<th
					data-options="field:'phone',width:100"><s:message
						code="liver.phone"  text="联系电话"/></th>
				<th
					data-options="field:'broker.name',width:100,sortable:'true'"><s:message
						code="liver.broker"  text="经纪人"/></th>
				<th
					data-options="field:'entryStatus',width:100,align:'center',sortable:'true',formatter:liverStatusFormatter"><s:message
						code="liver.entryStatus"   text="入职状态"/></th>		
				<th
					data-options="field:'entryDate',width:100"><s:message
						code="liver.entryDate"  text="入职时间"/></th>		
				<th
					data-options="field:'leaveDate',width:100"><s:message
						code="liver.leaveDate"  text="离职时间"/></th>
				<th
					data-options="field:'signStatus',width:100,align:'center',sortable:'true',formatter:signStatusFormatter"><s:message
						code="liver.signStatus"   text="签约状态"/></th>		
				<th
					data-options="field:'signDate',width:100"><s:message
						code="liver.signDate"  text="签约时间"/></th>		
				<th
					data-options="field:'breakDate',width:100"><s:message
						code="liver.breakDate"  text="解约时间"/></th>
			</tr>
		</thead>
	</table>
	<script type="text/javascript">
	
		var liverStatus;
		function liverStatusFormatter(value,row,index){
			if(!liverStatus){
				$.ajax({ url: "${ctx }/dict/get?pvalue=liver.entryStatus",async:false, success: function(data){
			        liverStatus = data;
			      },dataType:'json'});
			}
			
			for(var i in liverStatus){
				if(liverStatus[i].value == value){
					return liverStatus[i].name;
				}
			}
		}
		
		var liverSignStatus;
		function liverStatusFormatter(value,row,index){
			if(!liverSignStatus){
				$.ajax({ url: "${ctx }/dict/get?pvalue=liver.signStatus",async:false, success: function(data){
					liverSignStatus = data;
			      },dataType:'json'});
			}
			
			for(var i in liverSignStatus){
				if(liverSignStatus[i].value == value){
					return liverSignStatus[i].name;
				}
			}
		}
		
		function liverUpdate(){
			if(!$("#liver_w").window("options").closed){

				$("#liverRoleComb").combobox('reload');
				var selRows = $("#liver_grid").datagrid("getSelections");
				var roleIds = [];
				var roles = selRows[0].roles;
				if(roles && roles.length > 0){
					for(var i in roles){
						roleIds.push(roles[i].id);
					}
				}
				$("#liverRoleComb").combobox('setValues', roleIds);
			}
		}
	</script>
	
	<div id="liver_w" class="easyui-window" title='<s:message code="liver.add" />'
		data-options="modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false"
		style="width: 400px; height: 400px; padding: 10px;">
		<div class="content">
				<form id="liver_add" method="post" action="${ctx }/liver/add">
					<div style="margin-bottom: 20px">
						<input class="easyui-textbox" name="name" style="width: 100%"
							data-options="label:'<s:message code="liver.name"/>:',required:true,validType:'length[1,30]'">
					</div>
					<div style="margin-bottom: 20px">
						<input class="easyui-textbox" name="livername" style="width: 100%"
							data-options="label:'<s:message code="liver.livername"/>:',required:true,
							validType:{length:[1,30],myRemote:['${ctx }/liver/unameCheck','livername','#oldlivername']},
							invalidMessage:I18N.liver_uname_exits">
						<input type="hidden" name="oldlivername" id="oldlivername" />
					</div>
					<div style="margin-bottom: 20px">
						<input class="easyui-textbox" name="password" type="password"  id="pwd"
							style="width: 100%"
							data-options="label:'<s:message code="liver.pwd"/>:',required:true,validType:'length[6,32]'">
					</div>
					<div style="margin-bottom: 20px">
						<input class="easyui-combobox" name="type"
						style="width: 100%;"
						data-options="
		                    url:'${ctx }/dict/get?pvalue=liver.type',
		                    method:'get',
		                    valueField:'value',
		                    textField:'name',
		                    panelHeight:'auto',
		                    required:true,
		                    label:'<s:message code="liver.type"/>:'
	                    ">
                    </div>
					<div style="margin-bottom: 20px">
						<input class="easyui-combobox" name="status"
						style="width: 100%;"
						data-options="
		                    url:'${ctx }/dict/get?pvalue=liver.status',
		                    method:'get',
		                    valueField:'value',
		                    textField:'name',
		                    panelHeight:'auto',
		                    required:true,
		                    label:'<s:message code="liver.status"/>:'
	                    ">
                    </div>
                    <div style="margin-bottom: 20px">
						<input class="easyui-combobox" name="roleIds"  id="liverRoleComb"
						style="width: 100%;"
						data-options="
		                    url:'${ctx }/liver/roles',
		                    method:'get',
		                    valueField:'id',
		                    textField:'name',
		                    panelHeight:'auto',
		                    label:'<s:message code="role"/>:',
	                    	multiple:true
	                    ">
                    </div>
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