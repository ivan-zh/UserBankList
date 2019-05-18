<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
    <h2>Hello!</h2>
     <form action = "richest" method = "GET">
        <b>The richest user: </b> <input type="text" value = "${name}"/>
        <br>
        <b>Sum of all accounts: </b> <input type="text" value = "${total}"/>
        <br>
        <br>
        Press button to update info:<br>
        <input type="submit" value = "Get info"/>
     </form>
    <br/>
    <!--
        <c:out value = "${name}"/>
     -->
</body>

</html>
