package library;

import java.util.NoSuchElementException;

public class BookStack 
{
	private String[] items = new String[15];
	private int n= 0;
	
	/* 
	 * add item to stack
	 */
	public void push(String item) throws Exception
	{
		items[n++] = item;
	}
	
	/*
	 * remove items from stack
	 */
	public String pop() throws NoSuchElementException
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("Can't pop from empty stack"); 
		}
		String tmpVar = items[n-1];
		items[n-1] = null;
		n--;
		return tmpVar;
	}
	
	/*
	 * return top item from stack
	 */
	public String peek() 
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("Can't peek in empty stack"); 
		}
		return items[n-1];
	}
	
	/*
	 * return size of stack
	 */
	public int size()
	{
		return n; 
	}
	
	/*
	 * check if the stack is empty
	 */
	public Boolean isEmpty()
	{
		return (n == 0);
	}
	
	/*
	 * (return a string representation of the stack
	 * @see java.lang.Object#toString()
	 */
	public String[] toArray()
	{
		return items;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = n; i > 0; i--)
		{
			sb.append(items[i-1]).append(" ");
		}
		return sb.toString().trim();
	}
	

}	
