package com.jobportal.onlinejobportal.controller;

import com.jobportal.onlinejobportal.dto.JobDto;
import com.jobportal.onlinejobportal.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // GET: /api/jobs - Fetch all jobs
    @GetMapping
    public ResponseEntity<List<JobDto>> getAllJobs() {
        List<JobDto> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    // POST: /api/jobs/host - Host new job
    @PostMapping("/host")
    public ResponseEntity<JobDto> createJob(@RequestBody JobDto jobDto) {
        JobDto savedJob = jobService.createJob(jobDto);
        return new ResponseEntity<>(savedJob, HttpStatus.CREATED);
    }

    // POST: /api/jobs/{jobId}/apply - Apply for job
    @PostMapping("/{jobId}/apply")
    public ResponseEntity<String> applyForJob(@PathVariable String jobId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }

        boolean eligible = jobService.applyForJob(jobId, token);
        if (eligible) {
            return ResponseEntity.ok("Application submitted!");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not eligible for this job.");
        }
    }
}
