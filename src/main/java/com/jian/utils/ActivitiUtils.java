package com.jian.utils;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

/**
 * @author xx1
 * Created by xx1 on 2018/4/28.
 */
public class ActivitiUtils {
    private static ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    /**
     * 部署
     */
    public static void deploymentProcessInstance(String bpmn , String png , String name) {
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name(name)
                .addClasspathResource(bpmn)
                .addClasspathResource(png)
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 启动
     */
    public static void startProcessInstance(String key) {
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key);
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessDefinitionId());
    }

    /**
     * 查看个人任务
     * @param userTask
     */
    public static void findPersonalTask(String userTask){
        List<Task> list = processEngine.getTaskService().createTaskQuery()
                .taskAssignee(userTask)
                .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID：" + task.getId());
                System.out.println("任务办理人：" + task.getAssignee());
                System.out.println("任务创建时间：" + task.getCreateTime());
                System.out.println("任务名称：" + task.getName());
                System.out.println("#######################");

            }
        }
    }

    /**
     * 完成任务
     * @param taskId
     */
    public static void completePersonalTask(String taskId , Map<String , Object> variables){
        processEngine.getTaskService().complete(taskId , variables);
        System.out.println("完成！！！！！！！！！");
    }

}
