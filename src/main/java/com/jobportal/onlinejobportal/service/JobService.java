package com.jobportal.onlinejobportal.service;

import com.jobportal.onlinejobportal.dto.JobDto;
import com.jobportal.onlinejobportal.model.Job;
import com.jobportal.onlinejobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    // Create job
    public JobDto createJob(JobDto jobDto) {
        Job job = new Job();
        mapDtoToEntity(jobDto, job);
        Job savedJob = jobRepository.save(job);
        return mapToDto(savedJob);
    }

    // Get all jobs
    public List<JobDto> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Apply with eligibility check (mocked)
    public boolean applyForJob(String jobId, String token) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        int userExperience = 3; // TODO: Replace with actual user profile
        List<String> userSkills = List.of("Java", "Spring Boot", "MongoDB");

        if (job.getMinExperience() > userExperience) {
            return false;
        }

        if (job.getRequiredSkills() != null && !job.getRequiredSkills().isBlank()) {
            List<String> requiredSkills = Arrays.stream(job.getRequiredSkills().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());

            for (String skill : requiredSkills) {
                if (!userSkills.contains(skill)) {
                    return false;
                }
            }
        }

        return true;
    }

    // Convert Entity → DTO
    private JobDto mapToDto(Job job) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setCompany(job.getCompany());
        dto.setLocation(job.getLocation());
        dto.setType(job.getType());
        dto.setDescription(job.getDescription());
        dto.setSalary(job.getSalary());
        dto.setApplyLink(job.getApplyLink());
        dto.setMinExperience(job.getMinExperience());
        dto.setRequiredSkills(job.getRequiredSkills());
        return dto;
    }

    // Convert DTO → Entity
    private void mapDtoToEntity(JobDto dto, Job job) {
        job.setTitle(dto.getTitle());
        job.setCompany(dto.getCompany());
        job.setLocation(dto.getLocation());
        job.setType(dto.getType());
        job.setDescription(dto.getDescription());
        job.setSalary(dto.getSalary());
        job.setApplyLink(dto.getApplyLink());
        job.setMinExperience(dto.getMinExperience());
        job.setRequiredSkills(dto.getRequiredSkills());
    }
}
