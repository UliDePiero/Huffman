package jhuffman.util;

import java.util.Comparator;
import java.util.LinkedList;

public class SortedList<T> extends LinkedList<T>
{
	public void add(T t,Comparator<T> cmpTT)
	{
		int i=0;
		while( i<size() && cmpTT.compare(get(i),t)<=0)
		{
			i++;
		}
		
		add(i,t);
	}
}
