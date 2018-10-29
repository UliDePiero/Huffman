package demo;

import jhuffman.ds.Node;
import jhuffman.util.TreeUtil;

public class TreeDemo
{
	public static void main(String[] args)
	{
		// crea el arbol para COMO COME COCORITO... y retorna su raiz
		Node root = crearArbolHuffman();
		
		StringBuffer sb = new StringBuffer();
		TreeUtil ut = new TreeUtil(root);
		
		// primera hoja
		Node x = ut.next(sb);
		while( x!=null )
		{
			// muestro el codigo Huffman
			System.out.println((char)x.getC()+": "+sb);
			
			// siguiente hoja
			x = ut.next(sb);
		}
	}

	private static Node crearArbolHuffman()
	{
		// nivel 5 (ultimo nivel)
		Node nS=new Node('S',1,null,null);
		Node nR=new Node('R',1,null,null);
		Node nN=new Node('N',1,null,null);
		Node nI=new Node('I',1,null,null);
		// nivel 4
		Node a2=new Node(256+2,2,nS,nR);
		Node a1=new Node(256+1,2,nN,nI);
		Node nT=new Node('T',2,null,null);
		Node nE=new Node('E',2,null,null);
		Node nA=new Node('A',2,null,null);
		Node nU=new Node('U',1,null,null);
		// nivel 3
		Node nC=new Node('C',7,null,null);
		Node nM=new Node('M',5,null,null);
		Node nESP=new Node(' ',5,null,null);
		Node a5=new Node(256+5,4,a2,a1);
		Node a4=new Node(256+4,4,nT,nE);
		Node a3=new Node(256+3,3,nA,nU);
		// nivel 2
		Node nO=new Node('O',11,null,null);
		Node a8=new Node(256+8,12,nC,nM);
		Node a7=new Node(256+7,9,nESP,a5);
		Node a6=new Node(256+6,7,a4,a3);
		// nivel 1
		Node a10=new Node(256+10,23,a8,nO);
		Node a9=new Node(256+9,16,a7,a6);
		// nivel 0 (raiz)
		Node a11=new Node(256+11,39,a10,a9);
		return a11;
	}
}
