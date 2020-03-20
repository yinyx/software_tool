function sendFile() {
    var fileCatcher = document.getElementById("softwareForm");
    var file_icon = document.getElementById("icon");
    var file_soft = document.getElementById("soft");

    fileCatcher.addEventListener("submit",function (event) {
        event.preventDefault();
        sendFile();
        alert("添加成功");
        window.location.reload();
    });

    sendFile = function () {
        var formData = new FormData();
        var request = new XMLHttpRequest();
		formData.append("icon",file_icon.files[0]);
        formData.append("soft",file_soft.files[0]);
        var name = $(":input[name='softwareName']").val();
        var nameEn = $(":input[name='softwareName_en']").val();
        var introduction = $(":input[name='description']").val();
        var latestVersion = $(":input[name='version']").val();
        var kind = $(":radio[name=\"type\"]:checked").val();
        var data = {
            "name":name,
            "nameEn":nameEn,
            "introduction":introduction,
            "latestVersion":latestVersion,
            "kind":kind
        };
        formData.append("softwareForm",JSON.stringify(data));

        request.open("POST","software/uploadSoftware");
        request.send(formData);
    };
}

function showKind(){
    $.ajax({
        url:"softwareKind/queryAllKinds",
        type:"post",
        dataType: "text",
        contentType: "application/json",
        success:function (data) {
            data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
            if(data.status == "success")
            {
                for (var i = 0; i < data.dataList.length; i++) {
                    var html = "";
                    console.log(data.dataList[i].id);
                    html += '<label class="control-label">';
                    html += '<input type="radio" name="type" id="type" value= "'+data.dataList[i].id+'" >' +data.dataList[i].kind_name;
                    html += '</label>';
                    $("#type_radio").append(html);
                }
            }else{
                showSuccessOrErrorModal(data.msg,"error");
            }
        }
    })

}

$(document).ready(function () {
    //判断是否登录
    userMap = isLogined();
    if(userMap){//成功登录
        userId = userMap.id;
    }else{
        //parent.location.href = jQuery.getBasePath() + "/login.html";
    }
    showKind();
    sendFile();
});
