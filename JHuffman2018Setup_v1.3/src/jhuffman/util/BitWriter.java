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
	private int bitNo=0;
	
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
					System.out.println("caracter: "+i);																							//CONSOLA
					//int nCod = tabla.arr[i].cod.length(); 							
					int nCod = tabla.arr[i].codigo.len;
					raf.write(nCod); 	// Grabo la longitud del codigo.
					System.out.println("largo codigo: "+tabla.arr[i].codigo.len);																//CONSOLA
					raf.write(tabla.arr[i].n); 	// Grabo la cantidad de veces que aparece el caracter.
					System.out.println("cantidad de veces: "+tabla.arr[i].n);																//CONSOLA
					for(int j=0; j<nCod; j++)
					{
						//writeBit(tabla.arr[i].cod.codePointAt(j)); 	//Grabo el codigo bit a bit.
						writeBit(tabla.arr[i].codigo.arr[j]); 	//Grabo el codigo bit a bit.
						System.out.println(tabla.arr[i].codigo.arr[j]);	
					}	
					flush(); //Completo el byte con ceros. (Como tenes la longitud del codigo, estos '0' extras no generan conflicto)
					//aca deberia ir el byte separador pero cual seria?
				}
			}
			raf2 = new RandomAccessFile(filename, "r");
			raf.write(-1); 																		//Byte separador
			raf.writeLong(raf2.length());
			int c = raf2.read();
			while (c>=0)
			{			
				//for(int i=0; i<tabla.arr[c].cod.length(); i++)
				for(int i=0; i<tabla.arr[c].codigo.len; i++)
				{
					//writeBit(tabla.arr[c].cod.codePointAt(i)); 	//Grabo el codigo bit a bit.
					writeBit(tabla.arr[c].codigo.arr[i]); 	//Grabo el codigo bit a bit.
					System.out.println(tabla.arr[c].codigo.arr[i]);																				//CONSOLA
				}			
				
				c = raf2.read();
			}
			flush();  //No genera conflicto porque se reserva el tamaño del archivo original? o del codigo de huff?
			close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void restaurar(TreeUtil arbol, String filename) {
		// TODO Auto-generated method stub
		try
		{
			//BitReader archivo = new BitReader(filename);
			raf2 = new RandomAccessFile(filename, "r");
			System.out.println("+++++++++++++++++++++++++++++++++++++");	
			int c = 0;
			while (c>=0 && c<255) c=raf2.read();
			long longitudArchivoOriginal = raf2.readLong();
			System.out.println("Longitud archivo: "+longitudArchivoOriginal);
			//archivo.posicionarPunteroEnArchivo(longitudArchivoOriginal);
			Node nodo = arbol.raiz;																		 //sera la raiz?
			System.out.println("Caracter raiz: "+arbol.raiz.getC());
			for (long l=0; l<longitudArchivoOriginal; l++)
			{
				while (nodo.getIzq()!=null || nodo.getDer()!=null)
				{
					//int bit = archivo.readBit();
					int bit = readBit();
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
				System.out.println("caracter: "+nodo.getC());	
				nodo = arbol.raiz;
			}		
			close();
			//archivo.close();
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
				//System.out.println("codigo: "+x);																				//CONSOLA
				sBuffer="";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	public int readBit() //revisar //revisado
	{
		// programar aqui
		try
		{
			if( sBuffer.length()==0 || bitNo==8 )							//problema en segunda leida //corregido //NO TOCAR, se usa en BitWRITER.restaurar
			{
				int b=raf.read();
				
				if( b>=0 )
				{
					sBuffer = Integer.toBinaryString(b);
					//System.out.println("Codigo: "+sBuffer);									//CONSOLA
					String ret=replicate(8-sBuffer.length(),'0')+sBuffer;
					sBuffer=ret.substring(0,8);
					System.out.println("CodigoB: "+sBuffer);									//CONSOLA
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
