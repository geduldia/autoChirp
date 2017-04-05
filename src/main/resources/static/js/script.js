$('body').scrollspy({
    target: '.bs-docs-sidebar',
    offset: 40
});

if ($('textarea#content').length > 0) {
	var regex = new RegExp('https?://[^\\s]*', 'g');
	var subst = new Array(24).join('.');

	$('label[for="content"]').append('<div class="small text-muted">(<span id="charcount">0</span> of 140 chars)</div>');

	$('textarea#content').on('input propertychange', function() {
		var content = $(this).val().replace(regex, subst);
		var length = $(this).val().length - content.length;

		$(this).val($(this).val().substring(0, 140 + length));
		$('span#charcount').html($(this).val().replace(regex, subst).length);
	});
}
