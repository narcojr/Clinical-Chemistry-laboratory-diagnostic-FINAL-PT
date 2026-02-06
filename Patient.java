
public class Patient {

    private String name;
    private int age;
    private char sex; // this is for the gender for the Patient Male or female 

    public Patient(String name, int age, char sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public char getSex() {
        return sex;
    }
}



