package com.example.g5be.controller;

import com.example.g5be.model.Event;
import com.example.g5be.service.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EventController {

    private final EventService eventService;
    private final HttpSession httpSession;

    public EventController(EventService eventService, HttpSession httpSession) {
        this.eventService = eventService;
        this.httpSession = httpSession;
    }

    @PutMapping("/lecturer/events/{eid}/status")
    public ResponseEntity<String> updateEventStatus(@PathVariable String eid, @RequestParam String status) {
        // Check lecturer role from session
        String role = (String) httpSession.getAttribute("role");
        if (role == null || !role.equals("ROLE_LECTURER")) {
            return ResponseEntity.status(403).body("Access Denied: Only lecturers can update event status.");
        }

        try {
            eventService.updateEventStatus(eid, status);
            return ResponseEntity.ok("Event status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/event/delete/{eid}")
    public ResponseEntity<String> deleteEvent(@PathVariable String eid) {
        // Check lecturer role from session
        String role = (String) httpSession.getAttribute("role");
        if (role == null || !role.equals("ROLE_LECTURER")) {
            return ResponseEntity.status(403).body("Access Denied: Only lecturers can delete events.");
        }

        try {
            eventService.deleteById(eid);
            return ResponseEntity.ok("Event deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/event/all")
    public ResponseEntity<?> getAllEvents() {
        // Check lecturer role from session
        String role = (String) httpSession.getAttribute("role");
        String lecturerId = (String) httpSession.getAttribute("id");

        if (role == null || !role.equals("ROLE_LECTURER")) {
            return ResponseEntity.status(403).body("Access Denied: Only lecturers can view their events.");
        }

        try {
            List<Event> events = eventService.findEventsByLecturerId(lecturerId);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
