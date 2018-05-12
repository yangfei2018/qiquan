$.app = {
    self: this, /**
     * Created by Administrator on 2017/5/18 0018.
     */

    ctx:'',
    ctxComm:'',
    pageFramework : {
      tab   :   "tab"
    },
    constant:{
        YES :   "1",
        NO  :   "0",
        ROOT_ORGANISATION   :   "1",                    //组织架构根节点
        feeType:{
            COMMISSION              :   "1",            //手续费
            OVERDUE                 :   "5",             //滞纳金
            RATE                    :   "14",             //利率
            MANAGEMENT              :   "15",             //管理费
            BREACH                  :   "16"             //违约金
        },
        feeCalcType:{
            RATIO       :   "1",            //按比例
            CONSTANT    :   "2"             //固定值
        },
        organType:{
            ORG         :       0,          //机构
            POST        :       1,          //岗位
            DEPARTMENT  :       2,          //部门
            AREA        :       3,          //区域
            BRANCH      :       4,          //分行
            GROUP       :       5           //组别
        },
        templateType:{
            FILE    :       "1",            //服务器文件
            SCRIPT  :       "2",             //脚本,
            URL :           "3"             //图片url
        },
        downloadType:{
            WORD    :   "word",
            EXCEL   :   "excel",
            PICTURE :   "picture"
        }
    },

    addTab:(window.parent == window) ? null : window.parent.$.app.addTab,

    isJson: function (str) {
        try {
            $.parseJSON(str);
            return true;
        } catch (e) {
            return false;
        }
    },

    getAbsUrl: function (url) {
        var ctx = $.trim($.app.ctx);
        url = $.trim(url);
        if (ctx == null || ctx == '' || url == null || url == '') {
            return url;
        } else {
            if (ctx.substr(ctx.length - 1, ctx.length - 1) != '/' && url.substr(0, 1) != '/') {
                ctx = ctx + '/'
            }
            return ctx + url;
        }
    },

    //费用单位
    getAmountSuffix: function(feeType, calcType){
        if (calcType == $.app.constant.feeCalcType.RATIO){
            if (feeType == $.app.constant.feeType.OVERDUE){
                return "‰";
            } else {
                return "%";
            }
        } else {
            return '元';
        }
    },


    getDictLabelByValue: function (dictList, value) {
        if ((value == null || value == "") && value != 0) {
            return '';
        }
        for (var i = 0; i < dictList.length; i++) {
            if (dictList[i].value == value) {
                return dictList[i].label;
            }
        }
    },

    progress: function(){
        $.messager.progress({
            interval:3000,
            msg: '正在获取数据，请稍候...'
        });
    },

    close:function(){
        $.messager.progress('close');
    },



    //提示信息
    showTip: function (msg, timeout, height) {
        if (timeout == undefined){
            timeout = 2000;
        }
        if (height == undefined){
            height = 100;
        }
        $.messager.show({
            title: '温馨提示',
            msg: msg,
            timeout: timeout,
            showType: 'slide',
            height : height
        });
    },

    initDateRangeSelect: function (targetSelect, targetBeginDate, targetEndDate, defaultRange) {
        var dateSelect = targetSelect.combobox({
            valueField: 'value',
            textField: 'label',
            editable: false,
            hasDownArrow: false,
            panelHeight: 'auto',
            width: 46,
            data: [{
                label: '请选择',
                value: ''
            }, {
                label: '今天',
                value: 'today'
            }, {
                label: '昨天',
                value: 'yesterday'
            }, {
                label: '前天',
                value: 'beforeYesterday'
            }, {
                label: '近3日',
                value: 'last3Days'
            }, {
                label: '近7日',
                value: 'last7Days'
            }, {
                label: '近30日',
                value: 'last30Days'
            }, {
                label: '近60日',
                value: 'last60Days'
            }, {
                label: '近90日',
                value: 'last90Days'
            }, {
                label: '本周',
                value: 'thisWeek'
            }, {
                label: '上周',
                value: 'lastWeek'
            }, {
                label: '本月',
                value: 'thisMonth'
            }, {
                label: '上月',
                value: 'lastMonth'
            }],

            onSelect: function (record) {
                var date;
                if (record.value == null || record.value == '') {
                    targetBeginDate.textbox('setValue', '');
                    targetEndDate.textbox('setValue', '');
                } else {
                    var beginDate, endDate;
                    switch (record.value) {
                        case 'today':
                            date = Date.today();
                            beginDate = date.toFormat("YYYY-MM-DD");
                            endDate = date.toFormat("YYYY-MM-DD");
                            break;
                        case 'yesterday':
                            date = Date.yesterday();
                            beginDate = date.toFormat("YYYY-MM-DD");
                            endDate = date.toFormat("YYYY-MM-DD");
                            break;
                        case 'beforeYesterday':
                            var date = Date.yesterday().add({"days": -1});
                            beginDate = date.toFormat("YYYY-MM-DD");
                            endDate = date.toFormat("YYYY-MM-DD");
                            break;
                        case 'last3Days':
                            beginDate = Date.today().add({"days": -2}).toFormat("YYYY-MM-DD");
                            endDate = Date.today().toFormat("YYYY-MM-DD");
                            break;
                        case 'last7Days':
                            beginDate = Date.today().add({"days": -6}).toFormat("YYYY-MM-DD");
                            endDate = Date.today().toFormat("YYYY-MM-DD");
                            break;
                        case 'last30Days':
                            beginDate = Date.today().add({"days": -29}).toFormat("YYYY-MM-DD");
                            endDate = Date.today().toFormat("YYYY-MM-DD");
                            break;
                        case 'last60Days':
                            beginDate = Date.today().add({"days": -59}).toFormat("YYYY-MM-DD");
                            endDate = Date.today().toFormat("YYYY-MM-DD");
                            break;
                        case 'last90Days':
                            beginDate = Date.today().add({"days": -89}).toFormat("YYYY-MM-DD");
                            endDate = Date.today().toFormat("YYYY-MM-DD");
                            break;
                        case 'thisWeek':
                            var dayInWeek = Date.today().getDay();
                            beginDate = Date.today().add({"days": -dayInWeek + 1}).toFormat("YYYY-MM-DD");
                            endDate = Date.today().add({"days": 7 - dayInWeek}).toFormat("YYYY-MM-DD");
                            break;
                        case 'lastWeek':
                            var dayInWeek = Date.today().getDay();
                            beginDate = Date.today().add({"days": -dayInWeek - 6}).toFormat("YYYY-MM-DD");
                            endDate = Date.today().add({"days": -dayInWeek}).toFormat("YYYY-MM-DD");
                            break;
                        case 'thisMonth':
                            var year = Date.today().getYear();
                            year += (year < 2000) ? 1900 : 0
                            var month = Date.today().getMonth();
                            beginDate = (new Date(year, month, 1)).toFormat("YYYY-MM-DD");
                            endDate = (new Date(year, month, Date.getDaysInMonth(year, month))).toFormat("YYYY-MM-DD");
                            break;
                        case 'lastMonth':
                            var year = Date.today().getYear();
                            year += (year < 2000) ? 1900 : 0
                            var month = Date.today().getMonth() - 1;

                            if (month < 0) {
                                month = 12 + month;
                                year--;
                            }
                            endDate = (new Date(year, month, Date.getDaysInMonth(year, month))).toFormat("YYYY-MM-DD");
                            beginDate = (new Date(year, month, 1)).toFormat("YYYY-MM-DD");

                            break;
                        default :
                            break;
                    }
                    targetBeginDate.textbox('setValue', beginDate + " 00:00:00");
                    targetEndDate.textbox('setValue', endDate + " 23:59:59");
                }
            }
        });
        if (defaultRange) {
            dateSelect.combobox('select', defaultRange);
        }
    },


    formatDate: function(val, fmt){
        if (val != null && val != "") {
            var date = new Date(val);
            var yyyy = date.getFullYear();
            var mm = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : ("0" + (date.getMonth() + 1));
            var dd = date.getDate() > 9 ? date.getDate() : ("0" + date.getDate());
            var hh = date.getHours() > 9 ? date.getHours() : ("0" + date.getHours());
            var mi = date.getMinutes() > 9 ? date.getMinutes() : ("0" + date.getMinutes());
            var ss = date.getSeconds() > 9 ? date.getSeconds() : ("0" + date.getSeconds());
            fmt = fmt == undefined ? "" : fmt.toLocaleLowerCase();
            if (fmt == "yyyy-mm-dd") {
                return yyyy + '-' + mm + '-' + dd;
            } else if (fmt == "yyyy-mm-dd hh:mi") {
                return yyyy + '-' + mm + '-' + dd + ' ' + hh + ':' + mi;
            } else if (fmt == "yyyy-mm-dd hh:mi:ss") {
                return yyyy + '-' + mm + '-' + dd + ' ' + hh + ':' + mi + ':' + ss;
            } else {
                return yyyy + '-' + mm + '-' + dd + ' ' + hh + ':' + mi + ':' + ss;
            }
        }
    },

     jsonTimeStamp: function(milliseconds) {
        if (milliseconds != "" && milliseconds != null
            && milliseconds != "null") {
            var datetime = new Date();
            datetime.setTime(milliseconds);
            var year = datetime.getFullYear();
            var month = datetime.getMonth() + 1 < 10 ? "0"
                + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
            var date = datetime.getDate() < 10 ? "0" + datetime.getDate()
                : datetime.getDate();
            var hour = datetime.getHours() < 10 ? "0" + datetime.getHours()
                : datetime.getHours();
            var minute = datetime.getMinutes() < 10 ? "0"
                + datetime.getMinutes() : datetime.getMinutes();
            var second = datetime.getSeconds() < 10 ? "0"
                + datetime.getSeconds() : datetime.getSeconds();
            return year + "-" + month + "-" + date + " " + hour + ":" + minute
                + ":" + second;
        } else {
            return "";
        }
    },


    /***
     * 打开对话框通用方法
     * @param url
     * @param dialogId
     * @param parent
     * @param onLoadResult
     */
    openDialog: function (url, dialogId, parent, onLoadResult) {
        var dialog = parent.find('#' + dialogId);

        if (dialog.length == 0) {
            $.messager.progress({
                msg: '加载中，请稍候...'
            });
            $.messager.progress({
                msg: '加载中，请稍候...'
            });
            $.ajax({
                type: "get",
                url: url,
                cache: true,
                dataType: "html",
                localCache: true,
                cacheTTL: 2,
                success: function (data) {
                    $.messager.progress('close');
                    parent.append(data);
                    dialog = parent.find('#' + dialogId);
                    if (dialog.length > 0) {

                        $.parser.parse(dialog);
                        onLoadResult(dialog, true);
                    } else {
                        dialog = parent.find("#" + $(data).attr("id"));
                        if (dialog.length > 0) {
                            $.parser.parse(dialog);
                        } else {
                            $.parser.parse("#" + $(data).attr("id"));
                        }
                        onLoadResult(dialog, false);
                    }
                }, error: function (msg) {
                    console.debug(msg);
                }
            });

        } else {
            onLoadResult(dialog);
        }
    },




    /**  * 打印表格  * @param no 模板编号  * @param data 模板数据  * @param isPreview 是否预览  */
    print: function (title, no, data, isPreview) {
        try {
            //data = $.parseJSON(data);
            data.title = title;

            $.app.getPrintTemplate(no, function (pt) {
                var result = laytpl(pt.content).render(data);
                var LODOP = $.app.getCLodop();
                LODOP.PRINT_INIT(title);
                LODOP.SET_PRINT_MODE("POS_BASEON_PAPER", true);
                LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED", 1);//横向时的正向显示
                LODOP.ADD_PRINT_TABLE("10mm", "10mm", "RightMargin:0mm", "BottomMargin:0mm", result);
                if (isPreview == false) {
                    LODOP.PRINT();
                } else {
                    LODOP.PREVIEW();
                }


            });
        } catch (e) {
            $.messager.alert('操作提示', '操作失败:打印内容渲染异常' + e.message, 'error');
        }
    },

    /** 
     * 获取打印模板 
     *  @param no 模板编号 
     *  @param successCallaback 成功回调函数 
     **/
    getPrintTemplate: function (no, successCallaback) {
        $.getJSON($.app.getAbsUrl("/credit/template/getByNo"), {"no": no}, function (data, status) {
            if (data.data.content) {
                successCallaback.call(this, data.data);
            } else {
                $.messager.alert('操作提示', '操作失败:未配置模板或模板格式错误', 'error');
            }
        });
    },
    getCLodop: function () {
        try {
            var LODOP = getCLodop();
            //console.debug(LODOP);
            LODOP.SET_LICENSES("","CA63C02B2E4548EBDAF1BDEE5E996B78","C94CEE276DB2187AE6B65D56B3FC2848","");

            return LODOP;
        } catch (e) {
            $.messager.alert('操作提示', '操作失败:请安装打印控件<br/> <br/><a href="http://help.my56cloud.com/hc/kb/article/148720/" target="_blank">【→打印控件下载←】</a>', 'error');
            return null;
        }
    },


     downloadFromTemplate : function( data, no, zip) {

         var url;
         if (zip){
             url = $.app.getAbsUrl("/credit/template/download/") + no + "/zip";
         } else {
             url = $.app.getAbsUrl("/credit/template/download/") + no;
         }
         $.app.ajaxForm(url, data);

         // $.app.getPrintTemplate(no, function(pt){
         //    if (pt.type == $.app.constant.templateType.FILE){
         //        var url;
         //        if (zip){
         //            url = $.app.getAbsUrl("/credit/template/download/") + pt.extension + "/zip?filePath=" + pt.content + "&templateName=" + pt.name;
         //        } else {
         //            url = $.app.getAbsUrl("/credit/template/download/") + pt.extension + "?filePath=" + pt.content + "&templateName=" + pt.name;
         //        }
         //        $.app.ajaxForm(url, data);
         //    } else if (pt.type == $.app.constant.templateType.URL){
         //        // var result = laytpl(pt.content).render(data);
         //        // $(data).html('');
         //        // $(data).append(result);
         //        // $.app.downWordLocal("test", data);
         //    }
         // });
     },
    downloadFromMap : function( data, no) {

        var url = $.app.getAbsUrl("/credit/template/downloadFromMap/") + no + "/zip";
        $.app.ajaxForm(url, data);
    },



    ajaxForm : function (url, param) {
        jQuery('<form id="' + $.app.getUuid() + '"action="'+url+'" method="'+'post'+'">' +  // action请求路径及推送方法
            '<input type="hidden" name="formParam" value="'+param+'"/>' + // 数据
            '</form>')
            .appendTo('body').submit().remove();
    },

    downWordLocal: function (fileName, template) {
        office().save.word({
            filename: fileName,
            template: template
        });
    },

    fillOutPrint: function (data, no) {
        $.app.getPrintTemplate(no, function (template) {
            var result = laytpl(template.content).render(data);
            var LODOP = $.app.getCLodop();
            LODOP.PRINT_INIT("套打");
            LODOP.SET_PRINT_MODE("POS_BASEON_PAPER", true);
            eval(result);
            LODOP.PRINT();
        });
    },

    /**
     * 生成uuid
     * @returns {string}
     */
    getUuid: function () {
        function s4() {
            return Math.floor((1 + Math.random()) * 0x10000)
                .toString(16)
                .substring(1);
        }

        return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
            s4() + '-' + s4() + s4() + s4();
    },






    /***
     * 导出系列
     */
    export: function (title, datagrid, options) {
        /**options 说明
         options.seq   true 有序号  false 无序号

         options.specifiedColumns  Array 不想全部列都显示的话，特殊指定显示列
         例：['fromCustomerAccount', 'fromCustomerHolder', 'codSettle', {'title' : '标识符', 'value' : '|'}, {'title' : '备注', 'value' : '{{d.remarks}}'}]
         Array中的每一项，如果是grid包含的，则直接是field名，如fromCustomerAccount
         如果是grid不包含的，比如'标识符'这种字段，则传入结构体 {'title' : '标识符', 'value' : '|'},或有占位符的 {'title' : '备注', 'value' : '{{d.remarks}}'}

         options.isSelections  true 下载选中的    false 下载整个table的

         options.addFooterRows true 有foot  false 无foot
         **/

        layer.load(0, {
            shade: [0.4, '#ccc']
        });
        if (!options) {
            options = {};
        }
        var data = new Array();
        var columns = new Array();
        // if (datagrid.datagrid('options').frozenColumns[0] && datagrid.datagrid('options').frozenColumns[0].length > 0) {
        //     columns = columns.concat(datagrid.datagrid('options').frozenColumns[0]);
        // }
        // if (datagrid.datagrid('options').columns[0] && datagrid.datagrid('options').columns[0].length > 0) {
        //     columns = columns.concat(datagrid.datagrid('options').columns[0]);
        // }
        var titleRow = [];
        //是否需要序号
        if (options.seq) {
            titleRow.push('序号');
        }
        datagrid.find("thead tr th").each(function(i){
            var obj = $(this).attr("attrid");
            if("opt" != obj){
                console.log("come:" + $(this).html());
                columns.push($(this).html());
                titleRow.push($(this).html());
            }
        });
        console.log(titleRow);
        data.push(titleRow);

        var rows;
        if (options.isSelections) {
            rows = datagrid.datagrid('getSelections');
        } else {
            rows = datagrid.find('tbody tr');
        }
        rows.each(function(j){
            var r = new Array();
            if (options.seq) {
                r.push(j + 1);
            }
            $(this).find('td').each(function(i){
                var obj = $(this).attr("attrid");
                if("opt" != obj){
                    var val = $(this).html().replace(/<[^>]+>/g, "");
                    r.push(val);
                }
            });
            data.push(r);
        });
        console.log(data);
        layer.closeAll('loading');
        var excelWorker = new Worker("/static/export/excelWorker.js");
        var postData = {title: title, data: data};//, columnOptions: columnOptions
        excelWorker.postMessage(postData);      //向worker发送数据
        excelWorker.onmessage = function (evt) {//接收worker传过来的数据函数
            layer.closeAll('loading');
            saveAs(evt.data, title + ".xlsx");
        };

    },



}




/**
 * 创建图片查看器
 */
function ImageSelector(target, title, list, showCheck, height , url){
    var self = this;
    this.target = target;
    this._id = target.attr("id");
    this.datasource = list;
    this.showCheck = showCheck;
    this.selector;
    this.selectorSearchBtn;
    this.selectorSearchInput;
    this.inline;
    this.height = height == undefined ? '300px' : height;


    this._append = function (item) {
        var append = "<span style='padding-right:5px;float: left'><ul style='text-align: center'>" +
            "<li><img  src='" + item.url + "' style='width: 100px;height: 50px;' alt='" + item.name + "'></li>" +
            /*"<li><span>" + item.name + "</span></li>" +*/
            "<li><span>" + item.createdDate + "</span></li>";
        if (this.showCheck){
            append += "<li><input type='checkbox' name='checkbox' class='_seletor_check_" + self._id + "' value='" + item.url + "' data-imgId='" + item.id + "'>选择</input></li>";

        }
        append += "</ul></span>";

        return append;
    }

    var html = "<div class='tabTitle c mt20'> <h3><span class='h3bgl'>&nbsp;</span><span class='h3bgz'>" + title + "</span><span class='h3bgr'>&nbsp;</span> </h3> </div>" +
        "<div><input type='text' id='_seletor_input_" + self._id + "'><input type='button' id='_seletor_search_" + self._id + "' value='搜索'> " +
        "<input type='button' id='_seletor_pop_" + self._id + "' value='弹框显示'><input type='button' id='_seletor_Delete_" + self._id + "' class='cancel' value='删除' style='display: none;'></div>" +
        "<div style='height:" + self.height + "'><div id='_seletor_" + self._id + "'>";

    $.each(this.datasource, function (i, item) {
        html += self._append(item);
    })
    html += "</div></div>";


    target.append(html);
    // target.height("300px");

    if (this.showCheck){

        $(".cancel").css('display','block');

    }
    // target.height("300px");

    this.selector = $('#_seletor_' + self._id);
    this.selectorSearchBtn = $('#_seletor_search_' + self._id);
    this.selectorSearchInput = $('#_seletor_input_' + self._id);
    this.selectorPopBtn = $('#_seletor_pop_' + self._id);
    this.selectorDeleteBtn = $('#_seletor_Delete_' + self._id);
    this.selectorCheck = $('#_seletor_check_' + self._id);
    this.selectorSearchDialog = null;

    this.selectorSearchBtn.on('click', function () {
        self.query(self.selectorSearchInput.val());
    });

    self.selectorSearchInput.keydown(function (e) {
        if (e.keyCode == 13) {
            self.selectorSearchBtn.click();
        }
    });

    this.selectorDeleteBtn.off('click').on('click', function (){
        var codes = [];
        var imgIds = [];
        $("input[type=checkbox]:checked").each(function(){ //遍历table里的全部checkbox
            if($(this).attr("checked")) //如果被选中
                codes.push($(this).val());
                imgIds.push($(this).attr('data-imgId'));
        });
        if(imgIds.length){
            $.post(url, {codes: codes + '',imgIds: imgIds + ''}, function (data) {
                if (!data.success) {
                    $.messager.alert('操作提示', data.message, 'info');
                } else {
                    $("input[type=checkbox]:checked").parent().parent().parent().remove();
                    $.app.showTip("操作成功！");
                }
            }, "json");
        }
    });


    this.selectorPopBtn.on('click', function () {
        var dialogHtml = "<div id='_seletor_dialog_" + self._id + "'" +
        "data-options='resizable:false,modal:false,maximizable:true,minimizable:true'></div>";
        self.selectorSearchInput.append(dialogHtml);
        self.selectorSearchDialog = $('#_seletor_dialog_' + self._id);
        self.selectorSearchDialog.dialog({
            width: '80%',
            height: '100%',
            title: title,
            onClose: function (forceDestroy) {
                $(this).dialog('destroy');
            }
        });
        $.parser.onComplete = function () {};
        $.parser.parse(self.selectorSearchInput);

        var imageSelector = new ImageSelector(self.selectorSearchDialog, '附件', self.datasource, false, self.selectorSearchDialog.dialog('options').width);
        imageSelector.show(true);

    });


    this.show = function(inline) {
        if (inline == undefined){
            inline = false;
        }
        self.selector.viewer({
            navbar: 1,
            zIndexInline:99999999999999999,
            inline: inline
        });
        self.inline = inline;
    }



    this.query = function (keyword) {
        var _html="";

        $.each(self.datasource, function (i, item) {
            if (item.name.indexOf(keyword) != -1){
                _html += self._append(item);
            }
        })

        self.selector.html(_html);
        self.selector.viewer('destroy');

        if (_html != ""){
            self.show(self.inline);
        }
    }



    this.getSelections = function () {
        if (this.showCheck){
            var selections = new Array();
            var checks = $('._seletor_check_' + self._id);
            $.each(checks, function (i, item) {
                if (item.checked){
                    selections.push(item.value);
                }
            })
            return selections;
        } else {
            return new Array();
        }
    }
}






/**
 * 可编辑datagrid类
 * @param datagrid
 * @constructor
 */
function EditDatagrid(datagrid) {
    var self = this;
    this.editIndex = undefined;
    this.datagrid = datagrid;

    this.endEditing = function () {
        if (self.editIndex == undefined) {
            return true
        }
        if (self.datagrid.datagrid('validateRow', self.editIndex)) {
            self.datagrid.datagrid('endEdit', self.editIndex);
            self.editIndex = undefined;
            return true;
        } else {
            return false;
        }
    };

    this.onClickCell = function (index, field) {
        if (self.editIndex != index) {
            if (self.endEditing()) {
                self.datagrid.datagrid('selectRow', index).datagrid('beginEdit', index);
                var ed = self.datagrid.datagrid('getEditor', {index: index, field: field});
                if (ed) {
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
                self.editIndex = index;
            } else {
                setTimeout(function () {
                    self.datagrid.datagrid('selectRow', self.editIndex);
                }, 0);
            }
        }
    };

    this.append = function (row) {
        if (self.endEditing()) {
            if (row) {
                self.datagrid.datagrid('appendRow', row);
            } else {
                self.datagrid.datagrid('appendRow', {});
            }
            self.editIndex = self.datagrid.datagrid('getRows').length - 1;
            self.datagrid.datagrid('selectRow', self.editIndex).datagrid('beginEdit', self.editIndex);
        }
    };

    this.removeit = function () {
        if (self.editIndex == undefined) {
            return;
        }
        self.datagrid.datagrid('cancelEdit', self.editIndex).datagrid('deleteRow', self.editIndex);
        self.editIndex = undefined;
    };

    this.accept = function () {
        if (self.endEditing()) {
            self.datagrid.datagrid('acceptChanges');
        }
    };

    this.insert = function (index, row) {
        if (self.endEditing()) {
            if (row) {
                self.datagrid.datagrid('insertRow', {index : index, row : row});
            } else {
                self.datagrid.datagrid('insertRow', {index : index, row : {}});
            }
            self.editIndex = index;
            self.datagrid.datagrid('selectRow', self.editIndex).datagrid('beginEdit', self.editIndex);
        }
    };

    this.reject = function () {
        self.datagrid.datagrid('rejectChanges');
        self.editIndex = undefined;
    };
}



$.fn.serializeNestedObject = function() {
    var json = {};
    var arrObj = this.serializeArray();
    //alert(JSON.stringify(arrObj));
    $.each(arrObj, function() {
        // 对重复的name属性，会将对应的众多值存储成json数组
        if (json[this.name]) {
            if (!json[this.name].push) {
                json[this.name] = [ json[this.name] ];
            }
            json[this.name].push(this.value || '');
        } else {
            // 有嵌套的属性，用'.'分隔的
            if (this.name.indexOf('.') > -1) {
                var pos = this.name.indexOf('.');
                var key =  this.name.substring(0, pos);
                // 判断此key是否已存在json数据中，不存在则新建一个对象出来
                if(!existKeyInJSON(key, json)){
                    json[key] = {};
                }
                var subKey = this.name.substring(pos + 1);
                json[key][subKey] = this.value || '';
            }
            // 普通属性
            else{
                json[this.name] = this.value || '';
            }

        }
    });

    // 处理那些值应该属于数组的元素，即带'[number]'的key-value对
    var resultJson = {};
    for(var key in json){
        // 数组元素
        if(key.indexOf('[') > -1){
            var pos = key.indexOf('[');
            var realKey =  key.substring(0, pos);
            // 判断此key是否已存在json数据中，不存在则新建一个数组出来
            if(!existKeyInJSON(realKey, resultJson)){
                resultJson[realKey] = [];
            }
            resultJson[realKey].push(json[key]);

        }
        else{ // 单元素
            resultJson[key] = json[key];
        }
    }
    return resultJson;
};

/**
 * 功能：判断key在Json结构中是否存在
 * 存在，返回true; 否则，返回false.
 */
function existKeyInJSON(key, json){
    if(key == null || key == '' || $.isEmptyObject(json)){
        return false;
    }
    var exist = false;
    for(var k in json){
        if(key === k){
            exist = true;
        }
    }
    return exist;
}


