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

<c:set var="page" value="${empty page ? 0 : page}"/>
<c:set var="prevPage" value="${page == 0 ? null : page - 1}"/>
<c:set var="nextPage" value="${page == 6 ? null : page + 1}"/>

<div class="portlet-section-header"><spring:message code="book.add.title"/></div>

<div class="portlet-section-body">

	<portlet:actionURL var="actionUrl">
		<portlet:param name="action" value="addBook"/>
		<portlet:param name="_page" value="${page}"/>
	</portlet:actionURL>

	<form:form commandName="book" method="post" action="${actionUrl}" enctype="multipart/form-data">

		<div><form:errors cssClass="portlet-msg-error" path="*" /></div>

		<table border="0" cellpadding="4">
			<tr>
				<c:choose>
					<c:when test="${page == 0}" >
						<th class="portlet-form-field-label"><spring:message code="book.label.author"/></th>
						<td><form:input cssClass="portlet-form-input-field" path="author" size="30" maxlength="80" /></td>
						<td><form:errors cssClass="portlet-msg-error" path="author" /></td>
					</c:when>
					<c:when test="${page == 1}" >
						<th class="portlet-form-field-label"><spring:message code="book.label.title"/></th>
						<td><form:input cssClass="portlet-form-input-field" path="title" size="30" maxlength="80" /></td>
						<td><form:errors cssClass="portlet-msg-error" path="title" /></td>
					</c:when>
					<c:when test="${page == 2}" >
						<th class="portlet-form-field-label"><spring:message code="book.label.description"/></th>
						<td><form:textarea cssClass="portlet-form-input-field" path="description" rows="10" cols="30" /></td>
						<td><form:errors cssClass="portlet-msg-error" path="description" /></td>
					</c:when>
					<c:when test="${page == 3}" >
						<th class="portlet-form-field-label"><spring:message code="book.label.availability"/></th>
						<td><form:input cssClass="portlet-form-input-field" path="availability" size="30" maxlength="80" /></td>
						<td><form:errors cssClass="portlet-msg-error" path="availability" /></td>
					</c:when>
					<c:when test="${page == 4}" >
						<th class="portlet-form-field-label"><spring:message code="book.label.count"/></th>
						<td><form:input cssClass="portlet-form-input-field" path="count" size="30" maxlength="30" /></td>
						<td><form:errors cssClass="portlet-msg-error" path="count" /></td>
					</c:when>
					<c:when test="${page == 5}" >
						<th class="portlet-form-field-label"><spring:message code="book.label.website"/></th>
						<td><form:input cssClass="portlet-form-input-field" path="website" size="30" maxlength="800" /></td>
						<td><form:errors cssClass="portlet-msg-error" path="website" /></td>
					</c:when>
					<c:when test="${page == 6}" >
						<spring:bind path="book.coverPng">
							<th class="portlet-form-field-label"><spring:message code="book.label.coverPng"/></th>
							<td><spring:message code="book.coverPng.sizeLimit"/></br>
								<input class="portlet-form-input-field" type="file" name="${status.expression}" size="30" /></td>
							<td><span class="portlet-msg-error">${status.errorMessage}</span></td>
						</spring:bind>
					</c:when>
				</c:choose>
			<tr>
				<th colspan="3">
					<input type="submit" name="_target${nextPage}" ${empty nextPage ? "disabled" : ""} value="<spring:message code="button.next"/>"/>
					<input type="submit" name="_finish" value="<spring:message code="button.finish"/>"/>
					<input type="submit" name="_target${prevPage}" ${empty prevPage ? "disabled" : ""} value="<spring:message code="button.previous"/>"/>
					<input type="submit" name="_cancel" value="<spring:message code="button.cancel"/>"/>
				</th>
			</tr>
		</table>

	</form:form>

</div>

<div class="portlet-section-footer">
	<a href="<portlet:renderURL portletMode="view"/>"><spring:message code="button.home"/></a>
</div>
