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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.util.PortletUtils;

import com.opencredo.domain.Book;
import com.opencredo.domain.BookValidator;
import com.opencredo.service.BookService;


@Controller("bookController")
@RequestMapping("VIEW")
@SessionAttributes("book")
public class BooksController {

	private BookValidator bookValidator = new BookValidator();

	private BookService bookService;

	@Autowired
	public BooksController(@Qualifier("bookService") BookService bookService) {
		this.bookService = bookService;
	}

	// setup binder for adding/editing books

	@InitBinder
	protected void initBinder(PortletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		binder.setAllowedFields(new String[] { "author", "title", "description", "availability", "count", "website", "coverPng" });
	}

	// default: show list of books

	@RequestMapping
	public String listBooks(Model model) {
		model.addAttribute("books", bookService.getAllBooks());
		return "books";
	}

	// view details of a book

	@RequestMapping(params = "action=viewBook")
	public String viewBook(@RequestParam("book") Integer id, Model model) {
		model.addAttribute("book", bookService.getBook(id));
		return "bookView";
	}

	// increment/decrement book count

	@RequestMapping(params = "action=incrementBook")
	public void incrementBook(ActionResponse response, @RequestParam("book") Integer id, @RequestParam("increment") Integer increment) {
		Book book = bookService.getBook(id);
		book.incrementCount(increment);
		bookService.saveBook(book);
	}

	// delete a book

	@RequestMapping(params = "action=deleteBook")
	public void deleteBook(ActionResponse response, @RequestParam("book") Integer id) {
		bookService.deleteBook(id);
	}

	// redirect to book website

	@RequestMapping(params = "action=redirectBook")
	public void redirectToBookWebsite(ActionResponse response, @RequestParam("book") Integer id) throws IOException {
		Book book = bookService.getBook(id);
		if (book != null && book.getWebsite() != null) {
			response.sendRedirect(book.getWebsite().toString());
		}
	}

	// add a book

	@RequestMapping(params = "action=addBook")
	// render phase
	public String showAddBookForm(Model model) {
		if (!model.containsAttribute("book")) {
			model.addAttribute("book", new Book());
			model.addAttribute("page", 0);
		}
		return "bookAdd";
	}

	@RequestMapping(params = "action=addBook")
	// action phase
	public void submitAddBookFormPage(ActionRequest request, ActionResponse response, @ModelAttribute("book") Book book,
			BindingResult result, @RequestParam("_page") int currentPage, Model model) {

		if (request.getParameter("_cancel") != null) {
		} else if (request.getParameter("_finish") != null) {
			bookValidator.validate(book, result);
			if (result.hasErrors()) {
				model.addAttribute("page", currentPage);
				response.setRenderParameter("action", "addBook");
			} else {
				bookService.addBook(book);
			}
		} else {
			switch (currentPage) {
			case 0:
				bookValidator.validateAuthor(book, result);
				break;
			case 1:
				bookValidator.validateTitle(book, result);
				break;
			case 2:
				bookValidator.validateDescription(book, result);
				break;
			case 3:
				bookValidator.validateAvailability(book, result);
				break;
			case 4:
				bookValidator.validateCount(book, result);
				break;
			case 5:
				bookValidator.validateWebsite(book, result);
				break;
			case 6:
				bookValidator.validateCoverPng(book, result);
				break;
			default:
				;
			}
			int targetPage = result.hasErrors() ? currentPage : PortletUtils.getTargetPage(request, "_target", currentPage);
			model.addAttribute("page", targetPage);
			response.setRenderParameter("action", "addBook");
		}
	}

	// edit a book

	@RequestMapping(params = "action=editBook")
	// render phase
	public String showEditBookForm(@RequestParam("book") Integer id, Model model) {
		if (!model.containsAttribute("book")) {
			model.addAttribute("book", bookService.getBook(id));
		}
		return "bookEdit";
	}

	@RequestMapping(params = "action=editBook")
	// action phase
	public void submitEditBookForm(ActionRequest request, ActionResponse response, @ModelAttribute("book") Book book, BindingResult result) {
		bookValidator.validate(book, result);
		if (result.hasErrors()) {
			response.setRenderParameter("action", "editBook");
			response.setRenderParameter("book", book.getKey().toString());
		} else {
			bookService.saveBook(book);
			response.setRenderParameter("action", "viewBook");
			response.setRenderParameter("book", book.getKey().toString());
		}
	}
}
