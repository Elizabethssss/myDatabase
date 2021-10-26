let numOfCol = 0;

$('#num-of-col').change(e => {
    numOfCol = Number(e.target.value);
    console.log(numOfCol)
    let html = '';

    if (numOfCol !== 0) {
        for (let i = 1; i <= numOfCol; i++) {
            html += colTemplate(i);
        }
    }

    $('#create-columns').html(html)
})

$(document).on('click', '#new-table-submit', function () {
    const tableName = $('#table-name').val().trim();
    const dbName = $("#new-table-submit").attr("db-meta");

    if (!tableName) {
        alert("FIll table name!")
        return;
    }

    const json = {
        dbName,
        tableName,
        columns: []
    }

    if (numOfCol === 0) {
        alert("No columns!")
        return;
    }

    for (let i = 1; i <= numOfCol; i++) {
        const name = $(`#col-name-${i}`).val().trim()
        if (!name) {
            alert("FIll all names of columns!")
            return;
        }
        const col = {
            name,
            type: $(`#type-${i}`).val()
        }
        json.columns.push(col)
    }

    console.log(json)


    $.ajax({
        url: 'table',
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
});


const colTemplate = (number) => {
    return `<div>
            <hr/>
            <h4 class="mt-2">Column ${number}</h4>
            <label for="col-name-2" class="form-label">Column ${number} Name:</label>
            <input type="text" id="col-name-${number}" class="form-control" style="width: 300px">
            <label for="type-${number}" class="form-label">Column ${number} Type:</label>
            <select id="type-${number}" class="select">
                <option value="STRING">STRING</option>
                <option value="INTEGER">INTEGER</option>
                <option value="REAL">REAL</option>
                <option value="CHAR">CHAR</option>
                <option value="TIME">TIME</option>
                <option value="TIME_INTERVAL">TIME_INTERVAL</option>
            </select>
        </div>`
}

