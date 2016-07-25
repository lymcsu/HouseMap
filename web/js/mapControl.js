var cursor = "";
var coordx = "";
var coordy = "";
var content = [];
var searchInfoWindow = [];
var marker = [];
var interval = 0;
var district = "";
$(document).ready(function () {
    map = new BMap.Map("container", {enableMapClick: false}); // 创建地图实例  
    map.enableScrollWheelZoom();
    map.centerAndZoom("长沙", 13);      // 初始化地图，设置中心点坐标和地图级别  
    cursor = map.getDefaultCursor();

    map.addControl(new BMap.GeolocationControl());
    function userControl() {
        // 默认停靠位置和偏移量
        this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
        this.defaultOffset = new BMap.Size(40, 110);
    }
    function myControl() {
        // 默认停靠位置和偏移量
        this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
        this.defaultOffset = new BMap.Size(40, 200);
    }
    function addseller() {
        this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
        this.defaultOffset = new BMap.Size(40, 110);
    }
    function reloadControl() {
        // 默认停靠位置和偏移量
        this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
        this.defaultOffset = new BMap.Size(40, 200);
    }
    function searchseller() {
        this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
        this.defaultOffset = new BMap.Size(40, 290);
    }
    var mapType2 = new BMap.MapTypeControl({anchor: BMAP_ANCHOR_TOP_LEFT});
    userControl.prototype = new BMap.Control();
    myControl.prototype = new BMap.Control();
    addseller.prototype = new BMap.Control();
    reloadControl.prototype = new BMap.Control();
    searchseller.prototype = new BMap.Control();
    userControl.prototype.initialize = function (map) {
        // 创建一个DOM元素
        var div = document.createElement("div");
        div.className = 'funcdiv';
        // 添加文字说明
        if (localStorage.getItem("IS_LOGIN") === "ON") {
            div.appendChild(document.createTextNode("用户"));
        } else {
            div.appendChild(document.createTextNode("登录"));
        }
        div.onclick = function (e) {
            if (localStorage.getItem("IS_LOGIN") === "OFF" || localStorage.getItem("IS_LOGIN") === null) {
                $('#login').fadeIn("slow");
                $('#model').css("visibility", "visible");
            } else {
                //用户信息弹框，可编辑
                $('#user_name').html(localStorage.getItem("USER_NAME"));
                $('#user_realname').attr("value", localStorage.getItem("USER_REALNAME"));
                $('#user_tel').attr("value", localStorage.getItem("USER_TEL"));
                $('#user_email').attr("value", localStorage.getItem("USER_EMAIL"));
                $('#userinfo').fadeIn("slow");
                $('#model').css("visibility", "visible");
            }
        };
        map.getContainer().appendChild(div);
        // 将DOM元素返回
        return div;
    };
    myControl.prototype.initialize = function (map) {
        // 创建一个DOM元素
        var div = document.createElement("div");
        div.className = 'funcdiv';
        // 添加文字说明
        div.appendChild(document.createTextNode("我的"));
        div.onclick = function (e) {
            if (localStorage.getItem("IS_LOGIN") === "ON") {
                loadmyadd();
                loadmycomment();
                loadmyclc();
                $("#myadd").fadeIn();
                $('#model').css("visibility", "visible");
            } else {
                swal({title: "请先登录!", text: "  ", timer: 1000});
            }
        };
        map.getContainer().appendChild(div);
        // 将DOM元素返回
        return div;
    };
    reloadControl.prototype.initialize = function (map) {
        // 创建一个DOM元素
        var div = document.createElement("div");
        div.className = 'funcdiv';
        div.style.fontSize = "80%";
        // 添加文字说明
        div.appendChild(document.createTextNode("刷新地图"));
        div.onclick = function (e) {
            if (localStorage.getItem("IS_LOGIN") === "ON") {
                loadseller();
            }
            else {
                swal({title: "请先登录!", text: "  ", timer: 1000});
            }
        }
        ;
        map.getContainer().appendChild(div);
        // 将DOM元素返回
        return div;
    };
    addseller.prototype.initialize = function (map) {
        var div = document.createElement("div");
        div.className = 'funcdiv';
        div.style.fontSize = "80%";
        // 添加文字说明
        div.appendChild(document.createTextNode("添加摊位"));
        // 绑定事件,点击一次放大两级
        div.onclick = function (e) {
            if (localStorage.getItem("IS_LOGIN") === "ON") {
                $(".notice").fadeIn().css("font-size", "30px");
                setTimeout(function () {
                    $(".notice").css("font-size", "20px").fadeOut();
                }, 1000);
                map.removeEventListener('click', showadd);
                map.setDefaultCursor("pointer");
                map.addEventListener('click', showadd);
            } else {
                swal({title: "请先登录!", text: "  ", timer: 1000});
            }
        };
        map.getContainer().appendChild(div);
        // 将DOM元素返回
        return div;
    };
    searchseller.prototype.initialize = function (map) {
        var div = document.createElement("div");
        div.className = 'funcdiv';
        div.style.fontSize = "80%";
        // 添加文字说明
        div.appendChild(document.createTextNode("查找摊位"));
        div.onclick = function (e) {
            if (localStorage.getItem("IS_LOGIN") === "ON") {
                $("#searchdiv").fadeIn();
                $("#model").css("visibility", "visible");
            }
            else {
                swal({title: "请先登录!", text: "  ", timer: 1000});
            }
        };
        map.getContainer().appendChild(div);
        // 将DOM元素返回
        return div;
    };

    var myCtrl = new myControl();
    var userCtrl = new userControl();
    var sellerCtrl = new addseller();
    var reloadCtrl = new reloadControl();
    var searchCtrl = new searchseller();
// 添加到地图当中
    map.addControl(userCtrl);
    map.addControl(mapType2);
    map.addControl(myCtrl);
    map.addControl(sellerCtrl);
    map.addControl(reloadCtrl);
    map.addControl(searchCtrl);
    $('.close').on('click', function () {
        $('#userinfo').fadeOut();
        $('#detail').fadeOut();
        $("#myadd").fadeOut();
        $("#login").fadeOut();
        $("#searchdiv").fadeOut();
        $("#model").css("visibility", "hidden");
        window.clearInterval(interval);
    });
    $('#model').on('click', function () {
        if ($("#regis").css("display") === "block")
            return;
        $('#userinfo').fadeOut();
        $("#myadd").fadeOut();
        $("#login").fadeOut();
        $("#searchdiv").fadeOut();
        hideadd();
        $("#model").css("visibility", "hidden");
        window.clearInterval(interval);
    });
    $("#info_save").on("click", modifyinfo);
    loadseller();
});
function showadd(ev) {
    $('#input').show("slow");
    $('#model').css("visibility", "visible");
    coordx = ev.point.lng;
    coordy = ev.point.lat;
    var pt = ev.point;
    var geoc = new BMap.Geocoder();
    geoc.getLocation(pt, function (rs) {
        var addComp = rs.addressComponents;
        district = addComp.district;
    });
}
function hideadd() {
    $('#input').hide("slow");
    $('#model').css("visibility", "hidden");
    map.removeEventListener('click', showadd);
    map.setDefaultCursor(cursor);
}
function loadmyadd() {
    $.ajax({
        url: "LoadInfo",
        type: "GET",
        data: {
            type: "LOAD_ADD",
            username: localStorage.getItem("USER_NAME")
        },
        success: function (data) {
            var str = data.substring(1, data.length - 3);
            if (str === "") {
                $("#tab_add ul").html("<li style='border:none;color: #0099cc'><p style='position:absolute;top:50%;left:40%'>您尚未添加摊位！<br/>去发现，去爱！</p></li>");
                return;
            }
            $("#tab_add ul").html("<li style='border:none;color: #0099cc'><p class='show_name' style='color:inherit;'>摊点名称</p><p class='show_detail'>摊点详情</p><p class='show_loca'>摊点位置</p></li>");
            var myadd = str.split(",");
            for (var i = 0; i < myadd.length; i++) {
                var myaddinfo = myadd[i].split("_");
                var html = "<p class='show_name'>" + myaddinfo[0] + "</p><p class='show_detail'>" + myaddinfo[1] + "</p><p class='show_loca'>" + myaddinfo[2] + "</p><p class='add_delete'>删除</p>"
                $("<li>" + html + "</li>").appendTo($("#tab_add ul"));
            }
            deletefunc("add_delete", "DELETE_ADD");
        },
        error: function () {
            swal("网络故障！");
        }
    });
}
function loadmycomment() {
    $.ajax({
        url: "LoadInfo",
        type: "GET",
        data: {
            type: "LOAD_COMMENT",
            username: localStorage.getItem("USER_NAME")
        },
        success: function (data) {
            var str = data.substring(1, data.length - 3);
            if (str === "") {
                $("#tab_comment ul").html("<li style='border:none;color: #0099cc'><p style='position:absolute;top:50%;left:40%'>您尚未进行评论！<br/>去发声！</p></li>");
                return;
            }
            $("#tab_comment ul").html("<li style='border:none;color: #0099cc'><p class='show_name' style='color:inherit;'>摊点名称</p><p class='show_detail'>我的点评</p></li>");
            var mycomment = str.split(",");
            for (var i = 0; i < mycomment.length; i++) {
                var mycommentinfo = mycomment[i].split("_");
                var html = "<p class='show_name'>" + mycommentinfo[1] + "</p><p class='show_detail'>" + mycommentinfo[2] + "</p><p class='comment_delete'>删除</p>"
                $("<li id='" + mycommentinfo[0] + "'>" + html + "</li>").appendTo($("#tab_comment ul"));
            }
            deletefunc("comment_delete", "DELETE_COMMENT");
        },
        error: function () {
            swal("网络故障，删除失败!");
        }
    });
}
function loadmyclc() {
    $.ajax({
        url: "CellectionServer",
        type: "GET",
        data: {
            type: "LOAD_CLC",
            username: localStorage.getItem("USER_NAME")
        },
        success: function (data) {
            var str = data.substring(1, data.length - 1);
            if (str === "") {
                $("#tab_store ul").html("<li style='border:none;color: #0099cc'><p style='position:absolute;top:50%;left:40%'>快去收藏喜欢的摊位吧！</p></li>");
                return;
            }
            $("#tab_store ul").html("<li style='border:none;color: #0099cc'><p class='show_name' style='color:inherit;'>摊点名称</p></li>");
            var myclc = str.split(",");
            for (var i = 0; i < myclc.length; i++) {
                var html = "<p class='show_name'>" + myclc[i] + "</p><p class='geoloca'>定位</p>";
                $("<li>" + html + "</li>").appendTo($("#tab_store ul"));
            }
            $(".geoloca").on("click", function () {
                var seller = $(this).prev().html();
                $.ajax({
                    url: "LoadSeller",
                    type: "GET",
                    data: {
                        seller_name: seller,
                        server_type: "GET_COORD"
                    },
                    success: function (data) {
                        var jsondata = JSON.parse(data);
                        map.centerAndZoom(new BMap.Point(jsondata.coordx, jsondata.coordy), 20);
                        $("#myadd").fadeOut();
                        $("#model").css("visibility", "hidden");
                    },
                    error: function () {
                        swal("网络故障!");
                    }
                });
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            swal("网络故障");
        }
    });
}
function add_seller() {
    var name = $("input[name='seller_name']").val();
    var detail = $("input[name='detail']").val();
    var type = $("input[name='type']:checked").val();
    var price = $("input[name='price']").val();
    var location = $("input[name='location']").val();
    $.ajax({
        type: 'POST',
        url: 'UploadServer',
        data: {
            image: image,
            name: name,
            detail: detail,
            type: type,
            price: price,
            location: location,
            coordx: coordx,
            coordy: coordy,
            district: district,
            username: localStorage.getItem("USER_NAME")
        },
        success: function (data) {
            if (data.success) {
                swal("上传成功!", "Thanks for your adding!", "success");
                hideadd();
                var content = '<div style="margin:0;line-height:20px;padding:2px;">' +
                        '<img src="' + image + '" alt="" style="float:right;zoom:1;overflow:hidden;width:100px;height:100px;margin-left:3px;"/>' +
                        detail + "<a onclick='showdetail()' href='#' style='cursor:pointer;float:right'>详情></a>" +
                        '</div>';
                var searchInfoWindow = null;
                searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
                    title: name, //标题
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
                var marker = new BMap.Marker(new BMap.Point(coordx, coordy)); //创建marker对象
                map.addOverlay(marker);
                marker.addEventListener("click", function (e) {
                    searchInfoWindow.open(marker);
                    sessionStorage.setItem("INFO_NAME", searchInfoWindow.getTitle());
                });
            } else {
                swal("上传失败!", "error");
            }
        },
        error: function (err) {
            alert('网络故障');
        }
    });
}
function modifyinfo() {
    var rn = $('#user_realname').val();
    var tel = $('#user_tel').val();
    var email = $('#user_email').val();
    $.ajax({
        url: "LoginServer",
        type: "POST",
        data: {
            realname: rn,
            tel: tel,
            email: email,
            type: "MODIFY",
            username: localStorage.getItem("USER_NAME")
        },
        success: function (data) {
            if (data === "true") {
                swal("修改成功!");
            } else {
                swal("修改失败!");
            }
        },
        error: function () {
            alert("网络故障!");
        }
    });
}
function deletefunc(btntype, deltype) {
    $("." + btntype).on("click", function () {
        var id = $(this).parent().attr("id");
        var this_li = $(this).parent();
        var this_name = $(this).parent().children("p").get(0).innerHTML;
        swal({
            title: "确定删除吗?",
            text: "删除后不能恢复",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "删除",
            closeOnConfirm: false},
        function () {

            $.ajax({
                url: "LoadInfo",
                type: "GET",
                data: {
                    type: deltype,
                    username: localStorage.getItem("USER_NAME"),
                    sellername: this_name,
                    id: id
                },
                success: function (data) {
                    var jsondata = JSON.parse(data);
                    if (jsondata.success) {
                        this_li.remove();
                        $("#dialog").fadeOut("fast");
                        swal("删除成功!", "Deleted!", "success");
                        loadseller();
                        return;
                    }
                    else {
                        sweetAlert("删除失败", "Something went wrong!", "error");
                        $("#dialog").fadeOut("fast");
                        return;
                    }
                },
                error: function () {
                    alert("网络故障，删除失败!");
                }
            });
        });
    });
}
