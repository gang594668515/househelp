$(function () {
    $("#saleOrderError").hide();
});

//逻辑删除某用户信息
function deleteSaleOrder(saleOrderId) {
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data: {'saleOrderId': saleOrderId},
        url: '/saleOrder/deleteSaleOrder',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function (response) {
            if (response.rspCode == '000000') {
                locationUrl('/saleOrderList/s/s/s/0', 'saleOrderList');
                toastr.success('信息删除成功！', '操作成功');
            } else {
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}


//逻辑恢复某用户信息
function recoverSaleOrder(saleOrderId) {
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data: {'saleOrderId': saleOrderId},
        url: '/saleOrder/recoverSaleOrder',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function (response) {
            if (response.rspCode == '000000') {
                locationUrl('/saleOrderDeleteList/s/s/s/0', 'saleOrderDeleteList');
                toastr.success('信息恢复成功！', '操作成功');
            } else {
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}

function addSaleOrder() {
    var ok = $('#addSaleOrderForm').parsley().isValid({force: true});
    if (!ok) {
        return;
    }
    var url = '/saleOrder/addSaleOrder';
    $.ajax({
        async: false,
        url: url,
        data: 'productId=' + $("#productId").val() + '&buyerId=' + $("#buyerId").val() + '&number=' + $("#number").val()
        + '&unitPrice=' + $("#unitPrice").val() + '&totalPrice=' + $("#totalPrice").val(),
        type: 'POST',
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        },
        success: function (data, textStatus) {
            if (data.rspCode == '000000') {
                $("#saleOrderError").hide();
                $("#addSaleOrderForm")[0].reset();
                locationUrl('/addSaleOrder', 'addSaleOrder');
                toastr.success('销售单新增成功！', '操作成功')
            } else {
                $("#saleOrderError").show();
                $("#saleOrderError").html(data.rspMsg);
            }
        }
    });
}

function findSaleOrder(pageNum) {
    var name = $("#name").val();
    var model = $("#model").val();
    var corpName = $("#corpName").val();
    if (name == null || name == "") {
        name = "s";
    }
    if (model == null || model == "") {
        model = "s";
    }
    if (corpName == null || corpName == "") {
        corpName = "s";
    }
    locationUrl('/saleOrderList/' + name + '/' + model + '/' + corpName + '/' + pageNum, 'saleOrderList');
}

function findDeleteSaleOrder(pageNum) {
    var name = $("#name").val();
    var model = $("#model").val();
    var corpName = $("#corpName").val();
    if (name == null || name == "") {
        name = "s";
    }
    if (model == null || model == "") {
        model = "s";
    }
    if (corpName == null || corpName == "") {
        corpName = "s";
    }
    locationUrl('/saleOrderDeleteList/' + name + '/' + model + '/' + corpName + '/' + pageNum, 'saleOrderDeleteList');
}

function checkNum(num) {
    var num = num.value;
    var reg1 = /^([+]?)\d*\.?\d+$/; //验证数字
    var reg2 = /^\d+(?:\.\d{1,2})?$/ //验证两位小数，小数可有可无，如果有最多两位

    if (!reg1.test(num)) {
        toastr.error("请输入数字", '操作失败');
        $("#number").val(null);
        $("#totalPrice").val(null);
    } else if (!reg2.test(num)) {
        toastr.error("请输入两位小数", '操作失败');
        $("#number").val(null);
        $("#totalPrice").val(null);
    }
}

function selectCorpName() {
    var url = "/buyer/selectCorpName";
    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        dataType: "json",
        data: 'corpName=' + $("#corpName").val(),
        success: function (data) {
            var items = data;
            $("#modal-buyerList").find("table tbody tr").remove();
            for (var i = 0; i < items.length; i++) {
                var sTr = "<tr>" + "<td>" + items[i][0] + "</td>" + "<td>" + items[i][1] + "</td>"
                    + "<td><a href='javascript:;' onclick=\"getCorpName('" + items[i][0] + "','" + items[i][1] + "');\">选择</a></td>" + "</tr>";
                $("#modal-buyerList").find("table tbody").append(sTr);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        }
    });
    $("#modal-buyerList").modal();
}

function getCorpName(id, corpName) {
    $("#corpName").val(corpName);
    $("#buyerId").val(id);
    $('#modal-buyerList').modal('hide');
}

function getTotalPrice() {
    var number = $("#number").val();
    var unitPrice = $("#unitPrice").val();
    if (number == null || number == "") {
        toastr.error("数量不能为空", '操作失败');
        return;
    }
    if (unitPrice == null || unitPrice == "") {
        toastr.error("单价不能为空", '操作失败');
        return;
    }
    var totalPrice = number * unitPrice;
    $("#totalPrice").val(totalPrice);
}

function selectProductName() {
    var url = "/product/selectProductName";
    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        dataType: "json",
        data: 'name=' + $("#productName").val(),
        success: function (data) {
            var items = data;
            $("#modal-productList").find("table tbody tr").remove();
            for (var i = 0; i < items.length; i++) {
                var sTr = "<tr>" + "<td>" + items[i][0] + "</td>" + "<td>" + items[i][1] + "</td>" + "<td>" + items[i][2] + "</td>"
                    + "<td>" + items[i][3] + "</td>" + "<td>" + items[i][4] + "</td>" + "<td>" + items[i][5] + "</td>"
                    + "<td><a href='javascript:;' onclick=\"getProductName('" + items[i][0] + "','" + items[i][1] + "','" + items[i][2] + "','" + items[i][3] + "','" + items[i][5] + "');\">选择</a></td>" + "</tr>";
                $("#modal-productList").find("table tbody").append(sTr);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        }
    });
    $("#modal-productList").modal();
}

function getProductName(id, name, model, number, unitPrice) {
    $("#productName").val(name);
    $("#productId").val(id);
    $("#model").val(model);
    $("#leftNumber").val(number);
    $("#unitPrice").val(unitPrice);

    $('#modal-productList').modal('hide');
}