import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClinicalChemistryGUI extends JFrame {

    //this is for the Patient info
    private JTextField nameField = new JTextField(12);
    private JTextField ageField = new JTextField(5);
    private JComboBox<String> sexBox = new JComboBox<>(new String[]{"MALE", "FEMALE"});

    //this is for the Collection info
    private JTextField dateField = new JTextField(10);
    private JTextField timeCollectionField = new JTextField(8);
    private JTextField lastMealField = new JTextField(8);

    //this is for the Output
    private JTextArea outputArea = new JTextArea(12, 40);

    // this is for Test storage (used for the loops)
    private ArrayList<JCheckBox> testChecks = new ArrayList<>();
    private ArrayList<JTextField> testFields = new ArrayList<>();
    private ArrayList<String> testNames = new ArrayList<>();
    private ArrayList<double[]> ranges = new ArrayList<>();

    public ClinicalChemistryGUI() {
        setTitle("NUCOMP DIAGNOSTIC CORPORATION");
        setSize(760, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // this is for the automatic date and time record
        dateField.setText(LocalDate.now().toString());
        timeCollectionField.setText(
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        );

        dateField.setEditable(false);
        timeCollectionField.setEditable(false);

        // this is for the user or patient panel
        JPanel patientPanel = new JPanel(new GridLayout(2, 1));
        patientPanel.setBorder(BorderFactory.createTitledBorder("Patient Information"));

        JPanel row1 = new JPanel();
        row1.add(new JLabel("Name:"));
        row1.add(nameField);
        row1.add(new JLabel("Age:"));
        row1.add(ageField);
        row1.add(new JLabel("Sex:"));
        row1.add(sexBox);

        JPanel row2 = new JPanel();
        row2.add(new JLabel("Date of Collection:"));// this automaticly produce by the program using "DateTimeFormatter"
        row2.add(dateField);
        row2.add(new JLabel("Time of Collection:"));// this automaticly produce by the program using "DateTimeFormatter"
        row2.add(timeCollectionField);
        row2.add(new JLabel("Time of Last Meal:"));// this one is the user will manually input the time
        row2.add(lastMealField);

        patientPanel.add(row1);
        patientPanel.add(row2);
        add(patientPanel, BorderLayout.NORTH);

        // this is for the test panel
        JPanel testPanel = new JPanel(new GridLayout(0, 3));
        testPanel.setBorder(BorderFactory.createTitledBorder("Select Tests"));

        addTest("FBS", 74,100,74,100, testPanel);
        addTest("RBS", 0,200,0,200, testPanel);
        addTest("Total Cholesterol", 150,200,150,200, testPanel);
        addTest("HDL", 35,80,42,88, testPanel);
        addTest("LDL", 50,130,50,130, testPanel);
        addTest("Triglycerides", 60,165,40,140, testPanel);
        addTest("Creatinine", 0.9,1.3,0.6,1.2, testPanel);
        addTest("Uric Acid", 3.5,7.2,2.6,6.0, testPanel);

        add(testPanel, BorderLayout.CENTER);

        // this is for the output panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton generateBtn = new JButton("Click to Generate the Results");

        outputArea.setEditable(false);
        bottomPanel.add(generateBtn, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        generateBtn.addActionListener(e -> generateResults());

        setVisible(true);
    }

    // Reusable test creator
    private void addTest(String name,
                         double mLow, double mHigh,
                         double fLow, double fHigh,
                         JPanel panel) {

        JCheckBox check = new JCheckBox(name);
        JTextField field = new JTextField(5);

        testChecks.add(check);
        testFields.add(field);
        testNames.add(name);
        ranges.add(new double[]{mLow, mHigh, fLow, fHigh});

        panel.add(check);
        panel.add(new JLabel("Result:"));
        panel.add(field);
    }

    // Main logic
    private void generateResults() {

        outputArea.setText("");

        // this is for the error trapping this make sure no user input leave empty
        if (nameField.getText().isEmpty() ||
            ageField.getText().isEmpty() ||
            lastMealField.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please complete all required fields.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid age input.");
            return;
        }

        Patient patient = new Patient(
                nameField.getText(),
                age,
                sexBox.getSelectedItem().toString().charAt(0)
        );

        // DISPLAY HEADER 
        outputArea.append("Patient Name: " + patient.getName() + "\n");
        outputArea.append("Age: " + patient.getAge() + " | Sex: " + patient.getSex() + "\n");
        outputArea.append("Date of Collection: " + dateField.getText() + "\n");
        outputArea.append("Time of Collection: " + timeCollectionField.getText() + "\n");
        outputArea.append("Time of Last Meal: " + lastMealField.getText() + "\n\n");

        boolean selected = false;

        // LOOP THROUGH TESTS 
        for (int i = 0; i < testChecks.size(); i++) {

            if (testChecks.get(i).isSelected()) {
                selected = true;

                try {
                    double value = Double.parseDouble(testFields.get(i).getText());
                    double[] r = ranges.get(i);

                    double low = (patient.getSex() == 'M') ? r[0] : r[2];
                    double high = (patient.getSex() == 'M') ? r[1] : r[3];

                    String interpretation =
                            (value < low) ? "LOW" :
                            (value > high) ? "HIGH" : "NORMAL";

                    outputArea.append(testNames.get(i) + "\n");
                    outputArea.append("Result: " + value + "\n");
                    outputArea.append("Reference Range: " + low + " - " + high + "\n");
                    outputArea.append("Interpretation: " + interpretation + "\n\n");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid result for " + testNames.get(i));
                    return;
                }
            }
        }

        if (!selected) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one test.");
        }
    }

    public static void main(String[] args) {
        new ClinicalChemistryGUI();
    }
}
// in this program we use jframe for the GUI and we learn about jframe from a youtuber/coder "Bro code"
// this is the video we watch for learning about jframe " https://www.youtube.com/watch?v=7GaAW-DdPuI "
// we use the assistance of AI gpt for the loops and for the test panel
