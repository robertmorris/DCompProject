
package tcpserverrouter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerRouterGUI extends javax.swing.JFrame {

    Socket clientSocket;
    ServerSocket serverSocket;
    Boolean running;//prevent serverRouter from launching twice
    String[][] RouteTable; // routing table
    int portNumber;
    int tableIndex;
    //CommunicationThread commThread;
    InetAddress addr;
    BufferedReader inToMe;
    
    ServerSocket serverToserver;
    Socket serverComSocket;
    

    public ServerRouterGUI() {

        initComponents();
  
        running = false;

        try {

            addr = InetAddress.getLocalHost();
            String host = addr.getHostAddress(); // Local machine's IP          
            SRIPlabel.setText(host);//set GUI label

        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerRouterGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private class ServerHandler extends Thread {
        
        private String[][] RouteTable; // routing table
        private PrintWriter out;
        private PrintWriter outToDestination; // writers (for writing back to the machine and to destination)
        private BufferedReader incoming; // reader (for reading from the machine connected to)
        private String input;//used for incoming
        private String output;//used for outgoing
        private String clientToAdd;//forwarding IP
        private String address; // communication strings
        private Socket outGoingSocket; // socket for communicating with a destination
        private int tableIndex; // indext in the routing table
        private Socket incomingServerSocket;
        
        public ServerHandler(Socket socket,  String[][] table, int index) throws IOException{
            //set the in and out stream for communication
            
            RouteTable = table;
            tableIndex = index;
            incomingServerSocket = socket;
            outToDestination = new PrintWriter(incomingServerSocket.getOutputStream(), true);
            incoming = new BufferedReader(new InputStreamReader(incomingServerSocket.getInputStream()));
        }
        @Override
        public void run() {
            
            try {
                        outToDestination.println("Connected, searching");
                        input = incoming.readLine();
                        address = input;
                        
                        boolean foundIt = false;
                        for (int i = 0; i < tableIndex; i += 1) {
                
                         if(RouteTable[i][0].equals(address)){
                        outToDestination.println("FOUND");
                        outToDestination.println(RouteTable[i][0]);
                        outToDestination.println(RouteTable[i][1]);
                        foundIt = true;
                        break;
                    }
                        }
                        
                        if(foundIt == false){
                            outToDestination.println("NOTFOUND");
                        }
                        
                    outToDestination.close();
                    incoming.close();
                    incomingServerSocket.close();
                
                    
            }// end try
            catch (Exception e) {
               
                try {
                    MessageArea.append("Could not listen to socket. \n");
                    outToDestination.close();
                    incoming.close();
                    incomingServerSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerRouterGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
                
                    
        }
    }
  

    public class ServerStart implements Runnable {

        public void run() {

            try {

                clientSocket = null; // socket for the thread
                int SockNum = Integer.parseInt(SocketTextField.getText()); // port number
                running = true;//locks server start
                tableIndex = 0; // RouteTable Index
                //table will hold up to 20 connections
                RouteTable = new String[20][2];
                
                String incoming;

                //set serverSocket port
                serverSocket = null;
                serverSocket = new ServerSocket(SockNum);

                MessageArea.append("ServerRouter is Listening on port: " + SockNum + " \n");

                while (true) {
                    try {
                       
                        clientSocket = serverSocket.accept();
                        inToMe = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        incoming = inToMe.readLine();
                        switch (incoming) {
                            case "CLIENTCOMM":
                                new CommunicationThread(RouteTable,clientSocket,tableIndex).start();
                                break;
                            case "SERVERREQUEST":
                                new ServerHandler(clientSocket,RouteTable,tableIndex).start();                             
                                break;  
                            case "SERVERCOMM":                               
                                new ServerConnection(RouteTable,clientSocket,tableIndex).start();
                                tableIndex++;
                                break;
                        }
                      
                        //output communication and add client/server to list of connections
                        MessageArea.append("ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress() + "\n");
                        //CSListArea.append(clientSocket.getInetAddress().getHostAddress() + "\n");
                        //clientSocket.close();
          
                    } catch (IOException e) {
                        MessageArea.append("Client/Server failed to connect.");
                        e.printStackTrace();
                    }finally{
                        //serverSocket.close();
                    }
                }//end while

                //closing connections
                
                
            } catch (IOException e) {
                try {
                    MessageArea.append("Could not listen on port: \n");
                    e.printStackTrace();
                    clientSocket.close();
                    serverSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerRouterGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }


    }

    /**
     * This mecommThreadhod is called from wicommThreadhin commThreadhe
     * conscommThreadruccommThreador commThreado inicommThreadialize
     * commThreadhe form. WARNING: Do NOT modify commThreadhis code. The
     * concommThreadencommThread of commThreadhis mecommThreadhod is always
     * regeneracommThreaded by commThreadhe Form EdicommThreador.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        StartBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        MessageArea = new javax.swing.JTextArea();
        StopBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        CSListArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        SocketTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        SRIPlabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        SSPort = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        SSAddress = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        StartBtn.setText("Start");
        StartBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        StartBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartBtnActionPerformed(evt);
            }
        });

        jLabel1.setText("Server Router");

        MessageArea.setEditable(false);
        MessageArea.setColumns(20);
        MessageArea.setLineWrap(true);
        MessageArea.setRows(5);
        jScrollPane1.setViewportView(MessageArea);

        StopBtn.setText("Stop");
        StopBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("Servers Online");

        CSListArea.setEditable(false);
        CSListArea.setColumns(20);
        CSListArea.setRows(5);
        jScrollPane2.setViewportView(CSListArea);

        jLabel3.setText("Socket:");

        jLabel4.setText("ServerRouterIP:");

        SRIPlabel.setText("127.0.0.1");

        jLabel5.setText("Second Server Socket:");

        jLabel6.setText("Second Server IP:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(SocketTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(SRIPlabel)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(StartBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(StopBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(21, 21, 21))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SSPort, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SSAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StopBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StartBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(SocketTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(SRIPlabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(SSPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(SSAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void StartBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartBtnActionPerformed

        if (!running) {
            Thread starter = new Thread(new ServerStart());
            starter.start();
           
        }

    }//GEN-LAST:event_StartBtnActionPerformed

    private void StopBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopBtnActionPerformed
        //not finished
        running = false;

    }//GEN-LAST:event_StopBtnActionPerformed

    /**
     * @param args commThreadhe command line argumencommThreads
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
            java.util.logging.Logger.getLogger(ServerRouterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerRouterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerRouterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerRouterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerRouterGUI().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea CSListArea;
    public javax.swing.JTextArea MessageArea;
    private javax.swing.JLabel SRIPlabel;
    private javax.swing.JTextField SSAddress;
    private javax.swing.JTextField SSPort;
    private javax.swing.JTextField SocketTextField;
    private javax.swing.JButton StartBtn;
    private javax.swing.JButton StopBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    public class ServerConnection extends Thread  {
        
        private String[][] RouteTable;
        private PrintWriter sout;
        private BufferedReader sin;
        private String input;
        private String output;
        private String address;
        private int tableIndex;
        private Socket socket;
        
        ServerConnection(String[][] table, Socket cs, int index) throws IOException {
            socket = cs;
            sout = new PrintWriter(socket.getOutputStream(), true);
            sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            RouteTable = table;
            address = socket.getInetAddress().getHostAddress();
            //set values into table 
            tableIndex = index; 
            
        }
       
        @Override
        public void run(){
            try{
                sout.println("Adding to list");
                CSListArea.append(address);
                RouteTable[tableIndex][0] = address; // IP addresses 
                input = sin.readLine();
                RouteTable[tableIndex][1] = input; // sockets for communication
                sout.println("BYE");
                sout.close();
                sin.close();
                socket.close();
                
            }
            catch(Exception ex){
                
                    ex.printStackTrace();   
            }
        }
    }
    
    public class CommunicationThread extends Thread {

        private String[][] RouteTable; // routing table
        private PrintWriter out1;
        private PrintWriter out2; // writers (for writing back to the machine and to destination)
        private BufferedReader in1;
        private BufferedReader in2;// reader (for reading from the machine connected to)
        private String input;//used for incoming
        private String output;//used for outgoing
        private String destinationIP;//forwarding IP
        private String address; // communication strings
        private Socket outGoingSocket; // socket for communicating with a destination
        private int tableIndex; // indext in the routing table
        private Socket secondServerSocket;
        private int secondServerPort;
        private String secondServerAddress;

        // Constructor
        CommunicationThread(String[][] Table, Socket ClientSoc, int index) throws IOException {
            
            //set the in and out stream for communication
            outGoingSocket = ClientSoc;
            out1 = new PrintWriter(outGoingSocket.getOutputStream(), true);
            in1 = new BufferedReader(new InputStreamReader(outGoingSocket.getInputStream()));
            RouteTable = Table;
 
            tableIndex = index; 

            secondServerAddress = SSAddress.getText();
            secondServerPort = Integer.parseInt(SSPort.getText());
                    
            
        }

        @Override
        public void run() {
            try{
                
                out1.println("Searching for Server....");
                
                address = in1.readLine();
                
                boolean foundAddress = false;
                
                if(RouteTable != null){
                for (int i = 0; i < tableIndex; i += 1) {
                
                    if(RouteTable[i][0].equals(address)){
                        out1.println("FOUND");
                        out1.println(RouteTable[i][0]);
                        out1.println(RouteTable[i][1]);
                        foundAddress = true;
                        break;
                    }
            }
                }
                if(foundAddress == false){
                    
                    
                    secondServerSocket = new Socket(secondServerAddress,secondServerPort);
                    out2 = new PrintWriter(secondServerSocket.getOutputStream(), true);
                    in2 = new BufferedReader(new InputStreamReader(secondServerSocket.getInputStream()));
                    out2.println("SERVERREQUEST");
                    input = in2.readLine();
                    MessageArea.append(input);
                    out2.println(address);
                    input = in2.readLine();
                    
                    String address;
                    String port;
                    if(input.equals("FOUND")){                     
                        address = in2.readLine();
                        port = in2.readLine();

                        out1.println("FOUND");
                        out1.println(address);
                        out1.println(port);
                    }
                }

            
            
            } catch (IOException ex) {
                Logger.getLogger(ServerRouterGUI.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                try {
                    out1.close();
                    in1.close();
                    outGoingSocket.close();
                    out2.close();
                    in2.close();
                    secondServerSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerRouterGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            }
            
        }

    }


