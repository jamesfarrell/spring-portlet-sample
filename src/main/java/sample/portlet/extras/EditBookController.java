package sample.portlet.extras;

import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.opencredo.domain.Book;
import com.opencredo.service.BookService;


@Controller
@RequestMapping("VIEW")
@SessionAttributes("book")
public class EditBookController {
	@Autowired
	@Qualifier("bookService")
	private BookService bookService;
	@Autowired
	@Qualifier("editBookValidator")
	private Validator editBookValidator;

	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

	@RenderMapping(params="myaction=editBookForm")
	public String showEditBookForm() {
		return "editBookForm";
	}

	@ActionMapping(params="action=editBook")
	public void editBook(@ModelAttribute("book") Book book, BindingResult bindingResult, ActionResponse response, SessionStatus sessionStatus)  {
		editBookValidator.validate(book, bindingResult);
		if (!bindingResult.hasErrors()) {
//			bookService.editBook(book); TODO 
			response.setRenderParameter("action", "books");
			sessionStatus.setComplete();
		} else {
			//--this is required. the getBook method is not invoked but the @RequestParam
			//--is still evaluated
			response.setRenderParameter("count", book.getCount().toString());
			response.setRenderParameter("action", "editBookForm");
		}
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields(new String[] {"count"});
	}
	
	@ModelAttribute("book")
	public Book getBook(@RequestParam Integer id) {
		return bookService.getBook(id);
	}
}
