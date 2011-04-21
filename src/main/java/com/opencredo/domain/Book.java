/*
 * Copyright 2005-2008 the original author or authors.
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

package com.opencredo.domain;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public class Book implements Comparable<Book>, Serializable, Cloneable {

	private static final long serialVersionUID = 5L;

	private Integer key;
	private String author;
	private String title;
	private String description;
	private Date availability;
	private Integer count;
	private URL website;
	private byte[] coverPng;

	private int savedHashCode = Integer.MIN_VALUE;

	public Book() {
		super();
	}

	public Book(String author, String title, Integer count) {
		super();
		this.setAuthor(author);
		this.setTitle(title);
		this.setCount(count);
	}

	public Book(String author, String title, int count) {
		this(author, title, Integer.valueOf(count));
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		if (author == null)
			throw new IllegalArgumentException("author may not be null");
		this.author = author;
		this.savedHashCode = Integer.MIN_VALUE;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title == null)
			throw new IllegalArgumentException("title may not be null");
		this.title = title;
		this.savedHashCode = Integer.MIN_VALUE;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getAvailability() {
		return (availability == null ? null : (Date) availability.clone());
	}

	public void setAvailability(Date availability) {
		this.availability = (availability == null ? null : (Date) availability.clone());
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public URL getWebsite() {
		return website;
	}

	public void setWebsite(URL website) {
		this.website = website;
	}

	public byte[] getCoverPng() {
		return (coverPng == null ? null : coverPng.clone());
	}

	public void setCoverPng(byte[] coverPng) {
		this.coverPng = (coverPng == null ? null : coverPng.clone());
	}

	public void incrementCount(Integer increment) {
		int count = this.count.intValue() + increment.intValue();
		if (count < 0)
			count = 0;
		this.count = Integer.valueOf(count);
	}

	public int compareTo(Book book) {
		if (book == null)
			throw new IllegalArgumentException("Cannot compare to null object");
		if (this.author == null || this.title == null)
			throw new IllegalArgumentException(
					"This object is not initialized yet");
		if (this.equals(book))
			return 0;
		int res = getAuthor().compareTo(book.getAuthor());
		if (res != 0)
			return res;
		return getTitle().compareTo(book.getTitle());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Book))
			return false;
		if (this.author == null || this.title == null)
			return false;
		Book book = (Book) obj;
		return (this.author.equals(book.getAuthor()) && this.title.equals(book.getTitle()));
	}

	@Override
	public int hashCode() {
		if (Integer.MIN_VALUE == this.savedHashCode) {
			String hashStr = this.getClass().getName() + ":" + this.toString();
			this.savedHashCode = hashStr.hashCode();
		}
		return this.savedHashCode;
	}

	@Override
	public String toString() {
		return this.author + ":" + this.title;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		Book book = (Book) super.clone();

		if (this.getKey() != null)
			book.setKey(this.getKey());
		if (this.getAuthor() != null)
			book.setAuthor(this.getAuthor());
		if (this.getTitle() != null)
			book.setTitle(this.getTitle());
		if (this.getDescription() != null)
			book.setDescription(this.getDescription());
		if (this.getAvailability() != null)
			book.setAvailability(this.getAvailability());
		if (this.getCount() != null)
			book.setCount(this.getCount());
		if (this.getWebsite() != null)
			book.setWebsite(this.getWebsite());
		if (this.getCoverPng() != null)
			book.setCoverPng(this.getCoverPng());

		return book;
	}
}
