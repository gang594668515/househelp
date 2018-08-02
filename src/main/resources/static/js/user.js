
//逻辑删除某用户信息
function deleteUser(userId){
        $.ajax({
            async: false,
            type: 'POST',
            dataType: 'json',
            data:{'userId':userId},
            url: '/user/deleteUser',
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                console.log(XMLHttpRequest);
                console.log(textStatus);
                console.log(errorThrown);
            },
            success: function(response){
                if(response.rspCode == '000000'){
                    locationUrl('/userList','userList');
                    toastr.success('信息删除成功！', '操作成功');
                }else{
                    toastr.error(response.rspMsg, '操作失败');
                }
            }
        });
}


//逻辑恢复某用户信息
function recoverUser(userId){
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data:{'userId':userId},
        url: '/user/recoverUser',
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function(response){
            if(response.rspCode == '000000'){
                locationUrl('/userList','userList');
                toastr.success('信息恢复成功！', '操作成功');
            }else{
                toastr.error(response.rspMsg, '操作失败');
            }
        }
    });
}



