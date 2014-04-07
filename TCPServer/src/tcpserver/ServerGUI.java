
package tcpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;


public class ServerGUI extends javax.swing.JFrame {

    Socket socket; // socket to connect with ServerRouter
    PrintWriter out; // for writing to ServerRouter
    BufferedReader in; // for reading form ServerRouter
    InetAddress localAddress;//used to grab local machines ip
    String hostAddress; // Server machine's IP			
    String routerIP;//address to the server router
    int portNumber; // port number
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

    public class ServerStart implements Runnable {

        public void run() {
            try {

                if (isConnected == false) {
                    
                    RouteTable = new Object[20][2];
                    
                    //set router IP and Socket from GUI textbox inputs
                    routerIP = RouterAddressTextField.getText();
                    portNumber = Integer.parseInt(SocketTextField.getText()); // port number

                    MessageTextArea.append("Trying to connect. Router: " + routerIP + " Socket: " + portNumber + "\n");
                    // Tries to connect to the ServerRouter
                    try {
                        
                        socket = new Socket(routerIP, portNumber);
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
      
                    out.println("CLIENTCOMM");
                    String incoming = "";
                    int tableIndex = 0;
                    while(true)
                    {
                        incoming = in.readLine();
                        
                        if(incoming == "BYE")
                            break;
                        
                        RouteTable[tableIndex][0] = incoming;
                        incoming = in.readLine();
                        RouteTable[tableIndex][1] = incoming;
                        tableIndex++;
                        
                    }
                    
                    DefaultListModel listModel = new DefaultListModel();
                    
                    for(int i=0; i<RouteTable.length; i++){
                        listModel.addElement(RouteTable[i][0].toString());
                    }
                    
                    WhosOnlineListArea.setModel(listModel);
                    
                        
                    
                        /*
                    //set address from GUI input box
                    address = DestinationIPTextField.getText(); // destination IP (Client)

                    try {

                        // Communication process (initial sends/receives)
                        out.println(address);// initial send (IP of the destination Client)

                        clientText = in.readLine();// initial receive from router (verification of connection)

                        
                        MessageTextArea.append("ServerRouter: " + clientText + "\n");

                        // Communication while loop.  Will run as long as
                        //information is coming from client via the server router.
                        while ((clientText = in.readLine()) != null) {
                            
                            //This takes in text sent and capitalizes it then
                            //sends it back to the server router which forwards
                            //it to the client
                            MessageTextArea.append("Client said: " + clientText + "\n");
                            // converting received message to upper case
                            serverText = clientText.toUpperCase(); 
                            
                            MessageTextArea.append("Server said: " + serverText + "\n");
                            // sending the converted message back to the Client via ServerRouter
                            out.println(serverText); 
                        }
                    

                        //closing connections
                        out.close();
                        in.close();
                        socket.close();
                
                    } catch (IOException ex) {
                        Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        */
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
        jLabel4 = new javax.swing.JLabel();
        DestinationIPTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        WhosOnlineListArea = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Socket:");

        SocketTextField.setText("5555");

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

        jLabel4.setText("Destination IP:");

        jLabel5.setText("Server");

        jScrollPane2.setViewportView(WhosOnlineListArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(IPLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ConnectBtn)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel2)
                                        .addComponent(RouterAddressTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                                        .addComponent(jLabel1)
                                        .addComponent(DestinationIPTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                                        .addComponent(SocketTextField)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jLabel5))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(3, 3, 3)
                        .addComponent(DestinationIPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RouterAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SocketTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ConnectBtn)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
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
    private javax.swing.JTextField DestinationIPTextField;
    private javax.swing.JLabel IPLabel;
    private javax.swing.JTextArea MessageTextArea;
    private javax.swing.JTextField RouterAddressTextField;
    private javax.swing.JTextField SocketTextField;
    private javax.swing.JList WhosOnlineListArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
