$(document).ready(function () {
    $.get("database/all", function (data) {
        let dataObject = JSON.parse(data);
        fillTable(dataObject || []);
    })
})

function fillTable(dbs) {
    let dbItems = '';
    (dbs || []).forEach(db => {
        dbItems += getBicycleTemplate(db)
    })

    const dbList = document.getElementById('db-list');
    dbList.innerHTML = dbItems;
}

const getBicycleTemplate =
    (name = '') =>
        `<li>
            <a href="database/${name}">${name}</a>
         </li>`;

