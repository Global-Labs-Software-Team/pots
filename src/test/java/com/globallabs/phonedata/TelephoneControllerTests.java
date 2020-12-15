package com.globallabs.phonedata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.globallabs.phoneexceptions.InvalidNumberException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = TelephoneController.class)
@ComponentScan(basePackages = {"com.globallabs.phonedata"})
@EnableAutoConfiguration
class TelephoneControllerTests {
  @Autowired 
  private TelephoneController controller;

  @Test
  public void contextLoads() {
    assertThat(controller).isNotNull();
  }

  @Test
  public void testDeletingExistingTelephone() throws InvalidNumberException {
    int id = 1;
    controller.newTelephone(new TelephoneModel(id)); // Telephone with ID one
    ResponseEntity<?> response = controller.delete(id);
    assertEquals(204, response.getStatusCodeValue());
  }
}
