import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 *
 * This class manages the connections and client threads
 */
public class Server{
    // Unique ID for each connection
    private static int uniqueID;
    // List of connected clients
    private ArrayList<ClientThread> clients;
    
    private SimpleDateFormat sdf;
    // port number that we listen for connection on
    private int port;
    // decides whether server will still run or not
    private boolean on;
    ServerSocket serverSocket;

    // Constructor for the server
    // Can edit this to include GUI if needed
    public Server (int port) throws IOException{
        this.port = port;
        sdf = new SimpleDateFormat("HH:mm:ss");
        clients = new ArrayList<>();
    }
    
    public void start(){
        on = true;
        try {
            // Creates socket used by the server
            serverSocket = new ServerSocket(port);

            //infinite loop to check for connections
            while(on){
                 // Check if i should stop
                if (!on){ break; }    
                
                // Show that server is waiting
                display("Server is waiting for clients on port " + port + ".");
               
                // Accept connections
                Socket socket = serverSocket.accept();
               
                // make a thread of the connection
                ClientThread t = new ClientThread(socket); 
                // save the thread in the client list
                clients.add(t);
                t.start();
            }
            
            // possibly add try catches here so that error catching is easier
            for (int i=0; i<clients.size();i++){
                ClientThread tc = clients.get(i);
                tc.sInput.close();
                tc.sOutput.close();
                tc.socket.close();
            }
        } catch (IOException e) {
        }
    }

    // Dispays event (not a message) to the console
    public void display(String msg){
        String time = sdf.format(new Date()) + " " + msg;
        System.out.println(time);
    }
    // Send a message to all clients
    private synchronized void broadcast (String message){
        // Formats and prints message to console
        String bcmessage = sdf.format(new Date()) + " " + message + "\n";
        System.out.println(bcmessage);

        // Sends the message to the clients
        // In reverse order in case we need to remove someone
        for (int i=clients.size(); --i>=0;){
            ClientThread ct = clients.get(i);
            // try to write to client. If a false boolean is returned then send failed,
            // and we remove the client
            if (!ct.writeMsg(bcmessage)){
                clients.remove(i);
                display(ct.username + " client disconnected");
            }
        }
    }
    private synchronized void broadcastFrom (String message,String from){
        // Formats and prints message to console
        String bcmessage = sdf.format(new Date()) + " " + message + "\n";
        System.out.println(bcmessage);

        // Sends the message to the clients
        // In reverse order in case we need to remove someone
        for (int i=0; i<clients.size();i++){
            ClientThread ct = clients.get(i);
            if (!(ct.username.equals(from))){
                ct.writeMsg(bcmessage);
            }
        }
    }

    // used for when the client logs off using LOGOUT message
    public synchronized void remove(int id){
    
        for (int i=0; i<clients.size();i++){
            ClientThread ct = clients.get(i);
            if (ct.id == id){
                clients.remove(i);
                break;
            }
        }
        if(clients.isEmpty())
        {
          System.out.println("No more Clients");
          try{
          on=false;
          serverSocket.close();
           System.exit(0);
          }catch(IOException e){System.out.println("Oh No");}
         
        }
    }
    public String getClientUserNames(){
        String usernames = "";
        for (ClientThread client : clients){
            usernames = usernames + client.username + " ";
        }
        return usernames;
    }
    public static void main(String args[]){
        // start server on generic port unless a port number is specified
        int portNumber = 1500;
        switch(args.length){
            case 1:
                try {
                    portNumber = Integer.parseInt(args[0]);
                } catch (NumberFormatException e){
                    System.out.println("Invalid port number");
                }
            case 0:
                break;
            default:
                System.out.println("Usage is: > java Server {portNumber}");
                return;
        }
       
        Server server=null;
        try {
            server = new Server(portNumber);
        } catch (IOException e) {
        }
        server.start();

    }

    // Inner class for the thread. Easier this way than making a server object.
    public class ClientThread extends Thread {

        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        int id;
        String username;
        Message msg;
        String date;
        boolean pic;
        String message;
        Image i;

        // Thread constructor
        public ClientThread(Socket socket){
            id = uniqueID++;
            this.socket = socket;

            try {
                sOutput = new ObjectOutputStream((socket.getOutputStream()));
                sInput = new ObjectInputStream(socket.getInputStream());

                // Send list of usernames to client:
                writeMsg(getClientUserNames());

                username = (String) sInput.readObject();

                broadcast(username + " just connected.");//everyone should know

            } catch (IOException e) {
                display("Exception in creating data streams " + e);
            } catch (ClassNotFoundException e) {
            }
            date = new Date().toString() + "\n";
        }
        // Loops until LOGOUT
        @Override
        public void run(){
            boolean on = true;
            while(on){
                try {
                    msg = (Message) sInput.readObject();
                } catch (IOException e) {
                    display("I/O exception. Can't read message from client.");
                    break;
                } catch (ClassNotFoundException e) {
                    display("ClassNotFoundException. Can't find the object");
                    break;
                }
                message = msg.getMessage();

                switch (msg.getType()){
                    case Message.MESSAGE:
                        if(!pic){
                        broadcastFrom(username + ": " + message,username);
                        }
                        
                        break;
                    case Message.LOGOUT:

                        broadcast(username + " disconnected from server"); //i think everyone needs to know when someone leaves

                        on = false;
                        break;
                    
                }
            }
            // If here then client has logged out. So will remove client.
            remove(id);
            close();
        }
        private void close(){
            try {
                sOutput.close();
                sInput.close();
                socket.close();
            } catch (IOException e) {
            }

        }
        
        private boolean writeMsg(String msg){
            if (!socket.isConnected()){
                close();
                return false;
            }
            // write message to the stream
            try {
                //System.out.println("DEBUG: Trying to write message");
                sOutput.writeObject(msg);
            } catch (IOException e) {
                System.out.println("Error sending message to " + username);
            }
            return true;
        }
    }

}
