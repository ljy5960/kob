package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
//多线程注入类写component
@Component
public class MatchingPool extends Thread{
    private static List<Player> players=new ArrayList<>();
    private final ReentrantLock lock=new ReentrantLock();
    private static RestTemplate restTemplate;
    private static final String startGameUrl="http://127.0.0.1:9090/pk/start/game/";
    @Autowired
    public void getRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate=restTemplate;
    }
    public void addPlayer(Integer userIid,Integer  rating,Integer botId){
        lock.lock();
       try {
           players.add(new Player(userIid,rating,botId,0));
       }finally {
           lock.unlock();
       }
    }
    public void removePlayer(Integer userId){
        lock.lock();
        try {
            List<Player> newPlayers=new ArrayList<>();
            for(Player player:players){
                if(!player.getUserId().equals(userId)){
                   newPlayers.add(player);
                }
            }
            players=newPlayers;
        }finally {
            lock.unlock();
        }
    }
    private void increaseWaitingTime(){//将所有玩家的等待时间加一
        for(Player player:players){
            player.setWaitingTime(player.getWaitingTime()+1);
        }
    }
    private boolean checkMatched(Player a,Player b){//判断两名玩家是否匹配
        int ratingDelta=Math.abs(a.getRating()- b.getRating());
        int waitingTime=Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta<=waitingTime*10;
    }
    private void sendResult(Player a,Player b){//返回a，b的匹配结果
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("a_id",a.getUserId().toString());
        data.add("a_bot_id",a.getBotId().toString());
        data.add("b_id",b.getUserId().toString());
        data.add("b_bot_id",b.getBotId().toString());
        restTemplate.postForObject(startGameUrl,data,String.class);
    }
    private void matcherPlayers(){//尝试匹配所有玩家
        boolean[]used=new boolean[players.size()];
        for(int i=0;i<players.size();i++){
            if(used[i]) continue;
            for(int j=i+1;j<players.size();j++){
                if(used[j]) continue;;
                Player a=players.get(i);
                Player b=players.get(j);
                if(checkMatched(a,b)){
                    used[i]=used[j]=true;
                    sendResult(a,b);
                    break;
                }
            }
        }
        List<Player> newPlayer=new ArrayList<>();
        for(int i=0;i<players.size();i++){
            if(!used[i]){
                newPlayer.add(players.get(i));
            }
        }
        players=newPlayer;
    }
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matcherPlayers();
                }finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
              e.printStackTrace();
              break;
            }

        }
    }
}
