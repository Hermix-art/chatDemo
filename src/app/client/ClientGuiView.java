package app.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class ClientGuiView {
    private final ClientGuiController controller;

    private JFrame frame = new JFrame("Simple chat");
    private JTextField textField = new JTextField(50);
    private JTextArea messagesArea = new JTextArea(10, 50);
    private JTextArea usersArea = new JTextArea(10, 15);

    public ClientGuiView(ClientGuiController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        textField.setEditable(false);
        messagesArea.setEditable(false);
        usersArea.setEditable(false);

        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messagesArea), BorderLayout.WEST);
        frame.getContentPane().add(new JScrollPane(usersArea), BorderLayout.EAST);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sendTextMessage(textField.getText());
                textField.setText("");
            }
        });

    }

    private String getServerAddress() {
        return JOptionPane.showInputDialog(frame,
                "Provide server address",
                "Client configuration",
                JOptionPane.QUESTION_MESSAGE);
    }

    private int getPort() {
        while (true) {
            String portString = JOptionPane.showInputDialog(frame,
                    "Provide port",
                    "Client configuration",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                return Integer.parseInt(portString.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame,
                        "Inappropriate port, please try again",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private String getUserName() {
        return JOptionPane.showInputDialog(frame,
                "Provide your name",
                "Client configuration",
                JOptionPane.QUESTION_MESSAGE);
    }

    private void refreshUsers() {
        StringBuilder sb = new StringBuilder();
        Set<String> users = controller.getModel().getCurrentUsers();
        for (String user : users) {
            sb.append(user).append("\n");
        }
        usersArea.setText(sb.toString());

    }

    private void refreshMessage() {
        String lastMessage = controller.getModel().getLastAvailableMessage() + "\n";
        messagesArea.append(lastMessage);
    }

    private void updateConnectionStatus(boolean isConnected) {
        textField.setEditable(isConnected);
        if (isConnected) {
            JOptionPane.showMessageDialog(frame,
                    "Client is connected",
                    "Chat",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Something went wrong: client is not connected",
                    "Chat",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

}
