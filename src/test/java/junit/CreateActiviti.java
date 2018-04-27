package junit;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

public class CreateActiviti {
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

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
     * 部署流程，从classpath
     */
    @Test
    public void deploymentProcessDefinition_classpath() {
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("helloworld入门程序")
                .addClasspathResource("diagrams/helloworld.bpmn")
                .addClasspathResource("diagrams/helloworld.png")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 部署流程，从zip
     */
    @Test
    public void deploymentProcessDefinition_zip() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("zip部署的helloworld入门程序")
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void findProcessDefinition() {
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery()
                /*条件*/
                /*.deploymentId("")//使用部署对象ID查询
                .processDefinitionId("")//使用流程定义ID查询
                .processDefinitionKey("")//使用流程定义key查询*/
                /*排序*/
                .orderByProcessDefinitionVersion().asc()
                /*结果*/
                .list();//返回查询集合
                /*.listPage(firstResule, maxResult)//分页
                .singleResult()//返回唯一结果集
                .count()//数量*/
        if (list != null && list.size() > 0) {
            for (ProcessDefinition prodef : list) {
                System.out.println(prodef.getId());
                System.out.println(prodef.getKey());
                System.out.println(prodef.getName());
                System.out.println(" ####  ");
            }
        }
    }

    /**
     * 流程定义的删除
     */
    @Test
    public void deleteProcessDefinition() {
       /* 不带级联的删除，只能删除未启动的流程（默认第二个参数false）*/
        processEngine.getRepositoryService().deleteDeployment("1");
       /* 级联删除，不管流程是否启动，都能删除*/
        //processEngine.getRepositoryService().deleteDeployment("1" , true);
        System.out.println("删除成功！！！！！！！！！");
    }

    /**
     * 查看流程图
     */
    @Test
    public void viewPic() throws IOException {
        String deploymentId = "601";
        List<String> list = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        String resourceName = "";
        if (list != null && list.size() > 0) {
            for (String picName : list) {
                if (picName.indexOf(".png") > 0) {
                    resourceName = picName;
                }
            }
        }
        InputStream inputStream = processEngine.getRepositoryService().getResourceAsStream(deploymentId, resourceName);
        File file = new File("D:/" + resourceName);
        FileUtils.copyInputStreamToFile(inputStream, file);
    }

    /**
     * 查询最新版本的流程定义
     */
    @Test
    public void findLastVersionProcessDefinition(){
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc().list();
        Map<String , ProcessDefinition> map = new LinkedHashMap<String , ProcessDefinition>();
        if (list != null && list.size() > 0) {
            for (ProcessDefinition processDefinition : list) {
                map.put(processDefinition.getKey() , processDefinition);
            }
        }
        List<ProcessDefinition> ret  = new ArrayList<ProcessDefinition>(map.values());
        if ( ret.size() > 0) {
            for (ProcessDefinition prodef : ret) {
                System.out.println(prodef.getId());
                System.out.println(prodef.getKey());
                System.out.println(prodef.getName());
                System.out.println(" ####  ");
            }
        }
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

    /**
     * 查看当前人的个人任务信息
     */
    @Test
    public void findMyPersonalTask() {
        String assigness = "刘连胜";
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

    /**
     * 完成个人任务
     * 反复的去查询、完成，直到流程结束
     */
    @Test
    public void completePersonalTask() {
        String taskId = "302";
        processEngine.getTaskService().complete(taskId);
        System.out.println("完成任务！！！！！！！！！！！！");
    }

}
