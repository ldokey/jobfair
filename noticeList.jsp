<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:forEach items="${noticelist}" var="list">
	<tr>
		<td>${list.notice_no}</td>
		<td>${list.notice_title}</td>
		<td>${list.writer}</td>
		<td>${list.reg_date}</td>
		<td>${list.notice_count}</td>
	</tr>
</c:forEach>




