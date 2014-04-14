package tcpclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class ClientGUI extends javax.swing.JFrame {

    boolean isConnected; //true if connected / false if not connected
    Socket socket; // socket to connect with ServerRouter
    ServerSocket serverSocket;
    Socket socketToServer;
    PrintWriter out; // for writing to ServerRouter
    BufferedReader in; // for reading form ServerRouter
    InetAddress localAddress; //used to gather machine network information
    String hostAddress; // Server machine's IP			
    String routerIP; //Server Router address
    int portNumber; // port number
    String serverText; // messages sent to ServerRouter
    String clientText; // messages received from ServerRouter      
    String address; // destination IP (Client)
    PrintWriter writer; //was using to write to a file, will take out
    Reader reader;//used to read the file
    File file;//becomes the file after user selects the one they want
    JFileChooser chooser;//creates file chooser pop up box
    long[] timeCollection;//stores all times for each cycle
    BufferedReader fromFile;

    public ClientGUI() {
        initComponents();

        file = null;

        chooser = new JFileChooser();
        //if size becomes issue might make it dynamic
        timeCollection = new long[2496];

        try {
            
            localAddress = InetAddress.getLocalHost();
            hostAddress = localAddress.getHostAddress(); // Local machine's IP
            IPLabel.setText(hostAddress);//gives user a visual of local IP
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public class ServerStart implements Runnable {
        public void run() {
            
        }
    }

    public class ClientStart implements Runnable {

        public void run() {
            
            try {

                socket = null; // socket to connect with ServerRouter
                Socket socket2 = new  Socket();
                out = null; // for writing to ServerRouter
                in = null; // for reading form ServerRouter
                PrintWriter out2 = null; // for writing to ServerRouter
                BufferedReader in2 = null;
                String incoming;
                routerIP = RouterTextField.getText();
                portNumber = Integer.parseInt(SocketTextField.getText()); // port number
                MessageTextArea.append("Connecting to Router: " + routerIP + " Socket: " + portNumber + "\n");
                // Tries to connect to the ServerRouter
                try {
                    //instead localhost use router name.
                    socket = new Socket(routerIP, portNumber);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    MessageTextArea.append("Connection seccessful \n");
                } catch (UnknownHostException e) {

                    MessageTextArea.append("Don't know about router: " + routerIP + "\n");

                } catch (IOException e) {

                    MessageTextArea.append("Couldn't get I/O for the connection to: " + routerIP + "\n");

                }
                
                out.println("CLIENTCOMM");
                
                incoming = in.readLine();
                MessageTextArea.append(incoming);
                address = DestinationIPTextField.getText();
                out.println(address.toString());
                             
                
                incoming = in.readLine();

                if(incoming.equals("FOUND")){
                    
                     address = in.readLine();
                     portNumber = Integer.parseInt(in.readLine());
                     socketToServer = new Socket(address, portNumber);
                     out = new PrintWriter(socketToServer.getOutputStream(), true);
                     in = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));
                     in.close();
                     out.close();
                     socket.close();
                    //open up a file chooser.  Allows users to pick what file they want
                //to send.
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                }

                reader = new FileReader(file);
                fromFile = new BufferedReader(reader); // reader for the file

                //used for time coolection
                long t0, t1, t;
                int index = 0;

                t0 = System.currentTimeMillis();
                
                //incoming = in.readLine();
                  
                //while loop will run until end of file.
                while ((clientText = fromFile.readLine()) != null) {

                    //used to calc cycle time
                    t1 = System.currentTimeMillis();
                    t = t1 - t0;

                    MessageTextArea.append("Cycle time: " + t + "\n");
                    timeCollection[index] = t;
                    index += 1;

                    if (clientText != null) {

                        MessageTextArea.append("Client: " + clientText + "\n");
                        out2.println(clientText); // sending the strings to the Server via ServerRouter
                        t0 = System.currentTimeMillis();
                    }
                    //I had trouble with the client not waiting for server.
                    //this seemed to fix it.
                    //it keeps the client from going forward until he gets a response.
                    serverText = in2.readLine();
                    MessageTextArea.append(serverText + "\n");
                }

                int totalTime = 0;
                MessageTextArea.append("Times: \n");
                for (int i = 0; i < index; i += 1) {
                    totalTime += totalTime + timeCollection[i];
                    MessageTextArea.append(timeCollection[i] + ", ");
                }

                MessageTextArea.append("\n Total Time: " + totalTime);
                }
                else
                {
                    MessageTextArea.append("Unable to find server");
                }
                
               

                

                //closing connections
                out2.close();
                in2.close();
                socketToServer.close();

            } catch (Exception ex) {
                Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        MessageTextArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        DestinationIPTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        SocketTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        RouterTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        IPLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        MessageTextArea.setColumns(20);
        MessageTextArea.setRows(5);
        jScrollPane1.setViewportView(MessageTextArea);

        jLabel1.setText("Client");

        jLabel2.setText("Destination IP:");

        jLabel3.setText("Socket: ");

        SocketTextField.setText("5555");

        jLabel4.setText("Router Address:");

        jLabel5.setText("Client Address:");

        IPLabel.setText("127.0.0.1");

        jButton1.setText("Connect");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IPLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2)
                                .addComponent(jLabel4)
                                .addComponent(RouterTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                .addComponent(DestinationIPTextField)
                                .addComponent(jLabel3)
                                .addComponent(SocketTextField))
                            .addComponent(jButton1))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(4, 4, 4)
                        .addComponent(DestinationIPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RouterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SocketTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jButton1)))
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(IPLabel))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (!isConnected) {
            Thread starter = new Thread(new ClientStart());
            starter.start();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientGUI().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DestinationIPTextField;
    private javax.swing.JLabel IPLabel;
    private javax.swing.JTextArea MessageTextArea;
    private javax.swing.JTextField RouterTextField;
    private javax.swing.JTextField SocketTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
