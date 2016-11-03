<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<body>
<c:if test="${code eq 1}">
	<h5>采集结果为<span style="color: green">${result }</span></h5>
	<h5>biz_id为${bizId }</h5>
</c:if>
<c:if test="${code eq 0 }">
	<h5>采集结果为<span style="red">${result }</span></h5>
	<h5>失败原因为${failReason }</h5>
</c:if>
</body>
</html>
