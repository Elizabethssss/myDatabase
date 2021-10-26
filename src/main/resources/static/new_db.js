$(document).on('click', '#new-db-submit', function () {
    const input = $('#db-name').val()

    $.ajax({
        url: 'database',
        method: 'POST',
        contentType: 'text/plain',
        data: input,
    }).done(function () {
        document.location = "/home";
    }).fail(function (res) {
        console.error(res)
    })
});