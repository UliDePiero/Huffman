package jhuffman.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Comparator;

import jhuffman.Huffman.CmpInteger;
import jhuffman.ds.Node;

public class BitReader
{
	private RandomAccessFile raf = null;
	private String sBuffer="";
	private int bitNo=0;
	
	public class campos
	{
		public Integer n = 0;
		public StringBuffer cod;
	}
	
	public class Table
	{
		public campos arr[] = new campos[256];
	
		public Table()
		{
			for (int i=0;i<256;i++)
			{
				arr[i] = new campos();
			}
		}

	}
	
	public BitReader(String filename)
	{
		// programar aqui
		try {
			raf = new RandomAccessFile(filename, "r");
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Table crearTabla(){
		Table tabla = new Table();
		try {
			int c = raf.read();
			while( c>=0 )
			{
				tabla.arr[c].n++;
				c = raf.read();
			}
			raf.close();
		}
		catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		close();
		return tabla;
	}
	
	public TreeUtil cargarArchivo() {
		// TODO Auto-generated method stub
		{
			Table tabla = crearTabla();
			try
			{
				//Cargo la tabla:	
				int c, nCod; 
				c = raf.read(); 		//Leo que caracter es.
				
				while (c>=0)
				{			
					nCod = raf.read(); 	//Leo la longitud del codigo.
											
					for(int j=0; j<nCod; j++)
					{						
						//Leo el codigo bit a bit.
						readBit();								
					}	
					tabla.arr[c].cod.append(sBuffer);
					if (nCod%8 != 0)
					{
						for(int j=nCod;j<(1+nCod/8)*8;j++) //Completo la lectura del byte antes de leer el otro caracter.
						{
							readBit();
						}
					}
					c = raf.read();								
				}			
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			close();
			
			//Creo lista:
			SortedList<Integer> lista = new SortedList<>();
			Comparator<Integer> cmp = new CmpInteger();
			for (int i=0;i<256;i++)
			{
				if(tabla.arr[i].n>0)
					lista.add(i,cmp);
			}
			Node aux=null;
			for(int i=lista.size()-1;i>=0; i--)
			{
				int x = lista.get(i);
				Node nodo=new Node(x,tabla.arr[i].n, aux);
				aux = nodo;
			}
			
			//Armo arbol:
			TreeUtil arbol = Lista_Arbol.crearArbolHuffman(aux);			
			
			return arbol;
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
	
	public int readBit()
	{
		// programar aqui
		try
		{
			if( sBuffer.length()==0 || bitNo==8 )
			{
				int b=raf.read();
				
				if( b>=0 )
				{
					sBuffer = Integer.toBinaryString(b);
					String ret=replicate(8-sBuffer.length(),'0')+sBuffer;
					sBuffer=ret.substring(0,8);
					bitNo=0;
				}
				else
				{
					return -1;
				}
			}
			
			return sBuffer.charAt(bitNo++)-'0';
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public boolean eof()
	{
		// programar aqui
		return false;
	}
		
	public void close()
	{
		// programar aqui	
		try
			{
				if(raf!=null) raf.close();
			}
			catch(Exception e2)
			{
				e2.printStackTrace();
				throw new RuntimeException(e2);
			}
	}
}
