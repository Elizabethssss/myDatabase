const tableName = $('#add-column').attr("table-meta");
const dbName = $("#add-column").attr("db-meta");

const json = {
    dbName,
    tableName,
    columns: []
};
let table = {}

window.addEventListener('load', function () {
    $.get("/table/meta/" + tableName + "?dbName=" + dbName, function (data) {
        table = data
        console.log(table)
    })
});

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

$(document).on('click', '#save', function () {

    $('table.table tbody tr').each((i, tr) => {
        $(tr).find('td div').each((j, div) => {
            let cell = table.lines[i].cells[j];

            if (cell.type === 'TIME_INTERVAL') {
                cell.value = $(div).find('input')[0].value + ' - ' + $(div).find('input')[1].value
            } else {
                cell.type = $(div).find('input').attr("cellType")
                cell.value = $(div).find('input').val()
            }
            cell.columnName = $('table.table thead th').find('span.col-name').get(j).innerText;
            cell.lineId = i + 1;
        })
    })

    console.log(table)

    $.ajax({
        url: '/table/save',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(table),
        success: function (responseTable) {
            let cells = responseTable.lines.map((l) => {
                return l.cells
            })
            cells = [].concat.apply([], cells)
            let hasError = cells.map((c) => {
                return c.hasError;
            })
                .find((e) => e === true)
            console.log("hasError: ", hasError)
            if (hasError) {
                console.log(responseTable)
                document.location = "/table/validation/session";
            } else {
                document.location = "/table/" + responseTable.tableName + "?dbName=" + responseTable.dbName;
            }
        },
        error: function (e) {
            console.error(e);
        }
    })

})
