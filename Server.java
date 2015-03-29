/**
 * imported packages java.net.* as it provides the classes for implementing networking applications. and java.io.* as it provides for system input and output through data streams 
 */
import java.net.*;
import java.io.*;
/**
 * created class Hello1
 * @author VAIBHAV
 *
 */
public class Server 
{
	  /**
	   * calling constructor of Hello1 class
	   */
      public Server() throws IOException
      {
    	    /**
    	     * It listens the port no :10000 
    	     */
	        socket = new ServerSocket(10000);
	  }
      /**
       * declaration and implementation of myServe() method . This method is called from class main method
       *
       * @throws IOException
       */
	  public void myServe () throws IOException
	  {     
		   /**
		    * while loop is used and condition is kept true for infinite execution
		    */
	        while(true)
	        {   
	            /**
	             * accepted connection 
	             */
	        	Socket in = socket.accept();
	        	/**
	        	 * objects scocketWriter and socketReader created to read user command and to display output messages on the user screen
	        	 */
	            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter( in.getOutputStream()));
	            BufferedReader socketReader = new BufferedReader(new InputStreamReader( in.getInputStream()));
	            if( !connection( socketReader, socketWriter))
	            {
	                in.close();
	                continue;
	            }
	            /**
	             * try and catch is used to handle exception
	             */
	            try 
	            {   
	            	/**
	            	 * calls the userInput method to handle user commands
	            	 */
	                userInput( socketReader, socketWriter);
	            }
	            finally 
	            {
	            	/**
	            	 * close the connection
	            	 */
	                in.close();
	            }
	        }
	    }
	    /**
	     * Its a called method. It returns true or false by matching HELO command with the defined command in the program 
	     * @param socketReader
	     * @param socketWriter
	     * @return
	     * @throws IOException
	     */
	    private boolean connection( BufferedReader socketReader, BufferedWriter socketWriter)throws IOException
	    {
	    	   /**
	    	    * calling display method to display welcome message
	    	    */
	           display( "210 Welcome to the MINE server", socketWriter);
	           /**
	            * reads next command from user
	            */
	           String userinput = socketReader.readLine();
	           /**
	            * if HELO commands match returns true else return false
	            */
	           if( HELO.equals( userinput.substring( 0, 5).toUpperCase()))
	           {
	                display( "220 Hello " + userinput.substring( 5), socketWriter);
	                return true;
	            }
	           else
	           {
	                display( "550 you must give a HELO command", socketWriter);
	                return false;    
	            }
	        }
	        /**
	         * userInput method handles the user commands 
	         * @param socketReader
	         * @param socketWriter
	         * @throws IOException
	         */
	        private void userInput( BufferedReader socketReader,BufferedWriter socketWriter)throws IOException
	        {
	            while(true)
	            {  
	            	/**
	            	 * socketReader(object of BufferedReader class) calls readLine() method to read the user command from command line
	            	 */	              
	                String userinput = socketReader.readLine();
	                /**
	                 * use of switch statement to handle various command
	                 */
	                /**
	                 * for handling special quit command
	                 */
	                if( userinput.toUpperCase().equals( QUIT))
	                {
                        display( "100 Don't go away mad, just go away",socketWriter);
                        break;
                    }
	                switch(userinput.toUpperCase())
	                {
	                    case MINE:
	                        display("403 No, it's mine", socketWriter);
	                        break;
	                    case YOURS:
	                        display("203 Of course it's mine", socketWriter);
	                        break;
	                    case SHARE:
	                        display("404 Not on your life", socketWriter);
	                        break;
	                    default:
	                        display( "401 Unrecognized input", socketWriter);
	                }
	            }
	        }
            /**
             * display method is used to display text on the user screen
             * @param s
             * @param w
             * @throws IOException
             */
	        private void display( String s, BufferedWriter w)throws IOException 
	        {
	            w.write( s);
	            w.newLine();
	            w.flush();
	        }
	 /**
	   * Declaration of main method. 
	   */
	public static void main(String[] args) throws IOException 
	{   
		/**
		 * Initialize object as a null
		 */
		Server server = null;
		/**
		 * try and catch to handle exception
		 */
        try
        {  
           /**
         	 * created an object of class Server
         	 */    	 
           server = new Server();
           /**
            * object calling myServe method
            */
           server.myServe();
        }              
        /**
         * Exception thrown by try block will be handle by catch block
         */        
        catch ( IOException ex)
        {
       	   /**
            * If exception occurs then it terminates the program
            */
       	   System.exit( 1);
        }		
	}
	/**
	 * Declaration of variable used in the program . It is defined as final as switch case handle only constant value
	 */	
    final String HELO = "HELO ";
    final String MINE = "MINE";
    final String YOURS = "YOURS";   
    final String SHARE = "SHARE";
    final String QUIT= "QUIT" ;
    private ServerSocket socket;
}
