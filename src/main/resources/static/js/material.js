$(function () {
    $("#materialError").hide();
});

//逻辑删除某用户信息
function deleteMaterial(materialId) {
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data: {'materialId': materialId},
        url: '/material/deleteMaterial',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function (response) {
            if (response.rspCode == '000000') {
                locationUrl('/materialList/s/s/s/0', 'materialList');
                toastr.success('信息删除成功！', '操作成功');
            } else {
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}


//逻辑恢复某用户信息
function recoverMaterial(materialId) {
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data: {'materialId': materialId},
        url: '/material/recoverMaterial',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function (response) {
            if (response.rspCode == '000000') {
                locationUrl('/materialDeleteList/s/s/s/0', 'materialDeleteList');
                toastr.success('信息恢复成功！', '操作成功');
            } else {
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}

function addMaterial() {
    var ok = $('#addMaterialForm').parsley().isValid({force: true});
    if (!ok) {
        return;
    }
    var url = '/material/addMaterial';
    $.ajax({
        async: false,
        url: url,
        data: 'supplierId=' + $("#supplierId").val() + '&name=' + $("#name").val() + '&model=' + $("#model").val()
        + '&number=' + $("#number").val() + '&unit=' + $("#unit").val() + '&unitPrice=' + $("#unitPrice").val()
        + '&totalPrice=' + $("#totalPrice").val(),
        type: 'POST',
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        },
        success: function (data, textStatus) {
            if (data.rspCode == '000000') {
                $("#materialError").hide();
                $("#addMaterialForm")[0].reset();
                locationUrl('/addMaterial', 'addMaterial');
                toastr.success('原料采购单新增成功！', '操作成功')
            } else {
                $("#materialError").show();
                $("#materialError").html(data.rspMsg);
            }
        }
    });
}

function updateMaterial() {
    var ok = $('#updateMaterialForm').parsley().isValid({force: true});
    if (!ok) {
        return;
    }
    var url = '/material/updateMaterial';
    $.ajax({
        async: false,
        url: url,
        data: 'id=' + $("#id").val() + '&supplierId=' + $("#supplierId").val()
        + '&number=' + $("#number").val() + '&unit=' + $("#unit").val() + '&unitPrice=' + $("#unitPrice").val()
        + '&totalPrice=' + $("#totalPrice").val(),
        type: 'POST',
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        },
        success: function (data, textStatus) {
            if (data.rspCode == '000000') {
                $("#materialError").hide();
                $("#updateMaterialForm")[0].reset();
                locationUrl('/materialList/s/s/s/0', 'materialList');
                toastr.success('原料采购单更新成功！', '操作成功')
            } else {
                $("#materialError").show();
                $("#materialError").html(data.rspMsg);
            }
        }
    });
}

function findMaterial(pageNum) {
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
    locationUrl('/materialList/' + name + '/' + model + '/'  + corpName + '/' + pageNum, 'materialList');
}

function findDeleteMaterial(pageNum) {
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
    locationUrl('/materialDeleteList/' + name + '/' + model + '/'  + corpName + '/' + pageNum, 'materialDeleteList');
}

function checkNum(num) {
    var num = num.value;
    var reg1 = /^([+]?)\d*\.?\d+$/; //验证数字
    var reg2 = /^\d+(?:\.\d{1,2})?$/ //验证两位小数，小数可有可无，如果有最多两位

    if (!reg1.test(num)) {
        toastr.error("请输入数字", '操作失败');
        $("#number").val(null);
        $("#unitPrice").val(null);
        $("#totalPrice").val(null);
    } else if (!reg2.test(num)) {
        toastr.error("请输入两位小数", '操作失败');
        $("#number").val(null);
        $("#unitPrice").val(null);
        $("#totalPrice").val(null);
    }
}

function selectCorpName() {
    var url = "/supplier/selectCorpName";
    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        dataType: "json",
        data: 'corpName=' + $("#corpName").val(),
        success: function (data) {
            var items = data;
            $("#modal-supplierList").find("table tbody tr").remove();
            for (var i = 0; i < items.length; i++) {
                var sTr = "<tr>" + "<td>" + items[i] + "</td>" + "<td><a href='javascript:;' onclick=\"getCorpName('" + items[i] + "');\">选择</a></td>" + "</tr>";
                $("#modal-supplierList").find("table tbody").append(sTr);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        }
    });
    $("#modal-supplierList").modal();
}

function getCorpName(corpName) {
    var url = "/supplier/getId";
    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        dataType: "json",
        data: 'corpName=' + corpName,
        success: function (data) {
            $("#corpName").val(corpName);
            $("#supplierId").val(data);
            $('#modal-supplierList').modal('hide');
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        }
    });
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

