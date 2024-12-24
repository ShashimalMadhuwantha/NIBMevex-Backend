package com.example.g5be.service;

import com.example.g5be.dto.StudentResponse;
import com.example.g5be.model.Student;
import com.example.g5be.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final HttpSession httpSession;

    public StudentService(StudentRepository studentRepository, HttpSession httpSession) {
        this.studentRepository = studentRepository;
        this.httpSession = httpSession;
    }

    public void registerStudent(Student student) {
        checkAdminAccess();

        // Generate the next student ID
        String nextId = generateNextStudentId();
        student.setSid(nextId);

        // Save the student
        studentRepository.save(student);
    }

    public void updateStudent(String sid, Student updatedStudent) {
        checkAdminAccess();

        // Ensure the SID is set correctly
        updatedStudent.setSid(sid);

        // Update the student
        studentRepository.update(updatedStudent);
    }

    public void deleteStudent(String sid) {
        checkAdminAccess();

        // Delete the student
        studentRepository.deleteById(sid);
    }

    private void checkAdminAccess() {
        String role = (String) httpSession.getAttribute("role");
        if (role == null || !role.equals("ROLE_ADMIN")) {
            throw new RuntimeException("Access Denied: Only admins can perform this action.");
        }
    }

    private String generateNextStudentId() {
        String lastId = studentRepository.findLastStudentId();
        if (lastId == null || lastId.isEmpty()) {
            return "S001";
        }
        int idNumber = Integer.parseInt(lastId.substring(1)); // Remove "S" prefix
        return String.format("S%03d", idNumber + 1); // Format as "S001", "S002", etc.
    }

    public List<StudentResponse> getStudentsByBatch(String bid) {
        List<Student> students = studentRepository.findStudentsByBatch(bid);

        // Convert Student to StudentResponse
        return students.stream().map(student -> {
            StudentResponse response = new StudentResponse();
            response.setSid(student.getSid());
            response.setName(student.getName());
            response.setEmail(student.getEmail());
            response.setUsername(student.getUsername());
            response.setProfilePic(student.getProfilePic());
            response.setAge(student.getAge());

            // Set badge details
            if (student.getBadge() != null) {
                StudentResponse.BadgeResponse badgeResponse = new StudentResponse.BadgeResponse();
                badgeResponse.setBid(student.getBadge().getBid());
                badgeResponse.setName(student.getBadge().getName());
                response.setBadge(badgeResponse);
            }

            return response;
        }).toList();
    }


}
