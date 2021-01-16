package BulletinBoardClient;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.awt.*;
import java.awt.event.*;


//Java GUI BulletinBoard Client
//Author: Kush Banbah 55786740 
public class BulletinBoardClient {

	
	
	//Declaration of global variables
	private static JFrame frame;
	private JTextField ip_address_txt;
	static private JTextField user_input_field;
	static Socket con_socket;
	static OutputStream output ;
	static PrintWriter writer ;
	static InputStream input ;
	static BufferedReader reader ;
	static JButton POST_btn;
	static JButton Read_btn;
	static JButton Quit_btn;
	static JTextArea display_text;
	static String stringtosend;
	static JLabel msg_lbl;
	static JButton connect_btn;
	static boolean isConnected;
	
	//Main method to create GUI
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					BulletinBoardClient window = new BulletinBoardClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Constructor, runs  initialize method
	public BulletinBoardClient() {
		initialize();
	}

	//Creation of UI Window and components, setting event listeners for buttons, and text inputs.
	private void initialize() {
		frame = new JFrame();
		//isConnected represents if a connection is made
		isConnected = true;
		
		
		//A listener to run the quit method when the X button in the UI is clicked
		//it also makes sure communication with server is properly stopped.
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
				QUIT();
				}
				catch(Exception e1)
				{
					frame.dispose();
				}
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 688, 509);
	
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("BulletinBoard Client");
		
		
		JPanel panel = new JPanel();
		panel.setBounds(175, 40, 258, 36);
		frame.getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		ip_address_txt = new JTextField();
		//a listener to stop ip address field from accepting more than 15 digits (max ip length)
		
		ip_address_txt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(ip_address_txt.getText().length()>15)
				{
					char[] limit = ip_address_txt.getText().toCharArray();
					String set ="";
					for(int c= 0; c<15; c++)
					{
						set = set+limit[c];
					}
					ip_address_txt.setText(set);
					}
				
				
			}
		});
		
	

	
		
		ip_address_txt.setFont(new Font("Verdana", Font.PLAIN, 18));
		ip_address_txt.setBounds(0, 274, 968, 139);
		panel.add(ip_address_txt);
		ip_address_txt.setColumns(18);
		
		connect_btn = new JButton("Connect");	
		
		//Run the create_con function when connect button is clicked. The function manages the setup of the connection
		
		connect_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			
				create_con(ip_address_txt.getText());
				
			}
		});
		panel.add(connect_btn);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(88, 153, 518, 235);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		 display_text = new JTextArea();
		 display_text.setEditable(false);
		display_text.setEnabled(false);
		display_text.setLineWrap(true);
		display_text.setBounds(0, 0, 518, 192);
		
		JScrollPane scrollPane = new JScrollPane(display_text);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(0, 0, 518, 183);
		panel_1.add(scrollPane);
		
		msg_lbl = new JLabel("");
		msg_lbl.setBounds(59, 210, 383, 14);
		panel_1.add(msg_lbl);
		
		
		
		
		POST_btn = new JButton("POST");
		
		//Run the setup method for POST when the POST button is clicked
		
		POST_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				POSTsetup();
			}
		});
		POST_btn.setEnabled(false);
		POST_btn.setBounds(114, 109, 89, 23);
		frame.getContentPane().add(POST_btn);
		
		 Read_btn = new JButton("READ");
		 
		//Run the read function when READ button is clicked
		 
		 Read_btn.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		
		 		READ();
		 	}
		 });
		Read_btn.setEnabled(false);
		Read_btn.setBounds(250, 109, 89, 23);
		frame.getContentPane().add(Read_btn);
		
		 Quit_btn = new JButton("QUIT");
		 
		 //Run the quit method when QUIT button is clicked
		 //Allows for reconnection of client to another or same server
		 
		 Quit_btn.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		
		 		QUIT();
		 	}
		 });
		Quit_btn.setEnabled(false);
		Quit_btn.setBounds(408, 109, 89, 23);
		frame.getContentPane().add(Quit_btn);
		
		JLabel lblNewLabel = new JLabel("IP address");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(195, 21, 163, 14);
		frame.getContentPane().add(lblNewLabel);
		
		
		
		user_input_field = new JTextField();
		user_input_field.setBounds(88, 386, 518, 28);
		frame.getContentPane().add(user_input_field);
		
		//If the user types enter while using the text field, then run the POST method
		//It's more user friendly than having a button to click for every message sent
		
			
		user_input_field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
			{
				
						if(!user_input_field.getText().isBlank())
					POST(user_input_field.getText());
				
				user_input_field.setText("");
			}
				
			//Hard limit of 256 characters is used for practicality			
			//This code is used to cut the text input length to less than 256
				
				
			else if(user_input_field.getText().length()>256)
			{
				
				char[] limit = user_input_field.getText().toCharArray();
				String set ="";
				for(int c= 0; c<15; c++)
				{
					set = set+limit[c];
				}
				user_input_field.setText(set);
				}
			
			}
		});
		user_input_field.setEnabled(false);
		user_input_field.setColumns(10);
	}
	
	
	//begin connection, initialize all the streams and prepare the window for input
	public void create_con(String ipaddress)
	{
		if(!ipaddress.isBlank())
		try {
			
			stringtosend="";
			con_socket= new Socket(ipaddress, 16000);
			//connect socket
			input = con_socket.getInputStream();
			output = con_socket.getOutputStream();	
			writer = new PrintWriter(output, false);
			reader = new BufferedReader(new InputStreamReader(input));
			
			
			//function to enable all the disabled buttons and text inputs since socket is now connected.
			enableall();
			isConnected = false;
				
			if(display_text.getText().isBlank())
				display_text.setText("Connection Established, pick a command to begin!");
			else
			update("Connection Established, pick a command to begin!");
	
		}
		catch(Exception e)
		{
			//Error dialog for failure
			JOptionPane.showMessageDialog(frame, "Unsucessful Connection! Check IP address","Error!", JOptionPane.ERROR_MESSAGE);
			
		}
		
		else
			JOptionPane.showMessageDialog(frame, "No IP address entered","Error!", JOptionPane.ERROR_MESSAGE);
		
	}
	
	//Enable command buttons, display text area and disable connect button
	public static void enableall()
	{
		POST_btn.setEnabled(true);
		Quit_btn.setEnabled(true);
		Read_btn.setEnabled(true);
		connect_btn.setEnabled(false);
		display_text.setEnabled(true);		
	}
	
	//disable buttons
	public static void disablebtn() {
		POST_btn.setEnabled(false);
		Quit_btn.setEnabled(false);
		Read_btn.setEnabled(false);
	}
	
	
	//method to add new lines to the display text
	public static void update(String line)
	{
		display_text.setText(display_text.getText()+"\n"+line);
		
	}

	//Method to assemble a string for the POST command
	public static String send(String add)
	{
		stringtosend = stringtosend+add;
		return stringtosend;
	}
	
	//Method to assemble a string for the POST command
	public static String send()
	{
		
		return stringtosend;
	}
	
	
	//Method to clear the string assembled for POST
	public static String send(boolean clear)
	{
		stringtosend = "";
		return stringtosend;
	}
	
	
	
	//Setup for POST command
	public static void POSTsetup()
	{
		try{
			send("POST\n");
			update("POST - text input is enabled");
			user_input_field.setEnabled(true);
			disablebtn();
			msg_lbl.setText("Use enter to enter messages and . to send them to sever");
		}catch(Exception e)
		{
			//Error dialog for failure
			JOptionPane.showMessageDialog(null , "POST command could not be sent!\nPlease check connection! and restart connection","Error!", JOptionPane.ERROR_MESSAGE);
			QUIT();
		}
	}
	
	
	//Handles preparing each post message
	public static void POST(String msg) 
	{
		try {
		if(msg.equals("."))
		{
			
			send(msg+"\n");
			update("Client: "+msg);
			writer.print(send());
			writer.flush();
			char[] response = new char[10];
			reader.read(response);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Server: ");
			stringBuilder.append(response);
			update(stringBuilder.toString());
			send(true);
			user_input_field.setEnabled(false);
			enableall();
			msg_lbl.setText("");
			
		}
		else 
			{
				update("Client: "+msg);
				send(msg+"\n");
			
			}
		}
		catch(Exception e)
		{
			//Error dialog
			JOptionPane.showMessageDialog(null , "POST messages could not be sent!\nPlease check connection and restart connection","Error!", JOptionPane.ERROR_MESSAGE);
			QUIT();}
	}
	
	//Send READ command and output messages from server
	public static void READ()
	{
		String result="";
		update("Client: READ");
		writer.print("READ\n");
		writer.flush();
		
		try { 
		
			boolean finalans=false;
			
			while(!finalans)
			{
				
				//buf is the buffer char array holding the output
				
				char[] buf = new char[256];
				reader.read(buf);
				
				//Char array buf is converted to string to trim whitespace
				 String c = new String(buf);
			
				 c =c.trim();	
				
				if(c.contentEquals("."))
				{
					finalans=true;
					result = result+"Server: "+c;
				}
				else				
				 result = result+"Server: "+c+"\n";
				
			}
			
			//Display all the messages from server after all messages are received
			update(result);	
				
		} catch (Exception e) {
			
			
			JOptionPane.showMessageDialog(null , "READ command could not be properly sent!\nPlease check connection and restart connection","Error!", JOptionPane.ERROR_MESSAGE);
			QUIT();
		}
		
	}
	
	//Quit closes all streams, disables all the buttons, reads the server response and closes socket but does not close the GUI
	//It sets it up for a new connection if the user chooses
	public static void QUIT()
	{
		
		if(!isConnected) {
		try {
			writer.print("QUIT\n");
			writer.flush();
			update("Client: Quit");
			
			char[] response = new char[10];
			reader.read(response);
			String respond = new String(response);
			respond = "Server: "+respond;
			update(respond);
			
			
			reader.close();
			writer.close();
			output.close();
			input.close();
			con_socket.close();
			disablebtn();
			connect_btn.setEnabled(true);
			isConnected = true;
			JOptionPane.showMessageDialog(frame, "Socket closed and connection disconnected!","Closed Socket",JOptionPane.PLAIN_MESSAGE);
		} catch (IOException e) {
		
			JOptionPane.showMessageDialog(null , "Could not quit properly\nThe server might not have recieved the message properly and might need to be restarted\nPlease check connection","Error!", JOptionPane.ERROR_MESSAGE);
			isConnected = true;
			
			//Allow user to remake connection even if QUIT wasn't sucessful
			disablebtn();
			connect_btn.setEnabled(true);
		
		}	
	}
		
	
		
	}
}
