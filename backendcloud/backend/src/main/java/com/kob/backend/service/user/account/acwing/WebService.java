package com.kob.backend.service.user.account.acwing;

import com.alibaba.fastjson.JSONObject;

public interface WebService {
    JSONObject applyCode();//封装，更为方便的返回链接，更为方便的访问服务器的dpi
    JSONObject receiveCode(String code,String state);
}
