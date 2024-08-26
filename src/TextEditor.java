import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor extends JFrame implements ActionListener {

    JTextArea textarea;
    JScrollPane scrollpane;
    JSpinner fontSizeSpinner;
    JLabel fontLabel;
    JButton fontColorButton;
    JComboBox fontBox;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    TextEditor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // frame close of close button click
        this.setTitle("Text Editor");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null); // setting the frame to appear on the center
        this.setLayout(new FlowLayout()); // setting the layout manager of the frame

        textarea = new JTextArea();
        //textarea.setPreferredSize(new Dimension(450, 450)); // set the size of the textarea
        textarea.setLineWrap(true); // line wrap when line length finished
        textarea.setWrapStyleWord(true);
        textarea.setFont(new Font("Arial", Font.PLAIN, 20));

        scrollpane = new JScrollPane(textarea);
        scrollpane.setPreferredSize(new Dimension(450, 450));
        scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // set scroll bar to vertical

        fontLabel = new JLabel();
        fontLabel.setText("Font : ");

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.setValue(20); // setting initial value
        fontSizeSpinner.addChangeListener(new ChangeListener() { // changing the size of the font by the spinner value
            @Override
            public void stateChanged(ChangeEvent e) {
                textarea.setFont(new Font(textarea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
            }
        });

        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this); // adding the action listener to perform some kind of action when clicked

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); // getting all the fonts to the array

        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollpane);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fontColorButton) {
            JColorChooser colorChooser = new JColorChooser(); // making a color chooser

            Color color = JColorChooser.showDialog(null, "Choose a Color", Color.black); // choosing a color from a dialog box, default is black

            textarea.setForeground(color); // setting the color choosen from the dialog box to the editor
        }
        if (e.getSource() == fontBox) {
            textarea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textarea.getFont().getSize()));
        }

        if (e.getSource() == openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);

            if(response == JFileChooser.APPROVE_OPTION)
            {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;

                try {
                    fileIn = new Scanner(file);
                    if(file.isFile())
                    {
                        while(fileIn.hasNextLine())
                        {
                            String line = fileIn.nextLine()+"\n";
                            textarea.setText("");
                            textarea.append(line);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    fileIn.close();
                }
            }
        }
        if (e.getSource() == saveItem) {
            JFileChooser fileChooser = new JFileChooser(); // making a file chooser
            fileChooser.setCurrentDirectory(new File(".")); // default directory

            int response = fileChooser.showSaveDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try (PrintWriter fileOut = new PrintWriter(file)) {
                    fileOut.println(textarea.getText());
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getSource() == exitItem) {
                System.exit(0);
            }
        }
    }
}
