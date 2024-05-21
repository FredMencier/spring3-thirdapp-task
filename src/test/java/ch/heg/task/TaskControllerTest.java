package ch.heg.task;

import ch.heg.task.entities.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void tasksShouldReturnNonEmptyList() {
        Task[] tasks = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);
        Assertions.assertNotNull(tasks);
    }
}
