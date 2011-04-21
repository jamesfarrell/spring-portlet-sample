package com.opencredo.portlet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.SortedSet;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.portlet.MockActionRequest;
import org.springframework.mock.web.portlet.MockActionResponse;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.portlet.DispatcherPortlet;
import org.springframework.web.portlet.util.PortletUtils;
import org.springframework.web.servlet.ViewRendererServlet;

import com.opencredo.domain.Book;
import com.opencredo.service.BookService;
import com.opencredo.utils.MockViewResolver;
import com.opencredo.utils.MockWebApplication;
import com.opencredo.utils.MockWebApplicationContextLoader;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-test.xml"},
		loader=MockWebApplicationContextLoader.class)
@MockWebApplication(name="booksPortlet")
public class BooksControllerIntegrationTest {

    @Autowired
    private DispatcherPortlet dispatcherPortlet;

    @Autowired
    private MockViewResolver viewResolver;

    @Autowired
    @Qualifier("bookService")
    private BookService bookService;

    private static final String AUTHOR = "James Farrell";
    private static final String TITLE = "Integration Testing for Spring Portlet MVC";
    private static final Integer COUNT = 2;
    private static final Integer KEY = 18;

    @Before
    public void setupPortlet() throws PortletException {
    }

	private void setupRequestSession(PortletRequest request) {
    	Book book = new Book(AUTHOR, TITLE, COUNT);
    	book.setKey(KEY);
    	PortletUtils.setSessionAttribute(request, "book", book);
	}

	@Test
    public void viewListOfBooks() throws ServletException, IOException, PortletException {
    	MockRenderRequest request = new MockRenderRequest();
    	setupRequestSession(request);

   		dispatcherPortlet.render(request, new MockRenderResponse());

   		assertEquals("Incorrect view name found", "books", viewResolver.getViewName());

		assertNotNull("No UI model found", request.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE));
		ModelMap model = (ModelMap) request.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE);

   		assertNotNull("Null list of books found in model", model.get("books"));
    	assertTrue("List of books found in model was not a Sorted Set", model.get("books") instanceof SortedSet);
		SortedSet<Book> modelBooks = (SortedSet<Book>) model.get("books");
    	assertEquals("Incorrect number of books found in model", bookService.getAllBooks().size(), modelBooks.size());
    }

    @Test
    public void viewBook() throws ServletException, IOException, PortletException {
    	final Integer TEST_ID = 2;

    	MockRenderRequest request = new MockRenderRequest();
    	setupRequestSession(request);

    	request.addParameter("book", TEST_ID.toString());
    	request.addParameter("action", "viewBook");

   		dispatcherPortlet.render(request, new MockRenderResponse());

		assertEquals("Incorrect view name found", "bookView", viewResolver.getViewName());

		assertNotNull("No UI model found", request.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE));
		ModelMap model = (ModelMap) request.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE);

		Book expectedBook = bookService.getBook(TEST_ID);
		checkBook(model.get("book"), expectedBook);
    }

    @Test
    public void showAddBookForm() throws ServletException, IOException, PortletException {
    	MockRenderRequest request = new MockRenderRequest();
    	setupRequestSession(request);

    	request.addParameter("action", "addBook");

   		dispatcherPortlet.render(request, new MockRenderResponse());

		assertEquals("Incorrect view name found", "bookAdd", viewResolver.getViewName());

		assertNotNull("No UI model found", request.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE));
		ModelMap model = (ModelMap) request.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE);

		checkBook(model.get("book"), AUTHOR, TITLE, COUNT);
    }

    @Test
    public void showEditBookForm() throws ServletException, IOException, PortletException {
    	MockRenderRequest request = new MockRenderRequest();
    	setupRequestSession(request);

    	request.addParameter("book", "12");
    	request.addParameter("action", "editBook");

   		dispatcherPortlet.render(request, new MockRenderResponse());

		assertEquals("Incorrect view name found", "bookEdit", viewResolver.getViewName());

		assertNotNull("No UI model found", request.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE));
		ModelMap model = (ModelMap) request.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE);

		checkBook(model.get("book"), AUTHOR, TITLE, COUNT);
    }

    @Test
    public void testAddBookAction() throws PortletException, IOException {
    	MockActionRequest request = new MockActionRequest();
    	setupRequestSession(request);

    	request.addParameter("action", "addBook");
    	request.addParameter("_finish", "true");
    	request.addParameter("_page", "1");

    	int numberOfBooksBeforeAdding = bookService.getAllBooks().size();

    	dispatcherPortlet.processAction(request, new MockActionResponse());

    	assertEquals("Book not added", numberOfBooksBeforeAdding + 1, bookService.getAllBooks().size());
    }

    @Test
    public void testIncrementBookAction() throws PortletException, IOException {
    	final Integer TEST_ID = 2;
    	final Integer INCREMENT = 5;

    	MockActionRequest request = new MockActionRequest();
    	setupRequestSession(request);

    	request.addParameter("action", "incrementBook");
    	request.addParameter("book", TEST_ID.toString());
    	request.addParameter("increment", INCREMENT.toString());

    	Integer expectedCount = bookService.getBook(TEST_ID).getCount() + INCREMENT;

    	dispatcherPortlet.processAction(request, new MockActionResponse());

    	assertEquals("Book not incremented correctly", expectedCount, bookService.getBook(TEST_ID).getCount());
    }

    @Test
    public void testDeleteBook() throws PortletException, IOException {
    	final Integer TEST_ID = 2;

    	MockActionRequest request = new MockActionRequest();
    	setupRequestSession(request);

    	request.addParameter("action", "deleteBook");
    	request.addParameter("book", TEST_ID.toString());

    	dispatcherPortlet.processAction(request, new MockActionResponse());

    	assertNull("Book not deleted", bookService.getBook(TEST_ID));
    }

    @Test
    public void testRedirect() throws PortletException, IOException {
    	final Integer TEST_ID = 1;

    	MockActionRequest request = new MockActionRequest();
    	setupRequestSession(request);

    	MockActionResponse response = new MockActionResponse();

    	request.addParameter("action", "redirectBook");
    	request.addParameter("book", TEST_ID.toString());

    	String expectedUrl = bookService.getBook(TEST_ID).getWebsite().toString();

    	dispatcherPortlet.processAction(request, response);

    	assertEquals("Redirection did not work correctly", expectedUrl, response.getRedirectedUrl());
    }

    @Test
    public void testSubmitEditBookForm() throws PortletException, IOException {
    	MockActionRequest request = new MockActionRequest();
    	setupRequestSession(request);

    	MockActionResponse response = new MockActionResponse();

    	request.addParameter("action", "editBook");

    	dispatcherPortlet.processAction(request, response);

		assertNotNull("No 'action' render parameter found", response.getRenderParameter("action"));
		assertEquals("Wrong 'action' render parameter found", "viewBook", response.getRenderParameter("action"));

		assertNotNull("No 'book' render parameter found", response.getRenderParameter("book"));
		assertEquals("Wrong 'book' render parameter found", KEY.toString(), response.getRenderParameter("book"));

		Book editedBook = bookService.getBook(KEY);

		checkBook(editedBook, AUTHOR, TITLE, COUNT);
    }

    @Test
    public void testFlow() throws PortletException, IOException {
    	MockActionRequest actionRequest = new MockActionRequest();
    	setupRequestSession(actionRequest);
    	MockActionResponse actionResponse = new MockActionResponse();

    	actionRequest.addParameter("action", "editBook");

    	dispatcherPortlet.processAction(actionRequest, actionResponse);

    	MockRenderRequest renderRequest = new MockRenderRequest();
    	setRenderParameters(actionResponse, renderRequest);

    	dispatcherPortlet.render(renderRequest, new MockRenderResponse());

		assertEquals("Incorrect view name found", "bookView", viewResolver.getViewName());

		assertNotNull("No UI model found", renderRequest.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE));
		ModelMap model = (ModelMap) renderRequest.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE);

		Book expectedBook = bookService.getBook(KEY);
		checkBook(model.get("book"), expectedBook);
    }

    // ***** Get render parameters *****

    private void setRenderParameters(ActionResponse actionResponse, MockRenderRequest renderRequest) {
    	renderRequest.setParameters(actionResponse.getRenderParameterMap());
    }

    // ***** Helper methods for checking the test output *****

    private void checkBook(Object modelValue, String expectedAuthor, String expectedTitle, Integer expectedCount) {
    	assertNotNull("No book stored in model", modelValue);
    	assertTrue("Value in model was not a Book", modelValue instanceof Book);
    	Book book = (Book)modelValue;
    	assertEquals("Incorrect book author", expectedAuthor, book.getAuthor());
    	assertEquals("Incorrect book title", expectedTitle, book.getTitle());
    	assertEquals("Incorrect book count", expectedCount, book.getCount());
    }

    private void checkBook(Object modelValue, Book expectedBook) {
    	assertNotNull("No book stored in model", modelValue);
    	assertTrue("Value in model was not a Book", modelValue instanceof Book);
    	Book book = (Book)modelValue;
    	assertEquals("Incorrect book author", expectedBook.getAuthor(), book.getAuthor());
    	assertEquals("Incorrect book title", expectedBook.getTitle(), book.getTitle());
    	assertEquals("Incorrect book count", expectedBook.getCount(), book.getCount());
    	assertEquals("Incorrect book description", expectedBook.getDescription(), book.getDescription());
    	assertEquals("Incorrect book key", expectedBook.getKey(), book.getKey());
    	assertEquals("Incorrect book website", expectedBook.getWebsite(), book.getWebsite());
    }
}
