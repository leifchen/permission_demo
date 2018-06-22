<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>角色</title>
    <jsp:include page="../../common/backend_common.jsp"/>
    <link rel="stylesheet" href="/ztree/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="/assets/css/bootstrap-duallistbox.min.css" type="text/css">
    <script type="text/javascript" src="/ztree/jquery.ztree.all.min.js"></script>
    <script type="text/javascript" src="/assets/js/jquery.bootstrap-duallistbox.min.js"></script>
    <style type="text/css">
        .bootstrap-duallistbox-container .moveall, .bootstrap-duallistbox-container .removeall {
            width: 50%;
        }

        .bootstrap-duallistbox-container .move, .bootstrap-duallistbox-container .remove {
            width: 49%;
        }
    </style>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>
<div class="page-header">
    <h1>
        角色管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            维护角色与用户, 角色与权限关系
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-3">
        <div class="table-header">
            角色列表&nbsp;&nbsp;
            <a class="green" href="#">
                <i class="ace-icon fa fa-plus-circle orange bigger-130 role-add"></i>
            </a>
        </div>
        <div id="roleList"></div>
    </div>
    <div class="col-sm-9">
        <div class="tabbable" id="roleTab">
            <ul class="nav nav-tabs">
                <li class="active">
                    <a data-toggle="tab" href="#rolePermissionTab">
                        角色与权限
                    </a>
                </li>
                <li>
                    <a data-toggle="tab" href="#roleUserTab">
                        角色与用户
                    </a>
                </li>
            </ul>
            <div class="tab-content">
                <div id="rolePermissionTab" class="tab-pane fade in active">
                    <ul id="rolePermissionTree" class="ztree"></ul>
                    <button class="btn btn-info saveRolePermission" type="button">
                        <i class="ace-icon fa fa-check bigger-110"></i>
                        保存
                    </button>
                </div>

                <div id="roleUserTab" class="tab-pane fade">
                    <div class="row">
                        <div class="box1 col-md-6">待选用户列表</div>
                        <div class="box1 col-md-6">已选用户列表</div>
                    </div>
                    <select multiple="multiple" size="10" name="roleUserList" id="roleUserList">
                    </select>
                    <div class="hr hr-16 hr-dotted"></div>
                    <button class="btn btn-info saveRoleUser" type="button">
                        <i class="ace-icon fa fa-check bigger-110"></i>
                        保存
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialog-role-form" style="display: none;">
    <form id="roleForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td><label for="roleName">名称</label></td>
                <td>
                    <input type="text" name="name" id="roleName" value="" class="text ui-widget-content ui-corner-all">
                    <input type="hidden" name="id" id="roleId"/>
                </td>
            </tr>
            <tr>
                <td><label for="roleType">类型</label></td>
                <td>
                    <select id="roleType" name="type" data-placeholder="类型" style="width: 150px;">
                        <option value="1">管理员角色</option>
                        <option value="2">其他</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="roleStatus">状态</label></td>
                <td>
                    <select id="roleStatus" name="status" data-placeholder="状态" style="width: 150px;">
                        <option value="1">可用</option>
                        <option value="0">冻结</option>
                    </select>
                </td>
            </tr>
            <td><label for="roleRemark">备注</label></td>
            <td><textarea name="remark" id="roleRemark" class="text ui-widget-content ui-corner-all" rows="3"
                          cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<script id="roleListTemplate" type="x-tmpl-mustache">
<ol class="dd-list ">
    {{#roleList}}
        <li class="dd-item dd2-item role-name" id="role_{{id}}" href="javascript:void(0)" data-id="{{id}}">
            <div class="dd2-content" style="cursor:pointer;">
            {{name}}
            <span style="float:right;">
                <a class="green role-edit" href="#" data-id="{{id}}" >
                    <i class="ace-icon fa fa-pencil bigger-100"></i>
                </a>
                &nbsp;
                <a class="red role-delete" href="#" data-id="{{id}}" data-name="{{name}}">
                    <i class="ace-icon fa fa-trash-o bigger-100"></i>
                </a>
            </span>
            </div>
        </li>
    {{/roleList}}
</ol>
</script>

<script id="selectedUsersTemplate" type="x-tmpl-mustache">
{{#userList}}
    <option value="{{id}}" selected="selected">{{username}}</option>
{{/userList}}
</script>

<script id="unSelectedUsersTemplate" type="x-tmpl-mustache">
{{#userList}}
    <option value="{{id}}">{{username}}</option>
{{/userList}}
</script>

<script type="text/javascript">
    $(function () {
        var roleMap = {}; //存储map格式的角色信息
        var lastRoleId = -1; //最后点击的角色id
        var selectFirstTab = true; //默认选中第一个Tab标签
        var hasMultiSelect = false; //是否多选

        var roleListTemplate = $('#roleListTemplate').html();
        Mustache.parse(roleListTemplate);
        var selectedUsersTemplate = $('#selectedUsersTemplate').html();
        Mustache.parse(selectedUsersTemplate);
        var unSelectedUsersTemplate = $('#unSelectedUsersTemplate').html();
        Mustache.parse(unSelectedUsersTemplate);

        loadRoleList();

        /**
         * 加载角色列表
         */
        function loadRoleList() {
            $.ajax({
                url: '/sys/role/list.json',
                success: function (result) {
                    if (result.ret) {
                        var rendered = Mustache.render(roleListTemplate, {roleList: result.data});
                        $('#roleList').html(rendered);
                        bindRoleClick();
                        $.each(result.data, function (i, role) {
                            roleMap[role.id] = role;
                        });
                    } else {
                        showMessage('加载角色列表', result.msg, false);
                    }
                }
            });
        }

        /**
         * 绑定角色的点击事件
         */
        function bindRoleClick() {
            $('.role-edit').click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var roleId = $(this).attr('data-id');
                $('#dialog-role-form').dialog({
                    model: true,
                    title: '修改角色',
                    open: function (event, ui) {
                        $('.ui-dialog-titlebar-close', $(this).parent()).hide();
                        $('#roleForm')[0].reset();
                        var targetRole = roleMap[roleId];
                        if (targetRole) {
                            $('#roleId').val(roleId);
                            $('#roleName').val(targetRole.name);
                            $('#roleType').val(targetRole.type);
                            $('#roleStatus').val(targetRole.status);
                            $('#roleRemark').val(targetRole.remark);
                        }
                    },
                    buttons: {
                        '修改': function (e) {
                            e.preventDefault();
                            updateRole(false, function (data) {
                                $('#dialog-role-form').dialog('close');
                            }, function (data) {
                                showMessage('修改角色', data.msg, false);
                            })
                        },
                        '取消': function () {
                            $('#dialog-role-form').dialog('close');
                        }
                    }
                })
            });

            // 选中角色事件
            $('.role-name').click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var roleId = $(this).attr('data-id');
                handleRoleSelected(roleId);
            });
        }

        /**
         * 选择角色后处理事件
         */
        function handleRoleSelected(roleId) {
            if (lastRoleId != -1) {
                var lastRole = $('#role_' + lastRoleId + ' .dd2-content:first');
                lastRole.removeClass('btn-yellow');
                lastRole.removeClass('no-hover');
            }
            var currentRole = $('#role_' + roleId + ' .dd2-content:first');
            currentRole.addClass('btn-yellow');
            currentRole.addClass('no-hover');
            lastRoleId = roleId;

            $('#roleTab a:first').trigger('click');
            if (selectFirstTab) {
                loadRolePermission(roleId);
            }
        }

        /**
         * 加载角色-权限列表
         * @Param selectedRoleId 角色id
         */
        function loadRolePermission(selectedRoleId) {
            if (selectedRoleId == -1) {
                return;
            }

            $.ajax({
                url: '/sys/role/roleTree.json',
                data: {
                    roleId: selectedRoleId
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        renderRoleTree(result.data);
                    } else {
                        showMessage('加载角色权限数据', result.msg, false);
                    }
                }
            });
        }

        /**
         * 渲染角色树
         * @Param permissionModuleList 权限模块列表
         */
        function renderRoleTree(permissionModuleList) {
            zTreeObj = [];
            recursivePrepareTreeData(permissionModuleList);
            for (var key in nodeMap) {
                zTreeObj.push(nodeMap[key]);
            }
            $.fn.zTree.init($('#rolePermissionTree'), setting, zTreeObj);
        }

        /**
         * 递归准备树结构的数据
         */
        function recursivePrepareTreeData(permissionModuleList) {
            // prepare nodeMap
            if (permissionModuleList && permissionModuleList.length > 0) {
                $(permissionModuleList).each(function (i, permissionModule) {
                    var hasChecked = false;
                    if (permissionModule.permissionDTOList && permissionModule.permissionDTOList.length > 0) {
                        $(permissionModule.permissionDTOList).each(function (i, permission) {
                            zTreeObj.push({
                                id: permissionPrefix + permission.id,
                                pId: modulePrefix + permission.permissionModuleId,
                                name: permission.name + ((permission.type == 1) ? '(菜单)' : ''),
                                chkDisabled: !permission.hasPermission,
                                checked: permission.checked,
                                dataId: permission.id
                            });
                            if (permission.checked) {
                                hasChecked = true;
                            }
                        });
                    }
                    if ((permissionModule.permissionModuleList && permissionModule.permissionModuleList.length > 0) ||
                        (permissionModule.permissionDTOList && permissionModule.permissionDTOList.length > 0)) {
                        nodeMap[modulePrefix + permissionModule.id] = {
                            id: modulePrefix + permissionModule.id,
                            pId: modulePrefix + permissionModule.parentId,
                            name: permissionModule.name,
                            open: hasChecked
                        };
                        var tempPermissionModule = nodeMap[modulePrefix + permissionModule.id];
                        while (hasChecked && tempPermissionModule) {
                            if (tempPermissionModule) {
                                nodeMap[tempPermissionModule.id] = {
                                    id: tempPermissionModule.id,
                                    pId: tempPermissionModule.pId,
                                    name: tempPermissionModule.name,
                                    open: true
                                }
                            }
                            tempPermissionModule = nodeMap[tempPermissionModule.pId];
                        }
                    }
                    recursivePrepareTreeData(permissionModule.permissionModuleList);
                });
            }
        }

        // 新增角色
        $('.role-add').click(function () {
            $('#dialog-role-form').dialog({
                model: true,
                title: '新增角色',
                open: function (event, ui) {
                    $('.ui-dialog-titlebar-close', $(this).parent()).hide();
                    $('#roleForm')[0].reset();
                },
                buttons: {
                    '添加': function (e) {
                        e.preventDefault();
                        updateRole(true, function (data) {
                            $('#dialog-role-form').dialog('close');
                        }, function (data) {
                            showMessage('新增角色', data.msg, false);
                        })
                    },
                    '取消': function () {
                        $('#dialog-role-form').dialog('close');
                    }
                }
            });
        });

        /**
         * 更新角色
         * @param isCreate 是否新增
         * @param successCallback 成功的回调函数
         * @param failCallback 失败的回调函数
         */
        function updateRole(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? '/sys/role/save.json' : '/sys/role/update.json',
                data: $('#roleForm').serializeArray(),
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        loadRoleList();
                        if (successCallback) {
                            successCallback(result);
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
            });
        }

        // zTree
        <!-- 树结构相关 开始 -->
        var zTreeObj = [];
        var modulePrefix = 'm_';
        var permissionPrefix = 'p_';
        var nodeMap = {};

        var setting = {
            check: {
                enable: true,
                chkDisabledInherit: true,
                chkboxType: {'Y': 'ps', 'N': 'ps'}, //auto check 父节点 子节点
                autoCheckTrigger: true
            },
            data: {
                simpleData: {
                    enable: true,
                    rootPId: 0
                }
            },
            callback: {
                onClick: onClickTreeNode
            }
        };

        /**
         * 树节点的点击事件
         */
        function onClickTreeNode(e, treeId, treeNode) { // 绑定单击事件
            var zTree = $.fn.zTree.getZTreeObj('rolePermissionTree');
            zTree.expandNode(treeNode);
        }

        // 保存角色-权限点的关系
        $('.saveRolePermission').click(function (e) {
            e.preventDefault();
            if (lastRoleId == -1) {
                showMessage('保存角色与权限点的关系', '请先在左侧选择需要操作的角色', false);
                return;
            }

            $.ajax({
                url: '/sys/role/changePermissions.json',
                data: {
                    roleId: lastRoleId,
                    permissionIds: getTreeSelectedId()
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        showMessage('保存角色与权限点的关系', '操作成功', false);
                    } else {
                        showMessage('保存角色与权限点的关系', result.msg, false);
                    }
                }
            });
        });

        // 角色-用户页签
        $('#roleTab a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            if (lastRoleId == -1) {
                showMessage('加载角色关系', '请先在左侧选择操作的角色', false);
                return;
            }

            if (e.target.getAttribute('href') == '#rolePermissionTab') {
                selectFirstTab = true;
                loadRolePermission(lastRoleId);
            } else {
                selectFirstTab = false;
                loadRoleUser(lastRoleId);
            }
        });

        /**
         * 加载角色-用户列表
         * @param selectedRoleId 选中角色id
         */
        function loadRoleUser(selectedRoleId) {
            $.ajax({
                url: '/sys/role/users.json',
                data: {
                    roleId: selectedRoleId
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        var renderedSelect = Mustache.render(selectedUsersTemplate, {userList: result.data.selected});
                        var renderedUnSelect = Mustache.render(unSelectedUsersTemplate, {userList: result.data.unselected});
                        $('#roleUserList').html(renderedSelect + renderedUnSelect);

                        if (!hasMultiSelect) {
                            $('select[name="roleUserList"]').bootstrapDualListbox({
                                showFilterInputs: false,
                                moveOnSelect: false,
                                infoText: false
                            });
                            hasMultiSelect = true;
                        } else {
                            $('select[name="roleUserList"]').bootstrapDualListbox('refresh', true);
                        }
                    } else {
                        showMessage('加载角色用户数据', result.msg, false);
                    }
                }
            });
        }

        // 保存角色-用户的关系
        $('.saveRoleUser').click(function (e) {
            e.preventDefault();
            if (lastRoleId == -1) {
                showMessage('保存角色与用户的关系', '请先在左侧选择需要操作的角色', false);
                return;
            }

            $.ajax({
                url: '/sys/role/changeUsers.json',
                data: {
                    roleId: lastRoleId,
                    userIds: $('#roleUserList').val() ? $('#roleUserList').val().join(',') : ''
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        showMessage('保存角色与用户的关系', '操作成功', false);
                    } else {
                        showMessage('保存角色与用户的关系', result.msg, false);
                    }
                }
            });
        });

        /**
         * 获取选中id的树结构
         */
        function getTreeSelectedId() {
            var treeObj = $.fn.zTree.getZTreeObj('rolePermissionTree');
            var nodes = treeObj.getCheckedNodes(true);
            var v = '';
            for (var i = 0; i < nodes.length; i++) {
                if (nodes[i].id.startsWith(permissionPrefix)) {
                    v += ',' + nodes[i].dataId;
                }
            }
            return v.length > 0 ? v.substring(1) : v;
        }
    });
</script>
</body>
</html>
