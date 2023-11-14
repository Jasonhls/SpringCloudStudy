package com.cloud.flowable.controller;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 何立森
 * @Date: 2023/10/25/10:29
 * @Description:
 */
@RestController
@RequestMapping("/askForLeave")
@Slf4j
public class AskForLeaveController {
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;

    @PostMapping(value = "/startProcess")
    public void askForLeave(String staffId) {
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("leaveTask", staffId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ask_for_leave", map);
        runtimeService.setVariable(processInstance.getId(), "name", "javaboy");
        runtimeService.setVariable(processInstance.getId(), "reason", "休息一下");
        runtimeService.setVariable(processInstance.getId(), "days", 10);
        log.info("创建请假流程 processId:{}", processInstance.getId());
    }

    @PostMapping(value = "/submitToZuZhang")
    public void submitToZuZhang(String staffId, String zuZhangId) {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(staffId)
                .orderByTaskId()
                .desc()
                .list();
        for (Task task : list) {
            log.info("任务ID：{}，任务处理人：{}，任务是否挂起：{}", task.getId(), task.getAssignee(), task.isSuspended());
            Map<String, Object> map = new HashMap<>();
            //提交给组长的时候，需要指定组长的id，这里的key是流程中组长这个节点的id，即zuzhangTask
            map.put("zuzhangTask", zuZhangId);
            taskService.complete(task.getId(), map);
        }
    }

    @PostMapping(value = "/zuZhangApprove")
    public void zuZhangApprove(String zuZhangId, String managerId) {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(zuZhangId)
                .orderByTaskId()
                .desc()
                .list();
        for (Task task : list) {
            log.info("组长{}在审批{}任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>(2);
            map.put("managerTask", managerId);
            map.put("checkResult", "通过");
            taskService.complete(task.getId(), map);
        }
    }

    @PostMapping(value = "/zuZhangReject")
    public void zuZhangReject(String zuZhangId) {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(zuZhangId)
                .orderByTaskId()
                .desc()
                .list();
        for (Task task : list) {
            log.info("组长{}在审批{}任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>(1);
            //组长审批的时候，如果是拒绝，就不需要指定经理的 id
            map.put("checkResult", "拒绝");
            taskService.complete(task.getId(), map);
        }
    }

    @PostMapping(value = "/managerApprove")
    public void managerApprove(String managerId) {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(managerId)
                .orderByTaskId()
                .desc()
                .list();
        for (Task task : list) {
            log.info("经理{}在审批{}任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>(1);
            map.put("checkResult", "通过");
            taskService.complete(task.getId(), map);
        }
    }

    @PostMapping(value = "/managerReject")
    public void managerReject(String managerId) {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(managerId)
                .orderByTaskId()
                .desc()
                .list();
        for (Task task : list) {
            log.info("经理{}在审批{}任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>(1);
            map.put("checkResult", "拒绝");
            taskService.complete(task.getId(), map);
        }
    }
}
