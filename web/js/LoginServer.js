$(document).ready(function () {
    var succ_arr = [];
    $("input[name]").focusout(function (e) {
        var msg = "";
        if ($.trim($(this).val()) === "") {
            if ($(this).attr('name') === 'userName' || $(this).attr('name') === 'userName_reg') {
                succ_arr[0] = false;
                msg = "用户名为空";
            } else if ($(this).attr('name') === 'pass' || $(this).attr('name') === 'pass_reg') {
                succ_arr[1] = false;
                msg = "密码为空";
            } else if ($(this).attr('name') === 'passagain') {
                succ_arr[2] = false;
                msg = "确认密码为空";
            }
        } else {
            if ($(this).attr('name') === 'userName') {
                succ_arr[0] = true;
            } else if ($(this).attr('name') === 'pass') {
                succ_arr[1] = true;
            }
        }
        $(this).nextAll('span').eq(0).css({display: 'block'}).text(msg);
    });
    $('.register').on("click", function () {
        $('#regis').fadeIn();
    });
    $('#cancel').on('click', function () {
        $('#regis').fadeOut();
    });
    $('#info_out').on('click', function () {
        //登出代码
        localStorage.clear();
        localStorage.setItem("IS_LOGIN", "OFF");
        swal({title:"登出成功!", timer:1000});
        $(".funcdiv").each(function () {
            if ($(this).text() === "用户") {
                $(this).text("登录");
            }
        });
        $('#userinfo').fadeOut();
        $("#model").css("visibility", "hidden");
    });
    $('#user_register').on('click', function () {
        $("input[name]").focusout();  //让所有的input标记失去一次焦点来设置msg信息
        var username = $("input[name='userName_reg']").val();
        var password = $("input[name='pass_reg']").val();
        var passagain = $("input[name='passagain']").val();
        var realname = $("input[name='realname']").val();
        var tel = $("input[name='tel']").val();
        var email = $("input[name='email']").val();
        var email_reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var username_reg = /^[a-zA-Z0-9_]{8,16}$/ ;
        var tel_reg = /^1[0-9]{10}$/;
        if (password !== passagain) {
            swal({title: "两次输入的密码不一致！", timer: 1000});
            return;
        }
        else if(!email_reg.test(email)){
            swal({title: "请输入正确的邮箱!", timer: 1000});
            return;
        }else if(!username_reg.test(username)){
            swal({title: "请输入正确的用户名!", timer: 1000});
            return;
        }else if(!tel_reg.test(tel)){
            swal({title: "请输入正确的手机号!", timer: 1000});
            return; 
        }
        $.ajax({
            type: "POST",
            url: "LoginServer",
            data: {
                type: "REGISTER",
                username: username,
                pass: password,
                realname: realname,
                tel: tel,
                email: email
            },
            success: function (data) {
                var jsondata = JSON.parse(data);
                if (jsondata.success) {
                    swal(jsondata.user + "注册成功！");
                    //其他需要的用户信息
                    $('#regis').fadeOut("slow");
                }
                else {
                    swal(jsondata.message);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert("网络故障");
            }
        });
    });
    $('#submit').click(function () {
        $("input[name]").focusout();  //让所有的input标记失去一次焦点来设置msg信息
        var username = $("input[name='userName']").val();
        var password = $("input[name='pass']").val();
        $.ajax({
            type: "POST",
            url: "LoginServer",
            data: {
                type: "LOGIN",
                username: username,
                pass: password
            },
            success: function (data) {
                var jsondata = JSON.parse(data);
                if (jsondata.success) {
                    localStorage.setItem("IS_LOGIN", "ON");
                    localStorage.setItem("USER_NAME", jsondata.username);
                    localStorage.setItem("USER_PASS", jsondata.password);
                    localStorage.setItem("USER_TEL", jsondata.tel);
                    localStorage.setItem("USER_EMAIL", jsondata.email);
                    localStorage.setItem("USER_REALNAME", jsondata.realname);
                    //其他需要的用户信息
                    swal({title:'欢迎您' + jsondata.username, timer:1000});
                    $(".funcdiv").each(function () {
                        if ($(this).text() === "登录") {
                            $(this).text("用户");
                        }
                    });
                    $('#login').fadeOut("slow");
                    $('#model').css("visibility", "hidden");
                } else {
                    localStorage.setItem("IS_LOGIN", "OFF");
                    swal("账号或密码错误！");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert("网络故障");
            }
        });
    });
});
