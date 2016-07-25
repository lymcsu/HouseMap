
function showdetail() {
    window.clearInterval(interval);
    $(".send .s_btn").on("click", uploadcomment);
    $.ajax({
        url: "LoadSeller",
        type: "GET",
        data: {
            seller_name: sessionStorage.getItem("INFO_NAME"),
            server_type: "LOAD_DETAIL"
        },
        success: function (data) {
            var jsondata = JSON.parse(data);
            $('.sellerinfo').find('p').html(jsondata.detail);
            $('#price').html(jsondata.price);
            $("#star1").remove();
            $("<div id='star1' class='my-rating' style='position: absolute;top: 56px;left: 100px;'></div>").appendTo($("#sellerinfo")).starRating({
                starSize: 25,
                initialRating: parseFloat(jsondata.rate),
                readOnly: true
            });
            $('#detail').find('img').attr("src", "images/sellerimg/" + jsondata.img);
            $("#star2").remove();
            $("<div id='star2' class='my-rating' style='position: absolute;top: 96px;left: 100px;'></div>").appendTo($("#sellerinfo")).starRating({
                starSize: 25,
                callback: function (currentRating, $el) {
                    updaterate(currentRating);
                }
            });
            $('#location').html(jsondata.location);
            $("#cellection").off("click");
            $('#cellection').on("click", function () {
                if (localStorage.getItem("IS_LOGIN") !== "ON") {
                    swal({title: "请先登录!"});
                    return;
                }
                if ($(this).attr("class") === "clcactive") {
                    $(this).attr("class", "");
                    deleteclc();
                } else {
                    $(this).attr("class", "clcactive");
                    addclc();
                }
            });
            isclc();
            $('#detail').fadeIn().css("display", "flex");
            $('#model').css("visibility", "visible").click(function () {
                $('#detail').fadeOut("fast");
                $(this).css("visibility", "hidden");
            });
        },
        error: function () {
            alert("网络故障!");
        }
    });
    loadcomment();
}
function loadcomment() {
    $.ajax({
        url: "CommentServer",
        type: "GET",
        data: {
            sellername: sessionStorage.getItem("INFO_NAME"),
            type: "LOAD_COMMENT"
        },
        success: function (data) {
            if (data === "[]") {
                $(".judgement ul").html("");
                $(".mask").html("");
                $("<span>还没有评论哦！<span>").appendTo($(".judgement ul"));
                return;
            }
            var str = data.substring(1, data.length - 1);
            var commentarray = str.split(",");
            $(".mask").html("");
            $(".judgement ul").html("");
            for (var j = 0; j < commentarray.length; j++) {
                var commentinfo = commentarray[j].split("_");
                var _lable = $("<div style='right:20px;top:0px;opacity:1;color:#fff;'>" + commentinfo[1] + "</div>");
                var _li = $("<li><p><b>" + commentinfo[0] + ":</b>" + commentinfo[1] + "</p></li>");
                $(".mask").append(_lable.hide());
                $(".judgement ul").append(_li);
            }
            init_barrage();
        }
    });
    interval = window.setInterval(init_barrage, 11000);
}
function updaterate(rate) {
    $.ajax({
        url: "LoadSeller",
        type: "GET",
        data: {
            server_type: "UPDATERATE",
            userrate: rate,
            seller_name: sessionStorage.getItem("INFO_NAME")
        },
        success: function (data) {
            var jsondata = JSON.parse(data);
            swal("评分成功!", "Thanks for your rating!", "success")
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("网络故障");
        }
    });
}
function addclc() {
    $.ajax({
        url: "CellectionServer",
        type: "GET",
        data: {
            type: "ADD_CLC",
            username: localStorage.getItem("USER_NAME"),
            sellername: sessionStorage.getItem("INFO_NAME")
        },
        success: function (data) {
            swal(data, "Success!", "success");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("网络故障");
        }
    });
}
function deleteclc() {
    $.ajax({
        url: "CellectionServer",
        type: "GET",
        data: {
            type: "CANCEL_CLC",
            username: localStorage.getItem("USER_NAME"),
            sellername: sessionStorage.getItem("INFO_NAME")
        },
        success: function (data) {
            swal(data, "Cancel!", "error");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("网络故障");
        }
    });
}
function isclc() {
    $.ajax({
        url: "CellectionServer",
        type: "GET",
        data: {
            type: "IS_CLC",
            username: localStorage.getItem("USER_NAME"),
            sellername: sessionStorage.getItem("INFO_NAME")
        },
        success: function (data) {
            if (data === 'false') {
                $("#cellection").attr("class", "");
            } else if (data === 'true') {
                $("#cellection").attr("class", "clcactive");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("网络故障");
        }
    });
}