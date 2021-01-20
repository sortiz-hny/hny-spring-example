package com.honeycomb.demo.hnyexample.model;

public class Quote {
	
	private String type;
	private SpringQuoteValue value;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SpringQuoteValue getValue() {
		return value;
	}

	public void setValue(SpringQuoteValue value) {
		this.value = value;
	}
	
	public static class SpringQuoteValue {
		private long id;
		private String quote;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getQuote() {
			return quote;
		}
		public void setQuote(String quote) {
			this.quote = quote;
		}
		
	}

}
