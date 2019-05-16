<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
      <form action = "richest" method = "GET">
        <input type="text" value = "${name}"/>
        <input type="submit" value = "Get name of the richest user"/>
      </form>
    <br/>
      <form action = "sum" method = "GET">
        <input type="text" value = "${total}"/>
        <input type="submit" value = "Get sum of all accounts"/>
      </form>
</body>

</html>
