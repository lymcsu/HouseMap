$(document).ready(function () {
    $("#gobtn").on("click", searchseller);
    $(document).on("click",'.gobtn',function(){
       var location = $(this).attr("name").split("_");
       map.centerAndZoom(new BMap.Point(location[0],location[1]), 20);
       $("#searchdiv").fadeOut();
       $("#model").css("visibility","hidden");
    });
});

function searchseller() {
    var keyword = $("#keyinput").val();
    var type = $("#typeslc option:selected").attr("value");
    var location = $("#locaslc option:selected").attr("value");
    $.ajax({
        url: "SearchServer",
        type: "GET",
        data: {
            keyword: keyword,
            type: type,
            location: location
        },
        success: function (data) {
            $("#disp ul").html("");
            if (data === "") {
                $("#disp ul").html("<div style='position:absolute;top:50%;left:50%;margin-left:-150px;margin-top:-150px'><img src='images/error_1.jpg' style='height:200px;width:200px'/><br/><b>sorry,没有找到结果</b></div>");
                return; 
            }
            var array = data.split("_");
            var i = 0;
            while (array[i] != "") {
                var jsondata = JSON.parse(array[i]);
                var html = "<span>" + jsondata.name + "</span><span>" + jsondata.detail + "</span><img src='images/sellerimg/" + jsondata.img + "'/><input type='button' name='" + jsondata.coordx + "_" + jsondata.coordy + "' value='去看看' class='gobtn'/>";
                $("<li>" + html + "</li>").appendTo($("#disp ul"));
                i++;
            }
        },
        error: function () {
            alert("网络故障!");
        }
    });
}
;


