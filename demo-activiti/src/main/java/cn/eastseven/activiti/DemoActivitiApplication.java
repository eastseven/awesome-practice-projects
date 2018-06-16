package cn.eastseven.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author d7
 */
@Slf4j
@SpringBootApplication
public class DemoActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoActivitiApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(final RepositoryService repositoryService,
                                  final RuntimeService runtimeService,
                                  final TaskService taskService) {

        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                log.debug(">>> Number of process definitions : " + repositoryService.createProcessDefinitionQuery().count());
                log.debug(">>> Number of tasks : " + taskService.createTaskQuery().count());
                log.debug(">>> Number of tasks after process start: " + taskService.createTaskQuery().count());
            }
        };

    }
}
