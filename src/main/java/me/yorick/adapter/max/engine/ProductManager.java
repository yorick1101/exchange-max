package me.yorick.adapter.max.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoniter.JsonIterator;

import me.yorick.adapter.max.type.Product;

public class ProductManager {
	private Map<String, Product> products = new HashMap<>();
	private Map<String, List<Product>> baseCurrencyProducts = new HashMap<>(); 
	private Map<String, List<Product>> quoteCurrencyProducts = new HashMap<>(); 
	
	public ProductManager() throws IOException {
		JsonIterator iterator = JsonIterator.parse(ProductManager.class.getClassLoader().getResourceAsStream("markets.json"), 1024);
		while(iterator.readArray()) {
			Product product = iterator.read(Product.class);
			products.put(product.getId(), product);
			putCurrencyMap(baseCurrencyProducts, product.getBase_unit(), product);
			putCurrencyMap(quoteCurrencyProducts, product.getQuote_unit(), product);
			
		}
		
	}
	
	private void putCurrencyMap(Map<String, List<Product>> map, String key, Product product) {
		List<Product> list = map.computeIfAbsent(key, k-> new ArrayList<>());
		list.add(product);
	}
	
	public Map<String, Product> getProducts(){
		return products;
	}
	
	public Map<String, List<Product>> getBaseCurrencyProducts(){
		return baseCurrencyProducts;
	}
	
	public Map<String, List<Product>> getQuoteCurrencyProducts(){
		return quoteCurrencyProducts;
	}

}
