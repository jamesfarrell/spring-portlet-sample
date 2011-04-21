<%--
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
--%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>

<div class="portlet-section-header"><spring:message code="books.title"/></div>

<div class="portlet-section-body">

	<table border="0" cellpadding="4">

		<tr>
			<th><spring:message code="book.label.author"/></th>
			<th><spring:message code="book.label.title"/></th>
			<th><spring:message code="book.label.count"/></th>
			<th></th>
		</tr>

		<c:forEach items="${books}" var="book">
			<tr>
				<td>${book.author}</td>
				<td><a href="<portlet:renderURL><portlet:param name="action" value="viewBook"/><portlet:param name="book" value="${book.key}"/></portlet:renderURL>">${book.title}</a></td>
				<td align="right">${book.count}</td>
				<td>
					<a href="<portlet:actionURL><portlet:param name="action" value="incrementBook"/><portlet:param name="book" value="${book.key}"/><portlet:param name="increment" value="1"/></portlet:actionURL>"><img title="<spring:message code="book.increment.title"/>" src="<html:imagesPath/>increase.png" border=0 /></a>
					<a href="<portlet:actionURL><portlet:param name="action" value="incrementBook"/><portlet:param name="book" value="${book.key}"/><portlet:param name="increment" value="-1"/></portlet:actionURL>"><img title="<spring:message code="book.decrement.title"/>" src="<html:imagesPath/>decrease.png" border=0 /></a>
					<a href="<portlet:renderURL><portlet:param name="action" value="editBook"/><portlet:param name="book" value="${book.key}"/></portlet:renderURL>"><img title="<spring:message code="book.edit.title"/>" src="<html:imagesPath/>edit.png" border=0 /></a>
					<a href="<portlet:actionURL><portlet:param name="action" value="deleteBook"/><portlet:param name="book" value="${book.key}"/></portlet:actionURL>"><img title="<spring:message code="book.delete.title"/>" src="<html:imagesPath/>delete.png" border=0 /></a>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="4">
				<a href="<portlet:renderURL><portlet:param name="action" value="addBook"/></portlet:renderURL>"><img title="<spring:message code="book.add.title"/>" src="<html:imagesPath/>new.png" border=0 /> <spring:message code="book.add.title"/></a>
			</td>
		</tr>
	</table>

</div>

<div class="portlet-section-footer">
</div>
