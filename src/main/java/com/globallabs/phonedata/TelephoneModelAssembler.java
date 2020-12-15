package com.globallabs.phonedata;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TelephoneModelAssembler implements 
    RepresentationModelAssembler<TelephoneModel, EntityModel<TelephoneModel>> {
    
  @Override
  public EntityModel<TelephoneModel> toModel(TelephoneModel telephone) {
    return EntityModel.of(telephone, 
      linkTo(methodOn(TelephoneController.class).one(telephone.getId())).withSelfRel(),
      linkTo(methodOn(TelephoneController.class).all()).withRel("telephones")
    );
  }
}
