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

<div class="portlet-section-body">

	<p class="portlet-section-subheader"><spring:message code="books.selected.title"/></p>

	<table border="0" cellpadding="4">
		<tr>
			<th><spring:message code="book.label.author"/></th>
			<th><spring:message code="book.label.title"/></th>
			<th></th>
		</tr>
		<c:forEach items="${myBooks}" var="book">
			<tr>
				<td>${book.author}</td>
				<td>${book.title}</td>
				<td><a href="<portlet:actionURL><portlet:param name="what" value="remove"/><portlet:param name="book" value="${book.key}"/></portlet:actionURL>"><spring:message code="button.remove"/></a></td>
			</tr>
		</c:forEach>
	</table>

	<p class="portlet-section-subheader"><spring:message code="books.available.title"/></p>

	<table border="0" cellpadding="4">
		<tr>
			<th><spring:message code="book.label.author"/></th>
			<th><spring:message code="book.label.title"/></th>
			<th></th>
		</tr>
		<c:forEach items="${allBooks}" var="book">
			<tr>
				<td>${book.author}</td>
				<td>${book.title}</td>
				<td><a href="<portlet:actionURL><portlet:param name="what" value="add"/><portlet:param name="book" value="${book.key}"/></portlet:actionURL>"><spring:message code="button.add"/></a></td>
			</tr>
		</c:forEach>
	</table>

</div>

<div class="portlet-section-footer">
	<a href="<portlet:renderURL portletMode="view"/>"><spring:message code="button.home"/></a>
</div>
