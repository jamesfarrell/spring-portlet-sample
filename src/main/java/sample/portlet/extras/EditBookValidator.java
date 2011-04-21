package sample.portlet.extras;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.opencredo.domain.Book;


@Component("editBookValidator")
public class EditBookValidator implements Validator {

	public boolean supports(Class<?> klass) {
		return Book.class.isAssignableFrom(klass);
	}

	public void validate(Object target, Errors errors) {
		Book book = (Book)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty.book.title");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "author", "NotEmpty.book.author");
		String title = book.getTitle();
		if (title.length() > 100 || title.length() < 10) {
			errors.rejectValue("title", "fieldLength");
		}
	}
}
