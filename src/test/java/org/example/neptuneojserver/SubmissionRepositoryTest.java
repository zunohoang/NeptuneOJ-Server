package org.example.neptuneojserver;

import org.example.neptuneojserver.dto.submission.SubmissionResponseDTO;
import org.example.neptuneojserver.repositories.SubmissionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubmissionRepositoryTest {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    public void testFindByProblemIdAndUserUsername() {
        // Thay thế với dữ liệu giả hoặc dữ liệu thực tế có trong cơ sở dữ liệu của bạn
        Long problemId = 51L;  // Ví dụ
        String username = "zunohoang";  // Ví dụ

        Pageable pageable = PageRequest.of(0, 10);
        Page<SubmissionResponseDTO> results = submissionRepository.findByProblemIdAndUserUsername(problemId, username, pageable);

        // Kiểm tra rằng kết quả không rỗng và có đúng số lượng mục
        assertNotNull(results);
        assertTrue(results.getTotalElements() > 0);

        // Kiểm tra thông tin của các mục
        results.forEach(submission -> {
            assertNotNull(submission.getSubmissionId());
            assertEquals(problemId, submission.getProblemId());
            assertEquals(username, submission.getUsername());
            // Thực hiện các kiểm tra khác tùy theo yêu cầu của bạn
        });
    }
}
