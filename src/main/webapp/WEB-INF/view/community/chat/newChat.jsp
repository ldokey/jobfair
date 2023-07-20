<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>

    <!-- CSS and JavaScript libraries -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.7.0.js" integrity="sha256-JlqSTELeR4TLqP0OG9dxM7yDPqX1ox/HfgiSLBj8+kM=" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js" integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js" integrity="sha512-1QvjE7BtotQjkq8PxLeF6P46gEpBRXuskzIVgjFpekzFVF4yjRgrQvTG1MTOJ3yQgvTteKAcO7DSZI92+u/yZw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

 

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
			position : fixed; top:0; bottom:0; left:0; right:0;
			z-index: 99; 
			width : 100%;
			height : 100%;
			background: rgba(0,0,0,70%)
		}
    </style>
</head>

<body>

 
 
 <div class="container mt-5" style="width: 700px;">
        <!-- Chat room list section -->
        <div class="card">
            <div class="card-header">유저목록</div>
            <div class="card-body">
                <table class="w-100">
                    <tr>
                        <td width="20%">번호   </td>
                        <td width="60%"> 아이디  </td>
                        <td width="20%"> 기타  </td>
                    </tr>
						
						
						    <tr >
						        <td width="20%"></td>
						        <td width="60%"><button id="createChatButton" data-userid="dooli">Create Chat</button> </td>
						        <td width="20%"> </td>
						    </tr>
						    
						
                </table>
            </div>
        </div>
    </div>
 
 
 
 <script>
 
 
 
 </script>
 
</body>
</html>