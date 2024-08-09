package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    public static ConcurrentHashMap<Integer,WebSocketServer> users=new ConcurrentHashMap<>();//存储所有链接给最后发给前端,存储每个链接对应的是谁

    //private static final CopyOnWriteArraySet<User> matchpool=new CopyOnWriteArraySet<>();
    //CopyOnWriteArraySet<User> 是 Java 中一个线程安全的集合类。它是 CopyOnWriteArraySet 的一个具体实例，其中包含 User 类型的元素。
    //ConcurrentHashMap是线程安全的哈希表
    private User user;//用户信息
    private Session session=null;//用session维护

    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private static RestTemplate restTemplate;
    private Game game=null;
    private final static String addPlayerUrl="http://127.0.0.1:7070/player/add/";
    private final static String removePlayerUrl="http://127.0.0.1:7070/player/remove/";
    @Autowired//注入sql操作,多例模式（单例模式指一个类同一时间只能有一个实例），此处每有一个新链接就有一个实例
    public void setUserMapper(UserMapper userMapper){
        //静态变量访问要用类名访问
        WebSocketServer.userMapper=userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper=recordMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate=restTemplate;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session=session;
        System.out.println("connected");
        Integer userId= JwtAuthentication.getUserId(token);
        this.user=userMapper.selectById(userId);
        if(this.user!=null){
            users.put(userId,this);
        }else {
                this.session.close();
        }
        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);
        System.out.println("disconnected");
        if(this.user!=null){
            users.remove((this.user.getId()));
        }
    }

    public static void startGame(Integer aId,Integer bId){
        User a=userMapper.selectById(aId);
        User b=userMapper.selectById(bId);
        Game game=new Game(13,14,20,a.getId(),b.getId());
        game.createMap();
        if(users.get(a.getId())!=null) users.get(a.getId()).game=game;
        if(users.get(b.getId())!=null) users.get(b.getId()).game=game;
        game.start();
        JSONObject respGame=new JSONObject();
        respGame.put("a_id",game.getPlayerA().getId());
        respGame.put("a_sx",game.getPlayerA().getSx());
        respGame.put("a_sy",game.getPlayerA().getSy());
        respGame.put("b_id",game.getPlayerB().getId());
        respGame.put("b_sx",game.getPlayerB().getSx());
        respGame.put("b_sy",game.getPlayerB().getSy());
        respGame.put("map",game.getG());
        JSONObject respA=new JSONObject();
        respA.put("event","start-matching");
        respA.put("opponent_username",b.getUsername());
        respA.put("opponent_photo",b.getPhoto());
        respA.put("game",respGame);
        if(users.get(a.getId())!=null){
        users.get(a.getId()).sendMessage(respA.toJSONString());}
        JSONObject respB=new JSONObject();
        respB.put("event","start-matching");
        respB.put("opponent_username",a.getUsername());
        respB.put("opponent_photo",a.getPhoto());
        respB.put("game",respGame);
        if(users.get(b.getId())!=null){
        users.get(b.getId()).sendMessage(respB.toJSONString());}
    }
    private void startMatching(){
        System.out.println("start");
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        restTemplate.postForObject(addPlayerUrl,data,String.class);
//        当我们需要用到某个东西的时候、就定一个它的configuration
//        加一个注解bean、返回它的实例然后就可以了
//        定义完之后、未来我们用它的时候就可以加一个Autowired就可以自动注入进来
//                加了Autowired的话
//        他就会去看一下他就会去看一下这个service这个接口是不是有唯一的加了注解bean的函数和它对应
//                如果有的话就调用一下这个函数把它的返回赋过来
//        RestTemplate：是一个工具可以来两个进程间进行通信
//                我们这里需要向后端发送一个请求
    }
    private void stopMatching(){
        System.out.println("stop");
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);
    }
    private void move(int direction){
        if(game.getPlayerA().getId().equals(user.getId())){
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())){
            game.setNextStepB(direction);
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) {//当路由就是做判断
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data=JSONObject.parseObject(message);//将json解析出来
        String event=data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        }else if("move".equals(event)){
            move(data.getInteger("direction"));
        }
    }
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    public  void  sendMessage(String message){
        synchronized (this.session){
            try{//后端从当链接发送信息,异步通信
            this.session.getBasicRemote().sendText(message);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
