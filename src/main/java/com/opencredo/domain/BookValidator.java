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

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class BookValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return Book.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		Book book = (Book) obj;
		validateAuthor(book, errors);
		validateTitle(book, errors);
		validateDescription(book, errors);
		validateAvailability(book, errors);
		validateCount(book, errors);
		validateWebsite(book, errors);
		validateCoverPng(book, errors);
	}

	public void validateAuthor(Book book, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "author", "book.author.required", "Author is required.");
	}

	public void validateTitle(Book book, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "title", "book.title.required", "Title is required.");
	}

	public void validateDescription(Book book, Errors errors) {
	}

	public void validateAvailability(Book book, Errors errors) {
	}

	public void validateCount(Book book, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "count", "book.count.required", "Current count is required.");
	}

	public void validateWebsite(Book book, Errors errors) {
	}

	public void validateCoverPng(Book book, Errors errors) {
	}
}
