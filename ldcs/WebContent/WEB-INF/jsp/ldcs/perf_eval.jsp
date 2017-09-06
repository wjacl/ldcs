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
	
	<div id="liveData_tj_tb" style="padding: 10px;height:50px">
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

	<table id="liveData_tj_grid" class="easyui-datagrid"
		data-options="rownumbers:true,singleSelect:true,multiSort:true,
				sortName:'month,brokerId,liverId',sortOrder:'desc,asc,asc',url:'${ctx }/perfEval/evalList',
				idField:'id',method:'post',toolbar:'#liveData_tj_tb',onLoadSuccess:onGridDataLoadSuccess">
		<thead>
			<tr>
				<th rowspan="2" 
					data-options="field:'brokerId',width:80,sortable:'true',formatter:brokerFormatter"><s:message
						code="liver.broker"/></th>
				<th rowspan="2" data-options="field:'liverId',width:80,sortable:'true',formatter:liverFormatter"><s:message
						code="liveData.liver"/></th>
				<th rowspan="2" 
					data-options="field:'month',width:80,align:'center',sortable:'true'">月份</th>
				<th colspan="4">礼物收益完成情况</th>
				<th colspan="4">直播时长完成情况</th>
			</tr>
			<tr>
				<th
					data-options="field:'giftEarning',width:90">礼物收益(元)</th>	
				<th
					data-options="field:'giftEarningGoal',width:110">礼物收益目标(元)</th>
				<th
					data-options="field:'id',width:80,formatter:girfPercFormatter">礼物完成率</th>
				<th
					data-options="field:'brokerName',width:80,formatter:girfLastFormatter">礼物差距</th>
				<th
					data-options="field:'liveDuration',width:100">直播时长(分钟)</th>
				<th
					data-options="field:'liveDurationGoal',width:120">直播时长目标(分钟)</th>
				<th
					data-options="field:'liverName',width:80,formatter:duraPercFormatter">时长完成率</th>
				<th
					data-options="field:'valid',width:80,formatter:duraLastFormatter">时长差距</th>
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
		var standards = [];
		
		function computeGiftComm(month,gift,goal){
			var msta = standards[month];
			if(msta == undefined){
				$.getJSON(ctx + "/perfStandard/getByMonth",{month:month},function(data){
					standards[month] = data;
					msta = data;
				})
			}
			
			var res = {mess:"",comm:0};
			if(gift < goal){
				res.mess = "未完成目标!";
			}
			else if(gift > goal){
				int count = 0;
				while(gift > 0){
					if(count == 0){
						res.comm += goal * msta.commLevelBaseProp / 100;
						res.mess = "(" + goal + " * " + msta.commLevelBaseProp + "%) ";
					}
					else {
						res.comm += goal * msta.commLevelInterval  / 100 * (msta.commLevelBaseProp + mstacommLevelPropInterval * count) / 100; 
						res.mess = "(" + (goal * msta.commLevelInterval  / 100) + " * " + (msta.commLevelBaseProp + mstacommLevelPropInterval * count) + "%) ";
					}

					gift -= goal + (goal * msta.commLevelInterval  / 100 * count );
					count++;
				}
				
			}
			
			return res;
		}
		
		function onGridDataLoadSuccess(data){
			
			var lastPid = null;
			var lastRow = null;
			var count = 0;
			var giftOkCount = 0;
			var duraOKCount = 0;
			var giftComm = 0;
			var brokers = [];
			for(var i in data){
				var row = data[i];
				if(row.giftEarning && row.giftEarningGoal){
					row.gflv = Math.round(row.giftEarning / row.giftEarningGoal * 10000) / 100;
				}
				if(row.liveDuration && row.liveDurationGoal){
					row.dulv = Math.round(row.liveDuration / row.liveDurationGoal * 10000) / 100;
				}
				//礼物提成
				var commRes = computeGiftComm(row.month,row.giftEarning,row.giftEarningGoal);
				row.comm = commRes.mess;
				
				row._parentId = row.brokerId + "-" + row.month;
				
				if(lastPid != row._parentId){	
					if(lastPid != null){
						brokers.push({
							id:lastPid,
							brokerId:lastRow.brokerId,
							brokerName:lastRow.brokerName,
							liverName:lastRow.brokerName,
							gflv:Math.round(giftOkCount / count * 10000) / 100,
							dulv:Math.round(duraOKCount / count * 10000) / 100,
							comm:giftComm
						})
					}
					
					lastPid != row._parentId
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
		
		function girfLastFormatter(value,row,index){
			if(row.giftEarning && row.giftEarningGoal){
				if(row.giftEarning < row.giftEarningGoal){
					return row.giftEarningGoal - row.giftEarning;
				}
			}
			return "";
		}
		
		function girfPercFormatter(value,row,index){
			if(row.giftEarning && row.giftEarningGoal){
				return Math.round(row.giftEarning / row.giftEarningGoal * 10000) / 100 + "%";
			}
			return "";
		}
		
		function duraPercFormatter(value,row,index){
			if(row.liveDuration && row.liveDurationGoal){
				return Math.round(row.liveDuration / row.liveDurationGoal * 10000) / 100 + "%";
			}
			return "";
		}
		
		function duraLastFormatter(value,row,index){
			if(row.liveDuration && row.liveDurationGoal){
				if(row.liveDuration < row.liveDurationGoal){
					return row.liveDurationGoal - row.liveDuration;
				}
			}
			return "";
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