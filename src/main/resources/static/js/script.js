$("#selectAll").click(function() {

	if ($(this).is(':checked')) {
		$("input[type='checkbox']").prop('checked', true);
	} else {
		$("input[type='checkbox']").prop('checked', false);
	}

});

function ConfirmDeleteGroups(event) {
	if (!$("input:checked").length) {
		event.preventDefault();
		alert("no groups selected");
		return;
	}

	return confirm("Do you want to delete the selected groups and all containing tweets?");

};

function ConfirmDeleteTweets(event) {
	if (!$("input:checked").length) {
		event.preventDefault();
		alert("no tweets selected");
		return;
	}
	return confirm("Do you want to delete the selected tweets?");
};

$('body').scrollspy({
    target: '.bs-docs-sidebar',
    offset: 40
});

if ($('textarea#content').length > 0) {
	var regex = new RegExp('https?://[^\\s]*', 'g');
	var subst = new Array(25+1).join('.');
	var tarea = $('textarea#content');
	var tweet = window.location.pathname.substr(window.location.pathname.lastIndexOf('/') + 1);

	var flashcard = $('<div id="flashcard" class="small text-warning">Text too long!<br>Will use flashcard!</div>').hide();
	var charcount = $('<div id="tweetsize" class="small text-muted">(<span id="charcount">0</span> of '+MAX_TWEET_LENGTH+' chars)</div>');

	var calculate = function() {
		var text = tarea.val().replace(regex, subst);
		var size = 0;
		for(var i = 0; i < text.length; i++){
			size+= text.codePointAt(i) >= 4352 ? 2 : 1;
		}
		$('#charcount').html(size);

		if (size > MAX_TWEET_LENGTH && $('#flashcard').not(':visible')) {
			$('#tweetsize').hide();
			$('#flashcard').show();
		}
		if (size <= MAX_TWEET_LENGTH && $('#flashcard').is(':visible')) {
			$('#tweetsize').show();
			$('#flashcard').hide();
		}
	}

	$('label[for="content"]').append(charcount);
	$('label[for="content"]').append(flashcard);

	calculate();
	$('textarea#content').on('input propertychange', function() { calculate(); });
}

