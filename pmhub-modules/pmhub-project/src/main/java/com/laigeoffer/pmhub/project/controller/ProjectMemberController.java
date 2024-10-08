package com.laigeoffer.pmhub.project.controller;

import com.laigeoffer.pmhub.base.core.core.domain.AjaxResult;
import com.laigeoffer.pmhub.base.security.annotation.RequiresPermissions;
import com.laigeoffer.pmhub.project.domain.vo.project.ProjectVO;
import com.laigeoffer.pmhub.project.domain.vo.project.member.ProjectMemberReqVO;
import com.laigeoffer.pmhub.project.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zw
 * @date 2022-12-09 17:13
 */
@RestController
@RequestMapping("/project")
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    /**
     * 添加成员
     * @param projectVO
     * @return
     */
    @RequiresPermissions("project:member:inviteMemberList")
    @PostMapping("/inviteMemberList")
    public AjaxResult inviteMemberList(@RequestBody ProjectVO projectVO) {
        projectMemberService.inviteMemberList(projectVO);
        return AjaxResult.success();
    }

    /**
     * 移除成员
     * @param projectVO
     * @return
     */
    @RequiresPermissions("project:member:removeMemberList")
    @PostMapping("/removeMemberList")
    public AjaxResult removeMemberList(@RequestBody ProjectVO projectVO) {
        projectMemberService.removeMemberList(projectVO);
        return AjaxResult.success();
    }

    /**
     * 搜索成员
     * @param projectMemberReqVO
     * @return
     */
    @RequiresPermissions("project:member:list")
    @PostMapping("/member/list")
    public AjaxResult searchMember(@RequestBody ProjectMemberReqVO projectMemberReqVO) {
        return AjaxResult.success(projectMemberService.searchMember(projectMemberReqVO));
    }

    /**
     * 获取用户列表
     * @param projectMemberVO
     * @return
     */
    @RequiresPermissions("project:member:queryUserList")
    @PostMapping("/queryUserList")
    public AjaxResult queryUser(@RequestBody ProjectMemberReqVO projectMemberVO) {
        return AjaxResult.success(projectMemberService.queryUserList(projectMemberVO.getProjectId(), projectMemberVO.getKeyword()));
    }
    /**
     * 获取用户列表
     * @param projectMemberVO
     * @return
     */
    @RequiresPermissions("project:member:queryUserListById")
    @PostMapping("/member/queryUserListById")
    public AjaxResult queryUserListById(@RequestBody ProjectMemberReqVO projectMemberVO) {
        return AjaxResult.success(projectMemberService.queryUserListById(projectMemberVO.getProjectId()));
    }
}
