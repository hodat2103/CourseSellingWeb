package com.example.CourseSellingWeb.responses;

import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.models.Mentor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorResponse extends BaseResponse {
    private Integer id;

    private String name;

    private String email;

    private String major;

    private String experience;

    @JsonProperty("employee_id")
    private Integer employeeId;

    public static MentorResponse fromMentor(Mentor mentor){
        MentorResponse mentorResponse = MentorResponse.builder()
                .id(mentor.getId())
                .name(mentor.getName())
                .email(mentor.getEmail())
                .major(mentor.getMajor())
                .experience(mentor.getExperience())
                .employeeId(mentor.getEmployee().getId())
                .build();
        mentorResponse.setCreatedAt(mentor.getCreatedAt());
        mentorResponse.setUpdatedAt(mentor.getUpdatedAt());

        return mentorResponse;
    }
}
