/*
 * Copyright 2004-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.belex.supplier;

import java.io.Serializable;
import java.util.Vector;

import org.belex.entry.Entry;

public class Supplier implements Serializable {

	private static final long serialVersionUID = 1L;

	private String supplierCode;
	private String supplierName;
	
	public class Order implements Serializable {
		private static final long serialVersionUID = 1L;

		private int number;
		private String letter;
		private String date;
		private int creationNumber;
		
		public Order() {
			setNumber(0);
			setLetter("");
			setDate("");
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getLetter() {
			return letter;
		}

		public void setLetter(String letter) {
			this.letter = letter;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public int getCreationNumber() {
			return creationNumber;
		}

		public void setCreationNumber(int creationNumber) {
			this.creationNumber = (creationNumber == 0 ? 1 : creationNumber);
		}
		
		public String toString() {
			return "number:" + number + ";letter:" + letter + ";date:" + date + ";creationNumber:" + creationNumber; 
		}
	}

	private Vector<Order> orders;

	private boolean pending;
	private boolean closed;
	private boolean opened;

	public static final int STATUS_PENDING = 1;
	public static final int STATUS_CLOSED = 2;
	public static final int STATUS_OPENED = 3;

	private Vector<Entry> requestedEntries;

	public Supplier() {
		setSupplierCode("");
		setSupplierName("");
		setRequestedEntries(new Vector<Entry>());
		setOrders(new Vector<Order>());
	}
	
	public Supplier(String supplierCode, String supplierName) {
		setSupplierCode(supplierCode);
		setSupplierName(supplierName);
		setRequestedEntries(new Vector<Entry>());
		setOrders(new Vector<Order>());
	}

	public String toString() {
		return supplierName + "(" + supplierCode + ")";
	}

	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public boolean isPending() {
		return pending;
	}
	public boolean isClosed() {
		return closed;
	}
	public boolean isOpened() {
		return opened;
	}
	public void setStatus(int newStatus){
		pending = false;
		closed = false;
		opened = false;
		if (newStatus == STATUS_PENDING) {
			pending = true;
		} else if (newStatus == STATUS_CLOSED) {
			closed = true;
		} else {
			opened = true;
		}
	}

	public Vector<Entry> getRequestedEntries() {
		return requestedEntries;
	}

	public void setRequestedEntries(Vector<Entry> requestedEntries) {
		this.requestedEntries = requestedEntries;
	}

	public Vector<Order> getOrders() {
		return orders;
	}

	public void setOrders(Vector<Order> orders) {
		this.orders = orders;
	}
	public void addOrder(Order order) {
		if (orders == null) {
			setOrders(new Vector<Order>());
		}
		if (order != null) {
			orders.add(order);
		}
	}
}