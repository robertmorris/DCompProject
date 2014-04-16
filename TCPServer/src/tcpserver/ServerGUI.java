
package tcpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;


public class ServerGUI extends javax.swing.JFrame {

    Socket socket; // socket to connect with ServerRouter
    Socket socket2;
    ServerSocket serverSocket;
    int serverPortNumber;
    PrintWriter out; // for writing to ServerRouter
    BufferedReader in; // for reading form ServerRouter
    PrintWriter out2;
    BufferedReader in2;
    InetAddress localAddress;//used to grab local machines ip
    String hostAddress; // Server machine's IP			
    String routerIP;//address to the server router
    int routerPortNumber; // port number
    boolean isConnected;//used for connection status
    String serverText; // messages sent to ServerRouter
    String clientText; // messages received from ServerRouter      
    String address; // destination IP 
    Object[][] RouteTable;

    public ServerGUI() {

        initComponents();

        try {

            localAddress = InetAddress.getLocalHost();
            hostAddress = localAddress.getHostAddress(); // Local machine's IP
            IPLabel.setText(hostAddress);//display to GUI the IP

        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public class Listener implements Runnable {
        
        public void run() {
            while(true)
                    {
                        try {
                            
                            serverPortNumber = Integer.parseInt(ServerSocketField.getText());                
                            serverSocket = new ServerSocket(serverPortNumber);
                            
                            socket2 = serverSocket.accept();
                            
                            out2 = new PrintWriter(socket2.getOutputStream(), true);
                            in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
                            
                             
                    try {

                        // Communication process (initial sends/receives)
                        //out2.println("GO");// initial send (IP of the destination Client)

                     
                        // Communication while loop.  Will run as long as
                        //information is coming from client via the server router.
                        while ((clientText = in2.readLine()) != null) {
                            
                            //This takes in text sent and capitalizes it then
                            //sends it back to the server router which forwards
                            //it to the client
                            MessageTextArea.append("Client said: " + clientText + "\n");
                            // converting received message to upper case
                            serverText = clientText.toUpperCase(); 
                            
                            MessageTextArea.append("Server said: " + serverText + "\n");
                            // sending the converted message back to the Client via ServerRouter
                            out2.println(serverText); 
                        }
                    

                        //closing connections
                        out2.close();
                        in2.close();
                        socket2.close();
                        serverSocket.close();
                
                    } catch (IOException ex) {
                        Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        
                            
                        
                } catch (IOException ex) {
                    Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                    }
        }
        
    }

    public class ServerStart implements Runnable {

        public void run() {
            try {

                if (isConnected == false) {
                    
                    
                    
                    //set router IP and Socket from GUI textbox inputs
                    routerIP = RouterAddressTextField.getText();
                    routerPortNumber = Integer.parseInt(SocketTextField.getText()); // port number

                    MessageTextArea.append("Trying to connect. Router: " + routerIP + " Socket: " + routerPortNumber + "\n");
                    // Tries to connect to the ServerRouter
                    try {
                        
                        socket = new Socket(routerIP, routerPortNumber);
                        out = new PrintWriter(socket.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        MessageTextArea.append("Connection completed. \n");
                        
                    } catch (UnknownHostException e) {

                        MessageTextArea.append("unable to find router: " + routerIP + "\n");
                        e.printStackTrace();
                        
                    } catch (IOException e) {

                        MessageTextArea.append("Could not connect to: " + routerIP + "\n");
                        e.printStackTrace();
                        
                    }
      
                    out.println("SERVERCOMM");
                    MessageTextArea.append(in.readLine());
                    out.println(Integer.parseInt(ServerSocketField.getText()));
                    String incoming = "";
                    incoming = in.readLine();
                    
                        if(incoming == "BYE")
                        {
                            MessageTextArea.append("Listening for Connections"); 
                            
                            out.close();
                            in.close();
                            socket.close();
                        }
                        
      
                }
            } catch (Exception ex) {
                Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        SocketTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        RouterAddressTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        IPLabel = new javax.swing.JLabel();
        ConnectBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        MessageTextArea = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ServerSocketField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Router Socket:");

        jLabel2.setText("Router Address:");

        jLabel3.setText("Current Server Address:");

        IPLabel.setText("127.0.0.1");

        ConnectBtn.setText("Connect");
        ConnectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectBtnActionPerformed(evt);
            }
        });

        MessageTextArea.setColumns(20);
        MessageTextArea.setLineWrap(true);
        MessageTextArea.setRows(5);
        jScrollPane1.setViewportView(MessageTextArea);

        jLabel5.setText("Server");

        jLabel6.setText("Server Socket:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ServerSocketField)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(ConnectBtn)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel2)
                                            .addComponent(RouterAddressTextField)
                                            .addComponent(jLabel1)
                                            .addComponent(SocketTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel6)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(IPLabel)))
                        .addGap(12, 12, 12))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RouterAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SocketTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ServerSocketField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(ConnectBtn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(IPLabel))
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ConnectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectBtnActionPerformed

        if (!isConnected) {
            Thread starter = new Thread(new ServerStart());
            starter.start();
            Thread listen  = new Thread(new Listener());
            listen.start();
        }

    }//GEN-LAST:event_ConnectBtnActionPerformed

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
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ConnectBtn;
    private javax.swing.JLabel IPLabel;
    private javax.swing.JTextArea MessageTextArea;
    private javax.swing.JTextField RouterAddressTextField;
    private javax.swing.JTextField ServerSocketField;
    private javax.swing.JTextField SocketTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
