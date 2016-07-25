function loadseller() {
    map.clearOverlays();
    $.ajax({
        type: 'GET',
        url: 'LoadSeller',
        data: {
            server_type: "LOAD_SELLER"
        },
//        dataType: 'json',
        success: function (data) {
            var str = data.substring(1,data.length-3);
            var sellerarray = str.split(",");
            for (var k = 0; k < sellerarray.length; k++) {
                content[k] = null;
                searchInfoWindow[k] = null;
                marker[k] = null;
                sellerinfo = sellerarray[k].split("_");
                content[k] = '<div style="margin:0;line-height:20px;padding:2px;">' +
                        '<img src="images/sellerimg/' + sellerinfo[2] + '" alt="" style="float:right;zoom:1;overflow:hidden;width:100px;height:100px;margin-left:3px;"/>' +
                        sellerinfo[1] + "<a onclick='showdetail()' href='#' style='cursor:pointer;float:right'>详情></a>" +
                        '</div>';
//                searchInfoWindow = null;
                searchInfoWindow[k] = new BMapLib.SearchInfoWindow(map, content[k], {
                    title: sellerinfo[0], //标题
                    width: 290, //宽度
                    height: 105, //高度
                    enableSendToPhone: false,
                    panel: "panel", //检索结果面板
                    enableAutoPan: true, //自动平移
                    searchTypes: [
                        BMAPLIB_TAB_TO_HERE, //到这里去
                        BMAPLIB_TAB_FROM_HERE //从这里出发
                    ]
                });
                marker[k] = new BMap.Marker(new BMap.Point(sellerinfo[3], sellerinfo[4]));
                marker[k].addEventListener("click", function () {
                    var num = marker.indexOf(this);
                    searchInfoWindow[num].open(this);
                    sessionStorage.setItem("INFO_NAME", searchInfoWindow[num].getTitle());
                });
                map.addOverlay(marker[k]);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("网络故障!");
        }
    })
}
;
