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
		绩效评分标准管理
	</h3>
	
	<div id="perfStandard_tb" style="padding: 5px; height: auto">
		<div style="margin-bottom: 5px">
			<app:author path="/perfStandard/add">
				<a href="javascript:$('#perfStandard_grid').edatagrid('addRow')" class="easyui-linkbutton"
					iconCls="icon-add" plain="true"><s:message code='comm.add' /></a> 
			</app:author>
			<app:author path="/perfStandard/delete">
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="javascript:$('#perfStandard_grid').edatagrid('destroyRow')"><s:message code='comm.remove' /></a>
			</app:author>
			<app:author path="/perfStandard/add;/perfStandard/update">
				<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:$('#perfStandard_grid').edatagrid('saveRow')"><s:message code='comm.save' /></a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:$('#perfStandard_grid').edatagrid('cancelRow')"><s:message code='comm.cancel' /></a>
			</app:author>
		</div>
		<div>
			<form id="perfStandard_query_form">
				月份(YYYYMM)
				: <input class="easyui-numberbox" style="width: 80px" 
					data-options="min:201601"
					name="month_gte_intt">
					-
				  <input class="easyui-numberbox" style="width: 80px"
						data-options="min:201601,value:currMonth"
					name="month_lte_intt">
				<a
					href="javascript:$.ad.gridQuery('perfStandard_query_form','perfStandard_grid')"
					class="easyui-linkbutton" iconCls="icon-search"><s:message
						code="comm.query" /></a>
			</form>
		</div>
	</div>

	<table id="perfStandard_grid" 
		data-options="rownumbers:true,singleSelect:true,pagination:true,multiSort:true,selectOnCheck:true,checkOnSelect:true,
				sortName:'month',sortOrder:'desc',
				idField:'id',method:'post',toolbar:'#perfStandard_tb'">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true" rowspan="2"></th>
				<th rowspan="2"
					data-options="field:'month',width:70,align:'center',sortable:'true',editor:{type:'numberbox',
						options:{
							min:201601}}">月份</th>	
				<th colspan="3">权重比例%</th>
				<th colspan="4">礼物收益提成%</th>
				<th rowspan="2"
					data-options="field:'remark',align:'center',width:280,editor:{type:'textarea',
						options:{
							validType:'length[0,200]'}}">备注</th>
			</tr>
			<tr>
				<th
					data-options="field:'giftProp',width:70,formatter:percentFormatter,
						editor:{type:'numberbox',
						options:{
							precision:2,
							min:0,
							max:100}}">礼物收益</th>
				<th
					data-options="field:'durationProp',width:70,formatter:percentFormatter,
					editor:{type:'numberbox',
						options:{
							precision:2,
							min:0,
							max:100}}">直播时长</th>
				<th
					data-options="field:'gradeProp',width:70,formatter:percentFormatter,
					editor:{type:'numberbox',
						options:{
							precision:2,
							min:0,
							max:100}}">部门评分</th>
				<th
					data-options="field:'commLevelBase',width:100,formatter:percentFormatter,
					editor:{type:'numberbox',
						options:{
							min:0}}">基等级上值</th>
				<th
					data-options="field:'commLevelBaseProp',width:100,formatter:percentFormatter,
					editor:{type:'numberbox',
						options:{
							precision:2,
							min:0,
							max:100}}">基等级提成比例</th>
				<th
					data-options="field:'commLevelInterval',width:100,formatter:percentFormatter,
					editor:{type:'numberbox',
						options:{
							min:0}}">等级递增值</th>
				<th
					data-options="field:'commLevelPropInterval',width:100,formatter:percentFormatter,
					editor:{type:'numberbox',
						options:{
							precision:2,
							min:0,
							max:100}}">提成比例递增值</th>
			</tr>
		</thead>
	</table>
	<script type="text/javascript">	
		
		function percentFormatter(value,row,index){
			if(value){
				return value + "%";
			}
			return '';
		}

		$('#perfStandard_grid').edatagrid({
			url:'${ctx }/perfStandard/query',
			saveUrl: '${ctx }/perfStandard/add',
			updateUrl: '${ctx }/perfStandard/update',
			destroyUrl: '${ctx }/perfStandard/delete'
		});
		
		var today = '${appfn:today()}';
		var ss = today.split("-");
		var currMonth = ss[0] + ss[1];

	</script>
	<%@ include file="/WEB-INF/jsp/frame/footer.jsp"%>
</body>
</html>