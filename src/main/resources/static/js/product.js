$(function () {
    $("#productError").hide();
});

//逻辑删除某用户信息
function deleteProduct(productId) {
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data: {'productId': productId},
        url: '/product/deleteProduct',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function (response) {
            if (response.rspCode == '000000') {
                locationUrl('/productList/s/s/0', 'productList');
                toastr.success('信息删除成功！', '操作成功');
            } else {
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}


//逻辑恢复某用户信息
function recoverProduct(productId) {
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data: {'productId': productId},
        url: '/product/recoverProduct',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function (response) {
            if (response.rspCode == '000000') {
                locationUrl('/productDeleteList/s/s/0', 'productDeleteList');
                toastr.success('信息恢复成功！', '操作成功');
            } else {
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}

function addProduct() {
    var ok = $('#addProductForm').parsley().isValid({force: true});
    if (!ok) {
        return;
    }
    var url = '/product/addProduct';
    $.ajax({
        async: false,
        url: url,
        data: 'name=' + $("#name").val() + '&model=' + $("#model").val()
        + '&number=' + $("#number").val() + '&unit=' + $("#unit").val() + '&unitPrice=' + $("#unitPrice").val(),
        type: 'POST',
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        },
        success: function (data, textStatus) {
            if (data.rspCode == '000000') {
                $("#productError").hide();
                $("#addProductForm")[0].reset();
                locationUrl('/addProduct', 'addProduct');
                toastr.success('产品新增成功！', '操作成功')
            } else {
                $("#productError").show();
                $("#productError").html(data.rspMsg);
            }
        }
    });
}

function updateProduct() {
    var ok = $('#updateProductForm').parsley().isValid({force: true});
    if (!ok) {
        return;
    }
    var url = '/product/updateProduct';
    $.ajax({
        async: false,
        url: url,
        data: 'id=' + $("#id").val()
        + '&number=' + $("#number").val() + '&unit=' + $("#unit").val() + '&unitPrice=' + $("#unitPrice").val(),
        type: 'POST',
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        },
        success: function (data, textStatus) {
            if (data.rspCode == '000000') {
                $("#productError").hide();
                $("#updateProductForm")[0].reset();
                locationUrl('/productList/s/s/0', 'productList');
                toastr.success('产品更新成功！', '操作成功')
            } else {
                $("#productError").show();
                $("#productError").html(data.rspMsg);
            }
        }
    });
}

function findProduct(pageNum) {
    var name = $("#name").val();
    var model = $("#model").val();
    if (name == null || name == "") {
        name = "s";
    }
    if (model == null || model == "") {
        model = "s";
    }
    locationUrl('/productList/' + name + '/' + model + '/' + pageNum, 'productList');
}

function findDeleteProduct(pageNum) {
    var name = $("#name").val();
    var model = $("#model").val();
    if (name == null || name == "") {
        name = "s";
    }
    if (model == null || model == "") {
        model = "s";
    }
    locationUrl('/productDeleteList/' + name + '/' + model +  '/' + pageNum, 'productDeleteList');
}

function checkNum(num) {
    var num = num.value;
    var reg1 = /^([+]?)\d*\.?\d+$/; //验证数字
    var reg2 = /^\d+(?:\.\d{1,2})?$/ //验证两位小数，小数可有可无，如果有最多两位

    if (!reg1.test(num)) {
        toastr.error("请输入数字", '操作失败');
        $("#number").val(null);
        $("#unitPrice").val(null);
    } else if (!reg2.test(num)) {
        toastr.error("请输入两位小数", '操作失败');
        $("#number").val(null);
        $("#unitPrice").val(null);
    }
}

