<html>
    <head>
       <title>Accounter</title>
       <meta http-equiv="content-type" content="text/html; charset=UTF-8">
       <link type="text/css" rel="stylesheet" href="/Finance.css?version=<%= version%>">
    </head>
    <body style="background:none repeat scroll 0 0 #D8DCE0;">
      <div class="error_screen">
         <div class="error_vimukti_logo"></div>
         <div class="exception_box">
            <div class="main_data_box">
                 <h4>Exception in database</h4>
                 <div><%= request.getParameter("message") %></div>
                 <a href="/site/home">Go to Home page</a>
            </div>
         </div>
      </div>      
    </body>
</html>

