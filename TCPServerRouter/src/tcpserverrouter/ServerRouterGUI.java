/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserverrouter;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;

/**
 *
 * @author overload
 */
public class ServerRouterGUI extends javax.swing.JFrame {

    Socket clientSocket;
    ServerSocket serverSocket;
    Boolean running;//prevent serverRouter from launching twice
    Object[][] RouteTable; // routing table
    int portNumber;
    int tableIndex;
    CommunicationThread commThread;
    InetAddress addr;
    HashSet<String> names = new HashSet<String>();
    HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    ArrayList<String> clientList = new ArrayList<String>();
    
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
        
        private Object[][] RouteTable; // routing table
        private PrintWriter out;
        private PrintWriter outToDestination; // writers (for writing back to the machine and to destination)
        private BufferedReader in; // reader (for reading from the machine connected to)
        private String input;//used for incoming
        private String output;//used for outgoing
        private String clientToAdd;//forwarding IP
        private String address; // communication strings
        private Socket outGoingSocket; // socket for communicating with a destination
        private int tableIndex = 0; // indext in the routing table
        private Socket incomingServerSocket;
        
        public ServerHandler(Socket socket, ArrayList<String> Clist, Object[][] table, int index) throws IOException{
            //set the in and out stream for communication
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            RouteTable = table;
            address = socket.getInetAddress().getHostAddress();
            //set values into table
            //RouteTable[index][0] = address; // IP addresses 
            //RouteTable[index][1] = socket; // sockets for communication
            tableIndex = index;
            incomingServerSocket = socket;
        }
        
        public void run() {
            
            try {
                
              
                
                while(true)
                {
    
                        for (int i = 0; i < RouteTable.length; i += 1) {
                
                         out.println(RouteTable[i][0]);
                         out.println(RouteTable[i][1]);
                
                        if(i+1 == RouteTable.length)
                        {
                            out.println("BYE");
                            break;
                        }
               
                        }

                }
                    
            }// end try
            catch (Exception e) {
               
                MessageArea.append("Could not listen to socket. \n");
                
            }finally{
                try {
                    incomingServerSocket.close();
                    serverSocket.close();
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
                //tableIndex = 0; // RouteTable Index
                //table will hold up to 20 connections
                RouteTable = new Object[20][2];
                BufferedReader in;
                String incoming;

                //set serverSocket port
                serverSocket = null;
                serverSocket = new ServerSocket(SockNum);

                MessageArea.append("ServerRouter is Listening on port: " + SockNum + " \n");

                while (running == true) {
                    try {
                        
                        while(true){
                            
                        new MakeServerRequest(RouteTable, tableIndex).start();
                            
                        //new Handler(serverSocket.accept()).start();
                        clientSocket = serverSocket.accept();
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        incoming = in.readLine();
                        if(incoming == "CLIENTCOMM")
                        {
                            new CommunicationThread(RouteTable,clientSocket,tableIndex).start();   
                            //new MakeServerRequest(RouteTable, tableIndex).start();
                        }
                        else if(incoming == "SERVERREQUEST")
                        {
                            new ServerHandler(clientSocket, clientList, RouteTable, tableIndex).start();
                            
                        }
                        
                        //commThread = new CommunicationThread(RouteTable, clientSocket, tableIndex); // creates a thread with a random port
                        //commThread.start(); // starts the thread
                        tableIndex++; // increments the index
                        //output communication and add client/server to list of connections
                        MessageArea.append("ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress() + "\n");
                        //CSListArea.append(clientSocket.getInetAddress().getHostAddress() + "\n");
                        }
          
                    } catch (IOException e) {
                        MessageArea.append("Client/Server failed to connect.");
                        e.printStackTrace();
                    }finally{
                        serverSocket.close();
                    }
                }//end while

                //closing connections
                clientSocket.close();
                serverSocket.close();
                
            } catch (IOException e) {
                MessageArea.append("Could not listen on port: 5555. \n");
                e.printStackTrace();
            }

        }

        private class MakeServerRequest extends Thread {

        private Object[][] RouteTable; // routing table
        private PrintWriter out;       
        private BufferedReader in; // reader (for reading from the machine connected to)
        private String input;//used for incoming
        private String output;//used for outgoing   
        private String address; // communication strings
        private Socket outGoingSocket; // socket for communicating with a destination
        private int tableIndex; // indext in the routing table
        
            private MakeServerRequest(Object[][] table, int index) {
                this.RouteTable = table;
                this.tableIndex = index;
               
            }
            
            public void Run()
            {
                try{
                    outGoingSocket = new Socket("localhost", 5555);
                    out = new PrintWriter(outGoingSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(outGoingSocket.getInputStream()));
                    
                    out.println("SERVERREQUEST");
                    
                    while(true)
                    {
                        input = in.readLine();
                        
                        if(input == "BYE")
                            break;
                        
                        RouteTable[tableIndex][0] = input;
                        input = in.readLine();
                        RouteTable[tableIndex][1] = input;
                        tableIndex++;
                    }
                    
                }catch(IOException ie){
                    ie.printStackTrace();
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
        jButton1 = new javax.swing.JButton();

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

        jLabel2.setText("Client/Servers Online");

        CSListArea.setEditable(false);
        CSListArea.setColumns(20);
        CSListArea.setRows(5);
        jScrollPane2.setViewportView(CSListArea);

        jLabel3.setText("Socket:");

        SocketTextField.setText("5555");

        jLabel4.setText("ServerRouterIP:");

        SRIPlabel.setText("127.0.0.1");

        jButton1.setText("Server Request");
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
                        .addComponent(jButton1)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void StartBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartBtnActionPerformed

        if (!running) {
            Thread starter = new Thread(new ServerStart());
            starter.start();
            
            //Thread serverToserverThread = new Thread(new ServerToServer());
            //serverToserverThread.start();
        }

    }//GEN-LAST:event_StartBtnActionPerformed

    private void StopBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopBtnActionPerformed
        //not finished
        running = false;

    }//GEN-LAST:event_StopBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JTextField SocketTextField;
    private javax.swing.JButton StartBtn;
    private javax.swing.JButton StopBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    class CommunicationThread extends Thread {

        private Object[][] RouteTable; // routing table
        private PrintWriter out;
        private PrintWriter outToDestination; // writers (for writing back to the machine and to destination)
        private BufferedReader in; // reader (for reading from the machine connected to)
        private String input;//used for incoming
        private String output;//used for outgoing
        private String destinationIP;//forwarding IP
        private String address; // communication strings
        private Socket outGoingSocket; // socket for communicating with a destination
        private int tableIndex; // indext in the routing table

        // Constructor
        CommunicationThread(Object[][] Table, Socket ClientSoc, int index) throws IOException {
            
            //set the in and out stream for communication
            out = new PrintWriter(ClientSoc.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ClientSoc.getInputStream()));
            RouteTable = Table;
            address = ClientSoc.getInetAddress().getHostAddress();
            //set values into table
            RouteTable[index][0] = address; // IP addresses 
            RouteTable[index][1] = ClientSoc; // sockets for communication
            tableIndex = index; 
        }

        public void run() {
            try{
                
            while(true)
            {
            synchronized(address){
                if(!names.contains(address)){
                    names.add(address);
                    //cl.add(name);
                    //break;
                    CSListArea.append(address);
                }
            }
            
            /*
            try {
                // tells current thread to do nothing for
                // set time.
                Thread.currentThread().sleep(10000);
                
            } catch (InterruptedException ie) {
                
                MessageArea.append("Thread interrupted \n");
                
            }
                    */
            
            for (int i = 0; i < RouteTable.length; i += 1) {
                
                out.println(RouteTable[i][0]);
                out.println(RouteTable[i][1]);
                
                if(i+1 == RouteTable.length)
                {
                    out.println("BYE");
                    break;
                }
                //outGoingSocket = (Socket) RouteTable[i][1]; // gets the socket for communication from the table
                
                //MessageArea.append("Found destination: " + destinationIP + "\n");
                //outToDestination = new PrintWriter(outGoingSocket.getOutputStream(), true); // assigns a writer
            }
            out.close();
            in.close();
            }
            
            }
            catch(Exception ex){
                    ex.printStackTrace();
            }
        }
    }

}
