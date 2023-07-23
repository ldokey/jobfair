<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:forEach items="${chatRoomList}" var="list">
	<c:if test="${list.is_delete eq 2}">
		<tr>
			<td>${list.chatRoomNo}</td>
			<td><a href="javascript:openDetail(${list.chatRoomNo})">${list.chatTitle}</a></td>
			<td>${list.reg_date}</td>
		</tr>
	</c:if>
</c:forEach>

<input type="hidden" id="totcnt" name="totcnt" value="${countChatRoomList}"/>

