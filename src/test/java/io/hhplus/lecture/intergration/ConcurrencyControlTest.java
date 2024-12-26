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
import org.springframework.test.context.jdbc.Sql;
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
            .withInitScripts("schema.sql");

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
    @Sql(scripts = {"/data.sql"})
    void 최대_수강_인원보다_많은_수의_신청이_들어_왔을_때_최대_수강_인원까지만_신청_된다() throws InterruptedException {
        // given
        int threadCount = 40;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long lectureId = 1;
        AtomicInteger attendeeId = new AtomicInteger(2);
        AtomicInteger countSuccess = new AtomicInteger(0);

        // when
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

        // then
        Lecture lecture = lectureRepository.findById(lectureId);
        List<Registration> registrations = registrationRepository.findByLectureIdAndStatus(lectureId, RegistrationStatus.COMPLETED);

        assertThat(lecture.getCurrentCapacity()).isEqualTo(lecture.getMaxCapacity());
        assertThat(registrations.size()).isEqualTo(lecture.getMaxCapacity());
    }

    @Test
    @Sql(scripts = {"/data.sql"})
    void 동시에_같은_참석자가_여러_번의_신청을_보내도_한_건만_처리된다() throws InterruptedException {
        // given
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long lectureId = 1;
        long attendeeId = 2;

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    registrationService.register(lectureId,  attendeeId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();

        // then
        Lecture lecture = lectureRepository.findById(lectureId);
        List<Registration> registrations = registrationRepository.findByLectureIdAndStatus(lectureId, RegistrationStatus.COMPLETED);

        assertThat(registrations.size()).isEqualTo(1);
        assertThat(lecture.getCurrentCapacity()).isEqualTo(1);
    }

}
