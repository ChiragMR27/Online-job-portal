package com.jobportal.onlinejobportal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDto {
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
