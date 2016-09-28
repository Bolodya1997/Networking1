package ru.nsu.fit.bolodya.broadcaster;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {

    private JPanel startPane;
    private JPanel workPane;

    private JLabel label;
    private int previous = -1;

    public View() {
        super("Broadcaster");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(500, 300, 200, 100);
        setResizable(false);

        initStartPane();

        initWorkPane();

        setContentPane(startPane);
        setVisible(true);
    }

    private void initStartPane() {
        startPane = new JPanel(new GridLayout(2, 1));
        add(startPane);

        JTextField portField = new JTextField("Port number");
        portField.setHorizontalAlignment(SwingConstants.CENTER);
        portField.selectAll();
        startPane.add(portField);

        JButton startButton = new JButton("Start");
        startButton.setHorizontalAlignment(SwingConstants.CENTER);
        startButton.addActionListener(e -> {
            setContentPane(workPane);
            new Thread(() -> {
                try {
                    new Broadcaster(Integer.decode(portField.getText()), this);
                }
                catch (Exception e1) {
                    setContentPane(startPane);
                }
            }).start();
        });
        startPane.add(startButton);
    }

    private void initWorkPane() {
        workPane = new JPanel(new GridLayout(1, 1));
        add(workPane);

        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        update(0);
        workPane.add(label);
    }

    void update(int amount) {
        if (previous == amount)
            return;

        previous = amount;
        label.setText(String.format("There are %d of Me", amount));
    }
}
