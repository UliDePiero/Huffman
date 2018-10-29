package jhuffman.util;

import jhuffman.ds.Node;

public class Lista_Arbol {
	static Node p;
	
	public static Node sacarPrimerNodo()
	{
		Node aux = p;
		p = p.getSig();
		return aux;
	}

	public static void agregarNodo(Node n)
	{
		Node nuevo= new Node();
		nuevo.setC(n.getC());
		nuevo.setN(n.getN());
		nuevo.setDer(n.getDer());
		nuevo.setIzq(n.getIzq());
		nuevo.setSig(null); 
		Node ant = null;
		Node aux = p;
		while( aux!=null && compararNodo(aux, n)<=0)
		{
			ant = aux;
			aux = aux.getSig();
		}
		if( ant==null )
		{
			p = nuevo;
		}
		else
		{
			ant.setSig(nuevo);
		}
		nuevo.setSig(aux);
	}
	
	public static TreeUtil crearArbolHuffman(Node alpha)
	{	
		p=alpha;
		Node izq= sacarPrimerNodo();
		int i = 257;
		while(p!=null)
		{
			Node der = sacarPrimerNodo();
			Node aux = new Node();
			aux.setDer(izq);
			aux.setIzq(der);
			aux.setN(izq.getN()+der.getN());
			aux.setC(i);
			agregarNodo(aux);
			izq = sacarPrimerNodo();
			i++;
		}
		
		TreeUtil ut = new TreeUtil(izq);
		return ut;
	}
	
	public static long compararNodo(Node n1, Node n2)
	{
		return n1.getN()<n2.getN()?-1:
			   n1.getN()>n2.getN()?1:
			   n1.getC()<n2.getC()?0:1;
	}
}
