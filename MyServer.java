
/**
 * imported java packages used in the program
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
/**
 * MyServer is the class where server is created.
 * It processes the client requests  
 * @author VAIBHAV
 *
 */
public class MyServer 
{	
	
	/**
	 * connect() method is used to form the connection between client and server
	 * @param s
	 * @throws Exception
	 */
	public static void Connect(ServerSocket s) throws Exception {
		for ( ; ;) {
			/**
			 * accewpting client request
			 */
			incoming = s.accept();
			/**
			 * BufferedInputStream used in the reading and writting input and output stream
			 */
			in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			/**
			 * TO display the output on the client's screen
			 */
			out = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()));
			/**
			 * welcome message shows the client and server connection is formed
			 */
			out.println("210 Welcome to the MINE server");
			out.flush();
			/**
			 * It creates a new thread
			 */
			Thread th = new Thread();
			/**
			 * set the tread as a daemon
			 */
			th.setDaemon(true);
			/**
			 * starts the thread
			 */
			th.start();
			/**
			 * calls the readcommand method to read the command from client and print on the client side 
			 * 
			 */
			readCommand(in, out);
		}
	}
	/**
	 * runnable interface use the run method to implemnts the interface
	 */
	public void run() {
		for( ; ;) {
			try {
				out.println(in.readLine());
			} catch (Exception e) {
			}
		}
	}
	/**
	 * reads the commands from the clients side and process it
	 * @param in
	 * @param out
	 */
	public static void readCommand(BufferedReader in, PrintWriter out) {
		boolean init = false;
		for( ; ;) {
			String str;
			try {
				str = in.readLine();
				if (str == null) {
					break;
				}
				/**
				 * It starts with the HELO command.
				 * It prints the output when HELO commands enter
				 */
				else if (!init && str.startsWith("HELO")) {
					init = true;
					out.println("220 How are you doing" + str.substring(4)
							+ "?");
				}  
				/**
				 * It deals with the CD command.
				 * It prints the output when HELO commands enter
				 */
				else if (init && str.startsWith("CD")) {
					File dir = new File(str.substring(3));
					if (dir.isDirectory() == true) {
						System.setProperty("user.dir", dir.getAbsolutePath());
						out.println("201 Directory Changed");
					} else {
						out.println("401 Directory Change failed.");
					}
				}
				/**
				 * It deals with the pwd command.
				 * It prints the output when PWD commands enter
				 */
				else if (init && str.equals("PWD")) {
					String pwd = System.getProperty("user.dir"); 
					if (pwd == "" || pwd == null) {
						out.println("402 No current directory");
					}
					out.println("202 " + pwd);
				}
				/**
				 * It deals with the CR OR LF command.
				 * It prints the output when HELO commands enter
				 */
				else if (init && str.equals("CR") || init && str.equals("LF")) {
					out.println("402 No current directory");
					
				} 
				/**
				 * It deals with the LS command.
				 * It prints the output when HELO commands enter
				 */
				
				else if (init && str.equals("LS")) {
					File dir = new File(System.getProperty("user.dir"));
					String dir_List[] = dir.list();
					out.println("203 list follows, terminated by .\n");
					for (String file : dir_List) {
						out.println("." + file);
					}
					out.println(".");
				} 
				/**
				 * It deals with the GET command.
				 * It prints the output when GET commands enter
				 */
				else if (init && str.startsWith("GET")) {
					ServerSocket serverSocket = new ServerSocket(40001);
					Socket socket = serverSocket.accept();
					try {
						File transferFile = new File(str.substring(4));
						byte[] bytearray = new byte[(int) transferFile.length()];
						/**
						 * FileInputStream used in the reading and writting files
						 */
						FileInputStream fileInputStream = new FileInputStream(transferFile);
						/**
						 * BufferedInputStream used in the reading and writting input and output stream
						 */
						BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
						bufferedInputStream.read(bytearray, 0, bytearray.length);
						OutputStream outputStream = socket.getOutputStream();
						outputStream.write(bytearray, 0, bytearray.length);
						fileInputStream.close();
						bufferedInputStream.close();
						outputStream.flush();
						out.println("203 Content-Length:" + bytearray.length);
						out.println("This is the silly file being returned");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						out.println("403 No such file");
						e.printStackTrace();
					}
					socket.close();
					serverSocket.close();
					} else if (init && str.startsWith("PUT")) {
					ServerSocket serverSocket = new ServerSocket(40001);
					Socket socket = serverSocket.accept();
					try {
						File transferFile = new File(str.substring(4));
						byte[] bytearray = new byte[(int) transferFile.length()];
						FileInputStream fileInputStream = new FileInputStream(
								transferFile);
						BufferedInputStream bufferedInputStream = new BufferedInputStream(
								fileInputStream);
						bufferedInputStream
								.read(bytearray, 0, bytearray.length);
						OutputStream outputStream = socket.getOutputStream();
						outputStream.write(bytearray, 0, bytearray.length);
						fileInputStream.close();
						bufferedInputStream.close();
						outputStream.flush();
						out.println("204 ok to send file:");
						out.println("This is the silly file being sent");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					socket.close();
					serverSocket.close();
					}
					/**
					 * It deals with the QUIT command.
					 * It prints the output when QUIT commands enter
					 */
					else if (str.trim().equals("QUIT")) {
					out.println("100 Don't go way mad, just go away");
					out.println("Please close the Window");
					out.flush();
					break;
				} 
				/**
				 * It deals when there is bad command .
				 * It prints the output when other than mentioned command is entered
				 */
					else if (!init) {
					out.println("405 starts with a HELO command	!!!!");
				} else {
					out.println("401 Unrecognized input. !!!!!!");
						}
				out.flush();

			}
			/**
			 * If there is any error then it is handle by the catch block
			 */
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return;
	}
	/**
	 * Main method of the MyServer class from where execution starts
	 * @param args
	 */
	public static void main(String[] args) 
	{
		/**
		 * try and catch is used to handle the exception
		 */
		try {
			/**
			 * It creates server socket 
			 */
			ServerSocket s = new ServerSocket(40000); 
			/**
			 * accepting connection
			 */
			Connect(s);
			s.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * declaration of variables used in creating server
	 */
	static BufferedReader in; 
	static PrintWriter out; 
	static Socket incoming; 
} 