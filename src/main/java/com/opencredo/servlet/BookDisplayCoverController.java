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

package com.opencredo.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.opencredo.domain.Book;
import com.opencredo.service.BookService;


public class BookDisplayCoverController extends AbstractController {

	private BookService bookService;

	private static final String CONTENT_TYPE = "image/png";

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws IllegalStateException, IOException {

		// set the content type
		response.setContentType(CONTENT_TYPE);

		// get the ID of the book -- set "bad request" if its not a valid integer
		Integer id;
		try {
			id = ServletRequestUtils.getIntParameter(request, "book");
			if (id == null)
				throw new IllegalStateException("must specify the book id");
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid book");
			return null;
		}

		// get the book from the service
		Book book = bookService.getBook(id);

		// if the book doesn't exist then set "not found"
		if (book == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "no such book");
			return null;
		}

		// if the book doesn't have a picture, set "not found"
		if (book.getCoverPng() == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "book has no image");
			return null;
		}

		if (logger.isDebugEnabled())
			logger.debug("returning cover for book " + book.getKey() + " '"
					+ book.getTitle() + "'" + " size: "
					+ book.getCoverPng().length + " bytes");

		// send the image
		response.setContentLength(book.getCoverPng().length);
		response.getOutputStream().write(book.getCoverPng());
		response.getOutputStream().flush();

		// we already handled the response
		return null;
	}

	@Required
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
}
