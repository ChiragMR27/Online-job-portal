package com.jobportal.onlinejobportal.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jobs")
@Getter
@Setter
public class Job {

    @Id
    private String id;
    private String title;
    private String company;
    private String location;
    private String type;
    private String description;
    private double salary;
    private String applyLink;
    private int minExperience;
    private String requiredSkills;
}
