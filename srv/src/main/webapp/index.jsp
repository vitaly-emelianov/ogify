<%--
  Created by IntelliJ IDEA.
  User: melges.morgen
  Date: 14.02.15
  Time: 21:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String redirectPath = "/landing";
    boolean auth = false;
    Cookie[] cookies = request.getCookies();
    if(cookies != null)
        for(Cookie cookie : request.getCookies())
            if("sId".equals(cookie.getName()))
                redirectPath = "/client";

    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    response.setHeader("Location", redirectPath);
%>
<html>
<head>
    <title>Start page</title>
</head>
<body>
</body>
</html>
