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

package com.opencredo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import com.opencredo.domain.Book;


@Service(value="bookService")
public class BookServiceImpl extends ApplicationObjectSupport implements BookService {

	private final SortedMap<Integer, Book> books = Collections.synchronizedSortedMap(new TreeMap<Integer, Book>());

	private byte[] loadPngFile(String location) {
		try {
			return IOUtils.toByteArray(getApplicationContext().getResource(location).getInputStream());
		} catch (IOException e) {
			throw new BeanInitializationException("unable to load png file '" + location + "'", e);
		}
	}

	private URL toUrl(String website) {
		try {
			return new URL(website);
		} catch (MalformedURLException e) {
			throw new BeanInitializationException("unable to create URL '" + website + "'", e);
		}
	}

	@Override
	public void initApplicationContext() throws BeansException {

		Book book1 = new Book("Neal Stephenson", "Snow Crash", 50);
		book1.setCoverPng(loadPngFile("classpath:images/snowcrash.png"));
		book1.setWebsite(toUrl("http://en.wikipedia.org/wiki/Snow_Crash"));
		addBook(book1);

		Book book2 = new Book("William Gibson", "Neuromancer", 92);
		book2.setCoverPng(loadPngFile("classpath:images/neuromancer.png"));
		addBook(book2);

		Book book3 = new Book("Bruce Bethke", "Headcrash", 12);
		book3.setCoverPng(loadPngFile("classpath:images/headcrash.png"));
		addBook(book3);

		Book book4 = new Book("Eric S. Nylund", "Signal To Noise", 44);
		book4.setCoverPng(loadPngFile("classpath:images/signaltonoise.png"));
		addBook(book4);
	}

	public Book getBook(Integer key) {
		return cloneBook((Book) this.books.get(key));
	}

	public Book getBook(int key) {
		return getBook(Integer.valueOf(key));
	}

	public SortedSet<Book> getAllBooks() {
		TreeSet<Book> treeSet = new TreeSet<Book>();
		for (Iterator<Book> i = this.books.values().iterator(); i.hasNext();) {
			Book book = i.next();
			treeSet.add(cloneBook(book));
		}
		return treeSet;
	}

	public int addBook(Book book) {
		int key;
		Book newBook = cloneBook(book);
		synchronized (books) {
			if (books.isEmpty()) {
				key = 1;
			} else {
				key = books.lastKey().intValue() + 1;
			}
			Integer keyObj = Integer.valueOf(key);
			newBook.setKey(keyObj);
			this.books.put(keyObj, newBook);
		}
		return key;
	}

	public int addBook(String author, String title, Integer count) {
		Book book = new Book(author, title, count);
		return addBook(book);
	}

	public int addBook(String author, String title, int count) {
		Book book = new Book(author, title, count);
		return addBook(book);
	}

	public void saveBook(Book book) {
		this.books.put(book.getKey(), cloneBook(book));
	}

	public void deleteBook(Integer key) {
		this.books.remove(key);
	}

	public void deleteBook(Book book) {
		deleteBook(book.getKey());
	}

	public void deleteBook(int key) {
		deleteBook(Integer.valueOf(key));
	}

	private Book cloneBook(Book book) {
		try {
			if (book == null)
				return null;
			return (Book) book.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}
}
