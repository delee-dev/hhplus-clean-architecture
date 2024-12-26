package io.hhplus.lecture.intergration;

import io.hhplus.lecture.domain.lecture.Lecture;
import io.hhplus.lecture.domain.lecture.LectureRepository;
import io.hhplus.lecture.domain.lecture.enums.RegistrationStatus;
import io.hhplus.lecture.domain.registration.Registration;
import io.hhplus.lecture.domain.registration.RegistrationRepository;
import io.hhplus.lecture.domain.registration.RegistrationService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class ConcurrencyControlTest {
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    private static MySQLContainer container = new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
            .withDatabaseName("lecture_db")
            .withUsername("lecture_user")
            .withPassword("lecture_pass")
            .withInitScripts("schema.sql", "data.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> container.getJdbcUrl());
        registry.add("spring.datasource.username", () -> container.getUsername());
        registry.add("spring.datasource.password", () -> container.getPassword());
    }

    @BeforeAll
    static void beforeAll() {
        container.setPortBindings(List.of("3307:3306"));
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    void 동시에_여러_수강신청시_정확한_수강인원이_반영된다() throws InterruptedException {
        int threadCount = 30;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long lectureId = 1;
        AtomicInteger attendeeId = new AtomicInteger(2);
        AtomicInteger countSuccess = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    registrationService.register(lectureId,  attendeeId.getAndIncrement());
                    countSuccess.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();

        Lecture lecture = lectureRepository.findById(lectureId);
        List<Registration> registrations = registrationRepository.findByLectureIdAndStatus(lectureId, RegistrationStatus.COMPLETED);

        assertThat(registrations.size()).isEqualTo(countSuccess.get());
        assertThat(lecture.getCurrentCapacity()).isEqualTo(countSuccess.get());
    }

}
