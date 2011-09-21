/*
 * contactable 1.0 - jQuery Ajax contact form
 *
 * Copyright (c) 2009 Philip Beel (http://www.theodin.co.uk/)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php) 
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * Revision: $Id: jquery.contactable.pack.js 2009-08-24 $
 *
 */

(function($) {
	$.fn.contactable = function(options) {
		var defaults = {
			name : 'Name',
			email : 'Email',
			message : 'Message',
			ajax : true,
			subject : 'A contactable message'
		};
		var options = $.extend(defaults, options);
		return this
				.each(function(options) {
					$(this)
							.html(
									'<div id="contactable"></div><form id="contactForm" method="" action=""><div id="loading"></div><div id="callback"></div><div class="holder"><input type="hidden" id="recipient" name="recipient" value="'
											+ defaults.recipient
											+ '" /><input type="hidden" id="subject" name="subject" value="'
											+ defaults.subject
											+ '" /><p><label for="name">Name <span class="red"> * </span></label><br /><input id="name" class="contact" name="name" /></p><p><label for="email">E-Mail <span class="red"> * </span></label><br /><input id="email" class="contact" name="email" /></p><p><label for="comment">Your Feedback <span class="red"> * </span></label><br /><textarea id="comment" name="comment" class="comment" rows="4" cols="30" ></textarea></p><p><input class="submit" type="submit" value="Send"/></p><p>This will be some contact information about opening hours etc</p></div></form>');
					$('div#contactable').toggle(function() {
						$('#overlay').css( {
							display : 'block'
						});
						$(this).animate( {
							"marginLeft" : "-=5px"
						}, "fast");
						$('#contactForm').animate( {
							"marginLeft" : "-=0px"
						}, "fast");
						$(this).animate( {
							"marginLeft" : "+=387px"
						}, "slow");
						$('#contactForm').animate( {
							"marginLeft" : "+=390px"
						}, "slow")
					}, function() {
						$('#contactForm').animate( {
							"marginLeft" : "-=390px"
						}, "slow");
						$(this).animate( {
							"marginLeft" : "-=387px"
						}, "slow").animate( {
							"marginLeft" : "+=5px"
						}, "fast");
						$('#overlay').css( {
							display : 'none'
						})
					});
					$("#contactForm").validate(
							{
								rules : {
									name : {
										required : false,
										minlength : 2
									},
									email : {
										required : false,
										email : false
									},
									comment : {
										required : true
									}
								},
								messages : {
									name : "",
									email : "",
									comment : ""
								},
								submitHandler : function() {
									$('#loading').show();
									$.post('/site/support', {
										ajax : true,
										name : $('#name').val(),
										email : $('#email').val(),
										comment : $('#comment').val()
									}, function(data) {
										$('#loading').css( {
											display : 'none'

										});
										if (data == 'success') {
											$('#sucmsg').remove();
											var msgDiv = $('<div id="sucmsg"><p>Thankyou for your Feedback</p>')
											$(".holder").prepend(msgDiv);
											$("#sucmsg").fadeOut(5000);
											$('#name').val("");
											$('#email').val("");
											$('#comment').val("");
										} else {
											$('#failmsg').remove();
											var msgDiv = $('<div id="failmsg"><p>Sorry mail not send,please try again later</p>')
											$(".holder").prepend(msgDiv);
											$("#failmsg").fadeOut(5000);
											$('#name').val("");
											$('#email').val("");
											$('#comment').val("");
										}
									})
							

								}
							})
							
				})
	}
})(jQuery);