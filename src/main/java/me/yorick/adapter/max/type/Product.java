package me.yorick.adapter.max.type;

public class Product {
		
	/**
    	"id": "omgtwdt",
    	"name": "OMG/TWDT",
    	"base_unit": "omg",
    	"base_unit_precision": 3,
    	"quote_unit": "twdt",
    	"quote_unit_precision": 3
    **/
	
	private String id;
	private String name;
	private String base_unit;
	private int base_unit_precision;
	private String quote_unit;
	private int quote_unit_precision;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBase_unit() {
		return base_unit;
	}
	public void setBase_unit(String base_unit) {
		this.base_unit = base_unit;
	}
	public int getBase_unit_precision() {
		return base_unit_precision;
	}
	public void setBase_unit_precision(int base_unit_precision) {
		this.base_unit_precision = base_unit_precision;
	}
	public String getQuote_unit() {
		return quote_unit;
	}
	public void setQuote_unit(String quote_unit) {
		this.quote_unit = quote_unit;
	}
	public int getQuote_unit_precision() {
		return quote_unit_precision;
	}
	public void setQuote_unit_precision(int quote_unit_precision) {
		this.quote_unit_precision = quote_unit_precision;
	}
		
}
