const tableName = $('#add-column').attr("table-meta");
const dbName = $("#add-column").attr("db-meta");

const json = {
    dbName,
    tableName,
    columns: []
}

$(document).on('click', '#add-column', function () {
    const name = $("#new-column-name").val().trim()
    if (!name) {
        alert("FIll column name!")
        return;
    }
    const col = {
        name,
        type: $("#new-column-type").val()
    }
    json.columns.push(col)

    console.log(json)

    $.ajax({
        url: '/column',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(json),
        success: function (newTable) {
            document.location = "/table/" + newTable.tableName + "?dbName=" + newTable.dbName;
        },
        error: function (e) {
            console.error(e);
        }
    })
})

$(document).on('click', '.delete-column', function () {
    const name = $(this).attr("data-column-meta")
    const col = {
        name
    }
    json.columns.push(col)

    console.log(json)

    $.ajax({
        url: '/column/delete',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(json),
        success: function (newTable) {
            document.location = "/table/" + newTable.tableName + "?dbName=" + newTable.dbName;
        },
        error: function (e) {
            console.error(e);
        }
    })
})

$(document).on('click', '#undo', function () {
    document.location.reload()
})
