<!-- 

This is the jsp file which is responsible for discussion topic view.   
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<script language="javascript" type="text/javascript" src='/scripts/jquery.js' ></script>
<script language="javascript" type="text/javascript" src='/js/jquery.js' ></script>
<script language="javascript" type="text/javascript" src='/jscripts/tiny_mce/tiny_mce.js'></script>

<link rel="stylesheet" type="text/css" href="/scripts/stylesheets/discussionStyle.css">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script language="javascript" type="text/javascript">
</script>

<script type="text/javascript">
var tinyMCEId="";
$(document).ready(function(){
	$('a').attr("target", "_new");
		var tinymcenumber=0;	
		
	$('#inp1').click(function(){
		var userSelection;
		var  selectedtext;
		
		  if (window.getSelection) {
		      selection = window.getSelection();
		      selectedtext=selection;
		  } else if (document.selection) {
		      selection = document.selection.createRange();
		      selectedtext=selection.text;
		  }/*
		  here we find out which element of text he has selected and get the selected text
		  */
		 
		  if(tinymcenumber!=0){
			   $('#div1').fadeOut().remove();
			  }
		  var parent = selection.focusNode;
		  var generalParent=parent;
		  var divtag=document.createElement('div');
		  $(divtag).attr("id", "div1");
		  
		  var textbox  = document.createElement('textarea');
		  var button1=document.createElement('input');
		  var button2=document.createElement('input');
		  var br=document.createElement("br");
		  var br1=document.createElement("br");
		  $(textbox).attr("name", "topicresponse");
		  $(textbox).attr("id", "topicresponse");
		  $(textbox).attr("rows", "5");
		  $(textbox).attr("cols", "58");
		  $(button1).attr("type", "button");
		  $(button1).attr("value", "Save");
		  
		  $(button1).attr("id", "buttonsave");
		  $(button1).attr("onclick", "savefun(this)");
		  $(button2).attr("type", "button");
		  $(button2).attr("value", "Cancel");
		  $(button2).attr("onclick", "cancelfunction(this)");
		  divtag.appendChild(textbox);
		  divtag.appendChild(br);
		  divtag.appendChild(button1);
		  divtag.appendChild(button2);
		  divtag.appendChild(br);
		  divtag.appendChild(br1);
		  /*
		   * Here we have created the Response/comment box view that is one textbox,
		   one save button and one close button
		   */
		/*
		Now we are finding who is the parent of the user selected text. and insert that dynamic created div with according to condition match
		*/
				
		  
		  var flag=true;
		  if(parent==null||parent==""){
			  
			  $('#spanId').val('parent,parent');
			  $(divtag).insertAfter($("#superParent"));
			  flag=false;
		  }
		  else if (parent.parentNode!=null){
			  while(parent!=null){
				 if(parent.localName=='SPAN'&&parent.id!=null){
					 $('#spanId').val(parent.id);
						// parent=parent.parentNode;
					 break;
					 } parent=parent.parentNode;
				}
		  }
		  
		 if($('#spanId').val()==null||$('#spanId').val()==""){
			 
			  $('#spanId').val('parent,parent');
			  $(divtag).insertAfter($("#superParent"));
			  flag=false;
		}
		 else if(flag){
			 
			 if(parent==null){
				 
				 $('#spanId').val('parent,parent');
				  $(divtag).insertAfter($("#superParent"));
			 }else{
				  
				 $(divtag).insertAfter(parent);
			 }
			 
		  }
		  /*
		  Now we are intiate the tinymce on dynamically created textbox
		  */
		 tinyMCE.init({
			  mode : "textareas",
			  
			   elements : "text",
			theme : "advanced",
			skin_variant: "silver",
			plugins : "inlinepopups",
			theme_advanced_blockformats : "p,h1,h2,h3,h4,h5,h6",
			theme_advanced_buttons1 : "bold,italic,underline,strikethrough,separator,bullist,numlist,outdent,indent,separator,forecolor,backcolor,separator,link,unlink,separator,fontselect,fontsizeselect",
			theme_advanced_buttons2 : "",
			theme_advanced_buttons3 : "",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			valid_elements : "a[href|title|target],strong/b,em/i,strike,u,"
			+ "p[align|style],-ol[type|compact],-ul[type|compact],-li,br,img["
			+ "src|border|alt=|title|hspace|vspace|width|height|align],-sub,-sup,"
			+ "-span[style],address,-h1,-h2,-h3,-h4,-h5,-h6,hr[size|noshade],-font[face"
			+ "|size|color]",
			force_p_newlines : false,
			remove_trailing_nbsp : true,
			forced_root_block : false,
			force_br_newlines : true,
			relative_urls : false,
			remove_script_host : false,
			button_tile_map : true,
			ask : false,
			auto_cleanup_word : true,
			theme_advanced_resizing : true, 
			theme_advanced_resize_horizontal : false,
			safari_warning: false,
			
			skin : "o2k7"
			
			});
		 tinyMCEId=$(textbox).attr('id');
		  tinymcenumber++;
		  tinyMCE.execInstanceCommand(textbox.id, "mceFocus");
		});
	
});
/*
 * This is the cancel button click handlar 
 */
function cancelfunction(){
	 $('#topicresponse').remove();
	 $('#div1').fadeOut().remove();
}
var ii=0;
/*
This is the save button click handlar 
*/
function savefun(){
	
	
	document.getElementById("responseText").value=tinyMCE.get(tinyMCEId).getContent();
	var responseValue=document.getElementById("responseText").value;
	
	if(!responseValue==null||!responseValue==""){
		
		
		if(ii==0){
			$('#responseForm').submit();
			
			
		}
		$('#div1').fadeOut().remove();
	}	
	else{
		$('#topicresponse').fadeOut().remove();
		$('#div1').fadeOut().remove();
	}
}


</script>
</head>
<body >
<c:if test="${setShow}">
<div style="float: right;margin-top:5px;margin-right:20px;">
<c:if test="${discussionResponseStatus}">
<img id="inp1" src="/scripts/images/add-comment-button.png"/></c:if>
</div>
<span class="topicsubject">${topicsubject}</span>
<span class="title10"> by ${creator}</span>

<div style="clear:both;"> 
${topiccontent}
</div>
</c:if>
<span id="superParent"></span>
<form action="/do/workspaces/${workspaceID}/tools/${toolID}/discussion/${discussionID}/discussionTopicResponse?response=true" id="responseForm" method="post">
	<input type="hidden" name="responseText" id="responseText"/>
	<input type="hidden" name="spanId" id="spanId">
</form>

<script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
			document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
</body>
</html>