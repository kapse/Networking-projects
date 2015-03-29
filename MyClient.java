/**
 * imported java packages used in the program */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 * MyClient is the class where client request command through GUI .
 * It extends Frame to create GUI 
 * It implements ActionListener Runnable interface   
 * @author VAIBHAV
 *
 */

public class MyClient extends JFrame implements ActionListener, Runnable 
{
	    /**
	     * constructor of the MyClient class
	     */
		public MyClient()
		{
		/**
		 * It used in creating GUI(graphical user interface for the client
		 */
		JFrame frame = new JFrame("Client Server Network ");								
		/**
		 * used to set the layout of the frame
		 */
		frame.setLayout(new FlowLayout());
		this.setLayout(null);
		/**
		 * adds the background color to the frame
		 */
		frame.setBackground(Color.LIGHT_GRAY);
		/**
		 * it creates button in the frame
		 */
		button = new JButton("Click to run Command");
		button.setBounds(100,200, 310, 30);
		/**
		 * addActionListener() method is called to perform an action when client clicks on button
		 */
		button.addActionListener(this);
		frame.addWindowListener(new shut());	
		/**
		 * textField is added to the frame to get the command from the client
		 */
		textField= new JTextField(30);
		textField.setBounds(260,250,150,30);
		/**
		 * 
		 * It shows the output of the input command
		 */
		 LineBorder lb=new LineBorder(Color.red,4);
		 TitledBorder tb=new TitledBorder(lb,"Welcom to Client Server Network");
		       
	      l8=new JLabel();
	      l8.setBorder(tb);
	      l8.setBounds(10, 10, 600, 600);
		  
		label=new JLabel("Enter Command");
		label.setBounds(100,100, 150, 30);
		textArea = new JTextArea(30, 60);
		/**
		 * setbounds is used to place the textarea in the JFrame
		 */
		textArea.setBounds(260,300,300,400);
		frame.add(label);
		frame.add(textField);
		frame.add(button);
		frame.add(textArea);
		/**
		 * try and catch block is used to handle the exception if occurs
		 */
		try {
			/**
			 * creating socket for the client
			 */
			s = new Socket(InetAddress.getLocalHost(), 40000);
			/**
			 * BufferedReader is used to read input from the client
			 */
			b = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			/**
			 * PrintWriter is used to write the output on the textArea
			 */
			p = new PrintWriter(s.getOutputStream(), true);
		} 
		/**
		 * catch is used to handle exception
		 */
		catch (Exception e) {
		}
		/**
		 * It creates a new thread
		 */
		thread = new Thread(this);
		/**
		 * set the tread as a daemon
		 */
		thread.setDaemon(true);
		/**
		 * starts the thread
		 */
		thread.start();
		setFont(new Font("Arial", Font.BOLD, 20));
		frame.setSize(800, 800);
		/**
		 *It makes the frame visible 
		 */
		frame.setVisible(true);
		frame.setLocation(200, 200);
		frame.validate();
	}
		/**
		 * shut method is used to close the frame
		 * @author VAIBHAV
		 *
		 */
	private class shut extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	/**
	 * ActionedPerformed method is called when user click on the button
	 */
	public void actionPerformed(ActionEvent ae) {
		cmd=textField.getText();
		if(cmd != null)
			textArea.append(">> " + cmd + "\n");
		p.println(textField.getText());
		/**
		 * It takes the GET command and process the client request
		 */
		if (textField.getText().contains("GET")) {
			int filesize = 2000000;
			int bytesRead;
			int tot = 0;
			try {
				Socket socket = new Socket("localhost", 40001);
				byte[] bytearray = new byte[filesize];
				InputStream inputStream = socket.getInputStream();
				/**
				 * FileInputStream used in the reading and writting files
				 */
				FileOutputStream fileOutputStream = new FileOutputStream(
						textField.getText().substring(4));
				/**
				 * BufferedInputStream used in the reading and writting input and output stream
				 */
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
						fileOutputStream);
				bytesRead = inputStream.read(bytearray, 0, bytearray.length);
				tot = bytesRead;

				do {
					bytesRead = inputStream.read(bytearray, tot,
							(bytearray.length - tot));
					if (bytesRead >= 0)
						tot += bytesRead;
				} while (bytesRead > -1);

				bufferedOutputStream.write(bytearray, 0, tot);
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				socket.close();
			} catch (Exception e) {

			}
		}
		/**
		 * It takes the put command and process the client request
		 */
		if (textField.getText().contains("PUT")) {

			try {
				Socket socket = new Socket("localhost", 40001);
				String[] splited = textField.getText().split("\\s");
				File transferFile = new File(splited[1]);
				byte[] bytearray = new byte[(int) transferFile.length()];
				/**
				 * FileInputStream used in the reading and writting files
				 */
				FileInputStream fileInputStream = new FileInputStream(
						transferFile);
				/**
				 * BufferedInputStream used in the reading and writting input and output stream
				 */
				BufferedInputStream bufferedInputStream = new BufferedInputStream(
						fileInputStream);
				bufferedInputStream.read(bytearray, 0, bytearray.length);
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(bytearray, 0, bytearray.length);
				outputStream.flush();
				fileInputStream.close();
				bufferedInputStream.close();
				socket.close();
			} catch (Exception e) {
			}}
		textField.setText("");
	}
	/**
	 * runnable interface use the run method to implemnts the interface
	 */
	public void run() {
		while (true) {
			try {
				textArea.append(b.readLine() + "\n");			
			} catch (Exception e) {
			}
		}
	}
	/**
	 * main method of the MyClient class .It calls the constructor
	 * @param args
	 */
	public static void main(String args[]) {
		MyClient client = new MyClient();
	}
	/**
	 * declaration of variables used in the client GUI and interaction with the server
	 */
	JButton button;
	JTextField textField;
	JTextArea textArea;
	JLabel label,l8;
	Socket s;
	PrintWriter p;
	BufferedReader b;
	Thread thread;
	String cmd;
}