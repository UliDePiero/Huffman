package demo;

import java.io.RandomAccessFile;

public class FileDemo
{
	public static void main(String args[])
	{
		try
		{
			RandomAccessFile raf = new RandomAccessFile("cocorito.txt","r");
			
			int c = raf.read();
			while( c>=0 )
			{
				System.out.print((char)c);
				c = raf.read();
			}
			
			raf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
