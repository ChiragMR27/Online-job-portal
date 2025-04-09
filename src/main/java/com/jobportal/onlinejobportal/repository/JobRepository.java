package com.jobportal.onlinejobportal.repository;

import com.jobportal.onlinejobportal.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<Job, String> {
}
