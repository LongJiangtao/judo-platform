package com.github.judo.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.judo.admin.common.util.TreeUtil;
import com.github.judo.admin.mapper.SysDeptMapper;
import com.github.judo.admin.mapper.SysDeptRelationMapper;
import com.github.judo.admin.model.dto.DeptTree;
import com.github.judo.admin.model.entity.SysDept;
import com.github.judo.admin.model.entity.SysDeptRelation;
import com.github.judo.admin.service.SysDeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xiangjunzhong@qq.com
 * @Description: 部门管理 服务实现类
 * @Version: 1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysDeptRelationMapper sysDeptRelationMapper;

    /**
     * 添加信息部门
     *
     * @param dept 部门
     * @return
     */
    @Override
    @Transactional
    public Boolean insertDept(SysDept dept) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept, sysDept);
        this.insert(sysDept);
        this.insertDeptRelation(sysDept);
        return Boolean.TRUE;
    }

    /**
     * 维护部门关系
     *
     * @param sysDept 部门
     */
    private void insertDeptRelation(SysDept sysDept) {
        //增加部门关系表
        SysDeptRelation deptRelation = new SysDeptRelation();
        deptRelation.setDescendant(sysDept.getParentId());
        List<SysDeptRelation> deptRelationList = sysDeptRelationMapper.selectList(new EntityWrapper<>(deptRelation));
        for (SysDeptRelation sysDeptRelation : deptRelationList) {
            sysDeptRelation.setDescendant(sysDept.getDeptId());
            sysDeptRelationMapper.insert(sysDeptRelation);
        }
        //自己也要维护到关系表中
        SysDeptRelation own = new SysDeptRelation();
        own.setDescendant(sysDept.getDeptId());
        own.setAncestor(sysDept.getDeptId());
        sysDeptRelationMapper.insert(own);
    }

    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return 成功、失败
     */
    @Override
    @Transactional
    public Boolean deleteDeptById(Integer id) {
        SysDept sysDept = new SysDept();
        sysDept.setDeptId(id);
        this.deleteById(sysDept);
        sysDeptRelationMapper.deleteAllDeptRealtion(id);
        return Boolean.TRUE;
    }

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     * @return 成功、失败
     */
    @Override
    @Transactional
    public Boolean updateDeptById(SysDept sysDept) {
        //更新部门状态
        this.updateById(sysDept);
        //更新部门关系
        SysDeptRelation relation = new SysDeptRelation();
        relation.setAncestor(sysDept.getParentId());
        relation.setDescendant(sysDept.getDeptId());
        sysDeptRelationMapper.updateDeptRealtion(relation);
        return null;
    }

    /**
     * 查询部门树
     *
     * @param sysDeptEntityWrapper
     * @return 树
     */
    @Override
    public List<DeptTree> selectListTree(EntityWrapper<SysDept> sysDeptEntityWrapper) {
        return getDeptTree(this.selectList(sysDeptEntityWrapper), 0);
    }

    /**
     * 构建部门树
     *
     * @param depts 部门
     * @param root  根节点
     * @return
     */
    private List<DeptTree> getDeptTree(List<SysDept> depts, int root) {
        List<DeptTree> trees = new ArrayList<>();
        DeptTree node;
        for (SysDept dept : depts) {
            if (dept.getParentId().equals(dept.getDeptId())) {
                continue;
            }
            node = new DeptTree();
            node.setId(dept.getDeptId());
            node.setParentId(dept.getParentId());
            node.setName(dept.getName());
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root);
    }
}
