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
        setTitle("NUCOMP Diagnostic Corporation - Clinical Chemistry by COMSCI");
        setSize(950, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // this is the boderlayout, the size can be adjusted based right here

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("CLINICAL CHEMISTRY LABORATORY", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
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

        nameField = new JTextField();
        ageField = new JTextField();
        lastMealField = new JTextField();
        sexBox = new JComboBox<>(new String[]{"MALE", "FEMALE"});

        dateField = new JTextField(LocalDate.now().toString());//this is a automatic date formater
        timeField = new JTextField(LocalTime.now().withNano(0).toString());// this the same but for time it uses the computer build in time
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

        addTest(panel, "FBS", "74–100", "74–100");
        addTest(panel, "RBS", "-", "-");
        addTest(panel, "Total Cholesterol", "150–200", "150–200");
        addTest(panel, "HDL", "35–80", "42–88");
        addTest(panel, "LDL", "50–130", "50–130");
        addTest(panel, "Triglycerides", "60–165", "40–140");
        addTest(panel, "Creatinine", "0.9–1.3", "0.6–1.2");
        addTest(panel, "Uric Acid", "3.5–7.2", "2.6–6.0");
        addTest(panel, "BUN", "6.0–20.0", "6.0–20.0");
        addTest(panel, "AST/SGOT", "<46", "<46");
        addTest(panel, "ALT/SGPT", "<49", "<49");
        addTest(panel, "Sodium", "135–145", "135–145");
        addTest(panel, "Potassium", "3.5–5.0", "3.5–5.0");
        addTest(panel, "Chloride", "96–110", "96–110");
        addTest(panel, "TCalcium", "8.6–10.28", "8.6–10.28");
        addTest(panel, "iCalcium", "4.4–5.2", "4.4–5.2");

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(900, 400));
        return scroll;
    }

    private void addTest(JPanel panel, String testName, String maleRange, String femaleRange) {
        JCheckBox cb = new JCheckBox(testName);
        JTextField tf = new JTextField();
        JLabel rangeLabel = new JLabel("MALE: " + maleRange + " / FEMALE: " + femaleRange);

        testChecks.add(cb);
        testResults.add(tf);
        testNames.add(testName);
        refMale.add(maleRange);
        refFemale.add(femaleRange);

        panel.add(cb);
        panel.add(tf);
        panel.add(rangeLabel);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        JButton submit = new JButton("Click to Generate Result");
        submit.setFont(new Font("Arial", Font.BOLD, 20));

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

                    String refRange = sex.equals("MALE") ? refMale.get(i) : refFemale.get(i);
                    String interpretation = interpret(value, refRange);

                    result.append(String.format("%-20s = %-8s Ref: %-12s => %s\n",
                            testNames.get(i), valueStr, refRange, interpretation));
                }
            }

            if (!atLeastOneTest) {
                throw new Exception("Please select at least one laboratory test.");
            }

            JTextArea area = new JTextArea(result.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JOptionPane.showMessageDialog(
                    this,
                    new JScrollPane(area),
                    "Laboratory Result",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age and results must be valid numbers.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    // this part will determine if the patient result is normal or high
    private String interpret(double value, String refRange) {
        if (refRange.equals("-")) return "No Range";

        if (refRange.startsWith("<")) {
            double max = Double.parseDouble(refRange.substring(1));
            return value < max ? "NORMAL" : "HIGH";
        }

        String[] parts = refRange.split("–");
        double min = Double.parseDouble(parts[0]);
        double max = Double.parseDouble(parts[1]);

        if (value < min) return "LOW";
        if (value > max) return "HIGH";
        return "NORMAL";
    }

       
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClinicalChemistryApp::new);
    }
}
//---------------------------|
// Partner:                  |
// Narco D. Villando JR.     |
// Cijay Cortez              |
//---------------------------|


// in this program we use jframe for the GUI and we learn about jframe from a youtuber/coder "Bro code"
// this is the video we watch for learning about jframe " https://www.youtube.com/watch?v=7GaAW-DdPuI "
// we use the assistance of AI gpt for the loops and for the test panel
// also for we use copilot for the assistance some of the error trapping 
