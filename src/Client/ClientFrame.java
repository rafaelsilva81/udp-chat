package Client;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;

public class ClientFrame extends javax.swing.JFrame {
        /* make a form using swing */
        /* this form will receive username, ip and port */

        /* public variables */
        public String username;
        public String ip;
        public int port;
        private ClientConnection client;

        public ClientFrame(String ip, int port, String username) {
                /* initialize variables */
                this.username = username;
                this.ip = ip;
                this.port = port;

                try {
                        this.client = new ClientConnection(ip, port);
                        this.client.connect(this.username); /* Preconnection */
                } catch (IOException e) {
                        e.printStackTrace();
                        /* Show joption pane */
                        JOptionPane.showMessageDialog(null, "Could not connect to server");
                        System.exit(0);
                }

                initComponents();
                receive();
        }

        /* initialize components */
        private void initComponents() {
                /* Create the following GUI */

                /* 1. message input */
                messageInput = new javax.swing.JTextField();
                /* 2. message input label */
                messageInputLabel = new javax.swing.JLabel();
                /* 3. send button */
                sendButton = new javax.swing.JButton();
                /* 4. message output */
                messageOutput = new javax.swing.JTextArea();
                /* 5. clear button */
                clearButton = new javax.swing.JButton();

                /* set default close operation */
                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                /* set title */
                setTitle("UDP Client");
                /* set resizable */
                setResizable(false);

                /* initialize label */
                messageInputLabel.setText("Message:");

                /* make layout */
                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

                /* put labels on the buttons */
                sendButton.setText("Send");
                clearButton.setText("Clear");

                /* make messageOutput bigger */
                messageOutput.setColumns(30);
                messageOutput.setRows(10);

                /* make messageOuput not editable */
                messageOutput.setEditable(false);

                /* add outline and padding to messageOutput */
                messageOutput.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                messageOutput.setMargin(new java.awt.Insets(5, 5, 5, 5));

                /* make messageInput smaller */
                messageInput.setColumns(10);

                /* set layout */
                getContentPane().setLayout(layout);

                /* set horizontal group */
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                /* add label above message input */
                                                                                .addComponent(messageInputLabel)
                                                                                .addComponent(messageInput)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(sendButton,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                100,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                100,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(clearButton,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                100,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(messageOutput))
                                                                .addContainerGap()));
                /* set vertical group */
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                /* add label above messageInput */
                                                                .addComponent(messageInputLabel)
                                                                .addComponent(messageInput,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                50,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(sendButton,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                50,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(clearButton,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                50,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                /* make messageOutput bigger */
                                                                .addComponent(messageOutput,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                200,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                /* set frame properties */
                pack();
                setLocationRelativeTo(null);

                /* add action listener to send button */
                sendButton.addActionListener((ActionEvent evt) -> {
                        /* send message */
                        sendButtonActionPerformed(evt);
                });

                /* add action listener to clear button */
                clearButton.addActionListener((ActionEvent evt) -> {
                        /* clear message */
                        clearButtonActionPerformed(evt);
                });

        }

        private void clearButtonActionPerformed(ActionEvent evt) {
                /* clear message */
                messageInput.setText("");
        }

        private void sendButtonActionPerformed(ActionEvent evt) {
                /* get message from messageInput */
                String message = messageInput.getText();
                /* send message */
                this.client.send(this.username + ": " + message);
                /* clear messageInput */
                messageInput.setText("");
        }

        /* receive messages using thread */
        public void receive() {
                System.out.println("Started to receive data");
                /* create thread */
                Thread thread = new Thread(() -> {
                        /* while client is running */
                        while (this.client.isRunning) {
                                /* get message */
                                String message = this.client.receive();
                                System.out.println("mensagem recebida : " + message);
                                /* if message is not null */
                                if (message != null) {
                                        /* append message to messageOutput */
                                        messageOutput.append(message);
                                        /* add new line */
                                        messageOutput.append(System.lineSeparator());
                                }
                        }
                });
                /* start thread */
                thread.start();
        }

        /* variable declaration */
        private javax.swing.JButton clearButton;
        private javax.swing.JTextField messageInput;
        private javax.swing.JLabel messageInputLabel;
        private javax.swing.JTextArea messageOutput;
        private javax.swing.JButton sendButton;

        /* main method */
        public static void main(String args[]) {
                /* set look and feel */
                try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                                        .getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                                | javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(ClientFrame.class.getName())
                                        .log(java.util.logging.Level.SEVERE, null, ex);
                }

                /* create and display the form */
                java.awt.EventQueue.invokeLater(() -> {
                        /* get ip using Joptionpane, defaults to localhost */
                        String ip = JOptionPane.showInputDialog("Enter IP", "127.0.0.1");
                        /* get port using Joptionpane, defaults to 4445 */
                        int port = Integer.parseInt(JOptionPane.showInputDialog("Enter Port", "4445"));
                        /* get username with joption pane, defaults to random hex */
                        String username = JOptionPane.showInputDialog("Enter Username",
                                        Integer.toHexString(new Random().nextInt()));

                        /* create the frame with given data */
                        new ClientFrame(ip, port, username).setVisible(true);
                });
        }

}
