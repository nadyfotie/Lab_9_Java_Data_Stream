import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamFrame extends JFrame {
    private JTextArea originalTextArea = new JTextArea();
    private JTextArea newTextArea = new JTextArea();
    private JButton loadButton = new JButton("Load");
    private JButton searchButton = new JButton("Search");
    private JButton quitButton = new JButton("Quit");
    private JTextField searchField = new JTextField(20);
    private Path filePath;

    public DataStreamFrame() {
        setTitle("Data Stream");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        GridLayout gridcenter = new GridLayout(1,2);
        JPanel centralPanel = new JPanel(gridcenter);
        JPanel buttonPanel = new JPanel(new GridLayout(1,3));
        JPanel searchPanel = new JPanel();

        centralPanel.add(new JScrollPane(originalTextArea));
        centralPanel.add(new JScrollPane(newTextArea));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        buttonPanel.add(loadButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(quitButton);


        add(centralPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(searchPanel, BorderLayout.NORTH);

        loadButton.addActionListener( e -> loadFile());
        searchButton.addActionListener(e -> searchFile());
        quitButton.addActionListener(e -> System.exit(0));

        searchButton.setEnabled(false);

    }
    public void loadFile(){
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            filePath = chooser.getSelectedFile().toPath();
            try {
                String text = Files.readString(filePath);
                originalTextArea.setText(text);
                newTextArea.setText(" ");
                searchButton.setEnabled(true);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Could not open the file " + e.getMessage());
            }
        }
    }
    private void searchFile() {
        String word = searchField.getText();

        try (Stream<String> lines = Files.lines(filePath)) {
            String results = lines
                    .filter(line -> line.contains(word))
                    .collect(Collectors.joining("\n"));
            newTextArea.setText(results);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not search the file: " + ex.getMessage());
        }
    }


}