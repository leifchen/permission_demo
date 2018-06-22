<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>权限</title>
    <jsp:include page="../../common/backend_common.jsp"/>
    <jsp:include page="../../common/page.jsp"/>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div class="page-header">
    <h1>
        权限模块管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            维护权限模块和权限点关系
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-3">
        <div class="table-header">
            权限模块列表&nbsp;&nbsp;
            <a class="green" href="#">
                <i class="ace-icon fa fa-plus-circle orange bigger-130 permissionModule-add"></i>
            </a>
        </div>
        <div id="permissionModuleList">
        </div>
    </div>
    <div class="col-sm-9">
        <div class="col-xs-12">
            <div class="table-header">
                权限点列表&nbsp;&nbsp;
                <a class="green" href="#">
                    <i class="ace-icon fa fa-plus-circle orange bigger-130 permission-add"></i>
                </a>
            </div>
            <div>
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <div class="row">
                        <div class="col-xs-6">
                            <div class="dataTables_length" id="dynamic-table_length"><label>
                                展示
                                <select id="pageSize" name="dynamic-table_length" aria-controls="dynamic-table"
                                        class="form-control input-sm">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select> 条记录 </label>
                            </div>
                        </div>
                    </div>
                    <table id="dynamic-table" class="table table-striped table-bordered table-hover dataTable no-footer"
                           role="grid"
                           aria-describedby="dynamic-table_info" style="font-size:14px">
                        <thead>
                        <tr role="row">
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                权限名称
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                权限模块
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                类型
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                URL
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                状态
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                顺序
                            </th>
                            <th class="sorting_disabled" rowspan="1" colspan="1" aria-label=""></th>
                        </tr>
                        </thead>
                        <tbody id="permissionList"></tbody>
                    </table>
                    <div class="row" id="permissionPage">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialog-permissionModule-form" style="display: none;">
    <form id="permissionModuleForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentId">上级模块</label></td>
                <td>
                    <select id="parentId" name="parentId" data-placeholder="选择模块" style="width: 200px;"></select>
                    <input type="hidden" name="id" id="permissionModuleId"/>
                </td>
            </tr>
            <tr>
                <td><label for="permissionModuleName">名称</label></td>
                <td><input type="text" name="name" id="permissionModuleName" value=""
                           class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="permissionModuleSeq">顺序</label></td>
                <td><input type="text" name="seq" id="permissionModuleSeq" value=""
                           class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="permissionModuleStatus">状态</label></td>
                <td>
                    <select id="permissionModuleStatus" name="status" data-placeholder="选择状态" style="width: 150px;">
                        <option value="1">有效</option>
                        <option value="0">无效</option>
                        <option value="2">删除</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="permissionModuleRemark">备注</label></td>
                <td><textarea name="remark" id="permissionModuleRemark" class="text ui-widget-content ui-corner-all"
                              rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<div id="dialog-permission-form" style="display: none;">
    <form id="permissionForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentId">所属权限模块</label></td>
                <td>
                    <select id="permissionModuleSelectId" name="permissionModuleId" data-placeholder="选择权限模块"
                            style="width: 200px;"></select>
                </td>
            </tr>
            <tr>
                <td><label for="permissionName">名称</label></td>
                <input type="hidden" name="id" id="permissionId"/>
                <td><input type="text" name="name" id="permissionName" value=""
                           class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="permissionType">类型</label></td>
                <td>
                    <select id="permissionType" name="type" data-placeholder="类型" style="width: 150px;">
                        <option value="1">菜单</option>
                        <option value="2">按钮</option>
                        <option value="3">其他</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="permissionUrl">URL</label></td>
                <td><input type="text" name="url" id="permissionUrl" value=""
                           class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="permissionStatus">状态</label></td>
                <td>
                    <select id="permissionStatus" name="status" data-placeholder="选择状态" style="width: 150px;">
                        <option value="1">有效</option>
                        <option value="0">无效</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="permissionSeq">顺序</label></td>
                <td><input type="text" name="seq" id="permissionSeq" value=""
                           class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="permissionRemark">备注</label></td>
                <td><textarea name="remark" id="permissionRemark" class="text ui-widget-content ui-corner-all" rows="3"
                              cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>

<script id="permissionModuleListTemplate" type="x-tmpl-mustache">
<ol class="dd-list ">
    {{#permissionModuleList}}
        <li class="dd-item dd2-item permissionModule-name {{displayClass}}" id="permissionModule_{{id}}" href="javascript:void(0)" data-id="{{id}}">
            <div class="dd2-content" style="cursor:pointer;">
            {{name}}
            &nbsp;
            <a class="green {{#showDownAngle}}{{/showDownAngle}}" href="#" data-id="{{id}}" >
                <i class="ace-icon fa fa-angle-double-down bigger-120 sub-permissionModule"></i>
            </a>
            <span style="float:right;">
                <a class="green permissionModule-edit" href="#" data-id="{{id}}" >
                    <i class="ace-icon fa fa-pencil bigger-100"></i>
                </a>
                &nbsp;
                <a class="red permissionModule-delete" href="#" data-id="{{id}}" data-name="{{name}}">
                    <i class="ace-icon fa fa-trash-o bigger-100"></i>
                </a>
            </span>
            </div>
        </li>
    {{/permissionModuleList}}
</ol>

</script>

<script id="permissionListTemplate" type="x-tmpl-mustache">
{{#permissionList}}
<tr role="row" class="permission-name odd" data-id="{{id}}"><!--even -->
    <td><a href="#" class="permission-edit" data-id="{{id}}">{{name}}</a></td>
    <td>{{showPermissionModuleName}}</td>
    <td>{{showType}}</td>
    <td>{{url}}</td>
    <td>{{#bold}}{{showStatus}}{{/bold}}</td>
    <td>{{seq}}</td>
    <td>
        <div class="hidden-sm hidden-xs action-buttons">
            <a class="green permission-edit" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-pencil bigger-100"></i>
            </a>
            <a class="red permission-role" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-flag bigger-100"></i>
            </a>
        </div>
    </td>
</tr>
{{/permissionList}}

</script>

<script type="text/javascript">
    $(function () {
        var permissionModuleList; //存储树形权限模块列表
        var permissionModuleMap = {}; //存储map格式权限模块信息
        var permissionMap = {}; //存储map格式的权限点信息
        var optionStr = '';
        var lastClickPermissionModuleId = -1; //最后点击的权限模块id

        var permissionModuleListTemplate = $('#permissionModuleListTemplate').html();
        Mustache.parse(permissionModuleListTemplate);
        var permissionListTemplate = $('#permissionListTemplate').html();
        Mustache.parse(permissionListTemplate);

        loadPermissionModuleTree();

        /**
         * 加载权限模块树
         */
        function loadPermissionModuleTree() {
            $.ajax({
                url: '/sys/permissionModule/tree.json',
                success: function (result) {
                    if (result.ret) {
                        permissionModuleList = result.data;
                        var rendered = Mustache.render(permissionModuleListTemplate, {
                            permissionModuleList: result.data,
                            'showDownAngle': function () {
                                return function (text, render) {
                                    return (this.permissionModuleList && this.permissionModuleList.length > 0) ? '' : 'hidden';
                                }
                            },
                            'displayClass': function () {
                                return '';
                            }
                        });
                        $('#permissionModuleList').html(rendered);
                        recursiveRenderPermissionModule(result.data);
                        bindPermissionModuleClick();
                    } else {
                        showMessage("加载权限模块", result.msg, false);
                    }
                }
            })
        }

        /**
         * 递归渲染权限模块列表
         * @param permissionModuleList
         */
        function recursiveRenderPermissionModule(permissionModuleList) {
            if (permissionModuleList && permissionModuleList.length > 0) {
                $(permissionModuleList).each(function (i, permissionModule) {
                    permissionModuleMap[permissionModule.id] = permissionModule;
                    if (permissionModule.permissionModuleList && permissionModule.permissionModuleList.length > 0) {
                        var rendered = Mustache.render(permissionModuleListTemplate, {
                            permissionModuleList: permissionModule.permissionModuleList,
                            'showDownAngle': function () {
                                return function (text, render) {
                                    return (this.permissionModuleList && this.permissionModuleList.length > 0) ? '' : 'hidden';
                                }
                            },
                            'displayClass': function () {
                                return 'hidden';
                            }
                        });
                        $('#permissionModule_' + permissionModule.id).append(rendered);
                        recursiveRenderPermissionModule(permissionModule.permissionModuleList);
                    }
                });
            }
        }

        /**
         * 绑定权限模块点击事件
         */
        function bindPermissionModuleClick() {
            // 选择权限模块
            $('.permissionModule-name').click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var permissionModuleId = $(this).attr('data-id');
                handlePermissionModuleSelected(permissionModuleId);
            });

            // 编辑权限模块
            $('.permissionModule-edit').click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var permissionModuleId = $(this).attr('data-id');
                $('#dialog-permissionModule-form').dialog({
                    model: true,
                    title: '编辑权限模块',
                    open: function (event, ui) {
                        $('.ui-dialog-titlebar-close', $(this).parent()).hide();
                        optionStr = '<option value="0">-</option>';
                        recursiveRenderPermissionModuleSelect(permissionModuleList, 1);
                        $('#permissionModuleForm')[0].reset();
                        $('#parentId').html(optionStr);
                        $('#permissionModuleId').val(permissionModuleId);
                        var targetPermissionModule = permissionModuleMap[permissionModuleId];
                        if (targetPermissionModule) {
                            $('#parentId').val(targetPermissionModule.parentId);
                            $('#permissionModuleName').val(targetPermissionModule.name);
                            $('#permissionModuleSeq').val(targetPermissionModule.seq);
                            $('#permissionModuleRemark').val(targetPermissionModule.remark);
                            $('#permissionModuleStatus').val(targetPermissionModule.status);
                        }
                    },
                    buttons: {
                        '更新': function (e) {
                            e.preventDefault();
                            updatePermissionModule(false, function (data) {
                                $('#dialog-permissionModule-form').dialog('close');
                            }, function (data) {
                                showMessage('编辑权限模块', data.msg, false);
                            })
                        },
                        '取消': function () {
                            $('#dialog-permissionModule-form').dialog('close');
                        }
                    }
                });
            });

            // 删除权限模块
            $('.permissionModule-delete').click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var permissionModuleId = $(this).attr('data-id');
                var permissionModuleName = $(this).attr('data-name');
                if (confirm('确定要删除权限模块【' + permissionModuleName + '】吗?')) {
                    $.ajax({
                        url: '/sys/permissionModule/delete.json',
                        data: {
                            id: permissionModuleId
                        },
                        success: function (result) {
                            if (result.ret) {
                                showMessage('删除权限模块【' + permissionModuleName + '】', '操作成功', true);
                                loadPermissionModuleTree();
                            } else {
                                showMessage('删除权限模块【' + permissionModuleName + '】', result.msg, false);
                            }
                        }
                    });
                }
            });

            // 是否折叠
            $('.sub-permissionModule').click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                $(this).parent().parent().parent().children().children('.permissionModule-name').toggleClass('hidden');
                if ($(this).is('.fa-angle-double-down')) {
                    $(this).removeClass('fa-angle-double-down').addClass('fa-angle-double-up');
                } else {
                    $(this).removeClass('fa-angle-double-up').addClass('fa-angle-double-down');
                }
            });
        }

        /**
         * 选择权限模块后处理事件
         * @param permissionModuleId 权限模块id
         */
        function handlePermissionModuleSelected(permissionModuleId) {
            if (lastClickPermissionModuleId != -1) {
                var lastPermissionModule = $('#permissionModule_' + lastClickPermissionModuleId + ' .dd2-content:first');
                lastPermissionModule.removeClass('btn-yellow');
                lastPermissionModule.removeClass('no-hover');
            }
            var currentPermissionModule = $('#permissionModule_' + permissionModuleId + ' .dd2-content:first');
            currentPermissionModule.addClass('btn-yellow');
            currentPermissionModule.addClass('no-hover');
            lastClickPermissionModuleId = permissionModuleId;
            loadPermissionList(permissionModuleId);
        }

        // 新增权限模块
        $('.permissionModule-add').click(function () {
            $('#dialog-permissionModule-form').dialog({
                model: true,
                title: '新增权限模块',
                open: function (event, ui) {
                    $('.ui-dialog-titlebar-close', $(this).parent()).hide();
                    optionStr = '<option value="0">-</option>';
                    recursiveRenderPermissionModuleSelect(permissionModuleList, 1);
                    $('#permissionModuleForm')[0].reset();
                    $('#parentId').html(optionStr);
                },
                buttons: {
                    '添加': function (e) {
                        e.preventDefault();
                        updatePermissionModule(true, function (data) {
                            $('#dialog-permissionModule-form').dialog('close');
                        }, function (data) {
                            showMessage('新增权限模块', data.msg, false);
                        })
                    },
                    '取消': function () {
                        $('#dialog-permissionModule-form').dialog('close');
                    }
                }
            });
        });

        /**
         * 递归渲染权限模块下拉选择框
         * @param permissionModuleList 权限模块列表
         * @param level 层级
         */
        function recursiveRenderPermissionModuleSelect(permissionModuleList, level) {
            level = level | 0;
            if (permissionModuleList && permissionModuleList.length > 0) {
                $(permissionModuleList).each(function (i, permissionModule) {
                    permissionModuleMap[permissionModule.id] = permissionModule;
                    var blank = '';
                    if (level > 1) {
                        for (var j = 3; j <= level; j++) {
                            blank += '..';
                        }
                        blank += '∟';
                    }
                    optionStr += Mustache.render('<option value="{{id}}">{{name}}</option>', {
                        id: permissionModule.id,
                        name: blank + permissionModule.name
                    });
                    if (permissionModule.permissionModuleList && permissionModule.permissionModuleList.length > 0) {
                        recursiveRenderPermissionModuleSelect(permissionModule.permissionModuleList, level + 1);
                    }
                });
            }
        }

        /**
         * 更新权限模块
         * @param isCreate 是否新增
         * @param successCallback 成功回调函数
         * @param failCallback 失败回调函数
         */
        function updatePermissionModule(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? '/sys/permissionModule/save.json' : '/sys/permissionModule/update.json',
                data: $('#permissionModuleForm').serializeArray(),
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        loadPermissionModuleTree();
                        if (successCallback) {
                            successCallback(result);
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
            })
        }

        /**
         * 加载权限列表
         * @param permissionModuleId 权限模块id
         */
        function loadPermissionList(permissionModuleId) {
            var url = '/sys/permission/page.json?permissionModuleId=' + permissionModuleId;
            var pageSize = $('#pageSize').val();
            var pageNo = $('#permissionPage .pageNo').val() || 1;
            $.ajax({
                url: url,
                data: {
                    pageSize: pageSize,
                    pageNo: pageNo
                },
                success: function (result) {
                    renderPermissionListAndPage(result, url);
                }
            })
        }

        /**
         * 渲染权限列表和分页信息
         * @param result
         * @param url
         */
        function renderPermissionListAndPage(result, url) {
            if (result.ret) {
                if (result.data.total > 0) {
                    var rendered = Mustache.render(permissionListTemplate, {
                        permissionList: result.data.data,
                        'showPermissionModuleName': function () {
                            return permissionModuleMap[this.permissionModuleId].name;
                        },
                        'showStatus': function () {
                            return this.status == 1 ? '有效' : '无效';
                        },
                        'showType': function () {
                            return this.type == 1 ? '菜单' : (this.type == 2 ? '按钮' : '其他');
                        },
                        'bold': function () {
                            return function (text, render) {
                                var status = render(text);
                                if (status == '有效') {
                                    return '<span class="label label-sm label-success">有效</span>';
                                } else if (status == '无效') {
                                    return '<span class="label label-sm label-warning">无效</span>';
                                } else {
                                    return '<span class="label">删除</span>';
                                }
                            }
                        }
                    });
                    $('#permissionList').html(rendered);
                    bindPermissionClick();
                    $.each(result.data.data, function (i, permission) {
                        permissionMap[permission.id] = permission;
                    })
                } else {
                    $('#permissionList').html('');
                }
                var pageSize = $('#pageSize').val();
                var pageNo = $('#permissionPage .pageNo').val() || 1;
                renderPage(url, result.data.total, pageNo, pageSize, result.data.total > 0 ? result.data.data.length : 0, "permissionPage", renderPermissionListAndPage);
            } else {
                showMessage('获取权限点列表', result.msg, false);
            }
        }

        // 新增权限
        $('.permission-add').click(function () {
            $('#dialog-permission-form').dialog({
                model: true,
                title: '新增权限',
                open: function (event, ui) {
                    $('.ui-dialog-titlebar-close', $(this).parent()).hide();
                    optionStr = '';
                    recursiveRenderPermissionModuleSelect(permissionModuleList, 1);
                    $('#permissionForm')[0].reset();
                    $('#permissionModuleSelectId').html(optionStr);
                },
                buttons: {
                    '添加': function (e) {
                        e.preventDefault();
                        updatePermission(true, function (data) {
                            $("#dialog-permission-form").dialog('close');
                        }, function (data) {
                            showMessage('新增权限', data.msg, false);
                        })
                    },
                    '取消': function () {
                        $('#dialog-permission-form').dialog('close');
                    }
                }
            });
        });

        /**
         * 更新权限
         * @param isCreate 是否新增
         * @param successCallback 成功回调函数
         * @param failCallback 失败回调函数
         */
        function updatePermission(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? '/sys/permission/save.json' : '/sys/permission/update.json',
                data: $('#permissionForm').serializeArray(),
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        loadPermissionList(lastClickPermissionModuleId);
                        if (successCallback) {
                            successCallback(result);
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
            })
        }

        /**
         * 绑定权限点击事件
         */
        function bindPermissionClick() {
            $('.permission-edit').click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var permissionId = $(this).attr('data-id');
                $('#dialog-permission-form').dialog({
                    model: true,
                    title: '编辑权限',
                    open: function (event, ui) {
                        $('.ui-dialog-titlebar-close', $(this).parent()).hide();
                        optionStr = '';
                        recursiveRenderPermissionModuleSelect(permissionModuleList, 1);
                        $('#permissionForm')[0].reset();
                        $('#permissionModuleSelectId').html(optionStr);
                        var targetPermission = permissionMap[permissionId];
                        if (targetPermission) {
                            $('#permissionId').val(permissionId);
                            $('#permissionModuleSelectId').val(targetPermission.permissionModuleId);
                            $('#permissionStatus').val(targetPermission.status);
                            $('#permissionType').val(targetPermission.type);
                            $('#permissionName').val(targetPermission.name);
                            $('#permissionUrl').val(targetPermission.url);
                            $('#permissionSeq').val(targetPermission.seq);
                            $('#permissionRemark').val(targetPermission.remark);
                        }
                    },
                    buttons: {
                        '更新': function (e) {
                            e.preventDefault();
                            updatePermission(false, function (data) {
                                $('#dialog-permission-form').dialog('close');
                            }, function (data) {
                                showMessage('编辑权限', data.msg, false);
                            })
                        },
                        '取消': function () {
                            $('#dialog-permission-form').dialog('close');
                        }
                    }
                });
            });

            $('.permission-role').click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var permissionId = $(this).attr('data-id');
                $.ajax({
                    url: '/sys/permission/permissions.json',
                    data: {
                        permissionId: permissionId
                    },
                    success: function (result) {
                        if (result.ret) {
                            console.log(result)
                        } else {
                            showMessage('获取权限点分配的用户和角色', result.msg, false);
                        }
                    }
                });
            });
        }
    });
</script>

</body>
</html>
