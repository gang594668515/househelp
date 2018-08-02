$(function () {
    $("#supplierError").hide();
});

//逻辑删除某用户信息
function deleteSupplier(supplierId) {
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data: {'supplierId': supplierId},
        url: '/supplier/deleteSupplier',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function (response) {
            if (response.rspCode == '000000') {
                locationUrl('/supplierList/s/s/0', 'supplierList');
                toastr.success('信息删除成功！', '操作成功');
            } else {
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}


//逻辑恢复某用户信息
function recoverSupplier(supplierId) {
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data: {'supplierId': supplierId},
        url: '/supplier/recoverSupplier',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function (response) {
            if (response.rspCode == '000000') {
                locationUrl('/supplierDeleteList/s/s/0', 'supplierDeleteList');
                toastr.success('信息恢复成功！', '操作成功');
            } else {
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}

function addSupplier() {
    var ok = $('#addSupplierForm').parsley().isValid({force: true});
    if (!ok) {
        return;
    }
    var url = '/supplier/addSupplier';
    $.ajax({
        async: false,
        url: url,
        data: 'corpName=' + $("#corpName").val() + '&userName=' + $("#userName").val()
        + '&phone=' + $("#phone").val() + '&email=' + $("#email").val() + '&address='
        + $("#address").val() + '&payMethod=' + $("#payMethod").val()
        + '&account=' + $("#account").val(),
        type: 'POST',
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        },
        success: function (data, textStatus) {
            if (data.rspCode == '000000') {
                $("#supplierError").hide();
                $("#addSupplierForm")[0].reset();
                locationUrl('/addSupplier', 'addSupplier');
                toastr.success('原料供应商新增成功！', '操作成功')
            } else {
                $("#supplierError").show();
                $("#supplierError").html(data.rspMsg);
            }
        }
    });
}

function updateSupplier() {
    var ok = $('#updateSupplierForm').parsley().isValid({force: true});
    if (!ok) {
        return;
    }
    var url = '/supplier/updateSupplier';
    $.ajax({
        async: false,
        url: url,
        data: 'id=' + $("#id").val() + '&userName=' + $("#userName").val()
        + '&phone=' + $("#phone").val() + '&email=' + $("#email").val() + '&address='
        + $("#address").val() + '&payMethod=' + $("#payMethod").val()
        + '&account=' + $("#account").val(),
        type: 'POST',
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        },
        success: function (data, textStatus) {
            if (data.rspCode == '000000') {
                $("#supplierError").hide();
                $("#updateSupplierForm")[0].reset();
                locationUrl('/supplierList/s/s/0','supplierList');
                toastr.success('原料供应商更新成功！', '操作成功')
            } else {
                $("#supplierError").show();
                $("#supplierError").html(data.rspMsg);
            }
        }
    });
}

function findSupplier(pageNum) {
    var corpName= $("#corpName").val();
    var userName = $("#userName").val();
    if(corpName==null || corpName==""){
        corpName="s";
    }
    if(userName==null || userName==""){
        userName="s";
    }
    locationUrl('/supplierList/'+corpName+'/'+userName+'/'+pageNum,'supplierList');
}

function findDeleteSupplier(pageNum) {
    var corpName= $("#corpName").val();
    var userName = $("#userName").val();
    if(corpName==null || corpName==""){
        corpName="s";
    }
    if(userName==null || userName==""){
        userName="s";
    }
    locationUrl('/supplierDeleteList/'+corpName+'/'+userName+'/'+pageNum,'supplierDeleteList');
}