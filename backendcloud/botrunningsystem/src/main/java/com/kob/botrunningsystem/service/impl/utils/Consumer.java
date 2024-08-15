package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
@Component
public class Consumer extends Thread{
    private Bot bot;
    private static RestTemplate restTemplate;
    private final static String receiveBotMove="http://127.0.0.7:9090/pk/receive/bot/move/";
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate=restTemplate;
    }
    public void startTimeout(long timeout,Bot bot){
        this.bot=bot;
        this.start();
        try {
            this.join(timeout);//等待时间达到timeout秒
        } catch (InterruptedException e) {
           e.printStackTrace();
        }finally {
            this.interrupt();//中断当前线程
        }
    }
    private String addUid(String code,String uid){//在code中的Bot类名后加uid
        int k=code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0,k)+uid+code.substring(k);
    }
    @Override
    public void run() {
        UUID uuid=UUID.randomUUID();
        String uid=uuid.toString().substring(0,8);
        BotInterface botInterface= Reflect.compile(//动态编译代码,类名相同只执行一次这是动态生成的类名，
                // 包含了包路径 com.kob.botrunningsystem.utils 和类名 Bot，后面拼接了一个 uid，这使得类名对每个用户或每个实例都是唯一的。
                "com.kob.botrunningsystem.utils.Bot"+uid,//名字段
                addUid(bot.getBotCode(),uid)//代码端
        ).create().get();
        Integer direction=botInterface.nextMove(bot.getInput());
        System.out.println("move "+bot.getUserId()+" "+direction);
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUserId().toString());
        data.add("direction",direction.toString());
        restTemplate.postForObject(receiveBotMove,data,String.class);
    }
}
