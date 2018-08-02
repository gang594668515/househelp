$(function () {
    $("#buyerError").hide();
});

//逻辑删除某用户信息
function deleteBuyer(buyerId) {
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data: {'buyerId': buyerId},
        url: '/buyer/deleteBuyer',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function (response) {
            if (response.rspCode == '000000') {
                locationUrl('/buyerList/s/s/0', 'buyerList');
                toastr.success('信息删除成功！', '操作成功');
            } else {
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}


//逻辑恢复某用户信息
function recoverBuyer(buyerId) {
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data: {'buyerId': buyerId},
        url: '/buyer/recoverBuyer',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function (response) {
            if (response.rspCode == '000000') {
                locationUrl('/buyerDeleteList/s/s/0', 'buyerDeleteList');
                toastr.success('信息恢复成功！', '操作成功');
            } else {
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}

function addBuyer() {
    var ok = $('#addBuyerForm').parsley().isValid({force: true});
    if (!ok) {
        return;
    }
    var url = '/buyer/addBuyer';
    $.ajax({
        async: false,
        url: url,
        data: 'corpName=' + $("#corpName").val() + '&userName=' + $("#userName").val()
        + '&phone=' + $("#phone").val() + '&email=' + $("#email").val() + '&zipCode=' + $("#zipCode").val()
        + '&address=' + $("#address").val() + '&qualityType=' + $("#qualityType").val(),
        type: 'POST',
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        },
        success: function (data, textStatus) {
            if (data.rspCode == '000000') {
                $("#buyerError").hide();
                $("#addBuyerForm")[0].reset();
                locationUrl('/addBuyer', 'addBuyer');
                toastr.success('产品购买商新增成功！', '操作成功')
            } else {
                $("#buyerError").show();
                $("#buyerError").html(data.rspMsg);
            }
        }
    });
}

function updateBuyer() {
    var ok = $('#updateBuyerForm').parsley().isValid({force: true});
    if (!ok) {
        return;
    }
    var url = '/buyer/updateBuyer';
    $.ajax({
        async: false,
        url: url,
        data: 'id=' + $("#id").val() + '&userName=' + $("#userName").val()
        + '&phone=' + $("#phone").val() + '&email=' + $("#email").val() + '&zipCode=' + $("#zipCode").val()
        + '&address=' + $("#address").val() + '&qualityType=' + $("#qualityType").val(),
        type: 'POST',
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        },
        success: function (data, textStatus) {
            if (data.rspCode == '000000') {
                $("#buyerError").hide();
                $("#updateBuyerForm")[0].reset();
                locationUrl('/buyerList/s/s/0','buyerList');
                toastr.success('产品购买商更新成功！', '操作成功')
            } else {
                $("#buyerError").show();
                $("#buyerError").html(data.rspMsg);
            }
        }
    });
}

function findBuyer(pageNum) {
    var corpName= $("#corpName").val();
    var userName = $("#userName").val();
    if(corpName==null || corpName==""){
        corpName="s";
    }
    if(userName==null || userName==""){
        userName="s";
    }
    locationUrl('/buyerList/'+corpName+'/'+userName+'/'+pageNum,'buyerList');
}

function findDeleteBuyer(pageNum) {
    var corpName= $("#corpName").val();
    var userName = $("#userName").val();
    if(corpName==null || corpName==""){
        corpName="s";
    }
    if(userName==null || userName==""){
        userName="s";
    }
    locationUrl('/buyerDeleteList/'+corpName+'/'+userName+'/'+pageNum,'buyerDeleteList');
}