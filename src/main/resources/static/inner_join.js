window.addEventListener('load', function () {
    $('#table1').change(function () {
        let table = database.tables.find((t) => {
            return t.tableName === $('#table1').val()
        })

        let columnItems = '';
        (table?.columns || []).forEach(col => {
            columnItems += getColumnsTemplate(col)
        });

        document.getElementById("columns1").innerHTML = columnItems

        $('#table2 option').each((i, o) => {
            $(o).prop("disabled", $(o).val() === (table?.tableName || false));
        })

    });

    $('#table2').change(function () {
        let table = database.tables.find((t) => {
            return t.tableName === $('#table2').val()
        })

        let columnItems = '';
        (table?.columns || []).forEach(col => {
            columnItems += getColumnsTemplate(col)
        });

        document.getElementById("columns2").innerHTML = columnItems

        $('#table1 option').each((i, o) => {
            $(o).prop("disabled", $(o).val() === (table?.tableName || false));
        })

    });
})


$(document).on('click', '#inner_join', function () {
    let inner_join = {
        dbName: database.name
    }

    let column1 = $('#columns1')
    let col1 = {
        name: column1.val(),
        type: column1.find('option:selected').attr('colType')
    }
    let table1 = {
        tableName: $('#table1').val(),
        column: col1
    }

    let column2 = $('#columns2')
    let col2 = {
        name: column2.val(),
        type: column2.find('option:selected').attr('colType')
    }
    let table2 = {
        tableName: $('#table2').val(),
        column: col2
    }

    inner_join.table1 = table1
    inner_join.table2 = table2

    console.log(inner_join)

    $.ajax({
        url: '/table/join/inner',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(inner_join),
        success: function (data) {
            console.log(data)
            document.location = "/result";
        },
        error: function (e) {
            console.error(e);
        }
    })
})

const getColumnsTemplate = ({
                                name = '',
                                type = ''
                            }) =>
    ` <option value="${name}" colType="${type}">${name} (${type})</option>`;
