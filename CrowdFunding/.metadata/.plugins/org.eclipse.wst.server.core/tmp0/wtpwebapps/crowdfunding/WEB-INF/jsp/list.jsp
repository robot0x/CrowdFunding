<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>众筹系统</title>
<!-- 插件 -->
<link rel="stylesheet" href="static/loading/loading.css">
<link rel="stylesheet" href="static/pagination/pagination.css">
<link rel="stylesheet" href="static/bootstrap/bootstrap.min.css">
<script type="text/javascript" src="static/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="static/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript"
	src="static/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="static/validate/messages_zh.min.js"></script>
<script type="text/javascript" src="static/pagination/pagination.js"></script>
<script type="text/javascript" src="static/loading/loading.js"></script>
<!-- 代码 -->
<link rel="stylesheet" href="static/dist/list.css">
<script type="text/javascript" src="static/dist/list.js"></script>
</head>

<%
	int totalNum = (int) request.getAttribute("totalNum");
%>

<script type="text/javascript">
	$(function() {
		// 总记录
		var totalNum = $
		{
			totalNum
		}
		;
		$("#pagination").pagination(totalNum, {
			callback : pageCallback,
			prev_text : '<<<',
			next_text : '>>>',
			ellipse_text : '...',
			current_page : 0, // 当前选中的页面
			items_per_page : 5, // 每页显示的条目数
			num_display_entries : 4, // 连续分页主体部分显示的分页条目数
			num_edge_entries : 1
		// 两侧显示的首尾分页的条目数
		});

		function pageCallback(index, jq) {
			// 清空表格
			$("#table").empty();
			get(index);
		}

		function get(index) {
			$
					.ajax({
						type : "POST",
						dataType : "json",
						url : 'getFunds',
						data : {
							"pageIndex" : index
						},
						beforeSend : function() {
							// 显示loading
							loading();
						}, // beforeSend
						success : function(data) {
							// 后台返回结果为空
							if ($.isEmptyObject(data)) {
								$("#table").append(
										'<p class="text-danger">暂无众筹</p>');
							}
							// 后台返回结果非空
							else {
								var tab = "";

								// thead
								tab += '<thead><tr>';
								tab += '<th>编号</th>';
								tab += '<th>众筹地址</th>';
								tab += '<th>已筹人数</th>';
								tab += '<th>已筹金币</th>';
								tab += '<th>操作</th>';
								tab += '<tr></thead>';
								// tbody
								tab += '<tbody>';
								$
										.each(
												data,
												function(i, fund) {
													tab += '<tr>';
													tab += '<td>' + i + '</td>';
													tab += '<td>'
															+ fund['owner']
															+ '</td>';
													tab += '<td>'
															+ fund['number']
															+ '</td>';
													tab += '<td>'
															+ fund['coin']
															+ '</td>';
													tab += '<td>'
															+ '<button type="button" class="btn btn-primary btn-sm"'
															+ 'data-toggle="modal" data-target="#myModal"'
															+ 'onclick="select('
															+ "'"
															+ fund['owner']
															+ "'"
															+ ')">我要捐赠</button>'
															+ '</td>';
													tab += '</tr>';
												});
								tab += '</tbody>';

								$("#table").append(tab);
							}
						}, // success
						complete : function() {
							// 隐藏loading
							removeLoading('loading');
						} // complete
					});
		}
	});
</script>

<body>
	<div class="container">
		<div class="jumbotron">

			<h2 class="text-muted">众筹列表</h2>

			<!-- 表格 -->
			<table id="table" class="table"></table>
			<!-- 加载 -->
			<div id="loading"></div>
			<!-- 分页 -->
			<div id="pagination"></div>

			<button type="button" class="btn btn-success btn-lg"
				data-toggle="modal" data-target="#myModal2">我要众筹</button>
		</div>
	</div>

	<!-- 模态框1（Modal） -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">发送金币</h4>
				</div>

				<div class="modal-body">
					<form class="form-horizontal" role="form" id="myForm">
						<div class="form-group">
							<label for="owner" class="col-sm-2 control-label">地址</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="owner" name="owner"
									placeholder="0x..." readOnly />
							</div>
						</div>

						<div class="form-group">
							<label for="coin" class="col-sm-2 control-label">金币</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="coin" name="coin" />
							</div>
						</div>

						<div class="form-group">
							<label for="password" class="col-sm-2 control-label">密码</label>
							<div class="col-sm-8">
								<input type="password" class="form-control" id="password"
									name="password" />
							</div>
						</div>

						<div class="form-group">
							<label for="file" class="col-sm-2 control-label">钱包</label>
							<div class="col-sm-8">
								<input type="file" id="file" name="file" />
							</div>
						</div>
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="confirm">确认</button>
					<span id="tip"> </span>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

	<!-- 模态框2（Modal） -->
	<div class="modal fade" id="myModal2" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel2" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel2">发起众筹</h4>
				</div>

				<div class="modal-body">
					<form class="form-horizontal" role="form" id="myForm2">
						<div class="form-group">
							<label for="owner2" class="col-sm-2 control-label">地址</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="owner2"
									name="owner2" />
							</div>
						</div>
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="confirm2">确认</button>
					<span id="tip2"> </span>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</body>
</html>