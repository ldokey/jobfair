<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- CSS only -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
	crossorigin="anonymous">
<style>
@import
	url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300&display=swap')
	;

body {
	font-family: 'Noto Sans KR', sans-serif;
}

.cursor {
	cursor: pointer;
}

.chatchat {
	display: flex;
	padding: 5px;
	margin-bottom: 5px;
	align-items: center;
}

.chatchat p {
	margin: 0;
	padding: 0;
	font-size: 18px;
}

#profileImg {
	border-radius: 12px;
	margin-right: 5px;
}
</style>
<title>Insert title here</title>
</head>
<body>
	<title>chatRoom</title>

	<div class="container mt-5" style="width: 700px;">
		<button class="btn btn-primary btn-sm mb-2" onclick="goBack();">뒤로가기</button>


		<div class="card">
			<div class="card-header">ChatRoom ${userChatDto.name}님 어서오세요
			</div>
			<div class="card-body">
				<div>
					<div id="target" class="chatWindow"
						style="height: 400px; overflow-y: scroll;"></div>
					<div class="d-flex justify-content-between" style="height: 45px;">
						<input id="text" type="text" style="width: 77%" />
						<button style="width: 20%" onclick="send();"
							class="btn btn-success btn-sm">전송</button>
						<input type="hidden" id="name" value="${userChatDto.name}">
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-3.7.0.js" integrity="sha256-JlqSTELeR4TLqP0OG9dxM7yDPqX1ox/HfgiSLBj8+kM=" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js" integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js" integrity="sha512-1QvjE7BtotQjkq8PxLeF6P46gEpBRXuskzIVgjFpekzFVF4yjRgrQvTG1MTOJ3yQgvTteKAcO7DSZI92+u/yZw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	


	<!-- 스크립트 작성  -->
	<script>
		// 채팅 일시 저장 
		var chatHtml = "";
		var arr =[];
		// 현재 시간 객체 생성
		var currentTime = new Date();

		// 원하는 형식으로 날짜 정보 가져오기
		var year = currentTime.getFullYear();
		var month = ("0" + (currentTime.getMonth() + 1)).slice(-2);
		var day = ("0" + currentTime.getDate()).slice(-2);
		var hours = ("0" + currentTime.getHours()).slice(-2);
		var minutes = ("0" + currentTime.getMinutes()).slice(-2);
		var seconds = ("0" + currentTime.getSeconds()).slice(-2);

		// 원하는 형식으로 날짜 문자열 생성
		var formattedDate = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;

		console.log(formattedDate);

	
		$(function(){
			connect();	
		});
		
		
		function connect(){
			var socket = new SockJS("/chat/room");
			stompClient = Stomp.over(socket);
			stompClient.connect({}, function(frame){
				console.log("Connected" + frame);
				stompClient.subscribe("/topic/a", function(res){
				var data = JSON.parse(res.body);
			
			// 채팅 출력 위치 - 상대방 좌측 정렬  
			if(data.name != '${userChatDto.name}'){
				chatHtml = '<div class="chatchat">';
				chatHtml += '<div style="display:flex; flex-direction:column;">';
				//그림 
				chatHtml +='<p>'+ data.name +' : </p>';
				chatHtml += '</div>';
				chatHtml +='<p style="background:#f1f1f1; padding:5px 10px;">' + data.msg +'</p>';
				chatHtml +='</div>';
				$('#target').append(chatHtml);
			}else{
			// 채팅 출력 위치 - 본인 우측 정렬 
				chatHtml = '<div class="chatchat" style="justify-content:flex-end; margin-right:5px;">';
				chatHtml += '<div style="display:flex; flex-direction:column;">';
				chatHtml += '</div>';
				chatHtml +='<p style="background:#f1f1f1; padding:5px 10px;">' + data.msg +'</p>';
				chatHtml +='</div>';
				$('#target').append(chatHtml);
			}
				
				// 채팅을 배열에 저장 
				arr.push(data.msg);

				// 전송 버튼 누른 후, 채팅 입력칸 벨류 초기화
				$('#text').val("");
				});
			});
		}
		
		// 엔터로 전송버튼 설정하기 
		$(window).on('keydown', function(e){
			if(e.keyCode == 13){ // 키보드 배열 중, 13번은 엔터키. 이 e가 발생되면, 
				send();
			};
		})
		
		
		//send () 설정하기 
		function send(){
			var name = $('#name').val();
			var text = $('#text').val();
			var time = formattedDate;
			
			var chatData = {
					"name" : name,
					"msg" : text,
					"regDate" : time
			};
			
			
			// 날짜추가하기? 
			stompClient.send("/app/server", {}, JSON.stringify({
				"name":name,
				"msg":text,
				"regDate":currentTime
			}));
			
			// 채팅 DB에 저장 
			$.ajax({
				type : "POST",
				url : "/saveChat.do",
				contentType : "application/json",
				data : JSON.stringify(chatData),
				success: function(response){
					alert("done");
				},
				error : function(error){
					alert("에러발생");
				}
			})
		}
	
	
		//goback() 설정하기 
		function goBack(){
			history.go(-1);
		}
		
	
	</script>

</body>
</html>