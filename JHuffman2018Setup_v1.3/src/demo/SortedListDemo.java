package demo;

import java.util.Comparator;

import jhuffman.util.SortedList;

public class SortedListDemo
{
	public static void main(String[] args)
	{
		// lista de enteros, de menor a mayor
		SortedList<Integer> lst = new SortedList<>();
		
		Comparator<Integer> cmp = new CmpInteger();

		lst.add(3,cmp);
		lst.add(1,cmp);
		lst.add(2,cmp);
		lst.add(4,cmp);

		for(int i=0;i<lst.size(); i++)
		{
			int x = lst.get(i);
			System.out.println(x);
		}
	}
	
	static class CmpInteger implements Comparator<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return a-b;
		}
		
	}
}
