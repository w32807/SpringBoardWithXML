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
				<form action="/board/modify" method="post">
					<div class="form-group">
						<label>Bno</label><input class="form-control" name="bno" value='<c:out value="${board.bno }"/>' >
					</div>
					<div class="form-group">
						<label>Title</label><input class="form-control" name="title"value='<c:out value="${board.title }"/>' >
					</div>
					<div class="form-group">
						<label>Text area</label><textarea class="form-control" rows="3" name="content" ><c:out value="${board.content }"/></textarea>
					</div>
					<div class="form-group">
						<label>Writer</label><input class="form-control" name="writer" value='<c:out value="${board.writer }"/>' >
					</div>
					<input type="hidden" name="regDate" value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.regDate }"/>' >
					<input type="hidden" name="updateDate" value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.updateDate }"/>' >
               		<input type="hidden" name="pageNum"  value='<c:out value="${cri.pageNum }"/>'>
               		<input type="hidden" name="amount"  value='<c:out value="${cri.amount }"/>'>
	            	<input type="hidden" name="type" value="<c:out value="${cri.type }"/>">
	            	<input type="hidden" name="keyword" value="<c:out value="${cri.keyword }"/>">
					<button data-oper='modify' class="btn btn-default">Modify</button>                    
					<button data-oper='remove' class="btn btn-info">Remove</button>
					<button data-oper='list' class="btn btn-info">List</button>
				</form>                    
			</div>
		</div>
	</div>
</div>
<%@include file="../includes/footer.jsp" %>

<script>
	$(document).ready(function() {
		var formObj = $('form');
		
		$('button').on('click', function(e) {
			e.preventDefault();
			var operation = $(this).data('oper');

			if(operation === 'remove'){
				formObj.attr('action', '/board/remove');
			}else if(operation === 'list'){
				formObj.attr('action', '/board/list').attr('method', 'get');
				var pageNumTag = $('input[name="pageNum"]');
				var amountTag = $('input[name="amount"]');
				var keywordTag = $('input[name="keyword"]');
				var typeTag = $('input[name="type"]');
				
				formObj.empty(); 
				formObj.append(pageNumTag); 
				formObj.append(amountTag); 
				formObj.append(keywordTag); 
				formObj.append(typeTag); 
			}
			formObj.submit();
		})
	})
</script>