package org.example.neptuneojserver.services;

import org.example.neptuneojserver.dto.problem.*;
import org.example.neptuneojserver.models.*;
import org.example.neptuneojserver.repositorys.ProblemRepository;
import org.example.neptuneojserver.repositorys.TagRepository;
import org.example.neptuneojserver.repositorys.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@lombok.AllArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public List<ProblemResponseDTO> getProblems(int page, int size) {
        List<ProblemResponseDTO> problemResponseDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Problem> problems = problemRepository.findAllPaged(pageable);

        problems.forEach(problem -> {
            ProblemResponseDTO problemResponseDTO = convertToDTO(problem);
            problemResponseDTOS.add(problemResponseDTO);
        });

        return problemResponseDTOS;
    }

    public void addProblem(ProblemRequestDTO problemDTO, String username) {
        User author = userRepository.findByUsername(username);
        Problem problem = new Problem();
        problem.setCreatedAt(ZonedDateTime.now());
        System.out.println(problem.getCreatedAt() + " - " + ZonedDateTime.now());
        convertToEntity(problemDTO, problem, author);
        problemRepository.save(problem);
    }

    public void updateProblem(ProblemRequestDTO problemDTO, Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        convertToEntity(problemDTO, problem, problem.getAuthor());
        problemRepository.save(problem);
    }

    public void deleteProblem(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        problemRepository.delete(problem);
    }

    public ProblemResponseDTO getProblemById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        return convertToDTO(problem);
    }

    public ProblemResponseDTO getAdminProblemById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        ProblemResponseDTO problemResponseDTO = convertToDTO(problem);
        List<TestcaseDTO> testcases = new ArrayList<>();
        for (var testcase : problem.getTestcases()) {
            TestcaseDTO testcaseDTO = new TestcaseDTO();
            testcaseDTO.setInput(testcase.getInput());
            testcaseDTO.setOutput(testcase.getOutput());
            testcases.add(testcaseDTO);
        }
        problemResponseDTO.setTestcases(testcases);
        return problemResponseDTO;
    }

    private ProblemResponseDTO convertToDTO(Problem problem) {
        ProblemResponseDTO problemResponseDTO = new ProblemResponseDTO();
        problemResponseDTO.setId(problem.getId());
        problemResponseDTO.setTitle(problem.getTitle());
        problemResponseDTO.setBody(problem.getBody());
        problemResponseDTO.setPoint(problem.getPoint());
        problemResponseDTO.setNumberAccept(problem.getNumberAccept());
        problemResponseDTO.setTimeLimit(problem.getTimeLimit());
        problemResponseDTO.setMemory(problem.getMemory());
        problemResponseDTO.setCreatedAt(problem.getCreatedAt());
        problemResponseDTO.setAuthorId(problem.getAuthor().getId());
        problemResponseDTO.setAuthorName(problem.getAuthor().getUsername());
        problemResponseDTO.setAuthorUsername(problem.getAuthor().getUsername());

        List<TestExampleDTO> testExamples = new ArrayList<>();
        for (var testExample : problem.getTestExamples()) {
            TestExampleDTO testExampleDTO = new TestExampleDTO();
            testExampleDTO.setId(testExample.getId());
            testExampleDTO.setInput(testExample.getInput());
            testExampleDTO.setOutput(testExample.getOutput());
            testExampleDTO.setDescription(testExample.getDescription());
            testExamples.add(testExampleDTO);
        }
        problemResponseDTO.setTestExamples(testExamples);

        // add tag
        if(problem.getProblemTags() == null) {
            problem.setProblemTags(new ArrayList<>());
        }
        List<TagDTO> tags = new ArrayList<>();
        for (var problemTag : problem.getProblemTags()) {
            TagDTO tagDTO = new TagDTO();
            if(problemTag.getTag() == null) {
                continue;
            }
            if(problemTag.getTag().getTitle() == null) {
                continue;
            }
            if(problemTag.getTag().getId() == null) {
                continue;
            }
            tagDTO.setId(problemTag.getTag().getId());
            tagDTO.setTitle(problemTag.getTag().getTitle());
            tags.add(tagDTO);
        }
        problemResponseDTO.setTags(tags);

        return problemResponseDTO;
    }

    private Problem convertToEntity(ProblemRequestDTO problemDTO, Problem problem, User author) {
        problem.setTitle(problemDTO.getTitle());
        problem.setBody(problemDTO.getBody());
        problem.setPoint(problemDTO.getPoint());
        problem.setNumberAccept(problemDTO.getNumberAccept());
        problem.setTimeLimit(problemDTO.getTimeLimit());
        problem.setMemory(problemDTO.getMemory());
        problem.setHidden(problemDTO.isHidden());
        problem.setAuthor(author);

        // Khởi tạo danh sách nếu chưa tồn tại
        if (problem.getTestcases() == null) {
            problem.setTestcases(new ArrayList<>());
        }
        if (problem.getTestExamples() == null) {
            problem.setTestExamples(new ArrayList<>());
        }
        if(problem.getProblemTags() == null) {
            problem.setProblemTags(new ArrayList<>());
        }

        // Cập nhật danh sách testcases và testExamples
        updateTestcases(problem.getTestcases(), problemDTO.getTestcases(), problem);
        updateTestExamples(problem.getTestExamples(), problemDTO.getTestExamples(), problem);
        updateTags(problem.getProblemTags(), problemDTO.getTags(), problem);

        return problem;
    }

    private void updateTags(List<ProblemTag> existingTags, List<TagDTO> newTagDTOs, Problem problem) {
        existingTags.clear();
        for (TagDTO tagDTO : newTagDTOs) {
            Tag tag = new Tag();
            if(tagDTO.getId() != null) {
                tag = tagRepository.findById(tagDTO.getId()).orElseThrow(() -> new RuntimeException("Tag not found"));
            } else if(tagDTO.getTitle() != null) {
                // Tìm tag theo title, nếu không tồn tại thì tạo mới
                tag = tagRepository.findByTitle(tagDTO.getTitle())
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setTitle(tagDTO.getTitle());
                            newTag.setCreatedAt(ZonedDateTime.now());
                            return tagRepository.save(newTag);
                        });
            }

            ProblemTag problemTag = new ProblemTag();
            problemTag.setProblem(problem);
            problemTag.setTag(tag);
            existingTags.add(problemTag);
        }
    }
    private void updateTestcases(List<Testcase> existingTestcases, List<TestcaseDTO> newTestcaseDTOs, Problem problem) {
        existingTestcases.clear();
        for (int i = 0; i < newTestcaseDTOs.size(); i++) {
            Testcase testcase = new Testcase();
            testcase.setInput(newTestcaseDTOs.get(i).getInput());
            testcase.setOutput(newTestcaseDTOs.get(i).getOutput());
            testcase.setIndexInProblem((long) i + 1);
            testcase.setProblem(problem);
            existingTestcases.add(testcase);
        }
    }
    private void updateTestExamples(List<TestExample> existingTestExamples, List<TestExampleDTO> newTestExampleDTOs, Problem problem) {
        existingTestExamples.clear();
        for (int i = 0; i < newTestExampleDTOs.size(); i++) {
            TestExample testExample = new TestExample();
            testExample.setInput(newTestExampleDTOs.get(i).getInput());
            testExample.setOutput(newTestExampleDTOs.get(i).getOutput());
            testExample.setDescription(newTestExampleDTOs.get(i).getDescription());
            testExample.setProblem(problem);
            existingTestExamples.add(testExample);
        }
    }

    public List<Problem> getProblemsAcceptedByUsername(String username) {
        return problemRepository.findProblemsAcceptedByUsername(username);
    }

    public List<ProblemResponseDTO> getProblemsBySearch(int page, int size, String search) {
        List<ProblemResponseDTO> problemResponseDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Problem> problems = problemRepository.findByTitleContaining(search, pageable);

        problems.forEach(problem -> {
            ProblemResponseDTO problemResponseDTO = convertToDTO(problem);
            problemResponseDTOS.add(problemResponseDTO);
        });

        return problemResponseDTOS;
    }

    public List<ProblemResponseDTO> getProblemsByTagName(int page, int size, String tagName) {
        List<ProblemResponseDTO> problemResponseDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Problem> problems = problemRepository.findByTagName(tagName, pageable);

        problems.forEach(problem -> {
            ProblemResponseDTO problemResponseDTO = convertToDTO(problem);
            problemResponseDTOS.add(problemResponseDTO);
        });

        return problemResponseDTOS;
    }
}
