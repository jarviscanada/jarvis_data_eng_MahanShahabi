package ca.jrvs.insurance_api.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("people")
public class Person {

    private ObjectId id;
    private String firstName;
    private String lastName;
    private int age;
    private Address addressEntity;
    private Date createdAt = new Date();
    private Boolean insurance;
    private List<Car> carEntities;

    public Person(ObjectId id, String firstName, String lastName, int age, Address addressEntity, Date createdAt,
            Boolean insurance, List<Car> carEntities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.addressEntity = addressEntity;
        this.createdAt = createdAt;
        this.insurance = insurance;
        this.carEntities = carEntities;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(Address addressEntity) {
        this.addressEntity = addressEntity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public List<Car> getCarEntities() {
        return carEntities;
    }

    public void setCarEntities(List<Car> carEntities) {
        this.carEntities = carEntities;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age
                + ", addressEntity=" + addressEntity + ", createdAt=" + createdAt + ", insurance=" + insurance
                + ", carEntities=" + carEntities + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + age;
        result = prime * result + ((addressEntity == null) ? 0 : addressEntity.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((insurance == null) ? 0 : insurance.hashCode());
        result = prime * result + ((carEntities == null) ? 0 : carEntities.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (age != other.age)
            return false;
        if (addressEntity == null) {
            if (other.addressEntity != null)
                return false;
        } else if (!addressEntity.equals(other.addressEntity))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (insurance == null) {
            if (other.insurance != null)
                return false;
        } else if (!insurance.equals(other.insurance))
            return false;
        if (carEntities == null) {
            if (other.carEntities != null)
                return false;
        } else if (!carEntities.equals(other.carEntities))
            return false;
        return true;
    }

}
