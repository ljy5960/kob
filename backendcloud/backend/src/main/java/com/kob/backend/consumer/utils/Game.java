package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private final int[][] g;
    private final static int[] dx = {-1, 0, 1, 0};
    private final static int[] dy = {0, 1, 0, -1};
    private final Player playerA;
    private final Player playerB;
    //在Java中，final关键字用于声明常量，表示该变量一旦初始化后就不能被修改。
    // 对于类中的final成员变量，它们必须在声明时或在构造方法中进行初始化。一旦在构造方法中初始化之后，就不能再被修改。
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private final ReentrantLock lock = new ReentrantLock();
    private String status = "playing";//存储整个游戏状态playing表示正在进行，finished表示游戏结束
    private String loser = "";//all平局，A：A输，B：B输
    private final static String addBoturl="http://127.0.0.1:6060/bot/add/";

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Bot botA, Integer idB,Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        Integer botIdA=-1;
        Integer botIdB=-1;
        String botCodeA=null;
        String botCodeB=null;
        if(botA!=null){
            botIdA=botA.getId();
            botCodeA=botA.getContent();
        }
        if(botB!=null){
            botIdB=botB.getId();
            botCodeB=botB.getContent();
        }
        playerA = new Player(idA,botIdA,botCodeA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB,botIdB,botCodeB, 1, cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public int[][] getG() {
        return g;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;
        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    private boolean draw() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                g[i][j] = 0;
            }
        }
        for (int r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }
        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) {
                    continue;
                }
                if (r == this.rows - 2 && c == 1 || c == this.cols - 2 && r == 1) continue;
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) break;
        }
    }
    private String getInput(Player player){//将当前的局面信息编码成字符串
     //地图#me.sx#sy#操作，#对手的起始坐标#对手操作
        Player me,you;
        if(playerA.getId().equals(player.getId())){
            me=playerA;
            you=playerB;
        }else {
            me=playerB;
            you=playerA;
        }
        return getMapString()+"#"+me.getSx()+"#"+me.getSy()+"#("+me.getStepsString()+")#"+
                you.getSx()+"#"+you.getSy()+"#("+you.getStepsString()+")";
    }
    private void sendBotCode(Player player){
    if(player.getBotId().equals(-1)) return;
    MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
    data.add("user_id",player.getId().toString());
    data.add("bot_code",player.getBotCode());
    data.add("input",getInput(player));
    WebSocketServer.restTemplate.postForObject(addBoturl,data,String.class);
    }

    private boolean nextstep() {//等待两名玩家的下一步操作
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendBotCode(playerA);
        sendBotCode(playerB);
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    private void sendResult(){//向两个client公布结果
        JSONObject resp=new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());

    }
    private void judge(){//判断两名玩家下一步操作是否合法
        List<Cell> cellsA=playerA.getCells();
        List<Cell> cellsB=playerB.getCells();
        boolean validA=check_valid(cellsA,cellsB);
        boolean validB=check_valid(cellsB,cellsA);
        if(!validA||!validB){
            status="finished";
            if(!validA&&!validB){
                loser="all";
            }else if(!validA){
                loser="A";
            }else{
                loser="B";
            }
        }
    }
    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){
        int n=cellsA.size();
        Cell cell=cellsA.get(n-1);
        if(g[cell.x][cell.y]==1) return false;
        for(int i=0;i<n-1;i++){
            if(cell.x==cellsA.get(i).x&&cell.y==cellsA.get(i).y) return false;
        }
        for(int i=0;i<n-1;i++){
            if(cell.x==cellsB.get(i).x&&cell.y==cellsB.get(i).y) return false;
        }
        return true;
    }
    private void sendMove(){//向两个client传递移动信息
        lock.lock();
        try {
            JSONObject resp=new JSONObject();
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA=nextStepB=null;
        } finally {
            lock.unlock();
        }
    }
    private void sendAllMessage(String message){
        if(WebSocketServer.users.get(playerA.getId())!=null){
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);}
        if(WebSocketServer.users.get(playerB.getId())!=null){
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);}
    }
    private String getMapString(){
        StringBuilder res=new StringBuilder();
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }
    public void updateUserRating(Player player,Integer rating){
        User user=WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }
    private void saveToDatabase(){
        Integer ratingA=WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer ratingB=WebSocketServer.userMapper.selectById(playerB.getId()).getRating();
        if("A".equals(loser)){
            ratingA-=2;
            ratingB+=5;
        }else if("B".equals(loser)){
            ratingA+=5;
            ratingB-=2;
        }
        updateUserRating(playerA,ratingA);
        updateUserRating(playerB,ratingB);
    Record record =new Record(
            null,
            playerA.getId(),
            playerA.getSx(),
            playerA.getSy(),
            playerB.getId(),
            playerB.getSx(),
            playerB.getSy(),
            playerA.getStepsString(),
            playerB.getStepsString(),
            getMapString(),
            loser,
            new Date()
    );
       WebSocketServer.recordMapper.insert(record);
    }
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 1000; i++) {
            if (nextstep()) {//是否获取了两条蛇的下一步操作
                judge();
                if(status.equals("playing")){
                sendMove();
                }else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
