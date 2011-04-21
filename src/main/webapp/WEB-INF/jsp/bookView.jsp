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

<div class="portlet-section-header"><spring:message code="book.view.title"/></div>

<div class="portlet-section-body">

	<table border="0" cellpadding="4">
		<tr>
			<th><spring:message code="book.label.author"/></th>
			<td>${book.author}</td>
			<c:if test="${book.coverPng != null}">
				<td rowspan="5">
					<img src="<html:rootPath/>resources/bookCover?book=${book.key}" border="0" />
				</td>
			</c:if>
		</tr>
		<tr>
			<th><spring:message code="book.label.title"/></th>
			<td>${book.title}</td>
		</tr>
		<tr>
			<th><spring:message code="book.label.description"/></th>
			<td>${book.description}</td>
		</tr>
		<tr>
			<th><spring:message code="book.label.availability"/></th>
			<td><fmt:formatDate value="${book.availability}" dateStyle="medium" /></td>
		</tr>
		<tr>
			<th><spring:message code="book.label.count"/></th>
			<td>${book.count}</td>
		</tr>
	</table>

	<c:if test="${book.website != null}">
		<p><a href="<portlet:actionURL><portlet:param name="action" value="redirectBook"/><portlet:param name="book" value="${book.key}"/></portlet:actionURL>"><spring:message code="book.label.website"/></a></p>
	</c:if>

</div>

<div class="portlet-section-footer">
	<a href="<portlet:renderURL portletMode="view"/>"><spring:message code="button.home"/></a> -
	<a href="<portlet:renderURL><portlet:param name="action" value="editBook"/><portlet:param name="book" value="${book.key}"/></portlet:renderURL>"><spring:message code="button.edit"/></a>
</div>
