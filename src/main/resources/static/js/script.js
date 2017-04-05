$('body').scrollspy({
    target: '.bs-docs-sidebar',
    offset: 40
});

<<<<<<< HEAD
=======
a https://wiki.selfhtml.org/wiki/JavaScript/Objekte/RegExp https://encrypted.google.com/search?hl=de&q=javascript%20regex%20whitespace%20slash%20match

Ein Text mit einem Link und 28 Zeichen delta http://without-systemd.org/wiki/index.php/Main_Page wird richtig berechnet nehme ich an. Aber wird er auch korrekt abgeschnitten?
Ein Text mit einem Link und 28 Zeichen delta ....................... wird richtig berechnet nehme ich an. Aber wird er auch korrekt abgeschnitten?

>>>>>>> 8833b111b303987d288fcbe4a3cd1015fea9065e
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
