/* 
 TERMS OF USE - EASING EQUATIONS
 Open source under the BSD License.
 Copyright 2016 Vahe Arakelyan All rights reserved.
 */


function office()
{
    return window === this ? new office : void 0
}
var filename, template, static;
office.prototype.save = {
    word: function (e)
    {
        if (e)
            if (e.filename || e.template)
            {
                filename = e.filename, template = document.querySelector(e.template)
                    .innerHTML, static = {
                    mhtml:
                        {
                            top: "Mime-Version: 1.0\nContent-Base: " + location.href + '\nContent-Type: Multipart/related; boundary="NEXT.ITEM-BOUNDARY";type="text/html"\n\n--NEXT.ITEM-BOUNDARY\nContent-Type: text/html; charset="utf-8"\nContent-Location: ' + location.href + "\n\n<!DOCTYPE html>\n<html>\n_html_</html>"
                            , head: '<head>\n<meta http-equiv="Content-Type" content="text/html; charset=utf-8">\n<style>\n_styles_\n</style>\n</head>\n'
                            , body: "<body>_body_</body>"
                        }
                };
                var t = {
                    maxWidth: 624
                }
                    , n = document.querySelector(e.template)
                    .cloneNode(!0);
                Array.prototype.forEach.call(n, function (e, t)
                {
                    var n = template;
                    ":hidden" === n && n.removeChild()
                });
                for (var a = Array(), l = null !== n.querySelector("img"), o = 0; o < l.length; o++)
                {
                    var r = Math.min(l[o].width, t.maxWidth)
                        , i = l[o].height * (r / l[o].width)
                        , m = document.createElement("CANVAS");
                    m.width = r, m.height = i;
                    var s = m.getContext("2d");
                    s.drawImage(l[o], 0, 0, r, i);
                    var c = m.toDataURL("image/png");
                    document.querySelector(l[o])
                        .setAttribute("src", l[o].src), l[o].width = r, l[o].height = i, a[o] = {
                        type: c.substring(c.indexOf(":") + 1, c.indexOf(";"))
                        , encoding: c.substring(c.indexOf(";") + 1, c.indexOf(","))
                        , location: document.querySelector(l[o])
                            .getAttribute("src")
                        , data: c.substring(c.indexOf(",") + 1)
                    }
                }
                for (var f = "\n", o = 0; o < a.length; o++) f += "--NEXT.ITEM-BOUNDARY\n", f += "Content-Location: " + a[o].location + "\n", f += "Content-Type: " + a[o].type + "\n", f += "Content-Transfer-Encoding: " + a[o].encoding + "\n\n", f += a[o].data + "\n\n";
                f += "--NEXT.ITEM-BOUNDARY--";
                var h = ""
                    , p = static.mhtml.top.replace("_html_", static.mhtml.head.replace("_styles_", h) + static.mhtml.body.replace("_body_", n.innerHTML)) + f
                    , d = new Blob([p]
                    , {
                        type: "application/msword;charset=utf-8"
                    });
                saveAs(d, filename + ".doc")
            }
            else console.error('Cannot get parameters::{"filename" & "template"} Office.js');
        else console.error("Please set argument:: Office.js")
    }
    , excel: function (e)
    {
        if (e)
            if (e.filename || e.template)
            {
                filename = e.filename, template = document.querySelector(e.template)
                    .innerHTML;
                var t = '<html xmlns:x="urn:schemas-microsoft-com:office:excel">';
                t += "<head><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>", t += "<x:Name>Test Sheet</x:Name>", t += "<x:WorksheetOptions><x:Panes></x:Panes></x:WorksheetOptions></x:ExcelWorksheet>", t += "</x:ExcelWorksheets></x:ExcelWorkbook></xml></head><body>", t += "<table border='1px'>", t += template, t += "</table></body></html>";
                var n = new Blob([t]
                    , {
                        type: "application/vnd.ms-excel"
                    });
                saveAs(n, filename + ".xls")
            }
            else console.error("vars");
        else console.error("Please set argument:: Office.js")
    }
    , html: function (e)
    {
        if (e)
            if (e.filename || e.template)
            {
                filename = e.filename, template = document.querySelector(e.template)
                    .innerHTML;
                var t = new Blob([template]
                    , {
                        type: "text/html"
                    });
                saveAs(t, filename + ".html")
            }
            else console.error('Cannot get parameters::{"filename" & "template"} Office.js');
        else console.error("Please set argument:: Office.js")
    }
    , txt: function (e)
    {
        if (e)
            if (e.filename || e.content)
            {
                filename = e.filename, template = e.content.replace(/ /g, "");
                var t = new Blob([template]
                    , {
                        type: "text/plain"
                    });
                saveAs(t, filename + ".txt")
            }
            else console.error('Cannot get parameters::{"filename" & "template"} Office.js');
        else console.error("Please set argument:: Office.js")
    }
};
