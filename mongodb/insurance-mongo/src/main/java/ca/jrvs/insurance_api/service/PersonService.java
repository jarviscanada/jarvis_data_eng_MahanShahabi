package ca.jrvs.insurance_api.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.jrvs.insurance_api.model.Person;
import ca.jrvs.insurance_api.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private final PersonRepository repo;
	
	public PersonService(PersonRepository repo) {
		this.repo = repo;
	}

	public void save(Person person) { 
        repo.save(person);
    }
	public void saveAll(List<Person> people) { 
        repo.saveAll(people);
    }
	public Optional<Person> findOne(ObjectId id) { 
        return repo.findById(id);
    }
	public List<Person> findAll(List<ObjectId> ids) { 
        return repo.findAllById(ids);
    }
	public List<Person> findAll() { 
        return repo.findAll();
    }
	public void delete(ObjectId id) { 
        repo.deleteById(id);
    }
	public void delete(List<ObjectId> ids) { 
        repo.deleteAllById(ids);
    }
	public void deleteAll() {  
        repo.deleteAll();
    }
	public void update(Person person) { 
        if (repo.existsById(person.getId())) {
            repo.save(person);
        } else {
            throw new IllegalArgumentException("Person with ID " + person.getId() + " does not exist.");
        }
    }
	public void update(List<Person> people) { 
        for (Person person : people) {
            if (!repo.existsById(person.getId())) {
                throw new IllegalArgumentException("Person with ID " + person.getId() + " does not exist.");
            }
        }
        repo.saveAll(people);
    }
	public long count() { 
        return repo.count();
    }
	public double getAverageAge() { 
        List<Person> list = repo.findAll();
        double averageAge = 0;
        for (int i = 0; i < list.size(); i++) {
            averageAge += list.get(i).getAge();
        }
        averageAge /= list.size();
        return averageAge;
    }
	public int getMaxCars() { 
        List<Person> list = repo.findAll();
        int maxCars = list.get(0).getCarEntities().size();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCarEntities().size() > maxCars) {
                maxCars = list.get(i).getCarEntities().size();
            }
        }
        return maxCars;
    }
	
}
