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
        var introduction = $(":input[name='introduction']").val();
        var latestVersion = $(":input[name='version']").val();
        var kind = $(":input[name='type']").val();
        var data = {
            "name":name,
            "nameEn":nameEn,
            "introduction":introduction,
            "latestVersion":latestVersion,
            "kind":kind
        };
        formData.append("softwareForm",JSON.stringify(data));

        request.open("POST","uploadSoftware");
        request.send(formData);
    };
}
$(document).ready(function () {
    sendFile();
});
