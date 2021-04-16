<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>

<%@include file="../includes/header.jsp" %>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Tables</h1>
    </div>
</div>
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">Board List Page</div>
            <div class="panel-body">
               <div class="form-group">
                   <label>Bno</label><input class="form-control" name="bno" value='<c:out value="${board.bno }"/>' readonly="readonly">
               </div>
               <div class="form-group">
                   <label>Title</label><input class="form-control" name="title"value='<c:out value="${board.title }"/>' readonly="readonly">
               </div>
               <div class="form-group">
                   <label>Text area</label><textarea class="form-control" rows="3" name="content" readonly="readonly"><c:out value="${board.content }"/></textarea>
               </div>
               <div class="form-group">
                   <label>Writer</label><input class="form-control" name="writer" value='<c:out value="${board.writer }"/>' readonly="readonly">
               </div>
               <form id="operForm" action="/board/modify" method="get">
               		<input type="hidden" id="bno" name="bno"  value='<c:out value="${board.bno }"/>'>
               		<input type="hidden" name="pageNum"  value='<c:out value="${cri.pageNum }"/>'>
               		<input type="hidden" name="amount"  value='<c:out value="${cri.amount }"/>'>
	            	<input type="hidden" name="type" value="<c:out value="${cri.type }"/>">
	            	<input type="hidden" name="keyword" value="<c:out value="${cri.keyword }"/>">
               </form>
               <button data-oper='modify' class="btn btn-default">Modify</button>                    
               <button data-oper='list' class="btn btn-info">List</button>     
            </div>
        </div>
    </div><!--  -->
	<!-- 댓글 -->
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-comments fa-fw"></i>Reply
				<button id="addReplyBtn" class="btn btn-primary btn-xs pull-right">New Reply</button>
			</div>
			<div class="panel-body">
				<ul class="chat"></ul>
			</div>
			<div class="panel-footer">
			</div>
		</div>
	</div>  
    <!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	   <div class="modal-dialog">
	       <div class="modal-content">
	           <div class="modal-header">
	               <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	               <h4 class="modal-title" id="myModalLabel">Reply Modal</h4>
	           </div>
	           <div class="modal-body">
					<div class="form-group">
						<label>Reply</label><input class="form-control" name="reply" value='New Reply' >
					</div>
					<div class="form-group">
						<label>Replyer</label><input class="form-control" name="replyer"value='replyer' >
					</div>
					<div class="form-group">
						<label>Reply Date</label><input class="form-control" name="replyDate"value='' >
					</div>
	           </div>
	           <div class="modal-footer">
	               <button id="modalModBtn" type="button" class="btn btn-warning" >Modify</button>
	               <button id="modalRemoveBtn" type="button" class="btn btn-danger" >Remove</button>
	               <button id="modalRegisterBtn" type="button" class="btn btn-primary" >Register</button>
	               <button id="modalCloseBtn" type="button" class="btn btn-default" >Close</button>
	           </div>
	       </div>
	       <!-- /.modal-content -->
	   </div>
	   <!-- /.modal-dialog -->
	</div>
</div>
<%@include file="../includes/footer.jsp" %>
<script type="text/javascript" src="/resources/js/reply.js"></script>
<script>
	$(document).ready(function() {
		/*
		var bnoValue = '<c:out value="${board.bno}"/>';
		
		replyService.add(
			{reply:"JS TEST", replyer : "tester", bno : bnoValue},
			function (result) {
				alert("result : " + result);
			}
		)
		
		replyService.getList(
			{bno : bnoValue, page : 1},
			function (list) {
				for(var i = 0,len = list.length||0; i < len; i++){
					console.log(list[i]);
				}
			}
		)
		
		replyService.remove(13, function (count) {
			console.log(count)
			if(count === 'success') alert('REMOVED');
			}, function (err) {
				alert('Error....');
			}
		)
		
		replyService.update({rno : 9, bno : bnoValue, reply : "Modifyed Reply..."}, 
			function (result) {
			console.log()
				alert('수정완료');
		});
		
		replyService.get(9,function (data) {
			console.log(data)
		});
		*/
		
		var bnoValue = '<c:out value="${board.bno}"/>';
		var replyUL = $('.chat');
		showList(1);
		
		function showList(page) {
			replyService.getList(
				{bno : bnoValue, page : page || 1},
				function (replyCnt, list) {
					if(page == -1){
						pageNum = Math.ceil(replyCnt/10.0);
						showList(pageNum);
						return;
					}
					
					if(list == null || list.length == 0) return false;
					
					var str = '';
					for(var i = 0, len = list.length||0; i < len; i++){
						str += "<li class='left clearfix' data-rno='" + list[i].rno + "'>";
						str += "  <div class='header'><strong class='primary-font'>" + list[i].replyer + "</strong>";
						str += "  <small class='pull-right text-muted'>"+ replyService.displayTime(list[i].replyDate) + "</small></div>";
						str += "  <p>"+ list[i].reply + "</p></li>";
					}
					replyUL.html(str);
					showReplyPage(replyCnt);
				}
			)
		}
		
		var modal = $('.modal');
		var modalInputReply = modal.find('input[name="reply"]');
		var modalInputReplyer = modal.find('input[name="replyer"]');
		var modalInputReplyDate = modal.find('input[name="replyDate"]');
		
		var modalModBtn = $('#modalModBtn');
		var modalRemoveBtn = $('#modalRemoveBtn');
		var modalRegisterBtn = $('#modalRegisterBtn');
		// 새로운 댓글 등록
		$('#addReplyBtn').on('click', function(e) {
			modal.find("input").val('');
			modalInputReplyDate.closest('div').hide();
			modal.find("button[id != 'modalCloseBtn']").hide();
			
			modalRegisterBtn.show();
			$('.modal').modal('show');
		});
		// 새로운 댓글 저장
		modalRegisterBtn.on('click', function(e) {
			var reply = {
					reply : modalInputReply.val(),
					replyer : modalInputReplyer.val(),
					bno : bnoValue
			};
			replyService.add(reply, function (result) {
				alert(result);
				modal.find('input').val('');
				modal.modal('hide');
				//showList(1);
				showList(-1); // ???
			});
		});
		
		// 댓글 클릭 이벤트 처리
		// chat에 적용한 이벤트를 on 메소드로써 li 에 위임한다 (li 각각에 click 이벤트가 적용된다는 뜻)
		$('.chat').on('click', 'li', function(e) {
			var rno = $(this).data('rno');
			replyService.get(rno,function (reply) {
				modalInputReply.val(reply.reply);
				modalInputReplyer.val(reply.replyer);
				modalInputReplyDate.val(replyService.displayTime(reply.replyDate))
				.attr('readonly', 'readonly');
				modal.data('rno', reply.rno);
				
				modal.find("button[id != 'modalCloseBtn']").hide();
				
				modalModBtn.show();
				modalRemoveBtn.show();
				$('.modal').modal('show');
			});
		});
		
		// 댓글 수정
		modalModBtn.on('click', function(e) {
			var reply = {
					rno : modal.data('rno'),
					reply : modalInputReply.val(),
			};
			replyService.update(reply, function (result) {
				alert(result);
				modal.modal('hide');
				showList(pageNum);
			});
		});
		
		// 댓글 삭제
		modalRemoveBtn.on('click', function(e) {
			var rno = modal.data('rno');
			replyService.remove(rno, function (result) {
				alert(result);
				modal.modal('hide');
				showList(pageNum);
			});
		});
		
		// 댓글 페이징
		var pageNum = 1;
		var replyPageFooter = $('.panel-footer');
		
		function showReplyPage(replyCnt) {
			var endNum = Math.ceil(pageNum/10.0) * 10;
			var startNum = endNum - 9;
			
			var prev = startNum != 1;
			var next = false;
			
			endNum = (endNum * 10 >= replyCnt) ? Math.ceil(replyCnt/10.0) : endNum;
			next = (endNum * 10 < replyCnt) ? true : false;
			
			var str = '<ul class="pagination pull-right">';
			
			if(prev) str += '<li class="page-item"><a class="page-link" href="' + (startNum - 1) +'">Previous</a></li>"'
			
			for (var i = startNum; i <= endNum; i++) {
				var active = pageNum == i ? 'active' : '';
				
				str+='<li class="page-item ' + active + '"><a class="page-link" href="' + i + '">' + i + '</a></li>';
			}
			
			if(next) str += '<li class="page-item"><a class="page-link" href="' + (endNum + 1) +'">Next</a></li>';
			
			str += '</ul></div>';
			replyPageFooter.html(str);
		}
		
		// 페이징 번호 클릭 시 댓글 가져오기
		replyPageFooter.on('click', 'li a',  function(e) {
			e.preventDefault();
			showList($(this).attr('href'));
		});
		
		
		var openForm = $('form');
		
		$('button[data-oper="modify"]').on('click', function(e) {
			openForm.attr('action', '/board/modify').submit();
		});
		
		$('button[data-oper="list"]').on('click', function(e) {
			openForm.find('#bno').remove(); // id로 찾아서 지우기
			openForm.attr('action', '/board/list').submit();
		});
	})
</script>