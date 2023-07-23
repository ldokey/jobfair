<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <title>공지사항</title>
            <!-- sweet alert import -->
            <script src='${CTX_PATH}/js/sweetalert/sweetalert.min.js'></script>
             <jsp:include page="/WEB-INF/view/common/common_include.jsp"></jsp:include>

            <!-- CSS and JavaScript libraries -->
            
           <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
                crossorigin="anonymous">
                
                
            <script src="https://code.jquery.com/jquery-3.7.0.js"
                integrity="sha256-JlqSTELeR4TLqP0OG9dxM7yDPqX1ox/HfgiSLBj8+kM=" crossorigin="anonymous"></script>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
                integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g=="
                crossorigin="anonymous" referrerpolicy="no-referrer"></script>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"
                integrity="sha512-1QvjE7BtotQjkq8PxLeF6P46gEpBRXuskzIVgjFpekzFVF4yjRgrQvTG1MTOJ3yQgvTteKAcO7DSZI92+u/yZw=="
                crossorigin="anonymous" referrerpolicy="no-referrer"></script>



            <!-- Custom CSS -->
            <style>
            @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300&display=swap');

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

                #chatModal {
                    position: fixed;
                    top: 0;
                    bottom: 0;
                    left: 0;
                    right: 0;
                    z-index: 99;
                    width: 100%;
                    height: 100%;
                    background: rgba(0, 0, 0, 70%)
                }
            </style>
        </head>

        <body>
            <form id="myForm" action="" method="">
                <input type="hidden" name="action" id="action" value=""> <input type="hidden" name="loginId"
                    id="loginId" value="${loginId}">
                <input type="hidden" name="userNm" id="userNm" value="${userNm}">
                <input type="hidden" name="noticeno" id="noticeno" value=""> <input type="hidden" name="currentpage"
                    id="currentpage" value="">
                <input type="hidden" name="filecd" id="filecd" value="">

                <!-- 모달 배경 -->
                <div id="mask"></div>

                <div id="wrap_area">

                    <h2 class="hidden">header 영역</h2>
                    <jsp:include page="/WEB-INF/view/common/header.jsp"></jsp:include>

                    <h2 class="hidden">컨텐츠 영역</h2>
                    <div id="container">
                        <ul>
                            <li class="lnb">
                                <!-- lnb 영역 -->
                                <jsp:include page="/WEB-INF/view/common/lnbMenu.jsp"></jsp:include>
                                <!--// lnb 영역 -->
                            </li>
                            <li class="contents">
                                <!-- contents -->
                                <h3 class="hidden">contents 영역</h3>






                                <div class="card">
                                    <div class="card-header">${loginId}님의 채팅방 리스트</div>
                                    <div class="card-body">
                                        <table class="w-100" border="1">
                                            <tr>
                                                <td width="20%">번호</td>
                                                <td width="60%">방이름</td>
                                                <td width="20%">시간</td>
                                            </tr>
                                            <c:forEach items="${chatList}" var="chat">

                                                <tr onclick="openPop(${chat.chatRoomNo});">
                                                    <td width="20%">${chat.chatRoomNo}</td>
                                                    <td width="60%">${chat.chatTitle}</td>
                                                    <td width="20%">${chat.regDate}</td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </div>
                                </div>








                                <div id="chatModal" class="layerPop layerType2" style="width: 600px; display: none;">
                                    <div class="container mt-5" id="happy" style="width: 700px;">
                                        <a href="" class="closePop"><span class="hidden">닫기</span></a>

                                        <div class="card">
                                            <div class="card-header">채팅방! ${loginId}님 어서오세요</div>
                                            <div class="card-body">
                                                <div>
                                                    <div id="target" class="chatWindow"
                                                        style="height: 400px; overflow-y: scroll;"></div>
                                                    <div class="d-flex justify-content-between" style="height: 45px;">
                                                        <input id="text" type="text" style="width: 77%" />
                                                        <button style="width: 20%" onclick="send();"
                                                            class="btn btn-success btn-sm">전송</button>
                                                        <input type="hidden" id="name" value="${loginId}">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>




                                <div class="container mt-5" style="width: 700px;">
                                    <!-- Chat room list section -->
                                    <div class="card">
                                        <div class="card-header">유저목록</div>
                                        <div class="card-body">
                                            <table class="w-100">
                                                <tr>
                                                    <td width="20%">번호 </td>
                                                    <td width="60%"> 아이디 </td>
                                                    <td width="20%"> 기타 </td>
                                                </tr>


                                                <tr>
                                                    <td width="20%"></td>
                                                    <td width="60%">
                                                        <button id="createChatButton" data-target-loginid="">Create
                                                            Chat</button>
                                                    </td>
                                                    <td width="20%"></td>
                                                </tr>


                                            </table>
                                        </div>
                                    </div>
                                </div>


                                <h3 class="hidden">풋터 영역</h3>
                                <jsp:include page="/WEB-INF/view/common/footer.jsp"></jsp:include>
                            </li>
                        </ul>
                    </div>
                </div>
            </form>






            <script>

                $(function () {
                    // 방번호를 임시로 담는 roomNum 
                    var roomNum;
                })

                $("#createChatButton").on("click", function () {
                    var targetUserId = $(this).data("targetUserid"); // 데이터 속성 이름 수정
                    console.log(targetUserId);
                    createOrGetChatRoom(targetUserId);
                });




                // 채팅 일시 저장 
                var chatHtml = "";


                function openPop(chatRoomNo) {
                    /* initpopup(); */
                    gfModalPop("#chatModal");

                    console.log(chatRoomNo);

                    roomNum = chatRoomNo;

                    $.ajax({
                        url: "/chatHistory.do",
                        data: { chatRoomNo: chatRoomNo },
                        success: displayChatHistory,
                        error: function () {
                            alert("error");
                        }
                    })
                };

                // displayChatHistory 함수 정의 
                function displayChatHistory(chatHistory) {
                    //스톰프 연결 : 모달창 등장시, 스톰프 연결 
                    subscribeToChat(roomNum);
                    console.log("data : " + data)
                    console.log("roomNum : " + roomNum)

                    console.log(chatHistory);
                    for (var i = 0; i < chatHistory.length; i++) {
                        var data = chatHistory[i];
                        var sender = data.name;
                        var message = data.msg;
                        var date = data.regDate;
                        var count = data.readCount;

                        // 채팅창에 과거 메시지 붙이기 + 채팅 출력 위치 - 상대방 좌측 정렬   
                        if (sender != '${loginId}') {
                            chatHtml += '<div class="chatchat">';
                            chatHtml += '<div style="display:flex; flex-direction:column;">';
                            chatHtml += '<p>' + sender + ' : </p>';
                            chatHtml += '</div>';
                            chatHtml += '<p style="background:#f1f1f1; padding:5px 10px;">' + message + '</p>';
                            chatHtml += '<p style="font-size: 12px; color: #999; margin-right: 10px; padding-top: 6px;">&lt;' + date + '&gt;</p>';
                            chatHtml += '</div>';
                            //  채팅 출력 위치 - 본인 우측 정렬 
                        } else {
                            chatHtml += '<div class="chatchat" style="justify-content:flex-end; margin-right:5px;">';
                            chatHtml += '<p style="font-size: 12px; color: #999; margin-right: 10px; padding-top: 6px;">&lt;' + count + '&gt;</p>';
                            chatHtml += '<p style="font-size: 12px; color: #999; margin-right: 10px; padding-top: 6px;">&lt;' + date + '&gt;</p>';
                            chatHtml += '<div style="display:flex; flex-direction:column;">';
                            chatHtml += '<p>' + sender + ' : </p>';
                            chatHtml += '</div>';
                            chatHtml += '<p style="background:#fef01b; padding:5px 10px;">' + message + '</p>';
                            chatHtml += '</div>';
                        }
                    }
                    $('#target').append(chatHtml);
                    scrollToBottom();
                }

                // 스톰프 (속js 보조) 연결 및 채팅 
                function subscribeToChat(roomNum) {
                    var socket = new SockJS("/chat/room");
                    stompClient = Stomp.over(socket);
                    stompClient.connect({}, function (frame) {

                        console.log("Connected" + frame + roomNum); // 도착 

                        stompClient.subscribe("/topic/" + roomNum, function (res) {
                            var data = JSON.parse(res.body);

                            console.log("aa" + data);// 안나옴 

                            var formattedDate = getCurrentTime();
                            var sender = data.name;
                            var message = data.msg;
                            var date = data.regDate;

                            console.log(">>>>>>data.name " + data.name)

                            var count = data.readCount;
                            // 메시지 읽음을 서버에 전달 
                            sendReadCount(data.chatNo);
                            // 채팅 출력 위치 - 상대방 좌측 정렬  
                            if (data.name != '${loginId}') {

                                console.log(">>>>>>data.name " + data.name)


                                chatHtml = '<div class="chatchat">';
                                chatHtml += '<div style="display:flex; flex-direction:column;">';
                                chatHtml += '<p>' + sender + ' : </p>';
                                chatHtml += '</div>';
                                chatHtml += '<p style="background:#f1f1f1; padding:5px 10px;">' + message + '</p>';
                                chatHtml += '<p style="font-size: 12px; color: #999; margin-right: 10px; padding-top: 6px;">&lt; ' + formattedDate + ' &gt;</p>';
                                chatHtml += '</div>';

                                console.log(">>>>>>>>>sender: " + sender)

                                $('#target').append(chatHtml);
                            } else {
                                // 채팅 출력 위치 - 본인 우측 정렬 
                                chatHtml = '<div class="chatchat" style="justify-content:flex-end; margin-right:5px;">';
                                /* chatHtml += '<p style="font-size: 12px; color: #999; margin-right: 10px; padding-top: 6px;">&lt; ' + count + ' &gt;</p>'; */
                                chatHtml += '<p style="font-size: 12px; color: #999; margin-right: 10px; padding-top: 6px;">&lt; ' + formattedDate + ' &gt;</p>';
                                chatHtml += '<div style="display:flex; flex-direction:column;">';
                                chatHtml += '<p>' + sender + ' : </p>';
                                chatHtml += '</div>';
                                chatHtml += '<p style="background:#fef01b; padding:5px 10px;">' + message + '</p>';
                                chatHtml += '</div>';
                                $('#target').append(chatHtml);
                            }
                            // 전송 버튼 누른 후, 채팅 입력칸 벨류 초기화
                            $('#text').val("");
                            scrollToBottom();
                        });
                    });
                }

                //send () 설정하기 
                function send() {
                    var name = $('#name').val();
                    var text = $('#text').val();
                    var time = getCurrentTime();

                    // 분기 조건을 통해 null값의 메시지 전송 방지. 채팅에 빈 값일 경우, 조기에 return 처리.   
                    if (!text.trim()) {
                        alert("내용을 입력하세요");
                        return;
                    }

                    var chatData = {
                        "name": name,
                        "msg": text,
                        "regDate": time,
                        "chatRoomNo": roomNum
                    };

                    // 스톰프출발 ~ 
                    stompClient.send("/app/server/" + roomNum, {}, JSON.stringify({
                        "name": name,
                        "msg": text,
                        "regDate": time,
                        "chatRoomNo": roomNum
                    }));

                    // 채팅 DB에 저장 
                    $.ajax({
                        type: "POST",
                        url: "/saveChat.do",
                        contentType: "application/json",
                        data: JSON.stringify(chatData),
                        success: function (response) {
                            alert("발송완료");
                            // 새로운 채팅 입력시, 스크롤 맨 아래로 이동   
                            scrollToBottom();
                        },
                        error: function (error) {
                            alert("에러발생");
                        }
                    });
                }

                // 메시지 읽음 
                function sendReadCount(chatNo) {
                    $.ajax({
                        type: "POST",
                        url: "/chat/read",
                        data: JSON.stringify({ chatNo: chatNo }),
                        contentType: "application/json",
                        success: function (response) {
                            console.log("read done");
                        },
                        error: function (error) {
                            console.log("read error");
                        }
                    })
                }


                function gfModalPop(id) {

                    //var id = $(this).attr('href');

                    var maskHeight = $(document).height();
                    var maskWidth = $(document).width();

                    $('#mask').css({ 'width': maskWidth, 'height': maskHeight });

                    $('#mask').fadeIn(200);
                    $('#mask').fadeTo("fast", 0.5);

                    var winH = $(window).height();
                    var winW = $(window).width();
                    var scrollTop = $(window).scrollTop();

                    $(id).css('top', winH / 2 - $(id).height() / 2 + scrollTop);
                    $(id).css('left', winW / 2 - $(id).width() / 2);

                    $(".layerPop").hide();
                    $(id).fadeIn(100); //페이드인 속도..숫자가 작으면 작을수록 빨라집니다.
                }





                // 원하는 형식으로 날짜 정보 가져오기
                function getCurrentTime() {
                    var currentTime = new Date();
                    var year = currentTime.getFullYear();
                    var month = ("0" + (currentTime.getMonth() + 1)).slice(-2);
                    var day = ("0" + currentTime.getDate()).slice(-2);
                    var hours = ("0" + currentTime.getHours()).slice(-2);
                    var minutes = ("0" + currentTime.getMinutes()).slice(-2);
                    var seconds = ("0" + currentTime.getSeconds()).slice(-2);
                    var formattedDate = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
                    return formattedDate;
                }

                // 새로운 채팅 입력시, 스크롤 맨 아래로 이동   
                function scrollToBottom() {
                    var chatWindow = $('#target');
                    chatWindow.scrollTop(chatWindow.prop("scrollHeight"));
                }
                // 엔터로 전송버튼 설정하기 
                $(window).on('keydown', function (e) {
                    if (e.keyCode == 13) { // 키보드 배열 중, 13번은 엔터키. 이 e가 발생되면, 
                        send();
                    }
                    ;
                })


            </script>

        </body>

        </html>