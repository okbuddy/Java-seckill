/**
 * Created by zhk on 17/5/26.
 */
//JS交互模块化
var seckill = {
//    封装ajax请求的URL
    URL: {
        now: function () {
            return '/seckill/time/now'
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';

        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';

        }
    },
//    验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true
        } else {
            return false
        }

    },
    //获取秒杀地址，控制显示逻辑，执行秒杀
    handleSeckillKill: function (seckillId, seckillBox) {
        //控制节点的操作需要先隐藏节点
        seckillBox.hide().html("<button id='seckillBtn' class='btn btn-primary btn-lg'>开始秒杀</button>")

        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result.success) {
                var exposer = result.data
                if (exposer.exposed) {
                    var seckillBtn = $('#seckillBtn')
                    seckillBtn.one('click', function () {
                        $(this).addClass('disabled')
                        $.post(seckill.URL.execution(seckillId, exposer.md5), {}, function (result) {
                            if (result && result.success) {
                                var execution = result.data
                                seckillBox
                                    .html('<label id="seckillMessage"' +
                                        'class="label label-success">' + execution.stateInfo + '</label>')
                            }

                        })
                    })
                //    由于用户时间和服务器时间的差别，可能产生区别
                } else if (exposer.now < exposer.begin) {
                    seckill.countdown(seckillId, exposer.begin, exposer.end, exposer.now)
                } else if (exposer.now > exposer.end) {
                    seckillBox
                        .html('<label id="seckillMessage" class="label label-success">秒杀结束</label>')

                }
            } else {
                console.log('result: ' + result)
            }

        })


        seckillBox.show()


    }
    ,
    countdown: function (seckillId, beginTime, endTime, now) {
        var seckillBox = $('#seckill-box')
        if (now > endTime) {
            seckillBox.html("秒杀结束")

        } else if (now < beginTime) {
            seckillBox.countdown(new Date(beginTime + 1000), function (event) {
                var formatTime = event.strftime('秒杀倒计时：%d day %H hour %M minute %S second')
                seckillBox.html(formatTime)
            }).on('finish.countdown', function () {
                seckill.handleSeckillKill(seckillId, seckillBox)
            })


        } else {
            seckill.handleSeckillKill(seckillId, seckillBox)
        }


    }
    ,
//    detail页的初始化
    detail: {
        init: function (params) {
            //    登录页面的手机号验证，计时交互
            var killPhone = $.cookie('killPhone')

            //    验证cookie中的手机号，如果没通过弹出弹框
            if (!seckill.validatePhone(killPhone)) {
                var killPhoneModal = $('#killPhoneModal')
                killPhoneModal.modal(
                    {
                        show: true,
                        backdrop: 'static',
                        keyboard: false
                    }
                )
                var killPhoneBtn = $('#killPhoneBtn')
                killPhoneBtn.click(
                    function () {
                        var phoneNumber = $('#killPhoneKey').val();
                        if (!seckill.validatePhone(phoneNumber)) {
                            $('#killPhoneMessage').hide()
                                .html('<label for="" class="label label-danger">手机号错误</label>')
                                .show(300)

                        } else {
                            $.cookie("killPhone", phoneNumber, {expires: 7, path: '/seckill'})
                            window.location.reload()
                        }


                    }
                )


            } else {
                var seckillId = params.seckillId
                var beginTime = params.beginTime
                var endTime = params.endTime
                $.get(seckill.URL.now(), function (result) {
                    if (result && result.success) {
                        var now = result.data
                        seckill.countdown(seckillId, beginTime, endTime, now)

                    } else {
                        console.log("result: " + result)
                    }

                })


            }

        }
    }
}