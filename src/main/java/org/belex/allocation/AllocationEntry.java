package org.belex.allocation;

import org.belex.entry.Entry;
import org.belex.product.ProductTraceable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AllocationEntry extends Entry implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean selected = false;;
	private String closed;

	private List<ProductTraceable> products = new ArrayList<>();

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
	public List<ProductTraceable> getProducts() {
		if (products == null) {
			products = new ArrayList<>();
		}
		return products;
	}
	public void setProducts(List<ProductTraceable> products) {
		this.products = products;
	}
	public void addProducts(List<ProductTraceable> products) {
		this.products.addAll(products);
	}
}
