package com.example.g5be.repository;


import com.example.g5be.dto.InterviewDTO;
import com.example.g5be.model.Interview;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InterviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public InterviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save an Interview
    public void save(Interview interview) {
        String sql = "INSERT INTO Interview (EID, Interviewer, Location) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, interview.getEid(), interview.getInterviewer(), interview.getLocation());
    }

    public List<InterviewDTO> findInterviewsByLecturerId(String lecturerId) {
        String sql = """
            SELECT i.EID, i.Interviewer, i.Location, e.Date
            FROM Interview i
            INNER JOIN Event e ON i.EID = e.EID
            WHERE e.LID = ?
            """;

        return jdbcTemplate.query(sql, new Object[]{lecturerId}, (rs, rowNum) -> {
            InterviewDTO interviewDTO = new InterviewDTO();
            interviewDTO.setEid(rs.getString("EID"));
            interviewDTO.setInterviewer(rs.getString("Interviewer"));
            interviewDTO.setLocation(rs.getString("Location"));
            interviewDTO.setDate(rs.getDate("Date")); // Set the interview date
            return interviewDTO;
        });
    }


}
