<%@page pageEncoding="UTF-8" %>
<div id="language_picker"> 
	<select id='locale_option'>
		<option style="color:#999" value="">English</option>
		<option disabled="" style="color:#999" value="">------------</option>
		 <option value="/about/intl/ar/">العربية</option>
		 <option value="/about/intl/zh/">中国</option>
		 <option value="/about/intl/nl/">Nederlands</option>
		 <option value="/about/intl/fr/">française</option>
		 <option value="/about/intl/de/">Deutschland</option>
		 <option value="/about/intl/id/">Bahasa Indonesia</option>
		 <option value="/about/intl/it/">italiano</option>
		 <option value="/about/intl/jp/">日本</option>
		 <option value="/about/intl/ko/">한국의</option>
		 <option value="/about/intl/pl/">polski</option>
		 <option value="/about/intl/pt/">Português</option>
		 <option value="/about/intl/ru/">русский</option>
		 <option value="/about/intl/es/">spanska</option>
		 <option value="/about/intl/th/">ไทย</option>
		 <option value="/about/intl/tr/">Türk</option>
		 <option value="/about/intl/uk/">Український</option>
	</select>
	<!--<a href="http://translate.evernote.com">Help translate</a>-->
	<script  type="text/javascript" >
		$(document).ready(function() {
			$('#locale_option').change(function(){
				document.cookie="locale=" + $('#locale_option').val();
				 location.reload();
			});
		});
	</script>
</div>