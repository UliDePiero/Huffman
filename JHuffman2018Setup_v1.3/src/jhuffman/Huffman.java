package jhuffman;

import java.util.Comparator;

import jhuffman.ds.Node;
import jhuffman.util.BitReader;
import jhuffman.util.BitWriter;
import jhuffman.util.Lista_Arbol;
import jhuffman.util.BitReader.Table;
import jhuffman.util.SortedList;
import jhuffman.util.TreeUtil;

public class Huffman
{
	public static void main(String[] args)
	{
		String filename = args[0];
		if( filename.endsWith(".huf") )
		{
			descomprimir(filename);
		}
		else
		{
			comprimir(filename);
		}
	}
	//Paso 1: Determinar la cantiadad de veces que aparece cada carácter.
	//Paso 2: Crear una lista enlazada.
	//Paso 3: Convertir la lista enlazada en un árbol Huffman.
	//Paso 4: Asignación de los códigos Huffman 
	//Paso 5: Codificación y decodificación del contenido.
	public static class CmpInteger implements Comparator<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return a-b;
		}
		
	}

	public static void comprimir(String filename)
	{
		// PROGRAMAR AQUI...
		//Codificación (compresión) 
		BitReader archivo = new BitReader(filename);
		Table tabla = archivo.crearTabla();
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
		TreeUtil arbol = Lista_Arbol.crearArbolHuffman(aux);
		StringBuffer sb = new StringBuffer();
		// primera hoja
		Node x = arbol.next(sb);
		while( x!=null )
		{
			tabla.arr[x.getC()].cod = sb;
			// siguiente hoja
			x = arbol.next(sb);
		}
		BitWriter archivoHuff = new BitWriter(filename+ ".huff");
		archivoHuff.grabarArchivo(tabla, filename);
	}
	
	public static void descomprimir(String filename)
	{
		// PROGRAMAR AQUI...
		//Decodificación (descompresión)
		// abro el archivo Huffman
		BitReader archivoHuff = new BitReader(filename);

		// leo el archivo y genero el arbol
		TreeUtil arbol = archivoHuff.cargarArchivo();

		// recupera el archivo original
		int index = filename.indexOf("."); 
	    String filenameOriginal=filename.substring(0, index);  
		BitWriter archivo = new BitWriter(filenameOriginal);
		archivo.restaurar(arbol, filename);
	}
}
