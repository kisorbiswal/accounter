<div id="language_picker"> 
	<select id='locale_option'>
		<option style="color:#999" value="">English</option>
		<option disabled="" style="color:#999" value="">------------</option>
	    <option value="/about/intl/ca/">Català</option>
	    <option value="/about/intl/da/">Dansk</option>
	    <option value="/about/intl/de/">Deutsch</option>
	    <option value="/about/intl/es/">Español</option>
	    <option value="/about/intl/et/">Eesti</option>
	    <option value="/about/intl/fi/">Suomi</option>
	    <option value="/about/intl/fr/">Français</option>
	    <option value="/about/intl/gl/">Galego</option>
	    <option value="/about/intl/hu/">Magyar</option>
	    <option value="/about/intl/id/">Bahasa Indonesia</option>
	    <option value="/about/intl/it/">Italiano</option>
	    <option value="/about/intl/jp/">???</option>
	    <option value="/about/intl/ka/">???????</option>
	    <option value="/about/intl/ko/">???</option>
	    <option value="/about/intl/ms/">Melayu</option>
	    <option value="/about/intl/nl/">Nederlands</option>
	    <option value="/about/intl/pl/">Polski</option>
	    <option value="/about/intl/pt/">Português</option>
	    <option value="/about/intl/pt-br/">Português (Brasil)</option>
	    <option value="/about/intl/ru/">???????</option>
	    <option value="/about/intl/sh/">Srpski</option>
	    <option value="/about/intl/sr/">??????</option>
	    <option value="/about/intl/th/">???</option>
	    <option value="/about/intl/tr/">Türkçe</option>
	    <option value="/about/intl/zh-cn/">??(??)</option>
	    <option value="/about/intl/zh-tw/">??(??)</option>
	    <option value="/about/intl/sv/">Svenska</option>
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