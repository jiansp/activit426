package junit;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class CreateActiviti {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 使用配置文件创建23张表
     */
    @Test
    public void createTable() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println("processEngine : " + processEngine);
        System.out.println("processEngine : " + processEngine.getName());
    }

    /**
     * 部署流程
     */
    @Test
    public void deploymentProcessDefinition() {
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("helloworld入门程序")
                .addClasspathResource("diagrams/helloworld.bpmn")
                .addClasspathResource("diagrams/helloworld.png")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 启动流程
     */
    @Test
    public void startProcessInstance() {
        String key = "helloworld";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key);
        System.out.println("流程实例ID：" + processInstance.getId());
        System.out.println("流程定义ID：" + processInstance.getProcessDefinitionId());

    }

    @Test
    public void findMyPersonalTask() {
        String assigness = "李艳丽";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assigness).list();
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

}
