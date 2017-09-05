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
		主播月度数据统计
	</h3>
	
	<div id="liveData_tj_tb" style="padding: 5px; height: auto">
		<div>
			<form id="liveData_tj_query_form">
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
					href="javascript:$.ad.gridQuery('liveData_tj_query_form','liveData_tj_grid')"
					class="easyui-linkbutton" iconCls="icon-search"><s:message
						code="comm.query" /></a>
				<a
					href="javascript:doExport('liveData_tj_grid')"
					class="easyui-linkbutton" iconCls="icon-down">导 出</a>
			</form>
		</div>
	</div>

	<table id="liveData_tj_grid" 
		data-options="rownumbers:true,singleSelect:true,pagination:true,multiSort:true,
				sortName:'month,brokerId,liverId',sortOrder:'desc,asc,asc',url:'${ctx }/liveData/tjQuery',
				idField:'id',method:'post',toolbar:'#liveData_tj_tb'">
		<thead>
			<tr>
				<th data-options="field:'liverId',width:100,sortable:'true',formatter:liverFormatter"><s:message
						code="liveData.liver"/></th>
				<th
					data-options="field:'brokerId',width:100,sortable:'true',formatter:brokerFormatter"><s:message
						code="liver.broker"/></th>
				<th
					data-options="field:'month',width:80,align:'center',sortable:'true'">月份</th>	
				<th
					data-options="field:'giftEarning',width:120">礼物收益目标(元)</th>
				<th
					data-options="field:'liveDuration',width:120">直播时长目标(分钟)</th>
				<th
					data-options="field:'remark',width:240"><s:message
						code="liveData.remark"/></th>
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
		function doExport(gridId){
			var queryParams = $("#" + gridId).datagrid("options").queryParams;
			$("#exportForm").form("load",queryParams);
			$("#exportForm").form({url:ctx + "/liveData_tj/export"});
			$('#exportForm').form('submit');
		}
		
		function liverFormatter(value,row,index){
			return row.liverName? row.liverName : '';;
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