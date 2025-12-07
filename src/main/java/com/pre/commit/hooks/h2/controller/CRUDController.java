package com.pre.commit.hooks.h2.controller;

import com.pre.commit.hooks.h2.model.CRUDModel;
import com.pre.commit.hooks.h2.repository.CRUDRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CRUDController {

  @Autowired CRUDRepository cRUDRepository;

  @GetMapping("/tutorials")
  public ResponseEntity<List<CRUDModel>> getAllTutorials(
      @RequestParam(required = false) String title) {
    try {
      List<CRUDModel> cRUDModels = new ArrayList<CRUDModel>();

      if (title == null) cRUDRepository.findAll().forEach(cRUDModels::add);
      else cRUDRepository.findByTitleContainingIgnoreCase(title).forEach(cRUDModels::add);

      if (cRUDModels.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(cRUDModels, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/tutorials/{id}")
  public ResponseEntity<CRUDModel> getTutorialById(@PathVariable("id") long id) {
    Optional<CRUDModel> tutorialData = cRUDRepository.findById(id);

    if (tutorialData.isPresent()) {
      return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/tutorials")
  public ResponseEntity<CRUDModel> createTutorial(@RequestBody CRUDModel cRUDModel) {
    try {
      CRUDModel _tutorial =
          cRUDRepository.save(
              new CRUDModel(cRUDModel.getTitle(), cRUDModel.getDescription(), false));
      return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/tutorials/{id}")
  public ResponseEntity<CRUDModel> updateTutorial(
      @PathVariable("id") long id, @RequestBody CRUDModel cRUDModel) {
    Optional<CRUDModel> tutorialData = cRUDRepository.findById(id);

    if (tutorialData.isPresent()) {
      CRUDModel _tutorial = tutorialData.get();
      _tutorial.setTitle(cRUDModel.getTitle());
      _tutorial.setDescription(cRUDModel.getDescription());
      _tutorial.setPublished(cRUDModel.isPublished());
      return new ResponseEntity<>(cRUDRepository.save(_tutorial), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/tutorials/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
    try {
      cRUDRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/tutorials")
  public ResponseEntity<HttpStatus> deleteAllTutorials() {
    try {
      cRUDRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/tutorials/published")
  public ResponseEntity<List<CRUDModel>> findByPublished() {
    try {
      List<CRUDModel> cRUDModels = cRUDRepository.findByPublished(true);

      if (cRUDModels.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(cRUDModels, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
