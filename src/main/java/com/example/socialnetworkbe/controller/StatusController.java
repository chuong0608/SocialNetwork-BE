package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.model.Status;
import com.example.socialnetworkbe.service.StatusService;
import com.example.socialnetworkbe.service.UserService;
import org.aspectj.asm.IRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/statuses")
public class StatusController {
    @Autowired
    StatusService statusService;
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Iterable<Status>> findAllStatus() {
        return new ResponseEntity<>(statusService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Status> saveStatus(@Valid @RequestBody Status status) {
        status.setCreateAt(LocalDateTime.now());
        statusService.save(status);
        return new ResponseEntity(statusService.findLastStatus(), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Status> findById(@PathVariable Long id) {
        return new ResponseEntity(statusService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Status> updateStatus(@PathVariable Long id, @RequestBody Status status) {
        Optional<Status> oldStatusOptional = statusService.findById(id);
        if (!oldStatusOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        giữ nguyên đối tượng ko thay đổi
        status.setId(oldStatusOptional.get().getId());
        status.setOwner(oldStatusOptional.get().getOwner());
        status.setCreateAt(oldStatusOptional.get().getCreateAt());
        statusService.save(status);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Status> deleteStatus(@PathVariable Long id) {
        Optional<Status> statusOptional = statusService.findById(id);
        Status status = statusOptional.get();
        if (!statusOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        status.setStatus(0);
        statusService.save(status);
        return new ResponseEntity<>(status, HttpStatus.NO_CONTENT);
    }
}
