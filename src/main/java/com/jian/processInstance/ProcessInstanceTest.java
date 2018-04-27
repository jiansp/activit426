package com.jian.processInstance;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author xx1
 * Created by xx1 on 2018/4/27.
 * 流程实例
 */
public class ProcessInstanceTest {
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署
     */
    @Test
    public void deploymentProcessInstance() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("部署流程实例")
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 启动
     */
    @Test
    public void startProcessInstance() {
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("helloworld");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessDefinitionId());
    }

    /**
     * 查询个人任务
     */
    @Test
    public void findPersonalTask() {
        List<Task> list = processEngine.getTaskService().createTaskQuery()
                .orderByTaskCreateTime().asc()//可以添加各种条件
                .taskAssignee("李艳丽")
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
     * 查询历史任务
     */
    @Test
    public void findPersonalHistoryTask(){
        List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceStartTime().asc()
                .taskAssignee("李艳丽")
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance task : list) {
                System.out.println("任务ID：" + task.getId());
                System.out.println("任务办理人：" + task.getAssignee());
                System.out.println("任务名称：" + task.getName());
                System.out.println("#######################");

            }
        }
    }
    /**
     * 办理
     */
    @Test
    public void completePersonalProcessTask(){
        String taskId = "104";
        processEngine.getTaskService().complete(taskId);
        System.out.println("完成！！！！！！！！！");
    }
    /**
     * 查询流程状态（实例）
     */
    @Test
    public  void queryProcessStatus(){
        String processInstanceId = "301";
        ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        if(processInstance == null){
            System.out.println("end");
        }else{
            System.out.println("ing");
        }
    }

    /**
     * 查询历史流程实例
     */
    @Test
    public  void queryHistoryProcessInstance(){
        List<HistoricProcessInstance> list =  processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                .orderByProcessInstanceStartTime().asc()
                .processInstanceId("301")
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricProcessInstance historicProcessInstance : list) {
                System.out.println("：" + historicProcessInstance.getId());
                System.out.println("：" + historicProcessInstance.getStartTime());
                System.out.println("：" + historicProcessInstance.getStartUserId());
                System.out.println("#######################");

            }
        }
    }
    /**
     * 设置流程变量
     */
    @Test
    public void setVariables(){
        TaskService taskService = processEngine.getTaskService();
        String taskId = "104";
        //基本数据类型
        taskService.setVariableLocal(taskId , "请假天数" , 3);
        taskService.setVariable(taskId , "请假事由" , "回家探亲");
        /*javabean，一定要序列化
        * 此时Javabean不能更改，否则抛异常
        * 如果想更改，需要增加版本id     private static final long serialVersionUID = -6250211815378979592L;
         *
        * */
        Person person = new Person();
        person.setId("123");
        person.setName("花花");
        taskService.setVariable(taskId, "个人信息" , person);
        System.out.println("设置完成！！！！！");
    }
    /**
     * 获取流程变量
     */
    @Test
    public void getVariables(){
        TaskService taskService = processEngine.getTaskService();
        String taskId = "902";
        //基本数据类型
        Integer days = (Integer) taskService.getVariableLocal(taskId , "请假天数");
        String shi = (String) taskService.getVariable(taskId , "请假事由");
        //javabean，一定要序列化
        Person person = (Person) taskService.getVariable(taskId , "个人信息");
        System.out.println(person.getId());
        System.out.println(person.getName());
    }
}
