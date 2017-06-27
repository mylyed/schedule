<html>
<head>

    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/1.10.7/css/jquery.dataTables.css"/>
    <script type="text/javascript" src="//code.jquery.com/jquery-1.10.2.min.js"></script>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <title>调度任务管理</title>

</head>
<body>

<div id="container" v-cloak>
    <div v-show="showList">
        <div class="btn-group">
            <button id="add" @click="add">新增</button>
            <button id="update" @click="update">修改</button>
            <button id="delete" @click="deleteJob">删除</button>
            <button id="resume" @click="resume">恢复任务</button>
            <button id="pause" @click="pause">暂停任务</button>
            <button id="trigger" @click="trigger">立即运行</button>
        </div>
        <table id="table" class="display" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>
                    全选<input type="checkbox" class="checkall"/>
                </th>
                <th>任务id</th>
                <th>类名称</th>
                <th>方法名</th>
                <th>参数</th>
                <th>cron表达式</th>
                <th>任务状态</th>
                <th>备注</th>
                <th>创建时间</th>
            </tr>
            </thead>
        </table>
    </div>

    <div v-show="!showList" class="panel panel-default">
        <div class="panel-heading">{{title}}</div>
        <form class="form-horizontal">
            <div class="form-group">
                <div class="col-sm-2 control-label">类名称</div>
                <div class="col-sm-10">
                    <input type="text" class="form-control" v-model="schedule.className"
                           placeholder="spring bean名称，如：com.xx.xx.ClassName"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">方法名称</div>
                <div class="col-sm-10">
                    <input type="text" class="form-control" v-model="schedule.methodName" placeholder="方法名称"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">参数</div>
                <div class="col-sm-10">
                    <input type="text" class="form-control" v-model="schedule.params" placeholder="参数"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">cron表达式</div>
                <div class="col-sm-10">
                    <input type="text" class="form-control" v-model="schedule.cronExpression"
                           placeholder="如：0 0 12 * * ?"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">备注</div>
                <div class="col-sm-10">
                    <input type="text" class="form-control" v-model="schedule.remark" placeholder="备注"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label"></div>
                <input type="button" class="btn btn-primary" @click="saveOrUpdate" value="确定"/>
                &nbsp;&nbsp;<input type="button" class="btn btn-warning" @click="reload" value="返回"/>
            </div>
        </form>
    </div>
</div>


<script type="text/javascript" src="//cdn.datatables.net/1.10.7/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/lib/jquery.spring-friendly.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>
<script src="https://unpkg.com/vue/dist/vue.js"></script>
<script src="https://cdn.bootcss.com/layer/3.0.1/layer.min.js"></script>
<script>
    $(document).ready(function () {
                var table = $('#table').DataTable({
                    "serverSide": true,
                    "searching": false,
                    "deferRender": true,
                    "ajax": {
                        "dataType": 'json',
                        "url": "../schedule/jobs",
                        "type": 'POST'
                    },
                    "columns": [
                        {
                            "sClass": "text-center",
                            "data": "jobId",
                            "render": function (data, type, full, meta) {
                                return '<input type="checkbox"  class="checkchild" />';
                            },
                            "bSortable": false,
                            "orderable": false
                        },
                        {"data": "jobId", "orderable": false},
                        {"data": "className", "orderable": false},
                        {"data": "methodName", "orderable": false},
                        {"data": "params", "orderable": false},
                        {"data": "cronExpression", "orderable": false},
                        {
                            "data": "status",
                            "render": function (data, type, full) {
                                if (data == 'NORMAL') {
                                    return '正常';
                                } else {
                                    return '暂停';
                                }
                            }, "orderable": false
                        },
                        {"data": "remark", "orderable": false},
                        {"data": "createTime", "orderable": false}
                    ],
                    "language": {
                        "lengthMenu": "每页 _MENU_ 条记录",
                        "zeroRecords": " ",
                        "info": "当前 _START_ 条到 _END_ 条 共 _TOTAL_ 条",
                        "infoEmpty": "无记录",
                        "infoFiltered": "(从 _MAX_ 条记录过滤)",
                        "search": "搜索",
                        "processing": "载入中",
                        "paginate": {
                            "first": "首页",
                            "previous": "上一页",
                            "next": "下一页",
                            "last": "尾页"
                        }
                    }
                });

                $(".checkall").click(function () {
                    var check = $(this).prop("checked");
                    $(".checkchild").prop("checked", check);
                    $("#table tbody tr").toggleClass('selected', check);

                });

                $(".checkchild").click(function () {
                    var check = $(this).prop("checked");
                    $(this).parent().toggleClass('selected', check);
                });


                $('#table tbody').on('click', 'tr', function () {
                    $(this).toggleClass('selected');
                    var check = $(this).hasClass("selected");
                    $(this).find(".checkchild").prop("checked", check);
                });

                $('#table').on('page.dt', function () {
                    $(".checkall").prop("checked", false);
                });
            }
    );
    var vm = new Vue({
        el: '#container',
        data: {
            showList: true,
            title: null,
            schedule: {}
        },
        methods: {
            add: function () {
                vm.showList = false;
                vm.title = "新增";
                vm.schedule = {};
            },
            reload: function (event) {
                vm.showList = true;
                $('#table').DataTable().ajax.reload();
            },
            saveOrUpdate: function () {
                var url = "../schedule/save";
                $.ajax({
                    type: "POST",
                    url: url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.schedule),
                    success: function (r) {
                        if (r.success) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.message);
                        }
                    }
                });
            },
            update: function () {
                var data = getSelectedRow();
                vm.showList = false;
                vm.title = "修改";
                vm.schedule = data;
            },
            deleteJob: function () {
                var data = getSelectedRows();
                alert('确定要删除' + data.length + "条数据?", function (index) {
                    var url = "../schedule/delete";

                    var array = [];
                    for (var i = 0; i < data.length; i++) {
                        array.push(data[i])
                    }
                    $.ajax({
                        type: "POST",
                        url: url,
                        contentType: "application/json",
                        data: JSON.stringify(array),
                        success: function (r) {
                            if (r.success) {
                                alert('操作成功', function (index) {
                                    vm.reload();
                                });
                            } else {
                                alert(r.message);
                            }
                        }
                    });


                });

            },

            resume: function () {
                var data = getSelectedRows();
                var url = "../schedule/resume";
                var array = [];
                for (var i = 0; i < data.length; i++) {
                    array.push(data[i])
                }
                $.ajax({
                    type: "POST",
                    url: url,
                    contentType: "application/json",
                    data: JSON.stringify(array),
                    success: function (r) {
                        if (r.success) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.message);
                        }
                    }
                });

            },
            pause: function () {
                var data = getSelectedRows();
                var url = "../schedule/pause";
                var array = [];
                for (var i = 0; i < data.length; i++) {
                    array.push(data[i])
                }
                $.ajax({
                    type: "POST",
                    url: url,
                    contentType: "application/json",
                    data: JSON.stringify(array),
                    success: function (r) {
                        if (r.success) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.message);
                        }
                    }
                });

            },
            trigger: function () {
                var data = getSelectedRow();
                var url = "../schedule/trigger";
                $.ajax({
                    type: "POST",
                    url: url,
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success: function (r) {
                        if (r.success) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.message);
                        }
                    }
                });
            }
        }
    });

    function getSelectedRow() {
        var data = $('#table').DataTable().rows('.selected').data();
        if (data.length != 1) {
            alert("请选择一条记录");
            return;
        }
        return data[0];
    }

    function getSelectedRows() {
        var data = $('#table').DataTable().rows('.selected').data();
        if (data.length == 0) {
            alert("请选择一条记录");
            return;
        }
        return data;
    }

    window.alert = function (msg, callback) {
        parent.layer.alert(msg, function (index) {
            parent.layer.close(index);
            if (typeof(callback) === "function") {
                callback("ok");
            }
        });
    }
</script>


</body>
</html>