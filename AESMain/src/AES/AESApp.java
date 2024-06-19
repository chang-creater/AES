/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AES;

/**
 *
 * @author Admin
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AESApp extends JFrame {

    private JTextField keyTextField;
    private JTextArea encryptInputTextArea;
    private JTextArea encryptOutputTextArea;
    private JTextArea decryptInputTextArea;
    private JTextArea decryptOutputTextArea;
    private JButton generateKeyButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton switchButton;
    private JButton encryptFileButton;
    private JButton decryptFileButton;
    private JButton saveEncryptFileButton;
    private JButton saveDecryptFileButton;
    private byte[] key;

    public AESApp() {
        setTitle("AES Encryption/Decryption");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Encryption panel
        JPanel encryptPanel = new JPanel(new BorderLayout());
        encryptPanel.setBorder(BorderFactory.createTitledBorder("Mã hóa"));

        JPanel encryptInputPanel = new JPanel(new FlowLayout());
        encryptInputPanel.add(new JLabel("Bản rõ:"));
        encryptInputTextArea = new JTextArea(5, 20);
        encryptInputPanel.add(new JScrollPane(encryptInputTextArea));
        encryptFileButton = new JButton("File");
        encryptInputPanel.add(encryptFileButton);
        encryptPanel.add(encryptInputPanel, BorderLayout.NORTH);

        encryptButton = new JButton("Mã hóa");
        encryptPanel.add(encryptButton, BorderLayout.CENTER);

        JPanel encryptOutputPanel = new JPanel(new FlowLayout());
        encryptOutputPanel.add(new JLabel("Bản mã:"));
        encryptOutputTextArea = new JTextArea(5, 20);
        encryptOutputTextArea.setEditable(false);
        encryptOutputPanel.add(new JScrollPane(encryptOutputTextArea));
        saveEncryptFileButton = new JButton("Lưu");
        encryptOutputPanel.add(saveEncryptFileButton);
        switchButton = new JButton("Chuyển");
        encryptOutputPanel.add(switchButton);
        encryptPanel.add(encryptOutputPanel, BorderLayout.SOUTH);

        mainPanel.add(encryptPanel);

        // Decryption panel
        JPanel decryptPanel = new JPanel(new BorderLayout());
        decryptPanel.setBorder(BorderFactory.createTitledBorder("Giải mã"));

        JPanel decryptInputPanel = new JPanel(new FlowLayout());
        decryptInputPanel.add(new JLabel("Bản mã:"));
        decryptInputTextArea = new JTextArea(5, 20);
        decryptInputPanel.add(new JScrollPane(decryptInputTextArea));
        decryptFileButton = new JButton("File");
        decryptInputPanel.add(decryptFileButton);
        decryptPanel.add(decryptInputPanel, BorderLayout.NORTH);

        decryptButton = new JButton("Giải mã");
        decryptPanel.add(decryptButton, BorderLayout.CENTER);

        JPanel decryptOutputPanel = new JPanel(new FlowLayout());
        decryptOutputPanel.add(new JLabel("Bản rõ:"));
        decryptOutputTextArea = new JTextArea(5, 20);
        decryptOutputTextArea.setEditable(false);
        decryptOutputPanel.add(new JScrollPane(decryptOutputTextArea));
        saveDecryptFileButton = new JButton("Lưu");
        decryptOutputPanel.add(saveDecryptFileButton);
        decryptPanel.add(decryptOutputPanel, BorderLayout.SOUTH);

        mainPanel.add(decryptPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Key generation
        JPanel keyPanel = new JPanel(new FlowLayout());
        keyPanel.add(new JLabel("Khóa tự sinh:"));
        keyTextField = new JTextField(30);
        keyTextField.setEditable(false);
        keyPanel.add(keyTextField);
        generateKeyButton = new JButton("Generate Key");
        keyPanel.add(generateKeyButton);
        add(keyPanel, BorderLayout.NORTH);

        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateKey();
            }
        });

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encryptText();
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decryptText();
            }
        });

        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchText();
            }
        });

        encryptFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encryptFile();
            }
        });

        decryptFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decryptFile();
            }
        });

        saveEncryptFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(encryptOutputTextArea.getText(), "Lưu file mã hóa");
            }
        });

        saveDecryptFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(decryptOutputTextArea.getText(), "Lưu file giải mã");
            }
        });
    }

    private void generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // for example, you can use 128 or 192 bits also
            SecretKey secretKey = keyGen.generateKey();
            key = secretKey.getEncoded();
            keyTextField.setText(Base64.getEncoder().encodeToString(key));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void encryptText() {
        try {
            String plainText = encryptInputTextArea.getText();
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
            encryptOutputTextArea.setText(encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decryptText() {
        try {
            String encryptedText = decryptInputTextArea.getText();
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
            decryptOutputTextArea.setText(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchText() {
        String encryptedText = encryptOutputTextArea.getText();
        decryptInputTextArea.setText(encryptedText);
    }

    private void encryptFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                String fileContent = readFileContent(file);
                encryptInputTextArea.setText(fileContent);
                encryptText();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void decryptFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                String fileContent = readFileContent(file);
                decryptInputTextArea.setText(fileContent);
                decryptText();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String readFileContent(File file) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        }
        return fileContent.toString();
    }

    private void saveFile(String content, String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(dialogTitle);
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AESApp().setVisible(true);
            }
        });
    }
}



