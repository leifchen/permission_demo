package com.chen.service;

import com.chen.dao.SysDeptMapper;
import com.chen.dao.SysPermissionMapper;
import com.chen.dao.SysPermissionModuleMapper;
import com.chen.dto.DeptLevelDTO;
import com.chen.dto.PermissionDTO;
import com.chen.dto.PermissionModuleLevelDTO;
import com.chen.model.SysDept;
import com.chen.model.SysPermission;
import com.chen.model.SysPermissionModule;
import com.chen.util.LevelUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 树Service
 * @Author LeifChen
 * @Date 2018-04-18
 */
@Service
public class SysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysPermissionModuleMapper sysPermissionModuleMapper;
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysCoreService sysCoreService;

    /**
     * 部门树
     * @return
     */
    public List<DeptLevelDTO> deptTree() {
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDTO> dtoList = Lists.newArrayList();
        for (SysDept dept : deptList) {
            DeptLevelDTO dto = DeptLevelDTO.adapt(dept);
            dtoList.add(dto);
        }

        return deptListToTree(dtoList);
    }

    /**
     * 权限模块树
     * @return
     */
    public List<PermissionModuleLevelDTO> permissionModuleTree() {
        List<SysPermissionModule> permissionModuleList = sysPermissionModuleMapper.getAllPermissionModule();
        List<PermissionModuleLevelDTO> dtoList = Lists.newArrayList();
        for (SysPermissionModule permissionModule : permissionModuleList) {
            PermissionModuleLevelDTO dto = PermissionModuleLevelDTO.adapt(permissionModule);
            dtoList.add(dto);
        }

        return permissionModuleListToTree(dtoList);
    }

    /**
     * 角色树
     * @param roleId
     * @return
     */
    public List<PermissionModuleLevelDTO> roleTree(int roleId) {
        // 1.当前用户已分配的权限点
        List<SysPermission> userPermissionList = sysCoreService.getCurrentUserPermissionList();
        // 2.当前角色分配的权限点
        List<SysPermission> rolePermissionList = sysCoreService.getRolePermissionList(roleId);
        // 3.当前系统所有权限点
        List<PermissionDTO> permissionDTOList = Lists.newArrayList();

        Set<Integer> userPermissionSet = userPermissionList.stream().map(SysPermission::getId).collect(Collectors.toSet());
        Set<Integer> rolePermissionSet = rolePermissionList.stream().map(SysPermission::getId).collect(Collectors.toSet());

        List<SysPermission> allPermissionList = sysPermissionMapper.getAll();
        for (SysPermission permission : allPermissionList) {
            PermissionDTO dto = PermissionDTO.adapt(permission);
            if (userPermissionSet.contains(permission.getId())) {
                dto.setHasPermission(true);
            }
            if (rolePermissionSet.contains(permission.getId())) {
                dto.setChecked(true);
            }
            permissionDTOList.add(dto);
        }

        return permissionListToTree(permissionDTOList);
    }

    /**
     * 权限点树
     * @param userId
     * @return
     */
    public List<PermissionModuleLevelDTO> userPermissionTree(int userId) {
        List<SysPermission> userPermissionList = sysCoreService.getUserPermissionList(userId);
        List<PermissionDTO> permissionDTOList = Lists.newArrayList();
        for (SysPermission permission : userPermissionList) {
            PermissionDTO dto = PermissionDTO.adapt(permission);
            permissionDTOList.add(dto);
        }

        return permissionListToTree(permissionDTOList);
    }

    /**
     * 部门列表转换成树
     * @param deptLevelList 部门列表
     * @return
     */
    private List<DeptLevelDTO> deptListToTree(List<DeptLevelDTO> deptLevelList) {
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }
        // level -> [dept1,dept2,...]
        Multimap<String, DeptLevelDTO> levelDeptMap = ArrayListMultimap.create();
        List<DeptLevelDTO> rootList = Lists.newArrayList();
        for (DeptLevelDTO dto : deptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        // 按照seq从小到大排序
        rootList.sort(deptSeqComparator);
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);

        return rootList;
    }

    /**
     * 权限模块列表转换成树
     * @param permissionModuleLevelList 权限模块列表
     * @return
     */
    private List<PermissionModuleLevelDTO> permissionModuleListToTree(List<PermissionModuleLevelDTO> permissionModuleLevelList) {
        if (CollectionUtils.isEmpty(permissionModuleLevelList)) {
            return Lists.newArrayList();
        }
        // level -> [permissionModule1,permissionModule2,...]
        Multimap<String, PermissionModuleLevelDTO> levelPermissionModuleMap = ArrayListMultimap.create();
        List<PermissionModuleLevelDTO> rootList = Lists.newArrayList();
        for (PermissionModuleLevelDTO dto : permissionModuleLevelList) {
            levelPermissionModuleMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        // 按照seq从小到大排序
        rootList.sort(permissionModuleSeqComparator);
        transformPermissionModuleTree(rootList, LevelUtil.ROOT, levelPermissionModuleMap);

        return rootList;
    }

    /**
     * 权限点列表转换成树
     * @param permissionDTOList 权限点列表
     * @return
     */
    private List<PermissionModuleLevelDTO> permissionListToTree(List<PermissionDTO> permissionDTOList) {
        if (CollectionUtils.isEmpty(permissionDTOList)) {
            return Lists.newArrayList();
        }

        List<PermissionModuleLevelDTO> permissionModuleLevelList = permissionModuleTree();

        Multimap<Integer, PermissionDTO> moduleIdPermissionMap = ArrayListMultimap.create();
        for (PermissionDTO dto : permissionDTOList) {
            if (dto.getStatus() == 1) {
                moduleIdPermissionMap.put(dto.getPermissionModuleId(), dto);
            }
        }

        bindPermissionsWithOrder(permissionModuleLevelList, moduleIdPermissionMap);

        return permissionModuleLevelList;
    }

    /**
     * 递归生成部门树
     * @param deptLevelList 部门层级列表
     * @param level         层级
     * @param levelDeptMap  当前层级的部门列表map
     */
    private void transformDeptTree(List<DeptLevelDTO> deptLevelList, String level, Multimap<String, DeptLevelDTO> levelDeptMap) {
        // 遍历该层的每个元素
        for (DeptLevelDTO deptLevelDTO : deptLevelList) {
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDTO.getId());
            // 处理下一层
            List<DeptLevelDTO> tempDeptList = (List<DeptLevelDTO>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                tempDeptList.sort(deptSeqComparator);
                // 设置下一层部门
                deptLevelDTO.setDeptList(tempDeptList);
                // 进入到下一层处理
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }

    /**
     * 递归生成权限模块树
     * @param permissionModuleList  权限模块层级列表
     * @param level                 层级
     * @param levelPermissionModule 当前层级的权限模块map
     */
    private void transformPermissionModuleTree(List<PermissionModuleLevelDTO> permissionModuleList, String level, Multimap<String, PermissionModuleLevelDTO> levelPermissionModule) {
        // 遍历该层的每个元素
        for (PermissionModuleLevelDTO permissionModuleLevelDTO : permissionModuleList) {
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, permissionModuleLevelDTO.getId());
            // 处理下一层
            List<PermissionModuleLevelDTO> tempDeptList = (List<PermissionModuleLevelDTO>) levelPermissionModule.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                tempDeptList.sort(permissionModuleSeqComparator);
                // 设置下一层部门
                permissionModuleLevelDTO.setPermissionModuleList(tempDeptList);
                // 进入到下一层处理
                transformPermissionModuleTree(tempDeptList, nextLevel, levelPermissionModule);
            }
        }
    }

    /**
     * 按顺序绑定权限点到权限模块树上
     * @param permissionModuleLevelList
     * @param moduleIdPermissionMap
     */
    private void bindPermissionsWithOrder(List<PermissionModuleLevelDTO> permissionModuleLevelList, Multimap<Integer, PermissionDTO> moduleIdPermissionMap) {
        if (CollectionUtils.isEmpty(permissionModuleLevelList)) {
            return;
        }

        for (PermissionModuleLevelDTO dto : permissionModuleLevelList) {
            List<PermissionDTO> permissionDTOList = (List<PermissionDTO>) moduleIdPermissionMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(permissionDTOList)) {
                permissionDTOList.sort(permissionSeqComparator);
                dto.setPermissionDTOList(permissionDTOList);
            }
            bindPermissionsWithOrder(dto.getPermissionModuleList(), moduleIdPermissionMap);
        }
    }

    /**
     * 比较部门的展示顺序
     */
    private Comparator<DeptLevelDTO> deptSeqComparator = Comparator.comparing(DeptLevelDTO::getSeq);

    /**
     * 比较权限模块的展示顺序
     */
    private Comparator<PermissionModuleLevelDTO> permissionModuleSeqComparator = Comparator.comparing(PermissionModuleLevelDTO::getSeq);

    /**
     * 比较权限点的展示顺序
     */
    private Comparator<PermissionDTO> permissionSeqComparator = Comparator.comparing(PermissionDTO::getSeq);
}
