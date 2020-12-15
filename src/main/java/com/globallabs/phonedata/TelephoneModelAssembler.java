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
<<<<<<< HEAD
            linkTo(methodOn(TelephoneController.class).one(telephone.getId())).withSelfRel(),
            linkTo(methodOn(TelephoneController.class).all()).withRel("telephones")
        );
=======
      linkTo(methodOn(TelephoneController.class).one(telephone.getId())).withSelfRel(),
      linkTo(methodOn(TelephoneController.class).all()).withRel("telephones")
    );
>>>>>>> c1df60aa23ddddb593c28378b5b0ee6e122f9208
  }
}
