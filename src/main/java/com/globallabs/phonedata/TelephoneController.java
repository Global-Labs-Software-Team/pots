package com.globallabs.phonedata;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelephoneController {

  private final TelephoneRepository repository;
  private final TelephoneModelAssembler assembler;

  /**
   * Constructor of the class.
   * @param repository the interface to connect to the database
   */
  public TelephoneController(TelephoneRepository repository, TelephoneModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  /**
   * API to get all the telephones in the database.
   * @return a list with the telephones
   */
  @GetMapping("/telephones")
  public List<TelephoneModel> all() {
    return repository.findAll();
  }

  /**
   * API to get one telephone in the database.
   * @param id the id of the phone
   * @return the response
   * 
   * @apiNote it is not implemented yet
   */
  @GetMapping("/telephones/{id}")
  public ResponseEntity<?> one(@PathVariable int id) {
    return ResponseEntity
      .ok()
      .body("ok");
  }

  /**
   * Add a new telephone to the network.
   * @param newTelephone the new telephone information
   * @return the information of the new telephone in the network
   */
  @PostMapping("/telephones")
  ResponseEntity<?> newTelephone(@RequestBody TelephoneModel newTelephone) {
    EntityModel<TelephoneModel> entityModel = assembler.toModel(repository.save(newTelephone)); 
    return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  /**
   * Delete an existing phone from the network.
   * @param id the identifier of the phone
   * @return a response entity
   */
  @DeleteMapping("/telephones/{id}")
  ResponseEntity<?> delete(@PathVariable int id) {
    TelephoneModel telephoneModelToDelete = new TelephoneModel(id);
    if (!repository.existsById(id)) {
      return ResponseEntity
          .status(404)
          .body("Telephone with this id was not found");
    }
    repository.delete(telephoneModelToDelete);;
    return ResponseEntity
        .status(204)
        .body("Successfully deleted");
  }
}
