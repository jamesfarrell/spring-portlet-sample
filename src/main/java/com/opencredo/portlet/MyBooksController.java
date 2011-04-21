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

package com.opencredo.portlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.opencredo.domain.Book;
import com.opencredo.service.BookService;


@Controller
public class MyBooksController {

	private BookService bookService;

	private static final Log logger = LogFactory.getLog(MyBooksController.class);

	@Autowired
	public MyBooksController(BookService bookService) {
		this.bookService = bookService;
	}

	@RequestMapping("VIEW")
	public String myBooksView(PortletPreferences prefs, Model model) {
		SortedSet<Book> myBooks = loadMyBooks(prefs);
		model.addAttribute("books", myBooks);
		return "myBooks";
	}

	@RequestMapping("EDIT")
	public String myBooksEditRender(PortletPreferences prefs, Model model) {
		SortedSet<Book> myBooks = loadMyBooks(prefs);
		SortedSet<Book> allBooks = bookService.getAllBooks();
		allBooks.removeAll(myBooks);
		model.addAttribute("myBooks", myBooks);
		model.addAttribute("allBooks", allBooks);
		return "myBooksEdit";
	}

	@RequestMapping("EDIT")
	public void myEditBooksAction(ActionRequest request,
			PortletPreferences prefs, @RequestParam("what") String what,
			@RequestParam("book") Integer id) {
		Book book = bookService.getBook(id);
		if (book == null)
			return;
		SortedSet<Book> myBooks = loadMyBooks(prefs);
		if (what.equals("add")) {
			myBooks.add(book);
		} else if (what.equals("remove")) {
			myBooks.remove(book);
		}
		storeMyBooks(prefs, myBooks);
	}

	private SortedSet<Book> loadMyBooks(PortletPreferences prefs) {
		SortedSet<Book> myBooks = new TreeSet<Book>();
		String[] keys = prefs.getValues("myBooks", null);
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				try {
					Integer key = Integer.valueOf(keys[i]);
					Book book = bookService.getBook(key);
					if (book != null)
						myBooks.add(book);
				} catch (NumberFormatException ex) {
				}
			}
		}
		return myBooks;
	}

	private void storeMyBooks(PortletPreferences prefs, SortedSet<Book> myBooks) {
		ArrayList<String> keys = new ArrayList<String>();
		for (Iterator<Book> i = myBooks.iterator(); i.hasNext();) {
			Book book = i.next();
			keys.add(book.getKey().toString());
		}
		String[] keysArr = keys.toArray(new String[] {});
		try {
			prefs.setValues("myBooks", keysArr);
			prefs.store();
		} catch (Exception e) {
			logger.warn("unable to set portlet preference", e);
		}
	}
}
