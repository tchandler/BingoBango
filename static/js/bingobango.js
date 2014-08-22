window.jQuery(document).ready(function($) {
	$('.bingotable td').on('click', function() {
		$(this).toggleClass('selected');
	});
});