package com.vitor.meeting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vitor.meeting.exception.ResourceNotFoundException;
import com.vitor.meeting.model.Room;
import com.vitor.meeting.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class RoomController {

  @Autowired
  private RoomRepository roomRepository;

  @GetMapping("/rooms")
  public List<Room> getAllRooms() {
    return roomRepository.findAll();
  }

  @GetMapping("/rooms/id")
  public ResponseEntity<Room> getRoomById(@PathVariable(value = "id") Long roomId) throws ResourceNotFoundException {
    Room room = roomRepository.findById(roomId)
        .orElseThrow(() -> new ResourceNotFoundException("Room not found for id " + roomId));
    return ResponseEntity.ok().body(room);
  }

  @PostMapping("/rooms")
  public Room createRoom(@Valid @RequestBody Room room) {
    return roomRepository.save(room);
  }

  @PutMapping("/rooms/{id}")
  public ResponseEntity<Room> updateRoom(@PathVariable(value = "id") Long roomId, @Valid @RequestBody Room roomDetails)
      throws ResourceNotFoundException {
    Room room = roomRepository.findById(roomId)
        .orElseThrow(() -> new ResourceNotFoundException("Room not found for id " + roomId));
    room.setName(roomDetails.getName());
    room.setDate(roomDetails.getDate());
    room.setStartHour(roomDetails.getStartHour());
    room.setEndHour(roomDetails.getEndHour());
    final Room updateRoom = roomRepository.save(room);
    return ResponseEntity.ok(updateRoom);
  }

  public Map<String, Boolean> deleteRoom(@PathVariable(value = "id") Long roomId) throws ResourceNotFoundException {
    Room room = roomRepository.findById(roomId)
        .orElseThrow(() -> new ResourceNotFoundException("Room not found for id " + roomId));
    roomRepository.delete(room);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }
}
