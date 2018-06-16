package cn.eastseven.activiti;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Slf4j
public class ActivitiTests extends DemoActivitiApplicationTests {

    @Autowired private RuntimeService runtimeService;

    @Autowired private RepositoryService repositoryService;

    @Autowired private IdentityService identityService;

    @Autowired private TaskService taskService;

    private Group adminGroup = null;
    private Group userGroup = null;
    private User user = null;
    private User admin = null;

    @Before
    public void init() {
        adminGroup = identityService.newGroup("ROLE_ADMIN");
        userGroup = identityService.newGroup("ROLE_USER");
        identityService.saveGroup(adminGroup);
        identityService.saveGroup(userGroup);

        admin = identityService.newUser("admin");
        user = identityService.newUser("user");
        identityService.saveUser(admin);
        identityService.saveUser(user);

        identityService.createMembership(admin.getId(), adminGroup.getId());
        identityService.createMembership(user.getId(), userGroup.getId());

        identityService.createUserQuery().list().forEach(u -> {
            log.debug(">>> user {}", u.getId());
        });
    }

    @After
    public void clean() {
        List<User> userList = identityService.createUserQuery().list();
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                identityService.deleteUser(user.getId());
            }
        }

        List<Group> groupList = identityService.createGroupQuery().list();
        if (!CollectionUtils.isEmpty(groupList)) {
            for (Group group : groupList) {
                identityService.deleteGroup(group.getId());
            }
        }

        repositoryService.createDeploymentQuery().list().forEach(d -> {
            log.debug(">>> remove deploy id={}, key={}, name={}, cat={}, time={}", d.getId(), d.getKey(), d.getName(), d.getCategory(), d.getDeploymentTime());
            repositoryService.deleteDeployment(d.getId());
        });
    }

    @Test
    public void testRuntimeService() throws Exception {
        Assert.assertNotNull(runtimeService);

        Assert.assertNotNull(repositoryService);

        repositoryService.createDeploymentQuery().list().forEach(d -> {
            log.debug(">>> deploy id={}, key={}, name={}, cat={}, time={}", d.getId(), d.getKey(), d.getName(), d.getCategory(), d.getDeploymentTime());
        });

        runtimeService.createProcessInstanceQuery().list().forEach(p -> {
            log.debug(">>> process instance {}", p.getId());
        });

        // 开始流程
        log.debug(">>> 开始流程");
        final String key = "myProcess_1";

        // start
        Map<String, Object> data = Maps.newHashMap();
        data.put("message", "测试");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, data);
        final String pid = processInstance.getProcessDefinitionId();
        log.debug(">>> 创建一个流程实例 {}", pid);

        // 查看当前 task情况
        taskService.createTaskQuery().list().forEach(t -> {
            log.debug(">>> 1 task id={}, assignee={}, pid={}, state={}, createTime={}, claimTime={}, dueDate={}",
                    t.getId(), t.getAssignee(), t.getProcessDefinitionId(),
                    t.getDelegationState(), t.getCreateTime(), t.getClaimTime(), t.getDueDate());
        });

        //
        taskService.createTaskQuery().taskCandidateGroup("ROLE_ADMIN").list().forEach(t -> {
            taskService.claim(t.getId(), admin.getId());
        });

        taskService.createTaskQuery().list().forEach(t -> {
            log.debug(">>> 2 task id={}, assignee={}, pid={}, state={}, createTime={}, claimTime={}, dueDate={}",
                    t.getId(), t.getAssignee(), t.getProcessDefinitionId(),
                    t.getDelegationState(), t.getCreateTime(), t.getClaimTime(), t.getDueDate());
        });

        // 管理员审核
        taskService.createTaskQuery().taskAssignee(admin.getId()).list().forEach(t -> {
            log.debug(">>> 3 audit task id={}, assignee={}, pid={}, state={}, createTime={}, claimTime={}, dueDate={}",
                    t.getId(), t.getAssignee(), t.getProcessDefinitionId(),
                    t.getDelegationState(), t.getCreateTime(), t.getClaimTime(), t.getDueDate());

            taskService.complete(t.getId());
        });

        taskService.createTaskQuery().list().forEach(t -> {
            log.debug(">>> 4 final task id={}, assignee={}, pid={}, state={}, createTime={}, claimTime={}, dueDate={}",
                    t.getId(), t.getAssignee(), t.getProcessDefinitionId(),
                    t.getDelegationState(), t.getCreateTime(), t.getClaimTime(), t.getDueDate());
        });
    }
}
