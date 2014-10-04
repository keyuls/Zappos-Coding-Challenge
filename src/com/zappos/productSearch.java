package com.zappos;

import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class productSearch {
	private int numItems;			
	private double totalPrice;		
	private double maxPrice;		
	private int page;				
	private JSONArray products;		
	private ArrayList<productDetails> productObjects; 
	private ArrayList<productCombo> productCombos; 
	private final double TOL = Math.pow(10, -7);  
	private final int MAXCOMBOS = 30;
	
	
	public productSearch(int num, double total) {
		numItems = num;
		totalPrice = total;
		maxPrice = Integer.MAX_VALUE; 	
		page = 1;					
		products = new JSONArray();
		productObjects = new ArrayList<productDetails>();
		productCombos = new ArrayList<productCombo>();
	}
	
	
	private Double getPrice(Object item)
	{
		return Double.parseDouble(((String) ((JSONObject) item).get("price")).substring(1));
	}
	
	private void setProductsInRange() throws IOException, ParseException {
		String reply = jsonConnection.httpPost(jsonConnection.BASEURL + "&term=&limit=100&sort={\"price\":\"asc\"}");
		JSONObject replyObject = jsonConnection.parseReply(reply);
		JSONArray resultArray = jsonConnection.getResults(replyObject);
		
		
		double firstPrice = getPrice(resultArray.get(0));
		
				if( (firstPrice * numItems) > totalPrice)
				{
					products = null;
				}
		
				maxPrice = totalPrice - (numItems - 1)*(firstPrice);
		
		page++;
		
		Double lastPrice = getPrice(resultArray.get(resultArray.size() - 1));
		
		while(lastPrice < maxPrice) { 
			String nextPage = jsonConnection.httpPost(jsonConnection.BASEURL + "&term=&limit=100&sort={\"price\":\"asc\"}&page=" + page);
			JSONObject nextObject = jsonConnection.parseReply(nextPage);
			JSONArray nextArray = jsonConnection.getResults(nextObject);
			
			resultArray.addAll(nextArray);
			
			lastPrice = getPrice(nextArray.get(nextArray.size() - 1));
			
			page++;
		}

		products = resultArray;
	}
	

	private void setSearchableProducts() {
		productObjects.add(new productDetails((JSONObject)products.get(0)));
		
		int already = 1;
		int numPrices = 1;

		for(int i = 1; i < products.size() && getPrice(products.get(i)) < maxPrice; i++) {
			double currentPrice = getPrice(products.get(i));
			if( currentPrice > productObjects.get(numPrices-1).getPrice()) {
				productObjects.add(new productDetails((JSONObject)products.get(i)));
				numPrices++;
				already = 1;
			} else if(Math.abs(currentPrice - productObjects.get(numPrices-1).getPrice()) < TOL && already < numItems){
				productObjects.add(new productDetails((JSONObject)products.get(i)));
				numPrices++;
				already++;
			} else {
				while(i < products.size() && Math.abs(currentPrice - productObjects.get(numPrices-1).getPrice()) < TOL) {
					i++;
					currentPrice = getPrice(products.get(i));
				}
				i++;
				already = 0;
			}
		}
	}

	
	private void setProductCombos() {
		setProductCombosRecursive(productObjects, totalPrice, new ArrayList<productDetails>());
	}
		
	private void setProductCombosRecursive(ArrayList<productDetails> productList, double target, ArrayList<productDetails> partial) {
		int priceWithinAmount = 1;
		
			if(partial.size() > numItems) { return; }
		
		double sum = 0;
		for(productDetails p : partial) 
		{
			sum += p.getPrice();
		}
		
		if(Math.abs(sum - target) < priceWithinAmount && partial.size() == numItems && productCombos.size() < MAXCOMBOS) {
		
			if(productCombos.size() == 0) {	productCombos.add(new productCombo(partial, totalPrice)); }
			else{
				productCombo testerCombo = productCombos.get(productCombos.size() -1);
				productCombo partialCombo = new productCombo(partial, totalPrice);
				if(!partialCombo.equals(testerCombo)) {
					productCombos.add(partialCombo);
				}
			}
		}
		if(sum >= target + priceWithinAmount) {
			return;
		}
		
			for(int i = 0; i < productList.size() && !(partial.size() == numItems && sum < target); i++){
			ArrayList<productDetails> remaining = new ArrayList<productDetails>();
			productDetails n = productList.get(i);
			for(int j=i+1; j < productList.size(); j++) {remaining.add(productList.get(j)); }
			ArrayList<productDetails> partial_rec = new ArrayList<productDetails>(partial);
			partial_rec.add(n);
			setProductCombosRecursive(remaining, target, partial_rec);
		}
	}
	
	public String getGiftCombos() throws IOException, ParseException {
		System.out.println("Search begins");
		this.setProductsInRange();
		this.setSearchableProducts();
		this.setProductCombos();
		
		if(productCombos.size() != 0) {
			String toPrint = "\n Products Find!\n";
			for(productCombo pc:productCombos)
			{
				toPrint += pc.toString() + "\n";
			}
			return toPrint;
		}
		else {
				return "We couldn't find items matching your criteria. " +
					"Please try again";
		}
	}


}
