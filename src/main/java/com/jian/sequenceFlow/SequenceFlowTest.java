package com.jian.sequenceFlow;

import com.jian.utils.ActivitiUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xx1
 *         Created by xx1 on 2018/4/28.
 */
public class SequenceFlowTest {
    @Test
    public void testUtils() {
//        ActivitiUtils.deploymentProcessInstance("diagrams/sequenceFlow.bpmn", "diagrams/sequenceFlow.png", "流程连线部署");
//        ActivitiUtils.startProcessInstance("sequenceFlow");
//        ActivitiUtils.findPersonalTask("纪涛");
        Map<String , Object> map = new HashMap<String , Object>();
        map.put("message" , "重要");
        ActivitiUtils.completePersonalTask("1306" , map);
    }
}
