import java.io.*;

import javax.swing.JOptionPane;
public class FileRead 
{
	public FileRead(){
	      try{
	  		// Open the file that is the first 
	  		// command line parameter
	    	String fname =  JOptionPane.showInputDialog("Enter Class Description");
	  		FileInputStream fstream = new FileInputStream(fname);
	  		// Get the object of DataInputStream
	  		DataInputStream in = new DataInputStream(fstream);
	          BufferedReader br = new BufferedReader(new InputStreamReader(in));
	  		String strLine;
	  		//Read File Line By Line
	  		int i1,i2,i3,i4;
			int fi = 0,li;
			int len;
			int count = 0;

	  		while ((strLine = br.readLine()) != null) 	{
	  			// Print the content on the console
	  			fi = 0;
	  			System.out.println (strLine);
	  			len = strLine.length();
					li = strLine.indexOf(' ');
					i1 = Integer.parseInt(strLine.substring(fi,li));
					fi = li+1;
					li = strLine.indexOf(' ',fi);
					i2 = Integer.parseInt(strLine.substring(fi,li));
					fi = li+1;
					li = strLine.indexOf(' ',fi);
					i3 = Integer.parseInt(strLine.substring(fi,li));
					fi = li+1;
					i4 = Integer.parseInt(strLine.substring(fi,len));
	  			System.out.println("i4 "+i4);
	  		}
	  		//Close the input stream
	  		in.close();
	  		}catch (Exception e){//Catch exception if any
	  			System.err.println("Error: " + e.getMessage());
	  		}
	  		

	  		
	}
   public static void main(String args[])
	{
       FileRead f = new FileRead();
	}
}

