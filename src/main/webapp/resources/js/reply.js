// 함수를 변수에 담음 
// replyService에 함수들이 담긴 함수를 선언함
// https://api.jquery.com/jQuery.ajax/ 의 ajax API document를 보면 더 자세하게 알 수 있음
var replyService = (function(){
	function add(reply, callback, error){
		$.ajax({
			type : 'post',
			url : '/replies/new',
			data : JSON.stringify(reply),
			contentType : "application/json; charset=utf-8",
			// result와 error을 함수로 받음
			success : function(result, status, xhr){ // ajax의 success의 콜백의 인자는 순서가 정해져 있음
				console.log('result >> '+ result)
				console.log('status >> '+ status)
				console.log('xhr >> '+ xhr)
				if(callback) callback(result);
			},
			error : function(xhr, status, er){ // ajax의 error의 콜백의 인자는 순서가 정해져 있음
				if(error) error(er);
			}
		})
	}
	
	function getList(param, callback, error){
	/*
		자바스크립트에서 논리연산자 || 의 고찰
		1. 자바스크립트의 ||는 왼쪽부터 시작하여 피연산자가 true가 되는 값을 반환한다.
		2. const n1 = true;
			3 || 4     // 3 -> 숫자는 boolean으로 형변환 된다 (0일 때만 false)
			n1 || 8    // true
			false || 4 // 4 -> 왼쪽이 false니까 그 다음인 4를 반환
			0 || 9     // 9 -> 0은 내부적으로 false 이므로 그 다음인 9를 반환 
			
			** 값이 '', null, 0 일 때는  false를 반환하지만 문자열로 undefined를 쓰거나 NaN을 쓰면 true가 된다.
	*/
		var bno = param.bno;
		var page = param.page || 1;
		
		// 데이터를 Json으로 받아올 때는 $.getJSON 사용 (Json 데이터를 읽어오는 ajax라고 생각하면 됨)
		$.getJSON("/replies/pages/" + bno + "/" + page + ".json",	// Json 타입임을 .json으로 명시
				function(data){
					if(callback) callback(data.replyCnt, data.list);
				}).fail(function(xhr, status, err){
					if(error) error();
				});
	}
	
	function remove(rno, callback, error){
		$.ajax({
			type : 'delete',
			url : '/replies/' + rno,
			success : function(deleteResult, status, xhr){
				if(callback) callback(deleteResult);
			},
			error : function(xhr, status, er){
				if(error) error(er);
			}
		})
	}
	
	function update(reply, callback, error){
		$.ajax({
			type : 'put',
			url : '/replies/' + reply.rno,
			data : JSON.stringify(reply),
			contentType : "application/json; charset=utf-8",
			success : function(result, status, xhr){
				if(callback) callback(result);
			},
			error : function(xhr, status, er){
				if(error) error(er);
			}
		})
	}
	
	function get(rno, callback, error){
		// $.get로 HTTP에서 GET request의 값을 받아옴 (Ajax With type = get 으로 생각하면 됨)
		$.get("/replies/" + rno + ".json",
				function(result){
					if(callback) callback(result);
				}).fail(function(xhr, status, err){
					if(error) error();
				});
	}
	// 날짜 처리 함수
	function displayTime(timeValue){
		var today = new Date();
		var gap = today.getTime() - timeValue;
		var dateObj = new Date(timeValue);
		var str = '';
		
		if(gap < (1000 * 60 * 60 * 24)){
			var hh = dateObj.getHours();
			var mi = dateObj.getMinutes();
			var ss = dateObj.getSeconds();
			// .join으로 배열의 모든 요소를 연결하여 1개의 문자열로 만듬
			return [(hh > 9 ? '' : '0') + hh , ':' , (mi > 9 ? '' : '0') + mi + ':' ,(ss > 9 ? '' : '0') + ss ].join('');
		} else{
			var yy = dateObj.getFullYear();
			var mm = dateObj.getMonth() + 1;
			var dd = dateObj.getDate();
			return [yy, '/' , (mm > 9 ? '' : '0') + mm , '/' , (dd > 9 ? '' : '0') + dd].join('');
		}
	}
	return {
		add:add,
		getList : getList,
		remove : remove,
		update : update,
		get : get,
		displayTime : displayTime
	};
})();