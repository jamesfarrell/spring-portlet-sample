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

<div class="portlet-section-header"><spring:message code="book.edit.title"/></div>

<div class="portlet-section-body">

	<portlet:actionURL var="actionUrl">
		<portlet:param name="action" value="editBook"/>
		<portlet:param name="book" value="${book.key}"/>
	</portlet:actionURL>

	<form:form commandName="book" method="post" action="${actionUrl}">

		<div><form:errors cssClass="portlet-msg-error" /></div>

		<table border="0" cellpadding="4">
			<tr>
				<th class="portlet-form-field-label"><spring:message code="book.label.author"/></th>
				<td><form:input cssClass="portlet-form-input-field" path="author" size="30" maxlength="80" /></td>
				<td><form:errors cssClass="portlet-msg-error" path="author" /></td>
			</tr>
			<tr>
				<th class="portlet-form-field-label"><spring:message code="book.label.title"/></th>
				<td><form:input cssClass="portlet-form-input-field" path="title" size="30" maxlength="80" /></td>
				<td><form:errors cssClass="portlet-msg-error" path="title" /></td>
			</tr>
			<tr>
				<th class="portlet-form-field-label"><spring:message code="book.label.description"/></th>
				<td><form:textarea cssClass="portlet-form-input-field" path="description" rows="10" cols="30" /></td>
				<td><form:errors cssClass="portlet-msg-error" path="description" /></td>
			</tr>
			<tr>
				<th class="portlet-form-field-label"><spring:message code="book.label.availability"/></th>
				<td><form:input cssClass="portlet-form-input-field" path="availability" size="30" maxlength="80" /></td>
				<td><form:errors cssClass="portlet-msg-error" path="availability" /></td>
			</tr>
			<tr>
				<th class="portlet-form-field-label"><spring:message code="book.label.count"/></th>
				<td><form:input cssClass="portlet-form-input-field" path="count" size="30" maxlength="30" /></td>
				<td><form:errors cssClass="portlet-msg-error" path="count" /></td>
			</tr>
			<tr>
				<th class="portlet-form-field-label"><spring:message code="book.label.website"/></th>
				<td><form:input cssClass="portlet-form-input-field" path="website" size="30" maxlength="800" /></td>
				<td><form:errors cssClass="portlet-msg-error" path="website" /></td>
			</tr>
			<tr>
				<th colspan="3"><button type="submit" class="portlet-form-button"><spring:message code="button.save"/></button></th>
			</tr>
		</table>

	</form:form>

	<div class="portlet-section-subheader"><spring:message code="book.coverPng.title"/></div>

	<c:if test="${book.coverPng != null}">
		<p><img src="<html:rootPath/>resources/bookCover?book=${book.key}" border="0" /></p>
	</c:if>

	<p><spring:message code="book.coverPng.sizeLimit"/></p>

	<form method="post" action="${actionUrl}" enctype="multipart/form-data">

		<spring:bind path="book.coverPng">
			<input class="portlet-form-input-field" type="file" name="${status.expression}" size="30" /></br>
			<span class="portlet-msg-error">${status.errorMessage}</span>
		</spring:bind>
		<button type="submit" class="portlet-form-button"><spring:message code="button.uploadFile"/></button>

	</form>

</div>

<div class="portlet-section-footer">
	<a href="<portlet:renderURL portletMode="view"/>"><spring:message code="button.home"/></a>
</div>
