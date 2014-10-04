package com.zappos;

import java.util.ArrayList;

import com.zappos.productDetails;

public class productCombo{
	private ArrayList<productDetails> comboProducts;		//prices to sort, etc.
	private double sum;							//sum of the prices
	private double idealTotal; 					//total prices should be near
	private double closeness;					//how close sum is to total
	private final double TOL = Math.pow(10, -7);//tolerance for comparing doubles
	

	public productCombo(ArrayList<productDetails> productsForCombo, double total)
	{
		comboProducts = productsForCombo;
		sum = 0;
		idealTotal = total;
		for(productDetails p:comboProducts)
		{	
			sum += p.getPrice(); 
		}
			closeness = Math.abs(idealTotal - sum);
	}
	
	
	public double getPrice(int index) 
	{
		return comboProducts.get(index).getPrice();
	}
	
	
	public double getSum()
	{
		return sum;
	}
	

	public int getProductComboLength()
	{
		return comboProducts.size();
	}
	
	
	public double getCloseness() 
	{
		return closeness;
	}
	
	
	public double getTotal()
	{
		return idealTotal;
	}

		
	public int compareTo(Object o)
		{
		productCombo other = (productCombo) o;
		if(this.equals(other))
		{
			return 0;
		}
		else if(this.closeness < other.getCloseness())
		{
			return -1;
		}
		else{
			return 1;
			}
	}
	
	
	public boolean equals(productCombo other) 
	{
		if(this.comboProducts.size() != other.getProductComboLength())
		{
			return false;
		}
		if(this.idealTotal != other.getTotal())
		{
			return false;
		}
		
		for(int i = 0; i < comboProducts.size(); i++)
		{
			if(Math.abs(this.comboProducts.get(i).getPrice() - other.getPrice(i)) > TOL) 
			{
				return false;
			}
		}
		return true;
	}
	
	
	public String toString() 
	{
		String toReturn = "Products with sum $" + sum + "\n";
		
		for(int i = 0; i < comboProducts.size(); i ++) 
		{
			toReturn += (i+1) + ": " + comboProducts.get(i).toString() + "\n";
		}
		return toReturn;
	}
	
}

