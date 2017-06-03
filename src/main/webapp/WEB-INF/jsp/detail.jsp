<%--
  Created by IntelliJ IDEA.
  User: zhk
  Date: 17/5/25
  Time: 上午11:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>秒杀详情页</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%@include file="common/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-primary text-center">
        <div class="panel-heading">
            <h1>${seckill.name}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">
                <span class="glyphicon glyphicon-time"></span>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>
<div class="modal fade" id="killPhoneModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="text-center">
                    <span class="glyphicon glyphicon-phone"></span>秒杀手机号

                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-offset-2 col-xs-8">

                        <input name="killPhone" id="killPhoneKey" type="text" class="form-control"
                               placeholder="phone number">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <span class="glyphicon" id="killPhoneMessage"></span>
                <button id="killPhoneBtn" type="button" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>Submit
                </button>


            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.js"></script>
<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<script src="/resources/script/seckill.js"></script>
<script>
    $(function () {
        seckill.detail.init({
            seckillId:${seckill.seckillId},
            beginTime:${seckill.beginTime.time},
            endTime:${seckill.endTime.time}
        });
    })
</script>

</body>
