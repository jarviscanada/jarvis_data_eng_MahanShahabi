package ca.jrvs.insurance_api.controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.jrvs.insurance_api.model.Person;
import ca.jrvs.insurance_api.service.PersonService;

@RestController
@RequestMapping("/insurance_api")
public class PersonController {

	@Autowired
	private final PersonService service;
	
	public PersonController(PersonService service) {
		this.service = service;
	}
	
	@PostMapping("person")
	@ResponseStatus(HttpStatus.CREATED)
	public void postPerson(@RequestBody Person person) {
		service.save(person);
	}
	
	@GetMapping("people")
	public List<Person> getPeople() {
		return service.findAll();
	}
	
	@GetMapping("person/{id}")
	public ResponseEntity<Person> getPerson(@PathVariable ObjectId id) {
		Optional<Person> o = service.findOne(id);
		if (o.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(o.get());
	}
	
	@PostMapping("people")
    @ResponseStatus(HttpStatus.CREATED)
    public void postPeople(@RequestBody List<Person> people) {
        service.saveAll(people);
    }

    @DeleteMapping("person/{id}") 
    public ResponseEntity<Person> deletePerson(@PathVariable ObjectId id) {
        Optional<Person> o = service.findOne(id);
		if (o.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
        service.delete(id);
		return ResponseEntity.ok(o.get());
    }

    @DeleteMapping("people")
	public void deletePeople() {
		service.deleteAll();;
	}
	
    @PutMapping("person")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> updatePerson(@RequestBody Person person) {
		try {
            service.update(person);
            return ResponseEntity.ok("Person updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
	}

    @PutMapping("people")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updatePeople(@RequestBody List<Person> people) {
        try {
            service.update(people);
            return ResponseEntity.ok("People updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/person/count")
    public ResponseEntity<Long> countPeople() {
        long count = service.count();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/person/averageAge")
    public ResponseEntity<Double> getPeopleAverageAge() {
        double averageAge = service.getAverageAge();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/person/maxCars")
    public ResponseEntity<Integer> getPeopleMaxCars() {
        int maxCars = service.getMaxCars();
        return ResponseEntity.ok(maxCars);
    }
}
