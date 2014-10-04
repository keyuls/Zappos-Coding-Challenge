package com.zappos;

import org.json.simple.JSONObject;

public class productDetails {
	private double price;		
	private String productId;			
	private String productName;		
	private String productStyleId;		
	private String priceToString;		
	
public productDetails(JSONObject productDetails)
	{
		
		price = Double.parseDouble(((String) productDetails.get("price")).substring(1));
		productId = (String)productDetails.get("productId");
		productName = (String)productDetails.get("productName");
		productStyleId = (String)productDetails.get("styleId");
		priceToString = String.format("%.2f", price);
	}
	
	
	public String toString() 
	{
		return productName + ", $" + priceToString + " (productId:" + productId + ", productStyleId:" + productStyleId + ")";
	}
	
	public double getPrice() 
	{
		return price;
	}


}
