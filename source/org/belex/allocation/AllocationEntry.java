package org.belex.allocation;

import java.io.Serializable;
import java.util.ArrayList;

import org.belex.entry.Entry;
import org.belex.product.ProductTraceable;

public class AllocationEntry extends Entry implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean selected = false;;
	private String closed;

	private ArrayList<ProductTraceable> products = new ArrayList<ProductTraceable>();

	public AllocationEntry(){
		super();
	}

	public String getClosed() {
		return closed;
	}
	public void setClosed(String closed) {
		this.closed = closed;
	}

	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public ArrayList<ProductTraceable> getProducts() {
		if (products == null) {
			products = new ArrayList<ProductTraceable>();
		}
		return products;
	}
	public void setProducts(ArrayList<ProductTraceable> products) {
		this.products = products;
	}
	public void addProducts(ArrayList<ProductTraceable> products) {
		this.products.addAll(products);
	}
}
