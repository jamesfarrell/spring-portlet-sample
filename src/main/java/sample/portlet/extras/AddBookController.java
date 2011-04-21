package sample.portlet.extras;

import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.opencredo.domain.Book;
import com.opencredo.service.BookService;


@Controller(value="addBookController")
@RequestMapping(value = "VIEW")
@SessionAttributes(types = Book.class)
public class AddBookController {
	@Autowired
	@Qualifier("bookService")
	private BookService bookService;
	
	@Autowired
	@Qualifier("addBookValidator")
	private Validator addBookValidator;
	
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

	@ModelAttribute("book")
	public Book getCommandObject() {
		return new Book();
	}

	@RenderMapping(params = "action=addBookForm")
	public String showAddBookForm(RenderResponse response) {
		return "addBookForm";
	}

	@ActionMapping(params = "action=addBook")
	public void addBook(@ModelAttribute Book book,
			BindingResult bindingResult, ActionResponse response, SessionStatus sessionStatus) {
		addBookValidator.validate(book, bindingResult);
		if (!bindingResult.hasErrors()) {
			bookService.addBook(book);
			response.setRenderParameter("action", "books");
			//--set the session status as complete to cleanup the model attributes
			//--stored using @SessionAttributes, otherwise when you click
			//--'Add Book' button you'll see the book information pre-populated
			//-- because the getCommandObject method of the controller is not
			//--invoked
			sessionStatus.setComplete();
		} else {
			response.setRenderParameter("action", "addBookForm");
		}
	}
}
