importScripts("../export/js-xlsx/xlsx.core.min.js");
importScripts("../export/js-xlsx/Blob.js");
importScripts("../export/js-xlsx/Export2Excel.js");

onmessage = function (evt) {
    var title = evt.data.title;
    var data = evt.data.data;
    var ws = sheet_from_array_of_arrays(data);
    var wb = new Workbook();
    wb.SheetNames.push(title);
    wb.Sheets[title] = ws;
    var wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: true, type: 'binary'});

    // var ranges = [];
    // var out = [];
    // for (var R = 0; R < data.length; ++R) {
    //     var outRow = [];
    //     var row = data[R];
    //     for (var C = 0; C < row.length; ++C) {
    //         debugger;
    //         var cell = row[C];
    //         var colspan = cell.colspan;
    //         var rowspan = cell.rowspan;
    //         var cellValue = cell.value;
    //         if (cellValue !== "" && cellValue == +cellValue) cellValue = +cellValue;
    //         //Skip ranges
    //         ranges.forEach(function (range) {
    //             if (R >= range.s.r && R <= range.e.r && outRow.length >= range.s.c && outRow.length <= range.e.c) {
    //                 for (var i = 0; i <= range.e.c - range.s.c; ++i) outRow.push(null);
    //             }
    //         });
    //         if (rowspan) {
    //             ranges.push({s: {r: R, c: outRow.length}, e: {r: R + rowspan, c: outRow.length}});
    //         }
    //         if (colspan) {
    //             ranges.push({s: {r: R, c: outRow.length}, e: {r: R, c: outRow.length + colspan}});
    //         }
    //         outRow.push(cellValue !== "" ? cellValue : null);
    //         if (colspan) for (var k = 0; k < colspan - 1; ++k) outRow.push(null);
    //     }
    //     out.push(outRow);
    // }
    //
    //
    //
    //
    // var oo = [out, ranges];
    // var ranges = oo[1];
    // console.debug(ranges);
    // /* original data */
    // var wb = new Workbook(), ws = sheet_from_array_of_arrays(oo[0]);
    //
    // /* add ranges to worksheet */
    // ws['!merges'] = ranges;
    // /* add worksheet to workbook */
    // wb.SheetNames.push(title);
    // wb.Sheets[title] = ws;
    //
    // var wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: false, type: 'binary'});

    postMessage(new Blob([s2ab(wbout)], {type: "application/octet-stream"}));//灏嗚幏鍙栧埌鐨勬暟鎹彂閫佷細涓荤嚎绋�
};