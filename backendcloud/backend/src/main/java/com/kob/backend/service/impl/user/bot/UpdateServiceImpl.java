package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> update(Map<String, String> data) {
        Map<String,String> map=new HashMap<>();
        UsernamePasswordAuthenticationToken passwordAuthenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser= (UserDetailsImpl) passwordAuthenticationToken.getPrincipal();
        User user=loginUser.getUser();
        int bot_id=Integer.parseInt(data.get("bot_id"));
        String title=data.get("title");
        String description=data.get("description");
        String content=data.get("content");
        if(title==null||title.length()==0){
            map.put("error_message","标题不能为空");
            return map;
        }
        if(title.length()>20){
            map.put("error_message","标题过长");
            return map;
        }
        if(description==null||description.length()==0){
            description="这个用户很懒什么也没留下";
        }
        if(description.length()>100){
            map.put("error_message","bot的描述过长");
            return  map;
        }
        if(content==null||content.length()==0){
            map.put("error_message","bot代码不能为空");
            return  map;
        }
        if(content.length()>10000){
            map.put("error_message","bot的代码过长");
            return  map;
        }
      Bot bot=botMapper.selectById(bot_id);
        if(bot==null){
            map.put("error_message","bot不存在");
        }
        if(!bot.getUserId().equals(user.getId())){
            map.put("error_message","没有权限修改当前bot");
            return map;
        }
        Bot new_bot=new Bot(
        bot.getId(),
        user.getId(),
        title,
        description,
        content,
        bot.getCreatetime(),
        new Date()
        );
        botMapper.updateById(new_bot);
        map.put("error_message","success");
        return map;
    }
}
