// This is the Patient class used in ClinicalChemistryApp
import java.util.HashMap;
import java.util.Map;

public class Patient {
    private String name;
    private int age;
    private String sex;          // "MALE" or "FEMALE"
    private String timeLastMeal;
    private Map<String, Double> testResults; // store test name + result

    // Constructor
    public Patient(String name, int age, String sex, String timeLastMeal) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.timeLastMeal = timeLastMeal;
        this.testResults = new HashMap<>();
    }

    // Add a test result
    public void addTestResult(String testName, double value) {
        testResults.put(testName, value);
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getSex() { return sex; }
    public String getTimeLastMeal() { return timeLastMeal; }
    public Map<String, Double> getTestResults() { return testResults; }

    // For easy display of patient info
    @Override
    public String toString() {
        return "Name: " + name + "\n" +
               "Age: " + age + "\n" +
               "Sex: " + sex + "\n" +
               "Last Meal: " + timeLastMeal;
    }
}




