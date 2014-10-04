package com.zappos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.parser.ParseException;


public class userInputs {

	public static void main(String[] args) throws IOException, ParseException {
		
		
		boolean val = true;
		double totalPrice=0;
		int numberOfItems=0;
		
		while(val)
		{
			System.out.println( "How many items do you want to buy? ");
			BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
			try{
			 numberOfItems= Integer.parseInt(br.readLine());
			}
			catch(NumberFormatException e)
			{
				System.out.println("invalid number. Please try again");
			}	
			
			if(numberOfItems < 1) 
			{
				System.out.println("Enter valid total items no.");
			} 
			
			
			 System.out.println( "What are the total price (in $)");
			 BufferedReader brp = new BufferedReader (new InputStreamReader(System.in));
			 try{
				 totalPrice = Double.parseDouble((brp.readLine()));			 
			 	}		 
			 catch (NumberFormatException e)
			 	{
				 System.out.println("invalid amount. please enter again ");
				 
			 	}
		
			 if(totalPrice <= 0) 
			{
				System.out.println("Enter valid total price");
			} 
			 else{
					val = false;
				}
		}
	
		
		
		productSearch searchPro = new productSearch(numberOfItems, totalPrice);
		System.out.println(searchPro.getGiftCombos());
	}
}