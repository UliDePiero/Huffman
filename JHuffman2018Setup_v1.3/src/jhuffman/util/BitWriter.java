package jhuffman.util;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import jhuffman.ds.Node;
import jhuffman.util.BitReader.Table;

public class BitWriter
{
	private RandomAccessFile raf = null;
	private RandomAccessFile raf2 = null;
	private String sBuffer="";
	
	public BitWriter(String filename)
	{
		// programar aqui	
		try {
			raf = new RandomAccessFile(filename, "rw");
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void grabarArchivo(Table tabla, String filename)
	{
		try
		{
			for(int i=0; i<256; i++)
			{
				if(tabla.arr[i].n>0)
				{
					raf.write(i); 		//Grabo el caracter.	
					int nCod = tabla.arr[i].cod.length(); 							
					raf.write(nCod); 	// Grabo la longitud del codigo.					
					for(int j=0; j<nCod; j++)
					{
						writeBit(tabla.arr[i].cod.codePointAt(j)); 	//Grabo el codigo bit a bit.
					}	
					flush(); //Completo el byte con ceros.
				}
			}
			raf2 = new RandomAccessFile(filename, "r");
			raf.writeLong(raf2.length());
			int c = raf2.read();
			while (c>=0)
			{			
				for(int i=0; i<tabla.arr[c].cod.length(); i++)
				{
					writeBit(tabla.arr[c].cod.codePointAt(i)); 	//Grabo el codigo bit a bit.
				}			
				
				c = raf2.read();
			}
			flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		close();
	}
	
	public void restaurar(TreeUtil arbol, String filename) {
		// TODO Auto-generated method stub
		try
		{
			BitReader archivo = new BitReader(filename);
			raf2 = new RandomAccessFile(filename, "r");
			long longitudFile = raf2.length();
			Node nodo = arbol.raiz;
			
			for (long l=0; l<longitudFile; l++)
			{
				while (nodo.getIzq()!=null || nodo.getDer()!=null)
				{
					int bit = archivo.readBit();
					if (bit==0)			
					{
						nodo = nodo.getIzq();						
					}
					else
					{
						nodo = nodo.getDer();
					}
				}
				raf.write(nodo.getC());	
				nodo = arbol.raiz;
			}		
			close();
			archivo.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
			
	}
	
	public void writeBit(int bit)
	{
		// programar aqui
		try
		{
			// concateno el bit
			sBuffer+=(bit==1)?'1':'0';
			
			if( sBuffer.length()==8 )
			{
				int x = Integer.parseInt(sBuffer, 2);
				raf.write(x);
				sBuffer="";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	public static String replicate(int n, char c)
	{
		String x="";
		for(int i=0; i<n; i++)
		{
			x+=c;
		}
		
		return x;
	}
	public void flush()
	{
		// programar aqui	
		try
		{
			if( raf==null )
			{
				return;
			}
			

			// completo con ceros
			if( sBuffer.length()>0 )
			{			
				String ret=sBuffer+replicate(8-sBuffer.length(),'0');
				sBuffer=ret.substring(0,8);
			}
			
			// grabo los bits que no completaron 1 byte
			if( sBuffer.length()>0 )
			{
				int x = Integer.parseInt(sBuffer, 2);
				raf.write(x);
			}
			
			//bitNo=0;
			sBuffer="";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void close()
	{
		// programar aqui
		try
		{
			if(raf!=null) raf.close();
			if(raf2!=null) raf2.close();
		}
		catch(Exception e2)
		{
			e2.printStackTrace();
			throw new RuntimeException(e2);
		}
	}
}
