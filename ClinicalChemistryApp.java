import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClinicalChemistryApp extends JFrame {

    private JTextField nameField, ageField, lastMealField;
    private JTextField dateField, timeField;
    private JComboBox<String> sexBox;

    private List<JCheckBox> testChecks = new ArrayList<>();
    private List<JTextField> testResults = new ArrayList<>();
    private List<String> testNames = new ArrayList<>();
    private List<String> refMale = new ArrayList<>();
    private List<String> refFemale = new ArrayList<>();

    //this is the header
    public ClinicalChemistryApp() {
        // Global font override for readability
        UIManager.put("Label.font", new Font("Arial", Font.BOLD, 20));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 24));
        UIManager.put("ComboBox.font", new Font("Arial", Font.PLAIN, 20));
        UIManager.put("CheckBox.font", new Font("Arial", Font.PLAIN, 20));
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 20));
        UIManager.put("TitledBorder.font", new Font("Arial", Font.BOLD, 24));

        setTitle("NUCOMP Diagnostic Corporation - Clinical Chemistry by COMSCI");
        setSize(1280, 720);// this is the screen size, it can be edited to adjust the size of the application
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(30, 30)); 

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("CLINICAL CHEMISTRY LABORATORY", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32)); // header font
        header.add(title, BorderLayout.CENTER);
        return header;
    }

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(15, 10));
        center.add(buildPatientPanel(), BorderLayout.NORTH);
        center.add(buildTestPanel(), BorderLayout.CENTER);
        return center;
    }

    // this is for the patient info panel
    private JPanel buildPatientPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Patient Information"));

        Font inputFont = new Font("Arial", Font.PLAIN, 24);

        nameField = new JTextField();
        nameField.setFont(inputFont);

        ageField = new JTextField();
        ageField.setFont(inputFont);

        lastMealField = new JTextField();
        lastMealField.setFont(inputFont);

        sexBox = new JComboBox<>(new String[]{"MALE", "FEMALE"});
        sexBox.setFont(inputFont);

        dateField = new JTextField(LocalDate.now().toString());//this is a automatic date formater
        dateField.setFont(inputFont);
        timeField = new JTextField(LocalTime.now().withNano(0).toString());// this the same but for time it uses the computer build in time
        timeField.setFont(inputFont);
        dateField.setEditable(false);
        timeField.setEditable(false);

        panel.add(new JLabel("Patient Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Date of Collection:"));
        panel.add(dateField);

        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Time of Collection:"));
        panel.add(timeField);

        panel.add(new JLabel("Patient Sex:"));
        panel.add(sexBox);
        panel.add(new JLabel("Time of Last Meal:"));
        panel.add(lastMealField);

        return panel;
    }

    // this is the patient panel it show the available test that the user can input
    private JScrollPane buildTestPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Clinical Chemistry Tests"));

        addTest(panel, "FBS - mg/dL", "74–100", "74–100");
        addTest(panel, "RBS - mg/dL", "70–140", "70–140");
        addTest(panel, "Total Cholesterol - mg/dL", "150–200", "150–200");
        addTest(panel, "HDL - mg/dL", "35–80", "42–88");
        addTest(panel, "LDL - mg/dL", "50–130", "50–130");
        addTest(panel, "Triglycerides - mg/dL", "60–165", "40–140");
        addTest(panel, "Creatinine - mg/dL", "0.9–1.3", "0.6–1.2");
        addTest(panel, "Uric Acid - mg/dL", "3.5–7.2", "2.6–6.0");
        addTest(panel, "BUN - mg/dL", "6.0–20.0", "6.0–20.0");
        addTest(panel, "AST/SGOT - U/L", "<46", "<46");
        addTest(panel, "ALT/SGPT - U/L", "<49", "<49");
        addTest(panel, "Sodium - mEq/L", "135–145", "135–145");
        addTest(panel, "Potassium - mEq/L", "3.5–5.0", "3.5–5.0");
        addTest(panel, "Chloride - mEq/L", "96–110", "96–110");
        addTest(panel, "TCalcium - mg/dL", "8.6–10.28", "8.6–10.28");
        addTest(panel, "iCalcium - mg/dL", "4.4–5.2", "4.4–5.2");

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(1000, 450));
        return scroll;
    }

    private void addTest(JPanel panel, String testName, String maleRange, String femaleRange) {
        JCheckBox cb = new JCheckBox(testName);
        cb.setFont(new Font("Arial", Font.PLAIN, 24));

        JTextField tf = new JTextField();
        tf.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel rangeLabel = new JLabel("MALE: " + maleRange + " | FEMALE: " + femaleRange);
        rangeLabel.setFont(new Font("Arial", Font.ITALIC, 14));

        testChecks.add(cb);
        testResults.add(tf);
        testNames.add(testName);
        refMale.add(maleRange);
        refFemale.add(femaleRange);

        panel.add(cb);
        panel.add(tf);
        panel.add(rangeLabel);
    }

    //this method interpret the test result LOW NORMAL HIGH
    private String interpretResult(double value, String refRange) {
        try {
            //this is for reference range like "74–100"
            if (refRange.contains("–")) {
                String[] parts = refRange.replace("<", "").split("–");
                double min = Double.parseDouble(parts[0].trim());
                double max = Double.parseDouble(parts[1].trim());

                if (value < min) return "LOW";
                if (value > max) return "HIGH";
                return "NORMAL";
            }
            //this is for reference range like "<46"
            else if (refRange.contains("<")) {
                double max = Double.parseDouble(refRange.replace("<", "").trim());
                if (value > max) return "HIGH";
                return "NORMAL";
            }
        } 
        catch (Exception e) {
            return "N/A"; // if reference cannot be parsed
        }

        return "N/A";
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        JButton submit = new JButton("Click here to Generate Result");
        submit.setFont(new Font("Arial", Font.BOLD, 30));

        submit.addActionListener(e -> processData());
        panel.add(submit);

        return panel;
    }

    //this is for the error trapping to make sure there is no missing or empty input 
    private void processData() {
        try {
            if (nameField.getText().trim().isEmpty() || ageField.getText().trim().isEmpty()) {
                throw new Exception("Patient name and age are required.");
            }

            int age = Integer.parseInt(ageField.getText().trim());
            String sex = sexBox.getSelectedItem().toString();

            Patient patient = new Patient(
                    nameField.getText().trim(),
                    age,
                    sex,
                    lastMealField.getText().trim()
            );

            //this is for the result output
            StringBuilder result = new StringBuilder();
            result.append("PATIENT INFORMATION\n");
            result.append("-------------------\n");
            result.append(patient.toString()).append("\n");
            result.append("Date: ").append(dateField.getText()).append("\n");
            result.append("Time: ").append(timeField.getText()).append("\n\n");

            result.append("TEST RESULTS\n");
            result.append("------------\n");

            boolean atLeastOneTest = false;

            //this is also for the error trapping to make sure th information is not leave empty
            for (int i = 0; i < testChecks.size(); i++) {
                if (testChecks.get(i).isSelected()) {
                    atLeastOneTest = true;

                    String valueStr = testResults.get(i).getText().trim();
                    if (valueStr.isEmpty()) {
                        throw new Exception("Please enter a result for: " + testNames.get(i));
                    }

                    double value = Double.parseDouble(valueStr);

                    patient.addTestResult(testNames.get(i), value);

                    //this is for showing reference range based on sex
                    String refRange = sex.equals("MALE") ? refMale.get(i) : refFemale.get(i);

                    //this is for interpretation LOW NORMAL HIGH
                    String interpretation = interpretResult(value, refRange);

                    result.append(testNames.get(i))
                          .append(" = ")
                          .append(value)
                          .append(" | Ref: ")
                          .append(refRange)
                          .append(" | Result: ")
                          .append(interpretation)
                          .append("\n");
                }
            }

            if (!atLeastOneTest) {
                throw new Exception("Please select at least one test.");
            }

            //this is the output popup
            JTextArea outputArea = new JTextArea(result.toString());
            outputArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
            outputArea.setEditable(false);

            JOptionPane.showMessageDialog(this, new JScrollPane(outputArea),
                    "Laboratory Result", JOptionPane.INFORMATION_MESSAGE);

        } 
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age and test results must be numbers!", "Error", JOptionPane.ERROR_MESSAGE);
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //this is the main method to run the system
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClinicalChemistryApp());
    }
}

//---------------------------|
// Partner:                  |
// Narco D. Villando JR.     |
// Cijay Cortez              |
//---------------------------|


// in this program we use java swing for the GUI and we learn about jframe from a youtuber/coder "Bro code"
// this is the video we watch for learning about jframe " https://www.youtube.com/watch?v=7GaAW-DdPuI "
// also we use copilot for the assistance some of the error trapping

