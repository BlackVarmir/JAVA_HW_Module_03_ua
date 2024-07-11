package model;

import static java.lang.System.out;

import java.util.List;

public class Person {
    private String id;
    private String name;
    private String city;
    private List<Fine> fines;

    public Person(String id, String name, String city, List<Fine> fines) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.fines = fines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Fine> getFines() {
        return fines;
    }

    public void setFines(List<Fine> fines) {
        this.fines = fines;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", fines=" + fines +
                '}';
    }
}
